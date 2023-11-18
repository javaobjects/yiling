package com.yiling.search.word;

import java.util.HashMap;
import java.util.List;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.collect.Lists;
import com.yiling.search.BaseTest;
import com.yiling.search.word.api.EsWordExpansionApi;
import com.yiling.search.word.bo.EsWordExpansionBO;
import com.yiling.search.word.dto.EsWordExpansionDTO;
import com.yiling.search.word.dto.request.SaveOrUpdateEsWordExpansionRequest;
import com.yiling.search.word.enums.EsWordStatusEnum;
import com.yiling.search.word.enums.EsWordTypeEnum;
import com.yiling.search.word.service.EsWordExpansionService;

/**
 * @author shichen
 * @类名 EsWordTest
 * @描述
 * @创建时间 2022/4/20
 * @修改人 shichen
 * @修改时间 2022/4/20
 **/
public class EsWordTest extends BaseTest {

    @Autowired
    private EsWordExpansionService esWordExpansionService;

    @Autowired
    private EsWordExpansionApi esWordExpansionApi;

    @Value("${elasticsearch.goodsEsIndex}")
    private String goodsEsIndex;
    @Test
    public void saveWordTest(){
        /**
         * 单向同义词保存
         */
        SaveOrUpdateEsWordExpansionRequest request = new SaveOrUpdateEsWordExpansionRequest();
        request.setId(33L);
        request.setWord("单向同义词C");
        request.setRefId(0L);
        request.setStatus(EsWordStatusEnum.NORMAL.getCode());
        request.setType(EsWordTypeEnum.ONE_WAY_SYNONYM.getType());
        EsWordExpansionDTO dto = new EsWordExpansionDTO();
        dto.setId(34L);
        dto.setWord("单向同义词C1");
        dto.setStatus(EsWordStatusEnum.NORMAL.getCode());
        dto.setType(EsWordTypeEnum.ONE_WAY_SYNONYM.getType());
        dto.setRefId(33L);
        EsWordExpansionDTO dtoo = new EsWordExpansionDTO();
        dtoo.setWord("单向同义词C2");
        //dtoo.setId(35L);
        dtoo.setStatus(EsWordStatusEnum.NORMAL.getCode());
        dtoo.setType(EsWordTypeEnum.ONE_WAY_SYNONYM.getType());
        dtoo.setRefId(33L);
        List<EsWordExpansionDTO> list = Lists.newArrayList(dto,dtoo);
        request.setRefWordList(list);
        esWordExpansionService.saveOrUpdateWord(request);

        /**
         * 双向同义词保存
         */
        SaveOrUpdateEsWordExpansionRequest request1 = new SaveOrUpdateEsWordExpansionRequest();
        request1.setId(21L);
        request1.setWord("双向同义词A");
        request1.setStatus(EsWordStatusEnum.NORMAL.getCode());
        request1.setType(EsWordTypeEnum.TWO_WAY_SYNONYM.getType());
        request1.setRefId(21L);
        EsWordExpansionDTO dto1 = new EsWordExpansionDTO();
        dto1.setId(22L);
        dto1.setWord("双向同义词B");
        dto1.setStatus(EsWordStatusEnum.NORMAL.getCode());
        dto1.setRefId(21L);
        EsWordExpansionDTO dto11 = new EsWordExpansionDTO();
        dto11.setId(31L);
        dto11.setWord("双向同义词B1");
        dto11.setStatus(EsWordStatusEnum.NORMAL.getCode());
        dto11.setRefId(21L);
        List<EsWordExpansionDTO> list1 = Lists.newArrayList(dto1,dto11);
        request1.setRefWordList(list1);
        //esWordExpansionService.saveOrUpdateWord(request1);

        /**
         * 扩展词保存
         */
        SaveOrUpdateEsWordExpansionRequest request2 = new SaveOrUpdateEsWordExpansionRequest();
        request2.setWord("异烟");
        request2.setStatus(EsWordStatusEnum.NORMAL.getCode());
        request2.setType(EsWordTypeEnum.EXPAND.getType());
        //esWordExpansionService.saveOrUpdateWord(request2);
    }


    @Test
    public void uploadWordTest(){
        HashMap<EsWordTypeEnum, List<EsWordExpansionBO>> map=new HashMap<>();
        List<EsWordExpansionBO> list1 = esWordExpansionService.queryWordBOByType(EsWordTypeEnum.ONE_WAY_SYNONYM.getType());
        List<EsWordExpansionBO> list2 = esWordExpansionService.queryWordBOByType(EsWordTypeEnum.TWO_WAY_SYNONYM.getType());
        map.put(EsWordTypeEnum.ONE_WAY_SYNONYM,list1);
        map.put(EsWordTypeEnum.TWO_WAY_SYNONYM,list2);
        //本地单元测试需要把AliyunOSSConfig.java里的@Value("${aliyun.oss.endpoint:xxxxx}")改为@Value("${aliyun.oss.domain:xxxxx}")
        esWordExpansionService.uploadEsWord(map,EsWordTypeEnum.ONE_WAY_SYNONYM.getFileName(),EsWordTypeEnum.ONE_WAY_SYNONYM.getFilePath());
    }

    @Test
    public void refreshWordTest(){
        esWordExpansionService.updateEsWordDic(EsWordTypeEnum.ONE_WAY_SYNONYM,goodsEsIndex);
    }

    @Test
    public void syncWordTest()throws Exception{
        esWordExpansionApi.addSyncJob(EsWordTypeEnum.EXPAND.getType());
        esWordExpansionApi.syncGoodsWordByType(EsWordTypeEnum.EXPAND);
    }

    @Test
    public void getUrlTest(){
        String url = esWordExpansionService.getDownloadUrl(1);
        System.out.println(url);
    }
}
