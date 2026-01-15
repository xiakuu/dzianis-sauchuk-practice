package com.innowise.orderservice.orderservice.Integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.innowise.orderservice.orderservice.exceptions.ResourceNotFoundException;
import com.innowise.orderservice.orderservice.model.request.OrderRequest;
import com.innowise.orderservice.orderservice.model.response.OrderResponse;
import com.innowise.orderservice.orderservice.repository.OrderRepository;
import com.innowise.orderservice.orderservice.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class OrderServiceIntTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    private WireMockServer wireMockServer;
    @Container
    private static final MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0")).withDatabaseName("test").withUsername("user").withPassword("password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(8078);
        wireMockServer.start();
        wireMockServer.stubFor(WireMock.get("http://localhost:8079/api/users/1")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "id": 1,
                                    "name": "admin",
                                    "surname": "admin",
                                    "birth_date": "1999-09-09",
                                    "email": "admin@server.com"
                                }
                                """)
                ));


    }

    @AfterEach
    void closeWireMockServer() {
        if(wireMockServer.isRunning()){
            wireMockServer.stop();
        }

        orderRepository.deleteAll();
    }

    @Test
    void createOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setStatus("new");
        orderRequest.setUserId(1L);

        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        Assertions.assertNotNull(orderResponse);
        assertThat(orderResponse.getStatus()).isEqualTo("new");
        assertThat(orderResponse.getUser().getName()).isEqualTo("admin");
        assertThat(orderRepository.findById(orderResponse.getId())).isNotEmpty();
    }

    @Test
    void getOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setStatus("new");
        orderRequest.setUserId(1L);
        OrderResponse createOrderResponse = orderService.createOrder(orderRequest);

        OrderResponse orderResponse = orderService.getOrder(createOrderResponse.getId());

        Assertions.assertNotNull(orderResponse);
    }

    @Test
    void getOrder_ResourceNotFoundException(){
        assertThatThrownBy(()-> orderService.getOrder(1123L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getMultipleOrdersByIds(){
        OrderRequest orderRequest1 = new OrderRequest();
        OrderRequest orderRequest2 = new OrderRequest();
        orderRequest1.setStatus("new");
        orderRequest2.setStatus("processing");
        orderRequest1.setUserId(1L);
        orderRequest2.setUserId(1L);
        OrderResponse orderResponse1 = orderService.createOrder(orderRequest1);
        OrderResponse orderResponse2 = orderService.createOrder(orderRequest2);

        List<Long> ids = List.of(orderResponse1.getId(), orderResponse2.getId());
        List<OrderResponse> orderResponses = orderService.getOrdersByIds(ids);

        Assertions.assertNotNull(orderResponses);
        assertThat(orderService.getOrdersByIds(ids).size()).isEqualTo(2);
        assertThat(orderService.getOrdersByIds(ids).get(0).getStatus()).isEqualTo(orderResponse1.getStatus());
        assertThat(orderService.getOrdersByIds(ids).get(1).getStatus()).isEqualTo(orderResponse2.getStatus());
    }

    @Test
    void getMultipleOrdersByIds_ResourceNotFoundException(){
        List<Long> ids = List.of(145L, 2123L);
        assertThatThrownBy(()->orderService.getOrdersByIds(ids)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getMultipleOrdersByStatus(){
        OrderRequest orderRequest1 = new OrderRequest();
        OrderRequest orderRequest2 = new OrderRequest();
        OrderRequest orderRequest3 = new OrderRequest();
        orderRequest1.setStatus("new");
        orderRequest2.setStatus("processing");
        orderRequest3.setStatus("new");

        orderRequest1.setUserId(1L);
        orderRequest2.setUserId(2L);
        orderRequest3.setUserId(3L);

        OrderResponse orderResponse1 = orderService.createOrder(orderRequest1);
        OrderResponse orderResponse2 = orderService.createOrder(orderRequest2);
        OrderResponse orderResponse3 = orderService.createOrder(orderRequest3);

        assertThat(orderService.getAllOrdersByStatus("new")).isNotEmpty();
        assertThat(orderService.getAllOrdersByStatus("new").size()).isEqualTo(2);
        assertThat(orderService.getAllOrdersByStatus("processing")).isNotEmpty();
        assertThat(orderService.getAllOrdersByStatus("processing").size()).isEqualTo(1);
    }

    @Test
    void getMultipleOrdersByStatus_ResourceNotFoundException(){
        assertThatThrownBy(()->orderService.getAllOrdersByStatus("qweqweqwe")).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setStatus("new");
        orderRequest.setUserId(1L);
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        orderRequest.setStatus("processing");
        OrderResponse orderUpdated = orderService.updateOrder(orderResponse.getId(), orderRequest);

        assertThat(orderRepository.findById(orderResponse.getId())).isNotEmpty();
        assertThat(orderUpdated.getStatus()).isEqualTo("processing");
        assertThat(orderUpdated.getUser().getName()).isEqualTo("admin");
    }

    @Test
    void updateOrder_ResourceNotFoundException(){
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setStatus("new");
        assertThatThrownBy(()->orderService.updateOrder(10000L, orderRequest)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setStatus("new");
        orderRequest.setUserId(1L);
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        orderService.deleteOrder(orderResponse.getId());
        assertThat(orderRepository.findById(orderResponse.getId())).isEmpty();
    }

    @Test
    void deleteOrder_ResourceNotFoundException(){
        assertThatThrownBy(()->orderService.deleteOrder(10000L)).isInstanceOf(ResourceNotFoundException.class);
    }
}