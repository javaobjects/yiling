package com.yiling.admin.cms.content.form;

import com.yiling.framework.common.base.form.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询会议管理列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMeetingListForm extends BaseForm {

    /**
     * 标题
     */
    private String title;


}
