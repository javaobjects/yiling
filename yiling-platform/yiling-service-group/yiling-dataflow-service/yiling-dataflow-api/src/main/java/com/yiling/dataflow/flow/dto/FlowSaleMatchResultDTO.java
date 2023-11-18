package com.yiling.dataflow.flow.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/1/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowSaleMatchResultDTO extends BaseDTO {

    private static final long serialVersionUID = -6985339718606737571L;

    private List<ErpGoodsInfo> erpList;

    private Boolean crmFlag;

    public FlowSaleMatchResultDTO() {
        this.erpList = new ArrayList<>();
        this.crmFlag = false;
    }


    @Data
    public static class ErpGoodsInfo implements Serializable {

        private static final long serialVersionUID = 7392710485809223875L;

        /**
         * 商业公司id
         */
        private Long eid;

        /**
         * 商业公司名称
         */
        private String ename;

        /**
         * 以岭商品名称
         */
        private String ylGoodsName;

        /**
         * 以岭商品规格
         */
        private String ylGoodsSpec;

        /**
         * 以岭商品id
         */
        private Long ylGoodsId;

        /**
         * crm产品代码
         */
        private String crmGoodsCode;

        /**
         * 售卖规格id
         */
        private Long sellSpecId;

        /**
         * 商品批次号
         */
        private String soBatchNo;

        /**
         * 出库时间
         */
        private Date soTime;

        /**
         * 流向客户原始名称
         */
        private String enterpriseName;

        /**
         * 数量
         */
        private BigDecimal soQuantity;
    }
}
