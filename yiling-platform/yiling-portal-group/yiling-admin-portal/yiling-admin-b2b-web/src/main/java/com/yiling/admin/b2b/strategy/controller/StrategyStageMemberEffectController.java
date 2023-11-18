package com.yiling.admin.b2b.strategy.controller;


import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.member.vo.MemberBuyStageVO;
import com.yiling.admin.b2b.strategy.form.AddStrategyStageMemberEffectForm;
import com.yiling.admin.b2b.strategy.form.DeleteStrategyStageMemberEffectForm;
import com.yiling.admin.b2b.strategy.form.QueryStrategyStageMemberEffectPageForm;
import com.yiling.admin.b2b.strategy.vo.StrategyStageMemberEffectVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.strategy.api.StrategyStageMemberEffectApi;
import com.yiling.marketing.strategy.dto.StrategyStageMemberEffectDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyStageMemberEffectRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyStageMemberEffectRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyStageMemberEffectPageRequest;
import com.yiling.user.member.api.MemberBuyStageApi;
import com.yiling.user.member.dto.MemberBuyStageDTO;
import com.yiling.user.member.dto.request.QueryMemberBuyStagePageRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 策略满赠购买会员方案表 前端控制器
 * </p>
 *
 * @author zhangy
 * @date 2022-09-05
 */
@Api(tags = "策略满赠-购买会员方案")
@RestController
@RequestMapping("/strategy/stageMember")
public class StrategyStageMemberEffectController extends BaseController {

    @DubboReference
    StrategyStageMemberEffectApi strategyStageMemberEffectApi;

    @DubboReference
    MemberBuyStageApi memberBuyStageApi;

    @ApiOperation(value = "策略满赠会员方案-待添加会员方案-运营后台")
    @PostMapping("/list")
    public Result<Page<MemberBuyStageVO>> list(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody QueryStrategyStageMemberEffectPageForm form) {
        QueryMemberBuyStagePageRequest request = PojoUtils.map(form, QueryMemberBuyStagePageRequest.class);
        Page<MemberBuyStageDTO> dtoPage = memberBuyStageApi.queryMemberBuyStagePage(request);
        Page<MemberBuyStageVO> voPage = PojoUtils.map(dtoPage, MemberBuyStageVO.class);
        return Result.success(voPage);
    }

    @ApiOperation(value = "策略满赠会员方案-已添加会员方案-运营后台")
    @PostMapping("/pageList")
    public Result<Page<StrategyStageMemberEffectVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryStrategyStageMemberEffectPageForm form) {
        QueryStrategyStageMemberEffectPageRequest request = PojoUtils.map(form, QueryStrategyStageMemberEffectPageRequest.class);
        Page<StrategyStageMemberEffectDTO> dtoPage = strategyStageMemberEffectApi.pageList(request);
        Page<StrategyStageMemberEffectVO> voPage = PojoUtils.map(dtoPage, StrategyStageMemberEffectVO.class);
        for (StrategyStageMemberEffectVO stageMemberEffectVO : voPage.getRecords()) {
            MemberBuyStageDTO memberBuyStageDTO = memberBuyStageApi.getById(stageMemberEffectVO.getMemberId());
            stageMemberEffectVO.setMemberName(memberBuyStageDTO.getMemberName());
            stageMemberEffectVO.setPrice(memberBuyStageDTO.getPrice());
            stageMemberEffectVO.setValidTime(memberBuyStageDTO.getValidTime());
            stageMemberEffectVO.setName(memberBuyStageDTO.getName());
            stageMemberEffectVO.setSort(memberBuyStageDTO.getSort());
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "策略满赠会员方案-添加会员方案-运营后台")
    @PostMapping("/add")
    public Result<Object> add(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddStrategyStageMemberEffectForm form) {
        AddStrategyStageMemberEffectRequest request = PojoUtils.map(form, AddStrategyStageMemberEffectRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyStageMemberEffectApi.add(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("添加失败");
    }

    @ApiOperation(value = "策略满赠会员方案-删除会员方案-运营后台")
    @PostMapping("/delete")
    public Result<Object> delete(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid DeleteStrategyStageMemberEffectForm form) {
        DeleteStrategyStageMemberEffectRequest request = PojoUtils.map(form, DeleteStrategyStageMemberEffectRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyStageMemberEffectApi.delete(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }
}
