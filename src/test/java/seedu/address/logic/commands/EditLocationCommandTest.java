package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.location.EditLocationCommand.Action.ADD;
import static seedu.address.logic.commands.location.EditLocationCommand.Action.REMOVE;
import static seedu.address.logic.commands.location.EditLocationCommand.MESSAGE_ATTRACTION_ALREADY_PRESENT;
import static seedu.address.logic.commands.location.EditLocationCommand.MESSAGE_ATTRACTION_NOT_PRESENT;
import static seedu.address.logic.commands.location.EditLocationCommand.MESSAGE_EDIT_LOCATION_SUCCESS;
import static seedu.address.logic.commands.location.EditLocationCommand.MESSAGE_LOCATION_NOT_FOUND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalAttractions.ALICE;
import static seedu.address.testutil.TypicalAttractions.BENSON;
import static seedu.address.testutil.TypicalAttractions.CARL;
import static seedu.address.testutil.TypicalLocations.SINGAPORE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.location.EditLocationCommand;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyMaplet;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.attraction.Attraction;
import seedu.address.model.attraction.Name;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.location.Location;
import seedu.address.model.location.LocationName;

public class EditLocationCommandTest {

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new EditLocationCommand(null,
                ADD, Index.fromOneBased(1)));
        assertThrows(NullPointerException.class, () ->
                new EditLocationCommand(new LocationName("Singapore"),
                        null, Index.fromOneBased(1)));
        assertThrows(NullPointerException.class, () ->
                new EditLocationCommand(new LocationName("Singapore"), ADD, null));
    }

    @Test
    public void execute_addAttraction_success() throws Exception {
        Location initialLocation = SINGAPORE;
        ModelStubWithLocations modelStub = new ModelStubWithLocations(Arrays.asList(ALICE, BENSON, CARL),
                new ArrayList<>(Collections.singletonList(initialLocation)));
        EditLocationCommand command = new EditLocationCommand(initialLocation.getName(),
                ADD, Index.fromOneBased(3));

        Set<String> expectedAttractionNames = new HashSet<>(Arrays.asList(
                ALICE.getName().fullName, BENSON.getName().fullName, CARL.getName().fullName));

        CommandResult commandResult = command.execute(modelStub);
        Location updatedLocation = modelStub.getLocationList().get(0);

        assertEquals(String.format(MESSAGE_EDIT_LOCATION_SUCCESS, Messages.format(updatedLocation)),
                commandResult.getFeedbackToUser());
        assertEquals(expectedAttractionNames, extractAttractionNames(updatedLocation));
    }

    @Test
    public void execute_removeAttraction_success() throws Exception {
        Location initialLocation = SINGAPORE;
        ModelStubWithLocations modelStub = new ModelStubWithLocations(Arrays.asList(ALICE, BENSON),
                new ArrayList<>(Collections.singletonList(initialLocation)));
        EditLocationCommand command = new EditLocationCommand(initialLocation
                .getName(), REMOVE, Index.fromOneBased(1));

        CommandResult commandResult = command.execute(modelStub);
        Location updatedLocation = modelStub.getLocationList().get(0);

        assertEquals(String.format(MESSAGE_EDIT_LOCATION_SUCCESS, Messages.format(updatedLocation)),
                commandResult.getFeedbackToUser());
        assertEquals(Collections.singleton(BENSON.getName().fullName), extractAttractionNames(updatedLocation));
    }

    @Test
    public void execute_locationNotFound_throwsCommandException() {
        ModelStubWithLocations modelStub = new ModelStubWithLocations(Arrays.asList(ALICE, BENSON),
                new ArrayList<>());
        EditLocationCommand command = new EditLocationCommand(new LocationName("Sentosa"),
                ADD, Index.fromOneBased(1));

        assertThrows(CommandException.class, MESSAGE_LOCATION_NOT_FOUND, () -> command.execute(modelStub));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Location initialLocation = SINGAPORE;
        ModelStubWithLocations modelStub = new ModelStubWithLocations(Collections.singletonList(ALICE),
                new ArrayList<>(Collections.singletonList(initialLocation)));
        EditLocationCommand command = new EditLocationCommand(initialLocation.getName(),
                ADD, Index.fromOneBased(2));

        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_ATTRACTION_DISPLAYED_INDEX, (
                ) -> command.execute(modelStub));
    }

    @Test
    public void execute_addExistingAttraction_throwsCommandException() {
        Location initialLocation = SINGAPORE;
        ModelStubWithLocations modelStub = new ModelStubWithLocations(Arrays.asList(ALICE, BENSON),
                new ArrayList<>(Collections.singletonList(initialLocation)));
        EditLocationCommand command = new EditLocationCommand(initialLocation.getName(),
                ADD, Index.fromOneBased(1));

        assertThrows(CommandException.class, MESSAGE_ATTRACTION_ALREADY_PRESENT, () -> command.execute(modelStub));
    }

    @Test
    public void execute_removeNonExistingAttraction_throwsCommandException() {
        Location initialLocation = SINGAPORE;
        ModelStubWithLocations modelStub = new ModelStubWithLocations(Arrays.asList(ALICE, CARL),
                new ArrayList<>(Collections.singletonList(initialLocation)));
        EditLocationCommand command = new EditLocationCommand(initialLocation.getName(),
                REMOVE, Index.fromOneBased(2));

        assertThrows(CommandException.class, MESSAGE_ATTRACTION_NOT_PRESENT, () -> command.execute(modelStub));
    }

    @Test
    public void execute_removeLastAttraction_throwsCommandException() {
        Location singleAttractionLocation = new Location(new LocationName("Standalone"),
                Collections.singletonList(ALICE));
        ModelStubWithLocations modelStub = new ModelStubWithLocations(Collections.singletonList(ALICE),
                new ArrayList<>(Collections.singletonList(singleAttractionLocation)));
        EditLocationCommand command = new EditLocationCommand(singleAttractionLocation.getName(), REMOVE,
                Index.fromOneBased(1));

        assertThrows(CommandException.class, Location.MESSAGE_EMPTY_ATTRACTIONS, () -> command.execute(modelStub));
    }

    @Test
    public void equals() {
        LocationName singapore = SINGAPORE.getName();
        LocationName sentosa = new LocationName("Sentosa");
        Index firstIndex = Index.fromOneBased(1);
        Index secondIndex = Index.fromOneBased(2);

        EditLocationCommand addSingaporeFirstIndex = new EditLocationCommand(singapore, ADD, firstIndex);
        EditLocationCommand addSingaporeSecondIndex = new EditLocationCommand(singapore, ADD, secondIndex);
        EditLocationCommand removeSingaporeFirstIndex = new EditLocationCommand(singapore, REMOVE, firstIndex);
        EditLocationCommand addSentosaFirstIndex = new EditLocationCommand(sentosa, ADD, firstIndex);

        assertTrue(addSingaporeFirstIndex.equals(addSingaporeFirstIndex));
        assertFalse(addSingaporeFirstIndex.equals(null));
        assertFalse(addSingaporeFirstIndex.equals(1));
        assertTrue(addSingaporeFirstIndex.equals(new EditLocationCommand(singapore, ADD, firstIndex)));
        assertFalse(addSingaporeFirstIndex.equals(addSingaporeSecondIndex));
        assertFalse(addSingaporeFirstIndex.equals(removeSingaporeFirstIndex));
        assertFalse(addSingaporeFirstIndex.equals(addSentosaFirstIndex));
    }

    private Set<String> extractAttractionNames(Location location) {
        Set<String> names = new HashSet<>();
        for (Name name : location.getAttractionNames()) {
            names.add(name.fullName);
        }
        return names;
    }

    private static class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getMapletFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setMapletFilePath(Path mapletFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setMaplet(ReadOnlyMaplet maplet) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyMaplet getMaplet() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasAttraction(Attraction attraction) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasLocation(Location location) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasLocationName(LocationName locationName) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean isAttractionInAnyLocation(Attraction attraction) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean isAttractionInAnyItinerary(Attraction attraction) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteAttraction(Attraction target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addAttraction(Attraction attraction) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addLocation(Location location) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setLocation(Location target, Location editedLocation) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAttraction(Attraction target, Attraction editedAttraction) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteLocation(LocationName locationName) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Attraction> getFilteredAttractionList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Location> getLocationList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredAttractionList(Predicate<Attraction> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateSortedAttractionList(Comparator<Attraction> comparator) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasItinerary(Itinerary itinerary) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteItinerary(Itinerary itinerary) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addItinerary(Itinerary itinerary) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setItinerary(Itinerary target, Itinerary editedItinerary) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Itinerary> getFilteredItineraryList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredItineraryList(Predicate<Itinerary> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateSortedItineraryList(Comparator<Itinerary> comparator) {
            throw new AssertionError("This method should not be called.");
        }
    }

    private static class ModelStubWithLocations extends ModelStub {
        private final ObservableList<Location> locations = FXCollections.observableArrayList();
        private final ObservableList<Attraction> filteredAttractions = FXCollections.observableArrayList();

        ModelStubWithLocations(List<Attraction> attractions, List<Location> initialLocations) {
            filteredAttractions.setAll(attractions);
            locations.setAll(initialLocations);
        }

        @Override
        public ObservableList<Attraction> getFilteredAttractionList() {
            return filteredAttractions;
        }

        @Override
        public ObservableList<Location> getLocationList() {
            return FXCollections.unmodifiableObservableList(locations);
        }

        @Override
        public boolean hasLocationName(LocationName locationName) {
            requireNonNull(locationName);
            return locations.stream().anyMatch(location -> location.getName().equals(locationName));
        }

        @Override
        public void setLocation(Location target, Location editedLocation) {
            requireNonNull(target);
            requireNonNull(editedLocation);
            int index = locations.indexOf(target);
            if (index == -1) {
                throw new AssertionError("Target location should exist.");
            }
            locations.set(index, editedLocation);
        }
    }
}
