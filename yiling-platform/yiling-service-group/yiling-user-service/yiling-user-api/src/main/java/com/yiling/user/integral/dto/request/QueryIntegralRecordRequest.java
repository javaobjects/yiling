package com.yiling.user.integral.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.After;
import com.yiling.user.common.util.bean.Before;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.Like;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询积分发放/扣减记录分页 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralRecordRequest extends QueryPageListRequest {

    /**
     * 类型：1-发放 2-扣减
     */
    @Eq
    private Integer type;

    /**
     * 发放/扣减开始时间
     */
    @Before(name = "oper_time")
    private Date startOperTime;

    /**
     * 发放/扣减结束时间
     */
    @After(name = "oper_time")
    private Date endOperTime;

    /**
     * 规则名称
     */
    @Like
    private String ruleName;

    /**
     * 行为名称
     */
    @Eq
    private String behaviorName;

    /**
     * 创建人手机号
     */
    private String mobile;

    /**
     * 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手
     */
    @Eq
    private Integer platform;

    /**
     * 用户ID
     */
    @Eq
    private Long uid;

    /**
     * 用户名称
     */
    @Like
    private String uname;

    /**
     * 操作备注
     */
    @Like
    private String opRemark;

}
