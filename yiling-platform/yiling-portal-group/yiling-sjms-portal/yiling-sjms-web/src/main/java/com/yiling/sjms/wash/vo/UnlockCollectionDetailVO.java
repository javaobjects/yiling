package com.yiling.sjms.wash.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UnlockCollectionDetailVO extends BaseVO {

    /**
     * 标准产品编码
     */
    @ApiModelProperty(value = "标准产品编码")
    private Long crmGoodsCode;

    /**
     * 标准产品名称
     */
    @ApiModelProperty(value = "标准产品名称")
    private String crmGoodsName;

    /**
     * 标准产品规格
     */
    @ApiModelProperty(value = "标准产品规格")
    private String crmGoodsSpec;

    /**
     * 采集价格
     */
    @ApiModelProperty(value = "采集价格")
    private BigDecimal collectionPrice;

    /**
     * 品种id
     */
    @ApiModelProperty(value = "品种id")
    private Long categoryId;

    /**
     * 品种id
     */
    @ApiModelProperty(value = "品种名称")
    private String category;


    /**
     * 品种
     */
    @ApiModelProperty(value = "品种（废弃）")
    private String varietyType;

    /**
     * 产品分类
     */
    @ApiModelProperty(value = "产品分类")
    private String goodsGroup;

    /**
     * 状态 0:有效 1无效
     */
    @ApiModelProperty(value = "状态 0:有效 1无效")
    private Integer status;


    /**
     * 最后操作时间
     */
    @ApiModelProperty(value = "最后操作时间")
    private Date lastOpTime;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Long lastOpUser;

    @ApiModelProperty(value = "操作人姓名")
    private String lastOpUserName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
