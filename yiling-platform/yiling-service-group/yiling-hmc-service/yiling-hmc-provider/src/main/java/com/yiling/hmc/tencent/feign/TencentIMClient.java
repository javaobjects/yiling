package com.yiling.hmc.tencent.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 腾讯服务模块接口调用
 *
 * @author: fan.shen
 * @date: 2023-04-28
 */
@FeignClient(name = "tencentIMClient", url = "${tencent.service.baseUrl}")
public interface TencentIMClient {

    /**
     * 获取未读消息
     *
     * @param param
     * @return
     */
    @PostMapping("/v4/openim/get_c2c_unread_msg_num")
    String unreadMsgNum(@RequestBody String param);

    /**
     * 拉取资料
     *
     * @param param
     * @return
     */
    @PostMapping("v4/profile/portrait_get")
    String portraitGet(@RequestBody String param);

    /**
     * 设置资料
     *
     * @param param
     * @return
     */
    @PostMapping("v4/profile/portrait_set")
    String portraitSet(@RequestBody String param);

    /**
     * 患者发送消息给医生
     *
     * @param param
     * @return
     */
    @PostMapping("v4/openim/sendmsg")
    String patientSendMsgToDoctor(@RequestBody String param);

    /**
     * 导入账号
     *
     * @param param
     * @return
     */
    @PostMapping("v4/im_open_login_svc/account_import")
    String accountImport(@RequestBody String param);

    /**
     * 查询账号
     *
     * @param param
     * @return
     */
    @PostMapping("v4/im_open_login_svc/account_check")
    String accountCheck(@RequestBody String param);

    /**
     * 查询帐号在线状态
     *
     * @param param
     * @return
     */
    @PostMapping("/v4/openim/query_online_status")
    String queryOnlineStatus(@RequestBody String param);

    /**
     * 创建群组
     *
     * @param param
     * @return
     */
    @PostMapping("v4/group_open_http_svc/create_group")
    String createGroup(@RequestBody String param);

    /**
     * 发送群组消息
     *
     * @param param
     * @return
     */
    @PostMapping("v4/group_open_http_svc/send_group_msg")
    String sendGroupMsg(@RequestBody String param);

    /**
     * 获取群资料
     *
     * @param param
     * @return
     */
    @PostMapping("v4/group_open_http_svc/get_group_info")
    String getGroupInfo(@RequestBody String param);

}
