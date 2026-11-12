import java.util.ArrayList;

/**
 * A nurse: a health professional who works on a ward and can take blood samples.
 *
 * <p>A nurse records the ward and the shift they work, which no other professional in this system
 * records, and offers behaviour that only a nurse has, such as taking a blood sample. Everything
 * shared with the other professions is inherited from {@link HealthProfessional}.</p>
 *
 * @author Li Yuje
 * @version 1.0
 */
public class Nurse extends HealthProfessional {

    /** What a nurse does, shown with the professional's details. */
    private static final String DUTY = "monitors patients, takes blood samples and manages care";

    /** The shifts a nurse can be rostered on. */
    private static final String[] VALID_SHIFTS = {"Morning", "Evening", "Night"};

    /** The hospital ward the nurse works on. */
    private final String ward;

    /** The shift the nurse is rostered on, for example "Morning". */
    private final String shift;

    /**
     * Creates a nurse who works on the given days.
     *
     * @param id            the identifier, digits only
     * @param name          the nurse's full name
     * @param availableDays the days the nurse works
     * @param ward          the ward the nurse works on
     * @param shift         the shift, one of Morning, Evening or Night
     * @throws IllegalArgumentException if an argument is missing or invalid
     */
    public Nurse(String id, String name, ArrayList<Day> availableDays, String ward, String shift) {
        super(id, name, availableDays);
        this.ward = requireText(ward, "ward");
        this.shift = validateShift(shift);
    }

    /**
     * Creates a nurse who works Monday to Friday.
     *
     * @param id    the identifier, digits only
     * @param name  the nurse's full name
     * @param ward  the ward the nurse works on
     * @param shift the shift, one of Morning, Evening or Night
     * @throws IllegalArgumentException if an argument is missing or invalid
     */
    public Nurse(String id, String name, String ward, String shift) {
        super(id, name);
        this.ward = requireText(ward, "ward");
        this.shift = validateShift(shift);
    }

    /**
     * Checks that a shift is one of the shifts the service runs.
     *
     * @param shift the shift supplied by the caller, possibly {@code null}
     * @return the trimmed shift name
     * @throws IllegalArgumentException if the shift is not one of Morning, Evening or Night
     */
    private static String validateShift(String shift) {
        String trimmed = requireText(shift, "shift");
        for (int index = 0; index < VALID_SHIFTS.length; index++) {
            if (VALID_SHIFTS[index].equalsIgnoreCase(trimmed)) {
                return VALID_SHIFTS[index];
            }
        }
        throw new IllegalArgumentException(
                "A shift must be Morning, Evening or Night, but was \"" + shift + "\".");
    }

    /**
     * Returns the ward this nurse works on.
     *
     * @return the ward name
     */
    public String getWard() {
        return ward;
    }

    /**
     * Returns the shift this nurse is rostered on.
     *
     * @return the shift name
     */
    public String getShift() {
        return shift;
    }

    /**
     * Returns the job title of this professional.
     *
     * @return "Nurse"
     */
    @Override
    public String getRole() {
        return "Nurse";
    }

    /**
     * Returns the details unique to a nurse.
     *
     * @return the ward, shift and duty, one per line
     */
    @Override
    public String getSpecialtyDetails() {
        return formatLine("Ward", ward)
                + formatLine("Shift", shift)
                + formatLine("Duty", DUTY);
    }

    /**
     * Describes the main duty of a nurse.
     *
     * @return a sentence describing the nurse's duty
     */
    @Override
    public String performDuty() {
        return getName() + " (" + getRole() + ", " + ward + ") " + DUTY + ".";
    }

    /**
     * Records a blood sample taken from a patient.
     *
     * <p>This behaviour belongs only to nurses, so it is declared here instead of in
     * {@link HealthProfessional}: a surgeon does not take routine blood samples.</p>
     *
     * @param patient the patient the sample was taken from
     * @return a description of the sample collection
     * @throws IllegalArgumentException if the patient is missing
     */
    public String takeBloodSample(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("A blood sample needs a patient.");
        }
        return getName() + " (" + getRole() + ") took a blood sample from " + patient.getName()
                + " on " + ward + " during the " + shift + " shift.";
    }
}
