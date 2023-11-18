package com.yiling.mall.notice.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.mall.notice.dto.NoticeBeforeAfterDTO;
import com.yiling.mall.notice.dto.NoticeInfoDTO;
import com.yiling.mall.notice.dto.request.QueryNoticeInfoPageRequest;
import com.yiling.mall.notice.dto.request.SaveNoticeInfoRequest;

/**
 * @author:wei.wang
 * @date:2021/6/16
 */
public interface NoticeInfoApi {
    /**
     * 后台获取公告列表
     * @param request
     * @return
     */
    Page<NoticeInfoDTO> getNoticeInfoPage(QueryNoticeInfoPageRequest request);

    /**
     * POP前台获取公告信息
     * @param request
     * @return
     */
    Page<NoticeInfoDTO> getAvailableNoticeInfoPage(QueryPageListRequest request);

    /**
     * POP前台首页获取公告
     * @param number
     * @return
     */
    List<NoticeInfoDTO> getAvailableNoticeInfoList(Integer number);

    /**
     * 保存公告信息
     * @param request
     * @return
     */
    Boolean saveNoticeInfo(SaveNoticeInfoRequest request);

    /**
     * 获取单个公告详情信息
     * @param id
     * @return
     */
    NoticeInfoDTO getDetails(Long id);

    /**
     * 获取当前公告上一篇下一篇的信息
     * @param id
     * @return
     */
    NoticeBeforeAfterDTO getBeforeAfterNotice(Long id);
}
