package com.yiling.user.enterprise.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业审核分页列表 Request
 *
 * @author: lun.yu
 * @date: 2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEnterpriseAuthPageRequest extends QueryPageListRequest {

    /**
     * 企业名称
     */
    private String name;

    /**
     * 执业许可证号/社会信用统一代码
     */
    private String licenseNumber;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 类型：0-全部 1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    private Integer type;

    /**
     * 审核类型：1-首次认证 2-资质更新 3-驳回后再次认证
     */
    private Integer authType;

    /**
     * 认证状态：0-全部 1-未认证 2-认证通过 3-认证不通过
     */
    private Integer authStatus;

    /**
     * 数据来源：1-B2B 2-销售助手
     */
    private Integer source;

    /**
     * 申请开始时间
     */
    private Date startCreateTime;

    /**
     * 申请结束时间
     */
    private Date endCreateTime;
}
