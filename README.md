**Project: Personal Finance Tracker (CLI)**

**Overview:**

The Personal Finance Tracker is a command-line application designed to help users manage their personal finances by tracking income and expenses. It allows users to add transactions, categorize them, view summaries, and generate reports, providing a simple yet effective way to monitor financial activity.

**Features:**
Transaction Management:
Add income and expense transactions with details like date, amount, category, and description.
Categorize transactions using predefined and custom categories.
Store transaction data in a CSV file for persistence.
Reporting and Analysis:
View all transactions or filter them by date, category, or type.
Generate summaries of income and expenses for specific periods.
Generate category-wise spending reports.
Calculate and display the current balance.
User Interface:
Interactive command-line interface (CLI) for easy navigation and data entry.
Data Validation and Error Handling:
Validate user input to prevent errors.
Handle file I/O errors gracefully.

**Phases:**

**Phase 1: Core Transaction Management (Basic Data Storage)**

Goal: Implement the fundamental functionality for adding and viewing transactions, with basic file storage.

Tasks:
Create a Transaction record to store transaction data.
Implement methods to save and load transactions from a CSV file.
Implement the "add transaction" command.
Implement the "view transactions" command.
Create a basic CLI for user interaction.

Deliverables:
A functional CLI application that allows users to add and view transactions, with data persistence.

**Phase 2: Enhanced Reporting and Summaries**

Goal: Add basic reporting and summary features.

Tasks:
Implement a "summary" command to display income and expense summaries.
Calculate and display the current balance.
Implement category-wise spending reports.
Allow date filtering for viewing transactions.

Deliverables:
Enhanced CLI application with summary and reporting capabilities.

**Phase 3: Input Validation and Error Handling**

Goal: Improve the robustness of the application with better error handling and input validation.

Tasks:
Implement input validation for dates, amounts, and categories.
Create custom exceptions for specific error scenarios.
Implement comprehensive error handling for file I/O and user input.
Allow the user to create, and delete categories.

Deliverables:
A more robust and user-friendly CLI application with improved error handling.

**Phase 4: Refactoring and Modernization**

Goal: Refactor the code for better readability and maintainability, and incorporate modern Java features.

Tasks:
Refactor the code to improve its structure and readability.
Incorporate modern Java features (e.g., Optional, var, records, streams).
Use Generics where appropriate.

Deliverables:
A clean, well-structured, and modern Java application.
Well documented code.

**Technologies Used:**
Java
Maven (for build management)
