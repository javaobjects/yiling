package com.yiling.cms.question.service;

import java.util.List;

import com.yiling.cms.question.entity.QuestionReplyDO;
import com.yiling.cms.question.entity.QuestionReplyResourceDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 医药代表回复信息关联表 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2022-06-02
 */
public interface QuestionReplyResourceService extends BaseService<QuestionReplyResourceDO> {

    /**
     * 根据回复id获取回复信息
     * @param replyId 回复ID
     * @return
     */
    List<QuestionReplyResourceDO>  listByReplyId(Long replyId);

    /**
     * 根据回复ids获取回复信息
     * @param replyIds
     * @return
     */
    List<QuestionReplyResourceDO> listByReplyIds(List<Long> replyIds );
}
