package com.yiling.sjms.gb.dto;

import java.math.BigDecimal;
import java.util.Date;
import com.yiling.framework.common.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 团购表单
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StatisticDTO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 省区名称
     */
    private String provinceName;

    /**
     * 产品名称
     */
    private String goodsName;

    /**
     * 产品code
     */
    private Long goodsCode;

    /**
     * 提报盒数
     */
    private Integer quantityBox;

    /**
     * 提报实际团购金额
     */
    private BigDecimal finalAmount;

    /**
     * 取消盒数
     */
    private Integer cancelQuantityBox;

    /**
     * 取消金额
     */
    private BigDecimal cancelFinalAmount;

    /**
     * 团购月份
     */
    private Date month;

    /**
     * 日期
     */
    private Date dayTime;

    /**
     * 关联团购Id
     */
    private String gbListId;


    /**
     * 是否删除：0-否 1-是
     */

    private Integer delFlag;

    /**
     * 创建人
     */

    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 团购月份
     */
    private String monthDate;


}
