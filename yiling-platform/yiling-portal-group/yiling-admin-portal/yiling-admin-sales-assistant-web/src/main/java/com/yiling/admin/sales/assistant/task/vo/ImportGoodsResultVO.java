package com.yiling.admin.sales.assistant.task.vo;

import java.util.List;

import com.yiling.framework.oss.vo.ImportResultVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: gxl
 * @date: 2022/9/27
 */
@Data
public class ImportGoodsResultVO extends ImportResultVO {
    /**
     * 结果集
     */
    @ApiModelProperty(value = "成功结果集")
    private List<TaskSelectGoodsVO> successList;

}