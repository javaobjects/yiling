package com.yiling.goods.medicine.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author shichen
 * @类名 DistributorAgreementGoodsBO
 * @描述
 * @创建时间 2022/3/3
 * @修改人 shichen
 * @修改时间 2022/3/3
 **/
@Data
public class DistributorAgreementGoodsBO implements Serializable {

    private Long ylGoodsId;

    private Long distributorGoodsId;

    private String AgreementIds;
}
