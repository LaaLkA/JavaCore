package task_1_StringBuilder;

import java.util.ArrayDeque;
import java.util.Deque;

public class MyStringBuilder implements CharSequence {
    private final StringBuilder delegate = new StringBuilder();
    private final Deque<Snapshot> history = new ArrayDeque<>();

    @Override
    public int length() {
        return delegate.length();
    }

    @Override
    public char charAt(int index) {
        return delegate.charAt(index);
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public CharSequence subSequence(int startPosition, int endPosition) {
        return delegate.subSequence(startPosition, endPosition);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    private record Snapshot(String state) {}

    public MyStringBuilder append(String str) {
        return change( () -> delegate.append(str));
    }

    public MyStringBuilder delete(int startPosition, int endPosition) {
        return change( () -> delegate.delete(startPosition, endPosition));
    }

    public MyStringBuilder insert(int offset, String str) {
        return change( () -> delegate.insert(offset, str));
    }

    public MyStringBuilder reverse() {
        return change(delegate:: reverse);
    }

    public MyStringBuilder undo() {
        if (!history.isEmpty()) {
            delegate.setLength(0);
            delegate.append(history.pop().state());
        }
        return this;
    }


    private void save() {
        history.push(new Snapshot(delegate.toString()));
    }


    private MyStringBuilder change(Runnable action) {
        save();
        try {
            action.run();
        } catch (RuntimeException e) {
            history.pop();
            throw e;
        }
        return this;
    }
}
