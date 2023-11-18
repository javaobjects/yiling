package com.yiling.cms.meeting.dao;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.meeting.dto.request.QueryMeetingListRequest;
import com.yiling.cms.meeting.dto.request.QueryMeetingPageListRequest;
import com.yiling.cms.meeting.entity.MeetingDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 会议管理表 Dao 接口
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-01
 */
@Repository
public interface MeetingMapper extends BaseMapper<MeetingDO> {

    /**
     * 分页查询会议管理列表
     *
     * @param page
     * @param request
     * @return
     */
    Page<MeetingDO> queryMeetingListPage(Page page, @Param("request") QueryMeetingPageListRequest request);

    /**
     * 分页查询会议管理列表
     *
     * @param request
     * @return
     */
    List<MeetingDO> queryMeetingList(@Param("request") QueryMeetingListRequest request);

    /**
     * 阅读量加1
     *
     * @param id
     */
    void increaseRead(@Param("id") Long id);

}
