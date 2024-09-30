package edu.vt.ece.data;

public class DataTimestamp {
    public long value;
    public DataTimestamp() { this.value = System.currentTimeMillis();}
    public DataTimestamp(long value) { this.value = value; }
}
