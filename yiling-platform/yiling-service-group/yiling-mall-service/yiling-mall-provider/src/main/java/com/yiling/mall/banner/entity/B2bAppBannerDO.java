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
 * B2B的banner表
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b2b_app_banner")
public class B2bAppBannerDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    private String eid;

    /**
     * banner标题
     */
    private String title;

    /**
     * banner图片地址
     */
    private String pic;

    /**
     * 来源1-POP 2-销售助手 3-B2B
     */
    private Integer bannerSource;

    /**
     * 使用位置：1-B2B移动端主Banner 2-B2B移动端副Banner 3-B2B移动端会员中心Banner 4-B2B移动端店铺Banner
     */
    private Integer usageScenario;

    /**
     * 排序,排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序
     */
    private Integer sort;

    /**
     * 显示状态：0-启用 1-停用
     */
    private Integer bannerStatus;

    /**
     * 有效起始时间
     */
    private Date startTime;

    /**
     * 有效结束时间
     */
    private Date stopTime;

    /**
     * 页面配置1-活动详情H5 3-搜索结果页 4-商品页 5-旗舰店页 6-会员中心
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
