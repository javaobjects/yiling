package com.yiling.cms.collect.service.impl;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.collect.dao.MyCollectMapper;
import com.yiling.cms.collect.dto.MyCollectDTO;
import com.yiling.cms.collect.dto.request.QueryCollectPageRequest;
import com.yiling.cms.collect.dto.request.QueryCollectRequest;
import com.yiling.cms.collect.dto.request.SaveCollectRequest;
import com.yiling.cms.collect.entity.MyCollectDO;
import com.yiling.cms.collect.service.MyCollectService;
import com.yiling.cms.content.entity.ContentDO;
import com.yiling.cms.content.enums.CmsErrorCode;
import com.yiling.cms.content.enums.ContentStatusEnum;
import com.yiling.cms.content.service.ContentService;
import com.yiling.cms.document.entity.DocumentDO;
import com.yiling.cms.document.service.DocumentService;
import com.yiling.cms.meeting.entity.MeetingDO;
import com.yiling.cms.meeting.service.MeetingService;
import com.yiling.cms.read.enums.ReadTypeEnums;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 我的收藏 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2022-07-29
 */
@Service
public class MyCollectServiceImpl extends BaseServiceImpl<MyCollectMapper, MyCollectDO> implements MyCollectService {

    @Autowired
    private ContentService contentService;

    @Autowired
    private  DocumentService documentService;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private FileService fileService;
    @Override
    public void save(SaveCollectRequest request) {
        LambdaQueryWrapper<MyCollectDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MyCollectDO::getCollectId, request.getCollectId()).eq(MyCollectDO::getCreateUser, request.getOpUserId()).eq(MyCollectDO::getSource, request.getSource()).eq(MyCollectDO::getCollectType, request.getCollectType()).last("limit 1");
        MyCollectDO one = this.getOne(wrapper);
        //取消收藏
        if (request.getStatus() == 2) {
            if (Objects.isNull(one)) {
                return;
            }
            one.setStatus(2);
            this.updateById(one);
            return;
        }else{
            if (Objects.nonNull(one)) {
                one.setStatus(1);
                this.updateById(one);
                return;
            }
        }
        //收藏
        MyCollectDO collectDO = new MyCollectDO();
        PojoUtils.map(request, collectDO);
        //文章和视频
        if (request.getCollectType().equals(ReadTypeEnums.ARTICLE.getCode()) || request.getCollectType().equals(ReadTypeEnums.VIDEO.getCode())) {
            ContentDO contentDO = contentService.getById(request.getCollectId());
            if (Objects.isNull(contentDO)) {
                throw new BusinessException(CmsErrorCode.CONTENT_OFFLINE);
            }
            // 内容下架处理
            if (contentDO.getStatus().equals(ContentStatusEnum.UN_PUBLISH.getCode())) {
                throw new BusinessException(CmsErrorCode.CONTENT_OFFLINE);
            }
            collectDO.setContentTime(contentDO.getCreateTime()).setTitle(contentDO.getTitle());
        }
        //文献
        if(request.getCollectType().equals(ReadTypeEnums.DOCUMENT.getCode())){
            DocumentDO documentDO = documentService.getById(request.getCollectId());
            if(Objects.isNull(documentDO)){
                throw new BusinessException(CmsErrorCode.DOCUMENT_OFFLINE);
            }
            // 内容下架处理
            if(documentDO.getStatus().equals(ContentStatusEnum.UN_PUBLISH.getCode())){
                throw new BusinessException(CmsErrorCode.DOCUMENT_OFFLINE);
            }
            collectDO.setContentTime(documentDO.getCreateTime()).setTitle(documentDO.getTitle());
        }
        //会议
        if(request.getCollectType().equals(ReadTypeEnums.MEETING.getCode())){
            MeetingDO meetingDO = Optional.ofNullable(meetingService.getById(request.getCollectId())).orElseThrow(() -> new BusinessException(com.yiling.cms.common.CmsErrorCode.MEETING_NOT_EXIST));
            collectDO.setContentTime(meetingDO.getCreateTime()).setTitle(meetingDO.getTitle());
        }
        this.save(collectDO);
    }

    @Override
    public Page<MyCollectDTO> queryPage(QueryCollectPageRequest request) {
        LambdaQueryWrapper<MyCollectDO> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotEmpty(request.getTitle()),MyCollectDO::getTitle,request.getTitle()).eq(MyCollectDO::getStatus,1);
        wrapper.eq(MyCollectDO::getCreateUser,request.getOpUserId()).eq(MyCollectDO::getSource,request.getSource()).orderByDesc(MyCollectDO::getUpdateTime);
        Page<MyCollectDO> readDOPage = this.page(request.getPage(), wrapper);
        Page<MyCollectDTO> collectDTOPage = PojoUtils.map(readDOPage, MyCollectDTO.class);
        if(CollUtil.isNotEmpty(collectDTOPage.getRecords())){
            collectDTOPage.getRecords().forEach(myCollectDTO -> {
                //会议封面
                if(myCollectDTO.getCollectType().equals(ReadTypeEnums.MEETING.getCode())){
                    MeetingDO meetingDO = meetingService.getById(myCollectDTO.getCollectId());
                    if(Objects.nonNull(meetingDO) && StrUtil.isNotEmpty(meetingDO.getBackgroundPic())){
                        myCollectDTO.setCover(fileService.getUrl(meetingDO.getBackgroundPic(), FileTypeEnum.MEETING_BACKGROUND_PIC));
                    }
                }
                //内容封面
                if(myCollectDTO.getCollectType().equals(ReadTypeEnums.ARTICLE.getCode()) || myCollectDTO.getCollectType().equals(ReadTypeEnums.VIDEO.getCode())){
                    ContentDO contentDO = contentService.getById(myCollectDTO.getCollectId());
                    if(Objects.nonNull(contentDO) && StrUtil.isNotEmpty(contentDO.getCover())){
                        myCollectDTO.setCover(fileService.getUrl(contentDO.getCover(), FileTypeEnum.CONTENT_COVER));
                    }
                }
            });
        }
        return collectDTOPage;
    }

    @Override
    public MyCollectDTO getOneCollect(QueryCollectRequest request) {
        LambdaQueryWrapper<MyCollectDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MyCollectDO::getCollectId, request.getCollectId()).eq(MyCollectDO::getCreateUser, request.getOpUserId()).eq(MyCollectDO::getSource, request.getSource()).eq(MyCollectDO::getCollectType, request.getCollectType()).last("limit 1");
        MyCollectDO one = this.getOne(wrapper);
        MyCollectDTO myCollectDTO = new MyCollectDTO();
        if (Objects.isNull(one)) {
            return myCollectDTO;
        }
        PojoUtils.map(one,myCollectDTO);
        return  myCollectDTO;
    }
}
