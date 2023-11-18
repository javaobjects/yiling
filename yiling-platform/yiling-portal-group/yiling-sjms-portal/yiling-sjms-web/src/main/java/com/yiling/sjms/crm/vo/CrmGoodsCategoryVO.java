package com.yiling.sjms.crm.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 CrmGoodsCategoryVO
 * @描述
 * @创建时间 2023/4/10
 * @修改人 shichen
 * @修改时间 2023/4/10
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmGoodsCategoryVO extends BaseVO {
    /**
     * 品类编码
     */
    @ApiModelProperty(value = "品类编码")
    private String code;

    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品类名称")
    private String name;

    /**
     * 品类级别
     */
    @ApiModelProperty(value = "品类级别")
    private Integer categoryLevel;

    /**
     * 上级id
     */
    @ApiModelProperty(value = "上级id")
    private Long parentId;

    @ApiModelProperty(value = "上级名称")
    private String parentName;

    /**
     * 是否末级 0：非末级，1：末级
     */
    @ApiModelProperty(value = "是否末级 0：非末级，1：末级")
    private Integer finalStageFlag;

    @ApiModelProperty(value = "商品数量")
    private Long goodsCount;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 子级列表
     */
    @ApiModelProperty(value = "子级列表")
    private List<CrmGoodsCategoryVO> children;
}
