package com.yiling.export.imports.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.export.excel.handler.AbstractExcelImportHandler;
import com.yiling.export.imports.model.ImportPopProcRelationModel;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.procrelation.api.PopProcGoodsTemplateApi;
import com.yiling.user.procrelation.api.PopProcTemplateApi;
import com.yiling.user.procrelation.api.ProcurementRelationApi;
import com.yiling.user.procrelation.api.ProcurementRelationGoodsApi;
import com.yiling.user.procrelation.dto.PopProcGoodsTemplateDTO;
import com.yiling.user.procrelation.dto.PopProcTemplateDTO;
import com.yiling.user.procrelation.dto.request.AddGoodsForProcRelationRequest;
import com.yiling.user.procrelation.dto.request.SaveProcRelationRequest;
import com.yiling.user.procrelation.enums.PorcRelationDeliveryTypeEnum;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入pop采购关系
 *
 * @author: dexi.yao
 * @date: 2023-06-20
 */
@Slf4j
@Component
public class ImportPopProcRelationHandler extends AbstractExcelImportHandler<ImportPopProcRelationModel> {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    PopProcTemplateApi popProcTemplateApi;
    @DubboReference
    PopProcGoodsTemplateApi goodsTemplateApi;
    @DubboReference
    ProcurementRelationApi procurementRelationApi;
    @DubboReference
    ProcurementRelationGoodsApi relationGoodsApi;


    public static List<Long> channelTypeList = ListUtil.toList(EnterpriseChannelEnum.LEVEL_1.getCode(), EnterpriseChannelEnum.LEVEL_2.getCode(), EnterpriseChannelEnum.KA.getCode(), EnterpriseChannelEnum.Z2P1.getCode());
    public static List<Long> deliverylTypeList = ListUtil.toList(EnterpriseChannelEnum.INDUSTRY.getCode(), EnterpriseChannelEnum.INDUSTRY_DIRECT.getCode(), EnterpriseChannelEnum.LEVEL_1.getCode(), EnterpriseChannelEnum.LEVEL_2.getCode(), EnterpriseChannelEnum.Z2P1.getCode());

    @Override
    public ExcelVerifyHandlerResult verify(ImportPopProcRelationModel model) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        {
            // 渠道商ID
            Long channelPartnerEid = model.getChannelPartnerEid();
            if (Objects.nonNull(channelPartnerEid) && channelPartnerEid != 0) {
                EnterpriseDTO enterpriseDTO = enterpriseApi.getById(channelPartnerEid);
                if (Objects.isNull(enterpriseDTO)) {
                    return this.error("渠道商ID不存在");
                }

                if (!channelTypeList.contains(enterpriseDTO.getChannelId())) {
                    return this.error("渠道商类型不正确");
                }
            }
        }
        {
            // 工业主体ID
            Long factoryEid = model.getFactoryEid();
            if (Objects.nonNull(factoryEid) && factoryEid != 0) {
                EnterpriseDTO enterpriseDTO = enterpriseApi.getById(factoryEid);
                if (Objects.isNull(enterpriseDTO)) {
                    return this.error("工业主体ID不存在");
                }

                if (enterpriseDTO.getChannelId() != 1) {
                    return this.error("工业主体类型不正确");
                }
            }
        }
        {
            // 配送商ID
            Long deliveryEid = model.getDeliveryEid();
            if (Objects.nonNull(deliveryEid) && deliveryEid != 0) {
                EnterpriseDTO enterpriseDTO = enterpriseApi.getById(deliveryEid);
                if (Objects.isNull(enterpriseDTO)) {
                    return this.error("配送商ID不存在");
                }

                if (!deliverylTypeList.contains(enterpriseDTO.getChannelId())) {
                    return this.error("配送商类型不正确");
                }

                Long channelId = enterpriseDTO.getChannelId();
                if (channelId == 1 && ObjectUtil.notEqual(deliveryEid,model.getFactoryEid())) {
                    return this.error("配送商为工业时，配送商与工业必须一致");
                }

            }
        }

        {
            // 履约开始时间格式校验
            String startTimeStr = model.getStartTimeStr();
            if (StrUtil.isNotEmpty(startTimeStr)) {
                try {
                    DateTime dateTime = DateUtil.parse(startTimeStr, "yyyy-MM-dd");
                } catch (Exception e) {
                    return this.error("履约开始时间格式不正确");
                }

            }

        }
        {
            // 履约结束时间格式校验
            String endTimeStr = model.getEndTimeStr();
            if (StrUtil.isNotEmpty(endTimeStr)) {
                try {
                    DateTime dateTime = DateUtil.parse(endTimeStr, "yyyy-MM-dd");
                } catch (Exception e) {
                    return this.error("履约结束时间格式不正确");
                }

            }

        }
        {
            // 履约结束时间大小校验
            String endTimeStr = model.getEndTimeStr();
            DateTime endTime=DateUtil.offsetMillisecond(DateUtil.endOfDay(DateUtil.parse(endTimeStr, "yyyy-MM-dd")), -999);
            String startTimeStr = model.getStartTimeStr();
            DateTime startTime=DateUtil.beginOfDay(DateUtil.parse(startTimeStr, "yyyy-MM-dd"));

            if (DateUtil.compare(startTime,endTime)>=0){
                return this.error("履约开始时间必须小于履约结束时间");
            }
        }
        {
            // 模板ID校验
            String templateNumber = model.getTemplateNumber();
            if (StrUtil.isNotBlank(templateNumber)) {
                PopProcTemplateDTO templateDTO = popProcTemplateApi.queryTemplateByNumber(templateNumber);
                if (Objects.isNull(templateDTO)) {
                    return this.error("模板不存在");
                }
                List<PopProcGoodsTemplateDTO> goodsList = goodsTemplateApi.queryGoodsList(templateDTO.getId(), model.getFactoryEid());
                if (CollUtil.isEmpty(goodsList)) {
                    return this.error("模板下维护的工业商品为空");
                }
            }
        }

        return result;
    }

    @Override
    public List<ImportPopProcRelationModel> importData(List<ImportPopProcRelationModel> object, Map<String, Object> paramMap) {

        Long opUser = (Long) paramMap.get(MyMetaHandler.FIELD_OP_USER_ID);

        List<Long> eidList = ListUtil.toList();
        eidList.addAll(object.stream().map(ImportPopProcRelationModel::getFactoryEid).collect(Collectors.toList()));
        eidList.addAll(object.stream().map(ImportPopProcRelationModel::getDeliveryEid).collect(Collectors.toList()));
        eidList.addAll(object.stream().map(ImportPopProcRelationModel::getChannelPartnerEid).collect(Collectors.toList()));

        eidList = eidList.stream().distinct().collect(Collectors.toList());
        List<EnterpriseDTO> eList;
        if (CollUtil.isNotEmpty(eidList)) {
            eList = enterpriseApi.listByIds(eidList);
        } else {
            eList = ListUtil.toList();
        }

        Map<Long, String> eNameMap = eList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
        Map<Long, Long> eChannelMap = eList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getChannelId));

        //查询模板商品
        Map<String, List<PopProcGoodsTemplateDTO>> goodsMap = goodsTemplateApi.queryBatchGoodsList(object.stream().map(ImportPopProcRelationModel::getTemplateNumber).collect(Collectors.toList()));


        for (ImportPopProcRelationModel form : object) {
            try {
                SaveProcRelationRequest request = PojoUtils.map(form, SaveProcRelationRequest.class);

                request.setFactoryName(eNameMap.getOrDefault(form.getFactoryEid(), ""));
                request.setDeliveryName(eNameMap.getOrDefault(form.getDeliveryEid(), ""));
                request.setChannelPartnerName(eNameMap.getOrDefault(form.getChannelPartnerEid(), ""));

                Long deliveryChannelId = eChannelMap.get(form.getDeliveryEid());
                if (deliveryChannelId == 1) {
                    request.setDeliveryType(PorcRelationDeliveryTypeEnum.FACTORY.getCode());
                } else {
                    request.setDeliveryType(PorcRelationDeliveryTypeEnum.THIRD_ENTERPRISE.getCode());
                }
                request.setStartTime(DateUtil.beginOfDay(DateUtil.parse(form.getStartTimeStr(), "yyyy-MM-dd")));
                request.setEndTime(DateUtil.offsetMillisecond(DateUtil.endOfDay(DateUtil.parse(form.getEndTimeStr(), "yyyy-MM-dd")), -999));
                request.setOpUserId(opUser);
                request.setOpTime(new Date());
                //新增关系
                Long relationId = procurementRelationApi.saveProcurementRelation(request);

                if (relationId == null || relationId == 0) {
                    form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                    continue;
                }

                //新增商品
                List<PopProcGoodsTemplateDTO> goodsList = goodsMap.get(form.getTemplateNumber());
                if (CollUtil.isEmpty(goodsList)) {
                    continue;
                }
                Map<Long, List<PopProcGoodsTemplateDTO>> factoryGoodsMap= goodsList.stream().collect(Collectors.groupingBy(PopProcGoodsTemplateDTO::getFactoryEid));
                List<PopProcGoodsTemplateDTO> factoryGoodsList = factoryGoodsMap.get(form.getFactoryEid());

                if (CollUtil.isEmpty(factoryGoodsList)) {
                    continue;
                }

                List<AddGoodsForProcRelationRequest> varList = ListUtil.toList();
                factoryGoodsList.forEach(e -> {
                    AddGoodsForProcRelationRequest var = PojoUtils.map(e, AddGoodsForProcRelationRequest.class);
                    var.setId(null);
                    var.setRelationId(relationId);
                    var.setOpUserId(opUser);
                    var.setOpTime(new Date());
                    varList.add(var);
                });
                Boolean isSuccess = relationGoodsApi.saveGoodsForProcRelation(varList);
                if (!isSuccess) {
                    form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                }

            } catch (BusinessException be) {
                form.setErrorMsg(be.getMessage());
            } catch (Exception e) {
                form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                log.error("数据保存出错：{}", e.getMessage(), e);
            }
        }
        return object;
    }

}
