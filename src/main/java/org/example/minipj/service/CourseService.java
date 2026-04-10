package org.example.minipj.service;

import com.session06miniproject.model.Course;
import com.session06miniproject.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public List<Course> filterCourses(String level, Double maxFee) {
        return repository.findAll().stream()
                .filter(c -> (level == null || level.isEmpty() || c.getLevel().equals(level)))
                .filter(c -> (maxFee == null || c.getTuitionFee() <= maxFee))
                .collect(Collectors.toList());
    }

    public Course getCourseByCode(String code) {
        return repository.findByCode(code);
    }

    public void updateCourse(Course course) {
        repository.update(course);
    }

    public boolean deleteCourse(String code) {
        Course course = getCourseByCode(code);
        if (course != null && course.getStudentCount() == 0) {
            repository.delete(code);
            return true;
        }
        return false; // Trả về false nếu đã có học viên
    }
}