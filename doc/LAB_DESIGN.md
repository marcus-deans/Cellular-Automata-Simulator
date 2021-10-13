# Cell Society Design Discussion
### Team Number
### Names


### Discussion Questions

 * How does a Cell know what rules to apply for its simulation?

 * How does a Cell know about its neighbors?

 * How can a Cell update itself without affecting its neighbors update?

 * What behaviors does the Grid itself have?

 * How can a Grid update all the Cells it contains?

 * How is configuration information used to set up a simulation?

 * How is the GUI updated after all the cells have been updated?


### Alternate Designs

#### Design Idea #1

 * Data Structure #1 and File Format #1

 * Data Structure #2 and File Format #2


#### Design Idea #2

 * Data Structure #1 and File Format #1

 * Data Structure #2 and File Format #2


#### Design Idea #3

* Data Structure #1 and File Format #1

 * Data Structure #2 and File Format #2



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


This class's purpose or value is to represent a customer's order:
```java
public class Order {
     // returns whether or not the given items are available to order
     public boolean isInStock (OrderLine items)
     // sums the price of all the given items
     public double getTotalPrice (OrderLine items)
     // returns whether or not the customer's payment is valid
     public boolean isValidPayment (Customer customer)
     // dispatches the items to be ordered to the customer's selected address
     public void deliverTo (OrderLine items, Customer customer)
 }
 ```
 

### Use Cases

 * Apply the rules to a cell: set the next state of a cell to dead by counting its number of neighbors using the Game of Life rules for a cell in the middle (i.e., with all of its neighbors)
```java
 OrderLine items = new OrderLine();
 if (order.isInStock(items)) {
     total = order.getTotalPrice(items);
 }
```

 * Move to the next generation: update all cells in a simulation from their current state to their next state
```java
 OrderLine items = new OrderLine();
 if (order.isInStock(items)) {
     total = order.getTotalPrice(items);
 }
```

 * Switch simulations: load a new simulation from a data file, replacing the current running simulation with the newly loaded one
```java
 OrderLine items = new OrderLine();
 if (order.isInStock(items)) {
     total = order.getTotalPrice(items);
 }
```
