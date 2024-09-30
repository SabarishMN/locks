package edu.vt.ece.locks;

import edu.vt.ece.bench.ThreadId;
import edu.vt.ece.data.DataTimestamp;
import edu.vt.ece.locks.Lock;

import java.util.Arrays;

public class Bakery implements Lock, Timestamp, TimestampSystem {
    private final boolean[] flag;
    private final DataTimestamp[] label;
    private final long n;

    public Bakery() {
        this(2);
    }

//    public Bakery(int n) {
//        this(n, new DataTimestamp(n));
//    }

    public Bakery(int n) {
        this.n = n;
        this.flag = new boolean[n];
        this.label = new DataTimestamp[n];
        Arrays.fill(flag, false);
        for (int i = 0; i < n; i++) {
            label[i] = new DataTimestamp(0);
        }
    }

    @Override
    public void lock() {
        long i = Thread.currentThread().getId()%n;
        flag[(int)i] = true;

        DataTimestamp[] currentLabels = scan();
        DataTimestamp maxTimestamp = getMaxTimestamp(currentLabels);
        DataTimestamp newLabel = new DataTimestamp(maxTimestamp.value + 1);
        label(newLabel, (int)i);


        while (existsOtherThreadInCriticalSection((int)i, newLabel)) {
            // busy wait
//            int j = 0;
//            j++;
//            System.out.println(j);
        }
    }

    @Override
    public void unlock() {
        long i = Thread.currentThread().getId()%n;
        flag[(int)i] = false;
    }

    private DataTimestamp getMaxTimestamp(DataTimestamp[] timestamps) {
        DataTimestamp maxTimestamp = timestamps[0];
        for (DataTimestamp timestamp : timestamps) {
            if (timestamp.value > maxTimestamp.value) {
                maxTimestamp = timestamp;
            }
        }
        return maxTimestamp;
    }

    private boolean existsOtherThreadInCriticalSection(int i, DataTimestamp myTimestamp) {
        DataTimestamp[] currentLabels = scan();
        for (int k = 0; k < n; k++) {
            if (k != i && flag[k]) {
                if (compare(currentLabels[k], myTimestamp)) {
                    return true;
                } else if ((currentLabels[k].value - myTimestamp.value) == 0 && k < i) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean compare(DataTimestamp t1, DataTimestamp t2) {
        return t1.value < t2.value;
    }

    @Override
    public DataTimestamp[] scan() {
        return label;
    }

    @Override
    public void label(DataTimestamp timestamp, int i) {
        label[i] = timestamp;
    }

}
