package Appliction;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client{
    public static void main(String[] args){
        boolean loggedIn=true;
        boolean idExist=false;
        Scanner userInput=new Scanner(System.in);
        String message="";
        try{
            Socket socket=new Socket("localhost",4999);
            DataInputStream input =new DataInputStream(socket.getInputStream());
            DataOutputStream output=new DataOutputStream(socket.getOutputStream());
            System.out.println(input.readUTF());
            output.writeUTF(userInput.nextLine());
            idExist=input.readBoolean();
            System.out.println(input.readUTF());
            if(idExist){
                output.writeUTF(userInput.nextLine());
                System.out.println(input.readUTF());
                loggedIn=input.readBoolean();
                if(loggedIn) {
                    System.out.println(input.readUTF());
                    while (message.compareToIgnoreCase("exit")!=0){
                        message= userInput.nextLine();
                        output.writeUTF(message);
                        if(message.compareToIgnoreCase("exit")!=0)
                            System.out.println(input.readUTF());
                    }
                    output.writeUTF(message);
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
