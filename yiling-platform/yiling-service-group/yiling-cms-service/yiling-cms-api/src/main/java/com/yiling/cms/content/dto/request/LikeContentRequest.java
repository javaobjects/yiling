package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文章点赞
 *
 * @author: fan.shen
 * @date: 2022-10-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LikeContentRequest extends BaseRequest {

    private static final long serialVersionUID = 2561034198133937307L;

    /**
     * 文章id
     */
    private Long id;

    /**
     * 点赞标志：1-点赞，2-取消点赞
     */
    private Integer likeFlag;

    /**
     * cmsId
     */
    private Long cmsId;

    /**
     * 业务线 1-健康中心 2-互联网医院医生端 3-互联网医院患者端
     */
    private Integer LineType;

}