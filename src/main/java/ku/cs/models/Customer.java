package ku.cs.models;

public class Customer {

    private int roomNum;
    private String idCard;
    private String name;
    private String address;
    private String phoneNum;
    private String rentalPeriod;
    private float deposit;
    private float owed;

    public Customer(int roomNum, String idCard, String name, String address, String phoneNum, String rentalPeriod, float deposit, float owed) {
        this.roomNum = roomNum;
        this.idCard = idCard;
        this.name = name;
        this.address = address;
        this.phoneNum = phoneNum;
        this.rentalPeriod = rentalPeriod;
        this.deposit = deposit;
        this.owed = owed;
    }

    public Customer(int roomNum, String idCard, String name, String address, String phoneNum, String rentalPeriod, float deposit) {
        this.roomNum = roomNum;
        this.idCard = idCard;
        this.name = name;
        this.address = address;
        this.phoneNum = phoneNum;
        this.rentalPeriod = rentalPeriod;
        this.deposit = deposit;
        this.owed = 0;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public String getIdCard() {
        return idCard;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getRentalPeriod() {
        return rentalPeriod;
    }

    public float getDeposit() {
        return deposit;
    }

    public float getOwed() {
        return owed;
    }
}
