package com.yiling.hmc.cms.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.cms.content.api.QaApi;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.cms.content.api.ContentApi;
import com.yiling.cms.content.api.HMCContentApi;
import com.yiling.cms.content.dto.AppContentDTO;
import com.yiling.cms.content.dto.AppContentDetailDTO;
import com.yiling.cms.content.dto.request.LikeContentRequest;
import com.yiling.cms.content.dto.request.QueryAppContentPageRequest;
import com.yiling.cms.content.enums.ContentTypeEnum;
import com.yiling.cms.content.enums.LineEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.cms.form.LikeContentForm;
import com.yiling.hmc.cms.form.QueryContentPageForm;
import com.yiling.hmc.cms.vo.ContentDetailVO;
import com.yiling.hmc.cms.vo.ContentVO;
import com.yiling.hmc.cms.vo.HmcDoctorInfoVO;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.HmcDoctorInfoDTO;
import com.yiling.ih.user.dto.request.QueryDoctorPageRequest;
import com.yiling.user.system.bo.CurrentUserInfo;

import cn.hutool.core.collection.CollUtil;
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

    @DubboReference
    HMCContentApi hmcContentApi;

    @DubboReference
    DoctorApi doctorApi;

    @Autowired
    private FileService fileService;

    @DubboReference
    QaApi qaApi;

    @ApiOperation("文章列表")
    @GetMapping("queryContentPage")
    @Log(title = "文章列表", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<ContentVO>> queryContentPage(@Valid QueryContentPageForm queryContentPageForm) {


        List<Integer> docIdList = null;

        // 如果医生名字不为空 -> 调用接口获取医生信息
        if (StrUtil.isNotBlank(queryContentPageForm.getTitle())) {
            QueryDoctorPageRequest queryDoctorRequest = new QueryDoctorPageRequest();
            // 这里指定查询前100个医生
            queryDoctorRequest.setSize(100);
            queryDoctorRequest.setDoctorName(queryContentPageForm.getTitle());
            Page<HmcDoctorInfoDTO> hmcDoctorInfoDTOPage = doctorApi.queryDoctorPage(queryDoctorRequest);
            if (CollUtil.isNotEmpty(hmcDoctorInfoDTOPage.getRecords())) {
                docIdList = hmcDoctorInfoDTOPage.getRecords().stream().map(HmcDoctorInfoDTO::getId).collect(Collectors.toList());
            }
        }
        QueryAppContentPageRequest request = new QueryAppContentPageRequest();
        PojoUtils.map(queryContentPageForm, request);
        request.setContentType(ContentTypeEnum.ARTICLE.getCode());
        request.setIsOpen(1);
        request.setDocIdList(docIdList);
        // Page<AppContentDTO> contentDTOPage = contentApi.listAppContentPageBySql(request);
        Page<AppContentDTO> contentDTOPage = hmcContentApi.listAppContentPageBySql(request);
        Page<ContentVO> contentVOPage = PojoUtils.map(contentDTOPage, ContentVO.class);
        if (contentVOPage.getTotal() == 0) {
            return Result.success(contentVOPage);
        }
        contentVOPage.getRecords().forEach(contentVO -> {
            contentVO.setPublishTime(contentVO.getUpdateTime());
            if (StrUtil.isNotEmpty(contentVO.getCover())) {
                contentVO.setCover(fileService.getUrl(contentVO.getCover(), FileTypeEnum.CONTENT_COVER));
            }
        });
        return Result.success(contentVOPage);
    }

    @ApiOperation("视频列表")
    @GetMapping("queryVideoContentPage")
    @Log(title = "视频列表", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<ContentVO>> queryVideoContentPage(@Valid QueryContentPageForm queryContentPageForm) {

        List<Integer> docIdList = null;

        // 如果医生名字不为空 -> 调用接口获取医生信息
        if (StrUtil.isNotBlank(queryContentPageForm.getTitle())) {
            QueryDoctorPageRequest queryDoctorRequest = new QueryDoctorPageRequest();
            // 这里指定查询前100个医生
            queryDoctorRequest.setSize(100);
            queryDoctorRequest.setDoctorName(queryContentPageForm.getTitle());
            Page<HmcDoctorInfoDTO> hmcDoctorInfoDTOPage = doctorApi.queryDoctorPage(queryDoctorRequest);
            if (CollUtil.isNotEmpty(hmcDoctorInfoDTOPage.getRecords())) {
                docIdList = hmcDoctorInfoDTOPage.getRecords().stream().map(HmcDoctorInfoDTO::getId).collect(Collectors.toList());
            }
        }

        QueryAppContentPageRequest request = new QueryAppContentPageRequest();
        PojoUtils.map(queryContentPageForm, request);
        request.setContentType(ContentTypeEnum.VIDEO.getCode());
        request.setIsOpen(1);
        // 科普视频-2
        request.setModuleId(2L);
        request.setDocIdList(docIdList);
        Page<AppContentDTO> contentDTOPage = hmcContentApi.listAppContentPageBySql(request);
        Page<ContentVO> contentVOPage = PojoUtils.map(contentDTOPage, ContentVO.class);
        if (contentVOPage.getTotal() == 0) {
            return Result.success(contentVOPage);
        }

        // 获取医生id -> 调用doctorApi,获取医生信息
        List<Long> doctorIds = contentDTOPage.getRecords().stream().map(AppContentDTO::getDocId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        Map<Integer, HmcDoctorInfoDTO> doctorInfoDTOMap = Maps.newHashMap();
        if (CollUtil.isNotEmpty(doctorIds)) {
            List<Integer> doctorIdList = doctorIds.stream().map(Long::intValue).collect(Collectors.toList());
            List<HmcDoctorInfoDTO> doctorInfoList = doctorApi.getDoctorInfoByIds(doctorIdList);
            if (CollUtil.isNotEmpty(doctorInfoList)) {
                doctorInfoDTOMap.putAll(doctorInfoList.stream().collect(Collectors.toMap(HmcDoctorInfoDTO::getId, Function.identity())));
            }
        }

        contentVOPage.getRecords().forEach(contentVO -> {
            contentVO.setPublishTime(contentVO.getUpdateTime());
            if (StrUtil.isNotEmpty(contentVO.getCover())) {
                contentVO.setCover(fileService.getUrl(contentVO.getCover(), FileTypeEnum.CONTENT_COVER));
            }

            // 赋值医生相关信息
            if (Objects.nonNull(contentVO.getDocId()) && contentVO.getDocId() > 0 && doctorInfoDTOMap.containsKey(contentVO.getDocId().intValue())) {
                HmcDoctorInfoDTO doctorInfoDTO = doctorInfoDTOMap.get(contentVO.getDocId().intValue());
                HmcDoctorInfoVO doctorInfoVO = PojoUtils.map(doctorInfoDTO, HmcDoctorInfoVO.class);
                contentVO.setDoctorInfoVo(doctorInfoVO);
            }

        });
        return Result.success(contentVOPage);
    }

    @ApiOperation("详情")
    @GetMapping("getContentDetail")
    @Log(title = "详情", businessType = BusinessTypeEnum.OTHER)
    public Result<ContentDetailVO> getContentDetail(@CurrentUser CurrentUserInfo currentUser, @RequestParam Long id) {
        AppContentDetailDTO contentDetail = contentApi.getContentDetail(id, LineEnum.HMC);
        ContentDetailVO contentDetailVO = new ContentDetailVO();
        PojoUtils.map(contentDetail, contentDetailVO);

        // 如果存在医生id
        if (Objects.nonNull(contentDetailVO.getDocId()) && contentDetailVO.getDocId() > 0) {
            // 1、赋值医生相关信息
            List<HmcDoctorInfoDTO> doctorInfoList = doctorApi.getDoctorInfoByIds(Collections.singletonList(contentDetailVO.getDocId().intValue()));
            if (CollUtil.isNotEmpty(doctorInfoList)) {
                contentDetailVO.setDoctorInfoVo(PojoUtils.map(doctorInfoList.get(0), HmcDoctorInfoVO.class));
            }
            // 2、判断医生是否开启问诊服务项
            contentDetailVO.setCheckDoctorDiagnosis(doctorApi.checkDoctorDiagnosis(contentDetailVO.getDocId().intValue()));
        }

        // 3、如果用户已经登录 -> 获取点赞数量
        if (Objects.nonNull(currentUser) && currentUser.getCurrentUserId() > 0) {
            Integer likeStatus = contentApi.getLikeStatus(currentUser.getCurrentUserId(), id);
            contentDetailVO.setLikeStatus(likeStatus);
        }

        if (StrUtil.isNotEmpty(contentDetail.getVedioFileUrl())) {
            contentDetailVO.setVedioFileUrl(fileService.getUrl(contentDetail.getVedioFileUrl(), FileTypeEnum.VEDIO_CONTENT));
        }

        if (StrUtil.isNotEmpty(contentDetail.getCover())) {
            contentDetailVO.setCover(fileService.getUrl(contentDetail.getCover(), FileTypeEnum.CONTENT_COVER));
        }

        // 获取QA数量
        Integer qaCount = qaApi.getQaCountByContentId(contentDetail.getContentId());
        contentDetailVO.setQaCount(qaCount);

        return Result.success(contentDetailVO);
    }

    @ApiOperation("点赞")
    @PostMapping("likeContent")
    public Result<Long> likeContent(@CurrentUser CurrentUserInfo currentUser, @RequestBody LikeContentForm likeContentForm) {
        LikeContentRequest request = PojoUtils.map(likeContentForm, LikeContentRequest.class);
        request.setOpUserId(currentUser.getCurrentUserId());
        request.setLineType(LineEnum.HMC.getCode());
        return Result.success(contentApi.likeContent(request));
    }
}