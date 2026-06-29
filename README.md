# First Bank Uganda – Account Opening System.

First Bank Uganda is a robust JavaFX desktop application designed for managing bank account registrations. This system implements Object-Oriented Programming (OOP) principles to handle polymorphic account types and persistent data storage using MS Access.

---

## Project Structure.

```
firstbank/
├── FirstBankUganda1.accdb  # MS Access Database (Required)
├── db-drivers/            # UCanAccess and dependency JARs
├── README.md
└── src/
    └── firstbank/
        ├── model/         # Account (Abstract) & Subclasses
        ├── util/          # Validator & AccountNumberGenerator
        ├── db/            # DatabaseManager (MS Access Persistence)
        └── ui/            # BankFormApp (JavaFX GUI)
```

## Prerequisites and Environment Setup.

This project requires  **Java 17 or later** . Please note that standard JDK installations (like those from Oracle or OpenJDK)  **do not include JavaFX** . You have two options to run this project:

#### Option One: Use a "JavaFX-Ready" JDK (Recommended)

Download a JDK distribution that includes JavaFX, such as:

* [Liberica Full JDK](https://bell-sw.com/pages/downloads/) (Choose "Full JDK" which includes JavaFX).
* [ZuluFX](https://www.azul.com/downloads/) (Explicitly includes the JavaFX modules).

#### Option Two: Standard JDK + Manual JavaFX Libraries

If you use a standard JDK, you must manually download the [JavaFX SDK](https://openjfx.io/) and add it to your project path.

## How to Compile and Run (Manual Approach).

Our project avoids the complexity of `module-info.java` for ease of execution, use the following commands from the  **project root folder** :

###### Clone the repository:

```Shell
git clone https://github.com/ashabarobert305-hub/FirstBankUganda.git
cd FirstBankUganda
```

###### Compile the Source Code

This command compiles all your source files while including your database drivers in the classpath:

```Shell
javac -cp "db-drivers/*" model/*.java util/*.java database/*.java ui/*.java
```

###### Execute the Application

Once compiled, run the main class. Note the use of the classpath separator:

**On Windows:**

```Shell
java -cp ".;dbdrivers/*" ui.BankFormApp
```

**On macOS/Linux:**

```Shell
java -cp ".:dbdrivers/*" ui.BankFormApp
```

*Note: Ensure your `db-drivers/` folder contains the UCanAccess JAR and all its dependencies (hsqldb, jackcess, commons-lang3, commons-logging)*.

## Database Configuration.

The application is configured to use **MS Access** as the primary storage engine.

###### Setup Steps:

* **Database File:** Ensure `FirstBankUganda.accdb` exists in the project root. (If not, create a blank one in MS Access).
* **Drivers:** Ensure all required UCanAccess JARs (`ucanaccess`, `hsqldb`, `jackcess`, `commons-lang3`, `commons-logging`) are placed in the `dbdrivers/` folder.
* **Connection String:** Your `DatabaseManager.java` is configured as follows:

```Java
private static final String DB_URL = "jdbc:ucanaccess://" + System.getProperty("user.dir") + "/FirstBankUganda1.accdb";
```

## OOP Design Summary.

| **Element**        | **Implementation**                                |
| ------------------------ | ------------------------------------------------------- |
| **Abstract Class** | `Account`(defines state & polymorphic rules)          |
| **Concrete Types** | `Savings`,`Current`,`Fixed`,`Student`,`Joint` |
| **Polymorphism**   | `AccountFactory`handles instantiation logic           |
| **Encapsulation**  | Private fields with strict accessors                    |
| **Validation**     | Centralized`Validator`utility logic                   |

## Validation and Business RulesAccount Number Format.

* **Account Numbers:** `BRANCHCODE-YYYY-xxxxxx` (e.g., `KLA-2026-000001`)
* **NIN:** 14 uppercase alphanumeric characters.
* **Phone:** `+256XXXXXXXXX` format.
* **Age Requirements:** 18–75 years (Student accounts: 18–25).
* **Minimum Deposits:** Range from UGX 10,000 (Student) to UGX 1,000,000 (Fixed Deposit).

## License.

Academic coursework submitted to  **Victoria University, Kampala** . For educational purposes only.
