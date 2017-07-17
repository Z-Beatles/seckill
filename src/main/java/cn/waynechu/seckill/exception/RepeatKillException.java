package cn.waynechu.seckill.exception;

/**
 * 重复秒杀异常（运行期异常）
 * Created by waynechu on 2017-07-10.
 */
public class RepeatKillException extends SeckillException{

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
