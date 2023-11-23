package com.example.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author lvmeijuan
 */
public class Main {
    public static void main(String[] args) {
        List<Integer> dynamicArray = new ArrayList<>();
        dynamicArray.add(1);
        dynamicArray.add(2);
        dynamicArray.remove(1);

        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("A");
        linkedList.add("B");
        linkedList.add("C");
        linkedList.addFirst("D");
        linkedList.removeLast();

        Set<Integer> uniqueNumbers = new HashSet<>();
        uniqueNumbers.add(1);
        uniqueNumbers.add(2);
        uniqueNumbers.add(1);



    }
}
