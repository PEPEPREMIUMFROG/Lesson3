package org.example.model;

public enum BookStatus {
    BORROWED("borrowed"),
    RETURNED("returned");

    private final String dbVal;

    BookStatus(String dbVal) {
        this.dbVal = dbVal;
    }

    public String getDbVal() {
        return dbVal;
    }

    public static BookStatus fromDbVal(String val){
        if (val == null){
            throw new RuntimeException("Status value cant be null");
        }
        for (BookStatus status:values()){
            if (status.dbVal.equalsIgnoreCase(val)){
                return status;
            }
        }throw new RuntimeException("Unknown status: "+val);
    }
}
