/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scrabble.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DragSourceMotionListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.SwingWorker;

/**
 *
 * @author Adheesh
 */
public class MyScrabble1DD extends javax.swing.JFrame {

        Verifier verifier;
    Timer playTime,startTime,cTime,cTime1;
    ActionListener timerAction,startTimerAction, cTimerAction,cTimerAction1;
    int maxTime = 150, cPlayDelay=10;
    int fin_difficulty=8 ,difficulty=25,currPlayer = 2, moveNum = 0, currScore = 0, playerScore[] = {0,0};
    //javax.swing.JButton jButtonRackSelected, jButtonBoardSelected;
    Boolean centerFilled = false;//, rackButtonSelected = false, boardButtonSelected = false;
    javax.swing.JButton[] CurrentButtonList;
    ArrayList<DraggableButton> tList = new ArrayList<DraggableButton>();
    ArrayList<DraggableButton> fList = new ArrayList<DraggableButton>();
    javax.swing.JButton[] PButtonList = new DraggableButton[8];
    javax.swing.JButton[] CButtonList = new DraggableButton[8];  
    boolean cCanPlay=false;
    char B1Temp = '^', B2Temp='_', B1Fin = '^', B2Fin='_';    
    
    DraggableButton[][] GameBoard ;
            
    private char letters[] = {
                      'A','A','A','A','A','A','A','A','A','B',
                      'B','C','C','D','D','D','D','E','E','E',
                      'E','E','E','E','E','E','E','E','E','F',
                      'F','G','G','G','H','H','I','I','I','I',
                      'I','I','I','I','I','J','K','L','L','L',
                      //'_','_','_','_','_','_','_','_','_','_',
                      //'_','_','_','_','_','_','_','_','_','_',
                      //'_','_','_','_','_','_','_','_','_','_',
                      //'^','^','^','^','^','^','^','^','^','^',
                      //'^','^','^','^','^','^','^','^','^','^'                      
                      'L','M','M','N','N','N','N','N','N','O',
                      'O','O','O','O','O','O','O','P','P','Q',
                      'R','R','R','R','R','R','S','S','S','S',
                      'T','T','T','T','T','T','U','U','U','U',
                      'V','V','W','W','X','Y','Y','Z','A','E'//'^','_'
                      
    }; 
    
    private char[][] finalBoard = {
        {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '}        
    };
    
    private int [] letterValue = {
        1,3,3,2,1,//abcde
        4,3,4,1,8,//fghij
        5,1,3,1,1,3,//klmnop
        10,1,1,1,1,//qrstu
        4,4,8,4,10,//vwxyz        
        0,0,0,0,0//[\]^_
    };
    
    private int[][] letterMult = {
        {1,1,1,2,1,1,1,1,1,1,1,2,1,1,1},
        {1,1,1,1,1,3,1,1,1,3,1,1,1,1,1},
        {1,1,1,1,1,1,2,1,2,1,1,1,1,1,1},
        {2,1,1,1,1,1,1,2,1,1,1,1,1,1,2},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,3,1,1,1,3,1,1,1,3,1,1,1,3,1},
        {1,1,2,1,1,1,2,1,2,1,1,1,2,1,1},
        {1,1,1,2,1,1,1,1,1,1,1,2,1,1,1},
        {1,1,2,1,1,1,2,1,2,1,1,1,2,1,1},
        {1,3,1,1,1,3,1,1,1,3,1,1,1,3,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {2,1,1,1,1,1,1,2,1,1,1,1,1,1,2},
        {1,1,1,1,1,1,2,1,2,1,1,1,1,1,1},
        {1,1,1,1,1,3,1,1,1,3,1,1,1,1,1},
        {1,1,1,2,1,1,1,1,1,1,1,2,1,1,1}         
    };
    
        private int[][] wordMult = {
        {3,1,1,1,1,1,1,3,1,1,1,1,1,1,3},
        {1,2,1,1,1,1,1,1,1,1,1,1,1,2,1},
        {1,1,2,1,1,1,1,1,1,1,1,1,2,1,1},
        {1,1,1,2,1,1,1,1,1,1,1,2,1,1,1},
        {1,1,1,1,2,1,1,1,1,1,2,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {3,1,1,1,1,1,1,2,1,1,1,1,1,1,3},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,2,1,1,1,1,1,2,1,1,1,1},
        {1,1,1,2,1,1,1,1,1,1,1,2,1,1,1},
        {1,1,2,1,1,1,1,1,1,1,1,1,2,1,1},
        {1,2,1,1,1,1,1,1,1,1,1,1,1,2,1},
        {3,1,1,1,1,1,1,3,1,1,1,1,1,1,3}          
    };
        
    private char[][] tempBoard;
    
    private int endLetters = 99;
    private final GhostGlassPane glassPane;
    /**
     * Creates new form MyScrabble1DD
     */
    public MyScrabble1DD() {
        glassPane = new GhostGlassPane( );
	setGlassPane(glassPane);
        addComponentListener(glassPane);
        initComponents();
        timerAction = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                if(jProgressBarTimer.getValue()>0){
                    jProgressBarTimer.setValue(jProgressBarTimer.getValue()-1);
                    jLabelTimer.setText(String.valueOf(jProgressBarTimer.getValue()));
                }
                else{
                    changePlayer();
                    playTime.restart();                    
                }
            }
        };   
        playTime = new Timer(1000,timerAction);
        
        
        cTimerAction = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                if(cPlayDelay>0){
                    cPlayDelay--;
                }
                else{
                    System.out.println("The timer ends");
                    cTime.stop(); 
                    SwingWorker w = getCPlayWorker();
                    w.execute();                    
                }
            }
        };
        
        cTimerAction1 = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                if(cPlayDelay>0){
                    cPlayDelay--;
                }
                else{
                    //System.out.println("The timer1 ends");
                    cTime1.stop();                    
                    changePlayer();
                }
            }
        };        
        cTime = new Timer(1000,cTimerAction);
        cTime1 = new Timer(1000,cTimerAction1);
        
        javax.swing.JButton[] CButtonList1 = {jButtonCPlay, jButtonCR1, jButtonCR2, jButtonCR3, jButtonCR4, jButtonCR5, jButtonCR6, jButtonCR7 };
        CButtonList = CButtonList1;
        javax.swing.JButton[] PButtonList1 = {jButtonPPlay, jButtonPR1, jButtonPR2, jButtonPR3, jButtonPR4, jButtonPR5, jButtonPR6, jButtonPR7 };
        PButtonList = PButtonList1;
        CurrentButtonList = CButtonList;
        jButtonLetterB1.setEnabled(false);
        jButtonLetterB1.setForeground(Color.gray);
        jButtonLetterB2.setEnabled(false);
        jButtonLetterB2.setForeground(Color.gray);
        jLabelEmptyTileStatus.setVisible(false);
        jLabelStartGame.setVisible(false);
        DraggableButton[][] GameBoard1 ={
            {jButtonA1,jButtonB1,jButtonC1,jButtonD1,jButtonE1,jButtonF1,jButtonG1,jButtonH1,jButtonI1,jButtonJ1,jButtonK1,jButtonL1,jButtonM1,jButtonN1,jButtonO1},
            {jButtonA2,jButtonB2,jButtonC2,jButtonD2,jButtonE2,jButtonF2,jButtonG2,jButtonH2,jButtonI2,jButtonJ2,jButtonK2,jButtonL2,jButtonM2,jButtonN2,jButtonO2},
            {jButtonA3,jButtonB3,jButtonC3,jButtonD3,jButtonE3,jButtonF3,jButtonG3,jButtonH3,jButtonI3,jButtonJ3,jButtonK3,jButtonL3,jButtonM3,jButtonN3,jButtonO3},
            {jButtonA4,jButtonB4,jButtonC4,jButtonD4,jButtonE4,jButtonF4,jButtonG4,jButtonH4,jButtonI4,jButtonJ4,jButtonK4,jButtonL4,jButtonM4,jButtonN4,jButtonO4},
            {jButtonA5,jButtonB5,jButtonC5,jButtonD5,jButtonE5,jButtonF5,jButtonG5,jButtonH5,jButtonI5,jButtonJ5,jButtonK5,jButtonL5,jButtonM5,jButtonN5,jButtonO5},
            {jButtonA6,jButtonB6,jButtonC6,jButtonD6,jButtonE6,jButtonF6,jButtonG6,jButtonH6,jButtonI6,jButtonJ6,jButtonK6,jButtonL6,jButtonM6,jButtonN6,jButtonO6},
            {jButtonA7,jButtonB7,jButtonC7,jButtonD7,jButtonE7,jButtonF7,jButtonG7,jButtonH7,jButtonI7,jButtonJ7,jButtonK7,jButtonL7,jButtonM7,jButtonN7,jButtonO7},
            {jButtonA8,jButtonB8,jButtonC8,jButtonD8,jButtonE8,jButtonF8,jButtonG8,jButtonH8,jButtonI8,jButtonJ8,jButtonK8,jButtonL8,jButtonM8,jButtonN8,jButtonO8},
            {jButtonA9,jButtonB9,jButtonC9,jButtonD9,jButtonE9,jButtonF9,jButtonG9,jButtonH9,jButtonI9,jButtonJ9,jButtonK9,jButtonL9,jButtonM9,jButtonN9,jButtonO9},
            {jButtonA10,jButtonB10,jButtonC10,jButtonD10,jButtonE10,jButtonF10,jButtonG10,jButtonH10,jButtonI10,jButtonJ10,jButtonK10,jButtonL10,jButtonM10,jButtonN10,jButtonO10},
            {jButtonA11,jButtonB11,jButtonC11,jButtonD11,jButtonE11,jButtonF11,jButtonG11,jButtonH11,jButtonI11,jButtonJ11,jButtonK11,jButtonL11,jButtonM11,jButtonN11,jButtonO11},
            {jButtonA12,jButtonB12,jButtonC12,jButtonD12,jButtonE12,jButtonF12,jButtonG12,jButtonH12,jButtonI12,jButtonJ12,jButtonK12,jButtonL12,jButtonM12,jButtonN12,jButtonO12},
            {jButtonA13,jButtonB13,jButtonC13,jButtonD13,jButtonE13,jButtonF13,jButtonG13,jButtonH13,jButtonI13,jButtonJ13,jButtonK13,jButtonL13,jButtonM13,jButtonN13,jButtonO13},
            {jButtonA14,jButtonB14,jButtonC14,jButtonD14,jButtonE14,jButtonF14,jButtonG14,jButtonH14,jButtonI14,jButtonJ14,jButtonK14,jButtonL14,jButtonM14,jButtonN14,jButtonO14},
            {jButtonA15,jButtonB15,jButtonC15,jButtonD15,jButtonE15,jButtonF15,jButtonG15,jButtonH15,jButtonI15,jButtonJ15,jButtonK15,jButtonL15,jButtonM15,jButtonN15,jButtonO15}            
        };  
        GameBoard = GameBoard1;
        playVisibility(false);
        jButtonLetterB1.setVisible(false);
        jButtonLetterB2.setVisible(false);
    }

    private void changePlayer(){
        difficulty = fin_difficulty + (int)(Math.random()*7); 
        //code to find letters played and validate the play
        System.out.println("current player=" + currPlayer);
        formFList();
        if(moveNum>0){
            if(findValidate()){
                // code to update the board
                copyFinalBoard();
                //Code to close update score.
                playerScore[currPlayer-1] += currScore;

            }
            else{
                //reverse the play
                System.out.println("Reversing the play");
                reversePlay();
                moveNum--;
            }
        }
            
        PlayerDisable(CurrentButtonList);
        if (currPlayer==1){
            jLabelPPlayerTScoreVal.setText("" + playerScore[currPlayer-1]);
            jLabelPPlayerCScoreVal.setText("" + currScore);
            jLabelStartGame.setVisible(false);
            currPlayer=2;
            CurrentButtonList = CButtonList;
            cCanPlay=true;
            jButtonPHint.setEnabled(false);
            jButtonPGiveUp.setEnabled(false);
        }
        else if (currPlayer==2){
            jLabelCPlayerTScoreVal.setText("" + playerScore[currPlayer-1]);
            jLabelCPlayerCScoreVal.setText("" + currScore);
            currPlayer=1;
            CurrentButtonList = PButtonList;
            jButtonPHint.setEnabled(true);
            jButtonPGiveUp.setEnabled(true);
        }
        //code to enable the new player
        PlayerEnable(CurrentButtonList);

                //clearSelectedBoardandRackButtons;
       /* if(rackButtonSelected) {
            jButtonRackSelected.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        }
        rackButtonSelected = false;
        if(boardButtonSelected){
            jButtonBoardSelected.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 0)));
        }
        boardButtonSelected = false;*/
        //rseet timer
        jProgressBarTimer.setValue(maxTime);
        jLabelTimer.setText(String.valueOf(jProgressBarTimer.getValue()));
        fillRack();
        tList.clear();
        fList.clear();
        moveNum++;
        if(currPlayer==2 && cCanPlay){
            //playComputer();
            cPlayDelay=1;
            cTime.restart();
        }
        if(moveNum>2){           
            if(!checkGameOver()){
                checkReverse();
            }
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonFillRack = new javax.swing.JButton();
        jLabelCenterTileStatus = new javax.swing.JLabel();
        jLabelLetterRemainlabel = new javax.swing.JLabel();
        jLabelLetterRemainingVal = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButtonPR1 = new DraggableButton(glassPane);
        jButtonPR2 = new DraggableButton(glassPane);
        jButtonPR3 = new DraggableButton(glassPane);
        jButtonPR4 = new DraggableButton(glassPane);
        jButtonPR5 = new DraggableButton(glassPane);
        jButtonPR7 = new DraggableButton(glassPane);
        jButtonPR6 = new DraggableButton(glassPane);
        jButtonPPlay = new javax.swing.JButton();
        jLabelPPlayerTScore = new javax.swing.JLabel();
        jLabelPPlayerCScore = new javax.swing.JLabel();
        jLabelPPlayerCScoreVal = new javax.swing.JLabel();
        jLabelPPlayerTScoreVal = new javax.swing.JLabel();
        jButtonPHint = new javax.swing.JButton();
        jButtonPGiveUp = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButtonA1 = new DraggableButton(glassPane);
        jButtonA2 = new DraggableButton(glassPane);
        jButtonA3 = new DraggableButton(glassPane);
        jButtonA4 = new DraggableButton(glassPane);
        jButtonA5 = new DraggableButton(glassPane);
        jButtonA6 = new DraggableButton(glassPane);
        jButtonA7 = new DraggableButton(glassPane);
        jButtonB1 = new DraggableButton(glassPane);
        jButtonB2 = new DraggableButton(glassPane);
        jButtonB3 = new DraggableButton(glassPane);
        jButtonB4 = new DraggableButton(glassPane);
        jButtonB5 = new DraggableButton(glassPane);
        jButtonB6 = new DraggableButton(glassPane);
        jButtonB7 = new DraggableButton(glassPane);
        jButtonC1 = new DraggableButton(glassPane);
        jButtonC2 = new DraggableButton(glassPane);
        jButtonC3 = new DraggableButton(glassPane);
        jButtonC4 = new DraggableButton(glassPane);
        jButtonC5 = new DraggableButton(glassPane);
        jButtonD1 = new DraggableButton(glassPane);
        jButtonD2 = new DraggableButton(glassPane);
        jButtonD3 = new DraggableButton(glassPane);
        jButtonD4 = new DraggableButton(glassPane);
        jButtonD8 = new DraggableButton(glassPane);
        jButtonA8 = new DraggableButton(glassPane);
        jButtonB8 = new DraggableButton(glassPane);
        jButtonC8 = new DraggableButton(glassPane);
        jButtonC7 = new DraggableButton(glassPane);
        jButtonD7 = new DraggableButton(glassPane);
        jButtonD6 = new DraggableButton(glassPane);
        jButtonC6 = new DraggableButton(glassPane);
        jButtonD5 = new DraggableButton(glassPane);
        jButtonH8 = new DraggableButton(glassPane);
        jButtonG8 = new DraggableButton(glassPane);
        jButtonF8 = new DraggableButton(glassPane);
        jButtonE8 = new DraggableButton(glassPane);
        jButtonE7 = new DraggableButton(glassPane);
        jButtonF7 = new DraggableButton(glassPane);
        jButtonG7 = new DraggableButton(glassPane);
        jButtonH7 = new DraggableButton(glassPane);
        jButtonH6 = new DraggableButton(glassPane);
        jButtonG6 = new DraggableButton(glassPane);
        jButtonF6 = new DraggableButton(glassPane);
        jButtonE6 = new DraggableButton(glassPane);
        jButtonE5 = new DraggableButton(glassPane);
        jButtonF5 = new DraggableButton(glassPane);
        jButtonG5 = new DraggableButton(glassPane);
        jButtonH5 = new DraggableButton(glassPane);
        jButtonH4 = new DraggableButton(glassPane);
        jButtonG4 = new DraggableButton(glassPane);
        jButtonF4 = new DraggableButton(glassPane);
        jButtonE4 = new DraggableButton(glassPane);
        jButtonE3 = new DraggableButton(glassPane);
        jButtonF3 = new DraggableButton(glassPane);
        jButtonG3 = new DraggableButton(glassPane);
        jButtonH3 = new DraggableButton(glassPane);
        jButtonH2 = new DraggableButton(glassPane);
        jButtonG2 = new DraggableButton(glassPane);
        jButtonF2 = new DraggableButton(glassPane);
        jButtonE2 = new DraggableButton(glassPane);
        jButtonE1 = new DraggableButton(glassPane);
        jButtonF1 = new DraggableButton(glassPane);
        jButtonG1 = new DraggableButton(glassPane);
        jButtonH1 = new DraggableButton(glassPane);
        jButtonA15 = new DraggableButton(glassPane);
        jButtonB15 = new DraggableButton(glassPane);
        jButtonC15 = new DraggableButton(glassPane);
        jButtonD15 = new DraggableButton(glassPane);
        jButtonE15 = new DraggableButton(glassPane);
        jButtonF15 = new DraggableButton(glassPane);
        jButtonG15 = new DraggableButton(glassPane);
        jButtonH15 = new DraggableButton(glassPane);
        jButtonH14 = new DraggableButton(glassPane);
        jButtonG14 = new DraggableButton(glassPane);
        jButtonF14 = new DraggableButton(glassPane);
        jButtonE14 = new DraggableButton(glassPane);
        jButtonD14 = new DraggableButton(glassPane);
        jButtonC14 = new DraggableButton(glassPane);
        jButtonB14 = new DraggableButton(glassPane);
        jButtonA14 = new DraggableButton(glassPane);
        jButtonA13 = new DraggableButton(glassPane);
        jButtonB13 = new DraggableButton(glassPane);
        jButtonC13 = new DraggableButton(glassPane);
        jButtonD13 = new DraggableButton(glassPane);
        jButtonE13 = new DraggableButton(glassPane);
        jButtonF13 = new DraggableButton(glassPane);
        jButtonG13 = new DraggableButton(glassPane);
        jButtonH13 = new DraggableButton(glassPane);
        jButtonH12 = new DraggableButton(glassPane);
        jButtonG12 = new DraggableButton(glassPane);
        jButtonF12 = new DraggableButton(glassPane);
        jButtonD12 = new DraggableButton(glassPane);
        jButtonE12 = new DraggableButton(glassPane);
        jButtonC12 = new DraggableButton(glassPane);
        jButtonB12 = new DraggableButton(glassPane);
        jButtonA12 = new DraggableButton(glassPane);
        jButtonA11 = new DraggableButton(glassPane);
        jButtonB11 = new DraggableButton(glassPane);
        jButtonC11 = new DraggableButton(glassPane);
        jButtonD11 = new DraggableButton(glassPane);
        jButtonE11 = new DraggableButton(glassPane);
        jButtonF11 = new DraggableButton(glassPane);
        jButtonG11 = new DraggableButton(glassPane);
        jButtonH11 = new DraggableButton(glassPane);
        jButtonH10 = new DraggableButton(glassPane);
        jButtonG10 = new DraggableButton(glassPane);
        jButtonF10 = new DraggableButton(glassPane);
        jButtonE10 = new DraggableButton(glassPane);
        jButtonD10 = new DraggableButton(glassPane);
        jButtonC10 = new DraggableButton(glassPane);
        jButtonB10 = new DraggableButton(glassPane);
        jButtonA10 = new DraggableButton(glassPane);
        jButtonA9 = new DraggableButton(glassPane);
        jButtonB9 = new DraggableButton(glassPane);
        jButtonC9 = new DraggableButton(glassPane);
        jButtonD9 = new DraggableButton(glassPane);
        jButtonE9 = new DraggableButton(glassPane);
        jButtonF9 = new DraggableButton(glassPane);
        jButtonG9 = new DraggableButton(glassPane);
        jButtonH9 = new DraggableButton(glassPane);
        jButtonN1 = new DraggableButton(glassPane);
        jButtonM1 = new DraggableButton(glassPane);
        jButtonO1 = new DraggableButton(glassPane);
        jButtonL1 = new DraggableButton(glassPane);
        jButtonK1 = new DraggableButton(glassPane);
        jButtonJ1 = new DraggableButton(glassPane);
        jButtonI1 = new DraggableButton(glassPane);
        jButtonI2 = new DraggableButton(glassPane);
        jButtonJ2 = new DraggableButton(glassPane);
        jButtonK2 = new DraggableButton(glassPane);
        jButtonM2 = new DraggableButton(glassPane);
        jButtonL2 = new DraggableButton(glassPane);
        jButtonN2 = new DraggableButton(glassPane);
        jButtonO2 = new DraggableButton(glassPane);
        jButtonO4 = new DraggableButton(glassPane);
        jButtonN4 = new DraggableButton(glassPane);
        jButtonM4 = new DraggableButton(glassPane);
        jButtonL4 = new DraggableButton(glassPane);
        jButtonK4 = new DraggableButton(glassPane);
        jButtonJ4 = new DraggableButton(glassPane);
        jButtonI4 = new DraggableButton(glassPane);
        jButtonI3 = new DraggableButton(glassPane);
        jButtonJ3 = new DraggableButton(glassPane);
        jButtonK3 = new DraggableButton(glassPane);
        jButtonL3 = new DraggableButton(glassPane);
        jButtonM3 = new DraggableButton(glassPane);
        jButtonN3 = new DraggableButton(glassPane);
        jButtonO3 = new DraggableButton(glassPane);
        jButtonO8 = new DraggableButton(glassPane);
        jButtonN8 = new DraggableButton(glassPane);
        jButtonM8 = new DraggableButton(glassPane);
        jButtonL8 = new DraggableButton(glassPane);
        jButtonK8 = new DraggableButton(glassPane);
        jButtonJ8 = new DraggableButton(glassPane);
        jButtonI8 = new DraggableButton(glassPane);
        jButtonI7 = new DraggableButton(glassPane);
        jButtonI6 = new DraggableButton(glassPane);
        jButtonI5 = new DraggableButton(glassPane);
        jButtonJ5 = new DraggableButton(glassPane);
        jButtonJ6 = new DraggableButton(glassPane);
        jButtonJ7 = new DraggableButton(glassPane);
        jButtonK7 = new DraggableButton(glassPane);
        jButtonK6 = new DraggableButton(glassPane);
        jButtonK5 = new DraggableButton(glassPane);
        jButtonL5 = new DraggableButton(glassPane);
        jButtonL6 = new DraggableButton(glassPane);
        jButtonL7 = new DraggableButton(glassPane);
        jButtonM7 = new DraggableButton(glassPane);
        jButtonM6 = new DraggableButton(glassPane);
        jButtonM5 = new DraggableButton(glassPane);
        jButtonN5 = new DraggableButton(glassPane);
        jButtonN6 = new DraggableButton(glassPane);
        jButtonN7 = new DraggableButton(glassPane);
        jButtonO7 = new DraggableButton(glassPane);
        jButtonO6 = new DraggableButton(glassPane);
        jButtonO5 = new DraggableButton(glassPane);
        jButtonO14 = new DraggableButton(glassPane);
        jButtonN14 = new DraggableButton(glassPane);
        jButtonM14 = new DraggableButton(glassPane);
        jButtonL14 = new DraggableButton(glassPane);
        jButtonK14 = new DraggableButton(glassPane);
        jButtonJ14 = new DraggableButton(glassPane);
        jButtonI14 = new DraggableButton(glassPane);
        jButtonI13 = new DraggableButton(glassPane);
        jButtonJ13 = new DraggableButton(glassPane);
        jButtonK13 = new DraggableButton(glassPane);
        jButtonL13 = new DraggableButton(glassPane);
        jButtonM13 = new DraggableButton(glassPane);
        jButtonN13 = new DraggableButton(glassPane);
        jButtonO13 = new DraggableButton(glassPane);
        jButtonO12 = new DraggableButton(glassPane);
        jButtonN12 = new DraggableButton(glassPane);
        jButtonM12 = new DraggableButton(glassPane);
        jButtonL12 = new DraggableButton(glassPane);
        jButtonK12 = new DraggableButton(glassPane);
        jButtonJ12 = new DraggableButton(glassPane);
        jButtonI12 = new DraggableButton(glassPane);
        jButtonI11 = new DraggableButton(glassPane);
        jButtonJ11 = new DraggableButton(glassPane);
        jButtonK11 = new DraggableButton(glassPane);
        jButtonL11 = new DraggableButton(glassPane);
        jButtonM11 = new DraggableButton(glassPane);
        jButtonN11 = new DraggableButton(glassPane);
        jButtonO11 = new DraggableButton(glassPane);
        jButtonO10 = new DraggableButton(glassPane);
        jButtonN10 = new DraggableButton(glassPane);
        jButtonM10 = new DraggableButton(glassPane);
        jButtonL10 = new DraggableButton(glassPane);
        jButtonK10 = new DraggableButton(glassPane);
        jButtonJ10 = new DraggableButton(glassPane);
        jButtonI10 = new DraggableButton(glassPane);
        jButtonI9 = new DraggableButton(glassPane);
        jButtonJ9 = new DraggableButton(glassPane);
        jButtonK9 = new DraggableButton(glassPane);
        jButtonL9 = new DraggableButton(glassPane);
        jButtonM9 = new DraggableButton(glassPane);
        jButtonN9 = new DraggableButton(glassPane);
        jButtonO9 = new DraggableButton(glassPane);
        jButtonO15 = new DraggableButton(glassPane);
        jButtonN15 = new DraggableButton(glassPane);
        jButtonM15 = new DraggableButton(glassPane);
        jButtonL15 = new DraggableButton(glassPane);
        jButtonK15 = new DraggableButton(glassPane);
        jButtonJ15 = new DraggableButton(glassPane);
        jButtonI15 = new DraggableButton(glassPane);
        jLabelTimer = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButtonCR1 = new DraggableButton(glassPane);
        jButtonCR2 = new DraggableButton(glassPane);
        jButtonCR4 = new DraggableButton(glassPane);
        jButtonCR5 = new DraggableButton(glassPane);
        jButtonCR7 = new DraggableButton(glassPane);
        jButtonCR3 = new DraggableButton(glassPane);
        jButtonCR6 = new DraggableButton(glassPane);
        jButtonCPlay = new javax.swing.JButton();
        jLabelCPlayerTScore = new javax.swing.JLabel();
        jLabelCPlayerCScore = new javax.swing.JLabel();
        jLabelCPlayerCScoreVal = new javax.swing.JLabel();
        jLabelCPlayerTScoreVal = new javax.swing.JLabel();
        jLabelStartGame = new javax.swing.JLabel();
        jProgressBarTimer = new javax.swing.JProgressBar();
        jButtonStartGame = new javax.swing.JButton();
        jLabelEmptyTileStatus = new javax.swing.JLabel();
        jButtonLetterB2 = new javax.swing.JButton();
        jButtonLetterB1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(811, 620));

        jButtonFillRack.setText("Fill Racks");
        jButtonFillRack.setOpaque(false);
        jButtonFillRack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFillRackActionPerformed(evt);
            }
        });

        jLabelCenterTileStatus.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        jLabelCenterTileStatus.setText("Center Tile is empty. Fill it.");

        jLabelLetterRemainlabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelLetterRemainlabel.setText("Letters remaining in bag:");

        jLabelLetterRemainingVal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelLetterRemainingVal.setText(String.valueOf(endLetters+1));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Player1", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jPanel5.setPreferredSize(new java.awt.Dimension(297, 206));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Rack"));
        jPanel2.setPreferredSize(new java.awt.Dimension(281, 66));

        jButtonPR1.setBackground(new java.awt.Color(0, 0, 0));
        jButtonPR1.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/blankRack.jpg"))); // NOI18N
        jButtonPR1.setText("jButton226");
        jButtonPR1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonPR1.setPreferredSize(new java.awt.Dimension(28, 28));
        jButtonPR1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPR1ActionPerformed(evt);
            }
        });

        jButtonPR2.setBackground(new java.awt.Color(0, 0, 0));
        jButtonPR2.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/blankRack.jpg"))); // NOI18N
        jButtonPR2.setText("jButton226");
        jButtonPR2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonPR2.setPreferredSize(new java.awt.Dimension(28, 28));
        jButtonPR2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPR2ActionPerformed(evt);
            }
        });

        jButtonPR3.setBackground(new java.awt.Color(0, 0, 0));
        jButtonPR3.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/blankRack.jpg"))); // NOI18N
        jButtonPR3.setText("jButton226");
        jButtonPR3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonPR3.setPreferredSize(new java.awt.Dimension(28, 28));
        jButtonPR3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPR3ActionPerformed(evt);
            }
        });

        jButtonPR4.setBackground(new java.awt.Color(0, 0, 0));
        jButtonPR4.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/blankRack.jpg"))); // NOI18N
        jButtonPR4.setText("jButton226");
        jButtonPR4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonPR4.setPreferredSize(new java.awt.Dimension(28, 28));
        jButtonPR4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPR4ActionPerformed(evt);
            }
        });

        jButtonPR5.setBackground(new java.awt.Color(0, 0, 0));
        jButtonPR5.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/blankRack.jpg"))); // NOI18N
        jButtonPR5.setText("jButton226");
        jButtonPR5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonPR5.setPreferredSize(new java.awt.Dimension(28, 28));
        jButtonPR5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPR5ActionPerformed(evt);
            }
        });

        jButtonPR7.setBackground(new java.awt.Color(0, 0, 0));
        jButtonPR7.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/blankRack.jpg"))); // NOI18N
        jButtonPR7.setText("jButton226");
        jButtonPR7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonPR7.setPreferredSize(new java.awt.Dimension(28, 28));
        jButtonPR7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPR7ActionPerformed(evt);
            }
        });

        jButtonPR6.setBackground(new java.awt.Color(0, 0, 0));
        jButtonPR6.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/blankRack.jpg"))); // NOI18N
        jButtonPR6.setText("jButton226");
        jButtonPR6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonPR6.setPreferredSize(new java.awt.Dimension(28, 28));
        jButtonPR6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPR6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jButtonPR1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jButtonPR2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jButtonPR3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jButtonPR4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jButtonPR5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jButtonPR6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jButtonPR7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonPR5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonPR6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonPR7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonPR3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonPR4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonPR1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonPR2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jButtonPPlay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonPPlay.setText("Play");
        jButtonPPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPPlayActionPerformed(evt);
            }
        });

        jLabelPPlayerTScore.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelPPlayerTScore.setText("Total Score");

        jLabelPPlayerCScore.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelPPlayerCScore.setText("Score for last move");

        jLabelPPlayerCScoreVal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelPPlayerCScoreVal.setText("0");

        jLabelPPlayerTScoreVal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelPPlayerTScoreVal.setText("0");

        jButtonPHint.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonPHint.setText("Hint");
        jButtonPHint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPHintActionPerformed(evt);
            }
        });

        jButtonPGiveUp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonPGiveUp.setText("GiveUp");
        jButtonPGiveUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPGiveUpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButtonPPlay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonPHint)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonPGiveUp)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelPPlayerCScore, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelPPlayerTScore, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelPPlayerTScoreVal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelPPlayerCScoreVal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPPlayerTScore, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPPlayerTScoreVal, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPPlayerCScore, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPPlayerCScoreVal, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonPPlay)
                    .addComponent(jButtonPHint)
                    .addComponent(jButtonPGiveUp)))
        );

        jPanel1.setPreferredSize(new java.awt.Dimension(450, 450));

        jButtonA1.setBackground(new java.awt.Color(0, 0, 0));
        jButtonA1.setForeground(new java.awt.Color(102, 102, 255));
        jButtonA1.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Wtile.jpg"))); // NOI18N
        jButtonA1.setText("empty");
        jButtonA1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonA1.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonA1.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonA1.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonA1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonA1ActionPerformed(evt);
            }
        });

        jButtonA2.setBackground(new java.awt.Color(0, 0, 0));
        jButtonA2.setForeground(new java.awt.Color(102, 102, 255));
        jButtonA2.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonA2.setText("empty");
        jButtonA2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonA2.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonA2.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonA2.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonA2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonA2ActionPerformed(evt);
            }
        });

        jButtonA3.setBackground(new java.awt.Color(0, 0, 0));
        jButtonA3.setForeground(new java.awt.Color(102, 102, 255));
        jButtonA3.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonA3.setText("empty");
        jButtonA3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonA3.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonA3.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonA3.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonA3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonA3ActionPerformed(evt);
            }
        });

        jButtonA4.setBackground(new java.awt.Color(0, 0, 0));
        jButtonA4.setForeground(new java.awt.Color(102, 102, 255));
        jButtonA4.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonA4.setText("empty");
        jButtonA4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonA4.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonA4.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonA4.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonA4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonA4ActionPerformed(evt);
            }
        });

        jButtonA5.setBackground(new java.awt.Color(0, 0, 0));
        jButtonA5.setForeground(new java.awt.Color(102, 102, 255));
        jButtonA5.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonA5.setText("empty");
        jButtonA5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonA5.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonA5.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonA5.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonA5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonA5ActionPerformed(evt);
            }
        });

        jButtonA6.setBackground(new java.awt.Color(0, 0, 0));
        jButtonA6.setForeground(new java.awt.Color(102, 102, 255));
        jButtonA6.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonA6.setText("empty");
        jButtonA6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonA6.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonA6.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonA6.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonA6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonA6ActionPerformed(evt);
            }
        });

        jButtonA7.setBackground(new java.awt.Color(0, 0, 0));
        jButtonA7.setForeground(new java.awt.Color(102, 102, 255));
        jButtonA7.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonA7.setText("empty");
        jButtonA7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonA7.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonA7.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonA7.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonA7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonA7ActionPerformed(evt);
            }
        });

        jButtonB1.setBackground(new java.awt.Color(0, 0, 0));
        jButtonB1.setForeground(new java.awt.Color(102, 102, 255));
        jButtonB1.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonB1.setText("empty");
        jButtonB1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonB1.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonB1.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonB1.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonB1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonB1ActionPerformed(evt);
            }
        });

        jButtonB2.setBackground(new java.awt.Color(0, 0, 0));
        jButtonB2.setForeground(new java.awt.Color(102, 102, 255));
        jButtonB2.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Wtile.jpg"))); // NOI18N
        jButtonB2.setText("empty");
        jButtonB2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonB2.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonB2.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonB2.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonB2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonB2ActionPerformed(evt);
            }
        });

        jButtonB3.setBackground(new java.awt.Color(0, 0, 0));
        jButtonB3.setForeground(new java.awt.Color(102, 102, 255));
        jButtonB3.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonB3.setText("empty");
        jButtonB3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonB3.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonB3.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonB3.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonB3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonB3ActionPerformed(evt);
            }
        });

        jButtonB4.setBackground(new java.awt.Color(0, 0, 0));
        jButtonB4.setForeground(new java.awt.Color(102, 102, 255));
        jButtonB4.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonB4.setText("empty");
        jButtonB4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonB4.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonB4.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonB4.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonB4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonB4ActionPerformed(evt);
            }
        });

        jButtonB5.setBackground(new java.awt.Color(0, 0, 0));
        jButtonB5.setForeground(new java.awt.Color(102, 102, 255));
        jButtonB5.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonB5.setText("empty");
        jButtonB5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonB5.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonB5.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonB5.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonB5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonB5ActionPerformed(evt);
            }
        });

        jButtonB6.setBackground(new java.awt.Color(0, 0, 0));
        jButtonB6.setForeground(new java.awt.Color(102, 102, 255));
        jButtonB6.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Ltile.jpg"))); // NOI18N
        jButtonB6.setText("empty");
        jButtonB6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonB6.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonB6.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonB6.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonB6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonB6ActionPerformed(evt);
            }
        });

        jButtonB7.setBackground(new java.awt.Color(0, 0, 0));
        jButtonB7.setForeground(new java.awt.Color(102, 102, 255));
        jButtonB7.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonB7.setText("empty");
        jButtonB7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonB7.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonB7.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonB7.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonB7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonB7ActionPerformed(evt);
            }
        });

        jButtonC1.setBackground(new java.awt.Color(0, 0, 0));
        jButtonC1.setForeground(new java.awt.Color(102, 102, 255));
        jButtonC1.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonC1.setText("empty");
        jButtonC1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonC1.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonC1.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonC1.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonC1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonC1ActionPerformed(evt);
            }
        });

        jButtonC2.setBackground(new java.awt.Color(0, 0, 0));
        jButtonC2.setForeground(new java.awt.Color(102, 102, 255));
        jButtonC2.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonC2.setText("empty");
        jButtonC2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonC2.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonC2.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonC2.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonC2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonC2ActionPerformed(evt);
            }
        });

        jButtonC3.setBackground(new java.awt.Color(0, 0, 0));
        jButtonC3.setForeground(new java.awt.Color(102, 102, 255));
        jButtonC3.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Wtile.jpg"))); // NOI18N
        jButtonC3.setText("empty");
        jButtonC3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonC3.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonC3.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonC3.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonC3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonC3ActionPerformed(evt);
            }
        });

        jButtonC4.setBackground(new java.awt.Color(0, 0, 0));
        jButtonC4.setForeground(new java.awt.Color(102, 102, 255));
        jButtonC4.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonC4.setText("empty");
        jButtonC4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonC4.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonC4.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonC4.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonC4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonC4ActionPerformed(evt);
            }
        });

        jButtonC5.setBackground(new java.awt.Color(0, 0, 0));
        jButtonC5.setForeground(new java.awt.Color(102, 102, 255));
        jButtonC5.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonC5.setText("empty");
        jButtonC5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonC5.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonC5.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonC5.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonC5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonC5ActionPerformed(evt);
            }
        });

        jButtonD1.setBackground(new java.awt.Color(0, 0, 0));
        jButtonD1.setForeground(new java.awt.Color(102, 102, 255));
        jButtonD1.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonD1.setText("empty");
        jButtonD1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonD1.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonD1.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonD1.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonD1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonD1ActionPerformed(evt);
            }
        });

        jButtonD2.setBackground(new java.awt.Color(0, 0, 0));
        jButtonD2.setForeground(new java.awt.Color(102, 102, 255));
        jButtonD2.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonD2.setText("empty");
        jButtonD2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonD2.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonD2.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonD2.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonD2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonD2ActionPerformed(evt);
            }
        });

        jButtonD3.setBackground(new java.awt.Color(0, 0, 0));
        jButtonD3.setForeground(new java.awt.Color(102, 102, 255));
        jButtonD3.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonD3.setText("empty");
        jButtonD3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonD3.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonD3.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonD3.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonD3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonD3ActionPerformed(evt);
            }
        });

        jButtonD4.setBackground(new java.awt.Color(0, 0, 0));
        jButtonD4.setForeground(new java.awt.Color(102, 102, 255));
        jButtonD4.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Wtile.jpg"))); // NOI18N
        jButtonD4.setText("empty");
        jButtonD4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonD4.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonD4.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonD4.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonD4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonD4ActionPerformed(evt);
            }
        });

        jButtonD8.setBackground(new java.awt.Color(0, 0, 0));
        jButtonD8.setForeground(new java.awt.Color(102, 102, 255));
        jButtonD8.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonD8.setText("empty");
        jButtonD8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonD8.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonD8.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonD8.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonD8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonD8ActionPerformed(evt);
            }
        });

        jButtonA8.setBackground(new java.awt.Color(0, 0, 0));
        jButtonA8.setForeground(new java.awt.Color(102, 102, 255));
        jButtonA8.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Wtile.jpg"))); // NOI18N
        jButtonA8.setText("empty");
        jButtonA8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonA8.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonA8.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonA8.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonA8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonA8ActionPerformed(evt);
            }
        });

        jButtonB8.setBackground(new java.awt.Color(0, 0, 0));
        jButtonB8.setForeground(new java.awt.Color(102, 102, 255));
        jButtonB8.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonB8.setText("empty");
        jButtonB8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonB8.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonB8.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonB8.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonB8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonB8ActionPerformed(evt);
            }
        });

        jButtonC8.setBackground(new java.awt.Color(0, 0, 0));
        jButtonC8.setForeground(new java.awt.Color(102, 102, 255));
        jButtonC8.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonC8.setText("empty");
        jButtonC8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonC8.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonC8.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonC8.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonC8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonC8ActionPerformed(evt);
            }
        });

        jButtonC7.setBackground(new java.awt.Color(0, 0, 0));
        jButtonC7.setForeground(new java.awt.Color(102, 102, 255));
        jButtonC7.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonC7.setText("empty");
        jButtonC7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonC7.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonC7.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonC7.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonC7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonC7ActionPerformed(evt);
            }
        });

        jButtonD7.setBackground(new java.awt.Color(0, 0, 0));
        jButtonD7.setForeground(new java.awt.Color(102, 102, 255));
        jButtonD7.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonD7.setText("empty");
        jButtonD7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonD7.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonD7.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonD7.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonD7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonD7ActionPerformed(evt);
            }
        });

        jButtonD6.setBackground(new java.awt.Color(0, 0, 0));
        jButtonD6.setForeground(new java.awt.Color(102, 102, 255));
        jButtonD6.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonD6.setText("empty");
        jButtonD6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonD6.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonD6.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonD6.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonD6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonD6ActionPerformed(evt);
            }
        });

        jButtonC6.setBackground(new java.awt.Color(0, 0, 0));
        jButtonC6.setForeground(new java.awt.Color(102, 102, 255));
        jButtonC6.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonC6.setText("empty");
        jButtonC6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonC6.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonC6.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonC6.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonC6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonC6ActionPerformed(evt);
            }
        });

        jButtonD5.setBackground(new java.awt.Color(0, 0, 0));
        jButtonD5.setForeground(new java.awt.Color(102, 102, 255));
        jButtonD5.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonD5.setText("empty");
        jButtonD5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonD5.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonD5.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonD5.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonD5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonD5ActionPerformed(evt);
            }
        });

        jButtonH8.setBackground(new java.awt.Color(0, 0, 0));
        jButtonH8.setForeground(new java.awt.Color(102, 102, 255));
        jButtonH8.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Ctile.jpg"))); // NOI18N
        jButtonH8.setText("empty");
        jButtonH8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonH8.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonH8.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonH8.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonH8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonH8ActionPerformed(evt);
            }
        });

        jButtonG8.setBackground(new java.awt.Color(0, 0, 0));
        jButtonG8.setForeground(new java.awt.Color(102, 102, 255));
        jButtonG8.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonG8.setText("empty");
        jButtonG8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonG8.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonG8.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonG8.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonG8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonG8ActionPerformed(evt);
            }
        });

        jButtonF8.setBackground(new java.awt.Color(0, 0, 0));
        jButtonF8.setForeground(new java.awt.Color(102, 102, 255));
        jButtonF8.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonF8.setText("empty");
        jButtonF8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonF8.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonF8.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonF8.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonF8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonF8ActionPerformed(evt);
            }
        });

        jButtonE8.setBackground(new java.awt.Color(0, 0, 0));
        jButtonE8.setForeground(new java.awt.Color(102, 102, 255));
        jButtonE8.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonE8.setText("empty");
        jButtonE8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonE8.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonE8.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonE8.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonE8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonE8ActionPerformed(evt);
            }
        });

        jButtonE7.setBackground(new java.awt.Color(0, 0, 0));
        jButtonE7.setForeground(new java.awt.Color(102, 102, 255));
        jButtonE7.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonE7.setText("empty");
        jButtonE7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonE7.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonE7.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonE7.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonE7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonE7ActionPerformed(evt);
            }
        });

        jButtonF7.setBackground(new java.awt.Color(0, 0, 0));
        jButtonF7.setForeground(new java.awt.Color(102, 102, 255));
        jButtonF7.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonF7.setText("empty");
        jButtonF7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonF7.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonF7.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonF7.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonF7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonF7ActionPerformed(evt);
            }
        });

        jButtonG7.setBackground(new java.awt.Color(0, 0, 0));
        jButtonG7.setForeground(new java.awt.Color(102, 102, 255));
        jButtonG7.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonG7.setText("empty");
        jButtonG7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonG7.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonG7.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonG7.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonG7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonG7ActionPerformed(evt);
            }
        });

        jButtonH7.setBackground(new java.awt.Color(0, 0, 0));
        jButtonH7.setForeground(new java.awt.Color(102, 102, 255));
        jButtonH7.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonH7.setText("empty");
        jButtonH7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonH7.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonH7.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonH7.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonH7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonH7ActionPerformed(evt);
            }
        });

        jButtonH6.setBackground(new java.awt.Color(0, 0, 0));
        jButtonH6.setForeground(new java.awt.Color(102, 102, 255));
        jButtonH6.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonH6.setText("empty");
        jButtonH6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonH6.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonH6.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonH6.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonH6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonH6ActionPerformed(evt);
            }
        });

        jButtonG6.setBackground(new java.awt.Color(0, 0, 0));
        jButtonG6.setForeground(new java.awt.Color(102, 102, 255));
        jButtonG6.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonG6.setText("empty");
        jButtonG6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonG6.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonG6.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonG6.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonG6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonG6ActionPerformed(evt);
            }
        });

        jButtonF6.setBackground(new java.awt.Color(0, 0, 0));
        jButtonF6.setForeground(new java.awt.Color(102, 102, 255));
        jButtonF6.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Ltile.jpg"))); // NOI18N
        jButtonF6.setText("empty");
        jButtonF6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonF6.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonF6.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonF6.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonF6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonF6ActionPerformed(evt);
            }
        });

        jButtonE6.setBackground(new java.awt.Color(0, 0, 0));
        jButtonE6.setForeground(new java.awt.Color(102, 102, 255));
        jButtonE6.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonE6.setText("empty");
        jButtonE6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonE6.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonE6.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonE6.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonE6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonE6ActionPerformed(evt);
            }
        });

        jButtonE5.setBackground(new java.awt.Color(0, 0, 0));
        jButtonE5.setForeground(new java.awt.Color(102, 102, 255));
        jButtonE5.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Wtile.jpg"))); // NOI18N
        jButtonE5.setText("empty");
        jButtonE5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonE5.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonE5.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonE5.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonE5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonE5ActionPerformed(evt);
            }
        });

        jButtonF5.setBackground(new java.awt.Color(0, 0, 0));
        jButtonF5.setForeground(new java.awt.Color(102, 102, 255));
        jButtonF5.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonF5.setText("empty");
        jButtonF5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonF5.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonF5.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonF5.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonF5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonF5ActionPerformed(evt);
            }
        });

        jButtonG5.setBackground(new java.awt.Color(0, 0, 0));
        jButtonG5.setForeground(new java.awt.Color(102, 102, 255));
        jButtonG5.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonG5.setText("empty");
        jButtonG5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonG5.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonG5.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonG5.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonG5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonG5ActionPerformed(evt);
            }
        });

        jButtonH5.setBackground(new java.awt.Color(0, 0, 0));
        jButtonH5.setForeground(new java.awt.Color(102, 102, 255));
        jButtonH5.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonH5.setText("empty");
        jButtonH5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonH5.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonH5.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonH5.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonH5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonH5ActionPerformed(evt);
            }
        });

        jButtonH4.setBackground(new java.awt.Color(0, 0, 0));
        jButtonH4.setForeground(new java.awt.Color(102, 102, 255));
        jButtonH4.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonH4.setText("empty");
        jButtonH4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonH4.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonH4.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonH4.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonH4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonH4ActionPerformed(evt);
            }
        });

        jButtonG4.setBackground(new java.awt.Color(0, 0, 0));
        jButtonG4.setForeground(new java.awt.Color(102, 102, 255));
        jButtonG4.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonG4.setText("empty");
        jButtonG4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonG4.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonG4.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonG4.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonG4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonG4ActionPerformed(evt);
            }
        });

        jButtonF4.setBackground(new java.awt.Color(0, 0, 0));
        jButtonF4.setForeground(new java.awt.Color(102, 102, 255));
        jButtonF4.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonF4.setText("empty");
        jButtonF4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonF4.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonF4.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonF4.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonF4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonF4ActionPerformed(evt);
            }
        });

        jButtonE4.setBackground(new java.awt.Color(0, 0, 0));
        jButtonE4.setForeground(new java.awt.Color(102, 102, 255));
        jButtonE4.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonE4.setText("empty");
        jButtonE4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonE4.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonE4.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonE4.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonE4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonE4ActionPerformed(evt);
            }
        });

        jButtonE3.setBackground(new java.awt.Color(0, 0, 0));
        jButtonE3.setForeground(new java.awt.Color(102, 102, 255));
        jButtonE3.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonE3.setText("empty");
        jButtonE3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonE3.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonE3.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonE3.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonE3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonE3ActionPerformed(evt);
            }
        });

        jButtonF3.setBackground(new java.awt.Color(0, 0, 0));
        jButtonF3.setForeground(new java.awt.Color(102, 102, 255));
        jButtonF3.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonF3.setText("empty");
        jButtonF3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonF3.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonF3.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonF3.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonF3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonF3ActionPerformed(evt);
            }
        });

        jButtonG3.setBackground(new java.awt.Color(0, 0, 0));
        jButtonG3.setForeground(new java.awt.Color(102, 102, 255));
        jButtonG3.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonG3.setText("empty");
        jButtonG3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonG3.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonG3.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonG3.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonG3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonG3ActionPerformed(evt);
            }
        });

        jButtonH3.setBackground(new java.awt.Color(0, 0, 0));
        jButtonH3.setForeground(new java.awt.Color(102, 102, 255));
        jButtonH3.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonH3.setText("empty");
        jButtonH3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonH3.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonH3.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonH3.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonH3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonH3ActionPerformed(evt);
            }
        });

        jButtonH2.setBackground(new java.awt.Color(0, 0, 0));
        jButtonH2.setForeground(new java.awt.Color(102, 102, 255));
        jButtonH2.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonH2.setText("empty");
        jButtonH2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonH2.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonH2.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonH2.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonH2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonH2ActionPerformed(evt);
            }
        });

        jButtonG2.setBackground(new java.awt.Color(0, 0, 0));
        jButtonG2.setForeground(new java.awt.Color(102, 102, 255));
        jButtonG2.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonG2.setText("empty");
        jButtonG2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonG2.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonG2.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonG2.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonG2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonG2ActionPerformed(evt);
            }
        });

        jButtonF2.setBackground(new java.awt.Color(0, 0, 0));
        jButtonF2.setForeground(new java.awt.Color(102, 102, 255));
        jButtonF2.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Ltile.jpg"))); // NOI18N
        jButtonF2.setText("empty");
        jButtonF2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonF2.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonF2.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonF2.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonF2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonF2ActionPerformed(evt);
            }
        });

        jButtonE2.setBackground(new java.awt.Color(0, 0, 0));
        jButtonE2.setForeground(new java.awt.Color(102, 102, 255));
        jButtonE2.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonE2.setText("empty");
        jButtonE2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonE2.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonE2.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonE2.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonE2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonE2ActionPerformed(evt);
            }
        });

        jButtonE1.setBackground(new java.awt.Color(0, 0, 0));
        jButtonE1.setForeground(new java.awt.Color(102, 102, 255));
        jButtonE1.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonE1.setText("empty");
        jButtonE1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonE1.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonE1.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonE1.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonE1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonE1ActionPerformed(evt);
            }
        });

        jButtonF1.setBackground(new java.awt.Color(0, 0, 0));
        jButtonF1.setForeground(new java.awt.Color(102, 102, 255));
        jButtonF1.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonF1.setText("empty");
        jButtonF1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonF1.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonF1.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonF1.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonF1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonF1ActionPerformed(evt);
            }
        });

        jButtonG1.setBackground(new java.awt.Color(0, 0, 0));
        jButtonG1.setForeground(new java.awt.Color(102, 102, 255));
        jButtonG1.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonG1.setText("empty");
        jButtonG1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonG1.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonG1.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonG1.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonG1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonG1ActionPerformed(evt);
            }
        });

        jButtonH1.setBackground(new java.awt.Color(0, 0, 0));
        jButtonH1.setForeground(new java.awt.Color(102, 102, 255));
        jButtonH1.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Wtile.jpg"))); // NOI18N
        jButtonH1.setText("empty");
        jButtonH1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonH1.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonH1.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonH1.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonH1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonH1ActionPerformed(evt);
            }
        });

        jButtonA15.setBackground(new java.awt.Color(0, 0, 0));
        jButtonA15.setForeground(new java.awt.Color(102, 102, 255));
        jButtonA15.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Wtile.jpg"))); // NOI18N
        jButtonA15.setText("empty");
        jButtonA15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonA15.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonA15.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonA15.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonA15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonA15ActionPerformed(evt);
            }
        });

        jButtonB15.setBackground(new java.awt.Color(0, 0, 0));
        jButtonB15.setForeground(new java.awt.Color(102, 102, 255));
        jButtonB15.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonB15.setText("empty");
        jButtonB15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonB15.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonB15.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonB15.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonB15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonB15ActionPerformed(evt);
            }
        });

        jButtonC15.setBackground(new java.awt.Color(0, 0, 0));
        jButtonC15.setForeground(new java.awt.Color(102, 102, 255));
        jButtonC15.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonC15.setText("empty");
        jButtonC15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonC15.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonC15.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonC15.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonC15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonC15ActionPerformed(evt);
            }
        });

        jButtonD15.setBackground(new java.awt.Color(0, 0, 0));
        jButtonD15.setForeground(new java.awt.Color(102, 102, 255));
        jButtonD15.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonD15.setText("empty");
        jButtonD15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonD15.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonD15.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonD15.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonD15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonD15ActionPerformed(evt);
            }
        });

        jButtonE15.setBackground(new java.awt.Color(0, 0, 0));
        jButtonE15.setForeground(new java.awt.Color(102, 102, 255));
        jButtonE15.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonE15.setText("empty");
        jButtonE15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonE15.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonE15.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonE15.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonE15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonE15ActionPerformed(evt);
            }
        });

        jButtonF15.setBackground(new java.awt.Color(0, 0, 0));
        jButtonF15.setForeground(new java.awt.Color(102, 102, 255));
        jButtonF15.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonF15.setText("empty");
        jButtonF15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonF15.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonF15.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonF15.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonF15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonF15ActionPerformed(evt);
            }
        });

        jButtonG15.setBackground(new java.awt.Color(0, 0, 0));
        jButtonG15.setForeground(new java.awt.Color(102, 102, 255));
        jButtonG15.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonG15.setText("empty");
        jButtonG15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonG15.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonG15.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonG15.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonG15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonG15ActionPerformed(evt);
            }
        });

        jButtonH15.setBackground(new java.awt.Color(0, 0, 0));
        jButtonH15.setForeground(new java.awt.Color(102, 102, 255));
        jButtonH15.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Wtile.jpg"))); // NOI18N
        jButtonH15.setText("empty");
        jButtonH15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonH15.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonH15.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonH15.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonH15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonH15ActionPerformed(evt);
            }
        });

        jButtonH14.setBackground(new java.awt.Color(0, 0, 0));
        jButtonH14.setForeground(new java.awt.Color(102, 102, 255));
        jButtonH14.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonH14.setText("empty");
        jButtonH14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonH14.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonH14.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonH14.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonH14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonH14ActionPerformed(evt);
            }
        });

        jButtonG14.setBackground(new java.awt.Color(0, 0, 0));
        jButtonG14.setForeground(new java.awt.Color(102, 102, 255));
        jButtonG14.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonG14.setText("empty");
        jButtonG14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonG14.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonG14.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonG14.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonG14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonG14ActionPerformed(evt);
            }
        });

        jButtonF14.setBackground(new java.awt.Color(0, 0, 0));
        jButtonF14.setForeground(new java.awt.Color(102, 102, 255));
        jButtonF14.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Ltile.jpg"))); // NOI18N
        jButtonF14.setText("empty");
        jButtonF14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonF14.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonF14.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonF14.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonF14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonF14ActionPerformed(evt);
            }
        });

        jButtonE14.setBackground(new java.awt.Color(0, 0, 0));
        jButtonE14.setForeground(new java.awt.Color(102, 102, 255));
        jButtonE14.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonE14.setText("empty");
        jButtonE14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonE14.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonE14.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonE14.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonE14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonE14ActionPerformed(evt);
            }
        });

        jButtonD14.setBackground(new java.awt.Color(0, 0, 0));
        jButtonD14.setForeground(new java.awt.Color(102, 102, 255));
        jButtonD14.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonD14.setText("empty");
        jButtonD14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonD14.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonD14.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonD14.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonD14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonD14ActionPerformed(evt);
            }
        });

        jButtonC14.setBackground(new java.awt.Color(0, 0, 0));
        jButtonC14.setForeground(new java.awt.Color(102, 102, 255));
        jButtonC14.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonC14.setText("empty");
        jButtonC14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonC14.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonC14.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonC14.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonC14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonC14ActionPerformed(evt);
            }
        });

        jButtonB14.setBackground(new java.awt.Color(0, 0, 0));
        jButtonB14.setForeground(new java.awt.Color(102, 102, 255));
        jButtonB14.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Wtile.jpg"))); // NOI18N
        jButtonB14.setText("empty");
        jButtonB14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonB14.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonB14.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonB14.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonB14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonB14ActionPerformed(evt);
            }
        });

        jButtonA14.setBackground(new java.awt.Color(0, 0, 0));
        jButtonA14.setForeground(new java.awt.Color(102, 102, 255));
        jButtonA14.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonA14.setText("empty");
        jButtonA14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonA14.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonA14.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonA14.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonA14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonA14ActionPerformed(evt);
            }
        });

        jButtonA13.setBackground(new java.awt.Color(0, 0, 0));
        jButtonA13.setForeground(new java.awt.Color(102, 102, 255));
        jButtonA13.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonA13.setText("empty");
        jButtonA13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonA13.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonA13.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonA13.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonA13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonA13ActionPerformed(evt);
            }
        });

        jButtonB13.setBackground(new java.awt.Color(0, 0, 0));
        jButtonB13.setForeground(new java.awt.Color(102, 102, 255));
        jButtonB13.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonB13.setText("empty");
        jButtonB13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonB13.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonB13.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonB13.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonB13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonB13ActionPerformed(evt);
            }
        });

        jButtonC13.setBackground(new java.awt.Color(0, 0, 0));
        jButtonC13.setForeground(new java.awt.Color(102, 102, 255));
        jButtonC13.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Wtile.jpg"))); // NOI18N
        jButtonC13.setText("empty");
        jButtonC13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonC13.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonC13.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonC13.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonC13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonC13ActionPerformed(evt);
            }
        });

        jButtonD13.setBackground(new java.awt.Color(0, 0, 0));
        jButtonD13.setForeground(new java.awt.Color(102, 102, 255));
        jButtonD13.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonD13.setText("empty");
        jButtonD13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonD13.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonD13.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonD13.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonD13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonD13ActionPerformed(evt);
            }
        });

        jButtonE13.setBackground(new java.awt.Color(0, 0, 0));
        jButtonE13.setForeground(new java.awt.Color(102, 102, 255));
        jButtonE13.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonE13.setText("empty");
        jButtonE13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonE13.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonE13.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonE13.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonE13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonE13ActionPerformed(evt);
            }
        });

        jButtonF13.setBackground(new java.awt.Color(0, 0, 0));
        jButtonF13.setForeground(new java.awt.Color(102, 102, 255));
        jButtonF13.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonF13.setText("empty");
        jButtonF13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonF13.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonF13.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonF13.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonF13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonF13ActionPerformed(evt);
            }
        });

        jButtonG13.setBackground(new java.awt.Color(0, 0, 0));
        jButtonG13.setForeground(new java.awt.Color(102, 102, 255));
        jButtonG13.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonG13.setText("empty");
        jButtonG13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonG13.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonG13.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonG13.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonG13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonG13ActionPerformed(evt);
            }
        });

        jButtonH13.setBackground(new java.awt.Color(0, 0, 0));
        jButtonH13.setForeground(new java.awt.Color(102, 102, 255));
        jButtonH13.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonH13.setText("empty");
        jButtonH13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonH13.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonH13.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonH13.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonH13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonH13ActionPerformed(evt);
            }
        });

        jButtonH12.setBackground(new java.awt.Color(0, 0, 0));
        jButtonH12.setForeground(new java.awt.Color(102, 102, 255));
        jButtonH12.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonH12.setText("empty");
        jButtonH12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonH12.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonH12.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonH12.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonH12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonH12ActionPerformed(evt);
            }
        });

        jButtonG12.setBackground(new java.awt.Color(0, 0, 0));
        jButtonG12.setForeground(new java.awt.Color(102, 102, 255));
        jButtonG12.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonG12.setText("empty");
        jButtonG12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonG12.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonG12.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonG12.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonG12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonG12ActionPerformed(evt);
            }
        });

        jButtonF12.setBackground(new java.awt.Color(0, 0, 0));
        jButtonF12.setForeground(new java.awt.Color(102, 102, 255));
        jButtonF12.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonF12.setText("empty");
        jButtonF12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonF12.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonF12.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonF12.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonF12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonF12ActionPerformed(evt);
            }
        });

        jButtonD12.setBackground(new java.awt.Color(0, 0, 0));
        jButtonD12.setForeground(new java.awt.Color(102, 102, 255));
        jButtonD12.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Wtile.jpg"))); // NOI18N
        jButtonD12.setText("empty");
        jButtonD12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonD12.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonD12.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonD12.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonD12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonD12ActionPerformed(evt);
            }
        });

        jButtonE12.setBackground(new java.awt.Color(0, 0, 0));
        jButtonE12.setForeground(new java.awt.Color(102, 102, 255));
        jButtonE12.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonE12.setText("empty");
        jButtonE12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonE12.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonE12.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonE12.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonE12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonE12ActionPerformed(evt);
            }
        });

        jButtonC12.setBackground(new java.awt.Color(0, 0, 0));
        jButtonC12.setForeground(new java.awt.Color(102, 102, 255));
        jButtonC12.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonC12.setText("empty");
        jButtonC12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonC12.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonC12.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonC12.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonC12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonC12ActionPerformed(evt);
            }
        });

        jButtonB12.setBackground(new java.awt.Color(0, 0, 0));
        jButtonB12.setForeground(new java.awt.Color(102, 102, 255));
        jButtonB12.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonB12.setText("empty");
        jButtonB12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonB12.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonB12.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonB12.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonB12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonB12ActionPerformed(evt);
            }
        });

        jButtonA12.setBackground(new java.awt.Color(0, 0, 0));
        jButtonA12.setForeground(new java.awt.Color(102, 102, 255));
        jButtonA12.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonA12.setText("empty");
        jButtonA12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonA12.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonA12.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonA12.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonA12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonA12ActionPerformed(evt);
            }
        });

        jButtonA11.setBackground(new java.awt.Color(0, 0, 0));
        jButtonA11.setForeground(new java.awt.Color(102, 102, 255));
        jButtonA11.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonA11.setText("empty");
        jButtonA11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonA11.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonA11.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonA11.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonA11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonA11ActionPerformed(evt);
            }
        });

        jButtonB11.setBackground(new java.awt.Color(0, 0, 0));
        jButtonB11.setForeground(new java.awt.Color(102, 102, 255));
        jButtonB11.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonB11.setText("empty");
        jButtonB11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonB11.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonB11.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonB11.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonB11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonB11ActionPerformed(evt);
            }
        });

        jButtonC11.setBackground(new java.awt.Color(0, 0, 0));
        jButtonC11.setForeground(new java.awt.Color(102, 102, 255));
        jButtonC11.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonC11.setText("empty");
        jButtonC11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonC11.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonC11.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonC11.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonC11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonC11ActionPerformed(evt);
            }
        });

        jButtonD11.setBackground(new java.awt.Color(0, 0, 0));
        jButtonD11.setForeground(new java.awt.Color(102, 102, 255));
        jButtonD11.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonD11.setText("empty");
        jButtonD11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonD11.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonD11.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonD11.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonD11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonD11ActionPerformed(evt);
            }
        });

        jButtonE11.setBackground(new java.awt.Color(0, 0, 0));
        jButtonE11.setForeground(new java.awt.Color(102, 102, 255));
        jButtonE11.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Wtile.jpg"))); // NOI18N
        jButtonE11.setText("empty");
        jButtonE11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonE11.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonE11.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonE11.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonE11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonE11ActionPerformed(evt);
            }
        });

        jButtonF11.setBackground(new java.awt.Color(0, 0, 0));
        jButtonF11.setForeground(new java.awt.Color(102, 102, 255));
        jButtonF11.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonF11.setText("empty");
        jButtonF11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonF11.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonF11.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonF11.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonF11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonF11ActionPerformed(evt);
            }
        });

        jButtonG11.setBackground(new java.awt.Color(0, 0, 0));
        jButtonG11.setForeground(new java.awt.Color(102, 102, 255));
        jButtonG11.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonG11.setText("empty");
        jButtonG11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonG11.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonG11.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonG11.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonG11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonG11ActionPerformed(evt);
            }
        });

        jButtonH11.setBackground(new java.awt.Color(0, 0, 0));
        jButtonH11.setForeground(new java.awt.Color(102, 102, 255));
        jButtonH11.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonH11.setText("empty");
        jButtonH11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonH11.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonH11.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonH11.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonH11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonH11ActionPerformed(evt);
            }
        });

        jButtonH10.setBackground(new java.awt.Color(0, 0, 0));
        jButtonH10.setForeground(new java.awt.Color(102, 102, 255));
        jButtonH10.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonH10.setText("empty");
        jButtonH10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonH10.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonH10.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonH10.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonH10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonH10ActionPerformed(evt);
            }
        });

        jButtonG10.setBackground(new java.awt.Color(0, 0, 0));
        jButtonG10.setForeground(new java.awt.Color(102, 102, 255));
        jButtonG10.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonG10.setText("empty");
        jButtonG10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonG10.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonG10.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonG10.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonG10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonG10ActionPerformed(evt);
            }
        });

        jButtonF10.setBackground(new java.awt.Color(0, 0, 0));
        jButtonF10.setForeground(new java.awt.Color(102, 102, 255));
        jButtonF10.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Ltile.jpg"))); // NOI18N
        jButtonF10.setText("empty");
        jButtonF10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonF10.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonF10.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonF10.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonF10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonF10ActionPerformed(evt);
            }
        });

        jButtonE10.setBackground(new java.awt.Color(0, 0, 0));
        jButtonE10.setForeground(new java.awt.Color(102, 102, 255));
        jButtonE10.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonE10.setText("empty");
        jButtonE10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonE10.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonE10.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonE10.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonE10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonE10ActionPerformed(evt);
            }
        });

        jButtonD10.setBackground(new java.awt.Color(0, 0, 0));
        jButtonD10.setForeground(new java.awt.Color(102, 102, 255));
        jButtonD10.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonD10.setText("empty");
        jButtonD10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonD10.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonD10.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonD10.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonD10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonD10ActionPerformed(evt);
            }
        });

        jButtonC10.setBackground(new java.awt.Color(0, 0, 0));
        jButtonC10.setForeground(new java.awt.Color(102, 102, 255));
        jButtonC10.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonC10.setText("empty");
        jButtonC10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonC10.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonC10.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonC10.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonC10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonC10ActionPerformed(evt);
            }
        });

        jButtonB10.setBackground(new java.awt.Color(0, 0, 0));
        jButtonB10.setForeground(new java.awt.Color(102, 102, 255));
        jButtonB10.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Ltile.jpg"))); // NOI18N
        jButtonB10.setText("empty");
        jButtonB10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonB10.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonB10.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonB10.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonB10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonB10ActionPerformed(evt);
            }
        });

        jButtonA10.setBackground(new java.awt.Color(0, 0, 0));
        jButtonA10.setForeground(new java.awt.Color(102, 102, 255));
        jButtonA10.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonA10.setText("empty");
        jButtonA10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonA10.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonA10.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonA10.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonA10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonA10ActionPerformed(evt);
            }
        });

        jButtonA9.setBackground(new java.awt.Color(0, 0, 0));
        jButtonA9.setForeground(new java.awt.Color(102, 102, 255));
        jButtonA9.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonA9.setText("empty");
        jButtonA9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonA9.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonA9.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonA9.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonA9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonA9ActionPerformed(evt);
            }
        });

        jButtonB9.setBackground(new java.awt.Color(0, 0, 0));
        jButtonB9.setForeground(new java.awt.Color(102, 102, 255));
        jButtonB9.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonB9.setText("empty");
        jButtonB9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonB9.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonB9.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonB9.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonB9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonB9ActionPerformed(evt);
            }
        });

        jButtonC9.setBackground(new java.awt.Color(0, 0, 0));
        jButtonC9.setForeground(new java.awt.Color(102, 102, 255));
        jButtonC9.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonC9.setText("empty");
        jButtonC9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonC9.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonC9.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonC9.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonC9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonC9ActionPerformed(evt);
            }
        });

        jButtonD9.setBackground(new java.awt.Color(0, 0, 0));
        jButtonD9.setForeground(new java.awt.Color(102, 102, 255));
        jButtonD9.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonD9.setText("empty");
        jButtonD9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonD9.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonD9.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonD9.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonD9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonD9ActionPerformed(evt);
            }
        });

        jButtonE9.setBackground(new java.awt.Color(0, 0, 0));
        jButtonE9.setForeground(new java.awt.Color(102, 102, 255));
        jButtonE9.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonE9.setText("empty");
        jButtonE9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonE9.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonE9.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonE9.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonE9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonE9ActionPerformed(evt);
            }
        });

        jButtonF9.setBackground(new java.awt.Color(0, 0, 0));
        jButtonF9.setForeground(new java.awt.Color(102, 102, 255));
        jButtonF9.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonF9.setText("empty");
        jButtonF9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonF9.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonF9.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonF9.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonF9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonF9ActionPerformed(evt);
            }
        });

        jButtonG9.setBackground(new java.awt.Color(0, 0, 0));
        jButtonG9.setForeground(new java.awt.Color(102, 102, 255));
        jButtonG9.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonG9.setText("empty");
        jButtonG9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonG9.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonG9.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonG9.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonG9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonG9ActionPerformed(evt);
            }
        });

        jButtonH9.setBackground(new java.awt.Color(0, 0, 0));
        jButtonH9.setForeground(new java.awt.Color(102, 102, 255));
        jButtonH9.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonH9.setText("empty");
        jButtonH9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonH9.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonH9.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonH9.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonH9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonH9ActionPerformed(evt);
            }
        });

        jButtonN1.setBackground(new java.awt.Color(0, 0, 0));
        jButtonN1.setForeground(new java.awt.Color(102, 102, 255));
        jButtonN1.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonN1.setText("empty");
        jButtonN1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonN1.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonN1.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonN1.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonN1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonN1ActionPerformed(evt);
            }
        });

        jButtonM1.setBackground(new java.awt.Color(0, 0, 0));
        jButtonM1.setForeground(new java.awt.Color(102, 102, 255));
        jButtonM1.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonM1.setText("empty");
        jButtonM1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonM1.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonM1.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonM1.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonM1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonM1ActionPerformed(evt);
            }
        });

        jButtonO1.setBackground(new java.awt.Color(0, 0, 0));
        jButtonO1.setForeground(new java.awt.Color(102, 102, 255));
        jButtonO1.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Wtile.jpg"))); // NOI18N
        jButtonO1.setText("empty");
        jButtonO1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonO1.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonO1.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonO1.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonO1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonO1ActionPerformed(evt);
            }
        });

        jButtonL1.setBackground(new java.awt.Color(0, 0, 0));
        jButtonL1.setForeground(new java.awt.Color(102, 102, 255));
        jButtonL1.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonL1.setText("empty");
        jButtonL1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonL1.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonL1.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonL1.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonL1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonL1ActionPerformed(evt);
            }
        });

        jButtonK1.setBackground(new java.awt.Color(0, 0, 0));
        jButtonK1.setForeground(new java.awt.Color(102, 102, 255));
        jButtonK1.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonK1.setText("empty");
        jButtonK1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonK1.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonK1.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonK1.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonK1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonK1ActionPerformed(evt);
            }
        });

        jButtonJ1.setBackground(new java.awt.Color(0, 0, 0));
        jButtonJ1.setForeground(new java.awt.Color(102, 102, 255));
        jButtonJ1.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonJ1.setText("empty");
        jButtonJ1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonJ1.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonJ1.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonJ1.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonJ1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJ1ActionPerformed(evt);
            }
        });

        jButtonI1.setBackground(new java.awt.Color(0, 0, 0));
        jButtonI1.setForeground(new java.awt.Color(102, 102, 255));
        jButtonI1.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonI1.setText("empty");
        jButtonI1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonI1.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonI1.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonI1.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonI1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonI1ActionPerformed(evt);
            }
        });

        jButtonI2.setBackground(new java.awt.Color(0, 0, 0));
        jButtonI2.setForeground(new java.awt.Color(102, 102, 255));
        jButtonI2.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonI2.setText("empty");
        jButtonI2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonI2.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonI2.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonI2.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonI2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonI2ActionPerformed(evt);
            }
        });

        jButtonJ2.setBackground(new java.awt.Color(0, 0, 0));
        jButtonJ2.setForeground(new java.awt.Color(102, 102, 255));
        jButtonJ2.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Ltile.jpg"))); // NOI18N
        jButtonJ2.setText("empty");
        jButtonJ2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonJ2.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonJ2.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonJ2.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonJ2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJ2ActionPerformed(evt);
            }
        });

        jButtonK2.setBackground(new java.awt.Color(0, 0, 0));
        jButtonK2.setForeground(new java.awt.Color(102, 102, 255));
        jButtonK2.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonK2.setText("empty");
        jButtonK2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonK2.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonK2.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonK2.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonK2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonK2ActionPerformed(evt);
            }
        });

        jButtonM2.setBackground(new java.awt.Color(0, 0, 0));
        jButtonM2.setForeground(new java.awt.Color(102, 102, 255));
        jButtonM2.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonM2.setText("empty");
        jButtonM2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonM2.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonM2.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonM2.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonM2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonM2ActionPerformed(evt);
            }
        });

        jButtonL2.setBackground(new java.awt.Color(0, 0, 0));
        jButtonL2.setForeground(new java.awt.Color(102, 102, 255));
        jButtonL2.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonL2.setText("empty");
        jButtonL2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonL2.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonL2.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonL2.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonL2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonL2ActionPerformed(evt);
            }
        });

        jButtonN2.setBackground(new java.awt.Color(0, 0, 0));
        jButtonN2.setForeground(new java.awt.Color(102, 102, 255));
        jButtonN2.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Wtile.jpg"))); // NOI18N
        jButtonN2.setText("empty");
        jButtonN2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonN2.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonN2.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonN2.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonN2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonN2ActionPerformed(evt);
            }
        });

        jButtonO2.setBackground(new java.awt.Color(0, 0, 0));
        jButtonO2.setForeground(new java.awt.Color(102, 102, 255));
        jButtonO2.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonO2.setText("empty");
        jButtonO2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonO2.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonO2.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonO2.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonO2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonO2ActionPerformed(evt);
            }
        });

        jButtonO4.setBackground(new java.awt.Color(0, 0, 0));
        jButtonO4.setForeground(new java.awt.Color(102, 102, 255));
        jButtonO4.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonO4.setText("empty");
        jButtonO4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonO4.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonO4.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonO4.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonO4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonO4ActionPerformed(evt);
            }
        });

        jButtonN4.setBackground(new java.awt.Color(0, 0, 0));
        jButtonN4.setForeground(new java.awt.Color(102, 102, 255));
        jButtonN4.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonN4.setText("empty");
        jButtonN4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonN4.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonN4.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonN4.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonN4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonN4ActionPerformed(evt);
            }
        });

        jButtonM4.setBackground(new java.awt.Color(0, 0, 0));
        jButtonM4.setForeground(new java.awt.Color(102, 102, 255));
        jButtonM4.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonM4.setText("empty");
        jButtonM4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonM4.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonM4.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonM4.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonM4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonM4ActionPerformed(evt);
            }
        });

        jButtonL4.setBackground(new java.awt.Color(0, 0, 0));
        jButtonL4.setForeground(new java.awt.Color(102, 102, 255));
        jButtonL4.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Wtile.jpg"))); // NOI18N
        jButtonL4.setText("empty");
        jButtonL4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonL4.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonL4.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonL4.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonL4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonL4ActionPerformed(evt);
            }
        });

        jButtonK4.setBackground(new java.awt.Color(0, 0, 0));
        jButtonK4.setForeground(new java.awt.Color(102, 102, 255));
        jButtonK4.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonK4.setText("empty");
        jButtonK4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonK4.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonK4.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonK4.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonK4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonK4ActionPerformed(evt);
            }
        });

        jButtonJ4.setBackground(new java.awt.Color(0, 0, 0));
        jButtonJ4.setForeground(new java.awt.Color(102, 102, 255));
        jButtonJ4.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonJ4.setText("empty");
        jButtonJ4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonJ4.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonJ4.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonJ4.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonJ4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJ4ActionPerformed(evt);
            }
        });

        jButtonI4.setBackground(new java.awt.Color(0, 0, 0));
        jButtonI4.setForeground(new java.awt.Color(102, 102, 255));
        jButtonI4.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonI4.setText("empty");
        jButtonI4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonI4.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonI4.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonI4.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonI4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonI4ActionPerformed(evt);
            }
        });

        jButtonI3.setBackground(new java.awt.Color(0, 0, 0));
        jButtonI3.setForeground(new java.awt.Color(102, 102, 255));
        jButtonI3.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonI3.setText("empty");
        jButtonI3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonI3.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonI3.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonI3.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonI3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonI3ActionPerformed(evt);
            }
        });

        jButtonJ3.setBackground(new java.awt.Color(0, 0, 0));
        jButtonJ3.setForeground(new java.awt.Color(102, 102, 255));
        jButtonJ3.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonJ3.setText("empty");
        jButtonJ3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonJ3.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonJ3.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonJ3.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonJ3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJ3ActionPerformed(evt);
            }
        });

        jButtonK3.setBackground(new java.awt.Color(0, 0, 0));
        jButtonK3.setForeground(new java.awt.Color(102, 102, 255));
        jButtonK3.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonK3.setText("empty");
        jButtonK3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonK3.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonK3.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonK3.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonK3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonK3ActionPerformed(evt);
            }
        });

        jButtonL3.setBackground(new java.awt.Color(0, 0, 0));
        jButtonL3.setForeground(new java.awt.Color(102, 102, 255));
        jButtonL3.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonL3.setText("empty");
        jButtonL3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonL3.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonL3.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonL3.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonL3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonL3ActionPerformed(evt);
            }
        });

        jButtonM3.setBackground(new java.awt.Color(0, 0, 0));
        jButtonM3.setForeground(new java.awt.Color(102, 102, 255));
        jButtonM3.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Wtile.jpg"))); // NOI18N
        jButtonM3.setText("empty");
        jButtonM3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonM3.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonM3.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonM3.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonM3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonM3ActionPerformed(evt);
            }
        });

        jButtonN3.setBackground(new java.awt.Color(0, 0, 0));
        jButtonN3.setForeground(new java.awt.Color(102, 102, 255));
        jButtonN3.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonN3.setText("empty");
        jButtonN3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonN3.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonN3.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonN3.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonN3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonN3ActionPerformed(evt);
            }
        });

        jButtonO3.setBackground(new java.awt.Color(0, 0, 0));
        jButtonO3.setForeground(new java.awt.Color(102, 102, 255));
        jButtonO3.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonO3.setText("empty");
        jButtonO3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonO3.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonO3.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonO3.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonO3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonO3ActionPerformed(evt);
            }
        });

        jButtonO8.setBackground(new java.awt.Color(0, 0, 0));
        jButtonO8.setForeground(new java.awt.Color(102, 102, 255));
        jButtonO8.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Wtile.jpg"))); // NOI18N
        jButtonO8.setText("empty");
        jButtonO8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonO8.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonO8.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonO8.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonO8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonO8ActionPerformed(evt);
            }
        });

        jButtonN8.setBackground(new java.awt.Color(0, 0, 0));
        jButtonN8.setForeground(new java.awt.Color(102, 102, 255));
        jButtonN8.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonN8.setText("empty");
        jButtonN8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonN8.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonN8.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonN8.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonN8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonN8ActionPerformed(evt);
            }
        });

        jButtonM8.setBackground(new java.awt.Color(0, 0, 0));
        jButtonM8.setForeground(new java.awt.Color(102, 102, 255));
        jButtonM8.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonM8.setText("empty");
        jButtonM8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonM8.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonM8.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonM8.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonM8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonM8ActionPerformed(evt);
            }
        });

        jButtonL8.setBackground(new java.awt.Color(0, 0, 0));
        jButtonL8.setForeground(new java.awt.Color(102, 102, 255));
        jButtonL8.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonL8.setText("empty");
        jButtonL8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonL8.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonL8.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonL8.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonL8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonL8ActionPerformed(evt);
            }
        });

        jButtonK8.setBackground(new java.awt.Color(0, 0, 0));
        jButtonK8.setForeground(new java.awt.Color(102, 102, 255));
        jButtonK8.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonK8.setText("empty");
        jButtonK8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonK8.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonK8.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonK8.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonK8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonK8ActionPerformed(evt);
            }
        });

        jButtonJ8.setBackground(new java.awt.Color(0, 0, 0));
        jButtonJ8.setForeground(new java.awt.Color(102, 102, 255));
        jButtonJ8.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonJ8.setText("empty");
        jButtonJ8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonJ8.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonJ8.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonJ8.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonJ8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJ8ActionPerformed(evt);
            }
        });

        jButtonI8.setBackground(new java.awt.Color(0, 0, 0));
        jButtonI8.setForeground(new java.awt.Color(102, 102, 255));
        jButtonI8.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonI8.setText("empty");
        jButtonI8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonI8.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonI8.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonI8.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonI8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonI8ActionPerformed(evt);
            }
        });

        jButtonI7.setBackground(new java.awt.Color(0, 0, 0));
        jButtonI7.setForeground(new java.awt.Color(102, 102, 255));
        jButtonI7.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonI7.setText("empty");
        jButtonI7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonI7.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonI7.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonI7.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonI7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonI7ActionPerformed(evt);
            }
        });

        jButtonI6.setBackground(new java.awt.Color(0, 0, 0));
        jButtonI6.setForeground(new java.awt.Color(102, 102, 255));
        jButtonI6.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonI6.setText("empty");
        jButtonI6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonI6.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonI6.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonI6.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonI6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonI6ActionPerformed(evt);
            }
        });

        jButtonI5.setBackground(new java.awt.Color(0, 0, 0));
        jButtonI5.setForeground(new java.awt.Color(102, 102, 255));
        jButtonI5.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonI5.setText("empty");
        jButtonI5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonI5.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonI5.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonI5.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonI5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonI5ActionPerformed(evt);
            }
        });

        jButtonJ5.setBackground(new java.awt.Color(0, 0, 0));
        jButtonJ5.setForeground(new java.awt.Color(102, 102, 255));
        jButtonJ5.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonJ5.setText("empty");
        jButtonJ5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonJ5.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonJ5.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonJ5.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonJ5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJ5ActionPerformed(evt);
            }
        });

        jButtonJ6.setBackground(new java.awt.Color(0, 0, 0));
        jButtonJ6.setForeground(new java.awt.Color(102, 102, 255));
        jButtonJ6.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Ltile.jpg"))); // NOI18N
        jButtonJ6.setText("empty");
        jButtonJ6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonJ6.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonJ6.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonJ6.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonJ6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJ6ActionPerformed(evt);
            }
        });

        jButtonJ7.setBackground(new java.awt.Color(0, 0, 0));
        jButtonJ7.setForeground(new java.awt.Color(102, 102, 255));
        jButtonJ7.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonJ7.setText("empty");
        jButtonJ7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonJ7.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonJ7.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonJ7.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonJ7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJ7ActionPerformed(evt);
            }
        });

        jButtonK7.setBackground(new java.awt.Color(0, 0, 0));
        jButtonK7.setForeground(new java.awt.Color(102, 102, 255));
        jButtonK7.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonK7.setText("empty");
        jButtonK7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonK7.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonK7.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonK7.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonK7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonK7ActionPerformed(evt);
            }
        });

        jButtonK6.setBackground(new java.awt.Color(0, 0, 0));
        jButtonK6.setForeground(new java.awt.Color(102, 102, 255));
        jButtonK6.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonK6.setText("empty");
        jButtonK6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonK6.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonK6.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonK6.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonK6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonK6ActionPerformed(evt);
            }
        });

        jButtonK5.setBackground(new java.awt.Color(0, 0, 0));
        jButtonK5.setForeground(new java.awt.Color(102, 102, 255));
        jButtonK5.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Wtile.jpg"))); // NOI18N
        jButtonK5.setText("empty");
        jButtonK5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonK5.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonK5.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonK5.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonK5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonK5ActionPerformed(evt);
            }
        });

        jButtonL5.setBackground(new java.awt.Color(0, 0, 0));
        jButtonL5.setForeground(new java.awt.Color(102, 102, 255));
        jButtonL5.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonL5.setText("empty");
        jButtonL5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonL5.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonL5.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonL5.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonL5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonL5ActionPerformed(evt);
            }
        });

        jButtonL6.setBackground(new java.awt.Color(0, 0, 0));
        jButtonL6.setForeground(new java.awt.Color(102, 102, 255));
        jButtonL6.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonL6.setText("empty");
        jButtonL6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonL6.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonL6.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonL6.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonL6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonL6ActionPerformed(evt);
            }
        });

        jButtonL7.setBackground(new java.awt.Color(0, 0, 0));
        jButtonL7.setForeground(new java.awt.Color(102, 102, 255));
        jButtonL7.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonL7.setText("empty");
        jButtonL7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonL7.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonL7.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonL7.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonL7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonL7ActionPerformed(evt);
            }
        });

        jButtonM7.setBackground(new java.awt.Color(0, 0, 0));
        jButtonM7.setForeground(new java.awt.Color(102, 102, 255));
        jButtonM7.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonM7.setText("empty");
        jButtonM7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonM7.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonM7.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonM7.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonM7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonM7ActionPerformed(evt);
            }
        });

        jButtonM6.setBackground(new java.awt.Color(0, 0, 0));
        jButtonM6.setForeground(new java.awt.Color(102, 102, 255));
        jButtonM6.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonM6.setText("empty");
        jButtonM6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonM6.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonM6.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonM6.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonM6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonM6ActionPerformed(evt);
            }
        });

        jButtonM5.setBackground(new java.awt.Color(0, 0, 0));
        jButtonM5.setForeground(new java.awt.Color(102, 102, 255));
        jButtonM5.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonM5.setText("empty");
        jButtonM5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonM5.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonM5.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonM5.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonM5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonM5ActionPerformed(evt);
            }
        });

        jButtonN5.setBackground(new java.awt.Color(0, 0, 0));
        jButtonN5.setForeground(new java.awt.Color(102, 102, 255));
        jButtonN5.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonN5.setText("empty");
        jButtonN5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonN5.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonN5.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonN5.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonN5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonN5ActionPerformed(evt);
            }
        });

        jButtonN6.setBackground(new java.awt.Color(0, 0, 0));
        jButtonN6.setForeground(new java.awt.Color(102, 102, 255));
        jButtonN6.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Ltile.jpg"))); // NOI18N
        jButtonN6.setText("empty");
        jButtonN6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonN6.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonN6.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonN6.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonN6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonN6ActionPerformed(evt);
            }
        });

        jButtonN7.setBackground(new java.awt.Color(0, 0, 0));
        jButtonN7.setForeground(new java.awt.Color(102, 102, 255));
        jButtonN7.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonN7.setText("empty");
        jButtonN7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonN7.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonN7.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonN7.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonN7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonN7ActionPerformed(evt);
            }
        });

        jButtonO7.setBackground(new java.awt.Color(0, 0, 0));
        jButtonO7.setForeground(new java.awt.Color(102, 102, 255));
        jButtonO7.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonO7.setText("empty");
        jButtonO7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonO7.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonO7.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonO7.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonO7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonO7ActionPerformed(evt);
            }
        });

        jButtonO6.setBackground(new java.awt.Color(0, 0, 0));
        jButtonO6.setForeground(new java.awt.Color(102, 102, 255));
        jButtonO6.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonO6.setText("empty");
        jButtonO6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonO6.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonO6.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonO6.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonO6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonO6ActionPerformed(evt);
            }
        });

        jButtonO5.setBackground(new java.awt.Color(0, 0, 0));
        jButtonO5.setForeground(new java.awt.Color(102, 102, 255));
        jButtonO5.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonO5.setText("empty");
        jButtonO5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonO5.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonO5.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonO5.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonO5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonO5ActionPerformed(evt);
            }
        });

        jButtonO14.setBackground(new java.awt.Color(0, 0, 0));
        jButtonO14.setForeground(new java.awt.Color(102, 102, 255));
        jButtonO14.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonO14.setText("empty");
        jButtonO14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonO14.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonO14.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonO14.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonO14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonO14ActionPerformed(evt);
            }
        });

        jButtonN14.setBackground(new java.awt.Color(0, 0, 0));
        jButtonN14.setForeground(new java.awt.Color(102, 102, 255));
        jButtonN14.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Wtile.jpg"))); // NOI18N
        jButtonN14.setText("empty");
        jButtonN14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonN14.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonN14.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonN14.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonN14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonN14ActionPerformed(evt);
            }
        });

        jButtonM14.setBackground(new java.awt.Color(0, 0, 0));
        jButtonM14.setForeground(new java.awt.Color(102, 102, 255));
        jButtonM14.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonM14.setText("empty");
        jButtonM14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonM14.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonM14.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonM14.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonM14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonM14ActionPerformed(evt);
            }
        });

        jButtonL14.setBackground(new java.awt.Color(0, 0, 0));
        jButtonL14.setForeground(new java.awt.Color(102, 102, 255));
        jButtonL14.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonL14.setText("empty");
        jButtonL14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonL14.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonL14.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonL14.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonL14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonL14ActionPerformed(evt);
            }
        });

        jButtonK14.setBackground(new java.awt.Color(0, 0, 0));
        jButtonK14.setForeground(new java.awt.Color(102, 102, 255));
        jButtonK14.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonK14.setText("empty");
        jButtonK14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonK14.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonK14.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonK14.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonK14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonK14ActionPerformed(evt);
            }
        });

        jButtonJ14.setBackground(new java.awt.Color(0, 0, 0));
        jButtonJ14.setForeground(new java.awt.Color(102, 102, 255));
        jButtonJ14.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Ltile.jpg"))); // NOI18N
        jButtonJ14.setText("empty");
        jButtonJ14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonJ14.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonJ14.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonJ14.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonJ14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJ14ActionPerformed(evt);
            }
        });

        jButtonI14.setBackground(new java.awt.Color(0, 0, 0));
        jButtonI14.setForeground(new java.awt.Color(102, 102, 255));
        jButtonI14.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonI14.setText("empty");
        jButtonI14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonI14.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonI14.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonI14.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonI14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonI14ActionPerformed(evt);
            }
        });

        jButtonI13.setBackground(new java.awt.Color(0, 0, 0));
        jButtonI13.setForeground(new java.awt.Color(102, 102, 255));
        jButtonI13.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonI13.setText("empty");
        jButtonI13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonI13.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonI13.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonI13.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonI13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonI13ActionPerformed(evt);
            }
        });

        jButtonJ13.setBackground(new java.awt.Color(0, 0, 0));
        jButtonJ13.setForeground(new java.awt.Color(102, 102, 255));
        jButtonJ13.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonJ13.setText("empty");
        jButtonJ13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonJ13.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonJ13.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonJ13.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonJ13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJ13ActionPerformed(evt);
            }
        });

        jButtonK13.setBackground(new java.awt.Color(0, 0, 0));
        jButtonK13.setForeground(new java.awt.Color(102, 102, 255));
        jButtonK13.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonK13.setText("empty");
        jButtonK13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonK13.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonK13.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonK13.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonK13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonK13ActionPerformed(evt);
            }
        });

        jButtonL13.setBackground(new java.awt.Color(0, 0, 0));
        jButtonL13.setForeground(new java.awt.Color(102, 102, 255));
        jButtonL13.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonL13.setText("empty");
        jButtonL13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonL13.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonL13.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonL13.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonL13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonL13ActionPerformed(evt);
            }
        });

        jButtonM13.setBackground(new java.awt.Color(0, 0, 0));
        jButtonM13.setForeground(new java.awt.Color(102, 102, 255));
        jButtonM13.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Wtile.jpg"))); // NOI18N
        jButtonM13.setText("empty");
        jButtonM13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonM13.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonM13.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonM13.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonM13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonM13ActionPerformed(evt);
            }
        });

        jButtonN13.setBackground(new java.awt.Color(0, 0, 0));
        jButtonN13.setForeground(new java.awt.Color(102, 102, 255));
        jButtonN13.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonN13.setText("empty");
        jButtonN13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonN13.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonN13.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonN13.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonN13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonN13ActionPerformed(evt);
            }
        });

        jButtonO13.setBackground(new java.awt.Color(0, 0, 0));
        jButtonO13.setForeground(new java.awt.Color(102, 102, 255));
        jButtonO13.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonO13.setText("empty");
        jButtonO13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonO13.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonO13.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonO13.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonO13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonO13ActionPerformed(evt);
            }
        });

        jButtonO12.setBackground(new java.awt.Color(0, 0, 0));
        jButtonO12.setForeground(new java.awt.Color(102, 102, 255));
        jButtonO12.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonO12.setText("empty");
        jButtonO12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonO12.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonO12.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonO12.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonO12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonO12ActionPerformed(evt);
            }
        });

        jButtonN12.setBackground(new java.awt.Color(0, 0, 0));
        jButtonN12.setForeground(new java.awt.Color(102, 102, 255));
        jButtonN12.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonN12.setText("empty");
        jButtonN12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonN12.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonN12.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonN12.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonN12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonN12ActionPerformed(evt);
            }
        });

        jButtonM12.setBackground(new java.awt.Color(0, 0, 0));
        jButtonM12.setForeground(new java.awt.Color(102, 102, 255));
        jButtonM12.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonM12.setText("empty");
        jButtonM12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonM12.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonM12.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonM12.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonM12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonM12ActionPerformed(evt);
            }
        });

        jButtonL12.setBackground(new java.awt.Color(0, 0, 0));
        jButtonL12.setForeground(new java.awt.Color(102, 102, 255));
        jButtonL12.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Wtile.jpg"))); // NOI18N
        jButtonL12.setText("empty");
        jButtonL12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonL12.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonL12.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonL12.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonL12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonL12ActionPerformed(evt);
            }
        });

        jButtonK12.setBackground(new java.awt.Color(0, 0, 0));
        jButtonK12.setForeground(new java.awt.Color(102, 102, 255));
        jButtonK12.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonK12.setText("empty");
        jButtonK12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonK12.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonK12.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonK12.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonK12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonK12ActionPerformed(evt);
            }
        });

        jButtonJ12.setBackground(new java.awt.Color(0, 0, 0));
        jButtonJ12.setForeground(new java.awt.Color(102, 102, 255));
        jButtonJ12.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonJ12.setText("empty");
        jButtonJ12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonJ12.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonJ12.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonJ12.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonJ12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJ12ActionPerformed(evt);
            }
        });

        jButtonI12.setBackground(new java.awt.Color(0, 0, 0));
        jButtonI12.setForeground(new java.awt.Color(102, 102, 255));
        jButtonI12.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonI12.setText("empty");
        jButtonI12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonI12.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonI12.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonI12.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonI12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonI12ActionPerformed(evt);
            }
        });

        jButtonI11.setBackground(new java.awt.Color(0, 0, 0));
        jButtonI11.setForeground(new java.awt.Color(102, 102, 255));
        jButtonI11.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonI11.setText("empty");
        jButtonI11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonI11.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonI11.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonI11.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonI11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonI11ActionPerformed(evt);
            }
        });

        jButtonJ11.setBackground(new java.awt.Color(0, 0, 0));
        jButtonJ11.setForeground(new java.awt.Color(102, 102, 255));
        jButtonJ11.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonJ11.setText("empty");
        jButtonJ11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonJ11.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonJ11.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonJ11.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonJ11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJ11ActionPerformed(evt);
            }
        });

        jButtonK11.setBackground(new java.awt.Color(0, 0, 0));
        jButtonK11.setForeground(new java.awt.Color(102, 102, 255));
        jButtonK11.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Wtile.jpg"))); // NOI18N
        jButtonK11.setText("empty");
        jButtonK11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonK11.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonK11.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonK11.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonK11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonK11ActionPerformed(evt);
            }
        });

        jButtonL11.setBackground(new java.awt.Color(0, 0, 0));
        jButtonL11.setForeground(new java.awt.Color(102, 102, 255));
        jButtonL11.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonL11.setText("empty");
        jButtonL11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonL11.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonL11.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonL11.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonL11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonL11ActionPerformed(evt);
            }
        });

        jButtonM11.setBackground(new java.awt.Color(0, 0, 0));
        jButtonM11.setForeground(new java.awt.Color(102, 102, 255));
        jButtonM11.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonM11.setText("empty");
        jButtonM11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonM11.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonM11.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonM11.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonM11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonM11ActionPerformed(evt);
            }
        });

        jButtonN11.setBackground(new java.awt.Color(0, 0, 0));
        jButtonN11.setForeground(new java.awt.Color(102, 102, 255));
        jButtonN11.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonN11.setText("empty");
        jButtonN11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonN11.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonN11.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonN11.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonN11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonN11ActionPerformed(evt);
            }
        });

        jButtonO11.setBackground(new java.awt.Color(0, 0, 0));
        jButtonO11.setForeground(new java.awt.Color(102, 102, 255));
        jButtonO11.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonO11.setText("empty");
        jButtonO11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonO11.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonO11.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonO11.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonO11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonO11ActionPerformed(evt);
            }
        });

        jButtonO10.setBackground(new java.awt.Color(0, 0, 0));
        jButtonO10.setForeground(new java.awt.Color(102, 102, 255));
        jButtonO10.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonO10.setText("empty");
        jButtonO10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonO10.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonO10.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonO10.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonO10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonO10ActionPerformed(evt);
            }
        });

        jButtonN10.setBackground(new java.awt.Color(0, 0, 0));
        jButtonN10.setForeground(new java.awt.Color(102, 102, 255));
        jButtonN10.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Ltile.jpg"))); // NOI18N
        jButtonN10.setText("empty");
        jButtonN10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonN10.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonN10.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonN10.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonN10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonN10ActionPerformed(evt);
            }
        });

        jButtonM10.setBackground(new java.awt.Color(0, 0, 0));
        jButtonM10.setForeground(new java.awt.Color(102, 102, 255));
        jButtonM10.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonM10.setText("empty");
        jButtonM10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonM10.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonM10.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonM10.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonM10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonM10ActionPerformed(evt);
            }
        });

        jButtonL10.setBackground(new java.awt.Color(0, 0, 0));
        jButtonL10.setForeground(new java.awt.Color(102, 102, 255));
        jButtonL10.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonL10.setText("empty");
        jButtonL10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonL10.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonL10.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonL10.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonL10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonL10ActionPerformed(evt);
            }
        });

        jButtonK10.setBackground(new java.awt.Color(0, 0, 0));
        jButtonK10.setForeground(new java.awt.Color(102, 102, 255));
        jButtonK10.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonK10.setText("empty");
        jButtonK10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonK10.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonK10.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonK10.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonK10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonK10ActionPerformed(evt);
            }
        });

        jButtonJ10.setBackground(new java.awt.Color(0, 0, 0));
        jButtonJ10.setForeground(new java.awt.Color(102, 102, 255));
        jButtonJ10.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Ltile.jpg"))); // NOI18N
        jButtonJ10.setText("empty");
        jButtonJ10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonJ10.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonJ10.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonJ10.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonJ10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJ10ActionPerformed(evt);
            }
        });

        jButtonI10.setBackground(new java.awt.Color(0, 0, 0));
        jButtonI10.setForeground(new java.awt.Color(102, 102, 255));
        jButtonI10.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonI10.setText("empty");
        jButtonI10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonI10.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonI10.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonI10.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonI10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonI10ActionPerformed(evt);
            }
        });

        jButtonI9.setBackground(new java.awt.Color(0, 0, 0));
        jButtonI9.setForeground(new java.awt.Color(102, 102, 255));
        jButtonI9.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonI9.setText("empty");
        jButtonI9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonI9.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonI9.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonI9.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonI9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonI9ActionPerformed(evt);
            }
        });

        jButtonJ9.setBackground(new java.awt.Color(0, 0, 0));
        jButtonJ9.setForeground(new java.awt.Color(102, 102, 255));
        jButtonJ9.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonJ9.setText("empty");
        jButtonJ9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonJ9.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonJ9.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonJ9.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonJ9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJ9ActionPerformed(evt);
            }
        });

        jButtonK9.setBackground(new java.awt.Color(0, 0, 0));
        jButtonK9.setForeground(new java.awt.Color(102, 102, 255));
        jButtonK9.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonK9.setText("empty");
        jButtonK9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonK9.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonK9.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonK9.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonK9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonK9ActionPerformed(evt);
            }
        });

        jButtonL9.setBackground(new java.awt.Color(0, 0, 0));
        jButtonL9.setForeground(new java.awt.Color(102, 102, 255));
        jButtonL9.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonL9.setText("empty");
        jButtonL9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonL9.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonL9.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonL9.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonL9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonL9ActionPerformed(evt);
            }
        });

        jButtonM9.setBackground(new java.awt.Color(0, 0, 0));
        jButtonM9.setForeground(new java.awt.Color(102, 102, 255));
        jButtonM9.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonM9.setText("empty");
        jButtonM9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonM9.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonM9.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonM9.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonM9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonM9ActionPerformed(evt);
            }
        });

        jButtonN9.setBackground(new java.awt.Color(0, 0, 0));
        jButtonN9.setForeground(new java.awt.Color(102, 102, 255));
        jButtonN9.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonN9.setText("empty");
        jButtonN9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonN9.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonN9.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonN9.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonN9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonN9ActionPerformed(evt);
            }
        });

        jButtonO9.setBackground(new java.awt.Color(0, 0, 0));
        jButtonO9.setForeground(new java.awt.Color(102, 102, 255));
        jButtonO9.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonO9.setText("empty");
        jButtonO9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonO9.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonO9.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonO9.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonO9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonO9ActionPerformed(evt);
            }
        });

        jButtonO15.setBackground(new java.awt.Color(0, 0, 0));
        jButtonO15.setForeground(new java.awt.Color(102, 102, 255));
        jButtonO15.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/3Wtile.jpg"))); // NOI18N
        jButtonO15.setText("empty");
        jButtonO15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonO15.setDisabledIcon(new javax.swing.ImageIcon("C:\\Users\\Adheesh\\Desktop\\My Creativity\\maar-dunga_meme.png")); // NOI18N
        jButtonO15.setDisabledSelectedIcon(new javax.swing.ImageIcon("C:\\Users\\Adheesh\\Desktop\\My Creativity\\maar-dunga_meme.png")); // NOI18N
        jButtonO15.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonO15.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonO15.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonO15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonO15ActionPerformed(evt);
            }
        });

        jButtonN15.setBackground(new java.awt.Color(0, 0, 0));
        jButtonN15.setForeground(new java.awt.Color(102, 102, 255));
        jButtonN15.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonN15.setText("empty");
        jButtonN15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonN15.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonN15.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonN15.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonN15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonN15ActionPerformed(evt);
            }
        });

        jButtonM15.setBackground(new java.awt.Color(0, 0, 0));
        jButtonM15.setForeground(new java.awt.Color(102, 102, 255));
        jButtonM15.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonM15.setText("empty");
        jButtonM15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonM15.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonM15.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonM15.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonM15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonM15ActionPerformed(evt);
            }
        });

        jButtonL15.setBackground(new java.awt.Color(0, 0, 0));
        jButtonL15.setForeground(new java.awt.Color(102, 102, 255));
        jButtonL15.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/2Ltile.jpg"))); // NOI18N
        jButtonL15.setText("empty");
        jButtonL15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonL15.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonL15.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonL15.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonL15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonL15ActionPerformed(evt);
            }
        });

        jButtonK15.setBackground(new java.awt.Color(0, 0, 0));
        jButtonK15.setForeground(new java.awt.Color(102, 102, 255));
        jButtonK15.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonK15.setText("empty");
        jButtonK15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonK15.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonK15.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonK15.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonK15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonK15ActionPerformed(evt);
            }
        });

        jButtonJ15.setBackground(new java.awt.Color(0, 0, 0));
        jButtonJ15.setForeground(new java.awt.Color(102, 102, 255));
        jButtonJ15.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonJ15.setText("empty");
        jButtonJ15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonJ15.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonJ15.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonJ15.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonJ15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJ15ActionPerformed(evt);
            }
        });

        jButtonI15.setBackground(new java.awt.Color(0, 0, 0));
        jButtonI15.setForeground(new java.awt.Color(102, 102, 255));
        jButtonI15.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/Plaintile.jpg"))); // NOI18N
        jButtonI15.setText("empty");
        jButtonI15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonI15.setMaximumSize(new java.awt.Dimension(30, 30));
        jButtonI15.setMinimumSize(new java.awt.Dimension(30, 30));
        jButtonI15.setPreferredSize(new java.awt.Dimension(573, 351));
        jButtonI15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonI15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonA13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonB13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonA14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonB14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonA15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonB15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(jButtonD13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonC14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonD14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonC15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonD15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonA9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonB9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonA10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonB10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonA11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonB11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonA12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonB12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonC9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonD9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonC10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonD10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonC11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonD11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonC12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonD12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButtonC13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonE13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonF13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonE14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonF14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonE15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonF15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(jButtonH13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonG14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonH14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonG15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonH15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonE9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonF9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonE10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonF10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonE11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonF11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonE12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonF12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonG9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonH9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonG10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonH10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonG11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonH11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonG12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonH12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButtonG13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonA5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonB5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonA6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonB6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonA7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonB7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonA8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonB8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(jButtonD5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonC6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonD6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonC7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonD7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonC8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonD8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonA1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonB1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonA2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonB2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonA3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonB3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonA4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonB4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonC1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonD1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonC2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonD2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonC3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonD3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonC4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonD4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButtonC5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonE5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonF5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonE6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonF6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonE7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonF7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonE8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonF8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(jButtonH5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonG6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonH6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonG7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonH7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonG8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonH8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonE1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonF1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonE2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonF2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonE3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonF3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonE4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonF4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonG1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonH1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonG2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonH2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonG3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonH3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonG4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButtonH4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButtonG5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonI1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonJ1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonK1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonL1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonM1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonN1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonO1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonI2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonJ2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonK2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonL2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonM2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonN2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonO2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonI3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonJ3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonK3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonL3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonM3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonN3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonO3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonI4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonJ4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonK4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonL4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonM4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonN4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonO4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonI5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonJ5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonK5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonL5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonM5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonN5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonO5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonI6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonJ6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonK6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonL6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonM6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonN6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonO6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonI7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonJ7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonK7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonL7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonM7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonN7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonO7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonI8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonJ8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonK8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonL8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonM8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonN8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonO8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonI9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonJ9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonK9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonL9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonM9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonN9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonO9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonI10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonJ10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonK10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonL10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonM10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonN10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonO10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonI11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonJ11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonK11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonL11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonM11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonN11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonO11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonI12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonJ12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonK12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonL12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonM12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonN12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonO12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonI13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonJ13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonK13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonL13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonM13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonN13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonO13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonI14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonJ14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonK14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonL14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonM14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonN14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonO14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonI15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonJ15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonK15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonL15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonM15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonN15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButtonO15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonC1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonD1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonC2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonD2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonC3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonD3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonC4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonD4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonA1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonB1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonA2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonB2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonA3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonB3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonA4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonB4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButtonD5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonC6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonD6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonC7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonD7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonC8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonD8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonA5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonB5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonC5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonA6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonB6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonA7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonB7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonA8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonB8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonE1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonF1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonE2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonF2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonE3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonF3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonE4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonF4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jButtonJ1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jButtonK1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jButtonI1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jButtonN1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jButtonO1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jButtonL1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jButtonM1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButtonG1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButtonH1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButtonG2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButtonH2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jButtonJ2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jButtonK2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jButtonI2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jButtonN2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jButtonO2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jButtonL2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jButtonM2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonG3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonH3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonG4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonH4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jButtonJ3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jButtonK3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(jButtonI3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jButtonN3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jButtonO3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jButtonL3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jButtonM3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jButtonJ4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jButtonK4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(jButtonI4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jButtonN4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jButtonO4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jButtonL4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jButtonM4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButtonH5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonG6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonH6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonG7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonH7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonG8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonH8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonE5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonF5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonG5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonE6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonF6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonE7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonF7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonE8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonF8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonJ5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonK5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jButtonI5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonN5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonO5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonL5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonM5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jButtonJ6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButtonI6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonN6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonO6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonL6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonM6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonK6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonJ7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonK7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jButtonI7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonN7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonO7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonL7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonM7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonJ8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonK8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jButtonI8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonN8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonO8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonL8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonM8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonJ9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonK9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jButtonI9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonN9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonO9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonL9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonM9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonJ10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonK10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jButtonI10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonN10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonO10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonL10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonM10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonJ11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonK11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jButtonI11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonN11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonO11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonL11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonM11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonJ12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonK12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jButtonI12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonN12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonO12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonL12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonM12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonJ13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonK13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jButtonI13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonN13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonO13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonL13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonM13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonJ14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonK14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jButtonI14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonN14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonO14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonL14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonM14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonG9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonH9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonG10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonH10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonG11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonH11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonG12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonH12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonE9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonF9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonE10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonF10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonE11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonF11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonE12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonF12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonE13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonF13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonG13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonE14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonF14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonE15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonF15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jButtonJ15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jButtonK15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(jButtonI15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jButtonN15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jButtonO15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jButtonL15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jButtonM15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jButtonH13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, 0)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jButtonG14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jButtonH14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(0, 0, 0)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jButtonG15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jButtonH15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonC9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonD9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonC10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonD10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonC11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonD11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonC12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonD12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonA9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonB9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonA10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonB10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonA11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonB11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonA12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonB12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonD13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonC14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonD14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonC15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonD15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonA13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonB13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonC13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonA14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonB14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButtonA15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButtonB15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jLabelTimer.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelTimer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTimer.setText(String.valueOf(maxTime));
        jLabelTimer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabelTimer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabelTimer.setPreferredSize(new java.awt.Dimension(35, 24));

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Player2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jPanel7.setPreferredSize(new java.awt.Dimension(297, 206));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Rack"));
        jPanel3.setPreferredSize(new java.awt.Dimension(262, 86));

        jButtonCR1.setBackground(new java.awt.Color(0, 0, 0));
        jButtonCR1.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/blankRack.jpg"))); // NOI18N
        jButtonCR1.setText("jButton226");
        jButtonCR1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonCR1.setPreferredSize(new java.awt.Dimension(28, 28));
        jButtonCR1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCR1ActionPerformed(evt);
            }
        });

        jButtonCR2.setBackground(new java.awt.Color(0, 0, 0));
        jButtonCR2.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/blankRack.jpg"))); // NOI18N
        jButtonCR2.setText("jButton226");
        jButtonCR2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonCR2.setPreferredSize(new java.awt.Dimension(28, 28));
        jButtonCR2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCR2ActionPerformed(evt);
            }
        });

        jButtonCR4.setBackground(new java.awt.Color(0, 0, 0));
        jButtonCR4.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/blankRack.jpg"))); // NOI18N
        jButtonCR4.setText("jButton226");
        jButtonCR4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonCR4.setPreferredSize(new java.awt.Dimension(28, 28));
        jButtonCR4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCR4ActionPerformed(evt);
            }
        });

        jButtonCR5.setBackground(new java.awt.Color(0, 0, 0));
        jButtonCR5.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/blankRack.jpg"))); // NOI18N
        jButtonCR5.setText("jButton226");
        jButtonCR5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonCR5.setPreferredSize(new java.awt.Dimension(28, 28));
        jButtonCR5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCR5ActionPerformed(evt);
            }
        });

        jButtonCR7.setBackground(new java.awt.Color(0, 0, 0));
        jButtonCR7.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/blankRack.jpg"))); // NOI18N
        jButtonCR7.setText("jButton226");
        jButtonCR7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonCR7.setPreferredSize(new java.awt.Dimension(28, 28));
        jButtonCR7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCR7ActionPerformed(evt);
            }
        });

        jButtonCR3.setBackground(new java.awt.Color(0, 0, 0));
        jButtonCR3.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/blankRack.jpg"))); // NOI18N
        jButtonCR3.setText("jButton226");
        jButtonCR3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonCR3.setPreferredSize(new java.awt.Dimension(28, 28));
        jButtonCR3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCR3ActionPerformed(evt);
            }
        });

        jButtonCR6.setBackground(new java.awt.Color(0, 0, 0));
        jButtonCR6.setBackIcon(new javax.swing.ImageIcon(getClass().getResource("/blankRack.jpg"))); // NOI18N
        jButtonCR6.setText("jButton226");
        jButtonCR6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonCR6.setPreferredSize(new java.awt.Dimension(28, 28));
        jButtonCR6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCR6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jButtonCR1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jButtonCR2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jButtonCR3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jButtonCR4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jButtonCR5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jButtonCR6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jButtonCR7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonCR2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonCR3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonCR4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonCR5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonCR6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonCR7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonCR1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jButtonCPlay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonCPlay.setText("Play");
        jButtonCPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCPlayActionPerformed(evt);
            }
        });

        jLabelCPlayerTScore.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelCPlayerTScore.setText("Total Score");

        jLabelCPlayerCScore.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelCPlayerCScore.setText("Score for last move");

        jLabelCPlayerCScoreVal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelCPlayerCScoreVal.setText("0");

        jLabelCPlayerTScoreVal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelCPlayerTScoreVal.setText("0");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCPlay)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelCPlayerCScore, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelCPlayerTScore, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelCPlayerTScoreVal, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                                    .addComponent(jLabelCPlayerCScoreVal, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)))
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCPlayerTScore, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCPlayerTScoreVal, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCPlayerCScore, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCPlayerCScoreVal, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCPlay))
        );

        jLabelStartGame.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelStartGame.setText("Loading the dictionary, please wait for a minute");

        jProgressBarTimer.setForeground(java.awt.Color.green);
        jProgressBarTimer.setMaximum(maxTime);
        jProgressBarTimer.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jProgressBarTimer.setPreferredSize(new java.awt.Dimension(240, 24));

        jButtonStartGame.setText("Start Game");
        jButtonStartGame.setOpaque(false);
        jButtonStartGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartGameActionPerformed(evt);
            }
        });

        jLabelEmptyTileStatus.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        jLabelEmptyTileStatus.setText("Enter Value for B1/B2");

        jButtonLetterB2.setForeground(new java.awt.Color(255, 0, 0));
        jButtonLetterB2.setText("B2 Letter");
        jButtonLetterB2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLetterB2ActionPerformed(evt);
            }
        });

        jButtonLetterB1.setForeground(new java.awt.Color(255, 0, 0));
        jButtonLetterB1.setText("B1 Letter");
        jButtonLetterB1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLetterB1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelLetterRemainlabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelLetterRemainingVal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelEmptyTileStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelCenterTileStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButtonLetterB1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonLetterB2))
                            .addComponent(jButtonFillRack, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(48, 48, 48))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabelTimer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jProgressBarTimer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonStartGame, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelStartGame, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelTimer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jProgressBarTimer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonLetterB1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonLetterB2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonFillRack))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelLetterRemainingVal)
                            .addComponent(jLabelLetterRemainlabel))
                        .addGap(12, 12, 12)
                        .addComponent(jLabelCenterTileStatus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelEmptyTileStatus)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonStartGame, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelStartGame))
                .addContainerGap())
        );

        jPanel7.getAccessibleContext().setAccessibleName("Computer");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonFillRackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFillRackActionPerformed
        // TODO add your handling code here:

        //fillRack();
    }//GEN-LAST:event_jButtonFillRackActionPerformed

    private void jButtonPR1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPR1ActionPerformed
        // TODO add your handling code here:
        //jButtonPR1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0)));
        //System.out.println(jButtonPR1.getText());
        //handleRackButtonClick(jButtonPR1);

    }//GEN-LAST:event_jButtonPR1ActionPerformed

    private void jButtonPR2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPR2ActionPerformed
        // TODO add your handling code here:
        //jButtonPR1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0)));
        //System.out.println(jButtonPR2.getText());
        //System.out.println(jButtonPR2.getToolTipText());
        //handleRackButtonClick(jButtonPR2);
    }//GEN-LAST:event_jButtonPR2ActionPerformed

    private void jButtonPR3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPR3ActionPerformed
        // TODO add your handling code here:
        //handleRackButtonClick(jButtonPR3);
    }//GEN-LAST:event_jButtonPR3ActionPerformed

    private void jButtonPR4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPR4ActionPerformed
        // TODO add your handling code here:
        //handleRackButtonClick(jButtonPR4);
    }//GEN-LAST:event_jButtonPR4ActionPerformed

    private void jButtonPR5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPR5ActionPerformed
        // TODO add your handling code here:
        //handleRackButtonClick(jButtonPR5);
    }//GEN-LAST:event_jButtonPR5ActionPerformed

    private void jButtonPR7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPR7ActionPerformed
        // TODO add your handling code here:
        //handleRackButtonClick(jButtonPR7);
    }//GEN-LAST:event_jButtonPR7ActionPerformed

    private void jButtonPR6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPR6ActionPerformed
        // TODO add your handling code here:
        //handleRackButtonClick(jButtonPR6);
    }//GEN-LAST:event_jButtonPR6ActionPerformed

    private void jButtonPPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPPlayActionPerformed
        // TODO add your handling code here:
        changePlayer();
    }//GEN-LAST:event_jButtonPPlayActionPerformed

    private void jButtonPHintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPHintActionPerformed
        // TODO add your handling code here:
        //getHint();
        SwingWorker w = getHintWorker();
        w.execute();
    }//GEN-LAST:event_jButtonPHintActionPerformed

    private void jButtonPGiveUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPGiveUpActionPerformed
        // TODO add your handling code here:
        GameOver(2, " because Player1 gave up");
    }//GEN-LAST:event_jButtonPGiveUpActionPerformed

    private void jButtonA1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonA1ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonA1);
    }//GEN-LAST:event_jButtonA1ActionPerformed

    private void jButtonA2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonA2ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonA2);
    }//GEN-LAST:event_jButtonA2ActionPerformed

    private void jButtonA3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonA3ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonA3);
    }//GEN-LAST:event_jButtonA3ActionPerformed

    private void jButtonA4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonA4ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonA4);
    }//GEN-LAST:event_jButtonA4ActionPerformed

    private void jButtonA5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonA5ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonA5);
    }//GEN-LAST:event_jButtonA5ActionPerformed

    private void jButtonA6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonA6ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonA6);
    }//GEN-LAST:event_jButtonA6ActionPerformed

    private void jButtonA7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonA7ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonA7);
    }//GEN-LAST:event_jButtonA7ActionPerformed

    private void jButtonB1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonB1ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonB1);
    }//GEN-LAST:event_jButtonB1ActionPerformed

    private void jButtonB2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonB2ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonB2);
    }//GEN-LAST:event_jButtonB2ActionPerformed

    private void jButtonB3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonB3ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonB3);
    }//GEN-LAST:event_jButtonB3ActionPerformed

    private void jButtonB4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonB4ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonB4);
    }//GEN-LAST:event_jButtonB4ActionPerformed

    private void jButtonB5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonB5ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonB5);
    }//GEN-LAST:event_jButtonB5ActionPerformed

    private void jButtonB6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonB6ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonB6);
    }//GEN-LAST:event_jButtonB6ActionPerformed

    private void jButtonB7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonB7ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonB7);
    }//GEN-LAST:event_jButtonB7ActionPerformed

    private void jButtonC1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonC1ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonC1);
    }//GEN-LAST:event_jButtonC1ActionPerformed

    private void jButtonC2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonC2ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonC2);
    }//GEN-LAST:event_jButtonC2ActionPerformed

    private void jButtonC3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonC3ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonC3);
    }//GEN-LAST:event_jButtonC3ActionPerformed

    private void jButtonC4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonC4ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonC4);
    }//GEN-LAST:event_jButtonC4ActionPerformed

    private void jButtonC5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonC5ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonC5);
    }//GEN-LAST:event_jButtonC5ActionPerformed

    private void jButtonD1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonD1ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonD1);
    }//GEN-LAST:event_jButtonD1ActionPerformed

    private void jButtonD2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonD2ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonD2);
    }//GEN-LAST:event_jButtonD2ActionPerformed

    private void jButtonD3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonD3ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonD3);
    }//GEN-LAST:event_jButtonD3ActionPerformed

    private void jButtonD4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonD4ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonD4);
    }//GEN-LAST:event_jButtonD4ActionPerformed

    private void jButtonD8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonD8ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonD8);
    }//GEN-LAST:event_jButtonD8ActionPerformed

    private void jButtonA8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonA8ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonA8);
    }//GEN-LAST:event_jButtonA8ActionPerformed

    private void jButtonB8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonB8ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonB8);
    }//GEN-LAST:event_jButtonB8ActionPerformed

    private void jButtonC8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonC8ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonC8);
    }//GEN-LAST:event_jButtonC8ActionPerformed

    private void jButtonC7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonC7ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonC7);
    }//GEN-LAST:event_jButtonC7ActionPerformed

    private void jButtonD7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonD7ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonD7);
    }//GEN-LAST:event_jButtonD7ActionPerformed

    private void jButtonD6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonD6ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonD6);
    }//GEN-LAST:event_jButtonD6ActionPerformed

    private void jButtonC6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonC6ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonC6);
    }//GEN-LAST:event_jButtonC6ActionPerformed

    private void jButtonD5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonD5ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonD5);
    }//GEN-LAST:event_jButtonD5ActionPerformed

    private void jButtonH8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonH8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonH8ActionPerformed

    private void jButtonG8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonG8ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonG8);
    }//GEN-LAST:event_jButtonG8ActionPerformed

    private void jButtonF8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonF8ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonF8);
    }//GEN-LAST:event_jButtonF8ActionPerformed

    private void jButtonE8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonE8ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonE8);
    }//GEN-LAST:event_jButtonE8ActionPerformed

    private void jButtonE7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonE7ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonE7);
    }//GEN-LAST:event_jButtonE7ActionPerformed

    private void jButtonF7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonF7ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonF7);
    }//GEN-LAST:event_jButtonF7ActionPerformed

    private void jButtonG7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonG7ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonG7);
    }//GEN-LAST:event_jButtonG7ActionPerformed

    private void jButtonH7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonH7ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonH7);
    }//GEN-LAST:event_jButtonH7ActionPerformed

    private void jButtonH6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonH6ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonH6);
    }//GEN-LAST:event_jButtonH6ActionPerformed

    private void jButtonG6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonG6ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonG6);
    }//GEN-LAST:event_jButtonG6ActionPerformed

    private void jButtonF6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonF6ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonF6);
    }//GEN-LAST:event_jButtonF6ActionPerformed

    private void jButtonE6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonE6ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonE6);
    }//GEN-LAST:event_jButtonE6ActionPerformed

    private void jButtonE5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonE5ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonE5);
    }//GEN-LAST:event_jButtonE5ActionPerformed

    private void jButtonF5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonF5ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonF5);
    }//GEN-LAST:event_jButtonF5ActionPerformed

    private void jButtonG5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonG5ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonG5);
    }//GEN-LAST:event_jButtonG5ActionPerformed

    private void jButtonH5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonH5ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonH5);
    }//GEN-LAST:event_jButtonH5ActionPerformed

    private void jButtonH4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonH4ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonH4);
    }//GEN-LAST:event_jButtonH4ActionPerformed

    private void jButtonG4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonG4ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonG4);
    }//GEN-LAST:event_jButtonG4ActionPerformed

    private void jButtonF4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonF4ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonF4);
    }//GEN-LAST:event_jButtonF4ActionPerformed

    private void jButtonE4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonE4ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonE4);
    }//GEN-LAST:event_jButtonE4ActionPerformed

    private void jButtonE3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonE3ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonE3);
    }//GEN-LAST:event_jButtonE3ActionPerformed

    private void jButtonF3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonF3ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonF3);
    }//GEN-LAST:event_jButtonF3ActionPerformed

    private void jButtonG3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonG3ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonG3);
    }//GEN-LAST:event_jButtonG3ActionPerformed

    private void jButtonH3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonH3ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonH3);
    }//GEN-LAST:event_jButtonH3ActionPerformed

    private void jButtonH2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonH2ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonH2);
    }//GEN-LAST:event_jButtonH2ActionPerformed

    private void jButtonG2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonG2ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonG2);
    }//GEN-LAST:event_jButtonG2ActionPerformed

    private void jButtonF2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonF2ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonF2);
    }//GEN-LAST:event_jButtonF2ActionPerformed

    private void jButtonE2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonE2ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonE2);
    }//GEN-LAST:event_jButtonE2ActionPerformed

    private void jButtonE1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonE1ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonE1);
    }//GEN-LAST:event_jButtonE1ActionPerformed

    private void jButtonF1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonF1ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonF1);
    }//GEN-LAST:event_jButtonF1ActionPerformed

    private void jButtonG1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonG1ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonG1);
    }//GEN-LAST:event_jButtonG1ActionPerformed

    private void jButtonH1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonH1ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonH1);
    }//GEN-LAST:event_jButtonH1ActionPerformed

    private void jButtonA15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonA15ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonA15);
    }//GEN-LAST:event_jButtonA15ActionPerformed

    private void jButtonB15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonB15ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonB15);
    }//GEN-LAST:event_jButtonB15ActionPerformed

    private void jButtonC15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonC15ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonC15);
    }//GEN-LAST:event_jButtonC15ActionPerformed

    private void jButtonD15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonD15ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonD15);
    }//GEN-LAST:event_jButtonD15ActionPerformed

    private void jButtonE15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonE15ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonE15);
    }//GEN-LAST:event_jButtonE15ActionPerformed

    private void jButtonF15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonF15ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonF15);
    }//GEN-LAST:event_jButtonF15ActionPerformed

    private void jButtonG15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonG15ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonG15);
    }//GEN-LAST:event_jButtonG15ActionPerformed

    private void jButtonH15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonH15ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonH15);
    }//GEN-LAST:event_jButtonH15ActionPerformed

    private void jButtonH14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonH14ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonH14);
    }//GEN-LAST:event_jButtonH14ActionPerformed

    private void jButtonG14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonG14ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonG14);
    }//GEN-LAST:event_jButtonG14ActionPerformed

    private void jButtonF14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonF14ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonF14);
    }//GEN-LAST:event_jButtonF14ActionPerformed

    private void jButtonE14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonE14ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonE14);
    }//GEN-LAST:event_jButtonE14ActionPerformed

    private void jButtonD14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonD14ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonD14);
    }//GEN-LAST:event_jButtonD14ActionPerformed

    private void jButtonC14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonC14ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonC14);
    }//GEN-LAST:event_jButtonC14ActionPerformed

    private void jButtonB14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonB14ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonB14);
    }//GEN-LAST:event_jButtonB14ActionPerformed

    private void jButtonA14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonA14ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonA14);
    }//GEN-LAST:event_jButtonA14ActionPerformed

    private void jButtonA13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonA13ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonA13);
    }//GEN-LAST:event_jButtonA13ActionPerformed

    private void jButtonB13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonB13ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonB13);
    }//GEN-LAST:event_jButtonB13ActionPerformed

    private void jButtonC13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonC13ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonC13);
    }//GEN-LAST:event_jButtonC13ActionPerformed

    private void jButtonD13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonD13ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonD13);
    }//GEN-LAST:event_jButtonD13ActionPerformed

    private void jButtonE13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonE13ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonE13);
    }//GEN-LAST:event_jButtonE13ActionPerformed

    private void jButtonF13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonF13ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonF13);
    }//GEN-LAST:event_jButtonF13ActionPerformed

    private void jButtonG13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonG13ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonG13);
    }//GEN-LAST:event_jButtonG13ActionPerformed

    private void jButtonH13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonH13ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonH13);
    }//GEN-LAST:event_jButtonH13ActionPerformed

    private void jButtonH12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonH12ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonH12);
    }//GEN-LAST:event_jButtonH12ActionPerformed

    private void jButtonG12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonG12ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonG12);
    }//GEN-LAST:event_jButtonG12ActionPerformed

    private void jButtonF12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonF12ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonF12);
    }//GEN-LAST:event_jButtonF12ActionPerformed

    private void jButtonD12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonD12ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonD12);
    }//GEN-LAST:event_jButtonD12ActionPerformed

    private void jButtonE12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonE12ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonE12);
    }//GEN-LAST:event_jButtonE12ActionPerformed

    private void jButtonC12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonC12ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonC12);
    }//GEN-LAST:event_jButtonC12ActionPerformed

    private void jButtonB12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonB12ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonB12);
    }//GEN-LAST:event_jButtonB12ActionPerformed

    private void jButtonA12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonA12ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonA12);
    }//GEN-LAST:event_jButtonA12ActionPerformed

    private void jButtonA11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonA11ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonA11);
    }//GEN-LAST:event_jButtonA11ActionPerformed

    private void jButtonB11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonB11ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonB11);
    }//GEN-LAST:event_jButtonB11ActionPerformed

    private void jButtonC11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonC11ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonC11);
    }//GEN-LAST:event_jButtonC11ActionPerformed

    private void jButtonD11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonD11ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonD11);
    }//GEN-LAST:event_jButtonD11ActionPerformed

    private void jButtonE11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonE11ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonE11);
    }//GEN-LAST:event_jButtonE11ActionPerformed

    private void jButtonF11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonF11ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonF11);
    }//GEN-LAST:event_jButtonF11ActionPerformed

    private void jButtonG11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonG11ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonG11);
    }//GEN-LAST:event_jButtonG11ActionPerformed

    private void jButtonH11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonH11ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonH11);
    }//GEN-LAST:event_jButtonH11ActionPerformed

    private void jButtonH10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonH10ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonH10);
    }//GEN-LAST:event_jButtonH10ActionPerformed

    private void jButtonG10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonG10ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonG10);
    }//GEN-LAST:event_jButtonG10ActionPerformed

    private void jButtonF10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonF10ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonF10);
    }//GEN-LAST:event_jButtonF10ActionPerformed

    private void jButtonE10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonE10ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonE10);
    }//GEN-LAST:event_jButtonE10ActionPerformed

    private void jButtonD10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonD10ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonD10);
    }//GEN-LAST:event_jButtonD10ActionPerformed

    private void jButtonC10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonC10ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonC10);
    }//GEN-LAST:event_jButtonC10ActionPerformed

    private void jButtonB10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonB10ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonB10);
    }//GEN-LAST:event_jButtonB10ActionPerformed

    private void jButtonA10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonA10ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonA10);
    }//GEN-LAST:event_jButtonA10ActionPerformed

    private void jButtonA9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonA9ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonA9);
    }//GEN-LAST:event_jButtonA9ActionPerformed

    private void jButtonB9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonB9ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonB9);
    }//GEN-LAST:event_jButtonB9ActionPerformed

    private void jButtonC9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonC9ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonC9);
    }//GEN-LAST:event_jButtonC9ActionPerformed

    private void jButtonD9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonD9ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonD9);
    }//GEN-LAST:event_jButtonD9ActionPerformed

    private void jButtonE9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonE9ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonE9);
    }//GEN-LAST:event_jButtonE9ActionPerformed

    private void jButtonF9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonF9ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonF9);
    }//GEN-LAST:event_jButtonF9ActionPerformed

    private void jButtonG9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonG9ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonG9);
    }//GEN-LAST:event_jButtonG9ActionPerformed

    private void jButtonH9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonH9ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonH9);
    }//GEN-LAST:event_jButtonH9ActionPerformed

    private void jButtonN1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonN1ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonN1);
    }//GEN-LAST:event_jButtonN1ActionPerformed

    private void jButtonM1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonM1ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonM1);
    }//GEN-LAST:event_jButtonM1ActionPerformed

    private void jButtonO1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonO1ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonO1);
    }//GEN-LAST:event_jButtonO1ActionPerformed

    private void jButtonL1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonL1ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonL1);
    }//GEN-LAST:event_jButtonL1ActionPerformed

    private void jButtonK1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonK1ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonK1);
    }//GEN-LAST:event_jButtonK1ActionPerformed

    private void jButtonJ1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJ1ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonJ1);
    }//GEN-LAST:event_jButtonJ1ActionPerformed

    private void jButtonI1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonI1ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonI1);
    }//GEN-LAST:event_jButtonI1ActionPerformed

    private void jButtonI2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonI2ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonI2);
    }//GEN-LAST:event_jButtonI2ActionPerformed

    private void jButtonJ2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJ2ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonJ2);
    }//GEN-LAST:event_jButtonJ2ActionPerformed

    private void jButtonK2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonK2ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonK2);
    }//GEN-LAST:event_jButtonK2ActionPerformed

    private void jButtonM2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonM2ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonM2);
    }//GEN-LAST:event_jButtonM2ActionPerformed

    private void jButtonL2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonL2ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonL2);
    }//GEN-LAST:event_jButtonL2ActionPerformed

    private void jButtonN2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonN2ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonN2);
    }//GEN-LAST:event_jButtonN2ActionPerformed

    private void jButtonO2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonO2ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonO2);
    }//GEN-LAST:event_jButtonO2ActionPerformed

    private void jButtonO4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonO4ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonO4);
    }//GEN-LAST:event_jButtonO4ActionPerformed

    private void jButtonN4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonN4ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonN4);
    }//GEN-LAST:event_jButtonN4ActionPerformed

    private void jButtonM4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonM4ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonM4);
    }//GEN-LAST:event_jButtonM4ActionPerformed

    private void jButtonL4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonL4ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonL4);
    }//GEN-LAST:event_jButtonL4ActionPerformed

    private void jButtonK4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonK4ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonK4);
    }//GEN-LAST:event_jButtonK4ActionPerformed

    private void jButtonJ4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJ4ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonJ4);
    }//GEN-LAST:event_jButtonJ4ActionPerformed

    private void jButtonI4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonI4ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonI4);
    }//GEN-LAST:event_jButtonI4ActionPerformed

    private void jButtonI3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonI3ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonI3);
    }//GEN-LAST:event_jButtonI3ActionPerformed

    private void jButtonJ3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJ3ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonJ3);
    }//GEN-LAST:event_jButtonJ3ActionPerformed

    private void jButtonK3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonK3ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonK3);
    }//GEN-LAST:event_jButtonK3ActionPerformed

    private void jButtonL3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonL3ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonL3);
    }//GEN-LAST:event_jButtonL3ActionPerformed

    private void jButtonM3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonM3ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonM3);
    }//GEN-LAST:event_jButtonM3ActionPerformed

    private void jButtonN3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonN3ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonN3);
    }//GEN-LAST:event_jButtonN3ActionPerformed

    private void jButtonO3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonO3ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonO3);
    }//GEN-LAST:event_jButtonO3ActionPerformed

    private void jButtonO8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonO8ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonO8);
    }//GEN-LAST:event_jButtonO8ActionPerformed

    private void jButtonN8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonN8ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonN8);
    }//GEN-LAST:event_jButtonN8ActionPerformed

    private void jButtonM8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonM8ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonM8);
    }//GEN-LAST:event_jButtonM8ActionPerformed

    private void jButtonL8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonL8ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonL8);
    }//GEN-LAST:event_jButtonL8ActionPerformed

    private void jButtonK8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonK8ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonK8);
    }//GEN-LAST:event_jButtonK8ActionPerformed

    private void jButtonJ8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJ8ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonJ8);
    }//GEN-LAST:event_jButtonJ8ActionPerformed

    private void jButtonI8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonI8ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonI8);
    }//GEN-LAST:event_jButtonI8ActionPerformed

    private void jButtonI7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonI7ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonI7);
    }//GEN-LAST:event_jButtonI7ActionPerformed

    private void jButtonI6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonI6ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonI6);
    }//GEN-LAST:event_jButtonI6ActionPerformed

    private void jButtonI5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonI5ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonI5);
    }//GEN-LAST:event_jButtonI5ActionPerformed

    private void jButtonJ5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJ5ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonJ5);
    }//GEN-LAST:event_jButtonJ5ActionPerformed

    private void jButtonJ6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJ6ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonJ6);
    }//GEN-LAST:event_jButtonJ6ActionPerformed

    private void jButtonJ7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJ7ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonJ7);
    }//GEN-LAST:event_jButtonJ7ActionPerformed

    private void jButtonK7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonK7ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonK7);
    }//GEN-LAST:event_jButtonK7ActionPerformed

    private void jButtonK6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonK6ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonK6);
    }//GEN-LAST:event_jButtonK6ActionPerformed

    private void jButtonK5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonK5ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonK5);
    }//GEN-LAST:event_jButtonK5ActionPerformed

    private void jButtonL5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonL5ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonL5);
    }//GEN-LAST:event_jButtonL5ActionPerformed

    private void jButtonL6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonL6ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonL6);
    }//GEN-LAST:event_jButtonL6ActionPerformed

    private void jButtonL7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonL7ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonL7);
    }//GEN-LAST:event_jButtonL7ActionPerformed

    private void jButtonM7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonM7ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonM7);
    }//GEN-LAST:event_jButtonM7ActionPerformed

    private void jButtonM6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonM6ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonM6);
    }//GEN-LAST:event_jButtonM6ActionPerformed

    private void jButtonM5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonM5ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonM5);
    }//GEN-LAST:event_jButtonM5ActionPerformed

    private void jButtonN5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonN5ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonN5);
    }//GEN-LAST:event_jButtonN5ActionPerformed

    private void jButtonN6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonN6ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonN6);
    }//GEN-LAST:event_jButtonN6ActionPerformed

    private void jButtonN7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonN7ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonN7);
    }//GEN-LAST:event_jButtonN7ActionPerformed

    private void jButtonO7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonO7ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonO7);
    }//GEN-LAST:event_jButtonO7ActionPerformed

    private void jButtonO6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonO6ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonO6);
    }//GEN-LAST:event_jButtonO6ActionPerformed

    private void jButtonO5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonO5ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonO5);
    }//GEN-LAST:event_jButtonO5ActionPerformed

    private void jButtonO14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonO14ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonO14);
    }//GEN-LAST:event_jButtonO14ActionPerformed

    private void jButtonN14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonN14ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonN14);
    }//GEN-LAST:event_jButtonN14ActionPerformed

    private void jButtonM14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonM14ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonM14);
    }//GEN-LAST:event_jButtonM14ActionPerformed

    private void jButtonL14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonL14ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonL14);
    }//GEN-LAST:event_jButtonL14ActionPerformed

    private void jButtonK14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonK14ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonK14);
    }//GEN-LAST:event_jButtonK14ActionPerformed

    private void jButtonJ14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJ14ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonJ14);
    }//GEN-LAST:event_jButtonJ14ActionPerformed

    private void jButtonI14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonI14ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonI14);
    }//GEN-LAST:event_jButtonI14ActionPerformed

    private void jButtonI13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonI13ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonI13);
    }//GEN-LAST:event_jButtonI13ActionPerformed

    private void jButtonJ13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJ13ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonJ13);
    }//GEN-LAST:event_jButtonJ13ActionPerformed

    private void jButtonK13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonK13ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonK13);
    }//GEN-LAST:event_jButtonK13ActionPerformed

    private void jButtonL13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonL13ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonL13);
    }//GEN-LAST:event_jButtonL13ActionPerformed

    private void jButtonM13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonM13ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonM13);
    }//GEN-LAST:event_jButtonM13ActionPerformed

    private void jButtonN13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonN13ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonN13);
    }//GEN-LAST:event_jButtonN13ActionPerformed

    private void jButtonO13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonO13ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonO13);
    }//GEN-LAST:event_jButtonO13ActionPerformed

    private void jButtonO12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonO12ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonO12);
    }//GEN-LAST:event_jButtonO12ActionPerformed

    private void jButtonN12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonN12ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonN12);
    }//GEN-LAST:event_jButtonN12ActionPerformed

    private void jButtonM12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonM12ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonM12);
    }//GEN-LAST:event_jButtonM12ActionPerformed

    private void jButtonL12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonL12ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonL12);
    }//GEN-LAST:event_jButtonL12ActionPerformed

    private void jButtonK12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonK12ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonK12);
    }//GEN-LAST:event_jButtonK12ActionPerformed

    private void jButtonJ12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJ12ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonJ12);
    }//GEN-LAST:event_jButtonJ12ActionPerformed

    private void jButtonI12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonI12ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonI12);
    }//GEN-LAST:event_jButtonI12ActionPerformed

    private void jButtonI11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonI11ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonI11);
    }//GEN-LAST:event_jButtonI11ActionPerformed

    private void jButtonJ11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJ11ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonJ11);
    }//GEN-LAST:event_jButtonJ11ActionPerformed

    private void jButtonK11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonK11ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonK11);
    }//GEN-LAST:event_jButtonK11ActionPerformed

    private void jButtonL11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonL11ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonL11);
    }//GEN-LAST:event_jButtonL11ActionPerformed

    private void jButtonM11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonM11ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonM11);
    }//GEN-LAST:event_jButtonM11ActionPerformed

    private void jButtonN11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonN11ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonN11);
    }//GEN-LAST:event_jButtonN11ActionPerformed

    private void jButtonO11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonO11ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonO11);
    }//GEN-LAST:event_jButtonO11ActionPerformed

    private void jButtonO10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonO10ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonO10);
    }//GEN-LAST:event_jButtonO10ActionPerformed

    private void jButtonN10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonN10ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonN10);
    }//GEN-LAST:event_jButtonN10ActionPerformed

    private void jButtonM10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonM10ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonM10);
    }//GEN-LAST:event_jButtonM10ActionPerformed

    private void jButtonL10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonL10ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonL10);
    }//GEN-LAST:event_jButtonL10ActionPerformed

    private void jButtonK10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonK10ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonK10);
    }//GEN-LAST:event_jButtonK10ActionPerformed

    private void jButtonJ10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJ10ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonJ10);
    }//GEN-LAST:event_jButtonJ10ActionPerformed

    private void jButtonI10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonI10ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonI10);
    }//GEN-LAST:event_jButtonI10ActionPerformed

    private void jButtonI9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonI9ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonI9);
    }//GEN-LAST:event_jButtonI9ActionPerformed

    private void jButtonJ9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJ9ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonJ9);
    }//GEN-LAST:event_jButtonJ9ActionPerformed

    private void jButtonK9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonK9ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonK9);
    }//GEN-LAST:event_jButtonK9ActionPerformed

    private void jButtonL9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonL9ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonL9);
    }//GEN-LAST:event_jButtonL9ActionPerformed

    private void jButtonM9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonM9ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonM9);
    }//GEN-LAST:event_jButtonM9ActionPerformed

    private void jButtonN9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonN9ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonN9);
    }//GEN-LAST:event_jButtonN9ActionPerformed

    private void jButtonO9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonO9ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonO9);
    }//GEN-LAST:event_jButtonO9ActionPerformed

    private void jButtonO15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonO15ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonO15);
    }//GEN-LAST:event_jButtonO15ActionPerformed

    private void jButtonN15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonN15ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonN15);
    }//GEN-LAST:event_jButtonN15ActionPerformed

    private void jButtonM15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonM15ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonM15);
    }//GEN-LAST:event_jButtonM15ActionPerformed

    private void jButtonL15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonL15ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonL15);
    }//GEN-LAST:event_jButtonL15ActionPerformed

    private void jButtonK15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonK15ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonK15);
    }//GEN-LAST:event_jButtonK15ActionPerformed

    private void jButtonJ15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJ15ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonJ15);
    }//GEN-LAST:event_jButtonJ15ActionPerformed

    private void jButtonI15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonI15ActionPerformed
        // TODO add your handling code here:
        //handleBoardButtonClick(jButtonI15);
    }//GEN-LAST:event_jButtonI15ActionPerformed

    private void jButtonCR1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCR1ActionPerformed
        // TODO add your handling code here:
        //handleRackButtonClick(jButtonCR1);
    }//GEN-LAST:event_jButtonCR1ActionPerformed

    private void jButtonCR2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCR2ActionPerformed
        // TODO add your handling code here:
        //handleRackButtonClick(jButtonCR2);
    }//GEN-LAST:event_jButtonCR2ActionPerformed

    private void jButtonCR4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCR4ActionPerformed
        // TODO add your handling code here:
        //handleRackButtonClick(jButtonCR4);
    }//GEN-LAST:event_jButtonCR4ActionPerformed

    private void jButtonCR5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCR5ActionPerformed
        // TODO add your handling code here:
        //handleRackButtonClick(jButtonCR5);
    }//GEN-LAST:event_jButtonCR5ActionPerformed

    private void jButtonCR7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCR7ActionPerformed
        // TODO add your handling code here:
        //handleRackButtonClick(jButtonCR7);
    }//GEN-LAST:event_jButtonCR7ActionPerformed

    private void jButtonCR3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCR3ActionPerformed
        // TODO add your handling code here:
        //handleRackButtonClick(jButtonCR3);
    }//GEN-LAST:event_jButtonCR3ActionPerformed

    private void jButtonCR6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCR6ActionPerformed
        // TODO add your handling code here:
        //handleRackButtonClick(jButtonCR6);
    }//GEN-LAST:event_jButtonCR6ActionPerformed

    private void jButtonCPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCPlayActionPerformed
        // TODO add your handling code here:
        changePlayer();
    }//GEN-LAST:event_jButtonCPlayActionPerformed

    private void jButtonStartGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartGameActionPerformed
        // TODO add your handling code here:
        /*jStartGameBar.setVisible(true);
        startTimerAction = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                if(jStartGameBar.getValue()<100){
                    jStartGameBar.setValue(jStartGameBar.getValue()+1);
                }
                else{
                    jStartGameBar.setValue(0);
                    startTime.restart();
                }
            }
        };
        startTime = new Timer(1000,startTimerAction);
        startTime.start();*/
        jButtonStartGame.setVisible(false);
        jLabelStartGame.setVisible(true);
        //JOptionPane.showMessageDialog(this, "Starting the game");
        JPanel panel = new JPanel();
        //panel.add(new JRadioButton("Easy"));
        //panel.add(new JRadioButton("Normal"));
        //panel.add(new JRadioButton("Hard"));
        //panel.add(new JRadioButton("Ninja"));
        String[] options = {"Easy","Normal","Hard","Ninja"};
        int option = 1+ JOptionPane.showOptionDialog(null, "Choose Difficulty",
            "Choose Difficulty", JOptionPane.OK_OPTION,
            JOptionPane.QUESTION_MESSAGE, null,options, null);
        fin_difficulty *= option;
        System.out.println(fin_difficulty + " difficulty");
        try{
            verifier = new Verifier();
        }
        catch(Exception e){
            jLabelStartGame.setText("Issue with dictionary load");
            return;
        }
        if(verifier==null){
            jLabelStartGame.setText("Issue with dictionary load");
            return;
        }
        jLabelStartGame.setVisible(false);
        playVisibility(true);
        playTime.start();
        jButtonCPlay.setVisible(false);
        jButtonFillRack.setVisible(false);
        changePlayer();
        //jStartGameBar.setVisible(false);
    }//GEN-LAST:event_jButtonStartGameActionPerformed

    private void jButtonLetterB2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLetterB2ActionPerformed
        // TODO add your handling code here:
        String s = "";
        do{
            s= (String)JOptionPane.showInputDialog(
                this,
                "Enter single letter for B2") + "";
        }while(s.length()!=1);
        s=s.toUpperCase();
        System.out.println("B2 Letter:" + s);
        B2Temp = s.charAt(0);
        jButtonLetterB2.setText("B2 is " + s);
    }//GEN-LAST:event_jButtonLetterB2ActionPerformed

    private void jButtonLetterB1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLetterB1ActionPerformed
        // TODO add your handling code here:
        //JOptionPane.showMessageDialog(this, "Eggs are not supposed to be green.");
        String s = "";
        do{
            s= (String)JOptionPane.showInputDialog(
                this,
                "Enter single letter for B1") + "";
        }while(s.length()!=1);
        s=s.toUpperCase();
        System.out.println("B1 Letter:" + s);
        B1Temp = s.charAt(0);
        jButtonLetterB1.setText("B1 is " + s);
    }//GEN-LAST:event_jButtonLetterB1ActionPerformed

    private void playVisibility(boolean flag){
        /*jButtonPR1.setVisible(flag);
        jButtonPR2.setVisible(flag);
        jButtonPR3.setVisible(flag);
        jButtonPR4.setVisible(flag);
        jButtonPR5.setVisible(flag);
        jButtonPR6.setVisible(flag);
        jButtonPR7.setVisible(flag);
        jButtonPPlay.setVisible(flag);
        jButtonCPlay.setVisible(flag);
        jButtonCR1.setVisible(flag);
        jButtonCR2.setVisible(flag);
        jButtonCR3.setVisible(flag);
        jButtonCR4.setVisible(flag);
        jButtonCR5.setVisible(flag);
        jButtonCR6.setVisible(flag);
        jButtonCR7.setVisible(flag); */
        jLabelTimer.setVisible(flag); 
        jProgressBarTimer.setVisible(flag);
        jLabelCenterTileStatus.setVisible(flag);
        //jButtonLetterB1.setVisible(flag);
        //jButtonLetterB2.setVisible(flag);
        jLabelLetterRemainlabel.setVisible(flag);
        jLabelLetterRemainingVal.setVisible(flag);
        jPanel1.setVisible(flag);
        jPanel5.setVisible(flag);
        jButtonFillRack.setVisible(flag);
        jPanel7.setVisible(flag);
    }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MyScrabble1DD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MyScrabble1DD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MyScrabble1DD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MyScrabble1DD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MyScrabble1DD().setVisible(true);
            }
        });
    }

    public void checkReverse(){
        String rackString="";
        if(currPlayer==1){
            rackString = getRealLetter(jButtonPR1.letter) + getRealLetter(jButtonPR2.letter) +
                            getRealLetter(jButtonPR3.letter) + getRealLetter(jButtonPR4.letter) + 
                            getRealLetter(jButtonPR5.letter) + getRealLetter(jButtonPR6.letter) + 
                            getRealLetter(jButtonPR7.letter); 
        }
        else if(currPlayer==2){
            rackString = getRealLetter(jButtonCR1.letter) + getRealLetter(jButtonCR2.letter) +
                            getRealLetter(jButtonCR3.letter) + getRealLetter(jButtonCR4.letter) + 
                            getRealLetter(jButtonCR5.letter) + getRealLetter(jButtonCR6.letter) + 
                            getRealLetter(jButtonCR7.letter);            
        }
        if (rackString.length()==0){
            changePlayer();
        }
    }
    
    public boolean checkGameOver(){
        boolean result = false;
        String rackString1 = getRealLetter(jButtonPR1.letter) + getRealLetter(jButtonPR2.letter) +
                            getRealLetter(jButtonPR3.letter) + getRealLetter(jButtonPR4.letter) + 
                            getRealLetter(jButtonPR5.letter) + getRealLetter(jButtonPR6.letter) + 
                            getRealLetter(jButtonPR7.letter); 
        String rackString2 = getRealLetter(jButtonCR1.letter) + getRealLetter(jButtonCR2.letter) +
                            getRealLetter(jButtonCR3.letter) + getRealLetter(jButtonCR4.letter) + 
                            getRealLetter(jButtonCR5.letter) + getRealLetter(jButtonCR6.letter) + 
                            getRealLetter(jButtonCR7.letter);  
        //System.out.println(rackString1+":"+rackString2);
        //System.out.println(rackString1.length()+":"+rackString2.length());
        result = result || (rackString1.length()==0 && rackString2.length()==0);
        result = result || (rackString1.length()==0 && (playerScore[1]>playerScore[0] || playerScore[1]<1.5*playerScore[0]) );
        result = result || (rackString2.length()==0 && (playerScore[1]<playerScore[0] || playerScore[1]>1.5*playerScore[0]) );
        if (result){
            System.out.println("Empty racks");
            if(playerScore[0]>playerScore[1]){
                GameOver(1,"");
            }
            else if(playerScore[0]==playerScore[1]){
                GameOver(3,"");
            }    
            else if(playerScore[0]<playerScore[1]){
                GameOver(2,"");
            }            
        }
        return result;
        
    }
    
    
    private void GameOver(int ww, String reason){
        playVisibility(false);
        jLabelStartGame.setVisible(true);
        jLabelStartGame.setText("Close the game window now");
        String eol = System.getProperty("line.separator");
        String output = "Game Over" + eol + "Final Scores are:" + eol;
        output +=  "Player1 Score:" + playerScore[0] + eol;
        output +=  "Computer Wins Score:" + playerScore[1] + eol;
        if(ww==1){
            output += "Player1 Wins " + reason + eol + " ";
        }
        else if(ww==2){
            output += "Computer Wins" + reason + eol + " " + eol;
        }        
        else if(ww==3){
            output += "Game tied" + reason + eol + " " + eol;
        }
        JOptionPane.showMessageDialog(this, output);  
    }
    public String getRealLetter(char l){
        if (l==0){return "";}
        return "" + l;
    }
    
    public SwingWorker getHintWorker(){
        SwingWorker worker = new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() {
                /*for(int i=10; i>0; i--){
                    try{
                        Thread.sleep(1000);
                    }
                    catch (Exception e){
                        System.out.println("can't sleep in getWorker");
                        i=-1;
                    }
                    System.out.println("getWorker i=" + i);
                }*/
                getHint();
                return null;
            }

            @Override
            public void done() {
                System.out.println("done");
            }
        };
        return worker;
    }
    
  
    public SwingWorker getCPlayWorker(){
        SwingWorker worker = new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() {
                /*for(int i=10; i>0; i--){
                    try{
                        Thread.sleep(1000);
                    }
                    catch (Exception e){
                        System.out.println("can't sleep in getWorker");
                        i=-1;
                    }
                    System.out.println("getWorker i=" + i);
                }*/
                playComputer();
                return null;
            }

            @Override
            public void done() {
                System.out.println("done");
            }
        };
        return worker;
    }
    
    
    private void getHint(){
        //SwingWorker worker = getWorker();
        //worker.execute();
        String rackString = "" + getRealLetter(jButtonPR1.letter) + getRealLetter(jButtonPR2.letter) +
                            getRealLetter(jButtonPR3.letter) + getRealLetter(jButtonPR4.letter) + 
                            getRealLetter(jButtonPR5.letter) + getRealLetter(jButtonPR6.letter) + 
                            getRealLetter(jButtonPR7.letter); 
        rackString = rackString.replaceAll("\\s","");
        System.out.println("Getting Hint now:" + moveNum + " with rack=" + rackString);     
        Placement temp_place, best_bet = new Placement();
        boolean stop = true;
        for (int j=0; j<15 && stop; j++){
            for (int i=0;i<15 && stop;i++){
                if(finalBoard[j][i]!=' '){
                    //String c = "" +  finalBoard[j][i];
                    //c = c.replace('^',B1Fin).replace('_',B2Fin);
                    //System.out.println("j=" + j +" i=" + i + " value=" + c);
                    stop = jProgressBarTimer.getValue()<20?false:true;
                    temp_place = new Placement();
                    findSpace(j,i,rackString,temp_place);
                    if(temp_place.score > best_bet.score){
                        best_bet=temp_place;
                        //best_bet.print();
                    }  
                }             
            }
        }
        if(best_bet.score>0){
            jLabelStartGame.setVisible(true);
            jLabelStartGame.setText(best_bet.word);
        }
        if(finalBoard[7][7]!=' '){
            return;
        }
        findCSpace(7,7,true,true,rackString,best_bet);
        //best_bet.print();  
        if(best_bet.score>0){
            jLabelStartGame.setVisible(true);
            jLabelStartGame.setText(best_bet.word);
        }    
        //worker.cancel(true);
    }
    
    private void playComputer(){
        //SwingWorker worker = getWorker();
        //worker.execute();
        String rackString = getRealLetter(jButtonCR1.letter) + getRealLetter(jButtonCR2.letter) +
                            getRealLetter(jButtonCR3.letter) + getRealLetter(jButtonCR4.letter) + 
                            getRealLetter(jButtonCR5.letter) + getRealLetter(jButtonCR6.letter) + 
                            getRealLetter(jButtonCR7.letter);
        rackString = rackString.replaceAll("\\s","");        
        System.out.println("Computer plays now:" + moveNum + " with rack=" + rackString);     
        Placement temp_place, best_bet = new Placement();
        for (int j=0; j<15; j++){
            for (int i=0;i<15;i++){
                if(finalBoard[j][i]!=' '){
                    //String c = "" +  finalBoard[j][i];
                    //c = c.replace('^',B1Fin).replace('_',B2Fin);
                    //System.out.println("j=" + j +" i=" + i + " value=" + c);
                    temp_place = new Placement();
                    findSpace(j,i,rackString,temp_place);
                    if(temp_place.score > best_bet.score){
                        best_bet=temp_place;
                    }
                }
            }
        }
        if(best_bet!=null){
            ImplementMove(best_bet);
        }
        if(finalBoard[7][7]!=' '){
            //changePlayer();
            cPlayDelay=2;
            cTime1.restart();
            return;
        }
        System.out.println("Computer plays first move");
        findCSpace(7,7,true,true,rackString,best_bet);
        if(best_bet!=null){ImplementMove(best_bet);}
        cPlayDelay=2; 
        //worker.cancel(true);
        cTime1.restart();
        //changePlayer();
       
    }
    
    private DraggableButton findMyRackButt(char c){
        DraggableButton myRackButt= null,temp;
        for (int i=1; i<8; i++){
            temp = (DraggableButton) CButtonList[i];
            if(temp.covered && ("" + temp.letter).replace('^','E').replace('_','E').charAt(0)==c){
                myRackButt=(DraggableButton)CButtonList[i];
            }
        }
        return myRackButt;
    }
    private void makeMovement(DraggableButton myRackButt,DraggableButton myBoardButt){
        System.out.println("Moving tile, source = " + myRackButt + " to dest = " + myBoardButt);
        //myBoardButt.setDisabledIcon(myBoardButt.getIcon());
        myBoardButt.setFrontIcon(myRackButt.front);        
        
        //myBoardButt.setToolTipText(myRackButt.getText()); 
        myBoardButt.letter = myRackButt.letter;
        myBoardButt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 0)));
        //myBoardButt.setText("filled");
        
        myRackButt.setIcon(myRackButt.back);
        myRackButt.covered = false;
        myRackButt.letter = 0;
        //myRackButt.setText("");
        //tList.add(myBoardButt);
        fList.add(myBoardButt);  
        
        if(myBoardButt.letter=='^'){
            B1Temp='E';
            jButtonLetterB1.setText("B1 is E");
        }
        if(myBoardButt.letter=='_'){
            B2Temp='E';
            jButtonLetterB2.setText("B2 is E");
        }
        
        try{
            Thread.sleep(1500);
        }
        catch (Exception e){
            System.out.println("can't sleep in makeMovement");            
        }
        
    }
    
    private void ImplementMove(Placement best_bet){
        int fi,fj;
        best_bet.print();
        if(best_bet.score<1){
            return;
        }
        DraggableButton myRackButt, myBoardButt;
        fi=(best_bet.dir==1)?best_bet.bi:best_bet.bi_start;
        fj=(best_bet.dir==2)?best_bet.bj:best_bet.bj_start;
        if (best_bet.dir==1){
            for(int i = 0; i<best_bet.word.length();i++,fj++){
                if(finalBoard[fj][fi]==' '){
                    myBoardButt = GameBoard[fj][fi];
                    myRackButt = findMyRackButt(best_bet.word.charAt(i));
                    if(myRackButt==null){
                        System.out.println("Some problem occured in finding Rack button");
                        return;
                    }
                    makeMovement(myRackButt,myBoardButt); 
                }
            }
        }
        else if(best_bet.dir==2){
            for(int i = 0; i<best_bet.word.length();i++,fi++){
                if(finalBoard[fj][fi]==' '){                
                    myBoardButt = GameBoard[fj][fi];
                    myRackButt = findMyRackButt(best_bet.word.charAt(i));
                    if(myRackButt==null){
                        System.out.println("Some problem occured in finding Rack button");
                        return;
                    }
                    makeMovement(myRackButt,myBoardButt);   
                    if(fj==7 && fi==7){
                        centerFilled = true;
                    }
                }
            }
        }
    }
    
    private boolean tileNotExists(int bj, int bi){
        if(bj<0 || bj>=15 || bi<0 || bi>=15){return true;}
        return false;
    }
    
    
    private void findCSpace(int bj, int bi, boolean right, boolean left, String rackString, Placement best_bet){
        int right_margin = 0, left_margin =0;
        ArrayList wL = new ArrayList();
        while(right){
            right_margin++;
            right = isFreeToPlay(bj, bi+right_margin+1, 4);
        }
        while(left){
            left_margin++;
            left = isFreeToPlay(bj, bi-left_margin-1, 2);
        }
        
        rackString = rackString.replace('^','E').replace('_','E').toUpperCase();
        //System.out.println("j=" + bj +" i=" + bi + " value=" + c + " right_margin=" + right_margin + " left_margin=" + left_margin);   
        try{
        verifier.printAllWords(rackString,wL);
        }
        catch(Exception e){
            System.out.println("PrintWordsException" + e.toString());
        }
        //System.out.println("all words = " + wL.size());
        int max_score=0,temp_score,bi_start=0,bi_end=0,bi_start_max=0,bi_end_max=0,iAt;
        String wordSelected="";
        for (int i=0; wL!=null && i<wL.size(); i++){
            iAt=doesItFit("" + ((String)wL.get(i)).charAt(((String)wL.get(i)).length()/2),(String)wL.get(i),left_margin,right_margin);
            if(iAt>=0){
                //System.out.print(wL.get(i) + ",");
                bi_start=bi-iAt;
                bi_end=bi_start+((String)wL.get(i)).length()-1;
                temp_score=tellTentativeScore((String)wL.get(i),bj,bi,bi_start);
                if(temp_score>max_score && temp_score<difficulty){
                    wordSelected=(String)wL.get(i);
                    max_score=temp_score;
                    bi_start_max=bi_start;
                    bi_end_max=bi_end;                    
                }
            }
        }
        /*System.out.println("j=" + bj +" i=" + bi + "word = " 
                           + wordSelected + " with score=" + max_score
                           + " bi_start=" + bi_start_max + " bi_end=" + bi_end_max );*/
        best_bet.set(wordSelected,bj,bi,bi_start_max,2,max_score);
    }
        
    private void findHSpace(int bj, int bi, boolean right, boolean left, String rackString,Placement temp_place){
        int right_margin = 0, left_margin =0;
        ArrayList wL = new ArrayList();
        while(right){
            right_margin++;
            right = isFreeToPlay(bj, bi+right_margin+1, 4);
        }
        while(left){
            left_margin++;
            left = isFreeToPlay(bj, bi-left_margin-1, 2);
        }
        String c = "" +  finalBoard[bj][bi];
        c = c.replace('^',B1Fin).replace('_',B2Fin).toUpperCase();
        rackString = rackString.replace('^','E').replace('_','E').toUpperCase();
        //System.out.println("j=" + bj +" i=" + bi + " value=" + c + " right_margin=" + right_margin + " left_margin=" + left_margin);   
        try{
        verifier.printAllWords(rackString+c,wL);
        }
        catch(Exception e){
            System.out.println("PrintWordsException" + e.toString());
        }
        //System.out.println("all words = " + wL.size());
        int max_score=0,temp_score,bi_start=0,bi_end=0,bi_start_max=0,bi_end_max=0,iAt;
        String wordSelected="";
        for (int i=0; wL!=null && i<wL.size(); i++){
            iAt=doesItFit(c,(String)wL.get(i),left_margin,right_margin);
            if(iAt>=0){
                //System.out.print(wL.get(i) + ",");
                bi_start=bi-iAt;
                bi_end=bi_start+((String)wL.get(i)).length()-1;
                temp_score=tellTentativeHScore((String)wL.get(i),bj,bi,bi_start);
                if(temp_score>max_score && temp_score<difficulty){
                    wordSelected=(String)wL.get(i);
                    max_score=temp_score;
                    bi_start_max=bi_start;
                    bi_end_max=bi_end;                    
                }
            }
        }
        /*System.out.println("j=" + bj +" i=" + bi + " value=" + c + "word = " 
                           + wordSelected + " with score=" + max_score
                           + " bi_start=" + bi_start_max + " bi_end=" + bi_end_max );*/
        temp_place.set(wordSelected,bj,bi,bi_start_max,2,max_score);
    }

    private int tellTentativeHScore(String ipWord, int bj, int bi, int bi_start){
        int score=0, multiplier=1;
        ipWord=ipWord.toUpperCase();
        //score = ipWord.length();
        //mainWordScore = mainWordScore + letterValue[(int)tempBoard[j][i] - (int)'A']
        for(int i=0;i<ipWord.length();i++,bi_start++){            
            if(finalBoard[bj][bi_start]==' '){
                score +=  letterValue[(int)ipWord.charAt(i) - (int)'A']*letterMult[bj][bi_start];
                multiplier *= wordMult[bj][bi_start];
            }
            else{
                score += letterValue[(int)finalBoard[bj][bi_start] - (int)'A'];
            }            
        }
        return score*multiplier;
    }
    private int doesItFit(String cip, String ipWord, int before, int after){
        char c = cip.charAt(0);
        int result = -1;
        
        for (int i=0; i<ipWord.length() ; i++){
            if(ipWord.charAt(i)==c){
                if(i<=before && (ipWord.length()-1-i)<=after){
                    result = i;
                }
            }
        }
        
        return result;
    }
    
    private int tellTentativeScore(String ipWord, int bj, int bi, int bi_start){
        int score=0,multiplier=1;
        ipWord=ipWord.toUpperCase();
        //score = ipWord.length();
        //mainWordScore = mainWordScore + letterValue[(int)tempBoard[j][i] - (int)'A']
        for(int i=0;i<ipWord.length();i++){            
                score +=  letterValue[(int)ipWord.charAt(i) - (int)'A']*letterMult[bj][bi_start];
                multiplier *= wordMult[bj][bi_start];
             
        }
        return score*multiplier;
    }
    private void findVSpace(int bj, int bi, boolean above, boolean down,String rackString,Placement temp_place){
        int above_margin = 0, down_margin =0;
        ArrayList wL = new ArrayList();
        while(above){
            above_margin++;
            above = isFreeToPlay(bj-above_margin-1, bi, 1);
        }
        while(down){
            down_margin++;
            down = isFreeToPlay(bj+down_margin+1, bi, 3);
        }
        String c = "" +  finalBoard[bj][bi];
        c = c.replace('^',B1Fin).replace('_',B2Fin).toUpperCase();
        rackString = rackString.replace('^','E').replace('_','E').toUpperCase();
        //System.out.println("j=" + bj +" i=" + bi + " value=" + c + " above_margin=" + above_margin + " down_margin=" + down_margin);        
        try{
        verifier.printAllWords(rackString+c,wL);
        }
        catch(Exception e){
            System.out.println("PrintWordsException" + e.toString());
        }
        //System.out.println("all words = " + wL.size());
        int max_score=0,temp_score,bj_start=0,bj_end=0,bj_start_max=0,bj_end_max=0,iAt;
        String wordSelected="";
        for (int i=0; wL!=null && i<wL.size(); i++){
            iAt=doesItFit(c,(String)wL.get(i),above_margin,down_margin);
            if(iAt>=0){
                //System.out.print(wL.get(i) + ",");
                bj_start=bj-iAt;
                bj_end=bj_start+((String)wL.get(i)).length()-1;                
                temp_score=tellTentativeVScore((String)wL.get(i),bj,bi,bj_start);
                if(temp_score>max_score && temp_score<difficulty){
                    wordSelected=(String)wL.get(i);
                    max_score=temp_score;
                    bj_start_max=bj_start;
                    bj_end_max=bj_end;
                }
            }
        }
        /*System.out.println("j=" + bj +" i=" + bi + " value=" + c + "word = " 
                           + wordSelected + " with score=" + max_score
                           + " bj_start=" + bj_start_max + " bj_end=" + bj_end_max );*/
        temp_place.set(wordSelected,bj,bi,bj_start_max,1,max_score);
    }
    
    private int tellTentativeVScore(String ipWord, int bj, int bi, int bj_start){
        int score=0, multiplier=1;
        ipWord=ipWord.toUpperCase();
        //score = ipWord.length();
        //mainWordScore = mainWordScore + letterValue[(int)tempBoard[j][i] - (int)'A']
        for(int i=0;i<ipWord.length();i++,bj_start++){
            try{
                if(finalBoard[bj_start][bi]==' '){
                    score +=  letterValue[(int)ipWord.charAt(i) - (int)'A']*letterMult[bj_start][bi];
                    multiplier *= wordMult[bj_start][bi];
                }
                else{
                    score += letterValue[(int)finalBoard[bj_start][bi] - (int)'A'];
                }
            }
            catch(Exception e){
                System.out.println("j=" + bj +" i=" + bi + " bj_start=" + bj_start);
            }
            
        }
        return score*multiplier;
    }
    
    private void findSpace(int bj, int bi, String rackString,Placement temp_place){
        boolean hor_free=true,ver_free=true;
        boolean above, down, right, left;
        above = isFreeToPlay(bj-1, bi, 1);
        down = isFreeToPlay(bj+1, bi, 3);
        right = isFreeToPlay(bj, bi+1, 4);
        left = isFreeToPlay(bj, bi-1, 2);
        
        boolean above_full, down_full, right_full, left_full;
        above_full = bj>0?(finalBoard[bj-1][bi]!=' '):false;
        down_full = bj<14?(finalBoard[bj+1][bi]!=' '):false;
        left_full = bi>0?(finalBoard[bj][bi-1]!=' '):false;
        right_full = bi<14?(finalBoard[bj][bi+1]!=' '):false;
        
        
        if( above_full || down_full ) {ver_free=false;}
        if(left_full || right_full) {hor_free = false;}
        
        if (!(hor_free || ver_free)){return;}
        if(!(above || down || right || left)){return;}
        if(hor_free && !(right || left)){return;}
        if(ver_free && !(above || down)){return;}
        
        if(hor_free){findHSpace(bj,bi,right,left,rackString,temp_place);}
        if(ver_free){findVSpace(bj,bi,above,down,rackString,temp_place);}
        //String c = "" +  finalBoard[bj][bi];
        //c = c.replace('^',B1Fin).replace('_',B2Fin).toUpperCase();
        //System.out.println("j=" + bj +" i=" + bi + " value=" + c + " hor_free=" + hor_free + " ver_free=" + ver_free);             
    }
    
    private boolean isFreeToPlay(int bj, int bi, int dir){
        boolean thisOne, above, down, right, left;
        //System.out.println(bj + ":" + bi + ":" + dir);
        if(tileNotExists(bj,bi)){return false;}
        
        thisOne = (finalBoard[bj][bi]==' ');
        above = (dir==3)?true:bj>0?(finalBoard[bj-1][bi]==' '):true;
        down = (dir==1)?true:bj<14?(finalBoard[bj+1][bi]==' '):true;
        right = (dir==2)?true:bi<14?(finalBoard[bj][bi+1]==' '):true;
        left = (dir==4)?true:bi>0?(finalBoard[bj][bi-1]==' '):true;
        
        return thisOne&&above&&down&&right&&left;
    }
    
    
    public void PlayerEnable(javax.swing.JButton ButtonList[]){
        for (int i=0; i<8; i++){
            ButtonList[i].setEnabled(true);
        }        
    }
    
    public void PlayerDisable(javax.swing.JButton ButtonList[]){
        //System.out.println("here");
        //System.out.println(ButtonList.length);
        //System.out.println(ButtonList[0]);
        for (int i=0; i<8; i++){
            ButtonList[i].setEnabled(false);
            //System.out.println("here");
        }        
    }
    
    private void fillRack(){
        fillRackButton(jButtonPR1);
        fillRackButton(jButtonPR2);
        fillRackButton(jButtonPR3);
        fillRackButton(jButtonPR4);
        fillRackButton(jButtonPR5);
        fillRackButton(jButtonPR6);
        fillRackButton(jButtonPR7);
        
        fillRackButton(jButtonCR1);
        fillRackButton(jButtonCR2);
        fillRackButton(jButtonCR3);
        fillRackButton(jButtonCR4);
        fillRackButton(jButtonCR5);
        fillRackButton(jButtonCR6);
        fillRackButton(jButtonCR7);
        
        jLabelLetterRemainingVal.setText(String.valueOf(endLetters+1));
    }

    private void fillRackButton(DraggableButton jButtonRack){
        
        //String base = "file:/C:/Users/Adheesh/36CS/Netbeansdev/MyScrabble1/build/classes/";
        //String baseBlank = "blankRack.jpg";
        
        //System.out.println(jButtonRack.getIcon().toString());
        //System.out.println(base + baseBlank );
        //jButtonRack.getIcon().toString().equals(base + baseBlank )
        if (!jButtonRack.covered){//(isButtonBlank(jButtonRack)){
            //System.out.println("Its blank");
            //System.out.println("infillRackButton, enter if rack is blank. funtion returns: " + isButtonBlank(jButtonRack));
            char iGotChar = get_letter();
            if (iGotChar=='!') return;
            String letterTile  =  "/Letter" + iGotChar + ".jpg";
            //System.out.println(letterTile);
            jButtonRack.setFrontIcon(new javax.swing.ImageIcon(getClass().getResource(letterTile)));
            jButtonRack.setLetter(iGotChar);
            //System.out.println(jButtonRack.getText());
        }
    }
    
    private void formFList(){
        fList.clear();
        for(int i=0;i<15;i++)
            for(int j=0; j<15; j++){
                if(GameBoard[i][j].isEnabled() && GameBoard[i][j].covered){
                    fList.add(GameBoard[i][j]);
                    if(jButtonH8==GameBoard[i][j]){
                        centerFilled = true;
                        jLabelCenterTileStatus.setText("");
                    }
                }
            }        
    }
    
    private void reversePlay(){
        for(int i=1;i<8;i++){
            //System.out.println(i);
            //CurrentButtonList[i].getIcon().toString().equals("file:/C:/Users/Adheesh/36CS/Netbeansdev/MyScrabble1/build/classes/blankRack.jpg")
            if((fList.size() > 0) && !((DraggableButton)CurrentButtonList[i]).covered){
                //System.out.println("inreversePlay, enter if rack is blank. funtion returns: " + isButtonBlank(CurrentButtonList[i]));

                DraggableButton jBoardButton = fList.get(fList.size()-1);
                DraggableButton jRackButton = (DraggableButton)CurrentButtonList[i];                

                if (B1Temp!='^' && B1Fin=='^'){
                    jButtonLetterB1.setEnabled(false);
                    jButtonLetterB1.setForeground(Color.gray);
                    B1Temp='^';
                    jButtonLetterB1.setText("B1 Letter");
                }
                if (B2Temp!='_' && B2Fin=='_'){
                    jButtonLetterB2.setEnabled(false);
                    jButtonLetterB2.setForeground(Color.gray);
                    B2Temp='_';
                    jButtonLetterB2.setText("B2 Letter");
                }
                jLabelEmptyTileStatus.setVisible(false);                  


                //System.out.println(jBoardButton);
                //System.out.println(jBoardButton.getLocation().x);
                //System.out.println(jBoardButton.getLocation().y);
                //System.out.println(jRackButton);

                jRackButton.setFrontIcon(jBoardButton.front);
                jRackButton.setLetter(jBoardButton.letter);
                fList.remove(jBoardButton);
                jBoardButton.setIcon(jBoardButton.back);
                jBoardButton.covered=false;
                jBoardButton.setLetter((char)0);                
                jBoardButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                //jBoardButton.setToolTipText("");
                if(jButtonH8==jBoardButton){
                    centerFilled = false;
                    System.out.println("center empty");
                    jLabelCenterTileStatus.setText("Fill Center Tile, else the turn will be void!");
                }
            }
        }
        System.out.println(fList.size());
        
    }
    
        
    private boolean findValidate(){
        //System.out.println(tList.size());
        //System.out.println(fList.size());
        //Center is filled
        currScore = 0;
        int mainWordScore=0,secWordScore=0,finScore=0;
        if (!centerFilled){
            return false;
        }
        //are new tiles in one line?
        int dir = inOneLine();
        if (dir==0){
            return false;
        }
        //Adjecency        
        System.out.println("moveNum=" + moveNum);
        System.out.println(checkAdj());
        if (moveNum>1 && !(checkAdj())){
            return false;
        }
        //are the contiguous? consider old tiles as well.
        if (dir==1 || dir==2){
            mainWordScore = mainWordScore(dir);
            if (mainWordScore==-1){
                return false;
            }
            secWordScore = secWordScore(dir);
            if (secWordScore==-1){
                return false;
            }
            //dispTempBoard();
        }
        else if (dir==3){
            if (moveNum==1){
                return false;
            }
            secWordScore = singLetterScore();
            if (secWordScore==-1){
                return false;
            }
        }
        else{
            return false;
        }
        finScore = mainWordScore + secWordScore;
        System.out.println("final score:  " + finScore);
        currScore = finScore;
        return true;
    }

    
    private int mainWordScore(int dir){
        //first letter
        int fx=-1,fy=-1,lx=-1,ly=-1;
        boolean begin=true;
        String mainWord = "";
        int mainWordScore=0,wordMultiplier=1;
        for (int j=0; j<15 && fx<0; j++){
            for (int i=0;i<15 && fy<0;i++){
                //System.out.print("|" + tempBoard[j][i]);
                if (tempBoard[j][i]!=' '){
                    fx = i;
                    fy = j;
                }
            }
        }
        if (dir==1){            
            for(int j=14, i=fx;j>=fy;j--){
                if(begin && tempBoard[j][i]!=' '){
                    begin = false;
                    ly=j;
                }
                if (!begin){
                    if(tempBoard[j][i]!=' '){
                        mainWord = tempBoard[j][i] + mainWord;
                        mainWordScore = mainWordScore + letterValue[(int)tempBoard[j][i] - (int)'A']*letterMult[j][i];
                        wordMultiplier=wordMultiplier*wordMult[j][i];
                    }
                    else if (finalBoard[j][i]!=' '){
                        mainWord = Character.toLowerCase(finalBoard[j][i]) + mainWord;
                        mainWordScore = mainWordScore + letterValue[(int)finalBoard[j][i] - (int)'A'];
                    }
                    else{
                        return -1;
                    }
                    
                }
            }
            for(int j=fy-1, i=fx;j>-1 && finalBoard[j][i]!=' ';j--){
                mainWord = Character.toLowerCase(finalBoard[j][i]) + mainWord;
                mainWordScore = mainWordScore + letterValue[(int)finalBoard[j][i] - (int)'A'];
            }
            for(int j=ly+1, i=fx;j<15 && finalBoard[j][i]!=' ';j++){
                mainWord = mainWord + Character.toLowerCase(finalBoard[j][i]);
                mainWordScore = mainWordScore + letterValue[(int)finalBoard[j][i] - (int)'A'];
            }     
            //System.out.println(mainWord + mainWord.length());
        }
        //ystem.out.println(mainWord + mainWord.length());
        if (dir==2){
            for(int i=14, j=fy;i>=fx;i--){                
                if(begin && tempBoard[j][i]!=' '){
                    begin = false;
                    lx=i;
                }
                if (!begin){
                    if(tempBoard[j][i]!=' '){
                        mainWord = tempBoard[j][i] + mainWord;
                        mainWordScore = mainWordScore + letterValue[(int)tempBoard[j][i] - (int)'A']*letterMult[j][i];
                        wordMultiplier=wordMultiplier*wordMult[j][i];
                    }
                    else if (finalBoard[j][i]!=' '){
                        mainWord = Character.toLowerCase(finalBoard[j][i]) + mainWord;
                        mainWordScore = mainWordScore + letterValue[(int)finalBoard[j][i] - (int)'A'];
                    }
                    else{
                        return -1;
                    }
                }
            }
            for(int i=fx-1, j=fy;i>-1 && finalBoard[j][i]!=' ';i--){
                mainWord = Character.toLowerCase(finalBoard[j][i]) + mainWord;
                mainWordScore = mainWordScore + letterValue[(int)finalBoard[j][i] - (int)'A'];
            }
            //System.out.println("lx=" + lx + " finalBoard[lx+1][fy]" + finalBoard[lx+1][fy]);
            for(int i=lx+1, j=fy;i<15 && finalBoard[j][i]!=' ';i++){
                mainWord = mainWord + Character.toLowerCase(finalBoard[j][i]);
                mainWordScore = mainWordScore + letterValue[(int)finalBoard[j][i] - (int)'A'];
            }             
        }
        mainWordScore*=wordMultiplier;
        mainWord = mainWord.replace('^',B1Temp).replace('_',B2Temp).replace('^',B1Fin).replace('_',B2Fin);
        //System.out.println("mainWord=" + mainWord);
        if(verifyWord(mainWord.toUpperCase())){            
            System.out.println("mainWordScore=" + mainWordScore);
            return mainWordScore;
        }
        //System.out.println("mainWord to verify=" + mainWord.toUpperCase());
        return -1;
    }

    private boolean verifyWord(String inWord){
        boolean isPresent=false;
        System.out.println("Word recieved for validation = " + inWord);
        if (inWord.contains("^") || inWord.contains("_") || inWord.length()==0 || inWord.length()==1 || !inWord.toUpperCase().equals(inWord)){
            return false;
        }
        System.out.println("Word passed to validation = " + inWord);
        try{
            isPresent = verifier.verify(inWord);
        }
        catch(Exception e){
            
        }
           
        return(isPresent);
        //return true;
    }
    
    private int perpWordScore(int fx, int fy, int dir){
        if (dir==1){
            dir=2;
        }
        else if(dir==2){
            dir=1;
        }
        else{
            return -1;
        }
        int perpWordScore=0,wordMultiplier=1;
        String perpWord = "" + tempBoard[fy][fx];
        perpWordScore = perpWordScore + letterValue[(int)tempBoard[fy][fx] - (int)'A']*letterMult[fy][fx];
        wordMultiplier=wordMultiplier*wordMult[fy][fx];
        if (dir==1){            
            for(int j=fy-1, i=fx;j>-1 && finalBoard[j][i]!=' ';j--){
                perpWord = Character.toLowerCase(finalBoard[j][i]) + perpWord;
                perpWordScore = perpWordScore + letterValue[(int)finalBoard[j][i] - (int)'A'];
            }
            for(int j=fy+1, i=fx;j<15 && finalBoard[j][i]!=' ';j++){
                perpWord = perpWord + Character.toLowerCase(finalBoard[j][i]);
                perpWordScore = perpWordScore + letterValue[(int)finalBoard[j][i] - (int)'A'];
            }     
        }
        //ystem.out.println(mainWord + mainWord.length());
        if (dir==2){
            for(int i=fx-1, j=fy;i>-1 && finalBoard[j][i]!=' ';i--){
                perpWord = Character.toLowerCase(finalBoard[j][i]) + perpWord;
                perpWordScore = perpWordScore + letterValue[(int)finalBoard[j][i] - (int)'A'];
            }
            for(int i=fx+1, j=fy;i<15 && finalBoard[j][i]!=' ';i++){
                perpWord = perpWord + Character.toLowerCase(finalBoard[j][i]);
                perpWordScore = perpWordScore + letterValue[(int)finalBoard[j][i] - (int)'A'];
            }             
        }
        perpWordScore*=wordMultiplier;
        perpWord = perpWord.replace('^',B1Temp).replace('_',B2Temp).replace('^',B1Fin).replace('_',B2Fin);
        //System.out.println("perpWord=" + perpWord);
        //System.out.println("perpWord to verify=" + perpWord.toUpperCase());
        if (perpWord.length()>1){ 
            System.out.println("perpWordScore=" + perpWordScore);
            if(verifyWord(perpWord.toUpperCase())){           
                System.out.println("perpWordScore=" + perpWordScore);
                return perpWordScore;
            }
            return -1;
        }
        return 0;
    }
    
    private int secWordScore(int dir){
        int secWordScore = 0, perpWordScore;
        for (int j=0; j<15; j++){
            for (int i=0;i<15;i++){
                //System.out.print("|" + tempBoard[j][i]);
                if (tempBoard[j][i]!=' '){
                    perpWordScore = perpWordScore(i,j,dir);
                    if (perpWordScore==-1){
                        return -1;
                    }
                    secWordScore = secWordScore + perpWordScore;
                }
            }
        }
        System.out.println("secWordScore=" + secWordScore);
        return secWordScore;
    }
    
    private int singLetterScore(){
        int secWordScore = 0, perpWordScore;
        for (int j=0; j<15; j++){
            for (int i=0;i<15;i++){
                //System.out.print("|" + tempBoard[j][i]);
                if (tempBoard[j][i]!=' '){
                    perpWordScore = perpWordScore(i,j,1);
                    if (perpWordScore==-1){
                        return -1;
                    }
                    secWordScore = secWordScore + perpWordScore;
                    perpWordScore = perpWordScore(i,j,2);
                    if (perpWordScore==-1){
                        return -1;
                    }
                    secWordScore = secWordScore + perpWordScore;   
                    System.out.println("secWordScore=" + secWordScore);
                    return secWordScore;
                }
            }
        }
        return -1;
    }
    
    public boolean checkAdj(){
        for (int j=0; j<15; j++){
            for (int i=0;i<15;i++){
                //tempBoard[j][i]
                if(tempBoard[j][i]!= ' '){
                    if(i>0){
                        if(finalBoard[j][i-1]!=' '){
                            return true;
                        }
                    }
                    if(i<14){
                        if(finalBoard[j][i+1]!=' '){
                            return true;
                        }
                    }
                    if(j>0){
                        if(finalBoard[j-1][i]!=' '){
                            return true;
                        }
                    }
                    if(j<14){
                        if(finalBoard[j+1][i]!=' '){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
        
    private int inOneLine(){
        
        Iterator<DraggableButton> iter= fList.iterator();
        DraggableButton currButton;
        int x=-1,y=-1,dir = -1,tx,ty;
        tempBoard = getBlankBoard();

        while(iter.hasNext()){
            currButton = iter.next();
            tx=currButton.getLocation().x;
            ty=currButton.getLocation().y;
            //System.out.println("dir=" +  dir + " tx=" + tx + " ty=" + ty);
            tempBoard[ty/30][tx/30] = currButton.letter;

            if (dir == 1){
                if (x != tx){
                    return 0;
                }
            }
            else if (dir == 2){
                if (y != ty){
                    return 0;
                }
            }
            else if (dir == 0){
                if (x == tx){
                    dir = 1;
                }
                else if (y == ty){
                    dir = 2;
                }           
                else{
                    return 0;
                }
            }
            else if (dir ==-1){
               x = tx;
               y = ty;
               dir = 0;
               if (fList.size() == 1){
                   dir = 3;
                   return dir;
                }
            }

            else{
                return 0;
            }
        }        
        return dir;
    }

    public void copyFinalBoard(){
        for (int j=0; j<15; j++){
            for (int i=0;i<15;i++){
                if(finalBoard[j][i]==' ' && tempBoard[j][i]!= ' '){
                    finalBoard[j][i] = tempBoard[j][i];
                    if (tempBoard[j][i] == '^'){
                        B1Fin = ("" + B1Temp).toLowerCase().charAt(0);
                        jButtonLetterB1.setEnabled(false);
                        jButtonLetterB1.setForeground(Color.gray);
                        B1Temp='^';                              
                    }
                    if (tempBoard[j][i] == '_'){
                        B2Fin = ("" + B2Temp).toLowerCase().charAt(0);
                        jButtonLetterB2.setEnabled(false);
                        jButtonLetterB2.setForeground(Color.gray);
                        B2Temp='_';                              
                    }
                }
            }            
        }
        
        Iterator<DraggableButton> iter= fList.iterator();
        DraggableButton currButton;
        
        while(iter.hasNext()){
            currButton = iter.next();
            currButton.setEnabled(false);
            currButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));           
            currButton.setDisabledIcon(currButton.getIcon());
        }
        //ispFinBoard();
        //dispLetterMult();
    }
    
    public char[][] getBlankBoard(){
            char[][] blankBoard = {
            {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
            {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
            {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
            {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
            {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
            {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
            {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
            {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
            {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
            {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
            {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
            {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
            {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
            {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
            {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '}        
        };
        return blankBoard;
    }
    
    private char get_letter(){
        //System.out.println(letters[0]);
        //System.out.println(letters[end]);
        if (endLetters == -1){ return '!';}
        int randomL = (int)(Math.random()*endLetters); 
        //System.out.println(randomL + " " + letters[randomL]);
        char retChar = letters[randomL];
        letters[randomL] = letters[endLetters];
        letters[endLetters] = '!';
        //System.out.println(letters[end]);
        //System.out.println(retChar);
        //System.out.println(randomL + " " + letters[randomL]);
        endLetters--;
        return retChar;
        //System.out.println(letters[end]);
                
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private DraggableButton jButtonA1;
    private DraggableButton jButtonA10;
    private DraggableButton jButtonA11;
    private DraggableButton jButtonA12;
    private DraggableButton jButtonA13;
    private DraggableButton jButtonA14;
    private DraggableButton jButtonA15;
    private DraggableButton jButtonA2;
    private DraggableButton jButtonA3;
    private DraggableButton jButtonA4;
    private DraggableButton jButtonA5;
    private DraggableButton jButtonA6;
    private DraggableButton jButtonA7;
    private DraggableButton jButtonA8;
    private DraggableButton jButtonA9;
    private DraggableButton jButtonB1;
    private DraggableButton jButtonB10;
    private DraggableButton jButtonB11;
    private DraggableButton jButtonB12;
    private DraggableButton jButtonB13;
    private DraggableButton jButtonB14;
    private DraggableButton jButtonB15;
    private DraggableButton jButtonB2;
    private DraggableButton jButtonB3;
    private DraggableButton jButtonB4;
    private DraggableButton jButtonB5;
    private DraggableButton jButtonB6;
    private DraggableButton jButtonB7;
    private DraggableButton jButtonB8;
    private DraggableButton jButtonB9;
    private DraggableButton jButtonC1;
    private DraggableButton jButtonC10;
    private DraggableButton jButtonC11;
    private DraggableButton jButtonC12;
    private DraggableButton jButtonC13;
    private DraggableButton jButtonC14;
    private DraggableButton jButtonC15;
    private DraggableButton jButtonC2;
    private DraggableButton jButtonC3;
    private DraggableButton jButtonC4;
    private DraggableButton jButtonC5;
    private DraggableButton jButtonC6;
    private DraggableButton jButtonC7;
    private DraggableButton jButtonC8;
    private DraggableButton jButtonC9;
    private javax.swing.JButton jButtonCPlay;
    private DraggableButton jButtonCR1;
    private DraggableButton jButtonCR2;
    private DraggableButton jButtonCR3;
    private DraggableButton jButtonCR4;
    private DraggableButton jButtonCR5;
    private DraggableButton jButtonCR6;
    private DraggableButton jButtonCR7;
    private DraggableButton jButtonD1;
    private DraggableButton jButtonD10;
    private DraggableButton jButtonD11;
    private DraggableButton jButtonD12;
    private DraggableButton jButtonD13;
    private DraggableButton jButtonD14;
    private DraggableButton jButtonD15;
    private DraggableButton jButtonD2;
    private DraggableButton jButtonD3;
    private DraggableButton jButtonD4;
    private DraggableButton jButtonD5;
    private DraggableButton jButtonD6;
    private DraggableButton jButtonD7;
    private DraggableButton jButtonD8;
    private DraggableButton jButtonD9;
    private DraggableButton jButtonE1;
    private DraggableButton jButtonE10;
    private DraggableButton jButtonE11;
    private DraggableButton jButtonE12;
    private DraggableButton jButtonE13;
    private DraggableButton jButtonE14;
    private DraggableButton jButtonE15;
    private DraggableButton jButtonE2;
    private DraggableButton jButtonE3;
    private DraggableButton jButtonE4;
    private DraggableButton jButtonE5;
    private DraggableButton jButtonE6;
    private DraggableButton jButtonE7;
    private DraggableButton jButtonE8;
    private DraggableButton jButtonE9;
    private DraggableButton jButtonF1;
    private DraggableButton jButtonF10;
    private DraggableButton jButtonF11;
    private DraggableButton jButtonF12;
    private DraggableButton jButtonF13;
    private DraggableButton jButtonF14;
    private DraggableButton jButtonF15;
    private DraggableButton jButtonF2;
    private DraggableButton jButtonF3;
    private DraggableButton jButtonF4;
    private DraggableButton jButtonF5;
    private DraggableButton jButtonF6;
    private DraggableButton jButtonF7;
    private DraggableButton jButtonF8;
    private DraggableButton jButtonF9;
    private javax.swing.JButton jButtonFillRack;
    private DraggableButton jButtonG1;
    private DraggableButton jButtonG10;
    private DraggableButton jButtonG11;
    private DraggableButton jButtonG12;
    private DraggableButton jButtonG13;
    private DraggableButton jButtonG14;
    private DraggableButton jButtonG15;
    private DraggableButton jButtonG2;
    private DraggableButton jButtonG3;
    private DraggableButton jButtonG4;
    private DraggableButton jButtonG5;
    private DraggableButton jButtonG6;
    private DraggableButton jButtonG7;
    private DraggableButton jButtonG8;
    private DraggableButton jButtonG9;
    private DraggableButton jButtonH1;
    private DraggableButton jButtonH10;
    private DraggableButton jButtonH11;
    private DraggableButton jButtonH12;
    private DraggableButton jButtonH13;
    private DraggableButton jButtonH14;
    private DraggableButton jButtonH15;
    private DraggableButton jButtonH2;
    private DraggableButton jButtonH3;
    private DraggableButton jButtonH4;
    private DraggableButton jButtonH5;
    private DraggableButton jButtonH6;
    private DraggableButton jButtonH7;
    private DraggableButton jButtonH8;
    private DraggableButton jButtonH9;
    private DraggableButton jButtonI1;
    private DraggableButton jButtonI10;
    private DraggableButton jButtonI11;
    private DraggableButton jButtonI12;
    private DraggableButton jButtonI13;
    private DraggableButton jButtonI14;
    private DraggableButton jButtonI15;
    private DraggableButton jButtonI2;
    private DraggableButton jButtonI3;
    private DraggableButton jButtonI4;
    private DraggableButton jButtonI5;
    private DraggableButton jButtonI6;
    private DraggableButton jButtonI7;
    private DraggableButton jButtonI8;
    private DraggableButton jButtonI9;
    private DraggableButton jButtonJ1;
    private DraggableButton jButtonJ10;
    private DraggableButton jButtonJ11;
    private DraggableButton jButtonJ12;
    private DraggableButton jButtonJ13;
    private DraggableButton jButtonJ14;
    private DraggableButton jButtonJ15;
    private DraggableButton jButtonJ2;
    private DraggableButton jButtonJ3;
    private DraggableButton jButtonJ4;
    private DraggableButton jButtonJ5;
    private DraggableButton jButtonJ6;
    private DraggableButton jButtonJ7;
    private DraggableButton jButtonJ8;
    private DraggableButton jButtonJ9;
    private DraggableButton jButtonK1;
    private DraggableButton jButtonK10;
    private DraggableButton jButtonK11;
    private DraggableButton jButtonK12;
    private DraggableButton jButtonK13;
    private DraggableButton jButtonK14;
    private DraggableButton jButtonK15;
    private DraggableButton jButtonK2;
    private DraggableButton jButtonK3;
    private DraggableButton jButtonK4;
    private DraggableButton jButtonK5;
    private DraggableButton jButtonK6;
    private DraggableButton jButtonK7;
    private DraggableButton jButtonK8;
    private DraggableButton jButtonK9;
    private DraggableButton jButtonL1;
    private DraggableButton jButtonL10;
    private DraggableButton jButtonL11;
    private DraggableButton jButtonL12;
    private DraggableButton jButtonL13;
    private DraggableButton jButtonL14;
    private DraggableButton jButtonL15;
    private DraggableButton jButtonL2;
    private DraggableButton jButtonL3;
    private DraggableButton jButtonL4;
    private DraggableButton jButtonL5;
    private DraggableButton jButtonL6;
    private DraggableButton jButtonL7;
    private DraggableButton jButtonL8;
    private DraggableButton jButtonL9;
    private javax.swing.JButton jButtonLetterB1;
    private javax.swing.JButton jButtonLetterB2;
    private DraggableButton jButtonM1;
    private DraggableButton jButtonM10;
    private DraggableButton jButtonM11;
    private DraggableButton jButtonM12;
    private DraggableButton jButtonM13;
    private DraggableButton jButtonM14;
    private DraggableButton jButtonM15;
    private DraggableButton jButtonM2;
    private DraggableButton jButtonM3;
    private DraggableButton jButtonM4;
    private DraggableButton jButtonM5;
    private DraggableButton jButtonM6;
    private DraggableButton jButtonM7;
    private DraggableButton jButtonM8;
    private DraggableButton jButtonM9;
    private DraggableButton jButtonN1;
    private DraggableButton jButtonN10;
    private DraggableButton jButtonN11;
    private DraggableButton jButtonN12;
    private DraggableButton jButtonN13;
    private DraggableButton jButtonN14;
    private DraggableButton jButtonN15;
    private DraggableButton jButtonN2;
    private DraggableButton jButtonN3;
    private DraggableButton jButtonN4;
    private DraggableButton jButtonN5;
    private DraggableButton jButtonN6;
    private DraggableButton jButtonN7;
    private DraggableButton jButtonN8;
    private DraggableButton jButtonN9;
    private DraggableButton jButtonO1;
    private DraggableButton jButtonO10;
    private DraggableButton jButtonO11;
    private DraggableButton jButtonO12;
    private DraggableButton jButtonO13;
    private DraggableButton jButtonO14;
    private DraggableButton jButtonO15;
    private DraggableButton jButtonO2;
    private DraggableButton jButtonO3;
    private DraggableButton jButtonO4;
    private DraggableButton jButtonO5;
    private DraggableButton jButtonO6;
    private DraggableButton jButtonO7;
    private DraggableButton jButtonO8;
    private DraggableButton jButtonO9;
    private javax.swing.JButton jButtonPGiveUp;
    private javax.swing.JButton jButtonPHint;
    private javax.swing.JButton jButtonPPlay;
    private DraggableButton jButtonPR1;
    private DraggableButton jButtonPR2;
    private DraggableButton jButtonPR3;
    private DraggableButton jButtonPR4;
    private DraggableButton jButtonPR5;
    private DraggableButton jButtonPR6;
    private DraggableButton jButtonPR7;
    private javax.swing.JButton jButtonStartGame;
    private javax.swing.JLabel jLabelCPlayerCScore;
    private javax.swing.JLabel jLabelCPlayerCScoreVal;
    private javax.swing.JLabel jLabelCPlayerTScore;
    private javax.swing.JLabel jLabelCPlayerTScoreVal;
    private javax.swing.JLabel jLabelCenterTileStatus;
    private javax.swing.JLabel jLabelEmptyTileStatus;
    private javax.swing.JLabel jLabelLetterRemainingVal;
    private javax.swing.JLabel jLabelLetterRemainlabel;
    private javax.swing.JLabel jLabelPPlayerCScore;
    private javax.swing.JLabel jLabelPPlayerCScoreVal;
    private javax.swing.JLabel jLabelPPlayerTScore;
    private javax.swing.JLabel jLabelPPlayerTScoreVal;
    private javax.swing.JLabel jLabelStartGame;
    private javax.swing.JLabel jLabelTimer;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JProgressBar jProgressBarTimer;
    // End of variables declaration//GEN-END:variables
}



  // Make a Label draggable; You can use the example to make any component draggable
 class DraggableButton extends javax.swing.JButton implements DragGestureListener, DragSourceListener, DropTargetListener {
    DragSource dragSource;
    boolean covered = false, isRack=false;
    private final GhostGlassPane glassPane;
    Icon back,front;
    char letter;

    public DraggableButton(DragSourceMotionListener dsml) {
		new DropTarget(this, this);
		dragSource = new DragSource();
                dragSource.addDragSourceMotionListener(dsml);
                glassPane = ((GhostGlassPane)dsml);
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
                //this.addMouseMotionListener(new MyMouseMotionListner());
    }
    
    public void setIsRack(){
       String s = back.toString();
       s = "/" + s.split("/")[s.split("/").length-1];
       if (s.equals("/blankRack.jpg")){
           //System.out.println("Rack");
           isRack = true;
       }
    }
    public void setBackIcon(Icon i){
        back = i;
        if(!covered){
            setIcon(back);
        }
        setIsRack();
    }

    public void setFrontIcon(Icon i){
        front = i;
        covered=true;
        setIcon(front);
    }
    
    public void setFinalIcon(Icon i){
        front = i;
        covered=true;
        setDisabledIcon(front);
        setEnabled(false);
    }   
        
    public void dragGestureRecognized(DragGestureEvent evt) {
        if(covered && isEnabled()){
            //System.out.println(getIcon().toString() + "   " + evt.getComponent());
            Transferable transferable = new StringSelection(front.toString());
            dragSource.startDrag(evt, DragSource.DefaultCopyDrop, transferable, this);
            
            Component c = evt.getComponent();
            BufferedImage image = new BufferedImage(c.getWidth(),
                                                    c.getHeight(), 
                                                    BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            c.paint(g);
            glassPane.setVisible(true);
            Point p = (Point) evt.getComponent().getLocation( ).clone( );
            SwingUtilities.convertPointToScreen(p, c);
            SwingUtilities.convertPointFromScreen(p, glassPane);

            glassPane.setPoint(p);
            glassPane.setImage(image);
            glassPane.repaint();
            
        }
    }

    public void dragEnter(DragSourceDragEvent evt) {
		// Called when the user is dragging this drag source and enters the drop target
		//System.out.println("Drag enter");
    }

    public void dragOver(DragSourceDragEvent evt) {
		// Called when the user is dragging this drag source and moves over the drop target
		//System.out.println("Drag over");
    }

    public void dragExit(DragSourceEvent evt) {
		// Called when the user is dragging this drag source and leaves the drop target
		//System.out.println("Drag exit");
    }

    public void dropActionChanged(DragSourceDragEvent evt) {
		// Called when the user changes the drag action between copy or move
		//System.out.println("Drag action changed");
    }

    public void dragDropEnd(DragSourceDropEvent evt) {			
		// Called when the user finishes or cancels the drag operation
                glassPane.setImage(null);
                if(evt.getDropSuccess()){
                    //System.out.println("success");
                    setIcon(back);
                    covered = false;
                    if(!isRack){
                        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                    }letter = 0;
                }
                else{
                    //System.out.println("failure");
                }
		//System.out.println("Drag action End");
                glassPane.setVisible(false);
                
    }

    

    public void dragEnter(DropTargetDragEvent evt) {
		// Called when the user is dragging and enters this drop target
		//System.out.println("Drop enter");
    }

    public void dragOver(DropTargetDragEvent evt) {
		// Called when the user is dragging and moves over this drop target
		//System.out.println("Drop over");
    }

    public void dragExit(DropTargetEvent evt) {
		// Called when the user is dragging and leaves this drop target
		//System.out.println("Drop exit");
    }

    public void dropActionChanged(DropTargetDragEvent evt) {
		// Called when the user changes the drag action between copy or move
		//System.out.println("Drop action changed");
    }

    public void setLetter(char ip){        
        letter = ip;
        
    }
       
    private void setLetter(String ip){
        if(ip.substring(0,7).equals("/Letter")){
            letter = ip.charAt(7);
            //System.out.println("Letter=" + letter);
        }
    }
    
    public void drop(DropTargetDropEvent evt) {
        // Called when the user finishes or cancels the drag operation
        try {

                Transferable transferable = evt.getTransferable();
                if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor) && !covered && isEnabled()) {
                        
                        evt.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        String dragContents = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                        dragContents = "/" + dragContents.split("/")[dragContents.split("/").length-1];
                        //System.out.println(dragContents);
                        
                        // We append the label text to the text area when dropped
                        //setText(getText() + " " + dragContents);
                        //setDisabledIcon(getIcon());
                        front = new javax.swing.ImageIcon(getClass().getResource(dragContents));
                        setLetter(dragContents);
                        setIcon(front);
			
                        evt.getSource().getClass();
                        covered = true;
                        
                        if(!isRack){
                            setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 0)));
                        }
                        evt.getDropTargetContext().dropComplete(true);
                        } 
                else {
                        evt.rejectDrop();
                }				
        } 
        catch (IOException e) {
                evt.rejectDrop();
        } 
        catch (UnsupportedFlavorException e) {
                evt.rejectDrop();
        }
    }    

  }



class GhostGlassPane extends JPanel implements DragSourceMotionListener, ComponentListener{
		private AlphaComposite composite;
		private BufferedImage dragged = null;
		private Point location = new Point(0, 0), win_loc = new Point(0, 0);

		public GhostGlassPane( )
		{
			setOpaque(false);
			composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.99f); 
		}
		
		public void setImage(BufferedImage dragged)
		{
                        //System.out.println("PAint image" + dragged);
			this.dragged = dragged;
		}

		public void setPoint(Point location)
		{
                        //System.out.println("move point");
			this.location = location;
		}

		public void paintComponent(Graphics g){
			//System.out.println("HerepaintComponent");
                    if (dragged == null)
				return;
                        //System.out.println("HerepaintComponent");
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setComposite(composite);
			g2.drawImage(dragged,
				(int) (location.getX( ) - (dragged.getWidth()*7/8) - win_loc.getX()),
				(int) (location.getY( ) - (dragged.getHeight()*14/9) - win_loc.getY()),
				null);
                       
		}
                
                @Override
                public void dragMouseMoved(DragSourceDragEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    /*System.out.println("DSML Mouse Drag" 
                                    + " (" + e.getX() + "," + e.getY() + ")"
                                    + " detected on "
                                    + e.getDragSourceContext().getComponent());*/
                    Component c = e.getDragSourceContext().getComponent();
                    Point p = e.getLocation();
                    //SwingUtilities.convertPointToScreen(p, c);
                    //SwingUtilities.convertPointFromScreen(p, this);                  
                    setPoint(p);
                    repaint();

                }

    @Override
    public void componentResized(ComponentEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //System.out.println(e.getComponent().getLocation());
        win_loc = e.getComponent().getLocation();
        
    }

    @Override
    public void componentShown(ComponentEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
                
}


class Placement{            
    String word="";
    int bj=-1, bi=-1, bi_start=-1, bj_start=-1, dir=-1, score=0;
    public Placement(){
    }
    
    public Placement(String ipWord, int j, int i, int start, int dir, int score){
        word=ipWord;
        this.score = score;
        if(dir==1){
            bi=i;
            bj_start=start;
        }
        else if(dir==2){
            bj=j;
            bi_start = start;
        }
    }
    
    public void set(String ipWord, int j, int i, int start, int dir, int score){
        word=ipWord;
        this.score = score;
        this.dir = dir;
        if(dir==1){
            bi=i;
            bj_start=start;
        }
        else if(dir==2){
            bj=j;
            bi_start = start;
        }
    }
    
    public void print(){
        if(dir==1){
            System.out.println("Print  i=" + bi + "word = " 
                + word + " bj_start=" + bj_start  + " score =" + score + " dir= ver");
            
        }
        else if(dir==2){
            System.out.println("Print  j=" + bj + "word = " 
                + word + " bi_start=" + bi_start  + " score =" + score + " dir= horz");
        }    
    }
}
