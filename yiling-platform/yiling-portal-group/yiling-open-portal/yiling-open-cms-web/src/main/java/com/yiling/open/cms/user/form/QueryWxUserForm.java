package com.yiling.open.cms.user.form;


import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 查询互联网医院患者端用户form
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryWxUserForm extends QueryPageListForm {

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 注册开始时间
     */
    @ApiModelProperty(value = "注册开始时间")
    private Date registBeginTime;

    /**
     * 注册结束时间
     */
    @ApiModelProperty(value = "注册结束时间")
    private Date registEndTime;

    /**
     * 小程序id
     */
    @NotEmpty
    @ApiModelProperty(value = "小程序id")
    private String appId;
}
