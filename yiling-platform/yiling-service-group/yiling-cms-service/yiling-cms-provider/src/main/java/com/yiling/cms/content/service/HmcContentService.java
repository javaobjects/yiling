package com.yiling.cms.content.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dto.AppContentDTO;
import com.yiling.cms.content.dto.HMCContentDTO;
import com.yiling.cms.content.dto.request.*;
import com.yiling.cms.content.entity.HmcContentDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * HMC端内容表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-11-07
 */
public interface HmcContentService extends BaseService<HmcContentDO> {

    /**
     * 添加HMC内容
     * @param addContentRequest
     * @return
     */
    Boolean addContent(AddHmcContentRequest addContentRequest);

    /**
     * 排序
     * @param request
     * @return
     */
    Boolean contentRank(ContentRankRequest request);

    /**
     * 运营后台查询
     * @param request
     * @return
     */
    Page<HMCContentDTO> listPage(QueryHMCContentPageRequest request);

    /**
     * hMC查询
     * @param request
     * @return
     */
    Page<AppContentDTO> listAppContentPageBySql(QueryAppContentPageRequest request);

    /**
     * 根据contentId查询
     * @param idList
     * @return
     */
    List<HmcContentDO> listByContentIdList(List<Long> idList);

    /**
     * 更新浏览量
     * @param id
     */
    void updateView(Long id);

    /**
     * 修改引用状态
     * @param request
     * @return
     */
    Boolean updateReferStatus(ContentReferStatusRequest request);
}
