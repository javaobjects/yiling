package com.yiling.goods.medicine.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.dao.CustomerPriceLimitMapper;
import com.yiling.goods.medicine.dto.CustomerPriceLimitDTO;
import com.yiling.goods.medicine.dto.request.AddOrDeleteCustomerGoodsLimitRequest;
import com.yiling.goods.medicine.dto.request.QueryLimitFlagRequest;
import com.yiling.goods.medicine.dto.request.UpdateCustomerLimitRequest;
import com.yiling.goods.medicine.entity.CustomerPriceLimitDO;
import com.yiling.goods.medicine.service.CustomerGoodsPriceLimitService;
import com.yiling.goods.medicine.service.CustomerPriceLimitService;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 客户限价表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-25
 */
@Service
public class CustomerPriceLimitServiceImpl extends BaseServiceImpl<CustomerPriceLimitMapper, CustomerPriceLimitDO> implements CustomerPriceLimitService {

    @Autowired
    private CustomerGoodsPriceLimitService customerGoodsPriceLimitService;

    @Override
    public List<CustomerPriceLimitDTO> getCustomerLimitFlagByEidAndCustomerEid(QueryLimitFlagRequest request) {
        QueryWrapper<CustomerPriceLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerPriceLimitDO::getEid, request.getEid());
        if (CollUtil.isNotEmpty(request.getCustomerEids())) {
            queryWrapper.lambda().in(CustomerPriceLimitDO::getCustomerEid, request.getCustomerEids());
        }
        if (request.getLimitFlag() != null) {
            queryWrapper.lambda().eq(CustomerPriceLimitDO::getLimitFlag, request.getLimitFlag());
        }
        return PojoUtils.map(this.list(queryWrapper), CustomerPriceLimitDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCustomerLimitByEidAndCustomerEid(UpdateCustomerLimitRequest request) {
        QueryWrapper<CustomerPriceLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerPriceLimitDO::getEid, request.getEid())
                .in(CustomerPriceLimitDO::getCustomerEid, request.getCustomerEids());

        for (Long customerEid : request.getCustomerEids()) {
            CustomerPriceLimitDO customerPriceLimitDO = new CustomerPriceLimitDO();
            customerPriceLimitDO.setLimitFlag(request.getLimitFlag());
            customerPriceLimitDO.setRecommendationFlag(request.getRecommendationFlag());
            customerPriceLimitDO.setEid(request.getEid());
            customerPriceLimitDO.setCustomerEid(customerEid);
            customerPriceLimitDO.setOpUserId(request.getOpUserId());
            this.saveOrUpdate(customerPriceLimitDO, queryWrapper);
        }
        return true;
    }

    @Override
    public Map<Long, Integer> getRecommendationFlagByCustomerEids(List<Long> customerEids, Long eid) {
        QueryWrapper<CustomerPriceLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerPriceLimitDO::getEid, eid)
                .in(CustomerPriceLimitDO::getCustomerEid, customerEids);

        Map<Long, Integer> returnMap = new HashMap<>();
        List<CustomerPriceLimitDO> customerPriceLimitDOList = this.list(queryWrapper);
        Map<Long, Integer> customerPriceLimitDOMap = customerPriceLimitDOList.stream().collect(Collectors.toMap(CustomerPriceLimitDO::getCustomerEid, CustomerPriceLimitDO::getRecommendationFlag));
        for (Long customerEid : customerEids) {
            returnMap.put(customerEid, customerPriceLimitDOMap.getOrDefault(customerEid, 0));
        }
        return returnMap;
    }

    @Override
    public Long addCustomerGoodsLimitByCustomerEid(AddOrDeleteCustomerGoodsLimitRequest request) {
        QueryWrapper<CustomerPriceLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerPriceLimitDO::getEid, request.getEid())
                .eq(CustomerPriceLimitDO::getCustomerEid, request.getCustomerEid());

        CustomerPriceLimitDTO customerPriceLimitDTO = getCustomerPriceByCustomerEidAndEid(request.getEid(), request.getCustomerEid());
        CustomerPriceLimitDO customerPriceLimit = PojoUtils.map(request, CustomerPriceLimitDO.class);
        if (customerPriceLimitDTO == null) {
            this.save(customerPriceLimit);
        } else {
            customerPriceLimit.setId(customerPriceLimitDTO.getId());
            this.updateById(customerPriceLimit);
        }
        return customerPriceLimit.getId();
    }

    @Override
    public CustomerPriceLimitDTO getCustomerPriceLimitByCustomerEidAndEid(Long eid, Long customerEid) {
        QueryWrapper<CustomerPriceLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerPriceLimitDO::getEid, eid)
                .eq(CustomerPriceLimitDO::getCustomerEid, customerEid)
                .eq(CustomerPriceLimitDO::getLimitFlag, 1);

        return PojoUtils.map(this.getOne(queryWrapper), CustomerPriceLimitDTO.class);
    }

    @Override
    public CustomerPriceLimitDTO getCustomerPriceByCustomerEidAndEid(Long eid, Long customerEid) {
        QueryWrapper<CustomerPriceLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerPriceLimitDO::getEid, eid)
                .eq(CustomerPriceLimitDO::getCustomerEid, customerEid);

        return PojoUtils.map(this.getOne(queryWrapper), CustomerPriceLimitDTO.class);
    }
}
