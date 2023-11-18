package com.yiling.sales.assistant.userteam.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 我是队长时个人信息VO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/27
 */
@Data
@Accessors(chain = true)
public class MyLeaderDTO implements Serializable {

    private static final long serialVersionUID=-7851140500516591200L;

    /**
     * 团队名称
     */
    private String teamName;

    /**
     * 队员数量
     */
    private Integer memberNum;

    /**
     * 团队总单量
     */
    private Integer sumOrderNum;

    /**
     * 团队订单总金额
     */
    private BigDecimal sumOrderAmount;

    /**
     * 组队时间
     */
    private Date createTime;


}
