package com.yiling.sjms.flow.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 FlowEnterpriseGoodsMappingVO
 * @描述
 * @创建时间 2023/3/2
 * @修改人 shichen
 * @修改时间 2023/3/2
 **/
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class FlowEnterpriseGoodsMappingVO extends BaseVO {

    /**
     * 流向原始名称
     */
    @ApiModelProperty(value = "原始产品名称")
    private String flowGoodsName;

    /**
     * 流向原始规格
     */
    @ApiModelProperty(value = "原始规格")
    private String flowSpecification;

    /**
     * 流向原始商品内码
     */
    @ApiModelProperty(value = "原始商品内码")
    private String flowGoodsInSn;

    /**
     * 流向原始商品厂家
     */
    @ApiModelProperty(value = "原始商品厂家")
    private String flowManufacturer;

    /**
     * 流向原始商品单位
     */
    @ApiModelProperty(value = "原始商品单位")
    private String flowUnit;
    /**
     * crm商品编码
     */
    @ApiModelProperty(value = "标准商品编码")
    private Long crmGoodsCode;

    /**
     * 标准商品名称
     */
    @ApiModelProperty(value = "标准商品名称")
    private String goodsName;

    /**
     * 标准商品规格
     */
    @ApiModelProperty(value = "标准商品规格")
    private String goodsSpecification;

    /**
     * 经销商名称
     */
    @ApiModelProperty(value = "经销商名称")
    private String enterpriseName;

    /**
     * 经销商编码
     */
    @ApiModelProperty(value = "经销商编码")
    private Long crmEnterpriseId;

    /**
     * 转换单位：1-乘 2-除
     */
    @ApiModelProperty(value = "转换单位：1-乘 2-除")
    private Integer convertUnit;

    /**
     * 转换系数
     */
    @ApiModelProperty(value = "转换系数")
    private BigDecimal convertNumber;

    /**
     * 最后上传时间
     */
    @ApiModelProperty(value = "最后上传时间")
    private Date lastUploadTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "实施负责人")
    private String implementer;
}
