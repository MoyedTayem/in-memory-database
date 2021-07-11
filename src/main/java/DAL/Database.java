package DAL;

import Shared.Models.Employee;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;




public class Database {

    private static Database database;
    private static final ReentrantLock deleteLock = new ReentrantLock();
    private static final ReentrantLock addLock=new ReentrantLock();
    private static final ReentrantLock updateLock=new ReentrantLock();
    private static final TreeMap<Integer,Employee> companyEmployees = new TreeMap<Integer,Employee>();
    public static void getDatabase() {
        if (database == null) {
            database = new Database();
            JSONFile.readRecords();
        }
    }
    public static Employee getEmployeeById(int id) {
        while (updateLock.isLocked()|| deleteLock.isLocked()|| addLock.isLocked());
        return companyEmployees.get(id);
    }
    public static void addEmployee(Employee newEmployee)  {

        while (addLock.isLocked());
        addLock.lock();
        companyEmployees.put(newEmployee.getId(),newEmployee);
        addLock.unlock();
    }
    public static void deleteAllEmployees(){
        while (deleteLock.isLocked()||updateLock.isLocked());
        deleteLock.lock();
        companyEmployees.clear();
        deleteLock.unlock();
    }

    public static TreeMap<Integer,Employee> getAllEmployees() {
        while (updateLock.isLocked()|| deleteLock.isLocked()|| addLock.isLocked());
        return companyEmployees;
    }
    public static void updateEmployee(int id, Employee newEmployee) {
        while (deleteLock.isLocked()||updateLock.isLocked());
        updateLock.lock();
        companyEmployees.replace(id,newEmployee);
        updateLock.unlock();
    }
    public static boolean isDeleteLocked(){
        return deleteLock.isLocked() || updateLock.isLocked();
    }
    public static void unlockDelete(){
        deleteLock.unlock();
    }
    public static void lockDelete(){
        deleteLock.lock();
    }


}




