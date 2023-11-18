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
 * @date 2023/6/1
 */
@Data
@EqualsAndHashCode
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(20)
public class CrmHospitalDrugstoreRelationModel {

    /**
     * 院外药店机构编码
     */
    @ExcelProperty(value = "院外药店机构编码")
    private Long drugstoreOrgId;

    /**
     * 院外药店机构名称
     */
    @ExcelProperty(value = "院外药店机构名称")
    private String drugstoreOrgName;

    /**
     * 医疗机构编码
     */
    @ExcelProperty(value = "医疗机构编码")
    private Long hospitalOrgId;

    /**
     * 医疗机构名称
     */
    @ExcelProperty(value = "医疗机构名称")
    private String hospitalOrgName;

    /**
     * 品种名称
     */
    @ExcelProperty(value = "品种名称")
    private String categoryName;

    /**
     * 标准产品编码
     */
    @ExcelProperty(value = "产品编码")
    private Long crmGoodsCode;

    /**
     * 标准产品名称
     */
    @ExcelProperty(value = "产品名称")
    private String crmGoodsName;

    /**
     * 标准产品规格
     */
    @ExcelProperty(value = "产品规格")
    private String crmGoodsSpec;

    /**
     * 开始生效时间
     */
    @ColumnWidth(25)
    @ExcelProperty(value = "开始生效时间")
    private Date effectStartTime;

    /**
     * 结束生效时间
     */
    @ColumnWidth(25)
    @ExcelProperty(value = "结束生效时间")
    private Date effectEndTime;

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
     * 是否停用 0-否 1-是
     */
    @ExcelIgnore
    private Integer disableFlag;

    @ExcelProperty(value = "状态")
    private String statusDesc;

    /**
     * 数据来源 1-导入数据 2-审批流数据
     */
    @ExcelIgnore
    private Integer dataSource;

}
