package com.yiling.sales.assistant.task.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.sales.assistant.task.dto.TaskMemberDTO;
import com.yiling.sales.assistant.task.dto.request.app.InviteTaskMemberRequest;
import com.yiling.sales.assistant.task.entity.TaskMemberBuyDO;

/**
 * <p>
 * 会员推广-单独购买任务 服务类
 * </p>
 *
 * @author gxl
 * @date 2021-12-16
 */
public interface TaskMemberBuyService extends BaseService<TaskMemberBuyDO> {
    /**
     * 根据任务id查询
     *
     * @param taskId
     * @return
     */
    TaskMemberDTO getMemberById(Long taskId);

    /**
     * 分享邀请购买会员
     *
     * @param inviteTaskMemberRequest
     * @return
     */
    String getPromotionPic(InviteTaskMemberRequest inviteTaskMemberRequest);

    /**
     * 生成二维码
     *
     * @param content
     * @param originalFileName
     * @return
     */
    FileInfo generateQrCode(String content, String originalFileName);

}
