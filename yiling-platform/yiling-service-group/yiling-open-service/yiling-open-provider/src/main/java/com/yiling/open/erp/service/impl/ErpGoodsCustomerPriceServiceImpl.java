package com.yiling.open.erp.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.yiling.goods.medicine.bo.GoodsLineBO;
import com.yiling.goods.medicine.enums.GoodsLineStatusEnum;
import com.yiling.open.erp.dto.ErpClientDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.kingdee.bos.openapi.third.OpenApi;
import com.kingdee.bos.openapi.third.OpenApiFactory;
import com.kingdee.bos.openapi.third.OpenApiInfo;
import com.kingdee.bos.openapi.third.ctx.CommonLogin;
import com.kingdee.bos.openapi.third.login.EASLoginContext;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.pricing.goods.api.GoodsPriceCustomerApi;
import com.yiling.pricing.goods.dto.GoodsPriceCustomerDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceCustomerRequest;
import com.yiling.pricing.goods.dto.request.SaveOrUpdateGoodsPriceCustomerRequest;
import com.yiling.pricing.goods.enums.GoodsPriceRuleEnum;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dao.ErpEntityMapper;
import com.yiling.open.erp.dao.ErpGoodsCustomerPriceMapper;
import com.yiling.open.erp.dto.ErpGoodsCustomerPriceDTO;
import com.yiling.open.erp.entity.ErpGoodsCustomerPriceDO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.enums.OperTypeEnum;
import com.yiling.open.erp.enums.SyncStatus;
import com.yiling.open.erp.handler.ErpGoodsCustomerPriceHandler;
import com.yiling.open.erp.service.EasIncrementStampService;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpGoodsCustomerPriceService;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.webservice.basic.WSEasBasicDataWebserviceFacadeSrvProxyServiceLocator;
import com.yiling.open.webservice.json.GoodsCustomerPriceJson;
import com.yiling.open.webservice.login.EASLoginProxyServiceLocator;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerEasDTO;
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
@Service(value = "erpGoodsCustomerPriceService")
public class ErpGoodsCustomerPriceServiceImpl extends ErpEntityServiceImpl implements ErpGoodsCustomerPriceService {

    @Resource
    private ErpClientService erpClientService;
    @Resource
    private ErpGoodsCustomerPriceMapper erpGoodsCustomerPriceMapper;
    @Resource
    private ErpGoodsCustomerPriceHandler erpGoodsCustomerPriceHandler;
    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;
    @Autowired
    private EasIncrementStampService easIncrementStampService;
    @Autowired
    private EASLoginProxyServiceLocator easLoginProxyServiceLocator;
    @Autowired
    private WSEasBasicDataWebserviceFacadeSrvProxyServiceLocator wsEasBasicDataWebserviceFacadeSrvProxyServiceLocator;
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
    private GoodsPriceCustomerApi goodsPriceCustomerApi;
    @Resource
    protected RedisDistributedLock redisDistributedLock;
    @Value("${eas.login.webservice.ip}")
    private String ip;
    @Value("${eas.login.webservice.port}")
    private String port;
    @Value("${eas.login.webservice.userName}")
    private String userName;
    @Value("${eas.login.webservice.password}")
    private String password;
    @Value("${eas.login.webservice.language}")
    private String language;
    @Value("${eas.login.webservice.dcName}")
    private String dcName;

    @Override
    public boolean onlineData(BaseErpEntity baseErpEntity) {
        ErpGoodsCustomerPriceDO erpGoodsCustomerPriceDO = (ErpGoodsCustomerPriceDO) baseErpEntity;
        // 1.查询规则信息
        ErpClientDTO erpClient = erpClientService.getErpClientBySuIdAndSuDeptNo(erpGoodsCustomerPriceDO.getSuId(), erpGoodsCustomerPriceDO.getSuDeptNo());
        if (erpClient == null || erpClient.getSyncStatus() == null || erpClient.getSyncStatus() == 0) {
            erpGoodsCustomerPriceMapper.updateSyncStatusAndMsg(erpGoodsCustomerPriceDO.getId(), SyncStatus.UNSYNC.getCode(), "未开启同步规则");
            return false;
        }
        return syncGoodsCustomerPrice(erpGoodsCustomerPriceDO, erpClient);
    }

    @Override
    public void refreshErpInventoryList(List<String> inSnList, Long eid) {
        try {
            ErpClientDTO erpClient = erpClientService.selectByRkSuId(eid);

            List<ErpGoodsCustomerPriceDO> erpGoodsCustomerPriceDOList = erpGoodsCustomerPriceMapper.findBySyncStatusAndInSnList(inSnList, erpClient.getSuDeptNo(), erpClient.getSuId());
            if (CollUtil.isEmpty(erpGoodsCustomerPriceDOList)) {
                return;
            }

            for (ErpGoodsCustomerPriceDO erpGoodsCustomerPriceDO : erpGoodsCustomerPriceDOList) {
                erpGoodsCustomerPriceMapper.updateSyncStatusAndMsg(erpGoodsCustomerPriceDO.getId(), SyncStatus.SYNCING.getCode(), "商品审核重新处理");
                SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpGoodsPrice.getTopicName(), erpClient.getSuId() + "", DateUtil.formatDate(new Date()), JSON.toJSONString(Arrays.asList(PojoUtils.map(erpGoodsCustomerPriceDO, ErpGoodsCustomerPriceDTO.class))));
                if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                    erpGoodsCustomerPriceMapper.updateSyncStatusAndMsg(erpGoodsCustomerPriceDO.getId(), SyncStatus.UNSYNC.getCode(), "mq发送失败，未处理");
                }
            }
        } catch (Exception e) {
            log.error("客户定价重新刷新报错:", e);
        }
    }

    @Override
    public void syncGoodsCustomerPrice() {
        List<ErpGoodsCustomerPriceDO> erpGoodsCustomerPriceList = erpGoodsCustomerPriceMapper.syncGoodsCustomerPrice();
        for (ErpGoodsCustomerPriceDO erpGoodsCustomerPriceDO : erpGoodsCustomerPriceList) {
            int i = erpGoodsCustomerPriceMapper.updateSyncStatusByStatusAndId(erpGoodsCustomerPriceDO.getId(), SyncStatus.SYNCING.getCode(), SyncStatus.UNSYNC.getCode(), "job处理");
            if (i > 0) {
                erpGoodsCustomerPriceHandler.onlineData(erpGoodsCustomerPriceDO);
            }
        }
    }

    public boolean syncGoodsCustomerPrice(ErpGoodsCustomerPriceDO erpGoodsCustomerPriceDO, ErpClientDTO erpClient) {
        Long id = erpGoodsCustomerPriceDO.getId();
        try {
            String inSn = erpGoodsCustomerPriceDO.getInSn();
            if (StrUtil.isBlank(inSn)) {
                // 如果更新失败 ，则改变erp商品库中商品同步状态为失败
                erpGoodsCustomerPriceMapper.updateSyncStatusAndMsg(id, 3, "商品内码为空");
                return false;
            }

            String innerCode = erpGoodsCustomerPriceDO.getInnerCode();
            if (StrUtil.isBlank(innerCode)) {
                // 如果更新失败 ，则改变erp商品库中商品同步状态为失败
                erpGoodsCustomerPriceMapper.updateSyncStatusAndMsg(id, 3, "客户内码为空");
                return false;
            }

            Long customerEid = 0L;
            Long eid = Constants.YILING_EID;
            //如果客户定价为以岭suId默认赋值为1
            List<Long> list = enterpriseApi.listSubEids(Constants.YILING_EID);
            if (list.contains(erpClient.getRkSuId().longValue())) {
                customerEid = customerApi.getCustomerEidByEasCode(Constants.YILING_EID, innerCode);
            } else {
                eid = erpClient.getRkSuId();
                EnterpriseCustomerDTO enterpriseCustomerDTO = customerApi.listByEidAndCustomerErpCode(erpClient.getRkSuId().longValue(), innerCode);
                if (enterpriseCustomerDTO != null) {
                    customerEid = enterpriseCustomerDTO.getCustomerEid();
                }
            }
            if (customerEid == null || customerEid == 0) {
                // 如果更新失败 ，则改变erp商品库中商品同步状态为失败
                erpGoodsCustomerPriceMapper.updateSyncStatusAndMsg(id, 3, "pop客户平台为空");
                return false;
            }

            GoodsDTO goodsDTO = goodsApi.findGoodsAuditPassByInSnAndEidAndGoodsLine(erpClient.getRkSuId().longValue(), erpGoodsCustomerPriceDO.getInSn(), null);
            if (goodsDTO == null) {
                // 如果更新失败 ，则改变erp商品库中商品同步状态为失败
                erpGoodsCustomerPriceMapper.updateSyncStatusAndMsg(id, 3, "商品平台为空");
                return false;
            }

            GoodsLineBO goodsLineBO = goodsApi.getGoodsLineByGoodsIds(ListUtil.toList(goodsDTO.getId())).get(0);
            if (GoodsLineStatusEnum.NOT_ENABLED.getCode().equals(goodsLineBO.getMallStatus()) && GoodsLineStatusEnum.NOT_ENABLED.getCode().equals(goodsLineBO.getPopStatus())) {
                // 如果更新失败 ，则改变erp商品库中商品同步状态为失败
                erpGoodsCustomerPriceMapper.updateSyncStatusAndMsg(id, 3, "商品未开通任何产品线");
                return false;
            }

            //先判断商业公司开通的产品线
            EnterprisePlatformDTO enterprisePlatformDTO = enterpriseApi.getEnterprisePlatform(erpClient.getRkSuId().longValue());
            Integer popFlag = enterprisePlatformDTO.getPopFlag();
            //开通了pop产品线
            GoodsDTO goodsPopDTO = null;
            if (popFlag != null && popFlag == 1 && goodsLineBO.getPopStatus() == 1) {
                goodsPopDTO = goodsApi.findGoodsAuditPassByInSnAndEidAndGoodsLine(erpClient.getRkSuId().longValue(), erpGoodsCustomerPriceDO.getInSn(), GoodsLineEnum.POP.getCode());
                if (goodsPopDTO == null) {
                    erpGoodsCustomerPriceMapper.updateSyncStatusAndMsg(id, 3, "pop商品平台为空");
                    return false;
                }
            }
            Integer mallFlag = enterprisePlatformDTO.getMallFlag();
            //开通了b2b产品线
            GoodsDTO goodsB2bDTO = null;
            if (mallFlag != null && mallFlag == 1 && goodsLineBO.getMallStatus() == 1) {
                goodsB2bDTO = goodsApi.findGoodsAuditPassByInSnAndEidAndGoodsLine(erpClient.getRkSuId().longValue(), erpGoodsCustomerPriceDO.getInSn(), GoodsLineEnum.B2B.getCode());
                if (goodsB2bDTO == null) {
                    erpGoodsCustomerPriceMapper.updateSyncStatusAndMsg(id, 3, "b2b商品平台为空");
                    return false;
                }
            }

            if (goodsPopDTO != null) {
                executePrice(eid, erpGoodsCustomerPriceDO, goodsPopDTO, customerEid, GoodsLineEnum.POP.getCode());
            }
            if (goodsB2bDTO != null) {
                executePrice(eid, erpGoodsCustomerPriceDO, goodsB2bDTO, customerEid, GoodsLineEnum.B2B.getCode());
            }
            erpGoodsCustomerPriceMapper.updateSyncStatusAndMsg(id, SyncStatus.SUCCESS.getCode(), "同步成功");
            return true;
        } catch (Exception e) {
            erpGoodsCustomerPriceMapper.updateSyncStatusAndMsg(id, SyncStatus.FAIL.getCode(), "系统异常");
            log.error("客户定价同步出现错误", e);
        }
        return false;
    }

    public void executePrice(Long eid, ErpGoodsCustomerPriceDO erpGoodsCustomerPriceDO, GoodsDTO goodsDTO, Long customerEid, Integer goodsLine) {
        long start = System.currentTimeMillis();
        String lockName = "mph-erp-online-lock-excute-goods-price:" + eid + "-" + customerEid + "-" + goodsDTO.getId() + "-" + goodsLine;
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock(lockName, 60, 60, TimeUnit.SECONDS);
            if (StringUtils.isEmpty(lockId)) {
                throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "系统繁忙，获取锁失败，lockName:" + lockName);
            }
            QueryGoodsPriceCustomerRequest queryRequest = new QueryGoodsPriceCustomerRequest();
            queryRequest.setEid(eid);
            queryRequest.setCustomerEid(customerEid);
            queryRequest.setGoodsId(goodsDTO.getId());
            queryRequest.setGoodsLine(goodsLine);
            GoodsPriceCustomerDTO goodsPriceCustomerDTO = goodsPriceCustomerApi.get(queryRequest);
            Long priceId = 0L;
            if (goodsPriceCustomerDTO != null) {
                priceId = goodsPriceCustomerDTO.getId();
            }
            //查询客户定价
            if (erpGoodsCustomerPriceDO.getOperType() == 1 || erpGoodsCustomerPriceDO.getOperType() == 2) {
                //增加和修改客户定价
                SaveOrUpdateGoodsPriceCustomerRequest request = new SaveOrUpdateGoodsPriceCustomerRequest();
                request.setOpUserId(0L);
                request.setId(priceId);
                request.setEid(eid);
                request.setCustomerEid(customerEid);
                request.setGoodsId(goodsDTO.getId());
                request.setPriceRule(GoodsPriceRuleEnum.SPECIFIC_PRICE.getCode());
                request.setPriceValue(erpGoodsCustomerPriceDO.getPrice());
                request.setGoodsLine(goodsLine);
                goodsPriceCustomerApi.saveOrUpdate(request);
            } else {
                if (goodsPriceCustomerDTO != null) {
                    //删除客户定价
                    goodsPriceCustomerApi.removeById(goodsPriceCustomerDTO.getId());
                }
            }
        } catch (InterruptedException e) {
            throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "客户定价同步出错，lockName:" + lockName);
        } finally {
            long time = System.currentTimeMillis() - start;
            if (time > 3000) {
                log.warn("客户定价同步耗时:{}ms,lockName:", time, lockName);
            }
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }


    @Override
    public Boolean syncEasGoodsCustomerPrice() {
        String date = "1970-01-01 00:00:00";
        try {
            //查询以岭的编号
            List<ErpClientDTO> erpClientDTOList = erpClientService.selectBySuId(Constants.YILING_EID);
            List<EnterpriseCustomerDTO> enterpriseCustomerDTOList = customerApi.listByEid(Constants.YILING_EID);
            List<Long> eidlist = enterpriseCustomerDTOList.stream().map(e -> e.getCustomerEid()).collect(Collectors.toList());
            Map<Long, List<EnterpriseCustomerEasDTO>> customerIdMap = customerApi.listCustomerEasInfos(Constants.YILING_EID, eidlist);

            List<String> innerCodeStr = new ArrayList<>();
            for (Map.Entry<Long, List<EnterpriseCustomerEasDTO>> entry : customerIdMap.entrySet()) {
                List<EnterpriseCustomerEasDTO> list = entry.getValue();
                innerCodeStr.addAll(list.stream().map(e -> e.getEasCode()).filter(f -> StrUtil.isNotEmpty(f)).collect(Collectors.toList()));
            }
            List<GoodsCustomerPriceJson> list = new ArrayList<>();
            Map<String, String> goodsInsn = new HashMap<>();
            for (ErpClientDTO erpClientDO : erpClientDTOList) {
                List<GoodsDTO> goodsInfoDTOList = goodsApi.getGoodsListByEid(erpClientDO.getRkSuId().longValue());
                List<Long> goodsIds = goodsInfoDTOList.stream().map(e -> e.getId()).collect(Collectors.toList());
                List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsIds(goodsIds);
                List<String> insnStr = goodsSkuDTOList.stream().map(e -> e.getInSn()).filter(f -> StrUtil.isNotEmpty(f)).collect(Collectors.toList());
                for (String insn : insnStr) {
                    goodsInsn.put(insn, erpClientDO.getSuDeptNo());
                }
                EASLoginContext loginCtx = new EASLoginContext.Builder(ip, Integer.parseInt(port), new CommonLogin.Builder(userName, password, dcName, language).build()).https(false).build();
                OpenApiInfo info = new OpenApiInfo();
                //调用api的方法名
                info.setApi("EasBasicDataWebserviceFacade-pricePolicydetils");
                //调用参数，格式是数组形式
                info.setData("[\"" + erpClientDO.getSuDeptNo() + "\",\"" + Arrays.toString(innerCodeStr.toArray()).replaceAll(" ", "") + "\",\"" + Arrays.toString(insnStr.toArray()).replaceAll(" ", "") + "\",\"" + date + "\"]");
                OpenApi openApi = OpenApiFactory.getService(loginCtx);
                //返回数据
                String result = openApi.invoke(info);
                JSONObject jsonObject = JSON.parseObject(result);
                if (jsonObject.getInteger("resultCode").equals(200)) {
                    list.addAll(JSON.parseArray(jsonObject.getString("body"), GoodsCustomerPriceJson.class));
                }
            }

            // 保存去重后的请求数据
            Map<String,BaseErpEntity> dataMap = new HashMap<>();
            Set<String> price = new HashSet<>();

            for (GoodsCustomerPriceJson goodsCustomerPriceJson : list) {
                if (goodsCustomerPriceJson.getDisprice().compareTo(BigDecimal.ZERO) > 0) {
                    ErpGoodsCustomerPriceDTO erpGoodsCustomerPriceDTO = new ErpGoodsCustomerPriceDTO();
                    erpGoodsCustomerPriceDTO.setGcpIdNo(goodsCustomerPriceJson.getOrgno() + goodsCustomerPriceJson.getCustno() + goodsCustomerPriceJson.getMatno());
                    erpGoodsCustomerPriceDTO.setInSn(goodsCustomerPriceJson.getMatno());
                    erpGoodsCustomerPriceDTO.setInnerCode(goodsCustomerPriceJson.getCustno());
                    erpGoodsCustomerPriceDTO.setPrice(goodsCustomerPriceJson.getDisprice().setScale(4, BigDecimal.ROUND_HALF_UP));
                    erpGoodsCustomerPriceDTO.setSuDeptNo(goodsCustomerPriceJson.getOrgno());
                    erpGoodsCustomerPriceDTO.setSuId(Constants.YILING_EID);
                    erpGoodsCustomerPriceDTO.setOperType(1);
                    dataMap.put(erpGoodsCustomerPriceDTO.getErpPrimaryKey(),erpGoodsCustomerPriceDTO);
                    price.add(goodsCustomerPriceJson.getMatno() + "-" + goodsCustomerPriceJson.getCustno());
                }
            }

            //再次执行00组织下面的定价信息
            {
                List<GoodsCustomerPriceJson> priceList = new ArrayList<>();
                EASLoginContext loginCtx = new EASLoginContext.Builder(ip, Integer.parseInt(port), new CommonLogin.Builder(userName, password, dcName, language).build()).https(false).build();
                OpenApiInfo info = new OpenApiInfo();
                //调用api的方法名
                info.setApi("EasBasicDataWebserviceFacade-pricePolicydetils");
                //调用参数，格式是数组形式
                info.setData("[\"00\",\"" + Arrays.toString(innerCodeStr.toArray()).replaceAll(" ", "") + "\",\"" + Arrays.toString(goodsInsn.keySet().toArray()).replaceAll(" ", "") + "\",\"" + date + "\"]");
                OpenApi openApi = OpenApiFactory.getService(loginCtx);
                //返回数据
                String result = openApi.invoke(info);
                JSONObject jsonObject = JSON.parseObject(result);
                if (jsonObject.getInteger("resultCode").equals(200)) {
                    priceList.addAll(JSON.parseArray(jsonObject.getString("body"), GoodsCustomerPriceJson.class));
                }
                for (GoodsCustomerPriceJson goodsCustomerPriceJson : priceList) {
                    if (goodsCustomerPriceJson.getDisprice().compareTo(BigDecimal.ZERO) > 0) {
                        String orgNo = goodsInsn.get(goodsCustomerPriceJson.getMatno());
                        if (StrUtil.isEmpty(orgNo)) {
                            continue;
                        }
                        String key = goodsCustomerPriceJson.getMatno() + "-" + goodsCustomerPriceJson.getCustno();
                        if (price.contains(key)) {
                            continue;
                        }
                        ErpGoodsCustomerPriceDTO erpGoodsCustomerPriceDTO = new ErpGoodsCustomerPriceDTO();
                        erpGoodsCustomerPriceDTO.setGcpIdNo(orgNo + goodsCustomerPriceJson.getCustno() + goodsCustomerPriceJson.getMatno());
                        erpGoodsCustomerPriceDTO.setInSn(goodsCustomerPriceJson.getMatno());
                        erpGoodsCustomerPriceDTO.setInnerCode(goodsCustomerPriceJson.getCustno());
                        erpGoodsCustomerPriceDTO.setPrice(goodsCustomerPriceJson.getDisprice().setScale(4, BigDecimal.ROUND_HALF_UP));
                        erpGoodsCustomerPriceDTO.setSuDeptNo(orgNo);
                        erpGoodsCustomerPriceDTO.setSuId(Constants.YILING_EID);
                        erpGoodsCustomerPriceDTO.setOperType(1);
                        dataMap.put(erpGoodsCustomerPriceDTO.getErpPrimaryKey(),erpGoodsCustomerPriceDTO);
                    }
                }
            }

            localCompare(new ArrayList<>(dataMap.values()), Constants.YILING_EID);
        } catch (Exception e) {
            log.error("同步eas客户定价接口报错{}", e);
        }
        return true;
    }

    @Override
    public Boolean syncDayunheGoodsCustomerPrice() {
        String date = "1970-01-01 00:00:00";
        try {
            //查询以岭的编号
            ErpClientDTO erpClientDO = erpClientService.selectByRkSuId(4L);
            List<EnterpriseCustomerDTO> enterpriseCustomerDTOList = customerApi.listByEid(Constants.YILING_EID);
            List<Long> eidlist = enterpriseCustomerDTOList.stream().map(e -> e.getCustomerEid()).collect(Collectors.toList());
            Map<Long, List<EnterpriseCustomerEasDTO>> customerIdMap = customerApi.listCustomerEasInfos(Constants.YILING_EID, eidlist);

            //获取客户
            List<String> innerCodeStr = new ArrayList<>();
            for (Map.Entry<Long, List<EnterpriseCustomerEasDTO>> entry : customerIdMap.entrySet()) {
                List<EnterpriseCustomerEasDTO> list = entry.getValue();
                innerCodeStr.addAll(list.stream().map(e -> e.getEasCode()).filter(f -> StrUtil.isNotEmpty(f)).collect(Collectors.toList()));
            }
            List<GoodsCustomerPriceJson> list = new ArrayList<>();

            //获取商品信息
            Map<String, String> goodsInsn = new HashMap<>();
            List<GoodsDTO> goodsInfoDTOList = goodsApi.getGoodsListByEid(erpClientDO.getRkSuId().longValue());
            List<Long> goodsIds = goodsInfoDTOList.stream().map(e -> e.getId()).collect(Collectors.toList());
            List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsIds(goodsIds);
            List<String> insnStr = goodsSkuDTOList.stream().map(e -> e.getInSn()).filter(f -> StrUtil.isNotEmpty(f)).collect(Collectors.toList());
            for (String insn : insnStr) {
                goodsInsn.put(insn, erpClientDO.getSuDeptNo());
            }

            {
                //获取工业大运河定价
                EASLoginContext loginCtx = new EASLoginContext.Builder(ip, Integer.parseInt(port), new CommonLogin.Builder(userName, password, dcName, language).build()).https(false).build();
                OpenApiInfo info = new OpenApiInfo();
                //调用api的方法名
                info.setApi("EasBasicDataWebserviceFacade-pricePolicydetils");
                //调用参数，格式是数组形式
                info.setData("[\"02\",\"" + Arrays.toString(innerCodeStr.toArray()).replaceAll(" ", "") + "\",\"" + Arrays.toString(insnStr.toArray()).replaceAll(" ", "") + "\",\"" + date + "\"]");
                OpenApi openApi = OpenApiFactory.getService(loginCtx);
                //返回数据
                String result = openApi.invoke(info);
                JSONObject jsonObject = JSON.parseObject(result);
                if (jsonObject.getInteger("resultCode").equals(200)) {
                    list.addAll(JSON.parseArray(jsonObject.getString("body"), GoodsCustomerPriceJson.class));
                }
            }
            // 保存去重后的请求数据
            Map<String,BaseErpEntity> dataMap = new HashMap<>();
            Set<String> price = new HashSet<>();
            for (GoodsCustomerPriceJson goodsCustomerPriceJson : list) {
                if (goodsCustomerPriceJson.getDisprice().compareTo(BigDecimal.ZERO) > 0) {
                    ErpGoodsCustomerPriceDTO erpGoodsCustomerPriceDTO = new ErpGoodsCustomerPriceDTO();
                    erpGoodsCustomerPriceDTO.setGcpIdNo(goodsCustomerPriceJson.getCustno() + goodsCustomerPriceJson.getMatno());
                    erpGoodsCustomerPriceDTO.setInSn(goodsCustomerPriceJson.getMatno());
                    erpGoodsCustomerPriceDTO.setInnerCode(goodsCustomerPriceJson.getCustno());
                    erpGoodsCustomerPriceDTO.setPrice(goodsCustomerPriceJson.getDisprice().setScale(4, BigDecimal.ROUND_HALF_UP));
                    erpGoodsCustomerPriceDTO.setSuDeptNo("");
                    erpGoodsCustomerPriceDTO.setSuId(erpClientDO.getSuId().longValue());
                    erpGoodsCustomerPriceDTO.setOperType(1);
                    dataMap.put(erpGoodsCustomerPriceDTO.getErpPrimaryKey(),erpGoodsCustomerPriceDTO);
                    price.add(goodsCustomerPriceJson.getMatno() + "-" + goodsCustomerPriceJson.getCustno());
                }
            }

            //再次执行00组织下面的定价信息
            List<GoodsCustomerPriceJson> priceList = new ArrayList<>();
            EASLoginContext loginCtx = new EASLoginContext.Builder(ip, Integer.parseInt(port), new CommonLogin.Builder(userName, password, dcName, language).build()).https(false).build();
            OpenApiInfo info = new OpenApiInfo();
            //调用api的方法名
            info.setApi("EasBasicDataWebserviceFacade-pricePolicydetils");
            //调用参数，格式是数组形式
            info.setData("[\"00\",\"" + Arrays.toString(innerCodeStr.toArray()).replaceAll(" ", "") + "\",\"" + Arrays.toString(goodsInsn.keySet().toArray()).replaceAll(" ", "") + "\",\"" + date + "\"]");
            OpenApi openApi = OpenApiFactory.getService(loginCtx);
            //返回数据
            String result = openApi.invoke(info);
            JSONObject jsonObject = JSON.parseObject(result);
            if (jsonObject.getInteger("resultCode").equals(200)) {
                priceList.addAll(JSON.parseArray(jsonObject.getString("body"), GoodsCustomerPriceJson.class));
            }
            for (GoodsCustomerPriceJson goodsCustomerPriceJson : priceList) {
                if (goodsCustomerPriceJson.getDisprice().compareTo(BigDecimal.ZERO) > 0) {
                    String key = goodsCustomerPriceJson.getMatno() + "-" + goodsCustomerPriceJson.getCustno();
                    if (price.contains(key)) {
                        continue;
                    }
                    ErpGoodsCustomerPriceDTO erpGoodsCustomerPriceDTO = new ErpGoodsCustomerPriceDTO();
                    erpGoodsCustomerPriceDTO.setGcpIdNo(goodsCustomerPriceJson.getCustno() + goodsCustomerPriceJson.getMatno());
                    erpGoodsCustomerPriceDTO.setInSn(goodsCustomerPriceJson.getMatno());
                    erpGoodsCustomerPriceDTO.setInnerCode(goodsCustomerPriceJson.getCustno());
                    erpGoodsCustomerPriceDTO.setPrice(goodsCustomerPriceJson.getDisprice().setScale(4, BigDecimal.ROUND_HALF_UP));
                    erpGoodsCustomerPriceDTO.setSuDeptNo("");
                    erpGoodsCustomerPriceDTO.setSuId(erpClientDO.getSuId().longValue());
                    erpGoodsCustomerPriceDTO.setOperType(1);
                    dataMap.put(erpGoodsCustomerPriceDTO.getErpPrimaryKey(),erpGoodsCustomerPriceDTO);
                }
            }
            localCompare(new ArrayList<>(dataMap.values()), erpClientDO.getSuId().longValue());
        } catch (Exception e) {
            log.error("同步eas客户定价接口报错{}", e);
        }
        return true;
    }

    @Override
    public Integer updateOperTypeGoodsBatchFlowBySuId(Long suId) {
        return erpGoodsCustomerPriceMapper.updateOperTypeGoodsBatchFlowBySuId(suId);
    }

    public void localCompare(List<BaseErpEntity> dataList, Long eid) {
        List<ErpGoodsCustomerPriceDO> sqliteDataList = erpGoodsCustomerPriceMapper.getGoodsCustomerPriceBySuId(eid);
        sqliteDataList = sqliteDataList.stream().filter(e -> !e.getOperType().equals(3)).collect(Collectors.toList());
        List<ErpGoodsCustomerPriceDTO> list = PojoUtils.map(sqliteDataList, ErpGoodsCustomerPriceDTO.class);
        Map<String, String> sqliteData = list.stream().collect(Collectors.toMap(BaseErpEntity::getErpPrimaryKey, BaseErpEntity::getDataMd5));
        log.info("[客户定价同步] 本地缓存客户定价条数:" + sqliteDataList.size() + " | ERP系统客户定价条数:" + dataList.size());
        Map<String, BaseErpEntity> map = this.isRepeat(dataList);
        if (map == null) {
            log.info("[客户定价同步] ERP系统客户定价主键有重复数据,请检查" + ErpTopicName.ErpCustomer.getErpKey() + "字段");
            return;
        }

        Map<Integer, Set<String>> handlerMap = this.comparisonData(sqliteData, map);
        List<BaseErpEntity> deleteList = new ArrayList<>();
        List<BaseErpEntity> addList = new ArrayList<>();
        List<BaseErpEntity> updateList = new ArrayList<>();
        Set<String> deleteSet = handlerMap.get(OperTypeEnum.DELETE.getCode());
        Set<String> addSet = handlerMap.get(OperTypeEnum.ADD.getCode());
        Set<String> updateSet = handlerMap.get(OperTypeEnum.UPDATE.getCode());
        if (!CollectionUtils.isEmpty(deleteSet)) {
            for (String key : deleteSet) {
                ErpGoodsCustomerPriceDTO erpGoodsCustomerPriceDTO = new ErpGoodsCustomerPriceDTO();
                erpGoodsCustomerPriceDTO.setSuId(eid);
                // 先判断key是否包含$$$
                if (key.contains(OpenConstants.SPLITE_SYMBOL)) {
                    String[] keys = key.split(OpenConstants.SPLITE_SYMBOL_FALG);
                    erpGoodsCustomerPriceDTO.setGcpIdNo(keys[0]);
                    erpGoodsCustomerPriceDTO.setSuDeptNo(keys[1]);
                } else {
                    erpGoodsCustomerPriceDTO.setGcpIdNo(key);
                    erpGoodsCustomerPriceDTO.setSuDeptNo("");
                }
                erpGoodsCustomerPriceDTO.setOperType(OperTypeEnum.DELETE.getCode());
                deleteList.add(erpGoodsCustomerPriceDTO);
            }
        }

        if (!CollectionUtils.isEmpty(addSet)) {
            for (String key : addSet) {
                BaseErpEntity baseErpEntity = map.get(key);
                baseErpEntity.setOperType(OperTypeEnum.ADD.getCode());
                addList.add(baseErpEntity);
            }
        }

        if (!CollectionUtils.isEmpty(updateSet)) {
            for (String key : updateSet) {
                BaseErpEntity baseErpEntity = map.get(key);
                baseErpEntity.setOperType(OperTypeEnum.UPDATE.getCode());
                updateList.add(baseErpEntity);
            }
        }

        // 发送消息
        if (!CollectionUtils.isEmpty(deleteList)) {
            log.info("发送消息，客户定价信息同步，删除");
            SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpGoodsPrice.getTopicName(), eid + "", DateUtil.formatDate(new Date()), JSON.toJSONString(deleteList));
            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.info("发送消息，客户定价信息同步，删除,失败 json={}", JSON.toJSONString(deleteList));
            }
        }
        if (!CollectionUtils.isEmpty(addList)) {
            log.info("发送消息，客户定价信息同步，新增");
            SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpGoodsPrice.getTopicName(), eid + "", DateUtil.formatDate(new Date()), JSON.toJSONString(addList));
            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.info("发送消息，客户定价信息同步，新增,失败 json={}", JSON.toJSONString(deleteList));
            }
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            log.info("发送消息，客户定价信息同步，更新");
            SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpGoodsPrice.getTopicName(), eid + "", DateUtil.formatDate(new Date()), JSON.toJSONString(updateList));
            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.info("发送消息，客户定价信息同步，更新,失败 json={}", JSON.toJSONString(deleteList));
            }
        }
    }

    public Map<String, BaseErpEntity> isRepeat(List<BaseErpEntity> baseErpEntityList) {
        Map<String, BaseErpEntity> map = new HashMap<>();
        for (BaseErpEntity baseErpEntity : baseErpEntityList) {
            if(!map.containsKey(baseErpEntity.getErpPrimaryKey())){
                map.put(baseErpEntity.getErpPrimaryKey(), baseErpEntity);
            } else {
                log.error("客户定价主键重复:{}", JSON.toJSONString(baseErpEntity));
            }
        }
        if (map.size() == baseErpEntityList.size()) {
            return map;
        }
        return null;
    }

    /**
     * 5.判断是增删改的数据
     *
     * @param sqliteData 客户信息同步数据（本地缓存）
     * @param data ERP系统客户信息数据
     * @return
     */
    public Map<Integer, Set<String>> comparisonData(Map<String, String> sqliteData, Map<String, BaseErpEntity> data) {
        Map<Integer, Set<String>> handlerMap = new HashMap<>();
        // 删除数据
        Set<String> sqliteKey = sqliteData.keySet();
        Set<String> key = data.keySet();
        Set<String> deleteSet = Sets.difference(sqliteKey, key);
        handlerMap.put(OperTypeEnum.DELETE.getCode(), deleteSet);
        // 增加数据
        Set<String> insertSet = Sets.difference(key, sqliteKey);
        handlerMap.put(OperTypeEnum.ADD.getCode(), insertSet);
        // 修改数据
        Set<String> updateSet = new HashSet<>();

        Set<String> andSet = Sets.intersection(key, sqliteKey);
        for (String handleKey : andSet) {
            BaseErpEntity baseErpEntity = data.get(handleKey);
            if (!(sqliteData.get(handleKey).equals(baseErpEntity.sign()))) {
                updateSet.add(handleKey);
            }
        }
        handlerMap.put(OperTypeEnum.UPDATE.getCode(), updateSet);
        return handlerMap;
    }

    @Override
    public ErpEntityMapper getErpEntityDao() {
        return erpGoodsCustomerPriceMapper;
    }

}
