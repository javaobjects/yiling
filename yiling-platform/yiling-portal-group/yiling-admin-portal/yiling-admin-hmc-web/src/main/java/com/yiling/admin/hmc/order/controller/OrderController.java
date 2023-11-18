package com.yiling.admin.hmc.order.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.yiling.admin.hmc.order.form.OrderPageForm;
import com.yiling.admin.hmc.order.vo.OrderAndReturnDetailVO;
import com.yiling.admin.hmc.order.vo.OrderDetailVO;
import com.yiling.admin.hmc.order.vo.OrderPageVO;
import com.yiling.admin.hmc.order.vo.OrderPrescriptionDetailVO;
import com.yiling.admin.hmc.order.vo.OrderPrescriptionGoodsVO;
import com.yiling.admin.hmc.order.vo.OrderPrescriptionVO;
import com.yiling.admin.hmc.order.vo.OrderRelateUserVO;
import com.yiling.admin.hmc.order.vo.OrderReturnDetailVO;
import com.yiling.admin.hmc.order.vo.OrderReturnVO;
import com.yiling.admin.hmc.order.vo.OrderVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
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
import com.yiling.hmc.order.dto.request.OrderPageRequest;
import com.yiling.hmc.order.enums.HmcOrderRelateUserTypeEnum;
import com.yiling.hmc.wechat.api.InsuranceRecordApi;
import com.yiling.hmc.wechat.dto.InsuranceRecordDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Api(tags = "订单接口")
@RestController
@RequestMapping("/order")
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
    InsuranceCompanyApi insuranceCompanyApi;

    @DubboReference
    InsuranceRecordApi insuranceRecordApi;

    @DubboReference
    InsuranceApi insuranceApi;

    @DubboReference
    UserApi userApi;

    @DubboReference
    OrderPrescriptionApi orderPrescriptionApi;

    @DubboReference
    OrderPrescriptionGoodsApi orderPrescriptionGoodsApi;

    @DubboReference
    OrderRelateUserApi orderRelateUserApi;

    @DubboReference
    OrderOperateApi orderOperateApi;

    @DubboReference
    OrderDetailControlApi orderDetailControlApi;

    @DubboReference
    EnterpriseApi enterpriseApi;


    private final FileService fileService;

    @ApiOperation(value = "订单信息分页查询")
    @PostMapping("/pageList")
    public Result<Page<OrderPageVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid OrderPageForm form) {
        OrderPageRequest request = PojoUtils.map(form, OrderPageRequest.class);
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
    public Result<OrderAndReturnDetailVO> queryDetail(@CurrentUser CurrentAdminInfo staffInfo, @RequestParam("orderId") @ApiParam("订单id") Long orderId) {
        OrderAndReturnDetailVO orderAndReturnDetailVO = new OrderAndReturnDetailVO();

        OrderDTO orderDTO = orderApi.queryById(orderId);
        OrderVO orderVO = PojoUtils.map(orderDTO, OrderVO.class);

        orderAndReturnDetailVO.setOrder(orderVO);

        // 订单明细
        addOrderDetailList(orderAndReturnDetailVO, orderId);

        {
            InsuranceCompanyDTO insuranceCompanyDTO = insuranceCompanyApi.queryById(orderDTO.getInsuranceCompanyId());
            if (null != insuranceCompanyDTO) {
                orderAndReturnDetailVO.setInsuranceCompanyName(insuranceCompanyDTO.getCompanyName());
            }
        }

        {
            InsuranceRecordDTO insuranceRecordDTO = insuranceRecordApi.getById(orderDTO.getInsuranceRecordId());
            if (null != insuranceRecordDTO) {
                InsuranceDTO insuranceDTO = insuranceApi.queryById(insuranceRecordDTO.getInsuranceId());
                orderAndReturnDetailVO.setInsuranceName(insuranceDTO.getInsuranceName());
                orderAndReturnDetailVO.setHolderName(insuranceRecordDTO.getHolderName());
                orderAndReturnDetailVO.setHolderPhone(insuranceRecordDTO.getHolderPhone());
            }
        }

        {
            UserDTO userDTO = userApi.getById(orderDTO.getOrderUser());
            if (null != userDTO) {
                orderAndReturnDetailVO.setOrderUserName(userDTO.getName());
            }
        }

        // 订单收货人信息
        OrderRelateUserDTO orderRelateUserDTO = orderRelateUserApi.queryByOrderIdAndRelateType(orderId, HmcOrderRelateUserTypeEnum.RECEIVER);
        OrderRelateUserVO orderRelateUserVO = PojoUtils.map(orderRelateUserDTO, OrderRelateUserVO.class);
        orderAndReturnDetailVO.setOrderRelateUser(orderRelateUserVO);

        OrderReturnDTO orderReturnDTO = orderReturnApi.queryByOrderId(orderId);
        OrderReturnVO orderReturnVO = PojoUtils.map(orderReturnDTO, OrderReturnVO.class);
        orderAndReturnDetailVO.setOrderReturn(orderReturnVO);

        if (null != orderReturnDTO) {
            addOrderReturnDetailList(orderAndReturnDetailVO, orderReturnDTO.getId());
        }

        // 获取处方详情
        addOrderPrescriptionDetail(orderAndReturnDetailVO, orderId);

        // 订单处理者信息
        addOperateUser(orderAndReturnDetailVO, orderId);

        // 票据图片处理展示
        addOrderReceiptsList(orderAndReturnDetailVO, orderDTO.getOrderReceipts());

        return Result.success(orderAndReturnDetailVO);
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
        List<String> orderReceiptsList = new ArrayList<>();
        for (String str : orderReceiptStr) {
            orderReceiptsList.add(fileService.getUrl(str, FileTypeEnum.ORDER_RECEIPTS));
        }
        orderAndReturnDetailVO.setOrderReceiptsList(orderReceiptsList);
    }

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
        });

        orderAndReturnDetailVO.setOrderDetailList(orderDetailVOList);
    }

    private void addOrderReturnDetailList(OrderAndReturnDetailVO orderAndReturnDetailVO, Long returnId) {
        Map<Long, OrderDetailVO> detailMap = orderAndReturnDetailVO.getOrderDetailList().stream().collect(Collectors.toMap(OrderDetailVO::getId, e -> e, (k1, k2) -> k1));
        List<OrderReturnDetailDTO> orderReturnDetailDTOList = orderReturnDetailApi.listByReturnId(returnId);
        List<OrderReturnDetailVO> orderReturnDetailVOList = PojoUtils.map(orderReturnDetailDTOList, OrderReturnDetailVO.class);
        orderReturnDetailVOList.forEach(e -> {
            OrderDetailVO orderDetailVO = detailMap.get(e.getDetailId());
            e.setGoodsSpecifications(orderDetailVO.getGoodsSpecifications());
        });
        orderAndReturnDetailVO.setOrderReturnDetailList(orderReturnDetailVOList);
    }

    private void addOrderPrescriptionDetail(OrderAndReturnDetailVO orderAndReturnDetailVO, Long orderId) {
        OrderPrescriptionDTO prescriptionDTO = orderPrescriptionApi.getByOrderId(orderId);
        List<OrderPrescriptionGoodsDTO> goodsDTOList = orderPrescriptionGoodsApi.getByOrderId(orderId);
        OrderPrescriptionVO prescriptionVO = PojoUtils.map(prescriptionDTO, OrderPrescriptionVO.class);
        if (null != prescriptionVO && StringUtils.isNotBlank(prescriptionVO.getPrescriptionSnapshotUrl())) {
            List<String> prescriptionSnapshotUrlList = new ArrayList<>();
            String[] picStr = prescriptionVO.getPrescriptionSnapshotUrl().split(",");
            for (String str : picStr) {
                prescriptionSnapshotUrlList.add(fileService.getUrl(str, FileTypeEnum.PRESCRIPTION_PIC));
            }
            prescriptionVO.setPrescriptionSnapshotUrlList(prescriptionSnapshotUrlList);
        }
        List<OrderPrescriptionGoodsVO> goodsVO = PojoUtils.map(goodsDTOList, OrderPrescriptionGoodsVO.class);
        OrderPrescriptionDetailVO detailVO = new OrderPrescriptionDetailVO();
        detailVO.setPrescription(prescriptionVO);
        detailVO.setGoodsList(goodsVO);
        orderAndReturnDetailVO.setOrderPrescriptionDetail(detailVO);
    }

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
}
