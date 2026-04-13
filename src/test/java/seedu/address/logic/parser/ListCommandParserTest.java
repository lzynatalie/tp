package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.symptom.Symptom;
import seedu.address.testutil.PersonBuilder;

public class ListCommandParserTest {

    private ListCommandParser parser;
    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        parser = new ListCommandParser();
        model = new ModelManager(getMedicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void parse_emptyArgs_listsAllPatients() throws Exception {
        ListCommand command = parser.parse("");
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS,
                model.getFilteredPersonList().size());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
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
    public void parse_validPrefixedUrgency_filtersByUrgency() throws Exception {
        ListCommand command = parser.parse(" u/high");

        Predicate<Person> predicate = person -> person.getUrgencyLevel().toString()
                .equalsIgnoreCase("high");
        String criteriaDescription = "Urgency: high";

        expectedModel.updateFilteredPersonList(predicate);
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS_FILTERED,
                expectedModel.getFilteredPersonList().size(), criteriaDescription);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void parse_validPrefixedSymptoms_filtersBySymptoms() throws Exception {
        ListCommand command = parser.parse(" s/fever");

        Predicate<Person> predicate = person -> person.getSymptoms().stream()
                .anyMatch(symptom -> symptom.symptomName.equalsIgnoreCase("fever"));
        String criteriaDescription = "Symptoms: fever";

        expectedModel.updateFilteredPersonList(predicate);
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS_FILTERED,
                expectedModel.getFilteredPersonList().size(), criteriaDescription);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void parse_unprefixedSymptoms_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () ->
                parser.parse("fever cough"));
        assertEquals("Only prefixes \"u/\" (urgency) and \"s/\" (symptoms) are allowed for list.\n"
            + "Examples: `list u/high` `list s/fever` `list u/high s/fever`.",
            exception.getMessage());
    }

    @Test
    public void parse_validPrefixedUrgencyAndSymptoms_union() throws Exception {
        ListCommand command = parser.parse(" u/high s/fever");

        Predicate<Person> predicate = person -> person.getUrgencyLevel().toString()
                .equalsIgnoreCase("high")
                || person.getSymptoms().stream()
                .anyMatch(symptom -> symptom.symptomName.equalsIgnoreCase("fever"));

        String criteriaDescription = "Urgency: high or Symptoms: fever";

        expectedModel.updateFilteredPersonList(predicate);
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS_FILTERED,
                expectedModel.getFilteredPersonList().size(), criteriaDescription);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void parse_invalidUrgency_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(" u/urgent"));
    }

    @Test
    public void parse_emptyUrgency_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(" u/"));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE),
                exception.getMessage());
    }

    @Test
    public void parse_emptySymptoms_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(" s/"));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE),
                exception.getMessage());
    }

    @Test
    public void parse_invalidSymptoms_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(" s/fever!"));
        assertEquals(Symptom.MESSAGE_CONSTRAINTS, exception.getMessage());
    }

    @Test
    public void parse_invalidPrefix_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () ->
                parser.parse(" n/notes u/high"));
        assertEquals("Only prefixes \"u/\" (urgency) and \"s/\" (symptoms) are allowed for list.\n"
            + "Examples: `list u/high` `list s/fever` `list u/high s/fever`.",
            exception.getMessage());
    }

    @Test
    public void parse_invalidPrefixAfterValidPrefix_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(" u/high n/notes"));
    }

    @Test
    public void parse_urgencyWithPatientNamePrefix_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () ->
                parser.parse(" u/high pn/Alice"));
        assertEquals("Only prefixes \"u/\" (urgency) and \"s/\" (symptoms) are allowed for list.\n"
            + "Examples: `list u/high` `list s/fever` `list u/high s/fever`.",
            exception.getMessage());
    }
}

