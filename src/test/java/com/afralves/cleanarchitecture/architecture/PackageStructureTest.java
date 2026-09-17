package com.afralves.cleanarchitecture.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(
        packages = "com.afralves.cleanarchitecture",
        importOptions = ImportOption.DoNotIncludeTests.class
)
public class PackageStructureTest {

    static final String BASE = "com.afralves.cleanarchitecture";

    // --- Location ---

    @ArchTest
    public static final ArchRule controllers_should_reside_in_infrastructure_controller_package =
        classes()
            .that().haveSimpleNameEndingWith("Controller")
            .should().resideInAPackage(BASE + ".infrastructure.adapter.controller");

    @ArchTest
    public static final ArchRule repository_should_reside_in_infrastructure_persistence_package =
        classes()
            .that().haveSimpleNameEndingWith("Repository")
            .should().resideInAPackage(BASE + ".infrastructure.adapter.persistence..");

    @ArchTest
    public static final ArchRule interactors_should_reside_in_application_usecases_package =
        classes()
            .that().haveSimpleNameEndingWith("Interactor")
            .should().resideInAPackage(BASE + ".application.usecases..");

    @ArchTest
    public static final ArchRule gateways_should_reside_in_application_gateway_package =
        classes()
            .that().haveSimpleNameEndingWith("Gateway")
            .should().resideInAPackage(BASE + ".application.gateway");

    @ArchTest
    public static final ArchRule adapters_should_reside_in_infrastructure_adapter_package =
        classes()
            .that().haveSimpleNameEndingWith("Adapter")
            .should().resideInAPackage(BASE + ".infrastructure.adapter..");

    @ArchTest
    public static final ArchRule entities_should_reside_in_persistence_model_package =
        classes()
            .that().haveSimpleNameEndingWith("Entity")
            .should().resideInAPackage(BASE + ".infrastructure.adapter.persistence.model");

    @ArchTest
    public static final ArchRule converters_should_reside_in_persistence_converter_package =
        classes()
            .that().haveSimpleNameEndingWith("Converter")
            .should().resideInAPackage(BASE + ".infrastructure.adapter.persistence.converter");

    @ArchTest
    public static final ArchRule requests_should_reside_in_controller_request_package =
        classes()
            .that().haveSimpleNameEndingWith("Request")
            .should().resideInAPackage(BASE + ".infrastructure.adapter.controller.request");

    @ArchTest
    public static final ArchRule responses_should_reside_in_infrastructure_adapter_package =
        classes()
            .that().haveSimpleNameEndingWith("Response")
            .should().resideInAPackage(BASE + ".infrastructure.adapter..");

    @ArchTest
    public static final ArchRule inputs_should_reside_in_application_usecases_package =
        classes()
            .that().haveSimpleNameEndingWith("Input")
            .should().resideInAPackage(BASE + ".application.usecases..");

    @ArchTest
    public static final ArchRule outputs_should_reside_in_application_usecases_package =
        classes()
            .that().haveSimpleNameEndingWith("Output")
            .should().resideInAPackage(BASE + ".application.usecases..");

    @ArchTest
    public static final ArchRule input_boundaries_should_reside_in_application_usecases_package =
        classes()
            .that().haveSimpleNameEndingWith("InputBoundary")
            .should().resideInAPackage(BASE + ".application.usecases..");

    // --- Structural ---

    @ArchTest
    public static final ArchRule gateways_should_be_interfaces =
        classes()
            .that().haveSimpleNameEndingWith("Gateway")
            .should().beInterfaces();

    @ArchTest
    public static final ArchRule input_boundaries_should_be_interfaces =
        classes()
            .that().haveSimpleNameEndingWith("InputBoundary")
            .should().beInterfaces();

    @ArchTest
    public static final ArchRule data_carriers_should_be_records =
        classes()
            .that().haveSimpleNameEndingWith("Request")
            .or().haveSimpleNameEndingWith("Response")
            .or().haveSimpleNameEndingWith("Input")
            .or().haveSimpleNameEndingWith("Output")
            .should().beRecords();

    // --- Isolation ---

    @ArchTest
    public static final ArchRule domain_should_not_depend_on_spring =
        noClasses()
            .that().resideInAPackage(BASE + ".domain..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("org.springframework..");

    @ArchTest
    public static final ArchRule domain_should_not_depend_on_jpa =
        noClasses()
            .that().resideInAPackage(BASE + ".domain..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("jakarta.persistence..");

    @ArchTest
    public static final ArchRule http_dtos_should_not_depend_on_persistence =
        noClasses()
            .that().haveSimpleNameEndingWith("Request")
            .or().haveSimpleNameEndingWith("Response")
            .should().dependOnClassesThat()
            .resideInAPackage(BASE + ".infrastructure.adapter.persistence..");

    // --- Cycles ---

    @ArchTest
    public static final ArchRule packages_should_be_free_of_cycles =
        slices()
            .matching(BASE + ".(*)..")
            .should().beFreeOfCycles();

    @ArchTest
    public static final ArchRule usecases_should_not_depend_on_each_other =
        slices()
            .matching(BASE + ".application.usecases.(*)..")
            .should().notDependOnEachOther();

}
