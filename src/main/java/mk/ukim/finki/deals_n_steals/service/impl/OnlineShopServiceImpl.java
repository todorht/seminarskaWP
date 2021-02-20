package mk.ukim.finki.deals_n_steals.service.impl;

import mk.ukim.finki.deals_n_steals.model.Order;
import mk.ukim.finki.deals_n_steals.repository.OrderRepository;
import mk.ukim.finki.deals_n_steals.service.OnlineShopService;
import org.springframework.stereotype.Service;

import java.text.DateFormatSymbols;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class OnlineShopServiceImpl implements OnlineShopService {

    private final OrderRepository repository;

    public OnlineShopServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    private String[] getMonths(){
        DateFormatSymbols dfs = new DateFormatSymbols();
        return dfs.getMonths();
    }

    @Override
    public List<Double> profitForYearPerMonths(int year) {
        List<Double> perMonth = new ArrayList<>(12);
        List<Order> orders = this.repository.findAll()
                .stream()
                .filter(order -> order.getCreateTime().getYear()==year)
                .collect(Collectors.toList());
        IntStream.range(0,12).forEach(i->perMonth.add(0.0));
        if(orders.size()>0) {
            IntStream.range(0, 12)
                    .forEach(i ->
                            perMonth.set(i,
                                    (this.repository.findAll().stream()
                                            .filter(order -> order.getCreateTime().getMonthValue() == i + 1).count() <= 0) ? 0.0 :
                                    this.repository.findAll().stream()
                                            .filter(order ->order.getCreateTime().getYear()==year && order.getCreateTime().getMonthValue() == i + 1)
                                            .mapToDouble(Order::getTotal)
                                            .sum()
                    )
            );
        }
        return perMonth;
    }

    @Override
    public double totalProfit() {
        return this.repository.findAll()
                .stream()
                .mapToDouble(Order::getTotal)
                .sum();
    }

    @Override
    public double profitPerYear(int year) {
        return this.repository.findAll()
                .stream()
                .filter(order -> order.getCreateTime().getYear()==year)
                .mapToDouble(Order::getTotal)
                .sum();
    }

    @Override
    public Map<String, Double> mapYearly(int year) {
        List<Double> perMonth = this.profitForYearPerMonths(year);
        Map<String, Double> totalPerMonth = new HashMap<>();
        int i = 0;
        for(String m: getMonths()){
            if(i!=12) {
                totalPerMonth.put(m, perMonth.get(i));
            }
            i++;
        }
        return totalPerMonth;
    }


}
