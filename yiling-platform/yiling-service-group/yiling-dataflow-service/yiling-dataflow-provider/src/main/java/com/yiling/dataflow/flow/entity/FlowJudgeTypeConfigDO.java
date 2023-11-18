package com.yiling.dataflow.flow.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.baomidou.mybatisplus.annotation.Version;
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
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_judge_type_config")
public class FlowJudgeTypeConfigDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    private Integer orderType;

    private String containKey;

    private String endKey;

    private String type;


}
