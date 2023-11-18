package com.yiling.cms.meeting.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.common.CmsErrorCode;
import com.yiling.cms.meeting.dao.MeetingMapper;
import com.yiling.cms.meeting.dto.MeetingDTO;
import com.yiling.cms.meeting.dto.request.QueryMeetingListRequest;
import com.yiling.cms.meeting.dto.request.QueryMeetingPageListRequest;
import com.yiling.cms.meeting.dto.request.SaveMeetingRequest;
import com.yiling.cms.meeting.dto.request.UpdateMeetingStatusRequest;
import com.yiling.cms.meeting.entity.MeetingBusinessLineDO;
import com.yiling.cms.meeting.entity.MeetingContentDO;
import com.yiling.cms.meeting.entity.MeetingDO;
import com.yiling.cms.meeting.enums.MeetingOpTypeEnum;
import com.yiling.cms.meeting.enums.MeetingStatusEnum;
import com.yiling.cms.meeting.service.MeetingBusinessLineService;
import com.yiling.cms.meeting.service.MeetingContentService;
import com.yiling.cms.meeting.service.MeetingService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 会议管理表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-02
 */
@Service
public class MeetingServiceImpl extends BaseServiceImpl<MeetingMapper, MeetingDO> implements MeetingService {

    @Autowired
    MeetingBusinessLineService meetingBusinessLineService;
    @Autowired
    MeetingContentService meetingContentService;

    @Override
    public Page<MeetingDTO> queryMeetingListPage(QueryMeetingPageListRequest request) {
        if (Objects.nonNull(request.getStartCreateTime())) {
            request.setStartCreateTime(DateUtil.beginOfDay(request.getStartCreateTime()));
        }
        if (Objects.nonNull(request.getEndCreateTime())) {
            request.setEndCreateTime(DateUtil.endOfDay(request.getEndCreateTime()));
        }
        Page<MeetingDO> meetingDOPage = this.baseMapper.queryMeetingListPage(request.getPage(), request);
        return PojoUtils.map(meetingDOPage, MeetingDTO.class);
    }

    @Override
    public List<MeetingDTO> queryMeetingList(QueryMeetingListRequest request) {
        return PojoUtils.map(this.baseMapper.queryMeetingList(request), MeetingDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveMeeting(SaveMeetingRequest request) {
        // 会议活动时间校验
        if (request.getActivityStartTime().after(request.getActivityEndTime())) {
            throw new BusinessException(CmsErrorCode.MEETING_TIME_ERROR);
        }

        MeetingDO meetingDO = PojoUtils.map(request, MeetingDO.class);
        // 是否立即发布
        if (request.getPublishFlag() == 0) {
            meetingDO.setStatus(MeetingStatusEnum.NOT_RELEASE.getCode());
        } else {
            meetingDO.setStatus(MeetingStatusEnum.HAVE_RELEASE.getCode());
        }
        // 是否有学分为否时，清空学分值
        if (request.getCreditFlag() == 0) {
            meetingDO.setCreditValue(0);
        }

        // 更新
        if (Objects.nonNull(request.getId()) && request.getId() != 0) {
            if (Objects.isNull(request.getApplyEndTime())) {
                String date = "1970-01-01 00:00:00";
                meetingDO.setApplyEndTime(DateUtil.parseDate(date));
            }

            // 获取会议信息
            MeetingDO meeting = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(CmsErrorCode.MEETING_NOT_EXIST));
            // 已发布的不可编辑
            if (MeetingStatusEnum.HAVE_RELEASE == MeetingStatusEnum.getByCode(meeting.getStatus())) {
                throw new BusinessException(CmsErrorCode.MEETING_STATUS_EDIT_ERROR);
            }
            this.updateById(meetingDO);
            // 更新内容详情
            LambdaQueryWrapper<MeetingContentDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MeetingContentDO::getMeetingId, request.getId());

            MeetingContentDO meetingContentDO = new MeetingContentDO();
            meetingContentDO.setContent(request.getContent());
            meetingContentDO.setOpUserId(request.getOpUserId());
            this.meetingContentService.update(meetingContentDO, wrapper);

        } else {
            // 新增
            this.save(meetingDO);
            // 保存内容详情
            MeetingContentDO meetingContentDO = new MeetingContentDO();
            meetingContentDO.setMeetingId(meetingDO.getId());
            meetingContentDO.setContent(request.getContent());
            this.meetingContentService.save(meetingContentDO);
        }

        // 获取已经设置的引用业务线
        List<MeetingBusinessLineDO> lineDOList = meetingBusinessLineService.getBusinessLineByMeetingId(meetingDO.getId());
        List<Integer> lineList = lineDOList.stream().map(MeetingBusinessLineDO::getUseLine).distinct().collect(Collectors.toList());

        List<Integer> businessLineList = request.getBusinessLineList();
        // 移除
        List<Integer> removeCodes = lineList.stream().filter(code -> !businessLineList.contains(code)).distinct().collect(Collectors.toList());
        this.removeBusinessLine(removeCodes, meetingDO.getId(), request.getOpUserId());

        // 新增
        List<Integer> addCodes = businessLineList.stream().filter(code -> !lineList.contains(code)).distinct().collect(Collectors.toList());
        this.addBusinessLine(addCodes, meetingDO.getId(), request.getOpUserId());

        return true;
    }

    /**
     * 添加引用业务线
     *
     * @param addCodes
     * @param meetingId
     * @param opUserId
     */
    private void addBusinessLine(List<Integer> addCodes, Long meetingId, Long opUserId) {
        if (CollUtil.isEmpty(addCodes)) {
            return;
        }
        List<MeetingBusinessLineDO> businessLineDOList = addCodes.stream().map(lineId -> {
            MeetingBusinessLineDO businessLineDO = new MeetingBusinessLineDO();
            businessLineDO.setMeetingId(meetingId);
            businessLineDO.setUseLine(lineId);
            businessLineDO.setOpUserId(opUserId);
            return businessLineDO;
        }).collect(Collectors.toList());
        meetingBusinessLineService.saveBatch(businessLineDOList);
    }

    /**
     * 移除引用业务线
     *
     * @param removeCodes
     * @param meetingId
     * @param opUserId
     */
    private void removeBusinessLine(List<Integer> removeCodes, Long meetingId, Long opUserId) {
        if (CollUtil.isEmpty(removeCodes)) {
            return;
        }

        LambdaQueryWrapper<MeetingBusinessLineDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MeetingBusinessLineDO::getMeetingId, meetingId);
        queryWrapper.in(MeetingBusinessLineDO::getUseLine, removeCodes);

        MeetingBusinessLineDO entity = new MeetingBusinessLineDO();
        entity.setOpUserId(opUserId);

        meetingBusinessLineService.batchDeleteWithFill(entity, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMeeting(Long id, Long opUserId) {
        MeetingDO meetingDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(CmsErrorCode.MEETING_NOT_EXIST));
        // 已发布的不可删除
        if (MeetingStatusEnum.HAVE_RELEASE == MeetingStatusEnum.getByCode(meetingDO.getStatus())) {
            throw new BusinessException(CmsErrorCode.MEETING_STATUS_DELETE_ERROR);
        }

        // 删除内容详情
        MeetingContentDO meetingContentDO = new MeetingContentDO();
        meetingContentDO.setOpUserId(opUserId);

        LambdaQueryWrapper<MeetingContentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MeetingContentDO::getMeetingId, id);
        this.meetingContentService.batchDeleteWithFill(meetingContentDO, wrapper);

        // 删除引用业务线
        MeetingBusinessLineDO businessLineDO = new MeetingBusinessLineDO();
        meetingContentDO.setOpUserId(opUserId);

        LambdaQueryWrapper<MeetingBusinessLineDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MeetingBusinessLineDO::getMeetingId, id);
        this.meetingBusinessLineService.batchDeleteWithFill(businessLineDO, queryWrapper);

        return this.deleteByIdWithFill(meetingDO) > 0;
    }

    /**
     * 增加阅读量：暂时直接数据库更新，后期性能优化可以更改为队列、缓存等方式
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseRead(Long id) {
        this.baseMapper.increaseRead(id);
        return true;
    }

    @Override
    public MeetingDTO getMeeting(Long id) {
        MeetingDO meetingDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(CmsErrorCode.MEETING_NOT_EXIST));
        List<Integer> lineList = meetingBusinessLineService.getBusinessLineByMeetingId(id).stream().map(MeetingBusinessLineDO::getUseLine).collect(Collectors.toList());

        String content = Optional.ofNullable(meetingContentService.getContentByMeetingId(id)).orElse(new MeetingContentDO()).getContent();

        MeetingDTO meetingDTO = PojoUtils.map(meetingDO, MeetingDTO.class);
        meetingDTO.setBusinessLineList(lineList);
        meetingDTO.setContent(content);
        return meetingDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMeetingStatus(UpdateMeetingStatusRequest request) {
        MeetingDO meetingDO = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(CmsErrorCode.MEETING_NOT_EXIST));

        MeetingDO temp = new MeetingDO();
        temp.setId(request.getId());
        temp.setOpUserId(request.getOpUserId());
        if (MeetingOpTypeEnum.RELEASE.getCode().equals(request.getOpType())) {
            // 未发布的可以发布
            if (!meetingDO.getStatus().equals(MeetingStatusEnum.NOT_RELEASE.getCode())) {
                throw new BusinessException(CmsErrorCode.MEETING_STATUS_ERROR);
            }
            temp.setStatus(MeetingStatusEnum.HAVE_RELEASE.getCode());
            this.updateById(temp);

        } else if (MeetingOpTypeEnum.CANCEL_RELEASE.getCode().equals(request.getOpType())) {
            Date now = new Date();
            // 未开始和进行中的可以取消发布
            if (meetingDO.getStatus().equals(MeetingStatusEnum.NOT_RELEASE.getCode())) {
                throw new BusinessException(CmsErrorCode.MEETING_STATUS_ERROR);
            }
            if (!now.before(meetingDO.getActivityStartTime()) && !(now.after(meetingDO.getActivityStartTime()) && now.before(meetingDO.getActivityEndTime())) ) {
                throw new BusinessException(CmsErrorCode.MEETING_STATUS_ERROR);
            }
            temp.setStatus(MeetingStatusEnum.NOT_RELEASE.getCode());
            this.updateById(temp);

        } else if (MeetingOpTypeEnum.DELETE.getCode().equals(request.getOpType())) {
            // 未发布的可以删除
            if (MeetingStatusEnum.HAVE_RELEASE == MeetingStatusEnum.getByCode(meetingDO.getStatus())) {
                throw new BusinessException(CmsErrorCode.MEETING_STATUS_ERROR);
            }
            this.deleteByIdWithFill(temp);
        }

        return true;
    }
}
