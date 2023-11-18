package com.yiling.order.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.order.order.dto.ReturnDetailBathDTO;
import com.yiling.order.order.dto.ReturnOrderExportDTO;
import com.yiling.order.order.dto.request.QueryOrderReturnInfoRequest;
import com.yiling.order.order.entity.OrderReturnDO;
import com.yiling.order.order.entity.OrderReturnDetailBatchDO;

/**
 * <p>
 * 退货单明细批次信息 Dao 接口
 * </p>
 *
 * @author zhangy
 * @date 2021-08-06
 */
@Repository
public interface OrderReturnDetailBatchMapper extends BaseMapper<OrderReturnDetailBatchDO> {
    /**
     * 查询某种类型的退货单明细批次信息
     *
     * @param detailId
     * @param batchNo
     * @param returnType
     * @return
     */
    List<OrderReturnDetailBatchDO> queryByDetailIdAndBatchNoAndType(@Param("detailId") Long detailId, @Param("batchNo") String batchNo, @Param("returnType") Integer returnType);

    /**
     * 退货单信息导出接口查询
     *
     * @param request
     * @return
     */
    Page<ReturnOrderExportDTO> queryExportByCondition(Page<OrderReturnDO> page, @Param("request") QueryOrderReturnInfoRequest request);

    /**
     * 根据订单明细编号集合查询出所有的退货批次信息
     *
     * @param detailIdList 订单明细id集合
     * @return
     */
    List<ReturnDetailBathDTO> queryByDetailId(@Param("detailIdList") List<Long> detailIdList);
}
