package com.yiling.basic.version.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.version.dto.AppInfoDTO;
import com.yiling.basic.version.dto.AppVersionDTO;
import com.yiling.basic.version.dto.AppVersionPageDTO;
import com.yiling.basic.version.dto.request.VersionAddRequest;
import com.yiling.basic.version.dto.request.VersionPageRequest;
import com.yiling.basic.version.enums.AppChannelEnum;

/**
 * @author: yong.zhang
 * @date: 2021/10/27
 */
public interface AppVersionApi {

    /**
     * 查询最新的版本信息
     *
     * @param appType 版本平台：0-安卓 1-IOS
     * @return 版本信息
     */
    AppVersionDTO querySaNews(Integer appType, AppChannelEnum appChannelEnum);

    /**
     * 新增app版本
     *
     * @param request 新增版本请求参数
     * @return 成功/失败
     */
    boolean saveVersion(VersionAddRequest request);

    /**
     * 版本列表查询
     *
     * @param request 查询请求参数
     * @return 版本信息分页集合
     */
    Page<AppVersionPageDTO> pageList(VersionPageRequest request);

    /**
     * 查询最新的版本信息
     *
     * @param appType 版本平台：0-安卓 1-IOS
     * @return 版本信息
     */
    AppVersionDTO queryB2BNews(Integer appType, AppChannelEnum appChannelEnum);

    /**
     * 根据渠道查询这个渠道的所有应用
     *
     * @param channelEnum 渠道
     * @return app应用信息
     */
    List<AppInfoDTO> listAppInfoByChannelCode(AppChannelEnum channelEnum);
}
