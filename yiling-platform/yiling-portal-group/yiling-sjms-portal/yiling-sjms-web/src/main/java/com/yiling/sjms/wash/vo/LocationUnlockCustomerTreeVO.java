package com.yiling.sjms.wash.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LocationUnlockCustomerTreeVO extends BaseVO {

    /**
     * 区域编码
     */
    @ApiModelProperty(value = "省/市/区编码")
    private String code;

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "省/市/区名称")
    private String name;

    /**
     * 上级区域编码
     */
    @ApiModelProperty(value = "上级区域编码")
    private String parentCode;

    /**
     * 排序优先级
     */
    @ApiModelProperty(value = "排序优先级")
    private Integer priority;


    @ApiModelProperty(value = "是否勾选")
    private Boolean checkFlag = false;

    /**
     * 下级区域列表
     */
    @ApiModelProperty(value = "下级区域列表")
    private List<LocationUnlockCustomerTreeVO> children;
}
