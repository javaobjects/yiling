package com.yiling.bi.protocol.entity;

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
 * 零售部23年协议辅助陈列协议表
 * </p>
 *
 * @author baifc
 * @since 2023-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("input_tthreelsfl_auxdisplay_record")
public class InputTthreelsflAuxdisplayRecordDO extends BaseDO {

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
     * 陈列项目
     */
    @TableField(value = "display_xm", updateStrategy = FieldStrategy.IGNORED)
    private String displayXm;

    /**
     * 陈列内容
     */
    private String displayNr;

    /**
     * 通心络
     */
    @TableField(value = "txl", updateStrategy = FieldStrategy.IGNORED)
    private String txl;

    /**
     * 参松养心
     */
    @TableField(value = "ssyx", updateStrategy = FieldStrategy.IGNORED)
    private String ssyx;

    /**
     * 芪苈强心
     */
    @TableField(value = "qlqx", updateStrategy = FieldStrategy.IGNORED)
    private String qlqx;

    /**
     * 津力达
     */
    @TableField(value = "jld", updateStrategy = FieldStrategy.IGNORED)
    private String jld;

    /**
     * 夏荔芪
     */
    @TableField(value = "xlq", updateStrategy = FieldStrategy.IGNORED)
    private String xlq;

    /**
     * 乳结泰
     */
    @TableField(value = "rjt", updateStrategy = FieldStrategy.IGNORED)
    private String rjt;

    /**
     * 养正消积
     */
    @TableField(value = "yzxj", updateStrategy = FieldStrategy.IGNORED)
    private String yzxj;

    /**
     * 参灵蓝
     */
    @TableField(value = "sll", updateStrategy = FieldStrategy.IGNORED)
    private String sll;

    /**
     * 解郁除烦
     */
    @TableField(value = "jycf", updateStrategy = FieldStrategy.IGNORED)
    private String jycf;

    /**
     * 益肾养心
     */
    @TableField(value = "ysyx", updateStrategy = FieldStrategy.IGNORED)
    private String ysyx;

    /**
     * 连花清瘟
     */
    @TableField(value = "lhqw", updateStrategy = FieldStrategy.IGNORED)
    private String lhqw;

    /**
     * 连花清咳
     */
    @TableField(value = "lhqk", updateStrategy = FieldStrategy.IGNORED)
    private String lhqk;

    /**
     * 消杀防护
     */
    @TableField(value = "xsfh", updateStrategy = FieldStrategy.IGNORED)
    private String xsfh;

    /**
     * 双花
     */
    @TableField(value = "sh", updateStrategy = FieldStrategy.IGNORED)
    private String sh;

    /**
     * 八子补肾
     */
    @TableField(value = "bzbs", updateStrategy = FieldStrategy.IGNORED)
    private String bzbs;

    /**
     * 晚必安
     */
    @TableField(value = "wba", updateStrategy = FieldStrategy.IGNORED)
    private String wba;

    /**
     * 枣椹安神
     */
    @TableField(value = "zsas", updateStrategy = FieldStrategy.IGNORED)
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
