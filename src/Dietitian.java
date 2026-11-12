import java.util.ArrayList;

/**
 * A dietitian: a health professional who plans what a patient eats.
 *
 * <p>A dietitian records the area of nutrition they focus on and the length of a consultation,
 * neither of which the other professions record. The shared data and behaviour is inherited from
 * {@link HealthProfessional}.</p>
 *
 * @author Li Yuje
 * @version 1.0
 */
public class Dietitian extends HealthProfessional {

    /** What a dietitian does, shown with the professional's details. */
    private static final String DUTY = "assesses nutrition and designs meal plans for patients";

    /** The area of nutrition this dietitian focuses on. */
    private final String dietFocus;

    /** The length of one consultation in minutes. */
    private final int consultationMinutes;

    /**
     * Creates a dietitian who works on the given days.
     *
     * @param id                  the identifier, digits only
     * @param name                the dietitian's full name
     * @param availableDays       the days the dietitian works
     * @param dietFocus           the area of nutrition the dietitian focuses on
     * @param consultationMinutes the length of a consultation in minutes, greater than zero
     * @throws IllegalArgumentException if an argument is missing or invalid
     */
    public Dietitian(String id, String name, ArrayList<Day> availableDays, String dietFocus,
            int consultationMinutes) {
        super(id, name, availableDays);
        this.dietFocus = requireText(dietFocus, "diet focus");
        if (consultationMinutes <= 0 || consultationMinutes > 240) {
            throw new IllegalArgumentException(
                    "A consultation must be between 1 and 240 minutes, but was "
                            + consultationMinutes + " minutes.");
        }
        this.consultationMinutes = consultationMinutes;
    }

    /**
     * Creates a dietitian who works Monday to Friday.
     *
     * @param id                  the identifier, digits only
     * @param name                the dietitian's full name
     * @param dietFocus           the area of nutrition the dietitian focuses on
     * @param consultationMinutes the length of a consultation in minutes, greater than zero
     * @throws IllegalArgumentException if an argument is missing or invalid
     */
    public Dietitian(String id, String name, String dietFocus, int consultationMinutes) {
        super(id, name);
        this.dietFocus = requireText(dietFocus, "diet focus");
        if (consultationMinutes <= 0 || consultationMinutes > 240) {
            throw new IllegalArgumentException(
                    "A consultation must be between 1 and 240 minutes, but was "
                            + consultationMinutes + " minutes.");
        }
        this.consultationMinutes = consultationMinutes;
    }

    /**
     * Returns the area of nutrition this dietitian focuses on.
     *
     * @return the diet focus, for example "Diabetes management"
     */
    public String getDietFocus() {
        return dietFocus;
    }

    /**
     * Returns the length of one consultation.
     *
     * @return the consultation length in minutes
     */
    public int getConsultationMinutes() {
        return consultationMinutes;
    }

    /**
     * Returns the job title of this professional.
     *
     * @return "Dietitian"
     */
    @Override
    public String getRole() {
        return "Dietitian";
    }

    /**
     * Returns the details unique to a dietitian.
     *
     * @return the diet focus, consultation length and duty, one per line
     */
    @Override
    public String getSpecialtyDetails() {
        return formatLine("Focus", dietFocus)
                + formatLine("Consult", consultationMinutes + " minutes")
                + formatLine("Duty", DUTY);
    }

    /**
     * Describes the main duty of a dietitian.
     *
     * @return a sentence describing the dietitian's duty
     */
    @Override
    public String performDuty() {
        return getName() + " (" + getRole() + ", " + dietFocus + ") " + DUTY + ".";
    }

    /**
     * Builds a daily meal plan for a patient.
     *
     * @param patient       the patient the plan is for
     * @param dailyCalories the target daily energy intake in kilojoules, greater than zero
     * @return a description of the meal plan
     * @throws IllegalArgumentException if the patient is missing or the target is not positive
     */
    public String createMealPlan(Patient patient, int dailyCalories) {
        if (patient == null) {
            throw new IllegalArgumentException("A meal plan needs a patient.");
        }
        if (dailyCalories <= 0) {
            throw new IllegalArgumentException(
                    "A daily energy target must be greater than zero, but was " + dailyCalories
                            + ".");
        }
        return getName() + " (" + getRole() + ") prepared a " + dailyCalories
                + " kJ meal plan for " + patient.getName() + " focused on "
                + dietFocus.toLowerCase() + ".";
    }
}
