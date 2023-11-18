package com.yiling.basic.shortlink.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.shortlink.dto.ShortLinkDTO;
import com.yiling.basic.shortlink.dto.request.QueryShortLinkPageRequest;
import com.yiling.basic.shortlink.entity.ShortLinkDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 短链接 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-01-13
 */
public interface ShortLinkService extends BaseService<ShortLinkDO> {


    /**
     * 保存短链接
     *
     * @param longUrl
     * @return
     */
    String generatorShortLink(String longUrl);

    /**
     * 通过Key查询单个短链接
     * @param key
     * @return 长链接
     */
    String getByKey(String key);

    /**
     * 分页查询
     * @param request
     * @return
     */
    Page<ShortLinkDTO> queryListPage(QueryShortLinkPageRequest request);
}
