package com.yiling.sjms.flow.vo;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shichen
 * @类名 MappingPageVO
 * @描述
 * @创建时间 2023/3/6
 * @修改人 shichen
 * @修改时间 2023/3/6
 **/
@Data
public class MappingPageVO<T> extends Page<T> {

    /**
     * 客户对照流向清洗控制
     */
    @ApiModelProperty("客户对照流向清洗控制")
    private FlowMonthWashControlVO customerMappingWashControl;

    /**
     * 供应商对照流向清洗控制
     */
    @ApiModelProperty("供应商对照流向清洗控制")
    private FlowMonthWashControlVO supplierMappingWashControl;

}
