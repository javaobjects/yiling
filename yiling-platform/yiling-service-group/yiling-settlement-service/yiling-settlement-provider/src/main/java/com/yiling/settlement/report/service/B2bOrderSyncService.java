package com.yiling.settlement.report.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.settlement.report.bo.B2bOrderSyncBO;
import com.yiling.settlement.report.dto.ReportB2bOrderSyncDTO;
import com.yiling.settlement.report.dto.request.QueryOrderSyncPageListRequest;
import com.yiling.settlement.report.dto.request.UpdateB2bOrderIdenRequest;
import com.yiling.settlement.report.entity.B2bOrderSyncDO;
import com.yiling.settlement.report.enums.ReportStatusEnum;

/**
 * <p>
 * 返利报表的B2B订单同步表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-08-08
 */
public interface B2bOrderSyncService extends BaseService<B2bOrderSyncDO> {

    /**
     * 根据订单号查询同步订单
     *
     * @param orderCodeList
     * @return
     */
    List<B2bOrderSyncBO> queryOrderSyncByOrderCode(List<String> orderCodeList);

    /**
     * 根据订单号查询订单信息
     *
     * @param orderNo
     * @return
     */
    List<B2bOrderSyncBO> queryOrderSync(String orderNo);

    /**
     * 根据订单id查询订单信息
     *
     * @param orderId
     * @return
     */
    List<ReportB2bOrderSyncDTO> queryOrderSync(Long orderId);

    /**
     * 创建订单同步信息
     *
     * @param orderCode
     * @return
     */
    Boolean createOrderSync(String orderCode);

    /**
     * 查询b2b订单同步信息
     *
     * @param request
     * @return
     */
    Page<ReportB2bOrderSyncDTO> queryB2bOrderSyncInfoPageList(QueryOrderSyncPageListRequest request);

    /**
     * 查询b2b订单号
     *
     * @param request
     * @return
     */
    Page<String> queryB2bOrderNoPageList(QueryOrderSyncPageListRequest request);

    /**
     * 查询b2b信息同步表主键
     *
     * @param request
     * @return
     */
    Page<Long> queryB2bSyncKeyPageList(QueryOrderSyncPageListRequest request);

    /**
     * 根据报表id驳回同步的订单
     *
     * @param reportId
     * @param statusEnum
     */
    void rejectOrderSync(Long reportId, ReportStatusEnum statusEnum);

    /**
     * 更新b2b订单的订单标识
     *
     * @param request
     * @return
     */
    Boolean updateB2bOrderIdentification(UpdateB2bOrderIdenRequest request);

    //    /**
    //     * 根据b2b订单信息同步表主键更新订单标识为库存不足（只更新订单状态为正常的）
    //     *
    //     * @param idList
    //     * @return
    //     */
    //    Boolean updateB2bOrderIdentById(List<Long> idList);

    /**
     * 初始化订单数据
     *
     * @return
     */
    Boolean initOrderData();
}
