package com.yiling.dataflow.relation.api.impl;

import java.util.Date;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.bo.FlowGoodsRelationBO;
import com.yiling.dataflow.order.dto.FlowGoodsRelationDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationYlGoodsIdRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowGoodsRelationRequest;
import com.yiling.dataflow.relation.api.FlowGoodsRelationApi;
import com.yiling.dataflow.relation.api.FlowSupplierMappingApi;
import com.yiling.dataflow.relation.dto.FlowSupplierMappingDTO;
import com.yiling.dataflow.relation.dto.request.SaveFlowSupplierMappingRequest;
import com.yiling.dataflow.relation.service.FlowGoodsRelationService;
import com.yiling.dataflow.relation.service.FlowSupplierMappingService;
import com.yiling.framework.common.util.PojoUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: houjie.sun
 * @date: 2022/5/23
 */
@DubboService
public class FlowSupplierMappingApiImpl implements FlowSupplierMappingApi {

    @Autowired
    private FlowSupplierMappingService flowSupplierMappingService;

    @Override
    public FlowSupplierMappingDTO getByEnterpriseName(String enterpriseName) {
        return PojoUtils.map(flowSupplierMappingService.getByEnterpriseName(enterpriseName),FlowSupplierMappingDTO.class);
    }

    @Override
    public boolean saveFlowSupplierMapping(SaveFlowSupplierMappingRequest request) {
        return flowSupplierMappingService.saveFlowSupplierMapping(request);
    }
}
