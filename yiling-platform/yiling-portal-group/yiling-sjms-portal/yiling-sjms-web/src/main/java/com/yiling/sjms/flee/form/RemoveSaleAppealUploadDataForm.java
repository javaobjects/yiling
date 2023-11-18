package com.yiling.sjms.flee.form;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: xinxuan.jia
 * @date: 2023/6/26
 */
@Data
@Accessors(chain = true)
public class RemoveSaleAppealUploadDataForm {
    /**
     * 销量申诉表单id集合
     */
    @ApiModelProperty("销量申诉表单id集合")
    private List<Long> ids;
}
