package com.yiling.admin.hmc.pharmacy.vo;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 终端药店 VO
 *
 * @author: fan.shen
 * @date: 2024/5/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PharmacyVO extends BaseVO {

    /**
     * 商家id
     */
    @ApiModelProperty(value = "终端商家id")
    private Long eid;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String ename;

    /**
     * IH商家id
     */
    @ApiModelProperty(value = "IH商家id")
    private Long ihEid;

    /**
     * IH商家名称
     */
    @ApiModelProperty(value = "IH商家名称")
    private String ihEname;

    /**
     * 状态：1-合作，2-不合作
     */
    @ApiModelProperty(value = "状态：1-合作，2-不合作")
    private Integer status;

    /**
     * 是否删除：0-否 1-是
     */
    @ApiModelProperty(value = "是否删除：0-否 1-是")
    private Integer delFlag;

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
     * 备注
     */
    private String remark;

}