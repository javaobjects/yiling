package com.yiling.search.word.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.search.word.bo.EsWordAllTypeBO;
import com.yiling.search.word.dto.EsWordExpansionDTO;
import com.yiling.search.word.dto.request.QueryEsWordPageListRequest;
import com.yiling.search.word.dto.request.SaveOrUpdateEsWordExpansionRequest;
import com.yiling.search.word.enums.EsWordTypeEnum;

/**
 * @author shichen
 * @类名 EsWordExpansionApi
 * @描述
 * @创建时间 2022/4/25
 * @修改人 shichen
 * @修改时间 2022/4/25
 **/
public interface EsWordExpansionApi {

    /**
     * 类型查询词语分页
     * @param request
     * @return
     */
    Page<EsWordExpansionDTO> pageListByType(QueryEsWordPageListRequest request);

    /**
     * 词语查询 所有类型下的该词语
     * @param word
     * @return
     */
    EsWordAllTypeBO findByWord(String word);

    /**
     * 保存或修改词语
     * @param request
     */
    void saveOrUpdateWord(SaveOrUpdateEsWordExpansionRequest request);

    /**
     * 加入同步任务
     * @param type
     * @return
     */
    boolean addSyncJob(Integer type);

    /**
     * 更新redis标识的词语到es词库文件并同步索引
     */
    void syncEsWordDic();

    /**
     * 立即更新指定索引的词库
     * @param typeEnum
     */
    void syncGoodsWordByType(EsWordTypeEnum typeEnum);


    /**
     * 更新数据库数据到es词库文件
     * @param esWordTypeEnum
     */
    void updateEsWordDic(EsWordTypeEnum esWordTypeEnum);

    /**
     * 根据类型获取下载地址
     * @param type
     * @return
     */
    String getDownloadUrl(Integer type);
}
