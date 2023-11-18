package com.yiling.goods.medicine.dto;

import org.jasypt.util.binary.StrongBinaryEncryptor;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/4/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MergeGoodsRequest extends BaseRequest {

    private static final long serialVersionUID = -7017522519602170570L;

    private Long standardId;

    private String goodsName;

    private Long specificationsId;

    private String specifications;

    private String type;

    private Long mergeSpecificationsId;

    private String mergeSpecifications;

}
