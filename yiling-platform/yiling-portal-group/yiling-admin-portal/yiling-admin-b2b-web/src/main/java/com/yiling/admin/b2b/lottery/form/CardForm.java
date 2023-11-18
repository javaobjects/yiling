package com.yiling.admin.b2b.lottery.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 卡号信息
 * @author:wei.wang
 * @date:2021/11/4
 */
@Data
public class CardForm {

    /**
     * 卡号
     */
    @ApiModelProperty("卡号")
    private String cardNo;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;
}
