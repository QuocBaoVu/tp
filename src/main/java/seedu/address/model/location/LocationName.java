package seedu.address.model.location;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Locale;

/**
 * Represents the name of a {@link Location} in Maplet.
 * Guarantees: immutable; is valid as declared in {@link #isValidLocationName(String)}.
 */
public class LocationName {

    public static final String MESSAGE_CONSTRAINTS =
            "Location names should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the location name must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String value;

    /**
     * Constructs a {@code LocationName}.
     *
     * @param name A valid location name.
     */
    public LocationName(String name) {
        requireNonNull(name);
        checkArgument(isValidLocationName(name), MESSAGE_CONSTRAINTS);
        value = name;
    }

    /**
     * Returns true if a given string is a valid location name.
     */
    public static boolean isValidLocationName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof LocationName)) {
            return false;
        }

        LocationName otherLocationName = (LocationName) other;
        return value.equalsIgnoreCase(otherLocationName.value);
    }

    @Override
    public int hashCode() {
        return value.toLowerCase(Locale.ROOT).hashCode();
    }
}
