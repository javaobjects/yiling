package com.yiling.admin.erp.enterprise.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.erp.enterprise.form.ErpEnterpriseSaveForm;
import com.yiling.admin.erp.enterprise.form.QueryCrmEnterpriseListForm;
import com.yiling.admin.erp.enterprise.form.QueryEnterpriseListForm;
import com.yiling.admin.erp.enterprise.form.QueryErpEnterprisePageForm;
import com.yiling.admin.erp.enterprise.form.QueryErpEnterpriseParentPageForm;
import com.yiling.admin.erp.enterprise.form.UpdateMonitorStatusForm;
import com.yiling.admin.erp.enterprise.vo.ErpCrmEnterpriseInfoVO;
import com.yiling.admin.erp.enterprise.vo.ErpEnterpriseDetailVO;
import com.yiling.admin.erp.enterprise.vo.ErpEnterpriseInfoVO;
import com.yiling.admin.erp.enterprise.vo.ErpEnterprisePageVO;
import com.yiling.admin.erp.enterprise.vo.ErpEnterpriseParentPageVO;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseSimpleDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterprisePageRequest;
import com.yiling.dataflow.crm.dto.request.UpdateAgencyEnterpriseRequest;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientParentQueryRequest;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;
import com.yiling.open.erp.dto.request.ErpClientSaveRequest;
import com.yiling.open.erp.dto.request.UpdateMonitorStatusRequest;
import com.yiling.open.erp.enums.DepthEnum;
import com.yiling.open.erp.enums.ErpEnterpriseSyncStatus;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.dto.request.UpdateErpStatusRequest;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * B2B运营后台 ERP对接企业管理
 *
 * @author: houjie.sun
 * @date: 2022/1/13
 */
@Api(tags = "ERP对接企业管理接口")
@RestController
@RequestMapping("/erpEnterprise")
public class ErpEnterpriseController {
    @DubboReference
    ErpClientApi  erpClientApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    CrmEnterpriseApi crmEnterpriseApi;

    @ApiOperation(value = "ERP对接企业信息列表分页", httpMethod = "POST")
    @PostMapping("/queryListPage")
    public Result<Page<ErpEnterprisePageVO>> queryListPage(@CurrentUser CurrentAdminInfo adminInfo,
                                                           @RequestBody @Valid QueryErpEnterprisePageForm form) {
        ErpClientQueryRequest request = PojoUtils.map(form, ErpClientQueryRequest.class);
        Page<ErpEnterprisePageVO> page = PojoUtils.map(erpClientApi.page(request), ErpEnterprisePageVO.class);
        return Result.success(page);
    }

    @ApiOperation(value = "查询已对接父类企业信息列表分页", httpMethod = "POST")
    @PostMapping("/queryParentListPage")
    public Result<Page<ErpEnterpriseParentPageVO>> queryParentListPage(@CurrentUser CurrentAdminInfo adminInfo,
                                                                       @RequestBody @Valid QueryErpEnterpriseParentPageForm form) {
        ErpClientParentQueryRequest request = PojoUtils.map(form, ErpClientParentQueryRequest.class);
        Page<ErpEnterpriseParentPageVO> page = PojoUtils.map(erpClientApi.parentPage(request), ErpEnterpriseParentPageVO.class);
        return Result.success(page);
    }

    @ApiOperation(value = "查询企业信息", httpMethod = "POST")
    @PostMapping("/queryEnterpriseList")
    public Result<List<ErpEnterpriseInfoVO>> queryEnterpriseList(@CurrentUser CurrentAdminInfo adminInfo,
                                                                 @RequestBody @Valid QueryEnterpriseListForm form) {
        QueryEnterpriseByNameRequest request = PojoUtils.map(form, QueryEnterpriseByNameRequest.class);
        request.setTypeList(Arrays.asList(EnterpriseTypeEnum.BUSINESS.getCode(),EnterpriseTypeEnum.CHAIN_BASE.getCode(),EnterpriseTypeEnum.CHAIN_DIRECT.getCode(),EnterpriseTypeEnum.CHAIN_JOIN.getCode()));
        List<EnterpriseDTO> enterpriseList = enterpriseApi.getEnterpriseListByName(request);
        if (CollUtil.isEmpty(enterpriseList)) {
            return Result.success(ListUtil.empty());
        }
        // 查询ERP已对接的商业公司
        List<Long> suIdList = enterpriseList.stream().map(EnterpriseDTO::getId).distinct().collect(Collectors.toList());
        List<ErpClientDTO> erpClientList = erpClientApi.selectByRkSuIdList(Convert.toList(Long.class, suIdList));
        Map<Long, ErpClientDTO> erpClientMap = new HashMap<>();
        if (CollUtil.isNotEmpty(enterpriseList)) {
            erpClientMap = erpClientList.stream().collect(Collectors.toMap(ErpClientDTO::getRkSuId, e -> e, (v1, v2) -> v1));
        }

        // 去除已对接的商业公司
        List<ErpEnterpriseInfoVO> resultList = new ArrayList<>();
        for (EnterpriseDTO enterprise : enterpriseList) {
            ErpClientDTO erpClientDTO = erpClientMap.get(enterprise.getId());
            if (ObjectUtil.isNotNull(erpClientDTO)) {
                continue;
            }

            ErpEnterpriseInfoVO vo = new ErpEnterpriseInfoVO();
            vo.setSuId(enterprise.getId());
            vo.setClientName(enterprise.getName());
            //  将查询条件相等的放到列表首位
            if (enterprise.getName().equals(request.getName())) {
                resultList.add(0, vo);
            } else {
                resultList.add(vo);
            }
        }
        return Result.success(resultList);
    }

    @ApiOperation(value = "查询crm企业信息", httpMethod = "POST")
    @PostMapping("/queryCrmEnterpriseList")
    public Result<List<ErpCrmEnterpriseInfoVO>> queryCrmEnterpriseList(@CurrentUser CurrentAdminInfo adminInfo,
                                                                       @RequestBody @Valid QueryCrmEnterpriseListForm form) {
        if (ObjectUtil.isNull(form) || StrUtil.isBlank(form.getName())) {
            return Result.success(ListUtil.empty());
        }
        QueryCrmEnterprisePageRequest request = new QueryCrmEnterprisePageRequest();
        request.setLikeName(form.getName());
        request.setRoleIds(ListUtil.toList(1));
        request.setBusinessCode(1);
        request.setCurrent(1);
        request.setSize(100);
        Page<CrmEnterpriseSimpleDTO> crmPage = crmEnterpriseApi.getCrmEnterpriseSimplePage(request);
        if (ObjectUtil.isNull(crmPage) || CollUtil.isEmpty(crmPage.getRecords())){
            return Result.success(ListUtil.empty());
        }

        List<ErpCrmEnterpriseInfoVO> list = new ArrayList<>();
        for (CrmEnterpriseSimpleDTO crm : crmPage.getRecords()) {
            ErpCrmEnterpriseInfoVO vo = new ErpCrmEnterpriseInfoVO();
            vo.setCrmEnterpriseId(crm.getId());
            vo.setCrmEnterpriseName(crm.getName());
            if (ObjectUtil.equal(form.getName(), crm.getName())) {
                list.add(0, vo);
            } else {
                list.add(vo);
            }
        }
        return Result.success(list);
    }


    @Log(title = "ERP对接企业-新增", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "新增", httpMethod = "POST")
    @PostMapping("/save")
    public Result<Long> save(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid ErpEnterpriseSaveForm form) {
        ErpClientSaveRequest request = PojoUtils.map(form, ErpClientSaveRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        if (ObjectUtil.isNull(request.getRkSuId())) {
            throw new BusinessException(OpenErrorCode.ENTERPRISE_ID_NULL);
        }
        // 同步状态为 已开启，则对接级别不能为空或0
//        checkSyncStatusAndDepthAndFlow(request);
        // 校验企业信息
        checkEnterprise(request);
        // 校验crm企业
        CrmEnterpriseDTO crmEnterprise = getCrmEnterprise(request.getCrmEnterpriseId(), 0L);
        if (ObjectUtil.isNotNull(crmEnterprise)){
            request.setCrmProvinceCode(crmEnterprise.getProvinceCode());
        }
        // 保存
        Long id = erpClientApi.saveOrUpdateErpClient(request);
        // 更新企业信息的ERP对接级别
        updateEnterprise(request);
        // 更新crm中的eid
        if (ObjectUtil.isNotNull(form.getCrmEnterpriseId()) && 0 != form.getCrmEnterpriseId().intValue()) {
            updateCrmEnterpriseEidById(form.getCrmEnterpriseId(), form.getRkSuId());
        }
        return Result.success(id);
    }

    private void updateCrmEnterpriseEidById(Long crmId, Long eid) {
        UpdateAgencyEnterpriseRequest request = new UpdateAgencyEnterpriseRequest();
        request.setId(crmId);
        request.setEid(eid);
        crmEnterpriseApi.updateCrmEnterpriseSimple(request);
    }

    @Log(title = "ERP对接企业-修改", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "修改", httpMethod = "POST")
    @PostMapping("/update")
    public Result<Long> update(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid ErpEnterpriseSaveForm form) {
        ErpClientSaveRequest request = PojoUtils.map(form, ErpClientSaveRequest.class);
        // 同步状态为 已开启，则对接级别不能为空或0
//        checkSyncStatusAndDepthAndFlow(request);
        // erp企业信息
        ErpClientDTO erpClient = getByRkSuId(request.getRkSuId());
        // 校验分公司编码
        checkErpClient(request, erpClient);
        // 企业类型为工业的，不能修改
        checkEnterpriseType(Long.valueOf(request.getRkSuId()));
        // 校验crm企业
        CrmEnterpriseDTO crmEnterprise = getCrmEnterprise(request.getCrmEnterpriseId(), erpClient.getId());
        if (ObjectUtil.isNotNull(crmEnterprise)){
            request.setCrmProvinceCode(crmEnterprise.getProvinceCode());
        }
        // 更新
        Long id = erpClientApi.saveOrUpdateErpClient(request);
        // 更新企业信息的ERP对接级别
        updateEnterprise(request);
        // 更新crm中的eid
        if (!ObjectUtil.equal(request.getCrmEnterpriseId(), erpClient.getCrmEnterpriseId())){
            if (ObjectUtil.isNotNull(request.getCrmEnterpriseId()) && 0 != request.getCrmEnterpriseId()) {
                // 之前关联的crmid取消关联
                updateCrmEnterpriseEidById(erpClient.getCrmEnterpriseId(), 0L);
                // 更新新的crmid关联
                updateCrmEnterpriseEidById(request.getCrmEnterpriseId(), erpClient.getRkSuId());
            } else {
                // 之前关联的crmid取消关联
                updateCrmEnterpriseEidById(erpClient.getCrmEnterpriseId(), 0L);
            }
        }
        return Result.success(id);
    }

    @ApiOperation(value = "详情", httpMethod = "GET")
    @GetMapping("/queryDetail")
    public Result<ErpEnterpriseDetailVO> queryDetail(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("rkSuId") @NotNull Long rkSuId) {
        ErpEnterpriseDetailVO detail = PojoUtils.map(erpClientApi.selectByRkSuId(rkSuId), ErpEnterpriseDetailVO.class);
        if(ObjectUtil.isNotNull(detail)){
            // 父级企业
            if(!ObjectUtil.equal(detail.getSuId(), detail.getRkSuId())){
                ErpClientDTO erpClientParent = erpClientApi.selectByRkSuId(detail.getSuId());
                if (ObjectUtil.isNotNull(erpClientParent)) {
                    detail.setClientNameParent(erpClientParent.getClientName());
                }
            }
            // crm企业
            Long crmEnterpriseId = detail.getCrmEnterpriseId();
            if (ObjectUtil.isNotNull(detail.getCrmEnterpriseId()) && detail.getCrmEnterpriseId().intValue() > 0 ) {
                CrmEnterpriseDTO crmEnterprise = crmEnterpriseApi.getCrmEnterpriseById(crmEnterpriseId);
                if (ObjectUtil.isNotNull(crmEnterprise)) {
                    detail.setCrmEnterpriseName(crmEnterprise.getName());
                }
            }
        }
        return Result.success(detail);
    }

    @Log(title = "ERP对接企业-开启/关闭监控状态", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "开启/关闭监控状态", httpMethod = "POST")
    @PostMapping("/updateMonitorStatus")
    public Result<Boolean> updateMonitorStatus(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateMonitorStatusForm form) {
        UpdateMonitorStatusRequest request = PojoUtils.map(form, UpdateMonitorStatusRequest.class);
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getRkSuId()) || ObjectUtil.isNull(request.getMonitorStatus())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        ErpClientDTO erpClient = getByRkSuId(request.getRkSuId());
        if (ObjectUtil.equal(erpClient.getMonitorStatus(), request.getMonitorStatus())) {
            return Result.success(true);
        }

        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setSuId(erpClient.getSuId());
        return Result.success(erpClientApi.updateMonitorStatus(request));
    }

    /**
     * 企业类型为工业的，不能修改
     * @param eid
     */
    private void checkEnterpriseType(Long eid) {
        EnterpriseDTO enterprise = getEnterprise(eid);
        if(ObjectUtil.equal(EnterpriseTypeEnum.INDUSTRY.getCode(), enterprise.getType())){
            throw new BusinessException(OpenErrorCode.ENTERPRISE_TYPE_ERROR);
        }
    }

    /**
     * 根据rkSuId查询
     *
     * @param rkSuId
     * @return
     */
    private ErpClientDTO getByRkSuId(Long rkSuId) {
        ErpClientDTO erpClient = erpClientApi.selectByRkSuId(rkSuId);
        if (ObjectUtil.isNull(erpClient)) {
            throw new BusinessException(OpenErrorCode.ERP_ENTERPRISE_NOT_EXIST);
        }
        return erpClient;
    }

    /**
     * 校验ERP对接企业同步状态
     * 同步状态为 已开启，则对接级别、流向级别不能同时为空或0
     *
     * @param request
     */
    private void checkSyncStatusAndDepthAndFlow(ErpClientSaveRequest request) {
        if (ObjectUtil.equal(ErpEnterpriseSyncStatus.SYNCING.getCode(), request.getSyncStatus())) {
            boolean depthFlag = true;
            boolean flowFlag = true;
            if (ObjectUtil.isNull(request.getDepth()) || request.getDepth().intValue() == 0) {
                depthFlag = false;
            }
            if (ObjectUtil.isNull(request.getFlowLevel()) || request.getFlowLevel().intValue() == 0) {
                flowFlag = false;
            }
            if(!depthFlag && !flowFlag){
                throw new BusinessException(OpenErrorCode.DEPTH_ERROR);
            }
        }
    }

    /**
     * 保存，校验企业信息
     *
     * @param request
     * @return
     */
    private void checkEnterprise(ErpClientSaveRequest request) {
        // 校验企业信息
        Long rkSuId = request.getRkSuId();
        EnterpriseDTO enterprise = getEnterprise(Long.valueOf(rkSuId));
        request.setClientName(enterprise.getName());
        request.setLicenseNumber(enterprise.getLicenseNumber());
        // 校验erp企业信息
        ErpClientDTO erpClient = erpClientApi.selectByRkSuId(rkSuId);
        if(ObjectUtil.isNotNull(erpClient)){
            throw new BusinessException(ResultCode.FAILED, "erp对接企业信息已存在");
        }
        // 校验erp父类企业信息
        if (ObjectUtil.isNotNull(request.getSuId())) {
            // 父类企业不为空，分公司编码必填
//            if(StrUtil.isBlank(request.getSuDeptNo())){
//                throw new BusinessException(ResultCode.FAILED, "父类企业不为空，分公司编码必填");
//            }
            ErpClientDTO erpClientParent = erpClientApi.selectByRkSuId(request.getSuId());
            if(ObjectUtil.isNull(erpClientParent)){
                throw new BusinessException(ResultCode.FAILED, "此父类企业信息不存在");
            }
            // 分公司编码重复校验
            List<ErpClientDTO> erpClientList = erpClientApi.selectBySuId(request.getSuId());
            if (CollUtil.isEmpty(erpClientList)) {
                throw new BusinessException(ResultCode.FAILED, "此父类企业信息不存在");
            }
            List<ErpClientDTO> suDeptNoRepeatList = erpClientList.stream().filter(client -> ObjectUtil.equal(client.getSuDeptNo(), request.getSuDeptNo())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(suDeptNoRepeatList)) {
                throw new BusinessException(ResultCode.FAILED, "此分公司编码已存在");
            }
            request.setClientKey(erpClientParent.getClientKey());
            request.setClientSecret(erpClientParent.getClientSecret());
        } else {
            request.setSuId(rkSuId);
        }
    }

    /**
     * 编辑，校验分公司编码
     *
     * @param request
     * @param erpClient
     */
    private void checkErpClient(ErpClientSaveRequest request, ErpClientDTO erpClient) {
        if(!ObjectUtil.equal(erpClient.getSuId(), erpClient.getRkSuId())){
            // 分公司编码重复校验
            List<ErpClientDTO> erpClientList = erpClientApi.selectBySuId(erpClient.getSuId());
            if (CollUtil.isEmpty(erpClientList)) {
                throw new BusinessException(ResultCode.FAILED, "此父类企业信息不存在");
            }
            List<ErpClientDTO> suDeptNoRepeatList = erpClientList.stream().filter(client -> !ObjectUtil.equal(client.getRkSuId(), erpClient.getRkSuId()) && ObjectUtil.equal(client.getSuDeptNo(), request.getSuDeptNo())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(suDeptNoRepeatList)) {
                throw new BusinessException(ResultCode.FAILED, "此分公司编码已存在");
            }
        }
    }


    /**
     * 根据id查询crm企业信息
     *
     * @param crmId
     * @return
     */
    private CrmEnterpriseDTO getCrmEnterprise(Long crmId, Long oldId) {
        if (ObjectUtil.isNull(crmId) || 0 == crmId.intValue()) {
            return null;
        }
        CrmEnterpriseDTO crmEnterprise = crmEnterpriseApi.getCrmEnterpriseById(crmId);
        if (ObjectUtil.isNull(crmEnterprise)) {
            throw new BusinessException(ResultCode.FAILED, "此crm企业信息不存在");
        }
        if (!ObjectUtil.equal(crmEnterprise.getSupplyChainRole(), CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode())) {
            throw new BusinessException(ResultCode.FAILED, "此crm企业类型不是经销商");
        }
        if (!ObjectUtil.equal(crmEnterprise.getBusinessCode(), 1)){
            throw new BusinessException(ResultCode.FAILED, "此crm企业已失效");
        }
        // crmEnterpriseId 是否重复
        ErpClientDTO oldErpClient = erpClientApi.getByCrmEnterpriseId(crmId);
        if (ObjectUtil.isNotNull(oldErpClient) && !ObjectUtil.equal(oldId, oldErpClient.getId())) {
            throw new BusinessException(ResultCode.FAILED, "此crm企业在erp对接企业信息中已关联，请选择其他crm企业");
        }

        return crmEnterprise;
    }

    /**
     * 根据id查询企业信息
     *
     * @param eid
     * @return
     */
    private EnterpriseDTO getEnterprise(Long eid) {
        EnterpriseDTO enterprise = enterpriseApi.getById(eid);
        if (ObjectUtil.isNull(enterprise)) {
            throw new BusinessException(OpenErrorCode.ENTERPRISE_NOT_EXIST);
        }
        return enterprise;
    }

    /**
     * 更新企业信息的ERP对接级别
     * 条件：同步状态“已开启”、并且对接级别非“未对接”、并且与企业信息中状态不同
     * @param request
     */
    private void updateEnterprise(ErpClientSaveRequest request) {
        Long eid = Long.valueOf(request.getRkSuId());
        EnterpriseDTO enterprise = getEnterprise(eid);
        if (ObjectUtil.equal(ErpEnterpriseSyncStatus.SYNCING.getCode(), request.getSyncStatus())
            && !ObjectUtil.equal(DepthEnum.NO.getCode(), request.getDepth())
            && !ObjectUtil.equal(request.getDepth(), enterprise.getErpSyncLevel())) {
            UpdateErpStatusRequest enterpriseRequest = new UpdateErpStatusRequest();
            enterpriseRequest.setId(eid);
            enterpriseRequest.setErpSyncLevel(request.getDepth());
            enterpriseApi.updateErpStatus(enterpriseRequest);
        }
    }

}
