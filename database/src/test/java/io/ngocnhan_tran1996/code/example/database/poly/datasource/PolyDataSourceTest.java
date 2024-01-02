package io.ngocnhan_tran1996.code.example.database.poly.datasource;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import io.ngocnhan_tran1996.code.example.database.poly.datasource.config.OracleDataSourceConfig;
import io.ngocnhan_tran1996.code.example.database.poly.datasource.config.PostgresDataSourceConfig;
import io.ngocnhan_tran1996.code.example.database.poly.datasource.oracle.CatEntity;
import io.ngocnhan_tran1996.code.example.database.poly.datasource.oracle.CatRepo;
import io.ngocnhan_tran1996.code.example.database.poly.datasource.postgres.DogEntity;
import io.ngocnhan_tran1996.code.example.database.poly.datasource.postgres.DogRepo;

@ActiveProfiles("poly-datasource")
@SpringBootTest(classes = {OracleDataSourceConfig.class, PostgresDataSourceConfig.class})
class PolyDataSourceTest {

  @Autowired
  CatRepo catRepo;

  @Autowired
  DogRepo dogRepo;

  @BeforeEach
  void init() {

    catRepo.deleteAll();
    dogRepo.deleteAll();

    catRepo.saveAll(List.of(
        new CatEntity("Cat 1"),
        new CatEntity("Cat 2"),
        new CatEntity("Cat 3")));
    dogRepo.saveAll(List.of(
        new DogEntity("Dog 1"),
        new DogEntity("Dog 2"),
        new DogEntity("Dog 3")));
  }

  @Test
  void testCRUD() {

    // create
    catRepo.save(new CatEntity("Cat 4"));
    dogRepo.save(new DogEntity("Dog 4"));

    // read
    assertThat(catRepo.findAll()).hasSize(4);
    assertThat(dogRepo.findAll()).hasSize(4);

    // update
    catRepo.findById(1).ifPresent(cat -> {

      cat.setSpecies("Cat cat 1");
      catRepo.save(cat);
    });
    dogRepo.findById(1).ifPresent(dog -> {

      dog.setSpecies("Dog dog 1");
      dogRepo.save(dog);
    });

    // delete
    catRepo.deleteById(3);
    dogRepo.deleteById(3);

    assertThat(catRepo.findAll()).hasSize(3);
    assertThat(dogRepo.findAll()).hasSize(3);
  }

}
