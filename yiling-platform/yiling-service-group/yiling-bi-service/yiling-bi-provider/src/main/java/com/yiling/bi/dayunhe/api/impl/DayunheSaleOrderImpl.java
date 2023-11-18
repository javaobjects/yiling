package com.yiling.bi.dayunhe.api.impl;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.bi.dayunhe.DayunheSaleOrderApi;
import com.yiling.bi.dayunhe.dto.DayunheSaleOrderDTO;
import com.yiling.bi.dayunhe.dto.request.QueryDayunheSalePageRequest;
import com.yiling.bi.dayunhe.service.DayunheSaleOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/10/14
 */
@DubboService
@Slf4j
public class DayunheSaleOrderImpl implements DayunheSaleOrderApi {

    @Autowired
    private DayunheSaleOrderService dayunheSaleOrderService;


    @Override
    public Page<DayunheSaleOrderDTO> page(QueryDayunheSalePageRequest request) {
        return null;
    }
}
