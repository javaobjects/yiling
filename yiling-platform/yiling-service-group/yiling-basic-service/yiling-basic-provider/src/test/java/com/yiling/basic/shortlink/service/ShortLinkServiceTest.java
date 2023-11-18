package com.yiling.basic.shortlink.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.BaseTest;

import lombok.extern.slf4j.Slf4j;

/**
 * 短链接 Test
 * @author: lun.yu
 * @date: 2022/01/13
 */
@Slf4j
public class ShortLinkServiceTest extends BaseTest {

    @Autowired
    ShortLinkService shortLinkService;

    @Test
    public void generatorShortLink() {
        String shortUrl = shortLinkService.generatorShortLink("https://pop.59yi.com/invite?parentId=2&inviteType=1");
        System.out.println(shortUrl);
    }

    @Test
    public void getByKey() {
        String key = "feuuEr";
        String shortUrl = shortLinkService.getByKey(key);
        System.out.println(shortUrl);
    }
}
