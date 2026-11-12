/**
 * A patient of the health service.
 *
 * <p>Patient details are needed by an appointment, and the same details would otherwise have to
 * be repeated on every appointment object. Making {@code Patient} its own class means the data is
 * declared and validated in one place, and an appointment simply refers to one patient object.</p>
 *
 * <p>All fields are private and final, so a patient can be read by the rest of the system but can
 * never be changed into an invalid state after it has been created.</p>
 *
 * @author Li Yuje
 * @version 1.0
 */
public class Patient {

    /** The number of digits in an Australian mobile phone number, for example 0412345678. */
    private static final int MOBILE_DIGIT_COUNT = 10;

    /** The patient's full name. */
    private final String name;

    /** The patient's mobile phone number, as ten digits only. */
    private final String mobilePhone;

    /**
     * Creates a patient.
     *
     * @param name        the patient's full name, not blank
     * @param mobilePhone the patient's mobile phone number, ten digits and nothing else
     * @throws IllegalArgumentException if the name is blank or the phone number is not ten digits
     */
    public Patient(String name, String mobilePhone) {
        String cleanedName = trimToEmpty(name);
        String cleanedPhone = trimToEmpty(mobilePhone);
        if (cleanedName.isEmpty()) {
            throw new IllegalArgumentException("A patient must have a name.");
        }
        if (!isAllDigits(cleanedPhone) || cleanedPhone.length() != MOBILE_DIGIT_COUNT) {
            throw new IllegalArgumentException(
                    "A patient's mobile phone number must be " + MOBILE_DIGIT_COUNT
                            + " digits, for example 0412345678, but was \"" + mobilePhone + "\".");
        }
        this.name = cleanedName;
        this.mobilePhone = cleanedPhone;
    }

    /**
     * Returns the patient's full name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the patient's mobile phone number.
     *
     * @return the phone number as ten digits
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * Returns the patient's name and phone number on one readable line.
     *
     * @return the formatted patient details
     */
    @Override
    public String toString() {
        return name + " (mobile " + mobilePhone + ")";
    }

    /**
     * Tests whether this patient is the same person as another object.
     *
     * <p>Two patient objects are treated as the same patient when both the name and the mobile
     * phone number match. The phone number alone is not enough, because two family members may
     * share one mobile phone.</p>
     *
     * @param other the object to compare with
     * @return {@code true} if the other object is a patient with the same name and phone number
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Patient)) {
            return false;
        }
        Patient otherPatient = (Patient) other;
        return name.equals(otherPatient.name) && mobilePhone.equals(otherPatient.mobilePhone);
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code of this patient
     */
    @Override
    public int hashCode() {
        return (name.hashCode() * 31) + mobilePhone.hashCode();
    }

    /**
     * Trims text and turns {@code null} into an empty string, so that callers never deal with
     * a missing value.
     *
     * @param value the text to clean, possibly {@code null}
     * @return the trimmed text, or an empty string
     */
    private static String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    /**
     * Tests whether text consists only of the digits 0 to 9.
     *
     * @param text the text to check
     * @return {@code true} if every character is a digit
     */
    private static boolean isAllDigits(String text) {
        if (text.isEmpty()) {
            return false;
        }
        for (int index = 0; index < text.length(); index++) {
            if (!Character.isDigit(text.charAt(index))) {
                return false;
            }
        }
        return true;
    }
}
