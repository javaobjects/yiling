package com.yiling.goods.standard.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.goods.standard.bo.StandardSpecificationGoodsInfoBO;
import com.yiling.goods.standard.dto.request.IndexStandardGoodsSpecificationPageRequest;
import com.yiling.goods.standard.dto.request.StandardSpecificationPageRequest;
import com.yiling.goods.standard.entity.StandardGoodsSpecificationDO;

/**
 * <p>
 * 商品规格标准表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Repository
public interface StandardGoodsSpecificationMapper extends BaseMapper<StandardGoodsSpecificationDO> {

    /**
     * 获取标准商品信息
     * @param page
     * @param request
     * @return
     */
    Page<StandardGoodsSpecificationDO> getIndexStandardGoodsSpecificationInfoPage(Page<StandardGoodsSpecificationDO> page, @Param("request") IndexStandardGoodsSpecificationPageRequest request);


    /**
     * 获取标准规格商品信息
     * @param page
     * @param request
     * @return
     */
    Page<StandardSpecificationGoodsInfoBO> getSpecificationGoodsInfoPage(Page<StandardGoodsSpecificationDO> page, @Param("request") StandardSpecificationPageRequest request);

    /**
     * b2b分页查询标准规格
     * @param page
     * @param request
     * @return
     */
    Page<StandardGoodsSpecificationDO> querySpecificationByB2b(Page<StandardGoodsSpecificationDO> page, @Param("request") IndexStandardGoodsSpecificationPageRequest request);

}
