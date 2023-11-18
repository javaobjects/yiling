package com.yiling.sjms.sale.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: gxl
 * @date: 2023/4/18
 */
@Data
public class TargetResolveDetailVO extends Page<SaleDepartmentSubTargetResolveDetailVO> {
   @ApiModelProperty(value = "模板地址")
    private String templateUrl;
}