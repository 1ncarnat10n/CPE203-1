/**
 * Created by Justin Nguyen on 10/11/2017.
 */
public class BuyAction {

    private String sessionID;
    private Product product;
    private int quantity;

    public BuyAction(String sessionID, Product product, int quantity) {
        this.sessionID = sessionID;
        this.product = product;
        this.quantity = quantity;
    }

    public String getSessionID(){
        return sessionID;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

}
