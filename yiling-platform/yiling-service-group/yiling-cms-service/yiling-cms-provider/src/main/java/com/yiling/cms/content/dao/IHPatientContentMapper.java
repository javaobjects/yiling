package com.yiling.cms.content.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dto.IHPatientContentPageDTO;
import com.yiling.cms.content.dto.IHPatientHomeContentPageDTO;
import com.yiling.cms.content.dto.request.QueryIHPatientContentPageRequest;
import com.yiling.cms.content.entity.IHPatientContentDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * IH患者端内容表 Dao 接口
 * </p>
 *
 * @author fan.shen
 * @date 2022-11-11
 */
@Repository
public interface IHPatientContentMapper extends BaseMapper<IHPatientContentDO> {

    /**
     * 内容列表
     *
     * @param page
     * @param request
     * @return
     */
    Page<IHPatientContentPageDTO> listContentPageBySql(Page<IHPatientContentPageDTO> page, @Param("request") QueryIHPatientContentPageRequest request);

    /**
     * 忽略逻辑删除查询
     *
     * @param contentDO
     * @return
     */
    IHPatientContentDO selectIgnoreLogicDelete(@Param("content") IHPatientContentDO contentDO);

    /**
     * 更改删除状态
     *
     * @param ihPatientContentDO
     * @return
     */
    int updateDelFlag(@Param("content") IHPatientContentDO ihPatientContentDO);

    /**
     * 患者首页文章列表
     *
     * @param request
     * @return
     */
    Page<IHPatientHomeContentPageDTO> homeListContentPageBySql(Page<IHPatientHomeContentPageDTO> page, @Param("request") QueryIHPatientContentPageRequest request);

}
