package com.yiling.sjms.flee.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * 选择流向数据主表数据
 * @author: xinxuan.jia
 * @date: 2023/6/25
 */
@Data
public class SelectAppealFlowFormRequest extends BaseRequest {

    /**
     * 申诉数量
     */
    private BigDecimal appealFinalQuantity;
    /**
     * 根据变化类型变化的机构名称编码、产品名称编码、机构属性编码
     */
    private Long changeCode;
    /**
     * 根据申诉类型变化的机构名称、产品名称、机构属性字段
     */
    private String changeName;
    /**
     * 根据申诉类型变化的变化类型：1、标准机构名称 2、标准产品名称 3、终端类型
     */
    private Integer changeType;
    /**
     * 选择申诉流向数据id
     */
    private Long selectFlowId;

    /**
     * 选择申诉源流向数据key
     */
    private String flowKey;
    /**
     * 选择流向表中主键id
     */
    private Long id;

    /**
     * 申诉后产品品规
     */
    private String appealGoodsSpec;

}
