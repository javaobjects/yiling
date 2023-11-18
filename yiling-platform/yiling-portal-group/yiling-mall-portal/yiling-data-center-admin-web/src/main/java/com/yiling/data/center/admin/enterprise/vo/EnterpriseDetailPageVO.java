package com.yiling.data.center.admin.enterprise.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业详情页 VO
 *
 * @author: xuan.zhou
 * @date: 2021/5/21
 */
@Data
public class EnterpriseDetailPageVO {

    @ApiModelProperty("企业信息")
    private EnterpriseVO enterpriseInfo;
}
