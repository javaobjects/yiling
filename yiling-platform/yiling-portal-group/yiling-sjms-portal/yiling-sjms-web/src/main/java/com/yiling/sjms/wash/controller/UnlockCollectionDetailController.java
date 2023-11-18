package com.yiling.sjms.wash.controller;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.api.UnlockCollectionDetailApi;
import com.yiling.dataflow.wash.api.UnlockCollectionDetailRelationApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.UnlockAreaRecordRelationDTO;
import com.yiling.dataflow.wash.dto.UnlockCollectionDetailDTO;
import com.yiling.dataflow.wash.dto.UnlockCollectionDetailRelationDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCollectionDetailPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockCollectionDetailRequest;
import com.yiling.dataflow.wash.enums.WashErrorEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.wash.form.QueryUnlockCollectionDetailPageForm;
import com.yiling.sjms.wash.form.QueryUnlockCollectionPriceTreeForm;
import com.yiling.sjms.wash.form.SaveOrUpdateUnlockCollectionDetailForm;
import com.yiling.sjms.wash.vo.UnlockAreaRecordVO;
import com.yiling.sjms.wash.vo.UnlockCollectionDetailVO;
import com.yiling.sjms.wash.vo.UnlockCollectionPriceTreeVO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * <p>
 * 采集明细 前端控制器
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-05-11
 */
@RestController
@Api(tags = "非锁销量-采集明细接口")
@RequestMapping("/unlockCollectionDetail")
public class UnlockCollectionDetailController extends BaseController {

    @DubboReference
    private UnlockCollectionDetailApi unlockCollectionDetailApi;

    @DubboReference
    private UnlockCollectionDetailRelationApi unlockCollectionDetailRelationApi;

    @DubboReference
    private LocationApi locationApi;

    @DubboReference
    private UserApi userApi;

    @DubboReference
    private FlowMonthWashControlApi flowMonthWashControlApi;

    @DubboReference
    private CrmGoodsCategoryApi crmGoodsCategoryApi;

    @ApiOperation(value = "采集明细列表")
    @PostMapping("/listPage")
    public Result<Page<UnlockCollectionDetailVO>> listPage(@RequestBody QueryUnlockCollectionDetailPageForm form) {
        QueryUnlockCollectionDetailPageRequest request = PojoUtils.map(form, QueryUnlockCollectionDetailPageRequest.class);
        Page<UnlockCollectionDetailVO> page = PojoUtils.map(unlockCollectionDetailApi.listPage(request), UnlockCollectionDetailVO.class);
        if (CollUtil.isEmpty(page.getRecords())) {
            return Result.success(new Page<>());
        }

        List<Long> userIdList = page.getRecords().stream().map(UnlockCollectionDetailVO::getLastOpUser).distinct().collect(Collectors.toList());
        List<UserDTO> userDTOList = userApi.listByIds(userIdList);
        for (UnlockCollectionDetailVO unlockCollectionDetailVO : page.getRecords()) {
            if (unlockCollectionDetailVO.getLastOpUser() != null && unlockCollectionDetailVO.getLastOpUser() > 0) {
                // 设置操作人
                userDTOList.stream()
                        .filter(user -> user.getId().equals(unlockCollectionDetailVO.getLastOpUser()))
                        .findAny().ifPresent(userDTO -> unlockCollectionDetailVO.setLastOpUserName(userDTO.getName()));
            }
        }
        return Result.success(page);
    }

    @ApiOperation(value = "添加采集明细")
    @PostMapping("/add")
    public Result add(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveOrUpdateUnlockCollectionDetailForm form) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getUnlockStatus();
        if (flowMonthWashControlDTO != null) {
            throw new BusinessException(WashErrorEnum.FLOW_WASH_CONTROL_CANT_OPERATE);
        }
        SaveOrUpdateUnlockCollectionDetailRequest request = PojoUtils.map(form, SaveOrUpdateUnlockCollectionDetailRequest.class);
        if (userInfo != null) {
            request.setOpUserId(userInfo.getCurrentUserId());
        }
        unlockCollectionDetailApi.add(request);
        return Result.success();
    }

    @ApiOperation(value = "编辑采集明细")
    @PostMapping("/edit")
    public Result edit(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveOrUpdateUnlockCollectionDetailForm form) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getUnlockStatus();
        if (flowMonthWashControlDTO != null) {
            throw new BusinessException(WashErrorEnum.FLOW_WASH_CONTROL_CANT_OPERATE);
        }
        SaveOrUpdateUnlockCollectionDetailRequest request = PojoUtils.map(form, SaveOrUpdateUnlockCollectionDetailRequest.class);
        if (userInfo != null) {
            request.setOpUserId(userInfo.getCurrentUserId());
        }
        unlockCollectionDetailApi.update(request);
        return Result.success();
    }

    @ApiOperation(value = "查询详情")
    @GetMapping("/detail")
    public Result<UnlockCollectionDetailVO> detail(@RequestParam(value = "id") Long id) {
        UnlockCollectionDetailVO unlockCollectionDetailVO = PojoUtils.map(unlockCollectionDetailApi.getById(id), UnlockCollectionDetailVO.class);
        if (unlockCollectionDetailVO.getCategoryId() != null && unlockCollectionDetailVO.getCategoryId() > 0) {
            CrmGoodsCategoryDTO categoryDTO = crmGoodsCategoryApi.findById(unlockCollectionDetailVO.getCategoryId());
            if (categoryDTO != null) {
                unlockCollectionDetailVO.setCategory(categoryDTO.getName());
            }
        }
        return Result.success(PojoUtils.map(unlockCollectionDetailVO, UnlockCollectionDetailVO.class));
    }

    @ApiOperation(value = "删除")
    @GetMapping("/delete")
    public Result delete(@RequestParam(value = "id") Long id) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getUnlockStatus();
        if (flowMonthWashControlDTO != null) {
            throw new BusinessException(WashErrorEnum.FLOW_WASH_CONTROL_CANT_OPERATE);
        }
        unlockCollectionDetailApi.delete(id);
        return Result.success();
    }

    @ApiOperation(value = "获取采集明细区域设置信息")
    @PostMapping("/getUnlockCollectionPriceTree")
    public Result<UnlockCollectionPriceTreeVO> getUnlockCollectionPriceTree(@RequestBody QueryUnlockCollectionPriceTreeForm form) {
        //  获取省市区树
        List<LocationTreeDTO> locationTreeDTOList = locationApi.listTreeByParentCode("", 2);
        List<UnlockCollectionPriceTreeVO.AreaTreeVO> treeList = PojoUtils.map(locationTreeDTOList, UnlockCollectionPriceTreeVO.AreaTreeVO.class);

        //  获取该客户分类及品种下的所有区域设置
        List<UnlockCollectionDetailRelationDTO> allRelationList = unlockCollectionDetailRelationApi.getListByCrmGoodsCode(form.getCrmGoodsCode());
        if (CollUtil.isEmpty(allRelationList)) {
            return Result.success(new UnlockCollectionPriceTreeVO(treeList));
        }

        //  获取当前区域备案的区域详情列表（不置灰并勾选）
        List<UnlockCollectionDetailRelationDTO> currentRelationList = null;
        if (form.getUnlockCollectionDetailId() != null) {     // 编辑页面
            // allRelationList 中的元素包含 currentRelationList中的元素
            currentRelationList = unlockCollectionDetailRelationApi.getListByUnlockCollectionDetailId(form.getUnlockCollectionDetailId());
        }

        List<String> checkCodeList = new ArrayList<>();
        setCollectionPriceTree(treeList, allRelationList, currentRelationList, checkCodeList);
        return Result.success(new UnlockCollectionPriceTreeVO(checkCodeList, treeList));
    }


    private TreeEmpInfo setCollectionPriceTree(List<UnlockCollectionPriceTreeVO.AreaTreeVO> treeInfoList, List<UnlockCollectionDetailRelationDTO> allRelationList, List<UnlockCollectionDetailRelationDTO> currentRelationList, List<String> checkCodeList) {
        Set<TreeEmpInfo> treeEmpInfoSet = new HashSet<>();

        for (UnlockCollectionPriceTreeVO.AreaTreeVO areaTreeVO : treeInfoList) {
            if (CollUtil.isNotEmpty(areaTreeVO.getChildren())) {
                TreeEmpInfo childEmpInfo = setCollectionPriceTree(areaTreeVO.getChildren(), allRelationList, currentRelationList, checkCodeList);
                if (childEmpInfo != null) {
                    areaTreeVO.setCheckFlag(childEmpInfo.getCheckFlag());
                    if (childEmpInfo.getCheckFlag()) {
                        checkCodeList.add(areaTreeVO.getCode());
                    }
                    areaTreeVO.setDisabled(childEmpInfo.getDisabled());
                }
            }

            // 只有区级别会执行以下逻辑
            String code = areaTreeVO.getCode();
            UnlockCollectionDetailRelationDTO unlockCollectionDetailRelationDTO = allRelationList.stream().filter(rel -> rel.getRegionCode().equals(code)).findAny().orElse(null);
            if (unlockCollectionDetailRelationDTO != null) {
                if (CollUtil.isEmpty(currentRelationList)) {
                    // 锁定
                    areaTreeVO.setDisabled(true);
                } else {
                    if (currentRelationList.stream().anyMatch(rel -> rel.getRegionCode().equals(code))) {
                        // 选中，不锁定
                        areaTreeVO.setCheckFlag(true);
                        checkCodeList.add(code);
                    } else {
                        // 锁定
                        areaTreeVO.setDisabled(true);
                    }
                }
            }

            // 给上级对象赋值的逻辑
            TreeEmpInfo treeEmpInfo = new TreeEmpInfo();
            treeEmpInfo.setCheckFlag(areaTreeVO.getCheckFlag());
            treeEmpInfo.setDisabled(areaTreeVO.getDisabled());
            treeEmpInfoSet.add(treeEmpInfo);
        }

        if (treeEmpInfoSet.size() == 1) {   // 如果所有的child对象都是一致的，则将该信息返回给parent对象
            return treeEmpInfoSet.stream().findAny().get();
        }
        return null;
    }


    @Data
    public static class TreeEmpInfo {

        private Boolean disabled = false;

        private Boolean checkFlag = false;
    }

}
