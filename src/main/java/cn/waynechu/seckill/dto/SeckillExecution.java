package cn.waynechu.seckill.dto;

import cn.waynechu.seckill.entity.SuccessKilled;
import cn.waynechu.seckill.enums.SeckillStatEnum;

/**
 * 封装秒杀执行后的结果
 * Created by waynechu on 2017-07-10.
 */
public class SeckillExecution {

    private long seckillId;

    //执行秒杀结果状态
    private long state;

    //状态描述
    private String stateInfo;

    //秒杀成功对象
    private SuccessKilled successKilled;

    //秒杀成功返回所有信息
    public SeckillExecution(long seckillId, SeckillStatEnum statEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    //秒杀失败
    public SeckillExecution(long seckillId, SeckillStatEnum statEnum) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getState() {
        return state;
    }

    public void setState(long state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "seckillId=" + seckillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", successKilled=" + successKilled +
                '}';
    }
}
