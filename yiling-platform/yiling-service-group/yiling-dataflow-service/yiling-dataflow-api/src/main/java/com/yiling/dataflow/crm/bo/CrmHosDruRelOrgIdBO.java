package com.yiling.dataflow.crm.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author fucheng.bai
 * @date 2023/6/14
 */
@Data
public class CrmHosDruRelOrgIdBO implements Serializable {

    private static final long serialVersionUID = 1408395002128724234L;
    /**
     * 院外药店机构编码
     */
    private Long drugstoreOrgId;

    /**
     * 医疗机构编码
     */
    private Long hospitalOrgId;
}
