package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getMedicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    private AddressBook getMedicalAddressBook() {
        AddressBook ab = new AddressBook();
        ab.addPerson(new PersonBuilder()
                .withName("Alice")
                .withIc("S0000001A")
                .withSymptoms("fever")
                .withUrgencyLevel("high")
                .build());
        ab.addPerson(new PersonBuilder()
                .withName("Bob")
                .withIc("S0000002A")
                .withSymptoms("cough")
                .withUrgencyLevel("low")
                .build());
        ab.addPerson(new PersonBuilder()
                .withName("Cara")
                .withIc("S0000003A")
                .withSymptoms("fever", "cough")
                .withUrgencyLevel("moderate")
                .build());
        return ab;
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, model.getFilteredPersonList().size());
        assertCommandSuccess(new ListCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        model.updateFilteredPersonList(person -> person.getIc().value.equalsIgnoreCase("S0000001A"));
        int filteredSize = expectedModel.getFilteredPersonList().size();
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, filteredSize);
        assertCommandSuccess(new ListCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_listFilteredByUrgency_showsMatchingPatients() {
        Predicate<Person> predicate = person -> person.getUrgencyLevel().toString()
                .equalsIgnoreCase("high");
        String criteriaDescription = "Urgency: high";

        expectedModel.updateFilteredPersonList(predicate);
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS_FILTERED,
                expectedModel.getFilteredPersonList().size(), criteriaDescription);

        assertCommandSuccess(new ListCommand(predicate, criteriaDescription), model, expectedMessage,
                expectedModel);
    }

    @Test
    public void execute_listFilteredBySymptoms_showsMatchingPatients() {
        Predicate<Person> predicate = person -> person.getSymptoms().stream()
                .anyMatch(symptom -> symptom.symptomName.equalsIgnoreCase("fever"));
        String criteriaDescription = "Symptoms: fever";

        expectedModel.updateFilteredPersonList(predicate);
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS_FILTERED,
                expectedModel.getFilteredPersonList().size(), criteriaDescription);

        assertCommandSuccess(new ListCommand(predicate, criteriaDescription), model, expectedMessage,
                expectedModel);
    }

    @Test
    public void execute_listFilteredByUrgencyAndSymptoms_showsUnionOfMatches() {
        Predicate<Person> predicate = person -> person.getUrgencyLevel().toString()
                .equalsIgnoreCase("high")
                || person.getSymptoms().stream()
                .anyMatch(symptom -> symptom.symptomName.equalsIgnoreCase("fever"));
        String criteriaDescription = "Urgency: high or Symptoms: fever";

        expectedModel.updateFilteredPersonList(predicate);
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS_FILTERED,
                expectedModel.getFilteredPersonList().size(), criteriaDescription);

        assertCommandSuccess(new ListCommand(predicate, criteriaDescription), model, expectedMessage,
                expectedModel);
    }

    @Test
    public void execute_listEmptyModel_returnsEmptyListMessage() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(emptyModel.getAddressBook(), new UserPrefs());
        assertCommandSuccess(new ListCommand(), emptyModel, ListCommand.MESSAGE_EMPTY_LIST,
                expectedModel);
    }

    @Test
    public void execute_listNoMatchesWithCriteria_returnsNoMatchesMessage() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(emptyModel.getAddressBook(), new UserPrefs());
        Predicate<Person> predicate = person -> false;
        String criteriaDescription = "Urgency: high";
        String expectedMessage = String.format(ListCommand.MESSAGE_NO_MATCHES_FILTERED, criteriaDescription);
        assertCommandSuccess(new ListCommand(predicate, criteriaDescription), emptyModel, expectedMessage,
                expectedModel);
    }
}
