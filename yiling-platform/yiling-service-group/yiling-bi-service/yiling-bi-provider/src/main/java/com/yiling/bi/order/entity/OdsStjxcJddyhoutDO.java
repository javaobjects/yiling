package com.yiling.bi.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/12/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ods_stjxc_jddyhout")
public class OdsStjxcJddyhoutDO extends BaseDO {

    private static final long serialVersionUID = 3492774321507352580L;

    /**
     * 业务时间
     */
    private Date bizdate;

    /**
     * 一级采购
     */
    private String levelPurchase;

    /**
     * 采购客户名称
     */
    private String customer;

    /**
     * 物料编码
     */
    private String wlCode;

    /**
     * 物料名称
     */
    private String wdName;

    /**
     * 物料规格
     */
    private String wlSpec;

    /**
     * 数量
     */
    private BigDecimal wdNum;

    /**
     * 年份
     */
    private String dyear;

    /**
     * 月份
     */
    private String dmonth;

    /**
     * 提交时间
     */
    private Date dataTime;

    /**
     * 提交人
     */
    private String dataName;

    /**
     * 数据来源：fjdout反算数据 jdout京东购进 dyhout大运河购进
     */
    private String dataSource;

}
