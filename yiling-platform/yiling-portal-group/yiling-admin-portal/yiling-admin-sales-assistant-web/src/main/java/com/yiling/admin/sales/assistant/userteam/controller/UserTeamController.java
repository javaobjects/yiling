package com.yiling.admin.sales.assistant.userteam.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
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
import com.yiling.admin.sales.assistant.userteam.form.QueryCustomerDetailForm;
import com.yiling.admin.sales.assistant.userteam.form.QueryCustomerOrderForm;
import com.yiling.admin.sales.assistant.userteam.form.QueryUserTeamForm;
import com.yiling.admin.sales.assistant.userteam.vo.OrderDetailVO;
import com.yiling.admin.sales.assistant.userteam.vo.OrderListItemVO;
import com.yiling.admin.sales.assistant.userteam.vo.OrderProductItemVO;
import com.yiling.admin.sales.assistant.userteam.vo.OrderReceiveInfoVO;
import com.yiling.admin.sales.assistant.userteam.vo.UserCustomerVO;
import com.yiling.admin.sales.assistant.userteam.vo.UserInTeamDetailVO;
import com.yiling.admin.sales.assistant.userteam.vo.UserTeamListItemVO;
import com.yiling.admin.sales.assistant.userteam.vo.UserTeamSimpleVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderReportApi;
import com.yiling.order.order.dto.OrderAssistantConfirmQuantityAmountDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderFullDTO;
import com.yiling.order.order.dto.OrderSumDTO;
import com.yiling.order.order.dto.request.OrderSumQueryPageRequest;
import com.yiling.order.order.dto.request.OrderSumQueryRequest;
import com.yiling.order.order.dto.request.QueryAssistantConfirmOrderPageRequest;
import com.yiling.order.order.dto.request.ReceiveOrderSumQueryRequest;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.sales.assistant.enums.UserTeamStatusEnum;
import com.yiling.sales.assistant.userteam.api.UserTeamApi;
import com.yiling.sales.assistant.userteam.dto.MyLeaderDTO;
import com.yiling.sales.assistant.userteam.dto.MyTeamDTO;
import com.yiling.sales.assistant.userteam.dto.TeamDTO;
import com.yiling.sales.assistant.userteam.dto.TeamDetailDTO;
import com.yiling.sales.assistant.userteam.dto.UserTeamDTO;
import com.yiling.sales.assistant.userteam.dto.request.QueryUserTeamRequest;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.enums.UserCustomerStatusEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.enums.UserStatusEnum;
import com.yiling.user.system.enums.UserTypeEnum;
import com.yiling.user.usercustomer.api.UserCustomerApi;
import com.yiling.user.usercustomer.dto.UserCustomerDTO;
import com.yiling.user.usercustomer.dto.request.QueryUserCustomerRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 销售助手后台-团队管理Controller
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/29
 */
@Slf4j
@RestController
@RequestMapping("/teamManager")
@Api(tags = "团队管理")
public class UserTeamController extends BaseController {

    @DubboReference
    UserTeamApi userTeamApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    OrderReportApi orderReportApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    UserCustomerApi userCustomerApi;
    @DubboReference
    OrderAddressApi orderAddressApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;


    @ApiOperation(value = "成员列表")
    @PostMapping("/queryTeamListPage")
    public Result<Page<UserTeamListItemVO>> queryTeamListPage(@CurrentUser CurrentStaffInfo staffInfo , @RequestBody @Valid QueryUserTeamForm form) {
        QueryUserTeamRequest request = PojoUtils.map(form,QueryUserTeamRequest.class);
        request.setRegisterStatus(1);
        request.setDateOrder(1);

        Page<UserTeamDTO> myTeamListPage = userTeamApi.getMyTeamListPage(request);
        Page<UserTeamListItemVO> page = PojoUtils.map(myTeamListPage,UserTeamListItemVO.class);

        //获取用户信息
        List<Long> userIdList = page.getRecords().stream().map(UserTeamListItemVO::getUserId).collect(Collectors.toList());
        Map<Long, UserDTO> userMap = MapUtil.newHashMap();
        Map<Long, Integer> teamMap = MapUtil.newHashMap();
        if(CollUtil.isNotEmpty(userIdList)){
            List<UserDTO> userDtoList = userApi.listByIds(userIdList);
            userMap = userDtoList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity(),(k1, k2)->k2));

            //查询团队，看是否是队长
            teamMap = userTeamApi.getTeamList(userIdList).stream().collect(Collectors.toMap(TeamDTO::getParentId, TeamDTO::getMemberNum, (k1, k2) -> k2));
        }

        //获取拉取人用户信息
        List<Long> parentIdList = page.getRecords().stream().map(UserTeamListItemVO::getParentId).collect(Collectors.toList());
        Map<Long, String> nameMap = MapUtil.newHashMap();
        if(CollUtil.isNotEmpty(parentIdList)){
            List<UserDTO> parentDtoList = userApi.listByIds(parentIdList);
            nameMap = parentDtoList.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName,(k1,k2)->k2));
        }

        //根据批量用户ID调用@郭志刚提供的Dubbo接口，获取订单金额；
        Map<Long, BigDecimal> orderMap = MapUtil.newHashMap();
        if(CollUtil.isNotEmpty(page.getRecords())){
            List<UserTeamListItemVO> teamListItemVOList = page.getRecords();
            for (UserTeamListItemVO userTeamListItemVO : teamListItemVOList) {
                if (userTeamListItemVO.getUserId() == 0 || UserTeamStatusEnum.PASS != UserTeamStatusEnum.getByCode(userTeamListItemVO.getRegisterStatus())) {
                    continue;
                }

                QueryAssistantConfirmOrderPageRequest orderPageRequest = new QueryAssistantConfirmOrderPageRequest();
                orderPageRequest.setStartCreateTime(userTeamListItemVO.getRegisterTime());
                orderPageRequest.setOrderSource(OrderSourceEnum.SA.getCode());

                if (staffInfo.getUserType() == UserTypeEnum.YILING || staffInfo.getUserType() == UserTypeEnum.XIAOSANYUAN) {
                    orderPageRequest.setContacterIdList(ListUtil.toList(userTeamListItemVO.getUserId()));
                } else {
                    orderPageRequest.setCreateUser(userTeamListItemVO.getUserId());
                }
                OrderAssistantConfirmQuantityAmountDTO quantityAmountDTO = Optional.ofNullable(orderApi.getAssistantConfirmQuantityAmount(orderPageRequest)).orElse(new OrderAssistantConfirmQuantityAmountDTO());
                orderMap.put(userTeamListItemVO.getUserId(), quantityAmountDTO.getTotalAmount());
            }

        }

        Map<Long, UserDTO> finalUserMap = userMap;
        Map<Long, String> finalNameMap = nameMap;
        Map<Long, Integer> finalTeamMap = teamMap;
        page.getRecords().forEach(userTeamListItemVO -> {
            userTeamListItemVO.setOrderAmount(orderMap.get(userTeamListItemVO.getUserId()));
            UserDTO userDTO = finalUserMap.getOrDefault(userTeamListItemVO.getUserId(), new UserDTO());
            userTeamListItemVO.setIdNumber(userDTO.getIdNumber());
            userTeamListItemVO.setInviteName(finalNameMap.get(userTeamListItemVO.getParentId()));
            userTeamListItemVO.setName(userDTO.getName());
            if (Objects.nonNull(userDTO.getStatus()) && userDTO.getStatus().equals(UserStatusEnum.DEREGISTER.getCode())) {
                userTeamListItemVO.setRegisterStatus(UserStatusEnum.DEREGISTER.getCode());
                userTeamListItemVO.setDeregisterTime(userDTO.getUpdateTime());
            }

            if(Objects.nonNull(finalTeamMap.get(userTeamListItemVO.getUserId()))){
                userTeamListItemVO.setPosition("队长/队员");
                userTeamListItemVO.setTeamNum(2);
            }else{
                userTeamListItemVO.setPosition("队员");
                userTeamListItemVO.setTeamNum(1);
            }

        });

        return Result.success(page);
    }

    @ApiOperation(value = "查询成员详情")
    @GetMapping("/getMember")
    public Result<UserTeamListItemVO> getMember(@CurrentUser CurrentStaffInfo staffInfo ,@RequestParam("userId") Long userId) {
        UserTeamDTO userTeamDTO = Optional.ofNullable(userTeamApi.getUserTeamByUserId(userId)).orElseThrow(() -> new BusinessException(UserErrorCode.TEAM_USER_NO_EXIST));
        UserTeamListItemVO userTeamListItemVO = PojoUtils.map(userTeamDTO,UserTeamListItemVO.class);

        UserDTO userDto = Optional.ofNullable(userApi.getById(userTeamListItemVO.getUserId())).orElse(new UserDTO());
        UserDTO patentDto = Optional.ofNullable(userApi.getById(userTeamListItemVO.getParentId())).orElse(new UserDTO());

        ReceiveOrderSumQueryRequest request = new ReceiveOrderSumQueryRequest();
        request.setUserIdList(ListUtil.toList(userId));
        OrderSumDTO orderSumDTO = Optional.ofNullable(orderReportApi.sumReceiveOrderReportByCreateUserId(request)).orElse(OrderSumDTO.builder().build());

        userTeamListItemVO.setIdNumber(userDto.getIdNumber());
        userTeamListItemVO.setInviteName(patentDto.getName());
        userTeamListItemVO.setOrderAmount(orderSumDTO.getTotalOrderMoney());

        return Result.success(userTeamListItemVO);
    }

    @ApiOperation(value = "查询所属团队")
    @GetMapping("/getTeam")
    public Result<List<UserInTeamDetailVO>> getTeam(@CurrentUser CurrentStaffInfo staffInfo , @RequestParam("userId") Long userId) {
        List<UserInTeamDetailVO> list = ListUtil.toList();
        //根据用户ID调用@郭志刚提供的Dubbo接口，获取用户对应的订单信息
        TeamDetailDTO teamInfo = userTeamApi.getTeamInfo(userId);
        if(Objects.nonNull(teamInfo) && Objects.nonNull(teamInfo.getMyLeaderDTO())){
            MyLeaderDTO myLeaderDTO = teamInfo.getMyLeaderDTO();
            list.add(UserInTeamDetailVO.builder().teamName(myLeaderDTO.getTeamName()).position("队长").memberNum(myLeaderDTO.getMemberNum()).build());
        }
        if(Objects.nonNull(teamInfo) && Objects.nonNull(teamInfo.getMyTeamDTO())){
            MyTeamDTO myTeamDTO = teamInfo.getMyTeamDTO();
            list.add(UserInTeamDetailVO.builder().teamName(myTeamDTO.getTeamName()).position("队员").memberNum(myTeamDTO.getMemberNum()).build());
        }

        return Result.success(list);
    }

    @ApiOperation(value = "订单信息分页列表")
    @PostMapping("/queryMemberOrderPage")
    public Result<Page<OrderListItemVO>> queryMemberOrderPage(@CurrentUser CurrentStaffInfo staffInfo , @RequestBody @Valid QueryCustomerDetailForm form) {
        //根据用户ID调用@郭志刚提供的Dubbo接口，获取用户对应的订单信息
        OrderSumQueryPageRequest request = new OrderSumQueryPageRequest();
        request.setUserIdList(ListUtil.toList(form.getUserId()));
        Page<OrderDTO> orderDtoPage = orderReportApi.selectReceiveOrderPageByCreateUserId(request);

        List<OrderListItemVO> orderList = orderDtoPage.getRecords().stream().map(orderDTO -> {
            OrderListItemVO orderListItemVO = new OrderListItemVO();
            orderListItemVO.setOrderId(orderDTO.getId());
            orderListItemVO.setOrderNo(orderDTO.getOrderNo());
            orderListItemVO.setOrderAmount(orderDTO.getTotalAmount());
            orderListItemVO.setCreateTime(orderDTO.getCreateTime());
            orderListItemVO.setOrderStatus(orderDTO.getOrderStatus());
            orderListItemVO.setCustomerName(orderDTO.getBuyerEname());
            return orderListItemVO;
        }).collect(Collectors.toList());

        Page<OrderListItemVO> page = PojoUtils.map(orderDtoPage,OrderListItemVO.class);
        page.setRecords(orderList);

        return Result.success(page);
    }

    @ApiOperation(value = "客户信息分页列表")
    @PostMapping("/queryMemberCustomerPage")
    public Result<Page<UserCustomerVO>> queryMemberCustomerPage(@CurrentUser CurrentStaffInfo staffInfo , @RequestBody @Valid QueryCustomerDetailForm form) {

        QueryUserCustomerRequest request = new QueryUserCustomerRequest().setUserId(form.getUserId());
        Page<UserCustomerDTO> customerDtoPage = userCustomerApi.pageList(request);

        Page<UserCustomerVO> page = PojoUtils.map(customerDtoPage,UserCustomerVO.class);

        //根据企业ID和用户ID，调用@郭志刚订单接口，查询该企业的订单总金额
        page.getRecords().forEach(userCustomerVO -> {
            ReceiveOrderSumQueryRequest sumQueryRequest= new ReceiveOrderSumQueryRequest();
            sumQueryRequest.setEid(userCustomerVO.getCustomerEid());
            sumQueryRequest.setUserIdList(ListUtil.toList(form.getUserId()));
            OrderSumDTO orderSumDTO = Optional.ofNullable(orderReportApi.sumReceiveOrderReportByContacterId(sumQueryRequest)).orElse(OrderSumDTO.builder().build());
            userCustomerVO.setOrderAmount(orderSumDTO.getTotalOrderMoney());
            //审核通过才设置认证时间
            if(UserCustomerStatusEnum.getByCode(userCustomerVO.getStatus()) != UserCustomerStatusEnum.PASS){
                userCustomerVO.setAuditTime(null);
            }
        });

        return Result.success(page);
    }

    @ApiOperation(value = "拉人信息分页列表")
    @PostMapping("/queryUserTeamPage")
    public Result<Page<UserTeamSimpleVO>> queryUserTeamPage(@CurrentUser CurrentStaffInfo staffInfo , @RequestBody @Valid QueryCustomerDetailForm form) {
        QueryUserTeamRequest request = new QueryUserTeamRequest();
        request.setParentId(form.getUserId());
        Page<UserTeamDTO> teamListPage = userTeamApi.getMyTeamListPage(request);

        return Result.success(PojoUtils.map(teamListPage,UserTeamSimpleVO.class));
    }

    @ApiOperation(value = "订单详情")
    @GetMapping("/getOrderDetail")
    public Result<OrderDetailVO> getOrderDetail(@CurrentUser CurrentStaffInfo staffInfo , @RequestParam("orderId") Long orderId) {

        //订单对应的商品信息
        OrderFullDTO orderFullDTO = orderReportApi.selectOrderFullInfoByOrderId(orderId);
        List<OrderProductItemVO> list = PojoUtils.map(orderFullDTO.getOrderDetailDTOList(),OrderProductItemVO.class);

        //订单收货信息
        OrderReceiveInfoVO orderReceiveInfoVO = PojoUtils.map(orderAddressApi.getOrderAddressInfo(orderId), OrderReceiveInfoVO.class);
        List<OrderDetailChangeDTO> orderDetailChangeDtoList = orderDetailChangeApi.listByOrderId(orderId);
        Integer deliveryQuantity = orderDetailChangeDtoList.stream().map(OrderDetailChangeDTO::getDeliveryQuantity).reduce(0,Integer::sum);
        orderReceiveInfoVO.setDeliveryQuantity(deliveryQuantity);
        Integer receiveQuantity = orderDetailChangeDtoList.stream().map(OrderDetailChangeDTO::getReceiveQuantity).reduce(0,Integer::sum);
        orderReceiveInfoVO.setReceiveQuantity(receiveQuantity);

        OrderDetailVO orderDetailVO = new OrderDetailVO();
        orderDetailVO.setProductItemVoList(list);
        orderDetailVO.setOrderReceiveInfoVO(orderReceiveInfoVO);

        return Result.success(orderDetailVO);
    }

    @ApiOperation(value = "客户详情")
    @PostMapping("/getCustomerDetail")
    public Result<Page<OrderListItemVO>> getCustomerDetail(@CurrentUser CurrentStaffInfo staffInfo , @RequestBody @Valid QueryCustomerOrderForm form) {

        //根据企业客户ID和用户ID查询订单列表
        OrderSumQueryPageRequest request = new OrderSumQueryPageRequest();
        request.setEid(form.getCustomerEid());
        request.setUserIdList(ListUtil.toList(form.getUserId()));
        Page<OrderDTO> orderDtoPage = orderReportApi.selectReceiveOrderPageByCreateUserId(request);

        Page<OrderListItemVO> page = PojoUtils.map(orderDtoPage,OrderListItemVO.class);

        List<OrderListItemVO> orderListItemList = orderDtoPage.getRecords().stream().map(orderDTO -> {
            OrderListItemVO orderListItemVO = PojoUtils.map(orderDTO, OrderListItemVO.class);
            orderListItemVO.setCustomerName(orderDTO.getBuyerEname());
            orderListItemVO.setOrderAmount(orderDTO.getTotalAmount());
            orderListItemVO.setOrderId(orderDTO.getId());
            return orderListItemVO;
        }).collect(Collectors.toList());

        page.setRecords(orderListItemList);

        return Result.success(page);
    }


}
