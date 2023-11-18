package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flow.api.FlowPurchaseChannelApi;
import com.yiling.dataflow.flow.dto.FlowPurchaseChannelDTO;
import com.yiling.dataflow.flow.dto.request.QueryFlowPurchaseChannelRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowPurchaseChannelExportServiceImpl
 * @描述
 * @创建时间 2023/3/2
 * @修改人 shichen
 * @修改时间 2023/3/2
 **/
@Slf4j
@Service("flowPurchaseChannelExportService")
public class FlowPurchaseChannelExportServiceImpl implements BaseExportQueryDataService<QueryFlowPurchaseChannelRequest> {
    @DubboReference
    private FlowPurchaseChannelApi flowPurchaseChannelApi;

    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("crmOrgId", "*机构编码");
        FIELD.put("orgName", "*机构名称");
        FIELD.put("province", "省");
        FIELD.put("city", "市");
        FIELD.put("region", "区");
        FIELD.put("crmPurchaseOrgId", "*采购渠道机构编码");
        FIELD.put("purchaseOrgName", "*采购渠道机构名称");
    }



    @Override
    public QueryExportDataDTO queryData(QueryFlowPurchaseChannelRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<Map<String, Object>> data = new ArrayList<>();
        Page<FlowPurchaseChannelDTO> pageList = flowPurchaseChannelApi.pageList(request);
        if(CollectionUtil.isNotEmpty(pageList.getRecords())){
            pageList.getRecords().forEach(dto->{
                Map<String, Object> map = BeanUtil.beanToMap(dto);
                data.add(map);
            });
            ExportDataDTO exportDataDTO = new ExportDataDTO();
            exportDataDTO.setSheetName("采购渠道");
            // 页签字段
            exportDataDTO.setFieldMap(FIELD);
            // 页签数据
            exportDataDTO.setData(data);
            List<ExportDataDTO> sheets = new ArrayList<>();
            sheets.add(exportDataDTO);
            result.setSheets(sheets);
        }
        return result;
    }

    @Override
    public QueryFlowPurchaseChannelRequest getParam(Map<String, Object> map) {
        QueryFlowPurchaseChannelRequest request = PojoUtils.map(map, QueryFlowPurchaseChannelRequest.class);
        request.setCurrent(1);
        request.setSize(10000);
        String empId=map.getOrDefault("currentUserCode","").toString();
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(empId);
        log.info("采购渠道导出权限:userDatascopeBO={}", JSONUtil.toJsonStr(userDatascopeBO));
        if(null == userDatascopeBO || OrgDatascopeEnum.NONE.getCode().equals(userDatascopeBO.getOrgDatascope())){
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        request.setUserDatascopeBO(userDatascopeBO);
        return request;
    }
}
