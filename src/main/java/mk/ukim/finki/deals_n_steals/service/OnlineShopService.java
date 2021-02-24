package mk.ukim.finki.deals_n_steals.service;

import java.util.List;
import java.util.Map;

public interface OnlineShopService{

    List<Double> profitForYearPerMonths(int year);

    double totalProfit();

    Map<String, Double> mapYearly(int year);
}

