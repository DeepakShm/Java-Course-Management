package Course;

import CustomException.ValidationException;

public class Course {

    String courseId;
    String courseName;
    String pmTeacher;
    String scTeacher;
    int maxLimit;
    int studentCount;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getPmTeacher() {
        return pmTeacher;
    }

    public void setPmTeacher(String pmTeacher) {
        this.pmTeacher = pmTeacher;
    }

    public String getScTeacher() {
        return scTeacher;
    }

    public void setScTeacher(String scTeacher) {
        this.scTeacher = scTeacher;
    }

    public int getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public Course(){
        setCourseId(null);
        setCourseName(null);
        setMaxLimit(0);
        setPmTeacher(null);
        setScTeacher(null);
        setStudentCount(0);
    }

    public void incStudentCount() throws ValidationException {
        if(this.studentCount == this.maxLimit) throw new ValidationException("Cannot Enroll into this Course");
        this.studentCount++;
    }

    public void dscStudentCount() throws ValidationException{
        if(this.studentCount <=0) throw new ValidationException("Cannot Leave this Course");
        this.studentCount--;
    }



}
