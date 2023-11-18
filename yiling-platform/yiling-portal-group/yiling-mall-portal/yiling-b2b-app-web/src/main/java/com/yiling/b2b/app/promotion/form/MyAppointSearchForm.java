package com.yiling.b2b.app.promotion.form;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shixing.sun
 * @date: 2022/05/24
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class MyAppointSearchForm extends QueryPageListRequest {

    @ApiModelProperty("1-已预约，2-已开始。3-已结")
    private Integer type;
}
