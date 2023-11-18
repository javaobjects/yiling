package com.yiling.bi.resource.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-10-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("input_lsfl_cover")
public class InputLsflCoverDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 省区
     */
    private String province;

    /**
     * 标准编码
     */
    private String bzCode;

    /**
     * 标准名称
     */
    private String bzName;

    /**
     * 连锁总部
     */
    private String nkaZb;

    /**
     * 协议类型
     */
    private String xyType;

    /**
     * 签订类型
     */
    private String qdType;

    /**
     * 客户类型
     */
    private String customerType;

    /**
     * 产品分类
     */
    private String wlType;

    /**
     * 品种
     */
    private String wlBreed;

    /**
     * 门店家数
     */
    private BigDecimal storesNum;

    /**
     * 一季度覆盖家数
     */
    private BigDecimal quarter1CoverNum;

    /**
     * 一季度覆盖率
     */
    private BigDecimal quarter1CoverRate;

    /**
     * 二季度覆盖家数
     */
    private BigDecimal quarter2CoverNum;

    /**
     * 二季度覆盖率
     */
    private BigDecimal quarter2CoverRate;

    /**
     * 三季度覆盖家数
     */
    private BigDecimal quarter3CoverNum;

    /**
     * 三季度覆盖率
     */
    private BigDecimal quarter3CoverRate;

    /**
     * 四季度覆盖家数
     */
    private BigDecimal quarter4CoverNum;

    /**
     * 四季度覆盖率
     */
    private BigDecimal quarter4CoverRate;

    /**
     * 数据时间
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

    /**
     * 数据状态：-1 默认状态
     */
    private String dataStatus;

    /**
     * 年份
     */
    private String dyear;


}
