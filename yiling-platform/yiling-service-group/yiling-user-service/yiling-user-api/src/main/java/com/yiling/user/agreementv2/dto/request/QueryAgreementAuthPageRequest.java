package com.yiling.user.agreementv2.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询协议审核分页列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementAuthPageRequest extends QueryPageListRequest {

    /**
     * 开始创建时间
     */
    private Date startCreateTime;

    /**
     * 结束创建时间
     */
    private Date endCreateTime;


    /**
     * 甲方名称
     */
    private String ename;

    /**
     * 乙方名称
     */
    private String secondName;

    /**
     * 协议编号
     */
    private String agreementNo;

    /**
     * 协议负责人
     */
    private String mainUserName;

    /**
     * 协议类型：1-一级协议 2-二级协议 3-临时协议 4-商业供货协议 5-KA连锁协议 6-代理商协议（枚举：AgreementTypeEnum）
     */
    private Integer agreementType;

    /**
     * 单据类型：1-新建协议 2-修改协议
     */
    private Integer billsType;

    /**
     * 审核状态：1-待审核 2-审核通过 3-审核驳回 4-已归档
     */
    private Integer authStatus;

    /**
     * 不在此协议审核状态内：1-待审核 2-审核通过 3-审核驳回 4-已归档
     */
    private List<Integer> notAuthStatusList;

}
