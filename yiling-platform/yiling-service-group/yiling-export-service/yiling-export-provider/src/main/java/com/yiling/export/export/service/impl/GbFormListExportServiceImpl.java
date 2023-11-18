
package com.yiling.export.export.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.GbFormListBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.gb.api.GbFormApi;
import com.yiling.sjms.gb.api.GbGoodsInfoApi;
import com.yiling.sjms.gb.dto.GbFormExportListDTO;
import com.yiling.sjms.gb.dto.GoodsInfoDTO;
import com.yiling.sjms.gb.dto.request.QueryGBFormListPageRequest;
import com.yiling.sjms.gb.enums.GbFormBizTypeEnum;
import com.yiling.sjms.gb.enums.GbFormCityBelowEnum;
import com.yiling.sjms.gb.enums.GbFormRegionTypeEnum;
import com.yiling.sjms.gb.enums.GbFormReviewStatusEnum;
import com.yiling.sjms.gb.enums.GbFormReviewTypeEnum;
import com.yiling.workflow.workflow.api.TaskApi;
import com.yiling.workflow.workflow.dto.CommentDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * 团购列表导出
 *
 * @author:wei.wang
 * @date:2021/8/21
 */
@Slf4j
@Service("gbFormListExportService")
public class GbFormListExportServiceImpl implements BaseExportQueryDataService<QueryGBFormListPageRequest> {

    @DubboReference
    GbFormApi gbFormApi;
    @DubboReference
    GbGoodsInfoApi gbGoodsInfoApi;
    @DubboReference
    TaskApi taskApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("bizName", "所属流程");
        FIELD.put("statusName", "状态");
        FIELD.put("srcGbNo", "团购取消关联编号");
        FIELD.put("srcGbReason", "团购取消原因");
        FIELD.put("gbNo", "团购编号");
        FIELD.put("provinceName", "省区");
        FIELD.put("orgName", "事业部");
        FIELD.put("sellerEmpId", "销量计入人工号");
        FIELD.put("sellerEmpName", "销量计入人名称");
        FIELD.put("sellerDeptName", "销量计入人区办");
        FIELD.put("sellerYxDeptName", "销量计入人部门");
        FIELD.put("managerEmpId", "团购负责人工号");
        FIELD.put("managerEmpName", "团购负责人姓名");
        FIELD.put("managerYxDeptName", "团购负责人部门");
        FIELD.put("customerName", "团购单位");
        FIELD.put("termainalCompanyName", "团购出库终端");
        FIELD.put("termainalCompanyCode", "团购出货终端CRM编码");
        FIELD.put("termainalCompanyProvince", "出库终端所在省");
        FIELD.put("termainalCompanyCity", "出库终端所在市");
        FIELD.put("businessCompanyName", "团购出库商业");
        FIELD.put("businessCompanyCode", "团购出货商业CRM编码");
        FIELD.put("businessCompanyProvince", "出库商业所在省");
        FIELD.put("businessCompanyCity", "出库商业所在市");
        FIELD.put("month", "团购月份");
        FIELD.put("paymentTime", "团购成功回款日期");
        FIELD.put("regionName", "团购区域");
        FIELD.put("gbName", "团购性质");
        FIELD.put("remark", "备注");
        FIELD.put("name", "商品名称");
        FIELD.put("flowMonthDay", "流向月份");
        FIELD.put("quantityBox", "团购数量（盒）");
        FIELD.put("quantity", "团购数量（件）");
        FIELD.put("finalPrice", "实际团购单价");
        FIELD.put("finalAmount", "实际团购金额");
        FIELD.put("price", "产品核算价");
        FIELD.put("amount", "团购核算总额");
        FIELD.put("evidences", "证据类型");
        FIELD.put("gbReviewName", "团购属性调整");
        FIELD.put("gbCityBelowName", "是否地级市及以下政府机构");
        FIELD.put("reviewStatus", "复核状态");
        FIELD.put("reviewReply", "复核意见");
        FIELD.put("reviewTime", "复核时间");
        FIELD.put("approveReply", "财务审批意见");

    }


    /**
     * 查询excel中的数据
     *
     * @param queryGBFormListPageRequest
     * @return
     */

    @Override
    public QueryExportDataDTO queryData(QueryGBFormListPageRequest queryGBFormListPageRequest) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

      //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<GbFormExportListDTO> page = null;
        int current = 1;
        do {
            queryGBFormListPageRequest.setCurrent(current);
            queryGBFormListPageRequest.setSize(500);
            page = gbFormApi.getGBFormExportListPage(queryGBFormListPageRequest);
            if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }
            List<Long> ids = page.getRecords().stream().map(s -> s.getCompanyId()).collect(Collectors.toList());

            List<GoodsInfoDTO> goodsInfoList = gbGoodsInfoApi.listByCompanyIds(ids);
            Map<Long, List<GoodsInfoDTO>> goodsInfoMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(goodsInfoList)){
                goodsInfoMap = goodsInfoList.stream().collect(Collectors.groupingBy(GoodsInfoDTO::getCompanyId));
            }
            for (GbFormExportListDTO one : page.getRecords()){
                List<GoodsInfoDTO> goodsList = goodsInfoMap.get(one.getCompanyId());


                Date month = one.getMonthDate();
                String monthFormat = "";
                if (DateUtil.compare(month, DateUtil.parseDate("1970-01-01 00:00:00")) > 0) {
                    monthFormat = DateUtil.format(month, "yyyy-MM");
                }


                StringBuffer evidences = new StringBuffer();
                if (StringUtils.isNotBlank(one.getEvidences())) {
                    evidences.append(one.getEvidences());
                }
                if (StringUtils.isNotBlank(evidences)) {
                    if (StringUtils.isNotBlank(one.getOtherEvidence())) {
                        evidences.append(",").append(one.getOtherEvidence());
                    }
                } else {
                    evidences.append(one.getOtherEvidence());
                }
                log.info("团购列表查询数据code{}",one.getGbNo());
                CommentDTO commentDTO = taskApi.queryComment(one.getGbNo());
                if (CollectionUtil.isNotEmpty(goodsList)) {
                    for (GoodsInfoDTO goodsOne : goodsList) {
                        Date paymentTime = goodsOne.getPaymentTime();
                        String format = "";
                        if (DateUtil.compare(paymentTime, DateUtil.parseDate("1970-01-01 00:00:00")) > 0) {
                            format = DateUtil.format(paymentTime, "yyyy-MM-dd");
                        }
                        GbFormListBO gbFormListOne = PojoUtils.map(one, GbFormListBO.class);
                        gbFormListOne.setBizName(GbFormBizTypeEnum.getByCode(one.getBizType()).getName());
                        gbFormListOne.setStatusName(FormStatusEnum.getByCode(one.getStatus()).getName());
                        gbFormListOne.setPaymentTime(format);
                        gbFormListOne.setRegionName(GbFormRegionTypeEnum.getByCode(one.getRegionType()) != null ? GbFormRegionTypeEnum.getByCode(one.getRegionType()).getName() : "");
                        gbFormListOne.setGbName(GbFormReviewTypeEnum.getByCode(one.getGbType()) != null ? GbFormReviewTypeEnum.getByCode(one.getGbType()).getName() : "");
                        gbFormListOne.setMonth(monthFormat);
                        gbFormListOne.setName(goodsOne.getName());
                        gbFormListOne.setQuantityBox(goodsOne.getQuantityBox());
                        gbFormListOne.setQuantity(goodsOne.getQuantity().stripTrailingZeros().toPlainString());
                        gbFormListOne.setFinalPrice(goodsOne.getFinalPrice().stripTrailingZeros().toPlainString());
                        gbFormListOne.setFinalAmount(goodsOne.getFinalAmount().stripTrailingZeros().toPlainString());
                        gbFormListOne.setPrice(goodsOne.getPrice().stripTrailingZeros().toPlainString());
                        gbFormListOne.setAmount(goodsOne.getAmount().stripTrailingZeros().toPlainString());
                        gbFormListOne.setEvidences(evidences.toString());
                        gbFormListOne.setGbReviewName(GbFormReviewTypeEnum.getByCode(one.getGbReviewType()) != null ? GbFormReviewTypeEnum.getByCode(one.getGbReviewType()).getName() : "");
                        gbFormListOne.setGbCityBelowName(GbFormCityBelowEnum.getByCode(one.getGbCityBelow()) != null ? GbFormCityBelowEnum.getByCode(one.getGbCityBelow()).getName() : "");
                        gbFormListOne.setReviewReply(one.getReviewReply());
                        if (DateUtil.compare(one.getReviewTime(), DateUtil.parseDate("1970-01-01 00:00:00")) > 0) {
                            gbFormListOne.setReviewTime(one.getReviewTime());
                        } else {
                            gbFormListOne.setReviewTime(null);
                        }
                        String formatMonth = "";
                        if (DateUtil.compare(goodsOne.getFlowMonth(), DateUtil.parseDate("1970-01-01 00:00:00")) > 0) {
                            formatMonth = DateUtil.format(goodsOne.getFlowMonth(), "yyyy-MM");
                            gbFormListOne.setFlowMonthDay(formatMonth);
                        } else {
                            gbFormListOne.setFlowMonthDay(null);
                        }
                        gbFormListOne.setReviewStatus(GbFormReviewStatusEnum.getByCode(one.getReviewStatus()) != null ? GbFormReviewStatusEnum.getByCode(one.getReviewStatus()).getName() : "");
                        gbFormListOne.setApproveReply(commentDTO.getFullMessage());
                        Map<String, Object> dataPojo = BeanUtil.beanToMap(gbFormListOne);
                        data.add(dataPojo);
                    }
                }else{
                    GbFormListBO gbFormListOne = PojoUtils.map(one, GbFormListBO.class);
                    gbFormListOne.setBizName(GbFormBizTypeEnum.getByCode(one.getBizType()).getName());
                    gbFormListOne.setStatusName(FormStatusEnum.getByCode(one.getStatus()).getName());
                    gbFormListOne.setRegionName(GbFormRegionTypeEnum.getByCode(one.getRegionType()) != null ? GbFormRegionTypeEnum.getByCode(one.getRegionType()).getName() : "");
                    gbFormListOne.setGbName(GbFormReviewTypeEnum.getByCode(one.getGbType()) != null ? GbFormReviewTypeEnum.getByCode(one.getGbType()).getName() : "");
                    gbFormListOne.setMonth(monthFormat);
                    gbFormListOne.setName("");
                    gbFormListOne.setQuantityBox(0);
                    gbFormListOne.setFlowMonthDay(null);
                    gbFormListOne.setQuantity(BigDecimal.ZERO.stripTrailingZeros().toPlainString());
                    gbFormListOne.setFinalPrice(BigDecimal.ZERO.stripTrailingZeros().toPlainString());
                    gbFormListOne.setFinalAmount(BigDecimal.ZERO.stripTrailingZeros().toPlainString());
                    gbFormListOne.setPrice(BigDecimal.ZERO.stripTrailingZeros().toPlainString());
                    gbFormListOne.setAmount(BigDecimal.ZERO.stripTrailingZeros().toPlainString());
                    gbFormListOne.setEvidences(evidences.toString());
                    gbFormListOne.setGbReviewName(GbFormReviewTypeEnum.getByCode(one.getGbReviewType()) != null ? GbFormReviewTypeEnum.getByCode(one.getGbReviewType()).getName() : "");
                    gbFormListOne.setGbCityBelowName(GbFormCityBelowEnum.getByCode(one.getGbCityBelow()) != null ? GbFormCityBelowEnum.getByCode(one.getGbCityBelow()).getName() : "");
                    gbFormListOne.setReviewReply(one.getReviewReply());
                    if (DateUtil.compare(one.getReviewTime(), DateUtil.parseDate("1970-01-01 00:00:00")) > 0) {
                        gbFormListOne.setReviewTime(one.getReviewTime());
                    } else {
                        gbFormListOne.setReviewTime(null);
                    }
                    gbFormListOne.setReviewStatus(GbFormReviewStatusEnum.getByCode(one.getReviewStatus()) != null ? GbFormReviewStatusEnum.getByCode(one.getReviewStatus()).getName() : "");
                    gbFormListOne.setApproveReply(commentDTO.getFullMessage());
                    Map<String, Object> dataPojo = BeanUtil.beanToMap(gbFormListOne);
                    data.add(dataPojo);
                }
            }
            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("团购列表");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }



    @Override
    public QueryGBFormListPageRequest getParam(Map<String, Object> map) {

        QueryGBFormListPageRequest request = PojoUtils.map(map, QueryGBFormListPageRequest.class);
        if(StringUtils.isNotBlank((String)map.get("startMonthTime"))){
            request.setStartMonth(DateUtil.beginOfMonth(DateUtil.parse((String)map.get("startMonthTime"), "yyyy-MM")));
        }

        if(StringUtils.isNotBlank((String)map.get("endMonthTime"))){
            request.setEndMonth(DateUtil.endOfMonth(DateUtil.parse((String)map.get("endMonthTime"), "yyyy-MM")));
        }

        if(request.getStartApproveTime()!= null){
            request.setStartApproveTime(DateUtil.beginOfDay(request.getStartApproveTime()));
        }
        if(request.getEndApproveTime()!= null){
            request.setEndApproveTime(DateUtil.endOfDay(request.getEndApproveTime()));
        }

        if(request.getStartSubmitTime()!= null){
            request.setStartSubmitTime(DateUtil.beginOfDay(request.getStartSubmitTime()));
        }
        if(request.getEndSubmitTime()!= null){
            request.setEndSubmitTime(DateUtil.endOfDay(request.getEndSubmitTime()));
        }
        return request;
    }
}

