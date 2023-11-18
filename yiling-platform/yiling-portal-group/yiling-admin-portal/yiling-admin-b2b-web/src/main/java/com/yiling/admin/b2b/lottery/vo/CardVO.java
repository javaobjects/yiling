package com.yiling.admin.b2b.lottery.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:wei.wang
 * @date:2021/11/4
 */
@Data
public class CardVO {
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

