/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import model.Server;
import model.User;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {
    Connection conn = Server.connect;
    
    public User getUserById(int id) throws SQLException{
        String query = "SELECT * FROM info_user WHERE ID="+id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            User u = new User();
            if(rs.next()){
                u.setID(rs.getInt("ID"));
                u.setUser_name(rs.getString("user_name"));
                u.setPass(rs.getString("pass"));
                u.setFullname(rs.getString("fullname"));
                u.setTotalCore(rs.getDouble("totalCore"));
                u.setTotalTimePlayed(rs.getDouble("totalTimePlayed"));
                u.setTotalGames(rs.getInt("totalGames"));
                u.setTotalWins(rs.getInt("totalWins"));
                u.setStatus(rs.getString("status"));
                u.setIsOnline(rs.getInt("isOnline"));
                return u;
            } 
            return null;
    }
    
    public boolean updateUser(int id , double core, double time, int game, int win) throws SQLException{
        User u = new User();
        u = getUserById(id);
        double newCore = core + u.getTotalCore();     
        int newWin = win + u.getTotalWins();
        double newGame = game + u.getTotalGames();
        double newTime = (time + u.getTotalTimePlayed())/(double)newWin;
        String query = "UPDATE info_user SET totalCore=" + newCore + ", totalGames=" +newGame+", totalTimePlayed="+newTime+", "
                + "totalWins= " +newWin+" WHERE id=" + id;
        try {
            Statement stmt = conn.createStatement();
            int rs = stmt.executeUpdate(query);
            if(rs != 0){
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
         return false;
    }
    
    public ArrayList<User> getListUserOrderByCore() throws SQLException{
        String query = "SELECT * FROM info_user ORDER BY totalCore DESC";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ArrayList<User> list = new ArrayList<>();
        
        while(rs.next()){
            User u = new User();
            u.setID(rs.getInt("ID"));
            u.setUser_name(rs.getString("user_name"));
            u.setPass(rs.getString("pass"));
            u.setFullname(rs.getString("fullname"));
            u.setTotalCore(rs.getDouble("totalCore"));
            u.setTotalTimePlayed(rs.getDouble("totalTimePlayed"));
            u.setTotalGames(rs.getInt("totalGames"));
            u.setTotalWins(rs.getInt("totalWins"));
            u.setStatus(rs.getString("status"));
            u.setIsOnline(rs.getInt("isOnline"));
            list.add(u);
        } 
        return list;
    }
    
    public ArrayList<User> getListUsers() throws SQLException{
        String query = "SELECT * FROM info_user";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ArrayList<User> list = new ArrayList<>();
       
        while(rs.next()){
             User u = new User();
            u.setID(rs.getInt("ID"));
            u.setUser_name(rs.getString("user_name"));
            u.setPass(rs.getString("pass"));
            u.setFullname(rs.getString("fullname"));
            u.setTotalCore(rs.getDouble("totalCore"));
            u.setTotalTimePlayed(rs.getDouble("totalTimePlayed"));
            u.setTotalGames(rs.getInt("totalGames"));
            u.setTotalWins(rs.getInt("totalWins"));
            u.setStatus(rs.getString("status"));
            u.setIsOnline(rs.getInt("isOnline"));
            list.add(u);
        } 
        return list;
    }
    public ArrayList<User> getListUserOrderByTime() throws SQLException{
        String query = "SELECT * FROM info_user WHERE totalTimePlayed > " + 0 +" ORDER BY totalTimePlayed";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ArrayList<User> list = new ArrayList<>();
        
        while(rs.next()){
            User u = new User();
            u.setID(rs.getInt("ID"));
            u.setUser_name(rs.getString("user_name"));
            u.setPass(rs.getString("pass"));
            u.setFullname(rs.getString("fullname"));
            u.setTotalCore(rs.getDouble("totalCore"));
            u.setTotalTimePlayed(rs.getDouble("totalTimePlayed"));
            u.setTotalGames(rs.getInt("totalGames"));
            u.setTotalWins(rs.getInt("totalWins"));
            u.setStatus(rs.getString("status"));
            u.setIsOnline(rs.getInt("isOnline"));
            list.add(u);
        } 
        return list;
    }
    
}
