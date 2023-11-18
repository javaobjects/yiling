package com.yiling.admin.b2b.common.controller;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.admin.b2b.common.form.QueryPayRepeatOrderPageListForm;
import com.yiling.admin.b2b.common.form.RefundPageForm;
import com.yiling.admin.b2b.common.form.RefundReTryForm;
import com.yiling.admin.b2b.common.form.RepeatOrderProcessForm;
import com.yiling.admin.b2b.common.vo.PageOrderRefundVO;
import com.yiling.admin.b2b.common.vo.RepeatPayOrderVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.mall.order.dto.request.RefundReTryRequest;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderRefundApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.PageOrderRefundDTO;
import com.yiling.order.order.dto.request.RefundPageRequest;
import com.yiling.payment.enums.OrderPlatformEnum;
import com.yiling.payment.enums.RefundStateEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.api.RepeatOrderApi;
import com.yiling.payment.pay.dto.RepeatPayOrderDTO;
import com.yiling.payment.pay.dto.request.RepeatOrderPageRequest;
import com.yiling.payment.pay.dto.request.UpdateRepeatOrderRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2021/11/5
 */
@Slf4j
@Api(tags = "财务管理-退款单")
@RestController
@RequestMapping("/refund")
public class OrderRefundController extends BaseController {
    @DubboReference
    OrderRefundApi  orderRefundApi;
    @DubboReference
    OrderProcessApi orderProcessApi;
    @DubboReference
    RepeatOrderApi  repeatOrderApi;
    @DubboReference
    UserApi         userApi;
    @DubboReference
    EnterpriseApi   enterpriseApi;
    @DubboReference
    OrderApi        orderApi;

    @ApiOperation(value = "财务管理-退款单-列表查询")
    @PostMapping("/pageList")
    public Result<Page<PageOrderRefundVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody RefundPageForm form) {
        RefundPageRequest request = PojoUtils.map(form, RefundPageRequest.class);
        Page<PageOrderRefundDTO> pageDTO = orderRefundApi.pageList(request);
        pageDTO.getRecords().forEach(pageOrderRefundDTO -> {
            if (null == pageOrderRefundDTO.getOperateUser()) {
                pageOrderRefundDTO.setOperateUser(null);
                pageOrderRefundDTO.setOperateTime(null);
            } else {
                UserDTO operateUserDTO = userApi.getById(pageOrderRefundDTO.getOperateUser());
                if (null != operateUserDTO) {
                    pageOrderRefundDTO.setOperateUserName(operateUserDTO.getName());
                } else {
                    pageOrderRefundDTO.setOperateUser(null);
                    pageOrderRefundDTO.setOperateTime(null);
                }
            }
        });
        Page<PageOrderRefundVO> pageVO = PojoUtils.map(pageDTO, PageOrderRefundVO.class);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "财务管理-退款单-财务管理")
    @PostMapping("/retry")
    public Result<Object> saveVersion(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody RefundReTryForm form) {
        RefundReTryRequest request = PojoUtils.map(form, RefundReTryRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        boolean isSuccess = orderProcessApi.reTryRefund(request);
        if (isSuccess) {
            return Result.success("操作成功");
        } else {
            return Result.failed("操作失败");
        }
    }


    /**
     * 查询销售订单,销售金额
     * @param records 重复支付金额
     * @return 销售订单订单金额
     */
    private Map<String, BigDecimal> selectSaleOrderTotalAmount(List<RepeatPayOrderDTO> records) {

        // 查询销售订单的订单总金额
        List<String> payAppOrderNoList = records.stream().filter(t -> OrderPlatformEnum.B2B == OrderPlatformEnum.getByCode(t.getOrderPlatform()))
                .filter(e -> TradeTypeEnum.PAY == TradeTypeEnum.getByCode(e.getTradeType())
                || TradeTypeEnum.BACK == TradeTypeEnum.getByCode(e.getTradeType()) || TradeTypeEnum.DEPOSIT == TradeTypeEnum.getByCode(e.getTradeType())
                || TradeTypeEnum.BALANCE == TradeTypeEnum.getByCode(e.getTradeType())
        ).map(t -> t.getAppOrderNo()).collect(Collectors.toList());


        if (CollectionUtil.isEmpty(payAppOrderNoList)) {

            return MapUtil.empty();
        }

        return orderApi.listByOrderNos(payAppOrderNoList).stream().collect(Collectors.toMap(OrderDTO::getOrderNo, OrderDTO::getTotalAmount));
    }




    @ApiOperation("财务管理-重复退款单列表")
    @PostMapping("/repeat")
    public Result<Page<RepeatPayOrderVO>> repeatPageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryPayRepeatOrderPageListForm pageListForm) {
        RepeatOrderPageRequest request = PojoUtils.map(pageListForm, RepeatOrderPageRequest.class);
        Page<RepeatPayOrderDTO> page = repeatOrderApi.selectPageRepeatOrderList(request);
        if (page == null || CollUtil.isEmpty(page.getRecords())) {

            return Result.success(PojoUtils.map(page, RepeatPayOrderVO.class));
        }

        // 查询销售订单的订单总金额
        Map<String, BigDecimal> payAppOrderMap = this.selectSaleOrderTotalAmount(page.getRecords());

        List<Long> allBuyerEids = page.getRecords().stream().map(RepeatPayOrderDTO::getBuyerEid).distinct().collect(Collectors.toList());
        List<Long> allSellerEids = page.getRecords().stream().map(RepeatPayOrderDTO::getSellerEid).distinct().collect(Collectors.toList());
        List<Long> allEids = Stream.of(allBuyerEids, allSellerEids).flatMap(Collection::stream).distinct().collect(Collectors.toList());

        List<EnterpriseDTO> allEnterpriseDTOList = enterpriseApi.listByIds(allEids);
        Map<Long, EnterpriseDTO> allEnterpriseDTOMap = allEnterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));

        List<RepeatPayOrderVO> list = Lists.newArrayList();
        for (RepeatPayOrderDTO e : page.getRecords()) {
            RepeatPayOrderVO vo = PojoUtils.map(e, RepeatPayOrderVO.class);
            vo.setBuyerName(allEnterpriseDTOMap.get(e.getBuyerEid()).getName());
            vo.setSellerName(allEnterpriseDTOMap.get(e.getSellerEid()).getName());
            vo.setOrderAmount(payAppOrderMap.getOrDefault(e.getAppOrderNo(), e.getPayAmount()));
            vo.setRefundTime(e.getCreateTime());
            switch (RefundStateEnum.getByCode(e.getRefundState())) {
                case REFUND_ING:
                    vo.setDealState(2);
                    break;
                case WAIT_REFUND:
                    vo.setDealState(1);
                    break;
                case SUCCESS:
                    vo.setDealState(4);
                    break;
                default:
                    vo.setDealState(3);
                    break;
            }
            if (vo.getDealState() == 3 || vo.getDealState() == 2 ||  vo.getDealState() == 4) {
                UserDTO updateUserDto = userApi.getById(e.getOperateUser());
                if (0l == e.getOperateUser()) {
                    vo.setUpdateUserName("系统");
                } else {
                    Optional.ofNullable(updateUserDto).ifPresent(t -> vo.setUpdateUserName(t.getName()));
                }
                UserDTO createUser = userApi.getById(vo.getUserId());
                if (0l == vo.getUserId()) {
                    vo.setUserName("系统");
                } else {
                    Optional.ofNullable(createUser).ifPresent(t -> vo.setUserName(t.getName()));
                }
            } else  {
                vo.setUpdateUserName(null);
                vo.setUserName(null);
                vo.setUpdateTime(null);
            }
            list.add(vo);
        }
        Page<RepeatPayOrderVO> pageResult = new Page(page.getCurrent(), page.getSize(), page.getTotal());
        pageResult.setRecords(list);

        return Result.success(pageResult);
    }

    @ApiOperation("财务管理-重复退款-财务管理")
    @PostMapping("/repeat/process")
    @Log(title = "重复退款处理", businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> repeatProcess(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid RepeatOrderProcessForm repeatOrderProcessForm) {

        UpdateRepeatOrderRequest updateRepeatOrderRequest = PojoUtils.map(repeatOrderProcessForm, UpdateRepeatOrderRequest.class);
        updateRepeatOrderRequest.setOpUserId(staffInfo.getCurrentUserId());

        return repeatOrderApi.processRepeatOrder(updateRepeatOrderRequest);
    }

}
