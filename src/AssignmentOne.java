import java.util.ArrayList;
import java.util.Collections;

/**
 * Assessment 1 - appointment system for a health service.
 *
 * <p>This class contains the {@code main} method that demonstrates the whole model: the health
 * professionals from Parts 1-3 and the appointments from Parts 4-6. No data is saved and there is
 * no user interface; as the brief asks, the {@code main} method creates objects and exercises their
 * behaviour in a short scripted simulation that prints its results.</p>
 *
 * <p>The classes in this project are:</p>
 * <ul>
 *   <li>{@link HealthProfessional} - the abstract shared type for every health professional
 *       (Part 1).</li>
 *   <li>{@link GeneralPractitioner}, {@link Surgeon}, {@link Nurse}, {@link Dietitian} and
 *       {@link Physiotherapist} - five specific professionals that inherit from the shared type
 *       (Part 2).</li>
 *   <li>{@link Patient} - the patient details an appointment refers to (Part 4).</li>
 *   <li>{@link TimeSlot} and {@link ClinicSchedule} - how a time is stored and how the clinic's
 *       fixed daily schedule is represented (Parts 4-5).</li>
 *   <li>{@link Appointment} - one booking that links a patient to any health professional
 *       (Part 4).</li>
 *   <li>{@link AppointmentManager} - the collection of appointments and the operations on it
 *       (Part 5).</li>
 * </ul>
 *
 * @author Li Yuje
 * @version 1.0
 */
public class AssignmentOne {

    /** A wide separator used between the main sections of the output. */
    private static final String WIDE_LINE =
            "========================================================================";

    /** A narrow separator used inside a section. */
    private static final String THIN_LINE =
            "----------------------------------------------------------------------";

    /**
     * Runs the whole demonstration.
     *
     * @param args command line arguments, which are not used
     */
    public static void main(String[] args) {
        Patient weiChen = new Patient("Wei Chen", "0412345678");
        Patient mariaLopez = new Patient("Maria Lopez", "0423456789");
        Patient samPatel = new Patient("Sam Patel", "0434567890");
        Patient aikoTanaka = new Patient("Aiko Tanaka", "0445678901");

        ArrayList<HealthProfessional> professionals = createProfessionals();

        printProgramHeader(professionals, 4);
        demonstrateValidationRules(professionals);
        demonstrateHealthProfessionals(professionals, weiChen);
        demonstrateSingleAppointment(professionals, weiChen);
        demonstrateAppointmentSystem(professionals, weiChen, mariaLopez, samPatel, aikoTanaka);
        printProgramFooter();
    }

    /**
     * Creates the health professionals used by the demonstration.
     *
     * <p>Every object is added to one {@code ArrayList<HealthProfessional>}, so the different types
     * are stored together and can be processed through the shared abstract type.</p>
     *
     * @return the professionals, in the order they were created
     */
    private static ArrayList<HealthProfessional> createProfessionals() {
        ArrayList<Day> mondayWednesdayFriday = new ArrayList<Day>();
        mondayWednesdayFriday.add(Day.MONDAY);
        mondayWednesdayFriday.add(Day.WEDNESDAY);
        mondayWednesdayFriday.add(Day.FRIDAY);

        ArrayList<Day> mondayToThursday = new ArrayList<Day>();
        mondayToThursday.add(Day.MONDAY);
        mondayToThursday.add(Day.TUESDAY);
        mondayToThursday.add(Day.WEDNESDAY);
        mondayToThursday.add(Day.THURSDAY);

        ArrayList<Day> mondayAndTuesday = new ArrayList<Day>();
        mondayAndTuesday.add(Day.MONDAY);
        mondayAndTuesday.add(Day.TUESDAY);

        ArrayList<Day> mondayTuesdayThursday = new ArrayList<Day>();
        mondayTuesdayThursday.add(Day.MONDAY);
        mondayTuesdayThursday.add(Day.TUESDAY);
        mondayTuesdayThursday.add(Day.THURSDAY);

        ArrayList<Day> tuesdayToSaturday = new ArrayList<Day>();
        tuesdayToSaturday.add(Day.TUESDAY);
        tuesdayToSaturday.add(Day.WEDNESDAY);
        tuesdayToSaturday.add(Day.THURSDAY);
        tuesdayToSaturday.add(Day.FRIDAY);
        tuesdayToSaturday.add(Day.SATURDAY);

        ArrayList<HealthProfessional> professionals = new ArrayList<HealthProfessional>();

        // Three general practitioners. The third uses the constructor that leaves out the days,
        // so it inherits the standard Monday to Friday week.
        professionals.add(new GeneralPractitioner("1001", "Dr Amina Haddad", mondayWednesdayFriday,
                "Room 3", true));
        professionals.add(new GeneralPractitioner("1002", "Dr Ben Okafor", mondayToThursday,
                "Room 5", false));
        professionals.add(new GeneralPractitioner("1003", "Dr Claire Nguyen", mondayAndTuesday,
                "Room 1"));

        // Two surgeons; the second uses the constructor that leaves out the working days.
        professionals.add(new Surgeon("2001", "Dr Dmitri Volkov", mondayWednesdayFriday,
                "Orthopaedic surgery", 2));
        professionals.add(new Surgeon("2002", "Dr Elena Rossi", "Cardiothoracic surgery", 1));

        // Two nurses; the second works Tuesday to Saturday, so is unavailable on a Monday clinic.
        professionals.add(new Nurse("3001", "Grace Mbeki", "Ward 4B", "Morning"));
        professionals.add(new Nurse("3002", "Hassan Ali", tuesdayToSaturday, "Ward 2A",
                "Evening"));

        // A dietitian and a physiotherapist.
        professionals.add(new Dietitian("4001", "Irene Costa", mondayTuesdayThursday,
                "Diabetes management", 45));
        professionals.add(new Physiotherapist("5001", "Jack Wilson", mondayWednesdayFriday,
                "Sports injuries", 5));

        return professionals;
    }

    /**
     * Prints the title of the program and the objects the demonstration works with.
     *
     * @param professionals the health professionals that were created
     * @param patientCount  the number of patients that were created
     */
    private static void printProgramHeader(ArrayList<HealthProfessional> professionals,
            int patientCount) {
        System.out.println(WIDE_LINE);
        System.out.println(" PROG2004 Object Oriented Programming - Assessment 1");
        System.out.println(" Appointment system for a health service");
        System.out.println(" Li Yuje");
        System.out.println(WIDE_LINE);
        System.out.println();
        System.out.println(" This program builds the object model for an appointment system and");
        System.out.println(" demonstrates it. There is no user input and no data is saved: the main");
        System.out.println(" method plays the part of the users and prints what happens.");
        System.out.println();
        System.out.println(" " + professionals.size()
                + " health professionals of 5 different types and " + patientCount
                + " patients have been created.");
    }

    /**
     * Shows that object details are validated, so an object can never be created in an invalid
     * state, and that the internal data cannot be changed from outside.
     *
     * @param professionals the professionals, used to show an incomplete appointment being rejected
     */
    private static void demonstrateValidationRules(ArrayList<HealthProfessional> professionals) {
        printSection("PART 1 & 2 - VALIDATION AND ENCAPSULATION", "Invalid objects must be impossible");

        System.out.println("  Each constructor checks its arguments and refuses to create a broken");
        System.out.println("  object. The attempts below are all rejected:");
        System.out.println();

        try {
            new GeneralPractitioner("GP-1001", "Dr Nobody", "Room 9");
            System.out.println("    NOT rejected (unexpected)");
        } catch (IllegalArgumentException e) {
            System.out.println("    - identifier with letters  -> " + e.getMessage());
        }
        try {
            new Nurse("3009", "  ", "Ward 1", "Morning");
            System.out.println("    NOT rejected (unexpected)");
        } catch (IllegalArgumentException e) {
            System.out.println("    - blank name               -> " + e.getMessage());
        }
        try {
            new Nurse("3010", "Sam Lee", "Ward 1", "Afternoon");
            System.out.println("    NOT rejected (unexpected)");
        } catch (IllegalArgumentException e) {
            System.out.println("    - unknown shift            -> " + e.getMessage());
        }
        try {
            new TimeSlot(9, 75);
            System.out.println("    NOT rejected (unexpected)");
        } catch (IllegalArgumentException e) {
            System.out.println("    - impossible time          -> " + e.getMessage());
        }
        try {
            new Patient("Li Yuje", "0412-ABC");
            System.out.println("    NOT rejected (unexpected)");
        } catch (IllegalArgumentException e) {
            System.out.println("    - invalid phone number     -> " + e.getMessage());
        }
        try {
            new Appointment(null, professionals.get(0), new TimeSlot(9, 0));
            System.out.println("    NOT rejected (unexpected)");
        } catch (IllegalArgumentException e) {
            System.out.println("    - appointment without a patient -> " + e.getMessage());
        }

        System.out.println();
        System.out.println("  The data itself is private and has no setters, so it cannot be changed");
        System.out.println("  after creation. getAvailableDays() returns a copy, so this cannot corrupt it:");
        HealthProfessional professional = professionals.get(0);
        ArrayList<Day> daysHandedOut = professional.getAvailableDays();
        daysHandedOut.clear();
        System.out.println("    - days after clearing the returned copy -> "
                + professional.getAvailableDaysText() + " (unchanged)");
    }

    /**
     * Demonstrates Part 3: creating and working with a mix of health professional objects together.
     *
     * @param professionals the professionals to work with
     * @param patient       a patient used when exercising specialty behaviour
     */
    private static void demonstrateHealthProfessionals(ArrayList<HealthProfessional> professionals,
            Patient patient) {
        printSection("PART 3 - WORKING WITH HEALTH PROFESSIONAL OBJECTS",
                "One collection, one loop, many types");

        printSubHeading("1. The full details of every professional, printed through the shared type");
        for (HealthProfessional professional : professionals) {
            System.out.println(THIN_LINE);
            System.out.println(professional);
        }
        System.out.println(THIN_LINE);

        printSubHeading("2. The same collection sorted by name (the shared type implements Comparable)");
        ArrayList<HealthProfessional> sorted = new ArrayList<HealthProfessional>(professionals);
        Collections.sort(sorted);
        for (HealthProfessional professional : sorted) {
            System.out.println("    " + professional.getName() + " - " + professional.getRole()
                    + " (ID " + professional.getId() + "), works "
                    + professional.getAvailableDaysText());
        }

        printSubHeading("3. One method call, a different answer from each type (polymorphism)");
        for (HealthProfessional professional : professionals) {
            System.out.println("    - " + professional.performDuty());
        }
        System.out.println();
        System.out.println("  Adding a sixth type of professional would need one new class only: this");
        System.out.println("  loop and every other loop over the shared type would stay unchanged.");

        printSubHeading("4. Behaviour that belongs to one specialty only");
        GeneralPractitioner generalPractitioner =
                (GeneralPractitioner) findProfessional(professionals, "1001");
        Surgeon surgeon = (Surgeon) findProfessional(professionals, "2001");
        Nurse nurse = (Nurse) findProfessional(professionals, "3001");
        Dietitian dietitian = (Dietitian) findProfessional(professionals, "4001");
        Physiotherapist physiotherapist = (Physiotherapist) findProfessional(professionals, "5001");

        System.out.println("    - " + generalPractitioner.prescribeMedication(patient, "amoxicillin",
                "500 mg three times a day"));
        System.out.println("    - " + surgeon.performProcedure(patient, "knee reconstruction"));
        System.out.println("    - " + nurse.takeBloodSample(patient));
        System.out.println("    - " + dietitian.createMealPlan(patient, 8500));
        System.out.println("    - " + physiotherapist.runRehabilitationSession(patient,
                "knee extension"));

        printSubHeading("5. Identity: equals() and hashCode() compare identifiers, not references");
        HealthProfessional samePersonDifferentObject =
                new GeneralPractitioner("1001", "Dr Amina Haddad", "Room 3");
        HealthProfessional differentPerson = findProfessional(professionals, "1002");
        System.out.println("    - gp.equals(same ID held in another object) -> "
                + generalPractitioner.equals(samePersonDifferentObject));
        System.out.println("    - their hash codes are equal                -> "
                + (generalPractitioner.hashCode() == samePersonDifferentObject.hashCode()));
        System.out.println("    - gp.equals(a different professional)       -> "
                + generalPractitioner.equals(differentPerson));
        System.out.println();
        System.out.println("  This is what lets the appointment system decide whether two appointments are");
        System.out.println("  for the same health professional.");
    }

    /**
     * Demonstrates Part 4: a single appointment that links a patient to any type of professional.
     *
     * @param professionals the professionals to choose from
     * @param patient       the patient booking the appointment
     */
    private static void demonstrateSingleAppointment(ArrayList<HealthProfessional> professionals,
            Patient patient) {
        printSection("PART 4 - MODELLING AN APPOINTMENT", "A patient, any professional, one time");

        HealthProfessional generalPractitioner = findProfessional(professionals, "1001");
        HealthProfessional surgeon = findProfessional(professionals, "2001");

        Appointment appointment = new Appointment(patient, generalPractitioner, new TimeSlot(9, 0));
        System.out.println("  An appointment printed with System.out.println(appointment):");
        System.out.println("  " + appointment);
        System.out.println();
        System.out.println("  The appointment holds a HealthProfessional reference, so the very same class");
        System.out.println("  also accepts a surgeon, a nurse and any type added in the future:");
        Appointment withSurgeon = new Appointment(patient, surgeon, new TimeSlot(11, 30));
        System.out.println("  " + withSurgeon);

        printSubHeading("Ordering by time (Appointment implements Comparable)");
        Appointment earlier = new Appointment(patient, surgeon, new TimeSlot(8, 30));
        System.out.println("    - 08:30 compared with 09:00 -> " + earlier.compareTo(appointment)
                + " (negative, so the 08:30 appointment sorts first)");
        System.out.println("    - 09:00 with GP 1001 compared with 09:00 with surgeon 2001 -> "
                + appointment.compareTo(new Appointment(patient, surgeon, new TimeSlot(9, 0)))
                + " (the same time, so the professional ID breaks the tie and sorting stays"
                + " consistent)");
    }

    /**
     * Demonstrates Parts 5 and 6: managing the collection of appointments end to end, including the
     * operations that must be rejected.
     *
     * @param professionals the professionals the appointments are booked with
     * @param weiChen       a patient
     * @param mariaLopez    another patient
     * @param samPatel      another patient
     * @param aikoTanaka    another patient
     */
    private static void demonstrateAppointmentSystem(ArrayList<HealthProfessional> professionals,
            Patient weiChen, Patient mariaLopez, Patient samPatel, Patient aikoTanaka) {
        printSection("PART 5 & 6 - MANAGING AND WORKING WITH APPOINTMENTS",
                "Add, view, filter, sort and cancel");

        // Bookings are for the current day only. The clinic is running on a Monday.
        Day clinicDay = Day.MONDAY;
        AppointmentManager manager = new AppointmentManager(clinicDay);

        HealthProfessional generalPractitionerA = findProfessional(professionals, "1001");
        HealthProfessional generalPractitionerB = findProfessional(professionals, "1002");
        HealthProfessional surgeon = findProfessional(professionals, "2001");
        HealthProfessional nurse = findProfessional(professionals, "3001");
        HealthProfessional nurseWhoDoesNotWorkMonday = findProfessional(professionals, "3002");
        HealthProfessional dietitian = findProfessional(professionals, "4001");
        HealthProfessional physiotherapist = findProfessional(professionals, "5001");

        System.out.println("  " + manager.describeClinicDay());

        printSubHeading("A. Adding appointments across the different types of professional");
        System.out.println("    " + manager.addAppointment(weiChen, generalPractitionerA,
                new TimeSlot(9, 0)));
        System.out.println("    " + manager.addAppointment(mariaLopez, surgeon, new TimeSlot(9, 30)));
        System.out.println("    " + manager.addAppointment(samPatel, nurse, new TimeSlot(10, 0)));
        System.out.println("    " + manager.addAppointment(weiChen, dietitian, new TimeSlot(8, 0)));
        System.out.println("    " + manager.addAppointment(aikoTanaka, generalPractitionerB,
                new TimeSlot(11, 30)));
        System.out.println("    " + manager.addAppointment(mariaLopez, physiotherapist,
                new TimeSlot(14, 0)));

        printSubHeading("B. Operations that must be rejected, each answered with a warning");
        System.out.println("    " + manager.addAppointment(samPatel, generalPractitionerA,
                new TimeSlot(9, 0)));
        System.out.println("    " + manager.addAppointment(weiChen, generalPractitionerA,
                new TimeSlot(9, 15)));
        System.out.println("    " + manager.addAppointment(samPatel, nurseWhoDoesNotWorkMonday,
                new TimeSlot(10, 30)));
        System.out.println("    " + manager.addAppointment(null, generalPractitionerA,
                new TimeSlot(12, 0)));
        System.out.println("    " + manager.addAppointment(weiChen, generalPractitionerA, null));

        printSubHeading("C. Every appointment on the day, earliest first");
        printAppointments(manager.getAllAppointments());

        printSubHeading("D. Filtering by health professional ID (2001, then an unused ID)");
        printAppointments(manager.getAppointmentsForProfessional("2001"));
        System.out.println("    A filter for an ID that has no bookings returns an empty list, not an");
        System.out.println("    error:");
        printAppointments(manager.getAppointmentsForProfessional("9999"));

        printSubHeading("E. Filtering by patient mobile phone (Wei Chen, 0412345678)");
        ArrayList<Appointment> forWeiChen = manager.getAppointmentsForPatient("0412345678");
        printAppointments(forWeiChen);

        printSubHeading("F. Sorting a filtered subset (the same appointments, reversed, then sorted)");
        ArrayList<Appointment> reversed = new ArrayList<Appointment>(forWeiChen);
        Collections.reverse(reversed);
        System.out.println("    Before sorting:");
        printAppointments(reversed);
        System.out.println("    After manager.sortByTime(...):");
        printAppointments(manager.sortByTime(reversed));

        printSubHeading("G. Cancelling by health professional ID and appointment time");
        System.out.println("    " + manager.cancelAppointment("2001", "09:30"));
        System.out.println("    " + manager.cancelAppointment("2001", "09:30")
                + "   <- the same appointment a second time");
        System.out.println("    " + manager.cancelAppointment("2001", "half past nine")
                + "   <- the time is not HH:mm text");

        printSubHeading("H. The day after the cancellation");
        printAppointments(manager.getAllAppointments());

        printSubHeading("I. Cancelling by patient mobile phone (Maria Lopez, 0423456789)");
        System.out.println("    " + manager.cancelAppointmentsForPatient("0423456789"));
        System.out.println("    " + manager.cancelAppointmentsForPatient("0423456789")
                + "   <- nothing left to cancel");

        printSubHeading("J. Final state of the clinic day");
        printAppointments(manager.getAllAppointments());
        System.out.println("    " + manager.describeClinicDay());

        printSubHeading("K. What the clinic schedule means for booking a different day");
        ClinicSchedule extendedSchedule = new ClinicSchedule(9, 17, 20);
        AppointmentManager extendedManager =
                new AppointmentManager(extendedSchedule, Day.MONDAY);
        System.out.println("    A second manager can run 09:00-17:00 in 20 minute slots without any");
        System.out.println("    change to the booking code: "
                + extendedManager.getSchedule().describe());
        System.out.println("    " + extendedManager.addAppointment(weiChen, generalPractitionerA,
                new TimeSlot(9, 20)));
        System.out.println("    " + extendedManager.addAppointment(weiChen, generalPractitionerA,
                new TimeSlot(8, 0)));
    }

    /**
     * Prints the closing lines of the demonstration.
     */
    private static void printProgramFooter() {
        System.out.println();
        System.out.println(WIDE_LINE);
        System.out.println(" End of demonstration.");
        System.out.println(" All four pillars are exercised: abstraction (HealthProfessional),");
        System.out.println(" encapsulation (private validated fields), inheritance (the five");
        System.out.println(" professional classes) and polymorphism (toString, performDuty and the");
        System.out.println(" filters working through the shared types).");
        System.out.println(WIDE_LINE);
    }

    /**
     * Prints the heading of a main section.
     *
     * @param title       the section title
     * @param explanation a one line explanation of the section
     */
    private static void printSection(String title, String explanation) {
        System.out.println();
        System.out.println(WIDE_LINE);
        System.out.println(" " + title);
        System.out.println(" " + explanation);
        System.out.println(WIDE_LINE);
    }

    /**
     * Prints a small heading inside a section.
     *
     * @param heading the heading text
     */
    private static void printSubHeading(String heading) {
        System.out.println();
        System.out.println("  " + heading);
    }

    /**
     * Prints a collection of appointments in the order given, followed by a count.
     *
     * @param appointments the appointments to print
     */
    private static void printAppointments(ArrayList<Appointment> appointments) {
        if (appointments == null || appointments.isEmpty()) {
            System.out.println("    (no appointments)");
            return;
        }
        int number = 1;
        for (Appointment appointment : appointments) {
            if (number > 1) {
                System.out.println();
            }
            System.out.println("  " + appointment);
            number++;
        }
        System.out.println("    (" + appointments.size() + " appointment"
                + (appointments.size() == 1 ? "" : "s") + ")");
    }

    /**
     * Finds a professional by identifier.
     *
     * <p>Searching the shared collection by identifier shows that the different types can be
     * handled together; the caller then casts to the specific type only when it needs behaviour
     * that belongs to that specialty.</p>
     *
     * @param professionals the professionals to search
     * @param id            the identifier to look for
     * @return the matching professional
     * @throws IllegalArgumentException if no professional has that identifier
     */
    private static HealthProfessional findProfessional(ArrayList<HealthProfessional> professionals,
            String id) {
        for (HealthProfessional professional : professionals) {
            if (professional.getId().equals(id)) {
                return professional;
            }
        }
        throw new IllegalArgumentException("No health professional has the identifier " + id + ".");
    }
}
