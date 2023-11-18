package com.yiling.mall.banner.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class B2bAppBannerSaveRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

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
     * 使用场景1-以岭内部机构 2-非以岭机构
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
     * 备注
     */
    private String remark;

    /**
     * B2B移动端店铺Banner对应的企业
     */
    private List<B2bAppBannerEnterpriseSaveRequest> bannerEnterpriseList;
}
