package com.groupsinc.wordnetd;

// coordinate: X is a coordinate of Y, if they share the same hypernym Z.

import java.io.File;
import java.util.*;
import edu.mit.jwi.*;
import edu.mit.jwi.data.*;
import edu.mit.jwi.item.*;

public final class Wordnet {
	
	IRAMDictionary dict;
	
	public Wordnet() {
		File wnDir = new File("dict");
		this.load(wnDir);
	}
	
	public Wordnet(String wnDirname) {
		File wnDir = new File(wnDirname);
		this.load(wnDir);
	}
	
	private void load(File wnDir) {
		try {
		
			// construct the dictionary object and open it
			this.dict = new RAMDictionary ( wnDir , ILoadPolicy.NO_LOAD );
			dict.open ();
				
				
			// now load into memory
			System.out.println("Loading Wordnet into memory....");
			dict.load (true);
			System.out.println(" done") ;
			
		} catch(Exception e) {
			System.err.println(e.toString()+" "+e.getMessage());
		}
	}
	
	
	private List<String> unique(List<String> clist) {
		HashSet hs = new HashSet();
		hs.addAll(clist);
		clist.clear();
		clist.addAll(hs);
		return clist;
	}
	
	public List<String> getSynonyms(String word) {
		return this.getSynonyms(word, "");
	}
	
	public List<String> getSynonyms(String word, String type) {
		List < String > synonyms = new ArrayList<String>();
		List<IIndexWord> wordIDs;
		wordIDs = this.getWordIDs(word, type);
		List<ISynset> synsets = this.getSynsets(wordIDs);
		for( ISynset synset : synsets ) {
			for( IWord w : synset . getWords () )
				synonyms.add(w.getLemma());
		}
		return unique(synonyms);
	}
	
	public List<String> getHypernyms(String word) {
		return this.getHypernyms(word, "");
	}
	
	public List<String> getHypernyms(String word, String type) {
		List < String > to_return = new ArrayList<String>();
		List<IIndexWord> wordIDs = this.getWordIDs(word, type);
		List<ISynset> synsets = this.getSynsets(wordIDs);
		for( ISynset synset : synsets ) {
			List < ISynsetID > hypernyms = synset . getRelatedSynsets ( Pointer . HYPERNYM ); 
			List < IWord > words ;
			for( ISynsetID sid : hypernyms ) {
				words = dict . getSynset ( sid ) . getWords () ;
				for( Iterator < IWord > i = words . iterator () ; i . hasNext () ;) {
					to_return.add(i . next () . getLemma ());
				}
			}
		}
		return unique(to_return);
	}
	

	
	
	
	private List<ISynset> getSynsets(List<IIndexWord> idxWords) {
		
		List<ISynset> synsets = new ArrayList<ISynset>();
		
		for(IIndexWord idxWord: idxWords) {
			
			if(idxWord!=null) {
					
				List<IWordID> idxList = idxWord . getWordIDs () ;
				
				for(IWordID wordID: idxList) {
					
					IWord word = this.dict . getWord ( wordID ) ;
					ISynset synset = word . getSynset () ;
					synsets.add(synset);
							
				}
			
			}
					
		}
		
		return synsets;
	}
	
	
	private List<IIndexWord> getWordIDs ( String word, String type ) {

		//get the synset
		List<IIndexWord> idxWords = new ArrayList<IIndexWord>();
		if(type=="noun")
			idxWords.add(this.dict.getIndexWord (word, POS.NOUN ));
		else if(type=="verb")
			idxWords.add(this.dict.getIndexWord (word, POS.VERB ));
		else
			return this.getWordIDs(word);
		
		return idxWords;
		
	}
	
	private List<IIndexWord> getWordIDs ( String word, boolean short_form ) {

		//get the synset
		List<IIndexWord> idxWords = new ArrayList<IIndexWord>();
		idxWords.add(this.dict.getIndexWord (word, POS.NOUN ));
		idxWords.add(this.dict.getIndexWord (word, POS.VERB ));
		if(!short_form) {
			idxWords.add(this.dict.getIndexWord (word, POS.ADJECTIVE ));
			idxWords.add(this.dict.getIndexWord (word, POS.ADVERB ));
		}
		
		return idxWords;
		
	}
	
	private List<IIndexWord> getWordIDs ( String word ) {
		return this.getWordIDs(word, false);
	}
	
	public List<String> getPolysemy(String word) {
		List<String> hs = new ArrayList<String>();
		List<IIndexWord> wordIDs = this.getWordIDs(word);
		int i = 0;
		String s = new String();
		String types[] = {"Noun: ", "Verb: ", "Adjective: ", "Adverb: "};
		for(IIndexWord wordID: wordIDs) {
			s = types[i];
			if(wordID==null) {
				s+="0";
			}
			else {
				s+= wordID.getTagSenseCount()+1;
			}
			hs.add(s);
			i++;
		}
		return hs;
	}
	
	public String getTopPolysemy(String word) {
		List<IIndexWord> wordIDs = this.getWordIDs(word, true);
		IIndexWord noun = wordIDs.get(0);
		IIndexWord verb = wordIDs.get(1);
		if(noun==null&&verb!=null)
			return "verb";
		else if(noun!=null&&verb==null)
			return "noun";
		else if(noun==null&&verb==null)
			return "";
		else {
			int nounTagSenseCount = noun.getTagSenseCount();
			int verbTagSenseCount = verb.getTagSenseCount();
			if(nounTagSenseCount>verbTagSenseCount)
				return "noun";
			else if(verbTagSenseCount>nounTagSenseCount)
				return "verb";
			else
				return "";
		}
	}
	
}
