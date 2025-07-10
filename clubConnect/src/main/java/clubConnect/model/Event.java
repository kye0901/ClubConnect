package clubConnect.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Event {
    private int eventId;
    private int clubId;
    private String title;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private String venue;
    private Integer capacity;       // nullable => unlimited seats
    private boolean rsvpRequired;
    private List<String> tags;

    /* --- Builder -------------------------------------------------- */
    public static class Builder {
        private final Event e = new Event();
        public Builder club(int clubId)            { e.clubId = clubId; return this; }
        public Builder title(String t)             { e.title   = t;     return this; }
        public Builder desc(String d)              { e.description = d; return this; }
        public Builder when(LocalDate d, LocalTime t)
        { e.date=d; e.time=t; return this; }
        public Builder venue(String v)             { e.venue  = v;      return this; }
        public Builder capacity(Integer c)         { e.capacity = c;    return this; }
        public Builder rsvp(boolean r)             { e.rsvpRequired=r;  return this; }
        public Builder tags(List<String> t)        { e.tags = t;        return this; }
        public Event build()                       { return e; }
    }

    /* --- getters & setters --------------------------------------- */
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public int getClubId() { return clubId; }
    public void setClubId(int clubId) { this.clubId = clubId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public boolean isRsvpRequired() { return rsvpRequired; }
    public void setRsvpRequired(boolean rsvpRequired) { this.rsvpRequired = rsvpRequired; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    /* --- handy ---------------------------------------------------- */
    @Override public String toString() {
        return "#%d %s on %s %s @%s".formatted(eventId, title, date, time, venue);
    }
}
