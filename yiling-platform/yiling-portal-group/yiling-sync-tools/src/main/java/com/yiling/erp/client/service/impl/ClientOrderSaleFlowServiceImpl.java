package com.yiling.erp.client.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yiling.erp.client.common.Constants;
import com.yiling.erp.client.dao.TaskConfigDao;
import com.yiling.erp.client.enums.FileTypeEnum;
import com.yiling.erp.client.service.SyncTaskService;
import com.yiling.erp.client.util.CacheTaskUtil;
import com.yiling.erp.client.util.DateUtil;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.erp.client.util.InitErpSysData;
import com.yiling.erp.client.util.OssFileInfo;
import com.yiling.erp.client.util.OssManager;
import com.yiling.erp.client.util.Utils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpFlowControlDTO;
import com.yiling.open.erp.dto.ErpSaleFlowDTO;
import com.yiling.open.erp.dto.TaskConfig;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OperTypeEnum;
import com.yiling.open.erp.util.OpenConstants;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 销售订单流向
 * @author: houjie.sun
 * @date: 2021/9/22
 */
@Slf4j
@Service("clientOrderSaleFlowService")
public class ClientOrderSaleFlowServiceImpl extends InitErpSysData implements SyncTaskService {

    @Autowired
    private InitErpConfig       initErpConfig;
    @Autowired
    private OssManager ossManager;
    @Autowired
    private TaskConfigDao       taskConfigDao;

    @Override
    public void syncData() {
        Long startTime=System.currentTimeMillis();
        try {
            CacheTaskUtil.getInstance().addCacheData(ErpTopicName.ErpSaleFlow.getTopicName());
            log.info("[ERP销售订单流向信息同步]，任务开始 time -> {}", DateUtil.convertDate2String(new Date(), Constants.FORMATE_DAY_TIME));
            this.initDataBase(ErpTopicName.ErpSaleFlow.getTopicName());
            // sqlite缓存的数据
            Map<String, String> sqliteData = this.findSqliteData(ErpTopicName.ErpSaleFlow.getTopicName());
            // 查询数据
            List<BaseErpEntity> dataList = this.findDataAll(ErpTopicName.ErpSaleFlow.getMethod());
            if (CollUtil.isEmpty(dataList)) {
                log.error("[ERP销售订单流向信息同步] 查询时间范围内的中间表数据为空");
                return;
            }

            List<ErpSaleFlowDTO> erpSaleFlowList = PojoUtils.map(dataList, ErpSaleFlowDTO.class);
            // so_time 字段校验
            List<ErpSaleFlowDTO> poTimeNullList = erpSaleFlowList.stream().filter(d -> ObjectUtil.isNull(d.getSoTime())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(poTimeNullList)) {
                log.error("[ERP销售订单流向信息同步] 字段 so_time 有为空的,请检查 so_time 字段");
                return;
            }
            // enterprise_inner_code 字段校验
            List<ErpSaleFlowDTO> enterpriseInnerCodeNullList = erpSaleFlowList.stream().filter(d -> StrUtil.isBlank(d.getEnterpriseInnerCode()))
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(enterpriseInnerCodeNullList)) {
                log.error("[ERP销售订单流向信息同步] 字段 enterprise_inner_code 有为空的,请检查 enterprise_inner_code 字段");
                return;
            }
            // goods_in_sn 字段校验
            List<ErpSaleFlowDTO> goodsInnerSnNullList = erpSaleFlowList.stream().filter(d -> StrUtil.isBlank(d.getGoodsInSn()))
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(goodsInnerSnNullList)) {
                log.error("[ERP销售订单流向信息同步] 字段 goods_in_sn 有为空的,请检查 goods_in_sn 字段");
                return;
            }

            // 价格格式化，去除小数后面的0
            for (ErpSaleFlowDTO erpSaleFlowDTO : erpSaleFlowList) {
                BigDecimal price = erpSaleFlowDTO.getSoPrice().stripTrailingZeros();
                BigDecimal quantity = erpSaleFlowDTO.getSoQuantity().stripTrailingZeros();
                erpSaleFlowDTO.setSoPrice(price);
                erpSaleFlowDTO.setSoQuantity(quantity);
            }

            // 数据按照部门分组
            Map<String, List<ErpSaleFlowDTO>> deptMap = erpSaleFlowList.stream().collect(Collectors.groupingBy(e -> e.getSuDeptNo()));
            log.debug("[ERP销售订单流向信息同步] 部门:" + deptMap.keySet());

            // 每个部门的数据再按照日期分组处理
            for (Map.Entry<String, List<ErpSaleFlowDTO>> entry : deptMap.entrySet()) {
                String suDeptNo = entry.getKey();
                List<ErpSaleFlowDTO> deptList = entry.getValue();

                // 数据按照日期分组
                Map<String, List<ErpSaleFlowDTO>> dateMap = deptList.stream()
                    .collect(Collectors.groupingBy(e -> DateUtil.convertDate2String(e.getSoTime(), Constants.FORMATE_DAY)));
                // 查询日期范围，天数从当天开始向前推算
                List<String> dateStrList = getListByDate(dateMap);
                log.debug("[ERP销售订单流向信息同步] 部门:" + suDeptNo + ", 查询日期:" + dateStrList.get(0) + "至" + dateStrList.get(dateStrList.size() - 1));

                // 待同步数据
                List<ErpFlowControlDTO> addList = new ArrayList<>();
                List<ErpFlowControlDTO> updateList = new ArrayList<>();

                // 每天的数据进行处理
                boolean handlerFlag = handlerByDate(sqliteData, suDeptNo, dateMap, dateStrList, addList, updateList);
                if (!handlerFlag) {
                    return;
                }

                // 同步数据
                if (CollUtil.isNotEmpty(addList)) {
                    log.debug("[ERP销售订单流向信息同步]，发送消息，上传ERP销售订单流向数据压缩包，新增");
                    List<BaseErpEntity> syncAddList = PojoUtils.map(addList, BaseErpEntity.class);
                    this.sendData(syncAddList, initErpConfig.getSysConfig(), ErpTopicName.ErpSaleFlow, OperTypeEnum.ADD.getCode());
                }
                if (CollUtil.isNotEmpty(updateList)) {
                    log.debug("[ERP销售订单流向信息同步]，发送消息，上传ERP销售订单流向数据压缩包，更新");
                    List<BaseErpEntity> syncUpdateList = PojoUtils.map(updateList, BaseErpEntity.class);
                    this.sendData(syncUpdateList, initErpConfig.getSysConfig(), ErpTopicName.ErpSaleFlow, OperTypeEnum.UPDATE.getCode());
                }

            }
        } catch (Exception e) {
            log.error(MessageFormat.format(Constants.SYNC_EXCEPTION_NAME, System.currentTimeMillis()-startTime,"[ERP销售订单流向信息同步]", e.toString()));
        } finally {
            // 清空临时文件
            Utils.clearDirectory(InitErpConfig.ERP_SALE_FLOW_PATH);
            CacheTaskUtil.getInstance().removeCacheData(ErpTopicName.ErpSaleFlow.getTopicName());
        }
    }

    private boolean handlerByDate(Map<String, String> sqliteData, String suDeptNo, Map<String, List<ErpSaleFlowDTO>> handlerMap,
                                  List<String> dateStrList, List<ErpFlowControlDTO> addList, List<ErpFlowControlDTO> updateList) throws Exception {
        for (String time : dateStrList) {
            List<ErpSaleFlowDTO> list = handlerMap.getOrDefault(time,new ArrayList<>());
            String jsonList = JSON.toJSONString(list);
            String sign=list.stream().map(e->e.getErpPrimaryKey()).collect(Collectors.joining());
            String md5 = SecureUtil.md5(sign);
            // 校验md5
            String md5Cash = sqliteData.get(this.getSuDeptNoKey(time,suDeptNo));
            if (ObjectUtil.equal(md5, md5Cash)) {
                continue;
            }

            // 写入文件
            String textPath = InitErpConfig.ERP_SALE_FLOW_PATH + File.separator + time + ".txt";
            FileUtil.writeUtf8String(jsonList, textPath);
            // 压缩文件
            String zipPath = InitErpConfig.ERP_SALE_FLOW_PATH + File.separator + time + ".zip";
            ZipUtil.zip(textPath, zipPath);
            // 获取md5
            File file = new File(zipPath);


            // 上传压缩包
            Long suId = initErpConfig.getSysConfig().getSuId();
            String dateDir = time.replace("-", "/");
            OssFileInfo fileInfo = ossManager.flowUpload(file, FileTypeEnum.ERP_SALE_FLOW,initErpConfig.getSysConfig().getEnvName(), String.valueOf(suId), suDeptNo, dateDir);
            if(fileInfo==null){
                return false;
            }
            // 组装待同步数据
            ErpFlowControlDTO erpFlowControl;
            if (StringUtils.isBlank(md5Cash)) {
                erpFlowControl = buildErpFlowControl(time, md5, Long.valueOf(suId), suDeptNo, fileInfo, OperTypeEnum.ADD.getCode());
                addList.add(erpFlowControl);
            } else {
                erpFlowControl = buildErpFlowControl(time, md5, Long.valueOf(suId), suDeptNo, fileInfo, OperTypeEnum.UPDATE.getCode());
                updateList.add(erpFlowControl);
            }
        }
        return true;
    }

    private List<String> getListByDate(Map<String, List<ErpSaleFlowDTO>> handlerMap) throws Exception {
        List<TaskConfig> taskConfigList = taskConfigDao.executeQueryTaskConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME,
            "select * from task_config where taskNo = " + ErpTopicName.ErpSaleFlow.getMethod());
        int flowDateCount = Integer.parseInt(taskConfigList.get(0).getFlowDateCount());
        // 天数从当天开始向前推算
        Date endDate = new Date();
        List<String> dateStrList = Utils.dateList(flowDateCount, endDate);
        for (String dateStr : dateStrList) {
            if (!handlerMap.containsKey(dateStr)) {
                handlerMap.put(dateStr, new ArrayList<>());
            }
        }
        return dateStrList;
    }

    private ErpFlowControlDTO buildErpFlowControl(String time, String cacheFileMd5, Long suId, String suDeptNo, OssFileInfo fileInfo, Integer operType) {
        ErpFlowControlDTO erpFlowControl = new ErpFlowControlDTO();
        erpFlowControl.setSuId(suId);
        erpFlowControl.setTaskCode(Integer.parseInt(ErpTopicName.ErpSaleFlow.getMethod()));
        erpFlowControl.setFileTime(DateUtil.convertString2Date(time, Constants.FORMATE_DAY));
        erpFlowControl.setFileMd5(fileInfo.getMd5());
        erpFlowControl.setFileKey(fileInfo.getKey());
        erpFlowControl.setOperType(operType);
        erpFlowControl.setCacheFilemd5(cacheFileMd5);
        erpFlowControl.setSuDeptNo(suDeptNo);
        return erpFlowControl;
    }

    public String getSuDeptNoKey(String time, String suDeptNo) {
        if (org.springframework.util.StringUtils.isEmpty(suDeptNo)) {
            return time;
        }
        return time + OpenConstants.SPLITE_SYMBOL + suDeptNo;
    }

}
