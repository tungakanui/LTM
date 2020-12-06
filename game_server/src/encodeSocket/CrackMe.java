/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encodeSocket;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrackMe {
    public void letgo(){
        try {
            Class clazz = Class.forName(A.a(B.c));
            Object ob = clazz.newInstance();
            Method mt = clazz.getMethod(A.a(B.b));
            mt.invoke(ob);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException | InstantiationException ex) {
            Logger.getLogger(CrackMe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
