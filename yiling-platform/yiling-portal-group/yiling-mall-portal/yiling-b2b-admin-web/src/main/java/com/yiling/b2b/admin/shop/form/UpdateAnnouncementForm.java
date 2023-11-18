package com.yiling.b2b.admin.shop.form;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-修改公告 Form
 * </p>
 *
 * @author lun.yu
 * @date 2021/12/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class UpdateAnnouncementForm extends BaseForm {

    /**
     * 店铺公告
     */
    @Length(max = 300)
    @ApiModelProperty("店铺公告")
    private String shopAnnouncement;


}
