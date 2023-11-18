package com.yiling.export.export.model;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author dexi.yao
 * @date 2021-09-23
 */
@Data
@EqualsAndHashCode
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(20)
@ExcelIgnoreUnannotated
public class PopProcRelationExcelModel  {

    /**
     * 采购关系编号
     */
    @ExcelProperty(value = "采购关系编号")
    private String procRelationNumber;

    /**
     * 工业主体eid
     */
    
    private Long factoryEid;

    /**
     * 渠道商eid
     */
    @ExcelProperty(value = "渠道商eid")
    private Long channelPartnerEid;

    /**
     * 渠道商名称
     */
    @ExcelProperty(value = "渠道商名称")
    private String channelPartnerName;

    /**
     * 渠道商渠道id
     */
    private Long channelPartnerChannelId;

    /**
     * 渠道商渠道id
     */
    @ExcelProperty(value = "渠道商渠道类型")
    private String channelPartnerChannelStr;

    /**
     * 配送类型：1-工业直配 2-三方配送
     */

    private Integer deliveryType;
    
    /**
     * 配送类型：1-工业直配 2-三方配送
     */
    @ExcelProperty(value = "配送类型")
    private String deliveryTypeStr;

    /**
     * 工业主体名称
     */
    @ExcelProperty(value = "工业主体名称")
    private String factoryName;

    /**
     * 配送商eid
     */
    @ExcelProperty(value = "配送商eid")
    private Long deliveryEid;

    /**
     * 配送商名称
     */
    @ExcelProperty(value = "配送商名称")
    private String deliveryName;

    /**
     * 开始时间
     */
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "结束时间")
    private Date endTime;


    /**
     * 采购关系状态：1-未开始 2-进行中 3-已停用 4-已过期
     */
    
    private Integer procRelationStatus;

    /**
     * 采购关系状态：1-未开始 2-进行中 3-已停用 4-已过期
     */
    @ExcelProperty(value = "采购关系状态")
    private String procRelationStatusStr;

    /**
     * 创建人
     */
    
    private Long createUser;

    /**
     * 创建人
     */
    @ExcelProperty(value = "创建人")
    private String createUserStr;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    
    private Long updateUser;

    /**
     * 修改人
     */
    @ExcelProperty(value = "修改人")
    private String updateUserStr;

    /**
     * 修改时间
     */
    @ExcelProperty(value = "修改时间")
    private Date updateTime;

}
