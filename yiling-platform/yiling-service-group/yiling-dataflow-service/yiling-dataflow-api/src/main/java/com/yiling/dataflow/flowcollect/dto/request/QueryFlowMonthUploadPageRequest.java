package com.yiling.dataflow.flowcollect.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.After;
import com.yiling.user.common.util.bean.Before;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.Like;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询月流向上传记录分页列表 Request
 *
 * @author lun.yu
 * @date 2023-03-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowMonthUploadPageRequest extends QueryPageListRequest {

    /**
     * 文件名
     */
    @Like
    private String fileName;

    /**
     * 开始上传时间
     */
    @Before(name = "create_time")
    private Date startCreateTime;

    /**
     * 结束上传时间
     */
    @After(name = "create_time")
    private Date endCreateTime;

    /**
     * 流向数据类型：1-销售 2-库存 3-采购
     */
    @Eq
    private Integer dataType;

    /**
     * 检查状态：1-通过 2-未通过 3-警告
     */
    @Eq
    private Integer checkStatus;

    /**
     * 导入状态：1-导入成功 2-导入失败
     */
    @Eq
    private Integer importStatus;

    /**
     * 创建人
     */
    @Eq
    private Long createUser;

}
