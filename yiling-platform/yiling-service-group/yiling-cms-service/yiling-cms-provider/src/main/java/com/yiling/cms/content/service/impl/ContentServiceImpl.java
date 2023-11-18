package com.yiling.cms.content.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.cms.content.constants.CmsConstants;
import com.yiling.cms.content.dao.ContentMapper;
import com.yiling.cms.content.dto.AppContentDTO;
import com.yiling.cms.content.dto.AppContentDetailDTO;
import com.yiling.cms.content.dto.ContentDTO;
import com.yiling.cms.content.dto.DraftDTO;
import com.yiling.cms.content.dto.request.*;
import com.yiling.cms.content.entity.*;
import com.yiling.cms.content.enums.CmsErrorCode;
import com.yiling.cms.content.enums.ContentStatusEnum;
import com.yiling.cms.content.enums.ContentTypeEnum;
import com.yiling.cms.content.enums.LineEnum;
import com.yiling.cms.content.service.*;
import com.yiling.cms.meeting.entity.MeetingDO;
import com.yiling.cms.meeting.service.MeetingService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 内容 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
@Service
@Slf4j
public class ContentServiceImpl extends BaseServiceImpl<ContentMapper, ContentDO> implements ContentService {
    @Autowired
    private ContentDisplayLineService contentDisplayLineService;

    @Autowired
    private HmcContentService hmcContentService;

    @Autowired
    private IHDocContentService ihDocContentService;

    @Autowired
    private IHPatientContentService ihPatientContentService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ContentGoodsService contentGoodsService;

    @Autowired
    private ContentDeptService contentDeptService;

    @Autowired
    private ContentDiseaseService contentDiseaseService;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private SaContentService saContentService;

    @Autowired
    private B2bContentService b2bContentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addContent(AddContentRequest addContentRequest) {
        ContentDO contentDO = new ContentDO();
        PojoUtils.map(addContentRequest, contentDO);
        if (addContentRequest.getStatus().equals(ContentStatusEnum.PUBLISHED.getCode())) {
            contentDO.setPublishTime(DateUtil.date());
        }
        this.save(contentDO);
        // List<ContentDisplayLineDO> categoryDisplayLineDOList = Lists.newArrayList();
        // addContentRequest.getLineModuleList().forEach(line -> {
        //     line.getModuleList().forEach(module -> {
        //         module.getModuleCategoryList().forEach(category -> {
        //             ContentDisplayLineDO contentDisplayLineDO = new ContentDisplayLineDO();
        //             contentDisplayLineDO.setContentId(contentDO.getId());
        //             contentDisplayLineDO.setLineId(line.getLineId());
        //             contentDisplayLineDO.setLineName(line.getLineName());
        //             contentDisplayLineDO.setCreateUser(addContentRequest.getOpUserId());
        //             contentDisplayLineDO.setModuleId(module.getModuleId());
        //             contentDisplayLineDO.setModuleName(module.getModuleName());
        //             contentDisplayLineDO.setCategoryId(category.getCategoryId());
        //             contentDisplayLineDO.setCategoryName(category.getCategoryName());
        //             contentDisplayLineDO.setChoseFlag(line.getChoseFlag());
        //             categoryDisplayLineDOList.add(contentDisplayLineDO);
        //         });
        //     });
        // });
        // contentDisplayLineService.saveBatch(categoryDisplayLineDOList);
        //关联药品
        List<ContentGoodsDO> contentGoodsDOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(addContentRequest.getStandardGoodsIdList())) {
            addContentRequest.getStandardGoodsIdList().forEach(standardGoodsId -> {
                ContentGoodsDO contentGoodsDO = new ContentGoodsDO();
                contentGoodsDO.setContentId(contentDO.getId()).setCreateUser(addContentRequest.getOpUserId()).setStandardGoodsId(standardGoodsId);
                contentGoodsDOList.add(contentGoodsDO);
            });
            contentGoodsService.saveBatch(contentGoodsDOList);
        }
        //关联疾病
        List<ContentDiseaseDO> contentDiseaseDOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(addContentRequest.getDiseaseIdList())) {
            addContentRequest.getDiseaseIdList().forEach(diseaseId -> {
                ContentDiseaseDO contentDiseaseDO = new ContentDiseaseDO();
                contentDiseaseDO.setContentId(contentDO.getId()).setCreateUser(addContentRequest.getOpUserId()).setDiseaseId(diseaseId);
                contentDiseaseDOList.add(contentDiseaseDO);
            });
            contentDiseaseService.saveBatch(contentDiseaseDOList);
        }
        //关联科室
        List<ContentDeptDO> contentDeptDOS = Lists.newArrayList();
        if (CollUtil.isNotEmpty(addContentRequest.getDeptIdList())) {
            addContentRequest.getDeptIdList().forEach(deptId -> {
                ContentDeptDO contentDeptDO = new ContentDeptDO();
                contentDeptDO.setContentId(contentDO.getId()).setCreateUser(addContentRequest.getOpUserId()).setDeptId(deptId);
                contentDeptDOS.add(contentDeptDO);
            });
            contentDeptService.saveBatch(contentDeptDOS);
        }
        return contentDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContent(UpdateContentRequest updateContentRequest) {
        ContentDO contentDO = new ContentDO();
        PojoUtils.map(updateContentRequest, contentDO);
        // 发布时间处理-只第一次发布的时候填充发布时间
        if (updateContentRequest.getStatus().equals(ContentStatusEnum.PUBLISHED.getCode())) {
            ContentDO oldContent = this.getById(updateContentRequest.getId());
            if (oldContent.getPublishTime().compareTo(DateUtil.parse("1970-01-01 00:00:00")) == 0) {
                contentDO.setPublishTime(DateUtil.date());
            }
        }
        //草稿状态转变更新创建时间
        if (Objects.nonNull(updateContentRequest.getIsDraft()) && updateContentRequest.getIsDraft() == 0) {
            ContentDO oldContent = this.getById(updateContentRequest.getId());
            if (!oldContent.getIsDraft().equals(updateContentRequest.getIsDraft())) {
                contentDO.setCreateTime(new Date());
            }
        }
        this.updateById(contentDO);
        String key = String.format(CmsConstants.CONTENT_KEY, contentDO.getId());
        redisService.del(key);
        // if (CollUtil.isEmpty(updateContentRequest.getDisplayLines())) {
        //     return;
        // }
        //
        // LambdaQueryWrapper<ContentDisplayLineDO> wrapper = Wrappers.lambdaQuery();
        // wrapper.eq(ContentDisplayLineDO::getContentId, updateContentRequest.getId());
        // List<ContentDisplayLineDO> list = contentDisplayLineService.list(wrapper);
        // List<Long> lineList = list.stream().map(ContentDisplayLineDO::getId).collect(Collectors.toList());
        // contentDisplayLineService.removeByIds(lineList);

        // List<ContentDisplayLineDO> categoryDisplayLineDOList = Lists.newArrayList();
        // updateContentRequest.getLineModuleList().forEach(line -> {
        //     line.getModuleList().forEach(module -> {
        //         module.getModuleCategoryList().forEach(category -> {
        //             ContentDisplayLineDO contentDisplayLineDO = new ContentDisplayLineDO();
        //             contentDisplayLineDO.setContentId(contentDO.getId());
        //             contentDisplayLineDO.setLineId(line.getLineId());
        //             contentDisplayLineDO.setLineName(line.getLineName());
        //             contentDisplayLineDO.setCreateUser(updateContentRequest.getOpUserId());
        //             contentDisplayLineDO.setModuleId(module.getModuleId());
        //             contentDisplayLineDO.setModuleName(module.getModuleName());
        //             contentDisplayLineDO.setCategoryId(category.getCategoryId());
        //             contentDisplayLineDO.setCategoryName(category.getCategoryName());
        //             categoryDisplayLineDOList.add(contentDisplayLineDO);
        //         });
        //     });
        // });
        // contentDisplayLineService.saveBatch(categoryDisplayLineDOList);

        // List<Long> lines = list.stream().map(ContentDisplayLineDO::getLineId).collect(Collectors.toList());
        // List<Long> intersection = CollUtil.intersection(lines, updateContentRequest.getDisplayLines()).stream().collect(Collectors.toList());
        // if (intersection.size() != lines.size() || updateContentRequest.getDisplayLines().size() != lines.size()) {
        //     List<Long> lineList = list.stream().map(ContentDisplayLineDO::getId).collect(Collectors.toList());
        //     contentDisplayLineService.removeByIds(lineList);
        //     List<ContentDisplayLineDO> categoryDisplayLineDOList = Lists.newArrayList();
        //     updateContentRequest.getDisplayLines().forEach(lineId -> {
        //         ContentDisplayLineDO contentDisplayLineDO = new ContentDisplayLineDO();
        //         contentDisplayLineDO.setContentId(contentDO.getId()).setLineId(lineId);
        //         contentDisplayLineDO.setCreateUser(updateContentRequest.getOpUserId());
        //         categoryDisplayLineDOList.add(contentDisplayLineDO);
        //     });
        //     contentDisplayLineService.saveBatch(categoryDisplayLineDOList);
        // }
        this.updateGoodsRelation(updateContentRequest);
        this.updateDeptRelation(updateContentRequest);
        this.updateDiseaseRelation(updateContentRequest);

    }

    /**
     * 更新关联商品
     *
     * @param updateContentRequest
     */
    private void updateGoodsRelation(UpdateContentRequest updateContentRequest) {
        LambdaQueryWrapper<ContentGoodsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ContentGoodsDO::getContentId, updateContentRequest.getId()).select(ContentGoodsDO::getId, ContentGoodsDO::getStandardGoodsId);

        List<ContentGoodsDO> list = contentGoodsService.list(wrapper);
        List<Long> goodsIds = list.stream().map(ContentGoodsDO::getStandardGoodsId).collect(Collectors.toList());
        List<Long> intersection = CollUtil.intersection(goodsIds, updateContentRequest.getStandardGoodsIdList()).stream().collect(Collectors.toList());
        if (CollUtil.isNotEmpty(updateContentRequest.getStandardGoodsIdList()) && updateContentRequest.getStandardGoodsIdList().size() == goodsIds.size() && intersection.size() == goodsIds.size()) {
            return;
        }
        List<Long> lineList = list.stream().map(ContentGoodsDO::getId).collect(Collectors.toList());
        contentGoodsService.removeByIds(lineList);
        if (CollUtil.isEmpty(updateContentRequest.getStandardGoodsIdList())) {
            return;
        }
        List<ContentGoodsDO> contentGoodsDOList = Lists.newArrayList();
        updateContentRequest.getStandardGoodsIdList().forEach(standardGoodsId -> {
            ContentGoodsDO contentGoodsDO = new ContentGoodsDO();
            contentGoodsDO.setContentId(updateContentRequest.getId()).setStandardGoodsId(standardGoodsId);
            contentGoodsDO.setCreateUser(updateContentRequest.getOpUserId());
            contentGoodsDOList.add(contentGoodsDO);
        });
        contentGoodsService.saveBatch(contentGoodsDOList);
    }

    /**
     * 更新关联科室
     *
     * @param updateContentRequest
     */
    private void updateDeptRelation(UpdateContentRequest updateContentRequest) {
        LambdaQueryWrapper<ContentDeptDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ContentDeptDO::getContentId, updateContentRequest.getId()).select(ContentDeptDO::getId, ContentDeptDO::getDeptId);
        List<ContentDeptDO> list = contentDeptService.list(wrapper);
        List<Integer> deptIds = list.stream().map(ContentDeptDO::getDeptId).collect(Collectors.toList());
        List<Integer> intersection = CollUtil.intersection(deptIds, updateContentRequest.getDeptIdList()).stream().collect(Collectors.toList());
        if (intersection.size() == deptIds.size() && CollUtil.isNotEmpty(updateContentRequest.getDeptIdList()) && updateContentRequest.getDeptIdList().size() == deptIds.size()) {
            return;
        }
        List<Long> lineList = list.stream().map(ContentDeptDO::getId).collect(Collectors.toList());
        contentDeptService.removeByIds(lineList);
        if (CollUtil.isEmpty(updateContentRequest.getDeptIdList())) {
            return;
        }
        List<ContentDeptDO> contentGoodsDOList = Lists.newArrayList();
        updateContentRequest.getDeptIdList().forEach(deptId -> {
            ContentDeptDO contentDeptDO = new ContentDeptDO();
            contentDeptDO.setContentId(updateContentRequest.getId()).setDeptId(deptId);
            contentDeptDO.setCreateUser(updateContentRequest.getOpUserId());
            contentGoodsDOList.add(contentDeptDO);
        });
        contentDeptService.saveBatch(contentGoodsDOList);
    }

    private void updateDiseaseRelation(UpdateContentRequest updateContentRequest) {
        LambdaQueryWrapper<ContentDiseaseDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ContentDiseaseDO::getContentId, updateContentRequest.getId()).select(ContentDiseaseDO::getId, ContentDiseaseDO::getDiseaseId);
        List<ContentDiseaseDO> list = contentDiseaseService.list(wrapper);
        List<Integer> diseaseIds = list.stream().map(ContentDiseaseDO::getDiseaseId).collect(Collectors.toList());
        List<Integer> intersection = CollUtil.intersection(diseaseIds, updateContentRequest.getDiseaseIdList()).stream().collect(Collectors.toList());
        if (intersection.size() == diseaseIds.size() && CollUtil.isNotEmpty(updateContentRequest.getDiseaseIdList()) && updateContentRequest.getDiseaseIdList().size() == diseaseIds.size()) {
            return;
        }
        List<Long> lineList = list.stream().map(ContentDiseaseDO::getId).collect(Collectors.toList());
        contentDiseaseService.removeByIds(lineList);
        if (CollUtil.isEmpty(updateContentRequest.getDiseaseIdList())) {
            return;
        }
        List<ContentDiseaseDO> contentGoodsDOList = Lists.newArrayList();
        updateContentRequest.getDiseaseIdList().forEach(deptId -> {
            ContentDiseaseDO contentDiseaseDO = new ContentDiseaseDO();
            contentDiseaseDO.setContentId(updateContentRequest.getId()).setDiseaseId(deptId);
            contentDiseaseDO.setCreateUser(updateContentRequest.getOpUserId());
            contentGoodsDOList.add(contentDiseaseDO);
        });
        contentDiseaseService.saveBatch(contentGoodsDOList);
    }

    @Override
    public Page<ContentDTO> listPage(QueryContentPageRequest request) {

        if (Objects.nonNull(request.getStartTime())) {
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }

        if (Objects.nonNull(request.getEndTime())) {
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
        }

        Page<ContentDO> page = this.getBaseMapper().listPage(request.getPage(), request);
        if (page.getTotal() == 0 || CollUtil.isEmpty(page.getRecords())) {
            log.warn("[listPage]未获取到数据，跳过处理...");
            return new Page<>();
        }
        log.info("[listPage]获取到结果集:{}", JSONUtil.toJsonStr(page));

        List<Long> contentIdList = page.getRecords().stream().map(ContentDO::getId).collect(Collectors.toList());

        List<HmcContentDO> hmcContentDOList = hmcContentService.listByContentIdList(contentIdList);
        Map<Long, List<HmcContentDO>> hmcContentDOMap = hmcContentDOList.stream().collect(Collectors.groupingBy(HmcContentDO::getContentId));

        List<IHDoctorContentDO> ihDoctorContentList = ihDocContentService.listByContentIdList(contentIdList);
        Map<Long, List<IHDoctorContentDO>> ihDoctorContentDOMap = ihDoctorContentList.stream().collect(Collectors.groupingBy(IHDoctorContentDO::getContentId));

        List<IHPatientContentDO> ihPatientContentList = ihPatientContentService.listByContentIdList(contentIdList);
        Map<Long, List<IHPatientContentDO>> ihPatientContentDOMap = ihPatientContentList.stream().collect(Collectors.groupingBy(IHPatientContentDO::getContentId));

        List<SaContentDO> saContentDOList = saContentService.listByContentIdList(contentIdList);
        Map<Long, List<SaContentDO>> saContentDOMap = saContentDOList.stream().collect(Collectors.groupingBy(SaContentDO::getContentId));

        List<B2bContentDO> b2bContentDOList = b2bContentService.listByContentIdList(contentIdList);
        Map<Long, List<B2bContentDO>> b2bContentDOMap = b2bContentDOList.stream().collect(Collectors.groupingBy(B2bContentDO::getContentId));

        List<ContentDTO> contentDTOList = PojoUtils.map(page.getRecords(), ContentDTO.class);
        contentDTOList.forEach(contentDTO -> {
            List<Long> displayLines = Lists.newArrayList();
            if (hmcContentDOMap.containsKey(contentDTO.getId())) {
                displayLines.add(Long.valueOf(LineEnum.HMC.getCode()));
                contentDTO.setHmcView(hmcContentDOMap.get(contentDTO.getId()).stream().map(HmcContentDO::getView).reduce(Integer::sum).get());
            }
            if (ihDoctorContentDOMap.containsKey(contentDTO.getId())) {
                displayLines.add(Long.valueOf(LineEnum.IH_DOC.getCode()));
                contentDTO.setIhDocView(ihDoctorContentDOMap.get(contentDTO.getId()).stream().map(IHDoctorContentDO::getView).reduce(Integer::sum).get());
            }
            if (ihPatientContentDOMap.containsKey(contentDTO.getId())) {
                displayLines.add(Long.valueOf(LineEnum.IH_PATIENT.getCode()));
                contentDTO.setIhPatientView(ihPatientContentDOMap.get(contentDTO.getId()).stream().map(IHPatientContentDO::getView).reduce(Integer::sum).get());
            }
            if (saContentDOMap.containsKey(contentDTO.getId())) {
                displayLines.add(Long.valueOf(LineEnum.SA.getCode()));
                contentDTO.setSaView(saContentDOMap.get(contentDTO.getId()).stream().map(SaContentDO::getView).reduce(Integer::sum).get());
            }
            if (b2bContentDOMap.containsKey(contentDTO.getId())) {
                displayLines.add(Long.valueOf(LineEnum.B2B.getCode()));
                contentDTO.setB2bView(b2bContentDOMap.get(contentDTO.getId()).stream().map(B2bContentDO::getView).reduce(Integer::sum).get());
            }

            contentDTO.setPageView((Objects.nonNull(contentDTO.getHmcView()) ? contentDTO.getHmcView() : 0)
                    + (Objects.nonNull(contentDTO.getIhDocView()) ? contentDTO.getIhDocView() : 0)
                    + (Objects.nonNull(contentDTO.getIhPatientView()) ? contentDTO.getIhPatientView() : 0)
                    + (Objects.nonNull(contentDTO.getSaView()) ? contentDTO.getSaView() : 0)
                    + (Objects.nonNull(contentDTO.getB2bView()) ? contentDTO.getB2bView() : 0)
            );

            contentDTO.setDisplayLines(displayLines);
        });

        Page<ContentDTO> contentDTOPage = new Page<>(page.getCurrent(), page.getSize());
        contentDTOPage.setRecords(contentDTOList);
        contentDTOPage.setTotal(page.getTotal());
        log.info("[listPage]返回结果集:{}", JSONUtil.toJsonStr(contentDTOPage));
        return contentDTOPage;


        // LambdaQueryWrapper<ContentDO> wrapper = Wrappers.lambdaQuery();
        // if (Objects.nonNull(request.getLineId()) && request.getLineId().equals(1L)) {
        //     LambdaQueryWrapper<HmcContentDO> hmcWrapper = Wrappers.lambdaQuery();
        //     hmcWrapper.eq(HmcContentDO::getLineId, request.getLineId()).select(HmcContentDO::getContentId);
        //     List<Object> lineList = hmcContentService.listObjs(hmcWrapper);
        //     if (CollUtil.isEmpty(lineList)) {
        //         return new Page<>();
        //     }
        //     List<Long> longs = PojoUtils.map(lineList, Long.class);
        //     longs = longs.stream().distinct().collect(Collectors.toList());
        //     wrapper.in(ContentDO::getId, longs);
        // }
        // wrapper.ge(Objects.nonNull(request.getStartTime()), ContentDO::getCreateTime, request.getStartTime());
        // wrapper.eq(Objects.nonNull(request.getIsTop()), ContentDO::getIsTop, request.getIsTop());
        // wrapper.eq(Objects.nonNull(request.getDocId()), ContentDO::getDocId, request.getDocId());
        // wrapper.eq(Objects.nonNull(request.getStatus()), ContentDO::getStatus, request.getStatus());
        //
        //
        // wrapper.like(StringUtils.isNotBlank(request.getTitle()), ContentDO::getTitle, request.getTitle());
        // wrapper.orderByDesc(ContentDO::getIsTop, ContentDO::getCreateTime);
        // wrapper.eq(ContentDO::getIsDraft, 0).eq(ContentDO::getContentType, request.getContentType());
        // Page<ContentDO> contentDOPage = this.page(request.getPage(), wrapper);
        // if (contentDOPage.getTotal() == 0) {
        //     return new Page<>();
        // }
        // List<ContentDO> contentDOS = contentDOPage.getRecords();
        // // List<Long> ids = contentDOS.stream().map(ContentDO::getId).collect(Collectors.toList());
        // // LambdaQueryWrapper<ContentDisplayLineDO> lineWrapper = Wrappers.lambdaQuery();
        // // lineWrapper.in(ContentDisplayLineDO::getContentId, ids);
        // // List<ContentDisplayLineDO> displayLineDOS = contentDisplayLineService.list(lineWrapper);
        // // Map<Long, List<ContentDisplayLineDO>> lineDOMap = displayLineDOS.stream().collect(Collectors.groupingBy(ContentDisplayLineDO::getContentId));
        // List<ContentDTO> contentDTOS = PojoUtils.map(contentDOS, ContentDTO.class);
        // contentDTOS.forEach(contentDTO -> {
        //     // List<ContentDisplayLineDO> contentDisplayLineDOS = lineDOMap.get(contentDTO.getId());
        //     // if (CollUtil.isNotEmpty(contentDisplayLineDOS)) {
        //     //     contentDTO.setDisplayLines(contentDisplayLineDOS.stream().map(ContentDisplayLineDO::getLineId).collect(Collectors.toList()));
        //     // }
        //     if (StringUtils.isNotEmpty(contentDTO.getCover())) {
        //         fileService.getUrl(contentDTO.getCover(), FileTypeEnum.CONTENT_COVER);
        //     }
        //     if (StringUtils.isNotEmpty(contentDTO.getVedioFileUrl())) {
        //         fileService.getUrl(contentDTO.getVedioFileUrl(), FileTypeEnum.VEDIO_CONTENT);
        //     }
        // });
        // Page<ContentDTO> contentDTOPage = new Page<>(contentDOPage.getCurrent(), contentDOPage.getSize());
        // contentDTOPage.setRecords(contentDTOS);
        // contentDTOPage.setTotal(contentDOPage.getTotal());
        // return contentDTOPage;
    }

    @Override
    public ContentDTO getContentById(Long id) {
        ContentDO contentDO = this.getById(id);
        if (Objects.isNull(contentDO)) {
            throw new BusinessException(ResultCode.FAILED, "内容不存在");
        }
        ContentDTO contentDTO = new ContentDTO();
        PojoUtils.map(contentDO, contentDTO);
        // LambdaQueryWrapper<ContentDisplayLineDO> lineWrapper = Wrappers.lambdaQuery();
        // lineWrapper.eq(ContentDisplayLineDO::getContentId, id);
        // List<ContentDisplayLineDO> displayLineDOS = contentDisplayLineService.list(lineWrapper);
        // contentDTO.setDisplayLines(displayLineDOS.stream().map(ContentDisplayLineDO::getLineId).collect(Collectors.toList()));
        LambdaQueryWrapper<ContentGoodsDO> goodsDOLambdaQueryWrapper = Wrappers.lambdaQuery();
        goodsDOLambdaQueryWrapper.eq(ContentGoodsDO::getContentId, id);
        List<ContentGoodsDO> contentGoodsDOList = contentGoodsService.list(goodsDOLambdaQueryWrapper);
        contentDTO.setStandardGoodsIdList(contentGoodsDOList.stream().map(ContentGoodsDO::getStandardGoodsId).collect(Collectors.toList()));
        LambdaQueryWrapper<ContentDeptDO> deptDOLambdaQueryWrapper = Wrappers.lambdaQuery();
        deptDOLambdaQueryWrapper.eq(ContentDeptDO::getContentId, id);
        List<ContentDeptDO> contentDeptDOList = contentDeptService.list(deptDOLambdaQueryWrapper);
        contentDTO.setDeptIdList(contentDeptDOList.stream().map(ContentDeptDO::getDeptId).collect(Collectors.toList()));
        LambdaQueryWrapper<ContentDiseaseDO> diseaseDOLambdaQueryWrapper = Wrappers.lambdaQuery();
        diseaseDOLambdaQueryWrapper.eq(ContentDiseaseDO::getContentId, id);
        List<ContentDiseaseDO> diseaseDOList = contentDiseaseService.list(diseaseDOLambdaQueryWrapper);
        contentDTO.setDiseaseIdList(diseaseDOList.stream().map(ContentDiseaseDO::getDiseaseId).collect(Collectors.toList()));
        if (contentDO.getContentType().equals(ContentTypeEnum.VIDEO.getCode()) && contentDO.getMeetingId() > 0) {
            MeetingDO meeting = meetingService.getById(contentDO.getMeetingId());
            if (Objects.nonNull(meeting)) {
                contentDTO.setMeetingName(meeting.getTitle());
            }
        }
        return contentDTO;
    }

    @Override
    public Page<AppContentDTO> listAppContentPageBySql(QueryAppContentPageRequest request) {
        Page<AppContentDTO> page = this.getBaseMapper().listAppContentPageBySql(request.getPage(), request);
        return PojoUtils.map(page, AppContentDTO.class);
    }

    @Override
    public Page<AppContentDTO> listAppContentPage(QueryAppContentPageRequest request) {
        LineEnum lineEnum = LineEnum.getByCode(Integer.parseInt(request.getLineId().toString()));
        Page<AppContentDTO> doPage = new Page<>();
        if (lineEnum == LineEnum.SA) {
            doPage = this.getBaseMapper().listSaContentPage(request.getPage(), request);
        } else if (lineEnum == LineEnum.B2B) {
            doPage = this.getBaseMapper().listB2bContentPage(request.getPage(), request);
        }
        return doPage;
    }

    @Override
    public Integer updatePv(Long id) {
        LambdaQueryWrapper<ContentDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ContentDO::getId, id).select(ContentDO::getPageView).last("limit 1");
        ContentDO contentDO = this.getOne(wrapper);
        contentDO.setId(id);
        Integer pageView = contentDO.getPageView() + 1;
        contentDO.setPageView(pageView);
        this.baseMapper.updatePv(contentDO);
        return pageView;
    }

    @Override
    public Integer updateLinePv(Long id, LineEnum lineEnum, AppContentDetailDTO appContentDetailDTO) {
        LambdaQueryWrapper<ContentDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ContentDO::getId, id).last("limit 1");
        ContentDO contentDO = this.getOne(wrapper);
        contentDO.setId(id);
        Integer pageView = contentDO.getPageView() + 1;
        contentDO.setPageView(pageView);
        if (LineEnum.HMC.equals(lineEnum)) {
            contentDO.setHmcView(contentDO.getHmcView() + 1);
            appContentDetailDTO.setHmcView(contentDO.getHmcView());
        }
        if (LineEnum.IH_DOC.equals(lineEnum)) {
            contentDO.setIhDocView(contentDO.getIhDocView() + 1);
            appContentDetailDTO.setIhDocView(contentDO.getIhDocView());
        }
        this.updateById(contentDO);
        appContentDetailDTO.setPageView(pageView);
        return pageView;
    }

    @Override
    public Boolean updateLikeCount(Long id, Long likeCount) {
        LambdaQueryWrapper<ContentDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ContentDO::getId, id);
        ContentDO contentDO = this.getOne(wrapper);
        contentDO.setLikeCount(likeCount);
        return this.updateById(contentDO);
    }

    @Override
    public AppContentDetailDTO getContentDetail(Long id, LineEnum lineEnum) {

        Long contentId = null;
        if (lineEnum.equals(LineEnum.HMC)) {
            hmcContentService.updateView(id);
            HmcContentDO hmcContentDO = hmcContentService.getById(id);
            contentId = hmcContentDO.getContentId();
        }
        if (lineEnum.equals(LineEnum.IH_DOC)) {
            ihDocContentService.updateView(id);
            IHDoctorContentDO ihDoctorContentDO = ihDocContentService.getById(id);
            contentId = ihDoctorContentDO.getContentId();
        }

        if (lineEnum.equals(LineEnum.IH_PATIENT)) {
            ihPatientContentService.updateView(id);
            IHPatientContentDO ihPatientContentDO = ihPatientContentService.getById(id);
            contentId = ihPatientContentDO.getContentId();
        }

        if (lineEnum.equals(LineEnum.SA)) {
            saContentService.updateView(id);
            SaContentDO saContentDO = saContentService.getById(id);
            contentId = saContentDO.getContentId();
        }

        if (lineEnum.equals(LineEnum.B2B)) {
            b2bContentService.updateView(id);
            B2bContentDO b2bContentDO = b2bContentService.getById(id);
            contentId = b2bContentDO.getContentId();
        }

        if (Objects.isNull(contentId)) {
            throw new BusinessException(ResultCode.FAILED, "内容不存在");
        }
        ContentDO contentDO = this.getById(contentId);
        if (Objects.isNull(contentDO)) {
            throw new BusinessException(ResultCode.FAILED, "内容不存在");
        }
        if (contentDO.getStatus().equals(ContentStatusEnum.UN_PUBLISH.getCode())) {
            throw new BusinessException(CmsErrorCode.CONTENT_OFFLINE);
        }
        AppContentDetailDTO appContentDetailDTO = PojoUtils.map(contentDO, AppContentDetailDTO.class);
        this.updateLinePv(contentDO.getId(), lineEnum, appContentDetailDTO);
        appContentDetailDTO.setId(id);
        appContentDetailDTO.setContentId(contentId);
        return appContentDetailDTO;
    }

    @Override
    public void deleteDraft(DeleteDraftRequest request) {
        ContentDO contentDO = this.getById(request.getId());
        if (Objects.isNull(contentDO)) {
            return;
        }
        if (contentDO.getIsDraft() != 1) {
            return;
        }
        contentDO.setOpUserId(request.getOpUserId());
        this.deleteByIdWithFill(contentDO);
    }

    @Override
    public List<DraftDTO> queryDraftList(BaseRequest request) {
        LambdaQueryWrapper<ContentDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ContentDO::getCreateUser, request.getOpUserId()).eq(ContentDO::getIsDraft, 1).orderByDesc(ContentDO::getId);
        List<ContentDO> contentDOList = this.list(wrapper);
        if (CollUtil.isEmpty(contentDOList)) {
            return Lists.newArrayList();
        }
        List<DraftDTO> draftDTOList = PojoUtils.map(contentDOList, DraftDTO.class);
        // for (DraftDTO draftDTO : draftDTOList) {
        //     draftDTO.setCategory(categoryService.getCategoryById(draftDTO.getCategoryId()).getCategoryName());
        // }
        return draftDTOList;
    }

    @Override
    public List<ContentDTO> getContentInfoByIdList(List<Long> idList) {
        QueryWrapper<ContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(ContentDO::getId, idList);
        List<ContentDO> list = this.list(wrapper);
        return PojoUtils.map(list, ContentDTO.class);
    }

    @Override
    public List<ContentDTO> getByTitle(String title) {
        QueryWrapper<ContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(ContentDO::getTitle, title);
        wrapper.last(" limit 100");
        List<ContentDO> list = this.list(wrapper);
        return PojoUtils.map(list, ContentDTO.class);
    }

    // @Override
    // public Page<ContentBO> clientContentPage(QueryContentPageRequest request) {
    //     return this.baseMapper.listAppContentPageBySql(request.getPage(), request);
    // }

    // @Override
    // public Boolean contentRank(ContentRankRequest request) {
    //     return this.contentDisplayLineService.contentRank(request);
    // }
    //
    // @Override
    // public Boolean contentTop(ContentRankRequest request) {
    //     return this.contentDisplayLineService.contentTop(request);
    // }
}
