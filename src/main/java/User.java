import menues.TypeOfUser;

import java.util.ArrayList;

public class User{
    private int idUser;
    public String username;
    public String password;
    private String name;
    private ArrayList<Holiday> listOfHolidays = new ArrayList<>();
    public static final int daysOfHoliday = 21;
    private int remainingDaysOfHoliday = 21;
    TypeOfUser typeOfUser;

    User(String name, String username, String password, TypeOfUser typeOfUser){
        this.name = name;
        this.username = username;
        this.password = password;
        this.typeOfUser = typeOfUser;
    }
    public User(){}
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public TypeOfUser getTypeOfUser() {
        return typeOfUser;
    }

    public  ArrayList<Holiday> getlistOfHolidays(){return listOfHolidays;}

    public String getName(){ return name;}

    public int getRemainingDaysOfHoliday() {
        return remainingDaysOfHoliday;
    }

    public int getIdUser() {
        return idUser;
    }

    public ArrayList<Holiday> getListOfHolidays() {
        return listOfHolidays;
    }
    public void setRemainingDaysOfHoliday(int remainingDaysOfHoliday) {
        this.remainingDaysOfHoliday = remainingDaysOfHoliday;
    }
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setListOfHolidays(ArrayList<Holiday> listOfHolidays) {
        this.listOfHolidays = listOfHolidays;
    }

    public void setTypeOfUser(TypeOfUser typeOfUser) {
        this.typeOfUser = typeOfUser;
    }

    @Override
    public String toString() {

        return idUser + " "
               + name +
               " " + typeOfUser;
    }
}
