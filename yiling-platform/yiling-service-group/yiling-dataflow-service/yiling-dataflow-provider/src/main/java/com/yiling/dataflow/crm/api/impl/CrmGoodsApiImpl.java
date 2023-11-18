package com.yiling.dataflow.crm.api.impl;

import java.util.List;

import com.yiling.dataflow.crm.api.CrmGoodsApi;
import com.yiling.dataflow.crm.api.CrmGoodsGroupApi;
import com.yiling.dataflow.crm.dto.CrmGoodsDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.service.CrmGoodsGroupService;
import com.yiling.dataflow.crm.service.CrmGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/8/8
 */
@DubboService
@Slf4j
public class CrmGoodsApiImpl implements CrmGoodsApi {

    @Autowired
    private CrmGoodsService crmGoodsService;

    @Override
    public List<CrmGoodsDTO> getCrmGoodsBySpecificationId(Long specificationId) {
        return crmGoodsService.getCrmGoodsBySpecificationId(specificationId);
    }
}
