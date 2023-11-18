package com.yiling.open.ftp.dao;

import java.util.List;

import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.ftp.entity.FlowFtpCacheDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-02-25
 */
@Repository
public interface FlowFtpCacheMapper extends BaseMapper<FlowFtpCacheDO> {

    void deleteCache(@Param("keys") List<String> keys, @Param("suId") Long suId, @Param("taskNo")String taskNo);
}
