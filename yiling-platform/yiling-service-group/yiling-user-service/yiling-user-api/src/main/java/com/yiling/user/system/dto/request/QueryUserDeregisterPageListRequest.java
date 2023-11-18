package com.yiling.user.system.dto.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.After;
import com.yiling.user.common.util.bean.Before;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.Like;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 注销账号分页查询 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUserDeregisterPageListRequest extends QueryPageListRequest {

    /**
     * 注销状态：1-待注销 2-已注销 3-已撤销
     */
    @Eq
    private Integer status;

    /**
     * 用户ID
     */
    @Eq
    private Long userId;

    /**
     * 手机号
     */
    @Like
    private String mobile;

    /**
     * 姓名
     */
    @Like
    private String name;

    /**
     * 开始申请注销时间
     */
    @Before(name = "apply_time")
    private Date startApplyTime;

    /**
     * 结束申请注销时间
     */
    @After(name = "apply_time")
    private Date endApplyTime;

}
