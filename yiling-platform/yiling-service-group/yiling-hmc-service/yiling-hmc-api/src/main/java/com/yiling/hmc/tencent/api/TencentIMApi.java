package com.yiling.hmc.tencent.api;

import java.util.List;
import java.util.Map;

import com.yiling.hmc.tencent.dto.TencentImSigDTO;
import com.yiling.hmc.tencent.dto.UserProfileItemDTO;
import com.yiling.hmc.tencent.dto.request.CreateGroupRequest;
import com.yiling.hmc.tencent.dto.request.GetGroupInfoRequest;
import com.yiling.hmc.tencent.dto.request.PatientSendMsgToDoctorRequest;
import com.yiling.hmc.tencent.dto.request.SendGroupMsgRequest;
import com.yiling.hmc.tencent.enums.OnlineStatusEnum;

/**
 * @Author fan.shen
 * @Date 2022/5/30
 */
public interface TencentIMApi {

    /**
     * 获取未读消息数量
     *
     * @param userId
     * @return
     */
    Integer unreadMsgNum(Long userId);

    /**
     * 拉取资料
     *
     * @param userId
     * @return
     */
    UserProfileItemDTO portraitGet(Long userId, Integer type);

    /**
     * 设置资料
     *
     * @param userId
     * @return
     */
    Boolean portraitSet(Long userId, Integer type);

    /**
     * 患者给医生发送消息
     *
     * @param request
     */
    String patientSendMsgToDoctor(PatientSendMsgToDoctorRequest request);

    /**
     * 生成签名
     *
     * @param currentUserId
     * @return
     */
    TencentImSigDTO getTencentIMSig(Long currentUserId, Integer type);

    /**
     * 账户导入
     *
     * @param userId
     * @return
     */
    Boolean accountImport(Long userId, Integer type);

    /**
     * 查询账户
     *
     * @param userId
     * @return
     */
    Boolean accountCheck(Long userId, Integer type);

    /**
     * 查询账号的在线状态
     *
     * @param accountList 账号集合
     * @return 返回的用户状态，目前支持的状态有：
     * 前台运行状态（Online）：客户端登录后和即时通信 IM 后台有长连接
     * 后台运行状态（PushOnline）：iOS 和 Android 进程被 kill 或因网络问题掉线，进入 PushOnline 状态，此时仍然可以接收消息的离线推送。客户端切到后台，但是进程未被手机操作系统 kill 掉时，此时状态仍是 Online
     * 未登录状态（Offline）：客户端主动退出登录或者客户端自上一次登录起7天之内未登录过
     * 如果用户是多终端登录，则只要有一个终端的状态是 Online ，该字段值就是 Online
     */
    Map<String, Integer> queryOnlineStatus(List<String> accountList);

    /**
     * 创建群组
     *
     * @param request
     * @return
     */
    String createGroup(CreateGroupRequest request);

    /**
     * 发送群组消息
     *
     * @param request
     * @return
     */
    String sendGroupMsg(SendGroupMsgRequest request);

    /**
     * 获取群组信息
     *
     * @param request
     * @return
     */
    String getGroupInfo(GetGroupInfoRequest request);

}
