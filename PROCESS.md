# Process book

## day 2

### Design
Starting to implement the design. I found out that the proposed pie chart library (Any chart for android) isn't free. So im currently looking for alternatives. Currently looking at MPAndroidChart.

### API

I just found out that the ING api might not be as open as the website suggests. In the FAQ it says you need to be registered as AISP, but i can't find any information online on how to register. 
I'm also stuck on connection to the API, I'm getting an unexpected error. If i can't fix it today I'll contact the ING

If it becomes sure that I can't connect to the API, I'll switch to an importable CSV file. The user can download it from the ING online banking enviorment. 

## day 3

Implemented some adapters as described in the design document. No new decisions made.

## day 4

Tried to implement MPAndroidChart. It is pretty easy to work with, so i decided to use this library.
Stuggled a bit with implementing the stateviewpageradaptor, but it works now

## day 5

Thought about how to implement some functionality of my app: how to categorise incoming money. I've decided to make two options: general income and category income. Category income can be usefull when you pay for all your friends in a restaurant and they pay you back later. If you could only could choose general income, the app would show you've spent a lot of money on restaurants, while you have spent a lot less in reality.

## day 6

Did some research in how to select the CSV file. It would be easy if you can integrate it with google drive, so you can download the file on your computer, and immediately open it in the app. The android action_get_content seems to support this.

## day 7

Implented the csv selector dialog. It works perfect, no need to change it.

## day 8/9

Implemented the category chooser. Nothing changed from inital plan.

## day 10

The application now has the transaction view activity. 

## day 11

Started working on the date selector. Having some troubles, there is no default android month selector. Thinking if i've to create one by myself or use 2 date selectors.

## day 12/13

Created one myself. The app now checks which transaction you've downloaded and returns all the months

## day 14

Hackathon. Restructured the main activity. Now added an on period changed function that refreshes the main activity

## day 15

Better icon selection, now works with an enumeration. A lot of improvements but no big changes from initial plan

## day 15.1 (saturday)

Change from inital plan! I implented everythin from day 5. You can now create income and spending categories. There are two tabs, one for both. You can now also analyse your income.

## day 16

Fixing some bugs and design.

## day 17

Fixed the error that prevented one of the piecharts from initialising

## day 18

Last day! Fixed the bug that caused wrong percentages and other improvements



