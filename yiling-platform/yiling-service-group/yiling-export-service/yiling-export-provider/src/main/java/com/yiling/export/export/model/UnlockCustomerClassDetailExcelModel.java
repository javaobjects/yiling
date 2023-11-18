package com.yiling.export.export.model;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2023/5/8
 */
@Data
@EqualsAndHashCode
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(20)
public class UnlockCustomerClassDetailExcelModel {

    /**
     * 经销商编码
     */
    @ExcelProperty(value = "经销商编码")
    private Long crmEnterpriseId;

    /**
     * 经销商名称
     */
    @ExcelProperty(value = "经销商名称")
    private String ename;

    /**
     * 原始客户名称
     */
    @ExcelProperty(value = "原始客户名称")
    private String customerName;

    /**
     * 是否分类：0-未分类 1-已分类
     */
    @ExcelIgnore
    private Integer classFlag;

    @ExcelProperty(value = "是否分类")
    private String classFlagDesc;

    /**
     * 非锁客户分类：1-零售机构 2-商业公司 3-医疗机构 4-政府机构
     */
    @ExcelIgnore
    private Integer customerClassification;

    @ExcelProperty(value = "非锁客户分类")
    private String customerClassificationDesc;

    /**
     * 分类依据：1-规则 2-人工
     */
    @ExcelIgnore
    private Integer classGround;

    @ExcelProperty(value = "分类依据")
    private String classGroundDesc;

    /**
     * 操作人
     */
    @ExcelIgnore
    private Long lastOpUser;

    @ExcelProperty(value = "操作人")
    private String lastOpUserName;

    /**
     * 最后操作时间
     */
    @ColumnWidth(25)
    @ExcelProperty(value = "最后操作时间")
    private Date lastOpTime;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;
}
