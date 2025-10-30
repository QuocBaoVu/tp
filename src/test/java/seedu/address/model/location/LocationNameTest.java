package seedu.address.model.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class LocationNameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LocationName(null));
    }

    @Test
    public void constructor_invalidLocationName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new LocationName(invalidName));
    }

    @Test
    public void isValidLocationName() {
        // null name
        assertThrows(NullPointerException.class, () -> LocationName.isValidLocationName(null));

        // invalid name
        assertFalse(LocationName.isValidLocationName(""));
        assertFalse(LocationName.isValidLocationName(" "));
        assertFalse(LocationName.isValidLocationName("^"));
        assertFalse(LocationName.isValidLocationName("Sentosa*"));

        // valid name
        assertTrue(LocationName.isValidLocationName("Singapore"));
        assertTrue(LocationName.isValidLocationName("Area 51"));
        assertTrue(LocationName.isValidLocationName("Jurong East"));
    }

    @Test
    public void equals() {
        LocationName locationName = new LocationName("Valid Location");

        // same values -> returns true
        assertTrue(locationName.equals(new LocationName("Valid Location")));

        // same object -> returns true
        assertTrue(locationName.equals(locationName));

        // null -> returns false
        assertFalse(locationName.equals(null));

        // different types -> returns false
        assertFalse(locationName.equals(5));

        // different values -> returns false
        assertFalse(locationName.equals(new LocationName("Other Location")));

        // same values with different casing -> returns true
        assertTrue(locationName.equals(new LocationName("valid location")));

        // hashCode is case-insensitive
        assertEquals(locationName.hashCode(), new LocationName("valid location").hashCode());
    }
}
