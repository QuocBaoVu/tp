package seedu.address.logic.commands.location;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION_ACTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION_ATTRACTION_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION_NAME;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.attraction.Attraction;
import seedu.address.model.attraction.Name;
import seedu.address.model.location.Location;
import seedu.address.model.location.LocationName;

/**
 * Represents a command that edits an existing {@code Location} by either adding or removing
 * an {@code Attraction} identified by its index from the current attraction list.
 * This command updates the {@code Model}'s location list accordingly.
 */
public class EditLocationCommand extends Command {
    public static final String COMMAND_WORD = "editlocation";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the specified location by adding or removing "
            + "an attraction. "
            + "Parameters: " + PREFIX_LOCATION_NAME + "LOCATION_NAME "
            + PREFIX_LOCATION_ACTION + "ACTION " + PREFIX_LOCATION_ATTRACTION_INDEX + "ATTRACTION_INDEX\n"
            + "ACTION must be ADD or REMOVE.\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_LOCATION_NAME + "Singapore "
            + PREFIX_LOCATION_ACTION + "ADD " + PREFIX_LOCATION_ATTRACTION_INDEX + "3";

    public static final String MESSAGE_LOCATION_NOT_FOUND = "The specified location does not exist in the maplet.";
    public static final String MESSAGE_INVALID_ACTION = "Action must be either ADD or REMOVE.";
    public static final String MESSAGE_ATTRACTION_ALREADY_PRESENT =
            "The attraction is already associated with the specified location.";
    public static final String MESSAGE_ATTRACTION_NOT_PRESENT =
            "The attraction is not associated with the specified location.";
    public static final String MESSAGE_EDIT_LOCATION_SUCCESS = "Location updated: %1$s";

    private final LocationName locationName;
    private final Action action;
    private final Index attractionIndex;

    /**
     * Supported actions for the command.
     */
    public enum Action {
        ADD,
        REMOVE;

        /**
         * Parses the {@code value} into an {@link Action}.
         *
         * @throws IllegalArgumentException if {@code value} does not map to a valid action.
         */
        public static Action fromString(String value) {
            requireNonNull(value);
            switch (value.trim().toUpperCase()) {
            case "ADD":
                return ADD;
            case "REMOVE":
                return REMOVE;
            default:
                throw new IllegalArgumentException(MESSAGE_INVALID_ACTION);
            }
        }
    }

    /**
     * Creates an {@code EditLocationCommand} to add or remove an attraction from the specified location.
     */
    public EditLocationCommand(LocationName locationName, Action action, Index attractionIndex) {
        this.locationName = requireNonNull(locationName);
        this.action = requireNonNull(action);
        this.attractionIndex = requireNonNull(attractionIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        ObservableList<Attraction> lastShownList = model.getFilteredAttractionList();
        if (attractionIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ATTRACTION_DISPLAYED_INDEX);
        }

        Attraction attraction = lastShownList.get(attractionIndex.getZeroBased());

        Optional<Location> optionalTargetLocation = model.getLocationList().stream()
                .filter(location -> location.getName().equals(locationName))
                .findFirst();

        if (optionalTargetLocation.isEmpty()) {
            throw new CommandException(MESSAGE_LOCATION_NOT_FOUND);
        }

        Location targetLocation = optionalTargetLocation.get();
        List<Attraction> updatedAttractions = new ArrayList<>(targetLocation.getAttractions());

        switch (action) {
        case ADD:
            if (targetLocation.hasAttraction(attraction)) {
                throw new CommandException(MESSAGE_ATTRACTION_ALREADY_PRESENT);
            }
            updatedAttractions.add(attraction);
            break;
        case REMOVE:
            if (!updatedAttractions.remove(attraction)) {
                throw new CommandException(MESSAGE_ATTRACTION_NOT_PRESENT);
            }
            if (updatedAttractions.isEmpty()) {
                throw new CommandException(Location.MESSAGE_EMPTY_ATTRACTIONS);
            }
            break;
        default:
            throw new CommandException(MESSAGE_INVALID_ACTION);
        }

        Location editedLocation = new Location(locationName, updatedAttractions);
        model.setLocation(targetLocation, editedLocation);
        return new CommandResult(String.format(MESSAGE_EDIT_LOCATION_SUCCESS, Messages.format(editedLocation)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditLocationCommand)) {
            return false;
        }

        EditLocationCommand otherCommand = (EditLocationCommand) other;
        return locationName.equals(otherCommand.locationName)
                && action == otherCommand.action
                && attractionIndex.equals(otherCommand.attractionIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationName, action, attractionIndex);
    }

    @Override
    public String toString() {
        return EditLocationCommand.class.getCanonicalName()
                + "{locationName=" + locationName
                + ", action=" + action
                + ", attractionIndex=" + attractionIndex + "}";
    }
}

