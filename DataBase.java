import java.sql.*;

import java.nio.file.Files;
import java.nio.file.Paths;
 
class DataBase{  
    

public void addBook(String name, byte[] imageBytes) {
    try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "manager")) {
        String sql = "INSERT INTO books (name, pic) VALUES (?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setBytes(2, imageBytes);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Book added successfully.");
            } else {
                System.out.println("Failed to add book.");
            }
        }
    } catch (SQLException e) {
        System.out.println("Error adding book: " + e.getMessage());
    }
}

public static void main(String args[]){  
try{  

Class.forName("oracle.jdbc.driver.OracleDriver");  

Connection con=DriverManager.getConnection(  
"jdbc:oracle:thin:@localhost:1521:xe","system","manager");  

byte[] fileBytes = Files.readAllBytes(Paths.get("C:/Users/devma/Downloads/1.jpg"));
 
String sql = "INSERT INTO books (name, pic) VALUES (?, ?)";
PreparedStatement stmt = con.prepareStatement(sql);
stmt.setString(1, "test");
stmt.setBytes(2, fileBytes);
int i=stmt.executeUpdate();
if(i>0)
System.out.println("Inserted Successfully");
else
System.out.println("Not Inserted");
ResultSet rs= stmt.executeQuery("select * from books");  
while(rs.next())  
System.out.println(rs.getInt(1)+"  "+rs.getString(2));
con.close();  
 
}catch(Exception e){
System.out.println(e);
}  
}  
}  
