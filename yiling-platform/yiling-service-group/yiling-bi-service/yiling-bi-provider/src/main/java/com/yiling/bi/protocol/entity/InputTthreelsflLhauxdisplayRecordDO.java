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
 * 零售部23协议连花辅助陈列协议表
 * </p>
 *
 * @author baifc
 * @since 2023-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("input_tthreelsfl_lhauxdisplay_record")
public class InputTthreelsflLhauxdisplayRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 省份
     */
    @TableField(value = "province", updateStrategy = FieldStrategy.IGNORED)
    private String province;

    /**
     * 连锁分公司编码
     */
    private String bzCode;

    /**
     * 连锁分公司名称
     */
    @TableField(value = "bz_name", updateStrategy = FieldStrategy.IGNORED)
    private String bzName;

    /**
     * 陈列项目
     */
    @TableField(value = "display_xm", updateStrategy = FieldStrategy.IGNORED)
    private String displayXm;

    /**
     * 辅助陈列门店家数
     */
    @TableField(value = "display_storenum", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal displayStorenum;

    /**
     * 门店级别
     */
    @TableField(value = "store_level", updateStrategy = FieldStrategy.IGNORED)
    private String storeLevel;

    /**
     * 端架
     */
    @TableField(value = "bracket", updateStrategy = FieldStrategy.IGNORED)
    private String bracket;

    /**
     * 堆头
     */
    @TableField(value = "pilehead", updateStrategy = FieldStrategy.IGNORED)
    private String pilehead;

    /**
     * 花车
     */
    @TableField(value = "flower_car", updateStrategy = FieldStrategy.IGNORED)
    private String flowerCar;

    /**
     * 柜台堆头
     */
    @TableField(value = "gt_pilehead", updateStrategy = FieldStrategy.IGNORED)
    private String gtPilehead;

    /**
     * 收银台
     */
    @TableField(value = "cash_desk", updateStrategy = FieldStrategy.IGNORED)
    private String cashDesk;

    /**
     * 立柱
     */
    @TableField(value = "stud", updateStrategy = FieldStrategy.IGNORED)
    private String stud;

    /**
     * 灯箱
     */
    @TableField(value = "lamp_box", updateStrategy = FieldStrategy.IGNORED)
    private String lampBox;

    /**
     * 吊旗
     */
    @TableField(value = "showbill", updateStrategy = FieldStrategy.IGNORED)
    private String showbill;

    /**
     * 橱窗
     */
    @TableField(value = "shopwindow", updateStrategy = FieldStrategy.IGNORED)
    private String shopwindow;

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
