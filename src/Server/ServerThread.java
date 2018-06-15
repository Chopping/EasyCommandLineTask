package Server;

import Util.TalkProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * CommandLine Talk 服务器端针对每个客户端的请求所建立的线程对象
 * @author Bin Xiao
 * @since 2018/06/15
 */
public class ServerThread extends Thread {
    /**
     * 该线程连接时建立的对应的Socket
     */
    private Socket socket;
    /**
     * 用于接收客户端发来的消息的Reader
     */
    BufferedReader br = null;
    /**
     * 用来向客户端输出相应的内容
     */
    PrintStream ps = null;

    // 定义构造器，以构造注入的方式生成Socket
    public ServerThread(Socket clientSocket) {
        this.socket = clientSocket;
    }
    @Override
    public void run() {
        try {
            // 获取该Socket对象的输入、输出流
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ps = new PrintStream(socket.getOutputStream());
            // 用作存储从输出流读入的内容
            String line = null;
            // 读入的内容不为null
            while ((line = br.readLine()) != null) {
                // 如果读到的内容是以TalkProtocol.USER_ROUND开头并以其结尾的，则可以认为读到的是Username
                if (line.startsWith(TalkProtocol.USER_ROUND)
                        && line.endsWith(TalkProtocol.USER_ROUND)) {
                    // 获取去除无效的数据部分的用户名真实值
                    String userName = getRealMsg(line);
                    // 服务器已经存在该用户了，则提示用户名重复
                    if (Server.clients.containKey(userName)) {
                        System.out.println(String.format("[系统] 用户名: %s 重复!",userName));
                        ps.println(TalkProtocol.NAME_REP);
                    } else {
                        ps.println(TalkProtocol.LOGIN_SUCCESS);
                        Server.clients.put(userName, ps);
                        System.out.println(String.format("[系统] 用户[ %s ] 连接成功!当前用户数量为:%d",userName,Server.clients.size()));
                    }
                }
                // 如果读到的内容是以PRIVATE_ROUND开头并以之结尾的，则可以认为读到的是私聊的部分，私聊部分只向特定的输出流发出
                else if (line.startsWith(TalkProtocol.PRIVATE_ROUND)
                        && line.endsWith(TalkProtocol.PRIVATE_ROUND)) {
                    // 获取真实的私聊内容
                    String userAndMsg = getRealMsg(line);
                    // 截取用户名称
                    String toUser = userAndMsg.split(TalkProtocol.SPILIT_SIGN)[0];
                    // 截取用户输入内容的消息部分
                    String msg = userAndMsg.split(TalkProtocol.SPILIT_SIGN)[1];
                    System.out.println(String.format("[系统] 用户[ %s ] 对 用户[ %s ]发送了私信: %s",Server.clients.getKeyByValue(ps),toUser,msg));
                    // 获取私聊用户对应的输出流，并发送私聊信息
                    Server.clients.get(toUser) // 获取 toUser 用户对应输入流
                            .println(String.format("[私聊] %s 悄悄地对你说：%s"
                                    , Server.clients.getKeyByValue(ps)// 获取发送私聊消息的用户名称
                                    , msg));
                } else {
                    // 不是私聊也不是登入信息，那么就作为群聊的消息发送给所有的用户
                    // 获取真实的消息内容
                    String msg = getRealMsg(line);
                    System.out.println(String.format("[广场] 用户[ %s ]说:%s", Server.clients.getKeyByValue(ps), msg));
                    for (PrintStream clientPs : Server.clients.valueSet()) {
                        clientPs.println(String.format("[广场] 用户[ %s ]:%s", Server.clients.getKeyByValue(ps), msg));
                    }
                }
            }
        }
        // 捕获到此异常说明 Socket 对应的客户端出现了问题
        // 需要在处理异常的部分将其对应的输出流从 Map 中删除
        catch (IOException e) {
            System.out.println(String.format("[系统] 用户[ %s ] 断开了连接! ", Server.clients.getKeyByValue(ps)));
            Server.clients.removeByValue(ps);
            System.out.println(String.format("[系统] 当前剩余用户: %d 个用户", Server.clients.size()));
            try {
                if (br != null) {
                    br.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    /**
     * 将获取的消息处理成可用的内容
     * @param line
     * @return
     */
    private String getRealMsg(String line) {
        return line.substring(TalkProtocol.PROTOCOL_LEN,line.length()-TalkProtocol.PROTOCOL_LEN);
    }
    @Override
    public synchronized void start() {
        super.start();
    }
}
