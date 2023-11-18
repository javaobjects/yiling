package com.yiling.admin.data.center.article.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.admin.data.center.article.form.QueryArticleDetailForm;
import com.yiling.admin.data.center.article.form.QueryArticlePageForm;
import com.yiling.admin.data.center.article.form.SaveOrUpdateArticleForm;
import com.yiling.admin.data.center.article.form.SwitchStatusForm;
import com.yiling.admin.data.center.article.vo.ArticlePageVO;
import com.yiling.admin.data.center.article.vo.ArticleVO;
import com.yiling.basic.article.api.ArticleApi;
import com.yiling.basic.article.dto.ArticleDTO;
import com.yiling.basic.article.dto.request.ArticlePageQueryRequest;
import com.yiling.basic.article.dto.request.ArticleSaveOrUpdateRequest;
import com.yiling.basic.article.dto.request.ArticleSwitchStatusRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

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

    @DubboReference
    UserApi    userApi;

    @ApiOperation(value = "文章列表")
    @PostMapping("/pageList")
    public Result<Page<ArticlePageVO>> pageList(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody QueryArticlePageForm form) {
        ArticlePageQueryRequest request = PojoUtils.map(form, ArticlePageQueryRequest.class);
        Page<ArticleDTO> pageDTO = articleApi.pageList(request);
        if(CollectionUtils.isNotEmpty(pageDTO.getRecords())) {
            List<Long> createUserIdList = pageDTO.getRecords().stream().map(ArticleDTO::getCreateUser).collect(Collectors.toList());
            List<Long> updateUserIdList = pageDTO.getRecords().stream().map(ArticleDTO::getUpdateUser).collect(Collectors.toList());
            ArrayList<Long> userIdList = Lists.newArrayList();
            userIdList.addAll(createUserIdList);
            userIdList.addAll(updateUserIdList);
            List<UserDTO> userDTOList = userApi.listByIds(userIdList);
            Map<Long, UserDTO> userMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, o -> o, (k1, k2) -> k1));
            pageDTO.getRecords().stream().forEach(item -> {
                String createUserName = Optional.ofNullable(userMap.get(item.getCreateUser())).map(UserDTO::getName).orElse(null);
                item.setCreateUserName(createUserName);
                String updateUserName = Optional.ofNullable(userMap.get(item.getUpdateUser())).map(UserDTO::getName).orElse(null);
                item.setUpdateUserName(updateUserName);
            });
        }
        Page<ArticlePageVO> pageVO = PojoUtils.map(pageDTO, ArticlePageVO.class);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "保存文章")
    @PostMapping("/saveOrUpdate")
    @Log(title = "保存或者更新文章",businessType = BusinessTypeEnum.INSERT)
    public Result<ArticleVO> saveOrUpdate(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveOrUpdateArticleForm form) {
        ArticleSaveOrUpdateRequest request = PojoUtils.map(form, ArticleSaveOrUpdateRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        ArticleDTO articleDTO = articleApi.saveOrUpdateArticle(request);
        return Result.success(PojoUtils.map(articleDTO, ArticleVO.class));
    }

    @ApiOperation(value = "文章详情")
    @PostMapping("/queryById")
    public Result<ArticleVO> queryById(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryArticleDetailForm form) {
        ArticleDTO articleDTO = articleApi.queryById(form.getId());
        return Result.success(PojoUtils.map(articleDTO, ArticleVO.class));
    }

    @ApiOperation(value = "切换状态")
    @PostMapping("/switchStatus")
    @Log(title = "切换文章状态",businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> switchStatus(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SwitchStatusForm form) {
        ArticleSwitchStatusRequest request = PojoUtils.map(form, ArticleSwitchStatusRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(articleApi.switchStatus(request));
    }

}
