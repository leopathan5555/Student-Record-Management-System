package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Student;

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class MainController{

    @FXML private TableView<Student> table;
    @FXML private TableColumn<Student, String> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colDept;
    @FXML private TableColumn<Student, String> colEmail;
    @FXML private TableColumn<Student, Double> colCgpa;

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField deptField;
    @FXML private TextField emailField;
    @FXML private TextField cgpaField;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private final String FILE_NAME = "Students.csv";

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> data.getValue().idProperty());
        colName.setCellValueFactory(data -> data.getValue().nameProperty());
        colDept.setCellValueFactory(data -> data.getValue().departmentProperty());
        colEmail.setCellValueFactory(data -> data.getValue().emailProperty());
        colCgpa.setCellValueFactory(data -> data.getValue().cgpaProperty().asObject());


        loadData();
        table.setItems(studentList);
    }
    @FXML
    private void addStudent() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String department = deptField.getText().trim();
            String email = emailField.getText().trim();
            Double cgpa = Double.parseDouble(cgpaField.getText().trim());

            
            if (cgpa < 0.0 || cgpa > 4.0) {
                showAlert("Validation Error", "CGPA must be between 0.0 and 4.0.");
                return;
            }
            

            if (id.isEmpty() || name.isEmpty()) {
                showAlert("Validation Error", "ID and Name are required!");
                return;
            }

            Student student = new Student(id, name, department, email, cgpa);
            studentList.add(student);
            
            saveData();

            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "CGPA must be a number.");
        }
        
    }

    @FXML
    private void updateStudent() {
        Student selected = table.getSelectionModel().getSelectedItem();

        if (selected != null) {
            try {
                
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                String dept = deptField.getText().trim();
                String email = emailField.getText().trim();
                String cgpaText = cgpaField.getText().trim();

               

                if (!id.isEmpty()) selected.setId(id);
                if (!name.isEmpty()) selected.setName(name);
                if (!dept.isEmpty()) selected.setDepartment(dept);
                if (!email.isEmpty()) selected.setEmail(email);

                if (!cgpaText.isEmpty()) {
                    double cgpa = Double.parseDouble(cgpaText);
                    if (cgpa < 0.0 || cgpa > 4.0) {
                        showAlert("Validation Error", "CGPA must be between 0.0 and 4.0.");
                        return;
                    }
                    selected.setCgpa(cgpa);
                }

                table.refresh();
                saveData();
                clearFields();

            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "CGPA must be a number.");
            }
        } else {
            showAlert("No Selection", "Please select a student to update.");
        }
    }


    @FXML
    private void deleteStudent() {
        Student selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            studentList.remove(selected);
        } else {
            showAlert("No Selection", "Please select a student to delete.");
        }
        saveData();
    }

    @FXML
    private void searchStudent() {
        String idKey = idField.getText().trim().toLowerCase();
        String nameKey = nameField.getText().trim().toLowerCase();
        String deptKey = deptField.getText().trim().toLowerCase();
        String emailKey = emailField.getText().trim().toLowerCase();
        String cgpaKey = cgpaField.getText().trim();

        ObservableList<Student> filteredList = FXCollections.observableArrayList();

        for (Student s : studentList) {
            boolean matches = true;

            if (!idKey.isEmpty() && !s.getId().toLowerCase().contains(idKey)) {
                matches = false;
            }
            if (!nameKey.isEmpty() && !s.getName().toLowerCase().contains(nameKey)) {
                matches = false;
            }
            if (!deptKey.isEmpty() && !s.getDepartment().toLowerCase().contains(deptKey)) {
                matches = false;
            }
            if (!emailKey.isEmpty() && !s.getEmail().toLowerCase().contains(emailKey)) {
                matches = false;
            }
            if (!cgpaKey.isEmpty()) {
                try {
                    if (!String.valueOf(s.getCgpa()).contains(cgpaKey)) {
                        matches = false;
                    }
                } catch (NumberFormatException ignored) {}
            }

            if (matches) {
                filteredList.add(s);
            }
        }

        table.setItems(filteredList);
    }


    @FXML
    private void showAllStudents() {
        table.setItems(studentList);
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        deptField.clear();
        emailField.clear();
        cgpaField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void saveData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Student s : studentList) {
                writer.println(s.getId() + "," + s.getName() + "," +
                               s.getDepartment() + "," + s.getEmail() + "," +
                               s.getCgpa());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  
    private void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {          // Check if there’s another line
                String line = scanner.nextLine();
                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");      // Split by comma

                // Make sure we have all 5 fields
                if (lineScanner.hasNext()) {
                    String id = lineScanner.next();
                    String name = lineScanner.hasNext() ? lineScanner.next() : "";
                    String dept = lineScanner.hasNext() ? lineScanner.next() : "";
                    String email = lineScanner.hasNext() ? lineScanner.next() : "";
                    double cgpa = lineScanner.hasNext() ? Double.parseDouble(lineScanner.next()) : 0.0;

                    studentList.add(new Student(id, name, dept, email, cgpa));
                }

                lineScanner.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}