package converter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int baseFrom = in.nextInt();
        String value = in.next();
        int baseTo = in.nextInt();
        if (baseFrom >= 1 && baseFrom <= 36 && baseTo >= 1 && baseTo <= 36 &&
                value.matches("^[0-9a-z]+(\\.[0-9a-z]+)?$")) {
            System.out.println(new FloatingConverter(value, baseFrom, baseTo));
        }
        else {
            System.out.println("error");
        }
    }
}

class IntegerConverter {
    private final int value;
    private final int base;
    public IntegerConverter(int value) {
        this.value = value;
        this.base = 10;
    }
    public IntegerConverter(int value, int base) {
        this.value = value;
        this.base = base;
    }
    public IntegerConverter(String value, int baseFrom, int baseTo) {
        if (baseFrom == 1) {
            this.value = value.length();
        }
        else {
            this.value = Integer.parseInt(value, baseFrom);
        }
        this.base = baseTo;
    }
    @Override
    public String toString() {
        String str;
        if (this.base == 1) {
            StringBuilder tmp = new StringBuilder();
            for (int i = 0; i < this.value; i++) {
                tmp.append("1");
            }
            str = new String(tmp);
        }
        else {
            str = Integer.toString(this.value, this.base);
        }
        return str;
    }
}

class FloatingConverter {
    private final IntegerConverter intPart;
    private final FractionalConverter fractionPart;
    private boolean onlyInteger;

    public FloatingConverter(String value, int baseFrom, int baseTo) {
        String[] parts = value.split("\\.");
        if (parts.length == 1) {
            intPart = new IntegerConverter(value, baseFrom, baseTo);
            fractionPart = new FractionalConverter(0f, baseTo);
            this.onlyInteger = true;
        }
        else {
            intPart = new IntegerConverter(parts[0], baseFrom, baseTo);
            fractionPart = new FractionalConverter(parts[1], baseFrom, baseTo);
            this.onlyInteger = false;
        }
    }

    @Override
    public String toString() {
        return intPart.toString() + (this.onlyInteger ? "" : fractionPart.toString());
    }

    private class FractionalConverter {
        private final float value;
        private final int base;

        public FractionalConverter(float value, int base) {
            this.value = value;
            this.base = base;
        }
        public FractionalConverter(String value, int baseFrom, int baseTo) {
            float tmp = 0f;
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                tmp += (('0' <= c && c <= '9') ? ((int)(c - '0') / Math.pow(baseFrom, i + 1)) : ((int)(c - 'a' + 10) / Math.pow(baseFrom, i + 1)));
            }
            this.value = tmp;
            this.base = baseTo;
        }

        @Override
        public String toString() {
            StringBuilder tmp = new StringBuilder(".");
            float num = this.value;
            int i = 5;
            while (i-- > 0) {
                num *= this.base;
                int a = (int)num;
                tmp.append(a < 10 ? (char)(a + '0') : (char)(a - 10 + 'a'));
                num -= a;
            }
            return new String(tmp);
        }
    }
}
