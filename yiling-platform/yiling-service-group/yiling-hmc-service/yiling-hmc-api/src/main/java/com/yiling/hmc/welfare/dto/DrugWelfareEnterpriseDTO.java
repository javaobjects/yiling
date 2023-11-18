package com.yiling.hmc.welfare.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * @author fan.shen
 * @date 2022-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DrugWelfareEnterpriseDTO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 药品福利id
     */
    private Long drugWelfareId;

    /**
     * 备注
     */
    private String remake;

    /**
     * 是否删除：0-否 1-是
     */
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


}
