package vttp.batch4.csf.ecommerce.repositories;

public class LineItemException extends Exception{
    public LineItemException(){
        super();
    }

    public LineItemException(String message){
        super(message);
    }
}
