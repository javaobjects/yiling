package com.yiling.user.integral.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.bo.IntegralGiveUseRecordBO;
import com.yiling.user.integral.bo.UserSignDetailBO;
import com.yiling.user.integral.dto.IntegralGiveUseRecordDTO;
import com.yiling.user.integral.dto.IntegralOrderRecordDetailDTO;
import com.yiling.user.integral.dto.IntegralUserSignRecordDTO;
import com.yiling.user.integral.dto.request.AddIntegralRecordRequest;
import com.yiling.user.integral.dto.request.QueryIntegralRecordRequest;

/**
 * 积分发放/扣减记录 API
 *
 * @author: lun.yu
 * @date: 2023-01-10
 */
public interface IntegralRecordApi {

    /**
     * 积分发放/扣减记录分页列表
     *
     * @param request
     * @return
     */
    Page<IntegralGiveUseRecordBO> queryListPage(QueryIntegralRecordRequest request);

    /**
     * 添加积分发放/扣减记录
     *
     * @param request
     * @return
     */
    IntegralGiveUseRecordDTO addRecord(AddIntegralRecordRequest request);

    /**
     * 通过积分发放记录ID查询积分订单明细（订单送积分）
     *
     * @param recordId
     * @return
     */
    List<IntegralOrderRecordDetailDTO> getOrderDetailByRecordId(Long recordId);

    /**
     * 通过积分发放记录ID查询签到明细（签到送积分）
     *
     * @param recordId
     * @return
     */
    List<UserSignDetailBO> getSignDetailByRecordId(Long recordId);

}
