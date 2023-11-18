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
 * 厂家 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022/2/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementManufacturerDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 厂家编号
     */
    private Long eid;

    /**
     * 厂家名称
     */
    private String ename;

    /**
     * 厂家类型 1-生产厂家 2-品牌厂家
     */
    private Integer type;


    /**
     * 创建人id
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人id
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

}
