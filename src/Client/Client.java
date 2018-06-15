package Client;

import Util.TalkProtocol;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;

/**
 * CommandLine Talk 客户端部分
 * @author Bin Xiao
 * @since 2018/06/15
 */
public class Client {
    /**
     * 特定的SERVER_PORT
     */
    private static final int SERVER_PORT = 12345;
    private Socket socket;
    private PrintStream ps;
    private BufferedReader brServer;
    /**
     * 用来从命令行中读取用户的输入
     */
    private BufferedReader keyIn;

    /**
     * 初始化客户端，建立与服务器端的链接
     */
    public void init() {
        try {
            // 初始化代表键盘的输入流
            keyIn = new BufferedReader(
                    new InputStreamReader(System.in));
            // 连接到服务器端
            socket = new Socket("127.0.0.1", SERVER_PORT);
            // 获取该Socket对应的输入流与输出流
            ps = new PrintStream(socket.getOutputStream());
            brServer = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            String tip = "准备登入";
            String userName;
            // 采用死循环提示用户输入用户名
            while (true) {
                userName = JOptionPane.showInputDialog(String.format("%s ,输入用户名", tip));
                // 输入登入的用户名
                ps.println(String.format("%s%s%s",
                        TalkProtocol.USER_ROUND,
                        userName,
                        TalkProtocol.USER_ROUND));
                String result = brServer.readLine();
                if (TalkProtocol.NAME_REP.equals(result)) {
                    tip = "用户名重复";
                    continue;
                }
                // 成功登入跳出循环
                else if(TalkProtocol.LOGIN_SUCCESS.equals(result)){
                    break;
                }
            }
            System.out.println("欢迎进入聊天室，聊天规则：1. 正常输入默认向广场内所有人发消息 2. //userName:发言内容 指向特定用户发送悄悄话");
            System.out.println(String.format("您的用户名是:%s ",userName==null?"无":userName));
        }
        catch (UnknownHostException e) {
            System.out.println("无法找到远程服务器，请确认服务器已经启动");
            closeRs();
            System.exit(1);
        }
        catch (IOException e) {
            System.out.println("网络异常!请重新登录!");
            closeRs();
            System.exit(1);
        }
        // 以该Socket对应的输入流启动ClientThread线程
        new ClientThread(brServer).start();
    }
    /**
     * 保持持续的从控制台获取输入值，并且接受服务器端的反馈
     */
    private void readAndSend(){
        try {
            String line = null;
            while((line = keyIn.readLine())!=null){
                // 发送消息中有冒号，且以 // 开头，则认为是想发送私聊消息
                if(line.indexOf(":")>0 && line.startsWith("//")){
                    line = line.substring(2);
                    ps.println(String.format("%s%s%s%s%s",
                            TalkProtocol.PRIVATE_ROUND,
                            line.split(":")[0],
                            TalkProtocol.SPILIT_SIGN,
                            line.split(":")[1],
                            TalkProtocol.PRIVATE_ROUND
                    ));
                }else{
                    ps.println(String.format("%s%s%s",TalkProtocol.SPILIT_SIGN,line,TalkProtocol.SPILIT_SIGN));
                }
            }
        }catch (IOException e){
            System.out.println("网络通信异常，请重新登录!");
            closeRs();
            System.exit(1);
        }
    }
    /**
     * 关闭所有的输入输出流
     */
    private void closeRs() {
        try{
            if(keyIn!=null){
                keyIn.close();
            }
            if(brServer!=null){
                brServer.close();
            }
            if(ps!=null){
                ps.close();
            }
            if(socket!=null){
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        Client client =new Client();
        client.init();
        client.readAndSend();
    }
}
