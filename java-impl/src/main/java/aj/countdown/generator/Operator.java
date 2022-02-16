package aj.countdown.generator;

public enum Operator {
    ADD("+") {
        @Override
        public int apply(int x, int y) {
            return x + y;
        }

        @Override
        public boolean isOrderSensitive() {
            return false;
        }
    },

    SUBTRACT("-") {
        @Override
        public int apply(int x, int y) {
            return x - y;
        }

        @Override
        public boolean isOrderSensitive() {
            return true;
        }
    },

    MULTIPLY("*") {
        @Override
        public int apply(int x, int y) {
            return x * y;
        }

        @Override
        public boolean isOrderSensitive() {
            return false;
        }
    },

    DIVIDE("/") {
        @Override
        public int apply(int x, int y) {
            return x / y;
        }

        @Override
        public boolean isOrderSensitive() {
            return true;
        }
    };

    private final String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }

    public abstract int apply(int x, int y);
    public abstract boolean isOrderSensitive();

    @Override
    public String toString() {
        return " " + symbol + " ";
    }
}
