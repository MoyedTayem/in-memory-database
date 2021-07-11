package Shared.Helpers;

import Shared.Models.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PrintResult {
    public static String printResult(TreeMap<Integer, Employee> result,String query){
        StringBuilder resultString= new StringBuilder();
        if(query.contains("*")){
            resultString.append(String.format("|%-20s|", "ID")).append(String.format("%-20s|", "Name")).append(String.format("%-20s|", "Salary")).append(String.format("%-15s|", "Department")).append("\n");
        for (Map.Entry<Integer,Employee> entry:result.entrySet()
             ) {
            Employee value=entry.getValue();
            resultString.append(String.format("|%-20s|", value.getId())).append(String.format("%-20s|", value.getName())).append(String.format("%-20s|", value.getSalary())).append(String.format("%-15s|", value.getDepartment())).append("\n");
        }

        }else {
            resultString.append(requiredColumnsName(resultShape(query)));
            for (Map.Entry<Integer,Employee> entry:result.entrySet()
            ) {
                Employee value=entry.getValue();
                resultString.append(requiredColumns(resultShape(query), value));
            }
        }

        return resultString.toString();
    }
    public static String requiredColumns(ArrayList<String> columns, Employee temp){
        String result="";

        if(columns.contains("Id"))
            result+=String.format("|%-20s|",temp.getId());
        if(columns.contains("Name"))
            result+=String.format("%-20s|",temp.getName());
        if(columns.contains("Salary"))
            result+=String.format("%-20s|",temp.getSalary());
        if(columns.contains("Department"))
            result+=String.format("%-15s|",temp.getDepartment());

        return result+"\n";
    }
    public static ArrayList<String> resultShape(String query){
        query=query.toLowerCase();
        ArrayList<String> result=new ArrayList<>();
        if(query.contains("id"))
            result.add("Id");
        if(query.contains("name"))
            result.add("Name");
        if(query.contains("salary"))
            result.add("Salary");
        if(query.contains("department"))
            result.add("Department");


        return result;

    }
    public static String requiredColumnsName(ArrayList<String> columns){
        String result="";

        if(columns.contains("Id"))
            result+=String.format("|%-20s|","ID");
        if(columns.contains("Name"))
            result+=String.format("%-20s|","Name");
        if(columns.contains("Salary"))
            result+=String.format("%-20s|","Salary");
        if(columns.contains("Department"))
            result+=String.format("%-15s|","Department");

        return result+"\n";
    }

}
