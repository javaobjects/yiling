package com.yiling.user.agreement.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 详情信息
 *
 * @author:wei.wang
 * @date:2021/6/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementDTO extends BaseDTO {

    /**
     * 协议主体ID（甲方）
     */
    private Long    eid;

    /**
     * 主体名称
     */
    private String  ename;

    /**
     * 协议客户ID（乙方）
     */
    private Long    secondEid;

    /**
     * 乙方企业名称
     */
    private String  secondName;

    /**
     * 乙方渠道类型名称
     */
    private String  secondChannelName;

    /**
     * 第三方客户ID（丙方）
     */
    private Long    thirdEid;

    /**
     * 丙方名称
     */
    private String  thirdName;

    /**
     * 丙方渠道类型名称
     */
    private String  thirdChannelName;

    /**
     * 协议编号
     */
    private String  agreementNo;

    /**
     * 协议名称
     */
    private String  name;

    /**
     * 协议描述
     */
    private String  content;

    /**
     * 协议类型 1-采购类 2-其他
     */
    private Integer type;

    /**
     * 协议类型 1-双方协议 2-三方协议
     */
    private Integer mode;

    /**
     * 主协议ID
     */
    private Long    parentId;

    /**
     * 协议分类：1-主协议 2-临时协议
     */
    private Integer category;

    /**
     * 开始时间
     */
    private Date    startTime;

    /**
     * 结束时间
     */
    private Date    endTime;

    /**
     * 返利形式0全部1指定（返利形式集合 1- 票折 2- 现金 3-冲红 4-健康城卡）
     */
    private Integer restitutionType;

    /**
     * 返利类型：1-年度返利 2-临时政策返利
     */
    private Integer rebateType;

    private Integer rebateCycle;

    /**
     * 专利类型 0-全部 1-非专利 2-专利
     */
    private Integer isPatent;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 版本
     */
    private Integer version;

	/**
	 * 创建时间
	 */
	private Date createTime;

    /**
     * 迁移状态  1-采购关系已存在 2-迁移成功
     */
    private Integer migrateStatus;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private Long    updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date    updateTime;

}
