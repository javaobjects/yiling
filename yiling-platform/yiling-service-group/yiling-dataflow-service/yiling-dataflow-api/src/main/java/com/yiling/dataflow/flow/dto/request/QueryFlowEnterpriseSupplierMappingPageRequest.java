package com.yiling.dataflow.flow.dto.request;

import java.util.Date;

import com.baomidou.mybatisplus.extension.api.R;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryFlowEnterpriseSupplierMappingPageRequest
 * @描述
 * @创建时间 2023/5/31
 * @修改人 shichen
 * @修改时间 2023/5/31
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowEnterpriseSupplierMappingPageRequest extends QueryPageListRequest {
    /**
     * 原始配送商名称
     */
    private String flowSupplierName;

    /**
     * 标准机构编码
     */
    private Long crmOrgId;

    /**
     * 标准机构名称
     */
    private String orgName;

    /**
     * 经销商名称
     */
    private String enterpriseName;

    /**
     * 经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 开始更新时间
     */
    private Date startUpdateTime;

    /**
     * 结束更新时间
     */
    private Date endUpdateTime;

    /**
     * 开始最后上传时间
     */
    private Date startLastUploadTime;

    /**
     * 结束最后上传时间
     */
    private Date endLastUploadTime;

    private Boolean orderByUploadTime;

    /**
     * 权限对象
     */
    private SjmsUserDatascopeBO userDatascopeBO;
}
