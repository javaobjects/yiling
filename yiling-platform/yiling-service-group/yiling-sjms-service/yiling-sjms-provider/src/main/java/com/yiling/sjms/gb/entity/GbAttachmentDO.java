package com.yiling.sjms.gb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 团购附件
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("gb_attachment")
public class GbAttachmentDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 团购ID
     */
    private Long gbId;

    /**
     * 文件类型：1-团购证据 2-费用申请提交
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
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
