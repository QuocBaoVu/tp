package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showAttractionAtIndex;
import static seedu.address.testutil.TypicalAttractions.ALICE;
import static seedu.address.testutil.TypicalAttractions.getTypicalMaplet;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ATTRACTION;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ATTRACTION;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Maplet;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.attraction.Attraction;
import seedu.address.testutil.ItineraryBuilder;
import seedu.address.testutil.LocationBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalMaplet(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Attraction attractionToDelete = model.getFilteredAttractionList().get(INDEX_FIRST_ATTRACTION.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_ATTRACTION);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_ATTRACTION_SUCCESS,
                Messages.format(attractionToDelete));

        ModelManager expectedModel = new ModelManager(model.getMaplet(), new UserPrefs());
        expectedModel.deleteAttraction(attractionToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredAttractionList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_ATTRACTION_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showAttractionAtIndex(model, INDEX_FIRST_ATTRACTION);

        Attraction attractionToDelete = model.getFilteredAttractionList().get(INDEX_FIRST_ATTRACTION.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_ATTRACTION);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_ATTRACTION_SUCCESS,
                Messages.format(attractionToDelete));

        Model expectedModel = new ModelManager(model.getMaplet(), new UserPrefs());
        expectedModel.deleteAttraction(attractionToDelete);
        showNoAttraction(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showAttractionAtIndex(model, INDEX_FIRST_ATTRACTION);

        Index outOfBoundIndex = INDEX_SECOND_ATTRACTION;
        // ensures that outOfBoundIndex is still in bounds of Maplet list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getMaplet().getAttractionList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_ATTRACTION_DISPLAYED_INDEX);
    }

    @Test
    public void execute_attractionReferencedInItinerary_throwsCommandException() {
        Maplet mapletWithItinerary = getTypicalMaplet();
        mapletWithItinerary.addItinerary(new ItineraryBuilder().withAttractions(ALICE).build());
        Model modelWithItinerary = new ModelManager(mapletWithItinerary, new UserPrefs());

        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_ATTRACTION);

        assertCommandFailure(deleteCommand, modelWithItinerary, DeleteCommand.MESSAGE_ATTRACTION_IN_ITINERARY);
    }

    @Test
    public void execute_attractionReferencedInLocation_throwsCommandException() {
        Maplet mapletWithLocation = new Maplet();
        mapletWithLocation.addAttraction(ALICE);
        mapletWithLocation.addLocation(new LocationBuilder().withAttractions(ALICE).build());
        Model modelWithLocation = new ModelManager(mapletWithLocation, new UserPrefs());
        DeleteCommand deleteCommand = new DeleteCommand(Index.fromOneBased(1));

        assertCommandFailure(deleteCommand, modelWithLocation, DeleteCommand.MESSAGE_ATTRACTION_IN_LOCATION);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_ATTRACTION);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_ATTRACTION);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_ATTRACTION);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different attraction -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(targetIndex);
        String expected = DeleteCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoAttraction(Model model) {
        model.updateFilteredAttractionList(p -> false);

        assertTrue(model.getFilteredAttractionList().isEmpty());
    }
}
