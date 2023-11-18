package com.yiling.user.system.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户相关附件信息 DTO
 *
 * @author: xuan.zhou
 * @date: 2022/7/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserAttachmentDTO extends BaseDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 文件类型：1-身份证正面照 2-身份证反面照片
     */
    private Integer fileType;

    /**
     * 文件KEY
     */
    private String fileKey;

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
