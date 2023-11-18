package com.yiling.user.enterprise.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业分页列表 Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEnterprisePageListRequest extends QueryPageListRequest {

    /**
     * 企业ID
     */
    private Long id;

    /**
     * 企业名称（全模糊查询）
     */
    private String name;

    /**
     * 联系人（全模糊查询）
     */
    private String contactor;

    /**
     * 联系人电话
     */
    private String contactorPhone;

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
     * 状态：0-全部 1-启用 2-停用
     */
    private Integer status;

    /**
     * 认证状态：0-全部 1-未认证 2-认证通过 3-认证不通过
     */
    private Integer authStatus;

    /**
     * 系统处理标记：0-未处理 1-正确 2-异常
     */
    private Integer handleType;

    /**
     * 标签ID列表
     */
    private List<Long> tagIds;

    /**
     * 是否开通B2B商城：0-否 1-是
     */
    private Integer mallFlag;

    /**
     * 是否开通POP：0-否 1-是
     */
    private Integer popFlag;

    /**
     * HMC业务类型：空-全部 0-未开通 1-药+险销售 2-药+险销售与药品兑付
     */
    private Integer hmcType;

    /**
     * 不查询指定类型的企业
     */
    private List<Integer> notInTypeList;

    /**
     * 只查询指定类型的企业
     */
    private List<Integer> inTypeList;

    /**
     * 企业ID
     */
    private List<Long> ids;

    /**
     * 不包含的企业ID
     */
    private List<Long> notInIds;

    /**
     * ERP对接级别：0-未对接 1-基础对接 2-订单对接 3-发货单对接
     */
    private List<Integer> erpSyncLevelList;

    /**
     * 渠道类型集合（见字典：EnterpriseChannelEnum）
     */
    private List<Long> channelIdList;
}
