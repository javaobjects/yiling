package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 根据条件查询一发货收货的顶单，使用在协议里面
 * @author:wei.wang
 * @date:2021/7/6
 */
@Data
public class QueryOrderUseAgreementRequest implements java.io.Serializable {

    /**
     * 采购商Eid
     */
    private List<Long> buyerEids;

    /**
     * 配送商
     */
    private Long distributorEid;

    /**
     * 开始下单时间
     */
    private Date startCreateTime;

    /**
     * 开始下单时间
     */
    private Date endCreateTime;

    /**
     * EAS企业账号
     */
    private String easAccount;

}
