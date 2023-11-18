package com.yiling.admin.b2b.strategy.controller;


import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.member.form.QueryMemberForm;
import com.yiling.admin.b2b.member.vo.MemberListItemVO;
import com.yiling.admin.b2b.strategy.form.AddStrategyMemberLimitForm;
import com.yiling.admin.b2b.strategy.form.DeleteStrategyMemberLimitForm;
import com.yiling.admin.b2b.strategy.form.QueryStrategyMemberLimitPageForm;
import com.yiling.admin.b2b.strategy.vo.StrategyMemberLimitVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.strategy.api.StrategyMemberApi;
import com.yiling.marketing.strategy.dto.StrategyMemberLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyMemberLimitPageRequest;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.MemberDTO;
import com.yiling.user.member.dto.MemberDetailDTO;
import com.yiling.user.member.dto.request.QueryMemberRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 策略满赠会员方案 前端控制器
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Api(tags = "策略满赠-会员方案(会员维度)")
@RestController
@RequestMapping("/strategy/limit/member")
public class StrategyMemberLimitController extends BaseController {

    @DubboReference
    StrategyMemberApi strategyMemberApi;

    @DubboReference
    MemberApi memberApi;

    @ApiOperation(value = "策略满赠选择会员方案-选择需添加的会员方案-运营后台")
    @PostMapping("/page")
    public Result<Page<MemberListItemVO>> page(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody QueryMemberForm form) {
        QueryMemberRequest request = PojoUtils.map(form, QueryMemberRequest.class);
        Page<MemberDTO> dtoPage = memberApi.queryListPage(request);
        Page<MemberListItemVO> voPage = PojoUtils.map(dtoPage, MemberListItemVO.class);
        return Result.success(voPage);
    }

    @ApiOperation(value = "策略满赠选择会员方案-已添加会员方案列表查询-运营后台")
    @PostMapping("/pageList")
    public Result<Page<StrategyMemberLimitVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryStrategyMemberLimitPageForm form) {
        QueryStrategyMemberLimitPageRequest request = PojoUtils.map(form, QueryStrategyMemberLimitPageRequest.class);
        Page<StrategyMemberLimitDTO> dtoPage = strategyMemberApi.pageList(request);
        Page<StrategyMemberLimitVO> voPage = PojoUtils.map(dtoPage, StrategyMemberLimitVO.class);
        for (StrategyMemberLimitVO memberLimitVO : voPage.getRecords()) {
            MemberDetailDTO memberDetailDTO = memberApi.getMember(memberLimitVO.getMemberId());
            memberLimitVO.setMemberName(memberDetailDTO.getName());
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "策略满赠选择会员方案-添加会员方案-运营后台")
    @PostMapping("/add")
    public Result<Object> add(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddStrategyMemberLimitForm form) {
        AddStrategyMemberLimitRequest request = PojoUtils.map(form, AddStrategyMemberLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyMemberApi.add(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("添加失败");
    }

    @ApiOperation(value = "策略满赠选择会员方案-删除会员方案-运营后台")
    @PostMapping("/delete")
    public Result<Object> delete(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid DeleteStrategyMemberLimitForm form) {
        DeleteStrategyMemberLimitRequest request = PojoUtils.map(form, DeleteStrategyMemberLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyMemberApi.delete(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }
}
