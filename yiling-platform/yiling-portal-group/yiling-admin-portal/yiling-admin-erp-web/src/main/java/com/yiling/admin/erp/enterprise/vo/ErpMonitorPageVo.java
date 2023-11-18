package com.yiling.admin.erp.enterprise.vo;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/1/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("ERP监控信息列表分页")
public class ErpMonitorPageVo<T> extends Page<T> {

    /**
     * erp对接请求次数阈值信息列表
     */
    @ApiModelProperty(value = "erp对接请求次数阈值信息列表")
    private List<ErpMonitorCountInfoVO> monitorCountInfoList;

}
