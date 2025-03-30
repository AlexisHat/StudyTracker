package com.github.alexishat.backend;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packagesOf = BackendApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchUnitTest {

    @ArchTest
    ArchRule layerTest = layeredArchitecture()
            .consideringAllDependencies()
            .layer("web").definedBy("..web..")
            .layer("model").definedBy("..model..")
            .layer("service").definedBy("..service..")
            .layer("repository").definedBy("..repositories..")
            .whereLayer("web").mayNotBeAccessedByAnyLayer();
}
