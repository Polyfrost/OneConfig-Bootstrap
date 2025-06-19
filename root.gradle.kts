plugins {
    id("dev.deftu.gradle.multiversion-root")
}

preprocess {
    val forge10809 = createNode("1.8.9-forge", 1_08_09, "srg")
    val forge11202 = createNode("1.12.2-forge", 1_12_02, "srg")
    val forge11605 = createNode("1.16.5-forge", 1_16_05, "srg")
    val fabric11605 = createNode("1.16.5-fabric", 1_16_05, "yarn")
    val fabric12101 = createNode("1.21.1-fabric", 1_21_01, "yarn")
    val fabric12102 = createNode("1.21.2-fabric", 1_21_02, "yarn")
    val fabric12103 = createNode("1.21.3-fabric", 1_21_04, "yarn")
    val fabric12104 = createNode("1.21.4-fabric", 1_21_04, "yarn")
    val fabric12105 = createNode("1.21.5-fabric", 1_21_05, "yarn")

    forge11202.link(forge10809)
    forge11605.link(forge11202)
    fabric11605.link(forge11605)
    fabric12101.link(fabric11605)
    fabric12102.link(fabric12101)
    fabric12103.link(fabric12102)
    fabric12104.link(fabric12103)
    fabric12105.link(fabric12104)

    strictExtraMappings.set(true)
}