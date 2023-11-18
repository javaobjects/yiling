package com.yiling.sjms.flee.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 窜货申报表单
 * </p>
 *
 * @author yong.zhang
 * @date 2023-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FleeingGoodsFormDTO extends BaseDTO {

    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * 清洗任务id
     */
    private Long taskId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 上传人id
     */
    private String importUser;

    /**
     * 数据检查 1-通过 2-未通过 3-警告
     */
    private Integer checkStatus;

    /**
     * 导入状态：1-导入成功 2-导入失败
     */
    private Integer importStatus;

    /**
     * 导入失败原因
     */
    private String failReason;

    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 失败结果下载地址
     */
    private String resultUrl;

    /**
     * 上传时间
     */
    private Date importTime;

    /**
     * 文件类型 1-申报 2-申报确认
     */
    private Integer importFileType;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
