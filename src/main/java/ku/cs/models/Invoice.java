package ku.cs.models;

import java.time.LocalDate;
import java.util.Date;

public class Invoice {

    private int roomNum;
    private long invoiceNum;
    private LocalDate invoiceDate;
    private float totalPrice;

    private float paid;
    private int paidStatus;

    public Invoice(int roomNum, long invoiceNum, LocalDate invoiceDate, float totalPrice, float paid, int paidStatus) {
        this.roomNum = roomNum;
        this.invoiceNum = invoiceNum;
        this.invoiceDate = invoiceDate;
        this.totalPrice = totalPrice;
        this.paid = paid;
        this.paidStatus = paidStatus;
    }

    public Invoice(int roomNum, int paidStatus) {
        this.roomNum = roomNum;
        this.paidStatus = paidStatus;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public String getRoomNumStr() {
        return String.valueOf(roomNum);
    }

    public long getInvoiceNum() {
        return invoiceNum;
    }

    public String getInvoiceNumStr() {
        return String.valueOf(invoiceNum);
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public int getPaidStatus() {
        return paidStatus;
    }



}
