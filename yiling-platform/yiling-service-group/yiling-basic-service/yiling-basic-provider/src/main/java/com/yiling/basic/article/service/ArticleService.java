package com.yiling.basic.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.article.dto.request.ArticlePageQueryRequest;
import com.yiling.basic.article.dto.request.ArticleSaveOrUpdateRequest;
import com.yiling.basic.article.dto.request.ArticleSwitchStatusRequest;
import com.yiling.basic.article.entity.ArticleDO;
import com.yiling.framework.common.base.BaseService;

/**
 * 文章 服务类
 * @author fan.shen
 * @date 2021-12-27
 */
public interface ArticleService extends BaseService<ArticleDO> {

    /**
     * 短信日志查询
     * @param request
     * @return
     */
    Page<ArticleDO> pageList(ArticlePageQueryRequest request);

    /**
     * 短信日志查询
     * @param request
     * @return
     */
    ArticleDO saveOrUpdateArticle(ArticleSaveOrUpdateRequest request);

    /**
     * 查询文章
     * @param id
     * @return
     */
    ArticleDO queryById(Long id);

    /**
     * 更新状态
     * @param request
     * @return
     */
    Boolean switchStatus(ArticleSwitchStatusRequest request);
}
