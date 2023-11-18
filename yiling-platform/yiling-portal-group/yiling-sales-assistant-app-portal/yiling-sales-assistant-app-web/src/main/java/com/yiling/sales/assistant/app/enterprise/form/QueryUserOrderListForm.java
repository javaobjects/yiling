package com.yiling.sales.assistant.app.enterprise.form;


import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询用户对应的企业的订单 Form
 * 
 * @author lun.yu
 * @date 2021/9/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUserOrderListForm extends QueryPageListForm {

    /**
     * 用户ID
     */
    @NotNull
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
     * 客户名称
     */
    @Length(max = 30)
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

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
     * 日期排序：1-正序 2-倒序
     */
    @ApiModelProperty(value = "日期排序：1-正序 2-倒序")
    private Integer sort = 2 ;

}
