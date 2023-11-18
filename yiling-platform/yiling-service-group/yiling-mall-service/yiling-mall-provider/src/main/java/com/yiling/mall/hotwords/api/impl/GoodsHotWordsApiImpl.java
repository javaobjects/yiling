package com.yiling.mall.hotwords.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.hotwords.api.GoodsHotWordsApi;
import com.yiling.mall.hotwords.dto.GoodsHotWordsAvailableDTO;
import com.yiling.mall.hotwords.dto.GoodsHotWordsDTO;
import com.yiling.mall.hotwords.dto.request.QueryGoodsHotWordsPageRequest;
import com.yiling.mall.hotwords.dto.request.SaveGoodsHotWordsRequest;
import com.yiling.mall.hotwords.dto.request.UpdateGoodsHotWordsRequest;
import com.yiling.mall.hotwords.entity.GoodsHotWordsDO;
import com.yiling.mall.hotwords.service.GoodsHotWordsService;

/**
 * @author:wei.wang
 * @date:2021/6/15
 */
@DubboService
public class GoodsHotWordsApiImpl implements GoodsHotWordsApi {

    @Autowired
    private GoodsHotWordsService goodsHotWordsService;

    /**
     * 获取热词分页详情
     *
     * @param request
     * @return
     */
    @Override
    public Page<GoodsHotWordsDTO> getGoodsHotWordsPage(QueryGoodsHotWordsPageRequest request) {
        return goodsHotWordsService.getGoodsHotWordsPage(request);
    }

    /**
     * 根据id获取详情
     *
     * @param id
     * @return
     */
    @Override
    public GoodsHotWordsDTO getGoodsHotWordsDetails(Long id) {
        GoodsHotWordsDO details = goodsHotWordsService.getGoodsHotWordsDetails(id);
        return PojoUtils.map(details,GoodsHotWordsDTO.class);
    }

    /**
     * 修改热词
     *
     * @param request
     * @return
     */
    @Override
    public Boolean updateGoodsHotWordsById(UpdateGoodsHotWordsRequest request) {
        return goodsHotWordsService.updateGoodsHotWordsById(request);
    }

    /**
     * 保存热词
     *
     * @param request
     * @return
     */
    @Override
    public Boolean saveGoodsHotWords(SaveGoodsHotWordsRequest request) {

        return goodsHotWordsService.saveGoodsHotWords(request);
    }

    /**
     * 获取pop前台获取可用热词
     *
     * @param number
     * @return
     */
    @Override
    public List<GoodsHotWordsAvailableDTO> getAvailableGoodsHotWords(Integer number) {
        return  goodsHotWordsService.getAvailableGoodsHotWords(number);
    }
}
