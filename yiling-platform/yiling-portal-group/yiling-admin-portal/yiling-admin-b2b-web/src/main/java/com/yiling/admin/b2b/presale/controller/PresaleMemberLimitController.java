package com.yiling.admin.b2b.presale.controller;


import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.member.form.QueryMemberForm;
import com.yiling.admin.b2b.member.vo.MemberListItemVO;
import com.yiling.admin.b2b.presale.form.AddPresaleMemberLimitForm;
import com.yiling.admin.b2b.presale.form.DeletePresaleGoodsLimitForm;
import com.yiling.admin.b2b.presale.form.DeletePresaleMemberLimitForm;
import com.yiling.admin.b2b.presale.form.QueryPresaleMemberLimitPageForm;
import com.yiling.admin.b2b.presale.vo.PresaleMemberLimitVO;
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
import com.yiling.marketing.strategy.dto.request.AddPresaleMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.AddStrategyMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeletePresaleMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryPresaleMemberLimitPageRequest;
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
 * 预售活动会员方案 前端控制器
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Api(tags = "预售活动-会员方案(会员维度)")
@RestController
@RequestMapping("/presaleMember/limit/member")
public class PresaleMemberLimitController extends BaseController {

    @DubboReference
    StrategyMemberApi strategyMemberApi;

    @DubboReference
    MemberApi memberApi;


    @ApiOperation(value = "预售活动选择会员方案-已添加会员方案列表查询-运营后台")
    @PostMapping("/pageList")
    public Result<Page<PresaleMemberLimitVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryPresaleMemberLimitPageForm form) {
        QueryPresaleMemberLimitPageRequest request = PojoUtils.map(form, QueryPresaleMemberLimitPageRequest.class);
        Page<StrategyMemberLimitDTO> dtoPage = strategyMemberApi.pageListForPresale(request);
        Page<PresaleMemberLimitVO> voPage = PojoUtils.map(dtoPage, PresaleMemberLimitVO.class);
        for (PresaleMemberLimitVO memberLimitVO : voPage.getRecords()) {
            MemberDetailDTO memberDetailDTO = memberApi.getMember(memberLimitVO.getMemberId());
            memberLimitVO.setMemberName(memberDetailDTO.getName());
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "预售活动选择会员方案-添加会员方案-运营后台")
    @PostMapping("/add")
    public Result<Object> add(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddPresaleMemberLimitForm form) {
        AddPresaleMemberLimitRequest request = PojoUtils.map(form, AddPresaleMemberLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyMemberApi.addForPresale(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("添加失败");
    }

    @ApiOperation(value = "预售活动选择会员方案-删除会员方案-运营后台")
    @PostMapping("/delete")
    public Result<Object> delete(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid DeletePresaleGoodsLimitForm form) {
        DeletePresaleMemberLimitRequest request = PojoUtils.map(form, DeletePresaleMemberLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyMemberApi.deleteForPresale(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }
}
