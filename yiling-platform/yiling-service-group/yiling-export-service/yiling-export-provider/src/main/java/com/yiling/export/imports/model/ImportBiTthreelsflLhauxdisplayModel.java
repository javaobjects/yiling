package com.yiling.export.imports.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 零售部23协议连花辅助陈列协议表excel模型
 * @author fucheng.bai
 * @date 2023/1/4
 */
@Data
public class ImportBiTthreelsflLhauxdisplayModel extends BaseImportModel {

    /**
     * 省份
     */
    @ExcelShow
    @Excel(name = "省区", orderNum = "0")
    private String province;

    /**
     * 连锁分公司编码
     */
    @NotEmpty(message = "不可为空")
    @ExcelShow
    @Excel(name = "连锁编码", orderNum = "1", isImportField = "true")
    private String bzCode;

    /**
     * 连锁分公司名称
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
     * 辅助陈列门店家数
     */
    @ExcelShow
    @Excel(name = "辅助陈列门店家数", orderNum = "4")
    private String displayStorenum;

    /**
     * 门店级别
     */
    @ExcelShow
    @Excel(name = "门店级别", orderNum = "5")
    private String storeLevel;

    /**
     * 端架
     */
    @ExcelShow
    @Excel(name = "端架", orderNum = "6")
    private String bracket;

    /**
     * 堆头
     */
    @ExcelShow
    @Excel(name = "堆头", orderNum = "7")
    private String pilehead;

    /**
     * 花车
     */
    @ExcelShow
    @Excel(name = "花车", orderNum = "8")
    private String flowerCar;

    /**
     * 柜台堆头
     */
    @ExcelShow
    @Excel(name = "柜台堆头", orderNum = "9")
    private String gtPilehead;

    /**
     * 收银台
     */
    @ExcelShow
    @Excel(name = "收银台", orderNum = "10")
    private String cashDesk;

    /**
     * 立柱
     */
    @ExcelShow
    @Excel(name = "立柱", orderNum = "11")
    private String stud;

    /**
     * 灯箱
     */
    @ExcelShow
    @Excel(name = "灯箱", orderNum = "12")
    private String lampBox;

    /**
     * 吊旗
     */
    @ExcelShow
    @Excel(name = "吊旗", orderNum = "13")
    private String showbill;

    /**
     * 橱窗
     */
    @ExcelShow
    @Excel(name = "橱窗", orderNum = "14")
    private String shopwindow;
}
