package seedu.address.model.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalLocations.CBD;
import static seedu.address.testutil.TypicalLocations.SINGAPORE;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.attraction.Name;
import seedu.address.testutil.AttractionBuilder;
import seedu.address.testutil.LocationBuilder;

public class LocationTest {

    @Test
    public void constructor_nullFields_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Location(null,
                List.of(new AttractionBuilder().build())));
        assertThrows(NullPointerException.class, () -> new Location(new LocationName("Valid"), null));
    }

    @Test
    public void constructor_emptyAttractions_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Location(new LocationName("Empty"), List.of()));
    }

    @Test
    public void getAttractionNames_modifySet_throwsUnsupportedOperationException() {
        Location location = SINGAPORE;
        assertThrows(UnsupportedOperationException.class, () -> location.getAttractionNames().add(new Name("New")));
    }

    @Test
    public void isSameLocation() {
        // same object -> returns true
        assertTrue(SINGAPORE.isSameLocation(SINGAPORE));

        // null -> returns false
        assertFalse(SINGAPORE.isSameLocation(null));

        // same name -> returns true
        Location editedSingapore = new LocationBuilder(SINGAPORE)
                .withAttractions(new AttractionBuilder().withName("Alice Pauline").build())
                .build();
        assertTrue(SINGAPORE.isSameLocation(editedSingapore));

        // different name -> returns false
        assertFalse(SINGAPORE.isSameLocation(CBD));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Location singaporeCopy = new LocationBuilder(SINGAPORE).build();
        assertTrue(SINGAPORE.equals(singaporeCopy));

        // same object -> returns true
        assertTrue(SINGAPORE.equals(SINGAPORE));

        // null -> returns false
        assertFalse(SINGAPORE.equals(null));

        // different type -> returns false
        assertFalse(SINGAPORE.equals(5));

        // different location -> returns false
        assertFalse(SINGAPORE.equals(CBD));
    }

    @Test
    public void toStringMethod() {
        Location location = SINGAPORE;
        String expected = Location.class.getCanonicalName() + "{"
                + "name=" + location.getName()
                + ", attractions=" + location.getAttractions() + "}";
        assertEquals(expected, location.toString());
    }
}
