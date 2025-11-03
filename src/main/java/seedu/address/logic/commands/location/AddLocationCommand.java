package seedu.address.logic.commands.location;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION_ATTRACTION_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION_NAME;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.attraction.Attraction;
import seedu.address.model.location.Location;
import seedu.address.model.location.LocationName;

/**
 * Adds a new {@link Location} to the maplet.
 */
public class AddLocationCommand extends Command {

    public static final String COMMAND_WORD = "addlocation";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a location to the maplet. "
            + "Parameters: "
            + PREFIX_LOCATION_NAME + "LOCATION_NAME "
            + PREFIX_LOCATION_ATTRACTION_INDEX + "ATTRACTION_INDEX " + "[" + PREFIX_LOCATION_ATTRACTION_INDEX
            + "ATTRACTION_INDEX]...\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_LOCATION_NAME + "Singapore "
            + PREFIX_LOCATION_ATTRACTION_INDEX + "1 " + PREFIX_LOCATION_ATTRACTION_INDEX + "2";

    public static final String MESSAGE_SUCCESS = "New location added: %1$s";
    public static final String MESSAGE_DUPLICATE_LOCATION = "This location already exists in the maplet.";
    public static final String MESSAGE_NO_ATTRACTIONS = "At least one attraction index must be provided.";
    public static final String MESSAGE_DUPLICATE_ATTRACTION_REFERENCE =
            "Each attraction should be referenced at most once when creating an location.";

    private final LocationName locationName;
    private final List<Index> attractionIndexes;

    /**
     * Creates an {@code AddLocationCommand} to add the specified {@code Location} identified by name and attraction
     * indexes.
     */
    public AddLocationCommand(LocationName locationName, List<Index> attractionIndexes) {
        requireNonNull(locationName);
        requireNonNull(attractionIndexes);
        if (attractionIndexes.isEmpty()) {
            throw new IllegalArgumentException(MESSAGE_NO_ATTRACTIONS);
        }
        this.locationName = locationName;
        this.attractionIndexes = List.copyOf(attractionIndexes);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        ObservableList<Attraction> lastShownList = model.getFilteredAttractionList();
        Set<Attraction> seenAttraction = new HashSet<>();
        List<Attraction> attractions = new ArrayList<>();

        for (Index index : attractionIndexes) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_ATTRACTION_DISPLAYED_INDEX);
            }
            Attraction attraction = lastShownList.get(index.getZeroBased());
            if (!seenAttraction.add(attraction)) {
                throw new CommandException(MESSAGE_DUPLICATE_ATTRACTION_REFERENCE);
            }
            attractions.add(attraction);
        }

        Location location = new Location(locationName, attractions);

        if (model.hasLocationName(locationName)) {
            throw new CommandException(MESSAGE_DUPLICATE_LOCATION);
        }

        model.addLocation(location);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(location)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddLocationCommand)) {
            return false;
        }

        AddLocationCommand otherCommand = (AddLocationCommand) other;
        return locationName.equals(otherCommand.locationName)
                && new ArrayList<>(attractionIndexes).equals(new ArrayList<>(otherCommand.attractionIndexes));
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationName, attractionIndexes);
    }

    @Override
    public String toString() {
        return AddLocationCommand.class.getCanonicalName()
                + "{locationName=" + locationName
                + ", attractionIndexes=" + attractionIndexes + "}";
    }
}
