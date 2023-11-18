package com.yiling.bi.protocol.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yiling.bi.protocol.dao.InputTthreelsflLhauxdisplayRecordMapper;
import com.yiling.bi.protocol.dto.request.InputTthreelsflLhauxdisplayRecordRequest;
import com.yiling.bi.protocol.entity.InputTthreelsflLhauxdisplayRecordDO;
import com.yiling.bi.protocol.service.InputTthreelsflLhauxdisplayRecordService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/1/9
 */
@Service
@Slf4j
public class InputTthreelsflLhauxdisplayRecordServiceImpl extends BaseServiceImpl<InputTthreelsflLhauxdisplayRecordMapper, InputTthreelsflLhauxdisplayRecordDO> implements InputTthreelsflLhauxdisplayRecordService {

    @Override
    public boolean saveOrUpdateByUnique(InputTthreelsflLhauxdisplayRecordRequest request) {
        InputTthreelsflLhauxdisplayRecordDO record = PojoUtils.map(request, InputTthreelsflLhauxdisplayRecordDO.class);
        LambdaUpdateWrapper<InputTthreelsflLhauxdisplayRecordDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InputTthreelsflLhauxdisplayRecordDO::getBzCode, request.getBzCode());
        wrapper.eq(InputTthreelsflLhauxdisplayRecordDO::getDyear, request.getDyear());
        return this.saveOrUpdate(record, wrapper);
    }
}
