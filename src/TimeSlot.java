/**
 * A single time of day, stored as an hour and a minute.
 *
 * <p>An appointment time is modelled with this small immutable class instead of a plain
 * {@code String} such as "09:30". A string cannot be compared sensibly ("9:30" comes after
 * "10:00" as text), and it allows invalid values such as "29:75". Keeping the hour and minute as
 * {@code int} values means the class can:</p>
 * <ul>
 *   <li>reject an impossible time when the object is created,</li>
 *   <li>order times correctly for sorting, and</li>
 *   <li>print a consistent, padded "HH:mm" format.</li>
 * </ul>
 *
 * <p>The class is {@code final} and every field is {@code final}: a time slot can never change
 * after it has been created, so an object cannot be put into an invalid state later.</p>
 *
 * @author Li Yuje
 * @version 1.0
 */
public final class TimeSlot implements Comparable<TimeSlot> {

    /** The number of minutes in one hour, used to convert a time to a single number. */
    private static final int MINUTES_PER_HOUR = 60;

    /** The hour of this time slot, in 24 hour form (0-23). */
    private final int hour;

    /** The minute of this time slot (0-59). */
    private final int minute;

    /**
     * Creates a time slot.
     *
     * @param hour   the hour, in 24 hour form, from 0 to 23
     * @param minute the minute, from 0 to 59
     * @throws IllegalArgumentException if the hour or minute is outside its valid range
     */
    public TimeSlot(int hour, int minute) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException(
                    "Hour must be between 0 and 23 but was " + hour + ".");
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException(
                    "Minute must be between 0 and 59 but was " + minute + ".");
        }
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Creates a time slot from text in 24 hour "HH:mm" form, for example "08:30".
     *
     * @param text the time as text
     * @return the matching time slot
     * @throws IllegalArgumentException if the text is not a valid time
     */
    public static TimeSlot parse(String text) {
        String trimmed = text == null ? "" : text.trim();
        if (trimmed.length() != 5 || trimmed.charAt(2) != ':') {
            throw new IllegalArgumentException(
                    "A time must be written as HH:mm, for example 09:30, but was \"" + text + "\".");
        }
        try {
            int parsedHour = Integer.parseInt(trimmed.substring(0, 2));
            int parsedMinute = Integer.parseInt(trimmed.substring(3, 5));
            return new TimeSlot(parsedHour, parsedMinute);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "A time must be written as HH:mm, for example 09:30, but was \"" + text + "\".");
        }
    }

    /**
     * Returns the hour of this time slot.
     *
     * @return the hour, from 0 to 23
     */
    public int getHour() {
        return hour;
    }

    /**
     * Returns the minute of this time slot.
     *
     * @return the minute, from 0 to 59
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Converts this time slot to a single number of minutes from midnight.
     *
     * <p>This one number makes ordering simple: 09:30 becomes 570 minutes and 08:45 becomes
     * 525 minutes, so the later time always has the larger number.</p>
     *
     * @return the number of minutes from midnight
     */
    public int toMinutesFromMidnight() {
        return (hour * MINUTES_PER_HOUR) + minute;
    }

    /**
     * Compares this time slot with another one so that the earliest time comes first.
     *
     * @param other the time slot to compare against
     * @return a negative number, zero, or a positive number if this time is earlier than, the
     *         same as, or later than {@code other}
     */
    @Override
    public int compareTo(TimeSlot other) {
        return Integer.compare(this.toMinutesFromMidnight(), other.toMinutesFromMidnight());
    }

    /**
     * Tests whether this time slot is the same time as another object.
     *
     * <p>Two time slots represent the same time when the hour and minute match, which matters
     * when the system checks whether a health professional is already booked.</p>
     *
     * @param other the object to compare with
     * @return {@code true} if the other object is a time slot with the same hour and minute
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TimeSlot)) {
            return false;
        }
        TimeSlot otherSlot = (TimeSlot) other;
        return hour == otherSlot.hour && minute == otherSlot.minute;
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code of this time slot
     */
    @Override
    public int hashCode() {
        return (hour * 31) + minute;
    }

    /**
     * Returns the time in 24 hour "HH:mm" form, for example "08:30".
     *
     * @return the formatted time
     */
    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }
}
