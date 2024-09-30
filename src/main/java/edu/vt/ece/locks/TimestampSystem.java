package edu.vt.ece.locks;

import edu.vt.ece.data.DataTimestamp;

public interface TimestampSystem {
    public DataTimestamp[] scan();
    public void label(DataTimestamp timestamp, int i);
}
