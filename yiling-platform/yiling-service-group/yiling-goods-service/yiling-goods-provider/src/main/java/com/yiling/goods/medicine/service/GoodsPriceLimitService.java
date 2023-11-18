package com.yiling.goods.medicine.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.medicine.dto.GoodsPriceLimitDTO;
import com.yiling.goods.medicine.dto.request.DeleteGoodsPriceLimitRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPriceLimitPageRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsPriceLimitRequest;
import com.yiling.goods.medicine.entity.GoodsPriceLimitDO;

/**
 * <p>
 * 商品限价表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-26
 */
public interface GoodsPriceLimitService extends BaseService<GoodsPriceLimitDO> {

    /**
     * 商品限价分页接口
     * @param request
     * @return
     */
    Page<GoodsPriceLimitDTO> pageList(QueryGoodsPriceLimitPageRequest request);

    /**
     * 添加商品限价条件
     * @param request
     * @return
     */
    Boolean addGoodsPriceLimit(SaveOrUpdateGoodsPriceLimitRequest request);

    /**
     * 移除商品限价条件
     * @param request
     * @return
     */
    Boolean deleteGoodsPriceLimit(DeleteGoodsPriceLimitRequest request);

    /**
     * 编辑商品限价条件
     * @param request
     * @return
     */
    Boolean updateGoodsPriceLimit(SaveOrUpdateGoodsPriceLimitRequest request);

    /**
     * 获取商品限价条件
     * @param id
     * @return
     */
    GoodsPriceLimitDTO getGoodsPriceLimitById(Long id);

    /**
     * 获取商品限价条件列表
     * @param goodsIds
     * @return
     */
    Map<Long, List<GoodsPriceLimitDTO>> listGoodsPriceLimitByGoodsIds(List<Long> goodsIds);

}
