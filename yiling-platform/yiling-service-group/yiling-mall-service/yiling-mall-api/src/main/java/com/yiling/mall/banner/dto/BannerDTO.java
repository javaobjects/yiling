package com.yiling.mall.banner.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * banner dto
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BannerDTO extends BaseDTO {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * banner标题
     */
    private String title;

    /**
     * banner状态：0-未开始 1-进行中 2-已结束
     */
    private Integer bannerStatus;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 链接类型：1-商品 2：链接
     */
    private Integer linkType;

    /**
     * 链接url
     */
    private String linkUrl;

    /**
     * banner图片值
     */
    private String pic;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

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
