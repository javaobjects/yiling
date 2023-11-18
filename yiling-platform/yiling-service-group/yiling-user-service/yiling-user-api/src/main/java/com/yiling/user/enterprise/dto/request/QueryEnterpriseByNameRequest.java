package com.yiling.user.enterprise.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 根据名字模糊查询审核通过的公司 Request
 *
 * @author: lun.yu
 * @date: 2022/01/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEnterpriseByNameRequest extends BaseRequest {

    /**
     * 企业ID
     */
    private Long id;

    /**
     * 名称（模糊查询）
     */
    private String name;

    /**
     * ERP对接级别：0-未对接 1-基础对接 2-订单对接 3-发货单对接 (不传入时，则不指定该字段查询)
     */
    private List<Integer> erpSyncLevelList;

    /**
     * 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    private Integer type;

    /**
     * 企业类型集合
     */
    private List<Integer> typeList;

    /**
     * 渠道ID（见数据字典）
     */
    private Long channelId;

    /**
     * 启用状态
     */
    private Integer status;

    /**
     * 审核状态
     */
    private Integer authStatus;

    /**
     * 限制返回数量（不传入默认返回100）
     */
    private Integer limit;

}
