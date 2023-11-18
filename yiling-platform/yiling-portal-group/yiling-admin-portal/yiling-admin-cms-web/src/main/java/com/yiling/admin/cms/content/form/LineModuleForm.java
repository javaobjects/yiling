package com.yiling.admin.cms.content.form;

import com.yiling.admin.cms.content.vo.ModuleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 业务线模块设置
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
@Data
@Accessors(chain = true)
public class LineModuleForm {

    /**
     * 业务线Id
     */
    @ApiModelProperty(value = "业务线Id")
    private Long lineId;

    /**
     * 业务线名称
     */
    @ApiModelProperty(value = "业务线名称")
    private String lineName;

    /**
     * 板块list
     */
    @ApiModelProperty(value = "板块list")
    private List<ModuleForm> moduleList;

}
