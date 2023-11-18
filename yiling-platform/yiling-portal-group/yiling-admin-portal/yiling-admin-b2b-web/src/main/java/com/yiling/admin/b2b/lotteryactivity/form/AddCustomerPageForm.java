package com.yiling.admin.b2b.lotteryactivity.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加指定客户分页列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddCustomerPageForm extends BaseForm {

    /**
     * 抽奖活动ID
     */
    @NotNull
    @ApiModelProperty(value = "抽奖活动ID", required = true)
    private Long lotteryActivityId;

    /**
     * 1-单个添加 2-添加当前页 3-添加搜索结果
     */
    @NotNull
    @ApiModelProperty(value = "1-单个添加 2-添加当前页 3-添加搜索结果", required = true)
    private Integer type;

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private Long id;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String name;

    /**
     * 企业ID集合
     */
    @ApiModelProperty(value = "企业ID集合")
    private List<Long> idList;


}
