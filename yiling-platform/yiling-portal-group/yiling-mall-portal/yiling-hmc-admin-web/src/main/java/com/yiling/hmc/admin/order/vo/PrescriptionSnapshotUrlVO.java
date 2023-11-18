package com.yiling.hmc.admin.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 多张处方-方便前端修改图片时图片的处理
 *
 * @author: yong.zhang
 * @date: 2022/8/9
 */
@Data
public class PrescriptionSnapshotUrlVO {

    @ApiModelProperty("处方快照url的key")
    private String prescriptionSnapshotKey;

    @ApiModelProperty("处方快照url")
    private String prescriptionSnapshotUrl;
}
