package ch.epfl.xblast.testPerso.etape8;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ch.epfl.xblast.RunLengthEncoder;

public class RunLengthEncoderTests {
	private Byte d = 10;
	private Byte v = 20;
	private Byte t = 30;
	private Byte q = 40;
	private List<List<Byte>> expectedForDecoding = new ArrayList<List<Byte>>(Arrays.asList(
			Collections.nCopies(130, d),
			Arrays.asList(d,d,d,v,v,t,v,t,q,q,q,q,q,q),
			Arrays.asList(d)
			));
	
	private List<List<Byte>> expectedForEncoding = new ArrayList<List<Byte>>(Arrays.asList(
			Arrays.asList(d),
			Arrays.asList(d,v),
			Arrays.asList(),
			Arrays.asList((byte)-1, d, v, v, t, v, t, (byte) -4, q),
			Arrays.asList((byte)-128, d, d),
			Arrays.asList((byte)-128, d, (byte)-128,d)));
	@Test
	public void normalEncode() {

		List<Byte> l = new ArrayList<>(Arrays.asList(d,d,d,v,v,t,v,t,q,q,q,q,q,q));
		List<Byte> e = expectedForEncoding.get(3);
		l = RunLengthEncoder.encode(l);
		assertEquals(e.size(), l.size());
		for (int i = 0; i < e.size(); i++) {
			assertEquals(e.get(i), l.get(i));
		}
	}
	
	@Test
	public void oneElement(){
		List<Byte> l = new ArrayList<>(Arrays.asList((byte)10));
		List<Byte> e = expectedForEncoding.get(0);
		l = RunLengthEncoder.encode(l);
		assertEquals(e.size(), l.size());
		for (int i = 0; i < e.size(); i++) {
			assertEquals(e.get(i), l.get(i));
		}
	}
	@Test
	public void twoDiffElements(){
		List<Byte> l = new ArrayList<>(Arrays.asList((byte)10, (byte)20));
		List<Byte> e = expectedForEncoding.get(1);
		l = RunLengthEncoder.encode(l);
		assertEquals(e.size(), l.size());
		for (int i = 0; i < e.size(); i++) {
			assertEquals(e.get(i), l.get(i));
		}
	}
	
	@Test
	public void emptyList(){
		List<Byte> l = RunLengthEncoder.encode(new ArrayList<>());
		List<Byte> e = expectedForEncoding.get(2);
		assertEquals(e.size(), l.size());
		for (int i = 0; i < e.size(); i++) {
			assertEquals(e.get(i), l.get(i));
		}
	}
	
	@Test
	public void moreThan130(){
		List<Byte> l = Collections.nCopies(131, d);
		List<Byte> e = expectedForEncoding.get(4);
		l = RunLengthEncoder.encode(l);
		System.out.println(l);
		assertEquals(e.size(), l.size());
		for (int i = 0; i < e.size(); i++) {
			assertEquals(e.get(i), l.get(i));
		}
	}
	@Test
	public void elements260(){
		List<Byte> l = Collections.nCopies(260, d);
		List<Byte> e = expectedForEncoding.get(5);
		l = RunLengthEncoder.encode(l);
		assertEquals(e.size(), l.size());
		for (int i = 0; i < e.size(); i++) {
			assertEquals(e.get(i), l.get(i));
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void exceptionsInEncode(){
		Byte neg = -10;
		List<Byte> l = new ArrayList<>(Arrays.asList(d,d,neg,v,v,t,v,t,q,q,q,q,q,q));
		l = RunLengthEncoder.encode(l);
	}
	
	@Test
	public void  testOnLimitByteValue(){
		List<Byte> l = new ArrayList<>(Arrays.asList((byte)-128, d));
		List<Byte> e = expectedForDecoding.get(0);
		l = RunLengthEncoder.decode(l);
		assertEquals(e.size(), l.size());
		for (int i = 0; i < e.size(); i++) {
			assertEquals(e.get(i), l.get(i));
		}
	}
	@Test
	public void normalDecode(){
		List<Byte> l = new ArrayList<>(Arrays.asList((byte)-1, d, v, v, t, v, t, (byte) -4, q));
		List<Byte> e = expectedForDecoding.get(1);
		l = RunLengthEncoder.decode(l);
		assertEquals(e.size(), l.size());
		for (int i = 0; i < e.size(); i++) {
			assertEquals(e.get(i), l.get(i));
		}
	}
	
	@Test
	public void oneElementDecode(){
		List<Byte> l = new ArrayList<>(Arrays.asList(d));
		List<Byte> e = expectedForDecoding.get(2);
		l = RunLengthEncoder.decode(l);
		assertEquals(e.size(), l.size());
		for (int i = 0; i < e.size(); i++) {
			assertEquals(e.get(i), l.get(i));
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testOnExceptionsDecoding(){
		Byte neg = -10;
		List<Byte> l = new ArrayList<>(Arrays.asList((byte)-1, d, v, v, t, v, t, (byte) -4, neg));
		RunLengthEncoder.decode(l);
	}

}
