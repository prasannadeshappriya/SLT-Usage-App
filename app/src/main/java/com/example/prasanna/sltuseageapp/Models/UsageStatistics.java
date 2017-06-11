package com.example.prasanna.sltuseageapp.Models;

/**
 * Created by prasanna on 6/11/17.
 */

public class UsageStatistics {
    private Long id;
    private Double usedValue;
    private Double maximumValue;
    private String period;
    private Double availableData;
    private Double totalData;

    public UsageStatistics(Double usedValue, Double maximumValue, String period, Double availableData, Double totalData) {
        this.usedValue = usedValue;
        this.maximumValue = maximumValue;
        this.period = period;
        this.availableData = availableData;
        this.totalData = totalData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getUsedValue() {
        return usedValue;
    }

    public void setUsedValue(Double usedValue) {
        this.usedValue = usedValue;
    }

    public Double getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(Double maximumValue) {
        this.maximumValue = maximumValue;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Double getAvailableData() {
        return availableData;
    }

    public void setAvailableData(Double availableData) {
        this.availableData = availableData;
    }

    public Double getTotalData() {
        return totalData;
    }

    public void setTotalData(Double totalData) {
        this.totalData = totalData;
    }
}
