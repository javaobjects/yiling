package com.yiling.user.member.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.After;
import com.yiling.user.common.util.bean.Before;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.In;
import com.yiling.user.common.util.bean.Like;
import com.yiling.user.common.util.bean.Order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员购买记录 Request
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryBuyRecordRequest extends QueryPageListRequest {

    /**
     * 企业ID
     */
    @Eq
    private Long eid;

    /**
     * 企业名称
     */
    @Like
    private String ename;

    /**
     * 会员ID集合
     */
    @In(name = "member_id")
    private List<Long> memberIdList;

    /**
     * 会员名称
     */
    @Like
    private String memberName;

    /**
     * 推广方ID
     */
    @Eq
    private String promoterId;

    /**
     * 推广方名称
     */
    @Like
    private String promoterName;

    /**
     * 推广人ID
     */
    @Eq
    private Long promoterUserId;

    /**
     * 推广方人名称
     */
    @Like
    private String promoterUserName;

    /**
     * 更新人名称
     */
    private String updateUserName;

    /**
     * 取消开始时间
     */
    @Before(name = "update_time")
    private Date cancelStartTime;

    /**
     * 取消结束时间
     */
    @After(name = "update_time")
    private Date cancelEndTime;

    /**
     * 是否取消：0-否 1-是
     */
    @Eq(notZero = false)
    private Integer cancelFlag;

    /**
     * 购买开始时间
     */
    @Before(name = "create_time")
    private Date buyStartTime;

    /**
     * 购买结束时间
     */
    @After(name = "create_time")
    private Date buyEndTime;

    /**
     * 所属省份编码
     */
    @Eq
    private String provinceCode;

    /**
     * 所属城市编码
     */
    @Eq
    private String cityCode;

    /**
     * 所属区域编码
     */
    @Eq
    private String regionCode;

    /**
     * 是否退款：0-全部 1-未退款 2-已退款
     */
    private Integer returnFlag;

    /**
     * 是否过期：0-全部 1-未过期 2-已过期
     */
    private Integer expireFlag;

    /**
     * 开通类型：1-首次开通 2-续费开通
     */
    @Eq
    private Integer openType;

    /**
     * 数据来源集合：1-B2B-自然流量 2-B2B-企业推广 3-销售助手
     */
    @In(name = "source")
    List<Integer> sourceList;

    /**
     * 数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手
     */
    @Eq
    private Integer source;

    /**
     * 页面类型：1-购买记录 2-取消记录（默认为1）
     */
    private Integer pageType;

    /**
     * 推广方省份编码
     */
    private String promoterProvinceCode;

    /**
     * 推广方城市编码
     */
    private String promoterCityCode;

    /**
     * 推广方区域编码
     */
    private String promoterRegionCode;

}
