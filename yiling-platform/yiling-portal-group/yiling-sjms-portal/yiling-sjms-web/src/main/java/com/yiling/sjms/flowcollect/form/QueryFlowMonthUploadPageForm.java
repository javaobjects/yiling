package com.yiling.sjms.flowcollect.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询月流向上传记录分页列表 Form
 *
 * @author lun.yu
 * @date 2023-03-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowMonthUploadPageForm extends QueryPageListForm {

    /**
     * 文件名
     */
    @ApiModelProperty(value = "文件名")
    private String fileName;

    /**
     * 开始上传时间
     */
    @ApiModelProperty(value = "开始上传时间")
    private Date startCreateTime;

    /**
     * 结束上传时间
     */
    @ApiModelProperty(value = "结束上传时间")
    private Date endCreateTime;

    /**
     * 流向数据类型：1-销售 2-库存 3-采购
     */
    @ApiModelProperty("流向数据类型：1-销售 2-库存 3-采购")
    private Integer dataType;

    /**
     * 检查状态：1-通过 2-未通过 3-警告
     */
    @ApiModelProperty(value = "检查状态：1-通过 2-未通过 3-警告")
    private Integer checkStatus;

    /**
     * 导入状态：1-导入成功 2-导入失败
     */
    @ApiModelProperty(value = "导入状态：1-导入成功 2-导入失败")
    private Integer importStatus;

}
