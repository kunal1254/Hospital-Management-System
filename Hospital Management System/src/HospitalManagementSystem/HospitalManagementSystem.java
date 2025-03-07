package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String userName = "root";
    private static final String Password = "1212";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url,userName,Password );
            Patient patient= new Patient(connection, scanner);
            Doctors doctors = new Doctors(connection);
            while (true){
                System.out.println(" HOSPITAL MANAGEMENT SYSTEM");
                System.out.println(" 1. Add Patient");
                System.out.println(" 2. View Patient");
                System.out.println(" 3. View Doctors ");
                System.out.println(" 4. Book Appointment");
                System.out.println(" 5. Exit ");
                System.out.println(" Enter your Choice :");
                int Choice = scanner.nextInt();

                switch (Choice){
                    case 1:
                        patient.addPatients();
                        System.out.println();
                    case 2:
                        patient.viewPatients();
                        System.out.println();
                    case 3:
                        doctors.viewDoctors();
                        System.out.println();
                    case 4:
                        bookAppointment(patient,doctors,connection,scanner);
                        System.out.println();
                    case 5:
                        System.out.println("THANK YOU FOR USING HOSPITAL MANAGEMENT SYSTEM");
                    return ;
                    default:
                        System.out.println(" Enter valid choice !!");
                        break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void bookAppointment( Patient patient, Doctors doctors,Connection connection, Scanner scanner){
        System.out.println(" Enter Patient id :");
        int PatientId = scanner.nextInt();
        System.out.println(" Enter Doctor id");
        int DoctorId = scanner.nextInt();
        System.out.println(" Enter Appointment date (YYYY-MM-DD)");
        String appointmentDate = scanner.next();

        if (patient.getPatientById(PatientId) && doctors.getDoctorById(DoctorId)){
            if(checkDoctorAvability(DoctorId ,appointmentDate, connection)){
                String appointmentquerry = "INSERT INTO appointment( Patient_id,Doctor_id, appointment_date) VALUES (?,?,?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentquerry);
                    preparedStatement.setInt(1, PatientId);
                    preparedStatement.setInt(2, DoctorId);
                    preparedStatement.setString(3, appointmentDate);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0){
                        System.out.println(" Appointment booked");
                    }else{
                        System.out.println("Appointment Failed");
                    }

                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else{
                System.out.println(" Doctopr not avilable on this date !!");
            }
        }else {
            System.out.println(" Either doctor or Patient doesn't exits!!");
        }
    }


    public static boolean checkDoctorAvability(int DoctorId, String appointmentDate, Connection connection){
        String query = " select count(*) from appointments where Doctor_id= ? AND appointent_date =?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, DoctorId);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if( resultSet.next()){
                int count = resultSet.getInt(1);
                if(count==0){
                    return true;
                }else{
                    return false;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;

    }

}



