package com.yiling.sjms.gb.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.sjms.gb.dto.GbFormInfoListDTO;
import com.yiling.sjms.gb.dto.GbFormExportListDTO;
import com.yiling.sjms.gb.dto.request.QueryGBFormListPageRequest;
import com.yiling.sjms.gb.entity.GbFormDO;


/**
 * <p>
 * 团购表单 Dao 接口
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
@Repository
public interface GbFormMapper extends BaseMapper<GbFormDO> {

    /**
     * 获取团购列表
     * @param request
     * @return
     */
    Page<GbFormInfoListDTO> getGBFormListPage(Page<GbFormInfoListDTO> page, @Param("request") QueryGBFormListPageRequest request);


    /**
     * 导出查询
     * @param page
     * @param request
     * @return
     */
    Page<GbFormExportListDTO> getGBFormExportListPage(Page<GbFormInfoListDTO> page, @Param("request") QueryGBFormListPageRequest request);

    /**
     * 获取团购列表
     * @param request
     * @return
     */
    Page<GbFormInfoListDTO> getGBFeeApplicationFormListPage(Page<Object> page, @Param("request")QueryGBFormListPageRequest request);

    /**
     * 获取团购费用列表
     * @param request
     * @return
     */
    Page<GbFormInfoListDTO> getGBFeeFormListPage(Page<Object> page, @Param("request")QueryGBFormListPageRequest request);
}
