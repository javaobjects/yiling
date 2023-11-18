package com.yiling.sjms.wash.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.api.UnlockThirdRecordApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.UnlockThirdRecordDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockThirdRecordPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockThirdRecordRequest;
import com.yiling.dataflow.wash.enums.WashErrorEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.wash.form.QueryUnlockThirdRecordPageForm;
import com.yiling.sjms.wash.form.SaveOrUpdateUnlockThirdRecordForm;
import com.yiling.sjms.wash.vo.UnlockCollectionDetailVO;
import com.yiling.sjms.wash.vo.UnlockCustomerClassDetailVO;
import com.yiling.sjms.wash.vo.UnlockThirdRecordVO;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 小三批备案表 前端控制器
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-11
 */
@RestController
@Api(tags = "非锁销量-小三批备案接口")
@RequestMapping("/unlockThirdRecord")
public class UnlockThirdRecordController extends BaseController {

    @DubboReference
    private FlowMonthWashControlApi flowMonthWashControlApi;

    @DubboReference
    private UnlockThirdRecordApi unlockThirdRecordApi;

    @DubboReference
    private CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    private EsbOrganizationApi esbOrganizationApi;

    @DubboReference
    private UserApi userApi;

    @ApiOperation(value = "小三批备案表列表")
    @PostMapping("/listPage")
    public Result<Page<UnlockThirdRecordVO>> listPage(@RequestBody QueryUnlockThirdRecordPageForm form) {
        QueryUnlockThirdRecordPageRequest request = PojoUtils.map(form, QueryUnlockThirdRecordPageRequest.class);
        Page<UnlockThirdRecordVO> pageResult = PojoUtils.map(unlockThirdRecordApi.listPage(request), UnlockThirdRecordVO.class);

        if (CollUtil.isEmpty(pageResult.getRecords())) {
            return Result.success(new Page<>(form.getCurrent(), form.getSize()));
        }

        List<Long> userIdList = pageResult.getRecords().stream().map(UnlockThirdRecordVO::getLastOpUser).distinct().collect(Collectors.toList());
        List<UserDTO> userDTOList = userApi.listByIds(userIdList);

        for (UnlockThirdRecordVO unlockThirdRecordVO : pageResult.getRecords()) {
            // 设置用户名
            if (unlockThirdRecordVO.getLastOpUser() != null && unlockThirdRecordVO.getLastOpUser() > 0) {
                // 设置操作人
                userDTOList.stream()
                        .filter(user -> user.getId().equals(unlockThirdRecordVO.getLastOpUser()))
                        .findAny().ifPresent(userDTO -> unlockThirdRecordVO.setLastOpUserName(userDTO.getName()));
            }

            // 设置生效部门
            String effectiveDepartment = unlockThirdRecordVO.getEffectiveDepartment();
            if (StringUtils.isNotEmpty(effectiveDepartment)) {
                JSONArray array = JSONUtil.parseArray(effectiveDepartment);

                StringBuilder sb = new StringBuilder();
                for (Object obj : array) {
                    JSONObject json = JSONUtil.parseObj(JSONUtil.toJsonStr(obj));
                    String name = json.getStr("name");
                    sb.append(name).append("、");
                }

                if (sb.toString().endsWith("、")) {
                    String departmentsDesc = sb.substring(0, sb.lastIndexOf("、"));
                    unlockThirdRecordVO.setEffectiveDepartment(departmentsDesc);
                } else {
                    unlockThirdRecordVO.setEffectiveDepartment("");
                }
            }
        }
        return Result.success(pageResult);
    }

    @ApiOperation(value = "新增小三批备案表")
    @PostMapping("/add")
    public Result add(@CurrentUser CurrentSjmsUserInfo userInfo,  @Valid @RequestBody SaveOrUpdateUnlockThirdRecordForm form) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getUnlockStatus();
        if (flowMonthWashControlDTO != null) {
            throw new BusinessException(WashErrorEnum.FLOW_WASH_CONTROL_CANT_OPERATE);
        }
        SaveOrUpdateUnlockThirdRecordRequest request = PojoUtils.map(form, SaveOrUpdateUnlockThirdRecordRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        unlockThirdRecordApi.add(request);
        return Result.success();
    }

    @ApiOperation(value = "编辑小三批备案表")
    @PostMapping("/edit")
    public Result edit(@CurrentUser CurrentSjmsUserInfo userInfo, @Valid @RequestBody SaveOrUpdateUnlockThirdRecordForm form) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getUnlockStatus();
        if (flowMonthWashControlDTO != null) {
            throw new BusinessException(WashErrorEnum.FLOW_WASH_CONTROL_CANT_OPERATE);
        }
        SaveOrUpdateUnlockThirdRecordRequest request = PojoUtils.map(form, SaveOrUpdateUnlockThirdRecordRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        unlockThirdRecordApi.update(request);
        return Result.success();
    }


    @ApiOperation(value = "获取小三批备案详情")
    @GetMapping("/detail")
    public Result<UnlockThirdRecordVO> detail(@RequestParam(value = "id") Long id) {
        UnlockThirdRecordDTO unlockThirdRecordDTO = unlockThirdRecordApi.getById(id);

        UnlockThirdRecordVO unlockThirdRecordVO = PojoUtils.map(unlockThirdRecordDTO, UnlockThirdRecordVO.class);

        Long crmId = unlockThirdRecordVO.getOrgCrmId();
        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(crmId);
        // 设置供应链角色
        unlockThirdRecordVO.setRegionName(crmEnterpriseDTO.getRegionName());
        // 设置所属区域
        unlockThirdRecordVO.setSupplyChainRole(crmEnterpriseDTO.getSupplyChainRole());

        // 设置生效部门
        String effectiveDepartment = unlockThirdRecordVO.getEffectiveDepartment();
        if (StringUtils.isNotEmpty(effectiveDepartment)) {
            JSONArray array = JSONUtil.parseArray(effectiveDepartment);

            List<UnlockThirdRecordVO.DepartmentInfo> departmentInfoList = new ArrayList<>();
            List<Long> orgIdList = new ArrayList<>();
            for (Object obj : array) {
                JSONObject json = JSONUtil.parseObj(JSONUtil.toJsonStr(obj));
                String code = json.getStr("code");
                if (StringUtils.isNotEmpty(code)) {
                    orgIdList.add(Long.parseLong(code));

                    UnlockThirdRecordVO.DepartmentInfo departmentInfo = new UnlockThirdRecordVO.DepartmentInfo();
                    departmentInfo.setOrgId(Long.parseLong(code));
                    departmentInfoList.add(departmentInfo);
                }
            }

            List<EsbOrganizationDTO> esbOrganizationDTOList = esbOrganizationApi.listByOrgIds(orgIdList);
            for (UnlockThirdRecordVO.DepartmentInfo departmentInfo : departmentInfoList) {
                EsbOrganizationDTO esbOrganizationDTO = esbOrganizationDTOList.stream().filter(e -> e.getOrgId().equals(departmentInfo.getOrgId())).findAny().orElse(null);
                if (esbOrganizationDTO != null) {
                    departmentInfo.setOrgName(esbOrganizationDTO.getOrgName());
                    departmentInfo.setFullpath(esbOrganizationDTO.getFullpath());
                }
            }
            unlockThirdRecordVO.setDepartmentList(departmentInfoList);
        }

        return Result.success(unlockThirdRecordVO);

    }


    @ApiOperation(value = "校验日程并获取备案详情")
    @GetMapping("/verifyControlAndGetDetail")
    public Result<UnlockThirdRecordVO> verifyControlAndGetDetail(@RequestParam(value = "id") Long id) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getUnlockStatus();
        if (flowMonthWashControlDTO != null) {
            throw new BusinessException(WashErrorEnum.FLOW_WASH_CONTROL_CANT_OPERATE);
        }
        return detail(id);
    }

    @ApiOperation(value = "删除")
    @GetMapping("/delete")
    public Result delete(@RequestParam(value = "id") Long id) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getUnlockStatus();
        if (flowMonthWashControlDTO != null) {
            throw new BusinessException(WashErrorEnum.FLOW_WASH_CONTROL_CANT_OPERATE);
        }
        unlockThirdRecordApi.delete(id);
        return Result.success();
    }

}
