package cn.waynechu.seckill.exception;

/**
 * 秒杀关闭异常
 * Created by waynechu on 2017-07-10.
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
