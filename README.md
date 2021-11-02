Cell Society
====

This project implements a cellular automata simulator.

Names: Morgan Feist, Marcus Deans, Drew Peterson

### Timeline

Start Date: 20 November

Finish Date: 1 November

Hours Spent: 40 Hours each

### Primary Roles

* Morgan: Controller & Model
* Marcus: View & Model
* Drew: View & Model (as well as general problem-solving)

### Resources Used

Minimal external resources were used beyond the project documentation as part of the requirements,
as well as Java/JavaFX official documentation. StackOverflow searches were used periodically to
solve difficult problems such as reflection.

### Running the Program

Main class: Main.java

Data files needed: Requires a .sim file and a .csv file in the same folder (user selected)

Features implemented:

* Game of Life
* Spreading of Fire
* Segregation
* Percolation
* Wa-Tor World
* UI control for all simulations
* Different edge policies can be specified
* Different grid shapes can be specified
* Full language control and interactivity

### Notes/Assumptions

Assumptions or Simplifications:

* User will specify game parameters inside the simulation file that they choose to load

Interesting data files:

Known Bugs:

Noteworthy Features:

* Listeners used to pass information between program

### Impressions

### Error Handling

* Required value not given
* Invalid value given (like a non-existent simulation, edge policy, neighborhood arrangement, or
  negative percentage)
* Invalid cell state values given
* Cell locations given that are outside the bounds of the grid's size empty, badly formatted, or
  non-CSV files given In all of htese cases, the GameController or InputParser detects the error by
  comparing the aberrant input to the internal record of appropriate inputs. Upon identifying the
  error, it will then send an alert to the user detailing the issue and allowing the user to select
  a new file (they can modify the simulation file externally to match appropriate format)

