package BLL;

import DAL.Database;
import DAL.JSONFile;
import Shared.Helpers.QueryValidator;
import Shared.Models.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EmployeeServices {


   public static void createDatabase(){
      Database.getDatabase();
   }

   public static TreeMap<Integer,Employee> getCompanyEmployees(String query) {
      query = QueryValidator.cleanSpaces(query);
      TreeMap<Integer, Employee> resultTree = new TreeMap<>();
      String[] tokens = query.split(" ");

      if (tokens.length ==4) {
            for (Map.Entry<Integer, Employee> entry : Database.getAllEmployees().entrySet()) {
               Employee value = entry.getValue();
               resultTree.put(value.getId(),value);

            }
         }
      else{
         for (Map.Entry<Integer, Employee> entry : Database.getAllEmployees().entrySet()) {
            Employee value = entry.getValue();
            Integer key = entry.getKey();
            if (evalCondition(tokens[5], value)) {
               resultTree.put(value.getId(), value);

            }

         }
      }
      return resultTree;
}

   public static Employee getEmployeeById(int id){

     return Database.getEmployeeById(id);
   }

   public static void addNewEmployee(String query){

      query=QueryValidator.cleanSpaces(query);
      String[] tokens=query.split(" ");
      String[] args=tokens[3].split(",");
         int id=0;
         String name="";
         String department="";
         String password="";
         double salary=0;
         for (String arg:args) {
            switch (arg.substring(0,arg.indexOf("=")).toLowerCase()){
               case "id":
                  id=Integer.parseInt(arg.substring(arg.indexOf("=")+1));break;
               case "name":
                  name=arg.substring(arg.indexOf("=")+1);break;
               case "salary":
                  salary=Double.parseDouble(arg.substring(arg.indexOf("=")+1));break;
               case "department":
                  department=arg.substring(arg.indexOf("=")+1);break;
               case "password":
                  password=arg.substring(arg.indexOf("=")+1);break;
               default:break;

            }
         }
         if(tokens[4].equalsIgnoreCase("Supervisor")){
            Database.addEmployee(new Supervisor(id,name,salary,department,password));
         }
         else {
            Database.addEmployee(new Subordinate(id,name,salary,department,password));
         }
      }

   public static void deleteEmployee(String query){
      query=QueryValidator.cleanSpaces(query);
      String[] tokens=query.split(" ");
      TreeMap<Integer,Employee> temp=new TreeMap<>();

      if(tokens.length>4){
         while (Database.isDeleteLocked());
         Database.lockDelete();
         Database.getAllEmployees().keySet().removeIf(key ->
                 evalCondition(tokens[4], Database.getEmployeeById(key)));
         Database.unlockDelete();
      }
      else{
         Database.deleteAllEmployees();

      }

   }

   public static void updateEmployee(String query){
      query=QueryValidator.cleanSpaces(query);
      String[] tokens=query.split(" ");
      TreeMap<Integer,Employee> companyEmployees= Database.getAllEmployees();
      if(tokens.length>4){
         for (Map.Entry<Integer,Employee> entry : companyEmployees.entrySet()) {
            Employee value = entry.getValue();
            int key = entry.getKey();
            if (evalCondition(tokens[5], value)) {
               Database.updateEmployee(key,evalSet(tokens[3], value));
            }
         }
      }
      else{
         for (Map.Entry<Integer,Employee> entry : companyEmployees.entrySet()) {
            Employee value = entry.getValue();
            int key = entry.getKey();
            Database.updateEmployee(key,evalSet(tokens[3], value));
         }
      }
   }

   public static EmployeeEnum getRole(Employee employee){

      if(employee instanceof Supervisor)
         return EmployeeEnum.SUPERVISOR;

      return EmployeeEnum.SUBORDINATE;

   }

   public static boolean evalCondition(String condition,Employee employee){
      String[] operators=new String[]{"=","!=",">","<",">=","<="};
      String operator="";
      String[] tokens=new String[2];
      for (String op:operators){
         if(condition.split(op).length==2){
            if(op.equalsIgnoreCase(">")||
                    op.equalsIgnoreCase("<")||
                    op.equalsIgnoreCase("!")){
               if(condition.contains("="))
                  continue;
               else {
                  tokens=condition.split(op);
                  operator=op;
               }
            }
            else {
            tokens=condition.split(op);
            operator=op;
            }
         }
         switch (operator){
            case "=":
               switch (tokens[0].toLowerCase()){
                  case "id":if(employee.getId()==Integer.parseInt(tokens[1]))
                     return true;break;
                  case "name":if (employee.getName().equalsIgnoreCase(tokens[1]))
                     return true;break;
                  case "salary":if(employee.getSalary()==Double.parseDouble(tokens[1]))
                     return true;break;
                  case "department":if (employee.getDepartment().equalsIgnoreCase(tokens[1]))
                     return true;break;
               }break;
            case "!=":
               switch (tokens[0].toLowerCase()) {
                  case "id":
                     if (employee.getId() != Integer.parseInt(tokens[1]))
                        return true;break;
                  case "name":
                     if (!employee.getName().equalsIgnoreCase(tokens[1]))
                        return true;break;
                  case "salary":
                     if (employee.getSalary() != Double.parseDouble(tokens[1]))
                        return true;break;
                  case "department":
                     if (!employee.getDepartment().equalsIgnoreCase(tokens[1]))
                        return true;break;
               }break;
            case ">":
               switch (tokens[0].toLowerCase()) {
                  case "id":
                     if (employee.getId() > Integer.parseInt(tokens[1]))
                        return true;break;
                  case "name":
                     if (employee.getName().compareToIgnoreCase(tokens[1])>0)
                        return true;break;
                  case "salary":
                     if (employee.getSalary() > Double.parseDouble(tokens[1]))
                        return true;break;
                  case "department":
                     if (employee.getDepartment().compareToIgnoreCase(tokens[1])>0)
                        return true;break;
               }break;
            case ">=":
               switch (tokens[0].toLowerCase()) {
                  case "id":
                     if (employee.getId() >= Integer.parseInt(tokens[1]))
                        return true;break;
                  case "name":
                     if (employee.getName().compareToIgnoreCase(tokens[1])>=0)
                        return true;break;
                  case "salary":
                     if (employee.getSalary() >= Double.parseDouble(tokens[1]))
                        return true;break;
                  case "department":
                     if (employee.getDepartment().compareToIgnoreCase(tokens[1])>=0)
                        return true;break;
               }break;

            case "<":
               switch (tokens[0].toLowerCase()) {
                  case "id":
                     if (employee.getId() < Integer.parseInt(tokens[1]))
                        return true;break;
                  case "name":
                     if (employee.getName().compareToIgnoreCase(tokens[1])<0)
                        return true;break;
                  case "salary":
                     if (employee.getSalary() < Double.parseDouble(tokens[1]))
                        return true;break;
                  case "department":
                     if (employee.getDepartment().compareToIgnoreCase(tokens[1]) < 0)
                        return true;break;
               }break;

            case "<=":
               switch (tokens[0].toLowerCase()) {
                  case "id":
                     if (employee.getId() <= Integer.parseInt(tokens[1]))
                        return true;break;
                  case "name":
                     if (employee.getName().compareToIgnoreCase(tokens[1]) <= 0)
                        return true;break;
                  case "salary":
                     if (employee.getSalary() <= Double.parseDouble(tokens[1]))
                        return true;break;
                  case "department":
                     if (employee.getDepartment().compareToIgnoreCase(tokens[1]) <= 0)
                        return true;break;
               }break;
         }
      }
      return false;
   }

   public static Employee evalSet(String set,Employee employee){
      String[] setFields=set.split(",");
      Employee temp=employee;
      for (String setField:setFields) {
         switch (setField.substring(0,setField.indexOf("=")).toLowerCase()){
            case "name":temp.setName(setField.substring(setField.indexOf("=")+1));
            break;
            case "salary":temp.setSalary(Double.parseDouble(setField.substring(setField.indexOf("=")+1)));
            break;
            case "department":temp.setDepartment(setField.substring(setField.indexOf("=")+1));
            break;
            case "password":temp.setPassword(setField.substring(setField.indexOf("=")+1));
               break;
         }
      }
      return temp;
   }

   public static boolean doseExist(String query) {
      int id=-1;
      query=QueryValidator.cleanSpaces(query);
      QueryTypes queryType=QueryValidator.getQueryType(query);

      if(queryType.equals(QueryTypes.INVALID)){
         return Database.getAllEmployees().containsKey(Integer.parseInt(query));

      }else if(queryType.equals(QueryTypes.CREATE)){

         String[] tokens=query.split(" ");
         HashMap<String,String> columnsWithValues=new HashMap<>();

         for (String token:tokens[3].split(",")) {
            columnsWithValues.put(token.substring(0,token.indexOf("=")).toLowerCase(),token.substring(1+token.indexOf("=")));
         }

         id=Integer.parseInt(columnsWithValues.get("id".toLowerCase()));
         return Database.getAllEmployees().containsKey(id);

      }else if(queryType.equals(QueryTypes.DELETE)){

         String[] tokens=query.split(" ");
         id=Integer.parseInt(tokens[4].substring(1+tokens[4].indexOf("=")));
         return Database.getAllEmployees().containsKey(id);

      }else if(queryType.UPDATE.equals(queryType)) {
         String[] tokens = query.split(" ");
         id = Integer.parseInt(tokens[5].substring(1 + tokens[5].indexOf("=")));
         return Database.getAllEmployees().containsKey(id);
      }
         return false;

   }

   public static boolean isPasswordCorrect(Employee employee,String password){

      return Database.getEmployeeById(employee.getId()).getPassword().equals(password);
   }

   public static void exit(){
      JSONFile.writeRecords();
   }

   public static String printInstructions(){
      //condition is not required
      String result="select rows: select {name of columns/*} from {name of the table/employee} where {condition}\n";
      result+="create row: create {name of the table/employee} values: id={value},name={value},salary={value},department={value},password={value} {type==supervisor/subordinate}\n";
      result+="delete row/rows: delete from {name of the table/employee} where {condition}\n";
      result+="update row/rows: update {name of the table/employee} set {name of columns}={value} where {condition}\n";
      result+="note that name of columns separated with comma such as id,name...etc for select and id={value},name={value}...etc for update\n";
      result+="to stop the application: exit";
      return result;
   }



}

