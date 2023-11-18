package com.yiling.admin.system.system.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建菜单 Form
 *
 * @author lun.yu
 * @date 2021/7/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class SaveMenuForm extends BaseForm {

	@ApiModelProperty(value = "菜单ID")
	private Long id;

	@NotNull
	@ApiModelProperty(value = "应用ID：1-运营后台 2-POP后台 3-B2B后台 4-互联网医院后台 5-数据中台 6-销售助手",required = true)
	private Integer appId;

	@NotNull
	@ApiModelProperty(value = "父级id",required = true)
	private Long parentId;

	@Range(min = 1,max = 3)
	@NotNull
	@ApiModelProperty(value = "权限类型：1-目录 2-菜单 3-按钮",required = true)
	private Integer menuType;

	@Length(max = 50)
	@ApiModelProperty(value = "菜单名称",required = true)
	private String menuName;

	@Length(max = 2000)
	@ApiModelProperty(value = "接口url")
	private String menuUrl;

	@Length(max = 50)
	@ApiModelProperty(value = "菜单或按钮标识")
	private String menuIdentification;

	@Length(max = 100)
	@ApiModelProperty(value = "菜单图标")
	private String menuIcon;

	@Range(min = 0)
	@ApiModelProperty(value = "菜单排序")
	private Integer sortNum;

	@Range(min = 1,max = 2)
	@ApiModelProperty(value = "菜单状态：1-启用 2-停用")
	private Integer menuStatus;

	@Length(max = 200)
	@ApiModelProperty(value = "备注")
	private String remark;

}
