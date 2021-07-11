package Shared.Models;

public enum EmployeeEnum {
    SUBORDINATE("Subordinate"),
    SUPERVISOR("Supervisor");

    private String employeeType;

    EmployeeEnum(String employeeType) {
        this.setEmployeeType(employeeType);
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }
}
