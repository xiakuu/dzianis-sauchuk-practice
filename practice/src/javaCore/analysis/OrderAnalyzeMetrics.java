package javaCore.analysis;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class OrderAnalyzeMetrics {

    public Set<String> getUniqueCities(List<Order> orders) {
        return orders.stream()
                .map(order -> order.getCustomer().getCity())
                .collect(Collectors.toSet());

    }

    public double getTotalIncome(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .flatMap(order -> order.getItems().stream())
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public String getMostPopularProductBySales(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.toMap(OrderItem::getProductName, OrderItem::getQuantity, Integer::sum))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public double getAverageCheckForDeliveredOrders(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .mapToDouble(order -> order.getItems().stream()
                        .mapToDouble(item -> item.getPrice() * item.getQuantity()).sum())
                .average().orElse(0.0);
    }

    public List<Customer> getFrequentCustomers(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(order -> order.getCustomer().getCustomerId(), Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 5)
                .map(entry -> orders.stream()
                        .filter(order -> order.getCustomer().getCustomerId().equals(entry.getKey()))
                        .findFirst()
                        .map(Order::getCustomer)
                        .orElse(null))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }
}
