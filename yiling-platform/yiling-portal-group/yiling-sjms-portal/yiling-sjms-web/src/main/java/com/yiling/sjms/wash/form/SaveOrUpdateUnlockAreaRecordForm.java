package com.yiling.sjms.wash.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateUnlockAreaRecordForm extends BaseForm {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 非锁客户分类
     */
    @NotNull(message = "非锁客户分类不可为空")
    @ApiModelProperty(value = "非锁客户分类")
    private Integer customerClassification;

    /**
     * 品种id
     */
    @NotNull(message = "品种不可为空")
    @ApiModelProperty(value = "品种id")
    private Long categoryId;


    /**
     * 销量计入类型：1-销量计入主管 2-销量计入代表
     */
    @NotNull(message = "计入类型不可为空")
    @ApiModelProperty(value = "销量计入类型 1-销量计入主管 2-销量计入代表")
    private Integer type;

    /**
     * 代表岗位代码
     */
    @ApiModelProperty(value = "代表岗位代码")
    private String representativePostCode;

    /**
     * 代表岗位名称
     */
    @ApiModelProperty(value = "代表岗位名称")
    private String representativePostName;

    /**
     * 代表工号
     */
    @ApiModelProperty(value = "代表工号")
    private String representativeCode;

    /**
     * 代表姓名
     */
    @ApiModelProperty(value = "代表姓名")
    private String representativeName;

    /**
     * 主管岗位代码
     */
    @ApiModelProperty(value = "主管岗位代码")
    private String executivePostCode;

    /**
     * 主管岗位名称
     */
    @ApiModelProperty(value = "主管岗位名称")
    private String executivePostName;

    /**
     * 主管工号
     */
    @ApiModelProperty(value = "主管工号")
    private String executiveCode;

    /**
     * 主管姓名
     */
    @ApiModelProperty(value = "主管姓名")
    private String executiveName;

    /**
     * 业务部门
     */
    @ApiModelProperty(value = "业务部门")
    private String department;

    /**
     * 业务省区
     */
    @ApiModelProperty(value = "业务省区")
    private String province;

    /**
     * 业务区域
     */
    @ApiModelProperty(value = "业务区域（区办）")
    private String area;

    @ApiModelProperty(value = "设置业务区域")
    private List<String> regionCodeList;

    @ApiModelProperty(value = "备注")
    private String remark;

}
