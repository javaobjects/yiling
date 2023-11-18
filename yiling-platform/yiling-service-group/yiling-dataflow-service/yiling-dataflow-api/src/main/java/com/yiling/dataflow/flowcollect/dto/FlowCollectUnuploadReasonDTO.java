package com.yiling.dataflow.flowcollect.dto;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowCollectUnuploadReasonDTO extends BaseDTO {

    /**
     * 商业公司编码
     */
    private Long crmEnterpriseId;

    /**
     * 统计的时间
     */
    private Date statisticsTime;

    /**
     * 原因类型:1-不合作、2-实际无动销、3-未签直连协议、4-商业更换ERP系统、5-无业务人员负责、6-工具未开启、7-月传、8-未直连、9-商业ERP系统问题、10-重新直连、11-商业不愿意日传、12-直连正常、13-直连工具问题、14-商业服务器问题、15-商业当天未传流向、16-商业FTP上传错误、17-接口报错、18-商业取消直连、19-其他
     */
    private Integer reasonType;

    /**
     * 原因内容
     */
    private String reasonNote;
    /**
     * 备注
     */
    private String remark;

    private Date updateTime;
    private String optName;
}
