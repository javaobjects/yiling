package com.yiling.mall.banner.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 金刚位表
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b2b_app_vajra_position")
public class B2bAppVajraPositionDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    private String eid;

    /**
     * 金刚位标题名称
     */
    private String title;

    /**
     * 金刚位图片地址
     */
    private String pic;

    /**
     * 来源1-POP 2-销售助手 3-B2B
     */
    private Integer source;

    /**
     * 排序,排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序
     */
    private Integer sort;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer vajraStatus;

    /**
     * 页面配置1-活动详情H5 3-搜索结果页 4-商品页 5-店铺页 6-领券中心 7-活动中心 8-会员中心
     */
    private Integer linkType;

    /**
     * 活动详情超链接
     */
    private String activityLinks;

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
