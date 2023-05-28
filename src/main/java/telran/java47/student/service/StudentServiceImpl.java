package telran.java47.student.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import telran.java47.student.dao.StudentRepository;
import telran.java47.student.dto.ScoreDto;
import telran.java47.student.dto.StudentCreateDto;
import telran.java47.student.dto.StudentDto;
import telran.java47.student.dto.StudentUpdateDto;
import telran.java47.student.model.Student;

@Component
public class StudentServiceImpl implements StudentService {

	@Autowired
	StudentRepository studentRepository;

	@Override
	public boolean addStudent(StudentCreateDto studentCreateDto) {
		if (studentRepository.findById(studentCreateDto.getId()).isPresent()) {
			return false;
		}
		Student student = new Student(studentCreateDto.getId(), studentCreateDto.getName(),
				studentCreateDto.getPassword());
		studentRepository.save(student);
		return true;
	}

	@Override
	public StudentDto findStudent(int id) {
		Student student = findStudentOrThrow(id);
		return student == null ? null : new StudentDto(student.getId(), student.getName(), student.getScores());
	}

	@Override
	public StudentDto removeStudent(int id) {
		Student student = findStudentOrThrow(id);
		studentRepository.deleteById(id);
		return new StudentDto(student.getId(), student.getName(), student.getScores());

	}

	@Override
	public StudentCreateDto updateStudent(int id, StudentUpdateDto studentUpdateDto) {
		Student student = findStudentOrThrow(id);
		student.setName(studentUpdateDto.getName());
		student.setPassword(studentUpdateDto.getPassword());
		return new StudentCreateDto(student.getId(), student.getName(), student.getPassword());

	}

	@Override
	public boolean addScore(int id, ScoreDto scoreDto) {
		Student student = findStudentOrThrow(id);
		student.addScore(scoreDto.getExamName(), scoreDto.getScore());
		return true;
	}

	@Override
	public List<StudentDto> findStudentsByName(String name) {
		return studentRepository.findAll().stream().filter(student -> student.getName().toLowerCase().equals(name.toLowerCase()))
				.map(student -> new StudentDto(student.getId(), student.getName(), student.getScores()))
				.collect(Collectors.toList());
	}

	@Override
	public long getStudentsNamesQuantity(List<String> names) {
		return studentRepository.findAll().stream().filter(student -> names.contains(student.getName())).count();
	}

	@Override
	public List<StudentDto> getStudentsByExamMinScore(String exam, int minScore) {
		return studentRepository.findAll().stream()
				.filter(student -> student.getScores().containsKey(exam) && student.getScores().get(exam) >= minScore)
				.map(student -> new StudentDto(student.getId(), student.getName(), student.getScores()))
				.collect(Collectors.toList());
	}
	
    private Student findStudentOrThrow(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with id " + id + " not found"));
    }
}
