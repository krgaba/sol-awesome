package com.sol.awesome.employee.domain;

public enum Office {
    NewYork("New York"), Chicago("Chicago"), BA("Buenos Aires");


    Office(String cityName) {
        this.cityName = cityName;
    }

    private String cityName;

    @Override
    public String toString() {
        return cityName;
    }
}

