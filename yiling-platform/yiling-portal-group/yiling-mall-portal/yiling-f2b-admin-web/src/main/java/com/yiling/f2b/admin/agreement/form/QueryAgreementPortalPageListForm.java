package com.yiling.f2b.admin.agreement.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryAgreementPortalPageListForm extends QueryPageListForm {

    @ApiModelProperty("客户名称")
    private String name;

    @ApiModelProperty("客户省份编码")
    private String provinceCode;

    @ApiModelProperty("客户城市编码")
    private String cityCode;

    @ApiModelProperty("客户区域编码")
    private String regionCode;

    @ApiModelProperty("执业许可证号/社会信用统一代码")
    private String licenseNumber;
    /**
     * 渠道ID
     */
    @ApiModelProperty("渠道ID 0-全部 2-工业直属 3-一级商 4-二级商 5-KA用户 ")
    private Long channelId;
}
