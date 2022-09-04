package lk.IJSE.dep9.clinic.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.sql.DriverManager;
import java.sql.SQLException;

public class LoginFormController {
    public Label lblName;
    public Label lblPassword;
    public TextField txtName;
    public PasswordField txtPassword;
    public Button btnLogin;
    public void initialize(){
        btnLogin.setDefaultButton(true);
    }

    public void btnLoginOnAction(ActionEvent actionEvent) throws ClassNotFoundException {
        String userName = txtName.getText();
        String passwordText = txtPassword.getText();

        if(userName.isBlank()){
            new Alert(Alert.AlertType.ERROR,"Username cannot be empty").showAndWait();
            txtName.requestFocus();
            txtName.selectAll();
            return;
        }else if(passwordText.isBlank()){
            new Alert(Alert.AlertType.ERROR,"Password cannot be empty").showAndWait();
            txtPassword.requestFocus();
            txtPassword.selectAll();
            return;
        }else if(!txtName.getText().matches("^[a-zA-Z0-9]+$")){
            new Alert(Alert.AlertType.ERROR,"invalid credentials").showAndWait();
            txtPassword.requestFocus();
            txtPassword.selectAll();
            return;
        }

      //  if (rst,next()){
           String role= rst.getString("role");
        Scene scene = null;
        switch (role){
            case "Admin":
                scene= new Scene(FXMLLoader.load(this.getClass().getResource("/view/AdminDashBoardForm.fxml")));
                break;
            case "Doctor":
                scene= new Scene(FXMLLoader.load(this.getClass().getResource("/view/DoctorDashBoardForm.fxml")));
                break;
            default:
                scene= new Scene(FXMLLoader.load(this.getClass().getResource()));
                break;
        }

        Class.forName("com.mysql.cj.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/medical_clinic","root","61087912@Cha")){
            System.out.println(connection);
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to connect with the Database, Try Again").show();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
