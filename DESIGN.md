# Design Document

## Advanced sketches of UI

I could'nt fit all the activities in one sketch so I've added a sketch for every activity

### Main Activity

### Transaction overview

<img src="Images/design/Transaction_overview.png">

### process Transaction

<img src="Images/design/processTransaction.png">

### manage categories

<img src="Images/design/manage_categories.png">

### Read CSV

<img src="Images/design/CSVReader.png">

This gives a general idea of the application flow. There are still some simple activities missing, like the create/edit activity. They've almost the same diagram like the described activities above.


## External Data Sources

### ING API

The ING API returns a JSON file with all the transactions. I'm not sure yet if it is possible for consumers to connect to the API. That's why I've also considered the following option

### Import CSV File

If it is not possible to connect to the ING API, I'll let the user download a csv file from their online banking and select the file manually in the app. Instead of receiving an online JSON file, the app will read a offline file.
Every line is a new transaction. I'll need to read every transaction and check if the transaction is already in the database. If not, store it. I don't need to filter any data besides checking if it's already there.

## Databases

The application will contain 3 local SQL databases. One database for al the transactions, one for the categories and one for the linked a

### Transaction db structure
TYPE NAME PROPERTIES
Int ID NOT NULL AUTOINCREMENT
Text Name NOT NULL
double amount NOT NULL
Text IBAN NOT NULL
Text description
INT CATEGORY_ID 

### Category db structue

TYPE NAME PROPERTIES  
INT ID NOT NULL AUTOINCREMENT  
TEXT name NOT NULL  
TEXT logo  

### Linked AccountNumbers

TYPE NAME PROPERTIES
INT ID NOT NULL AUTOINCREMENT
TEXT Iban NOT NULL
DOUBLE amount
INT CATEGORY_ID NOT NULL


