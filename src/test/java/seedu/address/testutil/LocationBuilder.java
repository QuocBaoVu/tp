package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.attraction.Attraction;
import seedu.address.model.location.Location;
import seedu.address.model.location.LocationName;

/**
 * A utility class to help with building {@link Location} objects.
 */
public class LocationBuilder {

    public static final String DEFAULT_LOCATION_NAME = "Singapore";
    public static final String[] DEFAULT_ATTRACTIONS = {"Alice Pauline"};

    private LocationName locationName;
    private List<Attraction> attractions;

    /**
     * LocationBuilder Constructor
     */
    public LocationBuilder() {
        locationName = new LocationName(DEFAULT_LOCATION_NAME);
        attractions = new ArrayList<>();
        Arrays.stream(DEFAULT_ATTRACTIONS)
                .map(name -> new AttractionBuilder().withName(name).build())
                .forEach(attractions::add);
    }

    /**
     * LocationBuilder Constructor
     * @param location
     */
    public LocationBuilder(Location location) {
        locationName = location.getName();
        attractions = new ArrayList<>(location.getAttractions());
    }

    /**
     * Sets the {@code LocationName} of the {@code Location} that we are building.
     */
    public LocationBuilder withLocationName(String name) {
        locationName = new LocationName(name);
        return this;
    }

    /**
     * Sets the attraction names of the {@code Location} that we are building.
     */
    public LocationBuilder withAttractions(Attraction... attractions) {
        this.attractions = new ArrayList<>(Arrays.asList(attractions));
        return this;
    }

    public Location build() {
        return new Location(locationName, attractions);
    }
}
