package clubConnect;

import clubConnect.dao.*;
import clubConnect.model.*;
import clubConnect.service.ClubService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class clubConnectCLI {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentDAO studentDAO = new StudentDAO();
    private static final Scanner     sc       = new Scanner(System.in);
    private static final ClubService clubSvc  = new ClubService();

    public static void main(String[] args) {
        while (true) {
            System.out.println("===== Club Connect =====");
            System.out.println("1. Student Login");
            System.out.println("2. New Student Signup");
            System.out.println("3. Club Admin Login");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            String line = sc.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a number (0-3)");
                continue;
            }

            switch (choice) {
                case 1 -> loginStudent();
                case 2 -> registerStudent();      // your existing method
                case 3 -> loginClubAdmin();        // admin flow we just added
                case 0 -> {
                    System.out.println("Bye!");    // graceful shutdown
                    return;                        // <— the ONLY normal exit
                }
                default -> System.out.println("Pick between 0 and 3");
            }
        }
    }

    private static void registerStudent() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter interests (comma-separated): ");
        String[] interestArray = scanner.nextLine().split(",");
        List<String> interests = new ArrayList<>();
        for (String interest : interestArray) {
            interests.add(interest.trim());
        }

        Student student = new Student(name, email, password, interests);
        studentDAO.insertStudent(student);
    }

    private static void loginStudent() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        boolean valid = studentDAO.validateLogin(email, password);
        if (valid) {
            Student student = studentDAO.getStudentByEmail(email);
            System.out.println("\n--- Welcome, " + student.getName() + " ---");

            while (true) {
                System.out.println("\n1. View Profile");
                System.out.println("2. Update Interests");
                System.out.println("3. View All Events");
                System.out.println("4. View Matching Events");
                System.out.println("5. Register for an Event");
                System.out.println("6. View Registered Events");
                System.out.println("7. Cancel RSVP");
                System.out.println("8. Logout");
                System.out.print("Choose option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> System.out.println(student);
                    case 2 -> updateInterests(student);
                    case 3 -> viewAllEvents();
                    case 4 -> viewMatchingEvents(student);
                    case 5 -> registerForEvent(student);
                    case 6 -> viewRegisteredEvents(student);
                    case 7 -> cancelRSVP(student);
                    case 8 -> { return; }
                    default -> System.out.println("Invalid option.");
                }

            }

        } else {
            System.out.println("❌ Invalid credentials.");
        }
    }

    private static void updateInterests(Student student) {
        System.out.print("Enter new interests (comma-separated): ");
        String[] newInterests = scanner.nextLine().split(",");
        List<String> interestList = new ArrayList<>();
        for (String interest : newInterests) interestList.add(interest.trim());

        InterestDAO interestDAO = new InterestDAO();
        interestDAO.updateStudentInterests(student.getStudentId(), interestList);

        student.setInterests(interestList);
        System.out.println("✅ Interests updated!");
    }

    private static void viewAllEvents() {
        EventDAO eventDAO = new EventDAO();
        List<Event> events = eventDAO.getAllEvents();

        if (events.isEmpty()) {
            System.out.println("No events found.");
        } else {
            for (Event e : events) System.out.println(e);
        }
    }

    private static void viewMatchingEvents(Student student) {
        EventDAO eventDAO = new EventDAO();
        List<Event> events = eventDAO.getEventsMatchingTags(student.getInterests());

        if (events.isEmpty()) {
            System.out.println("No matching events found.");
        } else {
            System.out.println("--- Events Matching Your Interests ---");
            for (Event e : events) System.out.println(e);
        }
    }

    private static void registerForEvent(Student student) {
        System.out.print("Enter Event ID to register: ");
        int eventId = scanner.nextInt();
        scanner.nextLine();

        RSVPDAO rsvpDAO = new RSVPDAO();
        rsvpDAO.register(student.getStudentId(), eventId);
    }

    private static void viewRegisteredEvents(Student student) {
        RSVPDAO rsvpDAO = new RSVPDAO();
        EventDAO eventDAO = new EventDAO(); // Assume this exists

        List<Integer> eventIds = rsvpDAO.getRegisteredEventIds(student.getStudentId());

        if (eventIds.isEmpty()) {
            System.out.println("📭 You have not registered for any events.");
            return;
        }

        System.out.println("\n--- Your Registered Events ---");
        for (int i = 0; i < eventIds.size(); i++) {
            int eventId = eventIds.get(i);
            Event event = eventDAO.getEventById(eventId);
            System.out.println((i + 1) + ". " + event);
        }
    }

    private static void cancelRSVP(Student student) {
        RSVPDAO rsvpDAO = new RSVPDAO();
        EventDAO eventDAO = new EventDAO(); // Assume this exists

        List<Integer> eventIds = rsvpDAO.getRegisteredEventIds(student.getStudentId());

        if (eventIds.isEmpty()) {
            System.out.println("❌ No registrations to cancel.");
            return;
        }

        System.out.println("\n--- Your Registered Events ---");
        for (int i = 0; i < eventIds.size(); i++) {
            Event event = eventDAO.getEventById(eventIds.get(i));
            System.out.println((i + 1) + ". " + event);
        }

        System.out.print("Enter the number of the event to cancel (or 0 to cancel): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > eventIds.size()) {
            System.out.println("❌ Invalid choice.");
            return;
        }

        int eventIdToCancel = eventIds.get(choice - 1);
        rsvpDAO.cancelRSVP(student.getStudentId(), eventIdToCancel);
    }



    /* ---------- CLUB-ADMIN FLOW ---------- */
    private static void loginClubAdmin() {
        System.out.print("Club e-mail : ");
        String email = sc.nextLine();
        System.out.print("Password    : ");
        String pwd   = sc.nextLine();

        Club club = clubSvc.getProfileByEmailAndPwd(email, pwd);   // now exists
        if (club == null) {
            System.out.println("✖ Invalid credentials");
            return;
        }
        clubDashboard(club);
    }

    private static void clubDashboard(Club club) {
        while (true) {
            System.out.println("\n=== " + club.getName() + " Admin ===");
            System.out.println("1. View / Edit profile");
            System.out.println("2. Create event");
            System.out.println("3. Edit or cancel event");
            System.out.println("4. View analytics");
            System.out.println("5. Download RSVP list");
            System.out.println("6. Send reminders");
            System.out.println("0. Logout");

            switch (Integer.parseInt(sc.nextLine())) {
                case 1 -> editClubProfile(club);
                case 2 -> createEventFlow(club.getClubId());
                case 3 -> editOrCancelEventFlow(club.getClubId());
                case 4 -> analyticsFlow(club.getClubId());
                case 5 -> downloadRsvpFlow(club.getClubId());
                case 6 -> sendReminderFlow(club.getClubId());
                case 0 -> { return; }
                default -> System.out.println("Pick 0-6");
            }
        }
    }

    /* ---------- helper screens (minimal but compilable) ---------- */

    private static void editClubProfile(Club c) {
        System.out.println("Current description: " + c.getDescription());
        System.out.print("New description (blank = keep): ");
        String d = sc.nextLine();
        if (!d.isBlank()) c.setDescription(d);

        clubSvc.updateProfile(c);
        System.out.println("✓ Profile updated");
    }

    private static void createEventFlow(int clubId) {
        Event.Builder b = new Event.Builder().club(clubId);

        System.out.print("Title       : "); b.title(sc.nextLine());
        System.out.print("Description : "); b.desc(sc.nextLine());
        System.out.print("Date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(sc.nextLine());
        System.out.print("Time (HH:MM)    : ");
        LocalTime time = LocalTime.parse(sc.nextLine());
        b.when(date, time);
        System.out.print("Venue       : "); b.venue(sc.nextLine());
        System.out.print("Capacity (0 = unlimited): ");
        int cap = Integer.parseInt(sc.nextLine());
        b.capacity(cap == 0 ? null : cap);
        System.out.print("Tags comma-sep : ");
        b.tags(List.of(sc.nextLine().split(",")));

        int id = clubSvc.createEvent(b.build());
        System.out.println("✓ Event created with ID " + id);
    }

    private static void editOrCancelEventFlow(int clubId) {
        System.out.print("Event ID to edit/cancel: ");
        int id = Integer.parseInt(sc.nextLine());
        Event ev = clubSvc.getEventById(id);

        if (ev == null || ev.getClubId() != clubId) {
            System.out.println("✖ Event not found or not your club");
            return;
        }
        System.out.println(ev);
        System.out.print("1) Edit   2) Cancel : ");
        int ch = Integer.parseInt(sc.nextLine());
        if (ch == 2) {
            clubSvc.cancelEvent(id);
            System.out.println("✓ Event cancelled");
        } else {
            System.out.print("New title: ");
            ev.setTitle(sc.nextLine());
            clubSvc.editEvent(ev);
            System.out.println("✓ Event updated");
        }
    }

    private static void analyticsFlow(int clubId) {
        for (Event e : clubSvc.getEventsByClub(clubId)) {
            double ctr = clubSvc.getCTR(e.getEventId());
            System.out.printf("#%d %-25s  CTR: %.2f%n", e.getEventId(), e.getTitle(), ctr);
        }
    }

    private static void downloadRsvpFlow(int clubId) {
        System.out.print("Event ID: ");
        int id = Integer.parseInt(sc.nextLine());
        clubSvc.getRsvpList(id).forEach(System.out::println);
    }

    private static void sendReminderFlow(int clubId) {
        System.out.print("Event ID: ");
        int id = Integer.parseInt(sc.nextLine());
        clubSvc.sendReminder(id);
        System.out.println("✓ Reminders queued");
    }




}
