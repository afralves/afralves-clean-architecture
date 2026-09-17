package com.afralves.cleanarchitecture.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(
        packages = "com.afralves.cleanarchitecture",
        importOptions = ImportOption.DoNotIncludeTests.class
)
public class CleanArchitectureTest {

    static final String BASE = "com.afralves.cleanarchitecture";

    @ArchTest
    public static final ArchRule domain_should_not_depend_on_other_layers =
        noClasses()
            .that().resideInAPackage(BASE + ".domain..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                    BASE + ".application..",
                    BASE + ".infrastructure..",
                    BASE + ".main.."
            );

    @ArchTest
    public static final ArchRule application_should_not_depend_infrastructure_and_main_layers =
        noClasses()
            .that().resideInAPackage(BASE + ".application..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                    BASE + ".infrastructure..",
                    BASE + ".main.."
            );

    @ArchTest
    public static final ArchRule infrastructure_should_not_depend_main_layer =
        noClasses()
            .that().resideInAPackage(BASE + ".infrastructure..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                    BASE + ".main.."
            );

}
