package com.yiling.hmc.pharmacy.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 终端药店
 * </p>
 *
 * @author fan.shen
 * @date 2023-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PharmacyDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 商家id
     */
    private Long eid;

    /**
     * 商家名称
     */
    private String ename;

    /**
     * IH商家id
     */
    private Long ihEid;

    /**
     * IH商家名称
     */
    private String ihEname;

    /**
     * 状态：1-合作，2-不合作
     */
    private Integer status;

    /**
     * 小程序码
     */
    private String qrCode;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
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
