package com.yiling.data.center.admin.enterprise.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 客户企业详情页 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/9
 */
@Data
public class CustomerEnterpriseDetailPageVO {

    @ApiModelProperty("企业信息")
    private EnterpriseVO enterpriseInfo;

    @ApiModelProperty("商务联系人个数")
    private Long customerContactCount;

    @ApiModelProperty("商务联系人个数")
    private List<CustomerContactVO> customerContactList;


}
