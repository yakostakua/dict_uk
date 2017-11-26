package org.dict_uk.expand

import java.security.acl.LastOwnerException
import java.util.regex.Pattern

import org.dict_uk.common.DicEntry
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked;


class OutputValidator {
	static Logger log = LoggerFactory.getLogger(OutputValidator.class);
	
	static final Pattern WORD_RE = Pattern.compile("[а-яіїєґА-ЯІЇЄҐ][а-яіїєґА-ЯІЇЄҐ']*(-[а-яіїєґА-ЯІЇЄҐ']*)*|[А-ЯІЇЄҐ][А-ЯІЇЄҐ-]+|[а-яіїєґ]+\\.(-[а-яіїєґ]+\\.)?")
	static final Pattern POS_RE = Pattern.compile("(noun:([iu]n)?anim:|noun:.*:&pron|verb(:rev)?:(im)?perf:|advp:(im)?perf|adj:g[mfno][sp]:|adv|numr:|prep|part|intj|conj:|onomat|foreign|noninfl).*")
    static final List<String> IGNORED_NOUNS = ["бельмеса", "давніх-давен", "основанья", "предку-віку", "роб", "свободівець", "шатер",
            "галай-балай", "вепр", "вихідець", "гратами", "мати-одиночка", "кінця-краю", "усіх-усюд", "Таганріг"]

	final List<String> ALLOWED_TAGS = getClass().getResource("tagset.txt").readLines()

	OutputValidator() {
		log.debug("Read {} allowed tags\n", ALLOWED_TAGS.size())
	}
	
	@CompileStatic
	int checkEntries(List<DicEntry> lines) {
		int fatalErrorCount = 0
		
		for(DicEntry line in lines ) {
			DicEntry dicEntry = line //DicEntry.fromLine(line)
			def word = dicEntry.word
			def lemma = dicEntry.lemma
			def tags = dicEntry.tagStr

			if( ! WORD_RE.matcher(word).matches() || ! WORD_RE.matcher(lemma).matches() ) {
				log.error("Invalid pattern in word or lemma: " + line)
				fatalErrorCount++
			}

			if( ! POS_RE.matcher(tags).matches() ) {
				log.error("Invalid main postag in word: " + line)
				fatalErrorCount++
			}

			def tagList = dicEntry.tags

			for(String tag in tagList) {
				if( ! (tag in ALLOWED_TAGS) ) {
					log.error("Invalid tag " + tag + ": " + line)
					fatalErrorCount++
				}
			}

			def dup_tags = tagList.findAll { tagList.count(it) > 1 }.unique()
			if( dup_tags ) {
				log.error("Duplicate tags " + dup_tags.join(":") + ": " + line)
				if( !("coll" in dup_tags) ) {
					fatalErrorCount++
				}
			}
		}
		
		return fatalErrorCount
	}

	static final List<String> ALL_V_TAGS = ["v_naz", "v_rod", "v_dav", "v_zna", "v_oru", "v_mis", "v_kly"]
	static final List<String> ADJ_PRON_V_TAGS = ["v_naz", "v_rod", "v_dav", "v_zna", "v_oru", "v_mis"]
	static final List<String> ALL_IMPERF_VERB_TAGS = ["inf",
//			"impr:s:2", "impr:p:1", "impr:p:2", \
			"pres:g1s", "pres:g2s", "pres:g3s", \
			"pres:g1p", "pres:g2p", "pres:g3p", \
			"past:gms", "past:gfs", "past:gns", "past:gop", \
			 ]
	static final List<String> ALL_PERF_VERB_TAGS = ALL_IMPERF_VERB_TAGS.collect { it.replace("pres:", "futr:") }
	static final Pattern VERB_CHECK_PATTERN = ~/inf|impr:g2s|impr:g[12]p|(?:pres|futr):g[123][sp]|past:g(?:[mfn]s|op)/

	@CompileStatic
	int check_indented_lines(List<String> lines, List<String> limitedVerbLemmas) {
		String gender = ""
		HashSet<String> subtagSet = new HashSet<String>()
		String lemmaLine
		List<String> lastVerbTags = null
		int nonFatalErrorCount = 0
		
		limitedVerbLemmas << "хтітися"
		
		//		ParallelEnhancer.enhanceInstance(lines)

		lines.each { String line ->
			def parts = line.trim().split(" ")
			def word = parts[0]
			def tags = parts[1]
			
			if( ! line.startsWith(" ") ) {
				if (gender) {
					checkVTagSet(gender, subtagSet, lemmaLine)
				}
				else if( lastVerbTags && ! lemmaLine.contains(". ") ) {
					log.error("verb lemma is missing " + (lastVerbTags) + " for: " + lemmaLine)
					nonFatalErrorCount++
					lastVerbTags = null
				}

				subtagSet.clear()
				gender = ""
				lemmaLine = line
				
				if( tags.startsWith("verb:") && ! lemmaLine.contains(":inf:dimin") && ! (word in limitedVerbLemmas) ) {
					lastVerbTags = new ArrayList<>(tags.contains(":imperf") ? ALL_IMPERF_VERB_TAGS : ALL_PERF_VERB_TAGS)
				}
				else {
					lastVerbTags = null
				}
			}
			
			if( ( tags.startsWith("noun") && ! tags.contains("&pron") )
			        || ( tags.startsWith("adj") && tags.contains("&pron") ) ) {

				def tagSet = tags.split(':')
				def gen = tagSet.find { it.size() == 3 && it =~ /[mfn][sp]|[odx]p/ }
				assert gen : "Cound not find gen in " + tags + " for " + line

				if( gen != gender ) {
					if (gender) {
						checkVTagSet(gender, subtagSet, lemmaLine)
					}
					if( tags.contains(":short") ) {
					    gender = ''
					}
					else {
					    gender = gen
					}
					subtagSet.clear()
				}

				String v_tag = tagSet.find { it.startsWith("v_") }
				subtagSet.add( v_tag )
			}
			else if ( lastVerbTags ) {
				def tagg = VERB_CHECK_PATTERN.matcher(tags)
				if( tagg ) {
					lastVerbTags.remove(tagg[0])
				}
			}
		}
		
		return nonFatalErrorCount
	}

//	private static final Set V_KLY_ONLY = new HashSet(Arrays.asList("v_kly"))

	@CompileStatic
	private checkVTagSet(String gender, Set subtagSet, String line) {
		int nonFatalErrorCount = 0
		
		if( ! subtagSet.containsAll(ALL_V_TAGS) && ! line.contains(". ") ) {
			def missingVSet = ALL_V_TAGS - subtagSet

			if( missingVSet == ["v_kly"] && (line.contains(":lname") 
			        || (line.contains(" adj") && line.contains("&pron")) ) )
				return nonFatalErrorCount
			
			if( line.split()[0] in IGNORED_NOUNS )
			    return nonFatalErrorCount
			
			log.error("noun lemma is missing " + missingVSet + " on gender " + gender + " for: " + line)
			nonFatalErrorCount++
		}
		
		return nonFatalErrorCount
	}

	
}