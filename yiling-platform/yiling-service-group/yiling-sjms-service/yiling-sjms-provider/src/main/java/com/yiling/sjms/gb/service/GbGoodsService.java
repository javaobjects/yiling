package com.yiling.sjms.gb.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.gb.dto.GoodsDTO;
import com.yiling.sjms.gb.dto.request.QueryGBGoodsInfoPageRequest;
import com.yiling.sjms.gb.entity.GbGoodsDO;


/**
 * <p>
 * 团购商品 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */

public interface GbGoodsService extends BaseService<GbGoodsDO> {

    /**
     * 获取团购商品信息
     * @param request
     * @return
    Page<GoodsDTO> getGBGoodsPage(QueryGBGoodsInfoPageRequest request);

    *//**
     * 根据code获取商品信息
     * @param codeList
     * @return
     *//*
    List<GbGoodsDO> listByCode(List<String> codeList);


    *//**
     * 根据code获取商品信息
     * @param code
     * @return
     *//*
  GbGoodsDO getOneByCode(String code);*/


}
