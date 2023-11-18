package com.yiling.admin.data.center.report.vo;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 中台-报表参数
 * </p>
 *
 * @author gxl
 * @date 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReportParamVO extends BaseVO {


    /**
     * 参数名
     */
    private String paramName;

    /**
     * 备注
     */
    private String remark;


}
