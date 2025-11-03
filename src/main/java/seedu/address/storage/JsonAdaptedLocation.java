package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.attraction.Attraction;
import seedu.address.model.attraction.Name;
import seedu.address.model.location.Location;
import seedu.address.model.location.LocationName;

/**
 * Jackson-friendly version of {@link Location}.
 */
class JsonAdaptedLocation {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Location's %s field is missing!";
    public static final String MESSAGE_INVALID_ATTRACTION_REFERENCE =
            "Location references attraction '%s' which does not exist in the attraction list.";
    public static final String MESSAGE_DUPLICATE_ATTRACTION_REFERENCE =
            "Location contains duplicate attraction references.";

    private final String name;
    private final List<String> attractions = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedLocation} with the given location details.
     */
    @JsonCreator
    public JsonAdaptedLocation(@JsonProperty("name") String name,
                               @JsonProperty("attractions") List<String> attractions) {
        this.name = name;
        if (attractions != null) {
            this.attractions.addAll(attractions);
        }
    }

    /**
     * Converts a given {@code Location} into this class for Jackson use.
     */
    public JsonAdaptedLocation(Location source) {
        name = source.getName().toString();
        source.getAttractions().forEach(attraction -> attractions.add(attraction.getName().fullName));
    }

    /**
     * Converts this Jackson-friendly adapted location object into the model's {@code Location} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted location.
     */
    public Location toModelType(List<Attraction> existingAttractions) throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    LocationName.class.getSimpleName()));
        }
        if (!LocationName.isValidLocationName(name)) {
            throw new IllegalValueException(LocationName.MESSAGE_CONSTRAINTS);
        }
        final LocationName modelLocationName = new LocationName(name);

        if (attractions.isEmpty()) {
            throw new IllegalValueException(Location.MESSAGE_EMPTY_ATTRACTIONS);
        }

        final Set<Name> attractionNames = new HashSet<>();
        final List<Attraction> modelAttractions = new ArrayList<>();

        for (String attractionNameString : attractions) {
            if (attractionNameString == null) {
                throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                        Name.class.getSimpleName()));
            }
            if (!Name.isValidName(attractionNameString)) {
                throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
            }
            Name attractionName = new Name(attractionNameString);
            Attraction referencedAttraction = existingAttractions.stream()
                    .filter(attraction -> attraction.getName().equals(attractionName))
                    .findFirst()
                    .orElse(null);
            if (referencedAttraction == null) {
                throw new IllegalValueException(String.format(MESSAGE_INVALID_ATTRACTION_REFERENCE, attractionName));
            }
            boolean isNewEntry = attractionNames.add(attractionName);
            if (!isNewEntry) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_ATTRACTION_REFERENCE);
            }
        }

        try {
            return new Location(modelLocationName, modelAttractions);
        } catch (IllegalArgumentException ex) {
            throw new IllegalValueException(ex.getMessage());
        }
    }
}
