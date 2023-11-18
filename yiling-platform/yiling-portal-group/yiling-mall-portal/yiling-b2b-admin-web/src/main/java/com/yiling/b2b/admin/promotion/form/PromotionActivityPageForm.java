package com.yiling.b2b.admin.promotion.form;

import java.util.Date;
import java.util.Objects;

import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.marketing.promotion.enums.PromotionTypeEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionActivityPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "促销活动名称")
    private String name;

    @ApiModelProperty(value = "活动类型（1-满赠）")
    private Integer type;

    @ApiModelProperty(value = "活动状态（1-启用；2-停用；）")
    private Integer status;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "活动创建人名称")
    private String createUserName;

    @ApiModelProperty(value = "创建人手机号")
    private String createUserTel;

    @ApiModelProperty(value = "费用承担方（1-平台；2-商家）")
    private Integer bear;

    @ApiModelProperty(value = "企业id")
    private Long eid;

    @ApiModelProperty(value = "备注")
    private String remark;

    public void check() {
        if (Objects.isNull(this.type) || Integer.valueOf(0).equals(this.type)) {
            this.type = PromotionTypeEnum.FULL_GIFT.getType();
        }
    }
}
