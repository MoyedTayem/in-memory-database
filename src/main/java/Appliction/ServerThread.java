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
import java.net.Socket;
import java.util.TreeMap;

public class ServerThread implements Runnable{

    private Socket socket;
    Cache<String, TreeMap<Integer,Employee>> applicationCache;
    private String threadName;

    public ServerThread(Socket socket,
                        Cache<String, TreeMap<Integer,Employee>> applicationCache,
                        String threadName){
        this.socket=socket;
        this.applicationCache=applicationCache;
        this.threadName = threadName;
    }
    @Override
    public void run() {
        //input output declaration
        String message="";
        EmployeeServices.createDatabase();
        boolean done=false;
        try{
        DataInputStream input =new DataInputStream(socket.getInputStream());
        DataOutputStream output=new DataOutputStream(socket.getOutputStream());

        //login messages
        output.writeUTF(this.threadName + " - Enter your ID:");
        String loginMessage=input.readUTF();
        if(EmployeeServices.doseExist(loginMessage)){
            Employee currentEmployee=EmployeeServices.getEmployeeById(Integer.parseInt(loginMessage));
            output.writeBoolean(true);
            output.writeUTF(this.threadName + " - Enter Password:");
            if(EmployeeServices.isPasswordCorrect(currentEmployee,input.readUTF())){
                output.writeUTF(this.threadName + " - logged in successfully");
                output.writeBoolean(true);
                output.writeUTF(this.threadName +" - "+ EmployeeServices.printInstructions());
                // start of application quires
                while (!done){
                    message = input.readUTF();
                    if(!message.equalsIgnoreCase("Exit")){
                        if(QueryValidator.getQueryType(message)== QueryTypes.INVALID){
                            output.writeUTF(this.threadName + " - Invalid query");
                        }
                        else{
                            switch (QueryValidator.getQueryType(message)){
                                case CREATE :{
                                    if(RoleValidator.isAuthorized(QueryTypes.CREATE,EmployeeServices.getRole(currentEmployee))){
                                        if(QueryValidator.isValidCreateQuery(message)){
                                            if(!EmployeeServices.doseExist(message)){
                                                EmployeeServices.addNewEmployee(message);
                                                output.writeUTF(this.threadName + " - new Employee added");
                                                applicationCache.clear();
                                            }
                                            else
                                                output.writeUTF(this.threadName + " - Employee ID already exit");
                                        }
                                        else{
                                            output.writeUTF(this.threadName + " - Not valid Create query");
                                        }
                                    }else{
                                        output.writeUTF(this.threadName + " - Not Authorized");
                                    }
                                }break;
                                case SELECT:{
                                    if(QueryValidator.isValidSelectQuery(message))
                                        if(applicationCache.get(message)!=null){
                                            output.writeUTF(this.threadName + " -\n"+PrintResult.printResult(applicationCache.get(message),message));
                                        }else{
                                            applicationCache.put(message,EmployeeServices.getCompanyEmployees(message));
                                            output.writeUTF(this.threadName + " - \n"+PrintResult.printResult(EmployeeServices.getCompanyEmployees(message),message));
                                        }
                                    else {
                                        output.writeUTF(this.threadName + " - "+"Not valid select query");
                                    }
                                }break;
                                case UPDATE:{
                                    if(RoleValidator.isAuthorized(QueryTypes.UPDATE,EmployeeServices.getRole(currentEmployee))){
                                        if(QueryValidator.isValidUpdateQuery(message)){

                                            if(message.toLowerCase().contains("where") &&message.toLowerCase().contains("id")){
                                                if(EmployeeServices.doseExist(message)){
                                                    EmployeeServices.updateEmployee(message);
                                                    output.writeUTF(this.threadName + " - "+"Employee updated");
                                                    applicationCache.clear();

                                                }
                                                else {
                                                    output.writeUTF(this.threadName + " - "+"Employee ID couldn't be found");
                                                }
                                            }else {
                                                EmployeeServices.updateEmployee(message);
                                                output.writeUTF(this.threadName + " - "+"Employee updated");
                                                applicationCache.clear();
                                            }
                                        }
                                        else{
                                            output.writeUTF(this.threadName + " - "+"Not valid update query");
                                        }
                                    }
                                    else{
                                        output.writeUTF(this.threadName + " - "+"Not Authorized");
                                    }
                                }break;
                                case DELETE:{
                                    if(RoleValidator.isAuthorized(QueryTypes.DELETE,EmployeeServices.getRole(currentEmployee))){
                                        if(QueryValidator.isValidDeleteQuery(message)){
                                            if(message.toLowerCase().contains("where") &&message.toLowerCase().contains("id")){
                                                if(EmployeeServices.doseExist(message)){
                                                    EmployeeServices.deleteEmployee(message);
                                                    output.writeUTF(this.threadName + " - "+"Employee deleted");
                                                    applicationCache.clear();
                                                }
                                                else {
                                                    output.writeUTF(this.threadName + " - "+"Employee ID couldn't be found");
                                                }
                                            }else {
                                                EmployeeServices.deleteEmployee(message);
                                                output.writeUTF(this.threadName + " - "+"Employee deleted");
                                                applicationCache.clear();
                                            }
                                        }else{
                                            output.writeUTF(this.threadName + " - "+"Not valid delete query");
                                        }
                                    }else{
                                        output.writeUTF(this.threadName + " - "+"Not Authorized");
                                    }
                                }break;
                            }
                        }

                    }else if(message.equalsIgnoreCase("Exit")) {
                        EmployeeServices.exit();
                        done=true;
                    }
                }
            }
            else{
                output.writeUTF(this.threadName + " - "+"wrong password");
                output.writeBoolean(false);
            }
        }
        else{
            output.writeBoolean(false);
            output.writeUTF(this.threadName + " - "+"ID doesn't exist");
        }
    }catch (IOException e){
            EmployeeServices.exit();
        }
    }
}

