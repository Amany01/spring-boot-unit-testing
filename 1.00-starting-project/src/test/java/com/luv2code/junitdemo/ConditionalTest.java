package com.luv2code.junitdemo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

public class ConditionalTest {

    @Test
    @Disabled("don't run this test for now")
    void testBasic () {}

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testForWindowsOnly () {}

    @Test
    @EnabledOnOs(OS.MAC)
    void testForMacOnly () {}

    @Test
    @EnabledOnOs({OS.WINDOWS,OS.MAC})
    void testForMacAndWindows () {}

    @Test
    @EnabledOnJre(JRE.JAVA_17)
    void testForOnlyJava17 () {}

    @Test
    @EnabledOnJre(JRE.JAVA_13)
    void testForOnlyJava13 () {}

    @Test
    @EnabledForJreRange(min = JRE.JAVA_13, max = JRE.JAVA_17)
    void testForOnlyJavaٌRange () {}

    @Test
    @EnabledIfEnvironmentVariable(named = "LUV2CODE_ENV", matches = "DEV")
    void testOnlyForDevEnvironment() {}

    @Test
    @EnabledIfSystemProperty(named = "LUV2CODE_SYS_PROP", matches = "CI_CD_DEPLOY")
    void testOnlyForSystemProperties() {}

}
