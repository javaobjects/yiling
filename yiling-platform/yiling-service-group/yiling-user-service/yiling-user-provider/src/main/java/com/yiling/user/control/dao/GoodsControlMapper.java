package com.yiling.user.control.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.control.dto.request.QueryCustomerControlPageRequest;
import com.yiling.user.control.entity.GoodsControlDO;

/**
 * <p>
 * 商品控销表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-21
 */
@Repository
public interface GoodsControlMapper extends BaseMapper<GoodsControlDO> {
    /**
     * 根据搜索条件分页检索供应商商品
     * @param page
     * @param request
     * @return
     */
    Page<Long> getPageCustomerInfo(Page<Long> page, @Param("request") QueryCustomerControlPageRequest request);
}
