package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student {
    private StringProperty id;
    private StringProperty name;
    private StringProperty department;
    private StringProperty email;
    private DoubleProperty cgpa;

    public Student(String id, String name, String department, String email, double cgpa) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.department = new SimpleStringProperty(department);
        this.email = new SimpleStringProperty(email);
        this.cgpa = new SimpleDoubleProperty(cgpa);
    }

	public String getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getDepartment() { return department.get(); }
    public String getEmail() { return email.get(); }
    public double getCgpa() { return cgpa.get(); }

 
    public void setId(String value) { id.set(value); }
    public void setName(String value) { name.set(value); }
    public void setDepartment(String value) { department.set(value); }
    public void setEmail(String value) { email.set(value); }
    public void setCgpa(double value) { cgpa.set(value); }

   
    public StringProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty departmentProperty() { return department; }
    public StringProperty emailProperty() { return email; }
    public DoubleProperty cgpaProperty() { return cgpa; }
}