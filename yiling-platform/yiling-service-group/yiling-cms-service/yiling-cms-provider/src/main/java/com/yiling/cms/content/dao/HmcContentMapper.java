package com.yiling.cms.content.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dto.AppContentDTO;
import com.yiling.cms.content.dto.HMCContentDTO;
import com.yiling.cms.content.dto.request.QueryAppContentPageRequest;
import com.yiling.cms.content.dto.request.QueryHMCContentPageRequest;
import com.yiling.cms.content.entity.HmcContentDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * HMC端内容表 Dao 接口
 * </p>
 *
 * @author fan.shen
 * @date 2022-11-07
 */
@Repository
public interface HmcContentMapper extends BaseMapper<HmcContentDO> {

    /**
     * 分页查询
     * @param page
     * @param request
     * @return
     */
    Page<HMCContentDTO> listPage(Page<HmcContentDO> page, @Param("request") QueryHMCContentPageRequest request);

    /**
     * HMC内容查询
     * @param page
     * @param request
     * @return
     */
    Page<AppContentDTO> listAppContentPageBySql(Page<HmcContentDO> page, @Param("request") QueryAppContentPageRequest request);
}
