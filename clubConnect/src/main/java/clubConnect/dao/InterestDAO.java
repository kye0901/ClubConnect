package clubConnect.dao;

import clubConnect.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterestDAO {

    public void addInterests(int studentId, List<String> tags) {
        String query = "INSERT INTO interests (studentID, tags) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            for (String tag : tags) {
                ps.setInt(1, studentId);
                ps.setString(2, tag);
                ps.addBatch();
            }

            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getInterests(int studentId) {
        List<String> interests = new ArrayList<>();
        String query = "SELECT tags FROM interests WHERE studentID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                interests.add(rs.getString("tags"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return interests;
    }

    public void updateStudentInterests(int studentId, List<String> newTags) {
        deleteInterests(studentId);
        addInterests(studentId, newTags);
    }

    public void deleteInterests(int studentId) {
        String query = "DELETE FROM interests WHERE studentID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, studentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
