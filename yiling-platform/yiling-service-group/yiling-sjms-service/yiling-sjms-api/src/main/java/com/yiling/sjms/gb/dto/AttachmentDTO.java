package com.yiling.sjms.gb.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AttachmentDTO extends BaseDTO {
    /**
     * 团购ID
     */
    private Long gbId;

    /**
     * 文件类型：1-团购证据
     */
    private Integer fileType;

    /**
     * 文件KEY
     */
    private String fileKey;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件MD5
     */
    private String fileMd5;

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

    /**
     * 备注
     */
    private String remark;
}
