package com.yiling.admin.b2b.integral.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.integral.form.AddIntegralGiveMemberForm;
import com.yiling.admin.b2b.integral.form.QueryIntegralGiveMemberPageForm;
import com.yiling.admin.b2b.integral.vo.IntegralGiveMemberVO;
import com.yiling.admin.b2b.member.form.QueryMemberForm;
import com.yiling.admin.b2b.member.vo.MemberListItemVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.api.IntegralGiveMemberApi;
import com.yiling.user.integral.dto.IntegralOrderGiveMemberDTO;
import com.yiling.user.integral.dto.request.AddIntegralGiveMemberRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGiveMemberRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGiveMemberPageRequest;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.MemberDTO;
import com.yiling.user.member.dto.request.QueryMemberRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 订单送积分-指定会员方案 前端控制器
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-05
 */
@Api(tags = "订单送积分-指定会员方案接口")
@RestController
@RequestMapping("integralGiveMember")
public class IntegralGiveMemberController extends BaseController {

    @DubboReference
    IntegralGiveMemberApi integralGiveMemberApi;
    @DubboReference
    MemberApi memberApi;

    @ApiOperation(value = "订单送积分-选择需添加的会员方案")
    @PostMapping("/page")
    public Result<Page<MemberListItemVO>> page(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryMemberForm form) {
        QueryMemberRequest request = PojoUtils.map(form, QueryMemberRequest.class);
        Page<MemberDTO> dtoPage = memberApi.queryListPage(request);
        Page<MemberListItemVO> voPage = PojoUtils.map(dtoPage, MemberListItemVO.class);
        return Result.success(voPage);
    }

    @ApiOperation(value = "订单送积分-指定会员方案分页列表查询")
    @PostMapping("/pageList")
    public Result<Page<IntegralGiveMemberVO>> pageList(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryIntegralGiveMemberPageForm form) {
        QueryIntegralGiveMemberPageRequest request = PojoUtils.map(form, QueryIntegralGiveMemberPageRequest.class);
        Page<IntegralOrderGiveMemberDTO> dtoPage = integralGiveMemberApi.pageList(request);
        Page<IntegralGiveMemberVO> voPage = PojoUtils.map(dtoPage, IntegralGiveMemberVO.class);

        List<Long> memberIdList = voPage.getRecords().stream().map(IntegralGiveMemberVO::getMemberId).distinct().collect(Collectors.toList());
        Map<Long, String> memberMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(memberIdList)) {
            memberMap = memberApi.listByIds(memberIdList).stream().collect(Collectors.toMap(BaseDTO::getId, MemberDTO::getName));
        }

        Map<Long, String> finalMemberMap = memberMap;
        voPage.getRecords().forEach(integralGiveMemberVO -> integralGiveMemberVO.setMemberName(finalMemberMap.get(integralGiveMemberVO.getMemberId())));
        return Result.success(voPage);
    }

    @ApiOperation(value = "订单送积分-添加会员方案")
    @PostMapping("/add")
    @Log(title = "订单送积分-添加会员方案", businessType = BusinessTypeEnum.INSERT)
    public Result<Object> add(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid AddIntegralGiveMemberForm form) {
        AddIntegralGiveMemberRequest request = PojoUtils.map(form, AddIntegralGiveMemberRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(integralGiveMemberApi.add(request));
    }

    @ApiOperation(value = "订单送积分-删除会员方案")
    @PostMapping("/delete")
    @Log(title = "订单送积分-删除会员方案", businessType = BusinessTypeEnum.DELETE)
    public Result<Object> delete(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid AddIntegralGiveMemberForm form) {
        DeleteIntegralGiveMemberRequest request = PojoUtils.map(form, DeleteIntegralGiveMemberRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(integralGiveMemberApi.delete(request));
    }
}
