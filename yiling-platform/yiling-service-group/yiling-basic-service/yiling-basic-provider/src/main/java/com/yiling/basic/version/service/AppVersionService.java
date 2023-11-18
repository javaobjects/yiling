package com.yiling.basic.version.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.version.dto.AppVersionPageDTO;
import com.yiling.basic.version.dto.request.VersionAddRequest;
import com.yiling.basic.version.dto.request.VersionPageRequest;
import com.yiling.basic.version.entity.AppVersionDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 应用版本信息 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-27
 */
public interface AppVersionService extends BaseService<AppVersionDO> {

    /**
     * 查询最新的版本信息
     *
     * @param appType 版本平台：1-android 2-ios
     * @return
     */
    AppVersionDO queryNews(Integer appType, String channelCode);

    /**
     * 新增app版本
     *
     * @param request
     * @return
     */
    boolean saveVersion(VersionAddRequest request);

    /**
     * 版本列表查询
     *
     * @param request
     * @return
     */
    Page<AppVersionPageDTO> pageList(VersionPageRequest request);
}
