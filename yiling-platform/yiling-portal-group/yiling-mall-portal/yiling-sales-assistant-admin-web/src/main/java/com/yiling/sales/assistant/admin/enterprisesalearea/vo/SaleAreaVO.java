package com.yiling.sales.assistant.admin.enterprisesalearea.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 可售区域 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022/1/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class SaleAreaVO extends BaseVO {


    /**
     * 公司名称
     */
    @ApiModelProperty("公司名称")
    private String name;

    /**
     * 销售区域描述
     */
    @ApiModelProperty("销售区域描述")
    private String description;

}
