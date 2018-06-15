package Server;

import java.security.Key;
import java.util.*;

/**
 * Special structure with some similar part to Map Structure
 * None same value ,None same key either
 * Provide API(getKeyByValue) to get key by specified value
 * Thread-safe structure
 * @author Bin Xiao
 * @since 2018/06/15
 * @param <K> Class of key
 * @param <V> Value of class
 */
public class InfoMap<K,V> {
    /**
     * Thread-Safety Map to save data includes username and it's PrintStream
     */
    private Map<K,V> map = Collections.synchronizedMap(new HashMap<K,V>());
    /**
     * Remove the specified value in Map
     * @param value
     * @return return the specified value if it succeed.return null if it fail.
     */
    public synchronized V removeByValue(V value){
        K k = null;
        for(K key:map.keySet()){
            if(map.get(key) == value){
                k = key;
                break;
            }
        }
        if(k!=null){
            return map.remove(k);
        }else return null;
    }
    /**
     * Put the Specified key and value into map
     * @param key
     * @param value
     * @return return the specified value if it succeed.return null if it fail
     */
    public synchronized V put(K key,V value){
        for(V val:valueSet()){
            if(val.equals(value)
                    && val.hashCode()==value.hashCode()){
                return null;
            }
        }
        return map.put(key,value);
    }
    /**
     * Get the set of value in map
     * @return return the set of value in map
     */
    public synchronized Set<V> valueSet(){
        Set<V> res =new HashSet<>();
        map.forEach((k,v)->res.add(v));
        return res;
    }
    /**
     * Get the key with specified value in Map
     * @param value
     * @return return the specified key if it succeed. return null if it fail.
     */
    public synchronized K getKeyByValue(V value){
        for(K key:map.keySet()){
            if(map.get(key) == value|| map.get(key).equals(value)){
                return key;
            }
        }
        return null;
    }
    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified
     * key.  More formally, returns <tt>true</tt> if and only if
     * this map contains a mapping for a key <tt>k</tt> such that
     * <tt>(key==null ? k==null : key.equals(k))</tt>.  (There can be
     * at most one such mapping.)
     * @param key
     * @return
     */
    public boolean containKey(K key){

        return map.containsKey(key);
    }
    /**
     * Returns the value to which the specified key is mapped
     * @param key
     * @return
     */
    public V get(Object key){
        return map.get(key);
    }
    /**
     * Returns the number of key-value mappings in this map
     * @return
     */
    public int size(){
        return map.size();
    }
    /**
     * Removes the mapping for a key from this map if it is present
     * (optional operation).
     * @param key
     * @return
     */
    public synchronized V remove(K key){
        return map.remove(key);
    }

}
