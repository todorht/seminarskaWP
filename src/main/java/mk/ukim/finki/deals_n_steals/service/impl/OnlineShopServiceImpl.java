package mk.ukim.finki.deals_n_steals.service.impl;

import mk.ukim.finki.deals_n_steals.model.Order;
import mk.ukim.finki.deals_n_steals.repository.jpa.OnlineShopRepository;
import mk.ukim.finki.deals_n_steals.repository.jpa.OrderRepository;
import mk.ukim.finki.deals_n_steals.service.OnlineShopService;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class OnlineShopServiceImpl implements OnlineShopService {
    private final OrderRepository repository;

    public OnlineShopServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Double> profitPerMonths() {
        List<Double> perMonth = new ArrayList<>(12);
        List<Order> orders = this.repository.findAll();
        IntStream.range(0,12).forEach(i->perMonth.add(0.0));
        IntStream.range(0,12).forEach(i->perMonth
                .set(i,
                        ( this.repository.findAll().stream()
                        .filter(order -> order.getCreateTime().getMonthValue()==i+1)
                        .collect(Collectors.toList()).size()<=0) ? 0.0 :
                        this.repository.findAll().stream()
                                .filter(order -> order.getCreateTime().getMonthValue()==i+1)
                                .mapToDouble(Order::getTotal)
                                .sum()
                )
        );


        return perMonth;
    }

    @Override
    public double totalProfit() {
        return this.repository.findAll()
                .stream()
                .mapToDouble(Order::getTotal)
                .sum();
    }


}
