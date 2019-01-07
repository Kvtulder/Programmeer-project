# Programmeer-project

## Problem statement:

A lot of people have troubles managing their money, they spend too much on certain things. As a result, they don't have enough money left at the end of the month or they are saving less then they would like to. ]

Current tools to visualise either depend on sumbitting your 

## Solution

{app name} helps you to visualise your spendings and earnings in a easy way. Unlike other apps, who rely on entering your transactions manually, {app name} imports your transactions without any hassle. This is possible due the new PDS2 api, which allows you to give us permission to your transactions. We only download your transactions with your permission! We can't make any payments and you data will only be stored on your device. 

You can divide every transactions into a category

## Main features

## Activities:

### Overview acticity

<img src="sketch_main_activity.jpeg" alt="Overview acticity" width="400"/>

In the overview activity you can see how much you've spent and how much you've earned. The top bar consists of the app title and 3 buttons to navigate to other activities. The first button is the 'process transactions' button. The second button gives you an overview of all your transactions, and the third button gives you an overview of all your created groups.

### Process transactions activity

<img src="sketch_verwerk_transacties.jpeg" alt="Overview acticity" width="400"/>

This activity allows you to divide your transactions into categories. You can swipe trough every transaction and select a suitable category. 
[optional feature] If I've enough time, I'll implement a feature that automatically selects a category if the transaction account number and/or amount matches another transaction. 

### Transaction overview activity

<img src="sketch_transactie_overzicht.jpeg" alt="Overview acticity" width="400"/>

There will also be a general overview of all your transactions. This listviews contains all the transactions that are already in a category. 


### Group overview activity

<img src="sketch_groep_overzicht.jpeg" alt="Overview acticity" width="400"/>

This activity allows you to create new categories and delete them. Press a category to edit/ or create one

### Create/edit category

<img src="sketch_create_group.jpeg" alt="Overview acticity" width="400"/>

This activity allows you to edit a category or create a new one.


## Prerequisites

### External sources

The apps downloads your transactions from the ing api. First it authencates the application by retrieving an aplication access code. Then it authenticates the user by retreiving a user acces token. With the user access token we can download the transactions.

### Libraries

The app uses Any Chart for android to create a pie chart. 


