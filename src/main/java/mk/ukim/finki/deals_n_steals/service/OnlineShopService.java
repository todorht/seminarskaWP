package mk.ukim.finki.deals_n_steals.service;

import mk.ukim.finki.deals_n_steals.model.OnlineShop;

import java.util.List;

public interface OnlineShopService{
    List<Double> profitPerMonths();
    double totalProfit();
}
