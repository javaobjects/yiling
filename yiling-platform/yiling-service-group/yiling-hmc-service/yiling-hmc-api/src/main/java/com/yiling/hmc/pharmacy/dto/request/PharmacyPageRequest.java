package com.yiling.hmc.pharmacy.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 获取会议签到信息
 * </p>
 *
 * @author fan.shen
 * @date 2023-03-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PharmacyPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    /**
     * 终端商家id
     */
    private Long eid;

    /**
     * 商家名称
     */
    private String ename;

    /**
     * 状态：1-启用，2-停用
     */
    private Integer status;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

}
