package cn.waynechu.seckill.dto;

/**
 * 所有的ajax请求返回类型，封装json结果
 * Created by waynechu on 2017-07-15.
 */
public class SeckillResult<T> {

    /** 表示请求是否成功，包括秒杀成功、失败、重复等状态 */
    private boolean success;

    private T data;

    private String error;

    public boolean isSuccess() {
        return success;
    }

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "SeckillResult{" +
                "success=" + success +
                ", data=" + data +
                ", error='" + error + '\'' +
                '}';
    }
}
