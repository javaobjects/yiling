//package com.yiling.admin.data.center.report.handler;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import org.apache.dubbo.config.annotation.DubboReference;
//import org.springframework.stereotype.Component;
//
//import com.yiling.admin.data.center.report.form.ImportFlowGoodsRelationForm;
//import com.yiling.dataflow.order.dto.FlowGoodsRelationDTO;
//import com.yiling.dataflow.order.dto.request.UpdateFlowGoodsRelationRequest;
//import com.yiling.dataflow.relation.api.FlowGoodsRelationApi;
//import com.yiling.dataflow.relation.api.FlowGoodsRelationEditTaskApi;
//import com.yiling.dataflow.relation.enums.FlowGoodsRelationEditTaskSyncStatusEnum;
//import com.yiling.dataflow.relation.enums.FlowGoodsRelationLabelEnum;
//import com.yiling.export.excel.handler.ImportDataHandler;
//import com.yiling.framework.common.enums.ResultCode;
//import com.yiling.framework.common.exception.BusinessException;
//import com.yiling.framework.common.mybatis.MyMetaHandler;
//import com.yiling.framework.common.util.Constants;
//import com.yiling.framework.common.util.PojoUtils;
//import com.yiling.goods.medicine.api.GoodsApi;
//import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
//import com.yiling.user.enterprise.api.EnterpriseApi;
//import com.yiling.user.enterprise.dto.EnterpriseDTO;
//
//import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
//import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.collection.ListUtil;
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.StrUtil;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author: houjie.sun
// * @date: 2022/7/25
// */
//@Slf4j
//@Component
//public class ImportFlowGoodsRelationHandler implements IExcelVerifyHandler<ImportFlowGoodsRelationForm>, ImportDataHandler<ImportFlowGoodsRelationForm> {
//
//    @DubboReference
//    EnterpriseApi enterpriseApi;
//    @DubboReference(timeout = 60 * 1000)
//    FlowGoodsRelationApi flowGoodsRelationApi;
//    @DubboReference(timeout = 60 * 1000)
//    GoodsApi goodsApi;
//    @DubboReference
//    private FlowGoodsRelationEditTaskApi flowGoodsRelationEditTaskApi;
//
//    private ExcelVerifyHandlerResult error(String errorMessage) {
//        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(false);
//        result.setMsg(errorMessage);
//        return result;
//    }
//
//    @Override
//    public ExcelVerifyHandlerResult verifyHandler(ImportFlowGoodsRelationForm form) {
//        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
//        Long eid = form.getEid();
//        if (ObjectUtil.isNull(eid)) {
//            return this.error("商业ID不能为空");
//        }
//        if (eid.intValue() <= 0) {
//            return this.error("商业ID不能为0或负数");
//        }
//        String goodsInSn = form.getGoodsInSn();
//        if (StrUtil.isBlank(goodsInSn)) {
//            return this.error("商品内码不能为空");
//        }
//        Integer goodsRelationLabel = form.getGoodsRelationLabel();
//        if(ObjectUtil.isNull(goodsRelationLabel) || FlowGoodsRelationLabelEnum.EMPTY.getCode() == goodsRelationLabel.intValue()){
//            return this.error("商品关系标签不能为空、不能为无标签");
//        }
//        Long ylGoodsId = form.getYlGoodsId();
//        if(FlowGoodsRelationLabelEnum.YILING.getCode() == goodsRelationLabel.intValue()){
//            if(ObjectUtil.isNull(ylGoodsId) || 0 == ylGoodsId.intValue()){
//                return this.error("以岭商品ID不能为空或0");
//            }
//        }
//        if (ylGoodsId.intValue() <= 0) {
//            return this.error("以岭商品ID不能为0或负数");
//        }
//
//        EnterpriseDTO enterprise = enterpriseApi.getById(eid);
//        if (ObjectUtil.isNull(enterprise)) {
//            return this.error("未找到商业ID对应的公司信息");
//        }
//        FlowGoodsRelationDTO flowGoodsRelation = flowGoodsRelationApi.getByEidAndGoodsInSn(eid, goodsInSn);
//        if (ObjectUtil.isNull(flowGoodsRelation)) {
//            return this.error("未找到商业ID、商品内码对应的商家品与以岭品关系信息");
//        }
//        if (ObjectUtil.isNotNull(flowGoodsRelation.getYlGoodsId()) && flowGoodsRelation.getYlGoodsId().intValue() != 0) {
//            return this.error("商业ID、商品内码对应的以岭商品ID已经存在");
//        }
//        // 校验当前企业是否存在正在同步的以岭品关系修改，因返利报表的计算和驳回维度是按照企业id
//        Boolean relationEditTaskFlag = flowGoodsRelationEditTaskApi.existFlowGoodsRelationEditTask(flowGoodsRelation.getId(), FlowGoodsRelationEditTaskSyncStatusEnum.DOING.getCode());
//        if(relationEditTaskFlag){
//            throw new BusinessException(ResultCode.FAILED, "以岭品关系导入，此以岭商品存在未完成的修改任务，正在处理任务，请稍后再操作");
//        }
//        // 是否以岭品
//        List<Long> ylEidList = enterpriseApi.listSubEids(Constants.YILING_EID);
//        Map<Long, Long> yilingGoodsMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(ListUtil.toList(ylGoodsId), ylEidList);
//        Long queryYlGoodsId = Optional.ofNullable(yilingGoodsMap.get(ylGoodsId)).orElse(0L);
//        boolean notYl = Long.valueOf(0).equals(queryYlGoodsId);
//        if (notYl) {
//            return this.error("未找到此以岭商品ID对应的以岭品信息");
//        }
//        form.setId(flowGoodsRelation.getId());
//        return result;
//    }
//
//    @Override
//    public List<ImportFlowGoodsRelationForm> execute(List<ImportFlowGoodsRelationForm> object, Map<String, Object> paramMap) {
//        Long opUserId = (Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID, 0L);
//        Date opTime = new Date();
//        // 根据以岭品id带入名称、规格
//        Map<Long, StandardGoodsBasicDTO> ylGoodsMap = new HashMap<>();
//        if (CollUtil.isNotEmpty(object)) {
//            List<Long> ylGoodsIdList = object.stream().map(ImportFlowGoodsRelationForm::getYlGoodsId).distinct().collect(Collectors.toList());
//            List<StandardGoodsBasicDTO> standardGoodsBasicList = goodsApi.batchQueryStandardGoodsBasic(ylGoodsIdList);
//            if (CollUtil.isNotEmpty(standardGoodsBasicList)) {
//                for (StandardGoodsBasicDTO standardGoodsBasicDTO : standardGoodsBasicList) {
//                    if (!ylGoodsMap.containsKey(standardGoodsBasicDTO.getId())) {
//                        ylGoodsMap.put(standardGoodsBasicDTO.getId(), standardGoodsBasicDTO);
//                    }
//                }
//            }
//        }
//        for (ImportFlowGoodsRelationForm form : object) {
//            try {
//                UpdateFlowGoodsRelationRequest request = PojoUtils.map(form, UpdateFlowGoodsRelationRequest.class);
//                request.setId(form.getId());
//                request.setOpUserId(opUserId);
//                request.setOpTime(opTime);
//                StandardGoodsBasicDTO standardGoodsBasicDTO = ylGoodsMap.get(form.getYlGoodsId());
//                if (ObjectUtil.isNotNull(standardGoodsBasicDTO)) {
//                    request.setYlGoodsName(standardGoodsBasicDTO.getName());
//                    request.setYlGoodsSpecifications(standardGoodsBasicDTO.getSellSpecifications());
//                }
//                boolean result = flowGoodsRelationApi.edit(request);
//                if (!result) {
//                    form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
//                }
//            } catch (BusinessException be) {
//                form.setErrorMsg(be.getMessage());
//            } catch (Exception e) {
//                form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
//                log.error("商家品与以岭品关系导入, 数据保存出错：{}", e.getMessage(), e);
//            }
//        }
//        return object;
//    }
//
//}
