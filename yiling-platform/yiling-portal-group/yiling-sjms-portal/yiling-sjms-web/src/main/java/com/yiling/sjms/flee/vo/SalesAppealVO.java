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
public class SalesAppealVO extends FormVO {

    @ApiModelProperty(value = "申诉类型 1补传月流向、2调整月流向、3代表终端对应错误、4终端类型申诉、5其他")
    private Integer appealType;

    /**
     * 1-待提交 2-已提交"
     */
    @ApiModelProperty(value = "1-待提交 2-已提交")
    private Integer confirmStatus;

    /**
     * 提交清洗时间
     */
    @ApiModelProperty(value = "提交清洗时间")
    private Date submitWashTime;

    @ApiModelProperty(value = "确认人")
    private String confirmUserName;
}
