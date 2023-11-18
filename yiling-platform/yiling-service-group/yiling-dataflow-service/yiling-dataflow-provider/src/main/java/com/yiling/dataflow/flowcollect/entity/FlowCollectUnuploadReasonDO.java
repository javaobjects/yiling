package com.yiling.dataflow.flowcollect.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 日流向统计未上传原因表
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_collect_unupload_reason")
public class FlowCollectUnuploadReasonDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 商业公司编码
     */
    private Long crmEnterpriseId;

    /**
     * 统计的时间
     */
    private Date statisticsTime;

    /**
     * 原因类型:1-不合作、2-实际无动销、3-未签直连协议、4-商业更换ERP系统、5-无业务人员负责、6-工具未开启、7-月传、8-未直连、9-商业ERP系统问题、10-重新直连、11-商业不愿意日传、12-直连正常、13-直连工具问题、14-商业服务器问题、15-商业当天未传流向、16-商业FTP上传错误、17-接口报错、18-商业取消直连、19-其他
     */
    private Integer reasonType;

    /**
     * 原因内容
     */
    private String reasonNote;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;

    private String optName;
}
