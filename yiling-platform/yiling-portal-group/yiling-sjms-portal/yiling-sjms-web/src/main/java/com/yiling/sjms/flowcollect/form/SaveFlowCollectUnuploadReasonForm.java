package com.yiling.sjms.flowcollect.form;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFlowCollectUnuploadReasonForm extends BaseForm {
    private Long id;
    /**
     * 商业公司编码
     */
    @ApiModelProperty("机构编码")
    @NotNull
    private Long crmEnterpriseId;

    /**
     * 统计的时间
     */
    @ApiModelProperty("统计时间：年月日")
    @NotNull
    private Date statisticsTime;

    /**
     * 原因类型:1-不合作、2-实际无动销、3-未签直连协议、4-商业更换ERP系统、5-无业务人员负责、6-工具未开启、7-月传、8-未直连、9-商业ERP系统问题、10-重新直连、11-商业不愿意日传、12-直连正常、13-直连工具问题、14-商业服务器问题、15-商业当天未传流向、16-商业FTP上传错误、17-接口报错、18-商业取消直连、19-其他
     */
    @ApiModelProperty("原因类型")
    @NotNull
    private Integer reasonType;

    /**
     * 原因内容
     */
    @ApiModelProperty("原因备注")
    @NotBlank(message = "原因内容必填")
    @Length(max = 200,min = 5,message = "原因内容最少5个字符，最多200个字符")
    private String reasonNote;

  //  private String optName;
}
