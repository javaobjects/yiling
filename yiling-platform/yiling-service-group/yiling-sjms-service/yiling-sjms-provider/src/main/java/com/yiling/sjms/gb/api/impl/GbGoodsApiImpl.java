package com.yiling.sjms.gb.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsInfoPageRequest;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.gb.api.GbGoodsApi;
import com.yiling.sjms.gb.dto.GoodsDTO;
import com.yiling.sjms.gb.dto.request.QueryGBGoodsInfoPageRequest;
import com.yiling.sjms.gb.service.GbGoodsService;

import cn.hutool.core.collection.CollectionUtil;

/**
 * 团购商品
 *
 * @author: wei.wang
 * @date: 2022/11/28
 */
@DubboService
public class GbGoodsApiImpl implements GbGoodsApi {
    @DubboReference
    CrmGoodsInfoApi crmGoodsInfoApi;

    @Override
    public Page<GoodsDTO> getGBGoodsPage(QueryGBGoodsInfoPageRequest request) {
        QueryCrmGoodsInfoPageRequest goodsRequest = new QueryCrmGoodsInfoPageRequest();
        goodsRequest.setGoodsName(request.getName());
        goodsRequest.setIsGroupPurchase(1);
        goodsRequest.setStatus(0);
        Page<CrmGoodsInfoDTO> page = crmGoodsInfoApi.getPage(goodsRequest);
        Page<GoodsDTO> result = PojoUtils.map(page, GoodsDTO.class);
        if(CollectionUtil.isNotEmpty(page.getRecords())){
            List<GoodsDTO> list = new ArrayList<>();
            for(CrmGoodsInfoDTO one : page.getRecords()){
                GoodsDTO goods = new GoodsDTO();
                goods.setCode(one.getGoodsCode());
                goods.setName(one.getGoodsName());
                goods.setSpecification(one.getGoodsSpec());
                if(StringUtils.isNotBlank(one.getPacking())){
                    goods.setSmallPackage(Integer.valueOf(one.getPacking()));
                }else{
                    goods.setSmallPackage(0);
                }
                goods.setPrice(one.getSupplyPrice());
                list.add(goods);
            }
            result.setRecords(list);
        }
        return result;
    }

    @Override
    public List<GoodsDTO> listByCode(List<Long> codeList) {
        List<CrmGoodsInfoDTO> result = crmGoodsInfoApi.findByCodeList(codeList);
        List<GoodsDTO> list = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(result)){
            for(CrmGoodsInfoDTO one : result){
                GoodsDTO goods = new GoodsDTO();
                goods.setCode(one.getGoodsCode());
                goods.setName(one.getGoodsName());
                goods.setSpecification(one.getGoodsSpec());
                goods.setSmallPackage(Integer.valueOf(one.getPacking()));
                goods.setPrice(one.getSupplyPrice());
                list.add(goods);
            }
        }
        return list;
    }

    @Override
    public GoodsDTO getOneByCode(Long code) {
        CrmGoodsInfoDTO result = crmGoodsInfoApi.findByCodeAndName(code, null);
        if(result != null ){
            GoodsDTO goods = new GoodsDTO();
            goods.setCode(result.getGoodsCode());
            goods.setName(result.getGoodsName());
            goods.setSpecification(result.getGoodsSpec());
            goods.setSmallPackage(Integer.valueOf(result.getPacking()));
            goods.setPrice(result.getSupplyPrice());
            return goods;
        }
        return null;
    }


}
