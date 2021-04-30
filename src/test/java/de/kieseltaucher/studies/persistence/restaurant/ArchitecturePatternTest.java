package de.kieseltaucher.studies.persistence.restaurant;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(packages = "de.kieseltaucher.studies.persistence.restaurant")
class ArchitecturePatternTest {

    @ArchTest
    static final Object hexagonal = Architectures.onionArchitecture()
        .domainModels("de.kieseltaucher.studies.persistence.restaurant.model..")
        .domainServices("de.kieseltaucher.studies.persistence.restaurant.service..")
        .withOptionalLayers(true);

    private ArchitecturePatternTest() {
    }

}
