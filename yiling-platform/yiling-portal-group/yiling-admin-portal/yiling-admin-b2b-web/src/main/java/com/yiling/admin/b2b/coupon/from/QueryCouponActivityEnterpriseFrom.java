package com.yiling.admin.b2b.coupon.from;

import java.util.List;

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
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;

    /**
     * 企业类型
     */
    @ApiModelProperty("企业类型 1-以岭，2-非以岭")
    private Integer merchantType;

    /**
     * 所属省份编码
     */
    @ApiModelProperty("所属省份编码")
    private String provinceCode;

    /**
     * 所属城市编码
     */
    @ApiModelProperty("所属城市编码")
    private String cityCode;

    /**
     * 所属区域编码
     */
    @ApiModelProperty("所属区域编码")
    private String regionCode;

    /**
     * 标签ID列表
     */
    @ApiModelProperty("标签ID列表")
    private List<Long> tagIds;
}
