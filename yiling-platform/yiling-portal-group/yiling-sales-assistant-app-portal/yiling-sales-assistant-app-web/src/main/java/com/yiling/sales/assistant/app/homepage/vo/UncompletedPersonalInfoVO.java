package com.yiling.sales.assistant.app.homepage.vo;

import java.util.List;

import com.yiling.sales.assistant.app.system.vo.LocationTreeVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 待完善的个人信息 VO
 *
 * @author: xuan.zhou
 * @date: 2022/1/17
 */
@Data
public class UncompletedPersonalInfoVO {

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 身份证号
     */
    @ApiModelProperty("身份证号")
    private String idNumber;

    /**
     * 销售区域描述
     */
    @ApiModelProperty("销售区域描述")
    private String salesAreaDesc;

    /**
     * 销售区域树
     */
    @ApiModelProperty("销售区域树")
    private List<LocationTreeVO> salesAreaTree;
}
