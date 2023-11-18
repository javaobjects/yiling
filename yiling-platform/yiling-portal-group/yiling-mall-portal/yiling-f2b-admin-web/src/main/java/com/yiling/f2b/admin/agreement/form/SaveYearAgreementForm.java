package com.yiling.f2b.admin.agreement.form;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveYearAgreementForm extends BaseForm {
    /**
     * 协议主体ID（甲方）
     */
    @NotNull
    @ApiModelProperty(value = "协议主体ID（甲方）")
    private Long eid;

    /**
     * 协议主体名字（甲方）
     */
    @NotNull
    @ApiModelProperty(value = "协议主体名字（甲方）")
    private String ename;

    /**
     * 协议客户ID（乙方）
     */
    @NotNull
    @ApiModelProperty(value = "协议客户ID（乙方）")
    private Long secondEid;

    /**
     * 协协客户（乙方）
     */
    @NotNull
    @ApiModelProperty(value = "协协客户（乙方）")
    private String secondName;

    /**
     * 协协客户渠道名称（乙方）
     */
    @NotNull
    @ApiModelProperty(value = "协协客户渠道名称（乙方）")
    private String secondChannelName;


    /**
     * 协议编号
     */
    @ApiModelProperty(value = "协议编号")
    private String agreementNo;

    /**
     * 协议名称
     */
    @NotNull
    @ApiModelProperty(value = "协议名称")
	@Length(max = 50,message = "协议名称长度0-50")
    private String name;

    /**
     * 协议描述
     */
    @NotNull
    @ApiModelProperty(value = "协议描述")
	@Length(max = 200, message = "协议描述长度0-200")
    private String content;

    /**
     * 开始时间
     */
    @NotNull
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @NotNull
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 协议商品列表
     */
    @ApiModelProperty(value = "协议商品列表")
    private List<SaveAgreementGoodsForm> agreementGoodsList;

    public Date getStartTime() {
        if (startTime != null) {
            return DateUtil.beginOfDay(startTime);
        }
        return null;
    }

    public Date getEndTime() {
        if (endTime != null) {
            return DateUtil.endOfDay(endTime);
        }
        return null;
    }
}
