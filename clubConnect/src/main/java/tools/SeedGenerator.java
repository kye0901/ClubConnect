package tools;

import clubConnect.dao.ClubDAO;
import clubConnect.dao.EventDAO;
import clubConnect.dao.StudentDAO;
import clubConnect.model.Club;
import clubConnect.model.Event;
import clubConnect.model.Student;
import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.IntStream;

public class SeedGenerator {
    private static final int CLUBS = 30, STUDENTS = 5000, EVENTS = 800;
    private static final Faker faker = new Faker();

    public static void main(String[] args) {
        ClubDAO clubDAO = new ClubDAO();
        StudentDAO stuDAO = new StudentDAO();
        EventDAO evtDAO = new EventDAO();

        /* 1) 30 clubs ------------------------------------------------ */
        IntStream.rangeClosed(1, CLUBS).forEach(i -> {
            Club c = new Club();                           // already in your loop
            c.setName(faker.university().name());
            c.setDescription(faker.lorem().sentence());
            c.setCategory(faker.book().genre());
            c.setLogoPath("logo" + i + ".png");

            /* ――― add these two lines ――― */
            c.setEmail("club" + i + "@campus.edu");
            c.setPassword("pw"   + i);

            int id = clubDAO.insertClub(c);                // keep this line
            c.setClubId(id);
            // keep this line


        });

        /* 2) 5 000 students ----------------------------------------- */
        IntStream.rangeClosed(1, STUDENTS).forEach(i -> {
            Student s = new Student(
                    faker.name().fullName(),
                    "user" + i + "@campus.edu",
                    "pw" + i);                  // ← works
            stuDAO.insertStudent(s);
        });

        /* 3) 800 events (≈25/club) ---------------------------------- */
        List<Integer> clubIds = clubDAO.getAllClubIds();
        IntStream.rangeClosed(1, EVENTS).forEach(i -> {
            int clubId = clubIds.get(faker.random().nextInt(clubIds.size()));
            Event e = new Event.Builder()
                    .club(clubId)
                    .title(faker.book().title())
                    .desc(faker.lorem().paragraph())
                    .when(LocalDate.now().plusDays(faker.number().numberBetween(1, 120)),
                            LocalTime.of(faker.number().numberBetween(9, 19), 0))
                    .venue(faker.company().name() + " Hall")
                    .capacity(faker.number().numberBetween(50, 500))
                    .tags(List.of("tech","arts","sports","social")
                            .subList(0, faker.number().numberBetween(1,3)))
                    .build();
            evtDAO.insertEvent(e);
        });

        System.out.println("✔ Seed completed!");
    }
}
