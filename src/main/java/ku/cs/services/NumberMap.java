package ku.cs.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberMap {

    private static final String[] SCALE_TH = { "ล้าน", "สิบ", "ร้อย", "พัน", "หมื่น", "แสน", "" };
    private static final String[] DIGIT_TH = { "ศูนย์", "หนึ่ง", "สอง", "สาม", "สี่", "ห้า", "หก", "เจ็ด", "แปด", "เก้า" };
    private static final String[] SYMBOLS_TH = { "ลบ", "บาท", "ถ้วน", "สตางค์" ,"ยี่", "เอ็ด", ",", " ", "฿"};
    private String valueText;

    public String getText(double amount) {
        BigDecimal value = new BigDecimal(amount);
        this.valueText = getThaiBaht(value);
        return this.valueText;
    }

    public String getText(float amount) {
        BigDecimal value = new BigDecimal(amount);
        this.valueText = getThaiBaht(value);
        return this.valueText;
    }

    public String getText(int amount) {
        BigDecimal value = new BigDecimal(amount);
        this.valueText = getThaiBaht(value);
        return this.valueText;
    }

    public String getText(long amount) {
        BigDecimal value = new BigDecimal(amount);
        this.valueText = getThaiBaht(value);
        return this.valueText;
    }

    public String getText(String amount) {
        //ไม่ต้องการเครื่องหมายคอมมาร์, ไม่ต้องการช่องว่าง, ไม่ต้องการตัวหนังสือ บาท, ไม่ต้องการสัญลักษณ์สกุลเงินบาท
        for (String element : SYMBOLS_TH) {
            amount = amount.replace (element, "");
        }

        BigDecimal value = new BigDecimal(amount.trim());
        this.valueText = getThaiBaht(value);
        return this.valueText;
    }

    public String getText(Number amount) {
        BigDecimal value = new BigDecimal(String.valueOf(amount));
        this.valueText = getThaiBaht(value);
        return this.valueText;
    }

    private static String getThaiBaht(BigDecimal amount) {
        StringBuilder builder = new StringBuilder();
        BigDecimal absolute = amount.abs();
        int precision = absolute.precision();
        int scale = absolute.scale();
        int rounded_precision = ((precision - scale) + 2);
        MathContext mc = new MathContext(rounded_precision, RoundingMode.HALF_UP);
        BigDecimal rounded = absolute.round(mc);
        BigDecimal[] compound = rounded.divideAndRemainder(BigDecimal.ONE);
        boolean negative_amount = (-1 == amount.compareTo(BigDecimal.ZERO));

        compound[0] = compound[0].setScale(0);
        compound[1] = compound[1].movePointRight(2);

        if (negative_amount) {
            builder.append(SYMBOLS_TH[0].toString());
        }

        builder.append(getNumberText(compound[0].toBigIntegerExact()));
        builder.append(SYMBOLS_TH[1].toString());

        if (0 == compound[1].compareTo(BigDecimal.ZERO)) {
            builder.append(SYMBOLS_TH[2].toString());
        } else {
            builder.append(getNumberText(compound[1].toBigIntegerExact()));
            builder.append(SYMBOLS_TH[3].toString());
        }
        return builder.toString();
    }

    private static String getNumberText(BigInteger number) {
        StringBuffer buffer = new StringBuffer();
        char[] digits = number.toString().toCharArray();

        for (int index = digits.length; index > 0; --index) {
            int digit = Integer.parseInt(String.valueOf(digits[digits.length
                    - index]));
            String digit_text = DIGIT_TH[digit];
            int scale_idx = ((1 < index) ? ((index - 1) % 6) : 6);

            if ((1 == scale_idx) && (2 == digit)) {
                digit_text = SYMBOLS_TH[4].toString();
            }

            if (1 == digit) {
                switch (scale_idx) {
                    case 0:
                    case 6:
                        buffer.append((index < digits.length) ? SYMBOLS_TH[5].toString() : digit_text);
                        break;
                    case 1:
                        break;
                    default:
                        buffer.append(digit_text);
                        break;
                }
            } else if (0 == digit) {
                if (0 == scale_idx) {
                    buffer.append(SCALE_TH[scale_idx]);
                }
                continue;
            } else {
                buffer.append(digit_text);
            }
            buffer.append(SCALE_TH[scale_idx]);
        }
        return buffer.toString();
    }
}
