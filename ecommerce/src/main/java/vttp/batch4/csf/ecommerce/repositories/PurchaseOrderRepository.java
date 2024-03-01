package vttp.batch4.csf.ecommerce.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch4.csf.ecommerce.models.LineItem;
import vttp.batch4.csf.ecommerce.models.Order;

@Repository
public class PurchaseOrderRepository {

  @Autowired
  private JdbcTemplate template;

  private static final String SQL_ADD_SHOPPING_ORDER= """
      insert into shopping_order(orderId, date, name, address, priority, comments)
        values (?, ?, ?, ?, ?, ?)
      """;

  private static final String SQL_ADD_LINE_ITEMS = """
      insert into lineItem(productId, name, quantity, price, orderId)
        values (?, ?, ?, ?, ?)
      """;

  // IMPORTANT: DO NOT MODIFY THIS METHOD.
  // If this method is changed, any assessment task relying on this method will
  // not be marked
  // You may only add Exception to the method's signature
  public void create(Order order) throws OrderException, LineItemException {
    // TODO Task 3
    // add to shopping_cart table
    System.out.println("--adding into shopping_cart table--");
    int updateOrder = template.update(SQL_ADD_SHOPPING_ORDER, order.getOrderId(), order.getDate(), 
      order.getName(), order.getAddress(), order.getPriority(), order.getComments());
    
      if (updateOrder==0){
      throw new OrderException("Error: order not added");
    }

    // loop through items and add each item to lineitem table
    int count = 0;
    List<LineItem> items = order.getCart().getLineItems();
    for (int i = 0; i < items.size(); i ++){
      System.out.println("--adding into lineitem table--");
      LineItem item = items.get(i);
      int updateLineItem = template.update(SQL_ADD_LINE_ITEMS, item.getProductId(), item.getName(), 
        item.getQuantity(), item.getPrice(), order.getOrderId());
      count += updateLineItem;
      
    }
    if (count == items.size()){
      throw new LineItemException("test");
    }
    
    if (count < items.size()){
      System.out.println("error adding to table");
      throw new LineItemException("Error: not all items are added");
    }
    
  }
}
