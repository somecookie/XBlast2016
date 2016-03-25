package ch.epfl.xblast;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ListsTest {

    @Test
    public void testOnMirrored() {
        ArrayList<Character> a = new ArrayList<>();
        a.add('k');
        a.add('a');
        a.add('y');
        ArrayList<Character> b = new ArrayList<>(Lists.mirrored(a));
        assertEquals(5.0, b.size(), 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void emptyTest(){
        ArrayList<Character> a = new ArrayList<>();
        Lists.mirrored(a);
    }
    
    @Test
    public void onElement(){
        ArrayList<String> a = new ArrayList<>();
        a.add("k");
        ArrayList<String> b = new ArrayList<>(Lists.mirrored(a));
        assertEquals("k", b.get(0));
    }
    
    @Test
    public void normalPermutation(){
        List<Integer> a = Arrays.asList(1,2,3, 4);
        List<List<Integer>> b = Lists.permutations(a);
        assertEquals(24, b.size());
    }
    
    @Test
    public void nullPermutation(){
        List<Integer> a = new ArrayList<>();
        List<List<Integer>> b = Lists.permutations(a);
        assertTrue(b.get(0).isEmpty());
        assertEquals(1, b.size());
    }
    @Test
    public void onElementPermutation(){
        List<Integer> a = Arrays.asList(1);
        List<List<Integer>> b = Lists.permutations(a);
        assertEquals(1, b.size());
        assertEquals(1 ,b.get(0).get(0).intValue());
    }

}
