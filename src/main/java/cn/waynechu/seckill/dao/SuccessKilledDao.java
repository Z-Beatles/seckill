package cn.waynechu.seckill.dao;

import cn.waynechu.seckill.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * Created by waynechu on 2017-07-08.
 */
public interface SuccessKilledDao {
    /**
     * 插入购买明细，可过滤重复
     *
     * @param seckillId
     * @param userPhone
     * @return 插入的行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据id查询SuccessKilled并携带秒杀产品对象实体
     *
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);

}
