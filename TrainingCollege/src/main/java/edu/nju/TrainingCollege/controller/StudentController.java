package edu.nju.TrainingCollege.controller;

import edu.nju.TrainingCollege.domain.Student;
import edu.nju.TrainingCollege.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class StudentController {
    @Autowired
    StudentService studentService;

    @RequestMapping(value = "/student/login", method = RequestMethod.GET)
    public Student Login(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        HttpSession httpSession = request.getSession();
        Student student = studentService.login(email, password);
        if (student != null) {
            httpSession.setAttribute("userid", email);
            httpSession.setAttribute("usertype", "student");
        }
        return student;
    }

    @RequestMapping(value = "/student/logout", method = RequestMethod.GET)
    public void Logout(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        httpSession.removeAttribute("userid");
        httpSession.removeAttribute("usertype");
    }

    @RequestMapping(value = "/student/register", method = RequestMethod.GET)
    public int Register(HttpServletRequest request) {
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        return studentService.register(email, name, password);
    }

    @RequestMapping(value = "/student/activate", method = RequestMethod.GET)
    public void Activate(HttpServletRequest request, HttpServletResponse response) {
        try {
            String code = request.getParameter("code");
            String email = request.getParameter("email");
            if (studentService.activate(email, code) != 0) {
                response.sendRedirect("/#/home?message=Activate successfully!&type=success");
            } else {
                response.sendRedirect("/#/home?message=Failed to activate!&type=error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}