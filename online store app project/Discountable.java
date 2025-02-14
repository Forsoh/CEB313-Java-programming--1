// Interface that defines methods for products that can be discounted
public interface Discountable {
    void applyDiscount(double percentage);        // Method to apply discount
    double calculateDiscountedPrice();            // Method to calculate the discounted price
}
