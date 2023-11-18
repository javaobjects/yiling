package com.yiling.open.erp.entity;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.erp.util.SignatureAlgorithm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-08-03
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_goods_group_price")
public class ErpGoodsGroupPriceDO extends BaseErpEntity {

    private static final long serialVersionUID = 1L;

    /**
     * erp主键
     */
    @ToString.Include
    private String ggpIdNo;

    /**
     * 客户内码
     */
    @ToString.Include
    private String groupName;

    @ToString.Include
    private String inSn;

    @ToString.Include
    private BigDecimal price;


    @Override
    public Long getPrimaryKey() {
        return this.getId();
    }

    @Override
    public void setPrimaryKey(Long primaryKey) {
        this.setId(primaryKey);
    }

    @Override
    public String getErpPrimaryKey() {
        if (StringUtils.isEmpty(suDeptNo)) {
            return ggpIdNo;
        }
        return ggpIdNo + OpenConstants.SPLITE_SYMBOL + suDeptNo;
    }

    @Override
    public String sign() {
        try {
            return SignatureAlgorithm.byte2hex(SignatureAlgorithm.encryptMD5(toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getTaskNo() {
        return ErpTopicName.ErpGroupPrice.getMethod();
    }


}
