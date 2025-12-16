# **Bank Account Management System**

This is a comprehensive Java-based console application for managing bank accounts, performing transactions, and generating account statements. It includes input validation, multiple account types, customer tiers, transaction logging, wire transfers, and a built-in test runner.

---

## 📌 **Features**

### **✔ Account Management**

* Create new accounts with customer information
* View all accounts with summary statistics
* Load sample accounts on startup
* Account status management (Active/Inactive)

### **✔ Customer Types**

* **Regular Customer** - Standard banking services
* **Premium Customer** - Enhanced benefits, minimum deposit requirements ($10,000 for Savings accounts)

### **✔ Account Types**

* **Savings Account**
  * Interest rate: 3.5%
  * Minimum balance: $500
  * No overdraft facility

* **Checking Account**
  * Overdraft limit: $1,000
  * Monthly fee: $10
  * Allows negative balance within overdraft limit

### **✔ Transaction System**

* **Deposit** - Add funds to accounts with validation
* **Withdrawal** - Remove funds with balance/overdraft validation
* **Wire Transfer** - Transfer money between accounts
* Transaction confirmation before execution
* Records all transactions with timestamps

### **✔ Account Statement Generation**

* Complete account statements with:
  * Account and customer information
  * Full transaction history
  * Summary statistics (total deposits, withdrawals, net change)
  * Current balance

### **✔ Exception Handling**

* Custom exception classes:
  * `InsufficientFundsException` - When withdrawal exceeds available balance
  * `OverdraftExceededException` - When withdrawal exceeds overdraft limit
  * `InvalidAmountException` - When transaction amount is invalid
  * `InvalidAccountException` - When account is not found

### **✔ Input Validation**

* Prevents invalid menu options
* Ensures numeric inputs with range validation
* Ensures non-empty text fields
* Ensures valid deposit/withdrawal amounts
* Validates minimum deposit requirements

### **✔ Testing**

* Built-in test runner in the main menu
* JUnit 5 test suite covering:
  * Account operations (deposits, withdrawals)
  * Exception handling
  * Transaction management
  * Edge cases and validation

---

## 📁 **Project Structure**

```
Bank_Account_Management_Lab/
├── src/
│   ├── Main.java                    # Main entry point (default package)
│   ├── models/                      # Domain models package
│   │   ├── Account.java             # Abstract base account class
│   │   ├── SavingsAccount.java      # Savings account implementation
│   │   ├── CheckingAccount.java     # Checking account implementation
│   │   ├── Customer.java            # Base customer class
│   │   ├── RegularCustomer.java     # Regular customer implementation
│   │   ├── PremiumCustomer.java     # Premium customer implementation
│   │   ├── Transaction.java         # Transaction model
│   │   ├── Transactable.java        # Transaction interface
│   │   └── exceptions/              # Custom exceptions package
│   │       ├── InsufficientFundsException.java
│   │       ├── OverdraftExceededException.java
│   │       ├── InvalidAmountException.java
│   │       └── InvalidAccountException.java
│   ├── services/                    # Business logic services package
│   │   ├── AccountManager.java      # Account management service
│   │   ├── TransactionManager.java  # Transaction management service
│   │   └── StatementGenerator.java  # Account statement generation service
│   ├── utils/                       # Utility classes package
│   │   └── ValidationUtils.java     # Input validation utilities
│   ├── test/                        # Test source directory
│   │   └── java/
│   │       └── com/
│   │           └── bank/
│   │               └── tests/       # Test classes package
│   │                   ├── AccountTest.java
│   │                   ├── ExceptionTest.java
│   │                   └── TransactionManagerTest.java
│   └── docs/                        # Documentation
│       └── git-workflow.md
└── README.md
```

---

## 🔧 **Dependencies**

### **Required Libraries**

* **JUnit 5** (JUnit Jupiter & JUnit Platform)
  * `junit-jupiter-api` - For writing tests
  * `junit-jupiter-engine` - For running tests
  * `junit-platform-launcher` - For programmatic test execution

### **Installation**

If using **IntelliJ IDEA**:
1. The IDE should automatically detect and download JUnit 5 dependencies
2. If not, add JUnit 5 libraries via: `File → Project Structure → Libraries → + → From Maven`

If using **command line**:
1. Download JUnit 5 JARs from [JUnit 5 Releases](https://github.com/junit-team/junit5/releases)
2. Add them to your classpath when compiling and running

---

## 🚀 **How to Run the Project**

### **1. Install Java**

You must have **Java 8 or above** installed.

Check your version:

```bash
java -version
```

If not installed, download from:
[https://www.oracle.com/java/technologies/downloads/](https://www.oracle.com/java/technologies/downloads/)

---

### **2. Clone or Download the Project**

If using Git:

```bash
git clone https://github.com/dusengepeggy/Bank_Account_Management_Lab
cd Bank_Account_Management_Lab
```

Or download the ZIP and extract it.

---

### **3. Compile the Program**

Navigate to your project root directory:

```bash
cd Bank_Account_Management_Lab
```

Compile all Java files (from project root):

```bash
javac -d out src/Main.java src/models/*.java src/models/exceptions/*.java src/services/*.java src/utils/*.java
```

Or compile all files recursively:

```bash
javac -d out -sourcepath src src/**/*.java
```

**Note:** Make sure JUnit 5 JARs are in your classpath if you want to run tests.

---

### **4. Run the Program**

After successful compilation:

```bash
java -cp out Main
```

The Main menu should appear:

```
=======================================
  BANK ACCOUNT MANAGEMENT - MAIN MENU 
=======================================
    1. Manage Account
    2. Perform Transaction
    3. Generate Account Statement
    4. Run Tests
    5. Exit

Enter choice:
```

---

## 📋 **Menu Options**

### **1. Manage Account**
* Create Account - Create new accounts with customer details
* View All Accounts - Display all accounts with summary statistics

### **2. Perform Transaction**
* Deposit - Add funds to an account
* Withdrawal - Remove funds from an account
* Wire Money Between Accounts - Transfer funds between accounts

### **3. Generate Account Statement**
* Generate a complete account statement with:
  * Account and customer information
  * Transaction history
  * Summary statistics

### **4. Run Tests**
* Execute the built-in JUnit 5 test suite
* View test execution results

### **5. Exit**
* Exit the application

---

## 🧪 **Sample Data**

When the program starts, it automatically loads 5 sample accounts:

* **John Smith** – Regular Customer, Savings Account ($5,250.00)
* **Sarah Johnson** – Regular Customer, Checking Account ($3,450.00)
* **Michael Chen** – Premium Customer, Savings Account ($15,750.00)
* **Emily Brown** – Regular Customer, Checking Account ($890.00)
* **David Wilson** – Premium Customer, Savings Account ($25,300.00)

You can immediately test:

* Account creation
* Deposits and withdrawals
* Wire transfers between accounts
* Account statement generation
* Viewing all accounts
* Running the test suite

---

## 📥 **Running in IntelliJ IDEA**

1. Open IntelliJ IDEA
2. Click **File → Open**
3. Select your project folder (`Bank_Account_Management_Lab`)
4. IntelliJ should automatically detect the project structure
5. Mark directories:
   * `src` → Right-click → Mark Directory as → Sources Root
   * `src/test/java` → Right-click → Mark Directory as → Test Sources Root
6. Ensure JUnit 5 is added to the project:
   * `File → Project Structure → Libraries → + → From Maven`
   * Search for: `org.junit.jupiter:junit-jupiter:5.9.0` (or latest version)
7. Run `Main.java` using the green run button (▶) or press `Shift + F10`

---

## 🧪 **Running Tests**

### **Via Application Menu**
1. Run the application
2. Select option **4. Run Tests** from the main menu
3. View test execution results in the console

### **Via IntelliJ IDEA**
1. Right-click on the `src/test/java/com/bank/tests` folder
2. Select **Run 'All Tests'**
3. Or run individual test classes

### **Via Command Line**
```bash
# Compile test files
javac -cp "out:junit-jupiter-api-5.9.0.jar:junit-jupiter-engine-5.9.0.jar:junit-platform-launcher-1.9.0.jar" -d out src/test/java/com/bank/tests/*.java

# Run tests (requires JUnit Platform Console Launcher)
java -jar junit-platform-console-standalone-1.9.0.jar --class-path out --scan-class-path
```

---

## 🏗️ **Architecture**

### **Design Patterns**
* **Abstract Factory** - Account and Customer hierarchies
* **Strategy Pattern** - Different account types with varying behaviors
* **Service Layer** - Separation of business logic (services) from models

### **Package Organization**
* `models` - Domain entities and business objects
* `services` - Business logic and operations
* `utils` - Utility and helper classes
* `models.exceptions` - Custom exception classes
* `com.bank.tests` - Test classes

---

## 📝 **Notes**

* Account numbers are auto-generated in the format: `ACC001`, `ACC002`, etc.
* Premium customers require a minimum deposit of $10,000 for Savings accounts
* Checking accounts allow overdraft up to $1,000
* All transactions are logged and can be viewed in account statements
* The application uses console-based I/O for user interaction

---

## 🤝 **Contributing**

Feel free to fork this project and submit pull requests for any improvements.

---

## 📄 **License**

This project is open source and available for educational purposes.
