package com.yiling.bi.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 数拓进销存计算结果表
 * </p>
 *
 * @author baifc
 * @since 2022-09-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DwsStjxcDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 年份
     */
    private String dyear;

    /**
     * 月份
     */
    private String dmonth;

    /**
     * 客户名称
     */
    private String customer;

    /**
     * 产品编码
     */
    private String wlCode;

    /**
     * 产品名称
     */
    private String wlName;

    /**
     * 产品规格
     */
    private String wlSpec;

    /**
     * 当期大运河购进
     */
    private BigDecimal dqDyhoutnum;

    /**
     * 当期京东购进
     */
    private BigDecimal dqJdoutnum;

    /**
     * 当期销量
     */
    private BigDecimal dqSalenum;

    /**
     * 往期大运河购进
     */
    private BigDecimal lastDyhoutnum;

    /**
     * 往期京东购进
     */
    private BigDecimal lastJdoutnum;

    /**
     * 往期销量
     */
    private BigDecimal lastSalenum;

    /**
     * 期初大运河库存
     */
    private BigDecimal qcDyhkc;

    /**
     * 期初京东库存
     */
    private BigDecimal qcJskc;

    /**
     * 当期大运河剩余库存
     */
    private BigDecimal dqDyhkc;

    /**
     * 当期京东剩余库存
     */
    private BigDecimal dqJdkc;

    /**
     * 提交时间
     */
    private Date dataTime;

    /**
     * 提交人
     */
    private String dataName;

    /**
     * 数据来源
     */
    private String dataSource;

    private String ext01;


}
