package clubConnect.service;
import clubConnect.dao.MetricsDAO.Metrics;   // ADD THIS
import clubConnect.dao.*;
import clubConnect.model.Club;
import clubConnect.model.Event;

import java.time.LocalDate;
import java.util.List;

public class ClubService {
    private final ClubDAO clubDAO       = new ClubDAO();
    private final EventDAO eventDAO     = new EventDAO();
    private final MetricsDAO metricsDAO = new MetricsDAO();
    private final RSVPDAO rsvpDAO       = new RSVPDAO();
    private final FeedbackDAO fbDAO     = new FeedbackDAO();

    /* -------- Club profile -------- */
    public Club  getProfile(int clubId)            { return clubDAO.getClub(clubId); }
    public void  updateProfile(Club c)             { clubDAO.updateClub(c); }

    /* -------- Event life-cycle ---- */
    public int   createEvent(Event e)              { validate(e); return eventDAO.insertEvent(e); }
    public void  editEvent(Event e)                { validate(e); eventDAO.updateEvent(e); }
    public void  cancelEvent(int eventId)          { eventDAO.deleteEvent(eventId); }

    /* -------- Analytics ---------- */
    public double getCTR(int eventId)              {
        Metrics m = metricsDAO.getMetrics(eventId);
        return m.recommended()==0 ? 0 : (double)m.clicks()/m.recommended();
    }
    public List<String> topTags()                  { return metricsDAO.getTopTags(); }

    /* -------- Internal rule ------ */
    private void validate(Event e){
        if(e.getDate().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Event date must be in the future");
    }

    public Club getProfileByEmailAndPwd(String email, String pwd) {
        return clubDAO.getByCredentials(email, pwd);
    }

    public Event getEventById(int id)                { return eventDAO.getEventById(id); }
    public List<Event> getEventsByClub(int clubId)   { return eventDAO.getEventsByClub(clubId); }

    public List<Integer> getRsvpList(int eventId)    { return rsvpDAO.getStudentIds(eventId); }
    public void sendReminder(int eventId)            {
        // TODO integrate real e-mail/SMS; for now just log
        System.out.println("[DEBUG] would send reminders for event " + eventId);
    }

}

