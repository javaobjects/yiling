package com.yiling.hmc.gzh.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 公众号欢迎语
 * </p>
 *
 * @author fan.shen
 * @date 2023-03-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_gzh_greeting")
public class GzhGreetingDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 场景id
     */
    private Integer sceneId;

    /**
     * 场景名称
     */
    private String sceneName;

    /**
     * 累计触发次数
     */
    private Long triggerCount;

    /**
     * 发布状态 1-已发布，2-未发布
     */
    private Integer publishStatus;

    /**
     * 发布版本
     */
    private String publishVersion;

    /**
     * 发布时间
     */
    private Date publishDate;

    /**
     * 草稿版本
     */
    private String draftVersion;

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
