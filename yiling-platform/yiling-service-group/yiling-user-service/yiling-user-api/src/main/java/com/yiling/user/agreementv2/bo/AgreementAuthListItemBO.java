package com.yiling.user.agreementv2.bo;

import java.io.Serializable;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议审核列表项 BO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-11
 */
@Data
@Accessors(chain = true)
public class AgreementAuthListItemBO implements Serializable {

    /**
     * 协议ID
     */
    private Long id;

    /**
     * 单据类型：1-新建协议 2-修改协议
     */
    private Integer billsType;

    /**
     * 协议编号
     */
    private String agreementNo;

    /**
     * 甲方名称
     */
    private String ename;

    /**
     * 乙方名称
     */
    private String secondName;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 生效时间
     */
    private Date startTime;

    /**
     * 失效时间
     */
    private Date endTime;

    /**
     * 甲方类型：1-工业-生产厂家 2-工业-品牌厂家 3-商业-供应商 4-代理商
     */
    private Integer firstType;

    /**
     * 协议类型：1-一级协议 2-二级协议 3-临时协议 4-商业供货协议 5-KA连锁协议 6-代理商协议
     */
    private Integer agreementType;

    /**
     * 协议负责人名称
     */
    private String mainUserName;

    /**
     * 状态：1-待审核 2-审核通过 3-审核驳回 4-已归档
     */
    private Integer authStatus;

    /**
     * 审核人
     */
    private Long authUser;

    /**
     * 审核时间
     */
    private Date authTime;

    /**
     * 审核拒绝原因
     */
    private String authRejectReason;

    /**
     * 归档编号
     */
    private String archiveNo;

    /**
     * 归档备注
     */
    private String archiveRemark;


}
