package com.yiling.sjms.flee.vo;

import java.util.Date;

import com.yiling.sjms.form.vo.FormVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/3/11 0011
 */
@Data
public class FleeingFormVO extends FormVO {

    @ApiModelProperty(value = "申报类型 1-电商、2-非电商")
    private Integer reportType;

    /**
     * 确认状态：1-待确认 2-生成中 3-生成成功 4-生成失败
     */
    @ApiModelProperty(value = "申报类型 1-电商、2-非电商")
    private Integer confirmStatus;

    /**
     * 确认人工号
     */
    @ApiModelProperty(value = "确认人工号")
    private String confirmUserId;

    /**
     * 确认人名称
     */
    @ApiModelProperty(value = "确认人名称")
    private String confirmUserName;

    /**
     * 生成流向表单时间(提交清洗)
     */
    @ApiModelProperty(value = "生成流向表单时间(提交清洗)")
    private Date submitWashTime;
}
