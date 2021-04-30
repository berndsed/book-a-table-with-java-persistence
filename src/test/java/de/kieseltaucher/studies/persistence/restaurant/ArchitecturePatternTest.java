package de.kieseltaucher.studies.persistence.restaurant;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(
    packages = "de.kieseltaucher.studies.persistence.restaurant",
    importOptions = {ImportOption.DoNotIncludeTests.class}
)
class ArchitecturePatternTest {

    @ArchTest
    static final Object hexagonal = Architectures.onionArchitecture()
        .domainModels("de.kieseltaucher.studies.persistence.restaurant.model..")
        .domainServices("de.kieseltaucher.studies.persistence.restaurant.service..")
        .adapter("database", "de.kieseltaucher.studies.persistence.restaurant.db..")
        .withOptionalLayers(true);

    private ArchitecturePatternTest() {
    }

}
