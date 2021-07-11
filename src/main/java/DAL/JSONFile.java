package DAL;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import BLL.EmployeeServices;
import Shared.Models.Employee;
import Shared.Models.EmployeeEnum;
import Shared.Models.Subordinate;
import Shared.Models.Supervisor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class JSONFile {

    public static void readRecords(){
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("src/main/java/DAL/CompanyEmployees.JSON")) {
            Object obj = jsonParser.parse(reader);
            JSONArray employeeList = (JSONArray) obj;
            employeeList.forEach(emp -> parseEmployeeObject((JSONObject) emp));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void parseEmployeeObject(JSONObject employee) {
        JSONObject employeeObject = (JSONObject) employee.get("employee");
        int id = Integer.parseInt((String) employeeObject.get("id"));
        String name = (String) employeeObject.get("name");
        String salary=(String) employeeObject.get("salary");
        String department= (String) employeeObject.get("department");
        String type= (String) employeeObject.get("type");
        String password= (String) employeeObject.get("password");
        if(type.equalsIgnoreCase("supervisor")){
            Database.addEmployee(new Supervisor(id,name,Double.parseDouble(salary),department,password));
        }else{
            Database.addEmployee(new Subordinate(id,name,Double.parseDouble(salary),department,password));
        }
    }

    public static void writeRecords(){
        JSONArray employeeList =jsonArrayParser();
        try (FileWriter file = new FileWriter("src/main/java/DAL/CompanyEmployees.JSON")) {
            file.write(employeeList.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONArray jsonArrayParser() {
        JSONArray employeeList = new JSONArray();

        for (Map.Entry<Integer, Employee> employeeTreeMap : Database.getAllEmployees().entrySet()) {
            JSONObject employeeDetails = new JSONObject();
            JSONObject employeeObject = new JSONObject();
            Employee value = employeeTreeMap.getValue();
            employeeDetails.put("id", value.getId() + "");
            employeeDetails.put("name", value.getName());
            employeeDetails.put("salary", value.getSalary() + "");
            employeeDetails.put("department", value.getDepartment());
            employeeDetails.put("password", value.getPassword());
            if (value instanceof Supervisor)
                employeeDetails.put("type", "supervisor");
            else
                employeeDetails.put("type", "subordinate");

            employeeObject.put("employee", employeeDetails);

            employeeList.add(employeeObject);

        }
        return employeeList;
    }
}





