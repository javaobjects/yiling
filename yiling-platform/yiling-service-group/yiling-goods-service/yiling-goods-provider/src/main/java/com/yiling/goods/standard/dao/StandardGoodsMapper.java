package com.yiling.goods.standard.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.goods.standard.dto.request.StandardGoodsInfoRequest;
import com.yiling.goods.standard.entity.StandardGoodsDO;

/**
 * <p>
 * 商品标准表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Repository
public interface StandardGoodsMapper extends BaseMapper<StandardGoodsDO> {

    /**
     * 获取标准商品信息
     * @param page
     * @param request
     * @return
     */
    Page<StandardGoodsDO> standardGoodsInfoPage(Page<StandardGoodsDO> page, @Param("request") StandardGoodsInfoRequest request);

}
