package com.yiling.bi.protocol.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yiling.bi.protocol.dao.InputTthreelsflAuxdisplayRecordMapper;
import com.yiling.bi.protocol.dao.InputTthreelsflRecordMapper;
import com.yiling.bi.protocol.dto.request.InputTthreelsflAuxdisplayRecordRequest;
import com.yiling.bi.protocol.entity.InputTthreelsflAuxdisplayRecordDO;
import com.yiling.bi.protocol.entity.InputTthreelsflRecordDO;
import com.yiling.bi.protocol.service.InputTthreelsflAuxdisplayRecordService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/1/9
 */
@Service
@Slf4j
public class InputTthreelsflAuxdisplayRecordServiceImpl extends BaseServiceImpl<InputTthreelsflAuxdisplayRecordMapper, InputTthreelsflAuxdisplayRecordDO> implements InputTthreelsflAuxdisplayRecordService {

    @Override
    public boolean saveOrUpdateByUnique(InputTthreelsflAuxdisplayRecordRequest request) {
        InputTthreelsflAuxdisplayRecordDO inputTthreelsflAuxdisplayRecordDO = PojoUtils.map(request, InputTthreelsflAuxdisplayRecordDO.class);
        LambdaUpdateWrapper<InputTthreelsflAuxdisplayRecordDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InputTthreelsflAuxdisplayRecordDO::getBzCode, request.getBzCode());
        wrapper.eq(InputTthreelsflAuxdisplayRecordDO::getDyear, request.getDyear());
        wrapper.eq(InputTthreelsflAuxdisplayRecordDO::getDisplayNr, request.getDisplayNr());
        return this.saveOrUpdate(inputTthreelsflAuxdisplayRecordDO, wrapper);
    }
}
