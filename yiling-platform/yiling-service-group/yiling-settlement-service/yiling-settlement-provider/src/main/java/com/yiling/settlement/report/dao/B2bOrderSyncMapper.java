package com.yiling.settlement.report.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.settlement.report.dto.request.QueryOrderSyncPageListRequest;
import com.yiling.settlement.report.entity.B2bOrderSyncDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 返利报表的B2B订单同步表 Dao 接口
 * </p>
 *
 * @author dexi.yao
 * @date 2022-08-08
 */
@Repository
public interface B2bOrderSyncMapper extends BaseMapper<B2bOrderSyncDO> {

    /**
     * 查询b2b订单号
     *
     * @param page
     * @param request
     * @return
     */
    Page<String> queryB2bOrderNoPageList(Page<String> page, @Param("request")QueryOrderSyncPageListRequest request);

    /**
     * 查询订单信息同步表id
     *
     * @param page
     * @param request
     * @return
     */
    Page<Long> queryB2bSyncKeyPageList(Page<Long> page, @Param("request")QueryOrderSyncPageListRequest request);
}
