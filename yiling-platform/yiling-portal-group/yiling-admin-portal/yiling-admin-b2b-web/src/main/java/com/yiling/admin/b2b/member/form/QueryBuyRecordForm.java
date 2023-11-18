package com.yiling.admin.b2b.member.form;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员购买记录 Form
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryBuyRecordForm extends QueryPageListForm {

    /**
     * 终端ID
     */
    @ApiModelProperty("终端ID")
    private Long eid;

    /**
     * 终端名称
     */
    @Length(max = 50)
    @ApiModelProperty("终端名称")
    private String ename;

    /**
     * 会员ID集合
     */
    @ApiModelProperty("会员ID集合")
    private List<Long> memberIdList;

    /**
     * 推广方ID
     */
    @ApiModelProperty("推广方ID")
    private String promoterId;

    /**
     * 推广方名称
     */
    @ApiModelProperty("推广方名称")
    private String promoterName;

    /**
     * 推广人ID
     */
    @ApiModelProperty("推广人ID")
    private Long promoterUserId;

    /**
     * 推广方人名称
     */
    @ApiModelProperty("推广方人名称")
    private String promoterUserName;

    /**
     * 购买开始时间
     */
    @ApiModelProperty("购买开始时间")
    private Date buyStartTime;

    /**
     * 购买结束时间
     */
    @ApiModelProperty("购买结束时间")
    private Date buyEndTime;

    /**
     * 所属省份编码
     */
    @ApiModelProperty("所属省份编码")
    private String provinceCode;

    /**
     * 所属城市编码
     */
    @ApiModelProperty("所属城市编码")
    private String cityCode;

    /**
     * 所属区域编码
     */
    @ApiModelProperty("所属区域编码")
    private String regionCode;

    /**
     * 是否退款：0-全部 1-未退款 2-已退款
     */
    @ApiModelProperty("是否退款：0-全部 1-未退款 2-已退款")
    private Integer returnFlag;

    /**
     * 是否过期：0-全部 1-未过期 2-已过期
     */
    @ApiModelProperty("是否过期：0-全部 1-未过期 2-已过期")
    private Integer expireFlag;

    /**
     * 开通类型：1-首次开通 2-续费开通
     */
    @ApiModelProperty("开通类型：1-首次开通 2-续费开通（见字典：member_open_type）")
    private Integer openType;

    /**
     * 数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手
     */
    @ApiModelProperty("数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手（见字典：member_data_source）")
    private List<Integer> sourceList;

    /**
     * 推广方省份编码
     */
    @ApiModelProperty("推广方省份编码")
    private String promoterProvinceCode;

    /**
     * 推广方城市编码
     */
    @ApiModelProperty("推广方城市编码")
    private String promoterCityCode;

    /**
     * 推广方区域编码
     */
    @ApiModelProperty("推广方区域编码")
    private String promoterRegionCode;

}
