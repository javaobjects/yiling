package com.yiling.hmc.remind.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 用药提醒
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/5/31
 */
@Data
@ToString
@ApiModel(value = "MedsRemindVO", description = "用药提醒")
@Slf4j
public class MedsRemindVO extends BaseVO {

    private static final long serialVersionUID = -7722430332896313642L;

    /**
     * 售卖规格id
     */
    @ApiModelProperty(value = "售卖规格id")
    private Long sellSpecificationsId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 单次用量（例如：3、1）
     */
    @NotEmpty
    @ApiModelProperty(value = "单次用量（例如：1、3）")
    private String useAmount;

    /**
     * 用法用量单位（例如：粒、袋）
     */
    @NotEmpty
    @ApiModelProperty(value = "用法用量单位（例如：粒、袋）")
    private String useAmountUnit;

    /**
     * 用药次数
     */
    @ApiModelProperty(value = "用药次数")
    private Integer useTimesType;

    /**
     * 用药天数
     */
    @ApiModelProperty(value = "用药天数")
    private Integer useDaysType;

    /**
     * 提醒状态 1-有效，2-无效
     */
    @ApiModelProperty(value = "提醒状态 1-有效，2-无效")
    private Integer remindStatus;

    /**
     * 提醒时间
     */
    @ApiModelProperty(value = "提醒时间")
    private List<String> medsRemindTimeList;

    /**
     * 创建人头像
     */
    @ApiModelProperty(value = "创建人头像")
    private String creatorAvatarUrl;

    /**
     * 关注人头像列表
     */
    @ApiModelProperty(value = "关注人头像列表")
    private List<String> subAvatarUrlList;

    /**
     * 显示编辑按钮
     */
    @ApiModelProperty(value = "显示编辑按钮 true-显示，false-不显示")
    private Boolean showEditButton = false;

    /**
     * 是否订阅
     */
    @ApiModelProperty(value = "是否订阅 true-是，false-否")
    private Boolean subFlag = false;

}
