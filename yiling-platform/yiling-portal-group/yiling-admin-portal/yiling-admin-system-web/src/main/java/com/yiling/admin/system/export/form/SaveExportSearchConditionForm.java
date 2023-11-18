package com.yiling.admin.system.export.form;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * export导出查询条件对象
 * 
 * @author jian.mei
 * @date 2020-05-05
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class SaveExportSearchConditionForm implements Serializable {

	private static final long serialVersionUID = 3250693887083163129L;

	@ApiModelProperty(value = "字段的英文名称")
	private String name;

	@ApiModelProperty(value = "字段的中文名")
	private String desc;

	@ApiModelProperty(value = "字段的值")
	private String value;

	@ApiModelProperty(value = "字段的值中文描述。比如：0否1是")
	private String valueDescription;

	@ApiModelProperty(value = "是否显示。隐藏字段传0，显示字段传1")
	private Integer visibility;

}
