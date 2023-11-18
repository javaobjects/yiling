package com.yiling.b2b.admin.goods.vo;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CustomerControlPageVO<T> extends Page {

    private Long id;

    private Integer setType;

    private List<CustomerListItemVO> customerList;
}
