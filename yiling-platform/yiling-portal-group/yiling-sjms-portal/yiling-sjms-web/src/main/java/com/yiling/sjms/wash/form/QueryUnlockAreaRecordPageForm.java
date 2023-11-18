package com.yiling.sjms.wash.form;

import java.util.Date;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUnlockAreaRecordPageForm extends QueryPageListForm {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 非锁客户分类
     */
    @ApiModelProperty(value = "非锁客户分类")
    private Integer customerClassification;

    /**
     * 品种id
     */
    @ApiModelProperty(value = "品种id")
    private Long categoryId;

    /**
     * 代表姓名
     */
    @ApiModelProperty(value = "代表工号")
    private String representativeCode;

    /**
     * 主管姓名
     */
    @ApiModelProperty(value = "主管工号")
    private String executiveCode;

    @ApiModelProperty(value = "最后操作开始时间")
    private Date startOpTime;

    @ApiModelProperty(value = "最后操作结束时间")
    private Date endOpTime;


}
