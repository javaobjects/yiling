package com.yiling.cms.meeting.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.meeting.dto.MeetingDTO;
import com.yiling.cms.meeting.dto.request.QueryMeetingListRequest;
import com.yiling.cms.meeting.dto.request.QueryMeetingPageListRequest;
import com.yiling.cms.meeting.dto.request.SaveMeetingRequest;
import com.yiling.cms.meeting.dto.request.UpdateMeetingStatusRequest;
import com.yiling.cms.meeting.entity.MeetingDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 会议管理表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-02
 */
public interface MeetingService extends BaseService<MeetingDO> {

    /**
     * 分页查询会议管理列表
     *
     * @param request
     */
    Page<MeetingDTO> queryMeetingListPage(QueryMeetingPageListRequest request);

    /**
     * 查询会议列表
     *
     * @param request
     * @return
     */
    List<MeetingDTO> queryMeetingList(QueryMeetingListRequest request);

    /**
     * 添加会议
     *
     * @param request
     * @return
     */
    boolean saveMeeting(SaveMeetingRequest request);

    /**
     * 删除会议
     *
     * @param id
     * @param opUserId
     * @return
     */
    boolean deleteMeeting(Long id, Long opUserId);

    /**
     * 增加阅读量
     *
     * @param id
     * @return
     */
    boolean increaseRead(Long id);

    /**
     * 根据ID查询会议详情
     *
     * @param id
     * @return
     */
    MeetingDTO getMeeting(Long id);

    /**
     * 更新会议状态：发布/取消发布/删除
     *
     * @param request
     * @return
     */
    boolean updateMeetingStatus(UpdateMeetingStatusRequest request);
}
