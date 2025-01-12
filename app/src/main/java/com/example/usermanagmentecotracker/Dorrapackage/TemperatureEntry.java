package com.example.usermanagmentecotracker.Dorrapackage;

public class TemperatureEntry {
    private String temperature;
    private String date;

    public TemperatureEntry(String temperature, String date) {
        this.temperature = temperature;
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getDate() {
        return date;
    }

    public String getRecommendation() {
        try {
            float temp = Float.parseFloat(temperature);
            if (temp > 32) {
                return "Climatisation recommandé";
            } else if (temp < 15) {
                return "Chauffage recommandé" ;
            } else {
                return "Température idéale";
            }

        } catch (NumberFormatException e) {
            return "Température invalide";
        }
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TemperatureEntry{" +
                "temperature='" + temperature + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
