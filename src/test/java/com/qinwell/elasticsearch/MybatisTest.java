package com.qinwell.elasticsearch;


import com.qinwell.elasticsearch.entity.user.AppUser;
import com.qinwell.elasticsearch.mapper.user.AppUserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@MapperScan("com.qinwell.elasticsearch.mapper")
public class MybatisTest {

    @Autowired
    private AppUserMapper appUserMapper;

    @Test
    public void TestMybatis() {
        AppUser appUser = new AppUser();
        appUser.setPassword("55");
        appUser.setUsername("stacktc");
        int result = appUserMapper.insert(appUser);
        System.out.println(result);
    }
}
