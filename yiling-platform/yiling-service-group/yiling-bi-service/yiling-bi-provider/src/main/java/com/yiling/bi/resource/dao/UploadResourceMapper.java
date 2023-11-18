package com.yiling.bi.resource.dao;

import com.yiling.bi.resource.entity.UploadResourceDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author: houjie.sun
 * @date: 2022/9/9
 */
@Repository
public interface UploadResourceMapper extends BaseMapper<UploadResourceDO> {

    Integer deleteUploadResourceById(@Param("id") Long id);
}
