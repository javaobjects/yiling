package com.yiling.search.word.service.impl;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.aliyun.oss.model.ObjectMetadata;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.search.word.bo.EsWordAllTypeBO;
import com.yiling.search.word.bo.EsWordExpansionBO;
import com.yiling.search.word.dao.EsWordExpansionMapper;
import com.yiling.search.word.dto.EsWordExpansionDTO;
import com.yiling.search.word.dto.request.QueryEsWordPageListRequest;
import com.yiling.search.word.dto.request.SaveOrUpdateEsWordExpansionRequest;
import com.yiling.search.word.entity.EsWordExpansionDO;
import com.yiling.search.word.enums.EsWordErrorEnum;
import com.yiling.search.word.enums.EsWordStatusEnum;
import com.yiling.search.word.enums.EsWordTypeEnum;
import com.yiling.search.word.service.EsWordExpansionService;
import com.yiling.search.util.EsClientUtils;
import com.yiling.search.word.util.EsWordUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * @author shichen
 * @类名 EsWordExpansionServiceImpl
 * @描述 es扩展词服务实现类
 * @创建时间 2022/4/19
 * @修改人 shichen
 * @修改时间 2022/4/19
 **/
@Service
@Slf4j
public class EsWordExpansionServiceImpl extends BaseServiceImpl<EsWordExpansionMapper, EsWordExpansionDO> implements EsWordExpansionService {

    @Resource
    private EsClientUtils esClientUtils;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String ES_WORD_REDIS_PREFIX = "es_word_dic_";

    @Autowired
    private FileService fileService;
    @Override
    public List<EsWordExpansionBO> queryWordBOByType(Integer type) {
        EsWordTypeEnum typeEnum = EsWordTypeEnum.getByType(type);
        List<EsWordExpansionBO> boList=null;
        switch (typeEnum){
            case EXPAND:
                //获取主词列表
                List<EsWordExpansionDO> expandList = queryWordByTypeAndRefId(EsWordTypeEnum.EXPAND.getType(),0L);
                boList = PojoUtils.map(expandList, EsWordExpansionBO.class);
                break;
            case STOP:
                List<EsWordExpansionDO> stopList = queryWordByTypeAndRefId(EsWordTypeEnum.STOP.getType(),0L);
                boList = PojoUtils.map(stopList, EsWordExpansionBO.class);
                break;
            case ONE_WAY_SYNONYM:
                List<EsWordExpansionDO> oneWayMainList = queryWordByTypeAndRefId(EsWordTypeEnum.ONE_WAY_SYNONYM.getType(),0L);
                List<EsWordExpansionBO> oneWayBOList = Lists.newArrayList();
                List<Long> oneWayIds = oneWayMainList.stream().map(EsWordExpansionDO::getId).collect(Collectors.toList());
                //通过主词下的关联词
                Map<Long, List<EsWordExpansionDO>> oneWayListMap = qureyRefWordMapByTypeAndMainIds(EsWordTypeEnum.ONE_WAY_SYNONYM.getType(), oneWayIds);
                oneWayMainList.forEach(mainWord->{
                    List<EsWordExpansionDO> list = oneWayListMap.get(mainWord.getId());
                    if(CollectionUtil.isNotEmpty(list)){
                        EsWordExpansionBO bo = PojoUtils.map(mainWord,EsWordExpansionBO.class);
                        bo.setRefWordList(PojoUtils.map(list,EsWordExpansionDTO.class));
                        oneWayBOList.add(bo);
                    }
                });
                boList=oneWayBOList;
                break;
            case TWO_WAY_SYNONYM:
                List<Long> groupIds = this.baseMapper.getGroupByType(EsWordTypeEnum.TWO_WAY_SYNONYM.getType());
                Map<Long, List<EsWordExpansionDO>> twoWayListMap = qureyRefWordMapByTypeAndMainIds(EsWordTypeEnum.TWO_WAY_SYNONYM.getType(), groupIds);
                List<EsWordExpansionBO> twoWayBOList=Lists.newArrayList();
                twoWayListMap.keySet().forEach(group->{
                    List<EsWordExpansionDO> list = twoWayListMap.get(group);
                    if(CollectionUtil.isNotEmpty(list)&&list.size()>1){
                        EsWordExpansionBO bo = new EsWordExpansionBO();
                        bo.setRefWordList(PojoUtils.map(list,EsWordExpansionDTO.class));
                        twoWayBOList.add(bo);
                    }
                });
                boList=twoWayBOList;
                break;
        }
        return boList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateWord(SaveOrUpdateEsWordExpansionRequest request) {
        this.saveOrUpdateWordFilter(request);
        List<EsWordExpansionDO> doList = this.saveOrUpdateWordHandler(request);
        if(null!=request.getId() && 0L!=request.getId()){
            this.saveOrUpdateBatch(doList);
        }else {
            //生成主词
            EsWordExpansionDO mainDO = doList.stream().filter(wordDO -> wordDO.getRefId() == 0L).findFirst().orElse(null);
            this.save(mainDO);
            //保存修改关联词
            //双向同义词，所有词的refId=组号，第一个生成的EsWordExpansionDO主键为组号
            if(EsWordTypeEnum.TWO_WAY_SYNONYM.getType().equals(request.getType())){
                mainDO.setRefId(mainDO.getId());
                this.updateById(mainDO);
            }
            doList.remove(mainDO);
            doList.forEach(wordDO ->{
                wordDO.setRefId(mainDO.getId());
                wordDO.setType(request.getType());
            });
            this.saveBatch(doList);
        }
    }

    @Override
    public void uploadEsWord(Map<EsWordTypeEnum,List<EsWordExpansionBO>> boMap,String fileName,String filePath) {
        List<String> stringList = EsWordUtils.boToEsStr(boMap);
        byte[] strBytes = EsWordUtils.esStrToByte(stringList);
        ObjectMetadata metadata = new ObjectMetadata();
        //当lastModified变更时，扩展词库才会动态更新
        metadata.setLastModified(new Date());
        metadata.setContentType("text/x-c");
        try {
            fileService.fixedPathUpload(new ByteArrayInputStream(strBytes),fileName, FileTypeEnum.ES_WORD_DIC,filePath,metadata);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new BusinessException(EsWordErrorEnum.ERROR,"es词库数据上传oss失败");
        }

    }

    @Override
    public void updateEsWordDic(EsWordTypeEnum esWordTypeEnum,String index) {
        if(null==esWordTypeEnum){
            throw new BusinessException(EsWordErrorEnum.ERROR,"es词库枚举错误");
        }
        if(StringUtils.isBlank(index)){
            throw new BusinessException(EsWordErrorEnum.ERROR,"刷新索引不能为空");
        }
        HashMap<EsWordTypeEnum, List<EsWordExpansionBO>> map=new HashMap<>();
        switch (esWordTypeEnum){
            case EXPAND:
                List<EsWordExpansionBO> expandList = this.queryWordBOByType(EsWordTypeEnum.EXPAND.getType());
                map.put(EsWordTypeEnum.EXPAND,expandList);
                break;
            case STOP:
                List<EsWordExpansionBO> stopList = this.queryWordBOByType(EsWordTypeEnum.STOP.getType());
                map.put(EsWordTypeEnum.STOP,stopList);
                break;
            case ONE_WAY_SYNONYM:
            case TWO_WAY_SYNONYM:
                List<EsWordExpansionBO> ontWayList = this.queryWordBOByType(EsWordTypeEnum.ONE_WAY_SYNONYM.getType());
                List<EsWordExpansionBO> twoWayList = this.queryWordBOByType(EsWordTypeEnum.TWO_WAY_SYNONYM.getType());
                map.put(EsWordTypeEnum.ONE_WAY_SYNONYM,ontWayList);
                map.put(EsWordTypeEnum.TWO_WAY_SYNONYM,twoWayList);
                break;
        }
        this.uploadEsWord(map,esWordTypeEnum.getFileName(),esWordTypeEnum.getFilePath());
    }

    @Override
    public void syncEsWordDic() {
        //获取redis 需要更新的索引名
        for(EsWordTypeEnum typeEnum:EsWordTypeEnum.values()){
            Set<String> indexSet = stringRedisTemplate.opsForSet().members(ES_WORD_REDIS_PREFIX + typeEnum.getType());
            if(CollectionUtil.isNotEmpty(indexSet)){
                indexSet.forEach(index->{
                    syncWordByIndexAndType(typeEnum,index,false);
                });
            }
        }
    }

    @Override
    public void syncWordByIndexAndType(EsWordTypeEnum typeEnum, String index,boolean asyncFlag) {
        Boolean syncFlag = stringRedisTemplate.opsForSet().isMember(ES_WORD_REDIS_PREFIX + typeEnum.getType(), index);
        if(syncFlag){
            log.info("更新索引开始------类型：{}，索引名：{}",typeEnum.getName(),index);
            long start=System.currentTimeMillis();
            //更新所有数据索引
            esClientUtils.updateByQuery(index,asyncFlag);
            //更新成功从redis移除
            if(typeEnum.equals(EsWordTypeEnum.ONE_WAY_SYNONYM)||typeEnum.equals(EsWordTypeEnum.TWO_WAY_SYNONYM)){
                //单向同义词和双向同义词为同一文件不用重复更新
                stringRedisTemplate.opsForSet().remove(ES_WORD_REDIS_PREFIX + EsWordTypeEnum.ONE_WAY_SYNONYM.getType(),index);
                stringRedisTemplate.opsForSet().remove(ES_WORD_REDIS_PREFIX + EsWordTypeEnum.TWO_WAY_SYNONYM.getType(),index);
            }else {
                stringRedisTemplate.opsForSet().remove(ES_WORD_REDIS_PREFIX + typeEnum.getType(),index);
            }
            long end=System.currentTimeMillis();
            log.info("更新索引完成------类型：{}，索引名：{}，耗时：{}",typeEnum.getName(),index,end-start);
        }
    }

    @Override
    public boolean addSyncJob(Integer type, String index) {
        stringRedisTemplate.opsForSet().add(ES_WORD_REDIS_PREFIX+type,index);
        return true;
    }

    @Override
    public Page<EsWordExpansionDTO> pageListByType(QueryEsWordPageListRequest request) {
        QueryWrapper<EsWordExpansionDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(EsWordExpansionDO::getType,request.getType());
        if(StringUtils.isNotBlank(request.getWord())){
            queryWrapper.lambda().eq(EsWordExpansionDO::getWord,request.getWord());
        }
        if(EsWordTypeEnum.ONE_WAY_SYNONYM.getType().equals(request.getType())){
            queryWrapper.lambda().eq(EsWordExpansionDO::getRefId,0L);
        }
        Page<EsWordExpansionDO> doPage = this.page(request.getPage(), queryWrapper);
        return PojoUtils.map(doPage,EsWordExpansionDTO.class);
    }

    @Override
    public EsWordAllTypeBO findByWord(String word) {
        if(StringUtils.isBlank(word)){
            throw new BusinessException(EsWordErrorEnum.EMPTY_WORD);
        }
        EsWordAllTypeBO allTypeBO = new EsWordAllTypeBO();
        allTypeBO.setMainWord(word);
        //扩展词赋值
        EsWordExpansionDO expansionDO = queryWord(EsWordTypeEnum.EXPAND.getType(), word, null);
        allTypeBO.setExpansionWord(PojoUtils.map(expansionDO,EsWordExpansionBO.class));
        //停止词赋值
        EsWordExpansionDO stopDO = queryWord(EsWordTypeEnum.STOP.getType(), word, null);
        allTypeBO.setStopWord(PojoUtils.map(stopDO,EsWordExpansionBO.class));
        //单向同义词赋值
        EsWordExpansionDO oneWayDO = queryWord(EsWordTypeEnum.ONE_WAY_SYNONYM.getType(), word, 0L);
        if(null!=oneWayDO){
            List<EsWordExpansionDO> oneRefList = queryRefWord(EsWordTypeEnum.ONE_WAY_SYNONYM.getType(), oneWayDO.getId());
            EsWordExpansionBO oneWayBO = PojoUtils.map(oneWayDO, EsWordExpansionBO.class);
            oneWayBO.setRefWordList(PojoUtils.map(oneRefList,EsWordExpansionDTO.class));
            allTypeBO.setOneWaySynonym(oneWayBO);
        }
        //双向同义词赋值
        EsWordExpansionDO twoWayDO = queryWord(EsWordTypeEnum.TWO_WAY_SYNONYM.getType(), word, null);
        if(null!=twoWayDO){
            List<EsWordExpansionDO> twoWayDOList = queryRefWord(EsWordTypeEnum.TWO_WAY_SYNONYM.getType(), twoWayDO.getRefId());
            List<EsWordExpansionDO> twoRefList = twoWayDOList.stream().filter(e -> !twoWayDO.getWord().equals(e.getWord())).collect(Collectors.toList());
            EsWordExpansionBO twoWayBO = PojoUtils.map(twoWayDO, EsWordExpansionBO.class);
            twoWayBO.setRefWordList(PojoUtils.map(twoRefList,EsWordExpansionDTO.class));
            allTypeBO.setTwoWaySynonym(twoWayBO);
        }
        return allTypeBO;
    }

    @Override
    public String getDownloadUrl(Integer type) {
        EsWordTypeEnum esWordTypeEnum = EsWordTypeEnum.getByType(type);
        if(null==esWordTypeEnum){
            throw new BusinessException(EsWordErrorEnum.NOT_FOUND_TYPE);
        }
        return fileService.getFixedUrl(esWordTypeEnum.getFileName(),esWordTypeEnum.getFilePath(),FileTypeEnum.ES_WORD_DIC);
    }

    private List<EsWordExpansionDO> saveOrUpdateWordHandler(SaveOrUpdateEsWordExpansionRequest request){
        EsWordTypeEnum typeEnum = EsWordTypeEnum.getByType(request.getType());
        List<EsWordExpansionDO> list = Lists.newLinkedList();
        //插入主词
        EsWordExpansionDO wordExpansionDO = PojoUtils.map(request, EsWordExpansionDO.class);
        if(null==request.getId() || 0L==request.getId()){
            wordExpansionDO.setRefId(0L);
        }
        list.add(wordExpansionDO);
        switch (typeEnum){
            case EXPAND:
            case STOP:
                break;
            case ONE_WAY_SYNONYM:
                //如果主词id不为空（修改时），则将关联词关联上主词
                if(null!=request.getId() && 0L!=request.getId()){
                    List<EsWordExpansionDO> doList = queryWordList(request.getType(), null, request.getId());
                    Map<String, EsWordExpansionDO> doMap;
                    if(CollectionUtil.isNotEmpty(doList)){
                        doMap = doList.stream().collect(Collectors.toMap(EsWordExpansionDO::getWord, Function.identity()));
                    }else {
                        doMap = new HashMap<>();
                    }
                    request.getRefWordList().forEach(refWord->{
                        refWord.setRefId(request.getId());
                        refWord.setType(request.getType());
                        EsWordExpansionDO wordExpansion = doMap.get(refWord.getWord());
                        if(null!=wordExpansion && null==refWord.getId()){
                            refWord.setId(wordExpansion.getId());
                        }
                    });
                }
                list.addAll(PojoUtils.map(request.getRefWordList(),EsWordExpansionDO.class));
                break;
            case TWO_WAY_SYNONYM:
                //如果主词id不为空（修改时），则将关联词关联上主词
                if(null!=request.getId() && 0L!=request.getId()){
                    request.getRefWordList().forEach(refWord->{
                        refWord.setType(request.getType());
                        refWord.setRefId(request.getRefId());
                    });
                }
                list.addAll(PojoUtils.map(request.getRefWordList(),EsWordExpansionDO.class));
                break;
        }
        return list;
    }

    private void saveOrUpdateWordFilter(SaveOrUpdateEsWordExpansionRequest request){
        if(StringUtils.isBlank(request.getWord())){
            //词语不能为空错误
            throw new BusinessException(EsWordErrorEnum.EMPTY_WORD);
        }
        EsWordTypeEnum typeEnum = EsWordTypeEnum.getByType(request.getType());
        EsWordExpansionDO mainWord;
        List<EsWordExpansionDTO> refWordList = request.getRefWordList();
        switch (typeEnum){
            case EXPAND:
            case STOP:
                mainWord = queryWord(request.getType(), request.getWord(), 0L);
                if(null != mainWord && !mainWord.getId().equals(request.getId())){
                    //词语已存在错误
                    throw new BusinessException(EsWordErrorEnum.EXIST_WORD,request.getWord()+"-数据库已存在！！！");
                }
                break;
            case ONE_WAY_SYNONYM:
                mainWord = queryWord(request.getType(), request.getWord(), 0L);
                //主词语非自己以外已存在
                if(null != mainWord && !mainWord.getId().equals(request.getId())){
                    //词语已存在错误
                    throw new BusinessException(EsWordErrorEnum.EXIST_WORD,request.getWord()+"-主词数据库已存在！！！");
                }
                //关联词列表为空错误
                if(CollectionUtil.isEmpty(refWordList)){
                    throw new BusinessException(EsWordErrorEnum.EMPTY_WORD,"关联词列表为空！");
                }
                //获取关联词的词语并去重
                List<String> oneWaywordList = refWordList.stream().map(EsWordExpansionDTO::getWord).distinct().collect(Collectors.toList());
                //去重后列表大小小于原大小，词语重复
                if(oneWaywordList.size()<refWordList.size()){
                    throw new BusinessException(EsWordErrorEnum.REPEAT_WORD);
                }
                Boolean oneWayFilter = oneWaywordList.stream().anyMatch(word -> StringUtils.isBlank(word));
                //关联词列表存在空词错误
                if(oneWayFilter){
                    throw new BusinessException(EsWordErrorEnum.EMPTY_WORD);
                }
                //与主词重复错误
                oneWayFilter=oneWaywordList.stream().anyMatch(word->request.getWord().equals(word));
                if(oneWayFilter){
                    throw new BusinessException(EsWordErrorEnum.REPEAT_WORD,"与主词重复");
                }
                //获取数据库内关联词
                List<EsWordExpansionDO> repeatCheckList = queryWordList(request.getType(), oneWaywordList, null);
                //关联词数据库重复效验
                refWordList.forEach(refWord->{
                    //单向同义词 一个关联词不能绑定多个主词
                    if(EsWordTypeEnum.ONE_WAY_SYNONYM.getType().equals(request.getType())){
                        EsWordExpansionDO check = repeatCheckList.stream().filter(checkDO -> checkDO.getWord().equals(refWord.getWord())
                                && !checkDO.getRefId().equals(request.getId())
                                && checkDO.getRefId()!=0).findFirst().orElse(null);
                        if(null!=check){
                            //单向同义词已绑定主词
                            throw new BusinessException(EsWordErrorEnum.BIND_WORD,refWord.getWord()+"已绑定主词id:"+check.getRefId());
                        }
                    }
                });
                break;
            case TWO_WAY_SYNONYM:
                mainWord = queryWord(request.getType(), request.getWord(),null);
                if(null != mainWord && !mainWord.getId().equals(request.getId())){
                    //词语已存在错误
                    throw new BusinessException(EsWordErrorEnum.EXIST_WORD,request.getWord()+"-双向同义词数据库已存在！！！");
                }
                //关联词列表为空错误
                if(CollectionUtil.isEmpty(refWordList)){
                    throw new BusinessException(EsWordErrorEnum.EMPTY_WORD,"关联词列表为空！");
                }
                //获取关联词的词语并去重
                List<String> twoWayWordList = refWordList.stream().map(EsWordExpansionDTO::getWord).distinct().collect(Collectors.toList());
                //列表词语重复错误
                if(twoWayWordList.size()<refWordList.size()){
                    throw new BusinessException(EsWordErrorEnum.REPEAT_WORD);
                }
                Boolean twoWayFilter = twoWayWordList.stream().anyMatch(word -> StringUtils.isBlank(word));
                //关联词列表存在空词错误
                if(twoWayFilter){
                    throw new BusinessException(EsWordErrorEnum.EMPTY_WORD);
                }
                //与主词重复错误
                twoWayFilter=twoWayWordList.stream().anyMatch(word->request.getWord().equals(word));
                if(twoWayFilter){
                    throw new BusinessException(EsWordErrorEnum.REPEAT_WORD);
                }
                //获取数据库内关联词
                List<EsWordExpansionDO> twoWayDBCheckList = queryWordList(request.getType(), twoWayWordList, null);
                //关联词数据库重复效验
                refWordList.forEach(refWord->{
                    //词语在所有双向同义词内不能重复
                    if(EsWordTypeEnum.TWO_WAY_SYNONYM.getType().equals(request.getType())){
                        EsWordExpansionDO check = twoWayDBCheckList.stream().filter(checkDO ->checkDO.getWord().equals(refWord.getWord()) &&!checkDO.getId().equals(refWord.getId())).findFirst().orElse(null);
                        if(null!=check){
                            //双向同义词已存在
                            throw new BusinessException(EsWordErrorEnum.EXIST_WORD,refWord.getWord()+"-双向同义词数据库已存在！");
                        }
                    }
                });
                break;
        }
    }

    /**
     * 通过词语查询列表
     * @param type
     * @param words
     * @param refId
     * @return
     */
    private List<EsWordExpansionDO> queryWordList(Integer type,List<String> words,Long refId){
        QueryWrapper<EsWordExpansionDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsWordExpansionDO::getType,type);
        if(CollectionUtil.isNotEmpty(words)){
            queryWrapper.lambda().in(EsWordExpansionDO::getWord,words);
        }
        if(null!=refId){
            queryWrapper.lambda().eq(EsWordExpansionDO::getRefId,refId);
        }
        return this.list(queryWrapper);
    }

    private EsWordExpansionDO queryWord(Integer type,String word,Long refId){
        QueryWrapper<EsWordExpansionDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsWordExpansionDO::getType,type);
        queryWrapper.lambda().eq(EsWordExpansionDO::getWord,word);
        if(null!=refId){
            queryWrapper.lambda().eq(EsWordExpansionDO::getRefId,refId);
        }
        return this.getOne(queryWrapper);
    }


    private List<EsWordExpansionDO> queryRefWord(Integer type,Long refId){
        QueryWrapper<EsWordExpansionDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsWordExpansionDO::getType,type);
        queryWrapper.lambda().eq(EsWordExpansionDO::getRefId,refId);
        return this.list(queryWrapper);
    }
    /**
     * 通过类型和关联词id查询词语列表（refId=0 查询该类型所有主词，refid>0查询该类型和该refId下所有关联词）
     * @param type
     * @param refId
     * @return
     */
    private List<EsWordExpansionDO> queryWordByTypeAndRefId(Integer type,Long refId){
        QueryWrapper<EsWordExpansionDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsWordExpansionDO::getType,type);
        queryWrapper.lambda().eq(EsWordExpansionDO::getStatus, EsWordStatusEnum.NORMAL.getCode());
        queryWrapper.lambda().eq(EsWordExpansionDO::getRefId,refId);
        return this.list(queryWrapper);
    }

    /**
     * 查询主词idList下各个的关联词list
     * @param type
     * @param mainWordIds
     * @return key 主词id   value 关联词列表
     */
    private Map<Long, List<EsWordExpansionDO>> qureyRefWordMapByTypeAndMainIds(Integer type,List<Long> mainWordIds){
        if(CollectionUtils.isEmpty(mainWordIds)){
            return new HashMap<>();
        }
        QueryWrapper<EsWordExpansionDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EsWordExpansionDO::getType,type);
        queryWrapper.lambda().eq(EsWordExpansionDO::getStatus, EsWordStatusEnum.NORMAL.getCode());
        queryWrapper.lambda().in(EsWordExpansionDO::getRefId,mainWordIds);
        List<EsWordExpansionDO> refWordDOList = this.baseMapper.selectList(queryWrapper);
        return refWordDOList.stream().collect(Collectors.groupingBy(EsWordExpansionDO::getRefId));
    }
}
