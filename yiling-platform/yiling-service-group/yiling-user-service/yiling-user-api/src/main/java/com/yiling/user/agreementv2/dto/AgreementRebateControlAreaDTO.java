package com.yiling.user.agreementv2.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议返利范围控销区域表 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementRebateControlAreaDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 协议返利范围ID
     */
    private Long rebateScopeId;

    /**
     * 控销区域描述
     */
    private String description;

    /**
     * 控销区域Json串
     */
    private String jsonContent;

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
