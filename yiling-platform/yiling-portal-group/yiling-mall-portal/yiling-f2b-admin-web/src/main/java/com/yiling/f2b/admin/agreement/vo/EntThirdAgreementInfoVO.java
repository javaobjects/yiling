package com.yiling.f2b.admin.agreement.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/7
 */
@Data
public class EntThirdAgreementInfoVO {

    @ApiModelProperty(value = "年度协议")
    private Long yearAgreementCount;

    @ApiModelProperty(value = "临时协议")
    private Long tempAgreementCount;

}
