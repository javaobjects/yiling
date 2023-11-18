package com.yiling.user.payment.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.payment.entity.PaymentDaysTemporaryRecordDO;

/**
 * <p>
 * 账期临时额度记录 Dao 接口
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-07-05
 */
@Repository
public interface PaymentDaysTemporaryRecordMapper extends BaseMapper<PaymentDaysTemporaryRecordDO> {

    int updateTemporaryById(@Param("id") Long id,@Param("temporaryAmount")BigDecimal temporaryAmount);


}
