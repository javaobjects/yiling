package com.yiling.admin.data.center.goods.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/3
 */
@Data
public class EnterpriseGoodsListVO {
    @ApiModelProperty(value = "企业ID")
    private Long    id;
    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer status;
    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String  name;
    /**
     * 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    @ApiModelProperty(value = "类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所")
    private Integer type;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contactor;

    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private String contactorPhone;

    /**
     * 执业许可证号/社会信用统一代码
     */
    @ApiModelProperty(value = "执业许可证号/社会信用统一代码")
    private String licenseNumber;

    /**
     * 所属省份编码
     */
    @ApiModelProperty(value = "所属省份编码")
    private String provinceCode;

    /**
     * 所属省份名称
     */
    @ApiModelProperty(value = "所属省份名称")
    private String provinceName;

    /**
     * 所属城市编码
     */
    @ApiModelProperty(value = "所属城市编码")
    private String cityCode;

    /**
     * 所属城市名称
     */
    @ApiModelProperty(value = "所属城市名称")
    private String cityName;

    /**
     * 所属区域编码
     */
    @ApiModelProperty(value = "所属区域编码")
    private String regionCode;

    /**
     * 所属区域名称
     */
    @ApiModelProperty(value = "所属区域名称")
    private String regionName;

    /**
     * 企业地址
     */
    @ApiModelProperty(value = "企业地址")
    private String address;

    /**
     * 审核通过
     */
    @ApiModelProperty(value = "审核通过个数")
    private Long auditPassCount;
    /**
     * 待审核
     */
    @ApiModelProperty(value = "待审核个数")
    private Long underReviewCount;
    /**
     * 驳回
     */
    @ApiModelProperty(value = "驳回个数")
    private Long rejectCount;
}
