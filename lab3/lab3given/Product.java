/**
 * Created by Justin Nguyen on 10/11/2017.
 */
public class Product {

    private String id;
    private int price;

    public Product(String id, int price) {
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }
}
