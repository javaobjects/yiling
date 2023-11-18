package com.yiling.user.system.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.system.entity.UserAttachmentDO;
import com.yiling.user.system.enums.UserAttachmentTypeEnum;

/**
 * <p>
 * 用户相关附件表 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-01-21
 */
public interface UserAttachmentService extends BaseService<UserAttachmentDO> {

    /**
     * 批量保存用户附件数据
     *
     * @param userId 用户ID
     * @param attachmentTypeEnum 附件类型枚举
     * @param attachmentKeyList 附件KEY列表
     * @param opUserId 操作人ID
     * @return
     */
    boolean saveBatch(Long userId, UserAttachmentTypeEnum attachmentTypeEnum, List<String> attachmentKeyList, Long opUserId);

    /**
     * 获取用户指定类型的附件列表
     *
     * @param userId 用户ID
     * @param fileTypeList 附件类型列表
     * @return
     */
    List<UserAttachmentDO> listUserAttachmentsByType(Long userId, List<Integer> fileTypeList);

    /**
     * 删除用户指定类型的附件
     *
     * @param userId 用户ID
     * @param attachmentTypeEnum 附件类型枚举
     * @param opUserId 操作人ID
     * @return
     */
    Boolean deleteByUserId(Long userId, UserAttachmentTypeEnum attachmentTypeEnum, Long opUserId);
}
