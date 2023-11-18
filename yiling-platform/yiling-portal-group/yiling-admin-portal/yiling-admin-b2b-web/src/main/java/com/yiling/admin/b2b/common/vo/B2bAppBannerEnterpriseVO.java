package com.yiling.admin.b2b.common.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/11/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class B2bAppBannerEnterpriseVO extends BaseVO {
    
    @ApiModelProperty(value = "banner的id")
    private Long bannerId;

    @ApiModelProperty(value = "企业ID")
    private Long eid;

    @ApiModelProperty(value = "企业名称")
    private String ename;
}
