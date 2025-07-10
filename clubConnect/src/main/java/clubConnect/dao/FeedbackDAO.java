package clubConnect.dao;

import clubConnect.util.DBConnection;

import java.sql.*;

public class FeedbackDAO {
    public void addFeedback(int eventId,int studentId,int rating,String comment){
        String sql="INSERT INTO feedback VALUES(?,?,?,?)";
        try(Connection c= DBConnection.getConnection();
            PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,eventId); ps.setInt(2,studentId);
            ps.setInt(3,rating);  ps.setString(4,comment);
            ps.executeUpdate();
        }catch(SQLException e){e.printStackTrace();}
    }
    public double avgRating(int eventId){
        String q="SELECT AVG(rating) FROM feedback WHERE eventID="+eventId;
        try(Connection c=DBConnection.getConnection();
            Statement st=c.createStatement(); ResultSet rs=st.executeQuery(q)){
            return rs.next()? rs.getDouble(1):0;
        }catch(SQLException e){e.printStackTrace();}
        return 0;
    }
}

