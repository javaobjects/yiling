package com.yiling.bi.protocol.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2023/1/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InputTthreelsflAuxdisplayRecordDTO extends BaseDTO {

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
     * 陈列项目
     */
    private String displayXm;

    /**
     * 陈列内容
     */
    private String displayNr;

    /**
     * 通心络
     */
    private String txl;

    /**
     * 参松养心
     */
    private String ssyx;

    /**
     * 芪苈强心
     */
    private String qlqx;

    /**
     * 津力达
     */
    private String jld;

    /**
     * 夏荔芪
     */
    private String xlq;

    /**
     * 乳结泰
     */
    private String rjt;

    /**
     * 养正消积
     */
    private String yzxj;

    /**
     * 参灵蓝
     */
    private String sll;

    /**
     * 解郁除烦
     */
    private String jycf;

    /**
     * 益肾养心
     */
    private String ysyx;

    /**
     * 连花清瘟
     */
    private String lhqw;

    /**
     * 连花清咳
     */
    private String lhqk;

    /**
     * 消杀防护
     */
    private String xsfh;

    /**
     * 双花
     */
    private String sh;

    /**
     * 八子补肾
     */
    private String bzbs;

    /**
     * 晚必安
     */
    private String wba;

    /**
     * 枣椹安神
     */
    private String zsas;

    /**
     * 提交时间
     */
    private Date dataTime;

    /**
     * 提交人
     */
    private String dataName;

    /**
     * 数据状态：-1 默认状态 0 省区业务人员提报 1 运营经理审批通过 2 运营经理打回 3 运营经理提报 4 财务审核通过 5 财务打回
     */
    private String dataStatus;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 年份
     */
    private String dyear;

}
