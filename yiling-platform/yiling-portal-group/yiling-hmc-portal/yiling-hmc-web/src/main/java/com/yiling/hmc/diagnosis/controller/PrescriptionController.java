package com.yiling.hmc.diagnosis.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.diagnosis.form.*;
import com.yiling.hmc.diagnosis.vo.*;
import com.yiling.hmc.order.api.MarketOrderApi;
import com.yiling.hmc.order.dto.MarketOrderDTO;
import com.yiling.hmc.order.enums.*;
import com.yiling.hmc.order.vo.CreateMarketOrderVO;
import com.yiling.hmc.wechat.dto.request.PrescriptionOrderSubmitRequest;
import com.yiling.ih.patient.api.HmcPrescriptionApi;
import com.yiling.ih.patient.dto.*;
import com.yiling.ih.patient.dto.request.*;
import com.yiling.user.system.bo.CurrentUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 处方控制器
 *
 * @author: fan.shen
 * @date: 2022-12-12
 */
@Api(tags = "处方控制器")
@RestController
@RequestMapping("/prescription")
@Slf4j
public class PrescriptionController extends BaseController {

    @DubboReference
    HmcPrescriptionApi prescriptionApi;

    @DubboReference
    MarketOrderApi marketOrderApi;

    @ApiOperation(value = "我的处方列表")
    @PostMapping("/myPre/list")
    @Log(title = "我的处方列表", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<HmcPrescriptionVO>> myPreList(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid QueryPageListForm form) {
        HmcMyPrescriptionPageRequest request = PojoUtils.map(form, HmcMyPrescriptionPageRequest.class);
        request.setFromUserId(currentUser.getCurrentUserId().intValue());
        HmcMyPrescriptionResultDTO prescriptionResultDTO = prescriptionApi.myPreList(request);
        Page<HmcPrescriptionVO> result = form.getPage();
        if (Objects.isNull(prescriptionResultDTO) || prescriptionResultDTO.getTotal() <= 0) {
            return Result.success(result);
        }
        result.setTotal(prescriptionResultDTO.getTotal());
        List<HmcMyPrescriptionDTO> list = prescriptionResultDTO.getList();
        List<HmcPrescriptionVO> map = PojoUtils.map(list, HmcPrescriptionVO.class);
        map.forEach(item -> {
            MarketOrderDTO marketOrderDTO = marketOrderApi.queryPrescriptionOrderByPrescriptionId(Long.parseLong(item.getPrescriptionId().toString()));
            item.setOrderId(Objects.nonNull(marketOrderDTO) ? marketOrderDTO.getId() : null);
        });
        result.setRecords(map);
        return Result.success(result);
    }

    @ApiOperation(value = "处方详情接口")
    @PostMapping("/getDetailById")
    @Log(title = "处方详情接口", businessType = BusinessTypeEnum.OTHER)
    public Result<HmcPrescriptionDetailVO> getDetailById(@CurrentUser CurrentUserInfo currentUser, @RequestBody GetPrescriptionDetailForm form) {
        HmcPrescriptionDetailDTO prescriptionDetailDTO = prescriptionApi.getDetailById(form.getId());
        HmcPrescriptionDetailVO prescriptionDetailVO = PojoUtils.map(prescriptionDetailDTO, HmcPrescriptionDetailVO.class);
        if (Objects.nonNull(prescriptionDetailVO) && Objects.nonNull(prescriptionDetailVO.getId()) && prescriptionDetailVO.getId() > 0) {
            MarketOrderDTO marketOrderDTO = marketOrderApi.queryPrescriptionOrderByPrescriptionId(Long.parseLong(prescriptionDetailDTO.getId().toString()));
            prescriptionDetailVO.setOrderId(Objects.nonNull(marketOrderDTO) ? marketOrderDTO.getId() : null);
        }
        return Result.success(prescriptionDetailVO);
    }

    @ApiOperation(value = "处方药品信息接口")
    @PostMapping("/getPrescriptionGoodsById")
    @Log(title = "处方药品信息接口", businessType = BusinessTypeEnum.OTHER)
    public Result<HmcPrescriptionGoodsInfoVO> getPrescriptionGoodsById(@CurrentUser CurrentUserInfo currentUser, @RequestBody GetPrescriptionDetailForm form) {
        HmcPrescriptionGoodsInfoDTO prescriptionDetailDTO = prescriptionApi.getPrescriptionGoodsById(form.getId());
        return Result.success(PojoUtils.map(prescriptionDetailDTO, HmcPrescriptionGoodsInfoVO.class));
    }

    @PostMapping("/createOrder")
    @ApiOperation(value = "处方创建订单", notes = "处方创建订单")
    @Log(title = "处方创建订单", businessType = BusinessTypeEnum.OTHER)
    public Result<CreateMarketOrderVO> createOrder(@CurrentUser CurrentUserInfo currentUser, @Valid @RequestBody HmcCreatePrescriptionOrderForm form) {
        log.info("createOrder 参数form：{}", JSONUtil.toJsonStr(form));
        PrescriptionOrderSubmitRequest request = PojoUtils.map(form, PrescriptionOrderSubmitRequest.class);
        request.setOpUserId(currentUser.getCurrentUserId());
        request.setHmcOrderStatus(HmcOrderStatusEnum.UN_PAY);
        request.setPaymentStatusEnum(HmcPaymentStatusEnum.UN_PAY);
        request.setPaymentMethodEnum(HmcPaymentMethodEnum.WECHAT_PAY);
        request.setDeliveryType(HmcDeliveryTypeEnum.FREIGHT);
        request.setCreateSource(HmcCreateSourceEnum.HMC_MA);
        request.setEid(form.getHmcEid());
        request.setEname(form.getHmcEname());
        request.setIhEid(form.getIhEid());
        request.setIhEname(form.getIhEname());
        request.setPrescriptionId(form.getPrescriptionId());
        request.setPrescriptionPrice(form.getPrescriptionPrice());
        log.info("createOrder request：{}", JSONUtil.toJsonStr(request));

        try {
            MarketOrderDTO marketOrderDTO = marketOrderApi.createPrescriptionOrder(request);
            log.info("创建订单完成：{}", marketOrderDTO);
            return Result.success(PojoUtils.map(marketOrderDTO, CreateMarketOrderVO.class));
        } catch (Exception e) {
            log.error("创建订单失败，错误信息:{}", e.getMessage(), e);
            return Result.failed("创建订单失败");
        }
    }

}