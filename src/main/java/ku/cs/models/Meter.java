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

    public Meter(int roomNum, LocalDate meterDate, String lastMonthWaterMeter, String waterMeter, String lastMonthElectMeter, String electMeter, int waterUnitPrice, int electUnitPrice, float waterPrice, float electPrice, int waterUnit, int electUnit) {
        this.roomNum = roomNum;
        this.meterDate = meterDate;
        this.lastMonthWaterMeter = lastMonthWaterMeter;
        this.waterMeter = waterMeter;
        this.lastMonthElectMeter = lastMonthElectMeter;
        this.electMeter = electMeter;
        this.waterUnitPrice = waterUnitPrice;
        this.electUnitPrice = electUnitPrice;
        this.waterPrice = waterPrice;
        this.electPrice = electPrice;
        this.waterUnit = waterUnit;
        this.electUnit = electUnit;
    }


}
