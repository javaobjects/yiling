package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.ChannelCustomerApi;
import com.yiling.user.enterprise.api.CustomerContactApi;
import com.yiling.user.enterprise.api.CustomerGroupApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.enterprise.dto.EnterpriseChannelCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerContactDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerGroupDTO;
import com.yiling.user.enterprise.dto.request.QueryChannelCustomerPageListRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerContactPageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.PaymentMethodDTO;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * @author: yuecheng.chen
 * @date: 2021/6/9
 */
@Service("channelCustomerExportService")
public class ChannelCustomerExportServiceImpl implements BaseExportQueryDataService<QueryChannelCustomerPageListRequest> {

    @DubboReference
    ChannelCustomerApi channelCustomerApi;
    @DubboReference
    CustomerContactApi customerContactApi;
    @DubboReference
    CustomerGroupApi   customerGroupApi;
    @DubboReference
    UserApi            userApi;
    @DubboReference
    PaymentMethodApi   paymentMethodApi;
    @DubboReference
    EnterpriseApi      enterpriseApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();
    static {
        FIELD.put("customerEid", "企业ID");
        FIELD.put("customerName", "企业名称");
        FIELD.put("licenseNumber","社会信用统一代码");
        FIELD.put("enterpriseStatus", "企业状态");
        FIELD.put("channelName", "渠道类型");
        FIELD.put("purchaseRelationNum", "采购关系数量");
        FIELD.put("customerContactNum", "商务联系人个数");
        FIELD.put("customerContactInfo", "商务联系人（联系方式）");
        FIELD.put("paymentMethod", "支付方式");
        FIELD.put("customerGroupName", "客户分组");
    }

    @Override
    public QueryExportDataDTO queryData(QueryChannelCustomerPageListRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        int current=1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            Page<EnterpriseChannelCustomerDTO> page = channelCustomerApi.pageList(request);
            if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }
            for (EnterpriseChannelCustomerDTO dto : page.getRecords()) {
                dto.setEnterpriseStatus(EnterpriseStatusEnum.getByCode(dto.getStatus()).getName());
                // 处理商务联系人
                QueryCustomerContactPageListRequest queryCustomerContact = new QueryCustomerContactPageListRequest();
                queryCustomerContact.setEid(dto.getEid());
                queryCustomerContact.setCustomerEid(dto.getCustomerEid());
                Page<EnterpriseCustomerContactDTO> customerContactDTOPage = customerContactApi.pageList(queryCustomerContact);
                if (CollUtil.isNotEmpty(customerContactDTOPage.getRecords())) {
                    StringBuilder customerContactInfo = new StringBuilder();
                    List<Long> userIds = customerContactDTOPage.getRecords().stream().map(EnterpriseCustomerContactDTO::getContactUserId).distinct().collect(Collectors.toList());
                    List<UserDTO> userDTOList = userApi.listByIds(userIds);
                    userDTOList.forEach(e -> {
                        String userInfo = e.getName().concat("(" + e.getMobile() + ")");
                        customerContactInfo.append(userInfo);
                    });
                    dto.setCustomerContactInfo(customerContactInfo.toString());
                }
                // 处理支付方式
                Map<Long, List<PaymentMethodDTO>> customerPaymentMethods = paymentMethodApi.listByEidAndCustomerEids(dto.getEid(),  ListUtil.list(false, dto.getCustomerEid()), PlatformEnum.POP);
                List<PaymentMethodDTO> paymentMethodDTOS = customerPaymentMethods.get(dto.getCustomerEid());
                StringBuilder paymentMethod = new StringBuilder();
                if (CollUtil.isNotEmpty(paymentMethodDTOS)) {
                    paymentMethodDTOS.forEach(e -> paymentMethod.append(e.getName()));
                }
                dto.setPaymentMethod(paymentMethod.toString());
                // 处理客户分组
                if (dto.getCustomerGroupId() != null && dto.getCustomerGroupId() != 0) {
                    EnterpriseCustomerGroupDTO customerGroupDTO = customerGroupApi.getById(dto.getCustomerGroupId());
                    if (customerGroupDTO != null) {
                        dto.setCustomerGroupName(customerGroupDTO.getName());
                    }
                }
                data.add(BeanUtil.beanToMap(dto));
            }
            current=current+1;
        } while (CollectionUtils.isNotEmpty(data));
        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("渠道商列表导出");
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
    public QueryChannelCustomerPageListRequest getParam(Map<String, Object> map) {
        //参数eid的转换为eidList
        QueryChannelCustomerPageListRequest request = PojoUtils.map(map, QueryChannelCustomerPageListRequest.class);
        Long eid = Long.parseLong(map.getOrDefault("eid", 0L).toString());
        List<Long> eids = new ArrayList<>();
        eids.add(eid);
        if (eid != 0) {
            List<Long> eidList = enterpriseApi.listSubEids(eid);
            if (CollUtil.isNotEmpty(eidList)) {
                eids.addAll(eidList);
            }
        }
        request.setEids(eids);
        return request;
    }
}
