package com.yiling.order.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.dao.OrderInvoiceMapper;
import com.yiling.order.order.dto.OrderInvoiceDTO;
import com.yiling.order.order.entity.OrderInvoiceDO;
import com.yiling.order.order.service.OrderInvoiceApplyService;
import com.yiling.order.order.service.OrderInvoiceService;

/**
 * <p>
 * 订单发票 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2021-07-02
 */
@Service
public class OrderInvoiceServiceImpl extends BaseServiceImpl<OrderInvoiceMapper, OrderInvoiceDO> implements OrderInvoiceService {

    @Autowired
    OrderInvoiceApplyService orderInvoiceApplyService;

    /**
     * 根据发票id获取发票明细
     *
     * @param applyId
     * @return
     */
    @Override
    public List<OrderInvoiceDTO> getOrderInvoiceByApplyId(Long applyId) {
        QueryWrapper<OrderInvoiceDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderInvoiceDO :: getApplyId,applyId);
        return PojoUtils.map(list(wrapper),OrderInvoiceDTO.class);
    }

    /**
     * 根据发票id获取发票明细
     *
     * @param applyIds
     * @return
     */
    @Override
    public List<OrderInvoiceDTO> getOrderInvoiceByApplyIdList(List<Long> applyIds) {
        QueryWrapper<OrderInvoiceDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderInvoiceDO :: getApplyId,applyIds);
        return  PojoUtils.map(list(wrapper),OrderInvoiceDTO.class);
    }

    /**
     * 根据发票单号模糊查询出所有单号
     *
     * @param invoiceNo 发票单号
     * @return
     */
    @Override
    public List<OrderInvoiceDTO> getOrderInvoiceLikeInvoiceNo(String invoiceNo) {
        QueryWrapper<OrderInvoiceDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(OrderInvoiceDO :: getInvoiceNo,invoiceNo);
        List<OrderInvoiceDO> list = list(wrapper);
        return PojoUtils.map(list,OrderInvoiceDTO.class);
    }


}
