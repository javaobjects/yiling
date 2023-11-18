package com.yiling.settlement.report.service;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.settlement.report.bo.MemberOrderSyncBO;
import com.yiling.settlement.report.entity.MemberSyncDO;
import com.yiling.settlement.report.enums.ReportStatusEnum;

/**
 * <p>
 * 报表的会员订单表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-17
 */
public interface MemberSyncService extends BaseService<MemberSyncDO> {

    /**
     * 根据订单号查询会员订单
     *
     * @param orderNo
     * @return
     */
    MemberOrderSyncBO queryMemberOrderSync(String orderNo);

    /**
     * 根据订单号查询会员订单
     *
     * @param orderNo
     * @return
     */
    List<MemberOrderSyncBO> queryMemberOrderSyncBatch(List<String> orderNo);

    /**
     * 根据推广方eid和下单起止时间查询未生成返利的会员订单 来源为 2-B2B-企业推广 3-销售助手
     *
     * @param eid
     * @param startTime
     * @param endTime
     * @return
     */
    List<MemberOrderSyncBO> queryOrderByEid(Long eid, Date startTime, Date endTime);

    /**
     * 根据报表id查询关联的会员订单
     *
     * @param reportId
     * @return
     */
    List<MemberOrderSyncBO> queryOrderByReportId(Long reportId);

    /**
     * 同步会员订单
     *
     * @param memberSyncDO
     * @return
     */
    Boolean createMemberOrderSync(MemberSyncDO memberSyncDO);

    /**
     * 会员订单退款同步
     *
     * @param memberSyncDO
     * @return
     */
    Boolean createMemberOrderRefund(MemberSyncDO memberSyncDO);

    /**
     * 更新会员订单的推广方
     *
     * @param orderNo
     * @param promoterId
     * @param source
     * @return
     */
    Boolean updatePromoter(String orderNo, Long promoterId, Integer source);

    /**
     * 根据报表id驳回会员订单
     *
     * @param reportId
     * @param statusEnum
     */
    void rejectMemberOrder(Long reportId, ReportStatusEnum statusEnum);

    /**
     * 初始化订单数据
     *
     * @return
     */
    Boolean initMemberOrderData();
}
