package cn.waynechu.seckill.dao;

import cn.waynechu.seckill.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 *配置spring和junit整合，junit启动时加载springIOC容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junitspring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        /**
         * 第一次返回1，表示插入成功
         * 第二次返回0，表示插入失败，此时主键冲突
         */
        long id = 1001L;
        long phone = 15500000000L;
        int insertCount = successKilledDao.insertSuccessKilled(id,phone);
        System.out.println(insertCount);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        long id = 1001L;
        long phone = 15500000000L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id,phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }

}