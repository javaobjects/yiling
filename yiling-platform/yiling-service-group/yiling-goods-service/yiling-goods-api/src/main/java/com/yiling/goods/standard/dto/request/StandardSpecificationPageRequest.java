package com.yiling.goods.standard.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: wei.wang
 * @date: 2021/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardSpecificationPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -33371033128332221L;

    /**
     * 标准库商品id
     */
   private Long standardId;

    /**
     * 标准库规格id
     */
   private List<Long> specIdList;

    /**
     * 商品名称
     */
   private String name;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 批准文号
     */
   private String licenseNo;

    /**
     * 标准库商品名称
     */
   private String standardGoodsName;

   private Integer ylFlag;

}
