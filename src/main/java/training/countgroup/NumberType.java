package training.countgroup;

public enum NumberType {

    POSITIVE, ZERO, NEGATIVE;

    public static NumberType typeOf(int number) {
        if (number > 0) {
            return POSITIVE;
        }
        else if (number == 0) {
            return ZERO;
        }
        else {
            return NEGATIVE;
        }
    }
}
