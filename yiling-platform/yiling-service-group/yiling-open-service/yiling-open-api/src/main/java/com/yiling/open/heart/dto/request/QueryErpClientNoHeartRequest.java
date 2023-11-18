package com.yiling.open.heart.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryErpClientNoHeartRequest  extends BaseRequest {

    /**
     * 分公司id
     */
    private List<Long> rkSuIdList;

    /**
     * 统计日期
     */
    private Date taskTime;


    /**
     * 创建时间
     */
    private Date createTime;

}
