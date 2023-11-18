package com.yiling.hmc.activity.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 身份证照片正面照 OCR VO
 *
 * @author: fan.shen
 * @date: 2022/9/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IdCardFrontPhotoOcrVO extends BaseVO {

    @ApiModelProperty(value = "名称",example = "张三")
    private String name;

    @ApiModelProperty(value = "身份证",example = "101102199809089988")
    private String idNo;

    @ApiModelProperty(value = "性别",example = "女")
    private String gender;

    @ApiModelProperty(value = "民族",example = "汉")
    private String nation;

    @ApiModelProperty(value = "生日",example = "1991/5/25")
    private String birth;

    @ApiModelProperty(value = "省code", required = true)
    private String provinceCode;

    @ApiModelProperty(value = "省名称", required = true)
    private String provinceName;

    @ApiModelProperty(value = "市code", required = true)
    private String cityCode;

    @ApiModelProperty(value = "市名称", required = true)
    private String cityName;

    @ApiModelProperty(value = "区code", required = true)
    private String regionCode;

    @ApiModelProperty(value = "区名称", required = true)
    private String regionName;


    @ApiModelProperty(value = "地址",example = "广东省深圳市南山区深南大道10000号")
    private String address;

}
