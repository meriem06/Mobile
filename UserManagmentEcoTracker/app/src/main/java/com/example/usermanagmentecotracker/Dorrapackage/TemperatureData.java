package com.example.usermanagmentecotracker.Dorrapackage;

import java.util.ArrayList;
import java.util.List;

public class TemperatureData {
    private static List<TemperatureEntry> temperatureList = new ArrayList<>();

    public static List<TemperatureEntry> getTemperatureList() {
        return temperatureList;
    }

    public static void addTemperatureEntry(TemperatureEntry entry) {
        temperatureList.add(entry);
    }
}
