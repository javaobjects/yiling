package com.yiling.cms.content.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.bo.ContentBO;
import com.yiling.cms.content.dto.AppContentDTO;
import com.yiling.cms.content.dto.request.QueryAppContentPageRequest;
import com.yiling.cms.content.dto.request.QueryContentPageRequest;
import com.yiling.cms.content.entity.ContentDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 内容 Dao 接口
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
@Repository
public interface ContentMapper extends BaseMapper<ContentDO> {

    /**
     * 更新阅读量
     *
     * @param contentDO
     * @return
     */
    int updatePv(ContentDO contentDO);

    /**
     * 运营后台查询文章列表
     *
     * @param page
     * @param request
     * @return
     */
    Page<ContentDO> listPage(Page<ContentDO> page, @Param("request") QueryContentPageRequest request);

    /**
     * 患者端、医生端内容列表
     *
     * @param page
     * @param request
     * @return
     */
    Page<AppContentDTO> listAppContentPageBySql(Page<ContentBO> page, @Param("request") QueryAppContentPageRequest request);


    Page<AppContentDTO> listSaContentPage(Page<ContentDO> page, @Param("request") QueryAppContentPageRequest request);

    Page<AppContentDTO> listB2bContentPage(Page<ContentDO> page, @Param("request") QueryAppContentPageRequest request);
}
