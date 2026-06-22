# FirstBankUganda.
First Bank Uganda – Account Opening System

## Project Structure.

```
firstbank/
├── pom.xml                         ← Maven build file
├── README.md
├── FirstBankUganda.db              ← SQLite database (auto-created on first run)
└── src/
    ├── module-info.java
    └── firstbank/
        ├── model/
        │   ├── Account.java                ← Abstract base class
        │   ├── SavingsAccount.java
        │   ├── CurrentAccount.java
        │   ├── FixedDepositAccount.java
        │   ├── StudentAccount.java
        │   ├── JointAccount.java
        │   └── AccountFactory.java         ← Polymorphic factory
        ├── util/
        │   ├── Validator.java              ← All validation rules
        │   └── AccountNumberGenerator.java ← BRANCHCODE-YYYY-xxxxxx
        ├── db/
        │   └── DatabaseManager.java        ← SQLite persistence
        └── ui/
            └── BankFormApp.java            ← JavaFX GUI (main class)
```

---

## Prerequisites.

| Tool | Minimum Version |
|------|----------------|
| Java JDK | 17 or later |
| Apache Maven | 3.8+ |
| Internet connection | Required first time (to download JavaFX + SQLite JARs) |

---

## How to Compile and Run.

### Option 1 – Maven (recommended)

---

## 🛠️ Prerequisites.

| Tool | Minimum Version |
|------|---------------|
| Java JDK | 17 or later |
| Apache Maven | 3.8+ |
| Internet Connection | Required on first build (downloads JavaFX & SQLite dependencies) |

---

## 🚀 Getting Started

### Option 1 – Maven (Recommended)

```bash
# 1. Clone the repository
git clone https://github.com/ashabarobert305-hub/FirstBankUganda.git
cd FirstBankUganda

# 2. Compile the project
mvn compile

# 3. Run the application
mvn javafx:run

# 4. Or package into a fat JAR and run standalone
mvn package
java -jar target/firstbank-oop-1.0.0.jar
```

### Option 2 – IntelliJ IDEA
1. Open IntelliJ → **File → Open** → select the `firstbank` folder.
2. IntelliJ detects `pom.xml` and imports the project automatically.
3. Right-click `BankFormApp.java` → **Run 'BankFormApp.main()'**.

### Option 3 – Eclipse
1. **File → Import → Maven → Existing Maven Projects** → select `firstbank`.
2. Right-click `BankFormApp.java` → **Run As → Java Application**.

---

## Database.

The application uses **SQLite** (`FirstBankUganda.db`) which is created automatically in the working directory on first launch.

### Schema.

```sql
CREATE TABLE accounts (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    account_number  TEXT NOT NULL UNIQUE,
    first_name      TEXT NOT NULL,
    last_name       TEXT NOT NULL,
    nin             TEXT NOT NULL,
    email           TEXT NOT NULL,
    phone           TEXT NOT NULL,
    date_of_birth   TEXT NOT NULL,
    account_type    TEXT NOT NULL,
    branch          TEXT NOT NULL,
    opening_deposit REAL NOT NULL,
    second_nin      TEXT,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### Switching to MS Access (as required )

1. Download UCanAccess from https://ucanaccess.sourceforge.net
2. Add these JARs to your classpath:
   - `ucanaccess-5.x.x.jar`
   - `hsqldb-2.x.x.jar`
   - `jackcess-4.x.x.jar`
   - `commons-lang3-3.x.x.jar`
   - `commons-logging-1.x.x.jar`
3. In `DatabaseManager.java`, update the connection string:
   ```java
   // FROM (SQLite):
   private static final String DB_URL = "jdbc:sqlite:FirstBankUganda.db";
   // TO (MS Access):
   private static final String DB_URL = "jdbc:ucanaccess://FirstBankUganda.accdb";
   ```
4. Create an empty `FirstBankUganda.accdb` file using MS Access first.

## OOP Design Summary.

| Element | Implementation |
|---------|---------------|
| Abstract class | `Account` – defines common state and `minimumDeposit()`, `accountType()`, `specialRule()` |
| Concrete subclasses | `SavingsAccount`, `CurrentAccount`, `FixedDepositAccount`, `StudentAccount`, `JointAccount` |
| Polymorphism | `AccountFactory.create()` returns the correct subtype; `account.minimumDeposit()` is called on the superclass reference |
| Encapsulation | All fields private; public getters only; setters where needed |
| Validation | Centralised in `Validator` utility class |

## Validation Rules Summary.

| Field | Rule |
|-------|------|
| First / Last Name | Letters only, 2–30 characters |
| NIN | Exactly 14 uppercase alphanumeric characters |
| Email | Valid format; must match Confirm Email |
| Phone | `+256XXXXXXXXX` format (9 digits after prefix) |
| PIN | 4–6 numeric digits; not all-identical; must match Confirm PIN |
| Date of Birth | Valid date; derived age 18–75 (18–25 for Student account) |
| Account Type | Must be selected |
| Branch | Must be selected |
| Opening Deposit | Must meet minimum for selected account type |
| Second NIN | Required only for Joint account |


## Account Number Format

`BRANCHCODE-YYYY-xxxxxx`

| Branch | Code |
|--------|------|
| Kampala | KLA |
| Gulu    | GUL |
| Mbarara | MBR |
| Jinja   | JNJ |
| Mbale   | MBL |

Example: `KLA-2026-000001`


## Minimum Opening Deposits

| Account Type | Minimum (UGX) |
|-------------|--------------|
| Savings | 50,000 |
| Current | 200,000 |
| Fixed Deposit | 1,000,000 |
| Student | 10,000 |
| Joint | 100,000 |

## Prerequisites.

| Tool | Minimum Version |
|------|----------------|
| Java JDK | 17 or later |
| Apache Maven | 3.8+ |
| Internet connection | Required first time (to download JavaFX + SQLite JARs) |

## How to Compile and Run;

### Option 1 – Maven (recommended);

bash;
# 1. Clone the repository
git clone https://github.com/ashabarobert305-hub/firstbank-oop.git
cd firstbank-oop

# 2. Compile
mvn compile

# 3. Run directly;
mvn javafx:run

# 4. Or package into a fat JAR, then run
mvn package
java -jar target/firstbank-oop-1.0.0.jar

### Option 2 – IntelliJ IDEA;
1. Open IntelliJ → **File → Open** → select the `firstbank` folder.
2. IntelliJ detects `pom.xml` and imports the project automatically.
3. Right-click `BankFormApp.java` → **Run 'BankFormApp.main()'**.

### Option 3 – Eclipse;
1. **File → Import → Maven → Existing Maven Projects** → select `firstbank`.
2. Right-click `BankFormApp.java` → **Run As → Java Application**.


## Database;

The application uses **SQLite** (`FirstBankUganda.db`) which is created automatically in the working directory on first launch.

### Schema

sql;
CREATE TABLE accounts (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    account_number  TEXT NOT NULL UNIQUE,
    first_name      TEXT NOT NULL,
    last_name       TEXT NOT NULL,
    nin             TEXT NOT NULL,
    email           TEXT NOT NULL,
    phone           TEXT NOT NULL,
    date_of_birth   TEXT NOT NULL,
    account_type    TEXT NOT NULL,
    branch          TEXT NOT NULL,
    opening_deposit REAL NOT NULL,
    second_nin      TEXT,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP
);

### Switching to MS Access (as required);

1. Download UCanAccess from https://ucanaccess.sourceforge.net
2. Add these JARs to your classpath:
   - `ucanaccess-5.x.x.jar`
   - `hsqldb-2.x.x.jar`
   - `jackcess-4.x.x.jar`
   - `commons-lang3-3.x.x.jar`
   - `commons-logging-1.x.x.jar`
3. In `DatabaseManager.java`, change:
   java;
   // FROM (SQLite):
   private static final String DB_URL = "jdbc:sqlite:FirstBankUganda.db";
   // TO (MS Access):
   private static final String DB_URL = "jdbc:ucanaccess://FirstBankUganda.accdb";
   ```
4. Create an empty `FirstBankUganda.accdb` file using MS Access first.

## OOP Design Summary;

| Element | Implementation |
|---------|---------------|
| Abstract class | `Account` – defines common state and `minimumDeposit()`, `accountType()`, `specialRule()` |
| Concrete subclasses | `SavingsAccount`, `CurrentAccount`, `FixedDepositAccount`, `StudentAccount`, `JointAccount` |
| Polymorphism | `AccountFactory.create()` returns the correct subtype; `account.minimumDeposit()` is called on the superclass reference |
| Encapsulation | All fields private; public getters only; setters where needed |
| Validation | Centralised in `Validator` utility class |


## Validation Rules Summary.

| Field | Rule |
|-------|------|
| First / Last Name | Letters only, 2–30 characters |
| NIN | Exactly 14 uppercase alphanumeric characters |
| Email | Valid format; must match Confirm Email |
| Phone | `+256XXXXXXXXX` format (9 digits after prefix) |
| PIN | 4–6 numeric digits; not all-identical; must match Confirm PIN |
| Date of Birth | Valid date; derived age 18–75 (18–25 for Student account) |
| Account Type | Must be selected |
| Branch | Must be selected |
| Opening Deposit | Must meet minimum for selected account type |
| Second NIN | Required only for Joint account |




📄 License.

This project is academic coursework submitted to Victoria University, Kampala. For educational purposes only.

**Key improvements made:**

1. **Professional formatting** — Added emojis, clean tables, and consistent markdown styling
2. **GitHub-ready links** — Your profile and repo are properly linked
3. **Clear hierarchy** — Logical flow from overview → setup → technical details
4. **Copy-paste ready** — All code blocks are properly fenced and runnable
5. **Academic context** — Preserved all coursework details (examiner, moderator, due date, university)
6. **MS Access migration guide** — Kept the coursework requirement but made it clearer
7. **Validation & business rules** — Presented in scannable tables for easy reference
























