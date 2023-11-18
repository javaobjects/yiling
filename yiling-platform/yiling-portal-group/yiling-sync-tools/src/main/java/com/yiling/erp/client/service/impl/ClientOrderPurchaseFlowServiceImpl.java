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
import com.yiling.open.erp.dto.ErpPurchaseFlowDTO;
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
 * 采购订单流向
 *
 * @author: houjie.sun
 * @date: 2021/9/22
 */
@Slf4j
@Service("clientOrderPurchaseFlowService")
public class ClientOrderPurchaseFlowServiceImpl extends InitErpSysData implements SyncTaskService {

    @Autowired
    private InitErpConfig initErpConfig;
    @Autowired
    private TaskConfigDao taskConfigDao;
    @Autowired
    private OssManager ossManager;

    @Override
    public void syncData() {
        Long startTime=System.currentTimeMillis();
        try {
            CacheTaskUtil.getInstance().addCacheData(ErpTopicName.ErpPurchaseFlow.getTopicName());
            log.info("[ERP采购订单流向信息同步], 任务开始 time -> {}", DateUtil.convertDate2String(new Date(), Constants.FORMATE_DAY_TIME));
            this.initDataBase(ErpTopicName.ErpPurchaseFlow.getTopicName());
            // sqlite缓存的数据
            Map<String, String> sqliteData = this.findSqliteData(ErpTopicName.ErpPurchaseFlow.getTopicName());
            // 查询数据
            List<BaseErpEntity> dataList = this.findDataAll(ErpTopicName.ErpPurchaseFlow.getMethod());
            if (CollUtil.isEmpty(dataList)) {
                log.error("[ERP采购订单流向信息同步] 查询时间范围内的中间表数据为空");
                return;
            }
            log.info("[ERP采购订单流向信息同步] 本地缓存采购订单流向信息条数:" + sqliteData.size() + " | ERP系统采购订单流向信息条数:" + dataList.size());

            List<ErpPurchaseFlowDTO> erpPurchaseFlowList = PojoUtils.map(dataList, ErpPurchaseFlowDTO.class);
            // po_time 字段校验
            List<ErpPurchaseFlowDTO> poTimeNullList = erpPurchaseFlowList.stream().filter(d -> ObjectUtil.isNull(d.getPoTime())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(poTimeNullList)) {
                log.error("[ERP采购订单流向信息同步] 字段 po_time 有为空的,请检查 po_time 字段");
                return;
            }
            // enterprise_inner_code 字段校验
            List<ErpPurchaseFlowDTO> enterpriseInnerCodeNullList = erpPurchaseFlowList.stream().filter(d -> StrUtil.isBlank(d.getEnterpriseInnerCode()))
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(enterpriseInnerCodeNullList)) {
                log.error("[ERP采购订单流向信息同步] 字段 enterprise_inner_code 有为空的,请检查 enterprise_inner_code 字段");
                return;
            }
            // goods_in_sn 字段校验
            List<ErpPurchaseFlowDTO> goodsInnerSnNullList = erpPurchaseFlowList.stream().filter(d -> StrUtil.isBlank(d.getGoodsInSn()))
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(goodsInnerSnNullList)) {
                log.error("[ERP采购订单流向信息同步] 字段 goods_in_sn 有为空的,请检查 goods_in_sn 字段");
                return;
            }

            // 价格格式化，去除小数后面的0
            for (ErpPurchaseFlowDTO erpPurchaseFlowDTO : erpPurchaseFlowList) {
                BigDecimal price = erpPurchaseFlowDTO.getPoPrice().stripTrailingZeros();
                BigDecimal quantity = erpPurchaseFlowDTO.getPoQuantity().stripTrailingZeros();
                erpPurchaseFlowDTO.setPoPrice(price);
                erpPurchaseFlowDTO.setPoQuantity(quantity);
            }

            // 数据按照部门分组
            Map<String, List<ErpPurchaseFlowDTO>> deptMap = erpPurchaseFlowList.stream()
                    .collect(Collectors.groupingBy(e -> StrUtil.nullToEmpty(e.getSuDeptNo())));
            log.debug("[ERP采购订单流向信息同步] 部门:" + deptMap.keySet());

            // 每个部门的数据再按照日期分组处理
            for (Map.Entry<String, List<ErpPurchaseFlowDTO>> entry : deptMap.entrySet()) {
                String suDeptNo = entry.getKey();
                List<ErpPurchaseFlowDTO> deptList = entry.getValue();

                // 数据按照日期分组
                Map<String, List<ErpPurchaseFlowDTO>> dateMap = deptList.stream()
                        .collect(Collectors.groupingBy(e -> DateUtil.convertDate2String(e.getPoTime(), Constants.FORMATE_DAY)));
                // 日期范围列表，天数从当天开始向前推算
                List<String> dateStrList = getListByDate(dateMap);
                log.debug("[ERP采购订单流向信息同步] 部门:" + suDeptNo + ", 查询日期:" + dateStrList.get(0) + "至" + dateStrList.get(dateStrList.size() - 1));

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
                    log.debug("[ERP采购订单流向信息同步], 发送消息，上传ERP采购订单流向数据压缩包，新增");
                    List<BaseErpEntity> syncAddList = PojoUtils.map(addList, BaseErpEntity.class);
                    this.sendData(syncAddList, initErpConfig.getSysConfig(), ErpTopicName.ErpPurchaseFlow, OperTypeEnum.ADD.getCode());
                }
                if (CollUtil.isNotEmpty(updateList)) {
                    log.debug("[ERP采购订单流向信息同步], 发送消息，上传ERP采购订单流向数据压缩包，更新");
                    List<BaseErpEntity> syncUpdateList = PojoUtils.map(updateList, BaseErpEntity.class);
                    this.sendData(syncUpdateList, initErpConfig.getSysConfig(), ErpTopicName.ErpPurchaseFlow, OperTypeEnum.UPDATE.getCode());
                }
            }
        } catch (Exception e) {
            log.error(MessageFormat.format(Constants.SYNC_EXCEPTION_NAME, System.currentTimeMillis()-startTime,"[ERP采购订单流向信息同步]", e.toString()));
//            PopRequest popRequest = new PopRequest();
//            SysConfig sysConfig = initErpConfig.getSysConfig();
//            ClientToolLogDTO erpLog = new ClientToolLogDTO();
//            erpLog.setClientLog("[ERP采购订单流向信息同步], " + e.getMessage() + "堆栈信息：" + ExceptionUtils.getFullStackTrace(e));
//            erpLog.setMothedNo(ErpTopicName.ErpPurchaseFlow.getMethod());
//            erpLog.setLogType(Integer.valueOf(3));
//            popRequest.addErpLog(erpLog, sysConfig);
        } finally {
            // 清空临时文件
            Utils.clearDirectory(InitErpConfig.ERP_PURCHASE_FLOW_PATH);
            CacheTaskUtil.getInstance().removeCacheData(ErpTopicName.ErpPurchaseFlow.getTopicName());
        }
    }

    private boolean handlerByDate(Map<String, String> sqliteData, String suDeptNo, Map<String, List<ErpPurchaseFlowDTO>> dateMap,
                                  List<String> dateStrList, List<ErpFlowControlDTO> addList, List<ErpFlowControlDTO> updateList) throws Exception {
        for (String time : dateStrList) {
            List<ErpPurchaseFlowDTO> list = dateMap.getOrDefault(time,new ArrayList<>());
            String jsonList = JSON.toJSONString(list);
            String sign=list.stream().map(e->e.getErpPrimaryKey()).collect(Collectors.joining());
            String md5 = SecureUtil.md5(sign);
            // 校验md5
            String md5Cash = sqliteData.get(this.getSuDeptNoKey(time,suDeptNo));

            if (ObjectUtil.equal(md5, md5Cash)) {
                continue;
            }
            // 写入文件
            String textPath = InitErpConfig.ERP_PURCHASE_FLOW_PATH + File.separator + time + ".txt";
            FileUtil.writeUtf8String(jsonList, textPath);
            // 压缩文件
            String zipPath = InitErpConfig.ERP_PURCHASE_FLOW_PATH + File.separator + time + ".zip";
            ZipUtil.zip(textPath, zipPath);
            // 获取md5
            File file = new File(zipPath);

            // 上传压缩包
            Long suId = initErpConfig.getSysConfig().getSuId();
            String dateDir = time.replace("-", "/");
            OssFileInfo fileInfo = ossManager.flowUpload(file, FileTypeEnum.ERP_PURCHASE_FLOW,initErpConfig.getSysConfig().getEnvName(), String.valueOf(suId), suDeptNo, dateDir);
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

    private List<String> getListByDate(Map<String, List<ErpPurchaseFlowDTO>> handlerMap) throws Exception {
        List<TaskConfig> taskConfigList = taskConfigDao.executeQueryTaskConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME,
                "select * from task_config where taskNo = " + ErpTopicName.ErpPurchaseFlow.getMethod());
        int flowDateCount = Integer.parseInt(taskConfigList.get(0).getFlowDateCount());
        // 天数从当天开始向前推算
        Date endDate = new Date();
        List<String> dateStrList = Utils.dateList(flowDateCount, endDate);
        for (String dateStr : dateStrList) {
            if (!handlerMap.containsKey(dateStr)) {
                // 没有数据的，填补日期
                handlerMap.put(dateStr, new ArrayList<>());
            }
        }
        return dateStrList;
    }

    private ErpFlowControlDTO buildErpFlowControl(String time, String cacheFileMd5, Long suId, String suDeptNo, OssFileInfo fileInfo, Integer operType) {
        ErpFlowControlDTO erpFlowControl = new ErpFlowControlDTO();
        erpFlowControl.setSuId(suId);
        erpFlowControl.setTaskCode(Integer.parseInt(ErpTopicName.ErpPurchaseFlow.getMethod()));
        erpFlowControl.setFileTime(DateUtil.convertString2Date(time, Constants.FORMATE_DAY));
        erpFlowControl.setFileMd5(fileInfo.getMd5());
        erpFlowControl.setFileKey(fileInfo.getKey());
        erpFlowControl.setOperType(operType);
        erpFlowControl.setCacheFilemd5(cacheFileMd5);
        erpFlowControl.setSuDeptNo(suDeptNo);
        return erpFlowControl;
    }

    /*public static void main(String[] args){
    
    
        Date date = new Date();
        ErpPurchaseFlowDTO flow1 = new ErpPurchaseFlowDTO();
        flow1.setPoNo("abc-1");
        flow1.setPoTime(date);
        ErpPurchaseFlowDTO flow2 = new ErpPurchaseFlowDTO();
        flow2.setPoNo("abc-2");
        flow2.setPoTime(date);
    
        List<ErpPurchaseFlowDTO> list = new ArrayList<>();
        list.add(flow1);
        list.add(flow2);
    
        // 写入文件
        String dateStr = "2021-09-27";
        String basePath = "E:/rkConfig" + File.separator + ErpTopicName.ErpPurchaseFlow.getTopicName() + File.separator;
        String textPath =  basePath + dateStr + ".txt";
        FileUtil.writeUtf8String(JSON.toJSONString(list), textPath);
    
        // 压缩文件
        String zipPath = basePath + dateStr + ".zip";
        ZipUtil.zip(textPath, zipPath);
    
        File file = new File(zipPath);
        String md5 = SecureUtil.md5(file);
        System.out.println(">>>>> md5: " +md5);
    
        // 解压
        File unzip = ZipUtil.unzip(zipPath);
        String unzipPath = basePath + File.separator + dateStr + File.separator + dateStr + ".txt";
        String listString = FileUtil.readUtf8String(unzipPath);
        System.out.println(">>>>> listString ->: " + listString);
    
        // 清空临时文件
        clearDirectory( "E:/rkConfig" + File.separator + ErpTopicName.ErpPurchaseFlow.getTopicName()+ File.separator);
    
    //        zipPath.substring(zipPath.indexOf(InitErpConfig.ERP_PURCHASE_FLOW_PATH + File.separator)+1, zipPath.length() - 4);
    //        String str = InitErpConfig.ERP_PURCHASE_FLOW_PATH + File.separator + "2021-09-22" + ".zip";
    //        String substring = str.substring(str.indexOf(InitErpConfig.ERP_PURCHASE_FLOW_PATH + File.separator)+1, str.length() - 4);
    //        System.out.println(">>>>> substring:"+substring);
    
    }*/

    /*public static boolean clearDirectory(String path){
        File file = new File(path);
        if(!file.exists()){//判断是否待删除目录是否存在
            System.err.println("The dir are not exists!");
            return false;
        }
    
        String[] content = file.list();//取得当前目录下所有文件和文件夹
        for(String name : content){
            File temp = new File(path, name);
            if(temp.isDirectory()){//判断是否是目录
                clearDirectory(temp.getAbsolutePath());//递归调用，删除目录里的内容
                temp.delete();//删除空目录
            }else{
                if(!temp.delete()){//直接删除文件
                    System.err.println("Failed to delete " + name);
                }
            }
        }
        return true;
    }*/

    public String getSuDeptNoKey(String time, String suDeptNo) {
        if (org.springframework.util.StringUtils.isEmpty(suDeptNo)) {
            return time;
        }
        return time + OpenConstants.SPLITE_SYMBOL + suDeptNo;
    }

}
