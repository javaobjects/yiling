package com.yiling.cms.content.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * HMC内容
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HMCContentDTO extends BaseDTO {

    private static final long serialVersionUID = -8755429127432498324L;

    /**
     * cms_content主键
     */
    private Long contentId;

    /**
     * 标题
     */
    private String title;

    /**
     * 引用业务线id
     */
    private Long lineId;

    /**
     * 业务线名称
     */
    private String lineName;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 栏目id
     */
    private Long categoryId;

    /**
     * 栏目名称
     */
    private String categoryName;

    /**
     * 栏目列表排序
     */
    private Integer categoryRank;

    /**
     * 浏览量
     */
    private Integer view;

    /**
     * 是否置顶 0-否，1-是
     */
    private Integer topFlag;

    /**
     * 类型:1-文章 2-视频
     */
    private Integer contentType;

    /**
     * 所属医生id
     */
    private Long docId;

    /**
     * 状态 1未发布 2发布
     */
    private Integer status;

    /**
     * 引用状态 1-引用，2-取消引用
     */
    private Integer referStatus;

    /**
     * 创建来源 1-运营后台，2-IH后台
     */
    private Integer createSource;

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
