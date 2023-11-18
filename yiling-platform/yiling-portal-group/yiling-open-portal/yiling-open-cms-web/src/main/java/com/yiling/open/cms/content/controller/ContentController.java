package com.yiling.open.cms.content.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.cms.content.api.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.cms.collect.api.MyCollectApi;
import com.yiling.cms.collect.dto.MyCollectDTO;
import com.yiling.cms.collect.dto.request.QueryCollectRequest;
import com.yiling.cms.collect.enums.CollectStatusEnums;
import com.yiling.cms.common.enums.BusinessLineEnum;
import com.yiling.cms.content.dto.AppContentDTO;
import com.yiling.cms.content.dto.AppContentDetailDTO;
import com.yiling.cms.content.dto.CategoryDTO;
import com.yiling.cms.content.dto.ContentDTO;
import com.yiling.cms.content.dto.IHDocContentDTO;
import com.yiling.cms.content.dto.IHPatientContentDTO;
import com.yiling.cms.content.dto.IHPatientContentPageDTO;
import com.yiling.cms.content.dto.IHPatientHomeContentPageDTO;
import com.yiling.cms.content.dto.request.AddIHPatientContentRequest;
import com.yiling.cms.content.dto.request.LikeContentRequest;
import com.yiling.cms.content.dto.request.QueryAppContentPageRequest;
import com.yiling.cms.content.dto.request.QueryIHPatientContentPageRequest;
import com.yiling.cms.content.dto.request.UpdateContentCategoryRankRequest;
import com.yiling.cms.content.dto.request.UpdateContentRequest;
import com.yiling.cms.content.dto.request.UpdateIHPatientContentRequest;
import com.yiling.cms.content.enums.LineEnum;
import com.yiling.cms.read.api.MyReadApi;
import com.yiling.cms.read.dto.request.AddMyReadRequest;
import com.yiling.cms.read.enums.ReadTypeEnums;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.open.cms.content.form.AddContentForm;
import com.yiling.open.cms.content.form.GetContentForm;
import com.yiling.open.cms.content.form.PatientLikeContentForm;
import com.yiling.open.cms.content.form.PublishContentForm;
import com.yiling.open.cms.content.form.QueryContentPageForm;
import com.yiling.open.cms.content.form.QueryIHPatientContentPageForm;
import com.yiling.open.cms.content.form.UpdateContentCategoryRankForm;
import com.yiling.open.cms.content.form.UpdateContentForm;
import com.yiling.open.cms.content.vo.ContentDetailVO;
import com.yiling.open.cms.content.vo.ContentVO;
import com.yiling.open.cms.content.vo.IHPatientContentCategoryVO;
import com.yiling.open.cms.content.vo.PatientAdminContentVO;
import com.yiling.open.cms.content.vo.PatientContentDetailVO;
import com.yiling.open.cms.content.vo.QueryIHPatientContentPageVO;
import com.yiling.open.cms.goods.vo.StandardGoodsInfoVO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 内容
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
    IHDocContentApi ihDocContentApi;

    @DubboReference(async = true)
    MyReadApi myReadApi;
    @Autowired
    private FileService fileService;

    @DubboReference
    MyCollectApi myCollectApi;

    @DubboReference
    IHPatientContentApi ihPatientContentApi;

    @DubboReference
    CategoryApi categoryApi;

    @DubboReference
    StandardGoodsApi standardGoodsApi;

    @DubboReference
    QaApi qaApi;


    @ApiOperation("列表")
    @GetMapping("queryContentPage")
    public Result<Page<ContentVO>> queryContentPage(@Valid QueryContentPageForm queryContentPageForm) {
        QueryAppContentPageRequest request = new QueryAppContentPageRequest();
        PojoUtils.map(queryContentPageForm, request);
        // Page<AppContentDTO> contentDTOPage = contentApi.listAppContentPage(request);
        Page<AppContentDTO> contentDTOPage = ihDocContentApi.listAppContentPageBySql(request);
        Page<ContentVO> contentVOPage = PojoUtils.map(contentDTOPage, ContentVO.class);
        if (contentVOPage.getTotal() == 0) {
            return Result.success(contentVOPage);
        }
        contentVOPage.getRecords().forEach(contentVO -> {
            if(StrUtil.isNotEmpty(contentVO.getCover())){
                contentVO.setCover(fileService.getUrl(contentVO.getCover(),FileTypeEnum.CONTENT_COVER));
            }
            if(StrUtil.isNotEmpty(contentVO.getVedioFileUrl())){
                contentVO.setVedioFileUrl(fileService.getUrl(contentVO.getVedioFileUrl(), FileTypeEnum.VEDIO_CONTENT));
            }
        });
        return Result.success(contentVOPage);
    }

    @ApiOperation("详情")
    @GetMapping("getContentDetail")
    public Result<ContentDetailVO>  getContentDetail(@Valid GetContentForm form) {
        IHDocContentDTO ihDocContentDTO = ihDocContentApi.getIhDocContentById(form.getId());
        AppContentDetailDTO contentDetail = contentApi.getContentDetail(form.getId(), LineEnum.IH_DOC);
        ContentDetailVO contentDetailVO = new ContentDetailVO();
        PojoUtils.map(contentDetail, contentDetailVO);
        contentDetailVO.setViewLimit(ihDocContentDTO.getContentAuth());
        contentDetailVO.setId(ihDocContentDTO.getId());
        contentDetailVO.setContentId(contentDetail.getContentId());
        if (Objects.nonNull(form.getWxDoctorId())) {
            //我的阅读插入数据
            AddMyReadRequest readRequest = new AddMyReadRequest();
            readRequest.setContentTime(contentDetail.getCreateTime()).setSource(BusinessLineEnum.DOCTOR.getCode()).setReadId(contentDetail.getId()).setTitle(contentDetail.getTitle()).setOpTime(new Date());
            readRequest.setOpUserId(form.getWxDoctorId());
            readRequest.setCmsId(form.getId());
            //收藏状态
            QueryCollectRequest request = new QueryCollectRequest();
            request.setCollectId(contentDetail.getContentId()).setSource(BusinessLineEnum.DOCTOR.getCode()).setOpUserId(form.getWxDoctorId());
            if (StrUtil.isNotEmpty(contentDetail.getVedioFileUrl())) {
                request.setCollectType(ReadTypeEnums.VIDEO.getCode());
            } else {
                request.setCollectType(ReadTypeEnums.ARTICLE.getCode());
            }
            MyCollectDTO myCollectDTO = myCollectApi.getOne(request);
            if (Objects.isNull(myCollectDTO) || null == myCollectDTO.getStatus()) {
                contentDetailVO.setCollectStatus(CollectStatusEnums.UN_COLLECTED.getCode());
            } else {
                contentDetailVO.setCollectStatus(myCollectDTO.getStatus());
            }
            if (StrUtil.isNotEmpty(contentDetailVO.getVedioFileUrl())) {
                readRequest.setReadType(ReadTypeEnums.VIDEO.getCode());
            } else {
                readRequest.setReadType(ReadTypeEnums.ARTICLE.getCode());
            }
            myReadApi.save(readRequest);
            DubboUtils.quickAsyncCall("myReadApi", "save");
        }

        if (StrUtil.isNotEmpty(contentDetailVO.getCover())) {
            contentDetailVO.setCover(fileService.getUrl(contentDetailVO.getCover(), FileTypeEnum.CONTENT_COVER));
        }
        if (StrUtil.isNotEmpty(contentDetailVO.getVedioFileUrl())) {
            contentDetailVO.setVedioFileUrl(fileService.getUrl(contentDetailVO.getVedioFileUrl(), FileTypeEnum.VEDIO_CONTENT));
        }

        // 获取QA数量
        Integer qaCount = qaApi.getQaCountByContentId(contentDetail.getContentId());
        contentDetailVO.setQaCount(qaCount);

        return Result.success(contentDetailVO);
    }

    @Log(title = "IH管理后台添加内容", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("添加")
    @PostMapping("addContent")
    public Result<Boolean> addContent(@RequestBody @Valid AddContentForm form) {
        AddIHPatientContentRequest request = PojoUtils.map(form, AddIHPatientContentRequest.class);
        ihPatientContentApi.addIHPatientContent(request);
        return Result.success(true);
    }

    @Log(title = "IH管理后台修改内容", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改")
    @PostMapping("updateContent")
    public Result<Boolean> updateContent(@RequestBody @Valid UpdateContentForm form) {
        UpdateIHPatientContentRequest request = PojoUtils.map(form, UpdateIHPatientContentRequest.class);
        ihPatientContentApi.updateIHPatientContent(request);
        return Result.success(true);
    }

    @Log(title = "IH管理后台修改栏目排序", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改栏目排序")
    @PostMapping("updateContentCategoryRank")
    public Result<Boolean> updateContentCategoryRank(@RequestBody @Valid UpdateContentCategoryRankForm form) {
        UpdateContentCategoryRankRequest request = PojoUtils.map(form, UpdateContentCategoryRankRequest.class);
        ihPatientContentApi.updateContentCategoryRank(request);
        return Result.success(true);
    }

    @Log(title = "H管理后台文章发布和取消", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("发布/取消发布")
    @PostMapping("publishContent")
    public Result<Boolean> publishContent(@RequestBody @Valid PublishContentForm form) {
        UpdateContentRequest request = PojoUtils.map(form, UpdateContentRequest.class);
        contentApi.updateContent(request);
        return Result.success(true);
    }

    @ApiOperation("患者文章列表")
    @PostMapping("queryIHPatientContentPage")
    public Result<Page<QueryIHPatientContentPageVO>> queryIHPatientContentPage(@RequestBody @Valid QueryIHPatientContentPageForm form) {
        QueryIHPatientContentPageRequest request = PojoUtils.map(form, QueryIHPatientContentPageRequest.class);
        Page<IHPatientContentPageDTO> page = ihPatientContentApi.listContentPageBySql(request);
        if (page.getTotal() == 0) {
            return Result.success(request.getPage());
        }
        Page<QueryIHPatientContentPageVO> voPage = PojoUtils.map(page, QueryIHPatientContentPageVO.class);
        List<QueryIHPatientContentPageVO> records = voPage.getRecords();
        List<Long> ids = records.stream().map(e -> e.getId()).distinct().collect(Collectors.toList());
        List<IHPatientContentDTO> list = ihPatientContentApi.getIHPatientContentByContentIds(ids);
        Map<Long, List<IHPatientContentDTO>> dtoMap = list.stream().collect(Collectors.groupingBy(IHPatientContentDTO::getContentId));
        records.forEach(vo -> {
            if (StrUtil.isNotEmpty(vo.getCover())) {
                vo.setCover(fileService.getUrl(vo.getCover(), FileTypeEnum.CONTENT_COVER));
            }
            StringBuffer buffer = new StringBuffer();
            Integer clickNum = 0;
            List<IHPatientContentCategoryVO> categoryList = PojoUtils.map(dtoMap.get(vo.getId()), IHPatientContentCategoryVO.class);
            categoryList = categoryList.stream().sorted(Comparator.comparing(IHPatientContentCategoryVO::getModuleId).reversed()).collect(Collectors.toList());
            for (IHPatientContentCategoryVO ihPatientContentCategoryVO : categoryList) {
                CategoryDTO category = categoryApi.getCategoryById(ihPatientContentCategoryVO.getCategoryId());
                ihPatientContentCategoryVO.setCategoryName(category.getCategoryName());
                if (ihPatientContentCategoryVO.getCategoryRank().equals(0)) {
                    buffer.append("默认;");
                } else {
                    buffer.append(ihPatientContentCategoryVO.getCategoryRank() + ";");
                }
                clickNum += ihPatientContentCategoryVO.getView();
            }
            vo.setCategoryList(categoryList);
            vo.setCategoryRank(buffer.toString());
            vo.setClickNum(clickNum);
        });

        return Result.success(voPage);
    }

    @ApiOperation("患者首页文章列表")
    @PostMapping("queryIHPatientHomeContentPage")
    public Result<Page<QueryIHPatientContentPageVO>> queryIHPatientHomeContentPage(@RequestBody @Valid QueryIHPatientContentPageForm form) {
        QueryIHPatientContentPageRequest request = PojoUtils.map(form, QueryIHPatientContentPageRequest.class);
        Page<IHPatientHomeContentPageDTO> page = ihPatientContentApi.homeListContentPageBySql(request);
        if (page.getTotal() == 0) {
            return Result.success(request.getPage());
        }
        List<QueryIHPatientContentPageVO> list = new ArrayList<>();
        page.getRecords().forEach(dto -> {
            List<IHPatientContentCategoryVO> categoryList = new ArrayList();
            IHPatientContentCategoryVO categoryVO = new IHPatientContentCategoryVO();
            QueryIHPatientContentPageVO vo = PojoUtils.map(dto, QueryIHPatientContentPageVO.class);
            Long categoryId = dto.getCategoryId();
            CategoryDTO category = categoryApi.getCategoryById(categoryId);
            if (Objects.nonNull(category)) {
                categoryVO.setCategoryName(category.getCategoryName());
            }
            categoryVO.setCategoryId(categoryId);
            categoryVO.setModuleId(dto.getModuleId());
            categoryList.add(categoryVO);
            vo.setCategoryList(categoryList);
            list.add(vo);
            if (StringUtils.isNotBlank(vo.getCover())) {
                vo.setCover(fileService.getUrl(vo.getCover(), FileTypeEnum.CONTENT_COVER));
            }
        });
        Page<QueryIHPatientContentPageVO> queryIHPatientContentPageVOPage = new Page<>();
        queryIHPatientContentPageVOPage.setRecords(list);
        queryIHPatientContentPageVOPage.setTotal(page.getTotal());
        return Result.success(queryIHPatientContentPageVOPage);
    }

    @ApiOperation("患者文章点赞")
    @PostMapping("patientLikeContent")
    public Result<Long> patientLikeContent(@RequestBody @Valid PatientLikeContentForm form) {
        LikeContentRequest request = new LikeContentRequest();
        request.setId(form.getId());
        request.setLikeFlag(form.getLikeFlag());
        request.setOpUserId(form.getOpUserId());
        request.setLineType(LineEnum.IH_PATIENT.getCode());
        Long likeCount = contentApi.likeContent(request);
        return Result.success(likeCount);
    }

    @ApiOperation("患者文章详情")
    @GetMapping("getPatientContent")
    public Result<PatientContentDetailVO> getPatientContent(@RequestParam("id") Long id, @RequestParam(value = "categoryId", required = false) Long categoryId, @RequestParam(value = "moduleId", required = false) Long moduleId, @RequestParam("userId") Long userId) {
        ContentDTO contentDTO = ihPatientContentApi.getPatientContent(id, categoryId, moduleId);
        PatientContentDetailVO vo = PojoUtils.map(contentDTO, PatientContentDetailVO.class);
        Integer likeStatus = contentApi.getLikeStatus(userId, id);
        vo.setLikeStatus(likeStatus);
        return Result.success(vo);
    }

    @ApiOperation("后台文章详情")
    @GetMapping("getAdminContentById")
    public Result<PatientAdminContentVO> getContentById(@RequestParam("id") Long id) {
        //IHPatientContentDTO ihPatientContent = ihPatientContentApi.getIhPatientContentById(id);
        ContentDTO content = contentApi.getContentById(id);
        PatientAdminContentVO contentVO = PojoUtils.map(content, PatientAdminContentVO.class);
        contentVO.setCoverKey(contentVO.getCover());
        if (StrUtil.isNotEmpty(contentVO.getCover())) {
            contentVO.setCover(fileService.getUrl(contentVO.getCover(), FileTypeEnum.CONTENT_COVER));
        }

        List<Long> standardGoodsIdList = content.getStandardGoodsIdList();
        if (CollUtil.isNotEmpty(standardGoodsIdList)) {
            List<StandardGoodsInfoVO> standardGoodsList = Lists.newArrayListWithExpectedSize(standardGoodsIdList.size());
            standardGoodsIdList.forEach(standardGoodsId -> {
                StandardGoodsInfoVO goodsInfoVO = new StandardGoodsInfoVO();
                goodsInfoVO.setId(standardGoodsId);
                StandardGoodsAllInfoDTO standardGoods = standardGoodsApi.getStandardGoodsById(standardGoodsId);
                goodsInfoVO.setName(Optional.ofNullable(standardGoods).get().getBaseInfo().getName());
                standardGoodsList.add(goodsInfoVO);
            });
            contentVO.setStandardGoodsList(standardGoodsList);
        }
        List<IHPatientContentDTO> patientContentDTOS = ihPatientContentApi.getContentByContentIdList(new ArrayList<Long>() {{
            add(id);
        }});
        if (CollectionUtils.isNotEmpty(patientContentDTOS)) {
            List<IHPatientContentCategoryVO> categoryVOS = PojoUtils.map(patientContentDTOS, IHPatientContentCategoryVO.class);
            for (IHPatientContentCategoryVO categoryVO : categoryVOS) {
                CategoryDTO categoryById = categoryApi.getCategoryById(categoryVO.getCategoryId());
                if (Objects.nonNull(categoryById)) {
                    categoryVO.setCategoryName(categoryById.getCategoryName());
                }
            }
            contentVO.setCategoryList(categoryVOS);
        }
        return Result.success(contentVO);
    }
}