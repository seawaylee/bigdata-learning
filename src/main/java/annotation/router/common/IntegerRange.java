package annotation.router.common;

import java.util.Objects;

/**
 * @author NikoBelic
 * @create 2018/2/6 15:34
 */
public class IntegerRange {

    private final int start;
    private final int end;

    public IntegerRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public boolean between(int i) {
        return i >= start && i <= end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        IntegerRange that = (IntegerRange) o;
        return start == that.start &&
                end == that.end;
    }

    @Override
    public int hashCode() {

        return Objects.hash(start, end);
    }
}
