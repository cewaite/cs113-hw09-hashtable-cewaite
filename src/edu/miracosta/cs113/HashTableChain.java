package edu.miracosta.cs113;

import java.util.*;

public class HashTableChain<T, T1> implements Map<T, T1>
{
    public static void main(String[] args)
    {
        Map<String, Integer> hashTable = new HashTableChain<>();

//        /** Helper method using Map's put() to place a given number of unique key-value pairs into this Map. */
//        for (int i = 0; i < 10; i++) {
//            hashTable.put(Integer.toString(i), i);
//        }
//
//        System.out.println(hashTable.toString());
//
//        hashTable.clear();
//
//        System.out.println(hashTable.toString());
//        System.out.println(hashTable.size());
//        System.out.println(hashTable.isEmpty());

        hashTable.put("one", 1);
        hashTable.put("two", 2);
        hashTable.put("three", 3);

        // Because order is arbitrary, expect one of the following outputs from this Set's toString
        String[] expectedSets = { "[one=1, two=2, three=3]", "[one=1, three=3, two=2]", "[two=2, one=1, three=3]",
                "[two=2, three=3, one=1]", "[three=3, one=1, two=2]", "[three=3, two=2, one=1]" };

        String setString = hashTable.entrySet().toString();

        boolean validSet = setString.equals(expectedSets[0]) || setString.equals(expectedSets[1]) ||
                setString.equals(expectedSets[2]) || setString.equals(expectedSets[3]) ||
                setString.equals(expectedSets[4]) || setString.equals(expectedSets[5]);

        System.out.println("Test entrySet failed - invalid value returned by Set's toString: " + setString + " " + validSet);
    }

    private LinkedList<Entry<T, T1>>[] table;
    private int numKeys;
    private static final int CAPACITY = 101;
    private static final int LOAD_THRESHOLD = 3;

    static class Entry<T, T1>
    {
        private T key;
        private T1 value;

        public Entry(T key, T1 value)
        {
            this.key = key;
            this.value = value;
        }

        public Entry(String key, Integer value)
        {
            this.key = (T) key;
            this.value = (T1) value;
        }

        public T getKey() {
            return key;
        }

        public T1 getValue() {
            return value;
        }

        public void setValue(T1 value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this)
            {
                return true;
            }

            if (!(o instanceof Entry))
            {
                return false;
            }

            Entry c = (Entry) o;

            return this.getValue() == c.getValue() && this.getKey() == c.getKey();
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }
    }

    public HashTableChain()
    {
        table = new LinkedList[CAPACITY];
    }

//    public String toString()
//    {
//        String string = "";
//        for (int i = 0; i < CAPACITY; i++)
//        {
//            if (table[i] != null)
//            {
//                string += table[i].element().getKey();
//            }
//        }
//        return string;
//    }

    @Override
    public int size() {
        return numKeys;
    }

    @Override
    public boolean isEmpty() {
        return numKeys <= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
        {
            return true;
        }
        if (!(o instanceof Hashtable))
        {
            return false;
        }

        Hashtable c = (Hashtable) o;

        if (c.size() != this.size())
        {
            return false;
        }

        for (int i = 0; i < CAPACITY; i++)
        {
            if (table[i] != null)
            {
                for (Entry<T, T1> nextItem : table[i])
                {
                    if (!(c.containsKey(nextItem.getKey())))
                    {
                        return false;
                    }
                }
            }
        }

//        return this.size() == c.size() && this.keySet() == c.keySet();

        return true;
    }

    @Override
    public boolean containsKey(Object key) {
        for (int i = 0; i < CAPACITY; i++)
        {
            if (table[i] != null)
            {
                for (Entry<T, T1> nextItem : table[i])
                {
                    if (nextItem.getKey().equals(key))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (int i = 0; i < CAPACITY; i++)
        {
            if (table[i] != null)
            {
                for (Entry<T, T1> nextItem : table[i])
                {
                    if (nextItem.getValue().equals(value))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public T1 get(Object key) {
        int index = key.hashCode() % table.length;
        if (index < 0)
        {
            index += table.length;
        }
        if (table[index] == null)
        {
            return null;
        }

        for (Entry<T, T1> nextItem : table[index])
        {
            if (nextItem.getKey().equals(key))
            {
                return (T1) nextItem.getValue();
            }
        }
        return null;
    }

    @Override
    public T1 put(T key, T1 value) {
        int index = key.hashCode() % table.length;
        if (index < 0)
        {
            index += table.length;
        }
        if (table[index] == null)
        {
            table[index] = new LinkedList<Entry<T, T1>>();
        }

        for (Entry<T, T1> nextItem : table[index])
        {
            if (nextItem.getKey().equals(key))
            {
                T1 oldVal = (T1) nextItem.getValue();
                nextItem.setValue((T1) value);
                return oldVal;
            }
        }

        table[index].addFirst(new Entry<T, T1>(key, value));
        numKeys++;
        return null;
    }

    @Override
    public T1 remove(Object key) {
        int index = key.hashCode() % table.length;
        if (index < 0)
        {
            index += table.length;
        }
        if (table[index] == null)
        {
            return null;
        }

        for (Entry<T, T1> nextItem : table[index])
        {
            if (nextItem.getKey().equals(key))
            {
                T1 oldVal = (T1) nextItem.getValue();
                numKeys--;
                table[index] = null;
                return oldVal;
            }
        }
        return null;
    }

    // Ignore, do not implement
    @Override
    public void putAll(Map<? extends T, ? extends T1> m) {

    }

    @Override
    public void clear() {
        numKeys = 0;
        table = new LinkedList[CAPACITY];
    }

    public int hashCode()
    {
        int hashCode = 0;

        for (int i = 0; i < CAPACITY; i++)
        {
            if (table[i] != null)
            {
                for (Entry<T, T1> nextItem : table[i])
                {
                    hashCode += nextItem.getKey().hashCode();
                }
            }
        }

        return hashCode + 1;
    }

    @Override
    public Set<T> keySet() {
        Set<T> keySet = new HashSet<>();
        for (int i = 0; i < CAPACITY; i++)
        {
            if (table[i] != null)
            {
                for (Entry<T, T1> nextItem : table[i])
                {
                    keySet.add((T) nextItem.getKey());
                }
            }
        }

        return keySet;
    }

    // Ignore, do not implement
    @Override
    public Collection<T1> values() {
        return null;
    }

    @Override
    public Set<Map.Entry<T, T1>> entrySet() {
        return new EntrySet();
    }

    private class EntrySet extends AbstractSet<Map.Entry<T, T1>>
    {
        public EntrySet() {}

        public int size()
        {
            return numKeys;
        }

        public Iterator<Map.Entry<T, T1>> iterator()
        {
            return new SetIterator();
        }

        public String toString()
        {
            String string = "[";
            Iterator<Map.Entry<T, T1>> it = iterator();

            while (it.hasNext())
            {
                Entry nextItem = (Entry) it.next();
                if (nextItem != null)
                {
                    string += ", " + nextItem.getKey() + "=" + nextItem.getValue();
                    it.remove();
                }
            }

            string = string.replaceFirst(", ", "");
            string += "]";
            return string;
        }
    }

    private class SetIterator implements Iterator
    {
        private int index = 0;
        private Entry lastItemReturned = null;

        public SetIterator() {}

        @Override
        public boolean hasNext() {
            return index < CAPACITY;
        }

        @Override
        public Object next() {
            do {
                if (table[index] != null)
                {
                    lastItemReturned = table[index].element();
                    index++;
                    return lastItemReturned;
                }
                else
                {
                    index++;
                }
            } while (lastItemReturned == null && hasNext());

            return null;
        }

        public void remove()
        {
            if (lastItemReturned != null)
            {
                table[index] = null;
                lastItemReturned = null;
            }
        }
    }
}
