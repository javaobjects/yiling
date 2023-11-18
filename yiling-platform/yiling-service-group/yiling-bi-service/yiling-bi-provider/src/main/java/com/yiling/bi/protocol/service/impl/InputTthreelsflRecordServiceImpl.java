package com.yiling.bi.protocol.service.impl;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yiling.bi.protocol.dao.InputTthreelsflRecordMapper;
import com.yiling.bi.protocol.dto.request.InputTthreelsflRecordRequest;
import com.yiling.bi.protocol.entity.InputTthreelsflRecordDO;
import com.yiling.bi.protocol.service.InputTthreelsflRecordService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/1/5
 */
@Service
@Slf4j
public class InputTthreelsflRecordServiceImpl extends BaseServiceImpl<InputTthreelsflRecordMapper, InputTthreelsflRecordDO> implements InputTthreelsflRecordService {

    @Override
    public boolean saveOrUpdateByUnique(InputTthreelsflRecordRequest request) {
        InputTthreelsflRecordDO inputTthreelsflRecordDO = PojoUtils.map(request, InputTthreelsflRecordDO.class);
        LambdaUpdateWrapper<InputTthreelsflRecordDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InputTthreelsflRecordDO::getBzCode, inputTthreelsflRecordDO.getBzCode());
        wrapper.eq(InputTthreelsflRecordDO::getGoodsSpec, inputTthreelsflRecordDO.getGoodsSpec());
        wrapper.eq(InputTthreelsflRecordDO::getDyear, inputTthreelsflRecordDO.getDyear());
        return this.saveOrUpdate(inputTthreelsflRecordDO, wrapper);
    }
}
