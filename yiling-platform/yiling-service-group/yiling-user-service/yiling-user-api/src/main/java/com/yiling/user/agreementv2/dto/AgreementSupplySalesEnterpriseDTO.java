package com.yiling.user.agreementv2.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议供销指定商业公司 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementSupplySalesEnterpriseDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

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
