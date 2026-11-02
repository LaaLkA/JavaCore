public class BlockingQueue {

    private final Object[] data;
    private int tail;
    private int head;
    private int size;

    private static final int DEFAULT_CAPACITY = 8;


    public BlockingQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("size must be > 0");
        data = new Object[capacity];
    }

    public BlockingQueue() {
        data = new Object[DEFAULT_CAPACITY];
    }

    // add
    public synchronized void enqueue(Object item) throws InterruptedException {
        if (item == null){
            throw new NullPointerException("item cant be NULL");
        }
        while (size == data.length) {
            wait();
        }
        data[tail] = item;
        tail = (tail + 1) % data.length;
        size++;
        notifyAll();
    }

    // get
    public synchronized Object dequeue() throws InterruptedException {
        while (size == 0) {
            wait();
        }
        Object res = data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        size--;
        notifyAll();
        return res;

    }

    public synchronized int size() {
        return size;
    }
}
