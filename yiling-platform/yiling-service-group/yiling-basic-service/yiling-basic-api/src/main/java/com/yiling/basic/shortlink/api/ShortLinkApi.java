package com.yiling.basic.shortlink.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.shortlink.dto.ShortLinkDTO;
import com.yiling.basic.shortlink.dto.request.QueryShortLinkPageRequest;

/**
 * 短链接 API
 *
 * @author: lun.yu
 * @date: 2022/01/13
 */
public interface ShortLinkApi {

    /**
     * 生成短链接并保存
     *
     * @param longUrl
     * @return 返回shortUrl
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
