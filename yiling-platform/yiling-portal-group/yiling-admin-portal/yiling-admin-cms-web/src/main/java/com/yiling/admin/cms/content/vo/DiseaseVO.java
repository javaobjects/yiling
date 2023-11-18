package com.yiling.admin.cms.content.vo;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 关联疾病信息
 * @author gaoxinlei
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DiseaseVO extends BaseVO {

    private String name;

    private String code;
}