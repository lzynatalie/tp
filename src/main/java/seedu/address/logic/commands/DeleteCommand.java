package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SYMPTOM;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Address;
import seedu.address.model.person.DoctorName;
import seedu.address.model.person.Email;
import seedu.address.model.person.Ic;
import seedu.address.model.person.Name;
import seedu.address.model.person.NextOfKin;
import seedu.address.model.person.NextOfKinPhone;
import seedu.address.model.person.NextOfKinRelationship;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.UrgencyLevel;
import seedu.address.model.symptom.Symptom;

/**
 * Represents a command that deletes one or more people from the address book.
 */
public abstract class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = "Usage:\n"
            + "        Single deletion: " + COMMAND_WORD + " <INDEX>\n"
            + "        Multiple deletion: " + COMMAND_WORD + " <INDEX>,<INDEX>[,<INDEX>,...]\n"
            + "        Range deletion: " + COMMAND_WORD + " <START_INDEX>-<END_INDEX>";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted person(s): %1$s";
    public static final String MESSAGE_DELETE_FIELD_SUCCESS = "Deleted field value(s) for person(s): %1$s";
    public static final String MESSAGE_NON_OPTIONAL_FIELD_PREFIXES =
            "The following prefixes are not optional fields and cannot be used for deletion: %1$s\n"
                    + "Please specify only optional field prefixes.";
    public static final String MESSAGE_DUPLICATE_PREFIXES =
            "For all values deletion, duplicates are not allowed for the following prefix: %1$s\n"
                    + "Please specify the prefix only once without any value.";
    public static final String MESSAGE_VALUE_MISSING =
            "For specific value deletion, values must be provided for all instances of the following prefix: %1$s\n"
                    + "Please specify the values to delete for each prefix.";
    public static final String MESSAGE_VALUE_NOT_ALLOWED =
            "Values are not allowed for the following prefix: %1$s\n"
                    + "Please specify only the prefix without any value.";
    public static final String MESSAGE_NOTES_NOT_FOUND =
            "One or more specified person(s) do not have any notes to delete.\n";
    public static final String MESSAGE_SYMPTOM_NOT_FOUND =
            "One or more specified person(s) do not have the required symptom(s) to delete.\n";

    private final Map<Prefix, List<String>> prefixMap;

    public DeleteCommand(Map<Prefix, List<String>> prefixMap) {
        this.prefixMap = prefixMap;
    }

    public Map<Prefix, Set<String>> getPrefixMapWithSetValues() {
        Map<Prefix, Set<String>> prefixMapWithSetValues = new HashMap<>();
        prefixMap.forEach((prefix, values) -> {
            Set<String> valuesSet = new HashSet<>(values);
            valuesSet.remove("");
            prefixMapWithSetValues.put(prefix, valuesSet);
        });
        return prefixMapWithSetValues;
    }

    public Set<Prefix> getPrefixes() {
        return prefixMap.keySet();
    }

    public abstract Set<Index> getTargetIndicesAsSet();

    public Person getUpdatedPerson(Person personToDelete) throws CommandException {
        requireNonNull(personToDelete);
        assert !prefixMap.isEmpty() : "There are no specified fields to delete.";

        if (prefixMap.containsKey(PREFIX_NOTES) && personToDelete.getNotes().getValue().isEmpty()) {
            throw new CommandException(MESSAGE_NOTES_NOT_FOUND);
        }
        if (prefixMap.containsKey(PREFIX_SYMPTOM) && personToDelete.getSymptoms().isEmpty()) {
            throw new CommandException(MESSAGE_SYMPTOM_NOT_FOUND);
        }

        Set<Symptom> symptomsToDelete = prefixMap.getOrDefault(PREFIX_SYMPTOM, List.of()).stream()
                .filter(s -> !s.isEmpty())
                .map(Symptom::new)
                .collect(Collectors.toSet());
        Set<Symptom> updatedSymptoms = new HashSet<>(personToDelete.getSymptoms()); // instantiate with all symptoms
        if (prefixMap.containsKey(PREFIX_SYMPTOM) && symptomsToDelete.isEmpty()) { // delete all symptoms
            updatedSymptoms.clear();
        } else if (prefixMap.containsKey(PREFIX_SYMPTOM)) { // delete specific symptoms
            for (Symptom symptom : symptomsToDelete) {
                if (!updatedSymptoms.contains(symptom)) {
                    throw new CommandException(MESSAGE_SYMPTOM_NOT_FOUND);
                }
                updatedSymptoms.remove(symptom);
            }
        }

        Name name = personToDelete.getName();
        Phone phone = personToDelete.getPhone();
        Email email = personToDelete.getEmail();
        Address address = personToDelete.getAddress();
        Ic ic = personToDelete.getIc();
        UrgencyLevel urgencyLevel = personToDelete.getUrgencyLevel();
        NextOfKinPhone nextOfKinPhone = personToDelete.getNextOfKinPhone();
        DoctorName doctorName = personToDelete.getDoctorName();
        NextOfKin nextOfKin = personToDelete.getNextOfKin();
        NextOfKinRelationship nextOfKinRelationship = personToDelete.getNextOfKinRelationship();
        Notes updatedNotes =
                prefixMap.containsKey(PREFIX_NOTES) ? new Notes("") : personToDelete.getNotes();

        return new Person(name, phone, email, address, updatedSymptoms, ic,
                 urgencyLevel, nextOfKinPhone, doctorName, nextOfKin, nextOfKinRelationship, updatedNotes);
    }

    public Person[] getUpdatedPersons(Person[] personsToDelete) throws CommandException {
        requireNonNull(personsToDelete);
        Person[] updatedPersons = new Person[personsToDelete.length];
        for (int i = 0; i < personsToDelete.length; i++) {
            Person person = personsToDelete[i];
            Person updatedPerson = getUpdatedPerson(person);
            assert updatedPerson.isSamePerson(person)
                    : "Updated person should be have the same identity as original person.";
            updatedPersons[i] = updatedPerson;
        }
        return updatedPersons;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return this.getTargetIndicesAsSet().equals(otherDeleteCommand.getTargetIndicesAsSet())
                && this.getPrefixMapWithSetValues().equals(otherDeleteCommand.getPrefixMapWithSetValues());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", this.getTargetIndicesAsSet().stream()
                        .sorted(Comparator.comparingInt(Index::getOneBased))
                        .collect(Collectors.toList()))
                .add("prefixes", this.prefixMap.keySet().stream()
                        .sorted(Comparator.comparing(Prefix::getPrefix))
                        .map(p -> p + "=" + prefixMap.get(p))
                        .collect(Collectors.toList()))
                .toString();
    }
}
