package Shared.Models;

public enum QueryTypes {
    SELECT("Select"),
    UPDATE("Update"),
    DELETE("Delete"),
    CREATE("Create"),
    INVALID("Invalid");

    private String queryType;

    QueryTypes(String queryType) {
        this.setQueryType(queryType);
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
}
