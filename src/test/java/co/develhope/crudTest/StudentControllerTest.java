package co.develhope.crudTest;

import co.develhope.crudTest.controllers.StudentController;
import co.develhope.crudTest.entities.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Tania Ielpo
 */

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class StudentControllerTest {

	@Autowired
	private StudentController studentController;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void studentControllerLoads() {
		assertThat(studentController).isNotNull();
	}

	private Student getStudentFromId(Long id) throws Exception{
		MvcResult result = this.mockMvc.perform(get("/student/" + id))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();


		try {
			String studentJSON = result.getResponse().getContentAsString();
			Student student = objectMapper.readValue(studentJSON, Student.class);

			assertThat(student).isNotNull();
			assertThat(student.getId()).isNotNull();

			return student;
		}catch (Exception e){
			return null;
		}

	}

	private Student createAStudent() throws Exception {
		Student student = new Student();
		student.setName("Tania");
		student.setSurname("Ielpo");
		student.setWorking(true);
		return createAStudent(student);
	}

	private Student createAStudent(Student student) throws Exception {
		MvcResult result = createAStudentRequest(student);
		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);

		assertThat(studentFromResponse).isNotNull();
		assertThat(studentFromResponse.getId()).isNotNull();

		return studentFromResponse;
	}

	private MvcResult createAStudentRequest() throws Exception {
		Student student = new Student();
		student.setName("Tania");
		student.setSurname("Ielpo");
		student.setWorking(true);
		return createAStudentRequest(student);
	}

	private MvcResult createAStudentRequest(Student student) throws Exception {
		if(student == null) return null;
		String studentJSON = objectMapper.writeValueAsString(student);
		return this.mockMvc.perform(post("/student")
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}

	@Test
	void createAStudentTest() throws Exception {
		Student studentFromResponse = createAStudent();

		assertThat(studentFromResponse).isNotNull();
		assertThat(studentFromResponse.getId()).isNotNull();
	}

	@Test
	void readStudentsList() throws Exception {
		createAStudentRequest();

		MvcResult result =this.mockMvc.perform(get("/student/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		List<Student> studentsFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
		System.out.println("Students in database are: " + studentsFromResponse.size());
		assertThat(studentsFromResponse.size()).isNotZero();
	}

	@Test
	void readSingleStudent() throws Exception {
		Student student = createAStudent();
		Student studentFromResponse = getStudentFromId(student.getId());
		assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
	}

	@Test
	void updateStudentID() throws Exception{
		Student student = createAStudent();

		Long newId = 2L;
		student.setId(newId);

		String studentJSON = objectMapper.writeValueAsString(student);

		MvcResult result = this.mockMvc.perform(put("/student/"+student.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);

		assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
		Student studentFromResponseGet = getStudentFromId(student.getId());
		assertThat(studentFromResponseGet.getId()).isEqualTo(student.getId());
	}

	@Test
	void deleteStudent() throws Exception{
		Student student = createAStudent();
		assertThat(student.getId()).isNotNull();

		this.mockMvc.perform(delete("/student/"+student.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponseGet = getStudentFromId(student.getId());
		assertThat(studentFromResponseGet).isNull();
	}

	@Test
	void activateStudent() throws Exception{
		Student student = createAStudent();
		assertThat(student.getId()).isNotNull();

		MvcResult result = this.mockMvc.perform(put("/student/"+student.getId()+"/work?working=true"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		assertThat(studentFromResponse).isNotNull();
		assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponse.isWorking()).isEqualTo(true);

		Student studentFromResponseGet = getStudentFromId(student.getId());
		assertThat(studentFromResponseGet).isNotNull();
		assertThat(studentFromResponseGet.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponseGet.isWorking()).isEqualTo(true);
	}
}
