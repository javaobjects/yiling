package com.yiling.admin.cms.content.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: gxl
 * @date: 2022/6/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryDiseasePageForm extends QueryPageListForm {
    @NotNull
   private   String diseaseName;
}