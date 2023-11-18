package com.yiling.bi.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.bi.order.entity.BiOrderBackupDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * @author fucheng.bai
 * @date 2022/9/19
 */
@Repository
public interface BiOrderBackupMapper extends BaseMapper<BiOrderBackupDO> {

    Integer getCountByOrderExtractMonth(@Param("monthStr") String monthStr);

    List<BiOrderBackupDO> getByOrderExtractMonth(@Param("monthStr") String monthStr, Page<BiOrderBackupDO> page);

    int batchIgnoreInsert(@Param("list") List<BiOrderBackupDO> biOrderBackupDOList);

    String getMaxExtractTime();
}
