package com.yiling.bi.order.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @author fucheng.bai
 * @date 2022/12/26
 */
@Data
public class OdsStjxcJddyhoutBO implements Serializable {

    private static final long serialVersionUID = -636167374180896537L;

    private Integer id;

    private String dyear;

    private String dmonth;

    private String customer;

    private String wlCode;
    private String wlName;
    private String wlSpec;
    private BigDecimal wlNum;

    private String crmGoodsid;

    private String b2bGoodsid;

    private String dataSource;
}
