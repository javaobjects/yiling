package com.yiling.admin.erp.enterprise.vo;

/**
 * ERP非以岭商品配置信息查询列表分页
 *
 * @author: houjie.sun
 * @date: 2022/4/26
 */

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ErpFlowGoodsConfigPageVO {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Long id;

    /**
     * 商业ID
     */
    @ApiModelProperty(value = "商业ID")
    private Long eid;

    /**
     * 商业名称
     */
    @ApiModelProperty(value = "商业名称")
    private String ename;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品内码（供应商的ERP的商品主键）
     */
    @ApiModelProperty(value = "商品内码")
    private String goodsInSn;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String specifications;

    /**
     * 批准文号（注册证号）
     */
    @ApiModelProperty(value = "批准文号")
    private String licenseNo;

    /**
     * 生产厂家
     */
    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operName;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private Date opTime;

}
