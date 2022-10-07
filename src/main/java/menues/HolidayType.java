package menues;

public enum HolidayType {
    TIMEOFF(1, "Time off"),
    SICKLEAVE(2, "Sick leave"),
    STUDYLEAVE(3, "Study leave");

    private int optionTypeNumber;
    private String optionTypeName;

    HolidayType(int optionTypeNumber, String optionTypeName){
        this.optionTypeNumber = optionTypeNumber;
        this.optionTypeName = optionTypeName;
    }

    public int getOptionTypeNumber() {
        return optionTypeNumber;
    }

    public String getOptionTypeName() {
        return optionTypeName;
    }

}
