package com.yiling.job.executor.service.jobhandler;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyPageListRequest;
import com.yiling.dataflow.agency.enums.AgencySupplyChainRoleEnum;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.job.executor.log.JobLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author: shuang.zhang
 * @date: 2023/3/3
 */
@Slf4j
@Component
public class CrmEnterpriseJobHandler {

    @DubboReference
    private CrmEnterpriseApi crmEnterpriseApi;

    @JobLog
    @XxlJob("crmEnterpriseFlowGenerateHandler")
    public ReturnT<String> crmEnterpriseFlowGenerateHandler(String param) throws Exception {
        QueryCrmAgencyPageListRequest request = new QueryCrmAgencyPageListRequest();
        request.setSupplyChainRole(AgencySupplyChainRoleEnum.SUPPLIER.getCode());
        List<CrmEnterpriseDTO> crmEnterpriseDTOList=new ArrayList<>();
        Page<CrmEnterpriseDTO> page =null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(2000);
            page = crmEnterpriseApi.getCrmEnterpriseInfoPage(request);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            crmEnterpriseDTOList.addAll(page.getRecords());
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));

//        for(){
//
//        }
        return ReturnT.SUCCESS;
    }
}
