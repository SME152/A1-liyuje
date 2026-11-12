/**
 * The days of the working week.
 *
 * <p>A health professional works on a fixed set of days, so the days are modelled as an
 * enumeration rather than as free text. Using an enum means:</p>
 * <ul>
 *   <li>only valid day names can ever exist (no typo such as "Wendsday"),</li>
 *   <li>a day is a fixed collection of constant values, which is exactly what an enum is for,
 *       and</li>
 *   <li>the compiler checks every use of a day, so a misspelt day is caught before the program
 *       runs.</li>
 * </ul>
 *
 * @author Li Yuje
 * @version 1.0
 */
public enum Day {

    /** Monday. */
    MONDAY("Monday"),
    /** Tuesday. */
    TUESDAY("Tuesday"),
    /** Wednesday. */
    WEDNESDAY("Wednesday"),
    /** Thursday. */
    THURSDAY("Thursday"),
    /** Friday. */
    FRIDAY("Friday"),
    /** Saturday. */
    SATURDAY("Saturday"),
    /** Sunday. */
    SUNDAY("Sunday");

    /** The day name as it should be shown to a reader, for example "Monday". */
    private final String displayName;

    /**
     * Creates a day constant.
     *
     * @param displayName the readable name of the day
     */
    Day(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the readable name of this day.
     *
     * @return the day name, for example "Monday"
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns a three letter abbreviation of this day, for example "Mon".
     *
     * @return the abbreviated day name
     */
    public String getShortName() {
        return displayName.substring(0, 3);
    }

    /**
     * Returns the readable name, so that a day prints sensibly inside other output.
     *
     * @return the day name
     */
    @Override
    public String toString() {
        return displayName;
    }
}
