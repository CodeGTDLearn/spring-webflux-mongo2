package config.databuilders;

import com.github.javafaker.Faker;
import com.webflux.mongo2.project.Project;
import com.webflux.mongo2.task.Task;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Locale;

@Builder
@Getter
public class TaskBuilder {

  private static final Faker faker = new Faker(new Locale("en-CA.yml"));
  private final Task task;

  public static String createFakeUniqueRandomId() {
    return faker.regexify("PP[a-z0-9]{24}");
  }


  public static TaskBuilder taskWithID(
       String projectId,
       String ownerName,
       Long cost) {

    Task task = new Task();

    task.set_id(createFakeUniqueRandomId());
    task.setProjectId(projectId);
    task.setName(faker.name()
                      .fullName());

    task.setDescription(faker.lorem()
                             .paragraph(2));
    task.setOwnername(ownerName);
    task.setCost(cost);

    return TaskBuilder.builder()
                      .task(task)
                      .build();
  }


  public Task create() {
    return this.task;
  }
}