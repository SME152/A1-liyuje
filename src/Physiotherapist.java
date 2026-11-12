import java.util.ArrayList;

/**
 * A physiotherapist: a health professional who helps patients recover movement.
 *
 * <p>A physiotherapist records the treatment area they specialise in and how many rehabilitation
 * sessions a week they can run, which the other professions do not have. The shared data and
 * behaviour is inherited from {@link HealthProfessional}.</p>
 *
 * @author Li Yuje
 * @version 1.0
 */
public class Physiotherapist extends HealthProfessional {

    /** What a physiotherapist does, shown with the professional's details. */
    private static final String DUTY =
            "runs rehabilitation sessions to restore movement and strength";

    /** The area of the body this physiotherapist treats, for example "Sports injuries". */
    private final String treatmentArea;

    /** The number of rehabilitation sessions this physiotherapist runs each week. */
    private final int sessionsPerWeek;

    /**
     * Creates a physiotherapist who works on the given days.
     *
     * @param id             the identifier, digits only
     * @param name           the physiotherapist's full name
     * @param availableDays  the days the physiotherapist works
     * @param treatmentArea  the area the physiotherapist treats
     * @param sessionsPerWeek the number of rehabilitation sessions per week, from 1 to 7
     * @throws IllegalArgumentException if an argument is missing or invalid
     */
    public Physiotherapist(String id, String name, ArrayList<Day> availableDays,
            String treatmentArea, int sessionsPerWeek) {
        super(id, name, availableDays);
        this.treatmentArea = requireText(treatmentArea, "treatment area");
        if (sessionsPerWeek < 1 || sessionsPerWeek > 7) {
            throw new IllegalArgumentException(
                    "Sessions per week must be between 1 and 7, but was " + sessionsPerWeek + ".");
        }
        this.sessionsPerWeek = sessionsPerWeek;
    }

    /**
     * Creates a physiotherapist who works Monday to Friday.
     *
     * @param id              the identifier, digits only
     * @param name            the physiotherapist's full name
     * @param treatmentArea   the area the physiotherapist treats
     * @param sessionsPerWeek the number of rehabilitation sessions per week, from 1 to 7
     * @throws IllegalArgumentException if an argument is missing or invalid
     */
    public Physiotherapist(String id, String name, String treatmentArea, int sessionsPerWeek) {
        super(id, name);
        this.treatmentArea = requireText(treatmentArea, "treatment area");
        if (sessionsPerWeek < 1 || sessionsPerWeek > 7) {
            throw new IllegalArgumentException(
                    "Sessions per week must be between 1 and 7, but was " + sessionsPerWeek + ".");
        }
        this.sessionsPerWeek = sessionsPerWeek;
    }

    /**
     * Returns the area this physiotherapist treats.
     *
     * @return the treatment area
     */
    public String getTreatmentArea() {
        return treatmentArea;
    }

    /**
     * Returns the number of rehabilitation sessions this physiotherapist runs each week.
     *
     * @return the number of sessions per week
     */
    public int getSessionsPerWeek() {
        return sessionsPerWeek;
    }

    /**
     * Returns the job title of this professional.
     *
     * @return "Physiotherapist"
     */
    @Override
    public String getRole() {
        return "Physiotherapist";
    }

    /**
     * Returns the details unique to a physiotherapist.
     *
     * @return the treatment area, session count and duty, one per line
     */
    @Override
    public String getSpecialtyDetails() {
        return formatLine("Area", treatmentArea)
                + formatLine("Sessions", sessionsPerWeek + " per week")
                + formatLine("Duty", DUTY);
    }

    /**
     * Describes the main duty of a physiotherapist.
     *
     * @return a sentence describing the physiotherapist's duty
     */
    @Override
    public String performDuty() {
        return getName() + " (" + getRole() + ", " + treatmentArea + ") " + DUTY + ".";
    }

    /**
     * Runs a rehabilitation session for a patient.
     *
     * @param patient  the patient being treated
     * @param exercise the exercise performed, for example "knee extension"
     * @return a description of the session
     * @throws IllegalArgumentException if the patient or exercise is missing
     */
    public String runRehabilitationSession(Patient patient, String exercise) {
        if (patient == null) {
            throw new IllegalArgumentException("A rehabilitation session needs a patient.");
        }
        return getName() + " (" + getRole() + ") ran a rehabilitation session on "
                + requireText(exercise, "exercise") + " with " + patient.getName() + ".";
    }
}
