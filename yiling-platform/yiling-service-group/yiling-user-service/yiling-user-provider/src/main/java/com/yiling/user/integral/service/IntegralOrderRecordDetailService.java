package com.yiling.user.integral.service;

import java.util.List;

import com.yiling.user.integral.dto.IntegralOrderRecordDetailDTO;
import com.yiling.user.integral.entity.IntegralOrderRecordDetailDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 积分发放记录订单明细表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-08
 */
public interface IntegralOrderRecordDetailService extends BaseService<IntegralOrderRecordDetailDO> {

    /**
     * 通过积分发放记录ID查询积分订单明细
     *
     * @param recordId
     * @return
     */
    List<IntegralOrderRecordDetailDTO> getByRecordId(Long recordId);

}
