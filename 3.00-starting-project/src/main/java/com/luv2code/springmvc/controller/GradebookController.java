package com.luv2code.springmvc.controller;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.service.StudentAndGradesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GradebookController {

	@Autowired
	private Gradebook gradebook;

	@Autowired
	StudentAndGradesService studentAndGradesService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getStudents(Model m) {
		Iterable<CollegeStudent> lCollegeStudents = studentAndGradesService.getGradeBook();
		m.addAttribute("students", lCollegeStudents);
		return "index";
	}

	@PostMapping("/")
	public String createStudent(@ModelAttribute("student") CollegeStudent collegeStudent, Model model) {
		studentAndGradesService.createStudent(collegeStudent.getFirstname(), collegeStudent.getLastname(), collegeStudent.getEmailAddress());
		Iterable<CollegeStudent> lCollegeStudents = studentAndGradesService.getGradeBook();
		model.addAttribute("students", lCollegeStudents);
		return "index";
	}

	@GetMapping("/delete/student/{id}")
	public String deleteStudent(@PathVariable int id, Model model) {

		if (!studentAndGradesService.checkIfStudentIsNull(id)) {
			return "error";
		}
		studentAndGradesService.deleteStudent(id);
		Iterable<CollegeStudent> lCollegeStudents = studentAndGradesService.getGradeBook();
		model.addAttribute("students", lCollegeStudents);
		return "index";
	}

	@GetMapping("/studentInformation/{id}")
		public String studentInformation(@PathVariable int id, Model m) {
		return "studentInformation";
		}

}
