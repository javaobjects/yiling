package com.yiling.sjms.manor.vo;


import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 辖区机构品类关系
 * </p>
 *
 * @author xueli.ji
 * @date 2023-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseRelationManorVO extends BaseVO {

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
     * 品类名称
     */
    @ApiModelProperty(value = "品种")
    private String categoryName;




    @ApiModelProperty(value = "辖区编码变更前")
    private String manorNo;
    /**
     * 辖区名称
     */
    @ApiModelProperty(value = "辖区名称变更前")
    private String manorName;


    /**
     * 新辖区名称
     */
    @ApiModelProperty(value = "辖区名称变更后")
    private String newManorName;
    /**
     * 新辖区编码
     */
    @ApiModelProperty(value = "辖区编码变更后")
    private String newManorNo;



}
