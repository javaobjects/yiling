package com.yiling.sales.assistant.app.userteam.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.shortlink.api.ShortLinkApi;
import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsTypeEnum;
import com.yiling.basic.sms.enums.SmsVerifyCodeTypeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderReportApi;
import com.yiling.order.order.dto.OrderAssistantConfirmQuantityAmountDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderFullDTO;
import com.yiling.order.order.dto.request.QueryAssistantConfirmOrderPageRequest;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.sales.assistant.app.enterprise.vo.ProductItemVO;
import com.yiling.sales.assistant.app.userteam.enums.SmsInviteMemberTypeEnum;
import com.yiling.sales.assistant.app.userteam.form.AcceptInviteForm;
import com.yiling.sales.assistant.app.userteam.form.InviteMemberForm;
import com.yiling.sales.assistant.app.userteam.form.QueryMemberOrderForm;
import com.yiling.sales.assistant.app.userteam.form.QueryUserTeamForm;
import com.yiling.sales.assistant.app.userteam.vo.MemberDetailVO;
import com.yiling.sales.assistant.app.userteam.vo.MyLeaderVO;
import com.yiling.sales.assistant.app.userteam.vo.MyTeamVO;
import com.yiling.sales.assistant.app.userteam.vo.OrderListItemVO;
import com.yiling.sales.assistant.app.userteam.vo.TeamVO;
import com.yiling.sales.assistant.app.userteam.vo.UserTeamListItemVO;
import com.yiling.sales.assistant.enums.AssistantErrorCode;
import com.yiling.sales.assistant.enums.UserTeamInviteTypeEnum;
import com.yiling.sales.assistant.enums.UserTeamStatusEnum;
import com.yiling.sales.assistant.userteam.api.UserTeamApi;
import com.yiling.sales.assistant.userteam.dto.TeamDetailDTO;
import com.yiling.sales.assistant.userteam.dto.UserTeamDTO;
import com.yiling.sales.assistant.userteam.dto.request.AcceptInviteRequest;
import com.yiling.sales.assistant.userteam.dto.request.QueryUserTeamRequest;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.UserSalesAreaDTO;
import com.yiling.user.system.dto.request.CreateStaffRequest;
import com.yiling.user.system.enums.UserStatusEnum;
import com.yiling.user.system.enums.UserTypeEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrFormatter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 销售助手-团队管理Controller
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/26
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/userTeam")
@Api(tags = "团队管理")
public class UserTeamController extends BaseController {

    @DubboReference
    UserTeamApi userTeamApi;
    @DubboReference
    SmsApi smsApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    StaffApi staffApi;
    @DubboReference
    OrderReportApi orderReportApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    ShortLinkApi shortLinkApi;

    @Value("${sales.invite.url:https://h.59yi.com/#/sale/invite/}")
    private String inviteUrl;

    @ApiOperation(value = "我的团队信息")
    @GetMapping("/getMyTeamInfo")
    public Result<TeamVO> getMyTeamInfo(@CurrentUser CurrentStaffInfo staffInfo) {

        TeamDetailDTO teamDetailDto = userTeamApi.getTeamInfo(staffInfo.getCurrentUserId());
        TeamVO teamVO = PojoUtils.map(teamDetailDto,TeamVO.class);
        teamVO.setMyTeamVO(PojoUtils.map(teamDetailDto.getMyTeamDTO(), MyTeamVO.class));
        teamVO.setMyLeaderVO(PojoUtils.map(teamDetailDto.getMyLeaderDTO(), MyLeaderVO.class));
        if(Objects.nonNull(teamVO.getMyLeaderVO())){
            List<UserTeamDTO> userTeamDTOList = userTeamApi.getMemberList(staffInfo.getCurrentUserId());
            if(CollUtil.isNotEmpty(userTeamDTOList)) {
                BigDecimal totalAmount = BigDecimal.ZERO;
                int totalQuantity = 0;
                // 获取总单量和订单总金额
                for (UserTeamDTO userTeamDTO : userTeamDTOList){
                    if (userTeamDTO.getUserId() == 0 || UserTeamStatusEnum.PASS != UserTeamStatusEnum.getByCode(userTeamDTO.getRegisterStatus())) {
                        continue;
                    }
                    QueryAssistantConfirmOrderPageRequest orderPageRequest = new QueryAssistantConfirmOrderPageRequest();

                    if (staffInfo.getUserType() == UserTypeEnum.YILING) {
                        orderPageRequest.setContacterIdList(ListUtil.toList(userTeamDTO.getUserId()));
                    } else {
                        orderPageRequest.setCreateUser(userTeamDTO.getUserId());
                    }
                    orderPageRequest.setStartCreateTime(userTeamDTO.getRegisterTime());
                    orderPageRequest.setOrderSource(OrderSourceEnum.SA.getCode());
                    OrderAssistantConfirmQuantityAmountDTO quantityAmountDto = orderApi.getAssistantConfirmQuantityAmount(orderPageRequest);

                    totalAmount = totalAmount.add(quantityAmountDto.getTotalAmount());
                    totalQuantity = totalQuantity + quantityAmountDto.getTotalQuantity();
                }

                teamVO.setMyLeaderVO(teamVO.getMyLeaderVO().setSumOrderAmount(totalAmount));
                teamVO.setMyLeaderVO(teamVO.getMyLeaderVO().setSumOrderNum(totalQuantity));
            }
        }

        return Result.success(teamVO);
    }

    @ApiOperation(value = "我的成员列表")
    @PostMapping("/getMyTeamListPage")
    public Result<Page<UserTeamListItemVO>> getMyTeamListPage(@CurrentUser CurrentStaffInfo staffInfo ,@RequestBody @Valid QueryUserTeamForm form) {
        QueryUserTeamRequest request = PojoUtils.map(form,QueryUserTeamRequest.class);
        request.setParentId(staffInfo.getCurrentUserId());

        Page<UserTeamDTO> myTeamListPage = userTeamApi.getMyTeamListPage(request);
        Page<UserTeamListItemVO> page = PojoUtils.map(myTeamListPage,UserTeamListItemVO.class);

        Map<Long, BigDecimal> orderMap = MapUtil.newHashMap();
        List<UserTeamListItemVO> pageRecords = page.getRecords();
        //获取用户信息
        Map<Long, UserDTO> userMap = MapUtil.newHashMap();
        List<Long> userIdList = pageRecords.stream().map(UserTeamListItemVO::getUserId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(userIdList)) {
            List<UserDTO> userDtoList = userApi.listByIds(userIdList);
            userMap = userDtoList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity(),(k1, k2)->k2));
        }

        if (CollUtil.isNotEmpty(pageRecords)) {

            pageRecords.forEach(userTeamListItemVO -> {
                if (userTeamListItemVO.getUserId() == 0 || UserTeamStatusEnum.PASS != UserTeamStatusEnum.getByCode(userTeamListItemVO.getRegisterStatus())) {
                    return;
                }
                QueryAssistantConfirmOrderPageRequest orderPageRequest = new QueryAssistantConfirmOrderPageRequest();
                orderPageRequest.setStartCreateTime(userTeamListItemVO.getRegisterTime());
                orderPageRequest.setOrderSource(OrderSourceEnum.SA.getCode());

                if (staffInfo.getUserType() == UserTypeEnum.YILING) {
                    orderPageRequest.setContacterIdList(ListUtil.toList(userTeamListItemVO.getUserId()));
                } else {
                    orderPageRequest.setCreateUser(userTeamListItemVO.getUserId());
                }
                OrderAssistantConfirmQuantityAmountDTO quantityAmountDTO = Optional.ofNullable(orderApi.getAssistantConfirmQuantityAmount(orderPageRequest)).orElse(new OrderAssistantConfirmQuantityAmountDTO());
                orderMap.put(userTeamListItemVO.getUserId(), quantityAmountDTO.getTotalAmount());

            });

        }

        Map<Long, UserDTO> finalUserMap = userMap;
        page.getRecords().forEach(userTeamListItemVO -> {
            userTeamListItemVO.setOrderAmount(orderMap.get(userTeamListItemVO.getUserId()));
            userTeamListItemVO.setName(finalUserMap.getOrDefault(userTeamListItemVO.getUserId(), new UserDTO()).getName());
            UserDTO userDTO = finalUserMap.get(userTeamListItemVO.getUserId());
            if (Objects.nonNull(userDTO) && userDTO.getStatus().equals(UserStatusEnum.DEREGISTER.getCode())) {
                userTeamListItemVO.setRegisterStatus(UserStatusEnum.DEREGISTER.getCode());
            }
        });

        return Result.success(page);
    }

    @ApiOperation(value = "成员详情-成员信息")
    @GetMapping("/getMemberDetail")
    public Result<MemberDetailVO> getMemberDetail(@CurrentUser CurrentStaffInfo staffInfo , @RequestParam("userId") Long userId) {
        UserTeamDTO userTeamDTO = Optional.ofNullable(userTeamApi.getUserTeamByUserId(userId)).orElseThrow(() -> new BusinessException(UserErrorCode.TEAM_USER_NO_EXIST));
        MemberDetailVO memberDetailVO = PojoUtils.map(userTeamDTO,MemberDetailVO.class);
        //获取用户销售区域
        UserSalesAreaDTO salesAreaDTO = Optional.ofNullable(userApi.getSaleAreaByUserId(userId)).orElse(new UserSalesAreaDTO());
        memberDetailVO.setSalesArea(salesAreaDTO.getDescription());

        return Result.success(memberDetailVO);
    }

    @ApiOperation(value = "成员详情-订单信息")
    @PostMapping("/getMemberDetailOrderPage")
    public Result<Page<OrderListItemVO>> getMemberDetailOrderPage(@CurrentUser CurrentStaffInfo staffInfo , @RequestBody @Valid QueryMemberOrderForm form) {
        UserTeamDTO userTeamDTO = Optional.ofNullable(userTeamApi.getUserTeamByUserId(form.getUserId())).orElseThrow(() -> new BusinessException(AssistantErrorCode.ANY_TEAM_MEMBER));
        // 获取用户对应的订单信息，订单状态：待付款、待发货、待收货、已完成
        QueryAssistantConfirmOrderPageRequest orderPageRequest = PojoUtils.map(form, QueryAssistantConfirmOrderPageRequest.class);
        orderPageRequest.setOrderSource(OrderSourceEnum.SA.getCode());
        if (staffInfo.getUserType() == UserTypeEnum.YILING) {
            orderPageRequest.setContacterIdList(ListUtil.toList(form.getUserId()));
        } else {
            orderPageRequest.setCreateUser(form.getUserId());
        }
        orderPageRequest.setStartCreateTime(userTeamDTO.getRegisterTime());
        Page<OrderDTO> orderDtoPage = orderApi.getAssistantConfirmOrderPage(orderPageRequest);

        List<OrderListItemVO> orderList = orderDtoPage.getRecords().stream().map(orderDTO -> {
            OrderListItemVO itemVO = new OrderListItemVO();
            itemVO.setOrderId(orderDTO.getId());
            itemVO.setOrderNo(orderDTO.getOrderNo());
            itemVO.setOrderAmount(orderDTO.getTotalAmount());
            itemVO.setCreateTime(orderDTO.getCreateTime());
            itemVO.setCustomerName(orderDTO.getBuyerEname());
            return itemVO;
        }).collect(Collectors.toList());

        Page<OrderListItemVO> page = PojoUtils.map(orderDtoPage,OrderListItemVO.class);
        page.setRecords(orderList);

        return Result.success(page);
    }


    @ApiOperation(value = "根据订单获取商品信息")
    @GetMapping("/getGoodsByOrderId")
    public Result<List<ProductItemVO>> getGoodsByOrderId(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("orderId") Long orderId) {

        //根据订单ID获取对应的商品信息，需要@郭志刚 那边提供dubbo接口。接口参数：orderId
        OrderFullDTO orderFullDTO = orderReportApi.selectOrderFullInfoByOrderId(orderId);
        if(Objects.isNull(orderFullDTO)){
            return Result.success(ListUtil.toList());
        }

        List<ProductItemVO> list = orderFullDTO.getOrderDetailDTOList().stream().map(orderDetailDTO -> {
            ProductItemVO itemVO = new ProductItemVO();
            itemVO.setGoodsName(orderDetailDTO.getGoodsName());
            itemVO.setGoodsPrice(orderDetailDTO.getGoodsPrice());
            itemVO.setGoodsQuantity(orderDetailDTO.getGoodsQuantity());
            itemVO.setSpecifications(orderDetailDTO.getGoodsSpecification());
            itemVO.setTotalPrice(orderDetailDTO.getGoodsAmount());
            return itemVO;
        }).collect(Collectors.toList());

        return Result.success(list);
    }

    @ApiOperation(value = "邀请成员-手机号邀请")
    @PostMapping("/inviteMember")
    @Log(title = "邀请成员-手机号邀请", businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> inviteMember(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid InviteMemberForm form) {

        //邀请用户
        boolean result = userTeamApi.inviteMember(staffInfo.getCurrentUserId(),form.getMobilePhone());
        if(result){
            UserDTO userDTO = Optional.ofNullable(userApi.getById(staffInfo.getCurrentUserId())).orElseThrow(() -> new BusinessException(UserErrorCode.EMPLOYEE_NOT_EXISTS));

            // 生成长链接: 生成规则：parentId_inviteType
            String longUrl = inviteUrl + staffInfo.getCurrentUserId() + "_" + UserTeamInviteTypeEnum.SMS.getCode();
            // 生成短链接
            String shortUrl = shortLinkApi.generatorShortLink(longUrl);

            //发送短信邀请
            String content = StrFormatter.format(SmsInviteMemberTypeEnum.INVITE_MEMBER.getTemplateContent(), userDTO.getName() , shortUrl);
            result = smsApi.send(form.getMobilePhone(), content, SmsTypeEnum.INVITE_MEMBER);
        }

        return Result.success(result);
    }

    @ApiOperation(value = "退出团队")
    @GetMapping("/exitTeam")
    @Log(title = "退出团队", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> exitTeam(@CurrentUser CurrentStaffInfo staffInfo) {
        boolean result = userTeamApi.exitTeam(staffInfo.getCurrentUserId());

        return Result.success(result);
    }

    @ApiOperation(value = "获取团队邀请短信验证码")
    @PostMapping("/getInviteVerifyCode")
    public Result<Void> getInviteVerifyCode(@RequestBody @Valid InviteMemberForm form) {
        // 主要为了校验微信邀请时，防止一个手机号被拉进团队两次
        boolean flag = userTeamApi.checkUserTeamStatus(form.getMobilePhone());
        if (flag) {
            throw new BusinessException(UserErrorCode.INVITE_MEMBER_HAD_EXIST_TEAM);
        }

        boolean result = smsApi.sendVerifyCode(form.getMobilePhone(), SmsVerifyCodeTypeEnum.SALES_ASSISTANT_ACCEPT_INVITE);
        if (!result) {
            return Result.failed("获取团队邀请短信验证码失败");
        }

        return Result.success();
    }

    @ApiOperation(value = "根据ID获取邀请人名称")
    @GetMapping("/getNameByParentId")
    public Result<String> getNameByParentId(@RequestParam("parentId") Long parentId) {
        UserDTO userDTO = Optional.ofNullable(userApi.getById(parentId)).orElseThrow(() -> new BusinessException(UserErrorCode.INVITE_MEMBER_NOT_EXIST));
        return Result.success(userDTO.getName());
    }

    @ApiOperation(value = "通过链接确认邀请")
    @PostMapping("/acceptInvite")
    @Log(title = "通过链接确认邀请", businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> acceptInvite(@RequestBody @Valid AcceptInviteForm form) {
        // 短信验证码校验
        boolean checkVerifyCodeResult = smsApi.checkVerifyCode(form.getMobilePhone(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.SALES_ASSISTANT_ACCEPT_INVITE);
        if (!checkVerifyCodeResult) {
            throw new BusinessException(UserErrorCode.VERIFY_CODE_ERROR);
        }

        Staff staff = staffApi.getByMobile(form.getMobilePhone());
        // 不存在此用户，则需要注册进来；存在则直接调用接受邀请接口
        if (Objects.isNull(staff)) {
            CreateStaffRequest request = new CreateStaffRequest();
            request.setMobile(form.getMobilePhone());
            request.setOpUserId(0L);
            staffApi.create(request);

            // 队长邀请的新人注册成功后发送MQ消息
            userTeamApi.inviteSendMq(form.getParentId(),form.getMobilePhone());
        }

        AcceptInviteRequest request = new AcceptInviteRequest();
        request.setInviteType(form.getInviteType());
        request.setParentId(form.getParentId());
        request.setMobilePhone(form.getMobilePhone());
        Boolean inviteStatus = userTeamApi.acceptInvite(request);
        if (!inviteStatus) {
            return Result.failed("接受邀请失败");
        }

        return Result.success();
    }

}
