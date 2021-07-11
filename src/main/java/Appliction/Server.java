package Appliction;

import Appliction.Cache.Cache;
import BLL.EmployeeServices;
import Shared.Helpers.PrintResult;
import Shared.Helpers.QueryValidator;
import Shared.Helpers.RoleValidator;
import Shared.Models.Employee;
import Shared.Models.QueryTypes;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket socket = null;
        Cache<String, TreeMap<Integer,Employee>> applicationCache=new Cache<>(8);
        try{
            //connection
            int i = 1;
             serverSocket=new ServerSocket(4999);
            while(true){
                socket=serverSocket.accept();
                ServerThread st = new ServerThread(socket,applicationCache,"Thread#"+i++);
                Thread temp = new Thread(st);
                temp.start();
                System.out.println("connected");
            }

        }catch (Exception e){
            System.out.println(e);
        }finally {
                try {
                    EmployeeServices.exit();
                    if(socket!=null && !socket.isClosed())
                    socket.close();
                    if(serverSocket!=null && !serverSocket.isClosed())
                        serverSocket.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


