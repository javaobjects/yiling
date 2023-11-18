package com.yiling.f2b.admin.procrelation.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/6/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryProcRelationSnapshotDetailPageForm extends QueryPageListForm {

    /**
     * 采购关系id
     */
    @NotBlank
    @ApiModelProperty(value = "采购关系快照的版本id")
    private String versionId;

}
