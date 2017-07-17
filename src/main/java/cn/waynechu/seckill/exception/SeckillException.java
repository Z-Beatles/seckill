package cn.waynechu.seckill.exception;

/**
 * 秒杀相关的业务异常
 * Created by waynechu on 2017-07-10.
 */
public class SeckillException extends RuntimeException{
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
