package com.yiling.sjms.gb.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.gb.dao.GbGoodsMapper;
import com.yiling.sjms.gb.dto.GoodsDTO;
import com.yiling.sjms.gb.dto.request.QueryGBGoodsInfoPageRequest;
import com.yiling.sjms.gb.entity.GbGoodsDO;
import com.yiling.sjms.gb.service.GbGoodsService;

/**
 * <p>
 * 团购商品 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
@Service
public class GbGoodsServiceImpl extends BaseServiceImpl<GbGoodsMapper, GbGoodsDO> implements GbGoodsService {

   /* @Override
    public Page<GoodsDTO> getGBGoodsPage(QueryGBGoodsInfoPageRequest request) {
        QueryWrapper<GbGoodsDO> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(request.getName())){
            wrapper.lambda().like(GbGoodsDO:: getName,request.getName());
        }
        wrapper.lambda().eq(GbGoodsDO:: getStatus,1);
        Page<GbGoodsDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        Page<GoodsDTO> goodsPage = PojoUtils.map(page, GoodsDTO.class);
        return goodsPage;
    }

    @Override
    public List<GbGoodsDO> listByCode(List<String> codeList) {
        QueryWrapper<GbGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(GbGoodsDO:: getCode,codeList);
        return list(wrapper);
    }

    @Override
    public GbGoodsDO getOneByCode(String code) {
        QueryWrapper<GbGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GbGoodsDO:: getCode,code);
        return  getOne(wrapper);
    }*/
}
