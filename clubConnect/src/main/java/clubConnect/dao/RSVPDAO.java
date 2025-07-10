package clubConnect.dao;

import clubConnect.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RSVPDAO {

    public void register(int studentId, int eventId) {
        String query = "INSERT INTO RSVP (studentID, eventID, status) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, studentId);
            ps.setInt(2, eventId);
            ps.setString(3, "registered");

            ps.executeUpdate();
            System.out.println("✅ Registered successfully!");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("❌ Already registered or invalid event ID.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getRegisteredEventIds(int studentId) {
        List<Integer> eventIds = new ArrayList<>();
        String query = "SELECT eventID FROM RSVP WHERE studentID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                eventIds.add(rs.getInt("eventID"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eventIds;
    }

    public void cancelRSVP(int studentId, int eventId) {
        String query = "DELETE FROM RSVP WHERE studentID = ? AND eventID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, studentId);
            ps.setInt(2, eventId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ RSVP cancelled.");
            } else {
                System.out.println("❌ You were not registered for this event.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getStudentIds(int eventId) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT studentID FROM RSVP WHERE eventID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) ids.add(rs.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
        return ids;
    }

}
