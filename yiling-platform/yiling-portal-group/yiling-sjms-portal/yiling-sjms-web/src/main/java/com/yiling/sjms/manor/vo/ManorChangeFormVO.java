package com.yiling.sjms.manor.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 医院辖区变更表单
 * </p>
 *
 * @author gxl
 * @date 2023-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ManorChangeFormVO extends BaseVO {


    /**
     * 医院id
     */
    @ApiModelProperty(value = "机构id")
    private Long crmEnterpriseId;

    /**
     * 医院名称
     */
    @ApiModelProperty(value = "机构名称")
    private String enterpriseName;


    /**
     * 品种名称
     */
    @ApiModelProperty(value = "品种名称")
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

    /**
     * form表主键
     */
    @ApiModelProperty(value = "formId")
    private Long formId;
    /**
     * 数据归档：1-开启 2-关闭
     */
    @ApiModelProperty(value = "数据归档：1-开启 2-关闭")
    private Integer archiveStatus;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
