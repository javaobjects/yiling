package com.yiling.export.meeting;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 会议管理 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-01
 */
@Data
public class MeetingExcel implements IExcelDataModel, IExcelModel {

    private static final long serialVersionUID = 1L;

    /**
     * 行号
     */
    private Integer rowNum;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 省区
     */
    @Excel(name = "省区")
    private String provinceName;

    /**
     * 客户姓名
     */
    @Excel(name = "客户姓名")
    private String customerName;

    /**
     * 工作单位
     */
    @Excel(name = "工作单位")
    private String hospitalName;

    /**
     * 科室
     */
    @Excel(name = "科室")
    private String departmentName;

    /**
     * 职务
     */
    @Excel(name = "职务")
    private String jobTitle;

    /**
     * 电话
     */
    @Excel(name = "电话")
    private String mobile;

    /**
     * 机构编码
     */
    @Excel(name = "机构编码")
    private String code;

}
