import menues.HolidayStatus;
import menues.HolidayType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Holiday{
    private Date startDate;
    private Date endDate;
    private HolidayStatus status;
    private static AtomicInteger count = new AtomicInteger(0);
    private int idHoliday;
    private User user;

    private HolidayType holidayType;

    public Holiday(){}
    Holiday(Date start_date, Date end_date, HolidayStatus status, HolidayType type){
        this.startDate = start_date;
        this.endDate = end_date;
        this.status = status;
        this.holidayType = type;
        idHoliday = count.incrementAndGet();
    }

    public Holiday(Date dateStart, Date dateEnd, HolidayStatus pending, User user, HolidayType type) {
        this.startDate = dateStart;
        this.endDate = dateEnd;
        this.status = HolidayStatus.PENDING;
        this.user = user;
        this.holidayType = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getIdHoliday() {
        return idHoliday;
    }

    public HolidayStatus getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setIdHoliday(int idHoliday) {
        this.idHoliday = idHoliday;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStatus(HolidayStatus status) {
        this.status = status;
    }

    public HolidayType getHolidayType() {
        return holidayType;
    }

    public void setHolidayType(HolidayType holidayType) {
        this.holidayType = holidayType;
    }

    @Override
    public String toString() {
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String startDateFormat = simpleDateFormat.format(startDate);
        String endDateFormat = simpleDateFormat.format(endDate);
        return
                idHoliday + "," +
                startDateFormat +
                "," + endDateFormat +
                " " + status;
    }
}
