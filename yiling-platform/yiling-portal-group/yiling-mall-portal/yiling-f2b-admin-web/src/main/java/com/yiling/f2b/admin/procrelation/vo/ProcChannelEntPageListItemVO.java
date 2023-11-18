package com.yiling.f2b.admin.procrelation.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 以岭客户
 *
 * @author: dexi.yao
 * @date: 2021/6/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ProcChannelEntPageListItemVO extends BaseVO {

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String name;

    /**
     * 渠道ID
     */
    @ApiModelProperty("渠道字典channel_type")
    private Long channelId;

}
