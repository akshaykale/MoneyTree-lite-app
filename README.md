# MoneyTree-lite-app


This is a new project created for the demo app.
Reason -  time consuming to enable the data binding and view binding for the existing project. Version mismatch and some issues with kotlin dls. Tried solution but didn't work so without wasting time created a new project.


## Technical details:

### Architecture (MVVM)
-| **data** package can be considered as Model (contains all the data/model/network/localDb related code and separate from android libraries.) 

-| **ui** package can be considered as Model and ViewModel (contains Fragment and ViewModel and related classes like RecyclerViewAdapter, etc)

This layers of 
- View (Activity/Fragment)
- ViewModel
- Data (Model / network / local Db) 
will ensure the separation of concern and make the code easily testable.

To glue the **data** and **viewModel** layers we are using **coroutine flow**
and to handle interaction between **viewModel** and **View** we are using **data binding**.


### Data story
User **RoomDb** to store the accounts and transactions locally on background thread without obstructing the main thread.

### UI
#### Accounts Screen
- Accounts list **grouped** by institution name.
- Total balance card in base currency (JPY)
- Open Transaction screen when clicked on the account

#### Transaction Screen
- Show Account information
- Show transaction list of selected account grouped by **Month Year** in decreasing order.
- Show **in/out** amount in a month.
- **Delete a transaction**


## Tech

- Written in Kotlin
- Heavy use of **data binding** and **view binding**
- Kotlin Coroutine and Flow for the data flow and events handling.
- ViewModel for persisting data on a fragment or Activity level.
- Room Db to store the data locally.

### Testing
- **AccountsDao** and **TransactionDao** test cases are written to test the room db operation of read, write, delete methods.


## Points to improve
- Implement a custom DI (Dependency injection) or use Hilt.
- Implement complete test cases and instrumentation test case.
- Comment the code as much as possible
- Implement Dark mode
- ViewModel can be optimized specially in the collect method of flow. Can be further divide into simpler methods and can be testable.
