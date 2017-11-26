//import static org.junit.Assert

package org.dict_uk.expand

import org.dict_uk.common.DicEntry
import org.junit.Before
import org.junit.Ignore;
import org.junit.Test;

public class ExpandTest extends GroovyTestCase {
	
	static Expand expand
	
	static
	{
		def args = ["-corp", "-aff", ".", "-dict", "."].toArray(new String[0])
		Args.parse(args)
		
		expand = new Expand(false)
		def affixDir = new File("data/affix").isDirectory() ? "data/affix" : "../../../data/affix"
		expand.affix.load_affixes(affixDir)
	}
	
	@Override
	void setUp() {
//		expand = new Expand()
//		expand.affix.load_affixes("../data/affix")
//		println(expand.affix.affixMap.keySet())
	} 
	
	@Test
	void testAdjustCommonFlag() {
		assert expand.adjustCommonFlag("v2.cf") == "v.cf"
	}

	
	def port = 
'''
порт порт noun:gms:v_naz/v_zna
портові порт noun:gms:v_dav/v_mis
портом порт noun:gms:v_oru
порті порт noun:gms:v_mis
порту порт noun:gms:v_dav/v_mis/v_kly
портів порт noun:gmp:v_rod
портам порт noun:gmp:v_dav
портами порт noun:gmp:v_oru
портах порт noun:gmp:v_mis
порти порт noun:gmp:v_naz
'''.trim() //.split("\n")
	
	@Test
	void testExpandAffix() {
		assert join(expand.expand_suffixes("порт", "n20.p", [:], ":rare")) == port
	}

	def portFull =
'''
порт порт noun:inanim:gms:v_naz:rare
порту порт noun:inanim:gms:v_rod:rare
портові порт noun:inanim:gms:v_dav:rare
порту порт noun:inanim:gms:v_dav:rare
порт порт noun:inanim:gms:v_zna:rare
портом порт noun:inanim:gms:v_oru:rare
порті порт noun:inanim:gms:v_mis:rare
портові порт noun:inanim:gms:v_mis:rare
порту порт noun:inanim:gms:v_mis:rare
порте порт noun:inanim:gms:v_kly:rare
порти порт noun:inanim:gmp:v_naz:rare
портів порт noun:inanim:gmp:v_rod:rare
портам порт noun:inanim:gmp:v_dav:rare
порти порт noun:inanim:gmp:v_zna:rare
портами порт noun:inanim:gmp:v_oru:rare
портах порт noun:inanim:gmp:v_mis:rare
порти порт noun:inanim:gmp:v_kly:rare
'''.trim() //.split("\n")
	
	@Test
	void testProcessInput() {
		def lines = ["порт /n20.p :rare"]
		assert join(expand.process_input(lines)) == portFull
	}
	
	def zhabaFull =
	'''
котяра котяра noun:anim:gms:v_naz
котяри котяра noun:anim:gms:v_rod
котярі котяра noun:anim:gms:v_dav
котяру котяра noun:anim:gms:v_zna
котярою котяра noun:anim:gms:v_oru
котярі котяра noun:anim:gms:v_mis
котяро котяра noun:anim:gms:v_kly
котяри котяра noun:anim:gmp:v_naz
котяр котяра noun:anim:gmp:v_rod
котярам котяра noun:anim:gmp:v_dav
котяр котяра noun:anim:gmp:v_zna
котяри котяра noun:anim:gmp:v_zna
котярами котяра noun:anim:gmp:v_oru
котярах котяра noun:anim:gmp:v_mis
котяри котяра noun:anim:gmp:v_kly
'''.trim() //.split("\n")

	@Test
	void testGenderReplace() {
		def lines = ["котяра /n10.p1.<> ^noun:m"]
		assert expand.process_input(lines).join("\n").replaceAll(/[<>]/, '') == zhabaFull
	}

	
	def strilyatyFull =
'''
стрілявши стрілявши advp:imperf
стріляти стріляти verb:imperf:inf
стріляй стріляти verb:imperf:impr:g2s
стріляймо стріляти verb:imperf:impr:g1p
стріляйте стріляти verb:imperf:impr:g2p
стріляю стріляти verb:imperf:pres:g1s
стріляєш стріляти verb:imperf:pres:g2s
стріляє стріляти verb:imperf:pres:g3s
стріляєм стріляти verb:imperf:pres:g1p:subst
стріляємо стріляти verb:imperf:pres:g1p
стріляєте стріляти verb:imperf:pres:g2p
стріляють стріляти verb:imperf:pres:g3p
стрілятиму стріляти verb:imperf:futr:g1s
стрілятимеш стріляти verb:imperf:futr:g2s
стрілятиме стріляти verb:imperf:futr:g3s
стрілятимем стріляти verb:imperf:futr:g1p
стрілятимемо стріляти verb:imperf:futr:g1p
стрілятимете стріляти verb:imperf:futr:g2p
стрілятимуть стріляти verb:imperf:futr:g3p
стріляв стріляти verb:imperf:past:gms
стріляла стріляти verb:imperf:past:gfs
стріляло стріляти verb:imperf:past:gns
стріляли стріляти verb:imperf:past:gop
стріляно стріляти verb:imperf:impers
'''.trim()

	@Test
	void testStrilyaty() {
		def lines = ["стріляти /v2.cf.isNo :imperf"]
		assert join(expand.process_input(lines)) == strilyatyFull
	}

	
	def stryvatyFull =
'''
стривати стривати verb:imperf:inf
стривай стривати verb:imperf:impr:g2s
стриваймо стривати verb:imperf:impr:g1p
стривайте стривати verb:imperf:impr:g2p
'''.trim()

	@Test
	void testStryvaty() {
		def lines = ["стривати /v2 tag=:impr|:inf :imperf"]
		assert join(expand.process_input(lines)) == stryvatyFull
	}

	
	def adjLastName =
'''
Аверянова Аверянова noun:anim:gfs:v_naz:prop:lname
Аверянової Аверянова noun:anim:gfs:v_rod:prop:lname
Аверяновій Аверянова noun:anim:gfs:v_dav:prop:lname
Аверянову Аверянова noun:anim:gfs:v_zna:prop:lname
Аверяновою Аверянова noun:anim:gfs:v_oru:prop:lname
Аверяновій Аверянова noun:anim:gfs:v_mis:prop:lname
'''.trim()
//Аверянова Аверянова noun:anim:gfs:v_kly:prop:lname

	@Test
	void testAdjLastName() {
		def lines = ["Аверянова /n2adj1.<+"]
		assert join(expand.process_input(lines)) == adjLastName
//		assertEquals(DicEntry.fromLines(adjLastName.split(/\n/)), join(expand.process_input(lines)))
	}

def aFull =
'''
а а conj:coord
а а intj
а а part
'''.trim()

	@Test
	void testFullAlt() {
		def lines = ["а conj:coord|part|intj"]
		assert join(expand.process_input(lines)) == aFull
	}

def multilineFull =
'''
вагомий вагомий adj:gms:v_naz:compb
вагомого вагомий adj:gms:v_rod:compb
вагомому вагомий adj:gms:v_dav:compb
вагомого вагомий adj:gms:v_zna:ranim:compb
вагомий вагомий adj:gms:v_zna:rinanim:compb
вагомим вагомий adj:gms:v_oru:compb
вагомім вагомий adj:gms:v_mis:compb
вагомому вагомий adj:gms:v_mis:compb
вагомий вагомий adj:gms:v_kly:compb
вагома вагомий adj:gfs:v_naz:compb
вагомої вагомий adj:gfs:v_rod:compb
вагомій вагомий adj:gfs:v_dav:compb
вагому вагомий adj:gfs:v_zna:compb
вагомою вагомий adj:gfs:v_oru:compb
вагомій вагомий adj:gfs:v_mis:compb
вагома вагомий adj:gfs:v_kly:compb
вагоме вагомий adj:gns:v_naz:compb
вагомого вагомий adj:gns:v_rod:compb
вагомому вагомий adj:gns:v_dav:compb
вагоме вагомий adj:gns:v_zna:compb
вагомим вагомий adj:gns:v_oru:compb
вагомім вагомий adj:gns:v_mis:compb
вагомому вагомий adj:gns:v_mis:compb
вагоме вагомий adj:gns:v_kly:compb
вагомі вагомий adj:gop:v_naz:compb
вагомих вагомий adj:gop:v_rod:compb
вагомим вагомий adj:gop:v_dav:compb
вагомих вагомий adj:gop:v_zna:ranim:compb
вагомі вагомий adj:gop:v_zna:rinanim:compb
вагомими вагомий adj:gop:v_oru:compb
вагомих вагомий adj:gop:v_mis:compb
вагомі вагомий adj:gop:v_kly:compb
вагоміший вагоміший adj:gms:v_naz:compc
вагомішого вагоміший adj:gms:v_rod:compc
вагомішому вагоміший adj:gms:v_dav:compc
вагомішого вагоміший adj:gms:v_zna:ranim:compc
вагоміший вагоміший adj:gms:v_zna:rinanim:compc
вагомішим вагоміший adj:gms:v_oru:compc
вагомішім вагоміший adj:gms:v_mis:compc
вагомішому вагоміший adj:gms:v_mis:compc
вагоміший вагоміший adj:gms:v_kly:compc
вагоміша вагоміший adj:gfs:v_naz:compc
вагомішої вагоміший adj:gfs:v_rod:compc
вагомішій вагоміший adj:gfs:v_dav:compc
вагомішу вагоміший adj:gfs:v_zna:compc
вагомішою вагоміший adj:gfs:v_oru:compc
вагомішій вагоміший adj:gfs:v_mis:compc
вагоміша вагоміший adj:gfs:v_kly:compc
вагоміше вагоміший adj:gns:v_naz:compc
вагомішого вагоміший adj:gns:v_rod:compc
вагомішому вагоміший adj:gns:v_dav:compc
вагоміше вагоміший adj:gns:v_zna:compc
вагомішим вагоміший adj:gns:v_oru:compc
вагомішім вагоміший adj:gns:v_mis:compc
вагомішому вагоміший adj:gns:v_mis:compc
вагоміше вагоміший adj:gns:v_kly:compc
вагоміші вагоміший adj:gop:v_naz:compc
вагоміших вагоміший adj:gop:v_rod:compc
вагомішим вагоміший adj:gop:v_dav:compc
вагоміших вагоміший adj:gop:v_zna:ranim:compc
вагоміші вагоміший adj:gop:v_zna:rinanim:compc
вагомішими вагоміший adj:gop:v_oru:compc
вагоміших вагоміший adj:gop:v_mis:compc
вагоміші вагоміший adj:gop:v_kly:compc
найвагоміший найвагоміший adj:gms:v_naz:comps
найвагомішого найвагоміший adj:gms:v_rod:comps
найвагомішому найвагоміший adj:gms:v_dav:comps
найвагомішого найвагоміший adj:gms:v_zna:ranim:comps
найвагоміший найвагоміший adj:gms:v_zna:rinanim:comps
найвагомішим найвагоміший adj:gms:v_oru:comps
найвагомішім найвагоміший adj:gms:v_mis:comps
найвагомішому найвагоміший adj:gms:v_mis:comps
найвагоміший найвагоміший adj:gms:v_kly:comps
найвагоміша найвагоміший adj:gfs:v_naz:comps
найвагомішої найвагоміший adj:gfs:v_rod:comps
найвагомішій найвагоміший adj:gfs:v_dav:comps
найвагомішу найвагоміший adj:gfs:v_zna:comps
найвагомішою найвагоміший adj:gfs:v_oru:comps
найвагомішій найвагоміший adj:gfs:v_mis:comps
найвагоміша найвагоміший adj:gfs:v_kly:comps
найвагоміше найвагоміший adj:gns:v_naz:comps
найвагомішого найвагоміший adj:gns:v_rod:comps
найвагомішому найвагоміший adj:gns:v_dav:comps
найвагоміше найвагоміший adj:gns:v_zna:comps
найвагомішим найвагоміший adj:gns:v_oru:comps
найвагомішім найвагоміший adj:gns:v_mis:comps
найвагомішому найвагоміший adj:gns:v_mis:comps
найвагоміше найвагоміший adj:gns:v_kly:comps
найвагоміші найвагоміший adj:gop:v_naz:comps
найвагоміших найвагоміший adj:gop:v_rod:comps
найвагомішим найвагоміший adj:gop:v_dav:comps
найвагоміших найвагоміший adj:gop:v_zna:ranim:comps
найвагоміші найвагоміший adj:gop:v_zna:rinanim:comps
найвагомішими найвагоміший adj:gop:v_oru:comps
найвагоміших найвагоміший adj:gop:v_mis:comps
найвагоміші найвагоміший adj:gop:v_kly:comps
щонайвагоміший щонайвагоміший adj:gms:v_naz:comps
щонайвагомішого щонайвагоміший adj:gms:v_rod:comps
щонайвагомішому щонайвагоміший adj:gms:v_dav:comps
щонайвагомішого щонайвагоміший adj:gms:v_zna:ranim:comps
щонайвагоміший щонайвагоміший adj:gms:v_zna:rinanim:comps
щонайвагомішим щонайвагоміший adj:gms:v_oru:comps
щонайвагомішім щонайвагоміший adj:gms:v_mis:comps
щонайвагомішому щонайвагоміший adj:gms:v_mis:comps
щонайвагоміший щонайвагоміший adj:gms:v_kly:comps
щонайвагоміша щонайвагоміший adj:gfs:v_naz:comps
щонайвагомішої щонайвагоміший adj:gfs:v_rod:comps
щонайвагомішій щонайвагоміший adj:gfs:v_dav:comps
щонайвагомішу щонайвагоміший adj:gfs:v_zna:comps
щонайвагомішою щонайвагоміший adj:gfs:v_oru:comps
щонайвагомішій щонайвагоміший adj:gfs:v_mis:comps
щонайвагоміша щонайвагоміший adj:gfs:v_kly:comps
щонайвагоміше щонайвагоміший adj:gns:v_naz:comps
щонайвагомішого щонайвагоміший adj:gns:v_rod:comps
щонайвагомішому щонайвагоміший adj:gns:v_dav:comps
щонайвагоміше щонайвагоміший adj:gns:v_zna:comps
щонайвагомішим щонайвагоміший adj:gns:v_oru:comps
щонайвагомішім щонайвагоміший adj:gns:v_mis:comps
щонайвагомішому щонайвагоміший adj:gns:v_mis:comps
щонайвагоміше щонайвагоміший adj:gns:v_kly:comps
щонайвагоміші щонайвагоміший adj:gop:v_naz:comps
щонайвагоміших щонайвагоміший adj:gop:v_rod:comps
щонайвагомішим щонайвагоміший adj:gop:v_dav:comps
щонайвагоміших щонайвагоміший adj:gop:v_zna:ranim:comps
щонайвагоміші щонайвагоміший adj:gop:v_zna:rinanim:comps
щонайвагомішими щонайвагоміший adj:gop:v_oru:comps
щонайвагоміших щонайвагоміший adj:gop:v_mis:comps
щонайвагоміші щонайвагоміший adj:gop:v_kly:comps
щоякнайвагоміший щоякнайвагоміший adj:gms:v_naz:comps
щоякнайвагомішого щоякнайвагоміший adj:gms:v_rod:comps
щоякнайвагомішому щоякнайвагоміший adj:gms:v_dav:comps
щоякнайвагомішого щоякнайвагоміший adj:gms:v_zna:ranim:comps
щоякнайвагоміший щоякнайвагоміший adj:gms:v_zna:rinanim:comps
щоякнайвагомішим щоякнайвагоміший adj:gms:v_oru:comps
щоякнайвагомішім щоякнайвагоміший adj:gms:v_mis:comps
щоякнайвагомішому щоякнайвагоміший adj:gms:v_mis:comps
щоякнайвагоміший щоякнайвагоміший adj:gms:v_kly:comps
щоякнайвагоміша щоякнайвагоміший adj:gfs:v_naz:comps
щоякнайвагомішої щоякнайвагоміший adj:gfs:v_rod:comps
щоякнайвагомішій щоякнайвагоміший adj:gfs:v_dav:comps
щоякнайвагомішу щоякнайвагоміший adj:gfs:v_zna:comps
щоякнайвагомішою щоякнайвагоміший adj:gfs:v_oru:comps
щоякнайвагомішій щоякнайвагоміший adj:gfs:v_mis:comps
щоякнайвагоміша щоякнайвагоміший adj:gfs:v_kly:comps
щоякнайвагоміше щоякнайвагоміший adj:gns:v_naz:comps
щоякнайвагомішого щоякнайвагоміший adj:gns:v_rod:comps
щоякнайвагомішому щоякнайвагоміший adj:gns:v_dav:comps
щоякнайвагоміше щоякнайвагоміший adj:gns:v_zna:comps
щоякнайвагомішим щоякнайвагоміший adj:gns:v_oru:comps
щоякнайвагомішім щоякнайвагоміший adj:gns:v_mis:comps
щоякнайвагомішому щоякнайвагоміший adj:gns:v_mis:comps
щоякнайвагоміше щоякнайвагоміший adj:gns:v_kly:comps
щоякнайвагоміші щоякнайвагоміший adj:gop:v_naz:comps
щоякнайвагоміших щоякнайвагоміший adj:gop:v_rod:comps
щоякнайвагомішим щоякнайвагоміший adj:gop:v_dav:comps
щоякнайвагоміших щоякнайвагоміший adj:gop:v_zna:ranim:comps
щоякнайвагоміші щоякнайвагоміший adj:gop:v_zna:rinanim:comps
щоякнайвагомішими щоякнайвагоміший adj:gop:v_oru:comps
щоякнайвагоміших щоякнайвагоміший adj:gop:v_mis:comps
щоякнайвагоміші щоякнайвагоміший adj:gop:v_kly:comps
якнайвагоміший якнайвагоміший adj:gms:v_naz:comps
якнайвагомішого якнайвагоміший adj:gms:v_rod:comps
якнайвагомішому якнайвагоміший adj:gms:v_dav:comps
якнайвагомішого якнайвагоміший adj:gms:v_zna:ranim:comps
якнайвагоміший якнайвагоміший adj:gms:v_zna:rinanim:comps
якнайвагомішим якнайвагоміший adj:gms:v_oru:comps
якнайвагомішім якнайвагоміший adj:gms:v_mis:comps
якнайвагомішому якнайвагоміший adj:gms:v_mis:comps
якнайвагоміший якнайвагоміший adj:gms:v_kly:comps
якнайвагоміша якнайвагоміший adj:gfs:v_naz:comps
якнайвагомішої якнайвагоміший adj:gfs:v_rod:comps
якнайвагомішій якнайвагоміший adj:gfs:v_dav:comps
якнайвагомішу якнайвагоміший adj:gfs:v_zna:comps
якнайвагомішою якнайвагоміший adj:gfs:v_oru:comps
якнайвагомішій якнайвагоміший adj:gfs:v_mis:comps
якнайвагоміша якнайвагоміший adj:gfs:v_kly:comps
якнайвагоміше якнайвагоміший adj:gns:v_naz:comps
якнайвагомішого якнайвагоміший adj:gns:v_rod:comps
якнайвагомішому якнайвагоміший adj:gns:v_dav:comps
якнайвагоміше якнайвагоміший adj:gns:v_zna:comps
якнайвагомішим якнайвагоміший adj:gns:v_oru:comps
якнайвагомішім якнайвагоміший adj:gns:v_mis:comps
якнайвагомішому якнайвагоміший adj:gns:v_mis:comps
якнайвагоміше якнайвагоміший adj:gns:v_kly:comps
якнайвагоміші якнайвагоміший adj:gop:v_naz:comps
якнайвагоміших якнайвагоміший adj:gop:v_rod:comps
якнайвагомішим якнайвагоміший adj:gop:v_dav:comps
якнайвагоміших якнайвагоміший adj:gop:v_zna:ranim:comps
якнайвагоміші якнайвагоміший adj:gop:v_zna:rinanim:comps
якнайвагомішими якнайвагоміший adj:gop:v_oru:comps
якнайвагоміших якнайвагоміший adj:gop:v_mis:comps
якнайвагоміші якнайвагоміший adj:gop:v_kly:comps
'''.trim()
	
	@Test
//	@Ignore
	void testMultiline() {
		def lines = ["вагомий /adj \\", " +cs=вагоміший"]
//		assert join(expand.process_input(lines)) == multilineFull
		assertEquals(multilineFull, join(expand.process_input(lines)))
//		assertEquals(new HashSet<>(multilineFull.split(/\n/).collect{DicEntry.fromLine(it)}), 
//			new HashSet<>(expand.process_input(lines)))
	}

def taggedIn = 
'''
абичий абичий adj:gms:v_naz/v_zna:&pron:ind
абичийого абичий adj:gms:v_rod/v_zna//gns:v_rod:&pron:ind
абичийому абичий adj:gms:v_dav/v_mis//gns:v_dav/v_mis:&pron:ind
абичиєму абичий adj:gms:v_dav/v_mis//gns:v_dav/v_mis:&pron:ind
абичиїм абичий adj:gms:v_mis//gns:v_oru//gop:v_dav:&pron:ind
абичия абичий adj:gfs:v_naz:&pron:ind
абичиєї абичий adj:gfs:v_rod:&pron:ind
абичиїй абичий adj:gfs:v_dav/v_mis:&pron:ind
абичию абичий adj:gfs:v_zna:&pron:ind
абичиєю абичий adj:gfs:v_oru:&pron:ind
абичиї абичий adj:gop:v_naz/v_zna:&pron:ind
абичиїх абичий adj:gop:v_rod/v_zna/v_mis:&pron:ind
абичиїми абичий adj:gop:v_oru:&pron:ind
'''.trim().split("\n")
		
def taggedOut = 
'''
абичий абичий adj:gms:v_naz:&pron:ind
абичийого абичий adj:gms:v_rod:&pron:ind
абичиєму абичий adj:gms:v_dav:&pron:ind
абичийому абичий adj:gms:v_dav:&pron:ind
абичий абичий adj:gms:v_zna:&pron:ind
абичийого абичий adj:gms:v_zna:&pron:ind
абичиєму абичий adj:gms:v_mis:&pron:ind
абичиїм абичий adj:gms:v_mis:&pron:ind
абичийому абичий adj:gms:v_mis:&pron:ind
абичия абичий adj:gfs:v_naz:&pron:ind
абичиєї абичий adj:gfs:v_rod:&pron:ind
абичиїй абичий adj:gfs:v_dav:&pron:ind
абичию абичий adj:gfs:v_zna:&pron:ind
абичиєю абичий adj:gfs:v_oru:&pron:ind
абичиїй абичий adj:gfs:v_mis:&pron:ind
абичийого абичий adj:gns:v_rod:&pron:ind
абичиєму абичий adj:gns:v_dav:&pron:ind
абичийому абичий adj:gns:v_dav:&pron:ind
абичиїм абичий adj:gns:v_oru:&pron:ind
абичиєму абичий adj:gns:v_mis:&pron:ind
абичийому абичий adj:gns:v_mis:&pron:ind
абичиї абичий adj:gop:v_naz:&pron:ind
абичиїх абичий adj:gop:v_rod:&pron:ind
абичиїм абичий adj:gop:v_dav:&pron:ind
абичиї абичий adj:gop:v_zna:&pron:ind
абичиїх абичий adj:gop:v_zna:&pron:ind
абичиїми абичий adj:gop:v_oru:&pron:ind
абичиїх абичий adj:gop:v_mis:&pron:ind
'''.trim()	

	@Test
	void testTaggedLine() {
		assert join(expand.process_input(Arrays.asList(taggedIn))) == taggedOut
	}

	def taggedNvIn =
'''
авто noun:n:nv
'''.trim().split("\n")

	def taggedNvOut =
'''
авто авто noun:inanim:gns:v_naz:nv
авто авто noun:inanim:gns:v_rod:nv
авто авто noun:inanim:gns:v_dav:nv
авто авто noun:inanim:gns:v_zna:nv
авто авто noun:inanim:gns:v_oru:nv
авто авто noun:inanim:gns:v_mis:nv
авто авто noun:inanim:gns:v_kly:nv
авто авто noun:inanim:gnp:v_naz:nv
авто авто noun:inanim:gnp:v_rod:nv
авто авто noun:inanim:gnp:v_dav:nv
авто авто noun:inanim:gnp:v_zna:nv
авто авто noun:inanim:gnp:v_oru:nv
авто авто noun:inanim:gnp:v_mis:nv
авто авто noun:inanim:gnp:v_kly:nv
'''.trim()

	@Test
	void testExpandNv() {
		assert join(expand.process_input(Arrays.asList(taggedNvIn))) == taggedNvOut
	}

	
	def lastNameIn =
	'''
Адамишин /n2adj2.<+
Венедикт /n20.a.ke.<.patr
'''.trim().split("\n")

def lastNameOut =
'''
11:48:01.895 [main] DEBUG org.dict_uk.expand.OutputValidator - Read 101 allowed tags

Loading affixes from directory data/affix
11:48:02.057 [main] DEBUG org.dict_uk.expand.Affix - Loaded: [n32.ke, n24.a, n20.p, n10, n2nf, n20.u, v.isNo, n24.p, v4.advp, n21.zi, adj.adv, n20.pzi, n32.p, n2nm.p, n21, n20, n23, n22, n24, v3.advp, n21.ke, n.patr_pl, n20.pyn, n20.a, n10.p3, n10.p1, n10.p2, v.is3, v1.it0c, n30.ke, n23.a, n21.ku, n30, n20.p4, n32, vr2.advp, n2adj2, n31, n2adj1, n20.p1, n2adj3, n23.p, v1.it0, n24.ke, v1.it1, v.is1, v.is0, vr1.it0c, vr.cf, v2.advp, n31.p, n2adj1.ke, adj_pron, n40, n2n, n2n.p1, n2n.p2, n2n.p3, adj_ev, n2nm, n10.piv, v1, v2, n40.piv, n2n.piv, v3, v4, v6, n2adj1.p, n22.a, n40.yu, n2adj2.ke, n20.po, n22.u, np2.np3, vr4.advp, n22.p, n10.ku, n2nf.p, n2adj1.f, n22.ke, n10.ko, vr1.it0, vr1.it1, numr, adj, n31.ke, n22.ku, v.isTo, n30.p, np1, np3, np2, n2n.ovi, vr3.advp, n20.zi, v1.advp, n21.a, n40.p, n21.p, n20.ke, n21.u, vr1.advp, n.patr, n20.ku, v.cf, vr1, n23.ke, vr3, vr2, vr4, vr6, n2adj1i]
Адамишин Адамишин noun:anim:gms:v_naz:prop:lname
Адамишина Адамишин noun:anim:gms:v_rod:prop:lname
Адамишину Адамишин noun:anim:gms:v_dav:prop:lname
Адамишина Адамишин noun:anim:gms:v_zna:prop:lname
Адамишиним Адамишин noun:anim:gms:v_oru:prop:lname
Адамишині Адамишин noun:anim:gms:v_mis:prop:lname
Адамишину Адамишин noun:anim:gms:v_mis:prop:lname
Адамишини Адамишин noun:anim:gdp:v_naz:prop:lname
Адамишиних Адамишин noun:anim:gdp:v_rod:prop:lname
Адамишиним Адамишин noun:anim:gdp:v_dav:prop:lname
Адамишиних Адамишин noun:anim:gdp:v_zna:prop:lname
Адамишиними Адамишин noun:anim:gdp:v_oru:prop:lname
Адамишиних Адамишин noun:anim:gdp:v_mis:prop:lname
Адамишин Адамишин noun:anim:gfs:v_naz:nv:np:prop:lname
Адамишин Адамишин noun:anim:gfs:v_rod:nv:np:prop:lname
Адамишин Адамишин noun:anim:gfs:v_dav:nv:np:prop:lname
Адамишин Адамишин noun:anim:gfs:v_zna:nv:np:prop:lname
Адамишин Адамишин noun:anim:gfs:v_oru:nv:np:prop:lname
Адамишин Адамишин noun:anim:gfs:v_mis:nv:np:prop:lname
Адамишин Адамишин noun:anim:gfs:v_kly:nv:np:prop:lname
Адамишина Адамишина noun:anim:gfs:v_naz:prop:lname
Адамишиної Адамишина noun:anim:gfs:v_rod:prop:lname
Адамишиній Адамишина noun:anim:gfs:v_dav:prop:lname
Адамишину Адамишина noun:anim:gfs:v_zna:prop:lname
Адамишиною Адамишина noun:anim:gfs:v_oru:prop:lname
Адамишиній Адамишина noun:anim:gfs:v_mis:prop:lname
Венедикт Венедикт noun:anim:gms:v_naz:prop:fname
Венедикта Венедикт noun:anim:gms:v_rod:prop:fname
Венедиктові Венедикт noun:anim:gms:v_dav:prop:fname
Венедикту Венедикт noun:anim:gms:v_dav:prop:fname
Венедикта Венедикт noun:anim:gms:v_zna:prop:fname
Венедиктом Венедикт noun:anim:gms:v_oru:prop:fname
Венедикті Венедикт noun:anim:gms:v_mis:prop:fname
Венедиктові Венедикт noun:anim:gms:v_mis:prop:fname
Венедикту Венедикт noun:anim:gms:v_mis:prop:fname
Венедикте Венедикт noun:anim:gms:v_kly:prop:fname
Венедиктівна Венедиктівна noun:anim:gfs:v_naz:prop:pname
Венедиктівни Венедиктівна noun:anim:gfs:v_rod:prop:pname
Венедиктівні Венедиктівна noun:anim:gfs:v_dav:prop:pname
Венедиктівну Венедиктівна noun:anim:gfs:v_zna:prop:pname
Венедиктівною Венедиктівна noun:anim:gfs:v_oru:prop:pname
Венедиктівні Венедиктівна noun:anim:gfs:v_mis:prop:pname
Венедиктівно Венедиктівна noun:anim:gfs:v_kly:prop:pname
Венедиктович Венедиктович noun:anim:gms:v_naz:prop:pname
Венедиктовича Венедиктович noun:anim:gms:v_rod:prop:pname
Венедиктовичеві Венедиктович noun:anim:gms:v_dav:prop:pname
Венедиктовичу Венедиктович noun:anim:gms:v_dav:prop:pname
Венедиктовича Венедиктович noun:anim:gms:v_zna:prop:pname
Венедиктовичем Венедиктович noun:anim:gms:v_oru:prop:pname
Венедиктовичеві Венедиктович noun:anim:gms:v_mis:prop:pname
Венедиктовичі Венедиктович noun:anim:gms:v_mis:prop:pname
Венедиктовичу Венедиктович noun:anim:gms:v_mis:prop:pname
Венедиктовичу Венедиктович noun:anim:gms:v_kly:prop:pname
'''.trim()

	def lastNameOutSorted =
'''
Адамишин noun:anim:gms:v_naz:prop:lname
  Адамишина noun:anim:gms:v_rod:prop:lname
  Адамишину noun:anim:gms:v_dav:prop:lname
  Адамишина noun:anim:gms:v_zna:prop:lname
  Адамишиним noun:anim:gms:v_oru:prop:lname
  Адамишині noun:anim:gms:v_mis:prop:lname
  Адамишину noun:anim:gms:v_mis:prop:lname
  Адамишини noun:anim:gdp:v_naz:prop:lname
  Адамишиних noun:anim:gdp:v_rod:prop:lname
  Адамишиним noun:anim:gdp:v_dav:prop:lname
  Адамишиних noun:anim:gdp:v_zna:prop:lname
  Адамишиними noun:anim:gdp:v_oru:prop:lname
  Адамишиних noun:anim:gdp:v_mis:prop:lname
Адамишин noun:anim:gfs:v_naz:nv:np:prop:lname
  Адамишин noun:anim:gfs:v_rod:nv:np:prop:lname
  Адамишин noun:anim:gfs:v_dav:nv:np:prop:lname
  Адамишин noun:anim:gfs:v_zna:nv:np:prop:lname
  Адамишин noun:anim:gfs:v_oru:nv:np:prop:lname
  Адамишин noun:anim:gfs:v_mis:nv:np:prop:lname
  Адамишин noun:anim:gfs:v_kly:nv:np:prop:lname
Адамишина noun:anim:gfs:v_naz:prop:lname
  Адамишиної noun:anim:gfs:v_rod:prop:lname
  Адамишиній noun:anim:gfs:v_dav:prop:lname
  Адамишину noun:anim:gfs:v_zna:prop:lname
  Адамишиною noun:anim:gfs:v_oru:prop:lname
  Адамишиній noun:anim:gfs:v_mis:prop:lname
Венедикт noun:anim:m:v_naz:prop:fname
  Венедикта noun:anim:m:v_rod:prop:fname
  Венедиктові noun:anim:m:v_dav:prop:fname
  Венедикту noun:anim:m:v_dav:prop:fname
  Венедикта noun:anim:m:v_zna:prop:fname
  Венедиктом noun:anim:m:v_oru:prop:fname
  Венедикті noun:anim:m:v_mis:prop:fname
  Венедиктові noun:anim:m:v_mis:prop:fname
  Венедикту noun:anim:m:v_mis:prop:fname
  Венедикте noun:anim:m:v_kly:prop:fname
Венедиктівна noun:anim:f:v_naz:prop:pname
  Венедиктівни noun:anim:f:v_rod:prop:pname
  Венедиктівні noun:anim:f:v_dav:prop:pname
  Венедиктівну noun:anim:f:v_zna:prop:pname
  Венедиктівною noun:anim:f:v_oru:prop:pname
  Венедиктівні noun:anim:f:v_mis:prop:pname
  Венедиктівно noun:anim:f:v_kly:prop:pname
Венедиктович noun:anim:m:v_naz:prop:pname
  Венедиктовича noun:anim:m:v_rod:prop:pname
  Венедиктовичеві noun:anim:m:v_dav:prop:pname
  Венедиктовичу noun:anim:m:v_dav:prop:pname
  Венедиктовича noun:anim:m:v_zna:prop:pname
  Венедиктовичем noun:anim:m:v_oru:prop:pname
  Венедиктовичеві noun:anim:m:v_mis:prop:pname
  Венедиктовичі noun:anim:m:v_mis:prop:pname
  Венедиктовичу noun:anim:m:v_mis:prop:pname
  Венедиктовичу noun:anim:m:v_kly:prop:pname
'''.trim()

	@Test
	void testExpandLastName() {
		def outForms = expand.process_input(Arrays.asList(lastNameIn))
		
		assert join(outForms) == lastNameOut
		
		List<String> indentedLines = new DictSorter().indent_lines(expand.sortAndPostProcess(outForms))
		assert join(indentedLines) == lastNameOutSorted
	}

	
	static final String join(def entries) {
		return entries.join("\n").replaceAll(/[<>]/, '')
	}
}

