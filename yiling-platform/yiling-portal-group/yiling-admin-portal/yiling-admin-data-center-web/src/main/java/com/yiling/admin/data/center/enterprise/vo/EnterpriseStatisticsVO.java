package com.yiling.admin.data.center.enterprise.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业统计信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/8
 */
@Data
public class EnterpriseStatisticsVO {

    /**
     * 总数量
     */
    @ApiModelProperty("总数量")
    private Long totalCount;

    /**
     * 工业数量
     */
    @ApiModelProperty("工业数量")
    private Long industryCount;

    /**
     * 商业数量
     */
    @ApiModelProperty("商业数量")
    private Long businessCount;

    /**
     * 启用数量
     */
    @ApiModelProperty("启用数量")
    private Long enabledCount;

    /**
     * 停用数量
     */
    @ApiModelProperty("停用数量")
    private Long disabledCount;

    /**
     * 终端数量
     */
    @ApiModelProperty("终端数量")
    private Long terminalCount;

    /**
     * 医疗机构数量
     */
    @ApiModelProperty("医疗机构数量")
    private Long hospitalCount;

    /**
     * 连锁总店数量
     */
    @ApiModelProperty("连锁总店数量")
    private Long chainBaseCount;

    /**
     * 连锁直营数量
     */
    @ApiModelProperty("连锁直营数量")
    private Long chainDirectCount;

    /**
     * 连锁加盟数量
     */
    @ApiModelProperty("连锁加盟数量")
    private Long chainJoinCount;

    /**
     * 单体药房数量
     */
    @ApiModelProperty("单体药房数量")
    private Long pharmacyCount;

    /**
     * 诊所数量
     */
    @ApiModelProperty("诊所数量")
    private Long clinicCount;

    /**
     * 民营医院数量
     */
    @ApiModelProperty("民营医院数量")
    private Long privateHospitalCount;

    /**
     * 三级医院数量
     */
    @ApiModelProperty("三级医院数量")
    private Long level3HospitalCount;

    /**
     * 二级医院数量
     */
    @ApiModelProperty("二级医院数量")
    private Long level2HospitalCount;

    /**
     * 社区中心数量
     */
    @ApiModelProperty("社区中心数量")
    private Long communityCenterCount;

    /**
     * 县镇卫生院数量
     */
    @ApiModelProperty("县镇卫生院数量")
    private Long healthCenterCount;

    /**
     * 社区站/村卫生所数量
     */
    @ApiModelProperty("社区站/村卫生所数量")
    private Long communityStationCount;

    /**
     * 县人民/中医院数量
     */
    @ApiModelProperty("县人民/中医院数量")
    private Long peopleHospitalCount;

}
