package com.yiling.basic.article.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.article.api.ArticleApi;
import com.yiling.basic.article.dto.ArticleDTO;
import com.yiling.basic.article.dto.request.ArticlePageQueryRequest;
import com.yiling.basic.article.dto.request.ArticleSaveOrUpdateRequest;
import com.yiling.basic.article.dto.request.ArticleSwitchStatusRequest;
import com.yiling.basic.article.service.ArticleService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author: fan.shen
 * @date: 2021/6/9
 */
@DubboService
public class ArticleApiImpl implements ArticleApi {

    @Autowired
    ArticleService articleService;

    @Override
    public Page<ArticleDTO> pageList(ArticlePageQueryRequest request) {
        return PojoUtils.map(articleService.pageList(request), ArticleDTO.class);
    }

    @Override
    public ArticleDTO saveOrUpdateArticle(ArticleSaveOrUpdateRequest request) {
        return PojoUtils.map(articleService.saveOrUpdateArticle(request), ArticleDTO.class);
    }

    @Override
    public ArticleDTO queryById(Long id) {
        return PojoUtils.map(articleService.queryById(id), ArticleDTO.class);
    }

    @Override
    public Boolean switchStatus(ArticleSwitchStatusRequest request) {
        return articleService.switchStatus(request);
    }
}
