package com.yiling.sjms.wash.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.location.dto.LocationTreeDTO;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseByNamePageListRequest;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.api.CrmGoodsTagApi;
import com.yiling.dataflow.crm.bo.CrmEnterprisePartBO;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsTagDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsCategoryRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsInfoPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsTagPageRequest;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.api.UnlockSaleBusinessApi;
import com.yiling.dataflow.wash.api.UnlockSaleCustomerApi;
import com.yiling.dataflow.wash.api.UnlockSaleCustomerRangeApi;
import com.yiling.dataflow.wash.api.UnlockSaleCustomerRangeProvinceApi;
import com.yiling.dataflow.wash.api.UnlockSaleDepartmentApi;
import com.yiling.dataflow.wash.api.UnlockSaleGoodsApi;
import com.yiling.dataflow.wash.api.UnlockSaleGoodsCategoryApi;
import com.yiling.dataflow.wash.api.UnlockSaleGoodsTagApi;
import com.yiling.dataflow.wash.api.UnlockSaleRuleApi;
import com.yiling.dataflow.wash.bo.UnlockSaleGoodsCategoryBO;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.UnlockSaleCustomerRangeDTO;
import com.yiling.dataflow.wash.dto.UnlockSaleDepartmentDTO;
import com.yiling.dataflow.wash.dto.UnlockSaleRuleDTO;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleBusinessRequest;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleCustomerRequest;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleGoodsCategoryRequest;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleGoodsRequest;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleGoodsTagRequest;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleRuleRequest;
import com.yiling.dataflow.wash.dto.request.QueryCrmEnterpriseUnlockSalePageListRequest;
import com.yiling.dataflow.wash.dto.request.QueryUnlockSaleGoodsCategoryPageRequest;
import com.yiling.dataflow.wash.dto.request.QueryuUnlockSaleRulePageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockSaleRuleRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleBusinessRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleCustomerRangeProvinceRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleCustomerRangeRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleCustomerRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleDepartmentRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleGoodsCategoryRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleGoodsRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleGoodsTagRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.vo.EnterpriseDisableVO;
import com.yiling.sjms.crm.vo.CrmGoodsTagVO;
import com.yiling.sjms.wash.form.DeleteUnlockSaleBusinessForm;
import com.yiling.sjms.wash.form.DeleteUnlockSaleCustomerForm;
import com.yiling.sjms.wash.form.DeleteUnlockSaleGoodsCategoryForm;
import com.yiling.sjms.wash.form.DeleteUnlockSaleGoodsForm;
import com.yiling.sjms.wash.form.DeleteUnlockSaleGoodsTagForm;
import com.yiling.sjms.wash.form.QueryCrmEnterpriseUnlockSaleFlowPageListForm;
import com.yiling.sjms.wash.form.QueryUnlockSaleRulePageForm;
import com.yiling.sjms.wash.form.SaveCustomerRangeProvinceForm;
import com.yiling.sjms.wash.form.SaveOrUpdateUnlockSaleRuleForm;
import com.yiling.sjms.wash.form.SaveUnlockSaleBusinessForm;
import com.yiling.sjms.wash.form.SaveUnlockSaleCustomerForm;
import com.yiling.sjms.wash.form.SaveUnlockSaleGoodsCategoryForm;
import com.yiling.sjms.wash.form.SaveUnlockSaleGoodsForm;
import com.yiling.sjms.wash.form.SaveUnlockSaleGoodsTagForm;
import com.yiling.sjms.wash.form.UnlockSaleGoodsCategoryForm;
import com.yiling.sjms.wash.form.UnlockSaleGoodsPageForm;
import com.yiling.sjms.wash.form.UnlockSaleGoodsTagPageForm;
import com.yiling.sjms.wash.vo.CrmEnterpriseBusinessRuleVO;
import com.yiling.sjms.wash.vo.CrmEnterpriseUnlockSaleFlowVO;
import com.yiling.sjms.wash.vo.LocationUnlockCustomerTreeVO;
import com.yiling.sjms.wash.vo.LocationUnlockCustomerVO;
import com.yiling.sjms.wash.vo.UnlockSaleCustomerRangeVO;
import com.yiling.sjms.wash.vo.UnlockSaleDepartmentVO;
import com.yiling.sjms.wash.vo.UnlockSaleGoodsCategoryVO;
import com.yiling.sjms.wash.vo.UnlockSaleGoodsTagVO;
import com.yiling.sjms.wash.vo.UnlockSaleGoodsVO;
import com.yiling.sjms.wash.vo.UnlockSaleRuleVO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: shuang.zhang
 * @date: 2023/5/15
 */
@RestController
@Api(tags = "非锁销量分配规则")
@RequestMapping("/unlockSaleRule")
public class UnLockSaleRuleController extends BaseController {

    @DubboReference
    private UnlockSaleRuleApi                  unlockSaleRuleApi;
    @DubboReference
    private UnlockSaleBusinessApi              unlockSaleBusinessApi;
    @DubboReference
    private UnlockSaleCustomerApi              unlockSaleCustomerApi;
    @DubboReference
    private UnlockSaleGoodsApi                 unlockSaleGoodsApi;
    @DubboReference
    private UnlockSaleGoodsTagApi              unlockSaleGoodsTagApi;
    @DubboReference
    private UnlockSaleGoodsCategoryApi         unlockSaleGoodsCategoryApi;
    @DubboReference
    private CrmGoodsInfoApi                    crmGoodsInfoApi;
    @DubboReference
    private UnlockSaleCustomerRangeApi         unlockSaleCustomerRangeApi;
    @DubboReference
    private UnlockSaleDepartmentApi            unlockSaleDepartmentApi;
    @DubboReference
    private UnlockSaleCustomerRangeProvinceApi unlockSaleCustomerRangeProvinceApi;
    @DubboReference
    private LocationApi                        locationApi;
    @DubboReference
    private CrmGoodsTagApi                     crmGoodsTagApi;
    @DubboReference
    private CrmGoodsCategoryApi                crmGoodsCategoryApi;
    @DubboReference
    private UserApi                            userApi;
    @DubboReference
    private CrmEnterpriseApi                   crmEnterpriseApi;
    @DubboReference
    private FlowMonthWashControlApi            flowMonthWashControlApi;


    @ApiOperation(value = "非锁销量分配规则列表")
    @PostMapping("/listPage")
    public Result<Page<UnlockSaleRuleVO>> listPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryUnlockSaleRulePageForm form) {
        QueryuUnlockSaleRulePageRequest request = PojoUtils.map(form, QueryuUnlockSaleRulePageRequest.class);
        Page<UnlockSaleRuleVO> page = PojoUtils.map(unlockSaleRuleApi.listPage(request), UnlockSaleRuleVO.class);
        List<Long> ids = page.getRecords().stream().map(e -> e.getId()).collect(Collectors.toList());
        List<Long> updateUserIds = unlockSaleRuleApi.listPage(request).getRecords().stream().map(e -> e.getUpdateUser()).collect(Collectors.toList());
        List<UnlockSaleCustomerRangeDTO> unlockSaleCustomerRangeDTOS = unlockSaleCustomerRangeApi.getUnlockSaleCustomerRangeByRuleIds(ids);
        Map<Long, UnlockSaleCustomerRangeDTO> unlockSaleCustomerRangeDTOMap = unlockSaleCustomerRangeDTOS.stream().collect(Collectors.toMap(UnlockSaleCustomerRangeDTO::getRuleId, Function.identity()));
        List<UnlockSaleDepartmentDTO> unlockSaleDepartmentDTOS = unlockSaleDepartmentApi.getUnlockSaleDepartmentByRuleIds(ids);
        Map<Long, UnlockSaleDepartmentDTO> unlockSaleDepartmentDTOMap = unlockSaleDepartmentDTOS.stream().collect(Collectors.toMap(UnlockSaleDepartmentDTO::getRuleId, Function.identity()));
        List<UserDTO> userDTOList = ListUtil.empty();
        if (CollUtil.isNotEmpty(updateUserIds)) {
            userDTOList = userApi.listByIds(updateUserIds);
        }
        Map<Long, String> userMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        page.getRecords().stream().forEach(e -> {
            e.setUnlockSaleDepartment(PojoUtils.map(unlockSaleDepartmentDTOMap.get(e.getId()), UnlockSaleDepartmentVO.class));
            e.setUnlockSaleCustomerRange(PojoUtils.map(unlockSaleCustomerRangeDTOMap.get(e.getId()), UnlockSaleCustomerRangeVO.class));
            e.setUpdateUserName(userMap.get(e.getUpdateUser()));
        });
        return Result.success(page);
    }

    @ApiOperation(value = "新增或者修改主信息")
    @PostMapping("/saveOrUpdate")
    public Result<Long> saveOrUpdate(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveOrUpdateUnlockSaleRuleForm form) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getBasisStatus();
        if (flowMonthWashControlDTO == null) {
            return Result.failed("日程还没有开启或者已经结束");
        }
        if (form.getId() != null && form.getId() != 0) {
            if (form.getSource() != null && form.getSource() == 1) {
                if (form.getBusinessRange() == null || form.getBusinessRange() == 0) {
                    return Result.failed("商业公司范围必选");
                }
                if (form.getBusinessRange() != null && form.getBusinessRange() == 2) {
                    List<Long> longList = unlockSaleBusinessApi.getCrmEnterpriseIdByRuleId(form.getId());
                    if (CollUtil.isEmpty(longList)) {
                        return Result.failed("指定商业公司设置为0");
                    }
                }
                if (form.getCustomerRange() == null || form.getCustomerRange() == 0) {
                    return Result.failed("客户公司范围必选");
                }
                if (form.getCustomerRange() != null && form.getCustomerRange() == 2) {
                    List<Long> longList = unlockSaleCustomerApi.getCrmEnterpriseIdByRuleId(form.getId());
                    if (CollUtil.isEmpty(longList)) {
                        return Result.failed("指定客户公司设置为0");
                    }
                }
                if (form.getCustomerRange() != null && form.getCustomerRange() == 3) {
                    if (form.getUnlockSaleCustomerRange() == null) {
                        return Result.failed("指定范围必选");
                    }
                    UnlockSaleCustomerRangeDTO unlockSaleCustomerRangeDTO = unlockSaleCustomerRangeApi.getUnlockSaleCustomerRangeByRuleId(form.getId());
                    List<String> allRelationList = null;
                    if (unlockSaleCustomerRangeDTO != null) {
                        allRelationList = unlockSaleCustomerRangeProvinceApi.getProvinceListByUscId(unlockSaleCustomerRangeDTO.getId());
                    }
                    if (StrUtil.isEmpty(form.getUnlockSaleCustomerRange().getKeywords()) && CollUtil.isEmpty(form.getUnlockSaleCustomerRange().getClassificationIds()) && CollUtil.isEmpty(allRelationList)) {
                        return Result.failed("指定范围三个子项必须至少设置一项");
                    }
                }
                if (form.getGoodsRange() == null || form.getGoodsRange() == 0) {
                    return Result.failed("商品范围必选");
                }
                if (form.getGoodsRange() != null && form.getGoodsRange() == 2) {
                    List<Long> goodsCodeIds = unlockSaleGoodsCategoryApi.getCrmGoodsCodeByRuleId(form.getId());
                    if (CollUtil.isEmpty(goodsCodeIds)) {
                        return Result.failed("指定品种设置为0");
                    }
                }
                if (form.getGoodsRange() != null && form.getGoodsRange() == 4) {
                    List<Long> goodsCodeIds = unlockSaleGoodsApi.getCrmGoodsCodeByRuleId(form.getId());
                    if (CollUtil.isEmpty(goodsCodeIds)) {
                        return Result.failed("指定品规设置为0");
                    }
                }
                if (form.getSaleRange() == null || form.getSaleRange() == 0) {
                    return Result.failed("销量计入规则必选");
                }
                if (form.getSaleRange() != null && (form.getSaleRange() == 1 || form.getSaleRange() == 2)) {
                    if (form.getUnlockSaleDepartment() == null) {
                        return Result.failed("销量计入部门必填");
                    }
                    if (form.getUnlockSaleDepartment().getType() == null || form.getUnlockSaleDepartment().getType() == 0) {
                        return Result.failed("销量计入部门必填");
                    }
                    if (form.getUnlockSaleDepartment().getType() == 1) {
                        if (form.getUnlockSaleDepartment().getCrmBusinessDepartmentCode() == null || form.getUnlockSaleDepartment().getCrmBusinessDepartmentCode() == 0 || StrUtil.isEmpty(form.getUnlockSaleDepartment().getBusinessDepartmentName())) {
                            return Result.failed("业务部门必须带出部门ID");
                        }
                        if (form.getUnlockSaleDepartment().getCrmDepartmentCode() == null || form.getUnlockSaleDepartment().getCrmDepartmentCode() == 0 || StrUtil.isEmpty(form.getUnlockSaleDepartment().getDepartmentName())) {
                            return Result.failed("部门必须带出部门ID");
                        }
                    }
                    if (form.getUnlockSaleDepartment().getType() == 2) {
                        if (StrUtil.isEmpty(form.getUnlockSaleDepartment().getBusinessDepartmentName())) {
                            return Result.failed("业务部门必填");
                        }
                        if (StrUtil.isEmpty(form.getUnlockSaleDepartment().getDepartmentName())) {
                            return Result.failed("部门必填");
                        }
                        if (form.getSaleRange() == 2 && (form.getUnlockSaleDepartment().getSaleIncludedRange() == null || form.getUnlockSaleDepartment().getSaleIncludedRange() == 0)) {
                            return Result.failed("销量计入省区、业务省区必选");
                        }
                    }
                }
            } else if (form.getSource() != null && form.getSource() == 2) {
                form.setBusinessRange(0);
                form.setCustomerRange(0);
                form.setSaleRange(0);
                form.setGoodsRange(0);
            } else if (form.getSource() != null && form.getSource() == 3) {
                form.setBusinessRange(0);
                form.setCustomerRange(0);
                form.setSaleRange(0);
                form.setGoodsRange(0);
            } else if (form.getSource() != null && form.getSource() == 4) {
                form.setBusinessRange(0);
                form.setCustomerRange(0);
                form.setSaleRange(0);
                form.setGoodsRange(0);
            }
        }

        SaveOrUpdateUnlockSaleRuleRequest request = PojoUtils.map(form, SaveOrUpdateUnlockSaleRuleRequest.class);
        if (form.getUnlockSaleCustomerRange() != null) {
            String keywords = form.getUnlockSaleCustomerRange().getKeywords();
            if (StrUtil.isNotEmpty(keywords)) {
                form.getUnlockSaleCustomerRange().setKeywords(keywords.replaceAll("，", ","));
            }
            request.setSaveUnlockSaleCustomerRangeRequest(PojoUtils.map(form.getUnlockSaleCustomerRange(), SaveUnlockSaleCustomerRangeRequest.class));
        }
        if (form.getUnlockSaleDepartment() != null) {
            request.setSaveUnlockSaleDepartmentRequest(PojoUtils.map(form.getUnlockSaleDepartment(), SaveUnlockSaleDepartmentRequest.class));
        }
        request.setOpUserId(userInfo.getCurrentUserId());
        if (form.getId() == null || form.getId() == 0) {
            request.setCode(unlockSaleRuleApi.generateCode());
        }
        Long id = unlockSaleRuleApi.saveOrUpdate(request);
        return Result.success(id);
    }

    @ApiOperation(value = "删除规则信息")
    @GetMapping("/delete")
    public Result<Boolean> delete(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam @ApiParam(value = "ruleId", required = true) Long ruleId) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getBasisStatus();
        if (flowMonthWashControlDTO == null) {
            return Result.failed("日程还没有开启或者已经结束");
        }
        UnlockSaleRuleDTO unlockSaleRuleDTO = unlockSaleRuleApi.getById(ruleId);
        if (unlockSaleRuleDTO.getIsSystem() == 1) {
            return Result.failed("系统内置规则不能删除");
        }
        DeleteUnlockSaleRuleRequest request = new DeleteUnlockSaleRuleRequest();
        request.setId(ruleId);
        request.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(unlockSaleRuleApi.delete(request));
    }

    @ApiOperation(value = "查询规则信息")
    @GetMapping("/get")
    public Result<UnlockSaleRuleVO> get(@RequestParam @ApiParam(value = "ruleId", required = true) Long ruleId) {
        UnlockSaleRuleDTO unlockSaleRuleDTO = unlockSaleRuleApi.getById(ruleId);
        UnlockSaleRuleVO unlockSaleRuleVO = PojoUtils.map(unlockSaleRuleDTO, UnlockSaleRuleVO.class);
        if (unlockSaleRuleVO == null) {
            return Result.success(null);
        }
        unlockSaleRuleVO.setBusinessNumber(unlockSaleBusinessApi.getCrmEnterpriseIdByRuleId(ruleId).size());
        unlockSaleRuleVO.setCustomerNumber(unlockSaleCustomerApi.getCrmEnterpriseIdByRuleId(ruleId).size());
        unlockSaleRuleVO.setGoodsCategoryNumber(unlockSaleGoodsCategoryApi.getCrmGoodsCodeByRuleId(ruleId).size());
        unlockSaleRuleVO.setGoodsNumber(unlockSaleGoodsApi.getCrmGoodsCodeByRuleId(ruleId).size());
        UnlockSaleCustomerRangeDTO unlockSaleCustomerRangeDTO = unlockSaleCustomerRangeApi.getUnlockSaleCustomerRangeByRuleId(ruleId);
        if (unlockSaleCustomerRangeDTO != null) {
            UnlockSaleCustomerRangeVO unlockSaleCustomerRangeVO = new UnlockSaleCustomerRangeVO();
            unlockSaleCustomerRangeVO.setKeywords(unlockSaleCustomerRangeDTO.getKeyword());
            List<Integer> classificationIds = new ArrayList<>();
            if (StrUtil.isNotEmpty(unlockSaleCustomerRangeDTO.getClassification())) {
                for (String classification : unlockSaleCustomerRangeDTO.getClassification().split(",")) {
                    classificationIds.add(Integer.parseInt(classification));
                }
            }
            unlockSaleCustomerRangeVO.setClassificationIds(classificationIds);
            unlockSaleCustomerRangeVO.setProvinceNumber(unlockSaleCustomerRangeProvinceApi.getProvinceListByUscId(unlockSaleCustomerRangeDTO.getId()).size());
            unlockSaleRuleVO.setUnlockSaleCustomerRange(unlockSaleCustomerRangeVO);
        }
        UnlockSaleDepartmentDTO unlockSaleDepartmentDTO = unlockSaleDepartmentApi.getUnlockSaleDepartmentByRuleId(ruleId);
        if (unlockSaleDepartmentDTO != null) {
            unlockSaleRuleVO.setUnlockSaleDepartment(PojoUtils.map(unlockSaleDepartmentDTO, UnlockSaleDepartmentVO.class));
        }
        return Result.success(unlockSaleRuleVO);
    }

    @ApiOperation(value = "新增商业公司")
    @PostMapping("/addBusiness")
    public Result<Boolean> addBusiness(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveUnlockSaleBusinessForm form) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getBasisStatus();
        if (flowMonthWashControlDTO == null) {
            return Result.failed("日程还没有开启或者已经结束");
        }
        List<Long> crmEnterpriseIds = unlockSaleBusinessApi.getCrmEnterpriseIdByRuleId(form.getRuleId());
        if (form.getCrmEnterpriseIds().size() == 1) {
            List<Long> intersection = crmEnterpriseIds.stream().filter(item -> form.getCrmEnterpriseIds().contains(item)).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(intersection)) {
                return Result.failed("添加的商业信息存在重复的情况");
            }
        } else {
            form.getCrmEnterpriseIds().removeAll(crmEnterpriseIds);
            if (CollUtil.isEmpty(form.getCrmEnterpriseIds())) {
                return Result.success(true);
            }
        }
        SaveUnlockSaleBusinessRequest request = PojoUtils.map(form, SaveUnlockSaleBusinessRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(unlockSaleBusinessApi.save(request));
    }

    @ApiOperation(value = "删除商业公司")
    @PostMapping("/deleteBusiness")
    public Result<Boolean> deleteBusiness(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody DeleteUnlockSaleBusinessForm form) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getBasisStatus();
        if (flowMonthWashControlDTO == null) {
            return Result.failed("日程还没有开启或者已经结束");
        }
        DeleteUnlockSaleBusinessRequest request = PojoUtils.map(form, DeleteUnlockSaleBusinessRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(unlockSaleBusinessApi.delete(request));
    }

    @ApiOperation(value = "已添加商业公司列表")
    @PostMapping("/businessList")
    public Result<Page<CrmEnterpriseBusinessRuleVO>> businessList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryCrmEnterpriseUnlockSaleFlowPageListForm form) {
        QueryCrmEnterpriseUnlockSalePageListRequest request = PojoUtils.map(form, QueryCrmEnterpriseUnlockSalePageListRequest.class);
        return Result.success(PojoUtils.map(unlockSaleBusinessApi.page(request), CrmEnterpriseBusinessRuleVO.class));
    }

    @ApiOperation("机构信息分页列表")
    @PostMapping("/getCrmEnterpriseList")
    public Result<Page<CrmEnterpriseUnlockSaleFlowVO>> getCrmEnterpriseList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryCrmEnterpriseUnlockSaleFlowPageListForm form) {
        QueryCrmEnterpriseByNamePageListRequest request = PojoUtils.map(form, QueryCrmEnterpriseByNamePageListRequest.class);
        Page<CrmEnterprisePartBO> boPage = crmEnterpriseApi.getCrmEnterprisePartInfoByName(request);
        Page<CrmEnterpriseUnlockSaleFlowVO> voPage = PojoUtils.map(boPage, CrmEnterpriseUnlockSaleFlowVO.class);
        if (form.getBusinessRuleId() != null && form.getBusinessRuleId() != 0) {
            List<Long> crmEnterpriseIds = unlockSaleBusinessApi.getCrmEnterpriseIdByRuleId(form.getBusinessRuleId());
            voPage.getRecords().forEach(e -> {
                if (crmEnterpriseIds.contains(e.getId())) {
                    EnterpriseDisableVO enterpriseDisableVO = new EnterpriseDisableVO();
                    enterpriseDisableVO.setUnlockBusinessDisable(true);
                    enterpriseDisableVO.setUnlockBusinessDesc("已被占用");
                    e.setEnterpriseDisableVO(enterpriseDisableVO);
                }
            });
        }

        if (form.getCustomerRuleId() != null && form.getCustomerRuleId() != 0) {
            List<Long> crmEnterpriseIds = unlockSaleCustomerApi.getCrmEnterpriseIdByRuleId(form.getBusinessRuleId());
            voPage.getRecords().forEach(e -> {
                if (crmEnterpriseIds.contains(e.getId())) {
                    EnterpriseDisableVO enterpriseDisableVO = new EnterpriseDisableVO();
                    enterpriseDisableVO.setUnlockCustomerDisable(true);
                    enterpriseDisableVO.setUnlockCustomerDesc("已被占用");
                    e.setEnterpriseDisableVO(enterpriseDisableVO);
                }
            });
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "新增客户公司")
    @PostMapping("/addCustomer")
    public Result<Boolean> addCustomer(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveUnlockSaleCustomerForm form) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getBasisStatus();
        if (flowMonthWashControlDTO == null) {
            return Result.failed("日程还没有开启或者已经结束");
        }
        List<Long> crmEnterpriseIds = unlockSaleCustomerApi.getCrmEnterpriseIdByRuleId(form.getRuleId());
        if (form.getCrmEnterpriseIds().size() == 1) {
            List<Long> intersection = crmEnterpriseIds.stream().filter(item -> form.getCrmEnterpriseIds().contains(item)).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(intersection)) {
                return Result.failed("添加的客户信息存在重复的情况");
            }
        } else {
            form.getCrmEnterpriseIds().removeAll(crmEnterpriseIds);
            if (CollUtil.isEmpty(form.getCrmEnterpriseIds())) {
                return Result.success(true);
            }
        }
        SaveUnlockSaleCustomerRequest request = PojoUtils.map(form, SaveUnlockSaleCustomerRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(unlockSaleCustomerApi.save(request));
    }

    @ApiOperation(value = "删除客户公司")
    @PostMapping("/deleteCustomer")
    public Result<Boolean> deleteCustomer(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody DeleteUnlockSaleCustomerForm form) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getBasisStatus();
        if (flowMonthWashControlDTO == null) {
            return Result.failed("日程还没有开启或者已经结束");
        }
        DeleteUnlockSaleCustomerRequest request = PojoUtils.map(form, DeleteUnlockSaleCustomerRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(unlockSaleCustomerApi.delete(request));
    }

    @ApiOperation(value = "已添加客户公司列表")
    @PostMapping("/customerList")
    public Result<Page<CrmEnterpriseBusinessRuleVO>> customerList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryCrmEnterpriseUnlockSaleFlowPageListForm form) {
        QueryCrmEnterpriseUnlockSalePageListRequest request = PojoUtils.map(form, QueryCrmEnterpriseUnlockSalePageListRequest.class);
        return Result.success(PojoUtils.map(unlockSaleCustomerApi.page(request), CrmEnterpriseBusinessRuleVO.class));
    }

    @ApiOperation(value = "查看客户范围类型-客户区域")
    @GetMapping("/customerRange")
    public Result<LocationUnlockCustomerVO> customerRange(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam @ApiParam(value = "ruleId", required = true) Long ruleId) {
        //  获取省市区树
        List<LocationTreeDTO> locationTreeDTOList = locationApi.listTreeByParentCode("", 2);
        List<LocationUnlockCustomerTreeVO> locationEmpTreeVOList = PojoUtils.map(locationTreeDTOList, LocationUnlockCustomerTreeVO.class);

        LocationUnlockCustomerVO locationUnlockCustomerVO = new LocationUnlockCustomerVO();
        UnlockSaleCustomerRangeDTO unlockSaleCustomerRangeDTO = unlockSaleCustomerRangeApi.getUnlockSaleCustomerRangeByRuleId(ruleId);
        if (unlockSaleCustomerRangeDTO != null) {
            //  获取该规则下所有区域信息
            List<String> allRelationList = unlockSaleCustomerRangeProvinceApi.getProvinceListByUscId(unlockSaleCustomerRangeDTO.getId());
            locationUnlockCustomerVO.setCheckCodeList(allRelationList);
//            setLocationEmpTree(locationEmpTreeVOList, allRelationList);
        }
        locationUnlockCustomerVO.setTreeInfoList(locationEmpTreeVOList);
        return Result.success(locationUnlockCustomerVO);
    }

    @ApiOperation(value = "保存客户范围类型-客户区域")
    @PostMapping("/saveCustomerRangeProvince")
    public Result<Boolean> saveCustomerRangeProvince(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveCustomerRangeProvinceForm form) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getBasisStatus();
        if (flowMonthWashControlDTO == null) {
            return Result.failed("日程还没有开启或者已经结束");
        }
        SaveUnlockSaleCustomerRangeProvinceRequest request = PojoUtils.map(form, SaveUnlockSaleCustomerRangeProvinceRequest.class);
        UnlockSaleCustomerRangeDTO unlockSaleCustomerRangeDTO = unlockSaleCustomerRangeApi.getUnlockSaleCustomerRangeByRuleId(request.getRuleId());
        if (unlockSaleCustomerRangeDTO != null) {
            request.setUscId(unlockSaleCustomerRangeDTO.getId());
        }
        request.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(unlockSaleCustomerRangeProvinceApi.save(request));
    }

    @ApiOperation(value = "新增品规")
    @PostMapping("/addGoods")
    public Result<Boolean> addGoods(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveUnlockSaleGoodsForm form) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getBasisStatus();
        if (flowMonthWashControlDTO == null) {
            return Result.failed("日程还没有开启或者已经结束");
        }
        List<Long> goodsCodeIds = unlockSaleGoodsApi.getCrmGoodsCodeByRuleId(form.getRuleId());
        if (form.getGoodsCodes().size() == 1) {
            List<Long> intersection = goodsCodeIds.stream().filter(item -> form.getGoodsCodes().contains(item)).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(intersection)) {
                return Result.failed("添加的品规信息存在重复的情况");
            }
        } else {
            form.getGoodsCodes().removeAll(goodsCodeIds);
            if (CollUtil.isEmpty(form.getGoodsCodes())) {
                return Result.success(true);
            }
        }
        SaveUnlockSaleGoodsRequest request = PojoUtils.map(form, SaveUnlockSaleGoodsRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(unlockSaleGoodsApi.save(request));
    }

    @ApiOperation(value = "删除品规")
    @PostMapping("/deleteGoods")
    public Result<Boolean> deleteGoods(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody DeleteUnlockSaleGoodsForm form) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getBasisStatus();
        if (flowMonthWashControlDTO == null) {
            return Result.failed("日程还没有开启或者已经结束");
        }
        DeleteUnlockSaleGoodsRequest request = PojoUtils.map(form, DeleteUnlockSaleGoodsRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(unlockSaleGoodsApi.delete(request));
    }

    @ApiOperation(value = "已添加品规列表")
    @PostMapping("/goodsList")
    public Result<Page<UnlockSaleGoodsVO>> goodsList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody UnlockSaleGoodsPageForm form) {
        QueryCrmGoodsInfoPageRequest request = PojoUtils.map(form, QueryCrmGoodsInfoPageRequest.class);
        return Result.success(PojoUtils.map(unlockSaleGoodsApi.page(request), UnlockSaleGoodsVO.class));
    }


    @ApiOperation(value = "分页查询商品", httpMethod = "POST")
    @PostMapping("/goodsPage")
    public Result<Page<UnlockSaleGoodsVO>> goodsPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody UnlockSaleGoodsPageForm form) {
        QueryCrmGoodsInfoPageRequest request = PojoUtils.map(form, QueryCrmGoodsInfoPageRequest.class);
        Page<CrmGoodsInfoDTO> page = crmGoodsInfoApi.getPage(request);
        Page<UnlockSaleGoodsVO> voPage = PojoUtils.map(page, UnlockSaleGoodsVO.class);
        List<Long> goodsIds = null;
        if (form.getRuleId() != null && form.getRuleId() != 0) {
            goodsIds = unlockSaleGoodsApi.getCrmGoodsCodeByRuleId(form.getRuleId());
        }
        List<Long> finalGoodsIds = goodsIds;
        voPage.getRecords().stream().forEach(e -> {
            if (CollUtil.isNotEmpty(finalGoodsIds) && finalGoodsIds.contains(e.getGoodsCode())) {
                e.setDisable(true);
                e.setDisableDesc("被占用");
            }
        });
        return Result.success(voPage);
    }

    @ApiOperation(value = "新增商品标签")
    @PostMapping("/addGoodsTag")
    public Result<Boolean> addGoodsTag(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveUnlockSaleGoodsTagForm form) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getBasisStatus();
        if (flowMonthWashControlDTO == null) {
            return Result.failed("日程还没有开启或者已经结束");
        }
        SaveUnlockSaleGoodsTagRequest request = PojoUtils.map(form, SaveUnlockSaleGoodsTagRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(unlockSaleGoodsTagApi.save(request));
    }

    @ApiOperation(value = "删除商品标签")
    @PostMapping("/deleteGoodsTag")
    public Result<Boolean> deleteGoodsTag(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody DeleteUnlockSaleGoodsTagForm form) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getBasisStatus();
        if (flowMonthWashControlDTO == null) {
            return Result.failed("日程还没有开启或者已经结束");
        }
        DeleteUnlockSaleGoodsTagRequest request = PojoUtils.map(form, DeleteUnlockSaleGoodsTagRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(unlockSaleGoodsTagApi.delete(request));
    }

    @ApiOperation(value = "已添加商品标签列表")
    @PostMapping("/goodsTagList")
    public Result<Page<UnlockSaleGoodsTagVO>> goodsTagList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody UnlockSaleGoodsTagPageForm form) {
        QueryCrmGoodsTagPageRequest request = PojoUtils.map(form, QueryCrmGoodsTagPageRequest.class);
        return Result.success(PojoUtils.map(unlockSaleGoodsTagApi.page(request), UnlockSaleGoodsTagVO.class));
    }


    @ApiOperation(value = "分页查询商品标签", httpMethod = "POST")
    @PostMapping("/goodsTagPage")
    public Result<Page<UnlockSaleGoodsTagVO>> queryTagPage(@CurrentUser CurrentSjmsUserInfo userinfo, @RequestBody UnlockSaleGoodsTagPageForm form) {
        QueryCrmGoodsTagPageRequest request = PojoUtils.map(form, QueryCrmGoodsTagPageRequest.class);
        Page<CrmGoodsTagDTO> page = crmGoodsTagApi.queryTagPage(request);
        Page<UnlockSaleGoodsTagVO> voPage = PojoUtils.map(page, UnlockSaleGoodsTagVO.class);
        if (CollectionUtil.isNotEmpty(voPage.getRecords())) {
            List<Long> tagIds = voPage.getRecords().stream().map(CrmGoodsTagVO::getId).collect(Collectors.toList());
            Map<Long, Long> tagGoodsMap = crmGoodsTagApi.countTagGoods(tagIds);
            voPage.getRecords().forEach(tagVo -> {
                Long goodsCount = tagGoodsMap.getOrDefault(tagVo.getId(), 0L);
                tagVo.setGoodsCount(goodsCount);
            });
        }
        List<Long> goodsIds = null;
        if (form.getRuleId() != null && form.getRuleId() != 0) {
            goodsIds = unlockSaleGoodsTagApi.getCrmGoodsCodeByRuleId(form.getRuleId());
        }
        List<Long> finalGoodsIds = goodsIds;
        voPage.getRecords().stream().forEach(e -> {
            if (finalGoodsIds.contains(e.getId())) {
                e.setDisable(true);
                e.setDisableDesc("被占用");
            }
        });

        return Result.success(voPage);
    }

    @ApiOperation(value = "新增商品品种")
    @PostMapping("/addGoodsCategory")
    public Result<Boolean> addGoodsCategory(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveUnlockSaleGoodsCategoryForm form) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getBasisStatus();
        if (flowMonthWashControlDTO == null) {
            return Result.failed("日程还没有开启或者已经结束");
        }
        List<Long> goodsCodeIds = unlockSaleGoodsCategoryApi.getCrmGoodsCodeByRuleId(form.getRuleId());
        if (form.getGoodsCategoryIds().size() == 1) {
            List<Long> intersection = goodsCodeIds.stream().filter(item -> form.getGoodsCategoryIds().contains(item)).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(intersection)) {
                return Result.failed("添加的品种信息存在重复的情况");
            }
        } else {
            form.getGoodsCategoryIds().removeAll(goodsCodeIds);
            if (CollUtil.isEmpty(form.getGoodsCategoryIds())) {
                return Result.success(true);
            }
        }
        SaveUnlockSaleGoodsCategoryRequest request = PojoUtils.map(form, SaveUnlockSaleGoodsCategoryRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(unlockSaleGoodsCategoryApi.save(request));
    }

    @ApiOperation(value = "删除商品品种")
    @PostMapping("/deleteGoodsCategory")
    public Result<Boolean> deleteGoodsCategory(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody DeleteUnlockSaleGoodsCategoryForm form) {
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getBasisStatus();
        if (flowMonthWashControlDTO == null) {
            return Result.failed("日程还没有开启或者已经结束");
        }
        DeleteUnlockSaleGoodsCategoryRequest request = PojoUtils.map(form, DeleteUnlockSaleGoodsCategoryRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(unlockSaleGoodsCategoryApi.delete(request));
    }

    @ApiOperation(value = "已添加商品品种列表")
    @PostMapping("/goodsCategoryList")
    public Result<Page<UnlockSaleGoodsCategoryVO>> goodsCategoryList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody UnlockSaleGoodsCategoryForm form) {
        QueryUnlockSaleGoodsCategoryPageRequest request = PojoUtils.map(form, QueryUnlockSaleGoodsCategoryPageRequest.class);
        Page<UnlockSaleGoodsCategoryBO> page = unlockSaleGoodsCategoryApi.page(request);
        if (CollectionUtil.isNotEmpty(page.getRecords())) {
            List<Long> categoryIdList = page.getRecords().stream().map(UnlockSaleGoodsCategoryBO::getCategoryId).collect(Collectors.toList());
            Map<Long, Long> goodsCountMap = crmGoodsCategoryApi.getGoodsCountByCategory(categoryIdList);
            for (UnlockSaleGoodsCategoryBO unlockSaleGoodsCategoryBO : page.getRecords()) {
                List<Long> ids = new ArrayList<>();
                ids.add(unlockSaleGoodsCategoryBO.getCategoryId());
                getNumberByCategory(unlockSaleGoodsCategoryBO.getCategoryId(), ids);
                Long idNumber = 0L;
                for (Long id : ids) {
                    idNumber = idNumber + goodsCountMap.getOrDefault(id, 0L);
                }
                unlockSaleGoodsCategoryBO.setGoodsCount(idNumber);
            }
        }
        return Result.success(PojoUtils.map(page, UnlockSaleGoodsCategoryVO.class));
    }


    @ApiOperation(value = "分页查询商品品种", httpMethod = "POST")
    @PostMapping("/goodsCategoryPage")
    public Result<List<UnlockSaleGoodsCategoryVO>> queryCategoryPage(@CurrentUser CurrentSjmsUserInfo userinfo, @RequestBody UnlockSaleGoodsCategoryForm form) {
        QueryCrmGoodsCategoryRequest request = PojoUtils.map(form, QueryCrmGoodsCategoryRequest.class);
        List<CrmGoodsCategoryDTO> list = crmGoodsCategoryApi.queryCategoryList(request);
        List<UnlockSaleGoodsCategoryVO> firstVOList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list)) {
            List<Long> idList = list.stream().map(CrmGoodsCategoryDTO::getId).collect(Collectors.toList());
            Map<Long, Long> goodsCountMap = crmGoodsCategoryApi.getGoodsCountByCategory(idList);
            List<CrmGoodsCategoryDTO> firstList = list.stream().filter(e -> e.getCategoryLevel() == 1).collect(Collectors.toList());
            for (CrmGoodsCategoryDTO crmGoodsCategoryDTO : firstList) {
                UnlockSaleGoodsCategoryVO unlockSaleGoodsCategoryVO = PojoUtils.map(crmGoodsCategoryDTO, UnlockSaleGoodsCategoryVO.class);
                List<Long> ids = new ArrayList<>();
                ids.add(crmGoodsCategoryDTO.getId());
                getNumberByCategory(crmGoodsCategoryDTO.getId(), ids);
                Long idNumber = 0L;
                for (Long id : ids) {
                    idNumber = idNumber + goodsCountMap.getOrDefault(id, 0L);
                }
                unlockSaleGoodsCategoryVO.setGoodsCount(idNumber);
                firstVOList.add(unlockSaleGoodsCategoryVO);
            }

            List<Long> categoryIds = null;
            if (form.getRuleId() != null && form.getRuleId() != 0) {
                categoryIds = unlockSaleGoodsCategoryApi.getCrmGoodsCodeByRuleId(form.getRuleId());
            }
            List<Long> finalGoodsIds = categoryIds;
            firstVOList.stream().forEach(e -> {
                if (CollUtil.isNotEmpty(finalGoodsIds) && finalGoodsIds.contains(e.getId())) {
                    e.setDisable(true);
                    e.setDisableDesc("被占用");
                }
            });
        }
        return Result.success(firstVOList);
    }

    private void getNumberByCategory(Long id, List<Long> ids) {
        //获取子类
        List<CrmGoodsCategoryDTO> crmGoodsCategoryDTOList = crmGoodsCategoryApi.findByParentId(id);
        if (CollUtil.isNotEmpty(crmGoodsCategoryDTOList)) {
            ids.addAll(crmGoodsCategoryDTOList.stream().map(e -> e.getId()).collect(Collectors.toList()));
            for (CrmGoodsCategoryDTO crmGoodsCategoryDTO : crmGoodsCategoryDTOList) {
                getNumberByCategory(crmGoodsCategoryDTO.getId(), ids);
            }
        }
    }
}
