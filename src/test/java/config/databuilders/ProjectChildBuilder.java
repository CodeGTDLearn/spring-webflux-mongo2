package config.databuilders;

import com.github.javafaker.Faker;
import com.webflux.api.modules.project.entity.ProjectChild;
import com.webflux.api.modules.task.entity.Task;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Locale;

@Builder
@Getter
public class ProjectChildBuilder {

  private static final Faker faker = new Faker(new Locale("en-CA.yml"));
  private final ProjectChild projectChild;
  //    private static String FAKER_REGEX_CPF = "[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}";
  //    private static String FAKER_REGEX_DDD = "[0-9]{2}";
  //    private static String FAKER_REGEX_TEL = "[0-9]{9}";


  public static String createFakeUniqueRandomId() {

    return faker.regexify("PP[a-z0-9]{24}");
  }


  public static ProjectChildBuilder projectChildWithID(
       String code,
       String startDate,
       String endDate,
       Long estimatedCost,
       List<Task> listTasks) {

    ProjectChild proj = new ProjectChild();

    proj.set_id(createFakeUniqueRandomId());
    proj.setName(faker.name()
                      .fullName());

    proj.setCode(faker.letterify(code));
    proj.setDescription(faker.lorem()
                             .paragraph(2));
    proj.setStartDate(startDate);
    proj.setEndDate(endDate);
    proj.setEstimatedCost(estimatedCost);
    proj.setCountryList(List.of("UK", "USA"));
    proj.setTasks(listTasks);

    return ProjectChildBuilder.builder()
                              .projectChild(proj)
                              .build();
  }


  public ProjectChild create() {

    return this.projectChild;
  }
}