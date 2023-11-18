package com.yiling.dataflow.order.service.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.config.FlowSettlementEnterpriseTagConfig;
import com.yiling.dataflow.order.service.FlowSettlementEnterpriseTagService;
import com.yiling.user.enterprise.api.EnterpriseTagApi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;


/**
 * @author: houjie.sun
 * @date: 2022/6/28
 */
@Service
public class FlowSettlementEnterpriseTagServiceImpl implements FlowSettlementEnterpriseTagService {

    @Autowired
    private FlowSettlementEnterpriseTagConfig flowSettlementEnterpriseTagConfig;

    @DubboReference
    EnterpriseTagApi enterpriseTagApi;

    @Override
    public List<Long> getFlowSettlementEnterpriseTagNameList() {
        List<String> enterpriseTagNameList = flowSettlementEnterpriseTagConfig.getFlowSettlementEnterpriseTagList();
        if(CollUtil.isEmpty(enterpriseTagNameList)){
            return ListUtil.empty();
        }
        List<Long> eidList = enterpriseTagApi.getEidListByTagsNameList(enterpriseTagNameList);
        if(CollUtil.isEmpty(eidList)){
            return ListUtil.empty();
        }
        return eidList;
    }

}
