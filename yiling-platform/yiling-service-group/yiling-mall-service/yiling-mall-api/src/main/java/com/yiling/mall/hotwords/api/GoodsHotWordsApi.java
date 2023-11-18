package com.yiling.mall.hotwords.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.mall.hotwords.dto.GoodsHotWordsAvailableDTO;
import com.yiling.mall.hotwords.dto.GoodsHotWordsDTO;
import com.yiling.mall.hotwords.dto.request.QueryGoodsHotWordsPageRequest;
import com.yiling.mall.hotwords.dto.request.SaveGoodsHotWordsRequest;
import com.yiling.mall.hotwords.dto.request.UpdateGoodsHotWordsRequest;

/**
 * @author:wei.wang
 * @date:2021/6/15
 */
public interface GoodsHotWordsApi {
    /**
     * 获取热词分页详情
     * @param request
     * @return
     */
    Page<GoodsHotWordsDTO> getGoodsHotWordsPage(QueryGoodsHotWordsPageRequest request);

    /**
     * 根据id获取详情
     * @param id
     * @return
     */
    GoodsHotWordsDTO getGoodsHotWordsDetails(Long id);

    /**
     * 修改热词
     * @param request
     * @return
     */
    Boolean updateGoodsHotWordsById(UpdateGoodsHotWordsRequest request);

    /**
     * 保存热词
     * @param request
     * @return
     */
    Boolean saveGoodsHotWords(SaveGoodsHotWordsRequest request);

    /**
     * 获取pop前台获取可用热词
     * @param number
     * @return
     */
    List<GoodsHotWordsAvailableDTO> getAvailableGoodsHotWords(Integer number);
}
