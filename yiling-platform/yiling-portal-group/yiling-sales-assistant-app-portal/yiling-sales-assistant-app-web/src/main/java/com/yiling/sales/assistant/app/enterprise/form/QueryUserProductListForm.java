package com.yiling.sales.assistant.app.enterprise.form;


import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询用户对应的企业的商品 Form
 * 
 * @author lun.yu
 * @date 2021/9/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUserProductListForm extends QueryPageListForm {

    /**
     * 用户ID
     */
    @NotNull
    @ApiModelProperty(value = "用户ID")
    private Long userId;


    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 下单时间开始
     */
    @ApiModelProperty(value = "下单时间开始")
    private Date startCreateTime;

    /**
     * 下单时间结束
     */
    @ApiModelProperty(value = "下单时间结束")
    private Date endCreateTime;

    /**
     * 收货时间开始
     */
    @ApiModelProperty(value = "收货时间开始")
    private Date startReceiveTime ;

    /**
     * 收货时间结束
     */
    @ApiModelProperty(value = "收货时间结束")
    private Date endReceiveTime ;

}
