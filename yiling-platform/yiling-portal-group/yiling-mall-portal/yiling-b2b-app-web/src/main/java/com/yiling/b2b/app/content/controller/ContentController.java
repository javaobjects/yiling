package com.yiling.b2b.app.content.controller;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.app.content.form.GetContentForm;
import com.yiling.b2b.app.content.form.QueryContentPageForm;
import com.yiling.b2b.app.content.vo.ContentDetailVO;
import com.yiling.b2b.app.content.vo.ContentVO;
import com.yiling.cms.content.api.ContentApi;
import com.yiling.cms.content.dto.AppContentDTO;
import com.yiling.cms.content.dto.AppContentDetailDTO;
import com.yiling.cms.content.dto.request.QueryAppContentPageRequest;
import com.yiling.cms.content.enums.LineEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 内容
 *
 * @author: gxl
 * @date: 2022/3/28
 */
@Api(tags = "内容")
@RestController
@RequestMapping("/cms/content")
public class ContentController extends BaseController {

    @DubboReference
    ContentApi contentApi;

    @Autowired
    private FileService fileService;


    @ApiOperation("列表")
    @GetMapping("queryContentPage")
    public Result<Page<ContentVO>> queryContentPage(@Valid QueryContentPageForm queryContentPageForm) {
        QueryAppContentPageRequest request = new QueryAppContentPageRequest();
        PojoUtils.map(queryContentPageForm, request);
        Page<AppContentDTO> contentDTOPage = contentApi.listAppContentPage(request);
        Page<ContentVO> contentVOPage = PojoUtils.map(contentDTOPage, ContentVO.class);
        if (contentVOPage.getTotal() == 0) {
            return Result.success(contentVOPage);
        }
        contentVOPage.getRecords().forEach(contentVO -> {
            if (StrUtil.isNotEmpty(contentVO.getCover())) {
                contentVO.setCover(fileService.getUrl(contentVO.getCover(), FileTypeEnum.CONTENT_COVER));
            }
            if (StrUtil.isNotEmpty(contentVO.getVedioFileUrl())) {
                contentVO.setVedioFileUrl(fileService.getUrl(contentVO.getVedioFileUrl(), FileTypeEnum.VEDIO_CONTENT));
            }
        });
        return Result.success(contentVOPage);
    }

    @ApiOperation("详情")
    @GetMapping("getContentDetail")
    public Result<ContentDetailVO> getContentDetail(@Valid GetContentForm form) {
        AppContentDetailDTO contentDetail = contentApi.getContentDetail(form.getId(), LineEnum.getByCode(form.getLineId()));
        ContentDetailVO contentDetailVO = new ContentDetailVO();
        PojoUtils.map(contentDetail, contentDetailVO);
        contentDetailVO.setContentId(contentDetail.getContentId());

        if (StrUtil.isNotEmpty(contentDetailVO.getCover())) {
            contentDetailVO.setCover(fileService.getUrl(contentDetailVO.getCover(), FileTypeEnum.CONTENT_COVER));
        }
        if (StrUtil.isNotEmpty(contentDetailVO.getVedioFileUrl())) {
            contentDetailVO.setVedioFileUrl(fileService.getUrl(contentDetailVO.getVedioFileUrl(), FileTypeEnum.VEDIO_CONTENT));
        }

        return Result.success(contentDetailVO);
    }


}