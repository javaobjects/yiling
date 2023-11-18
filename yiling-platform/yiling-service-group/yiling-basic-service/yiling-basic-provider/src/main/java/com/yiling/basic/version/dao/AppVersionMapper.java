package com.yiling.basic.version.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.version.dto.AppVersionPageDTO;
import com.yiling.basic.version.dto.request.VersionPageRequest;
import com.yiling.basic.version.entity.AppVersionDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 应用版本信息 Dao 接口
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-27
 */
@Repository
public interface AppVersionMapper extends BaseMapper<AppVersionDO> {

    /**
     * 分页查询版本信息
     *
     * @param page
     * @param request
     * @return
     */
    Page<AppVersionPageDTO> pageByCondition(Page<AppVersionDO> page, @Param("request") VersionPageRequest request);

    /**
     * @param appVersionDO
     * @return
     */
    boolean updateVersionInfo(AppVersionDO appVersionDO);
}
