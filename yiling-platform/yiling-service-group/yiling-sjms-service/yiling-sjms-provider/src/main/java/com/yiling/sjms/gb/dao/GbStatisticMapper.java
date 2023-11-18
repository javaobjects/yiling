package com.yiling.sjms.gb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.sjms.gb.dto.StatisticDTO;
import com.yiling.sjms.gb.dto.request.GbFormStatisticPageRequest;
import com.yiling.sjms.gb.entity.GbStatisticDO;

/**
 * <p>
 * 团购信息 Dao 接口
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
@Repository
public interface GbStatisticMapper extends BaseMapper<GbStatisticDO> {

    /**
     * 获取团购统计按照省区分类
     * @param page
     * @param request
     * @return
     */
    Page<StatisticDTO> getGBStatisticByProvinceListPage(Page<GbStatisticDO> page, @Param("request") GbFormStatisticPageRequest request);

       /**
     * 获取团购统计按照品分类
     * @param page
     * @param request
     * @return
     */
    Page<StatisticDTO> getGBStatisticByCodeListPage(Page<GbStatisticDO> page, @Param("request") GbFormStatisticPageRequest request);

    /**
     * 获取团购统计按照日期分类
     * @param page
     * @param request
     * @return
     */
    Page<StatisticDTO> getGBStatisticByTimeListPage(Page<GbStatisticDO> page, @Param("request") GbFormStatisticPageRequest request);

    /**
     *
     * @param bizType
     * @return
     */
    List<StatisticDTO> getGBStatisticProgram(@Param("bizType") Integer bizType);

}
