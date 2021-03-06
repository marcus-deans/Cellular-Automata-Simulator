# Cell Society Design Plan

### Team Number 15

### Names

* Morgan Feist
* Marcus Deans
* Drew Peterson

## Design Overview
> Design Overview. Describe the classes you intend to create, without revealing any specific implementation details (like data structures, file formats, etc.). CRC cards can be a very useful way to develop your design.

###Model:
* Cells:
  * Cell:
    * Abstract class for general cell
    * Will allow access to x,y of cell
    * Also method to get state of cell
  * LifeCell
    * Can either be dead (0) or alive (1)
  * FireCell
    * Can be empty (0), tree (1), or on fire (2)
  * SegCell
    * Can be empty (0), blue (1), or red (2)
  * WatorCell
    * Can be water (0), fish (1), or shark (2)

* GameGrids:
  * GameGrid:
    * Abstract class for general grid
    * Contains Cell[][] array of cells
    * Determines which cells are neighbours
    * Allows for cell values to be updated
    * Saved updated cell state in new grid
  * LifeGrid
    * Iterate through the grid -> for each cell apply game rules based on computed neighbours
  * FireGrid 
    * Iterate through the grid -> for each cell apply game rules based on computed neighbours
  * SegGrid
    * Iterate through the grid -> for each cell apply game rules based on computed neighbours
  * WatorGrid
    * Iterate through the grid -> for each cell apply game rules based on computed neighbours


###View:
* GameView
  * Abstract JavaFX display for all games
  * Allows for all user interactions
    * Stop/Start button
    * Step button
    * Save button
    * Load button
    * Clear button
    * Change speed slier
  * Display all game details dynamically
* LifeView
  * View Game of Life animation play
  * Remove 'parameters' as not part of game
* FireView
  * View Fire animation
  * Use dark mode by default
* SegView
  * View Segregation animation
* WatorView
  * View Wator animation
  * use Duke blue mode by default

###Controller:
* GameController
  * Mediate data transfer between the model and view
  * Pass initial grid to the model and create
  * Facilitate saving and loading of files
* InputParser
  * Read the input file
  * Iterate through and read in data
  * Create grid based on data dimension
  * Populate grid with start conditions based on data
## Design Details
> Design Details. Describe how your classes work together to handle specific features (like collaborating with other classes, using resource files, or serving as an abstraction that could be extended). Use Cases can be a very useful way to help make your descriptions more concrete.
* One important feature is parsing an input CSV file and using it to initialize the states of cells in the grid. The view
class containing the main user interface will have a button allowing the user to select a file, which the view will then
pass directly to the model. From there, the model will use the provided file parser to obtain the relevant information
before initializing a new grid based on that information.
* For different cell society applications, cells can have different behaviors and states. To handle this, there will be 
an abstract Cell superclass that specifies all the key methods a cell should have regardless of application. Then, a
subclass of Cell can be created for each application that contains the specific states and behaviors needed.
* The model will likely update the view using an observable pattern, which is further detailed in the Design Considerations
below

## Design Considerations
> Design Considerations. Describe at least two design issues your team encountered (even if you have not yet resolved it). Address any alternatives the team considered including pros and cons from all sides of the discussion.
#### Design Issue #1
One major design issue we thought about was how autonomous the cells should be and whether they should
be able to look at their neighbors or whether that should be a grid functionality.

* Alternative #1: Cells contain information about their neighbors and therefore can check conditions
individually and determine state based on that.
* Alternative #2: Cells just contain their own state and grid checks neighbors for them.
* Trade-offs:
  * If a cell has neighbor information this means that information needs to be constantly updated as the grid is updated.
  * If the grid keeps track of neighbor information it is slightly easier to implement and allows grid
to have the role of managing cells instead of cells managing themselves
  * However, in the future the cells may have to have more agency so this logic may not always be true


#### Design Issue #2
Another design issue we haven't resolved is how the model should interact with the view and inform it of state changes 
to cells in the grid.

* Alternative #1: The model passes a data structure to the view that contains information about the new cell states. 
Ideally, this data structure is immutable so the view cannot modify it or do anything with it other than parse it to 
determine how it should update its display.

* Alternative #2: The model uses a PropertyChangeSupport object to send updates to the view, which implements
PropertyChangeListener (or other, similar observable pattern). In this case, the model would have to send 3 updates for 
each cell in the grid: one for the row of the cell being updated, one for the column of the cell being updated, and one
for the new state of the cell being updated.

* Trade-offs:
  * In order to pass a data structure to the view, the model has to have a reference to the view. This violates the
  separation of the model and the view.
  * If the model changes the type of data structure it uses to represent the grid and then passes the new data structure
  to the view, errors would occur in the view without also changing its parsing code
  * If an observable pattern is used to send updates to the view, numerous updates have to be sent for each cell
  * If an observable pattern is used and the model changes the indexing system for cells, errors would in the view without
  also changing its parsing code

## User Interface
> User Interface. Include one or more pictures of the user interface (these pictures can be hand drawn and scanned in, created with a standard drawing program, or screen shots from a dummy program that serves as a exemplar). Describe any erroneous situations that are reported to the user (i.e., bad input data, empty data, etc.).

Here is our intended UI which we drew out for Game of Life. We included the 'parameters' field to
show what values would appear like, even though Game of Life has no parameters. In this case the
parameters label would be present but the field would be empty. For illustrative purposes, potential
parameters for a Fire game were included in the image.

![Design UI](images/game_of_life_ui_design.png "An alternate design")

Alert Cases:
* Corrupted input data
* Empty data (missing file)
* Missing file name for save

## Team Responsibilities
> Team Responsibilities. List which parts of the project each team member plans to take primary and secondary responsibility for and a high-level schedule of when the team will complete different parts of the program.

* Morgan Feist
  * Model

* Marcus Deans
  * Controller and view

* Drew Peterson
  * View

#### Proposed Schedule
* 19 October: Test
  * Game of Life working individually
  * UI for Game of Life built
  * Controller built to read CSV
* 26 October: Expansion
  * Game of Life completely implemented and tested
  * Abstractions refined for easy use in other applications
  * Work begun on second application
