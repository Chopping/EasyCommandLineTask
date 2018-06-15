package Util;

/**
 * CommandLine Talk 聊天内容约定的关键字
 * @author Bin Xiao
 * @since 2018/06/15
 */
public final class TalkProtocol {
    /**
     * 约定的协议字符串长度
     */
    public static final int PROTOCOL_LEN=3;
    /**
     * 标识传输内容为消息
     */
    public static final String MSG_ROUND ="$M:";
    /**
     * 标识传输内容为用户名
     */
    public static final String USER_ROUND ="$U:";
    /**
     * 标识用户登入成功
     */
    public static final String LOGIN_SUCCESS ="&OK";
    /**
     * 表示用户名重复
     */
    public static final String NAME_REP ="&SA";
    /**
     * 表示传输内容为消息且消息为私聊消息
     */
    public static final String PRIVATE_ROUND ="$P:";
    /**
     * 用以分割消息体不同区域的内容的分隔符
     */
    public static final String SPILIT_SIGN =" % ";
    /**
     *
     */
    public static final String PRIVATE_SIGN = "//:";

}
