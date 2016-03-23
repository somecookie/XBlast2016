package ch.epfl.xblast;

import static org.junit.Assert.assertEquals;

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
        //assertEquals(120, b.size());
        for (int i = 0; i < b.size(); i++) {
            System.out.println(b.get(i));
        }
    }

}
