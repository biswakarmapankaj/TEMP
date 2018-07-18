/*create or replace procedure P_FIND_PASS_FAIL(m1 in number,m2 in number,m3 in number,result out varchar)
as 
begin
  if(m1<35 or m2<35 or m3<35)then
       result:='Fail';
 else 
     result:='Pass';
end if;
end;
/
*/
package com.nt.jdbc;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
public class AllStmtsTest extends JFrame implements ActionListener {
     private JLabel  lno,lname,lm1,lm2,lm3,lres;
     private JTextField tname,tm1,tm2,tm3,tres;
     private JButton bdetails,bresult;
     private JComboBox tno;
     private Connection con;
     private Statement st;
     private ResultSet rs1,rs2;
     private PreparedStatement ps;
     private CallableStatement cs;
     public AllStmtsTest() {
    	 System.out.println("AllStmtsTest: 0-param constructor");
    	 setTitle("-->MiniProject");
    	 setSize(300,300);
    	 setLayout(new FlowLayout());
    	 setBackground(Color.gray);
    	 // add comps
    	 lno=new JLabel("student Id::");
    	 add(lno);
    	 tno=new JComboBox();
    	 add(tno);
    	 bdetails=new JButton("details");
    	 bdetails.addActionListener(this);
    	 add(bdetails);
    	 lname=new JLabel("student name::");
    	 add(lname);
    	 tname=new JTextField(10);
    	 add(tname);
    	 lm1=new JLabel("Marks1::");
    	 add(lm1);
    	 tm1=new JTextField(10);
    	 add(tm1);
    	 lm2=new JLabel("Marks2::");
    	 add(lm2);
    	 tm2=new JTextField(10);
    	 add(tm2);
    	 lm3=new JLabel("Marks3::");
    	 add(lm3);
    	 tm3=new JTextField(10);
    	 add(tm3);
    	 bresult=new JButton("result");
    	 bresult.addActionListener(this);
    	 add(bresult);
    	 lres=new JLabel("Result::");
    	 add(lres);
    	 tres=new JTextField(10);
    	 add(tres);
    	 tname.setEditable(false);tm1.setEditable(false);tm2.setEditable(false);
    	 tm3.setEditable(false);tres.setEditable(false);
    	 setVisible(true);
    	 this.addWindowListener(new MyWindowAdapter(this));
    	 loadItems();
	}//constructor
     private void loadItems(){
    	 System.out.println("loadItems()");
    	 try{
    		 //register jdbc driver
    		 Class.forName("oracle.jdbc.driver.OracleDriver");
    		 //Establish the connection
    		 con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","manager");
    		 if(con!=null)
    		 st=con.createStatement();
    		 if(st!=null)
    		 rs1=st.executeQuery("select sno from All_Student");
    		 if(rs1!=null) {
    			 while(rs1.next()) {
    				 tno.addItem(rs1.getInt(1));
    			 }
    		 }
    		 if(con!=null)
    		 ps=con.prepareStatement("select * from All_Student where sno=?");
    		 if(con!=null) {
    		 cs=con.prepareCall("{call FIND_PASS_FAIL(?,?,?,?)}");
    		 cs.registerOutParameter(4,Types.VARCHAR);
    	 }//try
    }
    	 catch(SQLException se){
    		 se.printStackTrace();
    	 }
    	 catch(ClassNotFoundException cnf){
    		 cnf.printStackTrace();
    	 }
     }//initialize()
	@Override
	public void actionPerformed(ActionEvent ae) {
		int m1=0,m2=0,m3=0;
		String result=null;
		System.out.println("actionPerformed(-)");
		if(ae.getSource()==bdetails){
			 System.out.println("details button is clicked");
			 try{
				 int no=(Integer)tno.getSelectedItem();
				//set above no to query parameter
				 if(ps!=null) {
				 ps.setInt(1,no);
				 //execute Query
				 rs2=ps.executeQuery();
				 }
				 //process the ResultSet
				 if(rs2!=null) {
				 if(rs2.next()){
					 //set record values to text boxes
					 tname.setText(rs2.getString(2));
				     tm1.setText(rs2.getString(3));
				     tm2.setText(rs2.getString(4));
				     tm3.setText(rs2.getString(5));
				 }//if
			 }//try
		}
			 catch(SQLException se){
				 se.printStackTrace();
			 }
		}//if
		else{
			System.out.println("Result Button is clicked");
			try {
			//read m1,m2,m3 text box values
			m1=Integer.parseInt(tm1.getText());
			m2=Integer.parseInt(tm2.getText());
			m3=Integer.parseInt(tm3.getText());
			if(cs!=null){
			//set the above marks to PL/SQL PROCEDURE IN param value
			cs.setInt(1,m1);cs.setInt(2,m2);cs.setInt(3,m3);
			//call  PL/SQL Procedure
			cs.execute();
			//gather result from OUT params
			result=cs.getString(4);
			//set result to TExtbox (tres)
			tres.setText(result);
		}//try
	}
			 catch(SQLException se){
				 se.printStackTrace();
			 }
		}//else
	}//actionPerformed(-)
		public void closejdbcObjects() {
			try{
				if(rs1!=null)
					rs1.close();
			}
			catch(SQLException se){
				se.printStackTrace();
			}
			try{
				if(rs2!=null)
					rs2.close();
			}
			catch(SQLException se){
				se.printStackTrace();
			}
			try{
				if(cs!=null)
					cs.close();
			}
			catch(SQLException se){
				se.printStackTrace();
			}
			try{
				if(st!=null)
					st.close();
			}
			catch(SQLException se){
				se.printStackTrace();
			}
			try{
				if(ps!=null)
					ps.close();
			}
			catch(SQLException se){
				se.printStackTrace();
			}
			try{
				if(con!=null)
					con.close();
			}
			catch(SQLException se){
				se.printStackTrace();
			}
		}
		public static void main(String[] args) {
			System.out.println("main(-) method");
			AllStmtsTest test=new AllStmtsTest();
			}
		class MyWindowAdapter extends WindowAdapter{
			AllStmtsTest test=null;
			public MyWindowAdapter(AllStmtsTest test) {
				this.test=test;
			}
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("windowClosing");
				test.closejdbcObjects();
				System.exit(0);
			}
		}
	}
	
