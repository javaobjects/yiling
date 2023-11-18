package com.yiling.f2b.web.article.controller;

import java.util.Objects;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.basic.article.api.ArticleApi;
import com.yiling.basic.article.dto.ArticleDTO;
import com.yiling.basic.common.ArticleErrorCode;
import com.yiling.basic.sms.enums.ArticleStatusEnum;
import com.yiling.f2b.web.article.form.QueryArticleForm;
import com.yiling.f2b.web.article.vo.ArticleVO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 文章管理模块 Controller
 *
 * @author: fan.shen
 * @date: 2021/12/27
 */
@RestController
@RequestMapping("/article")
@Api(tags = "文章模块接口")
@Slf4j
public class ArticleController extends BaseController {

    @DubboReference
    ArticleApi articleApi;

    @ApiOperation(value = "文章详情")
    @PostMapping("/queryById")
    public Result<ArticleVO> queryById(@RequestBody @Valid QueryArticleForm form) {
        ArticleDTO articleDTO = articleApi.queryById(form.getId());
        if (Objects.isNull(articleDTO)) {
            return Result.failed(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }
        if (ArticleStatusEnum.disable.getStatus().equals(articleDTO.getArticleStatus())) {
            return Result.failed(ArticleErrorCode.ARTICLE_DISABLED);
        }
        return Result.success(PojoUtils.map(articleDTO, ArticleVO.class));
    }

}
