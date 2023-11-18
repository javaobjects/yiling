package com.yiling.bi.protocol.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2023/1/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InputTthreelsflRecordDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 省区
     */
    private String province;

    /**
     * 连锁编码
     */
    private String bzCode;

    /**
     * 连锁名称
     */
    private String bzName;

    /**
     * NKA总部
     */
    private String nkaZb;

    /**
     * 连锁类型
     */
    private String lsType;

    /**
     * 门店家数
     */
    private BigDecimal storeNum;

    /**
     * 产品分类
     */
    private String goodsType;

    /**
     * 品种
     */
    private String breed;

    /**
     * 品规编码
     */
    private String goodsId;

    /**
     * 剂型
     */
    private String dosageForm;

    /**
     * 规格
     */
    private String goodsSpec;

    /**
     * 协议核算价
     */
    private String hsPrice;

    /**
     * 建议零售价
     */
    private BigDecimal lsPrice;

    /**
     * 是否连花清瘟配额产品的品种
     */
    private String sfLhpe;

    /**
     * 签订类型
     */
    private String qdType;

    /**
     * 购进商业1
     */
    private String gjBusiness1;

    /**
     * 购进商业2
     */
    private String gjBusiness2;

    /**
     * 购进商业3
     */
    private String gjBusiness3;

    /**
     * 陈列奖励
     */
    private BigDecimal clAward;

    /**
     * 流向奖励
     */
    private BigDecimal lxAward;

    /**
     * 维价奖励
     */
    private BigDecimal wjAward;

    /**
     * 目标达成奖励
     */
    private BigDecimal targetAward;

    /**
     * 2022年达成
     */
    private BigDecimal ttwoRecord;

    /**
     * 一季度
     */
    private BigDecimal quarter1Num;

    /**
     * 二季度
     */
    private BigDecimal quarter2Num;

    /**
     * 三季度
     */
    private BigDecimal quarter3Num;

    /**
     * 四季度
     */
    private BigDecimal quarter4Num;

    /**
     * 提交时间
     */
    private Date dataTime;

    /**
     * 数据状态：-1 默认状态 0 省区业务人员提报 1 运营经理审批通过 2 运营经理打回 3 运营经理提报 4 财务审核通过 5 财务打回
     */
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
