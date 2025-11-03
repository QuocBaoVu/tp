package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.location.AddLocationCommand.MESSAGE_DUPLICATE_LOCATION;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalAttractions.ALICE;
import static seedu.address.testutil.TypicalAttractions.BENSON;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.location.AddLocationCommand;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyMaplet;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.attraction.Attraction;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.location.Location;
import seedu.address.model.location.LocationName;
import seedu.address.testutil.LocationBuilder;

public class AddLocationCommandTest {

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddLocationCommand(null, Collections.emptyList()));
        assertThrows(NullPointerException.class, () ->
                new AddLocationCommand(new LocationName("Singapore"), null));
    }

    @Test
    public void constructor_emptyIndexes_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new AddLocationCommand(new LocationName("Singapore"), Collections.emptyList()));
    }

    @Test
    public void execute_locationAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingLocationAdded modelStub =
                new ModelStubAcceptingLocationAdded(Arrays.asList(ALICE, BENSON));
        LocationName locationName = new LocationName("Singapore");
        List<Index> indexes = Arrays.asList(Index.fromOneBased(1), Index.fromOneBased(2));
        AddLocationCommand command = new AddLocationCommand(locationName, indexes);

        Location expectedLocation = new Location(locationName,
                Arrays.asList(ALICE, BENSON));
        CommandResult commandResult = command.execute(modelStub);

        assertEquals(String.format(AddLocationCommand.MESSAGE_SUCCESS, Messages.format(expectedLocation)),
                commandResult.getFeedbackToUser());
        assertEquals(Collections.singletonList(expectedLocation), modelStub.getLocationsAdded());
    }

    @Test
    public void execute_duplicateLocation_throwsCommandException() {
        ModelStubWithLocation modelStub = new ModelStubWithLocation(Arrays.asList(ALICE, BENSON),
                Collections.singletonList(new LocationBuilder().withLocationName("Singapore")
                        .withAttractions(ALICE)
                        .build()));
        LocationName locationName = new LocationName("Singapore");
        List<Index> indexes = Collections.singletonList(Index.fromOneBased(1));
        AddLocationCommand command = new AddLocationCommand(locationName, indexes);

        assertThrows(CommandException.class, MESSAGE_DUPLICATE_LOCATION, (
            ) -> command.execute(modelStub));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        ModelStubAcceptingLocationAdded modelStub =
                new ModelStubAcceptingLocationAdded(Collections.singletonList(ALICE));
        LocationName locationName = new LocationName("Singapore");
        List<Index> indexes = Arrays.asList(Index.fromOneBased(1), Index.fromOneBased(2));
        AddLocationCommand command = new AddLocationCommand(locationName, indexes);

        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_ATTRACTION_DISPLAYED_INDEX, (
            ) -> command.execute(modelStub));
    }

    @Test
    public void equals() {
        LocationName singapore = new LocationName("Singapore");
        LocationName sentosa = new LocationName("Sentosa");
        List<Index> firstIndexes = Collections.singletonList(Index.fromOneBased(1));
        List<Index> secondIndexes = Arrays.asList(Index.fromOneBased(1), Index.fromOneBased(2));

        AddLocationCommand addSingaporeCommand = new AddLocationCommand(singapore, firstIndexes);
        AddLocationCommand addSingaporeCommandCopy = new AddLocationCommand(singapore, firstIndexes);
        AddLocationCommand addSentosaCommand = new AddLocationCommand(sentosa, firstIndexes);
        AddLocationCommand addSingaporeWithDifferentIndexes = new AddLocationCommand(singapore, secondIndexes);

        assertTrue(addSingaporeCommand.equals(addSingaporeCommand));
        assertTrue(addSingaporeCommand.equals(addSingaporeCommandCopy));
        assertFalse(addSingaporeCommand.equals(addSentosaCommand));
        assertFalse(addSingaporeCommand.equals(addSingaporeWithDifferentIndexes));
        assertFalse(addSingaporeCommand.equals(null));
        assertFalse(addSingaporeCommand.equals(1));
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
        public boolean isAttractionInAnyLocation(Attraction attraction) {
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

    private static class ModelStubAcceptingLocationAdded extends ModelStub {
        protected final ObservableList<Location> locations = FXCollections.observableArrayList();
        private final ObservableList<Attraction> filteredAttractions = FXCollections.observableArrayList();

        ModelStubAcceptingLocationAdded(List<Attraction> attractions) {
            filteredAttractions.setAll(attractions);
        }

        @Override
        public ObservableList<Attraction> getFilteredAttractionList() {
            return filteredAttractions;
        }

        @Override
        public boolean hasLocationName(LocationName locationName) {
            requireNonNull(locationName);
            return locations.stream().anyMatch(location -> location.getName().equals(locationName));
        }

        @Override
        public void addLocation(Location location) {
            locations.add(location);
        }

        @Override
        public ObservableList<Location> getLocationList() {
            return FXCollections.unmodifiableObservableList(locations);
        }

        List<Location> getLocationsAdded() {
            return new ArrayList<>(locations);
        }
    }

    private static class ModelStubWithLocation extends ModelStubAcceptingLocationAdded {
        ModelStubWithLocation(List<Attraction> attractions, List<Location> locations) {
            super(attractions);
            this.locations.addAll(locations);
        }

        @Override
        public boolean hasLocationName(LocationName locationName) {
            requireNonNull(locationName);
            return locations.stream().anyMatch(location -> location.getName().equals(locationName));
        }

        @Override
        public void addLocation(Location location) {
            locations.add(location);
        }

        @Override
        public ObservableList<Location> getLocationList() {
            return FXCollections.unmodifiableObservableList(locations);
        }
    }
}
