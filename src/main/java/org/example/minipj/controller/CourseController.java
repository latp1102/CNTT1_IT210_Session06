package com.session06miniproject.controller;

import com.session06miniproject.model.Course;
import com.session06miniproject.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"/", "/course"})
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Handle root path - redirect to course list
    @GetMapping
    public String home() {
        return "redirect:/course/list";
    }

    // Chức năng 2: Danh sách & Lọc
    @GetMapping("/list")
    public String listCourses(
            @RequestParam(name = "level", required = false, defaultValue = "") String level,
            @RequestParam(name = "maxFee", required = false) Double maxFee,
            Model model) {

        model.addAttribute("courses", courseService.filterCourses(level, maxFee));
        model.addAttribute("selectedLevel", level);
        model.addAttribute("enteredFee", maxFee);
        return "course/list";
    }

    // Chức năng 3: Xem chi tiết
    @GetMapping("/detail/{code}")
    public String courseDetail(@PathVariable String code, Model model) {
        Course course = courseService.getCourseByCode(code);
        if (course == null) return "redirect:/course/list";
        model.addAttribute("course", course);
        return "course/detail";
    }

    // Chức năng 4: GET Form cập nhật
    @GetMapping("/edit/{code}")
    public String editCourseForm(@PathVariable String code, Model model) {
        Course course = courseService.getCourseByCode(code);
        if (course == null) return "redirect:/course/list";
        model.addAttribute("course", course);
        return "course/edit";
    }

    // Chức năng 4: POST Cập nhật (PRG Pattern)
    @PostMapping("/edit")
    public String updateCourse(@ModelAttribute("course") Course course, RedirectAttributes redirectAttributes) {
        courseService.updateCourse(course);
        redirectAttributes.addFlashAttribute("success", "Cập nhật khóa học thành công!");
        return "redirect:/course/list";
    }

    // Chức năng 5: POST Xóa/Lưu trữ
    @PostMapping("/delete/{code}")
    public String deleteCourse(@PathVariable String code, RedirectAttributes redirectAttributes) {
        boolean isDeleted = courseService.deleteCourse(code);
        if (isDeleted) {
            redirectAttributes.addFlashAttribute("success", "Đã hủy khóa học thành công.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không thể hủy khóa học đã có học viên đăng ký.");
        }
        return "redirect:/course/list";
    }
}