package cn.waynechu.seckill.enums;

/**
 * 秒杀状态的枚举类型
 * Created by waynechu on 2017-07-10.
 */
public enum SeckillStatEnum {
    SUCCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复秒杀"),
    INNER_ERROR(-2,"系统异常"),
    DATA_REWRITE(-3,"数据篡改");

    private int state;

    private String stateInfo;

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    SeckillStatEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static SeckillStatEnum stateOf(int index){
        for(SeckillStatEnum state : values()){
            if (state.getState() == index){
                return state;
            }
        }
        return null;
    }
}
