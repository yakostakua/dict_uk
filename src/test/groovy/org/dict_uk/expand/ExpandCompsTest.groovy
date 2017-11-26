package org.dict_uk.expand

import static org.junit.Assert.*;

import org.dict_uk.common.DicEntry
import org.junit.Test;

public class ExpandCompsTest extends GroovyTestCase {
	
	ExpandComps expandComps
	
	@Override
	void setUp() {
		def affixDir = new File("data/affix").isDirectory() ? "data/affix" : "../../../data/affix"
		Args.parse(["-aff", affixDir, "-dict", ""].toArray(new String[0]))
		
		def expand = new Expand(false)
		expand.affix.load_affixes(affixDir)
		expandComps = new ExpandComps(expand)
	}
	
	
	def fullComps =
'''
Афанасьєв-Чужбинський Афанасьєв-Чужбинський noun:anim:gms:v_naz:prop:lname
Афанасьєва-Чужбинського Афанасьєв-Чужбинський noun:anim:gms:v_rod:prop:lname
Афанасьєва-Чужбинського Афанасьєв-Чужбинський noun:anim:gms:v_zna:prop:lname
Афанасьєву-Чужбинському Афанасьєв-Чужбинський noun:anim:gms:v_dav:prop:lname
Афанасьєву-Чужбинському Афанасьєв-Чужбинський noun:anim:gms:v_mis:prop:lname
Афанасьєві-Чужбинському Афанасьєв-Чужбинський noun:anim:gms:v_mis:prop:lname
Афанасьєвим-Чужбинським Афанасьєв-Чужбинський noun:anim:gms:v_oru:prop:lname
Афанасьєву-Чужбинськім Афанасьєв-Чужбинський noun:anim:gms:v_mis:prop:lname
Афанасьєві-Чужбинськім Афанасьєв-Чужбинський noun:anim:gms:v_mis:prop:lname
'''.trim()
	
	@Test
	void test() {
		def input = ["Афанасьєв /n2adj2.<+ - Чужбинський /n2adj1.<+"]
//		assert expandComps.process_input(input) == fullComps.split(/\n/).collect { DicEntry.fromLine(it) }
		assertEquals(fullComps.split(/\n/).collect { DicEntry.fromLine(it) }, expandComps.process_input(input))
	}
	
	def fullComps2 =
'''
такий-сякий такий-сякий adj:gms:v_naz
такий-сякий такий-сякий adj:gms:v_zna:rinanim
такий-сякий такий-сякий adj:gms:v_kly
такого-сякого такий-сякий adj:gms:v_rod
такого-сякого такий-сякий adj:gms:v_zna:ranim
такого-сякого такий-сякий adj:gns:v_rod
такому-сякому такий-сякий adj:gms:v_dav
такому-сякому такий-сякий adj:gms:v_mis
такім-сякому такий-сякий adj:gms:v_mis
такому-сякому такий-сякий adj:gns:v_dav
такому-сякому такий-сякий adj:gns:v_mis
такім-сякому такий-сякий adj:gns:v_mis
таким-сяким такий-сякий adj:gms:v_oru
таким-сяким такий-сякий adj:gns:v_oru
таким-сяким такий-сякий adj:gop:v_dav
такому-сякім такий-сякий adj:gms:v_mis
такім-сякім такий-сякий adj:gms:v_mis
такому-сякім такий-сякий adj:gns:v_mis
такім-сякім такий-сякий adj:gns:v_mis
така-сяка такий-сякий adj:gfs:v_naz
такая-сяка такий-сякий adj:gfs:v_naz
така-сяка такий-сякий adj:gfs:v_kly
такая-сяка такий-сякий adj:gfs:v_kly
така-сякая такий-сякий adj:gfs:v_naz
такая-сякая такий-сякий adj:gfs:v_naz
така-сякая такий-сякий adj:gfs:v_kly
такая-сякая такий-сякий adj:gfs:v_kly
такої-сякої такий-сякий adj:gfs:v_rod
такій-сякій такий-сякий adj:gfs:v_dav
такій-сякій такий-сякий adj:gfs:v_mis
таку-сяку такий-сякий adj:gfs:v_zna
такую-сяку такий-сякий adj:gfs:v_zna
таку-сякую такий-сякий adj:gfs:v_zna
такую-сякую такий-сякий adj:gfs:v_zna
такою-сякою такий-сякий adj:gfs:v_oru
таке-сяке такий-сякий adj:gns:v_naz
такеє-сяке такий-сякий adj:gns:v_naz
таке-сяке такий-сякий adj:gns:v_zna
такеє-сяке такий-сякий adj:gns:v_zna
таке-сяке такий-сякий adj:gns:v_kly
такеє-сяке такий-сякий adj:gns:v_kly
таке-сякеє такий-сякий adj:gns:v_naz
такеє-сякеє такий-сякий adj:gns:v_naz
таке-сякеє такий-сякий adj:gns:v_zna
такеє-сякеє такий-сякий adj:gns:v_zna
таке-сякеє такий-сякий adj:gns:v_kly
такеє-сякеє такий-сякий adj:gns:v_kly
такі-сякі такий-сякий adj:gop:v_naz
такії-сякі такий-сякий adj:gop:v_naz
такі-сякі такий-сякий adj:gop:v_zna:rinanim
такії-сякі такий-сякий adj:gop:v_zna:rinanim
такі-сякі такий-сякий adj:gop:v_kly
такії-сякі такий-сякий adj:gop:v_kly
такі-сякії такий-сякий adj:gop:v_naz
такії-сякії такий-сякий adj:gop:v_naz
такі-сякії такий-сякий adj:gop:v_zna:rinanim
такії-сякії такий-сякий adj:gop:v_zna:rinanim
такі-сякії такий-сякий adj:gop:v_kly
такії-сякії такий-сякий adj:gop:v_kly
таких-сяких такий-сякий adj:gop:v_rod
таких-сяких такий-сякий adj:gop:v_zna:ranim
таких-сяких такий-сякий adj:gop:v_mis
такими-сякими такий-сякий adj:gop:v_oru
'''.trim()
		
		@Test
		void test2() {
			def input = ["такий /adj - сякий /adj"]
//			println expandComps.process_input(input).join("\n")
			assert ExpandTest.join(expandComps.process_input(input)) == fullComps2
		}

		
def fullComps3 =
'''
автомат-пакувальник автомат-пакувальник noun:inanim:gms:v_naz
автомат-пакувальник автомат-пакувальник noun:inanim:gms:v_zna
автоматові-пакувальникові автомат-пакувальник noun:inanim:gms:v_dav
автомату-пакувальникові автомат-пакувальник noun:inanim:gms:v_dav
автоматові-пакувальникові автомат-пакувальник noun:inanim:gms:v_mis
автоматі-пакувальникові автомат-пакувальник noun:inanim:gms:v_mis
автоматом-пакувальником автомат-пакувальник noun:inanim:gms:v_oru
автоматові-пакувальнику автомат-пакувальник noun:inanim:gms:v_dav
автомату-пакувальнику автомат-пакувальник noun:inanim:gms:v_dav
автоматові-пакувальнику автомат-пакувальник noun:inanim:gms:v_mis
автоматі-пакувальнику автомат-пакувальник noun:inanim:gms:v_mis
автомату-пакувальнику автомат-пакувальник noun:inanim:gms:v_mis
автомате-пакувальнику автомат-пакувальник noun:inanim:gms:v_kly
автомата-пакувальника автомат-пакувальник noun:inanim:gms:v_rod
автоматів-пакувальників автомат-пакувальник noun:inanim:gmp:v_rod
автоматам-пакувальникам автомат-пакувальник noun:inanim:gmp:v_dav
автоматами-пакувальниками автомат-пакувальник noun:inanim:gmp:v_oru
автоматах-пакувальниках автомат-пакувальник noun:inanim:gmp:v_mis
автомати-пакувальники автомат-пакувальник noun:inanim:gmp:v_naz
автомати-пакувальники автомат-пакувальник noun:inanim:gmp:v_zna
автомати-пакувальники автомат-пакувальник noun:inanim:gmp:v_kly
'''.trim()
				
	@Test
	void test3() {
		def input = ["автомат /n20.a.p - пакувальник /n20.a.p !u-*"]
		assert ExpandTest.join(expandComps.process_input(input)) == fullComps3
	}

}