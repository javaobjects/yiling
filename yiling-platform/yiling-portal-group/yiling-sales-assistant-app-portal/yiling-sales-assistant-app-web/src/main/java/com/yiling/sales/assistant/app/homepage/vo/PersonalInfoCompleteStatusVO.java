package com.yiling.sales.assistant.app.homepage.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 个人信息完善状态 VO
 *
 * @author: xuan.zhou
 * @date: 2021/9/28
 */
@Data
public class PersonalInfoCompleteStatusVO {

    public PersonalInfoCompleteStatusVO(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    @ApiModelProperty("个人信息完善情况：0-未填写 1-审核中 2-审核通过 3-审核驳回")
    private Integer status;

    @ApiModelProperty("提示语")
    private String message;
}
