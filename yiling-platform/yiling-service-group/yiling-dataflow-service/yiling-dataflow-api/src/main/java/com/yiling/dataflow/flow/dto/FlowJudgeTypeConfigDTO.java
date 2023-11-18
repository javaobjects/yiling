package com.yiling.dataflow.flow.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-01-12
 */
@Data
public class FlowJudgeTypeConfigDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private Integer orderType;

    private String containKey;

    private String endKey;

    private String type;


}
