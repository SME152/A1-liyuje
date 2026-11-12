import java.util.ArrayList;

/**
 * A general practitioner: the family doctor a patient usually sees first.
 *
 * <p>On top of the shared information every health professional has, a general practitioner works
 * from a consultation room and may or may not be accepting new patients. The shared information
 * comes from {@link HealthProfessional}; this class adds only what is unique to a GP, so nothing is
 * duplicated.</p>
 *
 * @author Li Yuje
 * @version 1.0
 */
public class GeneralPractitioner extends HealthProfessional {

    /** What a general practitioner does, shown with the professional's details. */
    private static final String DUTY =
            "prescribes medication, orders tests and refers patients to specialists";

    /** The consultation room this GP works from. */
    private final String consultationRoom;

    /** Whether this GP is currently accepting new patients. */
    private final boolean acceptingNewPatients;

    /**
     * Creates a general practitioner who works on the given days.
     *
     * @param id                   the identifier, digits only
     * @param name                 the GP's full name
     * @param availableDays        the days the GP works
     * @param consultationRoom     the consultation room the GP works from
     * @param acceptingNewPatients whether the GP is accepting new patients
     * @throws IllegalArgumentException if the room is blank or the shared details are invalid
     */
    public GeneralPractitioner(String id, String name, ArrayList<Day> availableDays,
            String consultationRoom, boolean acceptingNewPatients) {
        super(id, name, availableDays);
        this.consultationRoom = requireText(consultationRoom, "consultation room");
        this.acceptingNewPatients = acceptingNewPatients;
    }

    /**
     * Creates a general practitioner who works on the given days and is accepting new patients.
     *
     * <p>Being open to new patients is the sensible default, so a caller who does not care about
     * the flag can leave it out. This is the overloaded constructor the brief asks about.</p>
     *
     * @param id               the identifier, digits only
     * @param name             the GP's full name
     * @param availableDays    the days the GP works
     * @param consultationRoom the consultation room the GP works from
     * @throws IllegalArgumentException if the room is blank or the shared details are invalid
     */
    public GeneralPractitioner(String id, String name, ArrayList<Day> availableDays,
            String consultationRoom) {
        this(id, name, availableDays, consultationRoom, true);
    }

    /**
     * Creates a general practitioner who works Monday to Friday.
     *
     * @param id               the identifier, digits only
     * @param name             the GP's full name
     * @param consultationRoom the consultation room the GP works from
     * @throws IllegalArgumentException if the room is blank or the shared details are invalid
     */
    public GeneralPractitioner(String id, String name, String consultationRoom) {
        super(id, name);
        this.consultationRoom = requireText(consultationRoom, "consultation room");
        this.acceptingNewPatients = true;
    }

    /**
     * Returns the consultation room this GP works from.
     *
     * @return the room, for example "Room 3"
     */
    public String getConsultationRoom() {
        return consultationRoom;
    }

    /**
     * Reports whether this GP is accepting new patients.
     *
     * @return {@code true} if new patients are welcome
     */
    public boolean isAcceptingNewPatients() {
        return acceptingNewPatients;
    }

    /**
     * Returns the job title of this professional.
     *
     * @return "General Practitioner"
     */
    @Override
    public String getRole() {
        return "General Practitioner";
    }

    /**
     * Returns the details unique to a general practitioner.
     *
     * @return the room, new patient status and duty, one per line
     */
    @Override
    public String getSpecialtyDetails() {
        return formatLine("Room", consultationRoom)
                + formatLine("New patients", acceptingNewPatients ? "yes" : "no")
                + formatLine("Duty", DUTY);
    }

    /**
     * Describes the main duty of a general practitioner.
     *
     * @return a sentence describing the GP's duty
     */
    @Override
    public String performDuty() {
        return getName() + " (" + getRole() + ") " + DUTY + ".";
    }

    /**
     * Prescribes a medication for a patient.
     *
     * <p>This behaviour exists only on doctors, which is why it is declared here rather than in
     * {@link HealthProfessional}: a dietitian cannot prescribe drugs.</p>
     *
     * @param patient    the patient being treated
     * @param medication the name of the medication
     * @param dosage     the dosage, for example "500 mg twice a day"
     * @return a description of the prescription
     * @throws IllegalArgumentException if the patient, medication or dosage is missing
     */
    public String prescribeMedication(Patient patient, String medication, String dosage) {
        if (patient == null) {
            throw new IllegalArgumentException("A prescription needs a patient.");
        }
        return getName() + " (" + getRole() + ") in " + consultationRoom + " prescribed "
                + requireText(medication, "medication") + ", " + requireText(dosage, "dosage")
                + ", to " + patient.getName() + ".";
    }
}
