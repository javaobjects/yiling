package com.yiling.admin.hmc.common.form;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/4/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AdvertisementSaveForm extends BaseForm {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("标题")
    @NotBlank(message = "广告标题不能为空")
    private String title;

    @ApiModelProperty("图片地址")
    @NotBlank(message = "图片不能为空")
    private String pic;

    @ApiModelProperty("跳转类型 1-h5跳转，2-小程序内部跳转")
    @NotNull(message = "跳转类型不能为空")
    private Integer redirectType;

    @ApiModelProperty("链接地址")
    private String url;

    @ApiModelProperty("有效起始时间")
    private Date startTime;

    @ApiModelProperty("有效截止时间")
    private Date stopTime;

    @ApiModelProperty("投放位置:1-C端用户侧首页 2-C端用户侧我的")
    private List<Integer> position;

    @ApiModelProperty("显示顺序")
    @Min(value = 1, message = "排序权重范围为1-200")
    @Max(value = 200, message = "排序权重范围为1-200")
    private Integer sort;
}
