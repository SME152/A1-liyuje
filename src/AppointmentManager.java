import java.util.ArrayList;
import java.util.Collections;

/**
 * Manages the collection of appointments for one clinic day.
 *
 * <p>This class is the "one place" the health service asked for. It is the only class that owns
 * the collection of appointments, so the rules that keep the bookings consistent live here:</p>
 * <ul>
 *   <li>an appointment can only be added if all of its details are present,</li>
 *   <li>the time must be one of the clinic's scheduled slots,</li>
 *   <li>the health professional must be working on the clinic day, and</li>
 *   <li>the same health professional can never be booked into the same time slot twice.</li>
 * </ul>
 *
 * <h2>Design decisions</h2>
 * <ul>
 *   <li><strong>The collection.</strong> An {@code ArrayList<Appointment>} is used because the
 *       system needs an ordered, growable list that it can walk through, filter and sort, and
 *       because there is no key to look a booking up by. A set would lose the ordering and pairs of
 *       bookings are not unique, so a list is the right choice.</li>
 *   <li><strong>Feedback instead of exceptions.</strong> Every operation that can fail returns a
 *       readable message, so the caller always has something to show the user and the program does
 *       not crash on an ordinary wrong booking.</li>
 *   <li><strong>Defensive copies.</strong> The query methods return copies of the collection, so a
 *       caller that removes appointments from the returned list cannot corrupt the manager's
 *       data.</li>
 * </ul>
 *
 * @author Li Yuje
 * @version 1.0
 */
public class AppointmentManager {

    /** The prefix used for a message that reports a successful operation. */
    public static final String SUCCESS = "[OK]       ";

    /** The prefix used for a message that reports a rejected operation. */
    public static final String WARNING = "[WARNING]  ";

    /** Every appointment currently booked for the clinic day. */
    private final ArrayList<Appointment> appointments;

    /** The clinic's fixed daily schedule of appointment times. */
    private final ClinicSchedule schedule;

    /** The day the clinic is currently running, which is the only day that can be booked. */
    private final Day clinicDay;

    /**
     * Creates a manager for a clinic day using the standard 08:00-16:00 schedule.
     *
     * @param clinicDay the day the clinic is running
     * @throws IllegalArgumentException if the clinic day is missing
     */
    public AppointmentManager(Day clinicDay) {
        this(new ClinicSchedule(), clinicDay);
    }

    /**
     * Creates a manager for a clinic day with a custom schedule.
     *
     * @param schedule  the clinic's appointment schedule
     * @param clinicDay the day the clinic is running
     * @throws IllegalArgumentException if the schedule or the clinic day is missing
     */
    public AppointmentManager(ClinicSchedule schedule, Day clinicDay) {
        if (schedule == null) {
            throw new IllegalArgumentException("An appointment manager needs a clinic schedule.");
        }
        if (clinicDay == null) {
            throw new IllegalArgumentException("An appointment manager needs a clinic day.");
        }
        this.schedule = schedule;
        this.clinicDay = clinicDay;
        this.appointments = new ArrayList<Appointment>();
    }

    /**
     * Returns the clinic schedule this manager books into.
     *
     * @return the clinic schedule
     */
    public ClinicSchedule getSchedule() {
        return schedule;
    }

    /**
     * Returns the clinic day this manager is running.
     *
     * @return the clinic day
     */
    public Day getClinicDay() {
        return clinicDay;
    }

    /**
     * Adds an appointment for a patient, a health professional of any type and a time.
     *
     * <p>This overload builds the {@link Appointment} object itself. If a caller tries to book
     * with a missing detail, the attempt is rejected with a message rather than crashing.</p>
     *
     * @param patient      the patient attending
     * @param professional the health professional being seen
     * @param time         the requested time
     * @return a message describing the result
     */
    public String addAppointment(Patient patient, HealthProfessional professional, TimeSlot time) {
        if (patient == null || professional == null || time == null) {
            return WARNING + "A booking needs a patient, a health professional and a time.";
        }
        return addAppointment(new Appointment(patient, professional, time));
    }

    /**
     * Adds a prepared appointment, checking every clinic rule first.
     *
     * @param appointment the appointment to add
     * @return a message describing the result
     */
    public String addAppointment(Appointment appointment) {
        if (appointment == null) {
            return WARNING + "A booking needs an appointment.";
        }
        HealthProfessional professional = appointment.getProfessional();
        TimeSlot time = appointment.getTime();

        if (!schedule.isValidSlot(time)) {
            return WARNING + time + " is not a clinic time slot. The clinic runs "
                    + schedule.describe() + ".";
        }
        if (!professional.worksOn(clinicDay)) {
            return WARNING + professional.getName() + " does not work on " + clinicDay
                    + " (available " + professional.getAvailableDaysText() + ").";
        }
        if (isSlotBooked(professional, time)) {
            return WARNING + professional.getName() + " is already booked at " + time
                    + " on " + clinicDay + ". A professional cannot be double booked.";
        }

        appointments.add(appointment);
        return SUCCESS + describe(appointment);
    }

    /**
     * Tests whether a health professional is already booked at a given time.
     *
     * @param professional the health professional to check
     * @param time         the time to check
     * @return {@code true} if that professional already has an appointment then
     */
    public boolean isSlotBooked(HealthProfessional professional, TimeSlot time) {
        for (Appointment appointment : appointments) {
            if (appointment.isWith(professional) && appointment.isAt(time)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests whether a health professional is free at a given clinic time.
     *
     * @param professional the health professional to check
     * @param time         the time to check
     * @return {@code true} if the time is a clinic slot and the professional is free
     */
    public boolean isSlotAvailable(HealthProfessional professional, TimeSlot time) {
        return schedule.isValidSlot(time)
                && professional.worksOn(clinicDay)
                && !isSlotBooked(professional, time);
    }

    /**
     * Returns every appointment in time order.
     *
     * @return a sorted copy of all appointments
     */
    public ArrayList<Appointment> getAllAppointments() {
        return sortByTime(appointments);
    }

    /**
     * Returns the appointments for one health professional, in time order.
     *
     * @param professionalId the professional's identifier
     * @return a sorted copy of that professional's appointments, which is empty if there are none
     */
    public ArrayList<Appointment> getAppointmentsForProfessional(String professionalId) {
        ArrayList<Appointment> matching = new ArrayList<Appointment>();
        for (Appointment appointment : appointments) {
            if (appointment.isWithProfessionalId(professionalId)) {
                matching.add(appointment);
            }
        }
        return sortByTime(matching);
    }

    /**
     * Returns the appointments for one patient, found by mobile phone number, in time order.
     *
     * @param mobilePhone the patient's mobile phone number
     * @return a sorted copy of that patient's appointments, which is empty if there are none
     */
    public ArrayList<Appointment> getAppointmentsForPatient(String mobilePhone) {
        ArrayList<Appointment> matching = new ArrayList<Appointment>();
        for (Appointment appointment : appointments) {
            if (appointment.isForMobilePhone(mobilePhone)) {
                matching.add(appointment);
            }
        }
        return sortByTime(matching);
    }

    /**
     * Sorts any collection of appointments from earliest to latest.
     *
     * <p>The caller's collection is not changed; a sorted copy is returned. Because the method
     * accepts any collection of appointments, it sorts the whole day and a filtered subset in
     * exactly the same way.</p>
     *
     * @param toSort the appointments to sort
     * @return a sorted copy of the given appointments
     */
    public ArrayList<Appointment> sortByTime(ArrayList<Appointment> toSort) {
        if (toSort == null) {
            return new ArrayList<Appointment>();
        }
        ArrayList<Appointment> sorted = new ArrayList<Appointment>(toSort);
        Collections.sort(sorted);
        return sorted;
    }

    /**
     * Cancels the appointment for a health professional at a given time.
     *
     * @param professionalId the professional's identifier
     * @param time           the time of the appointment to cancel
     * @return a message describing the result
     */
    public String cancelAppointment(String professionalId, TimeSlot time) {
        if (time == null) {
            return WARNING + "A cancellation needs a time.";
        }
        for (int index = 0; index < appointments.size(); index++) {
            Appointment appointment = appointments.get(index);
            if (appointment.isWithProfessionalId(professionalId) && appointment.isAt(time)) {
                appointments.remove(index);
                return SUCCESS + "Cancelled " + describe(appointment) + ".";
            }
        }
        return WARNING + "No appointment found for professional ID " + wrap(professionalId)
                + " at " + time + " on " + clinicDay + ". Nothing was cancelled.";
    }

    /**
     * Cancels the appointment for a health professional at a time given as text, for example
     * "09:30".
     *
     * @param professionalId the professional's identifier
     * @param timeText       the time of the appointment as HH:mm text
     * @return a message describing the result
     */
    public String cancelAppointment(String professionalId, String timeText) {
        TimeSlot time;
        try {
            time = TimeSlot.parse(timeText);
        } catch (IllegalArgumentException e) {
            return WARNING + "Cannot cancel: " + e.getMessage();
        }
        return cancelAppointment(professionalId, time);
    }

    /**
     * Cancels every appointment booked for the patient with a given mobile phone number.
     *
     * <p>The clinic knows a patient by their phone number, so cancelling by phone number removes
     * each appointment that patient has on the day.</p>
     *
     * @param mobilePhone the patient's mobile phone number
     * @return a message describing the result
     */
    public String cancelAppointmentsForPatient(String mobilePhone) {
        String phone = mobilePhone == null ? "" : mobilePhone.trim();
        if (phone.isEmpty()) {
            return WARNING + "A cancellation needs the patient's mobile phone number.";
        }
        ArrayList<Appointment> removed = new ArrayList<Appointment>();
        for (int index = appointments.size() - 1; index >= 0; index--) {
            Appointment appointment = appointments.get(index);
            if (appointment.isForMobilePhone(phone)) {
                appointments.remove(index);
                removed.add(appointment);
            }
        }
        if (removed.isEmpty()) {
            return WARNING + "No appointment found for mobile " + phone + " on " + clinicDay
                    + ". Nothing was cancelled.";
        }
        String message = SUCCESS + "Cancelled " + removed.size() + " appointment"
                + (removed.size() == 1 ? "" : "s") + " for mobile " + phone + ":";
        for (Appointment appointment : sortByTime(removed)) {
            message = message + System.lineSeparator() + "             - " + describe(appointment);
        }
        return message;
    }

    /**
     * Returns the number of appointments currently booked.
     *
     * @return the number of appointments
     */
    public int getAppointmentCount() {
        return appointments.size();
    }

    /**
     * Returns a one line summary of the clinic day.
     *
     * @return the clinic day, the schedule and the number of bookings
     */
    public String describeClinicDay() {
        return clinicDay + ", " + schedule.describe() + ", "
                + appointments.size() + " appointment" + (appointments.size() == 1 ? "" : "s")
                + " booked";
    }

    /**
     * Describes one appointment in a single line, which is what the success and warning messages
     * use.
     *
     * @param appointment the appointment to describe
     * @return a single line description such as "09:00 - Wei Chen with Dr Amina Haddad (General
     *         Practitioner)"
     */
    private String describe(Appointment appointment) {
        HealthProfessional professional = appointment.getProfessional();
        return appointment.getTime() + " - " + appointment.getPatient().getName() + " with "
                + professional.getName() + " (" + professional.getRole() + ")";
    }

    /**
     * Wraps an optional value in quotes for a message, so that a missing value is visible.
     *
     * @param value the value to wrap, possibly {@code null}
     * @return the value in quotes, or "not supplied" when the value is missing
     */
    private String wrap(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "not supplied";
        }
        return "\"" + value.trim() + "\"";
    }
}
