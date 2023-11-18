package com.yiling.f2b.admin.agreementv2.form;

import org.springframework.web.multipart.MultipartFile;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议商品导入
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementImportGoodsForm extends BaseForm {

    @ApiModelProperty(value = "文件")
    private MultipartFile file;

    @ApiModelProperty(value = "甲方类型：甲方类型：1-工业生产厂家 2-工业品牌厂家 3-商业供应商 4-代理商")
    private Integer firstType;

    @ApiModelProperty(value = "甲方ID")
    private Long eid;

}
