package clubConnect.model;

import java.util.ArrayList;
import java.util.List;

public class Club {
    private int clubId;
    private String name;
    private String description;
    private String category;
    private String logoPath;
    private String email;
    private String password;
    private List<Integer> adminIds = new ArrayList<>();

    /* --- ctors ---------------------------------------------------- */
    public Club() {}
    public Club(int clubId, String name, String description,
                String category, String logoPath, List<Integer> adminIds) {
        this.clubId = clubId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.logoPath = logoPath;
        this.email       = email;
        this.password    = password;
        this.adminIds = adminIds;
    }

    /* --- getters & setters --------------------------------------- */
    public int getClubId() { return clubId; }
    public void setClubId(int clubId) { this.clubId = clubId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLogoPath() { return logoPath; }
    public void setLogoPath(String logoPath) { this.logoPath = logoPath; }

    public String getEmail()                { return email; }
    public void setEmail(String email)      { this.email = email; }

    public String getPassword()             { return password; }
    public void setPassword(String pwd)     { this.password = pwd; }

    public List<Integer> getAdminIds() { return adminIds; }
    public void setAdminIds(List<Integer> adminIds) { this.adminIds = adminIds; }


    /* --- handy ---------------------------------------------------- */
    @Override public String toString() {
        return "%s (%s) – %s".formatted(name, category, description);
    }
}
