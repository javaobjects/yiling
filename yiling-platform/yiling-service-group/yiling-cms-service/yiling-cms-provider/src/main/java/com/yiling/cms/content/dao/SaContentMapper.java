package com.yiling.cms.content.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dto.HMCContentDTO;
import com.yiling.cms.content.dto.request.QueryHMCContentPageRequest;
import com.yiling.cms.content.entity.SaContentDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 销售助手内容表 Dao 接口
 * </p>
 *
 * @author yong.zhang
 * @date 2023-06-25
 */
@Repository
public interface SaContentMapper extends BaseMapper<SaContentDO> {

    /**
     * 分页查询
     *
     * @param page
     * @param request
     * @return
     */
    Page<HMCContentDTO> listPage(Page<SaContentDO> page, @Param("request") QueryHMCContentPageRequest request);

}
