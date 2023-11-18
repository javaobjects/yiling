package com.yiling.f2b.admin.procrelation.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/6/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryChannelPartnerPageForm extends QueryPageListForm {

    @ApiModelProperty("客户名称")
    private String name;

    /**
     * 渠道ID
     */
    @ApiModelProperty("渠道ID 0-全部 3-一级商 4-二级商 5-KA用户----仅在建采管理列表页需传此字段")
    private Long channelId;

}
