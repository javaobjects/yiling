package com.yiling.dataflow.relation.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.relation.dao.FlowCustomerMappingMapper;
import com.yiling.dataflow.relation.dto.FlowCustomerMappingDTO;
import com.yiling.dataflow.relation.dto.request.QueryFlowCustomerMappingListPageRequest;
import com.yiling.dataflow.relation.entity.FlowCustomerMappingDO;
import com.yiling.dataflow.relation.service.FlowCustomerMappingService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-07-18
 */
@Service
public class FlowCustomerMappingServiceImpl extends BaseServiceImpl<FlowCustomerMappingMapper, FlowCustomerMappingDO> implements FlowCustomerMappingService {

    @Autowired
    private StringRedisTemplate  stringRedisTemplate;
    @Autowired
    private CrmEnterpriseService crmEnterpriseService;

    /**
     * 当月无销售的企业，Redis缓存key
     */
    public static final String ERP_FLOW_SALE_SIGN_ENTERPRISE_CRM_CODE = "erp_flow_sale_sign_enterprise_crm_code";

    @Override
    public Page<FlowCustomerMappingDTO> page(QueryFlowCustomerMappingListPageRequest request) {
        return PojoUtils.map(this.baseMapper.page(request.getPage(), request), FlowCustomerMappingDTO.class);
    }

//    @Override
//    public FlowCustomerMappingDTO getByFlowCustomerNameAndBusinessCode(String customerName, String businessCode) {
//        if (StrUtil.isEmpty(customerName) || StrUtil.isEmpty(businessCode)) {
//            return null;
//        }
//        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseService.getCrmEnterpriseByName(customerName);
//        FlowCustomerMappingDTO flowCustomerMappingDTO = null;
//        if (crmEnterpriseDTO != null) {
//            flowCustomerMappingDTO = new FlowCustomerMappingDTO();
//            flowCustomerMappingDTO.setInnerCode(crmEnterpriseDTO.getCode());
//            flowCustomerMappingDTO.setInnerName(crmEnterpriseDTO.getName());
//            flowCustomerMappingDTO.setCustomerTypeName(crmEnterpriseDTO.getName());
//            flowCustomerMappingDTO.setBelongDepartment("");
//            flowCustomerMappingDTO.setLockType(1);
//            return flowCustomerMappingDTO;
//        }
//
//        Object jsonObject = stringRedisTemplate.opsForHash().get(ERP_FLOW_SALE_SIGN_ENTERPRISE_CRM_CODE, customerName + "-" + businessCode);
//        if (jsonObject != null) {
//            flowCustomerMappingDTO = JSON.parseObject(String.valueOf(jsonObject), FlowCustomerMappingDTO.class);
//            return flowCustomerMappingDTO;
//        } else {
//            QueryWrapper<FlowCustomerMappingDO> queryWrapper = new QueryWrapper<>();
//            queryWrapper.lambda().eq(FlowCustomerMappingDO::getBusinessCode, businessCode);
//            queryWrapper.lambda().eq(FlowCustomerMappingDO::getFlowCustomerName, customerName);
//            queryWrapper.last("limit 1");
//
//            flowCustomerMappingDTO = PojoUtils.map(this.getOne(queryWrapper), FlowCustomerMappingDTO.class);
//            if (flowCustomerMappingDTO != null) {
//                stringRedisTemplate.opsForHash().put(ERP_FLOW_SALE_SIGN_ENTERPRISE_CRM_CODE, customerName + "-" + businessCode, JSON.toJSONString(flowCustomerMappingDTO));
//                return flowCustomerMappingDTO;
//            }
//        }
//        return null;
//    }



}
