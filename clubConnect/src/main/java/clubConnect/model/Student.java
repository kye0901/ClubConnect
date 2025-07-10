package clubConnect.model;
import java.util.ArrayList;
import java.util.List;

public class Student {
    private int studentId;
    private String name;
    private String email;
    private String password;
    private List<String> interests;

    public Student(String name, String email, String password, List<String> interests) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.interests = interests;
    }

    public Student(int studentId, String name, String email, String password, List<String> interests) {
        this(name, email, password, interests);
        this.studentId = studentId;
    }

    public Student() {

    }
    public Student(String name, String email, String password) {
        this(name, email, password, new ArrayList<>());   // interests = empty
    }

    /** NEW – nullable ID wrapper to allow “unknown” (null)  */
    public Student(Integer studentId, String name, String email, String password) {
        this(name, email, password, new ArrayList<>());
        this.studentId = (studentId == null ? 0 : studentId);
    }

    //GETERS
    public int getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public List<String> getInterests() { return interests; }

    //SETTERS
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setInterests(List<String> interests) { this.interests = interests; }


    @Override
    public String toString() {
        return name + " (" + email + ") - Interests: " + interests;
    }
}
