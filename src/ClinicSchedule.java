import java.util.ArrayList;

/**
 * The fixed daily schedule of a clinic.
 *
 * <p>The clinic runs to a set of appointment times such as 08:00, 08:30, 09:00 and so on until
 * 16:00. That schedule is a {@code ClinicSchedule} object describing the opening and closing
 * time and the length of one appointment, built up as a collection of {@link TimeSlot} objects.</p>
 *
 * <p>Keeping the schedule in its own class means the rest of the system never hard-codes a time.
 * The whole clinic can be moved from 08:00-16:00 in 30 minute blocks to, say, 09:00-17:00 in
 * 20 minute blocks by changing one constructor argument, and every booking, check and listing
 * follows the new schedule without any other change.</p>
 *
 * @author Li Yuje
 * @version 1.0
 */
public class ClinicSchedule {

    /** The opening hour of the standard clinic day. */
    public static final int DEFAULT_START_HOUR = 8;

    /** The closing hour of the standard clinic day. */
    public static final int DEFAULT_END_HOUR = 16;

    /** The length in minutes of one standard appointment. */
    public static final int DEFAULT_SLOT_MINUTES = 30;

    /** The minutes in one hour, used when the schedule is generated. */
    private static final int MINUTES_PER_HOUR = 60;

    /** Every appointment time offered by the clinic, from earliest to latest. */
    private final ArrayList<TimeSlot> slots;

    /** The hour the clinic starts taking appointments. */
    private final int startHour;

    /** The hour the clinic stops taking appointments (the last slot ends here). */
    private final int endHour;

    /** The length in minutes of one appointment slot. */
    private final int slotMinutes;

    /**
     * Creates the standard clinic schedule: 08:00-16:00 in 30 minute slots.
     */
    public ClinicSchedule() {
        this(DEFAULT_START_HOUR, DEFAULT_END_HOUR, DEFAULT_SLOT_MINUTES);
    }

    /**
     * Creates a clinic schedule with a custom opening time, closing time and slot length.
     *
     * @param startHour   the hour the clinic opens, from 0 to 23
     * @param endHour     the hour the clinic closes, from 0 to 23, later than {@code startHour}
     * @param slotMinutes the length of one appointment in minutes, greater than zero
     * @throws IllegalArgumentException if the arguments do not describe a usable schedule
     */
    public ClinicSchedule(int startHour, int endHour, int slotMinutes) {
        if (startHour < 0 || startHour > 23 || endHour < 0 || endHour > 23) {
            throw new IllegalArgumentException("Clinic opening and closing hours must be 0 to 23.");
        }
        if (endHour <= startHour) {
            throw new IllegalArgumentException(
                    "The clinic must close after it opens, but opened at " + startHour
                            + ":00 and closed at " + endHour + ":00.");
        }
        if (slotMinutes <= 0 || slotMinutes > MINUTES_PER_HOUR) {
            throw new IllegalArgumentException(
                    "An appointment must be between 1 and " + MINUTES_PER_HOUR
                            + " minutes long, but was " + slotMinutes + " minutes.");
        }
        this.startHour = startHour;
        this.endHour = endHour;
        this.slotMinutes = slotMinutes;
        this.slots = new ArrayList<TimeSlot>();

        // Fill the day with slots from the opening time up to (but not including) closing time.
        int minutesFromMidnight = startHour * MINUTES_PER_HOUR;
        int closingMinutes = endHour * MINUTES_PER_HOUR;
        while (minutesFromMidnight + slotMinutes <= closingMinutes) {
            slots.add(new TimeSlot(minutesFromMidnight / MINUTES_PER_HOUR,
                    minutesFromMidnight % MINUTES_PER_HOUR));
            minutesFromMidnight = minutesFromMidnight + slotMinutes;
        }
    }

    /**
     * Returns every appointment time offered by the clinic, from earliest to latest.
     *
     * <p>A copy of the collection is returned so that other code cannot add or remove slots and
     * quietly change the clinic schedule.</p>
     *
     * @return a copy of the clinic's time slots
     */
    public ArrayList<TimeSlot> getSlots() {
        return new ArrayList<TimeSlot>(slots);
    }

    /**
     * Tests whether a time is one of the clinic's appointment times.
     *
     * @param time the time to check
     * @return {@code true} if the clinic books appointments at that time
     */
    public boolean isValidSlot(TimeSlot time) {
        return slots.contains(time);
    }

    /**
     * Returns the number of appointments the clinic can offer in one day.
     *
     * @return the number of time slots in the day
     */
    public int getSlotCount() {
        return slots.size();
    }

    /**
     * Returns the first available time of the day.
     *
     * @return the earliest time slot
     */
    public TimeSlot getFirstSlot() {
        return slots.get(0);
    }

    /**
     * Returns the last available time of the day.
     *
     * @return the latest time slot
     */
    public TimeSlot getLastSlot() {
        return slots.get(slots.size() - 1);
    }

    /**
     * Describes the schedule in one readable line.
     *
     * @return a description such as "08:00-16:00, 30 minute appointments, 16 slots per day"
     */
    public String describe() {
        return String.format("%02d:00-%02d:00", startHour, endHour) + ", " + slotMinutes
                + " minute appointments, " + slots.size() + " slots per day (latest appointment "
                + getLastSlot() + ")";
    }
}
