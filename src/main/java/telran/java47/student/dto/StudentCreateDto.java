package telran.java47.student.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentCreateDto {
	int id;
	String name;
	String password;
}
