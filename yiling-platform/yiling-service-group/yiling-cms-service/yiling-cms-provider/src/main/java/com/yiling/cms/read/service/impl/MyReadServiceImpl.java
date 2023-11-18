package com.yiling.cms.read.service.impl;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.entity.ContentDO;
import com.yiling.cms.content.service.ContentService;
import com.yiling.cms.meeting.entity.MeetingDO;
import com.yiling.cms.meeting.service.MeetingService;
import com.yiling.cms.read.dao.MyReadMapper;
import com.yiling.cms.read.dto.MyReadDTO;
import com.yiling.cms.read.dto.request.AddMyReadRequest;
import com.yiling.cms.read.dto.request.QueryMyReadPageRequest;
import com.yiling.cms.read.entity.MyReadDO;
import com.yiling.cms.read.enums.ReadTypeEnums;
import com.yiling.cms.read.service.MyReadService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 我的阅读 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2022-07-28
 */
@Service
public class MyReadServiceImpl extends BaseServiceImpl<MyReadMapper, MyReadDO> implements MyReadService {
    @Autowired
    private MeetingService meetingService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ContentService contentService;
    @Override
    public void add(AddMyReadRequest request) {
        LambdaQueryWrapper<MyReadDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MyReadDO::getReadId, request.getReadId()).eq(MyReadDO::getCreateUser, request.getOpUserId()).eq(MyReadDO::getSource, request.getSource()).eq(MyReadDO::getReadType, request.getReadType()).last("limit 1");
        MyReadDO readDO = this.getOne(wrapper);
        if (Objects.isNull(readDO) ||null==readDO.getId()) {
            MyReadDO myReadDO = new MyReadDO();
            PojoUtils.map(request, myReadDO);
            this.save(myReadDO);
            return;
        }
        //更新阅读时间
        readDO.setUpdateTime(new Date());
        this.updateById(readDO);
    }

    @Override
    public Page<MyReadDTO> queryPage(QueryMyReadPageRequest request) {
        LambdaQueryWrapper<MyReadDO> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotEmpty(request.getTitle()), MyReadDO::getTitle, request.getTitle());
        wrapper.eq(MyReadDO::getCreateUser, request.getOpUserId()).eq(MyReadDO::getSource, request.getSource()).orderByDesc(MyReadDO::getUpdateTime);
        Page<MyReadDO> readDOPage = this.page(request.getPage(), wrapper);
        Page<MyReadDTO> myReadDTOPage = PojoUtils.map(readDOPage, MyReadDTO.class);
        if (CollUtil.isNotEmpty(myReadDTOPage.getRecords())) {
            myReadDTOPage.getRecords().forEach(myReadDTO -> {
                //会议封面
                if (myReadDTO.getReadType().equals(ReadTypeEnums.MEETING.getCode())) {
                    MeetingDO meetingDO = meetingService.getById(myReadDTO.getReadId());
                    if (Objects.nonNull(meetingDO) && StrUtil.isNotEmpty(meetingDO.getBackgroundPic())) {
                        myReadDTO.setCover(fileService.getUrl(meetingDO.getBackgroundPic(), FileTypeEnum.MEETING_BACKGROUND_PIC));
                    }
                }
                //内容封面
                if (myReadDTO.getReadType().equals(ReadTypeEnums.ARTICLE.getCode()) || myReadDTO.getReadType().equals(ReadTypeEnums.VIDEO.getCode())) {
                    ContentDO contentDO = contentService.getById(myReadDTO.getReadId());
                    if (Objects.nonNull(contentDO) && StrUtil.isNotEmpty(contentDO.getCover())) {
                        myReadDTO.setCover(fileService.getUrl(contentDO.getCover(), FileTypeEnum.CONTENT_COVER));
                    }
                }
            });
        }

        return myReadDTOPage;
    }
}
