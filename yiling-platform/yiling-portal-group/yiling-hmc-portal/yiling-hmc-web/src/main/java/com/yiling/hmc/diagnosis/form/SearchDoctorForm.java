package com.yiling.hmc.diagnosis.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 找医生详情 form
 *
 * @author: fan.shen
 * @date: 2024/5/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SearchDoctorForm extends QueryPageListForm {

    @ApiModelProperty(value = "搜索内容")
    private String content;

    @ApiModelProperty(value = "一级科室id")
    private Integer departmentParentId;

    @ApiModelProperty(value = "二级科室id")
    private Integer departmentId;

    @ApiModelProperty(value = "排序")
    private String sort;

    @ApiModelProperty(value = "服务类型")
    private List<String> type;

    @ApiModelProperty(value = "职称")
    private List<String> profession;

    @ApiModelProperty(value = "医院级别")
    private String hospitalLevel;

    @ApiModelProperty(value = "医院性质")
    private List<String> hospitalType;
}