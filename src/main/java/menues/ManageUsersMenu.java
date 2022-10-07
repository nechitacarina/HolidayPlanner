package menues;

public enum ManageUsersMenu {
    ADD_NEW_USER(1, "Add new user"),
    DELETE_USER(2,"Delete user");

    private int optionNumber;
    private String optionDescription;

    ManageUsersMenu(int optionNumber, String optionDescription){
        this.optionNumber = optionNumber;
        this.optionDescription = optionDescription;
    }
    public int getOptionNumber(){
        return optionNumber;
    }
    public String getOptionDescription(){
        return optionDescription;
    }
}
