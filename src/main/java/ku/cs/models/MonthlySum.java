package ku.cs.models;

import java.time.LocalDate;

public class MonthlySum {

    private LocalDate date;

    private int roomUnitAmount;

    private int waterUnitAmount;

    private int electUnitAmount;

    private float roomPriceAmount;

    private float waterPriceAmount;

    private float electPriceAmount;

    private float totalAmount;

    private float paidAmount;

    private float owedAmount;

    public MonthlySum(LocalDate date, int roomUnitAmount, int waterUnitAmount, int electUnitAmount, float roomPriceAmount, float waterPriceAmount, float electPriceAmount, float totalAmount, float paidAmount, float owedAmount) {
        this.date = date;
        this.roomUnitAmount = roomUnitAmount;
        this.waterUnitAmount = waterUnitAmount;
        this.electUnitAmount = electUnitAmount;
        this.roomPriceAmount = roomPriceAmount;
        this.waterPriceAmount = waterPriceAmount;
        this.electPriceAmount = electPriceAmount;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.owedAmount = owedAmount;
    }

    public int getRoomUnitAmount() {
        return roomUnitAmount;
    }

    public int getWaterUnitAmount() {
        return waterUnitAmount;
    }

    public int getElectUnitAmount() {
        return electUnitAmount;
    }

    public float getRoomPriceAmount() {
        return roomPriceAmount;
    }

    public float getWaterPriceAmount() {
        return waterPriceAmount;
    }

    public float getElectPriceAmount() {
        return electPriceAmount;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public float getPaidAmount() {
        return paidAmount;
    }

    public float getOwedAmount() {
        return owedAmount;
    }

    public LocalDate getDate() {
        return date;
    }
}
