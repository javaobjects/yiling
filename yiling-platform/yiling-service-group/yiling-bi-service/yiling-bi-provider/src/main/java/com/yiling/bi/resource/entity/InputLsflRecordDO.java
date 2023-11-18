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
 * 零售部计算返利对应的备案表
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-09-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("input_lsfl_record")
public class InputLsflRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 省份
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
     * 客户类型-新
     */
    private String customerType;

    /**
     * NKA总部
     */
    private String nkaZb;

    /**
     * 签订类型
     */
    private String qdType;

    /**
     * 协议类型
     */
    private String xyType;

    /**
     * 产品分类
     */
    private String wlType;

    /**
     * 品种
     */
    private String wlBreed;

    /**
     * 产品编码
     */
    private String wlCode;

    /**
     * 产品名称
     */
    private String wlName;

    /**
     * 核算价
     */
    private BigDecimal accountPrice;

    /**
     * 基础费-小计
     */
    private BigDecimal basicNum;

    /**
     * 项目费-覆盖
     */
    private BigDecimal projectFg;

    /**
     * 项目费-其他
     */
    private BigDecimal projectOther;

    /**
     * 目标达成奖励（元/盒）
     */
    private BigDecimal targetNum;

    /**
     * 2022年规划目标
     */
    private BigDecimal yearTarget;

    /**
     * 1月份
     */
    private BigDecimal month1Num;

    /**
     * 2月份
     */
    private BigDecimal month2Num;

    /**
     * 3月份
     */
    private BigDecimal month3Num;

    /**
     * 4月份
     */
    private BigDecimal month4Num;

    /**
     * 5月份
     */
    private BigDecimal month5Num;

    /**
     * 6月份
     */
    private BigDecimal month6Num;

    /**
     * 7月份
     */
    private BigDecimal month7Num;

    /**
     * 8月份
     */
    private BigDecimal month8Num;

    /**
     * 9月份
     */
    private BigDecimal month9Num;

    /**
     * 10月份
     */
    private BigDecimal month10Num;

    /**
     * 11月份
     */
    private BigDecimal month11Num;

    /**
     * 12月份
     */
    private BigDecimal month12Num;

    /**
     * 时间戳
     */
    private String dataTimestamp;

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

    /**
     * 修改原因
     */
    private String reason;

    /**
     * 数据状态：-1 默认状态
     */
    private String dataStatus;

    /**
     * 备用字段
     */
    private String ext01;

    private String ext02;

    private String ext03;

    /**
     * 年份
     */
    private String dyear;


}
