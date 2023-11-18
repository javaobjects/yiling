package com.yiling.user.integral.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.api.IntegralRecordApi;
import com.yiling.user.integral.bo.IntegralGiveUseRecordBO;
import com.yiling.user.integral.bo.UserSignDetailBO;
import com.yiling.user.integral.dto.IntegralGiveUseRecordDTO;
import com.yiling.user.integral.dto.IntegralOrderRecordDetailDTO;
import com.yiling.user.integral.dto.IntegralUserSignRecordDTO;
import com.yiling.user.integral.dto.request.AddIntegralRecordRequest;
import com.yiling.user.integral.dto.request.QueryIntegralRecordRequest;
import com.yiling.user.integral.entity.IntegralGiveUseRecordDO;
import com.yiling.user.integral.service.IntegralGiveUseRecordService;
import com.yiling.user.integral.service.IntegralOrderRecordDetailService;
import com.yiling.user.integral.service.IntegralUserSignRecordService;

import lombok.extern.slf4j.Slf4j;

/**
 * 积分发放/扣减记录 API 实现
 *
 * @author: lun.yu
 * @date: 2023-01-10
 */
@Slf4j
@DubboService
public class IntegralRecordApiImpl implements IntegralRecordApi {

    @Autowired
    IntegralGiveUseRecordService integralGiveUseRecordService;
    @Autowired
    IntegralOrderRecordDetailService integralOrderRecordDetailService;
    @Autowired
    IntegralUserSignRecordService integralUserSignRecordService;

    @Override
    public Page<IntegralGiveUseRecordBO> queryListPage(QueryIntegralRecordRequest request) {
        return integralGiveUseRecordService.queryListPage(request);
    }

    @Override
    public IntegralGiveUseRecordDTO addRecord(AddIntegralRecordRequest request) {
        return PojoUtils.map(integralGiveUseRecordService.addRecord(request), IntegralGiveUseRecordDTO.class);
    }

    @Override
    public List<IntegralOrderRecordDetailDTO> getOrderDetailByRecordId(Long recordId) {
        return integralOrderRecordDetailService.getByRecordId(recordId);
    }

    @Override
    public List<UserSignDetailBO> getSignDetailByRecordId(Long recordId) {
        return integralUserSignRecordService.getSignDetailByRecordId(recordId);
    }
}
