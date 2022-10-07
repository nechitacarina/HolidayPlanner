package menues;

//this enum contains principale operations

public enum StartMenu {
    LOGIN(1, "Login"),
    EXIT(2, "Exit");
    private int option_number;
    private String option_name;

    StartMenu(int option_number, String option_name ){
        this.option_name = option_name;
        this.option_number = option_number;
    }

    public int getOption_number() {
        return option_number;
    }

    public String getOption_name() {
        return option_name;
    }
}
