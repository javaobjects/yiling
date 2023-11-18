package com.yiling.admin.data.center.report.form;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.form.QueryPageListForm;

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
public class QueryParamFlowGoodsRelationPageForm extends QueryPageListForm {

    /**
     * 商业id
     */
    @ApiModelProperty(value = "商业id")
    private Long eid;

    /**
     * 以岭商品名称
     */
    @ApiModelProperty(value = "以岭商品名称")
    private String ylGoodsName;

    /**
     * 创建时间-开始
     */
    @ApiModelProperty(value = "创建时间-开始")
    private Date createTimeStart;

    /**
     * 创建时间-结束
     */
    @ApiModelProperty(value = "创建时间-结束")
    private Date createTimeEnd;

    /**
     * 操作人姓名
     */
    @ApiModelProperty(value = "操作人姓名")
    private String opUserName;

    /**
     * 有无商品关系：0-无, 1-有
     */
    @ApiModelProperty(value = "有无商品关系：0-无, 1-有")
    private Integer relationFlag;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 生产厂家
     */
    @ApiModelProperty(value = "生产厂家")
    private String goodsManufacturer;


    /**
     * 商品关系标签：0-无标签 1-以岭品 2-非以岭品 3-中药饮片
     */
    @ApiModelProperty(value = "商品关系标签列表：1-以岭品 2-非以岭品 3-中药饮片 0-无标签 ")
    private List<Integer> goodsRelationLabelList;

}
