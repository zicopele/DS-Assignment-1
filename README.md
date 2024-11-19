# Dynamic Group Budget Tracker

The **Dynamic Group Budget Tracker** is a collaborative financial management application that allows multiple users to join a shared budget-tracking environment. Users can easily record their expenses, view a summary of all expenses, and receive real-time notifications about user activity within the group. With features such as detailed expense tracking and a transparent view of collective spending, this application enhances group financial management and fosters better communication among participants.

## Features
- **Add Expense:** Users can add expenses by specifying an amount and a detailed description.
- **View All Expenses:** Request a summary of all expenses recorded by all users.
- **Real-time Notifications:** Receive notifications when a user joins or leaves the budget tracker.
- **Detailed Expense Tracking:** Record expenses with descriptive details for better clarity.

## Commands
- ADD
- VIEW
- EXIT
  
For command ADD the format goes as ```ADD <amount> <detail>```. Amount is the money amount, and detail is what the expense was based on.

## Requirements
- Java Development Kit (JDK) 8 or higher
- A terminal or command prompt

## How to Run the Application

1.  **Open Terminal for Server and Clients**
2.  **Clone the repository:**
   ```
   git clone <repository-url>
   cd <repository-directory>
   ```
3. **Compile .java files**
  ```
  javac *.java
  ```
4. **Run Server.java**
  ```
  java Server
  ```
5. **Run Client.java**
  ```
  java Client
  ```
6. **Follow the prompts in the client terminal to enter your username and manage expenses.**
- Use the command ```ADD <amount> <detail>``` to add expenses.
- Use the command ```VIEW``` to see a summary of all expenses.
- Use the command ```EXIT``` to leave the tracker.
