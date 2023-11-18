package com.yiling.b2b.admin.coupon.from;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryCouponActivityEnterpriseFrom extends QueryPageListForm {

    /**
     * 企业id
     */
    @ApiModelProperty("企业id")
    private String eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;

}
