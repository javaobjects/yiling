package com.yiling.basic.article.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.article.dto.ArticleDTO;
import com.yiling.basic.article.dto.request.ArticlePageQueryRequest;
import com.yiling.basic.article.dto.request.ArticleSaveOrUpdateRequest;
import com.yiling.basic.article.dto.request.ArticleSwitchStatusRequest;

/**
 * @author: fan.shen
 * @date: 2021/12/27
 */
public interface ArticleApi {

    /**
     * 文章列表
     * @param request
     * @return
     */
    Page<ArticleDTO> pageList(ArticlePageQueryRequest request);

    /**
     * 保存或者更新文章
     * @param request
     * @return
     */
    ArticleDTO saveOrUpdateArticle(ArticleSaveOrUpdateRequest request);

    /**
     * 查询文章
     * @param id
     * @return
     */
    ArticleDTO queryById(Long id);

    /**
     * 切换状态
     * @param request
     * @return
     */
    Boolean switchStatus(ArticleSwitchStatusRequest request);
}
