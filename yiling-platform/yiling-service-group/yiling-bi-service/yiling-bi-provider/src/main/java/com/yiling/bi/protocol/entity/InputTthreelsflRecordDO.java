package com.yiling.bi.protocol.entity;

import java.math.BigDecimal;
import java.util.Date;


import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 2023年主协议备案表
 * </p>
 *
 * @author baifc
 * @since 2023-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("input_tthreelsfl_record")
public class InputTthreelsflRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 省区
     */
    @TableField(value = "province", updateStrategy = FieldStrategy.IGNORED)
    private String province;

    /**
     * 连锁编码
     */
    private String bzCode;

    /**
     * 连锁名称
     */
    @TableField(value = "bz_name", updateStrategy = FieldStrategy.IGNORED)
    private String bzName;

    /**
     * NKA总部
     */
    @TableField(value = "nka_zb", updateStrategy = FieldStrategy.IGNORED)
    private String nkaZb;

    /**
     * 连锁类型
     */
    @TableField(value = "ls_type", updateStrategy = FieldStrategy.IGNORED)
    private String lsType;

    /**
     * 门店家数
     */
    @TableField(value = "store_num", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal storeNum;

    /**
     * 产品分类
     */
    @TableField(value = "goods_type", updateStrategy = FieldStrategy.IGNORED)
    private String goodsType;

    /**
     * 品种
     */
    @TableField(value = "breed", updateStrategy = FieldStrategy.IGNORED)
    private String breed;

    /**
     * 品规编码
     */
    @TableField(value = "goods_id", updateStrategy = FieldStrategy.IGNORED)
    private String goodsId;

    /**
     * 剂型
     */
    @TableField(value = "dosage_form", updateStrategy = FieldStrategy.IGNORED)
    private String dosageForm;

    /**
     * 规格
     */
    private String goodsSpec;

    /**
     * 协议核算价
     */
    @TableField(value = "hs_price", updateStrategy = FieldStrategy.IGNORED)
    private String hsPrice;

    /**
     * 建议零售价
     */
    @TableField(value = "ls_price", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal lsPrice;

    /**
     * 是否连花清瘟配额产品的品种
     */
    @TableField(value = "sf_lhpe", updateStrategy = FieldStrategy.IGNORED)
    private String sfLhpe;

    /**
     * 签订类型
     */
    @TableField(value = "qd_type", updateStrategy = FieldStrategy.IGNORED)
    private String qdType;

    /**
     * 购进商业1
     */
    @TableField(value = "gj_business1", updateStrategy = FieldStrategy.IGNORED)
    private String gjBusiness1;

    /**
     * 购进商业2
     */
    @TableField(value = "gj_business2", updateStrategy = FieldStrategy.IGNORED)
    private String gjBusiness2;

    /**
     * 购进商业3
     */
    @TableField(value = "gj_business3", updateStrategy = FieldStrategy.IGNORED)
    private String gjBusiness3;

    /**
     * 陈列奖励
     */
    @TableField(value = "cl_award", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal clAward;

    /**
     * 流向奖励
     */
    @TableField(value = "lx_award", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal lxAward;

    /**
     * 维价奖励
     */
    @TableField(value = "wj_award", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal wjAward;

    /**
     * 目标达成奖励
     */
    @TableField(value = "target_award", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal targetAward;

    /**
     * 2022年达成
     */
    @TableField(value = "ttwo_record", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal ttwoRecord;

    /**
     * 一季度
     */
    @TableField(value = "quarter1_num", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal quarter1Num;

    /**
     * 二季度
     */
    @TableField(value = "quarter2_num", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal quarter2Num;

    /**
     * 三季度
     */
    @TableField(value = "quarter3_num", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal quarter3Num;

    /**
     * 四季度
     */
    @TableField(value = "quarter4_num", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal quarter4Num;

    /**
     * 提交时间
     */
    @TableField(value = "data_time", updateStrategy = FieldStrategy.IGNORED)
    private Date dataTime;

    /**
     * 数据状态：-1 默认状态 0 省区业务人员提报 1 运营经理审批通过 2 运营经理打回 3 运营经理提报 4 财务审核通过 5 财务打回
     */
    @TableField(value = "data_status", updateStrategy = FieldStrategy.IGNORED)
    private String dataStatus;

    /**
     * 提交人
     */
    private String dataName;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 年份
     */
    private String dyear;


}
