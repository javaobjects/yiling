package com.yiling.admin.data.center.report.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/5/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ParamFlowGoodsRelationPageVO extends BaseVO {

    /**
     * 商业公司eid
     */
    @ApiModelProperty(value = "商业公司id")
    private Long eid;

    /**
     * 商业名称
     */
    @ApiModelProperty(value = "商业名称")
    private String ename;

    /**
     * 商业商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商业商品内码
     */
    @ApiModelProperty(value = "商品内码")
    private String goodsInSn;

    /**
     * 商业商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String goodsSpecifications;

    /**
     * 生产厂家
     */
    @ApiModelProperty(value = "生产厂家")
    private String goodsManufacturer;

    /**
     * 以岭商品ID
     */
    @ApiModelProperty(value = "以岭商品ID")
    private String ylGoodsId;

    /**
     * 以岭商品名称
     */
    @ApiModelProperty(value = "以岭品名称")
    private String ylGoodsName;

    /**
     * 以岭商品规格
     */
    @ApiModelProperty(value = "以岭品规格")
    private String ylGoodsSpecifications;

    /**
     * 商品关系标签：0-无标签 1-以岭品 2-非以岭品 3-中药饮片
     */
    @ApiModelProperty(value = "商品关系标签：1-以岭品 2-非以岭品 3-中药饮片 0-无标签")
    private Integer goodsRelationLabel;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作人姓名
     */
    @ApiModelProperty(value = "操作人姓名")
    private String opUserName;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private Date opTime;

}
