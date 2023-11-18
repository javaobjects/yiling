package com.yiling.sjms.agency.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.api.CrmHosptialApi;
import com.yiling.dataflow.agency.api.CrmPharmacyApi;
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.agency.dto.CrmHospitalDTO;
import com.yiling.dataflow.agency.dto.CrmPharmacyDTO;
import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import com.yiling.dataflow.agency.enums.AgencySupplyChainRoleEnum;
import com.yiling.dataflow.agency.enums.CrmBusinessCodeEnum;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.api.CrmGoodsGroupApi;
import com.yiling.dataflow.crm.dto.CrmDepartmentAreaRelationDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.api.AgencyLockFormApi;
import com.yiling.sjms.agency.api.AgencyLockRelationShipApi;
import com.yiling.sjms.agency.dto.AgencyLockFormDTO;
import com.yiling.sjms.agency.dto.AgencyLockRelationShipDTO;
import com.yiling.sjms.agency.dto.request.QueryAgencyLockDetailPageRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyLockRequest;
import com.yiling.sjms.agency.dto.request.UpdateAgencyLockArchiveRequest;
import com.yiling.sjms.agency.enums.AgencyLocTypeEnum;
import com.yiling.sjms.agency.enums.AgencyLockFormDataTypeEnum;
import com.yiling.sjms.agency.form.QueryAgencyLockDetailPageForm;
import com.yiling.sjms.agency.form.QueryExtendForm;
import com.yiling.sjms.agency.form.SubmitAgencyExtendChangeForm;
import com.yiling.sjms.agency.form.SubmitAgencyLockForm;
import com.yiling.sjms.agency.form.UpdateArchiveStatusForm;
import com.yiling.sjms.agency.vo.AgencyLockDetailPageListItemVO;
import com.yiling.sjms.agency.vo.LockAgencyExtendVO;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.sjms.workflow.dto.request.SubmitFormBaseRequest;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import static com.yiling.sjms.agency.controller.CrmHospitalController.changeField;

/**
 * @author: dexi.yao
 * @date: 2023/2/23
 */
@Slf4j
@RestController
@RequestMapping("/agency/lock")
@Api(tags = "机构锁定")
public class AgencyLockController extends BaseController {

    //销售代表
    private String dutyGradeIdConstant = "1";

    @DubboReference
    AgencyLockFormApi agencyLockFormApi;
    @DubboReference
    CrmEnterpriseApi crmEnterpriseApi;
    @DubboReference
    CrmSupplierApi crmSupplierApi;
    @DubboReference
    CrmPharmacyApi crmPharmacyApi;
    @DubboReference
    CrmHosptialApi crmHosptialApi;
    @DubboReference
    CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;
    @DubboReference
    AgencyLockRelationShipApi agencyLockRelationShipApi;
    @DubboReference
    FormApi formApi;
    @DubboReference
    EsbEmployeeApi esbEmployeeApi;
    @DubboReference
    BusinessDepartmentApi businessDepartmentApi;
    @DubboReference
    CrmGoodsGroupApi crmGoodsGroupApi;

    /**
     * 根据机构id查询机构拓展信息及三者信息
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "根据机构id查询机构拓展信息及三者信息--用于机构锁定新增")
    @GetMapping("/queryAgencyExtendInfo")
    public Result<LockAgencyExtendVO> queryAgencyExtendInfo(@Valid QueryExtendForm form) {
        LockAgencyExtendVO agencyExtendVO = new LockAgencyExtendVO();
        Integer dataType= AgencyLockFormDataTypeEnum.LOCK_INPUT.getCode();
        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(form.getCrmEnterpriseId());
        if (ObjectUtil.isNull(crmEnterpriseDTO)) {
            return Result.success(agencyExtendVO);
        }
        //如果机构已锁定则查询现有机构拓展信息
        if (ObjectUtil.equal(crmEnterpriseDTO.getBusinessCode(), CrmBusinessCodeEnum.CHAIN.getCode())) {
            if (crmEnterpriseDTO.getSupplyChainRole().equals(CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode())) {
                CrmSupplierDTO crmSupplierDTO = crmSupplierApi.getCrmSupplierByCrmEnterId(form.getCrmEnterpriseId());
                if (Objects.nonNull(crmSupplierDTO)) {
                    dataType=AgencyLockFormDataTypeEnum.BUSINESS_STORE.getCode();
                    PojoUtils.map(crmSupplierDTO, agencyExtendVO);
                }
            }
            if (crmEnterpriseDTO.getSupplyChainRole().equals(CrmSupplyChainRoleEnum.HOSPITAL.getCode())) {
                CrmHospitalDTO crmHospitalDTO = crmHosptialApi.getCrmSupplierByCrmEnterId(form.getCrmEnterpriseId().toString());
                if (Objects.nonNull(crmHospitalDTO)) {
                    dataType=AgencyLockFormDataTypeEnum.BUSINESS_STORE.getCode();
                    PojoUtils.map(crmHospitalDTO, agencyExtendVO);
                }
            }
            if (crmEnterpriseDTO.getSupplyChainRole().equals(CrmSupplyChainRoleEnum.PHARMACY.getCode())) {
                CrmPharmacyDTO crmPharmacyDTO = crmPharmacyApi.queryByEnterpriseId(form.getCrmEnterpriseId());
                if (Objects.nonNull(crmPharmacyDTO)) {
                    dataType=AgencyLockFormDataTypeEnum.BUSINESS_STORE.getCode();
                    PojoUtils.map(crmPharmacyDTO, agencyExtendVO);
                }
            }
        }
        //查询机构三者信息
        //List<CrmEnterpriseRelationShipDTO> relationShipList = crmEnterpriseRelationShipApi.getCrmEnterpriseRelationShipByNameList(ListUtil.toList(crmEnterpriseDTO.getName()));
        List<CrmEnterpriseRelationShipDTO> relationShipList=crmEnterpriseRelationShipApi.getCrmEnterpriseRelationShipByCrmenterpriseIdList(ListUtil.toList(crmEnterpriseDTO.getId()));
        buildEsbEmployInfo(relationShipList,false);
        agencyExtendVO.setRelationList(PojoUtils.map(relationShipList, LockAgencyExtendVO.CrmEnterpriseRelationShipVO.class));
        agencyExtendVO.setDataType(dataType);
        List<Long> proIdList = agencyExtendVO.getRelationList().stream().map(LockAgencyExtendVO.CrmEnterpriseRelationShipVO::getProductGroupId).distinct().collect(Collectors.toList());

        //查询三者关系的产品组名称
        if (CollUtil.isNotEmpty(proIdList)){
            Map<Long, String> proMap = crmGoodsGroupApi.findGroupByIds(proIdList).stream().collect(Collectors.toMap(CrmGoodsGroupDTO::getId, CrmGoodsGroupDTO::getName));
            agencyExtendVO.getRelationList().forEach(e -> {
                LockAgencyExtendVO.ProductGroupVO var=new LockAgencyExtendVO.ProductGroupVO();
                var.setId(e.getProductGroupId());
                var.setProductGroupId(e.getProductGroupId());
                var.setProductGroup(proMap.getOrDefault(e.getProductGroupId(),""));
                e.setGroupList(ListUtil.toList(var));
            });
        }
        return Result.success(agencyExtendVO);
    }


    //    /**
    //     * @param userInfo
    //     * @param form
    //     * @return
    //     */
    //    @ApiOperation(value = "提交审核/提交草稿")
    //    @PostMapping("/submit")
    //    public Result<Boolean> submit(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SubmitAgencyLockForm form) {
    //
    //        //todo 判断form状态
    //        if (ObjectUtil.isNotNull(form.getFormId()) && ObjectUtil.notEqual(form.getFormId(), 0L)) {
    //            Integer formStatus = null;
    //            if (ObjectUtil.notEqual(10, formStatus)) {
    //                return Result.failed("表单非待提交，不能修改");
    //            }
    //        }
    //        //判断机构是否重复
    //        if (CollUtil.isEmpty(form.getAgencyList())) {
    //            return Result.failed("机构列表不能为空");
    //        }
    //        List<SubmitAgencyLockForm.AgencyExtendInfoForm> agencyList = form.getAgencyList();
    //        List<Long> var = agencyList.stream().map(SubmitAgencyLockForm.AgencyExtendInfoForm::getCrmEnterpriseId).distinct().collect(Collectors.toList());
    //        if (ObjectUtil.notEqual(agencyList.size(), var.size())) {
    //            return Result.failed("机构列表中存在重复的机构");
    //        }
    //        for (SubmitAgencyLockForm.AgencyExtendInfoForm e : agencyList) {
    //            Integer supplyChainRole = e.getSupplyChainRole();
    //            //校验商业公司是否维护了锁定类型
    //            if (ObjectUtil.equal(1, supplyChainRole)) {
    //                if (ObjectUtil.isNull(e.getLockType()) || ObjectUtil.equal(e.getLockType(), 0)) {
    //                    return Result.failed(e + "没有维护锁定类型");
    //                }
    //            }
    //            //校验是否都维护了三者关系
    //            if (CollUtil.isEmpty(e.getRelationList())) {
    //                return Result.failed(e.getName() + "没有维护三者关系");
    //            }
    //            //校验三者关系产品组重复
    //            List<SubmitAgencyLockForm.AgencyLockRelationShipForm> relationList = e.getRelationList();
    //            List<String> var3 = relationList.stream().map(SubmitAgencyLockForm.AgencyLockRelationShipForm::getProductGroup).distinct().collect(Collectors.toList());
    //            if (ObjectUtil.notEqual(relationList.size(), var3.size())) {
    //                return Result.failed(e.getName() + "三者关系的产品在有重复");
    //            }
    //            //校验商业/医疗/目标药店都要绑定到销售代表
    //            //零售
    //            if (ObjectUtil.equal(3, supplyChainRole)) {
    //                //如果档案中的“是否目标”选择是
    //                if (ObjectUtil.equal(e.getTargetFlag(), 1)) {
    //                    Boolean hasErr = Boolean.FALSE;
    //                    String empName = "";
    //                    for (SubmitAgencyLockForm.AgencyLockRelationShipForm r : relationList) {
    //                        if (ObjectUtil.notEqual(r.getDutyGradeId(), dutyGradeIdConstant)) {
    //                            hasErr = Boolean.TRUE;
    //                            break;
    //                        }
    //                    }
    //                    if (hasErr) {
    //                        return Result.failed("机构" + e.getName() + "下的员工" + empName + "不是销售代表，请核实");
    //                    }
    //                }
    //            }
    //            //医疗、商业
    //            if (ObjectUtil.equal(1, supplyChainRole) || ObjectUtil.equal(2, supplyChainRole)) {
    //                Boolean hasErr = Boolean.FALSE;
    //                String empName = "";
    //                for (SubmitAgencyLockForm.AgencyLockRelationShipForm r : relationList) {
    //                    if (ObjectUtil.notEqual(r.getDutyGradeId(), dutyGradeIdConstant)) {
    //                        hasErr = Boolean.TRUE;
    //                        break;
    //                    }
    //                }
    //                if (hasErr) {
    //                    return Result.failed("机构" + e.getName() + "下的员工" + empName + "不是销售代表，请核实");
    //                }
    //            }
    //        }
    //
    //        SubmitAgencyLockRequest request = new SubmitAgencyLockRequest();
    //        PojoUtils.map(form, request);
    //        Map<Long, List<SubmitAgencyLockForm.AgencyLockRelationShipForm>> listMap = form.getAgencyList().stream().collect(Collectors.toMap(SubmitAgencyLockForm.AgencyExtendInfoForm::getCrmEnterpriseId, SubmitAgencyLockForm.AgencyExtendInfoForm::getRelationList));
    //        request.setAgencyList(PojoUtils.map(form.getAgencyList(), SubmitAgencyLockRequest.AgencyExtendInfoRequest.class));
    //        request.getAgencyList().stream().forEach(e -> {
    //            e.setRelationList(PojoUtils.map(listMap.get(e.getCrmEnterpriseId()), SubmitAgencyLockRequest.AgencyLockRelationShipRequest.class));
    //        });
    //        request.setOpUserId(userInfo.getCurrentUserId());
    //        agencyLockFormApi.submitAgencyLock(request);
    //        return Result.success(Boolean.TRUE);
    //    }

    /**
     * @param userInfo
     * @param form
     * @return
     */
    @ApiOperation(value = "修改或新增")
    @PostMapping("/save")
    public Result<Long> save(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SubmitAgencyLockForm form) {

        //判断form状态
        if (ObjectUtil.isNotNull(form.getFormId()) && ObjectUtil.notEqual(form.getFormId(), 0L)) {
            FormDTO formDTO = formApi.getById(form.getFormId());
            if (ObjectUtil.isNull(formDTO)) {
                return Result.failed("表单不存在");
            }
            Integer formStatus = formDTO.getStatus();
            if (ObjectUtil.notEqual(FormStatusEnum.UNSUBMIT.getCode(), formStatus) && ObjectUtil.notEqual(FormStatusEnum.REJECT.getCode(), formStatus)) {
                return Result.failed("表单非待提交或驳回状态，不能修改");
            }
        }

        Integer supplyChainRole = form.getSupplyChainRole();
        if(ObjectUtil.equal(AgencySupplyChainRoleEnum.HOSPITAL.getCode(), supplyChainRole)){
            return Result.failed("医疗机构三者关系，请通过辖区功能进行维护！");
        }
        //校验商业公司是否维护了锁定类型
        if (ObjectUtil.equal(1, supplyChainRole)) {
            if (ObjectUtil.isNull(form.getLockType()) || ObjectUtil.equal(form.getLockType(), 0)) {
                return Result.failed(form + "没有维护锁定类型");
            }
        }
        //只有零售、医疗、商业的类型为销售是必须录入三者关系
        if (ObjectUtil.equal(AgencySupplyChainRoleEnum.PHARMACY.getCode(), supplyChainRole) || ObjectUtil.equal(AgencySupplyChainRoleEnum.HOSPITAL.getCode(), supplyChainRole)||ObjectUtil.equal(form.getLockType(), AgencyLocTypeEnum.SALE.getCode())) {
            //校验是否都维护了三者关系
            if (CollUtil.isEmpty(form.getRelationList().stream().filter(e -> ObjectUtil.equal(e.getIsIgnoreSubmit(), 0)).collect(Collectors.toList()))) {
                return Result.failed(form.getName() + "没有维护三者关系");
            }
        }
        //校验三者关系产品组重复
        List<SubmitAgencyLockForm.AgencyLockRelationShipForm> relationList = form.getRelationList();
        List<Long> var3 = relationList.stream().map(SubmitAgencyLockForm.AgencyLockRelationShipForm::getProductGroupId).distinct().collect(Collectors.toList());

        if (ObjectUtil.notEqual(relationList.size(), var3.size())) {
            return Result.failed(form.getName() + "三者关系的产品在有重复");
        }
        //校验商业/医疗/目标药店都要绑定到销售代表
        //零售
        if (ObjectUtil.equal(AgencySupplyChainRoleEnum.PHARMACY.getCode(), supplyChainRole)) {
            //如果档案中的“是否目标”选择是
            if (ObjectUtil.equal(form.getTargetFlag(), 1)) {
                Boolean hasErr = Boolean.FALSE;
                String empName = "";
                for (SubmitAgencyLockForm.AgencyLockRelationShipForm r : relationList) {
                    //忽略原有三者关系岗位校验
                    if(ObjectUtil.equal(r.getIsIgnoreSubmit(),1)){
                        continue;
                    }
                    if (ObjectUtil.notEqual(r.getDutyGredeId(), dutyGradeIdConstant)) {
                        hasErr = Boolean.TRUE;
                        break;
                    }
                }
                if (hasErr) {
                    return Result.failed("机构" + form.getName() + "下的员工" + empName + "不是销售代表，请核实");
                }
            }
        }
        //医疗、商业
        if (ObjectUtil.equal(AgencySupplyChainRoleEnum.SUPPLIER.getCode(), supplyChainRole) || ObjectUtil.equal(AgencySupplyChainRoleEnum.HOSPITAL.getCode(), supplyChainRole)) {
            Boolean hasErr = Boolean.FALSE;
            String empName = "";
            for (SubmitAgencyLockForm.AgencyLockRelationShipForm r : relationList) {
                //忽略原有三者关系岗位校验
                if(ObjectUtil.equal(r.getIsIgnoreSubmit(),1)){
                    continue;
                }
                if (ObjectUtil.notEqual(r.getDutyGredeId(), dutyGradeIdConstant)) {
                    hasErr = Boolean.TRUE;
                    break;
                }
            }
            if (hasErr) {
                return Result.failed("机构" + form.getName() + "下的员工" + empName + "不是销售代表，请核实");
            }
        }

        if(CollectionUtils.isNotEmpty(form.getRelationList())){
            form.getRelationList().forEach(item->{
                if(StringUtils.equals(item.getDutyGredeId(),"2")){
                    item.setPostCode(item.getSuperiorJob());
                }
            });
        }
        SaveAgencyLockRequest request = new SaveAgencyLockRequest();
        PojoUtils.map(form, request);
        //忽略现有三者关系
        request.setRelationList(PojoUtils.map(form.getRelationList().stream().filter(e -> ObjectUtil.equal(e.getIsIgnoreSubmit(), 0)).collect(Collectors.toList()), SaveAgencyLockRequest.AgencyLockRelationShipRequest.class));
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setEmpNo(userInfo.getCurrentUserCode());
        request.setUserId(userInfo.getCurrentUserId());
        Long formId = agencyLockFormApi.saveAgencyLock(request);
        return Result.success(formId);
    }


    /**
     * 根据明细id查询机构拓展信息及三者信息
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "根据formId查询机构锁定的申请明细--用于展示机构锁定申请的分页明细")
    @GetMapping("/queryAgencyLockDetailPage")
    public Result<Page<AgencyLockDetailPageListItemVO>> queryAgencyLockDetailPage(@CurrentUser CurrentSjmsUserInfo userInfo, QueryAgencyLockDetailPageForm form) {
        Page<AgencyLockDetailPageListItemVO> result;
        //判断ID是否存在
        if (ObjectUtil.isNull(form.getFormId())) {
            return Result.success(form.getPage());
        }
        Page<AgencyLockFormDTO> page = agencyLockFormApi.queryAgencyLockDetailPage(PojoUtils.map(form, QueryAgencyLockDetailPageRequest.class));
        result = PojoUtils.map(page, AgencyLockDetailPageListItemVO.class);
        //查询三者关系数量
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<Long> agencyLockFormIdList = page.getRecords().stream().map(AgencyLockFormDTO::getId).collect(Collectors.toList());
            Map<Long, List<AgencyLockRelationShipDTO>> shipMap = agencyLockRelationShipApi.queryRelationListByAgencyFormId(agencyLockFormIdList);
            result.getRecords().stream().forEach(e -> {
                List<AgencyLockRelationShipDTO> var = Optional.ofNullable(shipMap.get(e.getId())).orElseGet(() -> ListUtil.toList());
                e.setRelationShipCount(var.size());
            });
        }
        return Result.success(result);
    }


    /**
     * 根据明细id查询机构拓展信息及三者信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据流程明细id查询机构拓展信息及三者信息--用于流程记录回显，前端可根据isIgnoreSubmit字段判断是否展示当前三者信息记录，如查看详情时无需展示isIgnoreSubmit为true的记录，但在编辑时需要展示isIgnoreSubmit=true但是不可编辑")
    @GetMapping("/queryAgExtendInfoByFormId")
    public Result<LockAgencyExtendVO> queryAgExtendInfoByFormId(@RequestParam Long id) {
        LockAgencyExtendVO result;
        //查询表单
        AgencyLockFormDTO lockForm = agencyLockFormApi.getAgencyLockForm(id);
        if (ObjectUtil.isNull(lockForm)) {
            return Result.success(new LockAgencyExtendVO());
        }
        result = PojoUtils.map(lockForm, LockAgencyExtendVO.class);
        result.setRelationList(ListUtil.toList());

        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(lockForm.getCrmEnterpriseId());
        if(ObjectUtil.isNull(crmEnterpriseDTO)){
            return Result.failed("此机构信息或已删除，请删除草稿后重新新增");
        }
        //设置机构属性
        if (ObjectUtil.isNotNull(crmEnterpriseDTO)) {
            result.setCrmEnterpriseInfo(PojoUtils.map(crmEnterpriseDTO, LockAgencyExtendVO.CrmEnterpriseInfoVO.class));
        }

        FormDTO formDTO = formApi.getById(lockForm.getFormId());
        //判断审核成功后不查询机构锁定表的三者关系，以免三者关系展示重复
        if (ObjectUtil.notEqual(formDTO.getStatus(),FormStatusEnum.APPROVE.getCode())){
            //查询机构锁定的三者关系
            //查询表单机构三者信息
            List<AgencyLockRelationShipDTO> formRelationShipList = agencyLockRelationShipApi.listByForm(lockForm.getId(), lockForm.getCrmEnterpriseId());
            formRelationShipList.stream().forEach(e -> {
                e.setId(null);
            });
            result.getRelationList().addAll((PojoUtils.map(formRelationShipList, LockAgencyExtendVO.CrmEnterpriseRelationShipVO.class)));
        }
        //查询现有三者关系
        //List<CrmEnterpriseRelationShipDTO> relationShipList = crmEnterpriseRelationShipApi.getCrmEnterpriseRelationShipByNameList(ListUtil.toList(crmEnterpriseDTO.getName()));
        List<CrmEnterpriseRelationShipDTO> relationShipList=crmEnterpriseRelationShipApi.getCrmEnterpriseRelationShipByCrmenterpriseIdList(ListUtil.toList(crmEnterpriseDTO.getId()));
        if (CollUtil.isNotEmpty(relationShipList)) {
            result.getRelationList().addAll(PojoUtils.map(relationShipList, LockAgencyExtendVO.CrmEnterpriseRelationShipVO.class));
        }
        List<CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipDTOList = PojoUtils.map(result.getRelationList(), CrmEnterpriseRelationShipDTO.class);
        buildEsbEmployInfo(crmEnterpriseRelationShipDTOList,false);
        result.setRelationList(PojoUtils.map(crmEnterpriseRelationShipDTOList, LockAgencyExtendVO.CrmEnterpriseRelationShipVO.class));
        //查询三者关系的可选产品组
        result.getRelationList().forEach(e -> {
            List<CrmDepartmentAreaRelationDTO> esbEmployeeDTO = crmEnterpriseRelationShipApi.getGoodsGroup(e.getBusinessDepartment(), result.getCrmEnterpriseInfo().getSupplyChainRole());
            esbEmployeeDTO = esbEmployeeDTO.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CrmDepartmentAreaRelationDTO::getProductGroup))), ArrayList::new));
            e.setGroupList(PojoUtils.map(esbEmployeeDTO, LockAgencyExtendVO.ProductGroupVO.class));
        });
        return Result.success(result);
    }

    @ApiOperation(value = "修改归档状态")
    @PostMapping(value = "/updateArchiveStatus")
    public Result<Boolean> updateArchiveStatus(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid UpdateArchiveStatusForm form) {
        UpdateAgencyLockArchiveRequest request = PojoUtils.map(form, UpdateAgencyLockArchiveRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        agencyLockFormApi.updateArchiveStatusById(request);
        return Result.success(true);
    }

    @ApiOperation(value = "提交审核")
    @PostMapping("/submit")
    public Result<Boolean> submit(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SubmitAgencyExtendChangeForm form) {
        FormDTO formDTO = formApi.getById(form.getFormId());
        if (ObjectUtil.isNull(formDTO)) {
            return Result.failed("表单不存在");
        }
        Integer formStatus = formDTO.getStatus();
        if (ObjectUtil.notEqual(FormStatusEnum.UNSUBMIT.getCode(), formStatus) && ObjectUtil.notEqual(FormStatusEnum.REJECT.getCode(), formStatus)) {
            return Result.failed("表单非待提交或驳回状态，不能提交");
        }
        SubmitFormBaseRequest request = new SubmitFormBaseRequest();
        request.setId(form.getFormId()).setEmpId(userInfo.getCurrentUserCode()).setOpUserId(userInfo.getCurrentUserId());
        agencyLockFormApi.submitAgencyLockForm(request);
        return Result.success(true);
    }

    public void buildEsbEmployInfo(List<CrmEnterpriseRelationShipDTO> crmEnterpriseDTOS, boolean needProvinceInfo) {
        // 用岗位id名字postcode找到人员信息，业务部门，省区，业务区域等
        List<Long> postCodes = crmEnterpriseDTOS.stream().filter(item->!(item.getPostCode().equals(0L))).map(CrmEnterpriseRelationShipDTO::getPostCode).collect(Collectors.toList());
        if(CollectionUtil.isEmpty(postCodes)){
            return;
        }
        List<EsbEmployeeDTO> esbEmployeeDTOS = esbEmployeeApi.listByJobIds(postCodes);
        Map<Long, EsbEmployeeDTO> esbEmployeeDTOMap=new HashMap<>();
        Map<Long, EsbOrganizationDTO> esbOrganizationDTOMap=new HashMap<>();
        if(CollectionUtil.isNotEmpty(esbEmployeeDTOS)){
            esbEmployeeDTOMap = esbEmployeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getJobId,o -> o, (k1, k2) -> k1));
            List<Long> deptIds = esbEmployeeDTOS.stream().map(EsbEmployeeDTO::getDeptId).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(deptIds)){
                esbOrganizationDTOMap = businessDepartmentApi.listByOrgIds(deptIds);
            }
        }
        Map<Long, EsbEmployeeDTO> finalEsbEmployeeDTOMap = esbEmployeeDTOMap;
        Map<Long, EsbOrganizationDTO> finalEsbOrganizationDTOMap = esbOrganizationDTOMap;
        // 用岗位id获取esbEmploy信息
        crmEnterpriseDTOS.forEach(item -> {
            Long postCode = item.getPostCode();
            if (ObjectUtil.isNull(postCode) || postCode.equals(0L)) {
                return;
            }
            EsbEmployeeDTO esbEmployeeDTO = finalEsbEmployeeDTOMap.get(postCode);
            if (ObjectUtil.isNull(esbEmployeeDTO)) {
                return;
            }
            changeField(item, esbEmployeeDTO);
            // 获取上级主管岗位id和名称
            if(StringUtils.equals(esbEmployeeDTO.getDutyGredeId(),"1")){
                List<String> jobNamesList = ListUtil.toList("地区经理", "占位");
                List<EsbEmployeeDTO> provincialManagerList = esbEmployeeApi.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince(), esbEmployeeDTO.getYxArea(), jobNamesList);
                if (CollUtil.isNotEmpty(provincialManagerList)) {
                    EsbEmployeeDTO esbEmployeeDTO1 = provincialManagerList.get(0);
                    item.setSuperiorJob(esbEmployeeDTO1.getJobId());
                    item.setSuperiorJobName(esbEmployeeDTO1.getJobName());
                }else{
                    item.setSuperiorJob(null);
                    item.setSuperiorJobName(null);
                    item.setSuperiorSupervisorName(null);
                    item.setSuperiorSupervisorCode(null);
                }
            }
            if(StringUtils.equals(esbEmployeeDTO.getDutyGredeId(),"2")){
                item.setSuperiorSupervisorCode(esbEmployeeDTO.getEmpId());
                item.setSuperiorSupervisorName(esbEmployeeDTO.getEmpName());
                item.setSuperiorJobName(esbEmployeeDTO.getJobName());
                item.setSuperiorJob(esbEmployeeDTO.getJobId());
                item.setRepresentativeCode(null);
                item.setRepresentativeName(null);
                item.setPostCode(null);
                item.setPostName(null);
            }
            //通过循环获取部门。
            EsbOrganizationDTO organizationDTO= finalEsbOrganizationDTOMap.get(esbEmployeeDTO.getDeptId());
            if (ObjectUtil.isNotEmpty(organizationDTO)) {
                item.setDepartment(organizationDTO.getOrgName());
            }
            //通过部门，业务部门，业务省区获取省区
            String provinceArea = crmEnterpriseRelationShipApi.getProvinceAreaByThreeParms(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince());
            item.setProvincialArea(provinceArea);
        });
    }
    //    @ApiOperation(value = "校验机构数据信息")
    //    @PostMapping("/checkAgencyFormData")
    //    public Result<CheckResultVO> checkAgencyFormData(@Valid @RequestBody CheckAgencyLockFormDataForm form) {
    //        CheckResultVO checkResultVO = new CheckResultVO();
    //        checkResultVO.setIsSuccess(true);
    //        QueryFirstAgencyLockFormRequest agencyFormRequest;
    //        //供应链角色检查
    //        if (Objects.nonNull(form.getSupplyChainRole()) && 0 != form.getSupplyChainRole() && Objects.nonNull(form.getFormId()) && 0 != form.getFormId()) {
    //            agencyFormRequest = new QueryFirstAgencyLockFormRequest();
    //            if (Objects.nonNull(form.getId())) {
    //                agencyFormRequest.setNotId(form.getId());
    //            }
    //            agencyFormRequest.setFormId(form.getFormId());
    //            AgencyLockFormDTO agencyFormDTO = agencyLockFormApi.getFirstInfo(agencyFormRequest);
    //            if (Objects.nonNull(agencyFormDTO)) {
    //                Integer supplyChainRole = agencyFormDTO.getSupplyChainRole();
    //                if (!supplyChainRole.equals(form.getSupplyChainRole())) {
    //                    checkResultVO.setIsSuccess(false);
    //                    checkResultVO.setErrorMessage("表单中机构的供应链角色必须一致");
    //                }
    //            }
    //        }
    //        //重复机构检查
    //        if (Objects.nonNull(form.getFormId()) && 0 != form.getFormId()) {
    //            agencyFormRequest = new QueryFirstAgencyLockFormRequest();
    //            if (Objects.nonNull(form.getId())) {
    //                agencyFormRequest.setNotId(form.getId());
    //            }
    //            agencyFormRequest.setCrmEnterpriseId(form.getCrmEnterpriseId());
    //            agencyFormRequest.setFormId(form.getFormId());
    //            AgencyLockFormDTO agencyFormDTO = agencyLockFormApi.getFirstInfo(agencyFormRequest);
    //            if (Objects.nonNull(agencyFormDTO)) {
    //                checkResultVO.setIsSuccess(false);
    //                checkResultVO.setErrorMessage("列表中已存在当前机构");
    //            }
    //        }
    //        return Result.success(checkResultVO);
    //    }

}