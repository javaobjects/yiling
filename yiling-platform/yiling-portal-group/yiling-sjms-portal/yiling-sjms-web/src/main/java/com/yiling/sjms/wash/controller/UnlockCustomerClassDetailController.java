package com.yiling.sjms.wash.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.UnlockCustomerClassDetailApi;
import com.yiling.dataflow.wash.api.UnlockCustomerClassRuleApi;
import com.yiling.dataflow.wash.dto.UnlockCustomerClassDetailDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerClassDetailCountRequest;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerClassDetailPageRequest;
import com.yiling.dataflow.wash.dto.request.UpdateCustomerClassificationRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.wash.form.QueryUnlockCustomerClassDetailPageForm;
import com.yiling.sjms.wash.form.UpdateCustomerClassificationForm;
import com.yiling.sjms.wash.vo.UnlockCustomerClassDetailPageVO;
import com.yiling.sjms.wash.vo.UnlockCustomerClassDetailVO;
import com.yiling.sjms.wash.vo.UnlockCustomerClassRulePageVO;
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
@Api(tags = "非锁客户分类明细")
@RequestMapping("/unlockCustomerClassDetail")
public class UnlockCustomerClassDetailController extends BaseController {

    @DubboReference
    private UnlockCustomerClassDetailApi unlockCustomerClassDetailApi;

    @DubboReference
    private UserApi userApi;

    @ApiOperation(value = "非锁客户分类明细列表")
    @PostMapping("/listPage")
    public Result<UnlockCustomerClassDetailPageVO> listPage(@RequestBody QueryUnlockCustomerClassDetailPageForm form) {
        QueryUnlockCustomerClassDetailPageRequest request = PojoUtils.map(form, QueryUnlockCustomerClassDetailPageRequest.class);

        // 列表数据
        Page<UnlockCustomerClassDetailDTO> page = unlockCustomerClassDetailApi.listPage(request);
        Page<UnlockCustomerClassDetailVO> pageResult = PojoUtils.map(page, UnlockCustomerClassDetailVO.class);

        if (CollUtil.isEmpty(pageResult.getRecords())) {
            UnlockCustomerClassDetailPageVO unlockCustomerClassDetailPageVO = new UnlockCustomerClassDetailPageVO();
            unlockCustomerClassDetailPageVO.setPage(new Page<>(request.getCurrent(), request.getSize()));
            return Result.success(unlockCustomerClassDetailPageVO);
        }

        List<Long> userIdList = pageResult.getRecords().stream().map(UnlockCustomerClassDetailVO::getLastOpUser).distinct().collect(Collectors.toList());
        List<UserDTO> userDTOList = userApi.listByIds(userIdList);

        for (UnlockCustomerClassDetailVO unlockCustomerClassDetailVO : pageResult.getRecords()) {
            if (unlockCustomerClassDetailVO.getLastOpUser() != null && unlockCustomerClassDetailVO.getLastOpUser() > 0) {
                // 设置操作人
                userDTOList.stream()
                        .filter(user -> user.getId().equals(unlockCustomerClassDetailVO.getLastOpUser()))
                        .findAny().ifPresent(userDTO -> unlockCustomerClassDetailVO.setLastOpUserName(userDTO.getName()));
            }
        }

        // 未分类
        QueryUnlockCustomerClassDetailCountRequest unclassifiedRequest = PojoUtils.map(form, QueryUnlockCustomerClassDetailCountRequest.class);
        unclassifiedRequest.setClassFlag(0);
        Integer unclassifiedCount = unlockCustomerClassDetailApi.count(unclassifiedRequest);

        // 已分类
        QueryUnlockCustomerClassDetailCountRequest classifiedRequest = PojoUtils.map(form, QueryUnlockCustomerClassDetailCountRequest.class);
        classifiedRequest.setClassFlag(1);
        Integer classifiedCount = unlockCustomerClassDetailApi.count(classifiedRequest);

        UnlockCustomerClassDetailPageVO unlockCustomerClassDetailPageVO = new UnlockCustomerClassDetailPageVO();
        unlockCustomerClassDetailPageVO.setTotal(((Long)pageResult.getTotal()).intValue());
        unlockCustomerClassDetailPageVO.setClassifiedCount(classifiedCount);
        unlockCustomerClassDetailPageVO.setUnclassifiedCount(unclassifiedCount);
        unlockCustomerClassDetailPageVO.setPage(pageResult);

        return Result.success(unlockCustomerClassDetailPageVO);
    }

    // 设置分类
    @ApiOperation(value = "非锁客户分类明细设置分类")
    @PostMapping("/resetCustomerClassification")
    public Result resetCustomerClassification(@CurrentUser CurrentSjmsUserInfo userInfo,  @RequestBody UpdateCustomerClassificationForm form) {
        if (userInfo == null) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        UpdateCustomerClassificationRequest request = PojoUtils.map(form, UpdateCustomerClassificationRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());

        unlockCustomerClassDetailApi.resetCustomerClassification(request);
        return Result.success();
    }

}
