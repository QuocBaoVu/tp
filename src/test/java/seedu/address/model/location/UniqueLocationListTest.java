package seedu.address.model.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalLocations.CBD;
import static seedu.address.testutil.TypicalLocations.SINGAPORE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.location.exceptions.DuplicateLocationException;
import seedu.address.model.location.exceptions.LocationNotFoundException;
import seedu.address.testutil.LocationBuilder;

public class UniqueLocationListTest {

    private final UniqueLocationList uniqueLocationList = new UniqueLocationList();

    @Test
    public void contains_nullLocation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueLocationList.contains(null));
    }

    @Test
    public void contains_locationNotInList_returnsFalse() {
        assertFalse(uniqueLocationList.contains(SINGAPORE));
    }

    @Test
    public void contains_locationInList_returnsTrue() {
        uniqueLocationList.add(SINGAPORE);
        assertTrue(uniqueLocationList.contains(SINGAPORE));
    }

    @Test
    public void containsLocationName_locationWithSameNameInList_returnsTrue() {
        uniqueLocationList.add(SINGAPORE);
        assertTrue(uniqueLocationList.containsLocationName(SINGAPORE.getName()));
    }

    @Test
    public void add_nullLocation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueLocationList.add(null));
    }

    @Test
    public void add_duplicateLocation_throwsDuplicateLocationException() {
        uniqueLocationList.add(SINGAPORE);
        assertThrows(DuplicateLocationException.class, () -> uniqueLocationList.add(SINGAPORE));
    }

    @Test
    public void add_locationWithSameNameDifferentCase_throwsDuplicateLocationException() {
        uniqueLocationList.add(SINGAPORE);
        Location singaporeLowerCase = new LocationBuilder(SINGAPORE).withLocationName("singapore").build();
        assertThrows(DuplicateLocationException.class, () -> uniqueLocationList.add(singaporeLowerCase));
    }

    @Test
    public void remove_nullLocation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueLocationList.remove((Location) null));
    }

    @Test
    public void remove_locationDoesNotExist_throwsLocationNotFoundException() {
        assertThrows(LocationNotFoundException.class, () -> uniqueLocationList.remove(SINGAPORE));
    }

    @Test
    public void remove_existingLocation_removesLocation() {
        uniqueLocationList.add(SINGAPORE);
        uniqueLocationList.remove(SINGAPORE);
        UniqueLocationList expectedList = new UniqueLocationList();
        assertEquals(expectedList, uniqueLocationList);
    }

    @Test
    public void removeByName_existingLocation_removesLocation() {
        uniqueLocationList.add(SINGAPORE);
        uniqueLocationList.remove(SINGAPORE.getName());
        UniqueLocationList expectedList = new UniqueLocationList();
        assertEquals(expectedList, uniqueLocationList);
    }

    @Test
    public void removeByName_locationDoesNotExist_throwsLocationNotFoundException() {
        assertThrows(LocationNotFoundException.class, () -> uniqueLocationList.remove(SINGAPORE.getName()));
    }

    @Test
    public void setLocations_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueLocationList.setLocations((List<Location>) null));
    }

    @Test
    public void setLocations_replacesData_success() {
        uniqueLocationList.add(SINGAPORE);
        List<Location> locationList = Collections.singletonList(CBD);
        uniqueLocationList.setLocations(locationList);
        UniqueLocationList expectedList = new UniqueLocationList();
        expectedList.add(CBD);
        assertEquals(expectedList, uniqueLocationList);
    }

    @Test
    public void setLocations_withDuplicateLocations_throwsDuplicateLocationException() {
        List<Location> locationList = Arrays.asList(SINGAPORE, new LocationBuilder(SINGAPORE).build());
        assertThrows(DuplicateLocationException.class, () -> uniqueLocationList.setLocations(locationList));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () ->
                uniqueLocationList.asUnmodifiableObservableList().remove(0));
    }
}
