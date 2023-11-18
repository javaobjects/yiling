package com.yiling.admin.b2b.member.form;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-企业会员分页列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEnterpriseMemberPageForm extends QueryPageListForm {

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Long eid;

    /**
     * 企业名称
     */
    @Length(max = 30)
    @ApiModelProperty(value = "企业名称")
    private String ename;

    /**
     * 企业类型
     */
    @ApiModelProperty(value = "企业类型")
    private Integer type;

    /**
     * 社会统一信用代码
     */
    @ApiModelProperty(value = "社会统一信用代码")
    private String licenseNumber;

    /**
     * 所属省份编码
     */
    @Length(max = 20)
    @ApiModelProperty(value = "所属省份编码")
    private String provinceCode;

    /**
     * 所属城市编码
     */
    @Length(max = 20)
    @ApiModelProperty(value = "所属城市编码")
    private String cityCode;

    /**
     * 所属区域编码
     */
    @Length(max = 20)
    @ApiModelProperty(value = "所属区域编码")
    private String regionCode;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人名称")
    private String contactor;

    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private String contactorPhone;

    /**
     * 会员ID集合
     */
    @ApiModelProperty("会员ID集合")
    private List<Long> memberIdList;

    /**
     * 会员状态：1-正常 2-过期
     */
    @ApiModelProperty("会员状态：1-正常 2-过期")
    private Integer status;

}
