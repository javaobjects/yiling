package com.yiling.export.imports.model;

import javax.validation.constraints.NotEmpty;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 零售部23年协议辅助陈列协议表excel模板
 * @author fucheng.bai
 * @date 2023/1/4
 */
@Data
public class ImportBiTthreelsflAuxdisplayModel extends BaseImportModel {

    /**
     * 省区
     */
    @ExcelShow
    @Excel(name = "省区", orderNum = "0")
    private String province;

    /**
     * 连锁编码
     */
    @NotEmpty(message = "不可为空")
    @ExcelShow
    @Excel(name = "连锁编码", orderNum = "1", isImportField = "true")
    private String bzCode;

    /**
     * 连锁名称
     */
    @ExcelShow
    @Excel(name = "连锁名称", orderNum = "2")
    private String bzName;

    /**
     * 陈列项目
     */
    @ExcelShow
    @Excel(name = "陈列项目", orderNum = "3", isImportField = "true")
    private String displayXm;

    /**
     * 陈列内容
     */
    @ExcelShow
    @Excel(name = "陈列内容", orderNum = "4", isImportField = "true")
    private String displayNr;

    /**
     * 通心络
     */
    @ExcelShow
    @Excel(name = "通心络", orderNum = "5")
    private String txl;

    /**
     * 参松养心
     */
    @ExcelShow
    @Excel(name = "参松养心", orderNum = "6")
    private String ssyx;

    /**
     * 芪苈强心
     */
    @ExcelShow
    @Excel(name = "芪苈强心", orderNum = "7")
    private String qlqx;

    /**
     * 津力达
     */
    @ExcelShow
    @Excel(name = "津力达", orderNum = "8")
    private String jld;

    /**
     * 夏荔芪
     */
    @ExcelShow
    @Excel(name = "夏荔芪", orderNum = "9")
    private String xlq;

    /**
     * 乳结泰
     */
    @ExcelShow
    @Excel(name = "乳结泰", orderNum = "10")
    private String rjt;

    /**
     * 养正消积
     */
    @ExcelShow
    @Excel(name = "养正消积", orderNum = "11")
    private String yzxj;

    /**
     * 参灵蓝
     */
    @ExcelShow
    @Excel(name = "参灵蓝", orderNum = "12")
    private String sll;

    /**
     * 解郁除烦
     */
    @ExcelShow
    @Excel(name = "解郁除烦", orderNum = "13")
    private String jycf;

    /**
     * 益肾养心
     */
    @ExcelShow
    @Excel(name = "益肾养心", orderNum = "14")
    private String ysyx;

    /**
     * 连花清瘟
     */
    @ExcelShow
    @Excel(name = "连花清瘟", orderNum = "15")
    private String lhqw;

    /**
     * 连花清咳
     */
    @ExcelShow
    @Excel(name = "连花清咳", orderNum = "16")
    private String lhqk;

    /**
     * 消杀防护
     */
    @ExcelShow
    @Excel(name = "消杀防护", orderNum = "17")
    private String xsfh;

    /**
     * 双花
     */
    @ExcelShow
    @Excel(name = "双花", orderNum = "18")
    private String sh;

    /**
     * 八子补肾
     */
    @ExcelShow
    @Excel(name = "八子补肾", orderNum = "19")
    private String bzbs;

    /**
     * 晚必安
     */
    @ExcelShow
    @Excel(name = "晚必安", orderNum = "20")
    private String wba;

    /**
     * 枣椹安神
     */
    @ExcelShow
    @Excel(name = "枣椹安神", orderNum = "21")
    private String zsas;
}
