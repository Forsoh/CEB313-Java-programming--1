public class Main {
    public static void main(String[] args) {
        Store store = new Store();  // Initialize store
        ShoppingCart shoppingCart = new ShoppingCart();  // Initialize shopping cart

        StoreOwnerInterface ownerInterface = new StoreOwnerInterface(store);
        ownerInterface.setVisible(true);  // Show store owner interface

       CustomerInterface customerInterface = new CustomerInterface(store, shoppingCart);
        customerInterface.setVisible(true);  // Show customer interface
    }
}
