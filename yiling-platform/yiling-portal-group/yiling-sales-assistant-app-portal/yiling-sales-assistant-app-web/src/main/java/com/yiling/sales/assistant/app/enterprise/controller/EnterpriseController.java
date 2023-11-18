package com.yiling.sales.assistant.app.enterprise.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.base.BaseController;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * 销售助手-企业管理Controller
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/23
 */
@Slf4j
@RestController
@RequestMapping("/enterprise")
@Api(tags = "企业管理")
public class EnterpriseController extends BaseController {

    /*@DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    EmployeeApi employeeApi;
    @DubboReference
    PositionApi positionApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    OrderReportApi orderApi;

    @ApiOperation(value = "企业成员列表")
    @PostMapping("/getEnterpriseMemberList")
    public Result<CollectionObject<EnterpriseMemberListVO>> getEnterpriseMemberList(@CurrentUser CurrentStaffInfo staffInfo ,@RequestBody @Valid QueryEnterpriseMemberForm form) {
        EnterpriseEmployeeDTO employeeDto = Optional.ofNullable(employeeApi.getByEidUserId(staffInfo.getCurrentEid(), staffInfo.getCurrentUserId()))
                .orElseThrow(() -> new BusinessException(UserErrorCode.STAFF_NOT_EXISTS));

        QueryEnterpriseEmployeeListRequest request = PojoUtils.map(form,QueryEnterpriseEmployeeListRequest.class);
        request.setEid(ErpConstants.YILING_EID);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setEmployeeId(employeeDto.getId());
        List<EnterpriseEmployeeInfoDTO> employeeDtoList = employeeApi.getByDepartmentId(request);

        //上级id获取上级姓名
        List<Long> parentIdList = employeeDtoList.stream().map(EnterpriseEmployeeDTO::getParentId).collect(Collectors.toList());
        Map<Long, String> parentMap = userApi.listByIds(parentIdList).stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName, (k1, k2) -> k2));
        employeeDtoList.forEach(enterpriseEmployeeInfoDTO -> enterpriseEmployeeInfoDTO.setParentName(parentMap.get(enterpriseEmployeeInfoDTO.getParentId())));

        List<EnterpriseMemberListVO> list = PojoUtils.map(employeeDtoList,EnterpriseMemberListVO.class);
        list.forEach(enterpriseMemberListVO -> {
            ReceiveOrderSumQueryRequest sumQueryRequest = new ReceiveOrderSumQueryRequest();
            sumQueryRequest.setUserIdList(ListUtil.toList(enterpriseMemberListVO.getUserId()));
            OrderSumDTO orderSumDTO = orderApi.sumReceiveOrderReportByContacterId(sumQueryRequest);
            //设置订单总额和已售商品数量
            enterpriseMemberListVO.setProductNum(orderSumDTO.getOrderTotalNumer());
            enterpriseMemberListVO.setOrderAmount(orderSumDTO.getTotalOrderMoney());
        });

        return Result.success(new CollectionObject<>(list));

    }

    @ApiOperation(value = "根据用户获取客户订单信息")
    @PostMapping("/getUserOrderListPage")
    public Result<Page<UserOrderListItemVO>> getUserOrderListPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryUserOrderListForm form) {
        QueryBuyerReceiveOrderPageRequest orderPageRequest = PojoUtils.map(form,QueryBuyerReceiveOrderPageRequest.class);

        List<Long> contactUserList = ListUtil.toList(form.getUserId());
        Map<Long, List<EnterpriseDTO>> map = enterpriseApi.listByContactUserIds(ErpConstants.YILING_EID, contactUserList);
        List<EnterpriseDTO> enterpriseDTOList = map.get(form.getUserId());

        //客户名称检索
        if(StrUtil.isNotEmpty(form.getCustomerName())){
            EnterpriseDTO enterpriseDto = enterpriseApi.getByName(form.getCustomerName());
            enterpriseDTOList = map.get(form.getUserId()).stream().filter(enterpriseDTO ->
                    enterpriseDTO.getId().equals(enterpriseDto.getId())).collect(Collectors.toList());
        }

        List<Long> customerEidList = enterpriseDTOList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
        //根据批量企业ID获取对应的订单信息，需要@郭志刚 那边提供dubbo接口。接口参数：orderNo、createTime、sort
        orderPageRequest.setBuyerEidList(customerEidList);
        orderPageRequest.setIsAscByCreateTime(form.getSort() == 1);
        Page<OrderFullDTO> orderFullDtoPage = orderApi.selectBuyerReceiveOrdersByEids(orderPageRequest);

        Page<UserOrderListItemVO> page = PojoUtils.map(orderFullDtoPage,UserOrderListItemVO.class);
        List<UserOrderListItemVO> list = orderFullDtoPage.getRecords().stream().map(orderFullDTO ->
                UserOrderListItemVO.builder().customerName(orderFullDTO.getBuyerEname()).orderNo(orderFullDTO.getOrderNo())
                .orderAmount(orderFullDTO.getTotalAmount()).orderCreateTime(orderFullDTO.getCreateTime()).typeNum(orderFullDTO.getTypeNum())
                .goodsNum(orderFullDTO.getGoodsNum()).build()).collect(Collectors.toList());

        page.setRecords(list);
        return Result.success(page);
    }

    @ApiOperation(value = "根据用户获取客户商品总计")
    @GetMapping("/getUserProductCount")
    public Result<UserProductCountVO> getUserProductCount(@RequestParam("userId") Long userId) {
        //根据用户ID即商务联系人获取对应的企业
        List<EnterpriseDTO> enterpriseDTOList = getEnterpriseDtoList(userId);
        List<Long> customerEidList = enterpriseDTOList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());

        //根据企商务联系人ID和业ID批量获取对应的商品信息，需要@郭志刚 那边提供dubbo接口。
        QueryBuyerReceiveOrderDetailPageRequest request = new QueryBuyerReceiveOrderDetailPageRequest();
        request.setContacterId(userId);
        request.setBuyerEidList(customerEidList);
        BuyerOrderSumBO buyerOrderSumBO = orderApi.selectReceiveGoodsReportByBuyerListInfo(request);

        return Result.success(PojoUtils.map(buyerOrderSumBO, UserProductCountVO.class));

    }

    @ApiOperation(value = "根据用户获取客户商品分页信息")
    @PostMapping("/getUserProductListPage")
    public Result<Page<UserProductListItemVO>> getUserProductListPage(@CurrentUser CurrentStaffInfo staffInfo , @RequestBody @Valid QueryUserProductListForm form) {
        //根据用户ID即商务联系人获取对应的企业
        List<EnterpriseDTO> enterpriseDTOList = getEnterpriseDtoList(form.getUserId());
        List<Long> customerEidList = enterpriseDTOList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());

        //根据用户ID和企业ID分页获取对应的商品信息，需要@郭志刚 那边提供dubbo接口。
        QueryBuyerReceiveOrderDetailPageRequest request = PojoUtils.map(form,QueryBuyerReceiveOrderDetailPageRequest.class);
        request.setContacterId(form.getUserId());
        request.setBuyerEidList(customerEidList);
        BuyerOrderSumBO buyerOrderSumBO = orderApi.selectReceiveGoodsReportByBuyerListInfo(request);

        return Result.success(PojoUtils.map(buyerOrderSumBO.getOrderDetailSumBOPage(),UserProductListItemVO.class));

    }

    private List<EnterpriseDTO> getEnterpriseDtoList(Long userId) {
        List<Long> contactUserList = ListUtil.toList(userId);
        Map<Long, List<EnterpriseDTO>> map = enterpriseApi.listByContactUserIds(ErpConstants.YILING_EID, contactUserList);
        return map.get(userId);
    }

    @ApiOperation(value = "根据商品ID获取订单信息")
    @GetMapping("/getOrderByGoodsId")
    public Result<Page<UserOrderListItemVO>> getOrderByGoodsId(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("goodsId") Long goodsId) {
        //根据商品ID获取对应的订单信息，需要@郭志刚 那边提供dubbo接口。接口参数：goodsId
        GoodDetailSumQueryPageRequest request = new GoodDetailSumQueryPageRequest();
        request.setGoodId(goodsId);
        request.setType(1);
        Page<OrderDTO> orderDtoPage = orderApi.pageReceiveOrderFullInfoByGoodsId(request);
        Page<UserOrderListItemVO> page = PojoUtils.map(orderDtoPage,UserOrderListItemVO.class);

        List<UserOrderListItemVO> list = orderDtoPage.getRecords().stream().map(orderDTO -> {
            OrderFullDTO orderFullDTO = orderApi.selectOrderFullInfoByOrderId(orderDTO.getId());

            return UserOrderListItemVO.builder().orderNo(orderDTO.getOrderNo()).orderAmount(orderDTO.getTotalAmount()).orderCreateTime(orderDTO.getCreateTime())
                    .goodsNum(orderFullDTO.getGoodsNum()).typeNum(orderFullDTO.getTypeNum()).customerName(orderDTO.getBuyerEname()).build();
        }).collect(Collectors.toList());

        page.setRecords(list);
        return Result.success(page);
    }

    @ApiOperation(value = "根据订单ID获取商品信息")
    @GetMapping("/getGoodsByOrderId")
    public Result<List<UserProductListItemVO>> getGoodsByOrderId(@CurrentUser CurrentStaffInfo staffInfo ,@RequestParam("orderId") Long orderId) {
        //根据订单ID获取对应的商品信息，需要@郭志刚 那边提供dubbo接口。接口参数：orderId
        OrderFullDTO orderFullDTO = orderApi.selectOrderFullInfoByOrderId(orderId);
        List<UserProductListItemVO> list = PojoUtils.map(orderFullDTO.getOrderDetailDTOList(),UserProductListItemVO.class);

        return Result.success(list);

    }*/


}
