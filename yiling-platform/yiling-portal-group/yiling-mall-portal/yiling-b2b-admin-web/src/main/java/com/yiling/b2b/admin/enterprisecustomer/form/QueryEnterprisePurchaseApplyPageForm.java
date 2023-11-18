package com.yiling.b2b.admin.enterprisecustomer.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业采购申请分页列表 Form
 *
 * @author: lun.yu
 * @date: 2022/01/17
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEnterprisePurchaseApplyPageForm extends QueryPageListForm {

    /**
     * 企业名称（全模糊查询）
     */
    @Length(max = 50)
    @ApiModelProperty("企业名称")
    private String name;

    /**
     * 执业许可证号/社会信用统一代码
     */
    @Length(max = 50)
    @ApiModelProperty("执业许可证号/社会信用统一代码")
    private String licenseNumber;

    /**
     * 所属省份编码
     */
    @Length(max = 20)
    @ApiModelProperty("所属省份编码")
    private String provinceCode;

    /**
     * 所属城市编码
     */
    @Length(max = 20)
    @ApiModelProperty("所属城市编码")
    private String cityCode;

    /**
     * 所属区域编码
     */
    @Length(max = 20)
    @ApiModelProperty("所属区域编码")
    private String regionCode;

    /**
     * 类型：0-全部 1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    @ApiModelProperty("类型：0-全部 1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所")
    private Integer type;

    /**
     * 审核状态：1-待审核 2-已建采 3-已驳回
     */
    @NotNull
    @ApiModelProperty("审核状态：1-待审核 2-已建采 3-已驳回")
    private Integer authStatus;

}
