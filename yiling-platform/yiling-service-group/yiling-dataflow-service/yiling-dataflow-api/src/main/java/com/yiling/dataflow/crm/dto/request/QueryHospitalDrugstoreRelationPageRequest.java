package com.yiling.dataflow.crm.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryHospitalDrugstoreRelationPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -6982226822132213498L;

    private Long drugstoreOrgId;

    private Long hospitalOrgId;

    private Long categoryId;

    private String categoryName;

    private Long crmGoodsCode;

    private Date startOpTime;

    private Date endOpTime;

    private Integer status;
}
