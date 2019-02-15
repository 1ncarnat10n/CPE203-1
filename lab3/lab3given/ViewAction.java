/**
 * Created by Justin Nguyen on 10/11/2017.
 */
public class ViewAction {

    private String sessionID;
    private Product product;

    public ViewAction(String sessionID, Product product) {
        this.sessionID = sessionID;
        this.product = product;
    }

    public String getSessionID() {
        return sessionID;
    }

    public Product getProduct() {
        return product;
    }

}
