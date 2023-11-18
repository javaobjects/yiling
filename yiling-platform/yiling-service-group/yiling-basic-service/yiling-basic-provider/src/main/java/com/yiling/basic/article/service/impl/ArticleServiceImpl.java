package com.yiling.basic.article.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.article.dao.ArticleMapper;
import com.yiling.basic.article.dto.request.ArticlePageQueryRequest;
import com.yiling.basic.article.dto.request.ArticleSaveOrUpdateRequest;
import com.yiling.basic.article.dto.request.ArticleSwitchStatusRequest;
import com.yiling.basic.article.entity.ArticleDO;
import com.yiling.basic.article.service.ArticleService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 文章服务实现类
 * @author fan.shen
 * @date 2021-12-27
 */
@Slf4j
@Service
public class ArticleServiceImpl extends BaseServiceImpl<ArticleMapper, ArticleDO> implements ArticleService {

    @Override
    public Page<ArticleDO> pageList(ArticlePageQueryRequest request) {
        Page<ArticleDO> page = new Page<>(request.getCurrent(), request.getSize());
        QueryWrapper<ArticleDO> queryWrapper = new QueryWrapper<>();
        String articleTitle = request.getArticleTitle();
        Integer articleStatus = request.getArticleStatus();
        if (StringUtils.isNotEmpty(articleTitle)) {
            queryWrapper.lambda().like(ArticleDO::getArticleTitle, articleTitle);
        }
        if (articleStatus != null && articleStatus > 0) {
            queryWrapper.lambda().eq(ArticleDO::getArticleStatus, articleStatus);
        }
        queryWrapper.lambda().orderByDesc(ArticleDO::getUpdateTime);
        return this.page(page, queryWrapper);
    }

    @Override
    public ArticleDO saveOrUpdateArticle(ArticleSaveOrUpdateRequest request) {
        ArticleDO articleDO = PojoUtils.map(request, ArticleDO.class);
        this.saveOrUpdate(articleDO);
        return articleDO;
    }

    @Override
    public ArticleDO queryById(Long id) {
        return this.getById(id);
    }

    @Override
    public Boolean switchStatus(ArticleSwitchStatusRequest request) {
        ArticleDO articleDO = PojoUtils.map(request, ArticleDO.class);
        return this.updateById(articleDO);
    }
}
