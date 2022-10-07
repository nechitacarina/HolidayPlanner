package menues;

//this enum contains the options numbers and options name for the admin menu

public enum AdminMenu {
    DISPLAY_REQUESTED_HOLIDAYS(1, "Display the requested holidays"),
    ACCEPT_HOLIDAY(2, "Accept holiday"),
    REJECT_HOLIDAY(3, "Reject holiday"),
    MANAGE_USERS(4, "Manage users"),
    LOGOUT(5, "Logout"),
    EXIT(0, "Exit");

    private int option_number;
    private String option_name;

    AdminMenu(int option_number, String option_name){
        this.option_number = option_number;
        this.option_name = option_name;
    }

    public int getOptionNumber() {
        return option_number;
    }

    public String getOption_name() {
        return option_name;
    }
}
