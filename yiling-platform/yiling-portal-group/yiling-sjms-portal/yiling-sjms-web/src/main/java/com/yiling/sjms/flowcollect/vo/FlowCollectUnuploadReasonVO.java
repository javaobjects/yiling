package com.yiling.sjms.flowcollect.vo;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowCollectUnuploadReasonVO extends BaseVO {


    /**
     * 商业公司编码
     */
    @ApiModelProperty("商业公司编码")
    private Long crmEnterpriseId;

    /**
     * 统计的时间
     */
    @ApiModelProperty("统计时间")
    private Date statisticsTime;

    /**
     * 原因类型:1-不合作、2-实际无动销、3-未签直连协议、4-商业更换ERP系统、5-无业务人员负责、6-工具未开启、7-月传、8-未直连、9-商业ERP系统问题、10-重新直连、11-商业不愿意日传、12-直连正常、13-直连工具问题、14-商业服务器问题、15-商业当天未传流向、16-商业FTP上传错误、17-接口报错、18-商业取消直连、19-其他
     */
    @ApiModelProperty("原因类型:1-不合作、2-实际无动销、3-未签直连协议、4-商业更换ERP系统、5-无业务人员负责、6-工具未开启、7-月传、8-未直连、9-商业ERP系统问题、10-重新直连、11-商业不愿意日传、12-直连正常、13-直连工具问题、14-商业服务器问题、15-商业当天未传流向、16-商业FTP上传错误、17-接口报错、18-商业取消直连、19-其他")
    private Integer reasonType;

    /**
     * 原因内容
     */
    @ApiModelProperty("原因内容")
    private String reasonNote;

    @ApiModelProperty("操作时间")
    private Date updateTime;
    @ApiModelProperty("操作人")
    private String optName;
}
