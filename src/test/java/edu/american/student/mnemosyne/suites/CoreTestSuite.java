package edu.american.student.mnemosyne.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.american.student.mnemosyne.core.HadoopForemanTest;

@RunWith(Suite.class)
@SuiteClasses
({
	HadoopForemanTest.class
})
public class CoreTestSuite
{

}
