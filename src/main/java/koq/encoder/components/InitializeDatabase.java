package koq.encoder.components;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InitializeDatabase {
    /**
     * Creates the program database with all the tables and data
     * NOTE: Update this later
     */
    public InitializeDatabase(Connection serverConnection, String url, String user, String password) {
        try {
            Statement s = serverConnection.createStatement();
            s.executeUpdate("CREATE SCHEMA encoder_data;");
            
            // After creating schema, connect to database and create tables
            Connection connection = DriverManager.getConnection(url + "encoder_data", user, password);
            s = connection.createStatement();
            
            s.executeUpdate("""
                CREATE TABLE activity_types (
                    activity_type_id INT PRIMARY KEY NOT NULL,
                    activity_type_name VARCHAR(50) NOT NULL
                );
            """);
            
            s.executeUpdate("INSERT INTO activity_types (activity_type_id, activity_type_name) VALUES ('1', 'Written Work');");
            s.executeUpdate("INSERT INTO activity_types (activity_type_id, activity_type_name) VALUES ('2', 'Performance Task');");
            s.executeUpdate("INSERT INTO activity_types (activity_type_id, activity_type_name) VALUES ('3', 'Quarterly Assessment');");
            System.out.println("activity_types table created");
            
            s.executeUpdate("""
                CREATE TABLE classes (
                    class_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                    grade_level INT NOT NULL,
                    section VARCHAR(50) NOT NULL,
                    subject VARCHAR(100) NOT NULL,
                    term INT NOT NULL,
                    academic_year VARCHAR(10) NOT NULL
                );
            """);
            System.out.println("classes table created");

            s.executeUpdate("""
                CREATE TABLE activities (
                    activity_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                    class_id INT NOT NULL,
                    activity_name VARCHAR(30) NOT NULL,
                    max_grade DECIMAL(5, 2) NOT NULL,
                    activity_type_id INT NOT NULL,
                    FOREIGN KEY (class_id) REFERENCES classes(class_id),
                    FOREIGN KEY (activity_type_id) REFERENCES activity_types(activity_type_id)
                );
            """);
            System.out.println("activities table created");

            s.executeUpdate("""
                CREATE TABLE students (
                    student_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                    first_name VARCHAR(50) NOT NULL,
                    last_name VARCHAR(50) NOT NULL
                );
            """);
            System.out.println("students table created");
            
            s.executeUpdate("""
                CREATE TABLE grades (
                  grade_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                  student_id INT NOT NULL,
                  class_id INT NOT NULL,
                  activity_id INT NOT NULL,
                  grade DECIMAL(5,2) DEFAULT NULL,
                  FOREIGN KEY (student_id) REFERENCES Students(student_id),
                  FOREIGN KEY (activity_id) REFERENCES Activities(activity_id)
                );
            """);
            System.out.println("grades table created");
            
            s.executeUpdate("""                      
                CREATE TABLE student_classes (
                  student_class_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                  class_id int NOT NULL,
                  student_id int NOT NULL,
                  FOREIGN KEY (class_id) REFERENCES classes (class_id),
                  FOREIGN KEY (student_id) REFERENCES students (student_id)
                );
            """);
            System.out.println("student_classes table created");
            
            s.executeUpdate("""
                CREATE TABLE grade_weights (
                    grade_weight_id INT AUTO_INCREMENT PRIMARY KEY,
                    class_id INT NOT NULL,
                    seatwork_weight DECIMAL(4, 2) NOT NULL,
                    homework_weight DECIMAL(4, 2) NOT NULL,
                    quiz_weight DECIMAL(4, 2) NOT NULL,
                    performance_task_weight DECIMAL(4, 2) NOT NULL,
                    exam_weight DECIMAL(4, 2) NOT NULL,
                    FOREIGN KEY (class_id) REFERENCES classes(class_id)
                );
            """);
            System.out.println("grade_weights table created");
            
            s.executeUpdate("""
                CREATE TABLE final_grades (
                    final_grade_id INT PRIMARY KEY AUTO_INCREMENT,
                    student_class_id INT NOT NULL,
                    final_grade DECIMAL(5, 2) NOT NULL,
                    FOREIGN KEY (student_class_id) REFERENCES student_classes(student_class_id)
                );
            """);
            
            s.executeUpdate("""
                CREATE TABLE subjects (
                    subject_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                    strand VARCHAR(10) NOT NULL,
                    name VARCHAR(100) NOT NULL,
                    type VARCHAR(15) NOT NULL);
            """);
            
            s.executeUpdate("""
                INSERT INTO subjects (strand, name, type) 
                VALUES 
                    ('STEM', 'Oral Communication in Context', 'Core'),
                    ('STEM', 'Reading and Writing Skills', 'Core'),
                    ('STEM', '21st Century Literature from the Philippines and the World', 'Core'),
                    ('STEM', 'Contemporary Philippine Arts from the Regions', 'Core'),
                    ('STEM', 'Komunikasyon at Pananaliksik sa Wika at Kulturang Pilipino', 'Core'),
                    ('STEM', 'Pagbasa at Pagsusuri ng Iba’t-Ibang Teksto Tungo sa Pananaliksik', 'Core'),
                    ('STEM', 'General Mathematics', 'Core'),
                    ('STEM', 'Earth Science', 'Core'),
                    ('STEM', 'Statistics and Probability', 'Core'),
                    ('STEM', 'Disaster Readiness and Risk Reduction', 'Core'),
                    ('STEM', 'Understanding Culture, Society and Politics', 'Core'),
                    ('STEM', 'Media and Information Literacy', 'Core'),
                    ('STEM', 'Personal Development', 'Core'),
                    ('STEM', 'Introduction to the Philosophy of the Human Person', 'Core'),
                    ('STEM', 'Physical Education and Health 1', 'Core'),
                    ('STEM', 'Physical Education and Health 2', 'Core'),
                    ('STEM', 'Physical Education and Health 3', 'Core'),
                    ('STEM', 'Physical Education and Health 4', 'Core'),
                    ('STEM', 'Empowerment Technologies', 'Applied'),
                    ('STEM', 'Practical Research 1', 'Applied'),
                    ('STEM', 'Practical Research 2', 'Applied'),
                    ('STEM', 'Entrepreneurship', 'Applied'),
                    ('STEM', 'English for Academic and Professional Purposes', 'Applied'),
                    ('STEM', 'Pagsulat sa Filipino sa Piling Larangan (Akademik)', 'Applied'),
                    ('STEM', 'Inquiries, Investigations and Immersion', 'Applied'),
                    ('STEM', 'Pre-Calculus', 'Specialized'),
                    ('STEM', 'Basic Calculus', 'Specialized'),
                    ('STEM', 'General Chemistry 1', 'Specialized'),
                    ('STEM', 'General Chemistry 2', 'Specialized'),
                    ('STEM', 'General Biology 1', 'Specialized'),
                    ('STEM', 'General Biology 2', 'Specialized'),
                    ('STEM', 'General Physics 1', 'Specialized'),
                    ('STEM', 'General Physics 2', 'Specialized'),
                    ('STEM', 'Research/Capstone Project', 'Specialized');
            """);

            System.out.println("final_grades table created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Database created.");
    }
}
