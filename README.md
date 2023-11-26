
![Logo](https://github.com/Zacle/Focused/blob/master/docs/images/logo.png?raw=true)


# Focused

Focused is a fully functional Android app built entirely with Kotlin and Jetpack Compose. It helps users to focus on their tasks and objectives using the pomodoro techniques. Focused follows Android recommended architecture best practices with multi-modules.




## Screenshots of main features

![Screenshots of tasks and timer](https://github.com/Zacle/Focused/blob/master/docs/images/focused-screenshots/introduction.png?raw=true)


## Features

### Tasks

Users should be able to schedule tasks that will be completed at any given time. Tasks can have a number of pomodoro with its specified length. The user can be reminded to do the task if he decides to be reminded while creating the task.

Tasks are also grouped by due date to make it easy to know the deadlines of each task.

![Screenshots of tasks feature](https://github.com/Zacle/Focused/blob/master/docs/images/focused-screenshots/tasks.png?raw=true)


### Projects

Projects are used to group and organize tasks. Deleting a project will also delete all its associated tasks.

![Screenshots of projects feature](https://github.com/Zacle/Focused/blob/master/docs/images/focused-screenshots/projects.png?raw=true)


### Timer

Timer is the main functionality that helps users focus on the task using the chosen time length from the preferences or the pomodoro length of the task. While running the screen stays on to keep track of the elapsed time.

![Screenshots of timer feature](https://github.com/Zacle/Focused/blob/master/docs/images/focused-screenshots/timer.png?raw=true)


### Report

The stats of completed tasks and pomodoro. Users can filter to find reports: daily, weekly and monthly. As for now only the bar chart displays the report.

![Screenshots of report feature](https://github.com/Zacle/Focused/blob/master/docs/images/focused-screenshots/report.png?raw=true)

# Architecture

In this learning journey you will learn about the Now in Android app architecture: its layers, key classes and the interactions between them.

The architecture has three layers: a data layer, a domain layer and a UI layer.

The architecture follows a reactive programming model with [unidirectional data flow](https://developer.android.com/jetpack/guide/ui-layer#udf). With the data layer at the bottom, the key concepts are:
Higher layers react to changes in lower layers.
Events flow down.
Data flows up.
The data flow is achieved using streams, implemented using Kotlin [Flows](https://developer.android.com/kotlin/flow).

![Screenshot of arcitectural example](https://github.com/Zacle/Focused/blob/master/docs/images/Architectutre%20example.png?raw=true)

Here’s what’s happening in each steps:

|Steps|Description|Code
|------|----------|-----|
|1|When we access the screen, the default state is Loading|`TimerViewModel::initState`|
|2|Get the stream of task entity using the running task id from the Dao, convert it to task resource and emit it|`LocalTaskDataSource.getTask`|
|3|Get the stream of of pomodoro state from the local data source backed by Proto DataStore, convert it and emit it using a Flow|`PomodoroStateDataSource.pomodoroState`|
|4|Get the stream of of pomodoro preferences from the local data source backed by Proto DataStore, convert it and emit it using a Flow|`PomodoroPreferencesDataSource.pomodoroPreferences`|
|5|Obtains the stream of task from the local data source and emit it|`DefaultTaskRepository.getTaskResource`|
|6|Emit the stream of pomodoro state from local data store|`DefaultPomodoroStateRepository.pomodoroState`|
|7|Emit the stream of pomodoro preferences from local data store|`DefaultPomodoroPreferencesRepository.pomodoroPreferences`|
|8|Handle the data emitted from the database to check if it was a Success or Error and emit the UseCase Result|`GetTaskUseCase`|
|9|TimerManager combines the datastore emitted values, countdown timer, notification, service using the business logic and emit the timer’s pomodoro state|`TimerManager`|
|10|TimerViewModel receive the use case case result and pomodoro state then emit them to the TimerScreen to render on the screen|`TimerViewModel`|
|11|Once the timer is completed, insert it to the report using InsertReportUseCase|`TimerManager.updateTask` <br/> `InsertReportUseCase`|
|12|If the timer was completed, and a task was running, update the task’s pomodoro elapsed time and pomodoro completed|`TimerManager.updateTask` <br/> `UpsertTaskUseCase`|

# Data Layer

The data layer is the source of truth of data in the app.

![Screenshot of Data layer](https://github.com/Zacle/Focused/blob/master/docs/images/Data%20layer.png?raw=true)

Each repository has its own model. For example, the TaskRepository has a TaskResource model and the ReportRepository has a ReportResource model.

Repositories are the public API for other layers, they provide the only way to access the app data. The repositories typically offer one or more methods for reading and writing data.

### Reading data

Data is exposed as data streams. This means each client of the repository must be prepared to react to data changes. Data is not exposed as a snapshot because there's no guarantee that it will still be valid by the time it is used.
Reads are performed from local storage as the source of truth, therefore errors are not expected when reading from Repository instances. However, errors may occur when trying to read data in local storage, then we act accordingly in the domain layer to catch such errors. 


*Example: Read a list of reports*

A list of records can be obtained by subscribing to `ReportRepository.getReportResources` flow which emits `List<ReportResource>`

Whenever the list of reports changes (for example, when a new report is added), the updated `List<ReportResource>` is emitted into the stream.


### Writing data

To write data, the repository provides suspend functions. It is up to the caller to ensure that their execution is suitably scoped.

*Example: Insert a report*

Simply call `ReportRepository.insertReport` with the Report model that we wish to save the database.

### Data sources

The report depends on the local data source (and remote data source coming soon).
Then the local data source depends on one or more data storage. For example:

|Name|Backed by|Purpose|
|----|---------|-------|
|ReportDao|[Room/SQLite](https://developer.android.com/training/data-storage/room)|Persistent relational data associated with Report|
|PomodoroPreferencesDataSource|[Proto DataStore](https://developer.android.com/topic/libraries/architecture/datastore)|Persistent unstructured data associated with user pomodoro preferences. This is defined and modeled in a .proto file, using the protobuf syntax.|

# Domain Layer

The domain layer contains use cases. These are classes that handle errors and failures on the execute method. They accept a Request object and return a Response object.

These use cases are used to simplify and remove duplicate logic from ViewModels. They typically get data from repositories.

They can be combined with other use cases to simplify the Result to be emitted. For example, `GetTasksWithMetadataUseCase` combines a stream of `GetTasksUseCase` with the stream of `GetTasksMetadataUseCase`. The combined stream can then be used by various ViewModels to display the data.

# UI Layer

The UI layer comprises:

* UI elements built using Jetpack Compose
* Android ViewModels

The ViewModels receive streams of data from use cases and repositories, and transform them into UI state. The UI elements reflect this state, and provide ways for the user to interact with the app. These interactions are passed as actions to the ViewModel where they are processed.

![Screenshot of UI layer](https://github.com/Zacle/Focused/blob/master/docs/images/UI%20layer.png?raw=true)

Modeling UI state

UI is modeled as a sealed hierarchy with the UI Model. UI is modeled as follows:

* Obtains the stream of data from the use case
* Convert the use case result using the interface `CommonResultConverter` which return either a `Success` or `Error` with the UI Model if a Success
* Emit the converted result to the UI to handle all possible states

*Example: Task resources on Tasks screen*

The list of tasks on the Tasks Screen is modeled using `UiState`. This is a sealed class which creates a hierarchy of three possible states:

* `Loading` indicates the data is loading
* `Error` indicates there was some unexpected behavior while retrieving the data
* `Success` indicates that the data is loaded successfully. The Success state contains the `TasksUiModel`

The `UiState` is passed to the `TasksScreen` which handles all 3 possible states.

### Processing user interactions

User actions are communicated from UI elements to ViewModels using `UiAction`. These actions are passed to the UI elements as lambda expressions.

*Example: Start running task*

The `TasksScreen` takes a lambda expression named `onStartTaskPressed` which in turn calls ViewModel.`handleAction` with the action `StartRunningTaskPressed`. The user is then navigated to the `TimerScreen` with the task id pressed.







## License

Focused is distributed under the terms of [MIT license](https://choosealicense.com/licenses/mit/)
