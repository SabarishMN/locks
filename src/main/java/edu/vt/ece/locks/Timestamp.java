package edu.vt.ece.locks;

import edu.vt.ece.data.DataTimestamp;

public interface Timestamp {
    boolean compare(DataTimestamp t1, DataTimestamp t2);
}
