package com.yiling.admin.system.basic.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySmsRecordPageForm extends QueryPageListForm {
    /**
     * 接收人手机号
     */
    @ApiModelProperty(value = "接收人手机号")
    private String mobile;

    /**
     * 开始创建时间
     */
    @ApiModelProperty(value = "开始创建时间")
    private Date startCreateTime;

    /**
     * 结束创建时间
     */
    @ApiModelProperty(value = "结束创建时间")
    private Date endCreateTime;

    /**
     * 发送状态：1-待发送 2-发送成功 3-发送失败
     */
    @ApiModelProperty(value = "发送状态：1-待发送 2-发送成功 3-发送失败")
    private Integer status;
}
