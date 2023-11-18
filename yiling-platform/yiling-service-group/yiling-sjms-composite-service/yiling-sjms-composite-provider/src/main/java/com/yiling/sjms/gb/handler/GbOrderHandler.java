package com.yiling.sjms.gb.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.yiling.dataflow.gb.api.GbAppealFormApi;
import com.yiling.dataflow.gb.api.GbOrderApi;
import com.yiling.dataflow.gb.bo.GbFormReviewMessageBO;
import com.yiling.dataflow.gb.dto.GbOrderDTO;
import com.yiling.dataflow.gb.dto.request.SaveGbAppealFormRequest;
import com.yiling.dataflow.gb.dto.request.SaveOrUpdateGbOrderRequest;
import com.yiling.dataflow.gb.enums.GbExecTypeEnum;
import com.yiling.dataflow.gb.enums.GbOrderExecStatusEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.gb.api.GbFormApi;
import com.yiling.sjms.gb.dto.GbFlowCompanyRelationDTO;
import com.yiling.sjms.gb.dto.GbFormFlowDTO;
import com.yiling.sjms.gb.dto.GoodsInfoDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2023/6/9
 */
@Slf4j
@Service
public class GbOrderHandler {

    @DubboReference(timeout = 10 * 1000)
    private GbOrderApi gbOrderApi;
    @DubboReference(timeout = 10 * 1000)
    private GbFormApi gbFormApi;
    @DubboReference(timeout = 10 * 1000)
    private GbAppealFormApi gbAppealFormApi;

    /**
     * 团购提报审批通过，同步到团购处理的团购数据
     *
     * @param formId 团购提报表单ID
     * @return
     */
    public boolean gbOrderApproved(Long formId) {
        // 通过formId查询团购数据
        GbFormFlowDTO gbFormFlowDTO = gbFormApi.getGbFormFlowList(formId);
        if (ObjectUtil.isNull(gbFormFlowDTO)) {
            log.warn("[团购审批通过同步团购处理], 根据formId查询团购提报数据为空, formId:{}", formId);
            return false;
        }
        // 然后插入或者修改主数据 ，新增商品明细数据
        List<SaveOrUpdateGbOrderRequest> saveOrUpdateList = new ArrayList<>();
        buildSaveOrUpdateGbOrderRequest(gbFormFlowDTO, saveOrUpdateList);
        if (CollUtil.isEmpty(saveOrUpdateList)) {
            log.warn("[团购审批通过同步团购处理], 根据formId查询团购提报商品明细数据为空, formId:{}", formId);
            return false;
        }

        // 根据formId查询已保存团购，key -> 主要字段拼接唯一值，value -> id
        Map<String, Long> oldMap = new HashMap<>();
        List<GbOrderDTO> oldGbOrderList = gbOrderApi.listByFormId(formId);
        // 根据关键字段生成key, 根据key判断是新增、更新
        for (GbOrderDTO gbOrderDTO : oldGbOrderList) {
            String key = getUniqueKey(gbOrderDTO);
            oldMap.put(key, gbOrderDTO.getId());
        }
        // 待保存、更新数据
        List<Long> updateIds = new ArrayList<>();
        for (SaveOrUpdateGbOrderRequest saveOrUpdateRequest : saveOrUpdateList) {
            GbOrderDTO gbOrderDTORequest = PojoUtils.map(saveOrUpdateRequest, GbOrderDTO.class);
            String requestKey = getUniqueKey(gbOrderDTORequest);
            Long id = oldMap.get(requestKey);
            if (ObjectUtil.isNotNull(id)) {
                saveOrUpdateRequest.setId(id);
                updateIds.add(id);
            }
        }
        List<Long> gbOrderIds = gbOrderApi.saveOrUpdateBatch(saveOrUpdateList);
        if (CollUtil.isEmpty(gbOrderIds)) {
            log.warn("[团购审批通过同步团购处理], 保存团购数据返回ID为空, formId:{}", formId);
            return false;
        }

        // 新增的团购数据，创建团购申请
        List<Long> saveGbOrderIds = gbOrderIds.stream().filter(o -> !updateIds.contains(o)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(saveGbOrderIds)) {
            return addGbAppealForm(saveGbOrderIds, "团购审批通过同步团购处理");
        }

        return true;
    }

    private boolean addGbAppealForm(List<Long> gbOrderIds, String method) {
        SaveGbAppealFormRequest request = new SaveGbAppealFormRequest();
        request.setGbOrderIdList(gbOrderIds);
        request.setExecType(GbExecTypeEnum.AUTO.getCode());
        request.setOpUserId(0L);
        request.setOpTime(new Date());
        try {
            boolean saveFlag = gbAppealFormApi.saveList(request);
            if (!saveFlag) {
                log.warn("["+method+"], 创建团购处理失败, execType:{}, gbOrderId:{}", "自动", gbOrderIds);
                return false;
            }
        } catch (Exception e) {
            log.warn("["+method+"], 创建团购处理异常, execType:{}, gbOrderId:{}, exception:{}", "自动", gbOrderIds, e.getMessage());
            return true;
        }
        return true;
    }

    /**
     * 团购提报复核，同步到团购处理的团购数据
     *
     * @param list 团购提报复核列表
     * @return
     */
    public boolean gbOrderReviewed(List<GbFormReviewMessageBO> list) {
        // 一批处理50个formId
        List<List<GbFormReviewMessageBO>> partList = Lists.partition(list, 50);
        for (List<GbFormReviewMessageBO> reviewMessageList : partList) {
            List<Long> formIds = reviewMessageList.stream().map(GbFormReviewMessageBO::getFormId).distinct().collect(Collectors.toList());

            // form_id是否已存在, map: key -> formId, value -> id
            Map<Long, Long> oldMap = new HashMap<>();
            List<GbOrderDTO> gbOrderDTOS = gbOrderApi.listByFormIdList(formIds);
            if (CollUtil.isNotEmpty(gbOrderDTOS)) {
                oldMap = gbOrderDTOS.stream().collect(Collectors.toMap(GbOrderDTO::getFormId, o -> o.getId(), (k1,k2) -> k1));
            }

            // 更新团购数据复核状态
            List<SaveOrUpdateGbOrderRequest> updateList = new ArrayList<>();
            // 插入数据
            List<SaveOrUpdateGbOrderRequest> saveList = new ArrayList<>();

            SaveOrUpdateGbOrderRequest request;
            for (GbFormReviewMessageBO gbFormReviewMessageBO : reviewMessageList) {
                Long formId = gbFormReviewMessageBO.getFormId();
                Long id = oldMap.get(formId);
                if (ObjectUtil.isNotNull(id)) {
                    request = new SaveOrUpdateGbOrderRequest();
                    request.setId(id);
                    request.setCheckStatus(gbFormReviewMessageBO.getReviewStatus());
                    request.setGbRemark(gbFormReviewMessageBO.getReviewReply());
                    updateList.add(request);
                } else {
                    // 根据formId查询团购提报
                    GbFormFlowDTO gbFormFlowDTO = gbFormApi.getGbFormFlowList(formId);
                    if (ObjectUtil.isNull(gbFormFlowDTO)) {
                        log.warn("[团购复核同步团购处理], 此团购提报查询不存在，formId:{}", formId);
                        continue;
                    }
                    buildSaveOrUpdateGbOrderRequest(gbFormFlowDTO, saveList);
                }
            }

            if (CollUtil.isNotEmpty(updateList)) {
                gbOrderApi.saveOrUpdateBatch(updateList);
            }

            if (CollUtil.isNotEmpty(saveList)) {
                List<Long> gbOrderIds = gbOrderApi.saveOrUpdateBatch(saveList);
                // 新增的团购数据，创建团购申请
                return addGbAppealForm(gbOrderIds, "团购复核同步团购处理");
            }
        }
        return true;
    }

    private String getUniqueKey(GbOrderDTO gbOrderDTO) {
        StringBuffer sb = new StringBuffer();
        sb.append(gbOrderDTO.getFormId()).append(gbOrderDTO.getGbNo()).append(gbOrderDTO.getGbProcess()).append(gbOrderDTO.getGbMonth()).append(gbOrderDTO.getFlowMonth())
                .append(gbOrderDTO.getCrmId()).append(gbOrderDTO.getOrgCrmId()).append(gbOrderDTO.getEnterpriseName()).append(gbOrderDTO.getGoodsCode());
        return sb.toString();
    }

    private void buildSaveOrUpdateGbOrderRequest(GbFormFlowDTO gbFormFlowDTO, List<SaveOrUpdateGbOrderRequest> saveOrUpdateList) {
        SaveOrUpdateGbOrderRequest request;
        List<GbFlowCompanyRelationDTO> companyRelationList = gbFormFlowDTO.getCompanyRelationList();
        if (CollUtil.isNotEmpty(companyRelationList)) {
            for (GbFlowCompanyRelationDTO gbFlowCompanyRelationDTO : companyRelationList) {
                List<GoodsInfoDTO> goodsList = gbFlowCompanyRelationDTO.getGoodsList();
                if (CollUtil.isNotEmpty(companyRelationList)) {
                    for (GoodsInfoDTO goodsInfoDTO : goodsList) {
                        // 仅接受团购提报，不需要其他的业务类型
                        if (!FormTypeEnum.GB_SUBMIT.getCode().equals(gbFormFlowDTO.getBizType())) {
                            continue;
                        }
                        request = new SaveOrUpdateGbOrderRequest();
                        // 商品信息
                        request.setGoodsCode(goodsInfoDTO.getCode());
                        request.setGoodsName(goodsInfoDTO.getName());
                        request.setGbQuantity(new BigDecimal(goodsInfoDTO.getQuantityBox().toString()));
                        request.setFlowMonth(DateUtil.format(goodsInfoDTO.getFlowMonth(), "yyyy-MM"));
                        // 出库商业、出库终端
                        request.setFormId(gbFlowCompanyRelationDTO.getFormId());
                        request.setCrmId(gbFlowCompanyRelationDTO.getBusinessCompanyId());
                        request.setEname(gbFlowCompanyRelationDTO.getBusinessCompanyName());
                        request.setOrgCrmId(gbFlowCompanyRelationDTO.getTermainalCompanyId());
                        request.setEnterpriseName(gbFlowCompanyRelationDTO.getTermainalCompanyName());
                        // 团购表单信息
                        request.setGbNo(gbFormFlowDTO.getGbNo());
                        request.setGbProcess(gbFormFlowDTO.getBizType());
                        request.setGbMonth(DateUtil.format(gbFormFlowDTO.getMonth(), "yyyy-MM"));
                        request.setSellerEmpId(gbFormFlowDTO.getSellerEmpId());
                        request.setSellerEmpName(gbFormFlowDTO.getSellerEmpName());
                        request.setCustomerId(gbFormFlowDTO.getCustomerId());
                        request.setCustomerName(gbFormFlowDTO.getCustomerName());
                        request.setGbType(gbFormFlowDTO.getGbType());
                        request.setAuditStatus(FormStatusEnum.APPROVE.getCode());
                        request.setCheckStatus(gbFormFlowDTO.getReviewStatus());
                        request.setGbRemark(gbFormFlowDTO.getReviewReply());
                        request.setCreateName(gbFormFlowDTO.getEmpName());
                        request.setCreateTime(gbFormFlowDTO.getSubmitTime());
                        request.setExecStatus(GbOrderExecStatusEnum.UN_START.getCode());
                        saveOrUpdateList.add(request);
                    }
                }
            }
        }
    }

}
