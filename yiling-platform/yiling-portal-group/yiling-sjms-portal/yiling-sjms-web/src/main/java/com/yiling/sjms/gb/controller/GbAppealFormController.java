package com.yiling.sjms.gb.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
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
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.gb.api.GbAppealAllocationApi;
import com.yiling.dataflow.gb.api.GbAppealFlowRelatedApi;
import com.yiling.dataflow.gb.api.GbAppealFlowStatisticApi;
import com.yiling.dataflow.gb.api.GbAppealFormApi;
import com.yiling.dataflow.gb.api.GbOrderApi;
import com.yiling.dataflow.gb.bo.GbAppealFormFlowCountBO;
import com.yiling.dataflow.gb.bo.GbAppealFormFlowStatisticBO;
import com.yiling.dataflow.gb.dto.GbAppealAllocationDTO;
import com.yiling.dataflow.gb.dto.GbAppealFlowRelatedDTO;
import com.yiling.dataflow.gb.dto.GbAppealFlowStatisticDTO;
import com.yiling.dataflow.gb.dto.GbAppealFormDTO;
import com.yiling.dataflow.gb.dto.GbOrderDTO;
import com.yiling.dataflow.gb.dto.request.DeleteGbAppealFormRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealFormExecuteEditDetailRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealFormUpdateExecuteStatusRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealSubstractCancleRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealSubstractMateFlowRequest;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormAllocationPageRequest;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormFlowStatisticPageRequest;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormListPageRequest;
import com.yiling.dataflow.gb.dto.request.QueryGbOrderPageRequest;
import com.yiling.dataflow.gb.dto.request.SaveGbAppealFormRequest;
import com.yiling.dataflow.gb.dto.request.SaveGbAppealFormSaleReportMatchRequest;
import com.yiling.dataflow.gb.enums.GbAllocationTypeEnum;
import com.yiling.dataflow.gb.enums.GbDataExecStatusEnum;
import com.yiling.dataflow.gb.enums.GbExecTypeEnum;
import com.yiling.dataflow.gb.enums.GbOrderExecStatusEnum;
import com.yiling.dataflow.report.api.FlowWashReportApi;
import com.yiling.dataflow.report.api.FlowWashSaleReportApi;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.dto.request.FlowWashSaleReportPageRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.vo.EsbEmployVO;
import com.yiling.sjms.gb.form.GbAppealFormDetailAllocationPageForm;
import com.yiling.sjms.gb.form.GbAppealFormDetailFlowWashSalePageForm;
import com.yiling.sjms.gb.form.GbAppealFormDetailOrderForm;
import com.yiling.sjms.gb.form.GbAppealFormExecuteCancleSubstractForm;
import com.yiling.sjms.gb.form.GbAppealFormExecuteEditDetailForm;
import com.yiling.sjms.gb.form.GbAppealFormExecuteFlowWashForm;
import com.yiling.sjms.gb.form.GbAppealFormExecuteSubstractForm;
import com.yiling.sjms.gb.form.GbAppealFormOrgListForm;
import com.yiling.sjms.gb.form.GbAppealFormSaleReportDeleteSubstractForm;
import com.yiling.sjms.gb.form.GbAppealFormUpdateExecuteStatusForm;
import com.yiling.sjms.gb.form.QueryGbAllocationListPageForm;
import com.yiling.sjms.gb.form.QueryGbAppealFormAllocationDetailForm;
import com.yiling.sjms.gb.form.QueryGbAppealFormListPageForm;
import com.yiling.sjms.gb.form.QueryGbAppealFormSaleReportPageForm;
import com.yiling.sjms.gb.form.QueryGbOrderListPageForm;
import com.yiling.sjms.gb.form.SaveGbAppealFormSaleReportMatchForm;
import com.yiling.sjms.gb.form.SaveGbOrderForm;
import com.yiling.sjms.gb.vo.GbAllocationListPageVO;
import com.yiling.sjms.gb.vo.GbAppealFormAllocationPageDetailVO;
import com.yiling.sjms.gb.vo.GbAppealFormAllocationPageVO;
import com.yiling.sjms.gb.vo.GbAppealFormExecuteAddDetailVO;
import com.yiling.sjms.gb.vo.GbAppealFormExecuteEditDetailVO;
import com.yiling.sjms.gb.vo.GbAppealFormFlowWashSaleReportPageDetailVO;
import com.yiling.sjms.gb.vo.GbAppealFormFlowWashSaleReportPageVO;
import com.yiling.sjms.gb.vo.GbAppealFormListPageVO;
import com.yiling.sjms.gb.vo.GbAppealFormOrderVO;
import com.yiling.sjms.gb.vo.GbAppealFormOrgListVO;
import com.yiling.sjms.gb.vo.GbAppealFormSaleReportVO;
import com.yiling.sjms.gb.vo.GbOrderListPageVO;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2023/5/15
 */
@Slf4j
@RestController
@RequestMapping("/gb/appealHandle")
@Api(tags = "团购处理")
public class GbAppealFormController extends BaseController {

    @DubboReference
    GbAppealFormApi gbAppealFormApi;
    @DubboReference
    GbAppealFlowRelatedApi gbAppealFlowRelatedApi;
    @DubboReference
    GbOrderApi gbOrderApi;
    @DubboReference
    FlowWashSaleReportApi flowWashSaleReportApi;
    @DubboReference
    GbAppealFlowStatisticApi gbAppealFlowStatisticApi;
    @DubboReference
    GbAppealAllocationApi gbAppealAllocationApi;
    @DubboReference
    CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;
    @DubboReference
    EsbOrganizationApi esbOrganizationApi;
    @DubboReference
    FlowMonthWashControlApi flowMonthWashControlApi;
    @DubboReference
    EsbEmployeeApi esbEmployeeApi;
    @DubboReference
    BusinessDepartmentApi businessDepartmentApi;
    @DubboReference(timeout = 10 * 1000)
    FlowWashReportApi flowWashReportApi;
    @DubboReference
    SjmsUserDatascopeApi userDatascopeApi;
    @DubboReference
    UserApi userApi;

    @ApiOperation(value = "团购处理列表")
    @PostMapping("/listPage")
    public Result<Page<GbAppealFormListPageVO>> listPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryGbAppealFormListPageForm form) {
        QueryGbAppealFormListPageRequest request = PojoUtils.map(form, QueryGbAppealFormListPageRequest.class);
        Page<GbAppealFormDTO> gbAppealFormPage = gbAppealFormApi.listPage(request);

        Page<GbAppealFormListPageVO> page = new Page();
        page.setCurrent(gbAppealFormPage.getCurrent());
        page.setSize(gbAppealFormPage.getSize());
        page.setTotal(gbAppealFormPage.getTotal());
        if (ObjectUtil.isNull(gbAppealFormPage) || CollUtil.isEmpty(gbAppealFormPage.getRecords())) {
            return Result.success(page);
        }

        List<GbAppealFormDTO> records = gbAppealFormPage.getRecords();
        List<Long> idList = records.stream().map(GbAppealFormDTO::getId).distinct().collect(Collectors.toList());
        List<Long> gbOrderIdList = records.stream().map(GbAppealFormDTO::getGbOrderId).distinct().collect(Collectors.toList());
        // 流向已扣减数量
        Map<Long, BigDecimal> flowMatchQuantityMap = new HashMap<>();
        List<GbAppealFlowRelatedDTO> gbAppealFlowRelatedList = gbAppealFlowRelatedApi.getByAppealFormIdList(idList);
        if (CollUtil.isNotEmpty(gbAppealFlowRelatedList)) {
            // 每个团购处理申请的已扣减数量
            flowMatchQuantityMap = gbAppealFlowRelatedList.stream().collect(Collectors.groupingBy(GbAppealFlowRelatedDTO::getAppealFormId, Collectors.mapping(GbAppealFlowRelatedDTO::getMatchQuantity, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
        }
        // 团购信息
        Map<Long, GbOrderDTO> gbOrderMap = new HashMap<>();
        if (CollUtil.isNotEmpty(gbOrderIdList)) {
            List<GbOrderDTO> gbOrderList = gbOrderApi.getByIdList(gbOrderIdList);
            gbOrderMap = gbOrderList.stream().collect(Collectors.toMap(GbOrderDTO::getId, Function.identity()));
        }
        // 操作人信息
        Map<Long, String> userNameMap = getUserNameMap(records);

        List<GbAppealFormListPageVO> list = new ArrayList<>();
        GbAppealFormListPageVO vo;
        for (GbAppealFormDTO record : gbAppealFormPage.getRecords()) {
            vo = PojoUtils.map(record, GbAppealFormListPageVO.class);
            // 团购信息
            GbOrderDTO gbOrderDTO = gbOrderMap.get(record.getGbOrderId());
            if (ObjectUtil.isNotNull(gbOrderDTO)) {
                vo.setGbProcess(gbOrderDTO.getGbProcess());
                vo.setAuditStatus(gbOrderDTO.getAuditStatus());
                vo.setCheckStatus(gbOrderDTO.getCheckStatus());
                vo.setGbRemark(gbOrderDTO.getGbRemark());
            }
            // 已扣减数量
            BigDecimal flowMatchQuantity = flowMatchQuantityMap.get(record.getId());
            if (ObjectUtil.isNull(flowMatchQuantity)) {
                flowMatchQuantity = BigDecimal.ZERO;
            }
            vo.setDataMatchNumber(flowMatchQuantity.longValue());
            // 操作人
            String name = "";
            if (ObjectUtil.isNotNull(record.getLastUpdateUser()) && record.getLastUpdateUser().intValue() == 0) {
                name = "admin";
            } else {
                name = userNameMap.get(record.getLastUpdateUser());
            }
            vo.setLastUpdateUserName(name);
            list.add(vo);
        }
        page.setRecords(list);
        return Result.success(page);
    }

    private Map<Long, String> getUserNameMap(List<GbAppealFormDTO> list) {
        Map<Long, String> userNameMap = new HashMap<>();
        Set<Long> opUserIdSet = list.stream().filter(o -> ObjectUtil.isNotNull(o.getLastUpdateUser()) && o.getLastUpdateUser() > 0).map(GbAppealFormDTO::getLastUpdateUser).distinct().collect(Collectors.toSet());
        if (CollUtil.isEmpty(opUserIdSet)) {
            return userNameMap;
        }
        List<UserDTO> userList = userApi.listByIds(ListUtil.toList(opUserIdSet));
        if (CollUtil.isNotEmpty(userList)) {
            userNameMap = userList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o.getName(), (k1, k2) -> k1));
        }
        return userNameMap;
    }

    @ApiOperation(value = "查看-团购数据")
    @PostMapping("/detail/order")
    public Result<GbAppealFormOrderVO> detailOrder(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid GbAppealFormDetailOrderForm form) {
        GbAppealFormOrderVO gbAppealFormOrderVO = new GbAppealFormOrderVO();
        // 团购处理申请
        GbAppealFormDTO gbAppealFormDTO = gbAppealFormApi.getById(form.getAppealFormId());
        if (ObjectUtil.isNull(gbAppealFormDTO)) {
            throw new BusinessException(ResultCode.FAILED, "此在团购处理信息不存在，请确认");
        }
        // 团购详情
        GbOrderDTO gbOrderDTO = gbOrderApi.getById(gbAppealFormDTO.getGbOrderId());
        if (ObjectUtil.isNull(gbOrderDTO)) {
            throw new BusinessException(ResultCode.FAILED, "此在团购数据不存在，请确认");
        }
        gbAppealFormOrderVO = PojoUtils.map(gbOrderDTO, GbAppealFormOrderVO.class);
        // 销量计入人区办
        gbAppealFormOrderVO.setDistrictCountyCode(gbOrderDTO.getSellerDeptId().toString());
        gbAppealFormOrderVO.setDistrictCounty(gbOrderDTO.getSellerDeptName());
        return Result.success(gbAppealFormOrderVO);
    }

    @ApiOperation(value = "查看-源流向数据")
    @PostMapping("/detail/flowWashSaleReportListPage")
    public Result<GbAppealFormFlowWashSaleReportPageVO<GbAppealFormFlowWashSaleReportPageDetailVO>> detailFlowWashSaleReportListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid GbAppealFormDetailFlowWashSalePageForm form) {
        // 团购处理申请
        GbAppealFormDTO gbAppealFormDTO = gbAppealFormApi.getById(form.getAppealFormId());
        if (ObjectUtil.isNull(gbAppealFormDTO)) {
            throw new BusinessException(ResultCode.FAILED, "此在团购处理信息不存在，请确认");
        }
        // 当前团购处理id获取关联流向分页列表
        QueryGbAppealFormFlowStatisticPageRequest request = new QueryGbAppealFormFlowStatisticPageRequest();
        request.setAppealFormId(form.getAppealFormId());
        request.setCurrent(form.getCurrent());
        request.setSize(form.getSize());
        Page<GbAppealFormFlowStatisticBO> flowStatisticBOPage = gbAppealFormApi.flowStatisticListPage(request);
        if (ObjectUtil.isNull(flowStatisticBOPage) || CollUtil.isEmpty(flowStatisticBOPage.getRecords())) {
            return Result.success(new GbAppealFormFlowWashSaleReportPageVO<>());
        }

        List<Long> flowWashIdList = flowStatisticBOPage.getRecords().stream().map(GbAppealFormFlowStatisticBO::getFlowWashId).distinct().collect(Collectors.toList());
        // 合计数量、已匹配团购数量合计、未匹配团购数量合计
        GbAppealFormFlowCountBO flowCountBO = gbAppealFlowStatisticApi.getFlowCountByGbAppealFormId(form.getAppealFormId());

        // 根据原始流向id查询销售合并报表的流向
        Map<Long, FlowWashSaleReportDTO> flowWashMap = getFlowWashMapByFlowWashIdList(flowWashIdList);

        // 设置流向信息
        GbAppealFormFlowWashSaleReportPageVO<GbAppealFormFlowWashSaleReportPageDetailVO> page = new GbAppealFormFlowWashSaleReportPageVO<>();
        page.setCurrent(flowStatisticBOPage.getCurrent());
        page.setSize(flowStatisticBOPage.getSize());
        page.setTotal(flowStatisticBOPage.getTotal());
        // 明细
        List<GbAppealFormFlowWashSaleReportPageDetailVO> list = new ArrayList<>();
        GbAppealFormFlowWashSaleReportPageDetailVO vo;
        for (GbAppealFormFlowStatisticBO record : flowStatisticBOPage.getRecords()) {
            vo = new GbAppealFormFlowWashSaleReportPageDetailVO();
            // 流向数量
            vo.setFlowKey(record.getFlowKey());
            vo.setFlowWashId(record.getFlowWashId());
            vo.setQuantity(record.getSoQuantity());
            vo.setMatchQuantity(record.getMatchQuantity());
            vo.setUnMatchQuantity(record.getUnMatchQuantity());
            // 是否已加入
            Boolean selectFlag = false;
            if (record.getGbMatchQuantity().compareTo(BigDecimal.ZERO) > 0) {
                selectFlag = true;
            }
            vo.setSelectFlag(selectFlag);
            FlowWashSaleReportDTO flowWashSaleReportDTO = flowWashMap.get(record.getFlowWashId());
            if (ObjectUtil.isNull(flowWashSaleReportDTO)) {
                continue;
            }
            // 销售合并报表信息
            buildFlowWashSaleReportInfo(vo, flowWashSaleReportDTO);
            list.add(vo);
        }
        page.setRecords(list);
        // 合计
        if (ObjectUtil.isNotNull(flowCountBO)) {
            page.setTotalQuantity(flowCountBO.getTotalQuantity());
            page.setTotalMatchQuantity(flowCountBO.getTotalMatchQuantity());
            page.setTotalUnMatchQuantity(flowCountBO.getTotalUnMatchQuantity());
        }
        return Result.success(page);
    }

    private void buildFlowWashSaleReportInfo(GbAppealFormFlowWashSaleReportPageDetailVO vo, FlowWashSaleReportDTO flowWashSaleReportDTO) {
        String matchMonth = flowWashSaleReportDTO.getYear().concat("-").concat(String.format("%02d", Integer.parseInt(flowWashSaleReportDTO.getMonth())));
        vo.setMatchMonth(matchMonth);
        vo.setSoTime(flowWashSaleReportDTO.getSoTime());
        vo.setCrmId(flowWashSaleReportDTO.getCrmId());
        vo.setEname(flowWashSaleReportDTO.getEname());
        vo.setOriginalEnterpriseName(flowWashSaleReportDTO.getOriginalEnterpriseName());
        // 机构编码默认为空
        Long customerCrmId = flowWashSaleReportDTO.getCustomerCrmId();
        boolean customerCrmIdFlag = ObjectUtil.isNull(customerCrmId) || (ObjectUtil.isNotNull(customerCrmId) && customerCrmId.intValue() == 0);
        vo.setCustomerCrmId(customerCrmIdFlag ? "" : customerCrmId.toString());
        vo.setEnterpriseName(flowWashSaleReportDTO.getEnterpriseName());
        vo.setSoGoodsName(flowWashSaleReportDTO.getSoGoodsName());
        vo.setSoSpecifications(flowWashSaleReportDTO.getSoSpecifications());
        // 标准产品编码默认为空
        Long goodsCode = flowWashSaleReportDTO.getGoodsCode();
        boolean goodsCodeFlag = ObjectUtil.isNull(goodsCode) || (ObjectUtil.isNotNull(goodsCode) && goodsCode.intValue() == 0);
        vo.setGoodsCode(goodsCodeFlag ? "" : goodsCode.toString());
        vo.setGoodsName(flowWashSaleReportDTO.getGoodsName());
        vo.setDepartment(flowWashSaleReportDTO.getDepartment());
        vo.setBusinessDepartment(flowWashSaleReportDTO.getBusinessDepartment());
        vo.setProvincialArea(flowWashSaleReportDTO.getProvincialArea());
        vo.setBusinessProvince(flowWashSaleReportDTO.getBusinessProvince());
        vo.setDistrictCountyCode(flowWashSaleReportDTO.getDistrictCountyCode());
        vo.setDistrictCounty(flowWashSaleReportDTO.getDistrictCounty());
        vo.setSuperiorSupervisorCode(flowWashSaleReportDTO.getSuperiorSupervisorCode());
        vo.setSuperiorSupervisorName(flowWashSaleReportDTO.getSuperiorSupervisorName());
        vo.setRepresentativeCode(flowWashSaleReportDTO.getRepresentativeCode());
        vo.setRepresentativeName(flowWashSaleReportDTO.getRepresentativeName());
        // 岗位代码默认为空
        Long postCode = flowWashSaleReportDTO.getPostCode();
        boolean postCodeFlag = ObjectUtil.isNull(postCode) || (ObjectUtil.isNotNull(postCode) && postCode.intValue() == 0);
        vo.setPostCode(postCodeFlag ? "" : postCode.toString());
        vo.setPostName(flowWashSaleReportDTO.getPostName());
    }

    private Map<Long, FlowWashSaleReportDTO> getFlowWashMapByFlowWashIdList(List<Long> flowWashIdList) {
        Map<Long, FlowWashSaleReportDTO> flowWashMap = new HashMap<>();
        List<FlowClassifyEnum> flowClassifyEnumList = new ArrayList<>();
        flowClassifyEnumList.add(FlowClassifyEnum.NORMAL);
        flowClassifyEnumList.add(FlowClassifyEnum.SALE_APPEAL);
        flowClassifyEnumList.add(FlowClassifyEnum.FLOW_CROSS);
        List<FlowWashSaleReportDTO> flowWashSaleReportList = flowWashSaleReportApi.listByFlowSaleWashIds(flowWashIdList, flowClassifyEnumList);
        if (CollUtil.isNotEmpty(flowWashSaleReportList)) {
            flowWashMap = flowWashSaleReportList.stream().collect(Collectors.toMap(o -> o.getFlowSaleWashId(), o -> o, (k1, k2) -> k1));
        }
        return flowWashMap;
    }

    @ApiOperation(value = "查看-团购数据处理（扣减/增加）")
    @PostMapping("/detail/gbAppealAllocationListPage")
    public Result<GbAppealFormAllocationPageVO<GbAppealFormAllocationPageDetailVO>> detailGbAppealAllocationListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid GbAppealFormDetailAllocationPageForm form) {
        // 团购处理申请
        GbAppealFormDTO gbAppealFormDTO = gbAppealFormApi.getById(form.getAppealFormId());
        if (ObjectUtil.isNull(gbAppealFormDTO)) {
            throw new BusinessException(ResultCode.FAILED, "此在团购处理信息不存在，请确认");
        }

        // 当前团购处理id获取关联流向分页列表
        QueryGbAppealFormAllocationPageRequest request = new QueryGbAppealFormAllocationPageRequest();
        request.setAppealFormId(form.getAppealFormId());
        request.setAllocationType(form.getAllocationType());
        request.setOrderByDescField("id");
        request.setCurrent(form.getCurrent());
        request.setSize(form.getSize());
        Page<GbAppealAllocationDTO> allocationDTOPage = gbAppealAllocationApi.flowStatisticListPage(request);
        if (ObjectUtil.isNull(allocationDTOPage) || CollUtil.isEmpty(allocationDTOPage.getRecords())) {
            return Result.success(new GbAppealFormAllocationPageVO<>());
        }

        // 团购数量，与扣减中总数量、增加总数量 是否相等的校验
        // 团购数量
        BigDecimal gbQuantity = gbAppealFormDTO.getGbQuantity();
        if (ObjectUtil.equal(GbAllocationTypeEnum.DEDUCT.getCode(), form.getAllocationType())) {
            // 扣减，团购数量的负数与扣减合计相等
            gbQuantity = gbQuantity.negate();
        }
        // 扣减、增加总数量
        BigDecimal totalQuantity = BigDecimal.ZERO;
        List<GbAppealAllocationDTO> gbAppealAllocationList = gbAppealAllocationApi.listByAppealFormIdAndAllocationType(form.getAppealFormId(), form.getAllocationType());
        if (CollUtil.isNotEmpty(gbAppealAllocationList)) {
            // 当前团购已扣减、已增加的总数量
            totalQuantity = gbAppealAllocationList.stream().map(GbAppealAllocationDTO::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        // 团购数量、扣减/增加总数量 不相等的提示信息
        boolean errorFlag = false;
        String errorMsg = "";
        if (gbQuantity.compareTo(totalQuantity) != 0) {
            errorFlag = true;
            if (ObjectUtil.equal(GbAllocationTypeEnum.DEDUCT.getCode(), form.getAllocationType())) {
                errorMsg = "扣减总数量与团购数量不一致，请注意检查！！";
            } else {
                errorMsg = "增加总数量与团购数量不一致，请注意检查！！";
            }
        }

        // 根据销量计入人工号查询人员信息, key -> 销量计入人工号, value -> GbAppealFormAllocationPageDetailVO
//        Map<String, GbAppealFormAllocationPageDetailVO> empMap = getEmpMap(allocationDTOPage.getRecords());

        GbAppealFormAllocationPageVO<GbAppealFormAllocationPageDetailVO> page = new GbAppealFormAllocationPageVO<>();
        page.setCurrent(allocationDTOPage.getCurrent());
        page.setSize(allocationDTOPage.getSize());
        page.setTotal(allocationDTOPage.getTotal());
        List<GbAppealFormAllocationPageDetailVO> list = new ArrayList<>();
        GbAppealFormAllocationPageDetailVO vo;
        for (GbAppealAllocationDTO record : allocationDTOPage.getRecords()) {
            vo = PojoUtils.map(record, GbAppealFormAllocationPageDetailVO.class);
//            buildAppealAllocationInfo(vo, record, empMap);
            // 默认值
            buildDefaultField(vo, record);
            list.add(vo);
        }
        page.setRecords(list);
        page.setErrorFlag(errorFlag);
        page.setErrorMsg(errorMsg);
        return Result.success(page);
    }

    private void buildDefaultField(GbAppealFormAllocationPageDetailVO vo, GbAppealAllocationDTO record) {
        // 机构编码默认为空
        Long customerCrmId = record.getCustomerCrmId();
        boolean customerCrmIdFlag = (ObjectUtil.isNotNull(customerCrmId) && customerCrmId.intValue() == 0) || ObjectUtil.isNull(customerCrmId);
        vo.setCustomerCrmId(customerCrmIdFlag ? "" : customerCrmId.toString());
        // 标准产品编码默认为空
        Long goodsCode = record.getGoodsCode();
        boolean goodsCodeFlag = (ObjectUtil.isNotNull(goodsCode) && goodsCode.intValue() == 0) || ObjectUtil.isNull(goodsCode);
        vo.setGoodsCode(goodsCodeFlag ? "" : goodsCode.toString());
        // 岗位代码默认为空
        Long postCode = record.getPostCode();
        boolean postCodeFlag = (ObjectUtil.isNotNull(postCode) && postCode.intValue() == 0) || ObjectUtil.isNull(postCode);
        vo.setPostCode(postCodeFlag ? "" : postCode.toString());
    }

    private Map<String, GbAppealFormAllocationPageDetailVO> getEmpMap(List<GbAppealAllocationDTO> list) {
        Map<String, GbAppealFormAllocationPageDetailVO> empMap = new HashMap<>();
        List<String> empIds = list.stream().map(GbAppealAllocationDTO::getRepresentativeCode).distinct().collect(Collectors.toList());
        List<EsbEmployeeDTO> esbEmployeeDTOs = esbEmployeeApi.listByEmpIds(empIds);
        if (CollUtil.isNotEmpty(esbEmployeeDTOs)) {
            for (EsbEmployeeDTO esbEmployeeDTO : esbEmployeeDTOs) {
                GbAppealFormAllocationPageDetailVO detailVO = new GbAppealFormAllocationPageDetailVO();
                detailVO.setBusinessDepartment(esbEmployeeDTO.getYxDept());
                detailVO.setBusinessProvince(esbEmployeeDTO.getYxProvince());
                Long deptId = Optional.ofNullable(esbEmployeeDTO.getDeptId()).orElse(0L);
                String districtCountyCode = deptId.toString();
                detailVO.setDistrictCountyCode(districtCountyCode);
                detailVO.setDistrictCounty(Optional.ofNullable(esbEmployeeDTO.getDeptName()).orElse(""));
                detailVO.setSuperiorSupervisorCode(Optional.ofNullable(esbEmployeeDTO.getSuperior()).orElse(""));
                detailVO.setSuperiorSupervisorName(Optional.ofNullable(esbEmployeeDTO.getSuperiorName()).orElse(""));
                detailVO.setRepresentativeCode(Optional.ofNullable(esbEmployeeDTO.getEmpId()).orElse(""));
                detailVO.setRepresentativeName(Optional.ofNullable(esbEmployeeDTO.getEmpName()).orElse(""));
                detailVO.setPostCode(ObjectUtil.isNotNull(esbEmployeeDTO.getJobId()) ? esbEmployeeDTO.getJobId().toString() : "");
                detailVO.setPostName(Optional.ofNullable(esbEmployeeDTO.getJobName()).orElse(""));
                // 根据部门id 获取部门
                EsbOrganizationDTO organizationDTO = esbOrganizationApi.getByOrgId(esbEmployeeDTO.getDeptId());
                detailVO.setDepartment(organizationDTO.getOrgName());
                // 根据业务部门、业务省区 获取省区
                String provinceArea = crmEnterpriseRelationShipApi.getProvinceAreaByThreeParms(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince());
                detailVO.setProvincialArea(provinceArea);
                empMap.put(detailVO.getRepresentativeCode(), detailVO);
            }
        }
        return empMap;
    }

    private void buildAppealAllocationInfo(GbAppealFormAllocationPageDetailVO vo, GbAppealAllocationDTO gbAppealAllocationDTO, Map<String, GbAppealFormAllocationPageDetailVO> empMap) {
        GbAppealFormAllocationPageDetailVO detailVO = empMap.get(gbAppealAllocationDTO.getRepresentativeCode());
        if (ObjectUtil.isNotNull(detailVO)) {
            if (StrUtil.isBlank(vo.getDepartment())) {
                vo.setDepartment(detailVO.getDepartment());
            }
            if (StrUtil.isBlank(vo.getBusinessDepartment())) {
                vo.setBusinessDepartment(detailVO.getBusinessDepartment());
            }
            vo.setProvincialArea(detailVO.getProvincialArea());
            vo.setBusinessProvince(detailVO.getBusinessProvince());
            vo.setDistrictCountyCode(detailVO.getDistrictCountyCode());
            vo.setDistrictCounty(detailVO.getDistrictCounty());
            vo.setSuperiorSupervisorCode(detailVO.getSuperiorSupervisorCode());
            vo.setSuperiorSupervisorName(detailVO.getSuperiorSupervisorName());
            vo.setRepresentativeCode(detailVO.getRepresentativeCode());
            vo.setRepresentativeName(detailVO.getRepresentativeName());
            vo.setPostCode(detailVO.getPostCode());
            vo.setPostName(detailVO.getPostName());
        }
    }

    @ApiOperation(value = "更新处理状态")
    @PostMapping("/updateExecuteStatus")
    public Result<Boolean> updateExecuteStatus(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid GbAppealFormUpdateExecuteStatusForm form) {
        // 团购处理申请
        GbAppealFormDTO gbAppealFormDTO = gbAppealFormApi.getById(form.getAppealFormId());
        if (ObjectUtil.isNull(gbAppealFormDTO)) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理信息不存在，请确认");
        }

        // 校验日程，锁定、非锁团购状态
        FlowMonthWashControlDTO flowMonthWashControlDTO = gbAppealFormApi.washControlGbStatusCheck(gbAppealFormDTO.getMatchMonth());
        if (ObjectUtil.isNull(flowMonthWashControlDTO)) {
            throw new BusinessException(ResultCode.FAILED, "当前没有可以用的团购处理日程, appealFormId:" + form.getAppealFormId());
        }

        // 自动处理中不能操作
        if (ObjectUtil.equal(GbDataExecStatusEnum.AUTO.getCode(), gbAppealFormDTO.getDataExecStatus())) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理状态是“自动处理中”，不能操作，请确认");
        }
        // 不能改成自动处理中
        if (ObjectUtil.equal(GbDataExecStatusEnum.AUTO.getCode(), form.getDataExecStatus())) {
            throw new BusinessException(ResultCode.FAILED, "团购处理状态不能更新为“自动处理中”，请确认");
        }
        // 不能改成未开始
        if (ObjectUtil.equal(GbDataExecStatusEnum.UN_START.getCode(), form.getDataExecStatus())) {
            throw new BusinessException(ResultCode.FAILED, "团购处理状态不能更新为“未开始”，请确认");
        }
        // 不能改成处理失败
        if (ObjectUtil.equal(GbDataExecStatusEnum.FAIL.getCode(), form.getDataExecStatus())) {
            throw new BusinessException(ResultCode.FAILED, "团购处理状态不能更新为“处理失败”，请确认");
        }

        // 未开始 -> 手动处理中，不允许改成其他状态
        if (ObjectUtil.equal(GbDataExecStatusEnum.UN_START.getCode(), gbAppealFormDTO.getDataExecStatus()) && !ObjectUtil.equal(GbDataExecStatusEnum.ARTIFICIAL.getCode(), form.getDataExecStatus())) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理状态是“未开始”，仅能更新为“手动处理中”，请确认");
        }
        // 开始处理，未开始 --> 手动处理中，校验数量
        // 用户点击“开始处理”按钮时，校验源流向数量，如果等于0，则拦截并提醒“源流向数量为0，请先选择源流向数据！”
        if (ObjectUtil.equal(GbDataExecStatusEnum.ARTIFICIAL.getCode(), form.getDataExecStatus()) && ObjectUtil.equal(GbDataExecStatusEnum.UN_START.getCode(), gbAppealFormDTO.getDataExecStatus())) {
            // 查询关联的源流向总数量
            BigDecimal flowMatchQuantity = gbAppealFormApi.getTotalFlowMatchQuantityByAppealFormId(form.getAppealFormId());
            if (flowMatchQuantity.compareTo(BigDecimal.ZERO) == 0) {
                throw new BusinessException(ResultCode.FAILED, "源流向数量为0，不能操作，请确认");
            }
        }

        // 手动处理中 -> 手动处理中
        if (ObjectUtil.equal(GbDataExecStatusEnum.ARTIFICIAL.getCode(), gbAppealFormDTO.getDataExecStatus()) && ObjectUtil.equal(GbDataExecStatusEnum.ARTIFICIAL.getCode(), form.getDataExecStatus())) {
            return Result.success(true);
        }
        // 手动处理中 -> 已处理，不允许改成其他状态
        if (ObjectUtil.equal(GbDataExecStatusEnum.ARTIFICIAL.getCode(), gbAppealFormDTO.getDataExecStatus()) && !ObjectUtil.equal(GbDataExecStatusEnum.FINISH.getCode(), form.getDataExecStatus())) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理状态是“手动处理中”，仅能更新为“已处理”，请确认");
        }
        // 手动处理中 -> 已处理，团购处理结果不能为空、数量不能为0
        if (ObjectUtil.equal(GbDataExecStatusEnum.FINISH.getCode(), form.getDataExecStatus())) {
            // 已处理，扣减、增加结果不能为空、已扣减数量>0
            List<GbAppealAllocationDTO> appealAllocationList = gbAppealAllocationApi.listByAppealFormIdAndAllocationType(form.getAppealFormId(), 0);
            if (CollUtil.isEmpty(appealAllocationList)) {
                throw new BusinessException(ResultCode.FAILED, "团购处理结果不能为空！");
            }
            appealAllocationList.forEach(o -> {
                if (o.getQuantity().abs().compareTo(BigDecimal.ZERO) == 0) {
                    throw new BusinessException(ResultCode.FAILED, "团购处理结果的扣减数量不能为0, 流向ID:" + o.getId());
                }
            });
        }

        //  已处理 -> 手动处理中，不允许改成其他状态
        if (ObjectUtil.equal(GbDataExecStatusEnum.FINISH.getCode(), gbAppealFormDTO.getDataExecStatus()) && !ObjectUtil.equal(GbDataExecStatusEnum.ARTIFICIAL.getCode(), form.getDataExecStatus())) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理状态是“已处理”，仅能更新为“手动处理中”，请确认");
        }

        // 更新状态
        GbAppealFormUpdateExecuteStatusRequest request = new GbAppealFormUpdateExecuteStatusRequest();
        request.setId(form.getAppealFormId());
        request.setDataExecStatus(form.getDataExecStatus());
        request.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(gbAppealFormApi.updateByIdAndExecuteStatus(request));
    }

    @ApiOperation(value = "处理-加入弹窗")
    @PostMapping("/execute/addDetail")
    public Result<GbAppealFormExecuteAddDetailVO> executeAddDetail(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid GbAppealFormExecuteFlowWashForm form) {
        Long appealFormId = form.getAppealFormId();
        Long flowWashId = form.getFlowWashId();
        // 团购处理申请
        GbAppealFormDTO gbAppealFormDTO = gbAppealFormApi.getById(appealFormId);
        if (ObjectUtil.isNull(gbAppealFormDTO)) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理信息不存在，请确认");
        }
        // 团购处理状态校验，未开始、自动处理中 不能加入扣减
        List<Integer> statusList = ListUtil.toList(GbDataExecStatusEnum.UN_START.getCode(), GbDataExecStatusEnum.AUTO.getCode());
        if (statusList.contains(gbAppealFormDTO.getDataExecStatus())) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理状态是“未开始”或“自动处理中”，不能操作，请确认");
        }

        // 校验日程，锁定、非锁团购状态
        FlowMonthWashControlDTO flowMonthWashControlDTO = gbAppealFormApi.washControlGbStatusCheck(gbAppealFormDTO.getMatchMonth());
        if (ObjectUtil.isNull(flowMonthWashControlDTO)) {
            throw new BusinessException(ResultCode.FAILED, "当前没有可以用的团购处理日程, appealFormId:" + appealFormId);
        }

        BigDecimal gbQuantity = gbAppealFormDTO.getGbQuantity();
        BigDecimal gbMatchQuantity = BigDecimal.ZERO;
        BigDecimal gbUnMatchQuantity = gbQuantity;
        // 查询团购处理已匹配源流向数量
        List<GbAppealFlowRelatedDTO> gbAppealFlowRelatedList = gbAppealFlowRelatedApi.getListByAppealFormId(appealFormId);
        if (CollUtil.isNotEmpty(gbAppealFlowRelatedList)) {
            List<Long> flowWashIdList = gbAppealFlowRelatedList.stream().filter(o -> ObjectUtil.isNotNull(o.getMatchQuantity()) && o.getMatchQuantity().compareTo(BigDecimal.ZERO) > 0).map(GbAppealFlowRelatedDTO::getFlowWashId).distinct().collect(Collectors.toList());
            if (flowWashIdList.contains(flowWashId)) {
                throw new BusinessException(ResultCode.FAILED, "当前团购处理已加入此源流向数据进行扣减，不能重复加入，请确认");
            }
            // 当前团购已扣减的总数量
            BigDecimal totalMatchQuantity = gbAppealFlowRelatedList.stream().map(GbAppealFlowRelatedDTO::getMatchQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
            gbMatchQuantity = totalMatchQuantity;
            // 未扣减数量
            gbUnMatchQuantity = gbQuantity.subtract(gbMatchQuantity);
        }

        BigDecimal soQuantity = BigDecimal.ZERO;
        BigDecimal soMatchQuantity = BigDecimal.ZERO;
        BigDecimal soUnMatchQuantity = BigDecimal.ZERO;
        // 查询源流向匹配数量
        GbAppealFlowStatisticDTO gbAppealFlowStatistic = gbAppealFlowStatisticApi.getByFlowWashId(flowWashId);
        if (ObjectUtil.isNotNull(gbAppealFlowStatistic)) {
            soQuantity = gbAppealFlowStatistic.getSoQuantity();
            soMatchQuantity = gbAppealFlowStatistic.getMatchQuantity();
            soUnMatchQuantity = gbAppealFlowStatistic.getUnMatchQuantity();
        }

        //扣减数量，未扣减数量 和 未匹配团购数量 中的较小值
        BigDecimal doMatchQuantity = gbUnMatchQuantity.compareTo(soUnMatchQuantity) < 0 ? gbUnMatchQuantity : soUnMatchQuantity;

        GbAppealFormExecuteAddDetailVO vo = new GbAppealFormExecuteAddDetailVO();
        vo.setAppealFormId(appealFormId);
        vo.setGbQuantity(gbQuantity);
        vo.setGbMatchQuantity(gbMatchQuantity);
        vo.setGbUnMatchQuantity(gbUnMatchQuantity);
        vo.setFlowWashId(flowWashId);
        vo.setSoQuantity(soQuantity);
        vo.setSoMatchQuantity(soMatchQuantity);
        vo.setSoUnMatchQuantity(soUnMatchQuantity);
        vo.setDoMatchQuantity(doMatchQuantity);
        return Result.success(vo);
    }

    @ApiOperation(value = "处理-加入扣减提交")
    @PostMapping("/execute/addSubstract")
    public Result<Boolean> executeAddSubstract(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid GbAppealFormExecuteSubstractForm form) {
        Long appealFormId = form.getAppealFormId();
        Long flowWashId = form.getFlowWashId();
        BigDecimal doMatchQuantity = form.getDoMatchQuantity();
        if (doMatchQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ResultCode.FAILED, "扣减数量必须大于0，请确认");
        }

        // 团购处理申请
        GbAppealFormDTO gbAppealFormDTO = gbAppealFormApi.getById(appealFormId);
        if (ObjectUtil.isNull(gbAppealFormDTO)) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理信息不存在，请确认");
        }
        // 团购处理状态校验，未开始、自动处理中 不能加入扣减
        List<Integer> statusList = ListUtil.toList(GbDataExecStatusEnum.UN_START.getCode(), GbDataExecStatusEnum.AUTO.getCode());
        if (statusList.contains(gbAppealFormDTO.getDataExecStatus())) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理状态是“未开始”或“自动处理中”，不能操作，请确认");
        }
        // 校验日程，锁定、非锁团购状态
        FlowMonthWashControlDTO flowMonthWashControlDTO = gbAppealFormApi.washControlGbStatusCheck(gbAppealFormDTO.getMatchMonth());
        if (ObjectUtil.isNull(flowMonthWashControlDTO)) {
            throw new BusinessException(ResultCode.FAILED, "当前没有可以用的团购处理日程, appealFormId:" + form.getAppealFormId());
        }

        // 查询团购处理、源流向关联
        GbAppealFlowRelatedDTO gbAppealFlowRelatedDTO = gbAppealFlowRelatedApi.getByAppealFormIdAndFlowWashId(appealFormId, flowWashId);
        if (ObjectUtil.isNull(gbAppealFlowRelatedDTO)) {
            throw new BusinessException(ResultCode.FAILED, "当前团购处理未匹配此源流向，请先进行源流向匹配");
        }
        // 校验扣减数量：扣减数量 不能大于源流向的未匹配团购数量，扣减数量不能大于团购数据的未扣减数量
        // 查询团购处理扣减
        BigDecimal gbUnMatchQuantity = gbAppealFormDTO.getGbQuantity();
        List<GbAppealFlowRelatedDTO> gbAppealFlowRelatedList = gbAppealFlowRelatedApi.getListByAppealFormId(appealFormId);
        if (CollUtil.isNotEmpty(gbAppealFlowRelatedList)) {
            GbAppealFlowRelatedDTO oldGbAppealFlowRelated = gbAppealFlowRelatedList.stream().filter(o -> flowWashId.equals(o.getFlowWashId()) && o.getMatchQuantity().compareTo(BigDecimal.ZERO) > 0).findFirst().orElse(null);
            if (ObjectUtil.isNotNull(oldGbAppealFlowRelated)) {
                throw new BusinessException(ResultCode.FAILED, "当前团购处理已加入此源流向数据进行扣减，不能重复加入，请确认");
            }
            // 当前团购已扣减的总数量
            BigDecimal totalMatchQuantity = gbAppealFlowRelatedList.stream().filter(o -> o.getMatchQuantity().compareTo(BigDecimal.ZERO) > 0).map(GbAppealFlowRelatedDTO::getMatchQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
            // 团购数据的未扣减数量
            gbUnMatchQuantity = gbAppealFormDTO.getGbQuantity().subtract(totalMatchQuantity.abs());
        }
        if (doMatchQuantity.compareTo(gbUnMatchQuantity) > 0) {
            throw new BusinessException(ResultCode.FAILED, "扣减数量不能大于团购数据的未扣减数量");
        }
        // 查询源流向扣减统计匹配数量
        BigDecimal soUnMatchQuantity = BigDecimal.ZERO;
        GbAppealFlowStatisticDTO gbAppealFlowStatistic = gbAppealFlowStatisticApi.getByFlowWashId(flowWashId);
        if (ObjectUtil.isNotNull(gbAppealFlowStatistic)) {
            soUnMatchQuantity = gbAppealFlowStatistic.getUnMatchQuantity();
        }
        if (doMatchQuantity.compareTo(soUnMatchQuantity) > 0) {
            throw new BusinessException(ResultCode.FAILED, "扣减数量不能大于源流向的未匹配团购数量");
        }

        // 扣减
        GbAppealSubstractMateFlowRequest request = new GbAppealSubstractMateFlowRequest();
        request.setAppealFormId(appealFormId);
        request.setFlowWashId(flowWashId);
        request.setDoMatchQuantity(doMatchQuantity);
        request.setExecType(GbExecTypeEnum.ARTIFICIAL.getCode());
        request.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(gbAppealFormApi.substractMateFlow(request));
    }

    @ApiOperation(value = "处理-取消加入扣减")
    @PostMapping("/execute/cancleSubstract")
    public Result<Boolean> executeCancleSubstract(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid GbAppealFormExecuteCancleSubstractForm form) {
        // 团购处理申请
        GbAppealFormDTO gbAppealFormDTO = gbAppealFormApi.getById(form.getAppealFormId());
        if (ObjectUtil.isNull(gbAppealFormDTO)) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理信息不存在，请确认");
        }
        // 未开始、自动处理中 不能操作
        if (ObjectUtil.equal(GbDataExecStatusEnum.UN_START.getCode(), gbAppealFormDTO.getDataExecStatus()) || ObjectUtil.equal(GbDataExecStatusEnum.AUTO.getCode(), gbAppealFormDTO.getDataExecStatus())) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理状态是“未开始”或“自动处理中”，不能操作，请确认");
        }

        // 校验日程，锁定、非锁团购状态
        FlowMonthWashControlDTO flowMonthWashControlDTO = gbAppealFormApi.washControlGbStatusCheck(gbAppealFormDTO.getMatchMonth());
        if (ObjectUtil.isNull(flowMonthWashControlDTO)) {
            throw new BusinessException(ResultCode.FAILED, "当前没有可以用的团购处理日程, appealFormId:" + form.getAppealFormId());
        }

        // 取消当前源流向扣减
        GbAppealSubstractCancleRequest cancleRequest = new GbAppealSubstractCancleRequest();
        cancleRequest.setAppealFormId(form.getAppealFormId());
        cancleRequest.setFlowWashId(form.getFlowWashId());
        cancleRequest.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(gbAppealFormApi.cancleSubstract(cancleRequest));
    }

    @ApiOperation(value = "根据部门名称查询")
    @PostMapping("/orgListByName")
    public Result<List<GbAppealFormOrgListVO>> orgListByName(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid GbAppealFormOrgListForm form) {
        List<EsbOrganizationDTO> esbOrganizationList = esbOrganizationApi.findByOrgName(form.getOrgName(), null);
        if (CollUtil.isEmpty(esbOrganizationList)) {
            return Result.success(ListUtil.empty());
        }

        List<GbAppealFormOrgListVO> list = new ArrayList<>();
        GbAppealFormOrgListVO vo;
        for (EsbOrganizationDTO esbOrganizationDTO : esbOrganizationList) {
            vo = new GbAppealFormOrgListVO();
            vo.setOrgId(esbOrganizationDTO.getOrgId());
            vo.setOrgName(esbOrganizationDTO.getOrgName());
            vo.setFullpath(esbOrganizationDTO.getFullpath());
            // 与查询名称相同的放在第一个
            if (ObjectUtil.equal(form.getOrgName(), esbOrganizationDTO.getOrgName())) {
                list.add(0, vo);
            } else {
                list.add(vo);
            }
        }
        return Result.success(list);
    }

    @ApiOperation(value = "业务代表工号或者岗位代码获取信息")
    @GetMapping("/getByEmpIdOrJobId")
    public Result<EsbEmployVO> getByEmpIdOrJobId(@RequestParam(value = "empId", required = false) String empId, @RequestParam(value = "jobId", required = false) String jobId) {
        if (StrUtil.isBlank(empId) && StrUtil.isBlank(jobId)) {
            return Result.failed(100030, "您输入的业务代表工号、岗位代码不能为空");
        }
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpIdOrJobId(empId, jobId, null);
        if (ObjectUtil.isEmpty(esbEmployeeDTO) && StringUtils.isNotBlank(empId)) {
            return Result.failed(100030, "您输入的工号不存在");
        }
        if (ObjectUtil.isEmpty(esbEmployeeDTO) && StringUtils.isNotBlank(jobId)) {
            return Result.failed(100030, "您输入的岗位代码不存在");
        }
        if (ObjectUtil.isNotEmpty(esbEmployeeDTO) && CompareUtil.compare(esbEmployeeDTO.getJobId(), 0L) == 0) {
            return Result.failed(100030, "您输入的" + (StringUtils.isEmpty(empId) ? "岗位代码" : "工号") + "不存在");
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
        return Result.success(result);
    }

    @ApiOperation(value = "处理-编辑扣减/新增弹窗")
    @PostMapping("/execute/editDetail")
    public Result<GbAppealFormExecuteEditDetailVO> executeEditDetail(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryGbAppealFormAllocationDetailForm form) {
        // 团购处理结果
        GbAppealAllocationDTO gbAppealAllocationDTO = gbAppealAllocationApi.getById(form.getAppealAllocationId());
        if (ObjectUtil.isNull(gbAppealAllocationDTO)) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理结果不存在，请确认");
        }
        // 团购处理申请
        GbAppealFormDTO gbAppealFormDTO = gbAppealFormApi.getById(gbAppealAllocationDTO.getAppealFormId());
        if (ObjectUtil.isNull(gbAppealFormDTO)) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理信息不存在，请确认");
        }
        // 团购处理状态校验，未开始、自动处理中 不能编辑
        List<Integer> statusList = ListUtil.toList(GbDataExecStatusEnum.UN_START.getCode(), GbDataExecStatusEnum.AUTO.getCode());
        if (statusList.contains(gbAppealFormDTO.getDataExecStatus())) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理状态是“未开始”或“自动处理中”，不能操作，请确认");
        }

        // 校验日程，锁定、非锁团购状态
        FlowMonthWashControlDTO flowMonthWashControlDTO = gbAppealFormApi.washControlGbStatusCheck(gbAppealFormDTO.getMatchMonth());
        if (ObjectUtil.isNull(flowMonthWashControlDTO)) {
            throw new BusinessException(ResultCode.FAILED, "当前没有可以用的团购处理日程, appealFormId:" + gbAppealAllocationDTO.getAppealFormId());
        }

        GbAppealFormExecuteEditDetailVO vo = PojoUtils.map(gbAppealAllocationDTO, GbAppealFormExecuteEditDetailVO.class);
        // 部门、业务部门回显
        if (ObjectUtil.isNotNull(vo.getOrgId()) && vo.getOrgId().intValue() > 0) {
            EsbOrganizationDTO esbOrganizationDTO = esbOrganizationApi.getByOrgId(vo.getOrgId());
            if (ObjectUtil.isNotNull(esbOrganizationDTO)) {
                vo.setFullpath(esbOrganizationDTO.getFullpath());
            }
        }
        if (ObjectUtil.isNotNull(vo.getBusinessOrgId()) && vo.getBusinessOrgId().intValue() > 0) {
            EsbOrganizationDTO esbOrganizationDTO = esbOrganizationApi.getByOrgId(vo.getBusinessOrgId());
            if (ObjectUtil.isNotNull(esbOrganizationDTO)) {
                vo.setBusinessFullpath(esbOrganizationDTO.getFullpath());
            }
        }
        return Result.success(vo);
    }


    @ApiOperation(value = "处理-编辑扣减/新增弹窗保存")
    @PostMapping("/execute/editDetailSubmit")
    public Result<Boolean> executeEditDetailSubmit(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid GbAppealFormExecuteEditDetailForm form) {
        // 团购处理结果
        GbAppealAllocationDTO gbAppealAllocationDTO = gbAppealAllocationApi.getById(form.getAppealAllocationId());
        if (ObjectUtil.isNull(gbAppealAllocationDTO)) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理结果不存在，请确认");
        }
        // 团购处理申请
        GbAppealFormDTO gbAppealFormDTO = gbAppealFormApi.getById(gbAppealAllocationDTO.getAppealFormId());
        if (ObjectUtil.isNull(gbAppealFormDTO)) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理信息不存在，请确认");
        }
        // 团购处理状态校验，是否手动处理中
        if (!ObjectUtil.equal(GbDataExecStatusEnum.ARTIFICIAL.getCode(), gbAppealFormDTO.getDataExecStatus())) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理状态不是“手动处理中”，请确认");
        }
        // 校验日程，锁定、非锁团购状态
        FlowMonthWashControlDTO flowMonthWashControlDTO = gbAppealFormApi.washControlGbStatusCheck(gbAppealFormDTO.getMatchMonth());
        if (ObjectUtil.isNull(flowMonthWashControlDTO)) {
            throw new BusinessException(ResultCode.FAILED, "当前没有可以用的团购处理日程, appealFormId:" + gbAppealAllocationDTO.getAppealFormId());
        }

        // 部门、业务部门校验
        if (ObjectUtil.isNotNull(form.getOrgId()) && form.getOrgId().intValue() > 0) {
            EsbOrganizationDTO esbOrganizationDTO = esbOrganizationApi.getByOrgId(form.getOrgId());
            if (ObjectUtil.isNull(esbOrganizationDTO)) {
                throw new BusinessException(ResultCode.FAILED, "您选择的部门不存在，请确认");
            }
        }
        if (ObjectUtil.isNotNull(form.getBusinessOrgId()) && form.getBusinessOrgId().intValue() > 0) {
            EsbOrganizationDTO esbOrganizationDTO = esbOrganizationApi.getByOrgId(form.getBusinessOrgId());
            if (ObjectUtil.isNull(esbOrganizationDTO)) {
                throw new BusinessException(ResultCode.FAILED, "您选择的业务部门不存在，请确认");
            }
        }
        // 扣减、增加数量校验
        if (ObjectUtil.equal(GbAllocationTypeEnum.DEDUCT.getCode(), gbAppealAllocationDTO.getAllocationType())) {
            if (form.getQuantity().compareTo(BigDecimal.ZERO) >= 0) {
                throw new BusinessException(ResultCode.FAILED, "扣减数量请填写负数");
            }
        } else if (ObjectUtil.equal(GbAllocationTypeEnum.ADD.getCode(), gbAppealAllocationDTO.getAllocationType())) {
            if (form.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException(ResultCode.FAILED, "新增数量请填写正数");
            }
        }
        // 校验扣减数量是否 <= 流向未匹配数量
        GbAppealFlowStatisticDTO gbAppealFlowStatisticDTO = gbAppealFlowStatisticApi.getByFlowWashId(gbAppealAllocationDTO.getFlowWashId());
        if (ObjectUtil.isNull(gbAppealFlowStatisticDTO)) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理源流向未匹配，请确认");
        }
        BigDecimal unMatchQuantity = gbAppealFlowStatisticDTO.getUnMatchQuantity();
        BigDecimal quantityOld = gbAppealAllocationDTO.getQuantity();
        BigDecimal quantityNew = form.getQuantity();
        if (unMatchQuantity.add(quantityOld.abs()).compareTo(quantityNew.abs()) < 0) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理关联的源流向未匹配数量小于扣减数量，请确认, 团购处理id:" + gbAppealAllocationDTO.getAppealFormId() + ", 扣减/新增数据id:" + gbAppealAllocationDTO.getId() + ", 源流向id:{}" + gbAppealAllocationDTO.getFlowWashId());
        }

        GbAppealFormExecuteEditDetailRequest request = buildEditAllocationRequest(userInfo, form, gbAppealFormDTO);
        return Result.success(gbAppealFormApi.editGbAppealAllocation(request));
    }

    private GbAppealFormExecuteEditDetailRequest buildEditAllocationRequest(CurrentSjmsUserInfo userInfo, GbAppealFormExecuteEditDetailForm form, GbAppealFormDTO gbAppealFormDTO) {
        GbAppealFormExecuteEditDetailRequest request = new GbAppealFormExecuteEditDetailRequest();
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setAppealFormId(gbAppealFormDTO.getId());
        request.setAppealAllocationId(form.getAppealAllocationId());
        request.setQuantity(form.getQuantity());
        request.setExecType(GbExecTypeEnum.ARTIFICIAL.getCode());
        request.setOrgId(form.getOrgId());
        request.setDepartment(form.getDepartment());
        request.setBusinessOrgId(form.getBusinessOrgId());
        request.setBusinessDepartment(form.getBusinessDepartment());
        request.setRepresentativeCode(form.getEmpId());
        request.setRepresentativeName(form.getEmpName());
        request.setPostCode(form.getJobId());
        request.setPostName(form.getJobName());
        request.setBusinessProvince(form.getYxProvince());
        Long deptId = form.getDeptId();
        request.setDistrictCountyCode(ObjectUtil.isNotNull(deptId)? deptId.toString() : "0");
        request.setDistrictCounty(form.getDeptName());
        request.setSuperiorSupervisorCode(form.getSuperior());
        request.setSuperiorSupervisorName(form.getSuperiorName());
        // 根据业务部门、业务省区 获取省区
        String yxDept = form.getYxDept();
        String yxProvince = form.getYxProvince();
        String provinceArea = "";
        if (StrUtil.isNotBlank(yxDept) && StrUtil.isNotBlank(yxProvince)) {
            provinceArea = crmEnterpriseRelationShipApi.getProvinceAreaByThreeParms(yxDept, yxProvince);
        }
        request.setProvincialArea(provinceArea);
        return request;
    }


    @ApiOperation(value = "新增团购数据-列表")
    @PostMapping("/gbOrder/listPage")
    public Result<Page<GbOrderListPageVO>> gbOrderListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryGbOrderListPageForm form) {
        QueryGbOrderPageRequest request = PojoUtils.map(form, QueryGbOrderPageRequest.class);
        request.setExecStatus(GbOrderExecStatusEnum.UN_START.getCode());
        request.setMainOrderFlag(1);
        request.setOrderByDescField("create_time");
        Page<GbOrderDTO> gbOrderPage = gbOrderApi.getGbOrderPage(request);
        if (ObjectUtil.isNull(gbOrderPage) || CollUtil.isEmpty(gbOrderPage.getRecords())) {
            return Result.success(new Page<>());
        }

        Page<GbOrderListPageVO> page = PojoUtils.map(gbOrderPage, GbOrderListPageVO.class);
        return Result.success(page);
    }

    @ApiOperation(value = "新增团购数据-保存")
    @PostMapping("/gbOrder/save")
    public Result<Boolean> gbOrderSave(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveGbOrderForm form) {
        // 团购数据
        List<GbOrderDTO> gbOrderDTOS = gbOrderApi.listByFormIdList(form.getFormIdList());
        if (CollUtil.isEmpty(gbOrderDTOS)) {
            throw new BusinessException(ResultCode.FAILED, "您选择的团购数据不存在，请确认");
        }
        List<Long> gbOrderUnStartIds = gbOrderDTOS.stream().filter(o -> GbOrderExecStatusEnum.UN_START.getCode().equals(o.getExecStatus())).map(GbOrderDTO::getId).collect(Collectors.toList());
        if (CollUtil.isEmpty(gbOrderUnStartIds)) {
            throw new BusinessException(ResultCode.FAILED, "您选择的团购数据已增加处理, formId:" + form.getFormIdList().toString() +"，请刷新页面重新选择");
        }

        // 保存团购处理数据
        SaveGbAppealFormRequest saveGbAppealFormRequest = new SaveGbAppealFormRequest();
        saveGbAppealFormRequest.setGbOrderIdList(gbOrderUnStartIds);
        saveGbAppealFormRequest.setExecType(GbExecTypeEnum.ARTIFICIAL.getCode());
        saveGbAppealFormRequest.setOpUserId(userInfo.getCurrentUserId());
        boolean saveFlag = gbAppealFormApi.saveList(saveGbAppealFormRequest);
        if (!saveFlag) {
            throw new BusinessException(ResultCode.FAILED, "当前没有可以用的团购处理日程, formId:" + form.getFormIdList().toString());
        }
        return Result.success(saveFlag);
    }

    @ApiOperation(value = "团购处理结果列表")
    @PostMapping("/allocation/listPage")
    public Result<Page<GbAllocationListPageVO>> allocationListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryGbAllocationListPageForm form) {
        // 当前团购处理id获取关联流向分页列表
        QueryGbAppealFormAllocationPageRequest request = PojoUtils.map(form, QueryGbAppealFormAllocationPageRequest.class);
        request.setOrderByDescField("create_time");
        Page<GbAppealAllocationDTO> allocationDTOPage = gbAppealAllocationApi.flowStatisticListPage(request);
        if (ObjectUtil.isNull(allocationDTOPage) || CollUtil.isEmpty(allocationDTOPage.getRecords())) {
            return Result.success(new Page<>());
        }
        Page<GbAllocationListPageVO> page = PojoUtils.map(allocationDTOPage, GbAllocationListPageVO.class);
        return Result.success(page);
    }

    @ApiOperation(value = "团购处理列表-删除")
    @GetMapping("/deleteAppealForm")
    public Result<Boolean> deleteAppealForm(@CurrentUser CurrentSjmsUserInfo userInfo, @ApiParam(value = "主键ID", required = true) @RequestParam("id") Long id) {

        // 团购处理申请
        GbAppealFormDTO gbAppealFormDTO = gbAppealFormApi.getById(id);
        if (ObjectUtil.isNull(gbAppealFormDTO)) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理信息不存在，请确认");
        }
        // 团购处理状态，自动处理中 不能删除
        if (GbDataExecStatusEnum.AUTO.getCode().equals(gbAppealFormDTO.getDataExecStatus())) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理状态是“自动处理中”，不能操作“删除”");
        }
        // 校验日程，锁定、非锁团购状态
        FlowMonthWashControlDTO flowMonthWashControlDTO = gbAppealFormApi.washControlGbStatusCheck(gbAppealFormDTO.getMatchMonth());
        if (ObjectUtil.isNull(flowMonthWashControlDTO)) {
            throw new BusinessException(ResultCode.FAILED, "当前没有可以用的团购处理日程, appealFormId:" + id);
        }

        // 删除
        DeleteGbAppealFormRequest deleteAppealFormRequest = new DeleteGbAppealFormRequest();
        deleteAppealFormRequest.setId(id);
        deleteAppealFormRequest.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(gbAppealFormApi.deleteAppealForm(deleteAppealFormRequest));
    }

    @ApiOperation(value = "选择源流向-列表")
    @PostMapping("/saleReport/listPage")
    public Result<Page<GbAppealFormSaleReportVO>> saleReportListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryGbAppealFormSaleReportPageForm form) {
        FlowWashSaleReportPageRequest request = PojoUtils.map(form, FlowWashSaleReportPageRequest.class);
        Date date = DateUtil.parse(form.getSoMonth(), "yyyy-MM");
        request.setYear(StrUtil.toString(DateUtil.year(date)));
        request.setMonth(StrUtil.toString(DateUtil.month(date) + 1));
        request.setCustomerCrmId(form.getCrmEnterpriseId());
        request.setFlowClassifyList(ListUtil.toList(FlowClassifyEnum.NORMAL.getCode(), FlowClassifyEnum.SUPPLY_TRANS_MONTH_FLOW.getCode()));

        Page<FlowWashSaleReportDTO> page = flowWashReportApi.saleReportPageList(request);
        return Result.success(PojoUtils.map(page, GbAppealFormSaleReportVO.class));
    }

    @ApiOperation(value = "选择源流向-保存")
    @PostMapping("/saleReport/selectSubmit")
    public Result<Boolean> saleReportSelectSubmit(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveGbAppealFormSaleReportMatchForm form) {
        // 团购处理申请
        GbAppealFormDTO gbAppealFormDTO = gbAppealFormApi.getById(form.getAppealFormId());
        if (ObjectUtil.isNull(gbAppealFormDTO)) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理信息不存在，请确认");
        }
        // 团购处理状态，自动处理中 不能匹配源流向
        if (GbDataExecStatusEnum.AUTO.getCode().equals(gbAppealFormDTO.getDataExecStatus())) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理状态是“自动处理中，不能选择源流向保存");
        }
        // 校验日程，锁定、非锁团购状态
        FlowMonthWashControlDTO flowMonthWashControlDTO = gbAppealFormApi.washControlGbStatusCheck(gbAppealFormDTO.getMatchMonth());
        if (ObjectUtil.isNull(flowMonthWashControlDTO)) {
            throw new BusinessException(ResultCode.FAILED, "当前没有可以用的团购处理日程, appealFormId:" + form.getAppealFormId());
        }

        // 匹配源流向
        SaveGbAppealFormSaleReportMatchRequest request = PojoUtils.map(form, SaveGbAppealFormSaleReportMatchRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(gbAppealFormApi.selectFlowForMatch(request));
    }


    @ApiOperation(value = "选择源流向-删除")
    @PostMapping("/saleReport/deleteSelect")
    public Result<Boolean> saleReportDeleteSelect(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid GbAppealFormSaleReportDeleteSubstractForm form) {
        // 团购处理申请
        GbAppealFormDTO gbAppealFormDTO = gbAppealFormApi.getById(form.getAppealFormId());
        if (ObjectUtil.isNull(gbAppealFormDTO)) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理信息不存在，请确认");
        }
        // 自动处理中 不能操作
        if (ObjectUtil.equal(GbDataExecStatusEnum.AUTO.getCode(), gbAppealFormDTO.getDataExecStatus())) {
            throw new BusinessException(ResultCode.FAILED, "此团购处理状态是“自动处理中”，不能操作删除，请确认");
        }
        // 校验日程，锁定、非锁团购状态
        FlowMonthWashControlDTO flowMonthWashControlDTO = gbAppealFormApi.washControlGbStatusCheck(gbAppealFormDTO.getMatchMonth());
        if (ObjectUtil.isNull(flowMonthWashControlDTO)) {
            throw new BusinessException(ResultCode.FAILED, "当前没有可以用的团购处理日程, appealFormId:" + form.getAppealFormId());
        }

        // 取消当前源流向扣减
        GbAppealSubstractCancleRequest cancleRequest = new GbAppealSubstractCancleRequest();
        cancleRequest.setAppealFormId(form.getAppealFormId());
        cancleRequest.setFlowWashId(form.getFlowWashId());
        cancleRequest.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(gbAppealFormApi.deleteFlowForMatch(cancleRequest));
    }

}
