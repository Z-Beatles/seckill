package cn.waynechu.seckill.service.impl;

import cn.waynechu.seckill.dao.SeckillDao;
import cn.waynechu.seckill.dao.SuccessKilledDao;
import cn.waynechu.seckill.dao.cache.RedisDao;
import cn.waynechu.seckill.dto.Exposer;
import cn.waynechu.seckill.dto.SeckillExecution;
import cn.waynechu.seckill.entity.Seckill;
import cn.waynechu.seckill.entity.SuccessKilled;
import cn.waynechu.seckill.enums.SeckillStatEnum;
import cn.waynechu.seckill.exception.RepeatKillException;
import cn.waynechu.seckill.exception.SeckillCloseException;
import cn.waynechu.seckill.exception.SeckillException;
import cn.waynechu.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by waynechu on 2017-07-10.
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //注入Service依赖
    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    //md5盐值字符串，用于混淆MD5
    private final String salt = "ksdfghk#$%5675e()*^%~!$#DjDF";

    public SeckillServiceImpl() {
    }

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getBySeckillId(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        // 使用edis进行缓存优化，超时的基础上维护数据一致性
        // 先尝试从redis获取
        Seckill seckill = redisDao.getSeckill(seckillId);
        if(seckill == null){
            // 从数据库获取
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                // 数据库也不存在该对象
                return new Exposer(false, seckillId);
            }else{
                // 保存数据到redis
                redisDao.putSeckill(seckill);
            }
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //当前系统时间
        Date nowTime = new Date();
        //小于开始时间或大于结束时间，getTime返回值long型
        if (nowTime.getTime() < startTime.getTime()
                || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(),
                    startTime.getTime(), endTime.getTime());
        }
        //转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    /**
     * 返回加盐后的MD5值
     *
     * @param seckillId
     * @return
     */
    private String getMD5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Transactional
    /**
     * 使用注解控制事务方法的优点:
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如只有一条修改操作、只读操作不要事务控制
     */
    @Override
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        //判断用户传入的MD5是否被篡改
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑：减库存 + 记录购买行为
        Date nowTime = new Date();

        try {
            //减库存
            int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
            if(updateCount <= 0){
                //没有更新记录到数据库，秒杀结束
                throw new SeckillCloseException("seckill is closed");
            }else{
                //像数据库中添加购买记录
                int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
                if(insertCount == 0){
                    //购买记录重复，返回值为0
                    throw new RepeatKillException("seckill is closed");
                }else{
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return  new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS,successKilled);
                }
            }
        }catch(SeckillCloseException e1){
            throw e1;
        }catch(RepeatKillException e2){
            throw e2;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            //所有编译期异常，转化为运行期异常
            throw  new SeckillException("seckill inner error:" +e.getMessage());
        }
    }
}
