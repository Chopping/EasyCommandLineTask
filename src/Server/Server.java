package Server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * CommandLine Talk 服务器端部分
 * @author Bin Xiao
 * @since 2018/06/15
 */
public class Server {
    private static final int SERVER_PORT = 12345;
    /**
     * 用来保存用户的信息(用户名以及其对用的输出流)
     */
    public static InfoMap<String,PrintStream> clients = new InfoMap<String, PrintStream>();

    /**
     * 初始化服务器端，并开始监听客户端的请求
     */
    public void init(){
        //try-with-resource added in Java7
        try(ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            // 简单的利用死循环不断地接受来自客户端的请求
            System.out.println("服务器初始化成功!正在等待连接....");
            while(true){
                Socket clientSocket = serverSocket.accept();
                new ServerThread(clientSocket).start();
            }
        }catch(IOException e){
            System.out.format("服务器启动失败，是否是端口%d被占用?",SERVER_PORT);
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        Server server = new Server();
        server.init();
    }
}
