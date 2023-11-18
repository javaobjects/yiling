package com.yiling.framework.oss.vo;

import com.yiling.framework.oss.bo.OssPolicyResult;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 获取OSS上传文件授权返回结果 VO
 *
 * @author: xuan.zhou
 * @date: 2022/6/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OssPolicyResultVO extends OssPolicyResult {

    @ApiModelProperty("服务端接口的访问域名")
    private String domain;
}
