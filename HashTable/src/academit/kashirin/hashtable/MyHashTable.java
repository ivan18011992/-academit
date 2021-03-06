package academit.kashirin.hashtable;

import java.util.*;

public class MyHashTable<T> implements Collection<T> {
    private ArrayList<T>[] hashItems;
    private int length;
    private int modCount = 0;

    public MyHashTable() {
        //noinspection unchecked
        hashItems = new ArrayList[10];
    }

    private int getHashCode(Object value) {
        if (value == null) {
            return 0;
        }
        return Math.abs(value.hashCode() % hashItems.length);
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public boolean contains(Object o) {
        int hash = getHashCode(o);
        if (hashItems[hash] == null) {
            return false;
        }
        return hashItems[hash].contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return new MyListIterator();
    }

    private class MyListIterator implements Iterator<T> {
        private int modCountSave = modCount;

        private int currentIndex = -1;
        private int itemsListIndex = 0;
        private int itemsIndex = 0;

        public boolean hasNext() {
            return currentIndex + 1 < length;
        }

        public T next() {
            if (modCountSave != modCount) {
                throw new ConcurrentModificationException("В коллекции добавились/удалились элементы за время обхода");
            }
            if (!hasNext()) {
                throw new NoSuchElementException("Коллекция закончилась");
            }
            while (hashItems[itemsListIndex] == null || hashItems[itemsListIndex].size() == itemsIndex) {
                itemsListIndex++;
                itemsIndex = 0;
            }
            itemsIndex++;
            ++currentIndex;
            return hashItems[itemsListIndex].get(itemsIndex - 1);
        }
    }

    @Override
    public Object[] toArray() {
        Object[] temp = new Object[length];
        int i = 0;
        for (T hashItem : this) {
            temp[i] = hashItem;
            i++;
        }
        return temp;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        //noinspection unchecked
        if (a.length >= length) {
            int i = 0;
            for (T hashItem : this) {
                //noinspection unchecked
                a[i] = (T1) hashItem;
                i++;
            }
            if (a.length > length) {
                a[length] = null;
            }
        } else {
            //noinspection SingleStatementInBlock,unchecked
            a = (T1[]) Arrays.copyOf(this.toArray(), length, a.getClass());
        }
        return a;
    }

    @Override
    public boolean add(T t) {
        int hash = getHashCode(t);
        if (hashItems[hash] == null) {
            hashItems[hash] = new ArrayList<>();
        }
        hashItems[hash].add(t);
        length++;
        modCount++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = getHashCode(o);
        if (hashItems[hash] == null) {
            return false;
        }
        boolean isRemove = hashItems[hash].remove(o);
        if (isRemove) {
            length--;
            modCount++;
        }
        return isRemove;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c.isEmpty()) {
            return false;
        }
        for (T element : c) {
            add(element);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isRemove = false;
        for (ArrayList<T> hashItem : hashItems) {
            if (hashItem != null) {
                int tempSize = hashItem.size();
                if (hashItem.removeAll(c)) {
                    length -= tempSize - hashItem.size();
                    isRemove = true;
                }
            }
        }
        if (isRemove) {
            modCount++;
        }
        return isRemove;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean isRetain = false;
        for (ArrayList<T> hashItem : hashItems) {
            if (hashItem != null) {
                int tempSize = hashItem.size();
                if (hashItem.retainAll(c)) {
                    length -= tempSize - hashItem.size();
                    isRetain = true;
                }
            }
        }
        if (isRetain) {
            modCount++;
        }
        return isRetain;
    }

    @Override
    public void clear() {
        length = 0;
        Arrays.fill(hashItems, null);
        modCount++;
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (ArrayList<T> element : hashItems) {
            if (element != null) {
                joiner.add(element.toString());
            }
        }
        return joiner.toString();
    }
}
