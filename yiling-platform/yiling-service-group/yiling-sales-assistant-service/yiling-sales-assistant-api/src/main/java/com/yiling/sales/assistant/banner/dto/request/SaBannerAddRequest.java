package com.yiling.sales.assistant.banner.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

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
public class SaBannerAddRequest extends BaseRequest {

    /**
     * 修改时需要的banner的id
     */
    private Long    id;
    /**
     * banner标题
     */
    private String  title;
    /**
     * 使用场景：1-以岭内部机构 2-非以岭机构
     */
    private Integer bannerCondition;
    /**
     * 发布时间
     */
    private Date    releaseTime;
    /**
     * 企业ID
     */
    private Long    eid;
    /**
     * 企业名称
     */
    private String  ename;
    /**
     * 下架时间
     */
    private Date    endTime;
    /**
     * banner图片地址
     */
    private String  pic;

    //=====================================================

    private Long releaseUserId;

    private String releaseUserName;

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
}
