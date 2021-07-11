package Shared.Models;

import Shared.Models.Employee;

public class Supervisor extends Employee {
    public Supervisor(int id, String name, double salary, String department, String password) {
        super(id, name, salary, department, password);
    }

    @Override
    public int compareTo(Employee o) {
        if(getId()==o.getId())
            return 0;
        else if(getId()>o.getId())
            return 1;
        else
            return -1;

    }
}
