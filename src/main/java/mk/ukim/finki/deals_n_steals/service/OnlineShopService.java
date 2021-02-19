package mk.ukim.finki.deals_n_steals.service;

import java.util.List;
import java.util.Map;

public interface OnlineShopService{
    List<Double> profitForYearPerMonths(int year);
    double totalProfit();
    double profitPerYear(int year);
    Map<String, Double> mapYearly(int year);
}

