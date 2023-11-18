package com.yiling.admin.b2b.member.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.member.form.CancelBuyRecordForm;
import com.yiling.admin.b2b.member.form.CreateMemberForm;
import com.yiling.admin.b2b.member.form.QueryBuyRecordForm;
import com.yiling.admin.b2b.member.form.QueryCancelRecordForm;
import com.yiling.admin.b2b.member.form.QueryMemberForm;
import com.yiling.admin.b2b.member.form.UpdateMemberForm;
import com.yiling.admin.b2b.member.form.UpdateMemberPromoterForm;
import com.yiling.admin.b2b.member.form.UpdateMemberReturnForm;
import com.yiling.admin.b2b.member.form.UpdateMemberSortForm;
import com.yiling.admin.b2b.member.vo.MemberBuyRecordVO;
import com.yiling.admin.b2b.member.vo.MemberDetailVO;
import com.yiling.admin.b2b.member.vo.MemberListItemVO;
import com.yiling.admin.b2b.member.vo.MemberSimpleVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.payment.enums.PaySourceEnum;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.api.MemberBuyRecordApi;
import com.yiling.user.member.api.MemberBuyStageApi;
import com.yiling.user.member.api.MemberReturnApi;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.member.dto.MemberDTO;
import com.yiling.user.member.dto.MemberSimpleDTO;
import com.yiling.user.member.dto.request.CancelBuyRecordRequest;
import com.yiling.user.member.dto.request.CreateMemberRequest;
import com.yiling.user.member.dto.request.QueryBuyRecordRequest;
import com.yiling.user.member.dto.request.QueryMemberRequest;
import com.yiling.user.member.dto.request.UpdateMemberPromoterRequest;
import com.yiling.user.member.dto.request.UpdateMemberRequest;
import com.yiling.user.member.dto.request.UpdateMemberReturnRequest;
import com.yiling.user.member.dto.request.UpdateMemberSortRequest;
import com.yiling.user.member.enums.MemberSourceEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.PaymentMethodDTO;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 会员管理 Controller
 *
 * @author: lun.yu
 * @date: 2021/10/25
 */
@Slf4j
@RestController
@RequestMapping("/member")
@Api(tags = "会员管理接口")
public class MemberController extends BaseController {

    @DubboReference
    MemberApi memberApi;
    @DubboReference
    MemberBuyStageApi memberBuyStageApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    PaymentMethodApi paymentMethodApi;
    @DubboReference(timeout = 5000)
    MemberBuyRecordApi memberBuyRecordApi;
    @DubboReference
    MemberReturnApi memberReturnApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "会员列表")
    @PostMapping("/queryMemberListPage")
    public Result<Page<MemberListItemVO>> queryMemberListPage(@CurrentUser CurrentAdminInfo adminInfo , @RequestBody @Valid QueryMemberForm form) {
        QueryMemberRequest request = PojoUtils.map(form,QueryMemberRequest.class);

        Page<MemberDTO> memberDtoPage = memberApi.queryListPage(request);
        if (CollUtil.isEmpty(memberDtoPage.getRecords())) {
            return Result.success(PojoUtils.map(memberDtoPage,MemberListItemVO.class));
        }

        List<Long> userIdList = memberDtoPage.getRecords().stream().map(MemberDTO::getUpdateUser).collect(Collectors.toList());
        Map<Long, String> userNameMap = userApi.listByIds(userIdList).stream().collect(Collectors.toMap(UserDTO::getId,UserDTO::getName));

        Page<MemberListItemVO> page = PojoUtils.map(memberDtoPage,MemberListItemVO.class);
        page.getRecords().forEach(memberVo -> {
            //拥有会员终端数=持有会员有效期内的终端总数
            int num = memberApi.getEnterpriseByMemberId(memberVo.getId()).size();
            memberVo.setOpenNum(num);
            //更新人
            memberVo.setUpdateName(userNameMap.get(memberVo.getUpdateUser()));
        });

        return Result.success(page);
    }

    @ApiOperation(value = "获取会员详情")
    @GetMapping("/getMember")
    public Result<MemberDetailVO> getMember(@CurrentUser CurrentAdminInfo adminInfo , @RequestParam("id") Long id) {
        MemberDetailVO memberDetailVO = PojoUtils.map(memberApi.getMember(id),MemberDetailVO.class);
        memberDetailVO.setBgPictureUrl(StrUtil.isNotEmpty(memberDetailVO.getBgPicture()) ? fileService.getUrl(memberDetailVO.getBgPicture(), FileTypeEnum.MEMBER_BACKGROUND_PICTURE) : null);
        memberDetailVO.setLightPictureUrl(StrUtil.isNotEmpty(memberDetailVO.getLightPicture()) ? fileService.getUrl(memberDetailVO.getLightPicture(), FileTypeEnum.MEMBER_LIGHT_PICTURE) : null);
        memberDetailVO.setExtinguishPictureUrl(StrUtil.isNotEmpty(memberDetailVO.getExtinguishPicture()) ? fileService.getUrl(memberDetailVO.getExtinguishPicture(), FileTypeEnum.MEMBER_LIGHT_PICTURE) : null);

        return Result.success(memberDetailVO);
    }

    @ApiOperation(value = "创建会员")
    @PostMapping("/createMember")
    @Log(title = "创建会员",businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> createMember(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid CreateMemberForm form) {
        CreateMemberRequest request = PojoUtils.map(form,CreateMemberRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());

        Boolean result = memberApi.createMember(request);

        return Result.success(result);
    }

    @ApiOperation(value = "更新会员")
    @PostMapping("/updateMember")
    @Log(title = "更新会员",businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> updateMember(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateMemberForm form) {
        UpdateMemberRequest request = PojoUtils.map(form, UpdateMemberRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());

        Boolean result = memberApi.updateMember(request);

        return Result.success(result);
    }

    @ApiOperation(value = "开始/停止获得")
    @GetMapping("/stopGetMember")
    @Log(title = "开始/停止获得",businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> stopGetMember(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("id") Long id) {
        boolean result = memberApi.stopGet(id, adminInfo.getCurrentUserId());
        return Result.success(result);
    }

    @ApiOperation(value = "更新排序")
    @PostMapping("/updateSort")
    @Log(title = "更新排序",businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> updateSort(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateMemberSortForm form) {
        UpdateMemberSortRequest request = PojoUtils.map(form, UpdateMemberSortRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        memberApi.updateSort(request);
        return Result.success();
    }

    @ApiOperation(value = "获取会员购买记录列表")
    @PostMapping("/queryBuyRecordListPage")
    public Result<Page<MemberBuyRecordVO>> queryBuyRecordListPage(@CurrentUser CurrentAdminInfo adminInfo , @RequestBody @Valid QueryBuyRecordForm form) {
        QueryBuyRecordRequest request = PojoUtils.map(form, QueryBuyRecordRequest.class);
        request.setCancelFlag(0);
        Page<MemberBuyRecordDTO> buyRecordDTOPage = memberBuyRecordApi.queryBuyRecordListPage(request);
        Page<MemberBuyRecordVO> page = this.getMemberBuyRecordVOPage(buyRecordDTOPage);

        return Result.success(page);
    }

    @ApiOperation(value = "退款时获取购买记录详情")
    @GetMapping("/getReturnDetail")
    public Result<MemberBuyRecordVO> getReturnDetail(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam(value = "购买记录ID", required = true) @RequestParam("id") Long id) {
        MemberBuyRecordDTO buyRecordDTO = memberBuyRecordApi.getBuyRecodeDetail(id);
        MemberBuyRecordVO buyRecordVO = PojoUtils.map(buyRecordDTO, MemberBuyRecordVO.class);
        List<PaymentMethodDTO> paymentMethodDtoList = paymentMethodApi.listByPlatform(PlatformEnum.B2B);
        Map<Long, String> nameMap = paymentMethodDtoList.stream().collect(Collectors.toMap(PaymentMethodDTO::getCode, PaymentMethodDTO::getName, (k1, k2) -> k2));
        buyRecordVO.setShowReturnFlag(BigDecimal.ZERO.compareTo(buyRecordDTO.getSubmitReturnAmount()) == 0);
        buyRecordVO.setPayMethodName(nameMap.get(buyRecordVO.getPayMethod().longValue()));

        return Result.success(buyRecordVO);
    }

    @ApiOperation(value = "提交退款")
    @PostMapping("/submitReturn")
    @Log(title = "提交退款",businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> submitReturn(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateMemberReturnForm form) {
        UpdateMemberReturnRequest request = PojoUtils.map(form, UpdateMemberReturnRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        memberReturnApi.submitReturn(request);
        return Result.success();
    }

    @ApiOperation(value = "取消导入购买记录")
    @PostMapping("/cancelBuyRecord")
    @Log(title = "取消导入购买记录",businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> cancelBuyRecord(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid CancelBuyRecordForm form) {
        CancelBuyRecordRequest request = PojoUtils.map(form, CancelBuyRecordRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(memberBuyRecordApi.cancelBuyRecord(request));
    }

    @ApiOperation(value = "更新推广人或推广方")
    @PostMapping("/updateBuyMemberPromoter")
    @Log(title = "更新推广人或推广方",businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> updateBuyMemberPromoter(@CurrentUser CurrentAdminInfo adminInfo , @RequestBody @Valid UpdateMemberPromoterForm form) {
        UpdateMemberPromoterRequest request = PojoUtils.map(form, UpdateMemberPromoterRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        memberApi.updateBuyMemberPromoter(request);
        return Result.success();
    }

    @ApiOperation(value = "获取所有会员")
    @GetMapping("/getMemberList")
    public Result<CollectionObject<MemberSimpleVO>> getMemberList(@CurrentUser CurrentAdminInfo adminInfo) {
        List<MemberSimpleDTO> simpleDTOList = memberApi.queryAllList();
        return Result.success(new CollectionObject<>(PojoUtils.map(simpleDTOList, MemberSimpleVO.class)));
    }

    @ApiOperation(value = "取消会员列表")
    @PostMapping("/queryCancelMemberPageList")
    public Result<Page<MemberBuyRecordVO>> queryCancelMemberPageList(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryCancelRecordForm form) {
        QueryBuyRecordRequest request = PojoUtils.map(form, QueryBuyRecordRequest.class);
        request.setCancelFlag(1);
        Page<MemberBuyRecordDTO> buyRecordDTOPage = memberBuyRecordApi.queryBuyRecordListPage(request);
        if (CollUtil.isEmpty(buyRecordDTOPage.getRecords())) {
            return Result.success(PojoUtils.map(buyRecordDTOPage, MemberBuyRecordVO.class));
        }

        List<Long> updateUserList = buyRecordDTOPage.getRecords().stream().map(MemberBuyRecordDTO::getUpdateUser).distinct().collect(Collectors.toList());
        Map<Long, String> userMap = userApi.listByIds(updateUserList).stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));

        Page<MemberBuyRecordVO> voPage = this.getMemberBuyRecordVOPage(buyRecordDTOPage);
        voPage.getRecords().forEach(memberBuyRecordVO -> memberBuyRecordVO.setUpdateUserName(userMap.get(memberBuyRecordVO.getUpdateUser())));

        return Result.success(voPage);
    }

    /**
     * 封装处理购买记录列表字段数据
     *
     * @param buyRecordDTOPage
     * @return
     */
    private Page<MemberBuyRecordVO> getMemberBuyRecordVOPage(Page<MemberBuyRecordDTO> buyRecordDTOPage) {
        Map<Long, MemberBuyRecordDTO> map = buyRecordDTOPage.getRecords().stream().collect(Collectors.toMap(BaseDTO::getId, Function.identity(), (k1, k2) -> k2));

        Page<MemberBuyRecordVO> page =  PojoUtils.map(buyRecordDTOPage, MemberBuyRecordVO.class);

        List<String> orderNoList = page.getRecords().stream().map(MemberBuyRecordVO::getOrderNo).collect(Collectors.toList());
        Map<String, String> stageNameMap = memberBuyStageApi.getStageNameByOrderNo(orderNoList);

        page.getRecords().forEach(memberBuyRecordVO -> {
            // 支付方式名称
            if (StrUtil.isNotEmpty(memberBuyRecordVO.getPayChannel()) && PaySourceEnum.getBySource(memberBuyRecordVO.getPayChannel()) != null) {
                memberBuyRecordVO.setPayMethodName(Objects.requireNonNull(PaySourceEnum.getBySource(memberBuyRecordVO.getPayChannel())).getName());
            }

            // 购买规则 = 购买规则 + 购买条件名称
            memberBuyRecordVO.setBuyRule(memberBuyRecordVO.getBuyRule() + stageNameMap.getOrDefault(memberBuyRecordVO.getOrderNo(), ""));

            MemberBuyRecordDTO memberBuyRecordDTO = map.get(memberBuyRecordVO.getId());
            // 是否展示退款按钮
            if (MemberSourceEnum.IMPORT_GIVE == MemberSourceEnum.getByCode(memberBuyRecordVO.getSource()) || MemberSourceEnum.IMPORT_P2P == MemberSourceEnum.getByCode(memberBuyRecordVO.getSource())) {
                memberBuyRecordVO.setShowReturnFlag(false);
            } else {
                memberBuyRecordVO.setShowReturnFlag(BigDecimal.ZERO.compareTo(memberBuyRecordDTO.getSubmitReturnAmount()) == 0);
            }

            // 是否退款、是否过期
            memberBuyRecordVO.setReturnFlag(memberBuyRecordVO.getReturnAmount().compareTo(BigDecimal.ZERO) == 0 ? 1 : 2);
            memberBuyRecordVO.setExpireFlag(memberBuyRecordVO.getEndTime().compareTo(new Date()) > 0 ? 1 : 2);
            memberBuyRecordVO.setAddress(memberBuyRecordDTO.getProvinceName() + memberBuyRecordDTO.getCityName() + memberBuyRecordDTO.getRegionName());
        });

        return page;
    }

}
