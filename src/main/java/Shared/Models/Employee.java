package Shared.Models;

public abstract class Employee implements Comparable<Employee> {
    private int id;
    private String name;
    private double salary;
    private String department;
    private String password;

    public Employee(){}

    public Employee(int id, String name, double salary, String department , String password){
        this.id=id;
        this.name=name;
        this.salary=salary;
        this.department=department;
        this.password=password;
    }

    @Override
    public String toString() {
        return "Shared.Models.Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", department='" + department + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static String[] getColumns(){

        return new String[]{"id","name","salary","department"};

    }

    @Override
    public abstract int compareTo(Employee o);


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


}
