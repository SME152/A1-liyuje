/**
 * One appointment: a patient seeing a chosen health professional at a chosen time today.
 *
 * <p>An appointment is the link between the two other parts of the model. It holds a
 * {@link Patient} object and a {@link HealthProfessional} reference, which means it can hold
 * <em>any</em> kind of professional (a GP, a surgeon, a nurse and so on) without knowing which
 * one it is. That is polymorphism doing real work in the design.</p>
 *
 * <h2>Design decisions</h2>
 * <ul>
 *   <li>All three parts of an appointment are required. The constructor rejects a missing patient,
 *       professional or time, so an appointment can never exist in an incomplete state.</li>
 *   <li>The class implements {@link Comparable}, so a whole collection of appointments can be
 *       sorted into time order with one call.</li>
 *   <li>Appointments are for the current day only, so the appointment does not store a date.</li>
 * </ul>
 *
 * @author Li Yuje
 * @version 1.0
 */
public class Appointment implements Comparable<Appointment> {

    /** The patient attending the appointment. */
    private final Patient patient;

    /** The health professional the patient is seeing. */
    private final HealthProfessional professional;

    /** The time of the appointment. */
    private final TimeSlot time;

    /**
     * Creates an appointment.
     *
     * @param patient      the patient attending
     * @param professional the health professional being seen, of any specialty
     * @param time         the time of the appointment
     * @throws IllegalArgumentException if any part of the appointment is missing
     */
    public Appointment(Patient patient, HealthProfessional professional, TimeSlot time) {
        if (patient == null) {
            throw new IllegalArgumentException("An appointment must have a patient.");
        }
        if (professional == null) {
            throw new IllegalArgumentException("An appointment must have a health professional.");
        }
        if (time == null) {
            throw new IllegalArgumentException("An appointment must have a time.");
        }
        this.patient = patient;
        this.professional = professional;
        this.time = time;
    }

    /**
     * Returns the patient attending this appointment.
     *
     * @return the patient
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Returns the health professional this appointment is with.
     *
     * @return the health professional
     */
    public HealthProfessional getProfessional() {
        return professional;
    }

    /**
     * Returns the time of this appointment.
     *
     * @return the appointment time
     */
    public TimeSlot getTime() {
        return time;
    }

    /**
     * Tests whether this appointment is with a given health professional.
     *
     * <p>The comparison uses {@link HealthProfessional#equals(Object)}, which compares identifiers,
     * so it works even when a different object representing the same professional is passed in.</p>
     *
     * @param other the professional to check
     * @return {@code true} if this appointment is with that professional
     */
    public boolean isWith(HealthProfessional other) {
        return professional.equals(other);
    }

    /**
     * Tests whether this appointment is with the professional holding a given identifier.
     *
     * @param professionalId the identifier to check
     * @return {@code true} if this appointment is with that professional
     */
    public boolean isWithProfessionalId(String professionalId) {
        return professional.getId().equals(professionalId == null ? "" : professionalId.trim());
    }

    /**
     * Tests whether this appointment is for the patient with a given mobile phone number.
     *
     * @param mobilePhone the phone number to check
     * @return {@code true} if this appointment belongs to that patient
     */
    public boolean isForMobilePhone(String mobilePhone) {
        return patient.getMobilePhone().equals(mobilePhone == null ? "" : mobilePhone.trim());
    }

    /**
     * Tests whether this appointment is at a given time.
     *
     * @param otherTime the time to check
     * @return {@code true} if this appointment is at that time
     */
    public boolean isAt(TimeSlot otherTime) {
        return time.equals(otherTime);
    }

    /**
     * Orders appointments from earliest to latest.
     *
     * <p>Two appointments at the same time for different professionals may appear in either order,
     * but the result must still be consistent, so the professional's identifier and then the
     * patient's name are used to break the tie.</p>
     *
     * @param other the appointment to compare with
     * @return a negative number, zero or a positive number if this appointment is earlier than, at
     *         the same time as, or later than {@code other}
     */
    @Override
    public int compareTo(Appointment other) {
        int byTime = this.time.compareTo(other.time);
        if (byTime != 0) {
            return byTime;
        }
        int byProfessional = this.professional.getId().compareTo(other.professional.getId());
        if (byProfessional != 0) {
            return byProfessional;
        }
        return this.patient.getName().compareTo(other.patient.getName());
    }

    /**
     * Returns the appointment's details in a readable form, including the details of the health
     * professional being seen.
     *
     * @return the formatted appointment
     */
    @Override
    public String toString() {
        return time + "  " + patient + System.lineSeparator()
                + "       with " + professional.getName() + " - " + professional.getRole()
                + " (ID " + professional.getId() + "), works "
                + professional.getAvailableDaysText();
    }
}
