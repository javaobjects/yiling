package com.yiling.sjms.flee.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: xinxuan.jia
 * @date: 2023/6/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveSelectAppealFlowFormRequest extends BaseRequest {

//    /**
//     * 选择流向表单
//     */
//    private SelectAppealFlowFormRequest appealFlowDataDetailForm;

    /**
     * 发起人姓名
     */
    private String empName;

    /**
     * 发起人姓名
     */
    private String empId;

    /**
     * 表单id
     */
    private Long formId;

    /**
     * 申诉类型 4终端类型申诉、5其他 6、漏做客户关系对照 7、未备案商业销售到锁定终端 8、医院分院以总院名头进货
     * 9、医院的院内外药店进货 10、医联体、医共体共用进货名头 11、互联网医院无法体现医院名字 12、药店子公司以总部名头进货 13、产品对照错误
     */
    private Integer appealType;

    /**
     * 传输方式 2、选择流向
     */
    private Integer transferType;

    /**
     * 选择流向数据确认
     */
    private List<SelectAppealConfirmFlowFormRequest> appealFlowDataDetailFormList;

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
     * 选择流向表中主键id
     */
    private Long id;

    /**
     * 申诉后产品品规
     */
    private String appealGoodsSpec;
}
