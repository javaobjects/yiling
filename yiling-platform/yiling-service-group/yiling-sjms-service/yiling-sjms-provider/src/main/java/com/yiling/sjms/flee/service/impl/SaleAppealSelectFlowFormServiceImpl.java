package com.yiling.sjms.flee.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.dataflow.report.api.FlowWashSaleReportApi;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.flee.dao.SaleAppealSelectFlowFormMapper;
import com.yiling.sjms.flee.dto.request.RemoveSelectAppealFlowFormRequest;
import com.yiling.sjms.flee.dto.request.SaveSelectAppealFlowFormRequest;
import com.yiling.sjms.flee.dto.request.SelectAppealConfirmFlowFormRequest;
import com.yiling.sjms.flee.dto.request.SelectAppealFlowFormRequest;
import com.yiling.sjms.flee.entity.SaleAppealSelectFlowFormDO;
import com.yiling.sjms.flee.entity.SalesAppealExtFormDO;
import com.yiling.sjms.flee.enums.FleeingConfirmStatusEnum;
import com.yiling.sjms.flee.service.SaleAppealSelectFlowFormService;
import com.yiling.sjms.flee.service.SalesAppealExtFormService;
import com.yiling.sjms.form.FleeFormErrorCode;
import com.yiling.sjms.form.dto.request.CreateFormRequest;
import com.yiling.sjms.form.enums.FormNoEnum;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.form.service.FormService;
import com.yiling.sjms.gb.service.NoService;
import com.yiling.sjms.util.OaTodoUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xinxuan.jia
 * @date: 2023/6/25
 */
@Service
@RefreshScope
@Slf4j
public class SaleAppealSelectFlowFormServiceImpl extends BaseServiceImpl<SaleAppealSelectFlowFormMapper, SaleAppealSelectFlowFormDO> implements SaleAppealSelectFlowFormService {
    @Autowired
    private FormService formService;
    @Autowired
    private SalesAppealExtFormService salesAppealExtFormService;
    @Autowired
    private NoService noService;

    @DubboReference
    FlowWashSaleReportApi flowWashSaleReportApi;

    @Override
    public boolean saveSelectAppealFlowData(SaveSelectAppealFlowFormRequest request) {
        LambdaQueryWrapper<SaleAppealSelectFlowFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SaleAppealSelectFlowFormDO::getFormId, request.getFormId());
        wrapper.eq(SaleAppealSelectFlowFormDO::getId, request.getId());
        wrapper.eq(SaleAppealSelectFlowFormDO::getDelFlag, 0);
        SaleAppealSelectFlowFormDO saleAppealSelectFlowFormDO = this.getOne(wrapper);
        if(Objects.isNull(request.getAppealFinalQuantity())){
            throw new BusinessException(FleeFormErrorCode.ERROR_19019);
        }
        if(Objects.isNull(request.getChangeCode())){
            throw new BusinessException(FleeFormErrorCode.ERROR_19019);
        }
        if(Objects.isNull(request.getChangeName())){
            throw new BusinessException(FleeFormErrorCode.ERROR_19019);
        }
        if(Objects.isNull(request.getChangeType())){
            throw new BusinessException(FleeFormErrorCode.ERROR_19019);
        }
        // a、申诉数量必须小于等于历史数量（盒），输入框默认空值，不允许输入0或负数。如输入不符合要求，保存时提示:“申诉数量必须小于等于历史数量”
        Pattern pattern = Pattern.compile("[0-9]+\\.?[0-9]*");
        String fields = String.valueOf(request.getAppealFinalQuantity());
        Matcher number = pattern.matcher(fields);
        if (!number.matches()) {
            throw new BusinessException(FleeFormErrorCode.Error_19009);
        }
        if (request.getAppealFinalQuantity().compareTo(BigDecimal.ZERO) == 0) {
            throw new BusinessException(FleeFormErrorCode.Error_19009);
        }
        if (request.getAppealFinalQuantity().compareTo(saleAppealSelectFlowFormDO.getFinalQuantity()) > 0) {
            throw new BusinessException(FleeFormErrorCode.Error_19010);
        }
        // b. 选择流向列表中的商业名称不能重复，选择流向ID不能重复。如选择多个商业公司，
        // 保存时提示:“申诉数据必须为同一商业”；如同一条流向数据（ID相同）被选择多次，保存时提示:“申诉数据（ID）不能重复”
        if (Objects.nonNull(request.getFormId())) { // 如果formId已经存在了，此时需要去校验表单中
            List<SaleAppealSelectFlowFormDO> saleAppealSelectFlowFormDOList = this.ListByFormId(request.getFormId(), 1);
            List<Long> crmIdList = saleAppealSelectFlowFormDOList.stream().map(SaleAppealSelectFlowFormDO::getCrmId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(crmIdList) && !crmIdList.contains(saleAppealSelectFlowFormDO.getCrmId())) {
                throw new BusinessException(FleeFormErrorCode.Error_19011);
            }
            List<String> selectFlowKeyList = saleAppealSelectFlowFormDOList.stream().map(SaleAppealSelectFlowFormDO::getFlowKey).collect(Collectors.toList());
            // 过滤出当前表单id中待提交的所有数据id
            List<Long> idList = saleAppealSelectFlowFormDOList.stream().map(SaleAppealSelectFlowFormDO::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(selectFlowKeyList)
                    && !idList.contains(request.getId()) // 如果当前操作的id不在待提交的数据中，并且数据流向id在当前表单id中待提交的所有流向id时，弹窗提示
                    && selectFlowKeyList.contains(saleAppealSelectFlowFormDO.getFlowKey())) {
                throw new BusinessException(FleeFormErrorCode.Error_19012);
            }
        }
        // c. 不同的申诉类型，对应的机构名称(申诉后)、产品品名(申诉后) 、终端类型(申诉后) 不能与原流向一致。如机构名称(申诉后)、产品品名(申诉后) 、终端类型(申诉后)与原流向一致，
        // 保存时提示:“机构名称(申诉后)\产品品名(申诉后) \终端类型(申诉后)不能与原流向一致”
        if (request.getChangeType() == 1 && (request.getChangeCode().equals(saleAppealSelectFlowFormDO.getCustomerCrmId())) || request.getChangeName().equals(saleAppealSelectFlowFormDO.getEname())) { // 判断机构名称申诉后是否与原流向一样
            throw new BusinessException(FleeFormErrorCode.NAME_IS_SAME);
        }
        if (request.getChangeType() == 2 && (request.getChangeCode().equals(saleAppealSelectFlowFormDO.getGoodsCode())) || request.getChangeName().equals(saleAppealSelectFlowFormDO.getGoodsName())) { // 判断产品名称申诉后是否与原流向一样
            throw new BusinessException(FleeFormErrorCode.ERROR_19016);
        }
        if (request.getChangeType() == 3 && request.getChangeCode() == saleAppealSelectFlowFormDO.getCustomerSupplyChainRole().intValue()) { // 判断终端类型申诉后是否与原流向一样
            throw new BusinessException(FleeFormErrorCode.ERROR_19017);
        }
        if (Objects.isNull(request.getFormId()) || 0L == request.getFormId()) {
            CreateFormRequest createFormRequest = new CreateFormRequest();
            createFormRequest.setOpUserId(request.getOpUserId());
            createFormRequest.setOpTime(request.getOpTime());
            createFormRequest.setCode(noService.genNo(FormNoEnum.SALES_APPEAL));
            createFormRequest.setType(FormTypeEnum.SALES_APPEAL.getCode());
            createFormRequest.setName(OaTodoUtils.genTitle(FormTypeEnum.SALES_APPEAL.getName(), request.getEmpName(), createFormRequest.getCode(), new Date()));
            createFormRequest.setTransferType(request.getTransferType());
            Long formId = formService.create(createFormRequest);
            request.setFormId(formId);

        }
//        SalesAppealExtFormDO goodsFormExtDO = PojoUtils.map(request, SalesAppealExtFormDO.class);
        LambdaQueryWrapper<SalesAppealExtFormDO> salesAppealExtFormDOLambdaQueryWrapper = Wrappers.lambdaQuery();
        salesAppealExtFormDOLambdaQueryWrapper.eq(SalesAppealExtFormDO::getFormId, request.getFormId());
        SalesAppealExtFormDO goodsFormExtDO = salesAppealExtFormService.getOne(salesAppealExtFormDOLambdaQueryWrapper);
        goodsFormExtDO.setFormId(request.getFormId());
        goodsFormExtDO.setConfirmStatus(FleeingConfirmStatusEnum.TO_BE_SUBMIT.getCode());
        goodsFormExtDO.setAppealType(request.getAppealType());
        salesAppealExtFormService.saveOrUpdate(goodsFormExtDO);

        // 选择流向id
        saleAppealSelectFlowFormDO.setSelectFlowId(saleAppealSelectFlowFormDO.getSelectFlowId());
        // 销售日期
        saleAppealSelectFlowFormDO.setSaleTime(saleAppealSelectFlowFormDO.getSaleTime());
        // 表单id
        saleAppealSelectFlowFormDO.setFormId(request.getFormId());
        // 表单类型 1销量申诉表，2销量申诉确认表，
        saleAppealSelectFlowFormDO.setType(1);
        // 机构名称
        saleAppealSelectFlowFormDO.setOrgName(saleAppealSelectFlowFormDO.getOrgName());
        // 传输方式
        saleAppealSelectFlowFormDO.setTransferType(2);
        // 保存状态：0-选择确认  1-待提交 3、提交审核
        saleAppealSelectFlowFormDO.setSaveStatus(1);
        saleAppealSelectFlowFormDO.setAppealType(request.getAppealType());
        saleAppealSelectFlowFormDO.setChangeType(request.getChangeType());
        saleAppealSelectFlowFormDO.setChangeName(request.getChangeName());
        saleAppealSelectFlowFormDO.setChangeCode(request.getChangeCode());
        // 申诉后产品品规
        saleAppealSelectFlowFormDO.setAppealGoodsSpec(request.getAppealGoodsSpec());
        // 申诉数量
        saleAppealSelectFlowFormDO.setAppealFinalQuantity(request.getAppealFinalQuantity());
        // 备注
        saleAppealSelectFlowFormDO.setRemark("销售申诉选择流向");
        this.saveOrUpdate(saleAppealSelectFlowFormDO);
        return true;
    }

    @Override
    public boolean removeById(RemoveSelectAppealFlowFormRequest request) {
        if (!Objects.isNull(request.getId())) {
            SaleAppealSelectFlowFormDO saleAppealSelectFlowFormDO = PojoUtils.map(request, SaleAppealSelectFlowFormDO.class);
            return this.deleteByIdWithFill(saleAppealSelectFlowFormDO) > 0;
        }
        return true;
    }

    @Override
    public List<SaleAppealSelectFlowFormDO> ListByFormId(Long formId, Integer type) {
        LambdaQueryWrapper<SaleAppealSelectFlowFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SaleAppealSelectFlowFormDO::getType, type);
        wrapper.eq(SaleAppealSelectFlowFormDO::getDelFlag, 0);
        wrapper.eq(SaleAppealSelectFlowFormDO::getSaveStatus, 1);
        wrapper.eq(SaleAppealSelectFlowFormDO::getFormId, formId).orderByDesc(SaleAppealSelectFlowFormDO::getId);
        return this.list(wrapper);
    }

    @Override
    public String saveAppealFlowDataAll(SaveSelectAppealFlowFormRequest request) {
        if(CollectionUtil.isEmpty(request.getAppealFlowDataDetailFormList())){
            return null;
        }
        if (Objects.isNull(request.getFormId()) || 0L == request.getFormId()) {
            CreateFormRequest createFormRequest = new CreateFormRequest();
            createFormRequest.setOpUserId(request.getOpUserId());
            createFormRequest.setOpTime(request.getOpTime());
            createFormRequest.setCode(noService.genNo(FormNoEnum.SALES_APPEAL));
            createFormRequest.setType(FormTypeEnum.SALES_APPEAL.getCode());
            createFormRequest.setName(OaTodoUtils.genTitle(FormTypeEnum.SALES_APPEAL.getName(), request.getEmpName(), createFormRequest.getCode(), new Date()));
            createFormRequest.setTransferType(request.getTransferType());
            Long formId = formService.create(createFormRequest);
            request.setFormId(formId);

            SalesAppealExtFormDO goodsFormExtDO = PojoUtils.map(request, SalesAppealExtFormDO.class);
            goodsFormExtDO.setFormId(formId);
            goodsFormExtDO.setConfirmStatus(FleeingConfirmStatusEnum.TO_BE_SUBMIT.getCode());
            salesAppealExtFormService.save(goodsFormExtDO);
        }

        List<SelectAppealConfirmFlowFormRequest> appealFlowDataDetailFormList = request.getAppealFlowDataDetailFormList();
        List<SaleAppealSelectFlowFormDO> saleAppealSelectFlowFormDOList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(appealFlowDataDetailFormList)) {
//            // 获取当前请求参数所有的流向key
//            List<String> flowKeyList = appealFlowDataDetailFormList.stream().map(x -> x.getFlowKey()).collect(Collectors.toList());
//            List<FlowWashSaleReportDTO> flowWashSaleReportDTOListByRequest = flowWashSaleReportApi.listByFlowKey(flowKeyList);
//            // 查询当前请求参数中的商业名称列表
//            List<String> enameList = flowWashSaleReportDTOListByRequest.stream().map(x -> x.getEname()).distinct().collect(Collectors.toList());
//
//            // 获取参数中申诉数据必须为同一商业
//            Map<String, List<FlowWashSaleReportDTO>> collect = flowWashSaleReportDTOListByRequest.stream().collect(Collectors.groupingBy(FlowWashSaleReportDTO::getEname));
//            if(collect.size()> 1){// 根据商业名称分组，如果根据名称分组大于1的证明有多个商业
//                // 申诉数据必须为同一商业
//                throw new BusinessException(FleeFormErrorCode.ERROR_19015);
//            }
//            LambdaQueryWrapper<SaleAppealSelectFlowFormDO> wrapper = Wrappers.lambdaQuery();
//            wrapper.eq(SaleAppealSelectFlowFormDO::getDelFlag, 0);
//            wrapper.eq(SaleAppealSelectFlowFormDO::getFormId, request.getFormId());
//            List<SaleAppealSelectFlowFormDO> saleAppealSelectFlowFormDOS = this.list(wrapper);
//            // 获取数据库中当前表单id所有的数据中是否包含请求中的商业名称
//            if ((!Objects.isNull(request.getFormId()) || 0L != request.getFormId()) && CollectionUtil.isNotEmpty(saleAppealSelectFlowFormDOS)) {
//                for (String eName : enameList) {
//                    LambdaQueryWrapper<SaleAppealSelectFlowFormDO> selectFlowFormDOLambdaQueryWrapper = Wrappers.lambdaQuery();
//                    selectFlowFormDOLambdaQueryWrapper.eq(SaleAppealSelectFlowFormDO::getDelFlag, 0);
//                    selectFlowFormDOLambdaQueryWrapper.eq(SaleAppealSelectFlowFormDO::getFormId, request.getFormId());
//                    selectFlowFormDOLambdaQueryWrapper.eq(SaleAppealSelectFlowFormDO::getEname, eName);
//                    // 查询当前请求中商业名称不在数据库中时，证明当前商业名称与数据库中商业名称不属于一个商业
//                    List<SaleAppealSelectFlowFormDO> saleAppealSelectFlowFormDOLists = this.list(selectFlowFormDOLambdaQueryWrapper);
//                    if(CollectionUtil.isEmpty(saleAppealSelectFlowFormDOLists)){
//                        // 申诉数据必须为同一商业
//                        throw new BusinessException(FleeFormErrorCode.ERROR_19015);
//                    }
//                }
//            }

            for (SelectAppealConfirmFlowFormRequest selectAppealFlowFormRequest : appealFlowDataDetailFormList) {
                List<String> flowKeyStringList = Arrays.asList(String.valueOf(selectAppealFlowFormRequest.getFlowKey()));
                List<FlowWashSaleReportDTO> flowWashSaleReportDTOList = flowWashSaleReportApi.listByFlowKey(flowKeyStringList);
                Optional<FlowWashSaleReportDTO> first = flowWashSaleReportDTOList.stream().findFirst();
                SaleAppealSelectFlowFormDO saleAppealSelectFlowFormDO = new SaleAppealSelectFlowFormDO();
                saleAppealSelectFlowFormDO.setFormId(request.getFormId());
                saleAppealSelectFlowFormDO.setSelectFlowId(selectAppealFlowFormRequest.getSelectFlowId());
                saleAppealSelectFlowFormDO.setFlowKey(selectAppealFlowFormRequest.getFlowKey());
                saleAppealSelectFlowFormDO.setType(1);
                saleAppealSelectFlowFormDO.setSaleTime(first.isPresent() ? first.get().getSoTime() : null);
                saleAppealSelectFlowFormDO.setCrmId(first.isPresent() ? first.get().getCrmId() : null);
                saleAppealSelectFlowFormDO.setEname(first.isPresent() ? first.get().getEname() : "");
                saleAppealSelectFlowFormDO.setCustomerCrmId(first.isPresent() ? first.get().getCustomerCrmId() : null);
                saleAppealSelectFlowFormDO.setOrgName(first.isPresent() ? first.get().getEnterpriseName() : "");
                saleAppealSelectFlowFormDO.setCustomerSupplyChainRole(first.isPresent() ? first.get().getCustomerSupplyChainRole() : null);
                saleAppealSelectFlowFormDO.setGoodsCode(first.isPresent() ? first.get().getGoodsCode() : null);
                saleAppealSelectFlowFormDO.setGoodsName(first.isPresent() ? first.get().getGoodsName() : "");
                saleAppealSelectFlowFormDO.setGoodsSpec(first.isPresent() ? first.get().getGoodsSpec() : "");
                saleAppealSelectFlowFormDO.setFinalQuantity(first.isPresent() ? first.get().getFinalQuantity() : BigDecimal.ZERO);
                saleAppealSelectFlowFormDO.setSoTotalAmount(first.isPresent() ? first.get().getSoTotalAmount() : BigDecimal.ZERO);
                saleAppealSelectFlowFormDO.setSoUnit(first.isPresent() ? first.get().getSoUnit() : "");
                saleAppealSelectFlowFormDO.setAppealType(request.getAppealType());
                saleAppealSelectFlowFormDO.setDelFlag(0);
                saleAppealSelectFlowFormDO.setCreateUser(request.getOpUserId());
                saleAppealSelectFlowFormDO.setCreateTime(new Date());
                saleAppealSelectFlowFormDO.setUpdateUser(request.getOpUserId());
                saleAppealSelectFlowFormDO.setUpdateTime(new Date());
                saleAppealSelectFlowFormDO.setRemark("销售申诉选择流向");
                saleAppealSelectFlowFormDO.setSalesPrice(first.isPresent() ? first.get().getSalesPrice() : BigDecimal.ZERO);
                saleAppealSelectFlowFormDO.setTransferType(request.getTransferType());
                saleAppealSelectFlowFormDO.setOpUserId(request.getOpUserId());
                saleAppealSelectFlowFormDO.setOpTime(new Date());
                saleAppealSelectFlowFormDOList.add(saleAppealSelectFlowFormDO);
            }
            this.saveOrUpdateBatch(saleAppealSelectFlowFormDOList);
        }
        return String.valueOf(request.getFormId());
    }

    @Override
    public List<SaleAppealSelectFlowFormDO> ListByFormIdAndType(Long formId, Integer type) {
        LambdaQueryWrapper<SaleAppealSelectFlowFormDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SaleAppealSelectFlowFormDO::getType, type);
        wrapper.eq(SaleAppealSelectFlowFormDO::getDelFlag, 0);
        wrapper.eq(SaleAppealSelectFlowFormDO::getFormId, formId).orderByDesc(SaleAppealSelectFlowFormDO::getId);
        return this.list(wrapper);
    }
}
