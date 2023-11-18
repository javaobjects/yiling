package com.yiling.bi.dayunhe;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.bi.dayunhe.dto.DayunheSaleOrderDTO;
import com.yiling.bi.dayunhe.dto.request.QueryDayunheSalePageRequest;

/**
 * @author: shuang.zhang
 * @date: 2022/10/14
 */
public interface DayunheSaleOrderApi {

    /**
     * 查询列表分页
     * @param request
     * @return
     */
    Page<DayunheSaleOrderDTO> page(QueryDayunheSalePageRequest request);

}
