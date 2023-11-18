package com.yiling.search.word.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.search.word.bo.EsWordAllTypeBO;
import com.yiling.search.word.bo.EsWordExpansionBO;
import com.yiling.search.word.dto.EsWordExpansionDTO;
import com.yiling.search.word.dto.request.QueryEsWordPageListRequest;
import com.yiling.search.word.dto.request.SaveOrUpdateEsWordExpansionRequest;
import com.yiling.search.word.entity.EsWordExpansionDO;
import com.yiling.search.word.enums.EsWordTypeEnum;

/**
 * @author shichen
 * @类名 EsWordExpansionService
 * @描述
 * @创建时间 2022/4/19
 * @修改人 shichen
 * @修改时间 2022/4/19
 **/
public interface EsWordExpansionService  extends BaseService<EsWordExpansionDO> {

    /**
     * type 组装词库bo
     * @param type
     * @return
     */
    List<EsWordExpansionBO> queryWordBOByType(Integer type);

    /**
     * 保存或修改词语
     * @param request
     */
    void saveOrUpdateWord(SaveOrUpdateEsWordExpansionRequest request);

    /**
     * 上传词库至oss
     * @param boMap
     * @param fileName
     * @param filePath
     */
    void uploadEsWord(Map<EsWordTypeEnum,List<EsWordExpansionBO>> boMap,String fileName,String filePath);

    /**
     * 更新数据库数据到es词库文件
     * @param esWordTypeEnum
     * @param index
     */
    void updateEsWordDic(EsWordTypeEnum esWordTypeEnum,String index);

    /**
     * 更新redis标识的词语到es词库文件并同步索引
     */
    void syncEsWordDic();

    /**
     * 立即更新指定索引的词库
     * @param typeEnum
     * @param index
     */
    void syncWordByIndexAndType(EsWordTypeEnum typeEnum,String index,boolean asyncFlag);
    /**
     * 加入同步任务
     * @param type
     * @param index
     * @return
     */
    boolean addSyncJob(Integer type,String index);

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
     * 根据类型获取下载地址
     * @param type
     * @return
     */
    String getDownloadUrl(Integer type);
}
