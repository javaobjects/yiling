package com.yiling.sjms.crm.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/6/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateHosDruRelAppendixListForm extends BaseForm {

    /**
     * form表主键
     */
    @ApiModelProperty(value = "主表单id")
    private Long formId;

    /**
     * 附件信息列表
     */
    @ApiModelProperty(value = "附件信息列表")
    private List<HospitalDrugstoreRelAppendixForm> appendixList;



    @Data
    public static class HospitalDrugstoreRelAppendixForm {

        @ApiModelProperty(value = "文件url")
        private String url;

        @ApiModelProperty(value = "文件key")
        private String key;

        @ApiModelProperty(value = "文件名称")
        private String name;
    }

}
