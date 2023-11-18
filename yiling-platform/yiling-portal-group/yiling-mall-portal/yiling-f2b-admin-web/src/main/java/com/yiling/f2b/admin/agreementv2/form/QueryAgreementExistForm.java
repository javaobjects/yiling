package com.yiling.f2b.admin.agreementv2.form;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议是否存在查询 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-25
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementExistForm extends BaseForm {

    /**
     * 甲方ID
     */
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "甲方ID",required = true)
    private Long eid;

    /**
     * 甲方类型：1-工业-生产厂家 2-工业-品牌厂家 3-商业-供应商 4-代理商
     */
    @NotNull
    @ApiModelProperty(value = "甲方类型：1-工业-生产厂家 2-工业-品牌厂家 3-商业-供应商 4-代理商",required = true)
    private Integer firstType;

    /**
     * 协议类型：1-一级协议 2-二级协议 3-临时协议 4-商业供货协议 5-KA连锁协议 6-代理商协议
     */
    @NotNull
    @ApiModelProperty(value = "协议类型：1-一级协议 2-二级协议 3-临时协议 4-商业供货协议 5-KA连锁协议 6-代理商协议",required = true)
    private Integer agreementType;

    /**
     * 乙方ID
     */
    @NotNull
    @ApiModelProperty(value = "乙方ID",required = true)
    private Long secondEid;

    /**
     * 生效时间
     */
    @NotNull
    @ApiModelProperty(value = "生效时间",required = true)
    private Date startTime;

    /**
     * 失效时间
     */
    @NotNull
    @ApiModelProperty(value = "失效时间",required = true)
    private Date endTime;

}
