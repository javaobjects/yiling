package com.yiling.sjms.gb.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.gb.dto.GoodsDTO;
import com.yiling.sjms.gb.dto.request.QueryGBGoodsInfoPageRequest;

/**
 * 团购商品
 *
 * @author: wei.wang
 * @date: 2022/11/28
 */
public interface GbGoodsApi {
    /**
     * 获取团购商品信息
     * @param request
     * @return
     */
    Page<GoodsDTO> getGBGoodsPage(QueryGBGoodsInfoPageRequest request);

    /**
     *  根据codeList查询商品信息
     * @param codeList
     * @return
     */
    List<GoodsDTO> listByCode(List<Long> codeList);

    /**
     *
     * @param code
     * @return
     */
    GoodsDTO getOneByCode(Long code);

}
