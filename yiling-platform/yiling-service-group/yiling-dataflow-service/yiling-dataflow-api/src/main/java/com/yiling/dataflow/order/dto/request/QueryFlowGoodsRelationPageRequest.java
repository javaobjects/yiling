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
 * @date: 2022/5/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowGoodsRelationPageRequest extends QueryPageListRequest {

    /**
     * 商业id
     */
    private Long eid;

    /**
     * 以岭商品名称
     */
    private String ylGoodsName;

    /**
     * 创建时间-开始
     */
    private Date createTimeStart;

    /**
     * 创建时间-结束
     */
    private Date createTimeEnd;

    /**
     * 操作人姓名
     */
    private List<Long> opUserIdList;

    /**
     * 有无商品关系：0-无, 1-有
     */
    private Integer relationFlag;

    /**
     * 商业商品名称
     */
    private String goodsName;

    /**
     * 生产厂家
     */
    private String goodsManufacturer;

    /**
     * 商品关系标签：0-无标签 1-以岭品 2-非以岭品 3-中药饮片
     */
    private Integer goodsRelationLabel;

    /**
     * 商品关系标签列表，查询用
     */
    private List<Integer> goodsRelationLabelList;

    /**
     * 商品关系标签列表，导出参数
     * 取值后再转为 goodsRelationLabelList 用于查询
     */
    private String goodsRelationLabelStr;


    public void setCreateTimeStart(Date createTimeStart) {
        if(ObjectUtil.isNotNull(createTimeStart)){
            this.createTimeStart = DateUtil.beginOfDay(createTimeStart);
        } else {
            this.createTimeStart = null;
        }
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        if(ObjectUtil.isNotNull(createTimeEnd)){
            this.createTimeEnd = DateUtil.endOfDay(createTimeEnd);
        } else {
            this.createTimeEnd = null;
        }
    }

}
