package com.yiling.sjms.crm.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmHospitalDrugstoreRelationVO extends BaseVO {

    /**
     * 院外药店机构编码
     */
    @ApiModelProperty(value = "院外药店机构编码")
    private Long drugstoreOrgId;

    /**
     * 院外药店机构名称
     */
    @ApiModelProperty(value = "院外药店机构名称")
    private String drugstoreOrgName;

    /**
     * 医疗机构编码
     */
    @ApiModelProperty(value = "医疗机构编码")
    private Long hospitalOrgId;

    /**
     * 医疗机构名称
     */
    @ApiModelProperty(value = "医疗机构名称")
    private String hospitalOrgName;

    /**
     * 品种名称
     */
    @ApiModelProperty(value = "品种名称")
    private String categoryName;

    /**
     * 标准产品编码
     */
    @ApiModelProperty(value = "标准产品编码")
    private Long crmGoodsCode;

    /**
     * 标准产品名称
     */
    @ApiModelProperty(value = "标准产品名称")
    private String crmGoodsName;

    /**
     * 标准产品规格
     */
    @ApiModelProperty(value = "标准产品规格")
    private String crmGoodsSpec;

    /**
     * 开始生效时间
     */
    @ApiModelProperty(value = "开始生效时间")
    private Date effectStartTime;

    /**
     * 结束生效时间
     */
    @ApiModelProperty(value = "结束生效时间")
    private Date effectEndTime;

    /**
     * 是否停用 0-否 1-是
     */
    @ApiModelProperty(value = "是否停用 0-否 1-是")
    private Integer disableFlag;

    /**
     * 状态 1-已停用 2-未生效 3-生效中 4-已过期
     */
    @ApiModelProperty(value = "状态 1-已停用 2-未生效 3-生效中 4-已过期")
    private Integer status;

    /**
     * 数据来源 1-导入数据 2-审批流数据
     */
    @ApiModelProperty(value = "数据来源 1-导入数据 2-审批流数据")
    private Integer dataSource;

    /**
     * 最后操作时间
     */
    @ApiModelProperty(value = "最后操作时间")
    private Date lastOpTime;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Long lastOpUser;

    /**
     * 操作人姓名
     */
    @ApiModelProperty(value = "操作人姓名")
    private String lastOpUserName;
}
