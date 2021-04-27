package es.projectnunsys;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("es.projectnunsys");

        noClasses()
            .that()
            .resideInAnyPackage("es.projectnunsys.service..")
            .or()
            .resideInAnyPackage("es.projectnunsys.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..es.projectnunsys.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
