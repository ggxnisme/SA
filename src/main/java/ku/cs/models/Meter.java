package ku.cs.models;

import java.time.LocalDate;

public class Meter {

    private int roomNum;
    private LocalDate meterDate;
    private String lastMonthWaterMeter;
    private String waterMeter;
    private String lastMonthElectMeter;
    private String electMeter;
    private int waterUnitPrice;
    private int electUnitPrice;
    private float waterPrice;
    private float electPrice;

    private int waterUnit;

    private int electUnit;

    public Meter(int roomNum, LocalDate meterDate, String waterMeter, String electMeter, int waterUnit, int electUnit, int waterUnitPrice, int electUnitPrice, float waterPrice, float electPrice) {
        this.roomNum = roomNum;
        this.meterDate = meterDate;
        this.waterMeter = waterMeter;
        this.electMeter = electMeter;
        this.waterUnit = waterUnit;
        this.electUnit = electUnit;
        this.waterUnitPrice = waterUnitPrice;
        this.electUnitPrice = electUnitPrice;
        this.waterPrice = waterPrice;
        this.electPrice = electPrice;

    }

    public int getRoomNum() {
        return roomNum;
    }

    public LocalDate getMeterDate() {
        return meterDate;
    }

    public String getLastMonthWaterMeter() {
        return lastMonthWaterMeter;
    }

    public String getWaterMeter() {
        return waterMeter;
    }

    public String getLastMonthElectMeter() {
        return lastMonthElectMeter;
    }

    public String getElectMeter() {
        return electMeter;
    }

    public int getWaterUnitPrice() {
        return waterUnitPrice;
    }

    public int getElectUnitPrice() {
        return electUnitPrice;
    }

    public float getWaterPrice() {
        return waterPrice;
    }

    public float getElectPrice() {
        return electPrice;
    }

    public int getWaterUnit() {
        return waterUnit;
    }

    public int getElectUnit() {
        return electUnit;
    }
}
