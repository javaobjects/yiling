package com.yiling.admin.erp.enterprise.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ERP流向封存信息列表分页VO
 *
 * @author: houjie.sun
 * @date: 2022/4/14
 */
@Data
public class ErpSealedPageVO extends BaseVO {

    /**
     * 商业ID
     */
    @ApiModelProperty(value = "商业ID")
    private Long eid;

    /**
     * 商业名称
     */
    @ApiModelProperty(value = "商业名称")
    private String ename;

    /**
     * 流向类型，字典(erp_flow_type)：1-采购流向 2-销售流向 0-全部
     */
    @ApiModelProperty(value = "流向类型，字典(erp_flow_type)：1-采购流向 2-销售流向 0-全部")
    private Integer type;

    /**
     * 封存状态，字典(erp_flow_sealed_status)：1-已解封 2-已封存 0-全部
     */
    @ApiModelProperty(value = "封存状态，字典(erp_flow_sealed_status)：1-已解封 2-已封存 0-全部")
    private Integer status;

    /**
     * 封存月份
     */
    @ApiModelProperty(value = "封存月份(\"2022年03月\")")
    private String month;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operName;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private Date opTime;
}
