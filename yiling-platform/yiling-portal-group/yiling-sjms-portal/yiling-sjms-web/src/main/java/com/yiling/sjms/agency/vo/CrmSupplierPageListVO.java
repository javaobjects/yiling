package com.yiling.sjms.agency.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmSupplierPageListVO extends CrmAgencyPageListVO {
    /**
     * 商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    @ApiModelProperty("商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商")
    private Integer supplierLevel;

    /**
     * 商业属性 1-城市商业、2-县级商业
     */
    @ApiModelProperty("商业属性 1-城市商业、2-县级商业")
    private Integer supplierAttribute;

    /**
     * 普药级别 1-协议普药一级、2-协议普药二级、3-非协议普药一级、4-非协议普药二级、不签协议
     */
    @ApiModelProperty("普药级别 1-协议普药一级、2-协议普药二级、3-非协议普药一级、4-非协议普药二级、不签协议")
    private Integer generalMedicineLevel;
    /**
     * 是否重点商业1是2否
     */
    @ApiModelProperty("是否重点商业1是2否")
    private Integer supplierImportFlag;

    /**
     * 是否连锁总部 1是2否
     */
    @ApiModelProperty("是否连锁总部 1是2否")
    private Integer headChainFlag;

    /**
     * 普药销售模式 1-分销模式、2-招商模式、3-底价承包模式、3-KA连锁模式
     */
    @ApiModelProperty("普药销售模式 1-分销模式、2-招商模式、3-底价承包模式、3-KA连锁模式")
    private Integer generalMedicineSaleType;
    /**
     * 流向打取人工号
     */
    @ApiModelProperty("流向打取人工号")
    private String flowJobNumber;

    /**
     * 流向打取人姓名
     */
    @ApiModelProperty("流向打取人姓名")
    private String flowLiablePerson;
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 锁定类型: 1-打单2-销售
     */
    @ApiModelProperty("锁定类型")
    private Integer lockType;
}
