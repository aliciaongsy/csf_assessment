package vttp.batch4.csf.ecommerce.controllers;


import java.io.StringReader;

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
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import vttp.batch4.csf.ecommerce.models.Cart;
import vttp.batch4.csf.ecommerce.models.LineItem;
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
  public ResponseEntity<String> postOrder(@RequestBody String payload) {

    // TODO Task 3
    try {
      JsonReader reader = Json.createReader(new StringReader(payload));
      JsonObject order = reader.readObject();
      Order o = new Order();
      o.setName(order.getString("name"));
      o.setAddress(order.getString("address"));
      o.setPriority(order.getBoolean("priority"));
      o.setComments(order.getString("comments"));
      
      JsonArray cart = order.getJsonObject("cart").getJsonArray("lineItems");

      Cart c = new Cart();
      for (JsonValue v:cart){
        JsonObject obj = v.asJsonObject();
        LineItem item = new LineItem();

        item.setName(obj.getString("prodId"));
        item.setPrice(obj.getJsonNumber("price").bigDecimalValue().floatValue());
        item.setProductId(obj.getString("prodId"));
        item.setQuantity(obj.getInt("quantity"));
        c.addLineItem(item);
      }
      o.setCart(c);
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
