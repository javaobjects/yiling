package com.yiling.dataflow.index.dao;

import java.util.Date;

import com.yiling.dataflow.index.entity.EsDispatchRecordDO;
import com.yiling.framework.common.base.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-02-13
 */
@Repository
public interface EsDispatchRecordMapper extends BaseMapper<EsDispatchRecordDO> {
    Date getLastMaxTime();
}
