package com.yiling.sjms.manor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2023/5/15
 */
@Data
@Accessors(chain = true)
public class ManorRelationVO {
    /**
     * 子表单id
     */
    @ApiModelProperty(value = "辖区变更明细id")
    private Long id;
    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品种")
    private String categoryName;



    @ApiModelProperty(value = "旧辖区编码")
    private String manorNo;
    /**
     * 辖区名称
     */
    @ApiModelProperty(value = "辖区名称")
    private String manorName;


    /**
     * 新辖区名称
     */
    @ApiModelProperty(value = "新辖区名称")
    private String newManorName;
    /**
     * 新辖区编码
     */
    @ApiModelProperty(value = "新辖区编码")
    private String newManorNo;

    /**
     * 品种id
     */
    @ApiModelProperty(value = "品种id")
    private Long categoryId;

    /**
     * 旧辖区id
     */
    @ApiModelProperty(value = "旧辖区id")
    private Long manorId;

    /**
     * 新辖区id
     */
    @ApiModelProperty(value = "新辖区id")
    private Long newManorId;

    /**
     * 是否选中
     */
    @ApiModelProperty(value = "是否选中")
    private Boolean isChecked;
}