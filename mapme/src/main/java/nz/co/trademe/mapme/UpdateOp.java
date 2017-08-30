package nz.co.trademe.mapme;

public class UpdateOp {

    static final int ADD = 1;

    static final int REMOVE = 1 << 1;

    static final int UPDATE = 1 << 2;

    static final int MOVE = 1 << 3;

    static final int POOL_SIZE = 30;

    int cmd;

    int positionStart;

    Object payload;

    // holds the target positionStart if this is a MOVE
    int itemCount;

    UpdateOp(int cmd, int positionStart, int itemCount, Object payload) {
        this.cmd = cmd;
        this.positionStart = positionStart;
        this.itemCount = itemCount;
        this.payload = payload;
    }

    String cmdToString() {
        switch (cmd) {
            case ADD:
                return "add";
            case REMOVE:
                return "remove";
            case UPDATE:
                return "update";
            case MOVE:
                return "move";
        }
        return "??";
    }

    @Override
    public String toString() {
        return cmdToString() + "(" + positionStart + ", " + itemCount + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UpdateOp op = (UpdateOp) o;

        if (cmd != op.cmd) {
            return false;
        }
        if (cmd == MOVE && Math.abs(itemCount - positionStart) == 1) {
            // reverse of this is also true
            if (itemCount == op.positionStart && positionStart == op.itemCount) {
                return true;
            }
        }
        if (itemCount != op.itemCount) {
            return false;
        }
        if (positionStart != op.positionStart) {
            return false;
        }
        if (payload != null) {
            if (!payload.equals(op.payload)) {
                return false;
            }
        } else if (op.payload != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = cmd;
        result = 31 * result + positionStart;
        result = 31 * result + itemCount;
        return result;
    }
}
