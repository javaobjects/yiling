package com.yiling.dataflow.crm.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shixing.sun
 * @date: 2023/02/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ChangeRelationShipDetailRequest extends BaseRequest {

    private static final long serialVersionUID = -2786118989515992305L;
    /**
     * 三者关系表id集合
     */
    private List<Long> ids;

    /**
     * 岗位id
     */
    private Long postCode;

    /**
     * 岗位名称
     */
    private String postName;
}
