package com.yiling.admin.hmc.qrcode.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/4/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryQrcodeStatisticsPageForm extends QueryPageListForm {

    private Date startTime;

    private Date endTime;
}