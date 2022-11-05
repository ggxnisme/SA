package ku.cs.models;

import java.time.LocalDate;
import java.util.Date;

public class Invoice {

    private int roomNum;
    private long invoiceNum;
    private LocalDate invoiceDate;
    private float roomPrice;
    private float totalPrice;
    private float customerPaid;
    private int paidStatus;
    private LocalDate receiptDate;

    public Invoice(int roomNum, long invoiceNum, LocalDate invoiceDate, float roomPrice, float totalPrice, float customerPaid, int paidStatus, LocalDate receiptDate) {
        this.roomNum = roomNum;
        this.invoiceNum = invoiceNum;
        this.invoiceDate = invoiceDate;
        this.roomPrice = roomPrice;
        this.totalPrice = totalPrice;
        this.customerPaid = customerPaid;
        this.paidStatus = paidStatus;
        this.receiptDate = receiptDate;
    }

    public Invoice(int roomNum, long invoiceNum, LocalDate invoiceDate, float roomPrice, float totalPrice, float customerPaid, int paidStatus) {
        this.roomNum = roomNum;
        this.invoiceNum = invoiceNum;
        this.invoiceDate = invoiceDate;
        this.roomPrice = roomPrice;
        this.totalPrice = totalPrice;
        this.customerPaid = customerPaid;
        this.paidStatus = paidStatus;
    }

    public Invoice(int roomNum, int paidStatus) {
        this.roomNum = roomNum;
        this.paidStatus = paidStatus;
    }

    public String getRoomNum() {
        return String.valueOf(roomNum);
    }

    public long getInvoiceNum() {
        return invoiceNum;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public float getRoomPrice() {
        return roomPrice;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public float getCustomerPaid() {
        return customerPaid;
    }

    public int getPaidStatus() {
        return paidStatus;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }
}
