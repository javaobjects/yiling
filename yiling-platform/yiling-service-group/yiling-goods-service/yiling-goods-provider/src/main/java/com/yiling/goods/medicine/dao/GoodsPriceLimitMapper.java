package com.yiling.goods.medicine.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.goods.medicine.dto.GoodsPriceLimitDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPriceLimitPageRequest;
import com.yiling.goods.medicine.entity.GoodsPriceLimitDO;

/**
 * <p>
 * 商品限价表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-26
 */
@Repository
public interface GoodsPriceLimitMapper extends BaseMapper<GoodsPriceLimitDO> {

    /**
     * 商品限价分页
     * @param page
     * @param request
     * @return
     */
    Page<GoodsPriceLimitDO> pageList(Page<GoodsPriceLimitDTO> page, @Param("request") QueryGoodsPriceLimitPageRequest request);
}
