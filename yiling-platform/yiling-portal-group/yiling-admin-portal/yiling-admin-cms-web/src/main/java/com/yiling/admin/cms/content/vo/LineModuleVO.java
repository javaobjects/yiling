package com.yiling.admin.cms.content.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
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
public class LineModuleVO {

    /**
     * 业务线Id
     */
    @ApiModelProperty(value = "业务线Id")
    private Long lineId;

    /**
     * 板块list
     */
    @ApiModelProperty(value = "板块list")
    private List<ModuleVO> moduleList;

}
