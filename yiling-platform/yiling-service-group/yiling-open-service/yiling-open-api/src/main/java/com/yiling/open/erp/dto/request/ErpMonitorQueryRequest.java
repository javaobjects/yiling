package com.yiling.open.erp.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/1/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ErpMonitorQueryRequest extends QueryPageListRequest {

    /**
     * 企业id
     */
    private Long rkSuId;

    /**
     * 企业名称
     */
    private String clientName;

    /**
     * 父类企业id
     */
    private Long suId;

    /**
     * 对接负责人
     */
    private String installEmployee;

    /**
     * 查询类型：0-全部，1-未激活的企业，2-24小时无心跳企业
     */
    private Integer openType;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 对接方式：1-未设置 2-工具 3-ftp 4-第三方接口 5-以岭平台接口
     */
    private Integer flowMode;

    /**
     * 企业id列表
     */
    private List<Long> rkSuIdList;

}
