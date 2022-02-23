package aj.countdown.generator;

public enum Operator {
    ADD("+") {
        @Override
        public int apply(int x, int y) {
            return x + y;
        }

        @Override
        public boolean isCommutative() {
            return true;
        }
    },

    SUBTRACT("-") {
        @Override
        public int apply(int x, int y) {
            return x - y;
        }

        @Override
        public boolean isCommutative() {
            return false;
        }
    },

    MULTIPLY("*") {
        @Override
        public int apply(int x, int y) {
            return x * y;
        }

        @Override
        public boolean isCommutative() {
            return true;
        }
    },

    DIVIDE("/") {
        @Override
        public int apply(int x, int y) {
            return x / y;
        }

        @Override
        public boolean isCommutative() {
            return false;
        }
    };

    private final String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }

    public abstract int apply(int x, int y);
    public abstract boolean isCommutative();

    @Override
    public String toString() {
        return " " + symbol + " ";
    }
}
