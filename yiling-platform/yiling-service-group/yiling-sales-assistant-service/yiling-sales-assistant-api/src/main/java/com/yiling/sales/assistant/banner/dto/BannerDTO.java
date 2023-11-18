package com.yiling.sales.assistant.banner.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/9/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BannerDTO extends BaseDTO {

    /**
     * banner标题
     */
    private String  title;

    /**
     * banner图片地址
     */
    private String  pic;

    /**
     * 使用场景：1-以岭内部机构 2-非以岭机构
     */
    private Integer bannerCondition;

    /**
     * 使用位置：1-B2B移动端主Banner 2-B2B移动端副Banner
     */
    private Integer usageScenario;

    /**
     * 排序,排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序
     */
    private Integer sort;

    /**
     * 显示状态：1-启用 2-停用
     */
    private Integer bannerStatus;

    /**
     * 有效起始时间
     */
    private Date    startTime;

    /**
     * 有效结束时间
     */
    private Date    stopTime;

    /**
     * 活动详情超链接
     */
    private String  activityLinks;

    /**
     * 创建人
     */
    private Long    createUser;

    /**
     * 创建时间
     */
    private Date    createTime;

    /**
     * 修改人
     */
    private Long    updateUser;

    /**
     * 修改时间
     */
    private Date    updateTime;

    /**
     * 备注
     */
    private String  remark;
}
