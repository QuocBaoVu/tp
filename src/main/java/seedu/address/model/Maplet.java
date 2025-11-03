package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.attraction.Attraction;
import seedu.address.model.attraction.UniqueAttractionList;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.itinerary.UniqueItineraryList;
import seedu.address.model.location.Location;
import seedu.address.model.location.LocationName;
import seedu.address.model.location.UniqueLocationList;

/**
 * Wraps all data at the Maplet level. Duplicates are not allowed (by
 * .isSameAttraction comparison)
 */
public class Maplet implements ReadOnlyMaplet {

    private final UniqueAttractionList attractions;
    private final UniqueItineraryList itineraries;
    private final UniqueLocationList locations;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        attractions = new UniqueAttractionList();
        itineraries = new UniqueItineraryList();
        locations = new UniqueLocationList();

    }

    public Maplet() {
    }

    /**
     * Creates a Maplet using the Attractions in the {@code toBeCopied}
     */
    public Maplet(ReadOnlyMaplet toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the attraction list with {@code attractions}.
     * {@code attractions} must not contain duplicate attractions.
     */
    public void setAttractions(List<Attraction> attractions) {
        this.attractions.setAttractions(attractions);
    }

    /**
     *
     */
    public void setLocations(List<Location> locations) {
        this.locations.setLocations(locations);
    }
    /**
     * Resets the existing data of this {@code Maplet} with {@code newData}.
     */
    public void resetData(ReadOnlyMaplet newData) {
        requireNonNull(newData);

        setAttractions(newData.getAttractionList());
        setItineraries(newData.getItineraryList());
        setLocations(newData.getLocationList());
    }

    //// attraction-level operations

    /**
     * Returns true if a attraction with the same identity as {@code attraction} exists in the Maplet.
     */
    public boolean hasAttraction(Attraction attraction) {
        requireNonNull(attraction);
        return attractions.contains(attraction);
    }

    /**
     * Adds an attraction to the Maplet. The attraction must not already exist in
     * the Maplet.
     */
    public void addAttraction(Attraction p) {
        attractions.add(p);
    }

    /**
     * Replaces the given attraction {@code target} in the list with
     * {@code editedAttraction}. {@code target} must exist in the Maplet. The
     * attraction identity of {@code editedAttraction} must not be the same as
     * another existing attraction in the Maplet.
     */
    public void setAttraction(Attraction target, Attraction editedAttraction) {
        requireNonNull(editedAttraction);

        attractions.setAttraction(target, editedAttraction);
    }

    /**
     * Removes {@code key} from this {@code Maplet}. {@code key} must exist in
     * the Maplet.
     */
    public void removeAttraction(Attraction key) {
        attractions.remove(key);
    }

    //// itinerary methods

    /**
     * Replaces the contents of the itinerary list with {@code itineraries}.
     * {@code itineraries} must not contain duplicate itineraries.
     */
    public void setItineraries(List<Itinerary> itineraries) {
        this.itineraries.setItineraries(itineraries);
    }

    /**
     * Returns true if an itinerary with the same identity as {@code itinerary}
     * exists in the Maplet.
     */
    public boolean hasItinerary(Itinerary itinerary) {
        requireNonNull(itinerary);
        return itineraries.contains(itinerary);
    }

    /**
     * Adds the given itinerary. The itinerary must not already exist in the
     * Maplet.
     */
    public void addItinerary(Itinerary itinerary) {
        itineraries.add(itinerary);
    }

    /**
     * Replaces the given itinerary {@code target} with {@code editedItinerary}.
     * {@code target} must exist in the Maplet. The identity of
     * {@code editedItinerary} must not be the same as another existing
     * itinerary in the Maplet.
     */
    public void setItinerary(Itinerary target, Itinerary editedItinerary) {
        requireNonNull(editedItinerary);
        itineraries.setItinerary(target, editedItinerary);
    }

    /**
     * Removes {@code itinerary} from the Maplet. {@code itinerary} must exist
     * in the Maplet.
     */
    public void removeItinerary(Itinerary itinerary) {
        itineraries.remove(itinerary);
    }

    /**
     * Returns true if the given attraction is referenced in any itinerary.
     */
    public boolean isAttractionInAnyItinerary(Attraction attraction) {
        requireNonNull(attraction);
        return itineraries.asUnmodifiableObservableList().stream()
                .anyMatch(itinerary -> itinerary.hasAttraction(attraction));
    }

    //// location-level operations

    /**
     * Returns true if the given attraction is referenced in any location.
     */
    public boolean isAttractionInAnyLocation(Attraction attraction) {
        requireNonNull(attraction);
        return locations.asUnmodifiableObservableList().stream()
                .anyMatch(location -> location.hasAttraction(attraction));
    }

    /**
     * Returns true if a location with the same identity as {@code location} exists in the Maplet.
     */
    public boolean hasLocation(Location location) {
        requireNonNull(location);
        return locations.contains(location);
    }

    /**
     * Returns true if a location with the same name as {@code locationName} exists in the Maplet.
     */
    public boolean hasLocationName(LocationName locationName) {
        requireNonNull(locationName);
        return locations.containsLocationName(locationName);
    }

    /**
     * Adds a location to the Maplet.
     * The location must not already exist in the Maplet.
     */
    public void addLocation(Location location) {
        locations.add(location);
    }

    /**
     * Removes the location with {@code locationName} from this {@code Maplet}.
     * {@code locationName} must exist in the Maplet.
     */
    public void removeLocation(LocationName locationName) {
        locations.remove(locationName);
    }

    /**
     * Replaces the given location {@code target} with {@code editedLocation}.
     * {@code target} must exist in the Maplet.
     */
    public void setLocation(Location target, Location editedLocation) {
        requireNonNull(target);
        requireNonNull(editedLocation);
        locations.setLocation(target, editedLocation);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("attractions", attractions)
                .add("itineraries", itineraries)
                .add("locations", locations)
                .toString();
    }

    @Override
    public ObservableList<Attraction> getAttractionList() {
        return attractions.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Itinerary> getItineraryList() {
        return itineraries.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Location> getLocationList() {
        return locations.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Maplet)) {
            return false;
        }

        Maplet otherMaplet = (Maplet) other;
        return attractions.equals(otherMaplet.attractions)
                && itineraries.equals(otherMaplet.itineraries)
                && locations.equals(otherMaplet.locations);
    }

    @Override
    public int hashCode() {
        // Update the hashcode with prime number to account for attractions and itineraries
        return attractions.hashCode() * 31 + itineraries.hashCode();
    }
}
