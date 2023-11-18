package com.yiling.admin.cms.content.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 医生对象
 *
 * @author shenfan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DoctorVO extends BaseVO {

    /**
     * 医生名称
     */
    @ApiModelProperty(value = "医生名称")
    private String doctorName;

    /**
     * 所在医疗机构
     */
    @ApiModelProperty(value = "所在医疗机构")
    private String hospitalName;

    /**
     * 所在医疗机构科室
     */
    @ApiModelProperty(value = "所在医疗机构科室")
    private String hospitalDepartment;


}
