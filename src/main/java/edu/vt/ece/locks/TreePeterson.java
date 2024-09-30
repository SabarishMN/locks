package edu.vt.ece.locks;


public class TreePeterson implements Lock {

    public TreePeterson() {
        this(16);
    }

    private final Peterson[] locks;
    private final int numThreads;
    private final int depth;


    public TreePeterson(int n) {
        if (n < 2 || (n & (n - 1)) != 0) {
            throw new IllegalArgumentException("Number of threads must be a power of 2");
        }

        this.numThreads = n;
        this.depth = (int) (Math.log(n) / Math.log(2));
        this.locks = new Peterson[numThreads - 1];

        for (int i = 0; i < locks.length; i++) {
            locks[i] = new Peterson();
        }
    }

    @Override
    public void lock() {
        int threadId = getThreadId();
        int currentLock = threadId + numThreads - 1;
        while (currentLock > 0) {
            int parent = (currentLock - 1) / 2;
            locks[parent].lock();
            currentLock = parent;
        }
    }

    @Override
    public void unlock() {
        int threadId = getThreadId();
        int currentLock = threadId + numThreads - 1;

//        System.out.println("Thread " + threadId + " releasing lock at level " + currentLock);
        while (currentLock > 0) {
            int parent = (currentLock - 1) / 2;
            locks[parent].unlock();
            currentLock = parent;
        }
    }

    private int getThreadId() {
        return (int) (Thread.currentThread().getId() % numThreads);
    }

    public static void main(String[] args) {
        final int numThreads = 4;
        final TreePeterson treeLock = new TreePeterson(numThreads);

        Runnable criticalSection = () -> {
            int threadId = treeLock.getThreadId();
//            System.out.println("Thread " + threadId + " attempting to lock...");
            treeLock.lock();
//            System.out.println("Thread " + threadId + " in critical section.");
            try {
                Thread.sleep(100); // Simulate work in the critical section
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println("Thread " + threadId + " releasing lock...");
            treeLock.unlock();
        };


        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(criticalSection);
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
