package com.yiling.user.esb.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author xueli.ji
 * @date 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EsbBackUpRecordDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 所属年月份:yyyyMM
     */
    private Long yearMonths;

    /**
     * 备份信息
     */
    private String info;

    /**
     * 备份名称
     */
    private String name;

    /**
     * 备份记录
     */
    private Long count;

    /**
     * 状态:0-待执行,1成功,2失败
     */
    private Integer status;

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


}
