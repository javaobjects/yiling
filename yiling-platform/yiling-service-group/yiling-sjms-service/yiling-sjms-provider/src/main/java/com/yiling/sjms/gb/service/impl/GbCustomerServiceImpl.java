package com.yiling.sjms.gb.service.impl;

import org.apache.dubbo.common.utils.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.gb.dao.CustomerMapper;
import com.yiling.sjms.gb.dto.request.QueryGBGoodsInfoPageRequest;
import com.yiling.sjms.gb.dto.request.SaveCustomerRequest;
import com.yiling.sjms.gb.entity.GbCustomerDO;
import com.yiling.sjms.gb.service.GbCustomerService;

/**
 * <p>
 * 团购单位 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
@Service
public class GbCustomerServiceImpl extends BaseServiceImpl<CustomerMapper, GbCustomerDO> implements GbCustomerService {

    @Override
    public Long addCustomer(SaveCustomerRequest request) {
        GbCustomerDO gbCustomerDO = PojoUtils.map(request, GbCustomerDO.class);
        save(gbCustomerDO);
        return gbCustomerDO.getId() ;
    }

    @Override
    public Page<GbCustomerDO> getCustomer(QueryGBGoodsInfoPageRequest request) {
        QueryWrapper<GbCustomerDO> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(request.getName())){
            wrapper.lambda().like(GbCustomerDO:: getName,request.getName());
        }
        Page<GbCustomerDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        return page;
    }

    @Override
    public GbCustomerDO getCustomerByName(String name) {
        QueryWrapper<GbCustomerDO> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(name)){
            wrapper.lambda().eq(GbCustomerDO:: getName,name).last(" limit 1 ");
        }
        return getOne(wrapper);
    }

    @Override
    public GbCustomerDO getCustomerByCreditCode(String CreditCode) {
        QueryWrapper<GbCustomerDO> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(CreditCode)){
            wrapper.lambda().eq(GbCustomerDO:: getCreditCode,CreditCode).last(" limit 1 ");
        }
        return getOne(wrapper);
    }
}
