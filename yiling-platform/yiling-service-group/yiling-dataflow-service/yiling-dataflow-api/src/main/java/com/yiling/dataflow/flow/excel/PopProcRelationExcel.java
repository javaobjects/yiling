package com.yiling.dataflow.flow.excel;

import java.io.Serializable;
import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author dexi.yao
 * @date 2021-09-23
 */
@Data
public class PopProcRelationExcel implements Serializable {


    private static final long serialVersionUID = 5922434920262761416L;

    /**
     * 采购关系编号
     */
    @Excel(name = "采购关系编号", orderNum = "0")
    private String procRelationNumber;

    /**
     * 工业主体eid
     */
    private Long factoryEid;

    /**
     * 工业主体名称
     */
    @Excel(name = "工业主体名称", orderNum = "1")
    private String factoryName;

    /**
     * 配送商eid
     */
    private Long deliveryEid;

    /**
     * 配送商名称
     */
    @Excel(name = "配送商名称", orderNum = "2")
    private String deliveryName;

    /**
     * 渠道商eid
     */
    private Long channelPartnerEid;

    /**
     * 渠道商名称
     */
    @Excel(name = "渠道商名称", orderNum = "3")
    private String channelPartnerName;

    /**
     * 渠道商渠道id
     */
    private Long channelPartnerChannelId;

    /**
     * 渠道商渠道id
     */
    @Excel(name = "渠道商渠道类型", orderNum = "4")
    private String channelPartnerChannelStr;

    /**
     * 开始时间
     */
    @Excel(name = "开始时间", orderNum = "5", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @Excel(name = "结束时间", orderNum = "6", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 配送类型：1-工业直配 2-三方配送
     */
    private Integer deliveryType;

    /**
     * 配送类型：1-工业直配 2-三方配送
     */
    @Excel(name = "配送类型", orderNum = "7")
    private String deliveryTypeStr;

    /**
     * 采购关系状态：1-未开始 2-进行中 3-已停用 4-已过期
     */
    private Integer procRelationStatus;

    /**
     * 采购关系状态：1-未开始 2-进行中 3-已停用 4-已过期
     */
    @Excel(name = "采购关系状态", orderNum = "8")
    private String procRelationStatusStr;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建人
     */
    @Excel(name = "创建人", orderNum = "9")
    private String createUserStr;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", orderNum = "10", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改人
     */
    @Excel(name = "修改人", orderNum = "11")
    private String updateUserStr;

    /**
     * 修改时间
     */
    @Excel(name = "修改时间", orderNum = "12", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
