import menues.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HolidayActions {

    protected static void addUser(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Introduce the username: ");
        String inputUsername = scan.nextLine();
        System.out.println("Introduce the password: ");
        String inputPassword = scan.nextLine();
        System.out.println("Introduce the name: ");
        String inputName = scan.nextLine();
        System.out.println("Introduce the type of user: ");
        displayTypeOfUser();
        TypeOfUser typeOfUser = null;
        while(typeOfUser == null){
            int inputTypeOfUser = scan.nextInt();
            for(TypeOfUser type: TypeOfUser.values())
                if(inputTypeOfUser == type.getNumber())
                    typeOfUser = type;
            if(typeOfUser==null) {
                System.out.println("This type of user does not exist! Please enter a valid one!");
                displayTypeOfUser();
            }
        }
        if(inputUsername.length() > 0 && inputName.length() > 0 && inputPassword.length() > 0){
        Configuration config = new Configuration();
        config.configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        User user = new User(inputName, inputUsername, inputPassword, typeOfUser);
        session.save(user);
        session.getTransaction().commit();
        System.out.println("The user has been added successfully");
        session.close();
        }
        else System.out.println("The user can't be added");
    }
    protected static void addHoliday(int idUser){
        try {
            Scanner scan = new Scanner(System.in);
            System.out.println("Introduce the start day of holiday:");
            String inputStartDate = scan.nextLine();
            Date dateStart = new SimpleDateFormat("dd/MM/yyyy").parse(inputStartDate);
            System.out.println("Introduce the end date of holiday:");
            String inputEndDate = scan.nextLine();
            Date dateEnd = new SimpleDateFormat("dd/MM/yyyy").parse(inputEndDate);
            System.out.println("Introduce the type of the holiday:");
            for(HolidayType h: HolidayType.values()){
                System.out.println(h.getOptionTypeNumber()+ "-" + h.getOptionTypeName());
            }
            int typeOfHoliday = scan.nextInt();
            HolidayType holidayType = null;
            for(HolidayType h: HolidayType.values()){
                if(typeOfHoliday == h.getOptionTypeNumber()) holidayType = h;
            }
            while(dateStart.after(dateEnd) && dateEnd.before(dateStart)){
                System.out.println("The start date can't be before the end date. Please enter a valid start date and end date!");
                scan = new Scanner(System.in);
                System.out.println("Introduce the start day of holiday:");
                String inputStartDate1 = scan.nextLine();
                dateStart = new SimpleDateFormat("dd/MM/yyyy").parse(inputStartDate1);
                System.out.println("Introduce the end date of holiday:");
                String inputEndDate1 = scan.nextLine();
                dateEnd = new SimpleDateFormat("dd/MM/yyyy").parse(inputEndDate1);
            }
            if (!isValidDate(inputStartDate) && !isValidDate(inputEndDate)){
                System.out.println("The date is  not valid!!");
            }
            else{
                Calendar c1 = Calendar.getInstance();
                c1.setTime(dateStart);

                Calendar c2 = Calendar.getInstance();
                c2.setTime(dateEnd);

                if((c1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || c1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) && (c2.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || c2.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) System.out.println("The date requested for holiday is a weekend");
                else {
                    Configuration config = new Configuration();
                    config.configure();
                    SessionFactory sessionFactory = config.buildSessionFactory();
                    Session session = sessionFactory.getCurrentSession();
                    session.beginTransaction();
                    User user = session.load(User.class, idUser);
                    if(session.createQuery("SELECT h.startDate, h.endDate, h.User.idUser FROM Holiday h WHERE ((h.startDate >= :sd AND h.startDate <= :ed) OR (h.endDate >= :sd AND h.endDate <= :ed) OR(h.startDate <= :sd AND h.endDate >= :ed)) AND h.User.idUser = :iu",Object[].class).setParameter("sd",dateStart).setParameter("ed", dateEnd).setParameter("iu",idUser).list().size() == 0)
                    {
                        Holiday holiday = new Holiday(dateStart, dateEnd, HolidayStatus.PENDING, user, holidayType);
                        session.save(holiday);
                        session.getTransaction().commit();
                        session.close();
                    }
                    else
                        System.out.println("This holiday can't be added. It contains dates that already exist in another holiday");
                }
            }
        }catch (ParseException e){
            System.out.println("Invalid date");
        }
    }

    protected static void acceptHoliday(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the id of the holiday you want to accept");
        int idHolidayWhichNeedToBeAccepted = scanner.nextInt();
        boolean idExists = false;
        ArrayList<User> users = loadingUsersFromDatabase();
        for(User user:users) {
            ArrayList<Holiday> holidays = loadingHolidaysFromDatabase(user.getIdUser());
            for (Holiday holiday : holidays) {
                if (holiday.getIdHoliday() == idHolidayWhichNeedToBeAccepted && holiday.getStatus() == HolidayStatus.PENDING) {
                    idExists = true;
                    Calendar c1 = Calendar.getInstance();
                    c1.setTime(holiday.getStartDate());
                    Calendar c2 = Calendar.getInstance();
                    c2.setTime(holiday.getEndDate());
                    int daysBetween = 1;
                    if((Calendar.SATURDAY == c2.get(Calendar.DAY_OF_WEEK)) || (Calendar.SUNDAY == c2.get(Calendar.DAY_OF_WEEK)) || Calendar.SATURDAY == c2.get(Calendar.DAY_OF_WEEK) || Calendar.SATURDAY == c1.get(Calendar.DAY_OF_WEEK) || Calendar.SUNDAY == c1.get(Calendar.DAY_OF_WEEK)){
                        daysBetween--;
                    }
                    while (c1.before(c2)) {
                        if ((Calendar.SATURDAY != c1.get(Calendar.DAY_OF_WEEK) && (Calendar.SUNDAY != c1.get(Calendar.DAY_OF_WEEK)))) {
                            daysBetween++;
                        }
                        c1.add(Calendar.DATE, 1);
                    }
                    Query query= createSession().createQuery("SELECT u.remainingDaysOfHoliday FROM User u WHERE u.idUser = :idUser").setParameter("idUser", user.getIdUser());
                    int remainingDaysOfHolidayFromDatabase = (int) query.uniqueResult();
                    int remainingDaysOfHoliday = remainingDaysOfHolidayFromDatabase -daysBetween;
                    if (remainingDaysOfHoliday >= daysBetween) {
                        int idCurrentUserInList = user.getIdUser();
                        Configuration config = new Configuration();
                        config.configure();
                        SessionFactory sessionFactory = config.buildSessionFactory();
                        Session session = sessionFactory.getCurrentSession();
                        session.beginTransaction();
                        session.createQuery("UPDATE Holiday h SET h.status = :s WHERE h.idHoliday = :idHolidayWhichNeedToBeAccepted").setParameter("idHolidayWhichNeedToBeAccepted", idHolidayWhichNeedToBeAccepted).setParameter("s", HolidayStatus.ACCEPTED).executeUpdate();
                        if(holiday.getHolidayType() == HolidayType.TIMEOFF)
                            session.createQuery("UPDATE User u SET u.remainingDaysOfHoliday = :remainingDays WHERE u.idUser = :idUser").setParameter("idUser", idCurrentUserInList).setParameter("remainingDays", remainingDaysOfHoliday).executeUpdate();
                        session.getTransaction().commit();
                        session.close();
                        System.out.println("The holiday with id " + idHolidayWhichNeedToBeAccepted + " has been accepted");
                    }
                }
            }
        }
        if (!idExists) System.out.println("The holiday can't be accepted");
    }

    protected static void cancelHoliday(int idUser){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the id of the holiday you want to cancel");
        int idHolidayWhichWantToBeCanceled = scanner.nextInt();
        boolean idPresent = false;
        ArrayList<Holiday> upcomingHolidays = loadingUpcomingHolidaysFromDatabase(idUser);
        for(Holiday holiday: upcomingHolidays){
            if(holiday.getIdHoliday() == idHolidayWhichWantToBeCanceled){
                idPresent = true;
                Configuration configuration = new Configuration();
                configuration.configure();
                SessionFactory sessionFactory = configuration.buildSessionFactory();
                Session session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                if(session.createQuery("FROM Holiday h WHERE h.status = 'ACCEPTED' AND h.idHoliday = :idHolidayWhichWantToBeCanceled").setParameter("idHolidayWhichWantToBeCanceled",idHolidayWhichWantToBeCanceled).list().size() > 0){
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(holiday.getStartDate());
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(holiday.getEndDate());
                    int daysBetween = 1;
                    if((Calendar.SATURDAY == calendar2.get(Calendar.DAY_OF_WEEK)) || (Calendar.SUNDAY == calendar2.get(Calendar.DAY_OF_WEEK)) || Calendar.SATURDAY == calendar2.get(Calendar.DAY_OF_WEEK) || Calendar.SATURDAY == calendar1.get(Calendar.DAY_OF_WEEK) || Calendar.SUNDAY == calendar1.get(Calendar.DAY_OF_WEEK)){
                        daysBetween--;
                    }
                    while (calendar1.before(calendar2)) {
                        if ((Calendar.SATURDAY != calendar1.get(Calendar.DAY_OF_WEEK) && (Calendar.SUNDAY != calendar1.get(Calendar.DAY_OF_WEEK)))) {
                            daysBetween++;
                        }
                        calendar1.add(Calendar.DATE, 1);
                    }
                    Query query= createSession().createQuery("SELECT u.remainingDaysOfHoliday FROM User u WHERE u.idUser = :idUser").setParameter("idUser", idUser);
                    int remainingDaysOfHolidayFromDatabase = (int) query.uniqueResult();
                    int daysToGiveBack = remainingDaysOfHolidayFromDatabase + daysBetween;
                    session.createQuery("UPDATE Holiday h SET h.status = :s WHERE h.idHoliday = :idHolidayWhichWantToBeCanceled").setParameter("idHolidayWhichWantToBeCanceled", idHolidayWhichWantToBeCanceled).setParameter("s",HolidayStatus.CANCELED).executeUpdate();
                    session.createQuery("UPDATE User u SET u.remainingDaysOfHoliday = :daysToGiveBack WHERE u.idUser = :idUser").setParameter("idUser", idUser).setParameter("daysToGiveBack", daysToGiveBack).executeUpdate();
                    session.getTransaction().commit();
                    session.close();
                }
                else{
                    session.createQuery("UPDATE Holiday h SET h.status = :s WHERE h.idHoliday = :idHolidayWhichWantToBeCanceled").setParameter("idHolidayWhichWantToBeCanceled", idHolidayWhichWantToBeCanceled).setParameter("s",HolidayStatus.CANCELED).executeUpdate();
                    session.getTransaction().commit();
                }
                System.out.println("The holiday with id " + idHolidayWhichWantToBeCanceled + " has been canceled");
            }
        }
        if(!idPresent) System.out.println("The holiday you are trying to cancel does not exist!");
    }

    protected static Session createSession(){
        Configuration config = new Configuration();
        config.configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        return session;
    }

    protected static void displayStartMenu(){
        for(StartMenu m: StartMenu.values()){
            System.out.println(m.getOption_number() + "-" + m.getOption_name());
        }
        System.out.println("Choose an option: ");
    }

    protected static void displayEmployeeMenu(){
        for(EmployeeMenu m: EmployeeMenu.values()){
            System.out.println(m.getOption_number() + "-" + m.getOption_name());
        }
        System.out.println("Choose an option: ");
    }

    protected static void displayAdminMenu(){
        for(AdminMenu m: AdminMenu.values()){
            System.out.println(m.getOptionNumber() + "-" + m.getOption_name());
        }
        System.out.println("Choose an option: ");
    }

    protected static void displayManageUsersMenu(){
        for(ManageUsersMenu usersMenu: ManageUsersMenu.values()){
            System.out.println(usersMenu.getOptionNumber() + "-" + usersMenu.getOptionDescription());
        }
        System.out.println("Please choose an option");
    }

    protected static void displayTypeOfUser(){
        for(TypeOfUser typeOfUser: TypeOfUser.values())
            System.out.println(typeOfUser.getNumber()+"-"+typeOfUser.getDescription());
    }

    protected static void displayUserHolidays(int userId){
        if(createSession().createQuery("FROM Holiday h WHERE h.User.idUser = :uId", Holiday.class).setParameter("uId",userId).list().size() > 0)
        {
            Query<Object[]> query = createSession().createQuery(" SELECT h.idHoliday, DATE_FORMAT(h.startDate,'%d-%m-%Y'), DATE_FORMAT(h.endDate,'%d-%m-%Y'), h.status, h.holidayType FROM Holiday h WHERE h.User.idUser = :uId", Object[].class).setParameter("uId",userId);
            List<Object[]> list = query.list();
            for(Object[] arr : list){
                System.out.println(Arrays.toString(arr));
            }
        }
        else
            System.out.println("There are no taken holidays");
    }

    protected static int displayPendingListOfHolidays(){
        if(createSession().createQuery("FROM Holiday h WHERE h.status = :s", Holiday.class).setParameter("s",HolidayStatus.PENDING).list().size() > 0)
        {
            Query<Object[]> query = createSession().createQuery("SELECT h.idHoliday, u.name, DATE_FORMAT(h.startDate,'%d-%m-%Y'), DATE_FORMAT(h.endDate,'%d-%m-%Y'), h.status, h.holidayType FROM Holiday h, User u WHERE h.status = 'PENDING' AND u.idUser = h.User.idUser", Object[].class);
            List<Object[]> list = query.list();
            for(Object[] arr : list){
                System.out.println(Arrays.toString(arr));
            }
            return 1;
        }
        else return 0;
    }

    protected static boolean displayUserPendingOrAcceptedUpcomingListOfHolidays(int idUser){
        if(createSession().createQuery("FROM Holiday h WHERE (h.status = 'PENDING' OR h.status = 'ACCEPTED') AND h.startDate >= current_date AND h.User.idUser = :idUser", Holiday.class).setParameter("idUser", idUser).list().size()>0){
            Query<Object[]> query = createSession().createQuery(" SELECT h.idHoliday, DATE_FORMAT(h.startDate,'%d-%m-%Y'), DATE_FORMAT(h.endDate,'%d-%m-%Y'), h.status, h.holidayType FROM Holiday h WHERE (h.status = 'PENDING' OR h.status = 'ACCEPTED') AND h.startDate >= current_date AND h.User.idUser = :idUser", Object[].class).setParameter("idUser", idUser);
            List<Object[]> list = query.list();
            for(Object[] arr : list){
                System.out.println(Arrays.toString(arr));
            }
            return true;
        }
        else return false;
    }

    protected static void displayUserAvailableDaysOff(int idUser){
        System.out.println(createSession().createQuery("SELECT u.remainingDaysOfHoliday FROM User u WHERE u.idUser = :idUser", Integer.class).setParameter("idUser",idUser).list());
    }

    protected static void deleteUser(){
        ArrayList<User> users = loadingUsersFromDatabase();
        System.out.println("Please enter the user id you want to delete: ");
        for(User user: users){
            System.out.println(user);
        }
        Scanner scanner = new Scanner(System.in);
        int idUserToDelete = 0;
        while (idUserToDelete == 0){
            int inputIdUserToDelete = scanner.nextInt();
            for (User user: users){
                if(user.getIdUser() == inputIdUserToDelete){
                    idUserToDelete = inputIdUserToDelete;
                    break;
                }
            }
        }
        Configuration config = new Configuration();
        config.configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM User u WHERE u.idUser = :idUser").setParameter("idUser",idUserToDelete).executeUpdate();
        session.getTransaction().commit();
        session.close();
        System.out.println("The user has been deleted successfully!");
    }

    private static boolean isValidDate(String input) { //this method verifies if an input date in valid
        String formatString = "dd/MM/yyyy";
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatString);
            format.setLenient(false);
            format.parse(input);
        } catch (ParseException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    protected static ArrayList<User> loadingUsersFromDatabase(){
        ArrayList<User>listOfUsers = (ArrayList<User>) createSession().createQuery("FROM User", User.class).list();
        return listOfUsers;
    }

    protected static ArrayList<Holiday> loadingHolidaysFromDatabase(int idUser){
        ArrayList<Holiday>listOfHolidays = (ArrayList<Holiday>) createSession().createQuery("FROM Holiday h WHERE h.User.idUser = :idUser", Holiday.class).setParameter("idUser", idUser).list();
        return listOfHolidays;
    }

    protected static ArrayList<Holiday> loadingUpcomingHolidaysFromDatabase(int idUser){
        ArrayList<Holiday> listOfUpcomingHolidays = (ArrayList<Holiday>) createSession().createQuery("FROM Holiday h WHERE h.startDate >= current_date AND h.User.idUser = :loginUser", Holiday.class).setParameter("loginUser", idUser).list();
        return listOfUpcomingHolidays;
    }

    protected static int readOptionNumberFromConsole() {
        Scanner scan = new Scanner(System.in);
        boolean isNumber = false;
        int number = 0;
        while (!isNumber) {
            if (scan.hasNextInt()) {
                number = scan.nextInt();
                isNumber = true;
            } else {
                System.out.println("Please enter an option number!");
                scan = new Scanner(System.in);
                number = scan.nextInt();
                isNumber = true;
            }
        }
        return number;
    }

    protected static void rejectHoliday(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the id of the holiday you want to reject");
        int idHolidayWhichWantToBeRejected = scanner.nextInt();
        boolean idExists = false;
        ArrayList<User> users = loadingUsersFromDatabase();
        for(User user:users){
            ArrayList<Holiday> holidays = loadingHolidaysFromDatabase(user.getIdUser());
            for (Holiday holiday : holidays) {
                if (holiday.getIdHoliday() == idHolidayWhichWantToBeRejected && holiday.getStatus() == HolidayStatus.PENDING) {
                    idExists = true;
                    Configuration config = new Configuration();
                    config.configure();
                    SessionFactory sessionFactory = config.buildSessionFactory();
                    Session session = sessionFactory.getCurrentSession();
                    session.beginTransaction();
                    session.createQuery("UPDATE Holiday h SET h.status = :s WHERE h.idHoliday = :idHolidayWhichWantToBeRejected").setParameter("idHolidayWhichWantToBeRejected", idHolidayWhichWantToBeRejected).setParameter("s", HolidayStatus.REJECTED).executeUpdate();
                    session.getTransaction().commit();
                    session.close();
                    System.out.println("The holiday with id " + idHolidayWhichWantToBeRejected + " has been rejected");
                }
            }
        }
        if(!idExists) System.out.println("The holiday does not exist or it was already accepted or rejected");
    }

    protected static User readCredentials(){
        User loggedInUser = null;
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter your credentials!");
        System.out.println("username: ");
        String username = scan.nextLine(); // read the line from console
        System.out.println("password: ");
        String password = scan.nextLine();
        boolean user_exist = false;
        ArrayList<User> users = loadingUsersFromDatabase();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                user_exist = true;
                loggedInUser = user;
            }
        }
        if (!user_exist)
            System.out.println("Incorrect username or password!");
        else
            System.out.println("You have been logged in successfully!");
        return loggedInUser;
    }

    protected static void showMainScreen() throws ParseException {
        while(true){
            int option = readOptionNumberFromConsole();
            if(option == StartMenu.EXIT.getOption_number()) System.exit(0);
            else if(option == StartMenu.LOGIN.getOption_number()){
                User loginUser = HolidayActions.readCredentials();
                if(loginUser!=null)
                {
                    System.out.println(loginUser);
                    if(loginUser.getTypeOfUser() == TypeOfUser.ADMIN) adminMenuOptions();
                    else if(loginUser.getTypeOfUser() == TypeOfUser.EMPLOYEE) employeeMenuOptions(loginUser);
                    else
                    {
                        System.out.println("This option does not exist!");
                        displayStartMenu();
                    }
                }
                else displayStartMenu();
            }
            else {
                System.out.println("This option does not exist!");
                displayStartMenu();
            }
        }
    }


    protected static void adminMenuOptions() throws ParseException {
        displayAdminMenu();
        while(true){
            int adminMenuOption = readOptionNumberFromConsole();
            if(adminMenuOption == AdminMenu.EXIT.getOptionNumber()) System.exit(0);
            else if(adminMenuOption == AdminMenu.DISPLAY_REQUESTED_HOLIDAYS.getOptionNumber())
            {
                System.out.println("--LIST OF REQUESTED HOLIDAYS--");
                if(displayPendingListOfHolidays() == 0)  System.out.println("There are no requested holidays");
                else displayPendingListOfHolidays();
                System.out.println("If you want to do another operation, please enter an option number");
                displayAdminMenu();
            }
            else if (adminMenuOption == AdminMenu.ACCEPT_HOLIDAY.getOptionNumber()){
                System.out.println("--ACCEPT HOLIDAYS--");
                if(displayPendingListOfHolidays()==1){
                acceptHoliday();
                displayAdminMenu();
                }
                else {
                    System.out.println("There are no pending holidays");
                    displayAdminMenu();
                }
            }
            else if (adminMenuOption == AdminMenu.REJECT_HOLIDAY.getOptionNumber())
            {
                System.out.println("--REJECT HOLIDAYS--");
                if(displayPendingListOfHolidays()==1){
                    rejectHoliday();
                    displayAdminMenu();
                }
                else {
                    System.out.println("There are no pending holidays");
                    displayAdminMenu();
                }
            }
            else if(adminMenuOption == AdminMenu.MANAGE_USERS.getOptionNumber()){
                displayManageUsersMenu();
                int manageUserOption = readOptionNumberFromConsole();
                if (manageUserOption == ManageUsersMenu.ADD_NEW_USER.getOptionNumber()) {
                    addUser();
                    System.out.println("If you want to do another operation please enter the option number: ");
                    displayAdminMenu();
                }
                else if(manageUserOption == ManageUsersMenu.DELETE_USER.getOptionNumber()){
                    deleteUser();
                    System.out.println("If you want to do another operation please enter the option number: ");
                    displayAdminMenu();
                }
                else {
                    System.out.println("This option does not exist!");
                    displayAdminMenu();
                }
            }
            else if(adminMenuOption == AdminMenu.LOGOUT.getOptionNumber())
            {
                displayStartMenu();
                showMainScreen();
            }
            else {
                System.out.println("This option does not exist!");
                displayAdminMenu();
            }
        }
    }

    protected static void employeeMenuOptions(User loginUser) throws ParseException {
        displayEmployeeMenu();
        while(true){
            int employee_menu_option = readOptionNumberFromConsole();
            if(employee_menu_option == EmployeeMenu.EXIT.getOption_number()) System.exit(0);
            else if(employee_menu_option == EmployeeMenu.SHOW_AVAILABLE_DAYS_OFF.getOption_number())
            {
                System.out.println("Remaining days of holiday: ");
                displayUserAvailableDaysOff(loginUser.getIdUser());
                System.out.println("If you want to do another operation, please enter an option number");
                displayEmployeeMenu();
            }
            else if (employee_menu_option == EmployeeMenu.SHOW_TAKEN_HOLIDAYS.getOption_number())
            {
                System.out.println("Holidays of: " + loginUser.getName());
                displayUserHolidays(loginUser.getIdUser());
                System.out.println("If you want to do another operation, please enter an option number");
                displayEmployeeMenu();
            }
            else if (employee_menu_option == EmployeeMenu.ADD_NEW_HOLIDAY_PERIOD.getOption_number())
            {
                addHoliday(loginUser.getIdUser());
                System.out.println(loginUser.toString());
                System.out.println("If you want to do another operation, please enter an option number");
                displayEmployeeMenu();
            }
            else if(employee_menu_option == EmployeeMenu.SHOW_UPCOMING_HOLIDAYS.getOption_number()){
                System.out.println("Upcoming holidays for: " + loginUser.getName());
                if(createSession().createQuery("FROM Holiday h WHERE h.startDate >= current_date AND h.User.idUser = :loginIdUser", Holiday.class).setParameter("loginIdUser",loginUser.getIdUser()).list().size() >0){
                    Query query = createSession().createQuery(" SELECT h.idHoliday, DATE_FORMAT(h.startDate,'%d-%m-%Y'), DATE_FORMAT(h.endDate,'%d-%m-%Y'), h.status, h.holidayType FROM Holiday h WHERE h.startDate >= current_date AND h.User.idUser = :loginIdUser", Object[].class).setParameter("loginIdUser",loginUser.getIdUser());
                    List<Object[]> list = query.list();
                    for(Object[] arr : list){
                        System.out.println(Arrays.toString(arr));
                    }
                }
                else
                    System.out.println("There are no upcoming holidays");
                System.out.println("If you want to do another operation, please enter an option number");
                displayEmployeeMenu();
            }
            else if(employee_menu_option ==  EmployeeMenu.CANCEL_UPCOMING_HOLIDAYS.getOption_number()){
                System.out.println("-- CANCEL HOLIDAYS --");
                if(displayUserPendingOrAcceptedUpcomingListOfHolidays(loginUser.getIdUser())){
                    cancelHoliday(loginUser.getIdUser());
                }
                else
                    System.out.println("There are no upcoming holidays that can be canceled");
                System.out.println("If you want to do another operation, please enter an option number");
                displayEmployeeMenu();
            }
            else if(employee_menu_option == EmployeeMenu.LOGOUT.getOption_number())
            {
                displayStartMenu();
                showMainScreen();
            }
            else {
                System.out.println("This option does not exist!");
                displayEmployeeMenu();
            }
        }
    }
}