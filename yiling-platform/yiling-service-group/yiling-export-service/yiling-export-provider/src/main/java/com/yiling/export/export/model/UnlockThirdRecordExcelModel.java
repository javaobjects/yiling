package com.yiling.export.export.model;

import java.math.BigDecimal;
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
 * @date 2023/5/18
 */
@Data
@EqualsAndHashCode
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(20)
public class UnlockThirdRecordExcelModel {

    /**
     * 客户机构编码
     */
    @ExcelProperty(value = "客户机构编码")
    private Long orgCrmId;

    /**
     * 客户机构名称
     */
    @ExcelProperty(value = "客户机构名称")
    private String customerName;


    /**
     * 月购进额度（万元）
     */
    @ColumnWidth(25)
    @ExcelProperty(value = "月购进额度（万元）")
    private BigDecimal purchaseQuota;

    /**
     * 生效业务部门
     */
    @ExcelIgnore
    private String effectiveDepartment;

    @ColumnWidth(30)
    @ExcelProperty(value = "生效业务部门")
    private String effectiveDepartmentDesc;

    /**
     * 最后操作时间
     */
    @ColumnWidth(25)
    @ExcelProperty(value = "最后操作时间")
    private Date lastOpTime;

    /**
     * 操作人
     */
    @ExcelIgnore
    private Long lastOpUser;

    @ExcelProperty(value = "操作人姓名")
    private String lastOpUserName;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;
}
