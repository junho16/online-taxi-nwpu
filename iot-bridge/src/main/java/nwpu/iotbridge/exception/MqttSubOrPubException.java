package nwpu.iotbridge.exception;

/**
 * IOT客户端订阅或发布消息过程 异常
 * @author Junho
 * @date 2022/7/17 17:22
 */
public class MqttSubOrPubException extends Exception {

    private static final long serialVersionUID = 1L;

    //无参构造函数
    public MqttSubOrPubException() {
        super();
    }

    //用详细信息指定一个异常
    public MqttSubOrPubException(String message) {
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public MqttSubOrPubException(String message, Throwable cause) {
        super(message, cause);
    }

    //用指定原因构造一个新的异常
    public MqttSubOrPubException(Throwable cause) {
        super(cause);
    }
}
