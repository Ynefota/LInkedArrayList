package com.github.ynefota;

import com.github.ynefota.LinkedArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class LinkedArrayListTest {

    private LinkedArrayList<String> list;


    @BeforeEach
    void initList() {
        list = new LinkedArrayList<>();
    }

    @Test
    void toArray() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 123; i++) {
            String rand = String.valueOf(Math.random());
            arrayList.add(rand);
            list.add(rand);
        }
        assertArrayEquals(arrayList.toArray(), list.toArray());
    }

    @Test
    void lastIndexOf() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 123; i++) {
            String rand = String.valueOf(Math.random());
            arrayList.add(rand);
            list.add(rand);
        }
        Object o = arrayList.get(23);
        assertEquals(arrayList.lastIndexOf(o), list.lastIndexOf(o));
    }

    @Test
    void lastIndexOf1() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = -20; i < 20; i++) {
            arrayList.add(String.valueOf(Math.abs(i)));
            list.add(String.valueOf(Math.abs(i)));
        }
        Object o = arrayList.get(3);
        assertEquals(arrayList.lastIndexOf(o), list.lastIndexOf(o));
    }

    @Test
    void indexOf() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 123; i++) {
            String rand = String.valueOf(Math.random());
            arrayList.add(rand);
            list.add(rand);
        }
        Object o = arrayList.get(23);
        assertEquals(arrayList.indexOf(o), list.indexOf(o));
    }

    @Test
    void contain() {
        ArrayList<String> arrayList;
        for (int i = 0; i < 129; i++) {
            arrayList = new ArrayList<>();
            for (int j = 0; j < 123 + i; j++) {
                String zahl = String.valueOf(j);
                arrayList.add(zahl);
                list.add(zahl);
            }
            String random = String.valueOf((int) (Math.random() * 3 * (i + 123) + 1));
            assertEquals(arrayList.contains(random), list.contains(random));
        }
    }

    @Test
    void set_test(){
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            arrayList.add(String.valueOf(i));
            list.add(String.valueOf(i));
        }
        arrayList.set(20, "Hallo");
        list.set(20, "Hallo");

        assertEquals("Hallo", list.get(20));
        assertEquals(arrayList.size(), list.size());

    }

    @Test
    void iterator() {
        int index = 0;
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 123; i++) {
            String rand = String.valueOf(Math.random());
            arrayList.add(rand);
            list.add(rand);
        }
        for (String str : list) {
            assertEquals(arrayList.get(index), str);
            index++;
        }
    }


    @Test
    void testClone_Equals() {
        for (int i = 0; i < 49; i++) {
            list.add(0, String.valueOf(i));
        }

        try {
            LinkedArrayList<String> cloned = (LinkedArrayList<String>) list.clone();
            assertEquals(list.size(), cloned.size());
            assertArrayEquals(list.toArray(), cloned.toArray());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            fail();
        }


    }

    @Test
    void testClone_NotEquals() {
        for (int i = 0; i < 49; i++) {
            list.add(0, String.valueOf(i));
        }

        try {
            LinkedArrayList<String> cloned = (LinkedArrayList<String>) list.clone();
            for (int i = 0; i < 20; i++) {
                cloned.remove(0);
            }
            assertNotEquals(list.size(), cloned.size());
            assertNotEquals(Arrays.toString(list.toArray()), Arrays.toString(cloned.toArray()));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testAddElements() {
        list.add(0, "Tom");
        list.add(1, "Tim");
        list.add(2, "Tam");
        list.add(3, "Kim");

        assertEquals("Tom", list.get(0));
        assertEquals("Tim", list.get(1));
        assertEquals("Tam", list.get(2));
        assertEquals("Kim", list.get(3));

        list.add(1, "Mario");

        assertEquals("Tom", list.get(0));
        assertEquals("Mario", list.get(1));
        assertEquals("Tim", list.get(2));
        assertEquals("Tam", list.get(3));
        assertEquals("Kim", list.get(4));

        assertTrue(list.size() == 5);
    }


    @Test
    public void testRemoveElement() {
        list.add(0, "Tom");
        list.add(1, "Tim");
        list.add(2, "Tam");
        list.add(3, "Kim");

        assertEquals("Tim", list.remove(1));
        assertTrue(list.size() == 3);
    }
}