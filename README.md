# A1-liyuje - Appointment system for a health service

**Unit:** PROG2004 Object Oriented Programming
**Assessment:** Assessment 1 - Portfolio/programming (40%)
**Student:** Li Yuje
**Language:** Java (plain `src` folder, no external libraries)
**Java version:** compiles with Java 8 or newer (developed and tested with JDK 21)

---

## 1. What this program is

This project is the object model ("the backbone") of an appointment system for a health
service. A patient books an appointment with a health professional - a general practitioner,
a surgeon, a nurse, a dietitian or a physiotherapist - for one day, using the clinic's fixed
daily schedule.

As the brief requires, this is **not** a finished application:

* there is no user interface, no menus and no login,
* no data is read from or written to a file or database, and
* bookings are only for the current clinic day.

What exists is a set of well-designed, collaborating classes, and a `main` method
(`AssignmentOne`) that plays the part of the users: it creates objects, exercises every
operation and prints the results to the console, including the operations that must be
rejected.

## 2. How to compile and run

From the `A1-liyuje` folder (a terminal with a JDK on the `PATH`):

```bash
javac -d bin src/*.java
java -cp bin AssignmentOne
```

Without a JDK on the `PATH`, any IDE works the same way:

* **Eclipse** - *File > Open Projects from File System* and choose this folder, then run
  `AssignmentOne`.
* **IntelliJ IDEA** - *File > Open* and choose this folder, mark `src` as the sources root,
  then run `AssignmentOne`.
* **VS Code** - open this folder with the *Extension Pack for Java* and run `AssignmentOne`.
* **NetBeans** - *File > New Project > Java with Existing Sources*, choose this folder and
  set `src` as the source folder.

The classes are in the default package, exactly as the brief asks ("In the `src` directory,
create a class called `AssignmentOne` containing the `main` method").

## 3. The classes and why they exist

| Class | Responsibility | Brief part |
| --- | --- | --- |
| `HealthProfessional` | Abstract shared type: identifier, name, the fixed set of working days, and the behaviour every professional must provide. | Part 1 |
| `GeneralPractitioner` | A GP: consultation room, accepting new patients, `prescribeMedication`. | Part 2 |
| `Surgeon` | A surgeon: surgical specialty, theatre number, `performProcedure`. | Part 2 |
| `Nurse` | A nurse: ward, shift, `takeBloodSample`. | Part 2 |
| `Dietitian` | A dietitian: diet focus, consultation length, `createMealPlan`. | Part 2 |
| `Physiotherapist` | A physiotherapist: treatment area, sessions per week, `runRehabilitationSession`. | Part 2 |
| `Day` | Enumeration of the days of the working week. | Part 1 |
| `Patient` | The patient an appointment refers to: name, validated mobile number. | Part 4 |
| `TimeSlot` | One time of day as hour + minute; validates, compares, prints as `HH:mm`. | Part 4 |
| `ClinicSchedule` | The clinic's fixed daily schedule (08:00-16:00 in 30 minute slots, and configurable). | Part 5 |
| `Appointment` | One booking linking a `Patient`, any `HealthProfessional` and a `TimeSlot`; `Comparable` by time. | Part 4 |
| `AppointmentManager` | Owns the `ArrayList<Appointment>` and all operations: add, view, filter, sort, cancel, each returning a message. | Part 5 |
| `AssignmentOne` | `main` method: the scripted demonstration of Parts 3 and 6. | Parts 3, 6 |

## 4. Design decisions and the reasoning behind them

### Part 1 - the shared health professional

* **What is genuinely common belongs in one place.** An identifier, a name and the days the
  professional works are shared by every type, so they live in `HealthProfessional`. A
  specialty such as a theatre number does not, so it is pushed down into `Surgeon`.
* **The days are a fixed collection of values**, so they are modelled with the `Day enum`,
  stored in an `ArrayList<Day>` (an enum cannot be added to at runtime, and a typo such as
  "Wendsday" is impossible because the compiler checks every use).
* **Controlled access = encapsulation.** Every field is `private final`, validation happens
  once in the constructor, and the getter for the day collection returns a **copy**, so
  external code can read the working days but cannot corrupt them. There are no setters, so
  an object cannot be put into an invalid state after it is created. The demonstration
  prints a rejected invalid professional, an invalid shift, an impossible time and a bad
  phone number to show this working.
* **Readable printing** comes from `toString()`, which returns `getDetails()`: the shared
  part from the base class plus the specialty part from the subclass. `getDetails()` and
  `toString()` are `final`, so no subclass can break the layout or forget the shared lines.
* **More than one way to construct an object is worthwhile here.** Optional information
  (whether a GP takes new patients, the working days) is given a sensible default by an
  overloaded constructor, for example
  `new GeneralPractitioner(id, name, room)` works Monday to Friday and accepts new patients.

### Part 2 - specialising the health professionals

* **Inheritance (`extends`) is the relationship** that lets the five specific types reuse the
  common fields, validation and behaviour instead of repeating them: no subclass declares
  an id, a name or a day collection again.
* **`HealthProfessional` is abstract, and three methods are abstract** (`getRole`,
  `getSpecialtyDetails`, `performDuty`). A "plain" professional with no specialty therefore
  cannot exist, and every new type is forced by the compiler to say what it is and what it
  does - the design stays robust as types are added.
* **Unique behaviour lives in the subclass that owns it**: only doctors prescribe
  (`GeneralPractitioner.prescribeMedication`), only a surgeon operates, only a nurse takes a
  blood sample. Putting those in the base class would give every professional behaviour it
  cannot really perform.
* **Constructor chaining** (`super(...)`) guarantees that the shared part is set up and
  validated before the subclass sets its own fields.

### Part 3 - working with the objects

* The nine professionals are stored in one `ArrayList<HealthProfessional>` and processed
  with one loop: `toString()` and `performDuty()` are called on the shared type and each
  object answers in its own way (polymorphism).
* Adding a sixth type needs **one new class and nothing else** - the loops, the filters and
  the appointment code do not change, which is the practical payoff of the abstract type.
* `HealthProfessional` implements `Comparable`, so the collection can be sorted by name with
  a single `Collections.sort` call.
* `equals()` and `hashCode()` are based on the **identifier**: two objects with the same
  identifier are the same person. That is what allows the appointment system to recognise a
  professional even when it holds a different object with the same details.

### Part 4 - modelling an appointment

* **`Patient` is its own class.** The details would otherwise be duplicated on every
  appointment for the same person, and the phone number format would have to be validated in
  more than one place.
* **An appointment holds a `HealthProfessional` reference**, so the one `Appointment` class
  works with every specialty, including types added later. This is polymorphism used for
  flexibility, not decoration.
* **An appointment cannot be incomplete.** All three parts are required and the constructor
  rejects a missing patient, professional or time.
* **Time is stored as a `TimeSlot` (hour + minute), not as text.** "09:30" as a string sorts
  after "10:00", and it allows "29:75"; a `TimeSlot` validates its values and converts to
  minutes from midnight, so ordering is correct and `compareTo` is simple.
* **`Appointment implements Comparable`**, ordered by time and then, to keep the result
  consistent, by professional identifier and patient name, so a whole day can be sorted with
  `Collections.sort`.

### Part 5 - managing the collection

* **`ArrayList<Appointment>` is the right collection**: the day is an ordered list that is
  walked, filtered and sorted, no booking has a unique key to look it up by, and duplicates
  at the same time for different professionals are legitimate - so a list, not a set or a
  map.
* **Every rule is enforced in one place**, `AppointmentManager.addAppointment`:
  1. all details present,
  2. the time is a real clinic slot,
  3. the professional works on the clinic day,
  4. the professional is not already booked at that time.
* **Every operation returns a message** (`[OK]` or `[WARNING]`) instead of throwing, so an
  ordinary wrong booking gives the user feedback rather than crashing the program.
* **The schedule is a class, not a constant**: `ClinicSchedule` builds its slots from an
  opening hour, a closing hour and a slot length, and `AppointmentManager` asks it whether a
  time is valid. Moving the clinic to 09:00-17:00 in 20 minute slots changes one constructor
  argument and nothing else - the demonstration runs a second manager to prove it.
* **Defensive copies from every query** (`getAllAppointments`,
  `getAppointmentsForProfessional`, `getAppointmentsForPatient`, `sortByTime`) so a caller
  cannot change the manager's data through the returned list.

### Part 6 - demonstrating it

`AssignmentOne.main` runs five labelled sections: validation, Part 3, Part 4, Parts 5-6. The
Parts 5-6 section walks through adding bookings for every type of professional, four different
rejections (double booking, a time outside the schedule, a professional who does not work
that day, missing details), the full sorted list, filtering by professional ID, filtering by
patient phone number, sorting a filtered subset, cancelling by professional ID and time
(including cancelling something that is not there and an invalid time), cancelling by phone
number, the updated list, and a second clinic schedule.

## 5. The four pillars in this code

| Pillar | Where it is applied | Why |
| --- | --- | --- |
| **Abstraction** | `HealthProfessional` is `abstract`, with the abstract methods `getRole()`, `getSpecialtyDetails()` and `performDuty()`. | Callers work with "a health professional" and never need to know which specialty; the concept that does not exist (a professional with no specialty) cannot be instantiated. |
| **Encapsulation** | Private final fields, validation in constructors, getters that copy collections, no setters (`HealthProfessional`, `Patient`, `TimeSlot`, `Appointment`, `AppointmentManager`). | An object can never be in an invalid state, and the manager's rules cannot be bypassed by another class editing the list directly. |
| **Inheritance** | `GeneralPractitioner`, `Surgeon`, `Nurse`, `Dietitian` and `Physiotherapist` all `extend HealthProfessional`; `super(...)` sets up the shared part. | The shared data, validation, `toString` and equality are written once instead of five times. |
| **Polymorphism** | `System.out.println(professional)` calls the object's own `toString()`; `professional.performDuty()` answers differently for each type; an `Appointment` accepts any `HealthProfessional`; overriding `equals`, `hashCode` and `compareTo`. | The same code processes different types, and adding a type needs no change to existing loops or the booking code. |

## 6. Limitations (deliberate, per the brief)

* No user interface, no login, no persistence, and no checking against real staff or patients.
* Bookings are for one clinic day only, so an appointment stores a time but not a date.
* A professional is identified by identifier alone; the model assumes identifiers are unique
  across all professions, which the clinic would enforce.
* Availability is checked against the working days, but not against leave, room conflicts or
  appointment length (a `Dietitian` consultation is 45 minutes, which is longer than a
  30 minute slot - a real system would need variable-length slots).

## 7. Submission checklist

* [x] `src` contains every source file (13 classes) and the project compiles and runs.
* [x] Zip the project folder as `A1-liyuje.zip`.
* [ ] Push the project to a GitHub repository, with regular, descriptive commits over the
      assessment period, and put the link in the submission comments.
* [ ] Run WakaTime while working and attach the report/screenshot as
      `A1-liyuje-WakaTime.png`.
* [ ] Record the short (max 5 minute) video explaining the four pillars, upload it to OneDrive
      and put the link in the comments.
* [ ] Paste the GenAI declaration in the comments (`docs/genai-declaration.txt`).
