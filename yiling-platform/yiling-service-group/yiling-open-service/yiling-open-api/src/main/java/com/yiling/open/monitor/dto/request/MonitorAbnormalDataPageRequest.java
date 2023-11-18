package com.yiling.open.monitor.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/10/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MonitorAbnormalDataPageRequest extends QueryPageListRequest {

    /**
     * 商业ID
     */
    private Long eid;

    /**
     * 商业名称
     */
    private String ename;

    /**
     * 父类商业ID
     */
    private Long parentId;

    /**
     * 销售单主键ID
     */
    private String soId;

    /**
     * 销售单号
     */
    private String soNo;

    /**
     * 销售单据时间 开始
     */
    private Date flowTimeStart;

    /**
     * 销售单据时间 结束
     */
    private Date flowTimeEnd;

}
