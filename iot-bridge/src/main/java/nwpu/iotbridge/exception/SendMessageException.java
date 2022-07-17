package nwpu.iotbridge.exception;

/**
 * 消息队列异常
 * @author Junho
 * @date 2022/7/17 16:35
 */
public class SendMessageException extends Exception{
    private static final long serialVersionUID = 1L;

    //无参构造函数
    public SendMessageException() {
        super();
    }

    //用详细信息指定一个异常
    public SendMessageException(String message) {
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public SendMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    //用指定原因构造一个新的异常
    public SendMessageException(Throwable cause) {
        super(cause);
    }
}
