package com.yiling.payment.pay.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.payment.pay.entity.PaymentTransferRecordDO;

/**
 * <p>
 * 企业付款记录表 服务类
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-11-19
 */
public interface PaymentTransferRecordService extends BaseService<PaymentTransferRecordDO> {

    /**
     * 批量插入企业付款交易记录
     * @param paymentTransferRecordDos
     * @return
     */
    Boolean batchInsertTransferRecord(List<PaymentTransferRecordDO> paymentTransferRecordDos);

    /**
     * 修改企业打款记录
     * @param paymentTransferRecordDos
     * @return
     */
    boolean batchUpdateTransferRecordByPayNO(List<PaymentTransferRecordDO> paymentTransferRecordDos);

    /**
     * 通过订单号查询打款信息
     * @param payNo
     * @return
     */
    PaymentTransferRecordDO selectPaymentTransferRecordByPayNo(String payNo);

    /**
     * 查询打款中的记录
     * @param limit
     * @return
     */
    List<PaymentTransferRecordDO> listTransferIngRecords(Integer limit);

    /**
     * 查询待待打款的打款记录
     * @param limit
     * @return
     */
    List<PaymentTransferRecordDO> listWaitTransferRecords(Integer limit);

    /**
     * 查询企业付款中的付款订单
     * @param businessIds 业务ID
     * @param type  付款类型
     * @return
     */
    List<PaymentTransferRecordDO> listTransferIngByBusinessId(List<Long> businessIds,Integer type);


}
