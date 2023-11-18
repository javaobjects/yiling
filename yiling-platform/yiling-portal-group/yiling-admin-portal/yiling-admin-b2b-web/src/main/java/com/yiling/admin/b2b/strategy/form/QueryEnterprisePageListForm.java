package com.yiling.admin.b2b.strategy.form;

import java.util.List;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业分页列表 Form
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEnterprisePageListForm extends QueryPageListForm {

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Long id;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String name;

    /**
     * 联系人
     */
    @ApiModelProperty("联系人")
    private String contactor;

    /**
     * 联系人电话
     */
    @ApiModelProperty("联系人电话")
    private String contactorPhone;

    /**
     * 执业许可证号/社会信用统一代码
     */
    @ApiModelProperty("执业许可证号/社会信用统一代码")
    private String licenseNumber;

    /**
     * 所属省份编码
     */
    @ApiModelProperty("所属省份编码")
    private String provinceCode;

    /**
     * 所属城市编码
     */
    @ApiModelProperty("所属城市编码")
    private String cityCode;

    /**
     * 所属区域编码
     */
    @ApiModelProperty("所属区域编码")
    private String regionCode;

    /**
     * 类型：0-全部 1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    @ApiModelProperty("类型：0-全部 1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所")
    private Integer type;

    /**
     * 状态：0-全部 1-启用 2-停用
     */
    @ApiModelProperty("状态：0-全部 1-启用 2-停用")
    private Integer status;

    /**
     * 认证状态：0-全部 1-未认证 2-认证通过 3-认证不通过
     */
    @ApiModelProperty("认证状态：0-全部 1-未认证 2-认证通过 3-认证不通过")
    private Integer authStatus;

    /**
     * 标签ID列表
     */
    @ApiModelProperty("标签ID列表")
    private List<Long> tagIds;

    /**
     * ERP对接级别：0-未对接 1-基础对接 2-订单对接 3-发货单对接
     */
    @ApiModelProperty("ERP对接级别：0-未对接 1-基础对接 2-订单对接 3-发货单对接")
    private Integer erpSyncLevel;
}
