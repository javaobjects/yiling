package com.yiling.hmc.goods.dao;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.hmc.goods.dto.request.GoodsControlPageRequest;
import com.yiling.hmc.goods.entity.GoodsControlDO;

/**
 * @author shichen
 * @类名 GoodsControlMapper
 * @描述 管控商品mapper
 * @创建时间 2022/3/29
 * @修改人 shichen
 * @修改时间 2022/3/29
 **/
public interface GoodsControlMapper extends BaseMapper<GoodsControlDO> {

    /**
     * 获取上架商品管控商品分页列表
     * @param page
     * @param request
     * @return
     */
    Page<GoodsControlDO> queryUpGoodsControlPageList(Page<GoodsControlDO> page, @Param("request")GoodsControlPageRequest request);

    /**
     * 获取保险商品管控商品分页列表
     * @param page
     * @param request
     * @return
     */
    Page<GoodsControlDO> queryInsuranceGoodsControlPageList(Page<GoodsControlDO> page, @Param("request")GoodsControlPageRequest request);
}
