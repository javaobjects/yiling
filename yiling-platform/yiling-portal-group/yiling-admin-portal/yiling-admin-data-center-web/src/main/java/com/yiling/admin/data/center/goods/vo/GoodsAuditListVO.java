package com.yiling.admin.data.center.goods.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/5/24
 */
@Data
public class GoodsAuditListVO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "待审核主键")
    private Long id;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long gid;

    /**
     * 供应商ID
     */
    @ApiModelProperty(value = "供应商ID")
    private Long eid;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String ename;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;

    /**
     * 通用名称
     */
    @ApiModelProperty(value = "通用名称")
    private String commonName;

    /**
     * 注册证号
     */
    @ApiModelProperty(value = "注册证号")
    private String licenseNo;

    /**
     * 生产厂家
     */
    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    /**
     * 生产地址
     */
    @ApiModelProperty(value = "生产地址")
    private String manufacturerAddress;

    /**
     * 销售规格
     */
    @ApiModelProperty(value = "销售规格")
    private String specifications;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 审核状态：0待审核1审核通过2审核不通3忽略
     */
    @ApiModelProperty(value = "审核状态：0待审核1审核通过2审核不通3忽略")
    private Integer auditStatus;

    /**
     * 数据来源
     */
    @ApiModelProperty(value = "数据来源")
    private Integer source;

    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间")
    private Date createTime;

    /**
     * 驳回信息
     */
    @ApiModelProperty(value = "驳回信息")
    private String rejectMessge;
}
