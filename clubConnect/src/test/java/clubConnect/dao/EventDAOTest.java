package clubConnect.dao;

import clubConnect.model.Event;
import org.junit.jupiter.api.*;
import java.time.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/** Uses the real DAO but runs against in-memory H2. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EventDAOTest {
    private final EventDAO dao = new EventDAO();

    @BeforeAll void initSchema() throws Exception {
        // Create the minimal tables H2 needs. One-time for all tests.
        try (var conn = clubConnect.util.DBConnection.getConnection();
             var st   = conn.createStatement()) {
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS events (
                  eventID INT AUTO_INCREMENT PRIMARY KEY,
                  clubID  INT,
                  title   VARCHAR(150),
                  date    DATE,
                  time    TIME,
                  venue   VARCHAR(100),
                  capacity INT,
                  tags TEXT
                );
            """);
        }
    }

    @Test void insertAndFetch() {
        Event e = new Event.Builder().club(1)
                .title("JUnit Talk").desc("TDD FTW")
                .when(LocalDate.now().plusDays(5), LocalTime.NOON)
                .venue("Lab 101").capacity(60).tags(List.of("tech")).build();

        int id = dao.insertEvent(e);
        assertNotEquals(-1, id);
        assertEquals("JUnit Talk", dao.getEventById(id).getTitle());
    }
}
