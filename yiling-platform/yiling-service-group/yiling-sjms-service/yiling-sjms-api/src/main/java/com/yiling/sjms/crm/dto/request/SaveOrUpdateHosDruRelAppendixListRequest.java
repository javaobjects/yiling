package com.yiling.sjms.crm.dto.request;

import java.io.Serializable;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.sjms.form.enums.FormTypeEnum;

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
public class SaveOrUpdateHosDruRelAppendixListRequest extends BaseRequest {

    private static final long serialVersionUID = -7776135584880579499L;
    /**
     * form表主键
     */
    private Long formId;

    private FormTypeEnum formTypeEnum;

    private String empName;

    private List<HospitalDrugstoreRelAppendixRequest> appendixList;


    @Data
    public static class HospitalDrugstoreRelAppendixRequest implements Serializable {

        private static final long serialVersionUID = 9139645980850550497L;

        /**
         * 文件url
         */
        private String url;

        /**
         * 文件key
         */
        private String key;

        /**
         * 文件名称
         */
        private String name;
    }
}
