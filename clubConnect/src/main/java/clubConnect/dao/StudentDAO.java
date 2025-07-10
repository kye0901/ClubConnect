package clubConnect.dao;

import clubConnect.model.Student;
import clubConnect.util.DBConnection;

import java.sql.*;
import java.util.List;

public class StudentDAO {
    private final InterestDAO interestDAO = new InterestDAO();

    public void insertStudent(Student student) {
        String query = "INSERT INTO student (name, email, password) VALUES (?, ?, ?)";
        String interestQuery = "INSERT INTO interests (studentID, tags) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getPassword());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int studentId = rs.getInt(1);
                student.setStudentId(studentId);

                // Insert interests
                try (PreparedStatement ps2 = conn.prepareStatement(interestQuery)) {
                    for (String interest : student.getInterests()) {
                        ps2.setInt(1, studentId);
                        ps2.setString(2, interest);
                        ps2.addBatch();
                    }
                    ps2.executeBatch();
                }
            }

            System.out.println("✅ Student registered successfully!");

        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("email")) {
                System.out.println("❌ Email already registered. Try logging in or use a different email.");
            } else {
                System.out.println("❌ Registration failed due to constraint violation.");
            }
        } catch (SQLException e) {
            System.out.println("❌ An unexpected database error occurred.");
            e.printStackTrace();
        }
    }

    public Student getStudentByEmail(String email) {
        String studentQuery = "SELECT * FROM student WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(studentQuery)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("studentID");
                String name = rs.getString("name");
                String password = rs.getString("password");

                List<String> interests = interestDAO.getInterests(id);

                return new Student(id, name, email, password, interests);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean validateLogin(String email, String password) {
        String query = "SELECT * FROM student WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
