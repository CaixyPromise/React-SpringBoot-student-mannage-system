package com.caixy.adminSystem;

import com.caixy.adminSystem.config.WxOpenConfig;

import javax.annotation.Resource;

import com.caixy.adminSystem.utils.EncryptionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * 主类测试
 */
@SpringBootTest
class MainApplicationTests
{
    @Resource
    private EncryptionUtils encryptionUtils;
    @Resource
    private WxOpenConfig wxOpenConfig;

    @Test
    void contextLoads()
    {
        System.out.println(wxOpenConfig);
    }

    @Test
    public void test()
    {
        List<Long> longList = Arrays.asList(21215620243L,
                21215620246L,
                21215620245L,
                21215620247L,
                21215620249L,
                21215620250L,
                21215620252L,
                21215620255L,
                21215620277L,
                21215620278L,
                21215620296L
                );
        longList.forEach(item -> {
            String password = encryptionUtils.encodePassword(String.valueOf(item));
            System.out.println(password);
        });
    }

}
