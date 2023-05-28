package telran.java47.student.dao;

import java.util.List;
import java.util.Optional;

import telran.java47.student.model.Student;

public interface StudentRepository {
	Student save(Student student);
	
	Optional<Student> findById(int id);
	
	void deleteById(int id);
	
	List<Student> findAll();
}
