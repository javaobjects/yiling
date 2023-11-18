package com.yiling.mall.banner.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 新增修改金刚位信息
 *
 * @author: yong.zhang
 * @date: 2021/10/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveB2bAppVajraPositionRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

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
     * 状态：0-启用 1-停用
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
     * 备注
     */
    private String remark;
}
