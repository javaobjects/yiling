package com.yiling.sjms.agency.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationPinchRunnerApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.api.CrmGoodsGroupApi;
import com.yiling.dataflow.crm.api.CrmManorApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationPinchRunnerDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationPostDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.dto.CrmManorDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseRelationPinchRunnerPageListRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorParamRequest;
import com.yiling.dataflow.crm.dto.request.RemoveCrmEnterpriseRelationPinchRunnerRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmEnterpriseRelationPinchRunnerRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.form.QueryCrmEnterpriseRelationPinchRunnerPageListForm;
import com.yiling.sjms.agency.form.SaveCrmEnterpriseRelationPinchRunnerForm;
import com.yiling.sjms.agency.form.UpdateCrmEnterpriseRelationPinchRunnerForm;
import com.yiling.sjms.agency.vo.CrmEnterpriseRelationPinchRunnerDetailVO;
import com.yiling.sjms.agency.vo.CrmEnterpriseRelationPinchRunnerShipVO;
import com.yiling.sjms.agency.vo.CrmEnterpriseRelationPinchRunnerVO;
import com.yiling.sjms.agency.vo.EsbEmployVO;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author houjie.sun
 * @date 2023-04-19
 */
@Slf4j
@RestController
@RequestMapping("/crm/agency/relationPinchRunner")
@Api(tags = "代跑三者关系管理")
public class CrmEnterpriseRelationPinchRunnerController extends BaseController {

    @DubboReference
    SjmsUserDatascopeApi sjmsUserDatascopeApi;

    @DubboReference
    UserApi userApi;

    @DubboReference
    CrmEnterpriseRelationPinchRunnerApi crmEnterpriseRelationPinchRunnerApi;

    @DubboReference
    CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;

    @DubboReference
    CrmGoodsGroupApi crmGoodsGroupApi;

    @DubboReference
    EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    BusinessDepartmentApi businessDepartmentApi;
    @DubboReference
    CrmManorApi crmManorApi;
    @DubboReference
    CrmGoodsCategoryApi crmGoodsCategoryApi;


    @ApiOperation(value = "列表查询")
    @PostMapping("/pageList")
    public Result<Page<CrmEnterpriseRelationPinchRunnerVO>> pageList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryCrmEnterpriseRelationPinchRunnerPageListForm form) {
        // 数据权限
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("代跑三者关系管理, 列表, 当前用户数据权限:empId={}, userDatascopeBO={}", userInfo.getCurrentUserCode(), JSONUtil.toJsonStr(userDatascopeBO));
        boolean datascopeFlag = checkUserDatascope(userDatascopeBO);
        if (!datascopeFlag) {
            throw new BusinessException(ResultCode.FORBIDDEN,ResultCode.FORBIDDEN.getMessage());
        }

        // 代跑三者关系列表
        QueryCrmEnterpriseRelationPinchRunnerPageListRequest pinchRunnerPageListRequest = PojoUtils.map(form, QueryCrmEnterpriseRelationPinchRunnerPageListRequest.class);
        pinchRunnerPageListRequest.setUserDatascopeBO(userDatascopeBO);
        Page<CrmEnterpriseRelationPinchRunnerDTO> pinchRunnerPage = crmEnterpriseRelationPinchRunnerApi.page(pinchRunnerPageListRequest);
        if (ObjectUtil.isNull(pinchRunnerPage) || CollUtil.isEmpty(pinchRunnerPage.getRecords())) {
            return Result.success(new Page<>());
        }

        // 操作人：key -> userId， value -> name
        Map<Long, String> userNameMap = getUserNameMap(pinchRunnerPage.getRecords());
        // 机构信息：key -> 机构id， value -> CrmEnterpriseDTO
        Map<Long, CrmEnterpriseDTO> crmEnterpriseMap = new HashMap<>();
        List<Long> crmEnterpriseIdList = pinchRunnerPage.getRecords().stream().filter(o -> ObjectUtil.isNotNull(o.getCrmEnterpriseId()) && o.getCrmEnterpriseId().intValue() > 0).map(CrmEnterpriseRelationPinchRunnerDTO::getCrmEnterpriseId).distinct().collect(Collectors.toList());
        List<CrmEnterpriseDTO> crmEnterpriseList = crmEnterpriseApi.getCrmEnterpriseListById(crmEnterpriseIdList);
        if (CollUtil.isNotEmpty(crmEnterpriseList)) {
            crmEnterpriseMap = crmEnterpriseList.stream().collect(Collectors.toMap(CrmEnterpriseDTO::getId, Function.identity()));
        }
        // 三者关系，key -> 三者关系id，value -> CrmEnterpriseRelationShipDTO
        Map<Long, CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipMap = new HashMap<>();
        List<Long> enterpriseCersIdList = pinchRunnerPage.getRecords().stream().filter(o -> ObjectUtil.isNotNull(o.getEnterpriseCersId()) && o.getEnterpriseCersId().intValue() > 0).map(CrmEnterpriseRelationPinchRunnerDTO::getEnterpriseCersId).distinct().collect(Collectors.toList());
        List<CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipList = crmEnterpriseRelationShipApi.listSuffixByIdList(enterpriseCersIdList, null);
        if (CollUtil.isNotEmpty(crmEnterpriseRelationShipList)) {
            crmEnterpriseRelationShipMap = crmEnterpriseRelationShipList.stream().collect(Collectors.toMap(CrmEnterpriseRelationShipDTO::getId, Function.identity()));
        }
        // 品种
        List<Long> categoryIds = crmEnterpriseRelationShipList.stream().filter(o -> ObjectUtil.isNotNull(o.getCategoryId()) && o.getCategoryId().intValue() > 0).map(CrmEnterpriseRelationShipDTO::getCategoryId).collect(Collectors.toList());
        Map<Long, String> categoryMap = getCategoryMap(categoryIds);
        // 辖区
        List<Long> manorIds = crmEnterpriseRelationShipList.stream().filter(o -> ObjectUtil.isNotNull(o.getManorId()) && o.getManorId().intValue() > 0).map(CrmEnterpriseRelationShipDTO::getManorId).collect(Collectors.toList());
        Map<Long, String> manorMap = getManorMap(manorIds);


        // 产品组：key -> 三者关系id，value -> productGroupName
        Map<Long, String> productGroupMap = getProductGroupMap(crmEnterpriseRelationShipList);
        // 岗位，key -> jobId，value -> EsbEmployeeDTO
        List<Long> superiorPostCodeList = pinchRunnerPage.getRecords().stream().filter(o -> ObjectUtil.isNotNull(o.getBusinessSuperiorPostCode()) && o.getBusinessSuperiorPostCode().intValue() > 0).map(CrmEnterpriseRelationPinchRunnerDTO::getBusinessSuperiorPostCode).distinct().collect(Collectors.toList());
        Map<Long, EsbEmployeeDTO> esbEmployeeMap = getEsbEmployeeMap(superiorPostCodeList, crmEnterpriseRelationShipList);

        Page<CrmEnterpriseRelationPinchRunnerVO> page = new Page<>();
        page.setCurrent(pinchRunnerPage.getCurrent());
        page.setSize(pinchRunnerPage.getSize());
        page.setTotal(pinchRunnerPage.getTotal());
        List<CrmEnterpriseRelationPinchRunnerVO> list = new ArrayList<>();
        CrmEnterpriseRelationPinchRunnerVO vo;
        for (CrmEnterpriseRelationPinchRunnerDTO record : pinchRunnerPage.getRecords()) {
            vo = PojoUtils.map(record, CrmEnterpriseRelationPinchRunnerVO.class);
            vo.setBusinessSuperiorPostCode(record.getBusinessSuperiorPostCode());
            // 机构名称
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseMap.get(record.getCrmEnterpriseId());
            if (ObjectUtil.isNotNull(crmEnterpriseDTO)) {
                vo.setCrmEnterpriseName(crmEnterpriseDTO.getName());
            }
            // 操作人、操作时间
            buildOperateInfo(record, vo, userNameMap);
            // 三者关系信息
            CrmEnterpriseRelationShipDTO relationShipDTO = crmEnterpriseRelationShipMap.get(record.getEnterpriseCersId());
            if (ObjectUtil.isNotNull(relationShipDTO)) {
                // 产品组信息
                String goodsGroupName = productGroupMap.get(relationShipDTO.getProductGroupId());
                vo.setProductGroupName(goodsGroupName);
                // 业务代表主管岗位信息
                Long postCode = relationShipDTO.getPostCode();
                buildBusinessRepresentative(vo, postCode, esbEmployeeMap);

                // 品种
                vo.setCategoryName(Optional.ofNullable(categoryMap.get(relationShipDTO.getCategoryId())).orElse(""));
                // 辖区
                vo.setManorName(Optional.ofNullable(manorMap.get(relationShipDTO.getManorId())).orElse(""));
            }
            // 主管岗位信息
            Long superiorPostCode = record.getBusinessSuperiorPostCode();
            buildBusinessSuperiorInfo(vo, superiorPostCode, esbEmployeeMap);

            list.add(vo);
        }
        page.setRecords(list);
        return Result.success(page);
    }

    private Map<Long, String> getManorMap(List<Long> manorIds) {
        Map<Long, String> manorMap = new HashMap<>();
        if (CollUtil.isEmpty(manorIds)) {
            return manorMap;
        }
        QueryCrmManorParamRequest manorRequest = new QueryCrmManorParamRequest();
        manorRequest.setIdList(manorIds);
        List<CrmManorDTO> manorList =crmManorApi.listByParam(manorRequest);
        if (CollUtil.isNotEmpty(manorList)) {
            manorMap = manorList.stream().collect(Collectors.toMap(CrmManorDTO::getId, o -> o.getName(), (k1, k2) -> k1));
        }
        return manorMap;
    }

    private Map<Long, String> getCategoryMap(List<Long> categoryIds) {
        Map<Long, String> categoryMap = new HashMap<>();
        if (CollUtil.isEmpty(categoryIds)) {
            return categoryMap;
        }
        List<CrmGoodsCategoryDTO> categoryList = crmGoodsCategoryApi.findByIds(categoryIds);
        if (CollUtil.isNotEmpty(categoryList)) {
            categoryMap = categoryList.stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId, o -> o.getName(), (k1, k2) -> k1));
        }
        return categoryMap;
    }

    @ApiOperation(value = "查看")
    @GetMapping("/detail")
    public Result<CrmEnterpriseRelationPinchRunnerDetailVO> detail(@CurrentUser CurrentSjmsUserInfo userInfo, @ApiParam(value = "主键ID", required = true) @RequestParam(value = "id") Long id) {
        // 数据权限
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("代跑三者关系管理, 查看, 当前用户数据权限:empId={}, userDatascopeBO={}", userInfo.getCurrentUserCode(), JSONUtil.toJsonStr(userDatascopeBO));
        boolean datascopeFlag = checkUserDatascope(userDatascopeBO);
        if (!datascopeFlag) {
            throw new BusinessException(ResultCode.FORBIDDEN,ResultCode.FORBIDDEN.getMessage());
        }
//        QueryCrmEnterpriseRelationPinchRunnerRequest request = new QueryCrmEnterpriseRelationPinchRunnerRequest();
//        request.setUserDatascopeBO(userDatascopeBO);
//        request.setId(id);
        CrmEnterpriseRelationPinchRunnerDTO old = crmEnterpriseRelationPinchRunnerApi.getById(userDatascopeBO, id);
        if (ObjectUtil.isNull(old)) {
            throw new BusinessException(ResultCode.FAILED, "此代跑三者关系信息不存在，请确认");
        }

        // 数据权限
        isUserDatascope(userDatascopeBO, old.getCrmEnterpriseId(), old.getCrmProvinceCode());

        CrmEnterpriseRelationPinchRunnerDetailVO vo = PojoUtils.map(old, CrmEnterpriseRelationPinchRunnerDetailVO.class);

        // 机构
        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(vo.getCrmEnterpriseId());
        // 机构名称
        if (ObjectUtil.isNotNull(crmEnterpriseDTO)) {
            vo.setCrmEnterpriseName(crmEnterpriseDTO.getName());
        }

        // 三者关系
        List<CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipList = new ArrayList<>();
        CrmEnterpriseRelationShipDTO crmEnterpriseRelationShipDTO = crmEnterpriseRelationShipApi.queryById(vo.getEnterpriseCersId());
        if (ObjectUtil.isNotNull(crmEnterpriseRelationShipDTO)) {
            crmEnterpriseRelationShipList.add(crmEnterpriseRelationShipDTO);
        }

        // 品种
        List<Long> categoryIds = new ArrayList<>();
        if (ObjectUtil.isNotNull(crmEnterpriseRelationShipDTO) && crmEnterpriseRelationShipDTO.getCategoryId().intValue() > 0) {
            categoryIds.add(crmEnterpriseRelationShipDTO.getCategoryId());
        }
        Map<Long, String> categoryMap = getCategoryMap(categoryIds);
        // 辖区
        List<Long> manorIds = new ArrayList<>();
        if (ObjectUtil.isNotNull(crmEnterpriseRelationShipDTO) && crmEnterpriseRelationShipDTO.getManorId().intValue() > 0) {
            manorIds.add(crmEnterpriseRelationShipDTO.getManorId());
        }
        Map<Long, String> manorMap = getManorMap(manorIds);

        // 产品组：key -> 三者关系id，value -> productGroupName
        Map<Long, String> productGroupMap = getProductGroupMap(crmEnterpriseRelationShipList);
        // 岗位信息，key -> jobId，value -> EsbEmployeeDTO
        List<Long> superiorPostCodeList = ListUtil.toList(old.getBusinessSuperiorPostCode());
        Map<Long, EsbEmployeeDTO> esbEmployeeMap = getEsbEmployeeMap(superiorPostCodeList, crmEnterpriseRelationShipList);
        // 三者关系已维护的代跑
        Map<Long,Long> pinchRunnerMap = new HashMap<>();
        List<CrmEnterpriseRelationPinchRunnerDTO> pinchRunnerList = crmEnterpriseRelationPinchRunnerApi.getByCrmEnterpriseIdAndRelationShipIds(old.getCrmEnterpriseId(), null, null);
        if (CollUtil.isNotEmpty(pinchRunnerList)) {
            pinchRunnerMap = pinchRunnerList.stream().collect(Collectors.toMap(o -> o.getEnterpriseCersId(), o -> o.getId(), (k1, k2) -> k1));
        }

        // 设置三者关系
        List<CrmEnterpriseRelationPinchRunnerShipVO> pinchRunnerShipList = buildDetailCrmEnterpriseRelationShip(id, pinchRunnerMap, crmEnterpriseRelationShipList, productGroupMap, esbEmployeeMap, categoryMap, manorMap);
        vo.setCrmEnterpriseRelationShipList(pinchRunnerShipList);
        // 设置代跑三者关系
        buildDetailPinchRunner(vo, old.getBusinessSuperiorPostCode(), esbEmployeeMap);

        return Result.success(vo);
    }

    @ApiOperation(value = "业务代表工号或者岗位代码获取信息")
    @GetMapping("/getByEmpIdOrJobId")
    public Result<EsbEmployVO> getByEmpIdOrJobId(@RequestParam(value = "empId", required = false) String empId, @RequestParam(value = "jobId", required = false) String jobId) {
        if (StrUtil.isBlank(empId) && StrUtil.isBlank(jobId)) {
            return Result.failed(100030, "您输入的销量计入主管工号不能为空");
        }
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpIdOrJobId(empId, jobId,null);
        if (ObjectUtil.isEmpty(esbEmployeeDTO) && StringUtils.isNotBlank(empId)) {
            return Result.failed(100030, "您输入的工号不存在");
        }
        if (ObjectUtil.isEmpty(esbEmployeeDTO) && StringUtils.isNotBlank(jobId)) {
            return Result.failed(100030, "您输入的岗位代码不存在");
        }
        if (ObjectUtil.isNotEmpty(esbEmployeeDTO) && CompareUtil.compare(esbEmployeeDTO.getJobId(), 0L) == 0) {
            return Result.failed(100030, "您输入的"+(StringUtils.isEmpty(empId)?"岗位代码":"工号")+"不存在");
        }
        //通过循环获取部门。
        EsbEmployVO result = PojoUtils.map(esbEmployeeDTO, EsbEmployVO.class);
        EsbOrganizationDTO organizationDTO = businessDepartmentApi.getByOrgId(esbEmployeeDTO.getDeptId());
        if (ObjectUtil.isNotEmpty(organizationDTO)) {
            result.setDepartment(organizationDTO.getOrgName());
        }
        //通过部门，业务部门，业务省区获取省区
        String provinceArea = crmEnterpriseRelationShipApi.getProvinceAreaByThreeParms(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince());
        result.setProvinceArea(provinceArea);
        // 省区经理，查不到则为空，查到多个则报错提示
        List<String> jobNamesList = ListUtil.toList("省区经理", "省区主管");
        List<EsbEmployeeDTO> provincialManagerList = esbEmployeeApi.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince(), null, jobNamesList);
        if (CollUtil.isNotEmpty(provincialManagerList)) {
            if (provincialManagerList.size() > 1) {
                throw new BusinessException(ResultCode.FAILED, "查询到多个省区经理，请检查数据！");
            }
            EsbEmployeeDTO provincialManager = provincialManagerList.get(0);
            result.setProvincialManagerPostCode(provincialManager.getJobId());
            result.setProvincialManagerPostName(provincialManager.getJobName());
            result.setProvincialManagerCode(provincialManager.getEmpId());
            result.setProvincialManagerName(provincialManager.getEmpName());
        }
        return Result.success(result);
    }

    @ApiOperation(value = "查询三者关系列表")
    @GetMapping("/crmRelatioShipList")
    public Result<List<CrmEnterpriseRelationPinchRunnerShipVO>> crmRelatioShipList(@CurrentUser CurrentSjmsUserInfo userInfo, @ApiParam(value = "机构ID", required = true) @RequestParam(value = "crmEnterpriseId") Long crmEnterpriseId,
                                                                                   @ApiParam(value = "主键ID", required = false) @RequestParam(value = "id", required = false) Long id) {
        // 数据权限
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("代跑三者关系管理, 查看, 当前用户数据权限:empId={}, userDatascopeBO={}", userInfo.getCurrentUserCode(), JSONUtil.toJsonStr(userDatascopeBO));
        boolean datascopeFlag = checkUserDatascope(userDatascopeBO);
        if (!datascopeFlag) {
            throw new BusinessException(ResultCode.FORBIDDEN,ResultCode.FORBIDDEN.getMessage());
        }

        // 机构
        CrmEnterpriseDTO crmEnterprise = crmEnterpriseApi.getCrmEnterpriseById(crmEnterpriseId);
        if (ObjectUtil.isNull(crmEnterprise)) {
            throw new BusinessException(ResultCode.FAILED, "此机构信息不存在，请确认");
        }
        // 数据权限
        isUserDatascope(userDatascopeBO, crmEnterprise.getId(), crmEnterprise.getProvinceCode());

        // 三者关系
        List<CrmEnterpriseRelationPostDTO> enterpriseRelationPost = crmEnterpriseRelationShipApi.getEnterpriseRelationPostByProductGroup(ListUtil.toList(crmEnterpriseId));
        if (ObjectUtil.isNull(enterpriseRelationPost)) {
            return Result.success(ListUtil.empty());
        }
        List<CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipList = PojoUtils.map(enterpriseRelationPost, CrmEnterpriseRelationShipDTO.class);

        // 品种
        List<Long> categoryIds = enterpriseRelationPost.stream().filter(o -> ObjectUtil.isNotNull(o.getCategoryId()) && o.getCategoryId().intValue() > 0).map(CrmEnterpriseRelationPostDTO::getCategoryId).distinct().collect(Collectors.toList());
        Map<Long, String> categoryMap = getCategoryMap(categoryIds);
        // 辖区
        List<Long> manorIds = enterpriseRelationPost.stream().filter(o -> ObjectUtil.isNotNull(o.getManorId()) && o.getManorId().intValue() > 0).map(CrmEnterpriseRelationPostDTO::getManorId).distinct().collect(Collectors.toList());
        Map<Long, String> manorMap = getManorMap(manorIds);

        // 产品组：key -> 三者关系id，value -> productGroupName
        Map<Long, String> productGroupMap = getProductGroupMap(crmEnterpriseRelationShipList);
        // 岗位信息，key -> jobId，value -> EsbEmployeeDTO
        List<Long> superiorPostCodeList = crmEnterpriseRelationShipList.stream().filter(o -> ObjectUtil.isNotNull(o.getPostCode()) && o.getPostCode().intValue() > 0).map(CrmEnterpriseRelationShipDTO::getPostCode).distinct().collect(Collectors.toList());
        Map<Long, EsbEmployeeDTO> esbEmployeeMap = getEsbEmployeeMap(superiorPostCodeList, crmEnterpriseRelationShipList);
        // 三者关系已维护的代跑
        Map<Long,Long> pinchRunnerMap = new HashMap<>();
        List<CrmEnterpriseRelationPinchRunnerDTO> pinchRunnerList = crmEnterpriseRelationPinchRunnerApi.getByCrmEnterpriseIdAndRelationShipIds(crmEnterpriseId, null, null);
        if (CollUtil.isNotEmpty(pinchRunnerList)) {
            pinchRunnerMap = pinchRunnerList.stream().collect(Collectors.toMap(o -> o.getEnterpriseCersId(), o -> o.getId(), (k1, k2) -> k1));
        }

        // 设置三者关系
        List<CrmEnterpriseRelationPinchRunnerShipVO> pinchRunnerShipVOS = buildDetailCrmEnterpriseRelationShip(id, pinchRunnerMap, crmEnterpriseRelationShipList, productGroupMap, esbEmployeeMap, categoryMap, manorMap);
        return Result.success(pinchRunnerShipVOS);
    }


    @ApiOperation(value = "新增")
    @PostMapping("/add")
    public Result<Long> add(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveCrmEnterpriseRelationPinchRunnerForm form) {
        // 数据权限
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("代跑三者关系管理, 新增, 当前用户数据权限:empId={}, userDatascopeBO={}", userInfo.getCurrentUserCode(), JSONUtil.toJsonStr(userDatascopeBO));
        boolean datascopeFlag = checkUserDatascope(userDatascopeBO);
        if (!datascopeFlag) {
            throw new BusinessException(ResultCode.FORBIDDEN,ResultCode.FORBIDDEN.getMessage());
        }
        // 校验机构、三者关系、销量计入主管岗位代码
        CrmEnterpriseDTO crmEnterprise = saveOrUpdateCheck(0L, userDatascopeBO, form.getCrmEnterpriseId(), form.getEnterpriseCersId(), form.getBusinessSuperiorPostCode());

        // 岗位信息，key -> jobId，value -> EsbEmployeeDTO
        List<Long> superiorPostCodeList = ListUtil.toList(form.getBusinessSuperiorPostCode());
        Map<Long, EsbEmployeeDTO> esbEmployeeMap = getEsbEmployeeMap(superiorPostCodeList, null);

        // 三者关系，品种、辖区
        CrmEnterpriseRelationShipDTO crmEnterpriseRelationShip = crmEnterpriseRelationShipApi.queryById(form.getEnterpriseCersId());

        // 保存
        SaveOrUpdateCrmEnterpriseRelationPinchRunnerRequest request = buildAddRequest(userInfo.getCurrentUserId(), null, crmEnterprise, null, form.getEnterpriseCersId(), form.getBusinessSuperiorPostCode(), esbEmployeeMap, crmEnterpriseRelationShip);
        return Result.success(crmEnterpriseRelationPinchRunnerApi.add(request));
    }

    @ApiOperation(value = "编辑")
    @PostMapping("/edit")
    public Result<Boolean> edit(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody UpdateCrmEnterpriseRelationPinchRunnerForm form) {
        // 数据权限
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("代跑三者关系管理, 编辑, 当前用户数据权限:empId={}, userDatascopeBO={}", userInfo.getCurrentUserCode(), JSONUtil.toJsonStr(userDatascopeBO));
        boolean datascopeFlag = checkUserDatascope(userDatascopeBO);
        if (!datascopeFlag) {
            throw new BusinessException(ResultCode.FORBIDDEN,ResultCode.FORBIDDEN.getMessage());
        }

        CrmEnterpriseRelationPinchRunnerDTO old = crmEnterpriseRelationPinchRunnerApi.getById(null, form.getId());
        if (ObjectUtil.isNull(old)) {
            throw new BusinessException(ResultCode.FAILED, "此代跑三者关系信息不存在，请确认");
        }
        // 校验机构、三者关系、销量计入主管岗位代码
        CrmEnterpriseDTO crmEnterprise = saveOrUpdateCheck(old.getId(), userDatascopeBO, form.getCrmEnterpriseId(), form.getEnterpriseCersId(), form.getBusinessSuperiorPostCode());

        // 岗位信息，key -> jobId，value -> EsbEmployeeDTO
        List<Long> superiorPostCodeList = ListUtil.toList(form.getBusinessSuperiorPostCode());
        Map<Long, EsbEmployeeDTO> esbEmployeeMap = getEsbEmployeeMap(superiorPostCodeList, null);

        // 三者关系，品种、辖区
        CrmEnterpriseRelationShipDTO crmEnterpriseRelationShip = crmEnterpriseRelationShipApi.queryById(form.getEnterpriseCersId());

        // 更新
        SaveOrUpdateCrmEnterpriseRelationPinchRunnerRequest request = buildAddRequest(userInfo.getCurrentUserId(), old.getId(), crmEnterprise, old.getEnterpriseCersId(), form.getEnterpriseCersId(), form.getBusinessSuperiorPostCode(), esbEmployeeMap, crmEnterpriseRelationShip);
        return Result.success(crmEnterpriseRelationPinchRunnerApi.edit(request));
    }

    @ApiOperation(value = "删除")
    @GetMapping("/remove")
    public Result<Boolean> remove(@CurrentUser CurrentSjmsUserInfo userInfo, @ApiParam(value = "主键ID", required = true) @RequestParam(value = "id") Long id) {
        // 数据权限
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("代跑三者关系管理, 编辑, 当前用户数据权限:empId={}, userDatascopeBO={}", userInfo.getCurrentUserCode(), JSONUtil.toJsonStr(userDatascopeBO));
        boolean datascopeFlag = checkUserDatascope(userDatascopeBO);
        if (!datascopeFlag) {
            throw new BusinessException(ResultCode.FORBIDDEN,ResultCode.FORBIDDEN.getMessage());
        }

        CrmEnterpriseRelationPinchRunnerDTO old = crmEnterpriseRelationPinchRunnerApi.getById(null, id);
        if (ObjectUtil.isNull(old)) {
            throw new BusinessException(ResultCode.FAILED, "此代跑三者关系信息不存在，请确认");
        }
        // 数据权限
        isUserDatascope(userDatascopeBO, old.getCrmEnterpriseId(), old.getCrmProvinceCode());

        // 删除
        RemoveCrmEnterpriseRelationPinchRunnerRequest removeRequest = new RemoveCrmEnterpriseRelationPinchRunnerRequest();
        removeRequest.setId(id);
        removeRequest.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(crmEnterpriseRelationPinchRunnerApi.remove(removeRequest));
    }

    private SaveOrUpdateCrmEnterpriseRelationPinchRunnerRequest buildAddRequest(Long currentUserId, Long id, CrmEnterpriseDTO crmEnterprise, Long oldEnterpriseCersId,
                                                                                Long enterpriseCersId, Long superiorPostCode, Map<Long, EsbEmployeeDTO> esbEmployeeMap,
                                                                                CrmEnterpriseRelationShipDTO crmEnterpriseRelationShip) {
        SaveOrUpdateCrmEnterpriseRelationPinchRunnerRequest request = new SaveOrUpdateCrmEnterpriseRelationPinchRunnerRequest();
        request.setId(id);
        request.setCrmEnterpriseId(crmEnterprise.getId());
        request.setCrmEnterpriseName(crmEnterprise.getName());
        request.setCrmSupplyChainRole(crmEnterprise.getSupplyChainRole());
        request.setCrmProvinceCode(crmEnterprise.getProvinceCode());
        request.setCrmProvinceName(crmEnterprise.getProvinceName());
        request.setCrmCityCode(crmEnterprise.getCityCode());
        request.setCrmCityName(crmEnterprise.getCityName());
        request.setCrmRegionCode(crmEnterprise.getRegionCode());
        request.setCrmRegionName(crmEnterprise.getRegionName());
        request.setEnterpriseCersId(enterpriseCersId);
        // 品种、辖区，不保存，取三者关系的进行展示
//        if (ObjectUtil.isNotNull(crmEnterpriseRelationShip)) {
//            request.setManorId(crmEnterpriseRelationShip.getManorId());
//            request.setCategoryId(crmEnterpriseRelationShip.getCategoryId());
//        }
        // 修改前的三者关系ID
        if (ObjectUtil.isNotNull(oldEnterpriseCersId)){
            request.setOldEnterpriseCersId(oldEnterpriseCersId);
        }
        // 销量计入主管岗位信息
        request.setBusinessSuperiorPostCode(superiorPostCode);
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeMap.get(superiorPostCode);
        request.setBusinessSuperiorPostName(esbEmployeeDTO.getJobName());
        request.setBusinessSuperiorCode(esbEmployeeDTO.getEmpId());
        request.setBusinessSuperiorName(esbEmployeeDTO.getEmpName());
        request.setBusinessSuperiorDepartment(esbEmployeeDTO.getYxDept());
        request.setBusinessSuperiorProvince(esbEmployeeDTO.getYxProvince());
        request.setBusinessSuperiorArea(esbEmployeeDTO.getYxArea());
        // 部门，通过部门id获取
        EsbOrganizationDTO organizationDTO = businessDepartmentApi.getByOrgId(esbEmployeeDTO.getDeptId());
        if (ObjectUtil.isNotEmpty(organizationDTO)) {
            request.setSuperiorDepartment(organizationDTO.getOrgName());
        }
        // 省区，通过业务部门名称、业务省区名称获取
        String provinceArea = crmEnterpriseRelationShipApi.getProvinceAreaByThreeParms(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince());
        request.setSuperiorProvincial(provinceArea);
        // 省区经理，查不到则为空，查到多个则报错提示
        List<String> jobNamesList = ListUtil.toList("省区经理", "省区主管");
        List<EsbEmployeeDTO> provincialManagerList = esbEmployeeApi.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince(), null, jobNamesList);
        if (CollUtil.isNotEmpty(provincialManagerList)) {
            if (provincialManagerList.size() > 1) {
                throw new BusinessException(ResultCode.FAILED, "查询到多个省区经理，请检查数据！");
            }
            EsbEmployeeDTO provincialManager = provincialManagerList.get(0);
            request.setProvincialManagerPostCode(provincialManager.getJobId());
            request.setProvincialManagerPostName(provincialManager.getJobName());
            request.setProvincialManagerCode(provincialManager.getEmpId());
            request.setProvincialManagerName(provincialManager.getEmpName());
        }
        request.setOpUserId(currentUserId);
        return request;
    }

    private CrmEnterpriseDTO saveOrUpdateCheck(Long oldId, SjmsUserDatascopeBO userDatascopeBO, Long crmEnterpriseId, Long enterpriseCersId, Long superiorPostCode) {
        // 校验机构
        CrmEnterpriseDTO crmEnterprise = crmEnterpriseApi.getCrmEnterpriseById(crmEnterpriseId);
        if (ObjectUtil.isNull(crmEnterprise)) {
            throw new BusinessException(ResultCode.FAILED, "此机构信息不存在，请确认");
        }
        // 数据权限
        isUserDatascope(userDatascopeBO, crmEnterpriseId, crmEnterprise.getProvinceCode());

        // 校验三者关系
        CrmEnterpriseRelationShipDTO crmEnterpriseRelationShipDTO = crmEnterpriseRelationShipApi.queryById(enterpriseCersId);
        if (ObjectUtil.isNull(crmEnterpriseRelationShipDTO)) {
            throw new BusinessException(ResultCode.FAILED, "此三者关系信息不存在，请确认");
        }
        CrmEnterpriseRelationPinchRunnerDTO old = crmEnterpriseRelationPinchRunnerApi.getByCrmEnterpriseIdAndCrmRelationShipId(crmEnterpriseId, enterpriseCersId);
        if (ObjectUtil.isNotNull(old) && !old.getId().equals(oldId)) {
            throw new BusinessException(ResultCode.FAILED, "此三者关系已经维护代跑三者关系，代跑三者关系id:" + old.getId() +"，请选择其他三者关系");
        }
        // 校验销量计入主管岗位代码
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpIdOrJobId(null, superiorPostCode.toString(),null);
        if (ObjectUtil.isNull(esbEmployeeDTO)) {
            throw new BusinessException(ResultCode.FAILED, "销量计入主管岗位代码对应的人员信息不存在，请确认");
        }
        return crmEnterprise;
    }


    private void buildDetailPinchRunner(CrmEnterpriseRelationPinchRunnerDetailVO vo, Long superiorPostCode, Map<Long, EsbEmployeeDTO> esbEmployeeMap){
        if (ObjectUtil.isNull(superiorPostCode) || 0 == superiorPostCode.intValue()) {
            return;
        }
        // 销量计入主管岗位代码
        vo.setBusinessSuperiorPostCode(superiorPostCode);
        // 销量计入主管信息
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeMap.get(superiorPostCode);
        if (ObjectUtil.isNull(esbEmployeeDTO)) {
            return;
        }
        // 销量计入主管岗位名称、销量计入主管工号、销量计入主管姓名、量计入业务部门、销量计入业务省区
        vo.setBusinessSuperiorPostName(esbEmployeeDTO.getJobName());
        vo.setBusinessSuperiorCode(esbEmployeeDTO.getEmpId());
        vo.setBusinessSuperiorName(esbEmployeeDTO.getEmpName());
        vo.setBusinessSuperiorDepartment(esbEmployeeDTO.getYxDept());
        vo.setBusinessSuperiorProvince(esbEmployeeDTO.getYxProvince());
        vo.setBusinessSuperiorArea(esbEmployeeDTO.getYxArea());
        // 部门，通过部门id获取
        EsbOrganizationDTO organizationDTO = businessDepartmentApi.getByOrgId(esbEmployeeDTO.getDeptId());
        if (ObjectUtil.isNotEmpty(organizationDTO)) {
            vo.setSuperiorDepartment(organizationDTO.getOrgName());
        }
        // 省区，通过业务部门名称、业务省区名称获取
        String provinceArea = crmEnterpriseRelationShipApi.getProvinceAreaByThreeParms(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince());
        vo.setSuperiorProvincial(provinceArea);
        // 省区经理，查不到则为空，查到多个则报错提示
        List<String> jobNamesList = ListUtil.toList("省区经理", "省区主管");
        List<EsbEmployeeDTO> provincialManagerList = esbEmployeeApi.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince(), null, jobNamesList);
        if (CollUtil.isNotEmpty(provincialManagerList)) {
            if (provincialManagerList.size() > 1) {
                throw new BusinessException(ResultCode.FAILED, "查询到多个省区经理，请检查数据！");
            }
            EsbEmployeeDTO provincialManager = provincialManagerList.get(0);
            vo.setProvincialManagerPostCode(provincialManager.getJobId());
            vo.setProvincialManagerPostName(provincialManager.getJobName());
            vo.setProvincialManagerCode(provincialManager.getEmpId());
            vo.setProvincialManagerName(provincialManager.getEmpName());
        }
    }

    private List<CrmEnterpriseRelationPinchRunnerShipVO> buildDetailCrmEnterpriseRelationShip(Long currentId, Map<Long,Long> pinchRunnerMap,
                                                                                              List<CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipList,
                                                                                              Map<Long, String> productGroupMap, Map<Long, EsbEmployeeDTO> esbEmployeeMap,
                                                                                              Map<Long, String> categoryMap, Map<Long, String> manorMap) {
        if (CollUtil.isEmpty(crmEnterpriseRelationShipList)) {
            return ListUtil.empty();
        }
        List<CrmEnterpriseRelationPinchRunnerShipVO> crmEnterpriseRelationShips = new ArrayList<>();
        CrmEnterpriseRelationPinchRunnerShipVO pinchRunnerShipVO;
        for (CrmEnterpriseRelationShipDTO crmEnterpriseRelationShip : crmEnterpriseRelationShipList) {
            pinchRunnerShipVO = new CrmEnterpriseRelationPinchRunnerShipVO();
            pinchRunnerShipVO.setEnterpriseCersId(crmEnterpriseRelationShip.getId());
            // 产品组
            if (MapUtil.isNotEmpty(productGroupMap)) {
                String productGroupName = productGroupMap.get(crmEnterpriseRelationShip.getProductGroupId());
                pinchRunnerShipVO.setProductGroupId(crmEnterpriseRelationShip.getProductGroupId());
                pinchRunnerShipVO.setProductGroupName(productGroupName);
            }

            // 品种
            pinchRunnerShipVO.setCategoryId(crmEnterpriseRelationShip.getCategoryId());
            pinchRunnerShipVO.setCategoryName(Optional.ofNullable(categoryMap.get(crmEnterpriseRelationShip.getCategoryId())).orElse(""));
            // 辖区
            pinchRunnerShipVO.setManorId(crmEnterpriseRelationShip.getManorId());
            pinchRunnerShipVO.setManorName(Optional.ofNullable(manorMap.get(crmEnterpriseRelationShip.getManorId())).orElse(""));

            // 业务代表岗位信息
            Long representativePostCode = crmEnterpriseRelationShip.getPostCode();
            pinchRunnerShipVO.setRepresentativePostCode(representativePostCode);

            EsbEmployeeDTO esbEmployeeDTO = esbEmployeeMap.get(representativePostCode);
            if (ObjectUtil.isNotNull(esbEmployeeDTO)) {
                pinchRunnerShipVO.setRepresentativePostName(esbEmployeeDTO.getJobName());
                pinchRunnerShipVO.setRepresentativeCode(esbEmployeeDTO.getEmpId());
                pinchRunnerShipVO.setRepresentativeName(esbEmployeeDTO.getEmpName());
                pinchRunnerShipVO.setRepresentativeDepartment(esbEmployeeDTO.getYxDept());
                pinchRunnerShipVO.setRepresentativeProvinc(esbEmployeeDTO.getYxProvince());
                pinchRunnerShipVO.setRepresentativeArea(esbEmployeeDTO.getYxArea());
            }
            // 标识是否已维护代跑三者关系
            Long pinchRunnerId = pinchRunnerMap.get(crmEnterpriseRelationShip.getId());
            // 是否已被当前代跑三者关系选择关联
            if (ObjectUtil.isNotNull(pinchRunnerId) && pinchRunnerId.intValue() > 0 ) {
                if (ObjectUtil.isNotNull(currentId) && currentId.intValue() > 0) {
                    if (ObjectUtil.equal(currentId, pinchRunnerId)) {
                        // 已被当前代跑三者关系选择关联
                        pinchRunnerShipVO.setCurrentSelectedFlag(true);
                    } else {
                        // 已被其他代跑三者关系选择关联
                        pinchRunnerShipVO.setOtherSelectedFlag(true);
                    }
                } else {
                    // 已被其他代跑三者关系选择关联
                    pinchRunnerShipVO.setOtherSelectedFlag(true);
                }
            }
            crmEnterpriseRelationShips.add(pinchRunnerShipVO);
        }
        // 设置列表顺序
        List<CrmEnterpriseRelationPinchRunnerShipVO> list = new LinkedList<>();
        List<CrmEnterpriseRelationPinchRunnerShipVO> currentSelectedList = new ArrayList<>();
        List<CrmEnterpriseRelationPinchRunnerShipVO> otherSelectedList = new ArrayList<>();
        List<CrmEnterpriseRelationPinchRunnerShipVO> notSelectedList = new ArrayList<>();
        for (CrmEnterpriseRelationPinchRunnerShipVO vo : crmEnterpriseRelationShips) {
            if (vo.getCurrentSelectedFlag()) {
                currentSelectedList.add(vo);
            } else if (vo.getOtherSelectedFlag()){
                otherSelectedList.add(vo);
            } else {
                notSelectedList.add(vo);
            }
        }
        if (CollUtil.isNotEmpty(currentSelectedList)) {
            list.addAll(currentSelectedList);
        }
        if (CollUtil.isNotEmpty(otherSelectedList)) {
            list.addAll(otherSelectedList);
        }
        if (CollUtil.isNotEmpty(notSelectedList)) {
            list.addAll(notSelectedList);
        }
        return list;
    }

    private void buildBusinessRepresentative(CrmEnterpriseRelationPinchRunnerVO vo, Long postCode, Map<Long, EsbEmployeeDTO> esbEmployeeMap) {
        if (ObjectUtil.isNull(postCode) || 0 == postCode.intValue()) {
            return;
        }
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeMap.get(postCode);
        if (ObjectUtil.isNull(esbEmployeeDTO)) {
            return;
        }
        // 业务代表工号、业务代表姓名、业务代表岗位代码、业务代表岗位名称
        vo.setRepresentativeCode(esbEmployeeDTO.getEmpId());
        vo.setRepresentativeName(esbEmployeeDTO.getEmpName());
        vo.setRepresentativePostCode(postCode);
        vo.setRepresentativePostName(esbEmployeeDTO.getJobName());
        // 部门
        //                        EsbOrganizationDTO organizationDTO = businessDepartmentApi.getByOrgId(esbEmployeeDTO.getDeptId());
        //                        if (ObjectUtil.isNotEmpty(organizationDTO)) {
        //                            vo.setRepresentativeDepartment(organizationDTO.getOrgName());
        //                        }
        //                        // 通过业务部门名称、业务省区名称，获取省区
        //                        String provinceArea = crmEnterpriseRelationShipApi.getProvinceAreaByThreeParms(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince());
        //                        vo.setRepresentativeProvincialArea(provinceArea);
    }

    private void buildBusinessSuperiorInfo(CrmEnterpriseRelationPinchRunnerVO vo, Long superiorPostCode, Map<Long, EsbEmployeeDTO> esbEmployeeMap) {
        if (ObjectUtil.isNull(superiorPostCode) || 0 == superiorPostCode.intValue()) {
            return;
        }
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeMap.get(superiorPostCode);
        if (ObjectUtil.isNull(esbEmployeeDTO)) {
            return;
        }
        // 销量计入主管岗位名称、销量计入主管工号、销量计入主管姓名、量计入业务部门、销量计入业务省区
        vo.setBusinessSuperiorPostName(esbEmployeeDTO.getJobName());
        vo.setBusinessSuperiorCode(esbEmployeeDTO.getEmpId());
        vo.setBusinessSuperiorName(esbEmployeeDTO.getEmpName());
        vo.setBusinessSuperiorDepartment(esbEmployeeDTO.getYxDept());
        vo.setBusinessSuperiorProvince(esbEmployeeDTO.getYxProvince());
        // 销量计入主管部门
        //                    EsbOrganizationDTO organizationDTO = businessDepartmentApi.getByOrgId(esbEmployeeDTO.getDeptId());
        //                    if (ObjectUtil.isNotEmpty(organizationDTO)) {
        //                        vo.setSuperiorDepartment(organizationDTO.getOrgName());
        //                    }
        // 通过业务部门名称、业务省区名称，销量计入主管获取省区
        //                    String provinceArea = crmEnterpriseRelationShipApi.getProvinceAreaByThreeParms(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince());
        //                    vo.setSuperiorProvincialArea(provinceArea);
    }

    private Map<Long, EsbEmployeeDTO> getEsbEmployeeMap(List<Long> superiorPostCodeList, List<CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipList) {
        Map<Long, EsbEmployeeDTO> esbEmployeeMap = new HashMap<>();
        if (CollUtil.isEmpty(superiorPostCodeList) && CollUtil.isEmpty(crmEnterpriseRelationShipList)) {
            return esbEmployeeMap;
        }
        List<Long> jobIds = new ArrayList<>();
        // 销量计入主管岗位代码
        if (CollUtil.isNotEmpty(superiorPostCodeList)) {
            jobIds.addAll(superiorPostCodeList);
        }
        // 业务代表岗位代码
        if (CollUtil.isNotEmpty(crmEnterpriseRelationShipList)) {
            List<Long> postCodeList = crmEnterpriseRelationShipList.stream().filter(o -> ObjectUtil.isNotNull(o.getPostCode()) && o.getPostCode().intValue() > 0).map(CrmEnterpriseRelationShipDTO::getPostCode).distinct().collect(Collectors.toList());
            jobIds.addAll(postCodeList);
        }

        if (CollUtil.isEmpty(jobIds)) {
            return esbEmployeeMap;
        }
        List<EsbEmployeeDTO> esbEmployeeDTOS = esbEmployeeApi.listByJobIds(jobIds);
        if (CollUtil.isNotEmpty(esbEmployeeDTOS)) {
            esbEmployeeMap = esbEmployeeDTOS.stream().collect(Collectors.toMap(o -> o.getJobId(), o -> o, (k1, k2) -> k1));
        }
        return esbEmployeeMap;
    }

    private void isUserDatascope(SjmsUserDatascopeBO userDatascopeBO, Long crmEnterpriseId, String crmProvinceCode) {
        if (OrgDatascopeEnum.PORTION.equals(OrgDatascopeEnum.getFromCode(userDatascopeBO.getOrgDatascope()))) {
            SjmsUserDatascopeBO.OrgPartDatascopeBO orgPartDatascopeBO = userDatascopeBO.getOrgPartDatascopeBO();
            List<Long> crmEids = orgPartDatascopeBO.getCrmEids();
            List<String> provinceCodes = orgPartDatascopeBO.getProvinceCodes();
            if (!crmEids.contains(crmEnterpriseId) && !provinceCodes.contains(crmProvinceCode)) {
                throw new BusinessException(ResultCode.FORBIDDEN,ResultCode.FORBIDDEN.getMessage());
            }
        }
    }

    private boolean checkUserDatascope(SjmsUserDatascopeBO userDatascopeBO){
        // 数据权限
        if (ObjectUtil.isNull(userDatascopeBO) || ObjectUtil.isNull(userDatascopeBO.getOrgDatascope()) || OrgDatascopeEnum.NONE.equals(OrgDatascopeEnum.getFromCode(userDatascopeBO.getOrgDatascope()))) {
            return false;
        }
        if (OrgDatascopeEnum.PORTION.equals(OrgDatascopeEnum.getFromCode(userDatascopeBO.getOrgDatascope()))) {
            SjmsUserDatascopeBO.OrgPartDatascopeBO orgPartDatascopeBO = userDatascopeBO.getOrgPartDatascopeBO();
            List<Long> crmEids = orgPartDatascopeBO.getCrmEids();
            List<String> provinceCodes = orgPartDatascopeBO.getProvinceCodes();
            if (CollUtil.isEmpty(crmEids) && CollUtil.isEmpty(provinceCodes)) {
                return false;
            }
        }
        return true;
    }


    private Map<Long, String> getProductGroupMap(List<CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipList) {
        Map<Long, String> productGroupMap = new HashMap<>();
        if (CollUtil.isEmpty(crmEnterpriseRelationShipList)) {
            return productGroupMap;
        }
        List<Long> productGroupIdList = crmEnterpriseRelationShipList.stream().filter(o -> ObjectUtil.isNotNull(o.getProductGroupId()) && o.getProductGroupId() > 0).map(CrmEnterpriseRelationShipDTO::getProductGroupId).collect(Collectors.toList());
        if (CollUtil.isEmpty(productGroupIdList)) {
            return productGroupMap;
        }
        // 产品组
        List<CrmGoodsGroupDTO> productGroupList = crmGoodsGroupApi.findGroupByIds(productGroupIdList);
        if (CollUtil.isNotEmpty(productGroupList)) {
            Map<Long, CrmGoodsGroupDTO> productGroupMapTemp = productGroupList.stream().collect(Collectors.toMap(CrmGoodsGroupDTO::getId, Function.identity()));
            crmEnterpriseRelationShipList.forEach(o -> {
                CrmGoodsGroupDTO crmGoodsGroupDTO = productGroupMapTemp.get(o.getProductGroupId());
                if (ObjectUtil.isNotNull(crmGoodsGroupDTO)) {
                    productGroupMap.put(crmGoodsGroupDTO.getId(), crmGoodsGroupDTO.getName());
                }
            });
        }
        return productGroupMap;
    }

    private void buildOperateInfo(CrmEnterpriseRelationPinchRunnerDTO record, CrmEnterpriseRelationPinchRunnerVO vo, Map<Long, String> userNameMap) {
        // 操作人
        Long createUser = record.getCreateUser();
        Long updateUser = record.getUpdateUser();
        Long opUser;
        if (ObjectUtil.isNull(updateUser) || 0 == updateUser.intValue()) {
            opUser = createUser;
        } else {
            opUser = updateUser;
        }
        String name = userNameMap.get(opUser);
        vo.setOpUser(name);

        // 操作时间
        Date createTime = record.getCreateTime();
        Date updateTime = record.getUpdateTime();
        Date opTime;
        if (ObjectUtil.isNull(updateTime) || ObjectUtil.equal("1970-01-01 00:00:00", DateUtil.format(updateTime, "yyyy-MM-dd HH:mm:ss"))) {
            opTime = createTime;
        } else {
            opTime = updateTime;
        }
        vo.setOpTime(opTime);
    }

    private Map<Long, String> getUserNameMap(List<CrmEnterpriseRelationPinchRunnerDTO> list) {
        Map<Long, String> userNameMap = new HashMap<>();
        Set<Long> opUserIdSet = new HashSet<>();
        List<Long> createUserIdList = list.stream().filter(o -> ObjectUtil.isNotNull(o.getCreateUser()) && o.getCreateUser() > 0).map(CrmEnterpriseRelationPinchRunnerDTO::getCreateUser).distinct().collect(Collectors.toList());
        List<Long> updateUserIdList = list.stream().filter(o -> ObjectUtil.isNotNull(o.getUpdateUser()) && o.getUpdateUser() > 0).map(CrmEnterpriseRelationPinchRunnerDTO::getUpdateUser).distinct().collect(Collectors.toList());
        opUserIdSet.addAll(createUserIdList);
        if (CollUtil.isNotEmpty(updateUserIdList)) {
            opUserIdSet.addAll(updateUserIdList);
        }
        if (CollUtil.isEmpty(opUserIdSet)) {
            return userNameMap;
        }
        List<UserDTO> userList = userApi.listByIds(ListUtil.toList(opUserIdSet));
        if (CollUtil.isNotEmpty(userList)) {
            userNameMap = userList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o.getName(), (k1, k2) -> k1));
        }
        return userNameMap;
    }


}
