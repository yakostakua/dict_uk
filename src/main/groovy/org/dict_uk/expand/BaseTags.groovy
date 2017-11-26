package org.dict_uk.expand

import java.util.regex.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked


class BaseTags {
	static Logger log = LoggerFactory.getLogger(BaseTags.class);

	static private final Pattern ending_i_nnia_re = Pattern.compile(/.*(([бвгджзклмнпрстфхцчшщ])\2|\'|[джлрт]|рн)я$/)

	static private final Util util = new Util()


	@CompileStatic
	String  get_base_tags(String word, String affixFlag, String allAffixFlags, String extra) {
		affixFlag = allAffixFlags

		def tag = ""

		if( affixFlag[0..<2] == "vr" )
			tag = ":rev:inf"
		else if( affixFlag[0] == "v" )
			tag = ":inf"

		if( tag )
			return tag

		def v_zna_for_inanim = ""

		if( ! util.istota(allAffixFlags) || util.bacteria(allAffixFlags) ) {
			v_zna_for_inanim = "/v_zna"
		}

		if( affixFlag.startsWith("adj") ) {

			if( word.endsWith("е") || word.endsWith("є") ) {
				tag = ":gns:v_naz/v_zna/v_kly"
		    }
			else if( word.endsWith("і") ) {
				tag = ":gxp:v_naz/v_zn2/v_kly:ns"
			}
			else if( word.endsWith("а") || word.endsWith("я") ) {
				tag = ":gfs:v_naz/v_kly"
			}
			else if( word.endsWith("ій") || word.endsWith("їй") ) {
				if( affixFlag.startsWith("adj_pron") ) {
					tag = ":gms:v_naz/v_zn2/v_kly"
				}
				else {
					tag = ":gms:v_naz/v_zn2/v_kly//gfs:v_dav/v_mis"
				}
			}
			else {
				tag = ":gms:v_naz/v_zn2/v_kly"
			}

			return tag
		}

		if( affixFlag == "numr" ) {
		    if( word.endsWith("ин") ) {
		        tag = ":gms:v_naz/v_zn2"
		    }
		    else {
			    tag = ":gop:v_naz/v_zna"
			}
			return tag
		}

		if( affixFlag.startsWith("n2n") ) {
		    if( affixFlag.startsWith("n2nm") ) { // сутяжище /n2nm.p.<
			    tag = ":gms:v_naz" + v_zna_for_inanim
			    tag += "/v_kly"
		    }
		    else if( affixFlag.startsWith("n2nf") ) { // сутяжище /n2nf.p.<
			    tag = ":gfs:v_naz" + v_zna_for_inanim
			    tag += "/v_zna/v_kly"
		    }
		    else if( ending_i_nnia_re.matcher(word).matches() ) {
				tag = ":gns:v_naz/v_rod/v_zna/v_kly//gnp:v_naz/v_kly"
			}
			else {
				tag = ":gns:v_naz/v_zna/v_kly"
			}
		}
		else if( affixFlag.startsWith("np") ) {
			tag = ":gxp:v_naz/v_kly" // + v_zna_for_inanim
		}
		else if( affixFlag.startsWith("n2adj1") ) {
		    if( word.endsWith("е") || word.endsWith("є") || word.endsWith("о") ) {
			    tag = ":gns:v_naz/v_kly" + v_zna_for_inanim
			}
		    else if( word.endsWith("а") || word.endsWith("я") ) {
			    tag = ":gfs:v_naz/v_kly"
			}
		    else if( word.endsWith("і") ) {
			    tag = ":gxp:v_naz/v_kly"
			}
			else {
    			tag = ":gms:v_naz/v_kly" + v_zna_for_inanim
			}
	    }
		else if( affixFlag.startsWith("n2adj2") ) {
   			tag = ":gms:v_naz" + v_zna_for_inanim
		}
		else if( affixFlag[0..<2] == "n2" ) {
			tag = ":gms:v_naz" + v_zna_for_inanim
//			if( affixFlag.startsWith("n20") && util.person(allAffixFlags) && (word[-2..-1] == "ло") && ! allAffixFlags.contains(".k") ) {
//				tag += "/v_kly"
//			}
		}
		else if( affixFlag[0..<2] == "n1" ) {
			tag = ":gfs:v_naz"
		}
		else if( affixFlag[0..<2] == "n4" ) {
			tag = ":gns:v_naz/v_zna/v_kly"
		}
		else if( affixFlag[0..<2] == "n3" ) {
			tag = ":gfs:v_naz/v_zna"
		}
		else
			assert "Unkown base for " + word + " " + allAffixFlags

		return tag

	}

}
