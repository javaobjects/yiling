package com.yiling.sjms.gb.service;

import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.gb.dto.StatisticDTO;
import com.yiling.sjms.gb.dto.request.GbFormStatisticPageRequest;
import com.yiling.sjms.gb.entity.GbStatisticDO;

/**
 * <p>
 * 团购统计信息
 * </p>
 *
 * @author wei.wang
 * @date 2023-02-14
 */
public interface GbStatisticService extends BaseService<GbStatisticDO> {

    /**
     * 获取团购库存信息
     * @param provinceName 省区名称
     * @param goodsCode 产品信息
     * @param dayTime 日期天数
     *  @param month 团购月份
     * @return
     */
    GbStatisticDO getStatisticOne(String provinceName, Long goodsCode, Date dayTime,Date month);

    /**
     * 获取团购统计信息
     * @param request
     * @return
     */
    Page<StatisticDTO> getStatistic(GbFormStatisticPageRequest request);

    /**
     * 统计日表单数据
     */
    void gBStatisticProgram();
}
