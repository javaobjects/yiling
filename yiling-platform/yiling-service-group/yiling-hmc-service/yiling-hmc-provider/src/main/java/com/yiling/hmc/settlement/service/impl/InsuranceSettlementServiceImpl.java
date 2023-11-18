package com.yiling.hmc.settlement.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.thread.SpringAsyncConfig;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.config.TkConfigProperties;
import com.yiling.hmc.insurance.bo.TkDrugBO;
import com.yiling.hmc.insurance.bo.TkFileBO;
import com.yiling.hmc.insurance.bo.TkOrderBO;
import com.yiling.hmc.insurance.bo.TkOrderDetailBO;
import com.yiling.hmc.insurance.bo.TkOrderResult;
import com.yiling.hmc.insurance.bo.TkRecipelInfoBO;
import com.yiling.hmc.insurance.bo.TkResult;
import com.yiling.hmc.insurance.entity.InsuranceCompanyDO;
import com.yiling.hmc.insurance.entity.InsuranceDO;
import com.yiling.hmc.insurance.enums.HmcInsuranceCompanyErrorCode;
import com.yiling.hmc.insurance.enums.TkResultCodeEnum;
import com.yiling.hmc.insurance.service.InsuranceCompanyService;
import com.yiling.hmc.insurance.service.InsuranceService;
import com.yiling.hmc.order.dto.OrderPrescriptionDTO;
import com.yiling.hmc.order.entity.OrderDO;
import com.yiling.hmc.order.entity.OrderDetailDO;
import com.yiling.hmc.order.enums.HmcOrderErrorCode;
import com.yiling.hmc.order.enums.HmcOrderStatusEnum;
import com.yiling.hmc.order.enums.HmcPrescriptionStatusEnum;
import com.yiling.hmc.order.enums.HmcSynchronousTypeEnum;
import com.yiling.hmc.order.service.OrderDetailService;
import com.yiling.hmc.order.service.OrderPrescriptionService;
import com.yiling.hmc.order.service.OrderService;
import com.yiling.hmc.order.util.TkUtil;
import com.yiling.hmc.settlement.bo.InsuranceSettlementPageBO;
import com.yiling.hmc.settlement.bo.InsuranceSettlementPageResultBO;
import com.yiling.hmc.settlement.dao.InsuranceSettlementMapper;
import com.yiling.hmc.settlement.dto.request.InsuranceSettlementCallbackRequest;
import com.yiling.hmc.settlement.dto.request.InsuranceSettlementPageRequest;
import com.yiling.hmc.settlement.dto.request.InsuranceSettlementRequest;
import com.yiling.hmc.settlement.dto.request.SyncOrderRequest;
import com.yiling.hmc.settlement.entity.InsuranceSettlementDO;
import com.yiling.hmc.settlement.entity.InsuranceSettlementDetailDO;
import com.yiling.hmc.settlement.enums.HmcSettlementErrorCode;
import com.yiling.hmc.settlement.enums.InsuranceSettlementStatusEnum;
import com.yiling.hmc.settlement.service.InsuranceSettlementDetailService;
import com.yiling.hmc.settlement.service.InsuranceSettlementService;
import com.yiling.hmc.wechat.dto.InsuranceRecordDTO;
import com.yiling.hmc.wechat.enums.HmcInsuranceBillTypeEnum;
import com.yiling.hmc.wechat.service.InsuranceRecordService;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 保司结账表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-31
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InsuranceSettlementServiceImpl extends BaseServiceImpl<InsuranceSettlementMapper, InsuranceSettlementDO> implements InsuranceSettlementService {

    @DubboReference
    EnterpriseApi enterpriseApi;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final OrderService orderService;

    private final OrderDetailService orderDetailService;

    private final OrderPrescriptionService orderPrescriptionService;

    private final InsuranceCompanyService insuranceCompanyService;

    private final InsuranceRecordService insuranceRecordService;

    private final InsuranceService insuranceService;

    private final InsuranceSettlementDetailService insuranceSettlementDetailService;

    private final FileService fileService;

    private final TkConfigProperties tkConfigProperties;

    private final SpringAsyncConfig springAsyncConfig;

    @Override
    public InsuranceSettlementPageResultBO pageList(InsuranceSettlementPageRequest request) {
        if (null != request.getStartTime()) {
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }
        if (null != request.getStopTime()) {
            request.setStopTime(DateUtil.endOfDay(request.getStopTime()));
        }
        Page<InsuranceSettlementDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        Page<InsuranceSettlementPageBO> page = this.getBaseMapper().pageList(objectPage, request);

        InsuranceSettlementPageResultBO resultBO = this.getBaseMapper().pageResult(request);
        resultBO.setPage(page);
        return resultBO;
    }

    @Override
    @GlobalTransactional
    public boolean importSettlementData(InsuranceSettlementRequest request) {
        log.info("保司导入已结算订单,请求数据为:[{}]", JSON.toJSONString(request));
        String orderNoStr = request.getOrderNoStr();
        String[] orderNoStrList = orderNoStr.split(",");
        List<String> orderNoList = new ArrayList<>(Arrays.asList(orderNoStrList));
        List<InsuranceSettlementDetailDO> insuranceSettlementDetailDOList = insuranceSettlementDetailService.listByOrderNoList(orderNoList);
        if (CollUtil.isNotEmpty(insuranceSettlementDetailDOList)) {
            throw new BusinessException(HmcSettlementErrorCode.INSURANCE_SETTLEMENT_ORDER_EXIST_ERROR);
        }

        InsuranceCompanyDO insuranceCompanyDO = insuranceCompanyService.getById(request.getInsuranceCompanyId());
        if (null == insuranceCompanyDO) {
            return false;
        }
        InsuranceRecordDTO insuranceRecordDTO = insuranceRecordService.getByPolicyNo(request.getPolicyNo());
        if (null == insuranceRecordDTO) {
            return false;
        }
        // 1.新增结算单
        InsuranceSettlementDO insuranceSettlementDO = PojoUtils.map(request, InsuranceSettlementDO.class);
        insuranceSettlementDO.setInsuranceRecordId(insuranceRecordDTO.getId());
        insuranceSettlementDO.setInsuranceCompanyName(insuranceCompanyDO.getCompanyName());
        insuranceSettlementDO.setInsuranceSettleStatus(InsuranceSettlementStatusEnum.SETTLEMENT.getCode());
        this.save(insuranceSettlementDO);

        // 2.新增结算单明细

        for (String orderNo : orderNoStrList) {
            OrderDO orderDO = orderService.queryByOrderNo(orderNo);
            if (null == orderDO) {
                throw new BusinessException(HmcOrderErrorCode.ORDER_NO_NOT_EXISTS);
            }
            if (InsuranceSettlementStatusEnum.getByCode(orderDO.getInsuranceSettleStatus()) != InsuranceSettlementStatusEnum.UN_SETTLEMENT) {
                continue;
            }

            InsuranceSettlementDetailDO insuranceSettlementDetailDO = new InsuranceSettlementDetailDO();
            insuranceSettlementDetailDO.setInsuranceSettlementId(insuranceSettlementDO.getId());
            insuranceSettlementDetailDO.setInsuranceRecordId(orderDO.getInsuranceRecordId());
            insuranceSettlementDetailDO.setOrderId(orderDO.getId());
            insuranceSettlementDetailDO.setOrderNo(orderNo);
            insuranceSettlementDetailDO.setPayAmount(orderDO.getInsuranceSettleAmount());
            insuranceSettlementDetailDO.setOpUserId(request.getOpUserId());
            insuranceSettlementDetailDO.setOpTime(request.getOpTime());
            insuranceSettlementDetailService.save(insuranceSettlementDetailDO);

            // 3.修改订单保司结算状态
            OrderDO order = new OrderDO();
            order.setId(orderDO.getId());
            order.setInsuranceSettleStatus(InsuranceSettlementStatusEnum.SETTLEMENT.getCode());
            order.setOpUserId(request.getOpUserId());
            order.setOpTime(request.getOpTime());
            orderService.updateById(order);
        }
        return true;
    }

    @Override
    public boolean callback(InsuranceSettlementCallbackRequest request) {
        InsuranceSettlementDO insuranceSettlementDO = new InsuranceSettlementDO();
        OrderDO orderDO = orderService.queryByOrderNo(request.getOrderNo());
        if (null == orderDO) {
            throw new BusinessException(HmcOrderErrorCode.ORDER_NO_NOT_EXISTS);
        }
        if (InsuranceSettlementStatusEnum.SETTLEMENT.getCode().equals(orderDO.getInsuranceSettleStatus())) {
            throw new BusinessException(HmcSettlementErrorCode.INSURANCE_SETTLEMENT_EXIST_ERROR);
        }
        InsuranceCompanyDO insuranceCompanyDO = insuranceCompanyService.getById(orderDO.getInsuranceCompanyId());
        if (null == insuranceCompanyDO) {
            throw new BusinessException(HmcInsuranceCompanyErrorCode.INSURANCE_COMPANY_NOT_EXISTS);
        }
        InsuranceRecordDTO insuranceRecordDTO = insuranceRecordService.getById(orderDO.getInsuranceRecordId());

        // 保司结算单创建
        insuranceSettlementDO.setIssueName(insuranceRecordDTO.getIssueName());
        insuranceSettlementDO.setInsuranceCompanyId(orderDO.getInsuranceCompanyId());
        insuranceSettlementDO.setInsuranceCompanyName(insuranceCompanyDO.getCompanyName());
        insuranceSettlementDO.setInsuranceRecordId(orderDO.getInsuranceRecordId());
        insuranceSettlementDO.setPolicyNo(orderDO.getPolicyNo());
        insuranceSettlementDO.setThirdPayNo(request.getThirdPayNo());
        insuranceSettlementDO.setInsuranceSettleStatus(InsuranceSettlementStatusEnum.SETTLEMENT.getCode());
        insuranceSettlementDO.setAccountNo("");
        insuranceSettlementDO.setPayTime(request.getPayTime());
        insuranceSettlementDO.setPayAmount(request.getPayAmount());
        insuranceSettlementDO.setOpUserId(request.getOpUserId());
        insuranceSettlementDO.setOpTime(request.getOpTime());
        this.save(insuranceSettlementDO);

        InsuranceSettlementDetailDO insuranceSettlementDetailDO = new InsuranceSettlementDetailDO();
        insuranceSettlementDetailDO.setInsuranceSettlementId(insuranceSettlementDO.getId());
        insuranceSettlementDetailDO.setInsuranceRecordId(orderDO.getInsuranceRecordId());
        insuranceSettlementDetailDO.setOrderId(orderDO.getId());
        insuranceSettlementDetailDO.setOrderNo(orderDO.getOrderNo());
        insuranceSettlementDetailDO.setPayAmount(request.getPayAmount());
        insuranceSettlementDetailDO.setOpUserId(request.getOpUserId());
        insuranceSettlementDetailDO.setOpTime(request.getOpTime());
        insuranceSettlementDetailService.save(insuranceSettlementDetailDO);

        // 订单保司结算状态修改
        OrderDO order = new OrderDO();
        order.setId(orderDO.getId());
        order.setInsuranceSettleStatus(InsuranceSettlementStatusEnum.SETTLEMENT.getCode());
        order.setOpUserId(request.getOpUserId());
        order.setOpTime(request.getOpTime());
        orderService.updateById(order);

        return true;
    }

    @Override
    public boolean syncOrder(SyncOrderRequest request) {
        long timeBegin = System.currentTimeMillis();
        // 校验是否达到同步条件   订单状态为已完成且与保司数据同步状态待同步与同步失败，且已有处方的订单数据
        OrderDO orderDO = orderService.getById(request.getId());
        if (!HmcOrderStatusEnum.FINISHED.getCode().equals(orderDO.getOrderStatus())) {
            throw new BusinessException(HmcOrderErrorCode.ORDER_STATUS_ERROR);
        }
        if (HmcSynchronousTypeEnum.SYNCED.getCode().equals(orderDO.getSynchronousType())) {
            throw new BusinessException(HmcOrderErrorCode.INSURANCE_ORDER_HAVE_SYNC);
        }
        if (!HmcPrescriptionStatusEnum.HAVE_PRESCRIPTION.getCode().equals(orderDO.getPrescriptionStatus())) {
            throw new BusinessException(HmcOrderErrorCode.ORDER_PRESCRIPTION_NOT_EXISTS);
        }

        // 理赔资料准备
        InsuranceRecordDTO insuranceRecordDTO = insuranceRecordService.getById(orderDO.getInsuranceRecordId());
        if (StringUtils.isBlank(insuranceRecordDTO.getIdCardFront())) {
            throw new BusinessException(HmcSettlementErrorCode.ID_CARD_FRONT_PHOTO_NOT_EXIST);
        }
        String idCardFrontUrl = fileService.getReSizeUrl(insuranceRecordDTO.getIdCardFront(), FileTypeEnum.ID_CARD_FRONT_PHOTO);

        if (StringUtils.isBlank(insuranceRecordDTO.getIdCardBack())) {
            throw new BusinessException(HmcSettlementErrorCode.ID_CARD_BACK_PHOTO_NOT_EXIST);
        }
        String idCardBackUrl = fileService.getReSizeUrl(insuranceRecordDTO.getIdCardBack(), FileTypeEnum.ID_CARD_BACK_PHOTO);
        if (StringUtils.isBlank(insuranceRecordDTO.getHandSignature())) {
            throw new BusinessException(HmcSettlementErrorCode.HAND_SIGNATURE_PICTURE_NOT_EXIST);
        }
        String handSignatureUrl = fileService.getReSizeUrl(insuranceRecordDTO.getHandSignature(), FileTypeEnum.HAND_SIGNATURE_PICTURE);

        OrderPrescriptionDTO prescriptionDTO = orderPrescriptionService.getByOrderId(orderDO.getId());
        if (Objects.isNull(prescriptionDTO) || StringUtils.isBlank(prescriptionDTO.getPrescriptionSnapshotUrl())) {
            throw new BusinessException(HmcSettlementErrorCode.ORDER_PRESCRIPTION_SNAPSHOT_PICTURE_NOT_EXIST);
        }
        String[] prescriptionSnapshotUrlList = prescriptionDTO.getPrescriptionSnapshotUrl().split(",");

        // 2.同步订单到保司
        InsuranceDO insuranceDO = insuranceService.getById(insuranceRecordDTO.getInsuranceId());

        TkOrderBO tkOrderBO = new TkOrderBO();
        // 提交泰康的订单信息
        buildTkOrder(tkOrderBO, insuranceRecordDTO, insuranceDO, orderDO);
        // 提交泰康的处方信息
        buildTkRecipelInfo(tkOrderBO, orderDO, prescriptionDTO);
        // 提交泰康的订单明细信息
        buildTkOrderDetail(tkOrderBO, orderDO);

        List<TkPic> tkPicList = new ArrayList<>();
        tkPicList.add(new TkPic(FileTypeEnum.ID_CARD_FRONT_PHOTO, idCardFrontUrl));
        tkPicList.add(new TkPic(FileTypeEnum.ID_CARD_BACK_PHOTO, idCardBackUrl));
        tkPicList.add(new TkPic(FileTypeEnum.HAND_SIGNATURE_PICTURE, handSignatureUrl));
        // 处方可能会有多个
        for (String prescriptionSnapshotUrl : prescriptionSnapshotUrlList) {
            tkPicList.add(new TkPic(FileTypeEnum.PRESCRIPTION_PIC, fileService.getReSizeUrl(prescriptionSnapshotUrl, FileTypeEnum.PRESCRIPTION_PIC)));
        }

        //        sendTkAsync(tkPicList, tkOrderBO, insuranceRecordDTO.getIssueCredentialNo(), request);

        CompletableFuture.runAsync(() -> sendTkAsync(tkPicList, tkOrderBO, insuranceRecordDTO.getIssueCredentialNo(), request), springAsyncConfig.getAsyncExecutor());

        log.info("同步订单到保司同步逻辑完成，耗时:[{}]ms", System.currentTimeMillis() - timeBegin);
        return true;
    }

    @Override
    public Result<Object> getSyncOrderResult(SyncOrderRequest request) {
        String value = stringRedisTemplate.opsForValue().get("tk_" + request.getId());
        if (StringUtils.isBlank(value)) {
            return Result.failed(10000, "同步订单到保司还在运行中，请稍后再试");
        }
        if ("success".equals(value)) {
            return Result.success(value);
        } else {
            return Result.failed(value);
        }
    }

    /**
     * 同步订单到保司异步逻辑
     *
     * @param tkPicList 需要推送的理赔资料信息（图片）
     * @param tkOrderBO 推送到保司的数据保单实体
     * @param issueCredentialNo 用户的证件号
     * @param request 同步保司接口的请求数据
     */
    private void sendTkAsync(List<TkPic> tkPicList, TkOrderBO tkOrderBO, String issueCredentialNo, SyncOrderRequest request) {
        long timeBegin = System.currentTimeMillis();
        try {
            // 1.理赔资料上传
            List<TkFileBO> fileList = new ArrayList<>();
            for (TkPic tkPic : tkPicList) {
                TkFileBO tkFileBO = null;
                if (tkPic.getFileTypeEnum() == FileTypeEnum.ID_CARD_FRONT_PHOTO) {
                    tkFileBO = sendTkUrl("P101A", sendPic(tkPic.getUrl(), issueCredentialNo, 1), "00");
                } else if (tkPic.getFileTypeEnum() == FileTypeEnum.ID_CARD_BACK_PHOTO) {
                    tkFileBO = sendTkUrl("P101B", sendPic(tkPic.getUrl(), issueCredentialNo, 2), "00");
                } else if (tkPic.getFileTypeEnum() == FileTypeEnum.HAND_SIGNATURE_PICTURE) {
                    tkFileBO = sendTkUrl("P201", sendPic(tkPic.getUrl(), issueCredentialNo, 3), "00");
                } else if (tkPic.getFileTypeEnum() == FileTypeEnum.PRESCRIPTION_PIC) {
                    tkFileBO = sendTkUrl("S104", sendPic(tkPic.getUrl(), issueCredentialNo, 7), "00");
                }
                if (Objects.nonNull(tkFileBO)) {
                    fileList.add(tkFileBO);
                }
            }

            // 提交泰康的文件信息
            tkOrderBO.setFileVoList(fileList);

            // 2.同步订单到保司
            log.info("同步订单数据到泰康接口->订单同步请求数据内容为:[{}],耗时:[{}]", JSONUtil.toJsonStr(tkOrderBO), System.currentTimeMillis() - timeBegin);
            // 提交已完成订单数据到泰康
            TkResult<TkOrderResult> tkResult = sendOrderToTk(tkOrderBO);
            if ("success".equals(tkResult.getResult())) {
                TkOrderResult sendOrderToTkResult = tkResult.getData();
                // 修改订单同步状态
                if (0 == sendOrderToTkResult.getClaimResultCode()) {
                    OrderDO order = new OrderDO();
                    order.setId(request.getId());
                    order.setSynchronousType(HmcSynchronousTypeEnum.SYNCED.getCode());
                    order.setOpUserId(request.getOpUserId());
                    orderService.updateById(order);
                    stringRedisTemplate.opsForValue().set("tk_" + request.getId(), "success", 50, TimeUnit.SECONDS);
                }
                // 理算结果 0 代表成功
                else if (0 != sendOrderToTkResult.getClaimResultCode()) {
                    // 保司同步失败：1⃣️理算结果：药品目录校验未通过；2⃣️结果说明：次购药金额校验不通过等
                    TkResultCodeEnum tkResultCodeEnum = TkResultCodeEnum.getByCode(sendOrderToTkResult.getClaimResultCode());
                    String message;
                    if (Objects.nonNull(tkResultCodeEnum)) {
                        message = "保司同步失败：1-理算结果：" + tkResultCodeEnum.getName() + "；2-结果说明：" + sendOrderToTkResult.getClaimResultMsg();
                    } else {
                        message = sendOrderToTkResult.getClaimResultMsg();
                    }
                    OrderDO order = new OrderDO();
                    order.setId(request.getId());
                    order.setSynchronousType(HmcSynchronousTypeEnum.FAILED.getCode());
                    order.setOpUserId(request.getOpUserId());
                    orderService.updateById(order);
                    stringRedisTemplate.opsForValue().set("tk_" + request.getId(), message, 50, TimeUnit.SECONDS);
                    return;
                }
            } else {
                log.info("同步订单数据到泰康接口->订单同步请求出现错误");
                stringRedisTemplate.opsForValue().set("tk_" + request.getId(), tkResult.getMessage(), 50, TimeUnit.SECONDS);
                return;
            }

            // 3.同步订单状态到保司
            orderService.syncOrderStatus(request.getId());

            log.info("同步订单数据到泰康接口->操作逻辑返回数据为:[{}]，耗时:[{}]", JSONUtil.toJsonStr(tkResult), System.currentTimeMillis() - timeBegin);
        } catch (BusinessException ex) {
            log.info("同步订单数据到泰康接口->异步逻辑出现错误:[{}],耗时:[{}]", ex, System.currentTimeMillis() - timeBegin);
            stringRedisTemplate.opsForValue().set("tk_" + request.getId(), ex.getMessage(), 50, TimeUnit.SECONDS);
        }
    }

    /**
     * 构建泰康订单数据
     *
     * @param tkOrderBO 同步泰康订单数据
     * @param insuranceRecordDTO 保险记录信息数据
     * @param insuranceDO 保险数据
     * @param orderDO 订单数据
     */
    @SneakyThrows
    private void buildTkOrder(TkOrderBO tkOrderBO, InsuranceRecordDTO insuranceRecordDTO, InsuranceDO insuranceDO, OrderDO orderDO) {
        tkOrderBO.setHasPayInfo(false);
        tkOrderBO.setHasRecipel(true);
        tkOrderBO.setSourceChannelCode(tkConfigProperties.getSourceChannelCode());
        tkOrderBO.setSourceChannelName(tkConfigProperties.getSourceChannelName());
        if (HmcInsuranceBillTypeEnum.YEAR.getType().equals(insuranceRecordDTO.getBillType())) {
            tkOrderBO.setProductCode(insuranceDO.getYearIdentification());
        }
        if (HmcInsuranceBillTypeEnum.QUARTER.getType().equals(insuranceRecordDTO.getBillType())) {
            tkOrderBO.setProductCode(insuranceDO.getQuarterIdentification());
        }
        tkOrderBO.setPolicyNo(orderDO.getPolicyNo());
        tkOrderBO.setChannelMainId(orderDO.getOrderNo());
        tkOrderBO.setApplyUserName(insuranceRecordDTO.getIssueName());
        tkOrderBO.setApplyUserPhone(insuranceRecordDTO.getIssuePhone());
        tkOrderBO.setApplyUserCid(insuranceRecordDTO.getIssueCredentialNo());
        tkOrderBO.setApplyUserCidType(1);
        tkOrderBO.setPatientName(insuranceRecordDTO.getIssueName());
        // 生日(yyyyMMdd)
        String birth = IdcardUtil.getBirth(insuranceRecordDTO.getIssueCredentialNo());
        tkOrderBO.setPatientBirth(DateUtils.parseDate(birth, DatePattern.PURE_DATE_PATTERN));
        tkOrderBO.setPatientCid(insuranceRecordDTO.getIssueCredentialNo());
        tkOrderBO.setPatientCidType(1);
        // (1 : 男 ， 0 : 女)
        int gender = IdcardUtil.getGenderByIdCard(insuranceRecordDTO.getIssueCredentialNo());
        tkOrderBO.setPatientGender(0 == gender ? "2" : "1");
        tkOrderBO.setPatientPhone(insuranceRecordDTO.getIssuePhone());
        tkOrderBO.setPharmacyType(4);
        tkOrderBO.setConsultationType(2);
        tkOrderBO.setStatus(23);
        tkOrderBO.setMarketPrices(orderDO.getInsuranceSettleAmount().toPlainString());
        tkOrderBO.setApplyTime(orderDO.getOrderTime());
    }

    /**
     * 构造recipelInfo对象（处方）信息
     *
     * @param tkOrderBO 同步泰康订单数据
     * @param orderDO 订单信息
     * @param prescriptionDTO 处方表数据
     */
    private void buildTkRecipelInfo(TkOrderBO tkOrderBO, OrderDO orderDO, OrderPrescriptionDTO prescriptionDTO) {
        // recipelInfo对象 处方信息
        TkRecipelInfoBO recipelInfo = new TkRecipelInfoBO();
        recipelInfo.setThirdId(orderDO.getOrderNo());
        recipelInfo.setPrescribingTime(prescriptionDTO.getReceiptDate());
        recipelInfo.setDiseaseCodeTable(1);
        recipelInfo.setDiseaseCode("I25.104");
        recipelInfo.setDiseaseName("冠心病心律失常型");
        recipelInfo.setRecipelTotalPrices(orderDO.getInsuranceSettleAmount().toPlainString());
        tkOrderBO.setRecipelInfo(recipelInfo);
    }

    /**
     * 构造orderDetail对象信息
     *
     * @param tkOrderBO 同步泰康订单数据
     * @param orderDO 订单信息
     */
    private void buildTkOrderDetail(TkOrderBO tkOrderBO, OrderDO orderDO) {
        // orderDetail对象信息
        List<TkOrderDetailBO> orderList = new ArrayList<>();
        List<OrderDetailDO> orderDetailDOList = orderDetailService.listByOrderId(orderDO.getId());
        TkOrderDetailBO tkOrderDetailBO = new TkOrderDetailBO();
        tkOrderDetailBO.setChannelOrderId(orderDO.getOrderNo());
        tkOrderDetailBO.setChannelCode(tkConfigProperties.getChannelCode());
        tkOrderDetailBO.setIsTraditional(0);
        tkOrderDetailBO.setReceiveWay(0);
        tkOrderDetailBO.setDeliverway(0);
        tkOrderDetailBO.setDrugstoreName(orderDO.getEname());
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(orderDO.getEid());
        tkOrderDetailBO.setDrugstoreAddress(enterpriseDTO.getProvinceName() + enterpriseDTO.getCityName() + enterpriseDTO.getRegionName() + enterpriseDTO.getAddress());
        tkOrderDetailBO.setOrderStatus(23);
        tkOrderDetailBO.setOrderTotalPrices(orderDO.getInsuranceSettleAmount().toPlainString());

        buildTkDrugDto(tkOrderDetailBO, orderDetailDOList);
        orderList.add(tkOrderDetailBO);
        tkOrderBO.setOrderList(orderList);
    }

    /**
     * 构造药品信息
     *
     * @param tkOrderDetailBO 同步泰康订单里的订单明细数据
     * @param orderDetailDOList 订单明细表数据
     */
    private void buildTkDrugDto(TkOrderDetailBO tkOrderDetailBO, List<OrderDetailDO> orderDetailDOList) {
        // 构造药品信息
        List<TkDrugBO> drugList = orderDetailDOList.stream().map(item -> {
            TkDrugBO drugDto = new TkDrugBO();
            drugDto.setDrugCode(item.getInsuranceGoodsCode());
            drugDto.setChannelDrugCode(item.getSellSpecificationsId().toString());
            drugDto.setDrugCategory("1");
            drugDto.setDrugGoodName(item.getGoodsName());
            drugDto.setDrugGenericName(item.getGoodsName());
            drugDto.setSpecification(item.getGoodsSpecifications());
            drugDto.setNum(item.getGoodsQuantity().intValue());
            drugDto.setMarketPrice(item.getSettlePrice().toPlainString());
            return drugDto;
        }).collect(Collectors.toList());
        tkOrderDetailBO.setDrugList(drugList);
    }

    /**
     * 同步订单到泰康
     *
     * @param tkOrderBO 同步泰康订单数据
     */
    private TkResult<TkOrderResult> sendOrderToTk(TkOrderBO tkOrderBO) {
        String params = TkUtil.encrypt(JSONUtil.toJsonStr(tkOrderBO), tkConfigProperties.getParamsKey());
        long currentSeconds = DateUtil.currentSeconds();

        Map<String, String> signMap = new HashMap<>();
        signMap.put("appId", tkConfigProperties.getAppId());
        signMap.put("params", params);
        signMap.put("timestamp", String.valueOf(currentSeconds));
        signMap.put("cipher", "123456");
        String sign = TkUtil.generateSign(signMap, tkConfigProperties.getSecretKey());

        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("timestamp", currentSeconds);
        paramMap.put("sign", sign);
        paramMap.put("appId", tkConfigProperties.getAppId());
        paramMap.put("secretKey", tkConfigProperties.getSecretKey());
        paramMap.put("params", params);

        String url = tkConfigProperties.getTkServerUrl() + "/pbm/adjustment/calculate";
        long timeBegin = System.currentTimeMillis();
        log.info("同步订单数据到泰康接口，请求数据为:[{}]", JSONUtil.toJsonStr(paramMap));
        String result = HttpUtil.post(url, JSONUtil.toJsonStr(paramMap));
        log.info("同步订单数据到泰康接口，返回数据为:[{}],耗时:[{}]", result, System.currentTimeMillis() - timeBegin);
        TkResult<TkOrderResult> tkResult = JSONUtil.toBean(result, new TypeReference<TkResult<TkOrderResult>>() {
        }, false);
        return tkResult;
    }

    @Data
    class TkPic {
        private FileTypeEnum fileTypeEnum;

        private String url;

        public TkPic(FileTypeEnum fileTypeEnum, String url) {
            this.fileTypeEnum = fileTypeEnum;
            this.url = url;
        }
    }

    /**
     * 同步订单到保司时需要的理赔资料上传信息
     *
     * @param fileTypeCode 文件类型
     * @param filePath 文件地址(带后缀)
     * @param mediaType 资料类型 00-图片,01-视频,02-PDF,03-WORD等
     * @return 同步订单到保司时的文件信息
     */
    private TkFileBO sendTkUrl(String fileTypeCode, String filePath, String mediaType) {
        TkFileBO tkFileBO = new TkFileBO();
        tkFileBO.setFileTypeCode(fileTypeCode);
        tkFileBO.setFilePath(filePath);
        tkFileBO.setMediaType(mediaType);
        return tkFileBO;
    }

    /**
     * 理赔资料上传 - 上传请求
     *
     * @param picUrl 图片地址
     * @param issueCredentialNo 被保人证件号
     * @param fileType 图片类型1:身份证（正面） 2:身份证（反面）
     *         3:签名 5:身份证明 6:关系证明 7:处方影像件 8：理赔申请书  9：发票or费用清单
     */
    private String sendPic(String picUrl, String issueCredentialNo, Integer fileType) {
        long timeBegin = System.currentTimeMillis();
        log.info("同步订单数据到泰康接口->理赔资料图片地址为:[{}],图片类型为:[{}],图片类型1:身份证（正面） 2:身份证（反面）3:签名 5:身份证明 6:关系证明 7:处方影像件 8：理赔申请书  9：发票or费用清单", picUrl, fileType);
        String tkServerUrl = tkConfigProperties.getTkServerUrl();
        String uploadUrl = tkServerUrl + "/pbm/adjustment/uploadFile";
        File file;
        File newImage;
        try {
            file = UrlToFile.urlToFile(new URL(picUrl));
            newImage = zipImage(file);
        } catch (Exception ex) {
            log.error("同步订单数据到泰康接口->理赔资料上传出现错误:" + ex);
            throw new BusinessException(HmcSettlementErrorCode.INSURANCE_RECORD_PIC_PUSH_ERROR);
        }
        if (Objects.isNull(file)) {
            throw new BusinessException(HmcSettlementErrorCode.INSURANCE_RECORD_DOWNLOAD_PIC_PUSH_ERROR);
        }
        String returnJson = HttpRequest.post(uploadUrl).header(Header.CONTENT_TYPE, "multipart/form-data").form("multipartFile", newImage).form("fileType", fileType).form("beCardNo", issueCredentialNo).form("applyCardNo", issueCredentialNo).execute().body();
        log.info("同步订单数据到泰康接口->理赔资料上传,返回数据为:[{}]，耗时:[{}]", returnJson, System.currentTimeMillis() - timeBegin);
        if (file.exists()) {
            file.delete();
        }
        if (newImage.exists()) {
            newImage.delete();
        }
        TkResult<String> tkResult = JSONUtil.toBean(returnJson, new TypeReference<TkResult<String>>() {
        }, false);
        if ("success".equals(tkResult.getResult())) {
            return tkResult.getData();
        } else {
            throw new BusinessException(HmcSettlementErrorCode.INSURANCE_RECORD_PIC_PUSH_ERROR, tkResult.getMessage());
        }

    }

    /**
     * 压缩图片
     *
     * @param file 原缓存图片
     * @return 压缩后的图片
     */
    private File zipImage(File file) {
        FileOutputStream fileOutputStream = null;
        try {
            BufferedImage templateImage = ImageIO.read(file);
            int height = templateImage.getHeight();
            int width = templateImage.getWidth();
            float scale = 0.4f;
            int withHeight = (int) (scale * height);
            int withWidth = (int) (scale * width);
            BufferedImage finalImage = new BufferedImage(withWidth, withHeight, BufferedImage.TYPE_INT_RGB);
            finalImage.getGraphics().drawImage(templateImage.getScaledInstance(withWidth, withHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
            File newFile = File.createTempFile("tk_temp_", file.getName().substring(file.getName().lastIndexOf(".")));
            fileOutputStream = new FileOutputStream(newFile);
            ImageIO.write(finalImage, file.getName().substring(file.getName().lastIndexOf(".") + 1), fileOutputStream);
            return newFile;
        } catch (IOException e) {
            log.error("压缩图片报错，错误信息:{}", ExceptionUtils.getFullStackTrace(e), e);
        } finally {
            if (null != fileOutputStream) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    log.error("压缩图片报错，错误信息:{}", ExceptionUtils.getFullStackTrace(e), e);
                }
            }
        }
        return null;
    }

}
