package clubConnect.dao;

import clubConnect.model.Club;
import clubConnect.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClubDAO {

    /* CREATE */
    public int insertClub(Club c){
        String sql = "INSERT INTO clubs(name,email,password,description,category,logoPath)"
                + " VALUES(?,?,?,?,?,?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,c.getName());
            ps.setString(2, c.getEmail()    != null ? c.getEmail()    : "club"+System.nanoTime()+"@tmp");
            ps.setString(3, c.getPassword() != null ? c.getPassword() : "pass");                              // placeholder pwd
            ps.setString(4,c.getDescription());
            ps.setString(5,c.getCategory());
            ps.setString(6,c.getLogoPath());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) return rs.getInt(1);
        }catch(SQLException ex){ ex.printStackTrace(); }
        return -1;
    }

    /* READ */
    public Club getClub(int clubId){
        String sql = "SELECT * FROM clubs WHERE clubID=?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,clubId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new Club(
                        rs.getInt("clubID"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("category"),
                        rs.getString("logoPath"),
                        getAdminIds(clubId)
                );
            }
        }catch(SQLException ex){ ex.printStackTrace(); }
        return null;
    }

    /* UPDATE */
    public void updateClub(Club c){
        String sql = "UPDATE clubs SET name=?,description=?,category=?,logoPath=? WHERE clubID=?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1,c.getName());
            ps.setString(2,c.getDescription());
            ps.setString(3,c.getCategory());
            ps.setString(4,c.getLogoPath());
            ps.setInt(5,c.getClubId());
            ps.executeUpdate();
        }catch(SQLException ex){ ex.printStackTrace(); }
    }

    /* — helpers — */
    private List<Integer> getAdminIds(int clubId){
        List<Integer> list = new ArrayList<>();
        String q="SELECT studentID FROM club_admins WHERE clubID=?";
        try(Connection conn=DBConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(q)){
            ps.setInt(1,clubId);
            ResultSet rs=ps.executeQuery();
            while(rs.next()) list.add(rs.getInt(1));
        }catch(SQLException ex){ex.printStackTrace();}
        return list;
    }
    public Club getByCredentials(String email,String pwd){
        String sql="SELECT * FROM clubs WHERE email=? AND password=?";
        try(Connection c=DBConnection.getConnection();
            PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,email); ps.setString(2,pwd);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                int id = rs.getInt("clubID");
                return new Club(id, rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("category"),
                        rs.getString("logoPath"),
                        getAdminIds(id));
            }
        }catch(SQLException e){e.printStackTrace();}
        return null;
    }

    /*  paste anywhere inside ClubDAO  */
    public List<Integer> getAllClubIds() {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT clubID FROM clubs";
        try (Connection c = DBConnection.getConnection();
             Statement  st = c.createStatement();
             ResultSet  rs = st.executeQuery(sql)) {

            while (rs.next()) ids.add(rs.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
        return ids;
    }


}
