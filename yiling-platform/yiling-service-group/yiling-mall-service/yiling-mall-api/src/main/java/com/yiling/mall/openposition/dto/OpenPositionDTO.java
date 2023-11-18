package com.yiling.mall.openposition.dto;

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
 * B2B-开屏位表 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2023-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OpenPositionDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private String no;

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
