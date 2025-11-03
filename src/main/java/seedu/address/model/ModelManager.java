package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.attraction.Attraction;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.location.Location;
import seedu.address.model.location.LocationName;

/**
 * Represents the in-memory model of the Maplet data.
 */
public class ModelManager implements Model {

    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final Maplet maplet;
    private final UserPrefs userPrefs;
    private final FilteredList<Attraction> filteredAttractions;
    private final SortedList<Attraction> sortedAttractions;
    private final FilteredList<Itinerary> filteredItineraries;
    private final SortedList<Itinerary> sortedItineraries;

    /**
     * Initializes a ModelManager with the given Maplet and userPrefs.
     */
    public ModelManager(ReadOnlyMaplet maplet, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(maplet, userPrefs);

        logger.fine("Initializing with Maplet: " + maplet + " and user prefs " + userPrefs);

        this.maplet = new Maplet(maplet);
        this.userPrefs = new UserPrefs(userPrefs);
        sortedAttractions = new SortedList<>(this.maplet.getAttractionList());
        sortedItineraries = new SortedList<>(this.maplet.getItineraryList());
        filteredAttractions = new FilteredList<>(sortedAttractions);
        filteredItineraries = new FilteredList<>(sortedItineraries);
    }

    public ModelManager() {
        this(new Maplet(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================
    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getMapletFilePath() {
        return userPrefs.getMapletFilePath();
    }

    @Override
    public void setMapletFilePath(Path mapletFilePath) {
        requireNonNull(mapletFilePath);
        userPrefs.setMapletFilePath(mapletFilePath);
    }

    //=========== Maplet ================================================================================
    @Override
    public void setMaplet(ReadOnlyMaplet maplet) {
        this.maplet.resetData(maplet);
    }

    @Override
    public ReadOnlyMaplet getMaplet() {
        return maplet;
    }

    @Override
    public boolean hasAttraction(Attraction attraction) {
        requireNonNull(attraction);
        return maplet.hasAttraction(attraction);
    }

    @Override
    public boolean hasLocation(Location location) {
        requireNonNull(location);
        return maplet.hasLocation(location);
    }

    @Override
    public boolean hasLocationName(LocationName locationName) {
        requireNonNull(locationName);
        return maplet.hasLocationName(locationName);
    }

    @Override
    public boolean isAttractionInAnyItinerary(Attraction attraction) {
        requireNonNull(attraction);
        return maplet.isAttractionInAnyItinerary(attraction);
    }

    @Override
    public boolean isAttractionInAnyLocation(Attraction attraction) {
        requireNonNull(attraction);
        return maplet.isAttractionInAnyLocation(attraction);
    }

    @Override
    public void deleteAttraction(Attraction target) {
        maplet.removeAttraction(target);
    }

    @Override
    public void addAttraction(Attraction attraction) {
        maplet.addAttraction(attraction);
        updateFilteredAttractionList(PREDICATE_SHOW_ALL_ATTRACTIONS);
    }

    @Override
    public void addLocation(Location location) {
        maplet.addLocation(location);
    }

    @Override
    public void setLocation(Location target, Location editedLocation) {
        requireAllNonNull(target, editedLocation);
        maplet.setLocation(target, editedLocation);
    }

    @Override
    public void setAttraction(Attraction target, Attraction editedAttraction) {
        requireAllNonNull(target, editedAttraction);

        maplet.setAttraction(target, editedAttraction);

        for (Itinerary itinerary : maplet.getItineraryList()) {
            if (itinerary.hasAttraction(target)) {
                itinerary.removeAttraction(target);
                itinerary.addAttraction(editedAttraction);
            }
        }

        for (Location location : maplet.getLocationList()) {
            if (location.hasAttraction(target)) {
                location.removeAttraction(target);
                location.addAttraction(editedAttraction);
            }
        }

    }

    @Override
    public void deleteLocation(LocationName locationName) {
        maplet.removeLocation(locationName);
    }

    // Logic duplication from attraction methods
    @Override
    public boolean hasItinerary(Itinerary itinerary) {
        requireNonNull(itinerary);
        return maplet.hasItinerary(itinerary);
    }

    @Override
    public void deleteItinerary(Itinerary itinerary) {
        maplet.removeItinerary(itinerary);
    }

    @Override
    public void addItinerary(Itinerary itinerary) {
        maplet.addItinerary(itinerary);
        updateFilteredItineraryList(PREDICATE_SHOW_ALL_ITINERARIES);
    }

    @Override
    public void setItinerary(Itinerary target, Itinerary editedItinerary) {
        requireAllNonNull(target, editedItinerary);
        maplet.setItinerary(target, editedItinerary);
    }

    //=========== Filtered Attraction List Accessors =============================================================
    /**
     * Returns an unmodifiable view of the list of {@code Attraction} backed by
     * the internal list of {@code versionedMaplet}
     */
    @Override
    public ObservableList<Attraction> getFilteredAttractionList() {
        return filteredAttractions;
    }

    @Override
    public ObservableList<Location> getLocationList() {
        return maplet.getLocationList();
    }

    @Override
    public void updateFilteredAttractionList(Predicate<Attraction> predicate) {
        requireNonNull(predicate);
        filteredAttractions.setPredicate(predicate);
    }

    //=========== Sorted Attraction List Accessors =============================================================
    @Override
    public void updateSortedAttractionList(Comparator<Attraction> comparator) {
        sortedAttractions.setComparator(comparator);
    }

    //=========== Filtered Itinerary List Accessors =============================================================
    @Override
    public ObservableList<Itinerary> getFilteredItineraryList() {
        return filteredItineraries;
    }

    @Override
    public void updateFilteredItineraryList(Predicate<Itinerary> predicate) {
        requireNonNull(predicate);
        filteredItineraries.setPredicate(predicate);
    }

    //=========== Sorted Itinerary List Accessors =============================================================
    @Override
    public void updateSortedItineraryList(Comparator<Itinerary> comparator) {
        sortedItineraries.setComparator(comparator);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return maplet.equals(otherModelManager.maplet)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredAttractions.equals(otherModelManager.filteredAttractions)
                && filteredItineraries.equals(otherModelManager.filteredItineraries);
    }

}
