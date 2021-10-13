# Cell Society Design Discussion

### Team Number 15

### Names

Morgan Feist (mjf58), Marcus Deans (md374),

### Discussion Questions

* How does a Cell know what rules to apply for its simulation?
    * The cell will not know about what rules are being applied in its simulation; instead, it will
      simply keep track of its state (either 0/1 options for Life, or 0/1/2 options for other games)
    * The model will contain rules and will perform computations to determine neighbours via looping
      through the entire grid
* How does a Cell know about its neighbors?
    * The model will hold a cell grid which it uses to determine the state of the current cell and
      its neighbors by checking adjacent cells by index.
* How can a Cell update itself without affecting its neighbors update?
    * The model will first loop through the grid and perform all of the calculations, which will
      then be stored in a futureArray
    * The futureArray will then be set as the new grid
    * The model will then loop through the new grid and update each Cell's value
* What behaviors does the Grid itself have?
    * The grid calls an update method which loops through all the values to find all new cell values
    * It then is updated to hold all these new values to be passed to the view
* How can a Grid update all the Cells it contains?
    * As described above, the model will be looping through the entire grid to
        * a) calculate the new values
        * b) save the new values
        * c) update the Cell values to their new values
* How is configuration information used to set up a simulation?
    * The rules are stored by the relevant controller which has them saved (hard-coded)
    * The simulation file will specify which game rules are being used
    * The controller then acts on each cell and tells it what to do
* How is the GUI updated after all the cells have been updated?
    * After all the cells have been updated, the model will notify the view and send it a list (or
      2D array?) of the states of all of its cells and the view will loop through it and update the
      cells on screen as necessary

### Alternate Designs

#### Design Idea #1
The cells individually have no knowledge of their neighbors. The grid itself is keeping track of 
where the cells are located and their neighbors' behavior. From this it determines how the cell states
should change.

######Implementation #1:

* Grid: ```Cell[][]``` and ```.csv``` for data

######Implementation #2:

* Grid: ```List<List<Cell>>``` and ```.json``` for data

#### Design Idea #2

The model will consist of the grid, which itself will be composed of the relevant Cell class for the
given program. Each Cell will keep track of its neighbours and the relevant rules that it needs to
perform. The model will iterate through the grid and call ```.update()``` on each Cell, which will
lead it to reference its neighbours and store its computation within itself. Once computation is complete, the model/grid will signal to all cells that they should set the ```computedValue``` equal to their ```statevalue```.

######Implementation #1:
* Grid: ```Cell[][]``` and File ```.csv``` for data

######Implementation #2:
* Grid: ```List<List<Cell>>``` and ```.json``` for data

#### Design Idea #3

######Implementation #1:
* Grid: ```Cell[][]``` and File ```.csv``` for data

######Implementation #2:
* Grid: ```List<List<Cell>>``` and ```.json``` for data

### CRC Card Classes

This class's purpose or value is to represent a customer's order:
![Order Class CRC Card](images/order_crc_card.png "Order Class")

This class's purpose or value is to represent a customer's order:

|Order| |
|---|---|
|boolean isInStock(OrderLine)         |OrderLine|
|double getTotalPrice(OrderLine)      |Customer|
|boolean isValidPayment (Customer)    | |
|void deliverTo (OrderLine, Customer) | |

This class's purpose or value is:

|Model| |
|---|---|
|Cell[][] updateCellValues(OrderLine)         |Cell|
|double getTotalPrice(OrderLine)      |Grid|
|boolean isValidPayment (Customer)    | |
|void deliverTo (OrderLine, Customer) | |

This class's purpose or value is:

|Grid| |
|---|---|
|update()         |Cell|
|getNeighborState      |Customer|
|boolean isValidPayment (Customer)    | |
|void deliverTo (OrderLine, Customer) | |

This class's purpose or value is:

 ```

### Use Cases

* Apply the rules to a cell: set the next state of a cell to dead by counting its number of
  neighbors using the Game of Life rules for a cell in the middle (i.e., with all of its neighbors)

```java
int aliveNeighbors = 0;
for(currentNeighbor : all neighbors){
    if(currentNeighbor is ALIVE){
      aliveNeighbors++;
    }
}
if(aliveNeighbors < 2){
    cellState = DEAD;
}
if(aliveNeighbors ){
}
```

* Move to the next generation: update all cells in a simulation from their current state to their
  next state

```java
 OrderLine items=new OrderLine();
    if(order.isInStock(items)){
    total=order.getTotalPrice(items);
    }
```

* Switch simulations: load a new simulation from a data file, replacing the current running
  simulation with the newly loaded one

```java
 OrderLine items=new OrderLine();
    if(order.isInStock(items)){
    total=order.getTotalPrice(items);
    }
```
