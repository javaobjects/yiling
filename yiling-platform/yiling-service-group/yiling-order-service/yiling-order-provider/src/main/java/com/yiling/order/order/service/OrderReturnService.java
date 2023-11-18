package com.yiling.order.order.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.dto.AgreementOrderReturnDetailDTO;
import com.yiling.order.order.dto.OrderReturnDTO;
import com.yiling.order.order.dto.OrderReturnDetailDTO;
import com.yiling.order.order.dto.ReturnOrderNumberDTO;
import com.yiling.order.order.dto.request.OrderReturnApplyRequest;
import com.yiling.order.order.dto.request.OrderReturnCountRequest;
import com.yiling.order.order.dto.request.OrderReturnPageListRequest;
import com.yiling.order.order.dto.request.OrderReturnPageRequest;
import com.yiling.order.order.dto.request.OrderReturnPullErpPageRequest;
import com.yiling.order.order.dto.request.QueryOrderReturnPageRequest;
import com.yiling.order.order.dto.request.QueryOrderUseAgreementRequest;
import com.yiling.order.order.dto.request.ReturnNumberRequest;
import com.yiling.order.order.dto.request.UpdateErpOrderReturnRequest;
import com.yiling.order.order.entity.OrderReturnDO;
import com.yiling.order.order.entity.OrderReturnDetailDO;


/**
 * <p>
 * 退货单 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
public interface OrderReturnService extends BaseService<OrderReturnDO> {

    /**
     * 退货单请求参数的处理
     *
     * @param request 退货单申请数据
     * @return 处理后的请求参数
     */
    OrderReturnApplyRequest operateOrderReturnRequest(OrderReturnApplyRequest request);

    /**
     * 根据订单id查询已经审核通过的退货单
     *
     * @param orderId 订单id
     * @return 退货单信息
     */
    List<OrderReturnDTO> listPassedByOrderId(Long orderId);

    /**
     * 查询某订单号某种类型退货的数量
     *
     * @param orderNo 订单编号
     * @param returnType 退货单类型
     * @return 数量
     */
    int countByOrderNoAndType(String orderNo, Integer returnType);

    /**
     * 查询某订单某审核状态的退货单数量
     *
     * @param orderId 订单id
     * @param returnStatus 退货单状态：1-待审核 2-审核通过 3-审核驳回
     * @return 数量
     */
    int countByOrderIdAndStatus(Long orderId, Integer returnStatus);

    /**
     *查询某订单某审核状态的退货单
     * @param orderIds 订单ids
     * @param returnStatus 退货单状态：1-待审核 2-审核通过 3-审核驳回
     * @return 退货单集合
     */
    List<OrderReturnDO> orderReturnByOrderIdsAndStatus(List<Long> orderIds, Integer returnStatus);

    /**
     * 退货单分页列表查询
     *
     * @param request 请求参数
     * @return 退货单分页信息
     */
    Page<OrderReturnDO> pageList(OrderReturnPageListRequest request);

    /**
     * 查询是否有待审核的退货单
     *
     * @param orderId 订单id
     * @return 退货单信息
     */
    List<OrderReturnDTO> getOrderReturnListByOrderId(Long orderId);

    /**
     * 根据采购商EID和退款单审核时间区间 返回已通过退款审核的退款明细
     *
     * @param request 查询条件
     * @return 退货单组装信息
     */
    List<AgreementOrderReturnDetailDTO> getOrderReturnDetailByEidAndTime(QueryOrderUseAgreementRequest request);

    /**
     * 查询出今天，昨天和总共退货单数量
     *
     * @param request 查询条件
     * @return 今天，昨天和总共退货单数量对象
     */
    ReturnOrderNumberDTO getOrderNumber(ReturnNumberRequest request);

    /**
     * 查询EAS待同步的退货单列表
     *
     * @param request 请求参数
     * @return 退货单信息
     */
    Page<OrderReturnDTO> getERPPullOrderReturn(OrderReturnPullErpPageRequest request);

    /**
     * 更新退货单ERP同步状态
     *
     * @param request 集体更新退货单ERP推送信息
     * @return 成功/失败
     */
    Boolean updateERPOrderReturnByOrderId(List<UpdateErpOrderReturnRequest> request);

    /**
     * 查询退货单列表
     *
     * @param request 查询条件
     * @return 退货单信息
     */
    Page<OrderReturnDTO> queryOrderReturnPage(QueryOrderReturnPageRequest request);

    /**
     * 根据订单号，退货单类型，退货单状态查询退货单明细
     *
     * @param orderId 订单id
     * @param returnType 退货单类型
     * @param returnStatus 退货单状态
     * @return 退货单明细信息集合
     */
    List<OrderReturnDetailDTO> getOrderReturnDetailByOrderIdAndTypeAndStatus(Long orderId, Integer returnType, Integer returnStatus);

    /**
     * 根据退货单状态获取，卖家订单数量
     *
     * @param returnStatus 退货单状态
     * @param eidList 企业集合
     * @param type 1：以岭管理员 2：以岭本部非管理员 3：非以岭人员
     * @param userId 登录用户id
     * @return 符合条件订单数量
     */
    Integer getSellerOrderReturnNumberByReturnStatus(Integer returnStatus, List<Long> eidList, Integer type, Long userId);

    /**
     * 清除退货单数据
     *
     * @param orderId 订单id
     * @return 成功/失败
     */
    boolean deleteReturnOrder(Long orderId);

    /**
     * 根据订单集合查询出所有的退货明细
     *
     * @param orderIdList 订单id集合
     * @return 退货明细集合
     */
    Map<Long, List<OrderReturnDetailDO>> queryByOrderIdList(List<Long> orderIdList);

    /**
     * 批量根据订单id统计退货单数量
     *
     * @param orderIdList 订单id集合
     * @return (订单id, 退货单数量)
     */
    Map<Long, Integer> countByOrderIdList(List<Long> orderIdList);

    /**
     * 分页查询退货单信息
     *
     * @param request 分页查询请求参数
     * @return 退货单分页数据
     */
    Page<OrderReturnDTO> pageByCondition(OrderReturnPageRequest request);

    /**
     * 查询符合条件的退货单数量
     *
     * @param request 查询条件
     * @return 符合的退货单数量
     */
    int countByCondition(OrderReturnCountRequest request);
}
