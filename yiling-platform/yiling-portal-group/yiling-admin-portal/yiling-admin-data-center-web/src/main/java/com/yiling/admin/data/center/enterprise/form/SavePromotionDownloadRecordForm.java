package com.yiling.admin.data.center.enterprise.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存企业推广下载记录 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-02
 */
@Data
@Accessors(chain = true)
public class SavePromotionDownloadRecordForm extends BaseForm {

    /**
     * 推广方ID
     */
    @NotNull
    @ApiModelProperty(value = "推广方ID", required = true)
    private Long promoterId;

}
