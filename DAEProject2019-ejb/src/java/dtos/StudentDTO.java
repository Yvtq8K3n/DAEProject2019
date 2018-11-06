package dtos;

import java.io.Serializable;

public class StudentDTO extends UserDTO implements Serializable{

    private int courseCode;
    private String courseName;
   
    public StudentDTO(){
    }

    public StudentDTO(
            String username,
            String password,
            String name,
            String email,            
            int courseCode,
            String courseName) {
        super(username, password, name, email);
        this.courseCode = courseCode;
        this.courseName = courseName;
    }
    
    @Override
    public void reset() {
        super.reset();
        setCourseCode(0);
        setCourseName(null);
    }
    
    public int getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(int courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
