# MoneyTree-lite-app


This is a new project created for the demo app.
Reason -  time consuming to enable the data binding and view binding for the existing project. Version mismatch and some issues with kotlin dls. Tried solution but didn't work so without wasting time created a new project.


## Technical details:

### Architecture (MVVM)
-| **data** package can be considered as Model (contains all the data/model/network/localDb related code and separate from android libraries.) 

-| **ui** package can be considered as Model and ViewModel (contains Fragment and ViewModel and related classes like RecyclerViewAdapter, etc)

<img width="354" alt="Screen Shot 2021-08-12 at 3 20 58" src="https://user-images.githubusercontent.com/10893215/129082471-f23ff6dc-c51a-43ed-8084-1e8bf4162f01.png">




This layers of 
- View (Activity/Fragment)
- ViewModel
- Data (Model / network / local Db) 
will ensure the separation of concern and make the code easily testable.

To glue the **data** and **viewModel** layers we are using **coroutine flow**
and to handle interaction between **viewModel** and **View** we are using **data binding**.


## Data story
User **RoomDb** to store the accounts and transactions locally on background thread without obstructing the main thread.

## UI
#### Accounts Screen
- Accounts list **grouped** by institution name.
- Total balance card in base currency (JPY)
- Open Transaction screen when clicked on the account

![Screenshot_1628706158](https://user-images.githubusercontent.com/10893215/129082753-b2d5962f-a57d-4d70-a0f6-0a9b542a0fe7.png =250x)



#### Transaction Screen
- Show Account information
- Show transaction list of selected account grouped by **Month Year** in decreasing order.
- Show **in/out** amount in a month.
- **Delete a transaction**

![Screenshot_1628706219](https://user-images.githubusercontent.com/10893215/129082796-96e2a60e-c1f4-4386-a96e-16621edf880a.png =250x) 
![Screenshot_1628706238](https://user-images.githubusercontent.com/10893215/129082840-ab5c7ef4-0514-4cce-96d7-a027e44d4767.png =250x)




## Tech

- Written in Kotlin
- Heavy use of **data binding** and **view binding**
- Kotlin Coroutine and Flow for the data flow and events handling.
- ViewModel for persisting data on a fragment or Activity level.
- Room Db to store the data locally.

## Testing
- **AccountsDao** and **TransactionDao** test cases are written to test the room db operation of read, write, delete methods.
<img width="525" alt="Screen Shot 2021-08-12 at 3 18 24" src="https://user-images.githubusercontent.com/10893215/129082257-2f6e1fd3-0a00-4c3a-a8e4-c3f2fb8ac330.png">


## Points to improve
- Implement a custom DI (Dependency injection) or use Hilt.
- Implement complete test cases and instrumentation test case.
- Comment the code as much as possible
- Implement Dark mode
- ViewModel can be optimized specially in the collect method of flow. Can be further divide into simpler methods and can be testable.
