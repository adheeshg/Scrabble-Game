/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scrabble.gui;

/**
 *
 * @author Adheesh
 */
public class Anagramler {
    int[] main_hash,sub_hash;
    int main_len=0;
    public Anagramler(){
        this("");
    }

    public Anagramler(String ip){
        int ref = 0;
        ip = ip.toUpperCase();
        main_hash = new int[] { 0,0,0,0,0,
                                0,0,0,0,0,
                                0,0,0,0,0,
                                0,0,0,0,0,
                                0,0,0,0,0,0};
        for (int i=0; i<ip.length(); i++){
            ref = ip.charAt(i) - 'A';
            main_hash[ref]++;            
        }
        main_len = ip.length();
    }
    
    
    public boolean isSubGram(String ip){
        if (main_hash == null){return false;}
        if (ip == null){return false;}
        if (ip.length()==0){return false;}
        if(main_len<ip.length()){return false;}
        sub_hash = new int[] { 0,0,0,0,0,
                                0,0,0,0,0,
                                0,0,0,0,0,
                                0,0,0,0,0,
                                0,0,0,0,0,0};
        int ref = 0;
        ip = ip.toUpperCase();
        for (int i=0; i<ip.length(); i++){
            ref = ip.charAt(i) - 'A';
            sub_hash[ref]++;            
        }
        for (int i=0; i<26; i++){
            if(sub_hash[i] > main_hash[i]){return false;}
        }
        return true;
    }
    
    public boolean isSubGram(String ip, int a){
        if(a!=0){return false;}
        if (main_hash == null){return false;}
        if (ip == null){return false;}
        if (ip.length()==0){return false;}
        if((main_len + 1)<ip.length()){return false;}
        int count=0;
        sub_hash = new int[] { 0,0,0,0,0,
                                0,0,0,0,0,
                                0,0,0,0,0,
                                0,0,0,0,0,
                                0,0,0,0,0,0};
        int ref = 0;
        ip = ip.toUpperCase();
        for (int i=0; i<ip.length(); i++){
            ref = ip.charAt(i) - 'A';
            sub_hash[ref]++;            
        }
        for (int i=0; i<26; i++){
            if(sub_hash[i] > main_hash[i]){count += sub_hash[i] - main_hash[i];}
        }
        if(count>0){return false;}
        return true;
    }
    
    public boolean isSubGram(String ip, char c){
        if(c<'A' || c>'Z'){return false;}
        if (main_hash == null){return false;}
        if (ip == null){return false;}
        if (ip.length()==0){return false;}
        if(main_len<ip.length()){return false;}
        sub_hash = new int[] { 0,0,0,0,0,
                                0,0,0,0,0,
                                0,0,0,0,0,
                                0,0,0,0,0,
                                0,0,0,0,0,0};
        int ref = 0;
        ip = ip.toUpperCase();
        for (int i=0; i<ip.length(); i++){
            ref = ip.charAt(i) - 'A';
            sub_hash[ref]++;            
        }
        ref = c - 'A';
        if(main_hash[ref]>0 && sub_hash[ref] == 0){return false;}
        for (int i=0; i<26; i++){
            if(sub_hash[i] > main_hash[i]){return false;}
        }
        return true;
    }
    
    public static void main(String[] args){
        /*Anagramler ang = new Anagramler("FAGGOT");
        System.out.println("TAG is subgram:" + ang.isSubGram("TAG",'F'));*/
    }
    
}
