package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.wash.api.FlowInTransitOrderApi;
import com.yiling.dataflow.wash.api.FlowTerminalOrderApi;
import com.yiling.dataflow.wash.dto.FlowGoodsBatchTransitDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchTransitPageRequest;
import com.yiling.dataflow.wash.enums.FlowGoodsBatchTransitTypeEnum;
import com.yiling.export.export.bo.FlowTerminalOrderPageDetailBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 流向清洗-终端库存导出
 *
 * @author: houjie.sun
 * @date: 2023/3/9
 */
@Service("flowGoodsBatchTerminalExportService")
public class FlowGoodsBatchTerminalExportServiceImpl implements BaseExportQueryDataService<QueryFlowGoodsBatchTransitPageRequest> {

    @DubboReference
    private FlowTerminalOrderApi flowTerminalOrderApi;
    @DubboReference
    private UserApi userApi;
    @DubboReference
    private CrmGoodsInfoApi crmGoodsInfoApi;
    @DubboReference
    private FlowInTransitOrderApi flowInTransitOrderApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("gbDetailMonth", "所属年月");
        FIELD.put("name", "终端名称");
        FIELD.put("crmGoodsCode", "标准产品编码");
        FIELD.put("crmGoodsName", "标准产品名称");
        FIELD.put("crmGoodsSpecifications", "标准产品规格");
        FIELD.put("gbBatchNo", "批号");
        FIELD.put("gbNumber", "库存数量");
        FIELD.put("opTime", "最后操作时间");
        FIELD.put("opUser", "操作人");
    }

    @Override
    public QueryExportDataDTO queryData(QueryFlowGoodsBatchTransitPageRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<Map<String, Object>> data = new ArrayList<>();

        // 查询数据
        buildFlowGoodsBatchTerminalExportData(request, data);

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("流向接口监控导出");
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
    public QueryFlowGoodsBatchTransitPageRequest getParam(Map<String, Object> map) {
        QueryFlowGoodsBatchTransitPageRequest request = PojoUtils.map(map, QueryFlowGoodsBatchTransitPageRequest.class);
        request.setGoodsBatchType(FlowGoodsBatchTransitTypeEnum.TERMINAL.getCode());
        // 根据规格查询标准商品code
        String crmGoodsSpecifications = map.getOrDefault("crmGoodsSpecifications", "").toString();
        buildCrmGoodsCodeList(crmGoodsSpecifications, request);
        String currentUserCode = map.getOrDefault("currentUserCode", "").toString();
        // 数据权限
        Map<String, List<String>> datascopeMap = flowInTransitOrderApi.buildUserDatascope(currentUserCode, "在途订单-导出");
        String datascopeType = datascopeMap.get("datascopeType").get(0);
        if (ObjectUtil.equal("1", datascopeType)) {
            List<Long> crmIdList = Convert.toList(Long.class, datascopeMap.get("crmIds"));
            List<String> crmProvinceCodeList = datascopeMap.get("provinceCodes");
            request.setCrmEnterpriseIdList(crmIdList);
            request.setCrmProvinceCodeList(crmProvinceCodeList);
        }
        return request;
    }

    private void buildCrmGoodsCodeList(String crmGoodsSpecifications, QueryFlowGoodsBatchTransitPageRequest request) {
        if (StrUtil.isBlank(crmGoodsSpecifications)) {
            return;
        }
        // 根据规格查询标准商品id
        List<CrmGoodsInfoDTO> crmGoodsInfoList = crmGoodsInfoApi.findByNameAndSpec(null, crmGoodsSpecifications, 0);
        if (CollUtil.isNotEmpty(crmGoodsInfoList)) {
            List<Long> crmGoodsCodeList = crmGoodsInfoList.stream().map(CrmGoodsInfoDTO::getGoodsCode).distinct().collect(Collectors.toList());
            request.setCrmGoodsCodeList(crmGoodsCodeList);
        }
    }

    private void buildFlowGoodsBatchTerminalExportData(QueryFlowGoodsBatchTransitPageRequest request, List<Map<String, Object>> data) {
        // 需要循环调用
        Page<FlowGoodsBatchTransitDTO> page;
        int current = 1;
        int size = 500;
        do {
            request.setCurrent(current);
            request.setSize(size);
            page = flowTerminalOrderApi.listPage(request);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }

            // 操作人信息
            Map<Long, String> userNameMap = getUserNameMap(page.getRecords());

            for (FlowGoodsBatchTransitDTO dto : page.getRecords()) {
                FlowTerminalOrderPageDetailBO bo = convertBo(dto, userNameMap);
                data.add(BeanUtil.beanToMap(bo));
            }

            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
    }

    private Map<Long, String> getUserNameMap(List<FlowGoodsBatchTransitDTO> list) {
        Map<Long, String> userNameMap = new HashMap<>();
        Set<Long> opUserIdSet = new HashSet<>();
        List<Long> createUserIdList = list.stream().filter(o -> ObjectUtil.isNotNull(o.getCreateUser()) && o.getCreateUser() > 0).map(FlowGoodsBatchTransitDTO::getCreateUser).distinct().collect(Collectors.toList());
        List<Long> updateUserIdList = list.stream().filter(o -> ObjectUtil.isNotNull(o.getUpdateUser()) && o.getUpdateUser() > 0).map(FlowGoodsBatchTransitDTO::getUpdateUser).distinct().collect(Collectors.toList());
        opUserIdSet.addAll(createUserIdList);
        if (CollUtil.isNotEmpty(updateUserIdList)) {
            opUserIdSet.addAll(updateUserIdList);
        }
        List<UserDTO> userList = userApi.listByIds(ListUtil.toList(opUserIdSet));
        if (CollUtil.isNotEmpty(userList)) {
            userNameMap = userList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o.getName(), (k1, k2) -> k1));
        }
        return userNameMap;
    }

    private FlowTerminalOrderPageDetailBO convertBo(FlowGoodsBatchTransitDTO dto, Map<Long, String> userNameMap) {
        FlowTerminalOrderPageDetailBO bo = new FlowTerminalOrderPageDetailBO();
        bo.setId(dto.getId());
        bo.setCrmEnterpriseId(dto.getCrmEnterpriseId());
        bo.setName(dto.getName());
        bo.setCrmGoodsCode(dto.getCrmGoodsCode());
        bo.setCrmGoodsName(dto.getCrmGoodsName());
        bo.setCrmGoodsSpecifications(dto.getCrmGoodsSpecifications());
        bo.setGbBatchNo(dto.getGbBatchNo());
        bo.setGbNumber(dto.getGbNumber());
        bo.setGbUnit(dto.getGbUnit());
        // 所属年月
//        String month = dto.getGbDetailMonth().replaceFirst("-", "年").concat("月");
        bo.setGbDetailMonth(dto.getGbDetailMonth());
        // 操作时间
        Date createTime = dto.getCreateTime();
        Date updateTime = dto.getUpdateTime();
        Date opTime;
        if (ObjectUtil.isNull(updateTime) || ObjectUtil.equal("1970-01-01 00:00:00", DateUtil.format(updateTime, "yyyy-MM-dd HH:mm:ss"))) {
            opTime = createTime;
        } else {
            opTime = updateTime;
        }
        bo.setOpTime(opTime);
        // 操作人
        Long createUser = dto.getCreateUser();
        Long updateUser = dto.getUpdateUser();
        Long opUser = 0L;
        if (ObjectUtil.isNull(updateUser) || 0 == updateUser.intValue()) {
            opUser = createUser;
        } else {
            opUser = updateUser;
        }
        // 用户姓名
        String name = userNameMap.get(opUser);
        bo.setOpUser(name);
        return bo;
    }

}
