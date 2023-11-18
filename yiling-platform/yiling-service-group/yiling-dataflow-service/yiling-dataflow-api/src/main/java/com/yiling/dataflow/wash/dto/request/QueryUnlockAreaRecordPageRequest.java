package com.yiling.dataflow.wash.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUnlockAreaRecordPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -5760415291127042506L;

    /**
     * 非锁客户分类
     */
    private Integer customerClassification;

    /**
     * 品种id
     */
    private Long categoryId;

    /**
     * 代表姓名
     */
    private String representativeCode;

    /**
     * 主管姓名
     */
    private String executiveCode;

    private Date startOpTime;

    private Date endOpTime;
}
