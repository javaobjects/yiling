package com.yiling.admin.erp.enterprisecustomer.form;

import java.util.List;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryEnterprisePageListForm
 * @描述
 * @创建时间 2023/3/22
 * @修改人 shichen
 * @修改时间 2023/3/22
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "检索企业参数")
public class QueryEnterprisePageListForm extends QueryPageListForm {

    /**
     * 企业名
     */
    @ApiModelProperty(value = "企业名")
    private String name;

    @ApiModelProperty(value = "企业指定类型 1.工业  2.商业 3.连锁总店 4.连锁直营 5.连锁加盟 6.单体药房 7.医疗机构 8.诊所")
    private List<Integer> inTypeList;
}
