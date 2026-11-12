import java.util.ArrayList;

/**
 * A health professional of the health service.
 *
 * <p>This class holds everything that is common to <em>every</em> health professional, whatever
 * their specialty: an identifier, a name and the fixed set of days they work. The specific kinds
 * of professional (general practitioner, surgeon, nurse, dietitian and physiotherapist) are
 * modelled as subclasses, so the shared data is written once here and inherited by all of
 * them.</p>
 *
 * <h2>Design decisions</h2>
 * <ul>
 *   <li><strong>Encapsulation.</strong> Every field is {@code private} and {@code final}. External
 *       code reads the data through getters but never receives the internal collection, so it
 *       cannot put an object into an invalid state. A professional is fully validated in the
 *       constructor.</li>
 *   <li><strong>Abstraction.</strong> The class is {@code abstract} because a "plain" health
 *       professional with no specialty does not exist in the real service. Java therefore makes
 *       it impossible to create one by accident with {@code new HealthProfessional(...)}.</li>
 *   <li><strong>Polymorphism.</strong> {@link #getRole()}, {@link #getSpecialtyDetails()} and
 *       {@link #performDuty()} are abstract, so every subclass must supply its own version. Code
 *       that holds a {@code HealthProfessional} reference can call them without knowing which
 *       specialty it is dealing with.</li>
 *   <li><strong>Identity.</strong> {@link #equals(Object)} and {@link #hashCode()} are based on
 *       the identifier only, because two objects with the same identifier are the same person.
 *       This is what later lets the appointment system decide whether two appointments are for the
 *       same health professional.</li>
 *   <li><strong>Readable printing.</strong> {@link #toString()} returns a complete, formatted
 *       description, so {@code System.out.println(doctor)} shows every detail.</li>
 * </ul>
 *
 * @author Li Yuje
 * @version 1.0
 */
public abstract class HealthProfessional implements Comparable<HealthProfessional> {

    /** The width of the label column used when the object's details are printed. */
    private static final int LABEL_WIDTH = 12;

    /** The professional's identifier: digits only, for example 1001. */
    private final String id;

    /** The professional's full name, for example "Dr Amina Haddad". */
    private final String name;

    /** The fixed set of days this professional works. */
    private final ArrayList<Day> availableDays;

    /**
     * Creates a health professional who works on the given days.
     *
     * @param id            the identifier, digits only
     * @param name          the professional's full name, not blank
     * @param availableDays the days this professional works, at least one day
     * @throws IllegalArgumentException if any argument is missing or invalid
     */
    public HealthProfessional(String id, String name, ArrayList<Day> availableDays) {
        this.id = validateId(id);
        this.name = validateName(name);
        if (availableDays == null || availableDays.isEmpty()) {
            throw new IllegalArgumentException(
                    "A health professional must work on at least one day of the week.");
        }
        // Copy the caller's collection so that later changes to it cannot affect this object.
        this.availableDays = new ArrayList<Day>();
        for (Day day : availableDays) {
            if (day == null) {
                throw new IllegalArgumentException("A working day cannot be null.");
            }
            if (!this.availableDays.contains(day)) {
                this.availableDays.add(day);
            }
        }
    }

    /**
     * Creates a health professional who works the standard Monday to Friday week.
     *
     * <p>This overloaded constructor exists so that a caller who does not care about working days
     * does not have to build a day collection: a sensible default is supplied instead. It shows
     * that providing more than one way to construct an object is worthwhile here.</p>
     *
     * @param id   the identifier, digits only
     * @param name the professional's full name, not blank
     * @throws IllegalArgumentException if the identifier or name is invalid
     */
    public HealthProfessional(String id, String name) {
        this(id, name, standardWeekdays());
    }

    /**
     * Builds the standard Monday to Friday working week.
     *
     * @return a collection holding Monday to Friday
     */
    private static ArrayList<Day> standardWeekdays() {
        ArrayList<Day> weekdays = new ArrayList<Day>();
        weekdays.add(Day.MONDAY);
        weekdays.add(Day.TUESDAY);
        weekdays.add(Day.WEDNESDAY);
        weekdays.add(Day.THURSDAY);
        weekdays.add(Day.FRIDAY);
        return weekdays;
    }

    /**
     * Returns this professional's identifier.
     *
     * @return the identifier, digits only
     */
    public String getId() {
        return id;
    }

    /**
     * Returns this professional's full name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the days this professional works.
     *
     * <p>A copy is returned so that external code cannot add or remove working days from this
     * object.</p>
     *
     * @return a copy of the working days
     */
    public ArrayList<Day> getAvailableDays() {
        return new ArrayList<Day>(availableDays);
    }

    /**
     * Tests whether this professional works on a given day.
     *
     * @param day the day to check
     * @return {@code true} if the professional works on that day
     */
    public boolean worksOn(Day day) {
        return availableDays.contains(day);
    }

    /**
     * Returns the working days as short text, for example "Mon, Wed, Fri".
     *
     * @return the working days in readable form
     */
    public String getAvailableDaysText() {
        String text = "";
        for (int index = 0; index < availableDays.size(); index++) {
            if (index > 0) {
                text = text + ", ";
            }
            text = text + availableDays.get(index).getShortName();
        }
        return text;
    }

    /**
     * Returns the job title of this kind of health professional, for example "Surgeon".
     *
     * <p>Every subclass must answer this, which is how each object identifies its own type when
     * it presents its details.</p>
     *
     * @return the job title of this professional
     */
    public abstract String getRole();

    /**
     * Returns the details that are unique to this kind of health professional.
     *
     * <p>The subclass supplies only its own extra information; the shared information is added by
     * {@link #getDetails()}, so a subclass never has to repeat the common part and cannot
     * accidentally leave it out.</p>
     *
     * @return the specialty-specific details, one per line
     */
    public abstract String getSpecialtyDetails();

    /**
     * Performs the main duty of this kind of health professional.
     *
     * <p>The duty is described in a sentence so that a demonstration can show the same method call
     * behaving differently for each subclass.</p>
     *
     * @return a description of what this professional did
     */
    public abstract String performDuty();

    /**
     * Builds the shared part of this professional's details.
     *
     * @return the common details, one per line
     */
    protected String getSharedDetails() {
        StringBuilder builder = new StringBuilder();
        builder.append(formatLine("Role", getRole()));
        builder.append(formatLine("ID", id));
        builder.append(formatLine("Name", name));
        builder.append(formatLine("Works", getAvailableDaysText()));
        return builder.toString();
    }

    /**
     * Builds the complete description of this professional: the shared details, then the details
     * that belong to the specific specialty.
     *
     * <p>The method is {@code final} so that no subclass can break the layout or leave out the
     * shared information.</p>
     *
     * @return the complete details, one per line
     */
    public final String getDetails() {
        return getSharedDetails() + getSpecialtyDetails();
    }

    /**
     * Returns all of this professional's details in a readable form, which is what
     * {@code System.out.println(professional)} prints.
     *
     * @return the complete details
     */
    @Override
    public final String toString() {
        return getDetails();
    }

    /**
     * Formats one "label : value" line of the detail block.
     *
     * @param label the label of the line, for example "Name"
     * @param value the value of the line
     * @return the padded, formatted line
     */
    protected static String formatLine(String label, String value) {
        String paddedLabel = label;
        while (paddedLabel.length() < LABEL_WIDTH) {
            paddedLabel = paddedLabel + " ";
        }
        return "  " + paddedLabel + ": " + value + System.lineSeparator();
    }

    /**
     * Trims required text and rejects a missing or blank value.
     *
     * <p>Every subclass validates its own extra fields with this one helper, so the same rule and
     * the same error message style is used throughout the hierarchy instead of being repeated in
     * five classes.</p>
     *
     * @param value the text supplied by the caller, possibly {@code null}
     * @param label what the text describes, used in the error message
     * @return the trimmed text
     * @throws IllegalArgumentException if the text is missing or blank
     */
    protected static String requireText(String value, String label) {
        String trimmed = value == null ? "" : value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("A " + label + " must not be blank.");
        }
        return trimmed;
    }

    /**
     * Tests whether this professional is the same professional as another object.
     *
     * <p>Two objects are the same professional when their identifiers match. Identifying a person
     * by their identifier is what makes it possible to tell whether two appointments are for the
     * same health professional, even when each appointment holds a different object reference.</p>
     *
     * @param other the object to compare with
     * @return {@code true} if the other object is a health professional with the same identifier
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HealthProfessional)) {
            return false;
        }
        HealthProfessional otherProfessional = (HealthProfessional) other;
        return id.equals(otherProfessional.id);
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code of the identifier
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Orders professionals alphabetically by name, then by identifier.
     *
     * <p>Implementing {@link Comparable} means a collection of professionals can be sorted with a
     * single call, and it also makes the order of professionals predictable in the output.</p>
     *
     * @param other the professional to compare with
     * @return a negative number, zero or a positive number if this professional comes before, at
     *         the same place as, or after {@code other}
     */
    @Override
    public int compareTo(HealthProfessional other) {
        int byName = this.name.compareToIgnoreCase(other.name);
        if (byName != 0) {
            return byName;
        }
        return this.id.compareTo(other.id);
    }

    /**
     * Validates a professional identifier.
     *
     * @param id the identifier supplied by the caller, possibly {@code null}
     * @return the trimmed identifier
     * @throws IllegalArgumentException if the identifier is missing or is not digits only
     */
    private static String validateId(String id) {
        String trimmed = requireText(id, "health professional identifier");
        for (int index = 0; index < trimmed.length(); index++) {
            if (!Character.isDigit(trimmed.charAt(index))) {
                throw new IllegalArgumentException(
                        "A health professional's identifier must contain numbers only, but was \""
                                + id + "\".");
            }
        }
        return trimmed;
    }

    /**
     * Validates a professional's name.
     *
     * @param name the name supplied by the caller, possibly {@code null}
     * @return the trimmed name
     * @throws IllegalArgumentException if the name is missing
     */
    private static String validateName(String name) {
        return requireText(name, "health professional name");
    }
}
