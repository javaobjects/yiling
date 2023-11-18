package com.yiling.hmc.admin.order.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
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
import com.yiling.hmc.admin.order.form.OrderReturnApplyForm;
import com.yiling.hmc.admin.order.form.OrderReturnPageForm;
import com.yiling.hmc.admin.order.vo.OrderOperateVO;
import com.yiling.hmc.admin.order.vo.OrderReturnAndDetailVO;
import com.yiling.hmc.admin.order.vo.OrderReturnDetailVO;
import com.yiling.hmc.admin.order.vo.OrderReturnVO;
import com.yiling.hmc.order.api.OrderApi;
import com.yiling.hmc.order.api.OrderDetailApi;
import com.yiling.hmc.order.api.OrderOperateApi;
import com.yiling.hmc.order.api.OrderRelateUserApi;
import com.yiling.hmc.order.api.OrderReturnApi;
import com.yiling.hmc.order.api.OrderReturnDetailApi;
import com.yiling.hmc.order.dto.OrderDTO;
import com.yiling.hmc.order.dto.OrderDetailDTO;
import com.yiling.hmc.order.dto.OrderOperateDTO;
import com.yiling.hmc.order.dto.OrderRelateUserDTO;
import com.yiling.hmc.order.dto.OrderReturnDTO;
import com.yiling.hmc.order.dto.OrderReturnDetailDTO;
import com.yiling.hmc.order.dto.request.OrderReturnApplyRequest;
import com.yiling.hmc.order.dto.request.OrderReturnPageRequest;
import com.yiling.hmc.order.enums.HmcOrderOperateTypeEnum;
import com.yiling.hmc.order.enums.HmcOrderRelateUserTypeEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 退货表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Api(tags = "退货接口")
@RestController
@RequestMapping("/order/return")
public class OrderReturnController extends BaseController {

    @DubboReference
    OrderReturnApi orderReturnApi;

    @DubboReference
    OrderReturnDetailApi orderReturnDetailApi;

    @DubboReference
    OrderApi orderApi;

    @DubboReference
    OrderDetailApi orderDetailApi;

    @DubboReference
    OrderOperateApi orderOperateApi;

    @DubboReference
    UserApi userApi;

    @DubboReference
    OrderRelateUserApi orderRelateUserApi;

    @ApiOperation(value = "退单")
    @PostMapping("/apply")
    public Result applyOrderReturn(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid OrderReturnApplyForm form) {
        OrderReturnApplyRequest request = PojoUtils.map(form, OrderReturnApplyRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        return orderReturnApi.apply(request);
    }

    @ApiOperation(value = "退货单分页查询")
    @PostMapping("/pageList")
    public Result<Page<OrderReturnVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid OrderReturnPageForm form) {
        OrderReturnPageRequest request = PojoUtils.map(form, OrderReturnPageRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        Page<OrderReturnDTO> dtoPage = orderReturnApi.pageList(request);
        Page<OrderReturnVO> voPage = PojoUtils.map(dtoPage, OrderReturnVO.class);

        for (OrderReturnVO returnVO : voPage.getRecords()) {
            OrderDTO orderDTO = orderApi.queryById(returnVO.getOrderId());
            returnVO.setReturnAmount(orderDTO.getTerminalSettleAmount());
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "退货单详情查询")
    @GetMapping("/queryDetail")
    public Result<OrderReturnAndDetailVO> queryDetail(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("returnId") @ApiParam("退货单id") Long returnId) {
        OrderReturnDTO orderReturnDTO = orderReturnApi.queryById(returnId);
        OrderReturnAndDetailVO orderReturnAndDetailVO = PojoUtils.map(orderReturnDTO, OrderReturnAndDetailVO.class);

        if (null != orderReturnDTO) {
            OrderDTO orderDTO = orderApi.queryById(orderReturnDTO.getOrderId());
            orderReturnAndDetailVO.setReturnAmount(orderDTO.getTerminalSettleAmount());

            List<OrderDetailDTO> detailDTOList = orderDetailApi.listByOrderId(orderReturnDTO.getOrderId());
            Map<Long, OrderDetailDTO> detailDTOMap = detailDTOList.stream().collect(Collectors.toMap(OrderDetailDTO::getId, e -> e, (k1, k2) -> k1));

            List<OrderReturnDetailDTO> orderReturnDetailDTOList = orderReturnDetailApi.listByReturnId(orderReturnDTO.getId());
            List<OrderReturnDetailVO> orderReturnDetailVOList = PojoUtils.map(orderReturnDetailDTOList, OrderReturnDetailVO.class);
            orderReturnDetailVOList.forEach(e -> {
                OrderDetailDTO orderDetailDTO = detailDTOMap.get(e.getDetailId());
                e.setGoodsSpecifications(orderDetailDTO.getGoodsSpecifications());
                e.setReturnPrice(orderDetailDTO.getTerminalSettlePrice());
                e.setGoodsAmount(orderDetailDTO.getTerminalSettlePrice().multiply(new BigDecimal(Long.toString(orderDetailDTO.getGoodsQuantity()))));
            });
            orderReturnAndDetailVO.setOrderReturnDetailList(orderReturnDetailVOList);

            // 内容日志
            List<Integer> operateTypeList = new ArrayList<>();
            operateTypeList.add(HmcOrderOperateTypeEnum.RETURN.getCode());
            List<OrderOperateDTO> operateDTOList = orderOperateApi.listByOrderIdAndTypeList(orderReturnDTO.getOrderId(), operateTypeList);
            List<OrderOperateVO> operateVOList = PojoUtils.map(operateDTOList, OrderOperateVO.class);
            operateVOList.forEach(e -> {
                UserDTO userDTO = userApi.getById(e.getOperateUserId());
                e.setOperateUserName(userDTO.getName());
            });
            orderReturnAndDetailVO.setOperateList(operateVOList);

            addOrderRelateUser(orderReturnAndDetailVO, orderReturnDTO.getOrderId());
        }

        return Result.success(orderReturnAndDetailVO);
    }

    /**
     * 退货单信息
     *
     * @param orderReturnAndDetailVO 退货订单详情返回
     * @param orderId 订单id
     */
    private void addOrderRelateUser(OrderReturnAndDetailVO orderReturnAndDetailVO, Long orderId) {
        OrderRelateUserDTO orderRelateUserDTO = orderRelateUserApi.queryByOrderIdAndRelateType(orderId, HmcOrderRelateUserTypeEnum.RECEIVER);
        orderReturnAndDetailVO.setReceiveUserName(orderRelateUserDTO.getUserName());
        orderReturnAndDetailVO.setReceiveUserTel(orderRelateUserDTO.getUserTel());
    }
}
