package com.yiling.open.erp.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/8/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveErpDeleteDataRequest extends BaseRequest {

    private Integer id;

    private String taskNo;

    private Long suId;

    private Integer status;

    private String dataId;

    private Date createTime;

    private Date updateTime;

}
