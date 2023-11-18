package com.yiling.export.imports.model;


import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import com.yiling.export.excel.model.BaseImportModel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 导入医药代表可售药品数据 Form
 *
 * @author: xuan.zhou
 * @date: 2022/6/7
 */
@Data
public class ImportGbFormReviewDataModel extends BaseImportModel {

    @NotEmpty(message = "不能为空")
    @Excel(name = "团购编号", orderNum = "0")
    private String gbNo;

    @NotEmpty(message = "不能为空")
    @Excel(name = "复核意见", orderNum = "1")
    private String reviewReply;

}
