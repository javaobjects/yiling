package com.yiling.sjms.demo.entity;

import com.yiling.sjms.form.entity.FormDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: xuan.zhou
 * @date: 2023/2/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DemoFormDO extends FormDO {

    private String extField;
}
