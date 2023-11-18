package com.yiling.admin.data.center.report.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReviseStockPageItemVO extends BaseVO {

    /**
     * eid
     */
    @ApiModelProperty("eid")
    private Long eid;

    /**
     * 商业名称
     */
    @ApiModelProperty("商业名称*")
    private String ename;

    /**
     * 商业商品内码
     */
    @ApiModelProperty("商业商品内码")
    private String goodsInSn;

    /**
     * 商业商品名称
     */
    @ApiModelProperty("商业商品名称")
    private String goodsName;

    /**
     * 商业商品规格
     */
    @ApiModelProperty("商业商品规格")
    private String goodsSpecifications;

    /**
     * 以岭商品id
     */
    @ApiModelProperty("以岭商品id")
    private Long ylGoodsId;

    /**
     * 以岭商品名称
     */
    @ApiModelProperty("以岭商品名称")
    private String ylGoodsName;

    /**
     * 以岭商品规格
     */
    @ApiModelProperty("以岭商品规格*")
    private String ylGoodsSpecifications;

    /**
     * 采购渠道：1-大运河采购 2-京东采购
     */
    @ApiModelProperty("供应商名称：1-大运河采购 2-京东采购*")
    private Integer poSource;

    /**
     * 库存调整数量
     */
    @ApiModelProperty("库存调整数量*")
    private Long poQuantity;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String opUserName;

    /**
     * 创建时间
     */
    @ApiModelProperty("调整时间")
    private Date createTime;

}
