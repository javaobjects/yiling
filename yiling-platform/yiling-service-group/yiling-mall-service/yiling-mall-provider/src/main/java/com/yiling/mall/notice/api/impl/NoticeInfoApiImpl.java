package com.yiling.mall.notice.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.notice.api.NoticeInfoApi;
import com.yiling.mall.notice.dto.NoticeBeforeAfterDTO;
import com.yiling.mall.notice.dto.NoticeInfoDTO;
import com.yiling.mall.notice.dto.request.QueryNoticeInfoPageRequest;
import com.yiling.mall.notice.dto.request.SaveNoticeInfoRequest;
import com.yiling.mall.notice.service.NoticeInfoService;

/**
 * @author:wei.wang
 * @date:2021/6/16
 */
@DubboService
public class NoticeInfoApiImpl implements NoticeInfoApi {

    @Autowired
    NoticeInfoService noticeInfoService;

    /**
     * 后台获取公告列表
     *
     * @param request
     * @return
     */
    @Override
    public Page<NoticeInfoDTO> getNoticeInfoPage(QueryNoticeInfoPageRequest request) {

        return noticeInfoService.getNoticeInfoPage(request);
    }

    /**
     * POP前台获取公告信息
     *
     * @param request
     * @return
     */
    @Override
    public Page<NoticeInfoDTO> getAvailableNoticeInfoPage(QueryPageListRequest request) {
        return noticeInfoService.getAvailableNoticeInfoPage(request);
    }

    /**
     * POP前台首页获取公告
     *
     * @param number
     * @return
     */
    @Override
    public List<NoticeInfoDTO> getAvailableNoticeInfoList(Integer number) {
        return noticeInfoService.getAvailableNoticeInfoList(number);
    }

    /**
     * 保存公告信息
     *
     * @param request
     * @return
     */
    @Override
    public Boolean saveNoticeInfo(SaveNoticeInfoRequest request) {
        return noticeInfoService.saveNoticeInfo(request);
    }

    @Override
    public NoticeInfoDTO getDetails(Long id) {
        return PojoUtils.map(noticeInfoService.getById(id),NoticeInfoDTO.class);
    }

    @Override
    public NoticeBeforeAfterDTO getBeforeAfterNotice(Long id) {
        return noticeInfoService.getBeforeAfterNotice(id);
    }
}
