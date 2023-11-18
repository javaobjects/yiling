package com.yiling.dataflow.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/2/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowPurchaseBackupListPageRequest extends QueryPageListRequest {

    /**
     * 商业代码（商家eid）
     */
    private Long eid;

    /**
     * 商业名称（商家名称）
     */
    private String ename;

    /**
     * 供应商编码（供应商内码）/客户编码
     */
    private String enterpriseInnerCode;

    /**
     * 供应商名称/客户名称
     */
    private String enterpriseName;

    /**
     * 执业许可证号/社会信用统一代码
     */
    private String licenseNumber;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品批准文号
     */
    private String license;

    /**
     * 采购/下单时间-开始
     */
    private Date startTime;

    /**
     * 采购/下单-结束
     */
    private Date endTime;

    /**
     * 采购/下单-结束
     */
    private Date orderTime;

    /**
     * 订单来源，多选
     */
    private List<String> sourceList;

    /**
     * 订单来源，多选
     */
    private String source;

    /**
     * 批次信息
     */
    private String batchNo;

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
     * 商品生产厂家
     */
    private String manufacturer;

    /**
     * 负责的公司
     */
    private List<Long> eidList;

    /**
     * 企业标签id列表
     */
    private List<Long> enterpriseTagIdList;

    /**
     * 企业标签，多选
     */
    private String enterpriseTagId;

    /**
     * 主键id列表
     */
    private List<Long> idList;

    /**
     * 供应商名称列表
     */
    private List<String> enterpriseNameList;

    /**
     * 商品内码
     */
    private List<String> goodsInSnList;

    /**
     * 供应商id列表
     */
    private List<Long> supplierIdList;

    /**
     * 同步返利状态，0-未同步 1-已同步
     */
    private Integer reportSyncStatus;

    /**
     * 客户crm内码
     */
    private String enterpriseCrmCode;

    /**
     * crm商品编码
     */
    private Long CrmGoodsCode;

    /**
     * 商品规格ID
     */
    private Long specificationId;

    /**
     * 月份
     */
    private String month;

    /**
     * 备份表名称
     */
    private String tableName;

    /**
     * 有无标准库规格关系：0-无, 1-有
     */
    private Integer specificationIdFlag;

    /**
     * 有无crm客户内码；0-无, 1-有
     */
    private Integer enterpriseCrmCodeFlag;
    /**
     * 有无crm商品内码；0-无, 1-有
     */
    private Integer goodsCrmCodeFlag;

    /**
     * 供应商Id标识：0-不限制 1-限制 supplierId 必须
     */
    private Integer supplierIdFlag;

    public void setStartTime(Date startTime) {
        if(ObjectUtil.isNotNull(startTime)){
            this.startTime = DateUtil.beginOfDay(startTime);
        }
    }

    public void setEndTime(Date endTime) {
        if(ObjectUtil.isNotNull(endTime)){
            this.endTime = DateUtil.endOfDay(endTime);
        }
    }
}
