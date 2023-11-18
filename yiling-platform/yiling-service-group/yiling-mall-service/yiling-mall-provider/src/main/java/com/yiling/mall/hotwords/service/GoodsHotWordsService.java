package com.yiling.mall.hotwords.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.mall.hotwords.dto.GoodsHotWordsAvailableDTO;
import com.yiling.mall.hotwords.dto.GoodsHotWordsDTO;
import com.yiling.mall.hotwords.dto.request.QueryGoodsHotWordsPageRequest;
import com.yiling.mall.hotwords.dto.request.SaveGoodsHotWordsRequest;
import com.yiling.mall.hotwords.dto.request.UpdateGoodsHotWordsRequest;
import com.yiling.mall.hotwords.entity.GoodsHotWordsDO;

/**
 * <p>
 * 商品热词表 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2021-06-11
 */
public interface GoodsHotWordsService extends BaseService<GoodsHotWordsDO> {

    /**
     * 获取热词分页信息
     * @param request
     * @return
     */
    Page<GoodsHotWordsDTO> getGoodsHotWordsPage(QueryGoodsHotWordsPageRequest request);

    /**
     * 根据id查询热词详情
     * @param id
     * @return
     */
    GoodsHotWordsDO getGoodsHotWordsDetails(Long id);

    /**
     * 修改商品信息
     * @param request
     * @return
     */
    Boolean updateGoodsHotWordsById(UpdateGoodsHotWordsRequest request);

    /**
     * 保存热词信息
     * @param request
     * @return
     */
    Boolean saveGoodsHotWords(SaveGoodsHotWordsRequest request);

    /**
     * 获取可用热词
     * @param number
     * @return
     */
    List<GoodsHotWordsAvailableDTO> getAvailableGoodsHotWords(Integer number);
}
