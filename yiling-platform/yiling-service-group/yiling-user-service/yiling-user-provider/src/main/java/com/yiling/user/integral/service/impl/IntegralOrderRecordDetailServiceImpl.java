package com.yiling.user.integral.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.dto.IntegralOrderRecordDetailDTO;
import com.yiling.user.integral.entity.IntegralOrderRecordDetailDO;
import com.yiling.user.integral.dao.IntegralOrderRecordDetailMapper;
import com.yiling.user.integral.service.IntegralOrderRecordDetailService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 积分发放记录订单明细表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-08
 */
@Service
public class IntegralOrderRecordDetailServiceImpl extends BaseServiceImpl<IntegralOrderRecordDetailMapper, IntegralOrderRecordDetailDO> implements IntegralOrderRecordDetailService {

    @Override
    public List<IntegralOrderRecordDetailDTO> getByRecordId(Long recordId) {
        LambdaQueryWrapper<IntegralOrderRecordDetailDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderRecordDetailDO::getRecordId, recordId);
        return PojoUtils.map(this.list(wrapper), IntegralOrderRecordDetailDTO.class);
    }
}
