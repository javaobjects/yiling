package com.yiling.user.enterprise.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.api.EnterpriseCustomerLineApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerLineDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerLineRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerLineListRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseCustomerLineRequest;
import com.yiling.user.enterprise.service.EnterpriseCustomerLineService;

/**
 * 企业客户使用产品线 API 实现
 *
 * @author: lun.yu
 * @date: 2021/11/29
 */
@DubboService
public class EnterpriseCustomerLineApiImpl implements EnterpriseCustomerLineApi {

    @Autowired
    EnterpriseCustomerLineService enterpriseCustomerLineService;

    @Override
    public boolean add(List<AddCustomerLineRequest> request) {
        return enterpriseCustomerLineService.add(request);
    }

    @Override
    public boolean delete(List<Long> idList ,Long opUserId) {
        return enterpriseCustomerLineService.delete(idList,opUserId);
    }

    @Override
    public List<EnterpriseCustomerLineDTO> queryList(QueryCustomerLineListRequest request) {
        return PojoUtils.map(enterpriseCustomerLineService.queryList(request),EnterpriseCustomerLineDTO.class);
    }

    @Override
    public boolean updateLine(UpdateEnterpriseCustomerLineRequest request) {
        return enterpriseCustomerLineService.updateLine(request);
    }

    @Override
    public boolean getCustomerLineFlag(Long eid, Long customerEid, EnterpriseCustomerLineEnum lineEnum) {
        return enterpriseCustomerLineService.getCustomerLineFlag(eid,customerEid,lineEnum);
    }

    @Override
    public Map<Long, Boolean> getCustomerLineListFlag(List<Long> eidList, Long customerEid, EnterpriseCustomerLineEnum lineEnum) {
        return enterpriseCustomerLineService.getCustomerLineListFlag(eidList,customerEid,lineEnum);
    }

}
