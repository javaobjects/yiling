package com.yiling.cms.document.dao;

import org.springframework.stereotype.Repository;

import com.yiling.cms.content.entity.ContentDO;
import com.yiling.cms.document.entity.DocumentDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 文献 Dao 接口
 * </p>
 *
 * @author gxl
 * @date 2022-06-02
 */
@Repository
public interface DocumentMapper extends BaseMapper<DocumentDO> {
    /**
     * 更新阅读量
     * @param documentDO
     * @return
     */
    int  updatePv(DocumentDO documentDO);
}
