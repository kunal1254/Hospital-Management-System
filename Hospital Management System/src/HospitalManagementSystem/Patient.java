package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatients() {
        System.out.print("Enter patient name : ");
        String Name = scanner.next();
        System.out.println("Enter patient age : ");
        int Age = scanner.nextInt();
        System.out.println(" Enter patient Gender :");
        String Gender = scanner.next();

        try {
            String query = " insert INTO Patients(Name, Age , Gender) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, Name);
            preparedStatement.setInt(2, Age);
            preparedStatement.setString(3, Gender);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("patient added Succesfully!!");
            } else {
                System.out.println(" failed to add patient");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatients() {
        String query = " select * from Patients";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients : ");
            System.out.println("+------------+--------------------+------+--------+");
            System.out.println("| Patient Id | Name               | Age  | Gender |");
            System.out.println("+------------+--------------------+------+--------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String Name = resultSet.getString("Name");
                int Age= resultSet.getInt("Age");
                String Gender = resultSet.getString("Gender");
                System.out.printf("|%-12s|%-21s|%-6s|%-8s|\n", id, Name,Age , Gender );
                System.out.println("+------------+--------------------+------+--------+");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id) {
        String query = " select * from Patients where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}


