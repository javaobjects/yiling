package com.yiling.dataflow.index.service.impl;

import java.util.Date;

import com.yiling.dataflow.index.dto.request.SaveEsDispatchRecordRequest;
import com.yiling.dataflow.index.entity.EsDispatchRecordDO;
import com.yiling.dataflow.index.dao.EsDispatchRecordMapper;
import com.yiling.dataflow.index.service.EsDispatchRecordService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-02-13
 */
@Service
public class EsDispatchRecordServiceImpl extends BaseServiceImpl<EsDispatchRecordMapper, EsDispatchRecordDO> implements EsDispatchRecordService {

    @Override
    public Date getLastMaxTime() {
        return this.baseMapper.getLastMaxTime();
    }

    @Override
    public boolean insertEsDispatchRecord(SaveEsDispatchRecordRequest recordRequest) {
        return this.save(PojoUtils.map(recordRequest,EsDispatchRecordDO.class));
    }
}
