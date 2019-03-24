package academit.kashirin.hastable;

import java.util.*;

public class MyHashTable<T> implements Collection<T> {
    private ArrayList<T>[] hashItems;
    private int length;
    private int countList;
    private int modCount = 0;

    public MyHashTable() {
        //noinspection unchecked
        hashItems = new ArrayList[10];
    }

    private int getHasCode(T value) {
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
        int hash = getHasCode((T) o);
        return hashItems[hash].contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return new MyListIterator();
    }

    private class MyListIterator implements Iterator<T> {
        private int modCountSave = modCount;

        private int currentIndex = -1;
        private int countListItems = 0;
        private int countItems = -1;

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
            if (hashItems[countListItems] != null) {
                ++currentIndex;
                ++countItems;
                if (hashItems[countListItems].size() == countItems) {
                    ++countListItems;
                    countItems = 0;
                    if (hashItems[countListItems] == null) {
                        while (hashItems[countListItems] == null) {
                            ++countListItems;
                        }
                    }
                }
            }
            return hashItems[countListItems].get(countItems);
        }
    }

    @Override
    public Object[] toArray() {
        Object[] temp = new Object[countList];
        int j = 0;
        for (ArrayList<T> hashItem : hashItems) {
            if (hashItem != null) {
                temp[j] = hashItem;
                j++;
            }
        }
        return temp;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a.length >= length) {
            a = (T1[]) Arrays.copyOf(hashItems, hashItems.length + 1);
            a[a.length - 1] = null;
        } else {
            a = (T1[]) Arrays.copyOf(hashItems, hashItems.length, Object[].class);
        }
        return a;
    }

    @Override
    public boolean add(T t) {
        int hash = getHasCode(t);
        while (hashItems[hash] != null) {
            hashItems[hash].add(t);
            length++;
            return true;
        }
        hashItems[hash] = new ArrayList<>();
        hashItems[hash].add(t);
        countList++;
        length++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = getHasCode((T) o);
        for (T element : hashItems[hash]) {
            if (Objects.equals(element, o)) {
                hashItems[hash].remove(o);
                length--;
                return true;
            }
        }
        return false;
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
        for (Object element : c) {
            add((T) element);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isRetain = false;
        for (int i = 0; i < size(); i++) {
            if (hashItems[i] != null) {
                for (int j = 0; j < hashItems[i].size(); j++) {
                    if (c.contains(hashItems[i].get(j))) {
                        remove(hashItems[i].get(j));
                        j--;
                        isRetain = true;
                    }
                }
                if (hashItems[i].size() == 0) {
                    hashItems[i] = null;
                    countList--;
                }
            }
        }
        return isRetain;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean isRetain = false;
        for (int i = 0; i < hashItems.length; i++) {
            if (hashItems[i] != null) {
                for (int j = 0; j < hashItems[i].size(); j++) {
                    if (!c.contains(hashItems[i].get(j))) {
                        remove(hashItems[i].get(j));
                        j--;
                        isRetain = true;
                    }
                }
                if (hashItems[i].size() == 0) {
                    hashItems[i] = null;
                    countList--;
                }
            }
        }
        return isRetain;
    }

    @Override
    public void clear() {
        length = 0;
        Arrays.fill(hashItems, null);
        modCount++;
        countList = 0;
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
