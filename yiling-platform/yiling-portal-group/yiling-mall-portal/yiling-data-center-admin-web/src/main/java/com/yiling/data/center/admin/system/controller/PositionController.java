package com.yiling.data.center.admin.system.controller;

import java.util.List;
import java.util.Map;
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
import com.yiling.data.center.admin.system.form.QueryPositionPageListForm;
import com.yiling.data.center.admin.system.form.SavePositionForm;
import com.yiling.data.center.admin.system.form.UpdatePositionStatusForm;
import com.yiling.data.center.admin.system.vo.PositionPageListItemVO;
import com.yiling.data.center.admin.system.vo.PositionVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.PositionApi;
import com.yiling.user.enterprise.dto.EnterprisePositionDTO;
import com.yiling.user.enterprise.dto.request.QueryPositionPageListRequest;
import com.yiling.user.enterprise.dto.request.SavePositionRequest;
import com.yiling.user.enterprise.dto.request.UpdatePositionStatusRequest;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 职位模块 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@RestController
@RequestMapping("/position")
@Api(tags = "职位模块接口")
@Slf4j
public class PositionController extends BaseController {

    @DubboReference
    PositionApi positionApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    EmployeeApi employeeApi;

    @ApiOperation(value = "职位分页列表")
    @PostMapping("/pageList")
    public Result<Page<PositionPageListItemVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryPositionPageListForm form) {
        QueryPositionPageListRequest request = PojoUtils.map(form, QueryPositionPageListRequest.class);
        request.setEid(staffInfo.getCurrentEid());

        Page<EnterprisePositionDTO> page = positionApi.pageList(request);
        List<EnterprisePositionDTO> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Result.success(form.getPage());
        }

        // 用户字典
        List<Long> createUserIds = records.stream().map(EnterprisePositionDTO::getCreateUser).distinct().collect(Collectors.toList());
        List<UserDTO> createUserList = userApi.listByIds(createUserIds);
        Map<Long, UserDTO> createUserMap = createUserList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));

        // 职位人数字典
        List<Long> positionIds = records.stream().map(EnterprisePositionDTO::getId).collect(Collectors.toList());
        Map<Long, Long> positionEmployNumMap = employeeApi.countByPositionIds(positionIds);

        Page<PositionPageListItemVO> newPage = PojoUtils.map(page, PositionPageListItemVO.class);
        newPage.getRecords().forEach(e -> {
            e.setCreateUserName(createUserMap.getOrDefault(e.getCreateUser(), new UserDTO()).getName());
            e.setPeopleNumber(positionEmployNumMap.getOrDefault(e.getId(), 0L));
        });

        return Result.success(newPage);
    }

    @ApiOperation(value = "获取职位详情")
    @GetMapping("/get")
    public Result get(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam @ApiParam(value = "职位ID", required = true) Long id) {
        EnterprisePositionDTO enterprisePositionDTO = positionApi.getById(id);
        if (enterprisePositionDTO == null) {
            return Result.failed("未找到id对应的职位信息");
        } else if (!enterprisePositionDTO.getEid().equals(staffInfo.getCurrentEid())) {
            return Result.failed("无权访问id对应的职位信息");
        }

        return Result.success(PojoUtils.map(enterprisePositionDTO, PositionVO.class));
    }

    @ApiOperation(value = "新增/修改职位信息")
    @PostMapping("/save")
    @Log(title = "新增/修改职位信息", businessType = BusinessTypeEnum.UPDATE)
    public Result save(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SavePositionForm form) {
        SavePositionRequest request = PojoUtils.map(form, SavePositionRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean result = positionApi.save(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "修改职位状态")
    @PostMapping("/updateStatus")
    @Log(title = "修改职位状态", businessType = BusinessTypeEnum.UPDATE)
    public Result updateStatus(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdatePositionStatusForm form) {
        UpdatePositionStatusRequest request = PojoUtils.map(form, UpdatePositionStatusRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean result = positionApi.updateStatus(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

}
