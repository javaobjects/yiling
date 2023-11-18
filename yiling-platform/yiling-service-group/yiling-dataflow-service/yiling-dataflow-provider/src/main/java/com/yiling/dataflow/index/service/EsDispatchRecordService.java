package com.yiling.dataflow.index.service;

import java.util.Date;

import com.yiling.dataflow.index.dto.request.SaveEsDispatchRecordRequest;
import com.yiling.dataflow.index.entity.EsDispatchRecordDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-02-13
 */
public interface EsDispatchRecordService extends BaseService<EsDispatchRecordDO> {

    Date getLastMaxTime();

    boolean insertEsDispatchRecord(SaveEsDispatchRecordRequest recordRequest);

}
