package com.yiling.sales.assistant.app.task.vo;

import java.io.Serializable;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 选择终端
 * @author ray
 * @date 2021-9-29
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TaskTerminalVO extends BaseVO implements Serializable {

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String   name;



    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private String   contactorPhone;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private String   provinceName;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String   cityName;

    /**
     * 区域
     */
    @ApiModelProperty(value = "区域")
    private String   regionName;

    /**
     * 企业地址
     */
    @ApiModelProperty(value = "企业地址")
    private String   address;


}
