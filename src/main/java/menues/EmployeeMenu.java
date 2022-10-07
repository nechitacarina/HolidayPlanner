package menues;

//this enum contains the options numbers and options names for the employee menu

public enum EmployeeMenu {
    SHOW_AVAILABLE_DAYS_OFF(1, "Show available days off"),
    SHOW_TAKEN_HOLIDAYS(2, "Show already taken holidays"),
    ADD_NEW_HOLIDAY_PERIOD(3, "Add new holidays"),
    SHOW_UPCOMING_HOLIDAYS(4, "Show upcoming holidays"),
    CANCEL_UPCOMING_HOLIDAYS(5, "Cancel upcoming holidays"),
    LOGOUT(6, "Logout"),
    EXIT(0, "Exit");

    private int option_number;
    private String option_name;

    EmployeeMenu(int option_number, String option_name){
        this.option_number = option_number;
        this.option_name = option_name;
    }

    public int getOption_number() {
        return option_number;
    }

    public String getOption_name() {
        return option_name;
    }
}
