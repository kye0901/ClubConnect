package clubConnect.dao;

import clubConnect.model.Event;
import clubConnect.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    /* CREATE using Builder */
    public int insertEvent(Event e){
        String sql="INSERT INTO events(clubID,title,date,time,venue,capacity,tags)"
                + " VALUES(?,?,?,?,?,?,?)";
        try(Connection conn=DBConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1,e.getClubId());
            ps.setString(2,e.getTitle());
            ps.setDate(3,Date.valueOf(e.getDate()));
            ps.setTime(4,Time.valueOf(e.getTime()));
            ps.setString(5,e.getVenue());
            ps.setObject(6,e.getCapacity());
            ps.setString(7,String.join(",",e.getTags()));
            ps.executeUpdate();
            ResultSet rs=ps.getGeneratedKeys();
            if(rs.next()) return rs.getInt(1);
        }catch(SQLException ex){ex.printStackTrace();}
        return -1;
    }

    /* READ ALL */
    public List<Event> getAllEvents(){
        return runQuery("SELECT * FROM events");
    }

    /* READ BY TAGS – simple LIKE %tag% match */
    public List<Event> getEventsMatchingTags(List<String> interests){
        if(interests.isEmpty()) return List.of();
        String like = String.join(" OR ", interests.stream()
                .map(t->"tags LIKE '%"+t+"%'").toList());
        return runQuery("SELECT * FROM events WHERE "+like);
    }

    /* READ ONE */
    public Event getEventById(int id){
        List<Event> l=runQuery("SELECT * FROM events WHERE eventID="+id);
        return l.isEmpty()? null : l.get(0);
    }

    // -----------------------------------------------------------------
    private List<Event> runQuery(String sql){
        List<Event> list=new ArrayList<>();
        try(Connection conn=DBConnection.getConnection();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(sql)){
            while(rs.next()){
                Event e=new Event.Builder()
                        .club(rs.getInt("clubID"))
                        .title(rs.getString("title"))
                        .desc("")                            // add desc col if needed
                        .when(rs.getDate("date").toLocalDate(),
                                rs.getTime("time").toLocalTime())
                        .venue(rs.getString("venue"))
                        .capacity((Integer)rs.getObject("capacity"))
                        .rsvp(false)
                        .tags(List.of(rs.getString("tags").split(",")))
                        .build();
                // set id via reflection or add setter
                list.add(e);
            }
        }catch(SQLException ex){ex.printStackTrace();}
        return list;
    }

    /* UPDATE existing event */
    public void updateEvent(Event e){
        String sql = """
        UPDATE events SET title=?, date=?, time=?, venue=?, capacity=?, tags=?
        WHERE eventID=?
        """;
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, e.getTitle());
            ps.setDate  (2, Date.valueOf(e.getDate()));
            ps.setTime  (3, Time.valueOf(e.getTime()));
            ps.setString(4, e.getVenue());
            ps.setObject(5, e.getCapacity());
            ps.setString(6, String.join(",", e.getTags()));
            ps.setInt   (7, e.getEventId());
            ps.executeUpdate();
        }catch(SQLException ex){ ex.printStackTrace(); }
    }

    /* DELETE (soft) */
    public void deleteEvent(int id){
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement("DELETE FROM events WHERE eventID=?")){
            ps.setInt(1, id);
            ps.executeUpdate();
        }catch(SQLException ex){ ex.printStackTrace(); }
    }

    public List<Event> getEventsByClub(int clubId){
        return runQuery("SELECT * FROM events WHERE clubID=" + clubId);
    }


}
