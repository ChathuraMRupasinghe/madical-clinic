package lk.IJSE.dep9.clinic.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.IJSE.dep9.clinic.misc.PasswordHashDemo;
import lk.IJSE.dep9.clinic.security.SecurityContextHolder;
import lk.IJSE.dep9.clinic.security.User;
import lk.IJSE.dep9.clinic.security.UserRole;

import java.io.IOException;
import java.sql.*;

public class LoginFormController {
    public Label lblName;
    public Label lblPassword;
    public TextField txtName;
    public PasswordField txtPassword;
    public Button btnLogin;
    public void initialize(){
        btnLogin.setDefaultButton(true);
    }

    public void btnLoginOnAction(ActionEvent actionEvent) throws ClassNotFoundException, SQLException, IOException {
        String userName = txtName.getText();
        String password = txtPassword.getText();

        if(userName.isBlank()){
            new Alert(Alert.AlertType.ERROR,"Username can't be empty").show();
            txtName.requestFocus();
            txtName.selectAll();
            return;
        }else if(password.isBlank()){
            new Alert(Alert.AlertType.ERROR,"Password can't be empty").show();
            txtPassword.requestFocus();
            txtPassword.selectAll();
            return;
        }else if(!userName.matches("^[a-zA-Z0-9]+$")){
            new Alert(Alert.AlertType.ERROR,"invalid login credentials").show();
            txtName.requestFocus();
            txtName.selectAll();
            return;
        }

        Class.forName("com.mysql.cj.jdbc.Driver");
        try(Connection connection = DriverManager.
                getConnection("jdbc:mysql://localhost:3306/medical_clinic", "root", "61087912@Cha");){
//            String sql = "SELECT role FROM User WHERE username='%s' AND password='%s'";
//            sql = String.format(sql, userName, password);
//
//            Statement stm = connection.createStatement();
//            ResultSet rst = stm.executeQuery(sql);
            String sql = "SELECT role,password FROM User WHERE username=?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1,userName);
            ResultSet rst = stm.executeQuery();

            if (rst.next()){
           String cipherText= rst.getString("password");

           if (!PasswordHashDemo.getSha256Hex(password).equals(cipherText)){
               new Alert(Alert.AlertType.ERROR, "Invalid login credentials").show();
               txtName.requestFocus();
               txtName.selectAll();
               return;
           }
           String role = rst.getString("role");
           SecurityContextHolder.setPrinciple(new User(userName, UserRole.valueOf(role)));
        Scene scene = null;
        switch (role){
            case "Admin":
                scene= new Scene(FXMLLoader.load(this.getClass().getResource("/view/AdminDashBoardForm.fxml")));
                break;
            case "Doctor":
                scene= new Scene(FXMLLoader.load(this.getClass().getResource("/view/DoctorDashBoardForm.fxml")));
                break;
            default:
                scene= new Scene(FXMLLoader.load(this.getClass().getResource("/view/ReceptionistDastBoardForm.fxml")));
        }
           Stage stage = new Stage();
           stage.setTitle("Open Source Medical Clinic");
           stage.setScene(scene);
           stage.show();
           stage.centerOnScreen();

           btnLogin.getScene().getWindow().hide();
       }else {
           new Alert(Alert.AlertType.ERROR, "Invalid login credentials").show();
           txtName.requestFocus();
           txtName.selectAll();
       }
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to connect with the Database, Try Again").show();
        }
    }
}
