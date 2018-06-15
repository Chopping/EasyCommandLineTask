package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;

/**
 * CommandLine Talk 客户端发起请求时采用的线程对象
 * @author Bin Xiao
 * @since 2018/06/15
 */
public class ClientThread extends Thread{
    // 该客户端线程负责处理的输入流
    BufferedReader br = null;
    public ClientThread(BufferedReader brServer) {
        this.br = brServer;
    }
    @Override
    public void run() {
        try {
            String line = null;
            // 线程不断地从输入流中读取数据，并将这些数据打印输出
            while((line = br.readLine())!=null){
                System.out.println(line);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        finally {
            try {
                if(br!=null){
                    br.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
