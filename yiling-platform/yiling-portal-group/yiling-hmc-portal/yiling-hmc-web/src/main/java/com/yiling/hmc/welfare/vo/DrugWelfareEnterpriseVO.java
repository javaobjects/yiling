package com.yiling.hmc.welfare.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author fan.shen
 * @date 2022-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DrugWelfareEnterpriseVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业id
     */
    @ApiModelProperty("企业id")
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;

    /**
     * 药品福利id
     */
    @ApiModelProperty("药品福利id")
    private Long drugWelfareId;

}
