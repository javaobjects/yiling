package com.yiling.open.cms.read.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/7/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMyReadPageForm extends QueryPageListForm {
    /**
     * 标题
     */
    private String title;

    @NotNull
    private Long wxDoctorId;
}