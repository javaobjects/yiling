package com.yiling.goods.medicine.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.bo.CustomerGoodsPriceLimitBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dao.CustomerGoodsPriceLimitMapper;
import com.yiling.goods.medicine.dto.CustomerPriceLimitDTO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.request.AddOrDeleteCustomerGoodsLimitRequest;
import com.yiling.goods.medicine.dto.request.BatchAddCustomerGoodsLimitRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsLimitPageListRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.QueryLimitFlagRequest;
import com.yiling.goods.medicine.entity.CustomerGoodsPriceLimitDO;
import com.yiling.goods.medicine.service.B2bGoodsService;
import com.yiling.goods.medicine.service.CustomerGoodsPriceLimitService;
import com.yiling.goods.medicine.service.CustomerPriceLimitService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 客户商品限价表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-25
 */
@Service
public class CustomerGoodsPriceLimitServiceImpl extends BaseServiceImpl<CustomerGoodsPriceLimitMapper, CustomerGoodsPriceLimitDO> implements CustomerGoodsPriceLimitService {

    @Autowired
    B2bGoodsService b2bGoodsService;

    @Autowired
    CustomerPriceLimitService customerPriceLimitService;

    @Override
    public Page<GoodsListItemBO> pageLimitList(QueryGoodsLimitPageListRequest request) {
        return this.baseMapper.pageLimitList(new Page<>(request.getCurrent(), request.getSize()), request);
    }

    @Override
    public Boolean addCustomerGoodsLimitByCustomerEid(AddOrDeleteCustomerGoodsLimitRequest request) {
        Long cplId = customerPriceLimitService.addCustomerGoodsLimitByCustomerEid(request);

        QueryWrapper<CustomerGoodsPriceLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerGoodsPriceLimitDO::getCplId, cplId);

        List<CustomerGoodsPriceLimitDO> list = this.list(queryWrapper);
        List<Long> goodsIds = list.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());

        List<CustomerGoodsPriceLimitDO> customerGoodsPriceLimitDOList = new ArrayList<>();
        request.getGoodsIds().forEach(e -> {
            if (!goodsIds.contains(e)) {
                CustomerGoodsPriceLimitDO customerGoodsPriceLimitDO = new CustomerGoodsPriceLimitDO();
                customerGoodsPriceLimitDO.setCplId(cplId);
                customerGoodsPriceLimitDO.setGoodsId(e);
                customerGoodsPriceLimitDO.setOpUserId(request.getOpUserId());
                customerGoodsPriceLimitDOList.add(customerGoodsPriceLimitDO);
            }
        });
        return this.saveBatch(customerGoodsPriceLimitDOList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchAddCustomerGoodsLimitByCustomerEid(BatchAddCustomerGoodsLimitRequest limitRequest) {
        QueryGoodsPageListRequest request = PojoUtils.map(limitRequest, QueryGoodsPageListRequest.class);
        List<Long> goodsIds = new ArrayList<>();
        Page<GoodsListItemBO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            page = b2bGoodsService.queryB2bGoodsPageList(request);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            goodsIds.addAll(page.getRecords().stream().map(e -> e.getId()).collect(Collectors.toList()));
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));

        AddOrDeleteCustomerGoodsLimitRequest addRequest = new AddOrDeleteCustomerGoodsLimitRequest();
        addRequest.setCustomerEid(limitRequest.getCustomerEid());
        addRequest.setEid(limitRequest.getEid());
        addRequest.setGoodsIds(goodsIds);
        addRequest.setOpUserId(limitRequest.getOpUserId());
        return this.addCustomerGoodsLimitByCustomerEid(addRequest);
    }

    @Override
    public Boolean deleteCustomerGoodsLimitByCustomerEid(AddOrDeleteCustomerGoodsLimitRequest request) {
        CustomerPriceLimitDTO customerPriceLimitDTO = customerPriceLimitService.getCustomerPriceByCustomerEidAndEid(request.getEid(),request.getCustomerEid());
        Long cplId=customerPriceLimitDTO.getId();

        CustomerGoodsPriceLimitDO customerGoodsPriceLimitDO = new CustomerGoodsPriceLimitDO();
        customerGoodsPriceLimitDO.setDelFlag(1);
        customerGoodsPriceLimitDO.setOpUserId(request.getOpUserId());

        QueryWrapper<CustomerGoodsPriceLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerGoodsPriceLimitDO::getCplId, cplId).in(CustomerGoodsPriceLimitDO::getGoodsId, request.getGoodsIds());
        this.batchDeleteWithFill(customerGoodsPriceLimitDO, queryWrapper);
        return true;
    }

    @Override
    public List<Long> getGoodsIdsByCustomerEid(Long eid,Long custoemrEid) {
        CustomerPriceLimitDTO customerPriceLimitDTO = customerPriceLimitService.getCustomerPriceLimitByCustomerEidAndEid(eid, custoemrEid);
        if (customerPriceLimitDTO == null) {
            return ListUtil.empty();
        }

        QueryWrapper<CustomerGoodsPriceLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerGoodsPriceLimitDO::getCplId, customerPriceLimitDTO.getId());

        List<CustomerGoodsPriceLimitDO> list = this.list(queryWrapper);
        return list.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
    }

    @Override
    public List<Long> getIsGoodsPriceByGoodsIdsAndBuyerEid(List<Long> gidList, Long eid, Long buyerEid) {
        QueryLimitFlagRequest request = new QueryLimitFlagRequest();
        request.setEid(eid);
        request.setCustomerEids(Arrays.asList(buyerEid));
        List<CustomerPriceLimitDTO> customerPriceLimitDTOList = customerPriceLimitService.getCustomerLimitFlagByEidAndCustomerEid(request);
        if (CollUtil.isEmpty(customerPriceLimitDTOList)) {
            return ListUtil.empty();
        }
        QueryWrapper<CustomerGoodsPriceLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CustomerGoodsPriceLimitDO::getCplId, customerPriceLimitDTOList.stream().map(e -> e.getId()).collect(Collectors.toList()));

        List<CustomerGoodsPriceLimitDO> list = this.list(queryWrapper);
        return list.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
    }

    @Override
    public Map<Long,List<Long>> getLimitByGidsAndCustomerEidsGroupByCustomerEid(List<Long> gidList, List<Long> customerEidList, Long eid) {
        if(CollectionUtil.isEmpty(gidList)||CollectionUtil.isEmpty(customerEidList)){
            return MapUtil.empty();
        }
        List<CustomerGoodsPriceLimitBO> enterpriceGoodsLimitMap = this.baseMapper.getLimitByGoodsIdsAndBuyerEids(gidList, customerEidList, eid);
        if(CollectionUtil.isNotEmpty(enterpriceGoodsLimitMap)){
            return enterpriceGoodsLimitMap.stream().collect(Collectors.groupingBy(CustomerGoodsPriceLimitBO::getEid,Collectors.mapping(CustomerGoodsPriceLimitBO::getGoodsId,Collectors.toList())));
        }
        return MapUtil.empty();
    }
}
