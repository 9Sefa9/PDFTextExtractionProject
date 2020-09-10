package utilities;

import extractor.DocumentParser;

/**
 * Datenstruktur zum abspeichern von zwei Paaren, Key und Value.
 */
public class KeyValueObject<K,V> {
    K key;
    V value;

    public KeyValueObject() {

    }

    public KeyValueObject(K key, V  value) {
        this.key = key;
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "KeyValueObject{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}