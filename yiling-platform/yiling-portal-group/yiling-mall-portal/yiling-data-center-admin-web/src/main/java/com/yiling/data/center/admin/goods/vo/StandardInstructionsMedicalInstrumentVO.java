package com.yiling.data.center.admin.goods.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 StandardInstructionsMedicalInstrumentVO
 * @描述
 * @创建时间 2022/7/19
 * @修改人 shichen
 * @修改时间 2022/7/19
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardInstructionsMedicalInstrumentVO extends BaseVO {
    /**
     * 组成结构
     */
    @ApiModelProperty(value = "组成结构")
    private String structure;

    /**
     * 注意事项
     */
    @ApiModelProperty(value = "注意事项")
    private String noteEvents;

    /**
     * 包装
     */
    @ApiModelProperty(value = "包装")
    private String packingInstructions;

    /**
     * 保质期
     */
    @ApiModelProperty(value = "保质期")
    private String expirationDate;

    /**
     * 使用范围
     */
    @ApiModelProperty(value = "使用范围")
    private String useScope;

    /**
     * 使用方法
     */
    @ApiModelProperty(value = "使用方法")
    private String usageDosage;

    /**
     * 存储条件
     */
    @ApiModelProperty(value = "存储条件")
    private String storageConditions;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
