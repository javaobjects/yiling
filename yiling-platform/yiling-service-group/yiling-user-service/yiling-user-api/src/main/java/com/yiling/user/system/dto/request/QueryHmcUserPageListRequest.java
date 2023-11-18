package com.yiling.user.system.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询HMC用户分页列表 Request
 *
 * @author xuan.zhou
 * @date 2022/4/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryHmcUserPageListRequest extends QueryPageListRequest {

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 状态：0-全部 1-启用 2-停用
     */
    private Integer status;

    /**
     * 注册开始时间
     */
    private Date registBeginTime;

    /**
     * 注册结束时间
     */
    private Date registEndTime;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 小程序id
     */
    private String appId;

    /**
     * C端活动idList
     */
    private List<Long> activityIdList;

    /**
     * 医生idList
     */
    private List<Long> doctorIdList;

    /**
     * 注册来源：1-自然流量 2-店员或销售 3-扫药盒二维码 4-医生推荐 5-用户推荐 6- 以岭互联网医院
     */
    private Integer registerSource;

}

