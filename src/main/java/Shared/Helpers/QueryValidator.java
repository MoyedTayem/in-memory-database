package Shared.Helpers;

import Shared.Models.Employee;
import Shared.Models.EmployeeEnum;
import Shared.Models.QueryTypes;


import java.util.*;
import java.util.stream.Collectors;

public abstract class QueryValidator {

    private static final String[] tables={"employee"};

    public static QueryTypes getQueryType(String query){

        query=QueryValidator.cleanSpaces(query);
        String[] tokens=query.split(" ");
        tokens[0]=tokens[0].toLowerCase();

        switch (tokens[0]){
            case "select": return QueryTypes.SELECT;
            case "delete": return QueryTypes.DELETE;
            case "update": return QueryTypes.UPDATE;
            case "create": return QueryTypes.CREATE;
        }

        return QueryTypes.INVALID;
    }

    public static boolean isValidSelectQuery(String query){

        query=cleanSpaces(query);
        String[] tokens=query.split(" ");
        if(tokens.length!=4 && tokens.length!=6)
            return false;
        if((tokens[0].equalsIgnoreCase("select") && tokens[2].equalsIgnoreCase("from"))
            &&(Arrays.stream(tables).noneMatch(t -> t.equalsIgnoreCase(tokens[3]))))
            return false;
        else{

            if (tokens[1].equalsIgnoreCase("*")){
                //select all parameters
            }else if (tokens[1].contains(",")){
                String[] parameters = tokens[1].split(",");

                for (String parameter:
                     parameters) {
                    if (!isValidParameter(parameter,tokens[3]))
                        return false;
                }
            }else {
                if (!isValidParameter(tokens[1],tokens[3]))
                    return false;
            }
        }
        if(tokens.length>4){
            if(!tokens[4].equalsIgnoreCase("where"))
                return false;
            return isValidCondition(tokens[5]);
        }
        return true;
    }

    public static boolean isValidUpdateQuery(String query) {

        query=cleanSpaces(query);
        String[] tokens = query.split(" ");
        if(tokens.length!=4 && tokens.length!=6)
            return false;

        if (tokens[0].equalsIgnoreCase("update")  && Arrays.stream(tables).filter(t -> t.equalsIgnoreCase(tokens[1])).count() == 0
                && tokens[2].equalsIgnoreCase("set") )
            return false;

        if(!isValidSetStatement(tokens[3]))
            return false;

        //update employee set id=1234,name=moyed where id=1343233
        if(tokens.length>4){
            if(!tokens[4].equalsIgnoreCase("where"))
                return false;
            return isValidCondition(tokens[5]);
        }

        return true;
    }

    public static boolean isValidDeleteQuery(String query){
        query=cleanSpaces(query);
        String[] tokens=query.split(" ");
        if(tokens.length!=3 && tokens.length!=5)
            return false;

        if(!tokens[0].equalsIgnoreCase("delete")&&
                Arrays.stream(tables).noneMatch(t -> t.equalsIgnoreCase(tokens[2])) &&
                !tokens[1].equalsIgnoreCase("from"))
            return false;

        if(tokens.length>3){
            if(tokens[3].compareToIgnoreCase("where")!=0)
                return false;
            return isValidCondition(tokens[4]);
        }

        return true;
    }

    public static boolean isValidCreateQuery(String query){
        query=cleanSpaces(query);
        String[] tokens=query.split(" ");
        if(tokens.length!=5)
            return false;
        if(!tokens[0].equalsIgnoreCase("create")&&!
                (tokens[4].equalsIgnoreCase(EmployeeEnum.SUBORDINATE.getEmployeeType())
            ||(tokens[4].equalsIgnoreCase(EmployeeEnum.SUPERVISOR.getEmployeeType())))&&
            !tokens[2].equalsIgnoreCase("values:")&&Arrays.stream(tables).noneMatch(t -> t.equalsIgnoreCase(tokens[1])))
            return false;


        return isValidSetStatement(tokens[3].substring(0, tokens[3].indexOf("password"))) && tokens[3].split(",").length == 5;
    }

    private static boolean isValidParameter(String parameter, String tableName){

        String[] availableColumns = null;

        if ("employee".equals(tableName)) {
            availableColumns = Employee.getColumns();
        }

        if (availableColumns == null)
            return false;

        return Arrays.stream(availableColumns).anyMatch(c -> c.equalsIgnoreCase(parameter));
    }

    private static boolean isValidCondition(String condition) {
        String[] comparisonOperators = {"=", "!=", "<=", ">=", ">", "<"};

        if(Arrays.stream(comparisonOperators).filter(condition::contains).count()==0)
            return false;

        boolean correct=false;
        for (String operator:comparisonOperators) {
            if(condition.split(operator).length==2)
                if(Arrays.stream(Employee.getColumns()).filter(e->e.equalsIgnoreCase(condition.split(operator)[0])).count()==1)
                    correct= true;

        }

        return correct;
    }

    public static String cleanSpaces(String query){
        query=query.trim().replaceAll(" +", " ");
        query=query.replaceAll("\\s[,]\\s+",",");

        return query;
    }

    private static boolean isValidSetStatement(String statement){
        String[] tokens=statement.split(",");
        for (String token:tokens) {
            if(!isValidParameter(token.substring(0,token.indexOf('=')),tables[0]))
                return false;
        }
        for (String token:tokens) {
            if(token.substring(token.indexOf('=')+1).equalsIgnoreCase("")||
                    token.substring(token.indexOf('=')+1).equalsIgnoreCase(" "))
                return false;
        }
        return true;
    }
}
