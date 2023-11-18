package com.yiling.hmc.third.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.AESUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.hmc.goods.api.GoodsApi;
import com.yiling.hmc.goods.api.GoodsControlApi;
import com.yiling.hmc.goods.bo.GoodsControlBO;
import com.yiling.hmc.goods.dto.HmcGoodsDTO;
import com.yiling.hmc.goods.dto.request.QueryHmcGoodsRequest;
import com.yiling.hmc.insurance.api.InsuranceApi;
import com.yiling.hmc.insurance.api.InsuranceDetailApi;
import com.yiling.hmc.insurance.dto.InsuranceDTO;
import com.yiling.hmc.insurance.dto.InsuranceDetailDTO;
import com.yiling.hmc.order.enums.HmcHolderTypeEnum;
import com.yiling.hmc.patient.dto.request.SavePatientRequest;
import com.yiling.hmc.third.form.InsuranceNotify;
import com.yiling.hmc.third.form.PayInsuranceNotify;
import com.yiling.hmc.third.form.PolicyStatusAsync;
import com.yiling.hmc.wechat.api.InsuranceRecordApi;
import com.yiling.hmc.wechat.api.InsuranceRecordPayPlanApi;
import com.yiling.hmc.wechat.dto.InsuranceRecordDTO;
import com.yiling.hmc.wechat.dto.InsuranceRecordPayPlanDTO;
import com.yiling.hmc.wechat.dto.request.*;
import com.yiling.hmc.wechat.enums.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 参保控制器
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/3/28
 */
@Slf4j
@RestController
@RequestMapping("/insurance_notify")
@Api(tags = "参保回调控制器")
public class InsuranceNotifyController {

    public static String key = "13f4d5e63e284cefaf008c94756861bf";

    @DubboReference
    InsuranceApi insuranceApi;

    @DubboReference
    InsuranceDetailApi insuranceDetailApi;

    @DubboReference
    InsuranceRecordApi insuranceRecordApi;

    @DubboReference
    InsuranceRecordPayPlanApi insuranceRecordPayPlanApi;

    @DubboReference
    GoodsControlApi goodsControlApi;

    @DubboReference
    GoodsApi goodsApi;

    @Autowired
    RedisDistributedLock redisDistributedLock;

    @ApiOperation(value = "参保回调")
    @PostMapping("/join")
    @Log(title = "参保回调", businessType = BusinessTypeEnum.OTHER)
    public Result<Long> joinNotify(@RequestBody String content) {

        log.info("参保回调，原始参数：{}", content);

        if (StrUtil.isBlank(content)) {
            return Result.failed("参数为空，请检查输入");
        }

        String decryptStr = AESUtils.decryptStr(content, key);

        log.info("参保回调,解密结果：{}", decryptStr);

        InsuranceNotify insuranceNotify = JSONUtil.toBean(decryptStr, InsuranceNotify.class);

        if (Objects.isNull(insuranceNotify)) {
            return Result.failed("参数转换异常");
        }

        // 参数校验
        insuranceNotify.check();

        Long insuranceId = insuranceNotify.getRequestData().getParameterMap().getInsuranceId();

        // 1、查询保险信息
        InsuranceDTO insuranceDTO = insuranceApi.queryById(insuranceId);

        if (Objects.isNull(insuranceDTO)) {
            log.error("根据保险id未查询到保险详情，保险id：{}", insuranceId);
            return Result.failed("根据保险id未查询到保险详情");
        }

        String policyNo = insuranceNotify.getRequestData().getPolicyNo();
        Long insuranceCompanyId = insuranceNotify.getRequestData().getParameterMap().getInsuranceCompanyId();
        String lockName = policyNo + insuranceCompanyId;
        String lockId = null;
        Long recordId;
        try {
            lockId = redisDistributedLock.lock2(lockName, 10, 10, TimeUnit.SECONDS);

            //
            boolean checkResult = insuranceRecordApi.checkInsuranceRecord(policyNo, insuranceCompanyId);
            if (checkResult) {
                log.info("根据 policyNo + insuranceCompanyId查询到参保记录，policyNo：{}， insuranceCompanyId：{}", policyNo, insuranceCompanyId);
                return Result.failed("根据 policyNo + insuranceCompanyId查询到参保记");
            }

            // 2、查询保险详情
            List<InsuranceDetailDTO> insuranceDetailList = insuranceDetailApi.listByInsuranceId(insuranceId);

            // 3、构建参保记录
            SaveInsuranceRecordRequest insuranceRecordRequest = buildRequest(insuranceNotify, insuranceDTO);

            // 4、构建参保缴费计划
            List<SaveInsuranceRecordPayPlanRequest> payPlanRequestList = buildPayPlanRequest(insuranceNotify);

            // 5、构建首次缴费记录
            SaveInsuranceRecordPayRequest payRequest = buildPayRequest(insuranceRecordRequest, insuranceNotify);

            // 6、构建拿药计划
            List<SaveInsuranceFetchPlanRequest> fetchPlanRequestList = buildFetchPlan(insuranceRecordRequest.getBillType(), insuranceRecordRequest.getIssueTime());

            // 7、构建拿药计划明细
            List<SaveInsuranceFetchPlanDetailRequest> fetchPlanDetailRequestList = buildFetchPlanDetail(insuranceNotify.getRequestData().getParameterMap().getEId(), insuranceDetailList);

            // 构建参保回调上下文对象
            InsuranceJoinNotifyContext context = new InsuranceJoinNotifyContext();
            context.setInsuranceRecordRequest(insuranceRecordRequest);
            context.setPayPlanRequestList(payPlanRequestList);
            context.setPayRequest(payRequest);
            context.setFetchPlanRequestList(fetchPlanRequestList);
            context.setFetchPlanDetailRequestList(fetchPlanDetailRequestList);

            log.info(">>> 准备保存参保回传记录，参数：{}", JSONUtil.toJsonStr(context));

            recordId = insuranceRecordApi.joinNotify(context);

        } catch (Exception e) {
            log.error("系统繁忙，请稍后操作：{}", e.getMessage(), e);
            return Result.failed("系统繁忙，请稍后操作!");
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }

        log.info("参保回调完成，参保记录id：{}", recordId);
        return Result.success(recordId);
    }

    /**
     * 构建拿药计划明细
     *
     * @param eId
     * @param insuranceDetailList
     * @return
     */
    private List<SaveInsuranceFetchPlanDetailRequest> buildFetchPlanDetail(Long eId, List<InsuranceDetailDTO> insuranceDetailList) {


        List<Long> controlIdList = insuranceDetailList.stream().map(InsuranceDetailDTO::getControlId).collect(Collectors.toList());

        List<GoodsControlBO> goodsControlList = goodsControlApi.getGoodsControlInfoByIds(controlIdList);


        Map<Long, GoodsControlBO> goodsControlMap = goodsControlList.stream().collect(Collectors.toMap(GoodsControlBO::getId, o -> o, (k1, k2) -> k1));

        List<SaveInsuranceFetchPlanDetailRequest> result = insuranceDetailList.stream().map(item -> {
            SaveInsuranceFetchPlanDetailRequest request = new SaveInsuranceFetchPlanDetailRequest();
            request.setEid(eId);
            request.setSellSpecificationsId(Optional.ofNullable(goodsControlMap.get(item.getControlId())).map(GoodsControlBO::getSellSpecificationsId).orElse(null));
            request.setPerMonthCount(item.getMonthCount());
            request.setGoodsName(Optional.ofNullable(goodsControlMap.get(item.getControlId())).map(GoodsControlBO::getName).orElse(null));
            request.setSpecificInfo(Optional.ofNullable(goodsControlMap.get(item.getControlId())).map(GoodsControlBO::getSellSpecifications).orElse(null));
            request.setMarketPrice(Optional.ofNullable(goodsControlMap.get(item.getControlId())).map(GoodsControlBO::getMarketPrice).orElse(null));
            request.setInsurancePrice(Optional.ofNullable(goodsControlMap.get(item.getControlId())).map(GoodsControlBO::getInsurancePrice).orElse(null));
            request.setSettlePrice(item.getSettlePrice());
            request.setInsuranceGoodsCode(item.getInsuranceGoodsCode());

            // 获取商品信息
            QueryHmcGoodsRequest hmcGoodsRequest = new QueryHmcGoodsRequest();
            hmcGoodsRequest.setEid(request.getEid());
            hmcGoodsRequest.setSellSpecificationsId(request.getSellSpecificationsId());
            hmcGoodsRequest.setGoodsStatus(GoodsStatusEnum.UP_SHELF.getCode());
            HmcGoodsDTO hmcGoodsDTO = goodsApi.findBySpecificationsIdAndEid(hmcGoodsRequest);

            if (Objects.isNull(hmcGoodsDTO)) {
                log.info("根据eid{},售卖规格id{}未查询到商家商品信息", request.getEid(), request.getSellSpecificationsId());
                throw new BusinessException(InsuranceErrorCode.GOODS_NOT_FOUND);
            }

            request.setHmcGoodsId(hmcGoodsDTO.getId());

            request.setGoodsId(hmcGoodsDTO.getGoodsId());

            request.setTerminalSettlePrice(hmcGoodsDTO.getTerminalSettlePrice());

            return request;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 构建拿药计划
     *
     * @param billType
     * @param proposalTime
     * @return
     */
    private List<SaveInsuranceFetchPlanRequest> buildFetchPlan(Integer billType, Date proposalTime) {

        int times = 0;

        // 季度
        if (HmcInsuranceBillTypeEnum.QUARTER.getType().equals(billType)) {
            times = 3;
        }

        // 年度
        if (HmcInsuranceBillTypeEnum.YEAR.getType().equals(billType)) {
            times = 12;
        }

        List<SaveInsuranceFetchPlanRequest> result = Lists.newArrayList();

        Instant instant = proposalTime.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        for (int i = 0; i < times; i++) {
            ZonedDateTime zdt = localDateTime.plusMonths(Long.valueOf(i)).atZone(zoneId);
            SaveInsuranceFetchPlanRequest request = new SaveInsuranceFetchPlanRequest();
            request.setInitFetchTime(Date.from(zdt.toInstant()));
            request.setFetchStatus(InsuranceFetchStatusEnum.WAIT.getType());
            result.add(request);
        }
        return result;
    }

    /**
     * 构造首次缴费记录
     *
     * @param insuranceRecordRequest
     * @param insuranceNotify
     * @return
     */
    private SaveInsuranceRecordPayRequest buildPayRequest(SaveInsuranceRecordRequest insuranceRecordRequest, InsuranceNotify insuranceNotify) {
        SaveInsuranceRecordPayRequest payRequest = new SaveInsuranceRecordPayRequest();
        payRequest.setAmount(insuranceNotify.getRequestData().getPremium());
        payRequest.setPolicyNo(insuranceNotify.getRequestData().getPolicyNo());
        payRequest.setPayTime(insuranceRecordRequest.getIssueTime());
        payRequest.setPayStatus(InsurancePayStatusEnum.SUCCESS.getType());
        payRequest.setMinSequence("1");
        payRequest.setMaxSequence("1");

        // 首次支付 开始时间 = 支付时间
        payRequest.setStartTime(insuranceRecordRequest.getIssueTime());
        payRequest.setEndTime(insuranceRecordRequest.getCurrentEndTime());

        return payRequest;
    }

    @ApiOperation(value = "主动支付回调")
    @PostMapping("/pay")
    @Log(title = "主动支付回调", businessType = BusinessTypeEnum.OTHER)
    public Result<String> payNotify(@RequestBody String content) {

        log.info("主动支付回调,请求参数 ： {}", JSONUtil.toJsonStr(content));

        String decryptStr = AESUtils.decryptStr(content, key);

        log.info("主动支付回调,解密结果：{}", decryptStr);

        PayInsuranceNotify payNotify = JSONUtil.toBean(decryptStr, PayInsuranceNotify.class);

        if (!payNotify.getPayStatus().equals(3)) {
            log.error("主动支付回调，不是支付成功状态，回调参数：{}", payNotify);
            return Result.success();
        }

        // 1、查询参保记录
        String policyNo = payNotify.getPolicyNo();
        InsuranceRecordDTO insuranceRecordDTO = insuranceRecordApi.getByPolicyNo(policyNo);

        if (Objects.isNull(insuranceRecordDTO)) {
            log.error("根据保单号未查询到参保记录，保单号：{}", policyNo);
            return Result.success();
        }

        // 2、根据保单号查询续费计划
        List<InsuranceRecordPayPlanDTO> payPlanList = insuranceRecordPayPlanApi.getByInsuranceRecordId(insuranceRecordDTO.getId());
        log.info("根据保单号查询续费计划：{}", JSONUtil.toJsonStr(payPlanList));

        String maxSequence = payNotify.getMaxSequence();
        Optional<InsuranceRecordPayPlanDTO> payPlan = payPlanList.stream().filter(item -> item.getPaySequence().equals(Integer.valueOf(maxSequence))).findFirst();

        if (!payPlan.isPresent()) {
            log.error("根据保单号未匹配到缴费期次，保单号：{}", policyNo);
            return Result.success();
        }

        // 如果是季付 -> 生成用药计划 + 计划详情
        Integer billType = insuranceRecordDTO.getBillType();
        if (!HmcInsuranceBillTypeEnum.QUARTER.getType().equals(billType)) {
            log.error("非季付型保单，不需要续期，保单号：{}", policyNo);
            return Result.success();
        }

        // 3、保存支付记录
        SaveInsuranceRecordPayRequest payRequest = PojoUtils.map(payNotify, SaveInsuranceRecordPayRequest.class);
        payRequest.setPayStatus(InsurancePayStatusEnum.SUCCESS.getType());
        payRequest.setStartTime(payPlan.get().getStartTime());
        payRequest.setEndTime(payPlan.get().getEndTime());

        // 4、更新保单有效期
        UpdateInsuranceRecordRequest updateInsuranceRequest = new UpdateInsuranceRecordRequest();
        updateInsuranceRequest.setId(insuranceRecordDTO.getId());
        updateInsuranceRequest.setCurrentEndTime(payPlan.get().getEndTime());

        // 5、构建拿药计划
        List<SaveInsuranceFetchPlanRequest> fetchPlanRequestList = buildFetchPlan(billType, payRequest.getPayTime());

//        // 5.2、构建拿药计划明细
//        List<InsuranceDetailDTO> insuranceDetailList = insuranceDetailApi.listByInsuranceId(insuranceRecordDTO.getInsuranceId());
//        List<SaveInsuranceFetchPlanDetailRequest> fetchPlanDetailRequestList = buildFetchPlanDetail(insuranceRecordDTO.getEid(), insuranceDetailList);
//        insuranceFetchPlanDetailApi.saveFetchPlanDetail(fetchPlanDetailRequestList);

        InsurancePayNotifyContext payNotifyContext = new InsurancePayNotifyContext();
        payNotifyContext.setPayRequest(payRequest);
        payNotifyContext.setUpdateInsuranceRequest(updateInsuranceRequest);
        payNotifyContext.setFetchPlanRequestList(fetchPlanRequestList);

        insuranceRecordApi.payNotify(payNotifyContext);

        log.info("更新保单效期参数：{} ", JSONUtil.toJsonStr(payNotifyContext));

        return Result.success();
    }

    @ApiOperation(value = "保单状态同步")
    @PostMapping("/status_async")
    @Log(title = "保单状态同步", businessType = BusinessTypeEnum.OTHER)
    public Result<String> statusAsync(@RequestBody String content) {

        log.info("保单状态同步,请求参数 ： {}", JSONUtil.toJsonStr(content));

        // 1、验证签名sign = md5(key + apply_content)
        LinkedHashMap jsonMap = JSON.parseObject(content, LinkedHashMap.class, Feature.OrderedField);
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.putAll(jsonMap);
        String md5Hex = DigestUtils.md5Hex(key + jsonObject.getString("apply_content"));
        String sign = jsonObject.getString("sign");
        if (StrUtil.isBlank(sign) || !StrUtil.equals(sign, md5Hex)) {
            log.error("加密得到的sign和原始报文不一致，参数：{}，新sign：{}", content, md5Hex);
            return Result.failed("加密得到的sign和原始报文不一致");
        }

        PolicyStatusAsync policyStatusAsync = JSONObject.parseObject(content, PolicyStatusAsync.class);
        String policyNo = policyStatusAsync.getApplyContent().getPolicyNo();
        InsuranceRecordDTO insuranceRecordDTO = insuranceRecordApi.getByPolicyNo(policyNo);
        if (Objects.isNull(insuranceRecordDTO)) {
            log.info("根据保单号未查询到参保记录，保单号：{}", policyNo);
            return Result.failed("根据保单号未查询到参保记录");
        }

        // 2、判断保单状态类型 16 - 是保单退保,18 - 是保险合同终止,71 - 保单失效
        Integer endPolicyType = policyStatusAsync.getApplyContent().getEndPolicyType();

        HmcPolicyEndTypeEnum match = HmcPolicyEndTypeEnum.match(endPolicyType);

        if (Objects.isNull(match)) {
            log.info("无效状态{}，跳过更新", endPolicyType);
            return Result.success();
        }

        // 3、更新保单状态、保存退保记录
        SaveInsuranceRetreatRequest request = buildRetreatRequest(insuranceRecordDTO, policyStatusAsync);
        insuranceRecordApi.statusAsync(request);

        return Result.success();
    }

    /**
     * 构建退保记录
     *
     * @param policyStatusAsync
     * @return
     */
    private SaveInsuranceRetreatRequest buildRetreatRequest(InsuranceRecordDTO insuranceRecordDTO, PolicyStatusAsync policyStatusAsync) {
        SaveInsuranceRetreatRequest request = new SaveInsuranceRetreatRequest();
        request.setInsuranceRecordId(insuranceRecordDTO.getId());
        request.setPolicyNo(policyStatusAsync.getApplyContent().getPolicyNo());
        request.setIdType(policyStatusAsync.getApplyContent().getIdType());
        request.setIdNo(policyStatusAsync.getApplyContent().getIdNo());
        request.setRetMoney(policyStatusAsync.getApplyContent().getRetMoney());
        request.setRetTime(policyStatusAsync.getApplyContent().getRetTime());
        request.setPremium(policyStatusAsync.getApplyContent().getPremium());
        request.setInstallmentsTotal(policyStatusAsync.getApplyContent().getInstallmentsTotal());
        request.setEndPolicyType(policyStatusAsync.getApplyContent().getEndPolicyType());
        request.setFlowId(policyStatusAsync.getApplyContent().getFlowid());
        return request;
    }

    /**
     * 构建支付计划request
     *
     * @param insuranceNotify
     * @return
     */
    private List<SaveInsuranceRecordPayPlanRequest> buildPayPlanRequest(InsuranceNotify insuranceNotify) {
        if (CollUtil.isNotEmpty(insuranceNotify.getRequestData().getPremiumPlanList())) {
            List<SaveInsuranceRecordPayPlanRequest> list = PojoUtils.map(insuranceNotify.getRequestData().getPremiumPlanList(), SaveInsuranceRecordPayPlanRequest.class);
            return list;
        }
        return Lists.newArrayList();
    }

    /**
     * 构建参保请求对象
     *
     * @param insuranceNotify
     * @return
     */
    private SaveInsuranceRecordRequest buildRequest(InsuranceNotify insuranceNotify, InsuranceDTO insuranceDTO) {
        SaveInsuranceRecordRequest request = SaveInsuranceRecordRequest.builder().build();
        PojoUtils.map(insuranceNotify.getRequestData(), request);

        request.setInsuranceId(insuranceNotify.getRequestData().getParameterMap().getInsuranceId());
        request.setInsuranceCompanyId(insuranceDTO.getInsuranceCompanyId());
        request.setUserId(insuranceNotify.getRequestData().getParameterMap().getUserId());
        request.setSellerUserId(insuranceNotify.getRequestData().getParameterMap().getSellerUserId());
        request.setEid(insuranceNotify.getRequestData().getParameterMap().getEId());
        request.setSellerEid(insuranceNotify.getRequestData().getParameterMap().getSellerEid());

        request.setPolicyStatus(HmcPolicyStatusEnum.PROCESSING.getType());

        // 参保时的生效时间 = 支付时间
        request.setEffectiveTime(insuranceNotify.getRequestData().getIssueTime());
        request.setIssueTime(insuranceNotify.getRequestData().getIssueTime());
        request.setRelationType(insuranceNotify.getRequestData().getRelationType());

//        // 如果是季度类型
//        if (insuranceNotify.getRequestData().getComboName().equals(insuranceDTO.getQuarterIdentification())) {
//            request.setBillType(HmcInsuranceBillTypeEnum.QUARTER.getType());
//            InsuranceNotify.RequestDataDTO.PremiumPlanListDTO premiumPlanListDTO = insuranceNotify.getRequestData().getPremiumPlanList().get(0);
//            request.setCurrentEndTime(premiumPlanListDTO.getEndTime());
//        }
//
//        // 如果是年度类型
//        if (insuranceNotify.getRequestData().getComboName().equals(insuranceDTO.getYearIdentification())) {
//            request.setBillType(HmcInsuranceBillTypeEnum.YEAR.getType());
//            request.setCurrentEndTime(insuranceNotify.getRequestData().getExpiredTime());
//        }

        // 这里判断：如果月交计划不为空，则为月交，否则为年交
        if (CollUtil.isNotEmpty(insuranceNotify.getRequestData().getPremiumPlanList())) {
            request.setBillType(HmcInsuranceBillTypeEnum.QUARTER.getType());
            InsuranceNotify.RequestDataDTO.PremiumPlanListDTO premiumPlanListDTO = insuranceNotify.getRequestData().getPremiumPlanList().get(0);
            request.setCurrentEndTime(premiumPlanListDTO.getEndTime());
        }

        // 如果是年度类型
        if (CollUtil.isEmpty(insuranceNotify.getRequestData().getPremiumPlanList())) {
            request.setBillType(HmcInsuranceBillTypeEnum.YEAR.getType());
            request.setCurrentEndTime(insuranceNotify.getRequestData().getExpiredTime());
        }

        return request;
    }


}
