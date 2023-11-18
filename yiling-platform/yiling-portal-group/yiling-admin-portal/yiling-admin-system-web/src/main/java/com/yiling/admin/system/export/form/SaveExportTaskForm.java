package com.yiling.admin.system.export.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/5/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveExportTaskForm extends BaseForm {

    /**
     * 所属组名
     */
    @ApiModelProperty(value = "所属组名")
    private String groupName;

    /**
     * 实现BaseExportQueryDataService接口的类名
     */
    @ApiModelProperty(value = "实现BaseExportQueryDataService接口的类名")
    private String className;

    /**
     * 调用菜单路径
     */
    @ApiModelProperty(value = "调用菜单路径")
    private String menuName;

    /**
     * 下载文件的名称
     */
    @ApiModelProperty(value = "下载文件的名称")
    private String fileName;

    /**
     * 查询条件对象列表
     */
    @ApiModelProperty(value = "查询条件对象列表")
    private List<SaveExportSearchConditionForm> searchConditionList;
}
