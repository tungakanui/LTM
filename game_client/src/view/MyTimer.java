/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

/**
 *
 * @author tungakanui
 */
public class MyTimer extends Thread{
    int i;

    public MyTimer() {
    }

    public MyTimer(int i) {
        this.i = i;
        
    }

    @Override
    public void run() {
        System.out.println("Pausing");
        while (i > 0){
          System.out.println("Remaining: "+i+" seconds");
          
          try {
            i--;
            Thread.sleep(1000L);    // 1000L = 1000ms = 1 second
           }
           catch (InterruptedException e) {
               //I don't think you need to do anything for your particular problem
           }
         }
    }

    public void runTimer(int i){
        this.start();
    }

}