package config.databuilders;

import com.github.javafaker.Faker;
import com.webflux.mongo2.project.entity.Project;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Locale;

@Builder
@Getter
public class ProjectBuilder {

  private static final Faker faker = new Faker(new Locale("en-CA.yml"));
  private final Project project;
  //    private static String FAKER_REGEX_CPF = "[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}";
  //    private static String FAKER_REGEX_DDD = "[0-9]{2}";
  //    private static String FAKER_REGEX_TEL = "[0-9]{9}";


  public static String createFakeUniqueRandomId() {
    return faker.regexify("PP[a-z0-9]{24}");
  }


  public static ProjectBuilder projecNoID(
       String code,
       String startDate,
       String endDate,
       Long estimatedCost,
      List<String> countryList) {

    Project proj = new Project();

//    proj.set_id(createFakeUniqueRandomId());
    proj.setName(faker.name()
                      .fullName());

    proj.setCode(faker.letterify(code));
    proj.setDescription(faker.lorem()
                             .paragraph(2));
    proj.setStartDate(startDate);
    proj.setEndDate(endDate);
    proj.setEstimatedCost(estimatedCost);
    proj.setCountryList(countryList);
//    proj.setVersion(0L);

    return ProjectBuilder.builder()
                         .project(proj)
                         .build();
  }

  public static ProjectBuilder projectWithID(
       String code,
       String startDate,
       String endDate,
       Long estimatedCost,
       List<String> countryList) {

    Project proj = new Project();

    proj.set_id(createFakeUniqueRandomId());
    proj.setName(faker.commerce()
                      .productName());

    proj.setCode(faker.letterify(code));
    proj.setDescription(faker.lorem()
                             .paragraph(2));
    proj.setStartDate(startDate);
    proj.setEndDate(endDate);
    proj.setEstimatedCost(estimatedCost);
    proj.setCountryList(countryList);
//    proj.setVersion(0L);

    return ProjectBuilder.builder()
                         .project(proj)
                         .build();
  }


  public Project create() {
    return this.project;
  }
}