/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import model.Server;
import model.Relationship;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RelaDAO {
    Connection conn = Server.connect;
    
    public ArrayList<Relationship> getAllRela (){
        ArrayList<Relationship> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM relationship";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                Relationship r = new Relationship();
                r.setId1(rs.getInt("id1"));
                r.setId2(rs.getInt("id2"));
                list.add(r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RelaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    public ArrayList<Relationship> getAllRelaById (int id){
        ArrayList<Relationship> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM relationship WHERE id1= " + id + " OR id2= " + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                Relationship r = new Relationship();
                r.setId1(rs.getInt("id1"));
                r.setId2(rs.getInt("id2"));
                list.add(r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RelaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    public void addRela(Relationship r){
        try {
            String query = "INSERT INTO relationship VALUES (?,?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, r.getId1());
            ps.setInt(2, r.getId2());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(RelaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
