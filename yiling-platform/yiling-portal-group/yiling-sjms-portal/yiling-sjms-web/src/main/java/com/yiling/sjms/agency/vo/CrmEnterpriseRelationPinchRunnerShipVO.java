package com.yiling.sjms.agency.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/4/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmEnterpriseRelationPinchRunnerShipVO extends BaseVO {

    /**
     * 三者关系ID
     */
    private Long enterpriseCersId;

    /**
     * 产品组id
     */
    @ApiModelProperty("产品组")
    private Long productGroupId;

    /**
     * 产品组名称
     */
    @ApiModelProperty("产品组名称")
    private String productGroupName;

    /**
     * 品种ID
     */
    @ApiModelProperty("品种ID")
    private Long categoryId;


    /**
     * 品种
     */
    @ApiModelProperty("品种")
    private String categoryName;

    /**
     * 辖区ID
     */
    @ApiModelProperty("辖区ID")
    private Long manorId;

    /**
     * 辖区
     */
    @ApiModelProperty("辖区")
    private String manorName;

    /**
     * 业务代表工号
     */
    @ApiModelProperty("业务代表工号")
    private String representativeCode;

    /**
     * 业务代表名称
     */
    @ApiModelProperty("业务代表名称")
    private String representativeName;

    /**
     * 业务代表岗位代码
     */
    @ApiModelProperty("业务代表岗位代码")
    private Long representativePostCode;

    /**
     * 业务代表岗位名称
     */
    @ApiModelProperty("业务代表岗位名称")
    private String representativePostName;

    /**
     * 业务代表业务部门
     */
    @ApiModelProperty("业务代表业务部门")
    private String representativeDepartment;

    /**
     * 业务代表业务省区
     */
    @ApiModelProperty("业务代表业务省区")
    private String representativeProvinc;

    /**
     * 业务代表业务区域
     */
    @ApiModelProperty("业务代表业务区域")
    private String representativeArea;

    /**
     * 是否已被当前代跑三者关系选择：true-已选择 false-未选择
     */
    @ApiModelProperty("是否已被当前代跑三者关系选择：true-已选择 false-未选择")
    private Boolean  currentSelectedFlag;

    /**
     * 是否已被其他代跑三者关系选择：true-已选择 false-未选择
     */
    @ApiModelProperty("是否已被其他代跑三者关系选择：true-已选择 false-未选择")
    private Boolean  otherSelectedFlag;

    /**
     * 初始化默认值
     */
    public CrmEnterpriseRelationPinchRunnerShipVO() {
        this.currentSelectedFlag = false;
        this.otherSelectedFlag = false;
    }

}
