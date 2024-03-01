package vttp.batch4.csf.ecommerce.repositories;

public class OrderException extends Exception{
    public OrderException(){
        super();
    }

    public OrderException(String message){
        super(message);
    }
}
