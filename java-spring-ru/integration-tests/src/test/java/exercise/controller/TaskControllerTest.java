package exercise.controller;

import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
// END
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;


    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    // BEGIN
    @Test
    public void testDeleteTask() throws Exception {
        var task = createTask();
        taskRepository.save(task);

        mockMvc.perform(delete("/tasks/" + task.getId()))
                .andExpect(status().isOk());

        assertThat(taskRepository.findById(task.getId())).isEmpty();
    }

    @Test
    public void testUpdateTask() throws Exception {
        var task = createTask();
        taskRepository.save(task);

        var data = new HashMap<>();

        String newTitle = faker.lorem().word();
        String newDescription = faker.lorem().paragraph();

        data.put("title", newTitle);
        data.put("description", newDescription);


        var result = mockMvc.perform(put("/tasks/" + task.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsString(data)))
                .andExpect(status().isOk())
                .andReturn();
        task = taskRepository.findById(task.getId()).get();
        assertThat(task).extracting(Task::getTitle, Task::getDescription)
                .containsExactly(newTitle, newDescription);
    }

    @Test
    public void testViewTask() throws Exception {
        Task task = createTask();
        taskRepository.save(task);

        var result = mockMvc.perform(get("/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                a -> a.node("title").isEqualTo(task.getTitle()),
                a -> a.node("description").isEqualTo(task.getDescription())
                );
    }

    @Test
    public void testCreateTask() throws Exception {
        Task task = createTask();
        taskRepository.save(task);

        mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsString(task)))
                .andExpect(status().isCreated());

        var taskRes = taskRepository.findById(task.getId());

        assertThatJson(taskRes.isPresent());
    }




    private Task createTask() {
        return Instancio.of(Task.class)
                .set(Select.field(Task::getTitle), faker.lorem().word())
                .set(Select.field(Task::getDescription), faker.lorem().word())
                .create();
    }
    // END
}
