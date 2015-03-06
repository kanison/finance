package com.tenpay.sm.common.lang.internal;

import com.tenpay.sm.common.lang.ArrayUtil;

/**
 * ʹ��������Ϊkey��hash��
 *
 * @author Michael Zhou
 * @version $Id: IntHashMap.java 509 2004-02-16 05:42:07Z baobao $
 */
public class IntHashMap {
    /** Ĭ�ϵĳ�ʼ���� - <code>2����������</code>. */
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    /** ������� - <code>2����������</code>. */
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /** Ĭ�ϵĸ���ϵ�� */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    // ˽�б���
    private Entry[] table;
    private int     count;
    private int     threshold;
    private float   loadFactor;

    /**
     * ����һ��hash��ʹ��Ĭ�ϵĳ�ʼ����<code>16</code>��Ĭ�ϵĸ���ϵ��<code>0.75</code>��
     */
    public IntHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * ����һ��hash��ʹ��ָ���ĳ�ʼ������Ĭ�ϵĸ���ϵ��<code>0.75</code>��
     *
     * @param initialCapacity hash��ĳ�ʼ����
     *
     * @throws IllegalArgumentException �����ʼ����С�ڻ����<code>0</code>
     */
    public IntHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * ����һ��hash��ʹ��Ĭ�ϵ�ָ���ĳ�ʼ������ָ���ĸ���ϵ����
     *
     * @param initialCapacity hash��ĳ�ʼ����
     * @param loadFactor ����ϵ��
     *
     * @throws IllegalArgumentException �����ʼ����С�ڻ����<code>0</code>������ϵ����������
     */
    public IntHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }

        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }

        if (loadFactor <= 0) {
            throw new IllegalArgumentException("Illegal Load: " + loadFactor);
        }

        // ȷ����ʼ����Ϊ2����������.
        int capacity = 1;

        while (capacity < initialCapacity) {
            capacity <<= 1;
        }

        this.loadFactor     = loadFactor;
        table               = new Entry[capacity];
        threshold           = (int) (capacity * loadFactor);
    }

    /**
     * ȡ�õ�ǰhash����Ԫ�صĸ�����
     *
     * @return Ԫ�ظ���
     */
    public int size() {
        return count;
    }

    /**
     * ����hash���Ƿ�Ϊ�ա�
     *
     * @return ���Ϊ�գ��򷵻�<code>true</code>
     */
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * �鿴hash�����Ƿ����ָ����key��
     *
     * @param key Ҫ������key
     *
     * @return ����ҵ����򷵻�<code>true</code>
     */
    public boolean containsKey(int key) {
        Entry[] tab   = table;
        int     hash  = key;
        int     index = (hash & 0x7FFFFFFF) % tab.length;

        for (Entry e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash) {
                return true;
            }
        }

        return false;
    }

    /**
     * �鿴hash�����Ƿ����ָ����ֵ��
     *
     * @param value Ҫ������ֵ
     *
     * @return ����ҵ����򷵻�<code>true</code>
     */
    public boolean containsValue(Object value) {
        Entry[] tab = table;

        boolean valueIsNull = value == null;

        for (int i = tab.length; i-- > 0;) {
            for (Entry e = tab[i]; e != null; e = e.next) {
                if (valueIsNull ? (e.value == null)
                                        : value.equals(e.value)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * ��hash����ȡ�ú�ָ��key��Ӧ��ֵ��
     *
     * @param key Ҫ���ҵ�key
     *
     * @return key����Ӧ��ֵ�����û�ҵ����򷵻�<code>null</code>
     */
    public Object get(int key) {
        Entry[] tab   = table;
        int     hash  = key;
        int     index = (hash & 0x7FFFFFFF) % tab.length;

        for (Entry e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash) {
                return e.value;
            }
        }

        return null;
    }

    /**
     * ��key��ָ���������������������hash���С�
     *
     * @param key �����key
     * @param value ����ֵ��
     *
     * @return ���ָ��key�Ѿ����ڣ��򷵻�key����Ӧ��ԭ�ȵ�ֵ
     */
    public Object put(int key, Object value) {
        // Makes sure the key is not already in the hashtable.
        Entry[] tab   = table;
        int     hash  = key;
        int     index = (hash & 0x7FFFFFFF) % tab.length;

        for (Entry e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash) {
                Object old = e.value;

                e.value = value;
                return old;
            }
        }

        if (count >= threshold) {
            rehash();

            tab       = table;
            index     = (hash & 0x7FFFFFFF) % tab.length;
        }

        // Creates the new entry.
        Entry e = new Entry(hash, key, value, tab[index]);

        tab[index] = e;
        count++;
        return null;
    }

    /**
     * ��hash����ɾ��һ��ֵ��
     *
     * @param key Ҫɾ����ֵ����Ӧ��key
     *
     * @return ���ָ��key�Ѿ����ڣ��򷵻�key����Ӧ��ԭ�ȵ�ֵ
     */
    public Object remove(int key) {
        Entry[] tab   = table;
        int     hash  = key;
        int     index = (hash & 0x7FFFFFFF) % tab.length;

        for (Entry e = tab[index], prev = null; e != null; prev = e, e = e.next) {
            if (e.hash == hash) {
                if (prev != null) {
                    prev.next = e.next;
                } else {
                    tab[index] = e.next;
                }

                count--;

                Object oldValue = e.value;

                e.value = null;
                return oldValue;
            }
        }

        return null;
    }

    /**
     * ���hash��
     */
    public void clear() {
        Entry[] tab = table;

        for (int index = tab.length; --index >= 0;) {
            tab[index] = null;
        }

        count = 0;
    }

    public int[] keys() {
        if (count == 0) {
            return ArrayUtil.EMPTY_INT_ARRAY;
        }

        int[] keys  = new int[count];
        int   index = 0;

        for (int i = 0; i < table.length; i++) {
            Entry entry = table[i];

            while (entry != null) {
                keys[index++]     = entry.key;
                entry             = entry.next;
            }
        }

        return keys;
    }

    /**
     * ȡ���ַ�����ʾ��
     *
     * @return �ַ�����ʾ
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append('{');

        int[] keys = keys();

        for (int i = 0; i < keys.length; i++) {
            int    key   = keys[i];
            Object value = get(key);

            if (i > 0) {
                buffer.append(", ");
            }

            buffer.append(key).append('=').append((value == this) ? "(this Map)"
                                                                  : value);
        }

        buffer.append('}');

        return buffer.toString();
    }

    /**
     * �ع�hash��������������
     */
    protected void rehash() {
        int     oldCapacity = table.length;
        Entry[] oldMap = table;

        int     newCapacity = oldCapacity * 2;
        Entry[] newMap      = new Entry[newCapacity];

        threshold     = (int) (newCapacity * loadFactor);
        table         = newMap;

        for (int i = oldCapacity; i-- > 0;) {
            for (Entry old = oldMap[i]; old != null;) {
                Entry e = old;

                old = old.next;

                int index = (e.hash & 0x7FFFFFFF) % newCapacity;

                e.next            = newMap[index];
                newMap[index]     = e;
            }
        }
    }

    /**
     * ȡ��hash���������
     *
     * @return hash�������
     */
    protected int getCapacity() {
        return table.length;
    }

    /**
     * ȡ��hash�����ֵ��
     *
     * @return hash�����ֵ
     */
    protected int getThreshold() {
        return threshold;
    }

    /**
     * ����hash���е�һ��Ԫ�ص��ࡣ
     */
    protected static class Entry {
        protected int    hash;
        protected int    key;
        protected Object value;
        protected Entry  next;

        protected Entry(int hash, int key, Object value, Entry next) {
            this.hash      = hash;
            this.key       = key;
            this.value     = value;
            this.next      = next;
        }
    }
}
