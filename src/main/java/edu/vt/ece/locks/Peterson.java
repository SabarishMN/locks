package edu.vt.ece.locks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import edu.vt.ece.bench.ThreadId;

public class Peterson implements Lock{

    private AtomicBoolean flag[] = new AtomicBoolean[2];
    private AtomicInteger victim;

    public Peterson() {
        flag[0] = new AtomicBoolean();
        flag[1] = new AtomicBoolean();
        victim = new AtomicInteger();
    }

    @Override
    public void lock() {
        int i = ((ThreadId)Thread.currentThread()).getThreadId()%2;
        int j = 1-i;
        flag[i].set(true);
        victim.set(i);
        while(flag[j].get() && victim.get() == i){
//            System.out.println("Thread " + Thread.currentThread().threadId()%16 + " attempting to acquire lock at level " + i);
        };
    }

    @Override
    public void unlock() {
        int i = ((ThreadId)Thread.currentThread()).getThreadId()%2;
        flag[i].set(false);
    }
}
