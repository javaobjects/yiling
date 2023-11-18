package com.yiling.sjms.agency.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.yiling.dataflow.crm.dto.request.UpdateManorNumRequest;
import com.yiling.framework.common.log.Log;
import com.yiling.sjms.agency.form.CheckManorDataForm;
import com.yiling.sjms.agency.vo.CrmManorSimplePageListVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationManorApi;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.api.CrmManorApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationManorDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.CrmManorDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseRelationManorPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorParamRequest;
import com.yiling.dataflow.crm.dto.request.RemoveManorRelationRequest;
import com.yiling.dataflow.crm.dto.request.RemoveManorRequest;
import com.yiling.dataflow.crm.dto.request.SaveCrmManorInfoRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateManorRelationRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.form.QueryCrmManorEnterprisePageListForm;
import com.yiling.sjms.agency.form.QueryManorPageForm;
import com.yiling.sjms.agency.form.SaveCrmManorInfoForm;
import com.yiling.sjms.agency.form.SaveOrUpdateManorRelation;
import com.yiling.sjms.agency.vo.CrmEnterpriseRelationManorVO;
import com.yiling.sjms.agency.vo.CrmManorPageListVO;
import com.yiling.sjms.agency.vo.CrmManorSimpleVO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import sun.swing.StringUIClientPropertyKey;

/**
 * 辖区管理
 */
@Slf4j
@Api(tags = "辖区管理接口")
@RestController
@RequestMapping("/crm/agency/manor")
public class CrmManorController extends BaseController {
    @DubboReference
    private CrmManorApi crmManorApi;
    @DubboReference
    private CrmEnterpriseRelationManorApi crmEnterpriseRelationManorApi;
    @DubboReference
    private CrmEnterpriseApi crmEnterpriseApi;
    @DubboReference
    private CrmGoodsCategoryApi crmGoodsCategoryApi;
    @DubboReference
    UserApi userApi;

    @ApiOperation(value = "列表查询")
    @PostMapping("/pageList")
    public Result<Page<CrmManorPageListVO>> pageList(@RequestBody QueryManorPageForm form) {
        QueryCrmManorPageRequest request = new QueryCrmManorPageRequest();
        PojoUtils.map(form, request);
        Page<CrmManorPageListVO> pageListVOPage = PojoUtils.map(crmManorApi.pageList(request), CrmManorPageListVO.class);
        if (CollUtil.isEmpty(pageListVOPage.getRecords())) {
            return Result.success(pageListVOPage);
        }
        List<Long> userIds = pageListVOPage.getRecords().stream().map(CrmManorPageListVO::getUpdateUser).collect(Collectors.toList());
        List<UserDTO> userDTOS = CollUtil.isNotEmpty(userIds) ? userApi.listByIds(userIds) : ListUtil.empty();
        //处理操作人名称处理
        Map<Long, UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        pageListVOPage.getRecords().forEach(item -> {
            UserDTO crmHospitalDTO = userDTOMap.get(item.getUpdateUser());
            if (ObjectUtil.isNotEmpty(crmHospitalDTO)) {
                item.setUpdateUserName(crmHospitalDTO.getName());
            }
        });
        return Result.success(pageListVOPage);
    }
    @ApiOperation(value = "名称搜索列表查询")
    @PostMapping("/name/pageList")
    public Result<Page<CrmManorSimplePageListVO>> pageByNameList(@RequestBody QueryManorPageForm form) {
        QueryCrmManorPageRequest request = new QueryCrmManorPageRequest();
        PojoUtils.map(form, request);
        Page<CrmManorSimplePageListVO> pageListVOPage = PojoUtils.map(crmManorApi.pageList(request), CrmManorSimplePageListVO.class);
        return Result.success(pageListVOPage);
    }
    @ApiOperation("新增/编辑")
    @PostMapping("/save")
    public Result<?> save(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Validated SaveCrmManorInfoForm form) {

        QueryCrmManorParamRequest checkNameParamRequest = new QueryCrmManorParamRequest();
        checkNameParamRequest.setName(form.getName());
        // 检查名称是否重复
        CheckManorDataForm checkManorDataForm = new CheckManorDataForm();
        PojoUtils.map(form, checkManorDataForm);
        Result checkResult = checkManorData(checkManorDataForm);
        if (checkResult.getCode() != 200) {
            return checkResult;
        }
        SaveCrmManorInfoRequest request = new SaveCrmManorInfoRequest();
        PojoUtils.map(form, request);
        request.setOpUserId(userInfo.getCurrentUserId());
        Long id = crmManorApi.saveOrUpdate(request);
        return Result.success(id);
    }
    @ApiOperation(value = "删除")
    @GetMapping("/remove")
    public Result<Boolean> remove(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id", required = true) Long id) {
        RemoveManorRequest request = new RemoveManorRequest();
        request.setId(id);
        request.setOpUserId(userInfo.getCurrentUserId());
        int count = crmManorApi.removeById(request);
        //大于0更新成功，小于0失败
        return Result.success(count > 0);
    }

    @ApiOperation(value = "辖区详情")
    @GetMapping("/detail")
    public Result<CrmManorSimpleVO> detail(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id", required = true) Long id) {
        //查询辖区基础信息
        CrmManorDTO crmManorDTO = crmManorApi.getManorById(id);
        CrmManorSimpleVO simpleVO = new CrmManorSimpleVO();
        PojoUtils.map(crmManorDTO, simpleVO);
        return Result.success(simpleVO);
    }
    @ApiOperation(value = "获取辖区机构品种数量")
    @GetMapping("/category/num")
    public Result<Integer> getCategoryNum(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id", required = true) Long id) {
        //查询辖区基础信息
        CrmManorDTO crmManorDTO = crmManorApi.getManorById(id);
        return Result.success(Optional.ofNullable(crmManorDTO.getCategoryNum()).orElse(0));
    }
    @ApiOperation(value = "辖区机构列表")
    @PostMapping("/e/list")
    public Result<Page<CrmEnterpriseRelationManorVO>> pageListEnterprise(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryCrmManorEnterprisePageListForm form) {
        if (Objects.isNull(form.getManorId()) || form.getManorId().longValue() == 0L) {
            return Result.success(new Page<>());
        }
        Page<CrmEnterpriseRelationManorVO> voPage = new Page<>();
        //查询辖区基础信息
        CrmManorDTO crmManorDTO = crmManorApi.getManorById(form.getManorId());
        if (Objects.nonNull(crmManorDTO.getId())) {
            //查询关系表中数据
            QueryCrmEnterpriseRelationManorPageRequest request = new QueryCrmEnterpriseRelationManorPageRequest();
            PojoUtils.map(form, request);
            Page<CrmEnterpriseRelationManorDTO> crmEnterpriseRelationManorDTOPage = crmEnterpriseRelationManorApi.pageListByManorId(request);
            voPage = PojoUtils.map(crmEnterpriseRelationManorDTOPage, CrmEnterpriseRelationManorVO.class);
            List<Long> crmEnIds = Optional.ofNullable(crmEnterpriseRelationManorDTOPage.getRecords().stream().map(CrmEnterpriseRelationManorDTO::getCrmEnterpriseId).collect(Collectors.toList())).orElse(ListUtil.empty());
            List<Long> categoryIds = Optional.ofNullable(crmEnterpriseRelationManorDTOPage.getRecords().stream().map(CrmEnterpriseRelationManorDTO::getCategoryId).collect(Collectors.toList())).orElse(ListUtil.empty());
            //如果参数是empty列表，返回空List;
            List<CrmEnterpriseDTO> crmEnterpriseDTOS = crmEnterpriseApi.getCrmEnterpriseListById(crmEnIds);
            //如果参数是empty列表，返回空List;
            List<CrmGoodsCategoryDTO> categoryDTOS = crmGoodsCategoryApi.findByIds(categoryIds);
            Map<Long, CrmEnterpriseDTO> crmEnterpriseDTOMap = Optional.ofNullable(crmEnterpriseDTOS.stream().collect(Collectors.toMap(CrmEnterpriseDTO::getId, m -> m, (a, b) -> a))).orElse(Maps.newHashMap());
            Map<Long, CrmGoodsCategoryDTO> categoryDTOMap = Optional.ofNullable(categoryDTOS.stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId, m -> m, (a, b) -> a))).orElse(Maps.newHashMap());
            voPage.getRecords().forEach(item -> {
                item.setCrmEnterpriseName(null);
                item.setCategoryName(null);
                if (Objects.nonNull(crmEnterpriseDTOMap.get(item.getCrmEnterpriseId()))) {
                    CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseDTOMap.get(item.getCrmEnterpriseId());
                    item.setCrmEnterpriseName(crmEnterpriseDTO.getName());
                    item.setCrmEnterpriseCode(crmEnterpriseDTO.getCode());
                }
                if (Objects.nonNull(categoryDTOMap.get(item.getCategoryId()))) {
                    CrmGoodsCategoryDTO crmGoodsCategoryDTO = categoryDTOMap.get(item.getCategoryId());
                    item.setCategoryName(crmGoodsCategoryDTO.getName());
                }
            });
        }
        return Result.success(voPage);
    }
    @ApiOperation(value = "辖区机构添加修改")
    @PostMapping("/e/save")
    public Result<Long> saveManorRelation(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Validated SaveOrUpdateManorRelation form) {

        boolean checkFlag = crmEnterpriseRelationManorApi.checkDuplicate(form.getCrmEnterpriseId(), form.getCategoryId(), form.getId());
        if (checkFlag) {
            return Result.failed(String.format("机构：%s，品种：%s 已关联其他辖区！", form.getCrmEnterpriseName(), form.getCategoryName()));
        }
        SaveOrUpdateManorRelationRequest request = new SaveOrUpdateManorRelationRequest();
        PojoUtils.map(form, request);
        request.setOpUserId(userInfo.getCurrentUserId());
        Long aLong = crmEnterpriseRelationManorApi.saveOrUpdate(request);
        UpdateManorNumRequest numRequest = new UpdateManorNumRequest();
        numRequest.setCrmManorId(form.getCrmManorId());
        numRequest.setOpUserId(userInfo.getCurrentUserId());
        crmManorApi.updateNum(numRequest);
        return Result.success(aLong);
    }
    @ApiOperation(value = "辖区机构删除")
    @GetMapping("/e/remove")
    public Result<Boolean> removeManorRelation(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id", required = true) Long id) {
        CrmEnterpriseRelationManorDTO relationManorDTO = crmEnterpriseRelationManorApi.getById(id);
        //删除逻辑
        RemoveManorRelationRequest request = new RemoveManorRelationRequest();
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setId(id);
        crmEnterpriseRelationManorApi.removeById(request);
        //更新辖区机构数量
        UpdateManorNumRequest numRequest = new UpdateManorNumRequest();
        numRequest.setCrmManorId(relationManorDTO.getCrmManorId());
        numRequest.setOpUserId(userInfo.getCurrentUserId());
        crmManorApi.updateNum(numRequest);
        return Result.success(true);
    }

    @ApiOperation(value = "辖区机构检查重复")
    @PostMapping("/check")
    public Result<Boolean> checkManorData(@RequestBody CheckManorDataForm form) {
        if (StringUtils.isBlank(form.getManorNo()) && StringUtils.isBlank(form.getName())) {
            return Result.failed("辖区名称或辖区编码不能同时为空");
        }
        if (Objects.isNull(form.getId())) {
            form.setId(0L);
        }
        if (StringUtils.isNotBlank(form.getName())) {
            QueryCrmManorParamRequest checkNameParamRequest = new QueryCrmManorParamRequest();
            checkNameParamRequest.setName(form.getName());
            // 检查名称是否重复
            List<CrmManorDTO> nameList = crmManorApi.listByParam(checkNameParamRequest);
            List<Long> ids = Optional.ofNullable(nameList.stream().filter(e -> form.getId().longValue() != e.getId().longValue()).map(CrmManorDTO::getId).collect(Collectors.toList())).orElse(ListUtil.empty());
            //新增
            if (ids.size() > 0) {
                return Result.failed("辖区名称已存在，请重新输入！");
            }
        }
        if (StringUtils.isNotBlank(form.getManorNo())) {
            QueryCrmManorParamRequest checkNoParamRequest = new QueryCrmManorParamRequest();
            checkNoParamRequest.setManorNo(form.getManorNo());
            List<CrmManorDTO> noList = crmManorApi.listByParam(checkNoParamRequest);
            List<Long> ids = Optional.ofNullable(noList.stream().filter(e -> form.getId().longValue() != e.getId().longValue()).map(CrmManorDTO::getId).collect(Collectors.toList())).orElse(ListUtil.empty());
            if (ids.size() > 0) {
                return Result.failed("辖区编码已存在，请重新输入！");
            }
        }
        return Result.success(true);
    }

    public static void main(String[] args) {
        List<CrmManorDTO> nameList = new ArrayList<>();
        CrmManorDTO crmManorDTO = new CrmManorDTO();
        crmManorDTO.setId(1L);
        nameList.add(crmManorDTO);
        List<Long> ss = nameList.stream().filter(e -> e.getId().longValue() != 0L).map(CrmManorDTO::getId).collect(Collectors.toList());
        System.out.println(ss.size());
    }
}
