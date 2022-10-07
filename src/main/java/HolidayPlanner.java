import menues.HolidayStatus;
import menues.TypeOfUser;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class HolidayPlanner {

    public static void main(String[] args) throws ParseException {

        /*User user1 = new User();
        //user1.setIdUser(1);
        user1.setUsername("carina");
        user1.setName("nechita carina");
        user1.setPassword("parola");
        user1.setTypeOfUser(TypeOfUser.ADMIN);
        //user1.setRemainingDaysOfHoliday(20);

        User user2 = new User();
        user2.setUsername("andrada");
        user2.setName("man andrada");
        user2.setPassword("parola");
        user2.setTypeOfUser(TypeOfUser.EMPLOYEE);

        User user3 = new User();
        user3.setUsername("mihai");
        user3.setName("popescu mihai");
        user3.setPassword("parola");
        user3.setTypeOfUser(TypeOfUser.EMPLOYEE);

        Holiday holiday1 = new Holiday();
        holiday1.setStartDate(new Date(2022,8,12));
        holiday1.setEndDate(new Date(2022,8,15));
        holiday1.setStatus(HolidayStatus.PENDING);
        holiday1.setUser(user2);

        Holiday holiday2 = new Holiday();
        holiday2.setStartDate(new Date(2022,6,6));
        holiday2.setEndDate(new Date(2022,6,10));
        holiday2.setStatus(HolidayStatus.PENDING);
        holiday2.setUser(user3);

        Configuration config = new Configuration();
        config.configure();
        // local SessionFactory bean created
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(user1);
        session.save(holiday1);
        session.save(holiday2);
        List<User> users = session.createQuery("FROM User", User.class).list();
        session.getTransaction().commit();
        session.close();

        for(User user:users)
            System.out.println(user);*/
        HolidayActions.loadingUsersFromDatabase();

        System.out.println("Hello User!");

        HolidayActions.displayStartMenu();

        HolidayActions.showMainScreen();
    }
}