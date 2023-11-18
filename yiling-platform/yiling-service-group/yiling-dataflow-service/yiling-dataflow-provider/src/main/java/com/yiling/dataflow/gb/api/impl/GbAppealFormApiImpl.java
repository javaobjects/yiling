package com.yiling.dataflow.gb.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.service.CrmDepartmentAreaRelationService;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.gb.api.GbAppealFormApi;
import com.yiling.dataflow.gb.bo.GbAppealFormEsbInfoBO;
import com.yiling.dataflow.gb.bo.GbAppealFormFlowStatisticBO;
import com.yiling.dataflow.gb.dto.GbAppealFormDTO;
import com.yiling.dataflow.gb.dto.request.DeleteGbAppealFormRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealFormExecuteEditDetailRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealFormUpdateExecuteStatusRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealSubstractCancleRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealSubstractMateFlowRequest;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormFlowStatisticPageRequest;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormListPageRequest;
import com.yiling.dataflow.gb.dto.request.SaveGbAppealFormRequest;
import com.yiling.dataflow.gb.dto.request.SaveGbAppealFormSaleReportMatchRequest;
import com.yiling.dataflow.gb.entity.GbAppealAllocationDO;
import com.yiling.dataflow.gb.entity.GbAppealFlowRelatedDO;
import com.yiling.dataflow.gb.entity.GbAppealFlowStatisticDO;
import com.yiling.dataflow.gb.entity.GbAppealFormDO;
import com.yiling.dataflow.gb.entity.GbOrderDO;
import com.yiling.dataflow.gb.enums.GbAllocationTypeEnum;
import com.yiling.dataflow.gb.enums.GbDataExecStatusEnum;
import com.yiling.dataflow.gb.enums.GbErrorEnum;
import com.yiling.dataflow.gb.enums.GbExecTypeEnum;
import com.yiling.dataflow.gb.enums.GbOrderExecStatusEnum;
import com.yiling.dataflow.gb.service.GbAppealAllocationService;
import com.yiling.dataflow.gb.service.GbAppealFlowRelatedService;
import com.yiling.dataflow.gb.service.GbAppealFlowStatisticService;
import com.yiling.dataflow.gb.service.GbAppealFormService;
import com.yiling.dataflow.gb.service.GbOrderService;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.service.FlowWashSaleReportService;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2023/5/12
 */
@Slf4j
@DubboService
public class GbAppealFormApiImpl implements GbAppealFormApi {

    @Autowired
    private GbAppealFormService gbAppealFormService;
    @Autowired
    private GbAppealFlowRelatedService gbAppealFlowRelatedService;
    @Autowired
    private GbAppealFlowStatisticService gbAppealFlowStatisticService;
    @Autowired
    private GbAppealAllocationService gbAppealAllocationService;
    @Autowired
    private GbOrderService gbOrderService;
    @Autowired
    private CrmDepartmentAreaRelationService crmDepartmentAreaRelationService;
    @Autowired
    private FlowWashSaleReportService flowWashSaleReportService;

    @DubboReference
    EsbEmployeeApi esbEmployeeApi;
    @DubboReference
    EsbOrganizationApi esbOrganizationApi;
    @DubboReference
    CrmEnterpriseApi crmEnterpriseApi;

    @Override
    public boolean substractMateFlow(GbAppealSubstractMateFlowRequest request) {
        log.info("开始团购处理扣减, request:{}", JSONUtil.toJsonStr(request));

        Long appealFormId = request.getAppealFormId();
        // 团购处理状态校验
        GbAppealFormDO gbAppealFormDO = gbAppealFormService.getById(appealFormId);
        if (!substractGbAppealFormCheck(gbAppealFormDO, request.getExecType())) {
            return false;
        }

        // 修改处理状态：自动处理中/手动处理中
        updateAppealFormExecuteStatus(request, gbAppealFormDO);

        // 团购销售计入人工号对应的esb人员、部门、省区
        GbAppealFormEsbInfoBO gbAppealFormEsbInfoBO = getEsbEmployeeInfo(gbAppealFormDO.getSellerEmpId());

        //todo 需要添加分布式锁

        // 团购处理申请与源流向关联信息
        List<GbAppealFlowRelatedDO> gbAppealFlowRelatedDOList = gbAppealFlowRelatedService.getListByAppealFormId(appealFormId);

        // 处理类型：1-自动 2-人工
        Integer execType = request.getExecType();
        // 根据自动、手动处理类型获取源流向id列表
        List<Long> flowWashIds = buildFlowWashIds(request, gbAppealFlowRelatedDOList, execType);

        // 根据源流向id查询源流向扣减统计, 获取源流向剩余未匹配数量合计
        List<GbAppealFlowStatisticDO> gbAppealFlowStatisticDOList = gbAppealFlowStatisticService.getListByFlowWashIds(flowWashIds);
        BigDecimal flowData = gbAppealFlowStatisticDOList.stream().map(GbAppealFlowStatisticDO::getUnMatchQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);

        // 手动处理的，设置用户输入的扣减数量
        if (GbExecTypeEnum.ARTIFICIAL.getCode().equals(execType)) {
            gbAppealFormDO.setGbQuantity(request.getDoMatchQuantity());
        }

        // 数量校验
        if (!substractTotalQuantityCheck(gbAppealFormDO, flowData)) {
            return false;
        }

        // 源流向数据
        Map<Long, FlowWashSaleReportDTO> flowWashSaleReportMap = getFlowWashSaleReportMap(flowWashIds);

        // 扣减
        boolean substractFlag = gbAppealFormService.substractMateFlow(gbAppealFormDO, gbAppealFlowRelatedDOList, gbAppealFlowStatisticDOList, flowWashSaleReportMap, gbAppealFormEsbInfoBO, execType, request.getOpUserId(), request.getOpTime());

        // 团购处理失败
        if (!substractFlag && GbExecTypeEnum.AUTO.getCode().equals(execType)) {
            updateExecuteStatus(request, gbAppealFormDO.getId());
        }

        // todo 释放锁

        return substractFlag;
    }

    private void updateExecuteStatus(GbAppealSubstractMateFlowRequest request, Long id) {
        GbAppealFormUpdateExecuteStatusRequest updateExecuteStatusRequest = new GbAppealFormUpdateExecuteStatusRequest();
        updateExecuteStatusRequest.setId(id);
        updateExecuteStatusRequest.setExecType(request.getExecType());
        updateExecuteStatusRequest.setDataExecStatus(GbDataExecStatusEnum.FAIL.getCode());
        updateExecuteStatusRequest.setOpUserId(request.getOpUserId());
        updateExecuteStatusRequest.setOpTime(request.getOpTime());
        gbAppealFormService.updateByIdAndExecuteStatus(updateExecuteStatusRequest);
    }

    private Map<Long, FlowWashSaleReportDTO> getFlowWashSaleReportMap(List<Long> flowWashIds) {
        Map<Long, FlowWashSaleReportDTO> flowWashSaleReportMap = new HashMap<>();
        List<FlowClassifyEnum> flowClassifyEnumList = new ArrayList<>();
        flowClassifyEnumList.add(FlowClassifyEnum.NORMAL);
        flowClassifyEnumList.add(FlowClassifyEnum.SUPPLY_TRANS_MONTH_FLOW);
        List<FlowWashSaleReportDTO> flowWashSaleReportList = flowWashSaleReportService.listByFlowSaleWashIds(flowWashIds, flowClassifyEnumList);
        if (CollUtil.isNotEmpty(flowWashSaleReportList)) {
            flowWashSaleReportMap = flowWashSaleReportList.stream().collect(Collectors.toMap(FlowWashSaleReportDTO::getFlowSaleWashId, Function.identity()));
        }
        return flowWashSaleReportMap;
    }

    private boolean substractTotalQuantityCheck(GbAppealFormDO gbAppealFormDO, BigDecimal flowData) {
        if (gbAppealFormDO.getGbQuantity().compareTo(flowData) > 0) {
            log.info("团购商品数量大于匹配原流向总数量，appealFormId={}", gbAppealFormDO.getId());
            GbAppealFormDO failAppealForm = new GbAppealFormDO();
            failAppealForm.setId(gbAppealFormDO.getId());
            failAppealForm.setDataExecStatus(GbDataExecStatusEnum.FAIL.getCode());
            gbAppealFormService.updateById(failAppealForm);
            return false;
        }
        return true;
    }

    private List<Long> buildFlowWashIds(GbAppealSubstractMateFlowRequest request, List<GbAppealFlowRelatedDO> gbAppealFlowRelatedDOList, Integer execType) {
        List<Long> flowWashIds = new ArrayList<>();
        if (GbExecTypeEnum.AUTO.getCode().equals(execType)) {
            // 日程自动触发处理团购处理id匹配的所有源流向扣减
            flowWashIds = gbAppealFlowRelatedDOList.stream().map(e -> e.getFlowWashId()).collect(Collectors.toList());
        } else {
            // 用户操作加入处理团购处理id、源流向id对应的单个源流向扣减
            flowWashIds = ListUtil.toList(request.getFlowWashId());
        }
        return flowWashIds;
    }

    private GbAppealFormEsbInfoBO getEsbEmployeeInfo(String empId) {
        GbAppealFormEsbInfoBO gbAppealFormEsbInfoBO = new GbAppealFormEsbInfoBO();
        // esb员工信息
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(empId);
        gbAppealFormEsbInfoBO.setEsbEmployeeDTO(esbEmployeeDTO);
        if (ObjectUtil.isNotNull(esbEmployeeDTO)) {
            // 根据部门id 获取部门
            EsbOrganizationDTO organizationDTO = esbOrganizationApi.getByOrgId(esbEmployeeDTO.getDeptId());
            gbAppealFormEsbInfoBO.setOrganizationDTO(organizationDTO);
            // 根据业务部门、业务省区 获取省区
            String provinceArea = crmDepartmentAreaRelationService.getProvinceAreaByThreeParms(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince());
            gbAppealFormEsbInfoBO.setProvinceArea(provinceArea);
        }
        return gbAppealFormEsbInfoBO;
    }

    private void updateAppealFormExecuteStatus(GbAppealSubstractMateFlowRequest request, GbAppealFormDO gbAppealFormDO) {
        GbAppealFormUpdateExecuteStatusRequest appealFormRequest = new GbAppealFormUpdateExecuteStatusRequest();
        appealFormRequest.setId(gbAppealFormDO.getId());
        if (GbExecTypeEnum.AUTO.getCode().equals(request.getExecType())) {
            appealFormRequest.setExecType(GbExecTypeEnum.AUTO.getCode());
            appealFormRequest.setDataExecStatus(GbDataExecStatusEnum.AUTO.getCode());
        } else {
            appealFormRequest.setExecType(GbExecTypeEnum.ARTIFICIAL.getCode());
            appealFormRequest.setDataExecStatus(GbDataExecStatusEnum.ARTIFICIAL.getCode());
        }
        appealFormRequest.setOpUserId(request.getOpUserId());
        appealFormRequest.setOpTime(request.getOpTime());
        gbAppealFormService.updateByIdAndExecuteStatus(appealFormRequest);
    }

    private boolean substractGbAppealFormCheck(GbAppealFormDO gbAppealFormDO, Integer execType) {
        Long appealFormId = gbAppealFormDO.getId();
        if (ObjectUtil.isNull(gbAppealFormDO)) {
            log.error("查询的团购处理数据为空，appealFormId={}", appealFormId);
            return false;
        }
        if (GbExecTypeEnum.AUTO.getCode().equals(execType)) {
            if (!GbExecTypeEnum.AUTO.getCode().equals(gbAppealFormDO.getExecType())) {
                log.info("查询的团购处理类型不是“自动”，appealFormId={}", appealFormId);
                return false;
            }
            if (!GbDataExecStatusEnum.UN_START.getCode().equals(gbAppealFormDO.getDataExecStatus())) {
                log.info("查询的团购处理数据状态不是“未开始”，appealFormId={}", appealFormId);
                return false;
            }
        } else if (GbExecTypeEnum.ARTIFICIAL.getCode().equals(execType)) {
            if (!GbExecTypeEnum.ARTIFICIAL.getCode().equals(gbAppealFormDO.getExecType())) {
                log.info("查询的团购处理类型不是“人工”，appealFormId={}", appealFormId);
                return false;
            }
            if (!GbDataExecStatusEnum.ARTIFICIAL.getCode().equals(gbAppealFormDO.getDataExecStatus())) {
                log.info("查询的团购处理数据状态不是“手动处理中”，appealFormId={}", appealFormId);
            }
        }
        if (gbAppealFormDO.getFlowMatchNumber() == 0) {
            log.info("查询的团购处理匹配原流向为空，appealFormId={}", appealFormId);
            return false;
        }
        return true;
    }


    @Override
    public Page<GbAppealFormDTO> listPage(QueryGbAppealFormListPageRequest request) {
        return PojoUtils.map(gbAppealFormService.listPage(request), GbAppealFormDTO.class);
    }

    @Override
    public GbAppealFormDTO getById(Long id) {
        Assert.notNull(id, "参数 id 不能为空");
        return PojoUtils.map(gbAppealFormService.getById(id), GbAppealFormDTO.class);
    }

    @Override
    public boolean updateByIdAndExecuteStatus(GbAppealFormUpdateExecuteStatusRequest request) {
        Assert.notNull(request.getId(), "参数 id 不能为空");
        Assert.notNull(request.getDataExecStatus(), "参数 dataExecStatus 不能为空");
        return gbAppealFormService.updateByIdAndExecuteStatus(request);
    }

    @Override
    public Page<GbAppealFormFlowStatisticBO> flowStatisticListPage(QueryGbAppealFormFlowStatisticPageRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getAppealFormId(), "参数 appealFormId 不能为空");
        return gbAppealFormService.flowStatisticListPage(request);
    }

    @Override
    public BigDecimal getTotalFlowMatchQuantityByAppealFormId(Long appealFormId) {
        Assert.notNull(appealFormId, "参数 appealFormId 不能为空");
        return gbAppealFormService.getTotalFlowMatchQuantityByAppealFormId(appealFormId);
    }

    @Override
    public boolean editGbAppealAllocation(GbAppealFormExecuteEditDetailRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getAppealAllocationId(), "参数 appealAllocationId 不能为空");
        Assert.notNull(request.getQuantity(), "参数 quantity 不能为空");
        Assert.notBlank(request.getDepartment(), "参数 department 不能为空");

        log.info("[团购处理], 编辑扣减/新增数据, request:{}", JSONUtil.toJsonStr(request));

        // todo 加锁

        // 团购处理结果
        GbAppealAllocationDO appealAllocation = gbAppealAllocationService.getById(request.getAppealAllocationId());
        if (ObjectUtil.isNull(appealAllocation)) {
            log.error("查询的团购处理结果数据为空，appealAllocationId:{}", request.getAppealAllocationId());
            return false;
        }

        // 团购处理状态校验
        GbAppealFormDO gbAppealFormDO = gbAppealFormService.getById(appealAllocation.getAppealFormId());
        if (!substractGbAppealFormCheck(gbAppealFormDO, request.getExecType())) {
            return false;
        }

        // 需要一起更新的团购处理结果id
        buildNegateDetailId(request, appealAllocation);

        // 根据源流向id查询源流向扣减统计, 获取源流向剩余未匹配数量合计
        GbAppealFlowStatisticDO appealFlowStatistic = gbAppealFlowStatisticService.getByFlowWashId(appealAllocation.getFlowWashId());
        BigDecimal flowData = appealFlowStatistic.getUnMatchQuantity();

        // 团购与该源流向已匹配数量
        GbAppealFlowRelatedDO appealFlowRelated = gbAppealFlowRelatedService.getByAppealFormIdAndFlowWashId(appealAllocation.getAppealFormId(), appealAllocation.getFlowWashId());

        // 已扣减数量
        BigDecimal quantityOld = appealFlowRelated.getMatchQuantity();
        // 新的扣减数量
        BigDecimal quantityNew = request.getQuantity();
        // 扣减数量差
        BigDecimal difQuantity = quantityNew.abs().subtract(quantityOld.abs());


        // 手动处理的，设置用户输入的扣减数量，编辑的设置扣减数量差进行扣减/归还
        if (GbExecTypeEnum.ARTIFICIAL.getCode().equals(request.getExecType())) {
            gbAppealFormDO.setGbQuantity(difQuantity);
        }

        // 数量校验
        if (!substractTotalQuantityCheck(gbAppealFormDO, flowData)) {
            return false;
        }

        // 扣减/归还、更新扣减/增加结果
        boolean editFlag = gbAppealFormService.editGbAppealAllocation(request, gbAppealFormDO, appealAllocation, appealFlowStatistic, appealFlowRelated);

        // todo 释放锁

        return editFlag;

    }

    private void buildNegateDetailId(GbAppealFormExecuteEditDetailRequest request, GbAppealAllocationDO appealAllocation) {
        Integer allocationType;
        if (GbAllocationTypeEnum.DEDUCT.getCode().equals(appealAllocation.getAllocationType())) {
            allocationType = GbAllocationTypeEnum.ADD.getCode();
        } else {
            allocationType = GbAllocationTypeEnum.DEDUCT.getCode();
        }
        List<GbAppealAllocationDO> allocationList = gbAppealAllocationService.listByAppealFormIdAndFlowWashId(request.getAppealFormId(), appealAllocation.getFlowWashId(), allocationType);
        GbAppealAllocationDO negateDetail = allocationList.stream().filter(o -> !appealAllocation.getId().equals(o.getId())).findFirst().get();
        request.setNegateAllocationId(negateDetail.getId());
    }

    @Override
    public boolean saveList(SaveGbAppealFormRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getGbOrderIdList(), "参数 gbOrderIdList 不能为空");

        // 校验流向日程锁定团购、非锁团购状态，阶段5进行中~阶段8进行中才能手动处理团购
        FlowMonthWashControlDTO flowMonthWashControlDTO = washControlGbStatusCheck(null);
        if (ObjectUtil.isNull(flowMonthWashControlDTO)) {
            log.warn("当前没有可以用的团购处理日程, gbOrderId:{}", request.getGbOrderIdList().toString());
            return false;
        }

        // 团购数据详情
        List<GbOrderDO> gbOrderList = gbOrderService.getByIdList(request.getGbOrderIdList());
        if (CollUtil.isEmpty(gbOrderList)) {
            log.warn("选择的团购数据不存在, gbOrderId:{}", request.getGbOrderIdList().toString());
            return false;
        }

        List<Long> gbOrderFinishFormIds = gbOrderList.stream().filter(o -> GbOrderExecStatusEnum.FINISH.getCode().equals(o.getExecStatus())).map(GbOrderDO::getId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(gbOrderFinishFormIds)) {
            log.warn("此团购数据已处理, gbOrderId:{}", request.getGbOrderIdList().toString());
            return false;
        }

        // 机构省份代码
        Map<Long, String> crmEnterpriseMap = getCrmEnterpriseMap(gbOrderList);

        // 团购销售计入人工号对应的esb人员
        Map<String, EsbEmployeeDTO> esbEmployeeMap = getEsbEmployeeMap(gbOrderList);

        return gbAppealFormService.saveList(request, flowMonthWashControlDTO, gbOrderList, crmEnterpriseMap, esbEmployeeMap);
    }

    private Map<String, EsbEmployeeDTO> getEsbEmployeeMap(List<GbOrderDO> gbOrderList) {
        Map<String, EsbEmployeeDTO> esbEmployeeMap = new HashMap<>();
        List<String> empIdList = gbOrderList.stream().map(GbOrderDO::getSellerEmpId).distinct().collect(Collectors.toList());
        List<EsbEmployeeDTO> esbEmployeeList = esbEmployeeApi.listByEmpIds(empIdList);
        if (CollUtil.isNotEmpty(esbEmployeeList)) {
            esbEmployeeMap = esbEmployeeList.stream().collect(Collectors.toMap(EsbEmployeeDTO::getEmpId, Function.identity()));
        }
        return esbEmployeeMap;
    }

    private Map<Long, String> getCrmEnterpriseMap(List<GbOrderDO> gbOrderList) {
        Map<Long, String> crmEnterpriseMap = new HashMap<>();
        List<Long> crmIdLIst = gbOrderList.stream().map(GbOrderDO::getCrmId).distinct().collect(Collectors.toList());
        List<CrmEnterpriseDTO> crmEnterpriseList = crmEnterpriseApi.getCrmEnterpriseListById(crmIdLIst);
        if (CollUtil.isNotEmpty(crmEnterpriseList)) {
            crmEnterpriseMap = crmEnterpriseList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o.getProvinceCode(), (k1,k2) -> k1));
        }
        return crmEnterpriseMap;
    }

    @Override
    public boolean cancleSubstract(GbAppealSubstractCancleRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getAppealFormId(), "参数 appealFormId 不能为空");
        Assert.notNull(request.getFlowWashId(), "参数 flowWashId 不能为空");

        // todo 加锁


        Long appealFormId = request.getAppealFormId();
        Long flowWashId = request.getFlowWashId();

        // 团购处理状态校验
        GbAppealFormDO gbAppealFormDO = gbAppealFormService.getById(appealFormId);
        if (!substractGbAppealFormCheck(gbAppealFormDO, GbExecTypeEnum.ARTIFICIAL.getCode())) {
            return false;
        }

        // 处理结果明细扣减、新增
        List<GbAppealAllocationDO> appealAllocationList = gbAppealAllocationService.listByAppealFormIdAndFlowWashId(appealFormId, flowWashId, null);
        if (CollUtil.isEmpty(appealAllocationList)) {
            throw new ServiceException("此团购处理与当前源流向没有处理加过, 不能取消加入, 请确认，团购处理ID:" + appealFormId + ", 源流向ID:" + flowWashId);
        }

        // 关联流向表 匹配数量
        GbAppealFlowRelatedDO appealFlowRelated = gbAppealFlowRelatedService.getByAppealFormIdAndFlowWashId(appealFormId, flowWashId);
        if (ObjectUtil.isNull(appealFlowRelated)) {
            throw new ServiceException("此团购处理与当前源流向未进行源流向匹配，团购处理ID:" + appealFormId + ", 源流向ID:" + flowWashId);
        }
        // 源流向数量统计
        GbAppealFlowStatisticDO appealFlowStatistic = gbAppealFlowStatisticService.getByFlowWashId(flowWashId);
        if (ObjectUtil.isNull(appealFlowStatistic)) {
            throw new ServiceException("当前源流向数量匹配统计信息不存在，团购处理ID:" + appealFormId + ", 源流向ID:" + flowWashId);
        }

        boolean cancleFlag =  gbAppealFormService.cancleSubstract(request, appealAllocationList, appealFlowRelated, appealFlowStatistic);

        // todo 释放锁

        return cancleFlag;
    }

    @Override
    public boolean deleteAppealForm(DeleteGbAppealFormRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getId(), "参数 id 不能为空");

        //        团购处理删除步骤：
        // 源流向id加锁
        //        1.查询当前团购处理，判断状态是否可以当前操作
        //《事务》
        //        2.查询流向关联表，获取团购扣减当前流向数量
        //        3.查询流向数量统计表，扣减归还
        //        4.流向关联表删除
        //        5.扣减、新增结果删除
        //        6.团购处理主表删除
        //        7.团购数据主表状态改成未处理
        //《事务》
        // 释放源流向id锁

        // todo 加锁


        Long appealFormId = request.getId();
        // 查询团购处理主表
        GbAppealFormDO gbAppealFormDO = gbAppealFormService.getById(appealFormId);
        if (ObjectUtil.isNull(gbAppealFormDO)) {
            throw new ServiceException("此团购处理信息不存在，请确认，团购处理ID:" + appealFormId);
        }
        if (GbDataExecStatusEnum.AUTO.getCode().equals(gbAppealFormDO.getDataExecStatus())) {
            throw new ServiceException(ResultCode.FAILED, "此团购处理状态是“自动处理中”，不能操作“删除”");
        }
        // 查询团购数据主表
        GbOrderDO gbOrderDO = gbOrderService.getById(gbAppealFormDO.getGbOrderId());

        // 根据团购处理id查询关联表的已匹配源流向
        List<GbAppealFlowRelatedDO> gbAppealFlowRelatedList = gbAppealFlowRelatedService.getListByAppealFormId(appealFormId);

        // 流向扣减统计表
        List<GbAppealFlowStatisticDO> gbAppealFlowStatisticList = new ArrayList<>();
        if (CollUtil.isNotEmpty(gbAppealFlowRelatedList)) {
            List<Long> flowWashIds = gbAppealFlowRelatedList.stream().map(GbAppealFlowRelatedDO::getFlowWashId).distinct().collect(Collectors.toList());
            gbAppealFlowStatisticList = gbAppealFlowStatisticService.getListByFlowWashIds(flowWashIds);
        }

        // 处理结果明细表
        List<GbAppealAllocationDO> gbAppealAllocationList = new ArrayList<>();
        gbAppealAllocationList = gbAppealAllocationService.listByAppealFormIdAndAllocationType(appealFormId, 0);

        // 删除团购处理
        boolean deleteFlag = gbAppealFormService.deleteAppealForm(request, gbAppealFormDO, gbOrderDO, gbAppealFlowRelatedList, gbAppealFlowStatisticList, gbAppealAllocationList);

        // todo 释放锁

        return deleteFlag;
    }

    @Override
    public boolean selectFlowForMatch(SaveGbAppealFormSaleReportMatchRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notEmpty(request.getFlowWashIdList(), "参数 flowWashIdList 不能为空");

        // todo 加锁

        try {
            // 查询团购处理状态，自动处理中 不能选择源流向
            GbAppealFormDO appealFormOld = gbAppealFormService.getById(request.getAppealFormId());
            if (GbDataExecStatusEnum.AUTO.getCode().equals(appealFormOld.getDataExecStatus())) {
                throw new BusinessException(ResultCode.FAILED, "此团购处理状态不是“未开始”或“手动处理中”，不能选择源流向保存");
            }
            request.setFlowMatchNumber(appealFormOld.getFlowMatchNumber());

            // 是否已经匹配过，根据团购处理id查询关联表的已匹配源流向, key -> flowWashId, value -> 团购处理源流向关联id
            Map<Long, Long> relatedMap = new HashMap<>();
            List<GbAppealFlowRelatedDO> gbAppealFlowRelatedList = gbAppealFlowRelatedService.getListByAppealFormId(request.getAppealFormId());
            if (CollUtil.isNotEmpty(gbAppealFlowRelatedList)) {
                relatedMap = gbAppealFlowRelatedList.stream().collect(Collectors.toMap(o -> o.getFlowWashId(), o -> o.getId(), (k1, k2) -> k1));
            }

            List<Long> existList = new ArrayList<>();
            for (Long flowWashId : request.getFlowWashIdList()) {
                Long exist = relatedMap.get(flowWashId);
                if (ObjectUtil.isNotNull(exist)) {
                    existList.add(flowWashId);
                }
            }
            if (CollUtil.isNotEmpty(existList)) {
                throw new BusinessException(ResultCode.FAILED, "以下源流向已经选择保存过, 不能重复选择保存, 源流向id：" + existList.toString());
            }

            // 源流向数据
            Map<Long, FlowWashSaleReportDTO> flowSaleReportMap = getFlowWashSaleReportMap(request.getFlowWashIdList());
            if (MapUtil.isEmpty(flowSaleReportMap)) {
                throw new BusinessException(ResultCode.FAILED, "源流向数据不存在, 不能选择源流向保存, 源流向id：" + request.getFlowWashIdList());
            }
            if (flowSaleReportMap.keySet().size() < request.getFlowWashIdList().size()) {
                Set<Long> flowWashIdSet = flowSaleReportMap.keySet();
                List<Long> flowWashIdNotExistList = request.getFlowWashIdList().stream().filter(flowWashId -> !flowWashIdSet.contains(flowWashId)).distinct().collect(Collectors.toList());
                throw new BusinessException(ResultCode.FAILED, "选择的部分源流向不存在，请确认，不存在的源流向id：" + flowWashIdNotExistList.toString());
            }

            return gbAppealFormService.selectFlowForMatch(request, appealFormOld, flowSaleReportMap);

        } finally {
            // todo 释放锁

        }
    }

    @Override
    public boolean deleteFlowForMatch(GbAppealSubstractCancleRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getAppealFormId(), "参数 appealFormId 不能为空");
        Assert.notNull(request.getFlowWashId(), "参数 flowWashId 不能为空");

        // 源流向id加锁
        //《事务》
        //        查询流向数量统计表，扣减归还
        //        扣减、新增结果删除
        //        流向关联表删除
        //        团购处理申请主表更新源流向匹配条数
        //《事务》
        // 释放源流向id锁

        // todo 加锁

        Long appealFormId = request.getAppealFormId();
        Long flowWashId = request.getFlowWashId();
        Long opUserId = request.getOpUserId();
        Date opTime = request.getOpTime();
        // 查询团购处理主表
        GbAppealFormDO gbAppealFormDO = gbAppealFormService.getById(appealFormId);
        if (ObjectUtil.isNull(gbAppealFormDO)) {
            throw new ServiceException("此团购处理信息不存在，请确认，团购处理ID:" + appealFormId);
        }
        if (GbDataExecStatusEnum.AUTO.getCode().equals(gbAppealFormDO.getDataExecStatus())) {
            throw new ServiceException(ResultCode.FAILED, "此团购处理状态是“自动处理中”，不能操作“删除”");
        }

        // 根据团购处理id查询关联表的已匹配源流向
        GbAppealFlowRelatedDO gbAppealFlowRelated = gbAppealFlowRelatedService.getByAppealFormIdAndFlowWashId(appealFormId, flowWashId);
        if (ObjectUtil.isNull(gbAppealFlowRelated)) {
            log.error("源流向匹配删除, 此团购处理匹配的当前源流向关联不存在, appealFormId:{}, flowWashId:{}", appealFormId, flowWashId);
            throw new BusinessException(GbErrorEnum.DELETE_MATCH_FLOW_ERROR);
        } else {
            // 已扣减数量
            BigDecimal matchQuantity = gbAppealFlowRelated.getMatchQuantity();
            if (matchQuantity.compareTo(BigDecimal.ZERO) == 0) {
                //                log.warn("源流向匹配删除, 此团购处理仅匹配了当前源流向未扣减, 直接删除关联表源流向, appealFormId:{}, flowWashId:{}", appealFormId, flowWashId);
                // 此团购处理仅匹配了当前源流向未扣减
                // 删除关联、更新团购处理匹配流向条数
                return gbAppealFormService.deleteAppealFlowRelatedForNotSubstract(appealFormId, flowWashId, gbAppealFlowRelated.getId(), opUserId, opTime);
            } else {
                // 此团购处理已匹配当前源流向、并且已扣减
                // 查询源流向扣减统计
                GbAppealFlowStatisticDO appealFlowStatistic = gbAppealFlowStatisticService.getByFlowWashId(flowWashId);
                if (ObjectUtil.isNull(appealFlowStatistic)) {
                    log.error("源流向匹配删除, 此团购处理匹配的当前源流向扣减统计不存在, appealFormId:{}, flowWashId:{}", appealFormId, flowWashId);
                    throw new BusinessException(GbErrorEnum.DELETE_MATCH_FLOW_ERROR);
                }
                // 根据团购处理、源流向ID 查询处理结果
                List<GbAppealAllocationDO> gbAppealAllocationList = gbAppealAllocationService.listByAppealFormIdAndFlowWashId(appealFormId, flowWashId, 0);
                if (ObjectUtil.isNull(gbAppealAllocationList)) {
                    log.error("源流向匹配删除, 此团购处理匹配的当前源流向扣减新增结果不存在, appealFormId:{}, flowWashId:{}", appealFormId, flowWashId);
                    throw new BusinessException(GbErrorEnum.DELETE_MATCH_FLOW_ERROR);
                }
                List<Long> appealAllocationIds = gbAppealAllocationList.stream().map(GbAppealAllocationDO::getId).collect(Collectors.toList());

                return   gbAppealFormService.deleteAppealFlowRelatedForSubstract(appealFormId, flowWashId, gbAppealFlowRelated.getId(), appealFlowStatistic.getId(), appealAllocationIds, matchQuantity, opUserId, opTime);
            }
        }

        // todo 释放锁

    }

    @Override
    public List<GbAppealFormDTO> listByFormIds(List<Long> formIds) {
        Assert.notEmpty(formIds, "参数 formIds 不能为空");
        return PojoUtils.map(gbAppealFormService.listByFormIds(formIds), GbAppealFormDTO.class);
    }

    @Override
    public FlowMonthWashControlDTO washControlGbStatusCheck(String month) {
        return gbAppealFormService.washControlGbStatusCheck(month);
    }


}
