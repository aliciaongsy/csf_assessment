package vttp.batch4.csf.ecommerce.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import vttp.batch4.csf.ecommerce.models.Order;
import vttp.batch4.csf.ecommerce.services.PurchaseOrderService;

@Controller
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OrderController {

  @Autowired
  private PurchaseOrderService poSvc;

  // IMPORTANT: DO NOT MODIFY THIS METHOD.
  // If this method is changed, any assessment task relying on this method will
  // not be marked
  @PostMapping(path = "/order")
  @ResponseBody
  public ResponseEntity<String> postOrder(@RequestBody Order order) {

    // TODO Task 3
    try {
      Order o = new Order();
      o.setName(order.getName());
      o.setAddress(order.getAddress());
      o.setPriority(order.getPriority());
      o.setComments(order.getComments());
      o.setCart(order.getCart());
      poSvc.createNewPurchaseOrder(o);

      // success
      JsonObjectBuilder builder = Json.createObjectBuilder();
      builder.add("orderId", o.getOrderId());

      return ResponseEntity.ok(builder.build().toString());

    } catch (Exception e) {
      // error
      JsonObjectBuilder builder = Json.createObjectBuilder();
      builder.add("message", e.getMessage());

      return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(builder.build().toString());
    }
    
  }
}
