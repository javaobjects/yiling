package com.yiling.order.order.dto;

/**
 * @author:wei.wang
 * @date:2021/7/6
 */

import java.util.List;

import lombok.Data;

@Data
public class OrderAndDetailedAllInfoDTO extends OrderDTO {

    /**
     * 明细订单
     */
    List<OrderDetailByAgreementDTO> detailLists;
}
