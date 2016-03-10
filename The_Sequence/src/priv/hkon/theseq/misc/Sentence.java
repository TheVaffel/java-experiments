package priv.hkon.theseq.misc;

import priv.hkon.theseq.sprites.Sprite;
import priv.hkon.theseq.world.Village;

public class Sentence {
	
	public static final int TYPE_STATEMENT = 0;
	public static final int TYPE_QUESTION = 1;
	
	public static final int STATEMENT_NEGATIVE = 0;
	public static final int STATEMENT_NEUTRAL = 1;
	public static final int STATEMENT_POSITIVE = 2;
	
	public static final int QUESTION_SELF_CONDITION = 0;
	public static final int QUESTION_RELATION_TO = 1;
	public static final int QUESTION_MEANING_OF_LIFE = 2;
	
	public static Village village;
	
	public static final String[][] STATEMENTS = {
			{"**o smells pretty bad","**o looks like a dead toad", "I heard that **o is actually a demon"},
			{"**o is just a normal humen", "Seems to be reasonable enough", "Hasn't seen **o much lately"},
			{"**o is really cool! I like em!", "**o is just amazing", "That humen has the look of a god!"}
	};
	
	public static final String[][] QUESTIONS = {
			{"How are you?", "How is your life by now?", "Are you having a good time?"},
			{"Honestly, what do you think of **s?", "How is **s in your eyes?" , "If you were to choose between a night with **s or a day with Hitler, what would you do?"},
			{"What is the meaning of our existence anyway?", "Is there more out there, or is this village all the world has got?", "WHY ARE WE HERE?"}
	};
	
	String[] strings;
	
	public static final float[] meaningWeight = {-1.0f, 0.0f, 1.0f};
	
	Sprite object;
	Sprite subject;
	
	int arg2, type; // The role of arg2 depends fully on the value of type
	
	public Sentence(Sprite o, int type, int arg2, Sprite s){
		object = o;
		subject = s;
		
		this.arg2 = arg2;
		this.type = type;
		
		switch(type){
			case TYPE_STATEMENT: {
				strings = new String[1];
				strings[0] = STATEMENTS[arg2][Sprite.RAND.nextInt(STATEMENTS[arg2].length)];
				break;
			}
			case TYPE_QUESTION: {
				strings = new String[1];
				strings[0] = QUESTIONS[arg2][Sprite.RAND.nextInt(STATEMENTS[arg2].length)];
				break;
			}
		}
	}
	
	public int getArg2(){
		return arg2;
	}
	
	public Sprite getSubject(){
		return subject;
	}
	
	public Sprite getObject(){
		return object;
	}
	
	public String parse(String s){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i< s.length(); i++){
			if(s.charAt(i)== '*'){
				if(i < s.length() -2&& s.charAt(i + 1) == '*'){
					switch(s.charAt(i + 2)){
					case 'o': sb.append("the other guy");//TODO: Add something useful here
					}
					continue;
				}
			}
			sb.append(s.charAt(i));
		}
		return sb.toString();
	}
	
	public String[] getStrings(){
		return strings;
	}
	

}
