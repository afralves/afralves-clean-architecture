package com.afralves.cleanarchitecture.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(
        packages = "com.afralves.cleanarchitecture",
        importOptions = ImportOption.DoNotIncludeTests.class
)
public class NamingConventionTest {

    static final String BASE = "com.afralves.cleanarchitecture";

    @ArchTest
    public static final ArchRule classes_in_controller_package_should_end_with_controller =
        classes()
            .that().resideInAPackage(BASE + ".infrastructure.adapter.controller")
            .should().haveSimpleNameEndingWith("Controller");

    @ArchTest
    public static final ArchRule classes_in_request_package_should_end_with_request =
        classes()
            .that().resideInAPackage(BASE + ".infrastructure.adapter.controller.request")
            .should().haveSimpleNameEndingWith("Request");

    @ArchTest
    public static final ArchRule classes_in_response_package_should_end_with_response =
        classes()
            .that().resideInAPackage(BASE + ".infrastructure.adapter.controller.response")
            .should().haveSimpleNameEndingWith("Response");

    @ArchTest
    public static final ArchRule classes_in_gateway_package_should_end_with_gateway =
        classes()
            .that().resideInAPackage(BASE + ".application.gateway")
            .should().haveSimpleNameEndingWith("Gateway");

    @ArchTest
    public static final ArchRule classes_in_model_package_should_end_with_entity =
        classes()
            .that().resideInAPackage(BASE + ".infrastructure.adapter.persistence.model")
            .should().haveSimpleNameEndingWith("Entity");

    @ArchTest
    public static final ArchRule classes_in_converter_package_should_end_with_converter =
        classes()
            .that().resideInAPackage(BASE + ".infrastructure.adapter.persistence.converter")
            .should().haveSimpleNameEndingWith("Converter");

    @ArchTest
    public static final ArchRule classes_in_repository_package_should_end_with_repository =
        classes()
            .that().resideInAPackage(BASE + ".infrastructure.adapter.persistence.repository")
            .should().haveSimpleNameEndingWith("Repository");

    @ArchTest
    public static final ArchRule classes_in_usecases_should_follow_naming_conventions =
        classes()
            .that().resideInAPackage(BASE + ".application.usecases..")
            .should().haveSimpleNameEndingWith("Interactor")
            .orShould().haveSimpleNameEndingWith("InputBoundary")
            .orShould().haveSimpleNameEndingWith("Input")
            .orShould().haveSimpleNameEndingWith("Output");

}
