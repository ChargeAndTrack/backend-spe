import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import common.Adapter
import common.InBoundPort
import common.OutBoundPort
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import io.kotest.core.spec.style.FunSpec

class ArchitectureTests : FunSpec({
    val domainPackage = "..domain.."
    val applicationPackage = "..application.."
    val infrastructurePackage = "..infrastructure.."

    val importedClasses = ClassFileImporter()
        .withImportOption(ImportOption.DoNotIncludeTests())
        .importPackages(domainPackage, applicationPackage, infrastructurePackage)

    test("layered architecture") {
        // domain should not depend on application/infrastructure
        val domainModelWithNoDeps =
            noClasses().that().resideInAPackage(domainPackage)
                .should().dependOnClassesThat().resideInAPackage(applicationPackage)
                .orShould().dependOnClassesThat().resideInAPackage(infrastructurePackage)

        // layered architecture
        val layeredRule = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Domain").definedBy(domainPackage)
            .layer("Application").definedBy(applicationPackage)
            .layer("Infrastructure").definedBy(infrastructurePackage)
            .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer()
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Infrastructure")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application","Infrastructure")

        domainModelWithNoDeps.check(importedClasses)
        layeredRule.check(importedClasses)
    }

    test ("hexagonal architecture") {
        // ports must be in application or domain
        val portsRule = classes().that()
            .areAnnotatedWith(InBoundPort::class.java)
            .or()
            .areAnnotatedWith(OutBoundPort::class.java)
            .should().resideInAPackage(applicationPackage)
            .orShould().resideInAPackage(domainPackage)

        // adapters must be in infrastructure
        val adaptersRule = classes().that()
            .areAnnotatedWith(Adapter::class.java)
            .should().resideInAPackage(infrastructurePackage)

        portsRule.check(importedClasses)
        adaptersRule.check(importedClasses)
    }
})