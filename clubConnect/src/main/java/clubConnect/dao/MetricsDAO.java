package clubConnect.dao;

import clubConnect.util.DBConnection;

import java.sql.*;
import java.util.*;

/**
 * Data-access object for event_metrics table.
 */
public class MetricsDAO {

    /** Nested – keeps file legal (only one public type) */
    public static record Metrics(int eventId, int recommended, int clicks) {}

    /* ---------------- basic CRUD --------------- */

    public Metrics getMetrics(int id) {
        String q = "SELECT * FROM event_metrics WHERE eventID = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(q)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return new Metrics(id,
                        rs.getInt("recommended"),
                        rs.getInt("clicks"));
        } catch (SQLException e) { e.printStackTrace(); }
        return new Metrics(id, 0, 0);      // fallback
    }

    public void incrementRecommended(int id) {
        update("UPDATE event_metrics SET recommended = recommended + 1 WHERE eventID = ?", id);
    }

    public void incrementClicks(int id) {
        update("UPDATE event_metrics SET clicks = clicks + 1 WHERE eventID = ?", id);
    }

    /* --------------- analytics helpers ---------- */

    /** Top-5 most frequent tags across all events */
    public List<String> getTopTags() {
        String q = "SELECT tags FROM events";
        Map<String, Integer> freq = new HashMap<>();

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(q)) {

            while (rs.next()) {
                for (String t : rs.getString(1).split(",")) {
                    freq.merge(t.trim(), 1, Integer::sum);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }

        return freq.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();
    }

    /* ---------------- internal ------------------ */

    private void update(String sql, int id) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
