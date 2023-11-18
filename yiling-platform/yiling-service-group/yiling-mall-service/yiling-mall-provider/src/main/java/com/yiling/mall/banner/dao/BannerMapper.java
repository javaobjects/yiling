package com.yiling.mall.banner.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.mall.banner.dto.request.QueryBannerPageListRequest;
import com.yiling.mall.banner.entity.BannerDO;

/**
 * <p>
 * banner表 Dao 接口
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Repository
public interface BannerMapper extends BaseMapper<BannerDO> {

    /**
     * 查询banner分页列表
     * @param page
     * @param request
     * @return
     */
    Page<BannerDO> pageList(Page page, @Param("request")QueryBannerPageListRequest request);
}
