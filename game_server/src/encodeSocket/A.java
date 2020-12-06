/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encodeSocket;

public class A {
    public static String a(byte[] a){
       String b = "iloveyou";
       String c = new String(a);
       StringBuilder d = new StringBuilder();
       for(int j = 0; j < c.length(); j ++){
           d.append((char)(c.charAt(j) ^ b.charAt(j%b.length())));
       }
       return d.toString();
    }
}
