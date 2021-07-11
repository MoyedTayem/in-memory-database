package Shared.Models;

import Shared.Models.Employee;

public class Empty extends Employee {
    @Override
    public int compareTo(Employee o) {
        return 0;
    }
}

