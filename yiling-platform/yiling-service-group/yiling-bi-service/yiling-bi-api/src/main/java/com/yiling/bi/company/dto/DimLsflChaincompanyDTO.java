package com.yiling.bi.company.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/2/10
 */
@Data
@Accessors(chain = true)
public class DimLsflChaincompanyDTO implements Serializable {

    private static final long serialVersionUID = -2131910859844180787L;

    /**
     * 省区
     */
    private String province;

    /**
     * 省区部门经理编码
     */
    private String dbCode;

    /**
     * 省区部门经理名称
     */
    private String dbName;

    /**
     * 新编码
     */
    private String chainCode;

    /**
     * 连锁公司名称
     */
    private String chainName;

    /**
     * NKA总部
     */
    private String nkaZb;

    /**
     * 签订类型
     */
    private String signedType;

    /**
     * 协议类型
     */
    private String agreementType;

    /**
     * 省区
     */
    private String provincech;

    /**
     * 药店属性
     */
    private String hospitalLevel;

    /**
     * 客户类型
     */
    private String customType;

    /**
     * 抽取时间
     */
    private Date extractTime;

    /**
     * 旧编码
     */
    private String oldChainCode;

    /**
     * 0 有效 1 无效 2 协议有效
     */
    private String delLevel;

    /**
     * 0 手动 1自动抽取
     */
    private String testMark;

    /**
     * 片区
     */
    private String area;

    /**
     * NKA总部旧编码
     */
    private String ylHeadChainCode;

    /**
     * 门店家数
     */
    private BigDecimal storeNum;
}
