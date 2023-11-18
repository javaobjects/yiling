package com.yiling.cms.content.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dto.AppContentDTO;
import com.yiling.cms.content.dto.IHDocContentDTO;
import com.yiling.cms.content.dto.request.*;
import com.yiling.cms.content.entity.IHDoctorContentDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * IH医生端内容表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-11-07
 */
public interface IHDocContentService extends BaseService<IHDoctorContentDO> {


    /**
     * 添加HMC内容
     * @param addContentRequest
     * @return
     */
    Boolean addContent(AddIHDocContentRequest addContentRequest);

    /**
     * 排序
     * @param request
     * @return
     */
    Boolean contentRank(ContentRankRequest request);

    /**
     * 查询
     * @param request
     * @return
     */
    Page<IHDocContentDTO> listPage(QueryIHDocContentPageRequest request);

    /**
     * IHDoc端查询内容
     * @param request
     * @return
     */
    Page<AppContentDTO> listAppContentPageBySql(QueryAppContentPageRequest request);


    List<IHDoctorContentDO> listByContentIdList(List<Long> idList);

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

    /**
     * 更新IhDoc文章权限
     * @param request
     * @return
     */
    Boolean updateIHDocContentAuth(UpdateContentAuthRequest request);
}
