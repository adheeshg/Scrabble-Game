/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scrabble.gui;

import java.io.*;
import java.util.*;
/**
 *
 * @author Adheesh
 */
public class Verifier{
    ArrayList fList = new ArrayList(), pre2List = new ArrayList(), post2List =  new ArrayList();
    int tWordCount=0;
    String inFilesbase="dict/WL",outFile;
    final String inFile = "dict/TWL.txt";
    final int inFileWordCount = 2500;
    Anagramler ang;
    
    
    public Verifier(int i) throws IOException{
        FileReader in = null;
        try{
            in = new FileReader(inFile);
            String inWord="", outFile;
            int c;
            //char c1;
            
            while((c = in.read())!=-1){
                //c1=(char)c;
                //System.out.print(""+c1);
                if (c==13){
                    putInInFile(inWord);
                    //System.out.println(inWord);
                    inWord="";
                }
                else if (c!=10){
                    //System.out.print("adding to inWord"+c);
                    inWord+= (char) c;
                }
            }
            //System.out.println(inWord);
            
            post2List.add(inWord.substring(0, 2));
            putInInFile(inWord);
            //pre2List.add(inWord.substring(0, 2));
            System.out.println("List of files " + fList);
            System.out.println("List of prefixes " + pre2List);
            System.out.println("List of postfixes " + post2List);
        }
        catch(Exception e){
            System.out.println("ConstructorException" + e.toString());
        }
        finally{
            if (in!=null){in.close();}
        }
    }
    
    public Verifier() throws IOException{
        FileReader in = null;
        int tempWordCount=0;
        in = new FileReader(inFile);
        try{
            
            String inWord="", outFile;
            String[] inWords = new String[inFileWordCount];
            int c;
            //char c1;
            
            while((c = in.read())!=-1){
                //c1=(char)c;
                //System.out.print(""+c1);
                if (c==13){
                    //putInInFile(inWord);
                    //System.out.println(inWord);
                    inWords[tempWordCount]=inWord;
                    tempWordCount++;
                    if(tempWordCount==inFileWordCount){
                        tempWordCount=0;
                        putInInFile(inWords);
                        post2List.add(inWord.substring(0, 2));
                        inWords = new String[inFileWordCount];
                    }
                    inWord="";
                }
                else if (c!=10){
                    //System.out.print("adding to inWord"+c);
                    inWord+= (char) c;
                }
            }
            //System.out.println(inWord);
            
            post2List.add(inWord.substring(0, 2));
            putInInFile(inWords);
            //pre2List.add(inWord.substring(0, 2));
            System.out.println("List of files " + fList);
            System.out.println("List of prefixes " + pre2List);
            System.out.println("List of postfixes " + post2List);
        }
        catch(Exception e){
            System.out.println("ConstructorException" + e.toString());
        }
        finally{
            if (in!=null){in.close();}
        }
    }
    
    
    
    
    
    public void setAng(String ip){
        ang = new Anagramler(ip);
    }
    
    public boolean isSubgram(String ip){
        if (ang==null){return false;}
        return ang.isSubGram(ip);
    }

       
    private void putInInFile(String[] inWord) throws IOException{
        outFile = inFilesbase + (int)((tWordCount)/inFileWordCount) + ".txt";
        fList.add(outFile);
        pre2List.add(inWord[0].substring(0, 2));
        //post2List.add(inWord[inFileWordCount-1].substring(0, 2));
        
        FileWriter out=null;       
        
        try{
            
            out = new FileWriter(outFile,false);
            for(int ind=0; ind<inFileWordCount; ind++){
                //System.out.println(inWord);
                if(inWord[ind]==null){
                    break;
                }
                out.write(inWord[ind]);
                out.write("\r\n");
                tWordCount++;
            }
        }
        catch(Exception e){
            System.out.println("InputFileCreationException" + e.toString() + outFile);
        }
        finally{
            if (out!=null){out.close();}
        }    
    }
    
    private void putInInFile(String inWord) throws IOException{
        boolean old_file=true;
        if((int)(tWordCount%inFileWordCount)==0){
            /*if((int)(tWordCount/inFileWordCount)>0){                
                post2List.add(inWord.substring(0, 2));
            }*/
            outFile = inFilesbase + (int)(tWordCount/inFileWordCount) + "O.txt";
            //System.out.println("Put in this file: " + outFile);
            fList.add(outFile);
            old_file=false;
            pre2List.add(inWord.substring(0, 2));
        }   
        if((int)(tWordCount%inFileWordCount)==inFileWordCount-1){
            post2List.add(inWord.substring(0, 2));
        }
        FileWriter out=null;
        
        
        try{
            out = new FileWriter(outFile,old_file);
            //System.out.println(inWord);
            out.write(inWord);
            out.write("\r\n");
        }
        catch(Exception e){
            System.out.println("InputFileCreationException" + e.toString());
        }
        finally{
            if (out!=null){out.close();}
        }    
        
        tWordCount++;
    }
    
    private boolean isWordInFile(String inString, String fName) throws IOException{
        //System.out.println("Searching for "  + inString + " in file " + fName);        
        
        FileReader in = null;
	boolean wFound=false;
        
        try{
            in = new FileReader(fName);
            String inWord="";
            int c;
            
            while((c = in.read())!=-1){
                if (c==13){                   
                    if((wFound = inWord.equals(inString))){break;}
                    //System.out.println("word found = " + inWord + " found = " + wFound);
                    inWord="";
                }
                else if (c!=10){
                    inWord+= (char) c;
                }
            }
			
            wFound = inWord.equals(inString);
            //System.out.println("Word found" + wFound);
        }
        catch(Exception e){
            System.out.println("WordVerifyException" + e.toString());
        }
        finally{
            if (in!=null){in.close();}
        return wFound;
        }
    }
    
    public boolean verify(String inString) throws IOException{
        int i;
        for(i=0;i<pre2List.size() && inString.compareTo(pre2List.get(i).toString()) > 0;i++){
            /*System.out.println(" i= " + i 
                               + " prefix = " + pre2List.get(i) + " compare= " 
                               + inString.compareTo(pre2List.get(i).toString())
                               + " postfix = " + post2List.get(i) + " compare= " 
                               + inString.compareTo(post2List.get(i).toString())  );*/
            if ((inString.substring(0, 2).compareTo(pre2List.get(i).toString()) >= 0) 
                    && (inString.substring(0, 2).compareTo(post2List.get(i).toString()) <= 0)  ){
                
                /*System.out.println("Searching for input here");
                System.out.println("input= " + inString + " i= " + i 
                               + " prefix = " + pre2List.get(i) + " compare= " 
                               + inString.compareTo(pre2List.get(i).toString())
                               + " postfix = " + post2List.get(i) + " compare= " 
                               + inString.compareTo(post2List.get(i).toString())  );*/
                if(isWordInFile(inString,fList.get(i).toString())){return true;}
            }
        }
        /*
        i--;
        System.out.println("Searching for "  + inString + " in file " + fList.get(i) 
                            + " that starts from " + pre2List.get(i));
        
        
        FileReader in = null;
	boolean wFound=false;
        
        try{
            in = new FileReader(fList.get(i).toString());
            String inWord="";
            int c;
            
            while((c = in.read())!=-1 && !(wFound)){
                if (c==13){
                    wFound = inWord.equals(inString);
                    inWord="";
                }
                else if (c!=10){
                    inWord+= (char) c;
                }
            }
			
            wFound = inWord.equals(inString);
            System.out.println("Word found" + wFound);
        }
        catch(Exception e){
            System.out.println("WordVerifyException" + e.toString());
        }
        finally{
            if (in!=null){in.close();}
        }*/
        
        return false;
    }
    
    public void printAllInFile(String fName,ArrayList wordList) throws IOException{
        FileReader in = null;
        
        try{
            in = new FileReader(fName);
            String inWord="";
            int c;
            
            while((c = in.read())!=-1){
                if (c==13){                   
                    //if((wFound = inWord.equals(inString))){break;}
                    //System.out.println("word found = " + inWord + " found = " + wFound);
                    if(ang.isSubGram(inWord)){
                        //System.out.println(inWord);
                        wordList.add(inWord);
                    }
                    inWord="";
                }
                else if (c!=10){
                    inWord+= (char) c;
                }
            }
			
            if(ang.isSubGram(inWord)){
                //System.out.println(inWord);
                wordList.add(inWord);
            }
            //System.out.println("Word found" + wFound);
        }
        catch(Exception e){
            System.out.println("WordVerifyException" + e.toString());
        }
        finally{
            if (in!=null){in.close();}
        }
    }
    
    
    public void printWordsInFile(String fName, ArrayList wL, String ip)  throws IOException{
        FileReader in = null;
        
        try{
            in = new FileReader(fName);
            String inWord="";
            int c;
            
            while((c = in.read())!=-1){
                if (c==13){  
                    if(ang.isSubGram(inWord,0)){wL.add(inWord);}
                    inWord="";
                }
                else if (c!=10){
                    inWord+= (char) c;
                }
            }

        }
        catch(Exception e){
            System.out.println("WordVerifyException" + e.toString());
        }
        finally{
            if (in!=null){in.close();}
        }        
    }
    
    
    public void printAllInFile(String fName,ArrayList wordList, char ch) throws IOException{
        FileReader in = null;
        
        try{
            in = new FileReader(fName);
            String inWord="";
            int c;
            
            while((c = in.read())!=-1){
                if (c==13){                   
                    //if((wFound = inWord.equals(inString))){break;}
                    //System.out.println("word found = " + inWord + " found = " + wFound);
                    if(ang.isSubGram(inWord,ch)){
                        //System.out.println(inWord);
                        wordList.add(inWord);
                    }
                    inWord="";
                }
                else if (c!=10){
                    inWord+= (char) c;
                }
            }
			
            if(ang.isSubGram(inWord)){
                //System.out.println(inWord);
                wordList.add(inWord);
            }
            //System.out.println("Word found" + wFound);
        }
        catch(Exception e){
            System.out.println("WordVerifyException" + e.toString());
        }
        finally{
            if (in!=null){in.close();}
        }
    }
        
    public void printAll(ArrayList wordList) throws IOException{
        for(int i=0;i<pre2List.size();i++){
            if(ang.isSubGram(pre2List.get(i).toString().substring(0,1)) || ang.isSubGram(post2List.get(i).toString().substring(0,1))){
                //System.out.println(fList.get(i) + " " + pre2List.get(i) + " " + post2List.get(i));
                printAllInFile(fList.get(i).toString(),wordList);
            }
        }
        
    }
    
    public void printAllWords(String ip, ArrayList wL) throws IOException{
        setAng(ip);
        for(int i=0;i<pre2List.size();i++){
            //if(ang.isSubGram(pre2List.get(i).toString().substring(0,1)) || ang.isSubGram(post2List.get(i).toString().substring(0,1))){
                //System.out.println(fList.get(i) + " " + pre2List.get(i) + " " + post2List.get(i));
                printWordsInFile(fList.get(i).toString(),wL,ip);
            //}
        }
        
    }
    
    public void printAll(ArrayList wordList, char c) throws IOException{
        for(int i=0;i<pre2List.size();i++){
            if(ang.isSubGram(pre2List.get(i).toString().substring(0,1)) || ang.isSubGram(post2List.get(i).toString().substring(0,1))){
                //System.out.println(fList.get(i) + " " + pre2List.get(i) + " " + post2List.get(i));
                printAllInFile(fList.get(i).toString(),wordList,c);
            }
        }
        
    }
    
    public void getAllWords1 (String ip, ArrayList wordList) throws IOException{
        setAng(ip);
        printAll(wordList);
    }
    
    
    public void getAllWords (String ip, ArrayList[] wordList) throws IOException{
        if (ip == null){return;}
        if (ip.length()==0){return;}
        if (wordList.length != 26){return;}
        setAng(ip);
        ArrayList bList = new ArrayList();
        printAll(bList);
        boolean[] isCharPre = {false,false,false,false,false,
                                false,false,false,false,false,
                                false,false,false,false,false,
                                false,false,false,false,false,
                                false,false,false,false,false,false};
        int ref;
        for (int i=0; i<ip.length(); i++){
            ref = ip.charAt(i) - 'A';
            isCharPre[ref]=true;                        
        }
        for (char i='A';i<='Z';i++){
            String pip = ip + i;
            setAng(pip);
            ref = i-'A';
            wordList[ref] = new ArrayList();
            printAll(wordList[ref],i);
            if(isCharPre[ref]){wordList[ref].addAll(bList);}
        }
        
        
    }
    public static void main(String[] args) throws IOException,InterruptedException{
        System.out.println((new Date()).toString());
        Verifier v = new Verifier();
        System.out.println("Dictionary Load Complete");
        System.out.println((new Date()).toString());        
        Thread.sleep(10000);
        //System.out.println((new Date()).toString());
        System.out.println("Nasal found =" + v.verify("NASAL"));
        //System.out.println((new Date()).toString());
        System.out.println("Nasala found =" + v.verify("NASALA"));
        //System.out.println((new Date()).toString());
        System.out.println("Retry found =" + v.verify("RETRY"));
        //System.out.println((new Date()).toString());
        //v.verify("RETRY");
        //v.verify("AAAA");
        //v.verify("ZZZZ");
        System.out.println("AAAA found =" + v.verify("AAAA"));
        System.out.println("ZZZ found =" + v.verify("ZZZ"));
        
        //v.setAng("FAGGOT");
        System.out.println("TAG is subgram:" + v.isSubgram("FAT"));
        System.out.println((new Date()).toString());
        ArrayList[] wordList1 = new ArrayList[26];
        ArrayList wordList;
        v.getAllWords("GWMHEKTA", wordList1);
        System.out.println((new Date()).toString());
        for(int j=0;j<26;j++){
            wordList = wordList1[j];
            for (int i=0; wordList!=null && i<wordList.size(); i++){
                System.out.print(wordList.get(i) + ",");
            }
            System.out.println();
        }
        
        System.out.println((new Date()).toString());
        System.out.println("Printing all words");
        ArrayList wL = new ArrayList();
        v.printAllWords("GWMHEKTA",wL);
        System.out.println("all words = " + wL.size());
        System.out.println((new Date()).toString());
            for (int i=0; wL!=null && i<wL.size(); i++){
                System.out.print(wL.get(i) + ",");
            }        
        
    }
    
}
