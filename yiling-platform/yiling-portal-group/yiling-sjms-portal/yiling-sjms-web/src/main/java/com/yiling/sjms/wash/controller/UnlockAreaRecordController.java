package com.yiling.sjms.wash.controller;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.location.dto.LocationTreeDTO;
import com.yiling.basic.location.dto.RegionFullViewDTO;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.api.UnlockAreaRecordApi;
import com.yiling.dataflow.wash.api.UnlockAreaRecordRelationApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.UnlockAreaRecordRelationDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockAreaRecordPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockAreaRecordRequest;
import com.yiling.dataflow.wash.enums.WashErrorEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.wash.form.QueryLocationEmpTreeInfoListForm;
import com.yiling.sjms.wash.form.QueryUnlockAreaRecordPageForm;
import com.yiling.sjms.wash.form.SaveOrUpdateUnlockAreaRecordForm;
import com.yiling.sjms.wash.vo.UnlockAreaRecordLocationTreeVO;
import com.yiling.sjms.wash.vo.UnlockAreaRecordVO;
import com.yiling.sjms.wash.vo.UnlockThirdRecordVO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * <p>
 * 区域备案 前端控制器
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-05-11
 */
@RestController
@Api(tags = "非锁销量-区域备案接口")
@RequestMapping("/unlockAreaRecord")
public class UnlockAreaRecordController extends BaseController {

    @DubboReference
    private UserApi userApi;

    @DubboReference
    private LocationApi locationApi;

    @DubboReference
    private UnlockAreaRecordApi unlockAreaRecordApi;

    @DubboReference
    private UnlockAreaRecordRelationApi unlockAreaRecordRelationApi;

    @DubboReference
    private CrmGoodsCategoryApi crmGoodsCategoryApi;

    @DubboReference
    private FlowMonthWashControlApi flowMonthWashControlApi;


    @ApiOperation(value = "区域备案列表")
    @PostMapping("/listPage")
    public Result<Page<UnlockAreaRecordVO>> listPage(@RequestBody QueryUnlockAreaRecordPageForm form) {
        QueryUnlockAreaRecordPageRequest request = PojoUtils.map(form, QueryUnlockAreaRecordPageRequest.class);
        Page<UnlockAreaRecordVO> page =  PojoUtils.map(unlockAreaRecordApi.listPage(request), UnlockAreaRecordVO.class);

        if (CollUtil.isEmpty(page.getRecords())) {
            return Result.success(new Page<>(form.getCurrent(), form.getSize()));
        }

        List<Long> userIdList = page.getRecords().stream().map(UnlockAreaRecordVO::getLastOpUser).distinct().collect(Collectors.toList());
        List<UserDTO> userDTOList = userApi.listByIds(userIdList);
        for (UnlockAreaRecordVO unlockAreaRecordVO : page.getRecords()) {
            // 设置操作人
            userDTOList.stream()
                    .filter(user -> user.getId().equals(unlockAreaRecordVO.getLastOpUser()))
                    .findAny().ifPresent(userDTO -> unlockAreaRecordVO.setLastOpUserName(userDTO.getName()));
        }
        return Result.success(page);
    }

    @ApiOperation(value = "添加区域备案")
    @PostMapping("/add")
    public Result add(@CurrentUser CurrentSjmsUserInfo userInfo, @Valid @RequestBody SaveOrUpdateUnlockAreaRecordForm form) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getUnlockStatus();
        if (flowMonthWashControlDTO != null) {
            throw new BusinessException(WashErrorEnum.FLOW_WASH_CONTROL_CANT_OPERATE);
        }
        SaveOrUpdateUnlockAreaRecordRequest request = PojoUtils.map(form, SaveOrUpdateUnlockAreaRecordRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());

        // 获取品种名称
        CrmGoodsCategoryDTO categoryDTO = crmGoodsCategoryApi.findById(request.getCategoryId());
        if (categoryDTO == null) {
            throw new BusinessException(ResultCode.FAILED, "品种不存在");
        }
        request.setCategoryName(categoryDTO.getName());

        unlockAreaRecordApi.add(request);
        return Result.success();
    }


    @ApiOperation(value = "编辑区域备案")
    @PostMapping("/update")
    public Result update(@CurrentUser CurrentSjmsUserInfo userInfo, @Valid @RequestBody SaveOrUpdateUnlockAreaRecordForm form) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getUnlockStatus();
        if (flowMonthWashControlDTO != null) {
            throw new BusinessException(WashErrorEnum.FLOW_WASH_CONTROL_CANT_OPERATE);
        }
        if (form.getId() == null) {
            throw new BusinessException(ResultCode.FAILED, "ID不可为空");
        }
        SaveOrUpdateUnlockAreaRecordRequest request = PojoUtils.map(form, SaveOrUpdateUnlockAreaRecordRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());

        // 获取品种名称
        CrmGoodsCategoryDTO categoryDTO = crmGoodsCategoryApi.findById(request.getCategoryId());
        if (categoryDTO == null) {
            throw new BusinessException(ResultCode.FAILED, "品种不存在");
        }
        request.setCategoryName(categoryDTO.getName());

        unlockAreaRecordApi.update(request);
        return Result.success();
    }

    @ApiOperation(value = "查询区域备案详情")
    @GetMapping("/detail")
    public Result<UnlockAreaRecordVO> detail(@RequestParam(value = "id") Long id) {
        return Result.success(PojoUtils.map(unlockAreaRecordApi.getById(id), UnlockAreaRecordVO.class));
    }

    @ApiOperation(value = "查询区域设置树详情")
    @PostMapping("/locationEmpTreeInfo")
    public Result<UnlockAreaRecordLocationTreeVO> getLocationEmpTreeInfo(@Valid @RequestBody QueryLocationEmpTreeInfoListForm form) {
        //  获取省市区树
        List<LocationTreeDTO> locationTreeDTOList = locationApi.listTreeByParentCode("", 2);
        List<UnlockAreaRecordLocationTreeVO.AreaTreeVO> areaTreeList = PojoUtils.map(locationTreeDTOList, UnlockAreaRecordLocationTreeVO.AreaTreeVO.class);

        // 给区级别的赋值省code（此为前端要求）
        List<RegionFullViewDTO> allPcrList = locationApi.getAllProvinceCityRegionList();
        setLocationEmpTreeProvinceCode(areaTreeList, allPcrList);

        //  获取该客户分类及品种下的所有区域设置
        List<UnlockAreaRecordRelationDTO> allRelationList = unlockAreaRecordRelationApi.getByClassAndCategoryId(form.getCustomerClassification(), form.getCategoryId());
        if (CollUtil.isEmpty(allRelationList)) {
            return Result.success(new UnlockAreaRecordLocationTreeVO(areaTreeList));
        }

        //  获取当前区域备案的区域详情列表（当前为编辑页面时，不置灰并勾选）
        List<UnlockAreaRecordRelationDTO> currentRelationList = null;
        if (form.getUnlockAreaRecordId() != null) {     // 编辑页面
            // allRelationList 中的元素包含 currentRelationList中的元素
            currentRelationList = unlockAreaRecordRelationApi.getListByUnlockAreaRecordRelationId(form.getUnlockAreaRecordId());
        }

        // 设置省市区树和区域备案的员工信息
        List<String> checkCodeList = new ArrayList<>();
        setLocationEmpTree(areaTreeList, allRelationList, currentRelationList, checkCodeList);

        return Result.success(new UnlockAreaRecordLocationTreeVO(checkCodeList, areaTreeList));
    }

    @ApiOperation(value = "删除")
    @GetMapping("/delete")
    public Result delete(@RequestParam(value = "id") Long id) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getUnlockStatus();
        if (flowMonthWashControlDTO != null) {
            throw new BusinessException(WashErrorEnum.FLOW_WASH_CONTROL_CANT_OPERATE);
        }
        unlockAreaRecordApi.delete(id);
        return Result.success();
    }


    private void setLocationEmpTreeProvinceCode(List<UnlockAreaRecordLocationTreeVO.AreaTreeVO> treeInfoList, List<RegionFullViewDTO> allPcrList) {
        for (UnlockAreaRecordLocationTreeVO.AreaTreeVO areaTreeVO : treeInfoList) {
            String code = areaTreeVO.getCode();
            RegionFullViewDTO regionFullViewDTO = allPcrList.stream().filter(pcr -> pcr.getRegionCode().equals(code)).findAny().orElse(null);
            if (regionFullViewDTO != null) {
                areaTreeVO.setProvinceCode(regionFullViewDTO.getProvinceCode());
                continue;
            }
            if (CollUtil.isNotEmpty(areaTreeVO.getChildren())) {
                setLocationEmpTreeProvinceCode(areaTreeVO.getChildren(), allPcrList);
            }
        }
    }

    private TreeEmpInfo setLocationEmpTree(List<UnlockAreaRecordLocationTreeVO.AreaTreeVO> treeInfoList, List<UnlockAreaRecordRelationDTO> allRelationList, List<UnlockAreaRecordRelationDTO> currentRelationList, List<String> checkCodeList) {
        Set<TreeEmpInfo> treeEmpInfoSet = new HashSet<>();

        for (UnlockAreaRecordLocationTreeVO.AreaTreeVO areaTreeVO : treeInfoList) {
            if (CollUtil.isNotEmpty(areaTreeVO.getChildren())) {
                TreeEmpInfo childEmpInfo = setLocationEmpTree(areaTreeVO.getChildren(), allRelationList, currentRelationList, checkCodeList);
                if (childEmpInfo != null) {
                    areaTreeVO.setEmpName(childEmpInfo.getEmpName());
                    areaTreeVO.setPostName(childEmpInfo.getPostName());
                    areaTreeVO.setCheckFlag(childEmpInfo.getCheckFlag());
                    if (childEmpInfo.getCheckFlag()) {
                        checkCodeList.add(areaTreeVO.getCode());
                    }
                    areaTreeVO.setDisabled(childEmpInfo.getDisabled());
                }
            }

            // 只有区级别会执行以下逻辑
            String code = areaTreeVO.getCode();
            UnlockAreaRecordRelationDTO unlockAreaRecordRelationDTO = allRelationList.stream().filter(rel -> rel.getRegionCode().equals(code)).findAny().orElse(null);
            if (unlockAreaRecordRelationDTO != null) {
                if (CollUtil.isEmpty(currentRelationList)) {
                    // 禁用
                    areaTreeVO.setDisabled(true);
                } else {
                    if (currentRelationList.stream().anyMatch(rel -> rel.getRegionCode().equals(code))) {
                        // 选中，不禁用
                        areaTreeVO.setCheckFlag(true);
                        checkCodeList.add(code);
                    } else {
                        // 禁用
                        areaTreeVO.setDisabled(true);
                    }
                }
                areaTreeVO.setPostName(unlockAreaRecordRelationDTO.getPostName());
                areaTreeVO.setEmpName(unlockAreaRecordRelationDTO.getEmpName());
            }
            // 只有区级别会执行以上逻辑

            // 给上级对象赋值的逻辑
            TreeEmpInfo treeEmpInfo = new TreeEmpInfo();
            treeEmpInfo.setEmpName(areaTreeVO.getEmpName());
            treeEmpInfo.setPostName(areaTreeVO.getPostName());
            treeEmpInfo.setCheckFlag(areaTreeVO.getCheckFlag());
            treeEmpInfo.setDisabled(areaTreeVO.getDisabled());
            treeEmpInfoSet.add(treeEmpInfo);
        }

        if (treeEmpInfoSet.size() == 1) {   // 如果所有的child对象都是一致的，则将该信息返回给parent对象
            TreeEmpInfo treeEmpInfo = treeEmpInfoSet.stream().findAny().get();
            if (treeEmpInfo.getCheckFlag()) {   // 只有置灰的时候会将岗位和员工信息返回至parent
                treeEmpInfo.setEmpName(null);
                treeEmpInfo.setPostName(null);
            }
            return treeEmpInfo;
        }
        return null;
    }


    @Data
    public static class TreeEmpInfo {

        private String postName;

        private String empName;

        private Boolean disabled = false;

        private Boolean checkFlag = false;
    }
}
