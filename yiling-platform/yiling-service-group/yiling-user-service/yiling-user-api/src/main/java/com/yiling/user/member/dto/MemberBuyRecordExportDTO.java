package com.yiling.user.member.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员购买记录导出 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-07-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberBuyRecordExportDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 购买企业ID
     */
    private Long eid;

    /**
     * 购买企业名称
     */
    private String ename;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 省市区组合的地址
     */
    private String address;

    /**
     * 企业管理员手机号
     */
    private String contactorPhone;

    /**
     * 推广方ID
     */
    private Long promoterId;

    /**
     * 推广方名称
     */
    private String promoterName;

    /**
     * 推广人ID
     */
    private Long promoterUserId;

    /**
     * 推广方人名称
     */
    private String promoterUserName;

    /**
     * 数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手
     */
    private Integer source;

    /**
     * 数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手
     */
    private String sourceName;

    /**
     * 开通类型：1-首次开通 2-续费开通
     */
    private Integer openType;

    /**
     * 开通类型：1-首次开通 2-续费开通
     */
    private String openTypeName;

    /**
     * 购买规则
     */
    private String buyRule;

    /**
     * 会员开始时间
     */
    private Date startTime;

    /**
     * 会员结束时间
     */
    private Date endTime;

    /**
     * 有效天数
     */
    private Integer validDays;

    /**
     * 支付方式
     */
    private Integer payMethod;

    /**
     * 支付渠道
     */
    private String payChannel;

    /**
     * 支付方式名称
     */
    private String payMethodName;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 提交退款金额
     */
    private BigDecimal submitReturnAmount;

    /**
     * 退款金额
     */
    private BigDecimal returnAmount;

    /**
     * 是否退款：1-未退款 2-已退款（为了与查询保持统一，故使用1和2）
     */
    private Integer returnFlag;

    /**
     * 是否退款：1-未退款 2-已退款
     */
    private String returnFlagName;

    /**
     * 是否过期：1-未过期 2-已过期（为了与查询保持统一，故使用1和2）
     */
    private Integer expireFlag;

    /**
     * 是否过期：1-未过期 2-已过期
     */
    private String expireFlagName;

    /**
     * 创建人id
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否取消：0-否 1-是
     */
    private Integer cancelFlag;

    /**
     * 取消名称
     */
    private String cancelFlagName;

    /**
     * 更新人名称
     */
    private String updateUserName;

}
