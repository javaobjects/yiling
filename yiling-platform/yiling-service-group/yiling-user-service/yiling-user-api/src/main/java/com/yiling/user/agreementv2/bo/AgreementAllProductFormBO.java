package com.yiling.user.agreementv2.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.dubbo.common.threadpool.manager.Ring;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议全品表单 BO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-17
 */
@Data
@ApiModel
@Accessors(chain = true)
public class AgreementAllProductFormBO extends AgreementDetailCommonFormBO implements Serializable {

    /**
     * 厂家名称
     */
    private String ename;

    /**
     * 当前商品数
     */
    private Long goodsNumber;

}
