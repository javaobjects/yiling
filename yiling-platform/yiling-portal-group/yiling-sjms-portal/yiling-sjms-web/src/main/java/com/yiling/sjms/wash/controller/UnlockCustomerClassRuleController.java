package com.yiling.sjms.wash.controller;

import java.util.List;
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
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.api.UnlockCustomerClassRuleApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerClassRulePageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockCustomerClassRuleRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.wash.form.QueryUnlockCustomerClassRulePageForm;
import com.yiling.sjms.wash.form.SaveOrUpdateUnlockCustomerClassRuleForm;
import com.yiling.sjms.wash.vo.UnlockCustomerClassRulePageVO;
import com.yiling.sjms.wash.vo.UnlockCustomerClassRuleVO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author fucheng.bai
 * @date 2023/5/5
 */
@RestController
@Api(tags = "非锁客户分类规则")
@RequestMapping("/unlockCustomerClassRule")
public class UnlockCustomerClassRuleController extends BaseController {

    @DubboReference
    private UnlockCustomerClassRuleApi unlockCustomerClassRuleApi;

    @DubboReference
    private FlowMonthWashControlApi flowMonthWashControlApi;

    @DubboReference
    private UserApi userApi;

    @ApiOperation(value = "非锁客户分类规则列表")
    @PostMapping("/listPage")
    public Result<Page<UnlockCustomerClassRulePageVO>> listPage(@RequestBody QueryUnlockCustomerClassRulePageForm form) {
        QueryUnlockCustomerClassRulePageRequest request = PojoUtils.map(form, QueryUnlockCustomerClassRulePageRequest.class);
        Page<UnlockCustomerClassRulePageVO> pageResult = PojoUtils.map(unlockCustomerClassRuleApi.listPage(request), UnlockCustomerClassRulePageVO.class);
        if (CollUtil.isEmpty(pageResult.getRecords())) {
            return Result.success(new Page<>(form.getCurrent(), form.getSize()));
        }

        List<Long> userIdList = pageResult.getRecords().stream().map(UnlockCustomerClassRulePageVO::getLastOpUser).distinct().collect(Collectors.toList());
        List<UserDTO> userDTOList = userApi.listByIds(userIdList);

        for (UnlockCustomerClassRulePageVO unlockCustomerClassRulePageVO : pageResult.getRecords()) {
            if (unlockCustomerClassRulePageVO.getLastOpUser() != null && unlockCustomerClassRulePageVO.getLastOpUser() > 0) {
                // 设置操作人
                userDTOList.stream()
                        .filter(user -> user.getId().equals(unlockCustomerClassRulePageVO.getLastOpUser()))
                        .findAny().ifPresent(userDTO -> unlockCustomerClassRulePageVO.setLastOpUserName(userDTO.getName()));

            }
        }
        return Result.success(pageResult);
    }

    @ApiOperation(value = "新增")
    @PostMapping("/add")
    public Result add(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveOrUpdateUnlockCustomerClassRuleForm form) {
        if (userInfo == null) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getBasisStatus();
        if (flowMonthWashControlDTO == null) {
            throw new BusinessException(ResultCode.FAILED, "日程未开启或已经结束，无法操作！");
        }
        SaveOrUpdateUnlockCustomerClassRuleRequest request = PojoUtils.map(form, SaveOrUpdateUnlockCustomerClassRuleRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        unlockCustomerClassRuleApi.add(request);
        return Result.success();
    }

    @ApiOperation(value = "编辑")
    @PostMapping("/update")
    public Result update(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveOrUpdateUnlockCustomerClassRuleForm form) {
        if (userInfo == null) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getBasisStatus();
        if (flowMonthWashControlDTO == null) {
            throw new BusinessException(ResultCode.FAILED, "日程未开启或已经结束，无法操作！");
        }
        SaveOrUpdateUnlockCustomerClassRuleRequest request = PojoUtils.map(form, SaveOrUpdateUnlockCustomerClassRuleRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        unlockCustomerClassRuleApi.update(request);
        return Result.success();
    }

    @ApiOperation(value = "查询详情")
    @GetMapping("/detail")
    public Result<UnlockCustomerClassRuleVO> detail(@RequestParam(value = "id") Long id) {
        return Result.success(PojoUtils.map(unlockCustomerClassRuleApi.getById(id), UnlockCustomerClassRuleVO.class));
    }

    @ApiOperation(value = "删除")
    @GetMapping("/delete")
    public Result delete(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        if (userInfo == null) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getBasisStatus();
        if (flowMonthWashControlDTO == null) {
            throw new BusinessException(ResultCode.FAILED, "当前存在未关闭日程，无法操作！");
        }
        unlockCustomerClassRuleApi.delete(id);
        return Result.success();
    }

}
