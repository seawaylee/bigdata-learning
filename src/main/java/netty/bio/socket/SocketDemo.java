package netty.bio.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 传统Socket编程
 *
 * @author NikoBelic
 * @create 2018/2/9 12:28
 */
public class SocketDemo {

    static class Server {
        private int port;
        private String ip;

        public Server(int port, String ip) {
            this.port = port;
            this.ip = ip;
        }

        public void run() throws IOException {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String request, response;
            while ((request = in.readLine()) != null) {
                if ("Done".equalsIgnoreCase(request)) {
                    break;
                }
                response = processRequest(request);
                out.println(response);
            }
        }

        private String processRequest(String req) {
            return "Handled " + req;
        }
    }

}


