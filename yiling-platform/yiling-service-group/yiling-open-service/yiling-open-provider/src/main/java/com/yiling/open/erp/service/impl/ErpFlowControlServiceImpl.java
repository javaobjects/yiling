package com.yiling.open.erp.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dao.ErpEntityMapper;
import com.yiling.open.erp.dao.ErpFlowControlMapper;
import com.yiling.open.erp.dao.ErpGoodsBatchFlowMapper;
import com.yiling.open.erp.dao.ErpPurchaseFlowMapper;
import com.yiling.open.erp.dao.ErpSaleFlowMapper;
import com.yiling.open.erp.dao.ErpShopSaleFlowMapper;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpPurchaseFlowDTO;
import com.yiling.open.erp.dto.ErpSaleFlowDTO;
import com.yiling.open.erp.dto.ErpShopSaleFlowDTO;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;
import com.yiling.open.erp.dto.request.ErpFlowControlFailRequest;
import com.yiling.open.erp.dto.request.QuerySaleFlowConditionRequest;
import com.yiling.open.erp.entity.ErpClientDO;
import com.yiling.open.erp.entity.ErpFlowControlDO;
import com.yiling.open.erp.entity.ErpPurchaseFlowDO;
import com.yiling.open.erp.entity.ErpSaleFlowDO;
import com.yiling.open.erp.entity.ErpShopSaleFlowDO;
import com.yiling.open.erp.enums.DataTagEnum;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.enums.SyncStatus;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpFlowControlService;
import com.yiling.open.erp.util.Zip;
import com.yiling.open.ftp.service.FlowFtpClientService;
import com.yiling.open.monitor.entity.MonitorAbnormalDataDO;
import com.yiling.open.monitor.service.MonitorAbnormalDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-09-22
 */
@Slf4j
@CacheConfig(cacheNames = "open:erpFlowControl")
@Service(value = "erpFlowControlService")
public class ErpFlowControlServiceImpl extends ErpEntityServiceImpl implements ErpFlowControlService {

    @Autowired
    private ErpFlowControlMapper erpFlowControlMapper;
    @Autowired
    private ErpPurchaseFlowMapper erpPurchaseFlowMapper;
    @Autowired
    private ErpSaleFlowMapper          erpSaleFlowMapper;
    @Autowired
    private ErpClientService           erpClientService;
    @Autowired
    private FileService                fileService;
    @Autowired
    private ErpFlowControlService      erpFlowControlService;
    @Autowired
    private FlowFtpClientService       flowFtpClientService;
    @Autowired
    private MonitorAbnormalDataService monitorAbnormalDataService;
    @Autowired(required = false)
    private RocketMqProducerService    rocketMqProducerService;
    @Autowired
    private ErpShopSaleFlowMapper      erpShopSaleFlowMapper;

    @Override
    public boolean onlineData(BaseErpEntity baseErpEntity) {
        ErpFlowControlDO erpFlowControlDO = (ErpFlowControlDO) baseErpEntity;
        // 1.查询规则信息
        ErpClientDTO erpClient = erpClientService.getErpClientBySuIdAndSuDeptNo(erpFlowControlDO.getSuId(), erpFlowControlDO.getSuDeptNo());
        if (erpClient == null || erpClient.getSyncStatus() == null || erpClient.getSyncStatus() == 0) {
            erpFlowControlMapper.updateSyncStatusAndMsg(erpFlowControlDO.getId(), SyncStatus.UNSYNC.getCode(), "未开启同步规则");
            return false;
        }
        return synErpFlowControl(erpFlowControlDO, erpClient);
    }

    @Override
    public List<ErpFlowControlDO> synFlowControlPage() {
        ErpClientQueryRequest request = new ErpClientQueryRequest();
        request.setClientStatus(1);
        request.setSyncStatus(1);

        List<ErpFlowControlDO> erpFlowControlList = new ArrayList<>();

        Page<ErpClientDO> pageSale = null;
        int currentSale = 1;
        do {
            request.setCurrent(currentSale);
            request.setSize(20);
            pageSale = erpClientService.page(request);

            if (pageSale == null || CollUtil.isEmpty(pageSale.getRecords())) {
                break;
            }

            List<QuerySaleFlowConditionRequest> erpClientDOList = new ArrayList<>();
            for (ErpClientDO e : pageSale.getRecords()) {
                QuerySaleFlowConditionRequest querySaleFlowConditionRequest = new QuerySaleFlowConditionRequest();
                querySaleFlowConditionRequest.setSuId(e.getSuId());
                querySaleFlowConditionRequest.setSuDeptNo(e.getSuDeptNo());
                erpClientDOList.add(querySaleFlowConditionRequest);
            }
            List<ErpFlowControlDO> erpFlowControlDOList = erpFlowControlMapper.syncFlowControlPage(erpClientDOList);
            erpFlowControlList.addAll(erpFlowControlDOList);
            currentSale = currentSale + 1;
        } while (pageSale != null && CollUtil.isNotEmpty(pageSale.getRecords()));
        return erpFlowControlList;
    }

    @Override
    @Cacheable(key = "#suId+'+'+#suDeptNo+'+getInitDateBySuIdAndSuDeptNo'")
    public Date getInitDateBySuIdAndSuDeptNo(Long suId, String suDeptNo) {
        return erpFlowControlMapper.getInitDateBySuIdAndSuDeptNo(suId, suDeptNo);
    }

    @Override
    public void syncFlowControl() {
        List<ErpFlowControlDO> erpFlowControlList = synFlowControlPage();
        syncFlowControl(erpFlowControlList);
    }

    public void syncFlowControl(List<ErpFlowControlDO> erpFlowControlDOList) {
        for (ErpFlowControlDO erpFlowControlDO : erpFlowControlDOList) {
            int i = erpFlowControlMapper.updateSyncStatusByStatusAndId(erpFlowControlDO.getId(), SyncStatus.SYNCING.getCode(), SyncStatus.UNSYNC.getCode(), "job处理");
            if (i > 0) {
                onlineData(erpFlowControlDO);
            }
        }
    }

    public boolean synErpFlowControl(ErpFlowControlDO erpFlowControl, ErpClientDTO erpClient) {
        try {
            List<Integer> countList = null;
            List<String> failMsgList = new ArrayList<>();
            // 名字里面包含suId任务类型时间
            String key = erpFlowControl.getFileKey();
            Date dateTime = erpFlowControl.getFileTime();
            Integer taskCode = erpFlowControl.getTaskCode();
            if (dateTime != null && taskCode != null) {
                String filePath = "";
                if (Integer.parseInt(ErpTopicName.ErpPurchaseFlow.getMethod()) == taskCode) {
                    filePath = fileService.getUrl(key, FileTypeEnum.ERP_PURCHASE_FLOW);
                } else if (Integer.parseInt(ErpTopicName.ErpSaleFlow.getMethod()) == taskCode) {
                    filePath = fileService.getUrl(key, FileTypeEnum.ERP_SALE_FLOW);
                }else if (Integer.parseInt(ErpTopicName.ErpShopSaleFlow.getMethod()) == taskCode) {
                    filePath = fileService.getUrl(key, FileTypeEnum.ERP_SHOP_SALE_FLOW);
                }
                //else {
                //    filePath = fileService.getUrl(key, FileTypeEnum.ERP_GOODS_BATCH_FLOW_PATH);
                //}
                if (StrUtil.isEmpty(filePath)) {
                    log.error("获取oss流向地址错误 erpFlowControl" + JSON.toJSONString(erpFlowControl));
                    erpFlowControlMapper.updateSyncStatusAndMsg(erpFlowControl.getId(), SyncStatus.FAIL.getCode(), "获取oss地址报错");
                    return false;
                }
                File zipFile = new File(FileUtil.getTmpDir() + File.separator + erpFlowControl.getErpPrimaryKey() + File.separator + System.nanoTime() + ".zip");
                HttpUtil.downloadFile(filePath, zipFile);
                if (zipFile == null) {
                    log.error("下载oss文件错误 erpFlowControl" + JSON.toJSONString(erpFlowControl));
                    erpFlowControlMapper.updateSyncStatusAndMsg(erpFlowControl.getId(), SyncStatus.FAIL.getCode(), "oss下载文件错误");
                    return false;
                }
                if (!verifyFile(erpFlowControl.getFileMd5(), zipFile)) {
                    log.error("验证oss文件错误 erpFlowControl" + JSON.toJSONString(erpFlowControl));
                    erpFlowControlMapper.updateSyncStatusAndMsg(erpFlowControl.getId(), SyncStatus.FAIL.getCode(), "文件验证失败");
                    return false;
                }
                String content = Zip.getZipFileContent(zipFile, DateUtil.format(dateTime, "yyyy-MM-dd") + ".txt");
                //删除压缩包文件
                zipFile.delete();
                if (StrUtil.isEmpty(content)) {
                    log.error("解压缩文件错误 erpFlowControl" + JSON.toJSONString(erpFlowControl));
                    erpFlowControlMapper.updateSyncStatusAndMsg(erpFlowControl.getId(), SyncStatus.FAIL.getCode(), "文件里数据为空");
                    return false;
                }
                countList = executeComparison(erpFlowControl, content,erpClient, failMsgList);
                if (CollUtil.isNotEmpty(countList)) {
                    Integer handleCount = countList.get(0);
                    Integer failedCount = countList.get(1);
                    ErpFlowControlFailRequest request = new ErpFlowControlFailRequest();
                    request.setId(erpFlowControl.getId());
                    request.setSuccessNumber(handleCount);
                    request.setFailedNumber(failedCount);
                    if (failedCount > 0) {
                        request.setSyncMsg("同步失败: ".concat(String.join("; ", failMsgList)));
                        request.setSyncStatus(SyncStatus.FAIL.getCode());
                    } else {
                        request.setSyncMsg("同步成功");
                        request.setSyncStatus(SyncStatus.SUCCESS.getCode());
                    }
                    erpFlowControlMapper.updateSyncStatusAndMsgAndFailnumber(request);
                } else {
                    erpFlowControlMapper.updateSyncStatusAndMsg(erpFlowControl.getId(), SyncStatus.SUCCESS.getCode(), "同步成功");
                }
            }

        } catch (Exception e) {
            log.error("流向数据解析异常", e);
            erpFlowControlMapper.updateSyncStatusAndMsg(erpFlowControl.getId(), SyncStatus.FAIL.getCode(), "流向数据解析异常");
        }
        return true;
    }


    public List<Integer> executeComparison(ErpFlowControlDO erpFlowControl, String content, ErpClientDTO erpClient,List<String> failMsgList) {
        List<Integer> lists = null;
        if (erpFlowControl.getTaskCode().equals(Integer.parseInt(ErpTopicName.ErpPurchaseFlow.getMethod()))) {
            List<ErpPurchaseFlowDTO> list = JSONArray.parseArray(content, ErpPurchaseFlowDTO.class);
            lists = executePurchaseOrderComparison(erpFlowControl, list,erpClient, failMsgList);
        } else if (erpFlowControl.getTaskCode().equals(Integer.parseInt(ErpTopicName.ErpSaleFlow.getMethod()))) {
            List<ErpSaleFlowDTO> list = JSONArray.parseArray(content, ErpSaleFlowDTO.class);
            lists = executeSaleOrderComparison(erpFlowControl, list,erpClient, failMsgList);
        }else if (erpFlowControl.getTaskCode().equals(Integer.parseInt(ErpTopicName.ErpShopSaleFlow.getMethod()))) {
            List<ErpShopSaleFlowDTO> list = JSONArray.parseArray(content, ErpShopSaleFlowDTO.class);
            lists = executeShopSaleOrderComparison(erpFlowControl, list,erpClient, failMsgList);
        }
        return lists;
    }

    /**
     * 按天对比处理采购单流向数据。合并重复数据，生成cnt数量，生成主键对比op库数据
     */
    public List<Integer> executePurchaseOrderComparison(ErpFlowControlDO erpFlowControl, List<ErpPurchaseFlowDTO> list,ErpClientDTO erpClient,List<String> failMsgList) {
        List<Integer> countList = new ArrayList<Integer>();
        Integer handleCount = 0;
        Integer failCount = 0;
        // 删除的主键
        Set<String> deleteResult = new HashSet<String>();
        // 合并重复
        HashMap<String, ErpPurchaseFlowDTO> mergeErpPurchaseOrder = new HashMap<String, ErpPurchaseFlowDTO>();
        for (ErpPurchaseFlowDTO erpPurchaseFlowDTO : list) {
            // 生成主键
            erpPurchaseFlowDTO.setPoId(erpPurchaseFlowDTO.getErpPrimaryKey());
            erpPurchaseFlowDTO.setControlId(erpFlowControl.getId());
            String key = erpPurchaseFlowDTO.getErpPrimaryKey();
            if (mergeErpPurchaseOrder.get(key) != null) {
                ErpPurchaseFlowDTO erpPurchaseOrderEntry = mergeErpPurchaseOrder.get(key);
                erpPurchaseOrderEntry.setCnt(erpPurchaseOrderEntry.getCnt() + 1);
            } else {
                mergeErpPurchaseOrder.put(key, erpPurchaseFlowDTO);
            }
        }
        // 获取op库数据，通过suId，时间
        List<ErpPurchaseFlowDO> erpOpPurchaseOrderList = erpPurchaseFlowMapper.findListBySuIdAndPoTime(DateUtil.beginOfDay(erpFlowControl.getFileTime()), DateUtil.endOfDay(erpFlowControl.getFileTime()), erpFlowControl.getSuDeptNo(), erpFlowControl.getSuId());
        List<ErpPurchaseFlowDTO> erpOpPurchaseOrderDTOList = PojoUtils.map(erpOpPurchaseOrderList, ErpPurchaseFlowDTO.class);
        // op库数据转成map
        Map<String, ErpPurchaseFlowDTO> erpOpPurchaseOrderOldMap = new HashMap<String, ErpPurchaseFlowDTO>();
        for (ErpPurchaseFlowDTO erpPurchaseOrder : erpOpPurchaseOrderDTOList) {
            if (erpOpPurchaseOrderOldMap.containsKey(erpPurchaseOrder.getPoId())) {
                ErpPurchaseFlowDTO erpPurchaseOrderEntry = erpOpPurchaseOrderOldMap.get(erpPurchaseOrder.getPoId());
                erpPurchaseOrderEntry.setCnt(erpPurchaseOrderEntry.getCnt() + erpPurchaseOrder.getCnt());
                erpPurchaseFlowMapper.deleteFlowById(erpPurchaseOrder.getId());
            } else {
                erpOpPurchaseOrderOldMap.put(erpPurchaseOrder.getPoId(), erpPurchaseOrder);
            }
        }
        // 排序
        List<ErpPurchaseFlowDTO> erpPurchaseOrderList = new ArrayList<ErpPurchaseFlowDTO>(mergeErpPurchaseOrder.values());
        Collections.sort(erpPurchaseOrderList, new Comparator<ErpPurchaseFlowDTO>() {
            @Override
            public int compare(ErpPurchaseFlowDTO arg0, ErpPurchaseFlowDTO arg1) {
                return arg0.getPoTime().compareTo(arg1.getPoTime());
            }
        });

        List<String> enterpriseInnerCodeList = new ArrayList<>();
        List<String> goodsInSnCodeList = new ArrayList<>();
        List<ErpPurchaseFlowDTO> sendMqErpPurchaseFlowList = new ArrayList<>();
        for (ErpPurchaseFlowDTO erpPurchaseOrder : erpPurchaseOrderList) {
            try {
                // 统计当前文件主键
                deleteResult.add(erpPurchaseOrder.getPoId());
                erpPurchaseOrder.setSuId(erpFlowControl.getSuId());
                String cInternalCode = erpPurchaseOrder.getEnterpriseInnerCode();
                String ginSn = erpPurchaseOrder.getGoodsInSn();
                if (StrUtil.isBlank(cInternalCode)) {
                    failCount = failCount + 1;
                    enterpriseInnerCodeList.add(erpPurchaseOrder.getErpPrimaryKey());
                    log.error("enterprise_inner_code为空:" + erpPurchaseOrder.toString());
                    continue;
                } else if (StrUtil.isBlank(ginSn)) {
                    failCount = failCount + 1;
                    goodsInSnCodeList.add(erpPurchaseOrder.getErpPrimaryKey());
                    log.error("goods_in_sn为空:" + erpPurchaseOrder.toString());
                    continue;
                } else {
                    handleCount = handleCount + 1;
                }

                // TODO 判断erpOpPurchaseOrderOldMap是否为空
                if (CollUtil.isEmpty(erpOpPurchaseOrderOldMap)) {
                    erpPurchaseOrder.setOperType(1);
                    erpPurchaseOrder.setDataTag(DataTagEnum.NORMAL.getCode());
                    sendMqErpPurchaseFlowList.add(erpPurchaseOrder);
                } else {
                    //存在修改的数据
                    if (erpOpPurchaseOrderOldMap.containsKey(erpPurchaseOrder.getPoId())) {
                        ErpPurchaseFlowDTO erpPurchaseOrderOld = erpOpPurchaseOrderOldMap.get(erpPurchaseOrder.getPoId());
                        if(erpPurchaseOrderOld.getCnt().intValue()!=erpPurchaseOrder.getCnt().intValue()) {
                            erpPurchaseOrder.setOperType(2);
                            erpPurchaseOrder.setDataTag(DataTagEnum.EXC_UPDATE.getCode());
                            erpPurchaseOrder.setId(erpPurchaseOrderOld.getId());
                            sendMqErpPurchaseFlowList.add(erpPurchaseOrder);
                        }
                    } else {
                        erpPurchaseOrder.setOperType(1);
                        erpPurchaseOrder.setDataTag(DataTagEnum.EXC_ADD.getCode());
                        sendMqErpPurchaseFlowList.add(erpPurchaseOrder);
                    }
                }


            } catch (Exception e) {
                failCount = failCount + 1;
                log.error("流向数据操作数据错误", e);
                erpFlowControlMapper.updateSyncStatusAndMsg(erpFlowControl.getId(), SyncStatus.FAIL.getCode(), "系统异常");
            }
        }

        // 删除op库数据
        Set<String> erpPoIdSet = erpOpPurchaseOrderOldMap.keySet();
        erpPoIdSet.removeAll(deleteResult);
        for (String erpPoId : erpPoIdSet) {
            ErpPurchaseFlowDTO erpPurchaseOrderOld = erpOpPurchaseOrderOldMap.get(erpPoId);
            erpPurchaseOrderOld.setControlId(erpFlowControl.getId());
            erpPurchaseOrderOld.setOperType(3);
            erpPurchaseOrderOld.setDataTag(DataTagEnum.DELETE.getCode());
            sendMqErpPurchaseFlowList.add(erpPurchaseOrderOld);
        }

        if (CollUtil.isNotEmpty(sendMqErpPurchaseFlowList)) {
            List<List<ErpPurchaseFlowDTO>> groupErpPurchaseFlowDTOList = this.groupErpPurchaseFlowDTOList(sendMqErpPurchaseFlowList, 2000);
            for (List<ErpPurchaseFlowDTO> erpPurchaseFlowDTOList : groupErpPurchaseFlowDTOList) {
                SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpPurchaseFlow.getTopicName(), erpFlowControl.getSuId() + "", DateUtil.formatDate(new Date()), JSON.toJSONString(erpPurchaseFlowDTOList));
                if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                    throw new BusinessException(OpenErrorCode.ERP_PURCHASE_FLOW_ERROR);
                }
            }
        }

        if (CollUtil.isNotEmpty(enterpriseInnerCodeList)) {
            failMsgList.add("enterprise_inner_code为空[".concat(String.join(",", enterpriseInnerCodeList)).concat("]"));
        }
        if (CollUtil.isNotEmpty(goodsInSnCodeList)) {
            failMsgList.add("goods_in_sn为空[".concat(String.join(",", goodsInSnCodeList)).concat("]"));
        }

        countList.add(handleCount);
        countList.add(failCount);
        return countList;
    }

    /**
     * 按天对比处理采购单流向数据。合并重复数据，生成cnt数量，生成主键对比op库数据
     */
    public List<Integer> executeSaleOrderComparison(ErpFlowControlDO erpFlowControl, List<ErpSaleFlowDTO> list,ErpClientDTO erpClient, List<String> failMsgList) {
        List<Integer> countList = new ArrayList<Integer>();
        Integer handleCount = 0;
        Integer failCount = 0;
        // 删除的主键
        Set<String> deleteResult = new HashSet<String>();
        // 合并重复
        HashMap<String, ErpSaleFlowDTO> mergeErpSaleOrder = new HashMap<>();
        for (ErpSaleFlowDTO erpSaleFlowDTO : list) {
            // 生成主键
            erpSaleFlowDTO.setSoId(erpSaleFlowDTO.getErpPrimaryKey());
            erpSaleFlowDTO.setControlId(erpFlowControl.getId());
            String key = erpSaleFlowDTO.getErpPrimaryKey();
            if (mergeErpSaleOrder.get(key) != null) {
                ErpSaleFlowDTO erpSaleOrderEntry = mergeErpSaleOrder.get(key);
                erpSaleOrderEntry.setCnt(erpSaleOrderEntry.getCnt() + 1);
            } else {
                mergeErpSaleOrder.put(key, erpSaleFlowDTO);
            }
        }
        // 获取op库数据，通过suId，时间
        List<ErpSaleFlowDO> erpOpSaleOrderList = erpSaleFlowMapper.findListBySuIdAndSoTime(DateUtil.beginOfDay(erpFlowControl.getFileTime()), DateUtil.endOfDay(erpFlowControl.getFileTime()), erpFlowControl.getSuDeptNo(), erpFlowControl.getSuId());
        List<ErpSaleFlowDTO> erpOpSaleOrderDTOList = PojoUtils.map(erpOpSaleOrderList, ErpSaleFlowDTO.class);
        // op库数据转成map
        Map<String, ErpSaleFlowDTO> erpOpSaleOrderOldMap = new HashMap<String, ErpSaleFlowDTO>();
        for (ErpSaleFlowDTO erpSaleOrder : erpOpSaleOrderDTOList) {
            if (erpOpSaleOrderOldMap.containsKey(erpSaleOrder.getSoId())) {
                ErpSaleFlowDTO erpSaleOrderEntry = erpOpSaleOrderOldMap.get(erpSaleOrder.getSoId());
                erpSaleOrderEntry.setCnt(erpSaleOrderEntry.getCnt() + erpSaleOrder.getCnt());
                erpSaleFlowMapper.deleteFlowById(erpSaleOrder.getId());
            } else {
                erpOpSaleOrderOldMap.put(erpSaleOrder.getSoId(), erpSaleOrder);
            }
        }
        // 排序
        List<ErpSaleFlowDTO> erpSaleOrderList = new ArrayList<ErpSaleFlowDTO>(mergeErpSaleOrder.values());
        Collections.sort(erpSaleOrderList, new Comparator<ErpSaleFlowDTO>() {
            @Override
            public int compare(ErpSaleFlowDTO arg0, ErpSaleFlowDTO arg1) {
                return arg0.getSoTime().compareTo(arg1.getSoTime());
            }
        });

        List<String> enterpriseInnerCodeList = new ArrayList<>();
        List<String> goodsInSnCodeList = new ArrayList<>();
        List<ErpSaleFlowDTO> sendMqErpSaleFlowList = new ArrayList<>();
        List<MonitorAbnormalDataDO> monitorAbnormalDataDOList=new ArrayList<>();
        for (ErpSaleFlowDTO erpSaleOrder : erpSaleOrderList) {
            try {
                // 统计当前文件主键
                deleteResult.add(erpSaleOrder.getSoId());
                erpSaleOrder.setSuId(erpFlowControl.getSuId());
                String cInternalCode = erpSaleOrder.getEnterpriseInnerCode();
                String ginSn = erpSaleOrder.getGoodsInSn();
                if (StrUtil.isBlank(cInternalCode)) {
                    failCount = failCount + 1;
                    enterpriseInnerCodeList.add(erpSaleOrder.getErpPrimaryKey());
                    log.error("enterprise_inner_code为空:" + erpSaleOrder.toString());
                    continue;
                } else if (StrUtil.isBlank(ginSn)) {
                    failCount = failCount + 1;
                    goodsInSnCodeList.add(erpSaleOrder.getErpPrimaryKey());
                    log.error("goods_in_sn为空:" + erpSaleOrder.toString());
                    continue;
                } else {
                    handleCount = handleCount + 1;
                }

                // TODO
                if (CollUtil.isEmpty(erpOpSaleOrderOldMap)) {
                    erpSaleOrder.setOperType(1);
                    erpSaleOrder.setDataTag(DataTagEnum.NORMAL.getCode());
                    sendMqErpSaleFlowList.add(erpSaleOrder);
                } else {
                    if (erpOpSaleOrderOldMap.containsKey(erpSaleOrder.getSoId())) {
                        ErpSaleFlowDTO erpSaleOrderOld = erpOpSaleOrderOldMap.get(erpSaleOrder.getSoId());
                        if(erpSaleOrderOld.getCnt().intValue()!=erpSaleOrder.getCnt().intValue()) {
                            erpSaleOrder.setOperType(2);
                            erpSaleOrder.setDataTag(DataTagEnum.EXC_UPDATE.getCode());
                            erpSaleOrder.setId(erpSaleOrderOld.getId());
                            sendMqErpSaleFlowList.add(erpSaleOrder);
//                            monitorData(erpFlowControl,erpSaleOrder,erpClient,2,monitorAbnormalDataDOList);
                        }
                    } else {
                        erpSaleOrder.setOperType(1);
                        erpSaleOrder.setDataTag(DataTagEnum.EXC_ADD.getCode());
                        sendMqErpSaleFlowList.add(erpSaleOrder);
//                        monitorData(erpFlowControl,erpSaleOrder,erpClient,1,monitorAbnormalDataDOList);
                    }
                }


            } catch (Exception e) {
                failCount = failCount + 1;
                log.error("流向数据操作数据错误", e);
                erpFlowControlMapper.updateSyncStatusAndMsg(erpFlowControl.getId(), SyncStatus.FAIL.getCode(), "系统异常");
            }
        }

        // 删除op库数据
        Set<String> erpPoIdSet = erpOpSaleOrderOldMap.keySet();
        erpPoIdSet.removeAll(deleteResult);
        for (String erpPoId : erpPoIdSet) {
            ErpSaleFlowDTO erpSaleOrderOld = erpOpSaleOrderOldMap.get(erpPoId);
            erpSaleOrderOld.setOperType(3);
            erpSaleOrderOld.setDataTag(DataTagEnum.DELETE.getCode());
            erpSaleOrderOld.setControlId(erpFlowControl.getId());
            sendMqErpSaleFlowList.add(erpSaleOrderOld);
        }

        if (CollUtil.isNotEmpty(sendMqErpSaleFlowList)) {
            List<List<ErpSaleFlowDTO>> groupErpSaleFlowDTOList = this.groupErpSaleFlowDTOList(sendMqErpSaleFlowList, 2000);
            for (List<ErpSaleFlowDTO> erpSaleFlowDTOList : groupErpSaleFlowDTOList) {
                SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpSaleFlow.getTopicName(), erpFlowControl.getSuId() + "", DateUtil.formatDate(new Date()), JSON.toJSONString(erpSaleFlowDTOList));
                if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                    throw new BusinessException(OpenErrorCode.ERP_PURCHASE_FLOW_ERROR);
                }
            }
        }

        if (CollUtil.isNotEmpty(enterpriseInnerCodeList)) {
            failMsgList.add("enterprise_inner_code为空[".concat(String.join(",", enterpriseInnerCodeList)).concat("]"));
        }
        if (CollUtil.isNotEmpty(goodsInSnCodeList)) {
            failMsgList.add("goods_in_sn为空[".concat(String.join(",", goodsInSnCodeList)).concat("]"));
        }

        monitorAbnormalDataService.saveBatch(monitorAbnormalDataDOList,2000);

        countList.add(handleCount);
        countList.add(failCount);
        return countList;
    }

    /**
     * 按天对比处理采购单流向数据。合并重复数据，生成cnt数量，生成主键对比op库数据
     */
    public List<Integer> executeShopSaleOrderComparison(ErpFlowControlDO erpFlowControl, List<ErpShopSaleFlowDTO> list,ErpClientDTO erpClient, List<String> failMsgList) {
        List<Integer> countList = new ArrayList<Integer>();
        Integer handleCount = 0;
        Integer failCount = 0;
        // 删除的主键
        Set<String> deleteResult = new HashSet<String>();
        // 合并重复
        HashMap<String, ErpShopSaleFlowDTO> mergeErpSaleOrder = new HashMap<>();
        for (ErpShopSaleFlowDTO erpSaleFlowDTO : list) {
            // 生成主键
            erpSaleFlowDTO.setSoId(erpSaleFlowDTO.getErpPrimaryKey());
            erpSaleFlowDTO.setControlId(erpFlowControl.getId());
            String key = erpSaleFlowDTO.getErpPrimaryKey();
            if (mergeErpSaleOrder.get(key) != null) {
                ErpShopSaleFlowDTO erpSaleOrderEntry = mergeErpSaleOrder.get(key);
                erpSaleOrderEntry.setCnt(erpSaleOrderEntry.getCnt() + 1);
            } else {
                mergeErpSaleOrder.put(key, erpSaleFlowDTO);
            }
        }
        // 获取op库数据，通过suId，时间
        List<ErpShopSaleFlowDO> erpOpSaleOrderList = erpShopSaleFlowMapper.findListBySuIdAndSoTime(DateUtil.beginOfDay(erpFlowControl.getFileTime()), DateUtil.endOfDay(erpFlowControl.getFileTime()), erpFlowControl.getSuDeptNo(), erpFlowControl.getSuId());
        List<ErpShopSaleFlowDTO> erpOpSaleOrderDTOList = PojoUtils.map(erpOpSaleOrderList, ErpShopSaleFlowDTO.class);
        // op库数据转成map
        Map<String, ErpShopSaleFlowDTO> erpOpSaleOrderOldMap = new HashMap<String, ErpShopSaleFlowDTO>();
        for (ErpShopSaleFlowDTO erpSaleOrder : erpOpSaleOrderDTOList) {
            if (erpOpSaleOrderOldMap.containsKey(erpSaleOrder.getSoId())) {
                ErpShopSaleFlowDTO erpSaleOrderEntry = erpOpSaleOrderOldMap.get(erpSaleOrder.getSoId());
                erpSaleOrderEntry.setCnt(erpSaleOrderEntry.getCnt() + erpSaleOrder.getCnt());
                erpShopSaleFlowMapper.deleteFlowById(erpSaleOrder.getId());
            } else {
                erpOpSaleOrderOldMap.put(erpSaleOrder.getSoId(), erpSaleOrder);
            }
        }
        // 排序
        List<ErpShopSaleFlowDTO> erpSaleOrderList = new ArrayList<ErpShopSaleFlowDTO>(mergeErpSaleOrder.values());
        Collections.sort(erpSaleOrderList, new Comparator<ErpShopSaleFlowDTO>() {
            @Override
            public int compare(ErpShopSaleFlowDTO arg0, ErpShopSaleFlowDTO arg1) {
                return arg0.getSoTime().compareTo(arg1.getSoTime());
            }
        });

        List<ErpShopSaleFlowDTO> sendMqErpSaleFlowList = new ArrayList<>();
        for (ErpShopSaleFlowDTO erpSaleOrder : erpSaleOrderList) {
            try {
                // 统计当前文件主键
                deleteResult.add(erpSaleOrder.getSoId());
                erpSaleOrder.setSuId(erpFlowControl.getSuId());

                if (erpOpSaleOrderOldMap.containsKey(erpSaleOrder.getSoId())) {
                    ErpShopSaleFlowDTO erpShopSaleFlowOld = erpOpSaleOrderOldMap.get(erpSaleOrder.getSoId());
                    if(erpShopSaleFlowOld.getCnt().intValue()!=erpSaleOrder.getCnt().intValue()) {
                        erpSaleOrder.setOperType(2);
                        erpSaleOrder.setId(erpShopSaleFlowOld.getId());
                        sendMqErpSaleFlowList.add(erpSaleOrder);
//                            monitorData(erpFlowControl,erpSaleOrder,erpClient,2,monitorAbnormalDataDOList);
                    }
                } else {
                    erpSaleOrder.setOperType(1);
                    sendMqErpSaleFlowList.add(erpSaleOrder);
                }
                handleCount = handleCount + 1;
            } catch (Exception e) {
                failCount = failCount + 1;
                log.error("流向数据操作数据错误", e);
                erpFlowControlMapper.updateSyncStatusAndMsg(erpFlowControl.getId(), SyncStatus.FAIL.getCode(), "系统异常");
            }
        }

        // 删除op库数据
        Set<String> erpPoIdSet = erpOpSaleOrderOldMap.keySet();
        erpPoIdSet.removeAll(deleteResult);
        for (String erpPoId : erpPoIdSet) {
            ErpShopSaleFlowDTO erpSaleOrderOld = erpOpSaleOrderOldMap.get(erpPoId);
            erpSaleOrderOld.setOperType(3);
            erpSaleOrderOld.setControlId(erpFlowControl.getId());
            sendMqErpSaleFlowList.add(erpSaleOrderOld);
        }

        if (CollUtil.isNotEmpty(sendMqErpSaleFlowList)) {
            List<List<ErpShopSaleFlowDTO>> groupErpSaleFlowDTOList = this.groupErpShopSaleFlowDTOList(sendMqErpSaleFlowList, 2000);
            for (List<ErpShopSaleFlowDTO> erpSaleFlowDTOList : groupErpSaleFlowDTOList) {
                SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpShopSaleFlow.getTopicName(), erpFlowControl.getSuId() + "", DateUtil.formatDate(new Date()), JSON.toJSONString(erpSaleFlowDTOList));
                if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                    throw new BusinessException(OpenErrorCode.ERP_PURCHASE_FLOW_ERROR);
                }
            }
        }
        countList.add(handleCount);
        countList.add(failCount);
        return countList;
    }


    // 验证文件是否完整
    public boolean verifyFile(String md5, File file) {
        boolean isVerify = false;
        String fileMd5 = com.yiling.framework.oss.util.FileUtil.fileMd5(cn.hutool.core.io.FileUtil.readBytes(file));
        if (fileMd5.equals(md5)) {
            isVerify = true;
        } else {
            log.info("验证oss文件md5错误, controlMd5：{}, fileMd5：{}", md5, fileMd5);
        }
        return isVerify;
    }

    @Override
    public ErpEntityMapper getErpEntityDao() {
        return erpFlowControlMapper;
    }

    /**
     * @param list
     * @return map
     */
    public List<List<ErpSaleFlowDTO>> groupErpSaleFlowDTOList(List<ErpSaleFlowDTO> list, Integer index) {
        //listSize为集合长度
        int listSize = list.size();
        //用map存起来新的分组后数据
        List<List<ErpSaleFlowDTO>> returnList = new ArrayList<>();
        for (int i = 0; i < list.size(); i += index) {
            //作用为Index最后没有1000条数据，则剩余的条数newList中就装几条
            if (i + index > listSize) {
                index = listSize - i;
            }
            //使用subList方法，keyToken用来记录循环了多少次或者每个map数据的键值
            List newList = list.subList(i, i + index);
            //每取一次放到map集合里，然后
            returnList.add(newList);
        }
        return returnList;
    }

    /**
     * @param list
     * @return map
     */
    public List<List<ErpShopSaleFlowDTO>> groupErpShopSaleFlowDTOList(List<ErpShopSaleFlowDTO> list, Integer index) {
        //listSize为集合长度
        int listSize = list.size();
        //用map存起来新的分组后数据
        List<List<ErpShopSaleFlowDTO>> returnList = new ArrayList<>();
        for (int i = 0; i < list.size(); i += index) {
            //作用为Index最后没有1000条数据，则剩余的条数newList中就装几条
            if (i + index > listSize) {
                index = listSize - i;
            }
            //使用subList方法，keyToken用来记录循环了多少次或者每个map数据的键值
            List newList = list.subList(i, i + index);
            //每取一次放到map集合里，然后
            returnList.add(newList);
        }
        return returnList;
    }

    /**
     * @param list
     * @return map
     */
    public List<List<ErpPurchaseFlowDTO>> groupErpPurchaseFlowDTOList(List<ErpPurchaseFlowDTO> list, Integer index) {
        //listSize为集合长度
        int listSize = list.size();
        //用map存起来新的分组后数据
        List<List<ErpPurchaseFlowDTO>> returnList = new ArrayList<>();
        for (int i = 0; i < list.size(); i += index) {
            //作用为Index最后没有1000条数据，则剩余的条数newList中就装几条
            if (i + index > listSize) {
                index = listSize - i;
            }
            //使用subList方法，keyToken用来记录循环了多少次或者每个map数据的键值
            List newList = list.subList(i, i + index);
            //每取一次放到map集合里，然后
            returnList.add(newList);
        }
        return returnList;
    }

    /**
     * 1获取商业公司第一次创建时间
     * 2任务创建时间在第一次创建时间超过3天上传非当前时间任务
     *
     * @param erpFlowControl
     * @param erpSaleFlowDTO
     * @param erpClient
     */
    public void monitorData(ErpFlowControlDO erpFlowControl, ErpSaleFlowDTO erpSaleFlowDTO,ErpClientDTO erpClient,Integer operType,List<MonitorAbnormalDataDO> monitorAbnormalDataDOList) {
        Date initDate = erpFlowControlService.getInitDateBySuIdAndSuDeptNo(erpFlowControl.getSuId(), erpFlowControl.getSuDeptNo());
        if(initDate==null||erpFlowControl.getAddTime()==null){
            log.info("监控异常数据，时间有为null的情况。initDate={},addTime={}",initDate,erpFlowControl.getAddTime());
            return;
        }

        if (DateUtil.between(initDate, erpFlowControl.getAddTime(), DateUnit.DAY) <= 3) {
            log.info("监控异常数据，添加数据大于初始数据initDate={},addTime={}",initDate,erpFlowControl.getAddTime());
            return;
        }

        if (DateUtil.between(erpSaleFlowDTO.getSoTime(), erpFlowControl.getAddTime(), DateUnit.DAY) <= 3) {
            log.info("监控异常数据，soTime={},addTime={}",erpSaleFlowDTO.getSoTime(),erpFlowControl.getAddTime());
            return;
        }

        //不是初次对接并且销售流向超过3天又重新上传的流向数
//        ErpSaleFlowDO erpSaleFlowOld = null;
//        if (StrUtil.isNotEmpty(erpSaleFlowDTO.getSoNo())) {
//            QueryWrapper<ErpSaleFlowDO> queryWrapper = new QueryWrapper<>();
//            queryWrapper.lambda().eq(ErpSaleFlowDO::getSoNo, erpSaleFlowDTO.getSoNo())
//                    .eq(ErpSaleFlowDO::getSuId, erpFlowControl.getSuId())
//                    .eq(ErpSaleFlowDO::getSuDeptNo, erpFlowControl.getSuDeptNo())
//                    .in(ErpSaleFlowDO::getOperType, Arrays.asList(1, 2))
//                    .ne(ErpSaleFlowDO::getSoId, erpSaleFlowDTO.getSoId());
//            List<ErpSaleFlowDO> list = erpSaleFlowMapper.selectList(queryWrapper);
//            if (CollUtil.isNotEmpty(list)) {
//                erpSaleFlowOld = list.get(0);
//            }
//        }

        MonitorAbnormalDataDO monitorAbnormalDataDO = new MonitorAbnormalDataDO();
        monitorAbnormalDataDO.setControlId(erpFlowControl.getId());
        monitorAbnormalDataDO.setEid(erpClient.getRkSuId());
        monitorAbnormalDataDO.setEname(erpClient.getClientName());
        monitorAbnormalDataDO.setFlowTime(erpSaleFlowDTO.getSoTime());
        monitorAbnormalDataDO.setSoId(erpSaleFlowDTO.getSoId());
        monitorAbnormalDataDO.setSoNo(erpSaleFlowDTO.getSoNo());
        monitorAbnormalDataDO.setUploadTime(erpFlowControl.getAddTime());
        monitorAbnormalDataDO.setDataType(1);
        monitorAbnormalDataDO.setOperType(operType);
        monitorAbnormalDataDOList.add(monitorAbnormalDataDO);
//        monitorAbnormalDataService.insertMonitorAbnormalData(monitorAbnormalDataDO);

//        if (erpSaleFlowOld != null) {
//            MonitorAbnormalDataDO monitorAbnormalData = new MonitorAbnormalDataDO();
//            monitorAbnormalData.setParentId(monitorAbnormalDataDO.getId());
//            monitorAbnormalData.setDataType(2);
//            monitorAbnormalData.setUploadTime(erpSaleFlowOld.getAddTime());
//            monitorAbnormalData.setSoNo(erpSaleFlowOld.getSoNo());
//            monitorAbnormalData.setSoId(erpSaleFlowOld.getSoId());
//            monitorAbnormalData.setFlowTime(erpSaleFlowOld.getSoTime());
//            monitorAbnormalData.setControlId(erpSaleFlowOld.getControlId());
//            monitorAbnormalData.setEid(erpClient.getRkSuId());
//            monitorAbnormalData.setEname(erpClient.getClientName());
//            monitorAbnormalData.setOperType(erpSaleFlowOld.getOperType());
//            monitorAbnormalDataService.insertMonitorAbnormalData(monitorAbnormalData);
//        }
    }

    //    public static void main(String[] args) {
    //        String filePath = "https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/prd/erpPurchaseFlow/55710/2022/03/08/96c1aeb8903f42519b868377049c855e.zip";
    //        String tmpDir = "E:\\logs";
    //        String controlMd5 = "0YrnGPPIVDLbNRSTrNFfyQ==";
    //        File zipFile = new File(tmpDir + File.separator + System.currentTimeMillis() + ".zip");
    //        HttpUtil.downloadFile(filePath, zipFile);
    //        if (zipFile == null) {
    //            System.out.println(">>>>> 下载oss文件错误");
    //        }
    //
    //        System.out.println(">>>>> controlMd5：" + controlMd5 + "; ");
    //
    //        String fileMd5 = com.yiling.framework.oss.util.FileUtil.fileMd5(cn.hutool.core.io.FileUtil.readBytes(zipFile));
    //        System.out.println(">>>>> fileMd5：" + fileMd5 + "; ");
    //
    //        if (!fileMd5.equals(controlMd5)) {
    //            System.out.println(">>>>> 验证oss文件错误, controlMd5：" + controlMd5 + "，fileMd5：" + fileMd5);
    //        }
    //
    //        zipFile.delete();
    //    }
}
