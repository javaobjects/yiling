package com.yiling.open.erp.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.yiling.goods.medicine.bo.GoodsLineBO;
import com.yiling.goods.medicine.enums.GoodsLineStatusEnum;
import com.yiling.open.erp.dto.ErpClientDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.pricing.goods.api.GoodsPriceCustomerGroupApi;
import com.yiling.pricing.goods.dto.GoodsPriceCustomerGroupDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceGroupRequest;
import com.yiling.pricing.goods.dto.request.SaveOrUpdateGoodsPriceCustomerGroupRequest;
import com.yiling.pricing.goods.enums.GoodsPriceRuleEnum;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dao.ErpEntityMapper;
import com.yiling.open.erp.dao.ErpGoodsGroupPriceMapper;
import com.yiling.open.erp.dto.ErpGoodsGroupPriceDTO;
import com.yiling.open.erp.entity.ErpGoodsGroupPriceDO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.enums.SyncStatus;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpGoodsGroupPriceService;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.CustomerGroupApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerGroupDTO;
import com.yiling.user.enterprise.dto.EnterprisePlatformDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Slf4j
@Service(value = "erpGoodsGroupPriceService")
public class ErpGoodsGroupPriceServiceImpl extends ErpEntityServiceImpl implements ErpGoodsGroupPriceService {

    @Resource
    private ErpClientService erpClientService;
    @Resource
    private ErpGoodsGroupPriceMapper erpGoodsGroupPriceMapper;
    @DubboReference
    private GoodsApi goodsApi;
    @DubboReference
    private PopGoodsApi popGoodsApi;
    @DubboReference
    private EnterpriseApi enterpriseApi;
    @DubboReference
    private InventoryApi inventoryApi;
    @DubboReference
    private CustomerApi customerApi;
    @DubboReference
    private CustomerGroupApi customerGroupApi;
    @DubboReference
    private GoodsPriceCustomerGroupApi goodsPriceCustomerGroupApi;
    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;
    @Resource
    protected RedisDistributedLock redisDistributedLock;

    @Override
    public boolean onlineData(BaseErpEntity baseErpEntity) {
        ErpGoodsGroupPriceDO erpGoodsGroupPriceDO = (ErpGoodsGroupPriceDO) baseErpEntity;
        // 1.查询规则信息
        ErpClientDTO erpClient = erpClientService.getErpClientBySuIdAndSuDeptNo(erpGoodsGroupPriceDO.getSuId(), erpGoodsGroupPriceDO.getSuDeptNo());
        if (erpClient == null || erpClient.getSyncStatus() == null || erpClient.getSyncStatus() == 0) {
            erpGoodsGroupPriceMapper.updateSyncStatusAndMsg(erpGoodsGroupPriceDO.getId(), SyncStatus.UNSYNC.getCode(), "未开启同步规则");
            return false;
        }
        // erpGoodsBatch.getOperType() == 3 删除逻辑
        if (erpGoodsGroupPriceDO.getOperType() == 3) {
            erpGoodsGroupPriceDO = erpGoodsGroupPriceMapper.selectById(erpGoodsGroupPriceDO.getId());
            erpGoodsGroupPriceDO.setOperType(3);
            if (erpGoodsGroupPriceDO == null) {
                // 如果是删除商品库存信息，但是查询op库数据为空
                erpGoodsGroupPriceMapper.updateSyncStatusAndMsg(erpGoodsGroupPriceDO.getId(), 3, "查询分组定价信息为空");
                return false;
            }
        }
        return syncGoodsGroupPrice(erpGoodsGroupPriceDO, erpClient);
    }

    @Override
    public void syncGoodsGroupPrice() {
        List<ErpGoodsGroupPriceDO> erpGoodsGroupPriceList = erpGoodsGroupPriceMapper.syncGoodsGroupPrice();
        for (ErpGoodsGroupPriceDO erpGoodsGroupPriceDO : erpGoodsGroupPriceList) {
            int i = erpGoodsGroupPriceMapper.updateSyncStatusByStatusAndId(erpGoodsGroupPriceDO.getId(), SyncStatus.SYNCING.getCode(), SyncStatus.UNSYNC.getCode(), "job处理");
            if (i > 0) {
                onlineData(erpGoodsGroupPriceDO);
            }
        }
    }

    @Override
    public void refreshErpInventoryList(List<String> inSnList, Long eid) {
        try {
            ErpClientDTO erpClient = erpClientService.selectByRkSuId(eid);

            List<ErpGoodsGroupPriceDO> erpGoodsGroupPriceDOList = erpGoodsGroupPriceMapper.findBySyncStatusAndInSnList(inSnList, erpClient.getSuDeptNo(), erpClient.getSuId());
            if (CollUtil.isEmpty(erpGoodsGroupPriceDOList)) {
                return;
            }

            for (ErpGoodsGroupPriceDO erpGoodsGroupPriceDO : erpGoodsGroupPriceDOList) {
                erpGoodsGroupPriceMapper.updateSyncStatusAndMsg(erpGoodsGroupPriceDO.getId(), SyncStatus.SYNCING.getCode(), "商品审核重新处理");
                SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpGroupPrice.getTopicName(), erpClient.getSuId() + "", DateUtil.formatDate(new Date()), JSON.toJSONString(Arrays.asList(PojoUtils.map(erpGoodsGroupPriceDO, ErpGoodsGroupPriceDTO.class))));
                if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                    erpGoodsGroupPriceMapper.updateSyncStatusAndMsg(erpGoodsGroupPriceDO.getId(), SyncStatus.UNSYNC.getCode(), "mq发送失败，未处理");
                }
            }
        } catch (Exception e) {
            log.error("客户分组定价重新刷新报错:", e);
        }
    }

    @Override
    public Integer updateOperTypeGoodsBatchFlowBySuId(Long suId) {
        return erpGoodsGroupPriceMapper.updateOperTypeGoodsBatchFlowBySuId(suId);
    }

    public boolean syncGoodsGroupPrice(ErpGoodsGroupPriceDO erpGoodsGroupPriceDO, ErpClientDTO erpClient) {
        Long id = erpGoodsGroupPriceDO.getId();
        try {
            String inSn = erpGoodsGroupPriceDO.getInSn();
            if (StrUtil.isBlank(inSn)) {
                // 如果更新失败 ，则改变erp商品库中商品同步状态为失败
                erpGoodsGroupPriceMapper.updateSyncStatusAndMsg(id, 3, "商品内码为空");
                return false;
            }

            String groupName = erpGoodsGroupPriceDO.getGroupName();
            if (StrUtil.isBlank(groupName)) {
                // 如果更新失败 ，则改变erp商品库中商品同步状态为失败
                erpGoodsGroupPriceMapper.updateSyncStatusAndMsg(id, 3, "客户分组信息为空");
                return false;
            }

            EnterpriseCustomerGroupDTO enterpriseCustomerGroupDTO = customerGroupApi.getByEidAndName(erpClient.getRkSuId().longValue(), erpGoodsGroupPriceDO.getGroupName());
            // 如果更新失败 ，则改变erp商品库中商品同步状态为失败
            if (enterpriseCustomerGroupDTO == null) {
                erpGoodsGroupPriceMapper.updateSyncStatusAndMsg(id, 3, "平台分组信息为空");
                return false;
            }

            Long eid = Constants.YILING_EID;
            //如果客户定价为以岭suId默认赋值为1
            List<Long> list = enterpriseApi.listSubEids(Constants.YILING_EID);
            if (!list.contains(erpClient.getRkSuId().longValue())) {
                eid = erpClient.getRkSuId().longValue();
            }

            if (erpGoodsGroupPriceDO.getPrice().compareTo(BigDecimal.ZERO) == 0) {
                // 如果更新失败 ，则改变erp商品库中商品同步状态为失败
                erpGoodsGroupPriceMapper.updateSyncStatusAndMsg(id, 3, "同步价格不能为0");
                return false;
            }

            GoodsDTO goodsDTO = goodsApi.findGoodsAuditPassByInSnAndEidAndGoodsLine(erpClient.getRkSuId().longValue(), erpGoodsGroupPriceDO.getInSn(), null);
            if (goodsDTO == null) {
                // 如果更新失败 ，则改变erp商品库中商品同步状态为失败
                erpGoodsGroupPriceMapper.updateSyncStatusAndMsg(id, 3, "商品平台为空");
                return false;
            }

            GoodsLineBO goodsLineBO = goodsApi.getGoodsLineByGoodsIds(ListUtil.toList(goodsDTO.getId())).get(0);
            if (GoodsLineStatusEnum.NOT_ENABLED.getCode().equals(goodsLineBO.getMallStatus()) && GoodsLineStatusEnum.NOT_ENABLED.getCode().equals(goodsLineBO.getPopStatus())) {
                // 如果更新失败 ，则改变erp商品库中商品同步状态为失败
                erpGoodsGroupPriceMapper.updateSyncStatusAndMsg(id, 3, "商品未开通任何产品线");
                return false;
            }

            //先判断商业公司开通的产品线
            EnterprisePlatformDTO enterprisePlatformDTO = enterpriseApi.getEnterprisePlatform(erpClient.getRkSuId().longValue());
            Integer popFlag = enterprisePlatformDTO.getPopFlag();
            //开通了pop产品线
            GoodsDTO goodsPopDTO = null;
            if (popFlag != null && popFlag == 1 && goodsLineBO.getPopStatus() == 1) {
                goodsPopDTO = goodsApi.findGoodsAuditPassByInSnAndEidAndGoodsLine(erpClient.getRkSuId().longValue(), erpGoodsGroupPriceDO.getInSn(), GoodsLineEnum.POP.getCode());
                if (goodsPopDTO == null) {
                    erpGoodsGroupPriceMapper.updateSyncStatusAndMsg(id, 3, "pop商品平台为空");
                    return false;
                }
            }
            Integer mallFlag = enterprisePlatformDTO.getMallFlag();
            //开通了pop产品线
            GoodsDTO goodsB2bDTO = null;
            if (mallFlag != null && mallFlag == 1 && goodsLineBO.getMallStatus() == 1) {
                goodsB2bDTO = goodsApi.findGoodsAuditPassByInSnAndEidAndGoodsLine(erpClient.getRkSuId().longValue(), erpGoodsGroupPriceDO.getInSn(), GoodsLineEnum.B2B.getCode());
                if (goodsB2bDTO == null) {
                    erpGoodsGroupPriceMapper.updateSyncStatusAndMsg(id, 3, "b2b商品平台为空");
                    return false;
                }
            }
            if (goodsPopDTO != null) {
                executePrice(eid, erpGoodsGroupPriceDO, enterpriseCustomerGroupDTO, goodsPopDTO, GoodsLineEnum.POP.getCode());
            }
            if (goodsB2bDTO != null) {
                executePrice(eid, erpGoodsGroupPriceDO, enterpriseCustomerGroupDTO, goodsB2bDTO, GoodsLineEnum.B2B.getCode());
            }
            erpGoodsGroupPriceMapper.updateSyncStatusAndMsg(id, SyncStatus.SUCCESS.getCode(), "同步成功");
            return true;
        } catch (Exception e) {
            erpGoodsGroupPriceMapper.updateSyncStatusAndMsg(id, SyncStatus.FAIL.getCode(), "系统异常");
            log.error("客户分组定价同步出现错误", e);
        }
        return false;
    }

    public void executePrice(Long eid, ErpGoodsGroupPriceDO erpGoodsGroupPriceDO, EnterpriseCustomerGroupDTO enterpriseCustomerGroupDTO, GoodsDTO goodsDTO, Integer goodsLine) {
        long start = System.currentTimeMillis();
        String lockName = "mph-erp-online-lock-excute-goods-group:" + eid + "-" + enterpriseCustomerGroupDTO.getId() + "-" + goodsDTO.getId() + "-" + goodsLine;
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock(lockName, 60, 60, TimeUnit.SECONDS);
            if (StringUtils.isEmpty(lockId)) {
                throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "系统繁忙，获取锁失败，lockName:" + lockName);
            }
            QueryGoodsPriceGroupRequest queryRequest = new QueryGoodsPriceGroupRequest();
            queryRequest.setEid(eid);
            queryRequest.setCustomerGroupId(enterpriseCustomerGroupDTO.getId());
            queryRequest.setGoodsId(goodsDTO.getId());
            queryRequest.setGoodsLine(goodsLine);
            GoodsPriceCustomerGroupDTO goodsPriceCustomerGroupDTO = goodsPriceCustomerGroupApi.get(queryRequest);
            Long priceId = 0L;
            if (goodsPriceCustomerGroupDTO != null) {
                priceId = goodsPriceCustomerGroupDTO.getId();
            }
            //查询客户定价
            if (erpGoodsGroupPriceDO.getOperType() == 1 || erpGoodsGroupPriceDO.getOperType() == 2) {
                //增加和修改客户定价
                SaveOrUpdateGoodsPriceCustomerGroupRequest request = new SaveOrUpdateGoodsPriceCustomerGroupRequest();
                request.setOpUserId(0L);
                request.setId(priceId);
                request.setEid(eid);
                request.setCustomerGroupId(enterpriseCustomerGroupDTO.getId());
                request.setGoodsId(goodsDTO.getId());
                request.setPriceRule(GoodsPriceRuleEnum.SPECIFIC_PRICE.getCode());
                request.setPriceValue(erpGoodsGroupPriceDO.getPrice());
                request.setGoodsLine(goodsLine);
                goodsPriceCustomerGroupApi.saveOrUpdate(request);
            } else {
                if (goodsPriceCustomerGroupDTO != null) {
                    //删除客户定价
                    goodsPriceCustomerGroupApi.removeById(goodsPriceCustomerGroupDTO.getId());
                }
            }
        } catch (InterruptedException e) {
            throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "客户分组定价同步出错，lockName:" + lockName);
        } finally {
            long time = System.currentTimeMillis() - start;
            if (time > 3000) {
                log.warn("客户分组定价同步耗时:{}ms,lockName:", time, lockName);
            }
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }

    @Override
    public ErpEntityMapper getErpEntityDao() {
        return erpGoodsGroupPriceMapper;
    }

}
