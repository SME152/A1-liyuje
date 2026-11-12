import java.util.ArrayList;

/**
 * A surgeon: a doctor who performs operations in an operating theatre.
 *
 * <p>A surgeon records information that other health professionals do not have, such as the
 * surgical specialty and the theatre they operate in. That information is added here while the
 * shared data and behaviour are inherited from {@link HealthProfessional}.</p>
 *
 * @author Li Yuje
 * @version 1.0
 */
public class Surgeon extends HealthProfessional {

    /** What a surgeon does, shown with the professional's details. */
    private static final String DUTY = "performs surgical procedures in an operating theatre";

    /** The surgeon's area of surgical specialty, for example "Orthopaedic surgery". */
    private final String surgicalSpecialty;

    /** The operating theatre the surgeon works in. */
    private final int theatreNumber;

    /**
     * Creates a surgeon who works on the given days.
     *
     * @param id                the identifier, digits only
     * @param name              the surgeon's full name
     * @param availableDays     the days the surgeon works
     * @param surgicalSpecialty the surgeon's area of specialty
     * @param theatreNumber     the operating theatre number, greater than zero
     * @throws IllegalArgumentException if an argument is missing or invalid
     */
    public Surgeon(String id, String name, ArrayList<Day> availableDays,
            String surgicalSpecialty, int theatreNumber) {
        super(id, name, availableDays);
        this.surgicalSpecialty = requireText(surgicalSpecialty, "surgical specialty");
        if (theatreNumber <= 0) {
            throw new IllegalArgumentException(
                    "An operating theatre number must be greater than zero, but was "
                            + theatreNumber + ".");
        }
        this.theatreNumber = theatreNumber;
    }

    /**
     * Creates a surgeon who works Monday to Friday.
     *
     * @param id                the identifier, digits only
     * @param name              the surgeon's full name
     * @param surgicalSpecialty the surgeon's area of specialty
     * @param theatreNumber     the operating theatre number, greater than zero
     * @throws IllegalArgumentException if an argument is missing or invalid
     */
    public Surgeon(String id, String name, String surgicalSpecialty, int theatreNumber) {
        super(id, name);
        this.surgicalSpecialty = requireText(surgicalSpecialty, "surgical specialty");
        if (theatreNumber <= 0) {
            throw new IllegalArgumentException(
                    "An operating theatre number must be greater than zero, but was "
                            + theatreNumber + ".");
        }
        this.theatreNumber = theatreNumber;
    }

    /**
     * Returns the surgeon's area of specialty.
     *
     * @return the specialty, for example "Orthopaedic surgery"
     */
    public String getSurgicalSpecialty() {
        return surgicalSpecialty;
    }

    /**
     * Returns the operating theatre the surgeon works in.
     *
     * @return the theatre number
     */
    public int getTheatreNumber() {
        return theatreNumber;
    }

    /**
     * Returns the job title of this professional.
     *
     * @return "Surgeon"
     */
    @Override
    public String getRole() {
        return "Surgeon";
    }

    /**
     * Returns the details unique to a surgeon.
     *
     * @return the specialty, theatre and duty, one per line
     */
    @Override
    public String getSpecialtyDetails() {
        return formatLine("Specialty", surgicalSpecialty)
                + formatLine("Theatre", "Theatre " + theatreNumber)
                + formatLine("Duty", DUTY);
    }

    /**
     * Describes the main duty of a surgeon.
     *
     * @return a sentence describing the surgeon's duty
     */
    @Override
    public String performDuty() {
        return getName() + " (" + getRole() + ", " + surgicalSpecialty + ") " + DUTY + ".";
    }

    /**
     * Records a surgical procedure performed for a patient.
     *
     * @param patient   the patient being operated on
     * @param procedure the name of the procedure, for example "knee reconstruction"
     * @return a description of the procedure
     * @throws IllegalArgumentException if the patient or procedure is missing
     */
    public String performProcedure(Patient patient, String procedure) {
        if (patient == null) {
            throw new IllegalArgumentException("A procedure needs a patient.");
        }
        return getName() + " (" + getRole() + ") performed a "
                + requireText(procedure, "procedure") + " on " + patient.getName()
                + " in theatre " + theatreNumber + ".";
    }
}
