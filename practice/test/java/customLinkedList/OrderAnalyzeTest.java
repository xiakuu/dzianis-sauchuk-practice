package customLinkedList;
import javaCore.analysis.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class OrderAnalyzeTest {
    private OrderAnalyzeMetrics orderAnalyzeMetrics;
    private List<Order> orders;

    private Order createOrder(String orderId, String customerName, String city, OrderStatus status, List<OrderItem> items) {
        Customer customer = new Customer();
        customer.setCustomerId(customerName);
        customer.setName(customerName);
        customer.setCity(city);

        Order order = new Order();
        order.setOrderId(orderId);
        order.setCustomer(customer);
        order.setItems(items);
        order.setStatus(status);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    private OrderItem createOrderItem(String productName, int quantity, double price) {
        OrderItem item = new OrderItem();
        item.setProductName(productName);
        item.setQuantity(quantity);
        item.setPrice(price);
        return item;
    }



    @BeforeEach
    void setUp() {
        orderAnalyzeMetrics = new OrderAnalyzeMetrics();
        orders = Arrays.asList(
                createOrder("1", "Customer1", "city1", OrderStatus.DELIVERED,
                        Arrays.asList(createOrderItem("product1", 1, 100))),
                createOrder("2", "Customer2", "city2", OrderStatus.DELIVERED,
                        Arrays.asList(createOrderItem("product2", 4, 50))),
                createOrder("3", "Customer1", "city1", OrderStatus.CANCELLED,
                        Arrays.asList(createOrderItem("product3", 1, 30))),
                createOrder("4", "Customer1", "city1", OrderStatus.DELIVERED,
                        Arrays.asList(createOrderItem("product1", 2, 100))),
                createOrder("5", "Customer3", "city3", OrderStatus.DELIVERED,
                        Arrays.asList(createOrderItem("product4", 1, 20))),
                createOrder("6", "Customer2", "city2", OrderStatus.DELIVERED,
                        Arrays.asList(createOrderItem("product2", 1, 50))),
                createOrder("7", "Customer2", "city2", OrderStatus.PROCESSING,
                        Arrays.asList(createOrderItem("product2", 2, 25))),
                createOrder("8", "Customer1", "city1", OrderStatus.DELIVERED,
                        Arrays.asList(createOrderItem("product3", 1, 30))),
                createOrder("9", "Customer1", "city1", OrderStatus.DELIVERED,
                        Arrays.asList(createOrderItem("product1", 1, 100))),
                createOrder("10", "Customer1", "city1", OrderStatus.DELIVERED,
                        Arrays.asList(createOrderItem("product1", 1, 100))),
                createOrder("11", "Customer1", "city1", OrderStatus.DELIVERED,
                        Arrays.asList(createOrderItem("product1", 1, 100)))  //product1; customer1
        );
    }

    @Test
    void testGetUniqueCity() {
        var uniqueCity = orderAnalyzeMetrics.getUniqueCities(orders);
        assertEquals(Set.of("city1", "city2", "city3"), uniqueCity);
    }

    @Test
    void testGetTotalIncomeForCompleted() {
        double totalIncome = orderAnalyzeMetrics.getTotalIncome(orders);
        assertEquals(900.0, totalIncome);
    }

    @Test
    void testGetMostPopularProductBySales() {
        String mostPopularProduct = orderAnalyzeMetrics.getMostPopularProductBySales(orders);
        assertEquals("product2", mostPopularProduct);
    }

    @Test
    void testGetAverageCheckForDeliveredOrders() {
        double averageCheck = orderAnalyzeMetrics.getAverageCheckForDeliveredOrders(orders);
        assertEquals(100.0, averageCheck);  //здесь считать доставку как за одну или делить по количеству товара?
    }

    @Test
    void testGetFrequentCustomers() {
        List<Customer> frequentCustomers = orderAnalyzeMetrics.getFrequentCustomers(orders);
        assertEquals(1, frequentCustomers.size());
        assertEquals("Customer1", frequentCustomers.get(0).getName());
    }
}
