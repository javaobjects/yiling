package com.yiling.mall.openposition.entity;

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
 * B2B-开屏位表
 * </p>
 *
 * @author lun.yu
 * @date 2023-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b2b_open_position")
public class OpenPositionDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private String no;

    /**
     * 序号
     */
    private Integer serialNo;

    /**
     * 标题
     */
    private String title;

    /**
     * 配置链接
     */
    private String link;

    /**
     * 图片
     */
    private String picture;

    /**
     * 状态：1-未发布 2-已发布
     */
    private Integer status;

    /**
     * 平台：1-大运河 2-销售助手
     */
    private Integer platform;

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
