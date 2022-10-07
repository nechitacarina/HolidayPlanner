package menues;

//this enum contains the types of users

public enum TypeOfUser {
    ADMIN(1, "Admin"),
    EMPLOYEE(2, "Employee");

     private int number;

     private String description;
    TypeOfUser(int number, String description) {
        this.number = number;
        this.description = description;
    }

    public int getNumber(){
        return number;
    }
    public String getDescription(){
        return description;
    }
}
