package com.yiling.export.imports.model;

import javax.validation.constraints.NotEmpty;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 2023年主协议备案表excel模板
 * @author fucheng.bai
 * @date 2023/1/4
 */
@Data
public class ImportBiTthreelsflModel extends BaseImportModel {

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
    @Excel(name = "连锁药店上级编码", orderNum = "1")
    private String bzCode;

    /**
     * 连锁名称
     */
    @ExcelShow
    @Excel(name = "连锁药店上级名称", orderNum = "2")
    private String bzName;

    /**
     * NKA总部-仅NKA填写
     */
    @ExcelShow
    @Excel(name = "NKA总部名称-仅NKA填写", orderNum = "3")
    private String nkaZb;

    /**
     * 连锁类型-NKA/SKA/LKA
     */
    @ExcelShow
    @Excel(name = "连锁类型-NKA/SKA/LKA", orderNum = "4")
    private String lsType;

    /**
     * 门店家数
     */
    @ExcelShow
    @Excel(name = "门店家数", orderNum = "5")
    private String storeNum;

    /**
     * 产品分类
     */
    @ExcelShow
    @Excel(name = "产品分类", orderNum = "6")
    private String goodsType;

    /**
     * 品种
     */
    @ExcelShow
    @Excel(name = "品种", orderNum = "7")
    private String breed;

    /**
     * 品规编码
     */
    @ExcelShow
    @Excel(name = "品规编码", orderNum = "8")
    private String goodsId;

    /**
     * 剂型
     */
    @ExcelShow
    @Excel(name = "剂型", orderNum = "9")
    private String dosageForm;

    /**
     * 规格
     */
    @NotEmpty(message = "不可为空")
    @ExcelShow
    @Excel(name = "规格", orderNum = "10")
    private String goodsSpec;

    /**
     * 是否连花清瘟配额产品的品种
     */
    @ExcelShow
    @Excel(name = "连花清瘟配额关联产品", orderNum = "11")
    private String sfLhpe;

    /**
     * 协议核算价
     */
    @ExcelShow
    @Excel(name = "协议核算价", orderNum = "12")
    private String hsPrice;

    /**
     * 建议零售价
     */
    @ExcelShow
    @Excel(name = "建议零售价", orderNum = "13")
    private String lsPrice;



    /**
     * 签订类型
     */
    @NotEmpty(message = "不可为空")
    @ExcelShow
    @Excel(name = "签订类型（统谈统签/统谈分签/单谈单签）", orderNum = "14")
    private String qdType;

    /**
     * 购进商业1
     */
    @ExcelShow
    @Excel(name = "购进商业1-全称", orderNum = "15")
    private String gjBusiness1;

    /**
     * 购进商业2
     */
    @ExcelShow
    @Excel(name = "购进商业2-全称", orderNum = "16")
    private String gjBusiness2;

    /**
     * 购进商业3
     */
    @ExcelShow
    @Excel(name = "购进商业3-全称", orderNum = "17")
    private String gjBusiness3;

    /**
     * 陈列奖励
     */
    @ExcelShow
    @Excel(name = "陈列奖励-费率%", orderNum = "18")
    private String clAward;

    /**
     * 流向奖励
     */
    @ExcelShow
    @Excel(name = "流向奖励-费率%", orderNum = "19")
    private String lxAward;

    /**
     * 维价奖励
     */
    @ExcelShow
    @Excel(name = "维价奖励-费率%", orderNum = "20")
    private String wjAward;

    /**
     * 目标达成奖励
     */
    @ExcelShow
    @Excel(name = "目标达成奖励-费率%", orderNum = "21")
    private String targetAward;

    /**
     * 2022年达成
     */
    @ExcelShow
    @Excel(name = "2022年达成", orderNum = "22")
    private String ttwoRecord;

    /**
     * 一季度
     */
    @ExcelShow
    @Excel(name = "1季度", orderNum = "23")
    private String quarter1Num;

    /**
     * 二季度
     */
    @ExcelShow
    @Excel(name = "2季度", orderNum = "24")
    private String quarter2Num;

    /**
     * 三季度
     */
    @ExcelShow
    @Excel(name = "3季度", orderNum = "25")
    private String quarter3Num;

    /**
     * 四季度
     */
    @ExcelShow
    @Excel(name = "4季度", orderNum = "26")
    private String quarter4Num;

}
