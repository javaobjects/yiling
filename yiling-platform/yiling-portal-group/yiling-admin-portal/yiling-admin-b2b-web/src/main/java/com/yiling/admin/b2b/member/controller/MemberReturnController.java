package com.yiling.admin.b2b.member.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.member.form.QueryMemberReturnPageForm;
import com.yiling.admin.b2b.member.form.UpdateMemberAuthReturnForm;
import com.yiling.admin.b2b.member.form.UpdateMemberReturnForm;
import com.yiling.admin.b2b.member.vo.MemberReturnVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.payment.enums.PayChannelEnum;
import com.yiling.payment.enums.PaySourceEnum;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.api.MemberReturnApi;
import com.yiling.user.member.dto.MemberReturnDTO;
import com.yiling.user.member.dto.request.QueryMemberReturnPageRequest;
import com.yiling.user.member.dto.request.UpdateMemberAuthReturnRequest;
import com.yiling.user.member.dto.request.UpdateMemberReturnRequest;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.PaymentMethodDTO;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 会员退款 Controller
 *
 * @author: lun.yu
 * @date: 2022-04-18
 */
@Slf4j
@RestController
@RequestMapping("/memberReturn")
@Api(tags = "会员退款接口")
public class MemberReturnController extends BaseController {

    @DubboReference
    MemberReturnApi memberReturnApi;
    @DubboReference
    UserApi userApi;

    @ApiOperation(value = "退款审核列表")
    @PostMapping("/queryMemberReturnPage")
    public Result<Page<MemberReturnVO>> queryMemberReturnPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryMemberReturnPageForm form) {
        QueryMemberReturnPageRequest request = PojoUtils.map(form, QueryMemberReturnPageRequest.class);
        Page<MemberReturnDTO> returnDTOPage = memberReturnApi.queryMemberReturnPage(request);

        Map<Long, MemberReturnDTO> dtoMap = returnDTOPage.getRecords().stream().collect(Collectors.toMap(BaseDTO::getId, Function.identity()));
        // 获取用户信息
        Set<Long> userSet = new HashSet<>();
        dtoMap.forEach((id, memberReturnDTO) -> {
            userSet.add(memberReturnDTO.getApplyUser());
            userSet.add(memberReturnDTO.getUpdateUser());
        });
        Map<Long, String> userMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(userSet)) {
            userMap = userApi.listByIds(ListUtil.toList(userSet)).stream().collect(Collectors.toMap(BaseDTO::getId, UserDTO::getName));
        }

        Page<MemberReturnVO> returnVOPage = PojoUtils.map(returnDTOPage, MemberReturnVO.class);
        Map<Long, String> finalUserMap = userMap;
        returnVOPage.getRecords().forEach(memberReturnVO -> {
            MemberReturnDTO memberReturnDTO = dtoMap.get(memberReturnVO.getId());
            if (StrUtil.isNotEmpty(memberReturnDTO.getPayChannel()) && PaySourceEnum.getBySource(memberReturnDTO.getPayChannel()) != null) {
                memberReturnVO.setPayMethodName(Objects.requireNonNull(PaySourceEnum.getBySource(memberReturnDTO.getPayChannel())).getName());
            }
            memberReturnVO.setApplyUserName(finalUserMap.get(memberReturnDTO.getApplyUser()));
            memberReturnVO.setUpdateUserName(finalUserMap.get(memberReturnDTO.getUpdateUser()));
        });

        return Result.success(returnVOPage);
    }

    @ApiOperation(value = "同意/驳回 退款")
    @PostMapping("/authReturn")
    public Result<Void> updateAuthStatus(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateMemberAuthReturnForm form) {
        UpdateMemberAuthReturnRequest request = PojoUtils.map(form, UpdateMemberAuthReturnRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        memberReturnApi.updateAuthStatus(request);
        return Result.success();
    }


}
