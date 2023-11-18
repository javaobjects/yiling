package com.yiling.framework.oss.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取oss上传策略
 *
 * @author: xuan.zhou
 * @date: 2023/3/18
 */
@Data
public class GetPolicyForm {

    /**
     * oss回调地址
     */
    @ApiModelProperty("上传回调地址（为空则使用默认回调地址）")
    private String callbackUrl;
}
