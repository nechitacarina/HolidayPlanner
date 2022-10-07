package menues;

//this enum contains the status numbers and descriptions for holidays

public enum HolidayStatus {
    ACCEPTED(1, "Accepted"),
    PENDING(2, "Pending"),
    REJECTED(3, "Rejected"),

    CANCELED(4, "Canceled");

    int status_number;
    String status_description;

    HolidayStatus(int status_number, String status_description){
        this.status_number = status_number;
        this.status_description = status_description;
    }

    public int getStatus_number() {
        return status_number;
    }

    public String getStatus_description() {
        return status_description;
    }
}
