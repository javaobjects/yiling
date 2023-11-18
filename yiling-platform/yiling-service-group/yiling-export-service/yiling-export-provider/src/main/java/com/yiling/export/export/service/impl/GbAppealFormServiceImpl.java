package com.yiling.export.export.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.gb.api.GbAppealFlowRelatedApi;
import com.yiling.dataflow.gb.api.GbAppealFormApi;
import com.yiling.dataflow.gb.api.GbOrderApi;
import com.yiling.dataflow.gb.dto.GbAppealFlowRelatedDTO;
import com.yiling.dataflow.gb.dto.GbAppealFormDTO;
import com.yiling.dataflow.gb.dto.GbOrderDTO;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormListPageRequest;
import com.yiling.dataflow.gb.enums.GbDataExecStatusEnum;
import com.yiling.export.export.bo.ExportGbAppealFormBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.gb.enums.GbFormReviewStatusEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 团购处理导出
 *
 * @author: houjie.sun
 * @date: 2023/6/14
 */
@Slf4j
@Service("gbAppealFormServiceImpl")
public class GbAppealFormServiceImpl implements BaseExportQueryDataService<QueryGbAppealFormListPageRequest> {

    @DubboReference
    GbAppealFormApi gbAppealFormApi;
    @DubboReference
    GbAppealFlowRelatedApi gbAppealFlowRelatedApi;
    @DubboReference
    GbOrderApi gbOrderApi;
    @DubboReference
    UserApi userApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("matchMonth", "所属年月");
        FIELD.put("gbNo", "团购编号");
        FIELD.put("gbProcess", "所属流程");// 1-团购提报 2-团购取消。字典：form_type
        FIELD.put("gbMonth", "团购月份");
        FIELD.put("ename", "出库商业");
        FIELD.put("enterpriseName", "出库终端");
        FIELD.put("goodsName", "标准产品名称");
        FIELD.put("gbQuantity", "团购数量");
        FIELD.put("auditStatus", "审批状态");// 10-待提交 20-审批中 200-已通过 201-已驳回，字典：gb_form_status
        FIELD.put("checkStatus", "复核状态");// 1-未复核 2-已复核，字典：gb_form_review_status
        FIELD.put("gbRemark", "复核意见");
        FIELD.put("flowMatchNumber", "源流向匹配");
        FIELD.put("dataMatchNumber", "已扣减数量");
        FIELD.put("dataExecStatus", "处理状态");// 1-未开始、2-自动处理中、3-已处理、4-处理失败、5-手动处理中。数据字典：gb_appeal_form_data_exec_status
        FIELD.put("lastUpdateUserName", "操作人");// 根据lastUpdateUser获取姓名
        FIELD.put("lastUpdateTime", "最后操作时间");
    }


    @Override
    public QueryExportDataDTO queryData(QueryGbAppealFormListPageRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<Map<String, Object>> data = new ArrayList<>();

        // 查询团购处理
        queryGbAppealFormListPage(request, data);

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("团购处理导出");
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
    public QueryGbAppealFormListPageRequest getParam(Map<String, Object> map) {
        return PojoUtils.map(map, QueryGbAppealFormListPageRequest.class);
    }

    private void queryGbAppealFormListPage(QueryGbAppealFormListPageRequest request, List<Map<String, Object>> data) {
        // 需要循环调用
        Page<GbAppealFormDTO> page;
        log.info("团购处理导出 gbAppealFormServiceImpl 查询条件为:{}", JSONUtil.toJsonStr(request));
        int current = 1;
        int size = 500;
        do {
            request.setCurrent(current);
            request.setSize(size);
            page = gbAppealFormApi.listPage(request);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }

            List<GbAppealFormDTO> records = page.getRecords();
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

            ExportGbAppealFormBO bo;
            for (GbAppealFormDTO record : records) {
                bo = PojoUtils.map(record, ExportGbAppealFormBO.class);
                // 团购信息
                GbOrderDTO gbOrderDTO = gbOrderMap.get(record.getGbOrderId());
                if (ObjectUtil.isNotNull(gbOrderDTO)) {
                    // 复核意见
                    bo.setGbRemark(gbOrderDTO.getGbRemark());
                }
                // 状态名称转换
                convertStatus(bo, record, gbOrderDTO);
                // 已扣减数量
                BigDecimal flowMatchQuantity = flowMatchQuantityMap.get(record.getId());
                if (ObjectUtil.isNull(flowMatchQuantity)) {
                    flowMatchQuantity = BigDecimal.ZERO;
                }
                bo.setDataMatchNumber(flowMatchQuantity.longValue());
                // 操作人
                String name = userNameMap.get(record.getLastUpdateUser());
                bo.setLastUpdateUserName(name);

                data.add(BeanUtil.beanToMap(bo));
            }

            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));
    }

    private void convertStatus(ExportGbAppealFormBO bo, GbAppealFormDTO record, GbOrderDTO gbOrderDTO) {
        if (ObjectUtil.isNotNull(gbOrderDTO)) {
            // 所属流程 FormTypeEnum
            FormTypeEnum formTypEnume = FormTypeEnum.getByCode(gbOrderDTO.getGbProcess());
            if (ObjectUtil.isNotNull(formTypEnume)) {
                bo.setGbProcess(formTypEnume.getName());
            }
            // 审批状态 FormStatusEnum
            FormStatusEnum formStatusEnum = FormStatusEnum.getByCode(gbOrderDTO.getAuditStatus());
            if (ObjectUtil.isNotNull(formStatusEnum)) {
                bo.setAuditStatus(formStatusEnum.getName());
            }
            // 复核状态 GbFormReviewStatusEnum
            GbFormReviewStatusEnum gbFormReviewStatusEnum = GbFormReviewStatusEnum.getByCode(gbOrderDTO.getCheckStatus());
            if (ObjectUtil.isNotNull(gbFormReviewStatusEnum)) {
                bo.setCheckStatus(gbFormReviewStatusEnum.getName());
            }
        }
        // 处理状态 GbDataExecStatusEnum
        GbDataExecStatusEnum gbDataExecStatusEnum = GbDataExecStatusEnum.getByCode(record.getDataExecStatus());
        if (ObjectUtil.isNotNull(gbDataExecStatusEnum)) {
            bo.setDataExecStatus(gbDataExecStatusEnum.getName());
        }
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


}
