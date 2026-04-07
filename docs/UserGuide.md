---
layout: default.md
title: "User Guide"
pageNav: 3
---

# ClinicConnect User Guide

ClinicConnect is a **desktop app for triage coordinators to manage patient records, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, ClinicConnect can get your triage tasks done faster than traditional GUI apps.

<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick Start

1. Ensure you have Java `17` or above installed in your Computer.
2. Download the latest `ClinicConnect.jar` file from our [releases](https://github.com/AY2526S2-CS2103-F11-4/tp/releases/) page.
3. Copy the file to the folder you want to use as the home folder for your clinic's database.
4. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar ClinicConnect.jar` command to run the application.<br>
   A GUI should appear in a few seconds. Note how the app contains some sample triage data.<br>
   ![Ui](images/Ui.png)

5. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

    * `list` : Lists all patients.
    * `add pn/John Doe p/98765432 e/johnd@example.com a/311, Clementi Ave 2, #02-25 ic/S1234567A u/high nk/John nkp/91234567 nkr/Brother d/Dr Sally s/fever s/cough n/Does not like to eat veggies` : Adds a patient named `John Doe` to ClinicConnect.
    * `update 1 u/high s/Severe chest pain` : Updates the urgency and symptoms of the 1st patient shown in the current list.
    * `find pn/John` : Searches for patients with the name "John".
    * `delete 1` : Deletes the 1st patient shown in the current list.
    * `undo` : Undoes the last command if it changed the data.
    * `exit` : Exits the app safely and saves your data.

6. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` and enclosed in angle brackets `<>` describes parameters to be supplied by the user.<br/>
  e.g. in `add pn/<PATIENT_NAME>`, `<PATIENT_NAME>` is a parameter which can be used as `add pn/John Doe`.
* Items in square brackets are optional.<br/>
  e.g. `[n/<NOTES>]` can be used as `n/Patient has history of asthma` or simply left out.
* Arguments can be provided in any order.<br/>
  e.g. if the command specifies `pn/<PATIENT_NAME> p/<PATIENT_PHONE>`, `p/<PATIENT_PHONE> pn/<PATIENT_NAME>` is also acceptable.
* All commands and prefixes are case-insensitive.
* Leading and trailing spaces are ignored/trimmed automatically.
* Internal spaces within a command (i.e. `d ele te 1`) or prefix (i.e. `p  n/`) are not allowed and will be rejected.

</box>
    
### Viewing help : `help`

Shows a message explaining how to access the help page.

**Format:** `help`

### Adding a patient: `add`

Records comprehensive patient information, add it to the address book and saves it to the hard disk.

**Format:** `add pn/<PATIENT_NAME> ic/<IC> p/<PATIENT_PHONE> a/<ADDRESS> e/<EMAIL> u/<LEVEL> d/<DOCTOR> nk/<NEXT_OF_KIN_NAME> nkp/<NEXT_OF_KIN_PHONE> nkr/<NEXT_OF_KIN_RELATIONSHIP> [s/<SYMPTOM>] [n/<NOTES>]`
* The prefixes `pn/`, `ic/`, `p/`, `a/`, `e/`, `u/`, `d/`, `nk/`, `nkp/`, and `nkr/` are **compulsory** and **must not be blank**.
* The prefixes `s/` and `n/` are **optional**.
* The same prefix cannot be provided more than once **except** for `s/` (symptoms).
* All parameters have their leading and trailing spaces ignored/trimmed automatically.

**Patient Name (`pn/`):**
* The patient's name.
* Can contain letters (A-Z, a-z), spaces, commas (,), hyphens (-), apostrophes ('), and periods (.).
* It is enforced that the first character must be a letter (A-Z, a-z) to prevent blank names that only contain spaces.

**IC Number (`ic/`):**
* The patient's IC number.
* Must follow the format: `[S/T/F/G/M]` + 7 digits + 1 letter (total 9 characters). For example, `S1234567A` is a valid IC number.
* Duplicates are **not allowed** (i.e., two patients cannot have the same IC number).
* Case-insensitivity (e.g., `s1234567a` is treated the same as `S1234567A`).

**Patient Phone Number (`p/`):**
* The patient's phone number.
* Must contain exactly 8 digits and contain only `0-9`. For example, `98765432` is a valid phone number.

**Address (`a/`):**
* The patient's address.
* Can contain any characters (including spaces).

**Email Address (`e/`):**
* The patient's email address.
* Must follow the format: `<local-part>@<domain>`, where `<local-part>` and `<domain>` can contain letters, digits, and certain special characters.

**Urgency Level (`u/`):**
* The urgency level of the patient's condition.
* Must be exactly one of: `low`, `moderate`, `high`, `extreme` (case-insensitive).
* The urgency level is used to prioritize patients in the list, with `extreme` being the highest priority and `low` being the lowest.

**Doctor Name (`d/`):**
* The name of the doctor assigned to the patient.
* Can contain letters (A-Z, a-z), spaces, commas (,), hyphens (-), apostrophes ('), and periods (.).
* It is enforced that the first character must be a letter (A-Z, a-z) to prevent blank doctor names that only contain spaces.

**Next-of-Kin Name (`nk/`):**
* The name of the patient's next of kin.
* Can contain letters (A-Z, a-z), spaces, commas (,), hyphens (-), apostrophes ('), and periods (.).
* It is enforced that the first character must be a letter (A-Z, a-z) to prevent blank next-of-kin names that only contain spaces.

**Next-of-Kin Phone Number (`nkp/`):**
* The phone number of the patient's next of kin.
* Must contain exactly 8 digits and contain only `0-9`. For example, `87654321` is a valid next-of-kin phone number.

**Next-of-Kin Relationship (`nkr/`):**
* The relationship of the next of kin to the patient (e.g., "Mother", "Brother", "Friend").
* Can contain letters (A-Z, a-z), spaces, commas (,), hyphens (-), apostrophes ('), and periods (.).
* It is enforced that the first character must be a letter (A-Z, a-z) to prevent blank relationships that only contain spaces.

**Symptoms (`s/`):**
* The symptoms that the patient is experiencing.
* Can contain alphanumeric characters and whitespace only (i.e., no special characters).
* A patient can have any number of symptoms (including 0). To specify multiple symptoms, use multiple `s/` prefixes (e.g., `s/fever s/cough`).
* If the prefix is declared then there must be a non-blank symptom after it (e.g., `s/` without any symptoms will be rejected).
* If the prefix is not declared at all, it will be treated as if the patient has no symptoms.
* Symptoms are compared case-insensitively to prevent duplicated entries. However, differences in internal spacing (e.g., "runny nose" vs "runnynose") are treated as distinct symptoms.

**Notes (`n/`):**
* Additional notes about the patient.
* Can contain any characters (including spaces).
* Maximum length of 500 characters.
* If the prefix is declared with a blank note (e.g., `n/` without any notes), it will be treated as an empty note (i.e., the patient will have no notes).
* If the prefix is not declared at all, it will be treated as if the patient has no notes as well.

>**Tip:** A patient can have any number of symptoms (including 0).

**Examples:**
* `add pn/John Doe Jun Kai ic/T0123456B p/12345678 a/21 Serangan Road e/john@doe.com u/high d/Dr Tan Ah Beng nk/Mary Doe nkp/87654321 nkr/Mother s/Diabetic n/Admitted at 12pm`

### Listing all patients : `list`

Displays all patients in the application in a structured list format. You can also filter the list by urgency level and/or symptoms.

**Format:** `list [u/<LEVEL>]... [s/<SYMPTOM>]...`

* You may provide `u/` (urgency level) to match urgency levels.
* You may provide `s/` (symptoms) to match symptoms.
* If both `u/` and `s/` are provided, only patients matching **both** criteria are shown.

**Examples:**
* `list` (shows everyone)
* `list u/high`
* `list s/fever s/cough`
* `list u/high s/fever`

### Updating a patient : `update`

Updates an existing patient's details in ClinicConnect.

**Format:** `update INDEX [pn/<PATIENT_NAME>] [ic/<IC>] [a/<ADDRESS>] [e/<EMAIL>] [p/<PATIENT_PHONE>] [s/<SYMPTOM>] [u/<LEVEL>] [d/<DOCTOR>] [nk/<NEXT_OF_KIN_NAME>] [nkp/<NEXT_OF_KIN_PHONE>] [nkr/<NEXT_OF_KIN_RELATIONSHIP>] [n/<NOTES>]`

* Edits the patient at the specified `INDEX`. The index refers to the index number shown in the displayed patient list. The index **must be a positive integer** (e.g. 1, 2, 3, …).
* **At least one** of the fields must be provided.
* Existing values will be overwritten by the input values.
* When editing symptoms, the existing symptoms will be removed (not cumulative).
* The same argument validation constraints from the `add` command apply here.

>**Tip**: You can remove all symptoms by typing `s/` without specifying any symptoms after it.

**Examples:**
* `update 1 p/91234567` Updates the phone number of the 1st patient to be `91234567`.
* `update 2 u/extreme s/Cardiac arrest` Updates the urgency level of the 2nd patient to `extreme` and symptoms to `Cardiac arrest`.

### Searching for a patient : `find`

Allows triage coordinators to locate specific patient records using various identifiers, reducing the manual effort of scrolling.

**Format:** `find [pn/<PATIENT_NAME>] [ic/<IC>] [p/<PATIENT_PHONE>] [e/<EMAIL>] [d/<DOCTOR>]`

* Finds patients whose identifiers match the given keywords.
* At least one identifier must be provided.
* The search is case-insensitive.
* Leading and trailing spaces are ignored/trimmed.
* Only full words will be matched for names (e.g. `Han` will not match `Hans`).
* Patients matching at least one keyword will be returned (i.e. `OR` search).

**Examples:**
* `find pn/Alice` returns patients with the name Alice.
* `find ic/S1234567A` returns the patient with that specific IC.
* `find e/johndoe@example.com` returns the patient(s) with that email.
* `find d/Dr Sally` returns the patient(s) with that doctor name.

### Deleting a patient : `delete`

Permanently removes patient records from ClinicConnect.

**Format:**

**Single deletion:** `delete <INDEX>`
* Edits the patient at the specified `<INDEX>`. The index refers to the index number shown in the displayed patient list. The index **must be a positive integer**.

**Multiple deletion:** `delete <INDEX>,<INDEX>[,<INDEX>,...]`
* Edits the patients at the specified indices. The indices refer to the index numbers shown in the displayed patient list. The indices **must be positive integers**.
* Delimiter: Comma (`,`)
* Duplicated indices (e.g. `delete 2,2`) will be rejected.

**Range deletion:** `delete <START_INDEX>-<END_INDEX>`
* Edits the patients in the range of the specified indices. The indices refer to the index numbers shown in the displayed patient list. The indices **must be positive integers**.
* Delimiter: Hyphen (`-`)
* The start index must be less than or equal to the end index.

**Optional fields deletion:** `delete <INDICES> [s/] [n/]`
* Deletes all the symptoms (`s/`) and/or notes (`n/`) of the patients at the specified indices. `<INDICES>` refers to any of the above index formats.
* Prefixes must be provided without any parameters (e.g. `n/notes` will be rejected).

**Specific values deletion:** `delete <INDICES> [s/<SYMPTOM>]...`
* Deletes the specified symptoms of the patients at the specified indices. `<INDICES>` refers to any of the above index formats.
* All prefixes must be provided with parameters (e.g. `s/fever s/` will be rejected).

**Examples:**
* `delete 2` deletes the 2nd patient in the list.
* `delete 1,3,5` deletes the 1st, 3rd, and 5th patients.
* `delete 1-4` deletes the 1st through 4th patients.
* `delete 1,3 s/ n/` deletes all the symptoms and notes of the 1st and 3rd patients.
* `delete 2 s/fever s/cough` deletes the symptoms "fever" and "cough" of the 2nd patient.

### Undoing the last command : `undo`

Reverts the most recent command if it changed the data.

**Format:** `undo`

* `add`, `update`, and `delete` commands can be undone.
* `undo` commands cannot be undone (i.e. you cannot redo an undone command).

### Clearing all entries : `clear`

Clears all entries from the clinic records.

**Format:** `clear`

### Exiting safely : `exit`

Exits the application and saves all data to the hard disk.

**Format:** `exit`

* The command does not accept any additional parameters (e.g., `exit now` will be rejected).

--------------------------------------------------------------------------------------------------------------------

## Automatic Triage Sorting

ClinicConnect automatically sorts the patient list to prioritize critical cases:
1.  **Urgency Level:** Patients are ranked `EXTREME` > `HIGH` > `MODERATE` > `LOW`.
2.  **Tie-Breaker:** If two patients have the exact same urgency level, they are sorted deterministically by their **IC Number**.

--------------------------------------------------------------------------------------------------------------------

## Command History navigation using the Up/Down arrow keys

ClinicConnect allows you to navigate through your command history using the `Up` and `Down` arrow keys when the command box is focused (_i.e. when there is a blinking vertical line in the command box_). This feature enables you to quickly recall and reuse previous commands without retyping them.

Only the commands that were successfully executed (_i.e. the command box does not light up red after you press the `enter` key_) are stored in the command history. Those commands will have their trailing and leading whitespaces trimmed before storing in the command history. When you press the `Up` arrow key, you will see the most recent command you entered. Pressing it again will take you further back in your command history. Conversely, pressing the `Down` arrow key will move you forward through the command history.

The command history is session-based, meaning that it is cleared when you exit the application. Therefore, only commands entered during the current session will be available for navigation using the Up and Down arrow keys.

The command history removes duplicated commands, so if you enter the same command multiple times, only the most recent instance will be stored in the history. This helps to keep the command history concise and relevant. Duplicated commands are determined based on their trimmed version and compared in a case-insensitive manner. Commands are considered duplicates only if they are exactly the same after normalization (i.e. trimming leading and trailing whitespaces and ignoring letter casing).

Examples of how duplicated commands are handled in the command history:

| Commands about to enter                                   | Command in command history | Is it considered a duplicate? | Why?                                                                                                                                            |
|-----------------------------------------------------------|----------------------------|-----|-------------------------------------------------------------------------------------------------------------------------------------------------|
| `LEADING_WHITESPACES delete 1 s/ n/ TRAILING_WHITESPACES` | `delete 1 s/ n/`           | Yes | After trimming leading and trailing whitespaces, the command is identical to the existing command in the history (case-insensitive comparison). |
| `DELETE 1 S/ N/`                                          | `delete 1 s/ n/`           | Yes | Case-insensitive comparison, the command is identical to the existing command in the history.                                                   |
| `delete 1 n/ s/`                                          | `delete 1 s/ n/`           | No  | Althought the both commands are functionally the same, they are not be treated as duplicates as they are not identical                          |

The command history navigation feature enhances your efficiency by allowing you to quickly access and reuse previously entered commands, saving you time and effort in managing patient records with ClinicConnect.

--------------------------------------------------------------------------------------------------------------------

## Data Management

### Saving the data

ClinicConnect data is saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

ClinicConnect data is saved automatically as a JSON file `[JAR file location]/data/clinicconnect.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file make its format invalid, ClinicConnect will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause ClinicConnect to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.

</box>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous ClinicConnect home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command Summary

| Action | Format                                                                                                                                                                                               | Examples                                                                   |
| :--- |:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------|
| **Add** | `add pn/<PATIENT_NAME> ic/<IC> p/<PATIENT_PHONE> a/<ADDRESS> e/<EMAIL> u/<LEVEL> d/<DOCTOR> nk/<NEXT_OF_KIN_NAME> nkp/<NEXT_OF_KIN_PHONE> nkr/<NEXT_OF_KIN_RELATIONSHIP> [s/<SYMPTOMS>] [n/<NOTES>]` | `add pn/John Doe Jun Kai ic/T0123456B p/12345678 a/21 Serangan Road e/john@doe.com u/high d/Dr Tan Ah Beng nk/Mary Doe nkp/87654321 nkr/Mother s/Diabetic n/Admitted at 12pm` |
| **Update** | `update INDEX [prefix/<VALUE>]...`                                                                                                                                                                   | `update 1 u/extreme n/Immediate surgery required`                                     |
| **Search** | `find [pn/<PATIENT_NAME>] [ic/<IC>] [p/<PATIENT_PHONE>] [e/<EMAIL>] [d/<DOCTOR>]`                                                                                                                    | `find e/johndoe@example.com`, `find d/Dr Sally`, `find ic/S1234567A`                  |
| **Delete** | `delete <INDEX>` <br> `delete <INDEX>,<INDEX>` <br> `delete <START>-<END>` <br> `delete <INDICES> [s/<SYMPTOM>]... [n/]`                                                                             | `delete 3` <br> `delete 1,4` <br> `delete 2-5` <br> `delete 1 s/fever n/`             |
| **List**   | `list [u/<LEVEL>]... [s/<SYMPTOM>]...`                                                                                                                                                               | `list` <br> `list u/high` <br> `list s/fever s/cough`                                 |
| **Undo**   | `undo`                                                                                                                                                                                               | `undo`                                                                                |
| **Clear**  | `clear`                                                                                                                                                                                              | `clear`                                                                               |
| **Exit**   | `exit`                                                                                                                                                                                               | `exit`                                                                                |
