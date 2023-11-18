package com.yiling.open.erp.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.goods.medicine.api.GoodsAuditApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsRefreshDTO;
import com.yiling.goods.medicine.dto.request.SaveGoodsLineRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsAuditRequest;
import com.yiling.goods.medicine.dto.request.UpdateAuditStatusByGoodsIdRequest;
import com.yiling.goods.medicine.enums.GoodsAuditStatusEnum;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.enums.GoodsSourceEnum;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.service.ErpGoodsBatchService;
import com.yiling.open.erp.service.ErpGoodsCustomerPriceService;
import com.yiling.open.erp.service.ErpGoodsGroupPriceService;
import com.yiling.open.erp.service.ErpOrderSendService;
import com.yiling.user.enterprise.dto.EnterprisePlatformDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsRequest;
import com.yiling.goods.medicine.enums.GoodsOutReasonEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dao.ErpEntityMapper;
import com.yiling.open.erp.dao.ErpGoodsMapper;
import com.yiling.open.erp.dto.ErpGoodsDTO;
import com.yiling.open.erp.entity.ErpGoodsDO;
import com.yiling.open.erp.enums.SyncStatus;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpGoodsService;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * erp商品同步
 *
 * @Filename: ErpGoodsServiceImpl.java
 * @Version: 1.0
 * @Author: shuang.zhang 张爽
 */
@Slf4j
@Service(value = "erpGoodsService")
public class ErpGoodsServiceImpl extends ErpEntityServiceImpl implements ErpGoodsService {

    @Autowired
    private ErpGoodsMapper erpGoodsMapper;
    @DubboReference(timeout = 1000 * 60)
    private GoodsApi       goodsApi;
    @DubboReference
    private GoodsAuditApi  goodsAuditApi;
    @DubboReference
    private PopGoodsApi    popGoodsApi;
    @DubboReference
    private EnterpriseApi  enterpriseApi;

    @Autowired
    private ErpClientService             erpClientService;
    @Autowired
    private ErpOrderSendService          erpOrderSendService;
    @Autowired(required = false)
    private RocketMqProducerService      rocketMqProducerService;
    @Autowired
    private ErpGoodsCustomerPriceService erpGoodsCustomerPriceService;
    @Autowired
    private ErpGoodsGroupPriceService    erpGoodsGroupPriceService;
    @Autowired
    private ErpGoodsBatchService         erpGoodsBatchService;

    @Override
    public boolean onlineData(BaseErpEntity baseErpEntity) {
        ErpGoodsDO erpGoodsDO = (ErpGoodsDO) baseErpEntity;
        //1.查询规则信息
        ErpClientDTO erpClient = erpClientService.getErpClientBySuIdAndSuDeptNo(erpGoodsDO.getSuId(), erpGoodsDO.getSuDeptNo());
        if (erpClient == null || erpClient.getSyncStatus() == null || erpClient.getSyncStatus() == 0) {
            erpGoodsMapper.updateSyncStatusAndMsg(erpGoodsDO.getId(), SyncStatus.UNSYNC.getCode(), "未开启同步规则");
            return false;
        }
        return synErpGoods(erpGoodsDO, erpClient);
    }

    @Override
    public void syncGoods() {
        List<ErpGoodsDO> goodsList = erpGoodsMapper.syncGoods();
        for (ErpGoodsDO erpGoods : goodsList) {
            int i = erpGoodsMapper.updateSyncStatusByStatusAndId(erpGoods.getId(), SyncStatus.SYNCING.getCode(), SyncStatus.UNSYNC.getCode(), "job处理");
            if (i > 0) {
                onlineData(erpGoods);
            }
        }
    }

    @Override
    public List<ErpGoodsDTO> getErpGoodsByInSnAndSuIdAndSuDeptNo(List<String> inSnList, Long suId, String suDeptNo) {
        List<ErpGoodsDO> erpGoodsDOList = erpGoodsMapper.findBySyncStatusAndInSnList(inSnList, suDeptNo, suId);
        if (CollUtil.isNotEmpty(erpGoodsDOList)) {
            return PojoUtils.map(erpGoodsDOList, ErpGoodsDTO.class);
        }
        return null;
    }

    @Override
    public void refreshErpInventoryList(List<String> inSnList, Long eid) {
        try {
            ErpClientDTO erpClient = erpClientService.selectByRkSuId(eid);

            List<ErpGoodsDO> erpGoodsDOList = erpGoodsMapper.findBySyncStatusAndInSnList(inSnList, erpClient.getSuDeptNo(), erpClient.getSuId());
            if (CollUtil.isEmpty(erpGoodsDOList)) {
                return;
            }

            for (ErpGoodsDO erpGoodsDO : erpGoodsDOList) {
                erpGoodsMapper.updateSyncStatusAndMsg(erpGoodsDO.getId(), SyncStatus.SYNCING.getCode(), "商品审核重新处理");
                SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpGoods.getTopicName(), erpClient.getSuId() + "", DateUtil.formatDate(new Date()), JSON.toJSONString(Arrays.asList(PojoUtils.map(erpGoodsDO, ErpGoodsDTO.class))));
                if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                    erpGoodsMapper.updateSyncStatusAndMsg(erpGoodsDO.getId(), SyncStatus.UNSYNC.getCode(), "mq发送失败，未处理");
                }
            }
        } catch (Exception e) {
            log.error("ERP商品重新刷新报错:", e);
        }
    }

    @Override
    public Integer updateOperTypeGoodsBatchFlowBySuId(Long suId) {
        return erpGoodsMapper.updateOperTypeGoodsBatchFlowBySuId(suId);
    }

    public boolean synErpGoods(ErpGoodsDO erpGoods, ErpClientDTO erpClient) {
        Long id = erpGoods.getId();
        log.info("synErpGoods, EAS goodsId -> {}", erpGoods.getId());
        try {
            String inSn = erpGoods.getInSn();
            if (StrUtil.isBlank(inSn)) {
                // 如果更新失败 ，则改变erp商品库中商品同步状态为失败
                erpGoodsMapper.updateSyncStatusAndMsg(id, 3, "商品内码为空");
                return false;
            }

            //如果销售包装为空默认为1
            if (erpGoods.getMiddlePackage() == null || erpGoods.getMiddlePackage() == 0) {
                erpGoods.setMiddlePackage(1);
            }

            // 处理对象属性里面的特殊字符（商品名称和商品厂家）
            if (erpGoods.getOperType() != 3) {
                if (erpGoods.getPrice() == null || erpGoods.getPrice().doubleValue() <= 0.01) {
                    // 如果更新失败 ，则改变erp商品库中商品同步状态为失败
                    erpGoodsMapper.updateSyncStatusAndMsg(id, 3, "商品价格过低或者为零");
                    return false;
                }
            }

            GoodsDTO goodsDTO = goodsApi.findGoodsByInSnAndEidAndGoodsLine(erpClient.getRkSuId().longValue(), inSn, null);
            //先判断商业公司开通的产品线
            SaveGoodsLineRequest goodsLineRequest = new SaveGoodsLineRequest();
            EnterprisePlatformDTO enterprisePlatformDTO = enterpriseApi.getEnterprisePlatform(erpClient.getRkSuId().longValue());
            if(enterprisePlatformDTO==null){
                erpGoodsMapper.updateSyncStatusAndMsg(id, 3, "商业公司没有开通产品线");
                return false;
            }

            Integer popFlag = enterprisePlatformDTO.getPopFlag();
            //开通了pop产品线
            if (popFlag != null && popFlag == 1) {
                goodsLineRequest.setPopFlag(1);
            }
            Integer mallFlag = enterprisePlatformDTO.getMallFlag();
            //开通了b2b产品线
            if (mallFlag != null && mallFlag == 1) {
                goodsLineRequest.setMallFlag(1);
            }

            // 删除
            if (erpGoods.getOperType() == 3) {
                if (goodsDTO != null) {
                    if (goodsDTO.getAuditStatus().equals(GoodsStatusEnum.AUDIT_PASS.getCode())) {
                        goodsApi.updateSkuStatusByEidAndInSn(erpClient.getRkSuId().longValue(), inSn, GoodsSkuStatusEnum.DISABLE.getCode(),0L);
                    }
                    //变成驳回
                    if (goodsDTO.getAuditStatus().equals(GoodsStatusEnum.UNDER_REVIEW.getCode())) {
                        UpdateAuditStatusByGoodsIdRequest request = new UpdateAuditStatusByGoodsIdRequest();
                        request.setId(goodsDTO.getId());
                        request.setAuditStatus(GoodsStatusEnum.REJECT.getCode());
                        request.setOpUserId(0L);
                        goodsApi.updateAuditStatusByGoodsId(request);

                        SaveOrUpdateGoodsAuditRequest saveOrUpdateGoodsAuditRequest = new SaveOrUpdateGoodsAuditRequest();
                        saveOrUpdateGoodsAuditRequest.setAuditStatus(GoodsAuditStatusEnum.NOT_PASS_AUDIT.getCode());
                        saveOrUpdateGoodsAuditRequest.setGid(goodsDTO.getId());
                        saveOrUpdateGoodsAuditRequest.setOpUserId(0L);
                        goodsAuditApi.notPassGoodsAudit(saveOrUpdateGoodsAuditRequest);
                    }
                }
                erpGoodsMapper.updateSyncStatusByStatusAndId(erpGoods.getId(), SyncStatus.SUCCESS.getCode(), SyncStatus.SYNCING.getCode(), "同步成功");
                return true;
            }

            // 如果在cbs中根据suid 和gsn查询到商品 则修改 否则添加
            if (goodsDTO != null) {
                updateCbsGoods(erpGoods, goodsDTO, goodsLineRequest);
            } else {
                // 添加
                insertCbsGoods(erpGoods, erpClient, goodsLineRequest);
            }
            return true;
        }catch (BusinessException be){
            erpGoodsMapper.updateSyncStatusAndMsg(erpGoods.getId(), SyncStatus.FAIL.getCode(), be.getMessage());
        }catch (Exception e) {
            erpGoodsMapper.updateSyncStatusAndMsg(erpGoods.getId(), SyncStatus.FAIL.getCode(), "系统异常");
            log.error("ERP商品同步出现错误", e);
        }
        return false;
    }


    public Boolean insertCbsGoods(ErpGoodsDO erpGoods, ErpClientDTO erpClient, SaveGoodsLineRequest goodsLineInfo) {
        SaveOrUpdateGoodsRequest goodsSyncDto = new SaveOrUpdateGoodsRequest();
        goodsSyncDto.setName(erpGoods.getName());
        goodsSyncDto.setCommonName(erpGoods.getCommonName());
        goodsSyncDto.setLicenseNo(erpGoods.getLicenseNo());
        goodsSyncDto.setSpecifications(erpGoods.getSpecifications());
        goodsSyncDto.setUnit(erpGoods.getUnit());
        goodsSyncDto.setSource(GoodsSourceEnum.ERP.getCode());
        Integer gMiddlePackage = erpGoods.getMiddlePackage();
        Integer gBigPackage = erpGoods.getBigPackage();
        goodsSyncDto.setMiddlePackage(gMiddlePackage.longValue());
        goodsSyncDto.setBigPackage(gBigPackage.longValue());
        goodsSyncDto.setSn(erpGoods.getSn());
        goodsSyncDto.setInSn(erpGoods.getInSn());
        goodsSyncDto.setEid(erpClient.getRkSuId().longValue());
        goodsSyncDto.setManufacturer(erpGoods.getManufacturer());
        goodsSyncDto.setManufacturerCode(erpGoods.getManufacturerCode());
        goodsSyncDto.setPrice(erpGoods.getPrice());
        goodsSyncDto.setCanSplit(erpGoods.getCanSplit());

        // 状态上下架
        Integer offlineReason = GoodsOutReasonEnum.REJECT.getCode();
        // 默认下架
        Integer goodsStatus = ObjectUtil.isNull(erpGoods.getGoodsStatus()) || 0 == erpGoods.getGoodsStatus() ? GoodsStatusEnum.UN_SHELF.getCode() : erpGoods.getGoodsStatus();

        goodsSyncDto.setOutReason(offlineReason);
        goodsSyncDto.setGoodsStatus(goodsStatus);
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(erpClient.getRkSuId().longValue());
        goodsSyncDto.setEid(enterpriseDTO.getId());
        goodsSyncDto.setEname(enterpriseDTO.getName());
        goodsSyncDto.setOpUserId(0L);
        goodsSyncDto.setGoodsLineInfo(goodsLineInfo);
        goodsSyncDto.setPopEidList(enterpriseApi.listSubEids(Constants.YILING_EID));
        // 添加商品
        Long goodsSyncInsert = goodsApi.saveGoodsByErp(goodsSyncDto);
        if (goodsSyncInsert == null || goodsSyncInsert <= 0) {
            log.error("ERP商品同步，插入平台报错");
            return false;
        }
        GoodsRefreshDTO goodsRefreshDTO = new GoodsRefreshDTO();
        goodsRefreshDTO.setEid(erpClient.getRkSuId());
        goodsRefreshDTO.setInSnList(Arrays.asList(erpGoods.getInSn()));
        erpGoodsCustomerPriceService.refreshErpInventoryList(goodsRefreshDTO.getInSnList(),Long.parseLong(String.valueOf(goodsRefreshDTO.getEid())));
        erpGoodsGroupPriceService.refreshErpInventoryList(goodsRefreshDTO.getInSnList(),Long.parseLong(String.valueOf(goodsRefreshDTO.getEid())));
        erpGoodsBatchService.refreshErpInventoryList(goodsRefreshDTO.getInSnList(),Long.parseLong(String.valueOf(goodsRefreshDTO.getEid())));
//        rocketMqProducerService.sendSync(Constants.TOPIC_GOODS_REFRESH, Constants.TAG_GOODS_REFRESH,Long.toString(goodsSyncInsert) ,JSON.toJSONString(goodsRefreshDTO));
        erpOrderSendService.refreshErpInventoryList(Arrays.asList(erpGoods.getInSn()),erpClient.getRkSuId());
        erpGoodsMapper.updateSyncStatusByStatusAndId(erpGoods.getId(), SyncStatus.SUCCESS.getCode(), SyncStatus.SYNCING.getCode(), "同步成功");
        return true;
    }


    public Boolean updateCbsGoods(ErpGoodsDO erpGoods, GoodsDTO goodsInfoDto, SaveGoodsLineRequest goodsLineInfo) {
        SaveOrUpdateGoodsRequest goods = new SaveOrUpdateGoodsRequest();
        // 状态上下架
        Integer offlineReason = GoodsOutReasonEnum.REJECT.getCode();
        // 默认下架
        Integer goodsStatus = ObjectUtil.isNull(erpGoods.getGoodsStatus()) || 0 == erpGoods.getGoodsStatus()? GoodsStatusEnum.UN_SHELF.getCode() : erpGoods.getGoodsStatus();

        goods.setId(goodsInfoDto.getId());
        goods.setEid(goodsInfoDto.getEid());
        goods.setEname(goodsInfoDto.getEname());
        goods.setInSn(erpGoods.getInSn());
        goods.setSn(erpGoods.getSn());
        goods.setOutReason(offlineReason);
        goods.setGoodsStatus(goodsStatus);
        goods.setPrice(erpGoods.getPrice());
        goods.setName(erpGoods.getName());
        goods.setSpecifications(erpGoods.getSpecifications());
        goods.setUnit(erpGoods.getUnit());
        goods.setLicenseNo(erpGoods.getLicenseNo());
        goods.setManufacturer(erpGoods.getManufacturer());
        goods.setManufacturerCode(erpGoods.getManufacturerCode());
        goods.setManufacturerAddress(erpGoods.getManufacturerAddress());
        goods.setSource(GoodsSourceEnum.ERP.getCode());
        goods.setCommonName(erpGoods.getCommonName());
        goods.setAliasName(erpGoods.getAliasName());
        Integer gMiddlePackage = erpGoods.getMiddlePackage();
        Integer gBigPackage = erpGoods.getBigPackage();
        goods.setMiddlePackage(gMiddlePackage.longValue());
        goods.setBigPackage(gBigPackage.longValue());
        goods.setCanSplit(erpGoods.getCanSplit());
        goods.setGoodsLineInfo(goodsLineInfo);
        goods.setPopEidList(enterpriseApi.listSubEids(Constants.YILING_EID));
        goods.setOpUserId(0L);
        //恢复sku为正常状态
        goodsApi.updateSkuStatusByEidAndInSn(goodsInfoDto.getEid(), erpGoods.getInSn(), GoodsSkuStatusEnum.NORMAL.getCode(),0L);
        // 修改商品信息
        goodsApi.saveGoodsByErp(goods);
        erpGoodsMapper.updateSyncStatusByStatusAndId(erpGoods.getId(), SyncStatus.SUCCESS.getCode(), SyncStatus.SYNCING.getCode(), "同步成功");
        return true;
    }


    /**
     * 检查erp中商品是否符合同步条件 商品名、规格、厂家、批文不能为空 商品规格不包含 原材药 商品名 不包含 赠、 禁销、限销、 送、 返、
     * 开票员、禁开、 不做、 原料药 字段
     *
     * @param erpg
     * @return
     */
    public String checkSyncGoods(ErpGoodsDO erpg) {
        String erpGName = erpg.getName();
        String erpGSpec = erpg.getSpecifications();
        String erpGManufacture = erpg.getManufacturer();
        // #没有内码不入库
        // #没有商品名不入库
        // #没有规格不入库
        // #没有批文和厂家不入库
        // #原料药不入库
        // #不做/禁销的不入库
        String syncMsg = "";
        if (StrUtil.isEmpty(erpGName)) {
            syncMsg = "商品名称为空";
            return syncMsg;
        } else if (StrUtil.isEmpty(erpGSpec)) {
            syncMsg = "商品规格为空";
            return syncMsg;
        } else if (StrUtil.isEmpty(erpGManufacture)) {
            syncMsg = "生成厂家为空";
            return syncMsg;
        } else if (erpGSpec.indexOf("原料药") > 0) {
            syncMsg = "商品规格包含 原料药 字段";
            return syncMsg;
        } else if (erpGName.indexOf("禁销") > 0) {
            syncMsg = "商品名称包含 禁销 字段";
            return syncMsg;
        } else if (erpGName.indexOf("限销") > 0) {
            syncMsg = "商品名称包含 限销 字段";
            return syncMsg;
        } else if (erpGName.indexOf("开票员禁开") > 0) {
            syncMsg = "商品名称包含 开票员禁开 字段";
            return syncMsg;
        } else if (erpGName.indexOf("不做") > 0) {
            syncMsg = "商品名称包含 不做 字段";
            return syncMsg;
        }
        return syncMsg;
    }


    @Override
    public ErpEntityMapper getErpEntityDao() {
        return erpGoodsMapper;
    }
}
