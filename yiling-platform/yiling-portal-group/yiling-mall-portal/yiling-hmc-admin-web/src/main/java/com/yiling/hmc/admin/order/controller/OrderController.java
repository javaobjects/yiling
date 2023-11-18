package com.yiling.hmc.admin.order.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.admin.order.form.CheckCardLastSixForm;
import com.yiling.hmc.admin.order.form.OrderDeliverForm;
import com.yiling.hmc.admin.order.form.OrderPageForm;
import com.yiling.hmc.admin.order.form.OrderPrescriptionSaveForm;
import com.yiling.hmc.admin.order.form.OrderReceiptsSaveForm;
import com.yiling.hmc.admin.order.form.OrderReceivedForm;
import com.yiling.hmc.admin.order.vo.InsuranceRecordVO;
import com.yiling.hmc.admin.order.vo.OrderAndReturnDetailVO;
import com.yiling.hmc.admin.order.vo.OrderDetailVO;
import com.yiling.hmc.admin.order.vo.OrderOperateVO;
import com.yiling.hmc.admin.order.vo.OrderPageVO;
import com.yiling.hmc.admin.order.vo.OrderPrescriptionDetailVO;
import com.yiling.hmc.admin.order.vo.OrderPrescriptionGoodsVO;
import com.yiling.hmc.admin.order.vo.OrderPrescriptionVO;
import com.yiling.hmc.admin.order.vo.OrderReceiptsVO;
import com.yiling.hmc.admin.order.vo.OrderRelateUserVO;
import com.yiling.hmc.admin.order.vo.OrderReturnDetailVO;
import com.yiling.hmc.admin.order.vo.OrderReturnVO;
import com.yiling.hmc.admin.order.vo.OrderVO;
import com.yiling.hmc.admin.order.vo.PrescriptionSnapshotUrlVO;
import com.yiling.hmc.control.api.GoodsPurchaseControlApi;
import com.yiling.hmc.insurance.api.InsuranceApi;
import com.yiling.hmc.insurance.api.InsuranceCompanyApi;
import com.yiling.hmc.insurance.dto.InsuranceCompanyDTO;
import com.yiling.hmc.insurance.dto.InsuranceDTO;
import com.yiling.hmc.order.api.OrderApi;
import com.yiling.hmc.order.api.OrderDetailApi;
import com.yiling.hmc.order.api.OrderDetailControlApi;
import com.yiling.hmc.order.api.OrderOperateApi;
import com.yiling.hmc.order.api.OrderPrescriptionApi;
import com.yiling.hmc.order.api.OrderPrescriptionGoodsApi;
import com.yiling.hmc.order.api.OrderRelateUserApi;
import com.yiling.hmc.order.api.OrderReturnApi;
import com.yiling.hmc.order.api.OrderReturnDetailApi;
import com.yiling.hmc.order.bo.OrderDetailControlBO;
import com.yiling.hmc.order.dto.OrderDTO;
import com.yiling.hmc.order.dto.OrderDetailDTO;
import com.yiling.hmc.order.dto.OrderOperateDTO;
import com.yiling.hmc.order.dto.OrderPrescriptionDTO;
import com.yiling.hmc.order.dto.OrderPrescriptionGoodsDTO;
import com.yiling.hmc.order.dto.OrderRelateUserDTO;
import com.yiling.hmc.order.dto.OrderReturnDTO;
import com.yiling.hmc.order.dto.OrderReturnDetailDTO;
import com.yiling.hmc.order.dto.request.OrderDeliverRequest;
import com.yiling.hmc.order.dto.request.OrderPageRequest;
import com.yiling.hmc.order.dto.request.OrderPrescriptionSaveRequest;
import com.yiling.hmc.order.dto.request.OrderReceiptsSaveRequest;
import com.yiling.hmc.order.dto.request.OrderReceivedRequest;
import com.yiling.hmc.order.enums.HmcCreateSourceEnum;
import com.yiling.hmc.order.enums.HmcOrderOperateTypeEnum;
import com.yiling.hmc.order.enums.HmcOrderRelateUserTypeEnum;
import com.yiling.hmc.wechat.api.InsuranceRecordApi;
import com.yiling.hmc.wechat.dto.InsuranceRecordDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Api(tags = "订单接口")
@Slf4j
@RestController
@RequestMapping("/order/order")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController extends BaseController {

    @DubboReference
    OrderApi orderApi;

    @DubboReference
    OrderDetailApi orderDetailApi;

    @DubboReference
    OrderReturnApi orderReturnApi;

    @DubboReference
    OrderReturnDetailApi orderReturnDetailApi;

    @DubboReference
    InsuranceRecordApi insuranceRecordApi;

    @DubboReference
    OrderPrescriptionApi orderPrescriptionApi;

    @DubboReference
    OrderPrescriptionGoodsApi orderPrescriptionGoodsApi;

    @DubboReference
    InsuranceCompanyApi insuranceCompanyApi;

    @DubboReference
    UserApi userApi;

    @DubboReference
    GoodsPurchaseControlApi goodsPurchaseControlApi;

    @DubboReference
    OrderOperateApi orderOperateApi;

    @DubboReference
    OrderRelateUserApi orderRelateUserApi;

    @DubboReference
    InsuranceApi insuranceApi;

    @DubboReference
    OrderDetailControlApi orderDetailControlApi;

    @DubboReference
    EnterpriseApi enterpriseApi;


    private final FileService fileService;

    @ApiOperation(value = "订单信息分页查询")
    @PostMapping("/pageList")
    public Result<Page<OrderPageVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid OrderPageForm form) {
        OrderPageRequest request = PojoUtils.map(form, OrderPageRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        Page<OrderDTO> dtoPage = orderApi.pageList(request);
        Page<OrderPageVO> voPage = PojoUtils.map(dtoPage, OrderPageVO.class);
        if (CollUtil.isEmpty(voPage.getRecords())) {
            return Result.success(voPage);
        }
        voPage.getRecords().forEach(this::addOrderDetail);
        return Result.success(voPage);
    }

    @ApiOperation(value = "订单详情查询")
    @GetMapping("/queryDetail")
    public Result<OrderAndReturnDetailVO> queryDetail(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("orderId") @ApiParam("订单id") Long orderId) {
        OrderAndReturnDetailVO orderAndReturnDetailVO = new OrderAndReturnDetailVO();

        OrderDTO orderDTO = orderApi.queryById(orderId);
        OrderVO orderVO = PojoUtils.map(orderDTO, OrderVO.class);
        addOrderReceiptsList(orderAndReturnDetailVO, orderDTO.getOrderReceipts());

        InsuranceCompanyDTO insuranceCompanyDTO = insuranceCompanyApi.queryById(orderDTO.getInsuranceCompanyId());
        if (null != insuranceCompanyDTO) {
            orderAndReturnDetailVO.setInsuranceCompanyName(insuranceCompanyDTO.getCompanyName());
        }

        UserDTO userDTO = userApi.getById(orderDTO.getOrderUser());
        if (null != userDTO) {
            orderAndReturnDetailVO.setOrderUserName(userDTO.getName());
        }

        orderAndReturnDetailVO.setOrder(orderVO);

        InsuranceRecordDTO insuranceRecordDTO = insuranceRecordApi.getById(orderDTO.getInsuranceRecordId());
        {
            InsuranceRecordVO insuranceRecordVO = PojoUtils.map(insuranceRecordDTO, InsuranceRecordVO.class);
            orderAndReturnDetailVO.setInsuranceRecord(insuranceRecordVO);

            InsuranceDTO insuranceDTO = insuranceApi.queryById(insuranceRecordDTO.getInsuranceId());
            orderAndReturnDetailVO.setInsuranceName(insuranceDTO.getInsuranceName());
        }
        // 订单明细
        addOrderDetailList(orderAndReturnDetailVO, orderId);

        // 订单收货人信息
        addOrderRelateUser(orderAndReturnDetailVO, orderId);

        // 退货相关信息
        {
            OrderReturnDTO orderReturnDTO = orderReturnApi.queryByOrderId(orderId);
            OrderReturnVO orderReturnVO = PojoUtils.map(orderReturnDTO, OrderReturnVO.class);
            orderAndReturnDetailVO.setOrderReturn(orderReturnVO);

            if (null != orderReturnDTO) {
                // 退货明细信息
                addOrderReturnDetailList(orderAndReturnDetailVO, orderReturnDTO.getId());
            }
        }

        // 获取处方详情
        addOrderPrescriptionDetail(orderAndReturnDetailVO, orderId, insuranceRecordDTO);

        // 拿货核销码
        addOffCode(orderAndReturnDetailVO, insuranceRecordDTO.getIssueCredentialNo());

        // 订单详情增加操作人信息
        addOperateUser(orderAndReturnDetailVO, orderId);

        // 订单详情增加订单操作记录信息
        addOperateList(orderAndReturnDetailVO, orderId);

        // 患者信息(处方编辑里面需要)
        addPatientsInfo(orderAndReturnDetailVO, insuranceRecordDTO);

        return Result.success(orderAndReturnDetailVO);
    }

    /**
     * 患者信息(处方编辑里面需要)
     *
     * @param orderAndReturnDetailVO 返回给前端的订单实体
     * @param insuranceRecordDTO 参保记录
     */
    private void addPatientsInfo(OrderAndReturnDetailVO orderAndReturnDetailVO, InsuranceRecordDTO insuranceRecordDTO) {
        orderAndReturnDetailVO.setPatientsName(insuranceRecordDTO.getIssueName());
        orderAndReturnDetailVO.setPatientsPhone(insuranceRecordDTO.getIssuePhone());
        String issueCredentialNo = insuranceRecordDTO.getIssueCredentialNo();
        int gender = IdcardUtil.getGenderByIdCard(issueCredentialNo);
        orderAndReturnDetailVO.setPatientsGender(gender == 0 ? "女" : "男");
        int age = IdcardUtil.getAgeByIdCard(issueCredentialNo);
        orderAndReturnDetailVO.setPatientsAge(age);
    }

    /**
     * 票据图片处理展示
     *
     * @param orderAndReturnDetailVO 返回的数据
     */
    private void addOrderReceiptsList(OrderAndReturnDetailVO orderAndReturnDetailVO, String orderReceipts) {
        if (StringUtils.isBlank(orderReceipts)) {
            return;
        }
        String[] orderReceiptStr = orderReceipts.split(",");
        List<OrderReceiptsVO> orderReceiptsList = new ArrayList<>();
        for (String str : orderReceiptStr) {
            OrderReceiptsVO orderReceiptsVO = new OrderReceiptsVO();
            orderReceiptsVO.setOrderReceiptsKey(str);
            orderReceiptsVO.setOrderReceiptsUrl(fileService.getUrl(str, FileTypeEnum.ORDER_RECEIPTS));
            orderReceiptsList.add(orderReceiptsVO);
        }
        orderAndReturnDetailVO.setOrderReceiptsList(orderReceiptsList);
    }

    /**
     * 订单明细
     *
     * @param orderAndReturnDetailVO 订单详情返回
     * @param orderId 订单id
     */
    private void addOrderDetailList(OrderAndReturnDetailVO orderAndReturnDetailVO, Long orderId) {
        List<OrderDetailDTO> detailDTOList = orderDetailApi.listByOrderId(orderId);
        List<OrderDetailVO> orderDetailVOList = PojoUtils.map(detailDTOList, OrderDetailVO.class);

        // 管控信息
        List<OrderDetailControlBO> orderDetailControlBOS = orderDetailControlApi.listByOrderIdAndSellSpecificationsIdList(orderId, null);
        Map<Long, OrderDetailControlBO> detailControlBOMap = orderDetailControlBOS.stream().collect(Collectors.toMap(OrderDetailControlBO::getSellSpecificationsId, e -> e, (k1, k2) -> k1));

        orderDetailVOList.forEach(e -> {
            OrderDetailControlBO detailControlBO = detailControlBOMap.get(e.getSellSpecificationsId());
            e.setControlStatus(0);
            if (null != detailControlBO) {
                List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.listByIds(detailControlBO.getEidList());
                List<String> channelNameList = enterpriseDTOS.stream().map(EnterpriseDTO::getName).collect(Collectors.toList());
                e.setControlStatus(1);
                e.setChannelNameList(channelNameList);
            }
            e.setGoodsAmount(e.getTerminalSettlePrice().multiply(new BigDecimal(Long.toString(e.getGoodsQuantity()))));
        });
        orderAndReturnDetailVO.setOrderDetailList(orderDetailVOList);
    }

    /**
     * 退货单信息
     *
     * @param orderAndReturnDetailVO 订单详情返回
     * @param orderId 订单id
     */
    private void addOrderRelateUser(OrderAndReturnDetailVO orderAndReturnDetailVO, Long orderId) {
        OrderRelateUserDTO orderRelateUserDTO = orderRelateUserApi.queryByOrderIdAndRelateType(orderId, HmcOrderRelateUserTypeEnum.RECEIVER);
        OrderRelateUserVO orderRelateUserVO = PojoUtils.map(orderRelateUserDTO, OrderRelateUserVO.class);
        orderAndReturnDetailVO.setOrderRelateUser(orderRelateUserVO);
    }

    /**
     * 退货明细信息
     *
     * @param orderAndReturnDetailVO 订单详情返回
     * @param returnId 退货单明细
     */
    private void addOrderReturnDetailList(OrderAndReturnDetailVO orderAndReturnDetailVO, Long returnId) {
        List<OrderReturnDetailDTO> orderReturnDetailDTOList = orderReturnDetailApi.listByReturnId(returnId);
        List<OrderReturnDetailVO> orderReturnDetailVOList = PojoUtils.map(orderReturnDetailDTOList, OrderReturnDetailVO.class);
        orderAndReturnDetailVO.setOrderReturnDetailList(orderReturnDetailVOList);
    }

    /**
     * 获取处方详情
     *
     * @param orderAndReturnDetailVO 订单详情返回
     * @param orderId 订单id
     */
    private void addOrderPrescriptionDetail(OrderAndReturnDetailVO orderAndReturnDetailVO, Long orderId, InsuranceRecordDTO insuranceRecordDTO) {
        OrderPrescriptionDTO prescriptionDTO = orderPrescriptionApi.getByOrderId(orderId);
        OrderPrescriptionDetailVO detailVO = new OrderPrescriptionDetailVO();
        if (Objects.isNull(prescriptionDTO)) {
            detailVO.setPrescription(null);
            detailVO.setGoodsList(null);
            orderAndReturnDetailVO.setOrderPrescriptionDetail(detailVO);
            return;
        }
        // 获取商品信息
        List<OrderPrescriptionGoodsDTO> goodsDTOList = orderPrescriptionGoodsApi.getByOrderId(orderId);

        OrderPrescriptionVO prescriptionVO = PojoUtils.map(prescriptionDTO, OrderPrescriptionVO.class);
        if (StringUtils.isNotBlank(prescriptionVO.getPrescriptionSnapshotUrl())) {
            List<PrescriptionSnapshotUrlVO> prescriptionSnapshotUrlList = new ArrayList<>();
            String[] picStr = prescriptionVO.getPrescriptionSnapshotUrl().split(",");
            for (String key : picStr) {
                PrescriptionSnapshotUrlVO prescriptionSnapshotUrlVO = new PrescriptionSnapshotUrlVO();
                prescriptionSnapshotUrlVO.setPrescriptionSnapshotKey(key);
                prescriptionSnapshotUrlVO.setPrescriptionSnapshotUrl(fileService.getUrl(key, FileTypeEnum.PRESCRIPTION_PIC));
                prescriptionSnapshotUrlList.add(prescriptionSnapshotUrlVO);
            }
            prescriptionVO.setPrescriptionSnapshotUrlList(prescriptionSnapshotUrlList);
        }
        List<OrderPrescriptionGoodsVO> goodsVO = PojoUtils.map(goodsDTOList, OrderPrescriptionGoodsVO.class);
        detailVO.setPrescription(prescriptionVO);
        detailVO.setGoodsList(goodsVO);
        orderAndReturnDetailVO.setOrderPrescriptionDetail(detailVO);
    }

    /**
     * 拿货核销码
     *
     * @param orderAndReturnDetailVO 订单详情返回
     * @param issueCredentialNo 被保人证件号
     */
    private void addOffCode(OrderAndReturnDetailVO orderAndReturnDetailVO, String issueCredentialNo) {
        if (StringUtils.isNotBlank(issueCredentialNo)) {
            String last6 = issueCredentialNo.substring(issueCredentialNo.length() - 6);
            String startLast6 = last6.substring(0, 1);
            String endLast6 = last6.substring(4, 6);
            orderAndReturnDetailVO.setOffCode(startLast6 + "***" + endLast6);
        }
    }

    /**
     * 订单详情增加操作人信息
     *
     * @param orderAndReturnDetailVO 订单详情返回
     * @param orderId 订单id
     */
    private void addOperateUser(OrderAndReturnDetailVO orderAndReturnDetailVO, Long orderId) {
        OrderOperateDTO orderOperateDTO = orderOperateApi.getLastOperate(orderId);
        if (null != orderOperateDTO) {
            orderAndReturnDetailVO.setOperateUserId(orderOperateDTO.getOperateUserId());
            UserDTO operator = userApi.getById(orderOperateDTO.getOperateUserId());
            if (null != operator) {
                orderAndReturnDetailVO.setOperateUserName(operator.getName());
            }
        }
    }

    /**
     * 订单详情增加订单操作记录信息
     *
     * @param orderAndReturnDetailVO 订单详情返回
     * @param orderId 订单id
     */
    private void addOperateList(OrderAndReturnDetailVO orderAndReturnDetailVO, Long orderId) {
        List<Integer> operateTypeList = new ArrayList<>();
        operateTypeList.add(HmcOrderOperateTypeEnum.SELF_PICKUP.getCode());
        List<OrderOperateDTO> operateDTOList = orderOperateApi.listByOrderIdAndTypeList(orderId, operateTypeList);
        List<OrderOperateVO> operateVOList = PojoUtils.map(operateDTOList, OrderOperateVO.class);
        operateVOList.forEach(e -> {
            UserDTO operator = userApi.getById(e.getOperateUserId());
            e.setOperateUserName(operator.getName());
        });
        orderAndReturnDetailVO.setOperateList(operateVOList);
    }

    @ApiResponses({ @ApiResponse(code = 10010, message = "身份证信息待维护，请扫码上传") })
    @ApiOperation(value = "订单已提")
    @PostMapping("/received")
    public Result received(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid OrderReceivedForm form) {
        log.info("[received]订单设为已提，参数:{}", JSONUtil.toJsonStr(form));
        OrderDTO orderDTO = orderApi.queryById(form.getOrderId());
        if (Objects.isNull(orderDTO)) {
            return Result.failed("根据订单id未获取到订单信息");
        }

        // 判断是否维护过身份证、手写签名
        InsuranceRecordDTO insuranceRecordDTO = insuranceRecordApi.getById(orderDTO.getInsuranceRecordId());
        if (Objects.isNull(insuranceRecordDTO)) {
            return Result.failed("根据订单id未获取到保单信息");
        }

        if (StrUtil.isBlank(insuranceRecordDTO.getIdCardBack()) || StrUtil.isBlank(insuranceRecordDTO.getIdCardFront())) {
            return Result.failed(10010, "身份证信息待维护，请扫码上传");
        }


        // 这里判断，如果是小程序端发起的兑付->不校验身份证后六位
        if (HmcCreateSourceEnum.HMC_MA.getCode().equals(orderDTO.getCreateSource())) {
            CheckCardLastSixForm checkCardLastSixForm = new CheckCardLastSixForm();
            checkCardLastSixForm.setInsuranceRecordId(orderDTO.getInsuranceRecordId());
            checkCardLastSixForm.setCardLastSix(form.getCardLastSix());
            Result result = checkCardLastSix(checkCardLastSixForm);
            if (!result.isSuccessful()) {
                return result;
            }
        }

        OrderReceivedRequest request = PojoUtils.map(form, OrderReceivedRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = orderApi.received(request);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "订单发货")
    @PostMapping("/deliver")
    public Result deliver(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid OrderDeliverForm form) {
        OrderDeliverRequest request = PojoUtils.map(form, OrderDeliverRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = orderApi.deliver(request);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.failed("操作失败");
        }
    }

    /**
     * 通过订单信息扩展订单明细和订单派送信息
     *
     * @param orderPageVO 订单完整实体
     */
    private void addOrderDetail(OrderPageVO orderPageVO) {
        List<OrderDetailDTO> detailDTOList = orderDetailApi.listByOrderId(orderPageVO.getId());
        List<OrderDetailVO> orderDetailVOList = PojoUtils.map(detailDTOList, OrderDetailVO.class);
        orderPageVO.setOrderDetailList(orderDetailVOList);

        // 订单收货人信息
        OrderRelateUserDTO orderRelateUserDTO = orderRelateUserApi.queryByOrderIdAndRelateType(orderPageVO.getId(), HmcOrderRelateUserTypeEnum.RECEIVER);
        OrderRelateUserVO orderRelateUserVO = PojoUtils.map(orderRelateUserDTO, OrderRelateUserVO.class);
        orderPageVO.setOrderRelateUser(orderRelateUserVO);
    }

    /**
     * 校验身份证后6位
     *
     * @param form 请求参数
     * @return 成功/失败
     */
    @ApiOperation(value = "身份证后6位校验")
    @PostMapping("/checkCardLastSix")
    public Result checkCardLastSix(@RequestBody @Valid CheckCardLastSixForm form) {
        InsuranceRecordDTO insuranceRecordDTO = insuranceRecordApi.getById(form.getInsuranceRecordId());
        if (Objects.isNull(insuranceRecordDTO)) {
            log.info("根据保单id未查询到保单信息，保单id:{}", form.getInsuranceRecordId());
            return Result.failed("根据保单id未查询到保单信息");
        }

        // 被保人证件号
        String issueCredentialNo = insuranceRecordDTO.getIssueCredentialNo();

        String last6 = issueCredentialNo.substring(issueCredentialNo.length() - 6);

        if (!StrUtil.equals(last6, form.getCardLastSix())) {
            return Result.failed("保单人身份证后6位校验不通过,请确认后重新输入!");
        }
        return Result.success();
    }

    @ApiOperation(value = "保存订单票据")
    @PostMapping("/saveOrderReceipts")
    public Result saveOrderReceipts(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid OrderReceiptsSaveForm form) {
        OrderReceiptsSaveRequest request = PojoUtils.map(form, OrderReceiptsSaveRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOrderReceipts("");
        if (CollUtil.isNotEmpty(form.getOrderReceiptsList())) {
            String orderReceipts = String.join(",", form.getOrderReceiptsList());
            request.setOrderReceipts(orderReceipts);
        }
        boolean isSuccess = orderApi.saveOrderReceipts(request);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.failed("保存订单票据出现错误");
        }
    }

    @ApiOperation(value = "保存处方信息")
    @PostMapping("/saveOrderPrescription")
    public Result saveOrderPrescription(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid OrderPrescriptionSaveForm form) {
        OrderPrescriptionSaveRequest request = PojoUtils.map(form, OrderPrescriptionSaveRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setPrescriptionSnapshotUrl("");
        if (CollUtil.isNotEmpty(form.getPrescriptionSnapshotUrlList())) {
            String prescriptionSnapshotUrl = String.join(",", form.getPrescriptionSnapshotUrlList());
            request.setPrescriptionSnapshotUrl(prescriptionSnapshotUrl);
        }
        boolean isSuccess = orderApi.saveOrderPrescription(request);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.failed("保存订单票据出现错误");
        }
    }
}
