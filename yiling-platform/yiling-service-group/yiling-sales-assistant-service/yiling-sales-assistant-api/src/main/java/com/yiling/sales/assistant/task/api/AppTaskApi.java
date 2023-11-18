package com.yiling.sales.assistant.task.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sales.assistant.task.dto.TaskAccompanyingBillDTO;
import com.yiling.sales.assistant.task.dto.TaskMemberPromotiondDTO;
import com.yiling.sales.assistant.task.dto.TaskTerminalDTO;
import com.yiling.sales.assistant.task.dto.app.AccompanyingBillDTO;
import com.yiling.sales.assistant.task.dto.app.GoodsProgressDTO;
import com.yiling.sales.assistant.task.dto.app.MyTaskDTO;
import com.yiling.sales.assistant.task.dto.app.MyTaskDetailDTO;
import com.yiling.sales.assistant.task.dto.app.MyTaskProgressDTO;
import com.yiling.sales.assistant.task.dto.app.TaskDTO;
import com.yiling.sales.assistant.task.dto.app.TaskDetailDTO;
import com.yiling.sales.assistant.task.dto.app.TaskDistributorDTO;
import com.yiling.sales.assistant.task.dto.app.TaskGoodsDTO;
import com.yiling.sales.assistant.task.dto.request.QueryTaskAccompanyBillPageRequest;
import com.yiling.sales.assistant.task.dto.request.app.GetTaskDetailRequest;
import com.yiling.sales.assistant.task.dto.request.app.InviteTaskMemberRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryAccompanyingBillPageRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryMyTaskDetailRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryMyTaskRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskDistributorPageRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskGoodsRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskPageRequest;
import com.yiling.sales.assistant.task.dto.request.app.QueryTaskTerminalPageRequest;
import com.yiling.sales.assistant.task.dto.request.app.SaveAccompanyingBillRequest;
import com.yiling.sales.assistant.task.dto.request.app.TakeTaskRequest;

/**
 * app端taskApi
 * @author: ray
 * @date: 2021/9/27
 */
public interface AppTaskApi {
    /**
     * 任务分页列表
     * @param queryTaskPageRequest
     * @return
     */
    Page<TaskDTO> listTaskPage(QueryTaskPageRequest queryTaskPageRequest);

    /**
     * 任务详情
     * @param getTaskDetailRequest
     * @return
     */
    TaskDetailDTO getTaskDetail(GetTaskDetailRequest getTaskDetailRequest);

    /**
     * 任务详情页-商品
     * @param queryTaskGoodsRequest
     * @return
     */
    List<TaskGoodsDTO>  listTaskGoods(QueryTaskGoodsRequest queryTaskGoodsRequest);

    /**
     * 任务详情-满赠会员
     * @param taskId
     * @return
     */
    TaskMemberPromotiondDTO getMemberPromotion(Long taskId);

    /**
     * 我的任务列表
     * @param queryMyTaskRequest
     * @return
     */
    Page<MyTaskDTO> listMyTaskPage(QueryMyTaskRequest queryMyTaskRequest);

    /**
     * 我的任务详情
     * @param queryMyTaskDetailRequest
     * @return
     */
    MyTaskDetailDTO getMyTaskDetail(QueryMyTaskDetailRequest queryMyTaskDetailRequest);

    /**
     * 我的任务详情-任务进度
     * @param userTaskId
     * @return
     */
    MyTaskProgressDTO getMyTaskProgress(Long userTaskId);

    /**
     * 我的任务详情-多品销售商品进度
     * @param userTaskId
     * @return
     */
    List<GoodsProgressDTO> getGoodsProgress(Long userTaskId);

    /**
     * 选择终端-我的终端
     * @param queryTaskTerminalPageRequest
     * @return
     */
    Page<TaskTerminalDTO> listTaskTerminalPage(QueryTaskTerminalPageRequest queryTaskTerminalPageRequest);

    /**
     * 可选终端
     * @param queryTaskTerminalPageRequest
     * @return
     */
    Page<TaskTerminalDTO>  listTaskAllTerminalPage(QueryTaskTerminalPageRequest queryTaskTerminalPageRequest);

    /**
     * 承接任务
     * @param takeTaskRequest
     * @return
     */
    Boolean takeTask(TakeTaskRequest takeTaskRequest);

    /**
     * 获取带二维码的分享海报
     * @param inviteTaskMemberRequest
     * @return
     */
    String getPromotionPic(InviteTaskMemberRequest inviteTaskMemberRequest);

    /**
     * 任务详情-查看配送商
     * @param queryTaskDistributorPageRequest
     * @return
     */
    Page<TaskDistributorDTO> listTaskDistributorPage(QueryTaskDistributorPageRequest queryTaskDistributorPageRequest);

    /**
     * 保存随货同行单
     * @param request
     */
    void save(SaveAccompanyingBillRequest request);

    /**
     * 单个随货同行单
     * @param id
     * @return
     */
    AccompanyingBillDTO getDetailById(Long id);

    /**
     * 分页列表
     * @param request
     * @return
     */
    Page<AccompanyingBillDTO> queryAccompanyingBillPage(QueryAccompanyingBillPageRequest request);

    /**
     * 用户任务随货同行单列表
     * @param queryTaskAccompanyBillPageRequest
     * @return
     */
    Page<TaskAccompanyingBillDTO> queryTaskAccompanyBillPage(QueryTaskAccompanyBillPageRequest queryTaskAccompanyBillPageRequest);
}
